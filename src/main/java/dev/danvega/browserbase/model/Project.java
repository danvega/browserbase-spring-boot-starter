package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Project(
        String id,
        int concurrency,
        String createdAt,
        int defaultTimeout,
        String name,
        String ownerId,
        String updatedAt
) {}
