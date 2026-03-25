package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

/**
 * Response returned when retrieving a session, including connection URLs.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SessionRetrieveResponse(
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
        Map<String, Object> userMetadata,
        String connectUrl,
        String seleniumRemoteUrl,
        String signingKey
) {}
