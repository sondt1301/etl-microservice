package usth.m1.proxy;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import usth.m1.model.ULakeFileUploadRequest;

@RegisterRestClient(configKey = "ulake-dashboard-api")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public interface ULakeFileProxy {

    @POST
    @Path("/file")
    Uni<Response> uploadFile(@HeaderParam("Authorization") String bearerToken,
                             ULakeFileUploadRequest request);
}
