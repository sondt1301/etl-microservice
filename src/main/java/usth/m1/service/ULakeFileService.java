package usth.m1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import usth.m1.model.ULakeFileInfo;
import usth.m1.model.ULakeFileUploadRequest;
import usth.m1.model.ULakeFolderInfo;
import usth.m1.proxy.ULakeFileProxy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class ULakeFileService {

    @Inject
    ULakeAuthService authService;

    @Inject
    @RestClient
    ULakeFileProxy fileProxy;

    @Inject
    ObjectMapper objectMapper;

    public Uni<Void> uploadTrueColorFile(Path folderPath, long parentId) {
        return authService.getAccessToken()
                .flatMap(token -> {
                    try (Stream<Path> paths = Files.list(folderPath)) {
                        List<Uni<Response>> uploads = paths
                                .filter(Files::isRegularFile)
                                .map(path -> uploadSingleImage(token, path, parentId))
                                .collect(Collectors.toList());

                        return Uni.combine().all().unis(uploads).discardItems();
                    } catch (IOException e) {
                        return Uni.createFrom().failure(e);
                    }
                });
    }

    private Uni<Response> uploadSingleImage(String token, Path path, long parentId) {
        try {
            long fileSize = Files.size(path);
            String fileName = path.getFileName().toString();
            long creationTime = Instant.now().toEpochMilli();

            ULakeFileInfo info = new ULakeFileInfo(
                    null,
                    creationTime,
                    null,
                    "image/png",
                    fileName,
                    null,
                    new ULakeFolderInfo(parentId, null, null, null, null, null),
                    fileSize
            );


            ULakeFileUploadRequest form = new ULakeFileUploadRequest();
            form.file = path.toFile();
            form.fileInfo = info;

            return fileProxy.uploadFile("Bearer " + token, form);

        } catch (IOException e) {
            return Uni.createFrom().failure(e);
        }
    }

}
