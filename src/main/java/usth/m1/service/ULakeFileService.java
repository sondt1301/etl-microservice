package usth.m1.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import usth.m1.model.ULakeFileInfo;
import usth.m1.model.ULakeFileUploadRequest;
import usth.m1.model.ULakeFolderInfo;
import usth.m1.proxy.ULakeFileProxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class ULakeFileService {

    @Inject
    ULakeAuthService authService;

    @Inject
    @RestClient
    ULakeFileProxy fileProxy;

    public Uni<Void> uploadImages(Path folderPath, long parentId, ImageType type) {
        return authService.getAccessToken()
                .flatMap(token -> {
                    try (Stream<Path> paths = Files.list(folderPath)) {
                        List<Uni<Response>> uploads = paths
                                .filter(Files::isRegularFile)
                                .map(path -> {
                                    try {
                                        File fileToUpload = type.requiresDownsampling ? downsampleWithGDAL(path) : path.toFile();
                                        return uploadGenericImage(token, fileToUpload, path.getFileName().toString(), parentId, type.mimeType);
                                    } catch (IOException | InterruptedException e) {
                                        return failedUpload(e);
                                    }
                                })
                                .collect(Collectors.toList());

                        return Uni.combine().all().unis(uploads).discardItems();
                    } catch (IOException e) {
                        return Uni.createFrom().failure(e);
                    }
                });
    }

    private Uni<Response> failedUpload(Throwable e) {
        return Uni.createFrom().failure(e);
    }

    private Uni<Response> uploadGenericImage(String token, File file, String originalName, long parentId, String mimeType) {
        long fileSize = file.length();
        long creationTime = Instant.now().toEpochMilli();

        ULakeFileInfo info = new ULakeFileInfo(
                null, creationTime, null, mimeType,
                originalName, null,
                new ULakeFolderInfo(parentId, null, null, null, null, null),
                fileSize
        );

        ULakeFileUploadRequest form = new ULakeFileUploadRequest();
        form.file = file;
        form.fileInfo = info;

        return fileProxy.uploadFile("Bearer " + token, form);
    }

    public enum ImageType {
        TRUE_COLOR("image/png", false),
        RAW("image/tiff", true);

        public final String mimeType;
        public final boolean requiresDownsampling;

        ImageType(String mimeType, boolean requiresDownsampling) {
            this.mimeType = mimeType;
            this.requiresDownsampling = requiresDownsampling;
        }
    }

    private File downsampleWithGDAL(Path originalPath) throws IOException, InterruptedException {
        String inputPath = originalPath.toAbsolutePath().toString();
        String outputPath = Files.createTempFile("downsampled-", ".tiff").toAbsolutePath().toString();

        ProcessBuilder pb = new ProcessBuilder(
                "D:\\Study Apps\\release-1928-x64-gdal-3-11-3-mapserver-8-4-0\\bin\\gdal\\apps\\gdal_translate.exe",
                "-outsize", "30%", "30%",
                inputPath,
                outputPath
        );

        Map<String, String> env = pb.environment();
        env.put("PATH", env.get("PATH") + ";D:\\Study Apps\\release-1928-x64-gdal-3-11-3-mapserver-8-4-0\\bin");

        pb.redirectErrorStream(true);
        Process process = pb.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
        }

        int exitCode = process.waitFor();
        System.out.println("[GDAL LOG] Exit code: " + exitCode);
        System.out.println("[GDAL LOG] Output:\n" + output);

        if (exitCode != 0) {
            throw new IOException("GDAL downsampling failed for " + inputPath + "\n\nFull output:\n" + output);
        }

        return new File(outputPath);
    }



    public void testDownsamplingTotalTime(Path folderPath) {
        long totalStart = System.currentTimeMillis();

        try (Stream<Path> paths = Files.list(folderPath)) {
            List<File> outputFiles = paths
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            System.out.println("Processing: " + path.getFileName());
                            File output = downsampleWithGDAL(path);
                            System.out.println("Saved as: " + output.getAbsolutePath());
                            return output;
                        } catch (Exception e) {
                            System.err.println("Failed: " + path.getFileName());
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();

            long totalEnd = System.currentTimeMillis();
            System.out.println("\nFinished " + outputFiles.size() + " downsampling(s)");
            System.out.println("Total time: " + (totalEnd - totalStart) + " ms");

        } catch (IOException e) {
            System.err.println("Error reading folder: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        ULakeFileService service = new ULakeFileService();
        Path folder = Path.of("images/raw");
        service.testDownsamplingTotalTime(folder);
    }

}
