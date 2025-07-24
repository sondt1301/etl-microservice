package usth.m1.proxy;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import usth.m1.model.CatalogSearchRequest;
import usth.m1.model.CatalogSearchResponse;

import java.util.Map;

@RegisterRestClient(configKey = "data-api")
@Path("/catalog/1.0.0")
public interface CatalogProxy {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/geo+json")
    @Path("/search")
    Uni<CatalogSearchResponse> search(@HeaderParam("Authorization") String bearerToken, Map<String, Object> body);
}
