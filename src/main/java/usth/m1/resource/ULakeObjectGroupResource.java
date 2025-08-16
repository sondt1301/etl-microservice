package usth.m1.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import usth.m1.model.ULakeObjectGroup;
import usth.m1.service.ULakeObjectGroupService;

import java.util.List;

@Path("/ulake/create-group")
public class ULakeObjectGroupResource {

    @Inject
    ULakeObjectGroupService ulakeObjectGroupService;

    @POST
    @Path("/all")
    public Uni<List<ULakeObjectGroup>> createAllGroups() {
        return ulakeObjectGroupService.createImagesAndChildGroups();
    }

}
