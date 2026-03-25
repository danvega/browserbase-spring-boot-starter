package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

/**
 * Represents a Browserbase browser session.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Session(
        String id,
        String createdAt,
        String expiresAt,
        boolean keepAlive,
        String projectId,
        long proxyBytes,
        String region,
        String startedAt,
        String status,
        String updatedAt,
        String contextId,
        String endedAt,
        Map<String, Object> userMetadata
) {}
