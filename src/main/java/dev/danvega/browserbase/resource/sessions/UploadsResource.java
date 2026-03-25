package dev.danvega.browserbase.resource.sessions;

import dev.danvega.browserbase.http.BrowserbaseHttpClient;
import dev.danvega.browserbase.model.UploadCreateResponse;

import java.nio.file.Path;

/**
 * Resource for uploading files to a browser session.
 */
public class UploadsResource {

    private final BrowserbaseHttpClient http;

    public UploadsResource(BrowserbaseHttpClient http) {
        this.http = http;
    }

    /** Upload a file to a session. */
    public UploadCreateResponse create(String sessionId, Path file) {
        return http.postMultipart("/v1/sessions/{id}/uploads", file, UploadCreateResponse.class, sessionId);
    }
}
