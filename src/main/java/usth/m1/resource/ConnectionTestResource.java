package usth.m1.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import usth.m1.service.ConnectionTestService;

@Path("/test")
public class ConnectionTestResource {

    @Inject
    ConnectionTestService connectionTestService;

    @GET
    public Uni<JsonObject> test() {

        return connectionTestService.testConnection();
    }
}
