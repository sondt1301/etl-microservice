package usth.m1.model;

import java.util.List;
import java.util.Map;

public record CatalogSearchRequest(
        Map<String, Object> bbox,
        Map<String, Object> datetime,
        List<String> collections,
        int limit,
        List<String> fields
) {}
