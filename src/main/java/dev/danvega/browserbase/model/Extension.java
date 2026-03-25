package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a Chrome extension uploaded to Browserbase.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Extension(
        String id,
        String createdAt,
        String fileName,
        String projectId,
        String updatedAt
) {}
