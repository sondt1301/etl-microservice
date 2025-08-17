package usth.m1.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import usth.m1.model.ULakeFolderInfo;
import usth.m1.model.ULakeFolderResponse;
import usth.m1.proxy.ULakeFolderProxy;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class ULakeFolderService {

    @Inject
    ULakeAuthService uLakeAuthService;

    @Inject
    @RestClient
    ULakeFolderProxy uLakeFolderProxy;

    public Uni<ULakeFolderResponse> createTrueColorFolder() {
        return uLakeAuthService.getAccessToken()
                .flatMap(token -> {
                    ULakeFolderInfo request = new ULakeFolderInfo(
                            null,
                            null,
                            Long.valueOf(Instant.now().toEpochMilli()),
                            List.of(),
                            "copernicus-true-color",
                            List.of()
                    );

                    return uLakeFolderProxy.createFolder("Bearer " + token, request);
                });
    }

    public Uni<ULakeFolderResponse> createRawFolder() {
        return uLakeAuthService.getAccessToken()
                .flatMap(token -> {
                    ULakeFolderInfo request = new ULakeFolderInfo(
                            null,
                            null,
                            Long.valueOf(Instant.now().toEpochMilli()),
                            List.of(),
                            "copernicus-raw",
                            List.of()
                    );

                    return uLakeFolderProxy.createFolder("Bearer " + token, request);
                });
    }

    public Uni<ULakeFolderResponse> getFolderById(Long folderId) {
        return uLakeAuthService.getAccessToken()
                .flatMap(token -> uLakeFolderProxy.getFolderById("Bearer " + token, folderId));
    }
}
