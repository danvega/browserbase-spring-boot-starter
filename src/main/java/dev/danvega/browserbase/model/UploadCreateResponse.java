package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response returned after uploading a file to a session.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UploadCreateResponse(
        String message
) {}
