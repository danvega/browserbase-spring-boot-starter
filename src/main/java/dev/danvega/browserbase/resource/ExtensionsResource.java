package dev.danvega.browserbase.resource;

import dev.danvega.browserbase.http.BrowserbaseHttpClient;
import dev.danvega.browserbase.model.Extension;

import java.nio.file.Path;

/**
 * Resource for managing Chrome extensions uploaded to Browserbase.
 */
public class ExtensionsResource {

    private final BrowserbaseHttpClient http;

    public ExtensionsResource(BrowserbaseHttpClient http) {
        this.http = http;
    }

    /** Upload a Chrome extension (ZIP file). */
    public Extension create(Path file) {
        return http.postMultipart("/v1/extensions", file, Extension.class);
    }

    /** Retrieve an extension by ID. */
    public Extension retrieve(String id) {
        return http.get("/v1/extensions/{id}", Extension.class, id);
    }

    /** Delete an extension by ID. */
    public void delete(String id) {
        http.delete("/v1/extensions/{id}", id);
    }
}
