package usth.m1.proxy;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import usth.m1.model.ULakeFolderInfo;
import usth.m1.model.ULakeFolderResponse;

@RegisterRestClient(configKey = "ulake-folder-api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ULakeFolderProxy {

    @POST
    @Path("/folder")
    Uni<ULakeFolderResponse> createFolder(@HeaderParam("Authorization") String bearerToken,
                                          ULakeFolderInfo request);

    @GET
    @Path("/folder/{id}")
    Uni<ULakeFolderResponse> getFolderById(@HeaderParam("Authorization") String bearerToken,
                                           @PathParam("id") Long folderId);
}
