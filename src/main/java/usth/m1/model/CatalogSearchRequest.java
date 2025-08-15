package usth.m1.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record CatalogSearchRequest(
        List<Double> bbox,
        String datetime,
        List<String> collections,
        Integer limit,
//        Map<String, Object> fields,
        Integer next,
        Map<String, Object> filter,

        @JsonProperty("filter-lang")
        String filterLang
) {}
