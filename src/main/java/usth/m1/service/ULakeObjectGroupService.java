package usth.m1.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import usth.m1.model.ULakeObjectGroup;
import usth.m1.model.ULakeObjectGroupRequest;
import usth.m1.model.ULakeObjectGroupResponse;
import usth.m1.proxy.ULakeObjectGroupProxy;

import java.util.List;

@ApplicationScoped
public class ULakeObjectGroupService {

    @Inject
    ULakeAuthService uLakeAuthService;

    @Inject
    @RestClient
    ULakeObjectGroupProxy uLakeObjectGroupProxy;

    public Uni<ULakeObjectGroup> createImagesGroup() {
        return uLakeAuthService.getAccessToken()
                .flatMap(token -> {
                    ULakeObjectGroupRequest request = new ULakeObjectGroupRequest(
                            "images",
                            null,
                            "",
                            "Parent group for copernicus satellite images",
                            "images",
                            List.of()
                    );
                    return uLakeObjectGroupProxy.createObjectGroup("Bearer " + token, request);
                })
                .map(ULakeObjectGroupResponse::resp);
    }

    public Uni<ULakeObjectGroup> createChildGroup(String groupName, String description, String tags, String parentGid) {
        return uLakeAuthService.getAccessToken()
                .flatMap(token -> {
                    ULakeObjectGroupRequest request = new ULakeObjectGroupRequest(
                            groupName,
                            null,
                            parentGid,
                            description,
                            tags,
                            List.of()
                    );
                    return uLakeObjectGroupProxy.createObjectGroup("Bearer " + token, request);
                })
                .map(ULakeObjectGroupResponse::resp);
    }

    public Uni<List<ULakeObjectGroup>> createImagesAndChildGroups() {
        return createImagesGroup()
                .flatMap(imagesGroup ->
                        Uni.combine().all().unis(
                                createChildGroup("true-color", "True color satellite images from Copernicus", "true-color,sentinel", imagesGroup.gid()),
                                createChildGroup("raw", "Raw spectral band images from Copernicus", "raw,sentinel", imagesGroup.gid())
                        ).asTuple()
                        .map(tuple -> List.of(imagesGroup, tuple.getItem1(), tuple.getItem2()))
                );
    }

}
