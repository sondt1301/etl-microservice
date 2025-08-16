package usth.m1.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import usth.m1.model.ULakeFolderResponse;
import usth.m1.service.ULakeFolderService;

@Path("/ulake/folder")
public class ULakeFolderResource {

    @Inject
    ULakeFolderService folderService;

    @POST
    @Path("/true-color")
    public Uni<ULakeFolderResponse> createTrueColorFolder() {
        return folderService.createTrueColorFolder();
    }

    @POST
    @Path("/raw")
    public Uni<ULakeFolderResponse> createRawFolder() {
        return folderService.createRawFolder();
    }
}
