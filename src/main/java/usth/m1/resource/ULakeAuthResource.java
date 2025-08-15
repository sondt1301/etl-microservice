package usth.m1.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import usth.m1.service.ULakeAuthService;

@Path("/ulake/auth")
public class ULakeAuthResource {

    @Inject
    ULakeAuthService uLakeAuthService;

    @GET
    @Path("/token")
    public String getULakeToken() {
        return uLakeAuthService.getAccessToken().await().indefinitely();
    }
}
