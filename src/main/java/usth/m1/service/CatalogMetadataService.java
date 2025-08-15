package usth.m1.service;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import usth.m1.repository.CatalogMetadataRepository;

import java.util.List;

@ApplicationScoped
public class CatalogMetadataService {

    @Inject
    CatalogMetadataRepository catalogMetadataRepository;

    public Uni<Void> upsertFeature(JsonObject feature) {
        return catalogMetadataRepository.upsertFromFeature(feature).replaceWithVoid();
    }

    public Uni<Void> upsertAll(List<JsonObject> features) {
        Uni<Void> chain =  Uni.createFrom().voidItem();
        for (JsonObject feature : features) {
            chain = chain.chain(() -> upsertFeature(feature));
        }
        return chain;
    }

    public Uni<Void> linkTrueColor(String stacId, String localPath) {
        return catalogMetadataRepository.linkTrueColor(stacId, localPath);
    }

    public Uni<Void> linkRaw(String stacId, String localPath) {
        return catalogMetadataRepository.linkRaw(stacId, localPath);
    }
}
