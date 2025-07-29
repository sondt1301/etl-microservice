package usth.m1.model;

import java.util.Map;

public record ProcessRequest(
        Map<String, Object> input,
        Map<String, Object> output,
        String evalscript
) {}
