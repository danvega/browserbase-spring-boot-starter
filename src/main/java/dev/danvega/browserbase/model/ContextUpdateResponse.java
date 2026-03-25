package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response returned when a browser context is updated.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ContextUpdateResponse(
        String id,
        String cipherAlgorithm,
        int initializationVectorSize,
        String publicKey,
        String uploadUrl
) {}
