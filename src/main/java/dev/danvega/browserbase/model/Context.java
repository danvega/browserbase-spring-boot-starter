package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Context(
        String id,
        String createdAt,
        String projectId,
        String updatedAt
) {}
