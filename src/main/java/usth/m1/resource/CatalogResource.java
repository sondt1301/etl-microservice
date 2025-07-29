package usth.m1.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import usth.m1.model.BoundingBox;
import usth.m1.model.CatalogSearchResponse;
import usth.m1.service.CatalogService;

@Path("/sentinel")
@Produces(MediaType.TEXT_PLAIN)
public class CatalogResource {

    @Inject
    CatalogService catalogService;

    @POST
    @Path("/red-river")
    public Uni<CatalogSearchResponse> redRiverSearch() {
        BoundingBox box = new BoundingBox(105.5, 20.5, 106.2, 21.5);
        return catalogService.searchCatalog(box);
    }

}

