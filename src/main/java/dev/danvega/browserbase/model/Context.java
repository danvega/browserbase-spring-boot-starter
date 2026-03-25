package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a reusable browser context that persists state across sessions.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Context(
        String id,
        String createdAt,
        String projectId,
        String updatedAt
) {}
