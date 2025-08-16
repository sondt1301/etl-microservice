package usth.m1.proxy;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import usth.m1.model.ULakeObjectGroupRequest;
import usth.m1.model.ULakeObjectGroupResponse;

@RegisterRestClient(configKey = "ulake-core-api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ULakeObjectGroupProxy {

    @POST
    @Path("/group")
    Uni<ULakeObjectGroupResponse> createObjectGroup(@HeaderParam("Authorization") String bearerToken,
                                                    ULakeObjectGroupRequest request);
}
