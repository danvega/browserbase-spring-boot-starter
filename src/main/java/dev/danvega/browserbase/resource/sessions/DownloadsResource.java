package dev.danvega.browserbase.resource.sessions;

import dev.danvega.browserbase.http.BrowserbaseHttpClient;

/**
 * Resource for downloading files from a browser session.
 */
public class DownloadsResource {

    private final BrowserbaseHttpClient http;

    public DownloadsResource(BrowserbaseHttpClient http) {
        this.http = http;
    }

    /** Download session files as a ZIP archive. */
    public byte[] list(String sessionId) {
        return http.getBytes("/v1/sessions/{id}/downloads", sessionId);
    }
}
