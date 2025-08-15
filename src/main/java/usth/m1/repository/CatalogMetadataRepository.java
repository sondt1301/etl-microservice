package usth.m1.repository;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import usth.m1.entity.CatalogMetadata;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class CatalogMetadataRepository implements PanacheRepositoryBase<CatalogMetadata, Long> {

    public Uni<CatalogMetadata> findByImageId(String imageId) {
        return find("imageId", imageId).firstResult();
    }

    public Uni<CatalogMetadata> upsertFromFeature(JsonObject feature) {
        final String stacId = feature.getString("id");
        final JsonObject properties = feature.getJsonObject("properties");

        return Panache.withTransaction(() ->
                findByImageId(stacId)
                        .onItem().transform(existing -> {
                            CatalogMetadata entity = (existing != null) ? existing : new CatalogMetadata();
                            entity.setImageId(stacId);
                            entity.setCollection(feature.getString("collection"));
                            entity.setGeometry(toMap(feature.getJsonObject("geometry")));
                            entity.setProperties(toMap(properties));
                            entity.setAssets(toMap(feature.getJsonObject("assets")));
                            entity.setLinks(feature.getJsonArray("links").encode());
                            entity.setBbox(feature.getJsonArray("bbox").encode());

                            if (properties != null && properties.getString("datetime") != null) {
                                entity.setDatetimeUtc(OffsetDateTime.parse(properties.getString("datetime")));
                            }

                            return entity;
                        })
                        .onItem().transformToUni(entity -> (entity == null || entity.id == null) ? persist(entity) : Uni.createFrom().item(entity))
        );
    }

    public Uni<Void> linkTrueColor(String stacId, String localPath) {
        return linkLocal(stacId, localPath, "true_color_local", "image/png", "Local true-color PNG")
                .replaceWithVoid();
    }

    public Uni<Void> linkRaw(String stacId, String localPath) {
        return linkLocal(stacId, localPath, "raw_local", "image/tiff", "Local raw multi-band TIFF")
                .replaceWithVoid();
    }

    private Uni<CatalogMetadata> linkLocal(String stacId, String path, String assetKey, String mime, String title) {
        return Panache.withTransaction(() ->
                findByImageId(stacId)
                        .onItem().ifNull().failWith(new IllegalStateException("Image not found: " +  stacId))
                        .onItem().transform(entity -> {
                            Map<String, Object> assets = entity.getAssets();
                            if (assets == null) assets = new java.util.HashMap<>();
                            assets.put(assetKey, Map.of(
                                    "href", "file:" + path,
                                    "type", mime,
                                    "title", title
                            ));
                            entity.setAssets(assets);

                            if ("true_color_local".equals(assetKey)) entity.setTrueColorImgPath(path);
                            if ("raw_local".equals(assetKey)) entity.setRawImgPath(path);
                            return entity;
                        })
        );
    }

    private static Map<String, Object> toMap(JsonObject jsonObject) {
        return (jsonObject == null) ? null : jsonObject.getMap();
    }

}
