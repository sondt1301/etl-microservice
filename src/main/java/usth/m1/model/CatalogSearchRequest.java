package usth.m1.model;

import java.util.List;
import java.util.Map;

public record CatalogSearchRequest(
        List<Double> bbox,
        String datetime,
        List<String> collections,
        Integer limit,
        Map<String, Object> fields,
        Integer next
) {}
