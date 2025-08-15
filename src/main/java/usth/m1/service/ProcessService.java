package usth.m1.service;

import io.quarkus.oidc.client.Tokens;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import usth.m1.model.ProcessRequest;
import usth.m1.proxy.ProcessProxy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ProcessService {

    @Inject
    AuthService authService;

    @Inject
    @RestClient
    ProcessProxy processProxy;

    @Inject
    CatalogMetadataService catalogMetadataService;

    public Uni<Void> downloadTrueColorImages(List<JsonObject> features) {
        return authService.getAccessToken()
                .flatMap(token -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();

                    for (JsonObject feature : features) {
                        chain = chain.chain(() -> {
                            Map<String, Object> input = buidInput(feature);

                            Map<String, Object> output = Map.of(
                                    "width", 512,
                                    "height", 512,
                                    "response", List.of(Map.of(
                                            "identifier", "default",
                                            "format", Map.of(
                                                    "type", "image/png"
                                            )
                                    ))
                            );

                            String evalscript = """
                                    //VERSION=3
                                    function setup() {
                                        return {
                                            input: ["B04", "B03", "B02"],
                                            output: {
                                                bands: 3
                                            }
                                        };
                                    }
                                    
                                    function evaluatePixel(sample) {
                                        return [2.5*sample.B04, 2.5*sample.B03, 2.5*sample.B02]
                                    }
                            """;

                            ProcessRequest processRequest = new ProcessRequest(input, output, evalscript);
                            String id = feature.getString("id");
                            String filename = "images/true-color/" + id + ".png";

                            return processProxy.fetchTrueColor("Bearer " + token, processRequest)
                                    .onItem().transformToUni(imageBytes -> writeFile(filename, imageBytes))
                                    .chain(() -> catalogMetadataService.linkTrueColor(id, filename));
                        });
                    }
                    return chain;
                });
    }

    public Uni<Void> downloadRawImages(List<JsonObject> features) {
        return authService.getAccessToken()
                .flatMap(token -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();

                    for (JsonObject feature : features) {
                        chain = chain.chain(() -> {
                            Map<String, Object> input = buidInput(feature, true);

                            Map<String, Object> output = Map.of(
                                    "width", 512,
                                    "height", 512,
                                    "response", List.of(Map.of(
                                            "identifier", "default",
                                            "format", Map.of(
                                                    "type", "image/tiff"
                                            )
                                    ))
                            );

                            String evalscript = """
                                    //VERSION=3
                                    function setup() {
                                        return {
                                            input: [{
                                                bands: ["B01","B02","B03","B04","B05","B06","B07","B08","B8A","B09","B11","B12"],
                                                units: "DN"
                                            }],
                                            output: {
                                                id: "default",
                                                bands: 12,
                                                sampleType: SampleType.UINT16
                                            }
                                        };
                                    }
                                    
                                    function evaluatePixel(sample) {
                                        return [
                                            sample.B01,
                                            sample.B02,
                                            sample.B03,
                                            sample.B04,
                                            sample.B05,
                                            sample.B06,
                                            sample.B07,
                                            sample.B08,
                                            sample.B8A,
                                            sample.B09,
                                            sample.B11,
                                            sample.B12,
                                        ]
                                    }
                            """;

                            ProcessRequest processRequest = new ProcessRequest(input, output, evalscript);
                            String id = feature.getString("id");
                            String filename = "images/raw/" + id + ".tiff";

                            return processProxy.fetchRaw("Bearer " + token, processRequest)
                                    .onItem().transformToUni(imageBytes -> writeFile(filename, imageBytes))
                                    .chain(() -> catalogMetadataService.linkRaw(id, filename));
                        });
                    }
                    return chain;
                });
    }

    private static Map<String, Object> buidInput(JsonObject feature) {
        return buidInput(feature, false);
    }

    private static Map<String, Object> buidInput(JsonObject feature, boolean raw) {
        Map<String, Object> input = new HashMap<>();
        input.put("bounds", Map.of(
                "bbox", feature.getJsonArray("bbox").getList(),
                "properties", Map.of("crs", "http://www.opengis.net/def/crs/EPSG/0/4326")
        ));

        Map<String, Object> dataFilter = new HashMap<>();
        dataFilter.put("timeRange", Map.of(
                "from", feature.getJsonObject("properties").getString("datetime"),
                "to",  feature.getJsonObject("properties").getString("datetime")
        ));

        input.put("data", List.of(Map.of(
                "type", "sentinel-2-l2a",
                "dataFilter", dataFilter
                )
        ));

        if (raw) {
            dataFilter.put("processing", Map.of("harmonizeValues", "false"));
        }

        return input;
    }

    private static Uni<Void> writeFile(String path, byte[] bytes) {
        try {
            Path p = Path.of(path);
            Files.createDirectories(p.getParent());
            Files.write(p, bytes);
            return Uni.createFrom().voidItem();
        } catch (IOException e) {
            return Uni.createFrom().failure(new RuntimeException("Failed to write image: " + path, e));
        }
    }

}
