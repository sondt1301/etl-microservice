package usth.m1.proxy;

import io.smallrye.mutiny.Uni;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "data-api")
@Path("/catalog/1.0.0")
public interface PingProxy {

    @GET
    @Path("/collections/sentinel-1-grd/queryables")
    @Produces("application/schema+json")
    Uni<JsonObject> ping(@HeaderParam("Authorization") String bearerToken);
}
