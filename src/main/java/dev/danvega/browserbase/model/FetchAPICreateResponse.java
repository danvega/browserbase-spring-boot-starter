package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FetchAPICreateResponse(
        String id,
        String content,
        String contentType,
        String encoding,
        Map<String, String> headers,
        int statusCode
) {}
