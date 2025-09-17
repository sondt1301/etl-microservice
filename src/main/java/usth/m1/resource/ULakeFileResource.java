package usth.m1.resource;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import usth.m1.model.ULakeFolderResponse;
import usth.m1.service.ULakeFileService;
import usth.m1.service.ULakeFolderService;

@Path("/ulake/file")
public class ULakeFileResource {

    @Inject
    ULakeFolderService ulakeFolderService;

    @Inject
    ULakeFileService ulakeFileService;

    @POST
    @Path("/true-color")
    @Blocking
    public Uni<Response> uploadTrueColorImages() {
        long startTime = System.currentTimeMillis();
        java.nio.file.Path folder = java.nio.file.Path.of("images/true-color");

        return ulakeFolderService.createTrueColorFolder()
                .flatMap(resp -> {
                    Long folderId = resp.resp().id();
                    return ulakeFileService.uploadImages(folder, folderId, ULakeFileService.ImageType.TRUE_COLOR)
                            .flatMap(_void -> ulakeFolderService.getFolderById(folderId))
                            .map(updatedFolder -> Response.ok(new ULakeFolderResponse(200, "Upload completed", updatedFolder.resp())).build())
                            .invoke(() -> {
                                long duration = System.currentTimeMillis() - startTime;
                                System.out.println("True-color image upload completed in " + duration + " ms");
                            });
                });
    }

    @POST
    @Path("/raw")
    @Blocking
    public Uni<Response> uploadRawImages() {
        long startTime = System.currentTimeMillis();
        java.nio.file.Path folder = java.nio.file.Path.of("images/raw");

        return ulakeFolderService.createRawFolder()
                .flatMap(resp -> {
                    Long folderId = resp.resp().id();
                    return ulakeFileService.uploadImages(folder, folderId, ULakeFileService.ImageType.RAW)
                            .flatMap(_void -> ulakeFolderService.getFolderById(folderId))
                            .map(updatedFolder -> Response.ok(new ULakeFolderResponse(200, "Upload completed", updatedFolder.resp())).build())
                            .invoke(() -> {
                                long duration = System.currentTimeMillis() - startTime;
                                System.out.println("Raw image upload completed in " + duration + " ms");
                            });
                });
    }
}
