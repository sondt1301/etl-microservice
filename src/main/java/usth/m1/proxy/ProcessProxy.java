package usth.m1.proxy;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import usth.m1.model.ProcessRequest;

@RegisterRestClient(configKey = "data-api")
@Path("/process")
public interface ProcessProxy {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("image/png")
    Uni<byte[]> fetchTrueColor(@HeaderParam("Authorization") String bearerToken, ProcessRequest request);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("image/tiff")
    Uni<byte[]> fetchRaw(@HeaderParam("Authorization") String bearerToken, ProcessRequest request);
}
