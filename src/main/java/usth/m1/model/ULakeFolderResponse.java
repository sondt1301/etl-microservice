package usth.m1.model;

import io.vertx.core.json.JsonObject;

public record ULakeFolderResponse(
        int code,
        String msg,
        JsonObject resp
) {}
