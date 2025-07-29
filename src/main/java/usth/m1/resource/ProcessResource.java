package usth.m1.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import usth.m1.model.BoundingBox;
import usth.m1.service.CatalogService;
import usth.m1.service.ProcessService;

@Path("/sentinel")
public class ProcessResource {

    @Inject
    CatalogService catalogService;

    @Inject
    ProcessService processService;

    @POST
    @Path("/red-river/download")
    public Uni<Void> processAllTrueColorImages() {
        BoundingBox box = new BoundingBox(105.5, 20.5, 106.2, 21.5);
        return catalogService.searchCatalog(box)
                .onItem().transformToUni(features -> processService.downloadTrueColorImages(features.features()));
    }

    @POST
    @Path("/red-river/download/raw")
    public Uni<Void> processAllRawImages() {
        BoundingBox box = new BoundingBox(105.5, 20.5, 106.2, 21.5);
        return catalogService.searchCatalog(box)
                .onItem().transformToUni(features -> processService.downloadRawImages(features.features()));
    }
}
