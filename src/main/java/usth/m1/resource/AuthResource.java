package usth.m1.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import usth.m1.service.AuthService;

@Path("/auth")
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/token")
    public String getToken() {
        return authService.getAccessToken().await().indefinitely();
    }
}
