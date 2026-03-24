package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ContextUpdateResponse(
        String id,
        String cipherAlgorithm,
        int initializationVectorSize,
        String publicKey,
        String uploadUrl
) {}
