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

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class SentinelService {
    @Inject
    AuthService authService;

    @Inject
    @RestClient
    CatalogProxy catalogProxy;

    public Uni<CatalogSearchResponse> searchCatalog(BoundingBox box) {
        return authService.getAccessToken()
                .flatMap(token -> {
                    Map<String, Object> body = new HashMap<>();
                    body.put("collections", List.of("sentinel-2-l2a"));
                    body.put("bbox", List.of(box.west(), box.south(), box.east(), box.north()));
                    body.put("limit", 25);
                    body.put("datetime", "2023-01-01T00:00:00Z/2023-01-31T23:59:59Z");
                    body.put("fields", Map.of());

                    return fetchPages("Bearer " + token, body, new ArrayList<>(), new ArrayList<>(), new JsonObject(), new AtomicInteger());
                });
    }

    private Uni<CatalogSearchResponse> fetchPages(String bearerToken, Map<String, Object> body, List<JsonObject> allFeatures, List<JsonObject> allLinks, JsonObject allContext, AtomicInteger totalReturned) {
        return catalogProxy.search(bearerToken, body)
                .flatMap(response -> {
                    if (response.features() != null) {
                        allFeatures.addAll(response.features());
                    }

                    if (response.context() != null) {
                        int returned = (int) response.context().getValue("returned");
                        totalReturned.addAndGet(returned);
                        allContext.put("total_returned", totalReturned);
                    }

                    if (response.links() != null) {
                        allLinks.addAll(response.links());

                        Optional<JsonObject> nextLink = response.links().stream()
                                .filter(link -> "next".equals(link.getString("rel", null)))
                                .findFirst();

                        if (nextLink.isPresent()) {
                            JsonObject nextBody = nextLink.get().getJsonObject("body");
                            if (nextBody != null && nextBody.containsKey("next")) {
                                int nextValue = nextBody.getInteger("next");
                                body.put("next", nextValue);
                                return fetchPages(bearerToken, body, allFeatures, allLinks, allContext,  totalReturned);
                            }
                        }
                    }
                    return Uni.createFrom().item(new CatalogSearchResponse(allFeatures, allLinks, allContext));
                });
    }
}
