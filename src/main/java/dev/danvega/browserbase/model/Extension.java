package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Extension(
        String id,
        String createdAt,
        String fileName,
        String projectId,
        String updatedAt
) {}
