package usth.m1.resource;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import usth.m1.model.BoundingBox;
import usth.m1.model.CatalogSearchResponse;
import usth.m1.service.SentinelService;

@Path("/sentinel")
@Produces(MediaType.TEXT_PLAIN)
public class SentinelResource {

    @Inject
    SentinelService sentinelService;

    @GET
    @Path("/red-river")
    public Uni<JsonObject> redRiverSearch() {
        BoundingBox box = new BoundingBox(105.5, 20.5, 106.2, 21.5);
        return sentinelService.searchCatalog(box);
    }

}

