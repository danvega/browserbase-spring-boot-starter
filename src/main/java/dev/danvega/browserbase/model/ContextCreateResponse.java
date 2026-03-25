package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response returned when a browser context is created, including encryption details.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ContextCreateResponse(
        String id,
        String cipherAlgorithm,
        int initializationVectorSize,
        String publicKey,
        String uploadUrl
) {}
