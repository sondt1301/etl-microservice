package usth.m1.proxy;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import usth.m1.model.OAuthTokenResponse;

@Path("/auth/realms/CDSE/protocol/openid-connect/token")
@RegisterRestClient(configKey = "auth-api")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public interface AuthProxy {

    @POST
    Uni<OAuthTokenResponse> getToken(@FormParam("grant_type") String grandType,
                                     @FormParam("client_id") String clientId,
                                     @FormParam("client_secret") String clientSecret);
}
