package usth.m1.service;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import usth.m1.model.BoundingBox;
import usth.m1.model.CatalogSearchRequest;
import usth.m1.model.CatalogSearchResponse;
import usth.m1.proxy.CatalogProxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ApplicationScoped
public class SentinelService {
    @Inject
    AuthService authService;

    @Inject
    @RestClient
    CatalogProxy catalogProxy;

    public Uni<JsonObject> searchCatalog(BoundingBox box) {
        return authService.getAccessToken()
                .flatMap(token -> {
                    Map<String, Object> body = Map.of(
                            "collections", List.of("sentinel-2-l2a"),
                            "bbox", List.of(box.west(), box.south(), box.east(), box.north()),
                            "limit", 100,
                            "datetime", "2023-01-01T00:00:00Z/2023-01-31T23:59:59Z",
                            "fields", Map.of()
                    );

                    return catalogProxy.search("Bearer " + token, body);
                });
    }
}
