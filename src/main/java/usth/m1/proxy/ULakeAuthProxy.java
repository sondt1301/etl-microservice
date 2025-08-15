package usth.m1.proxy;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import usth.m1.model.ULakeLoginRequest;
import usth.m1.model.ULakeLoginResponse;

@Path("/auth")
@RegisterRestClient(configKey = "ulake-api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ULakeAuthProxy {

    @POST
    @Path("/login")
    Uni<ULakeLoginResponse> uLakeLogin(ULakeLoginRequest request);

}
