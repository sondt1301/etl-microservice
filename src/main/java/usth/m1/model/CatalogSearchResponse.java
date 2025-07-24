package usth.m1.model;

import io.vertx.core.json.JsonObject;

import java.util.List;

public record CatalogSearchResponse(
        List<JsonObject> features,
        List<JsonObject> links,
        JsonObject context
) {}
