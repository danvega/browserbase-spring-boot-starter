package dev.danvega.browserbase.resource;

import dev.danvega.browserbase.http.BrowserbaseHttpClient;
import dev.danvega.browserbase.model.Context;
import dev.danvega.browserbase.model.ContextCreateParams;
import dev.danvega.browserbase.model.ContextCreateResponse;
import dev.danvega.browserbase.model.ContextUpdateResponse;

/**
 * Resource for managing reusable browser contexts.
 * Contexts allow session state (cookies, storage) to persist across sessions.
 */
public class ContextsResource {

    private final BrowserbaseHttpClient http;

    public ContextsResource(BrowserbaseHttpClient http) {
        this.http = http;
    }

    /** Create a new browser context. */
    public ContextCreateResponse create(ContextCreateParams params) {
        return http.post("/v1/contexts", params, ContextCreateResponse.class);
    }

    /** Create a new browser context with default settings. */
    public ContextCreateResponse create() {
        return create(new ContextCreateParams());
    }

    /** Retrieve a context by ID. */
    public Context retrieve(String id) {
        return http.get("/v1/contexts/{id}", Context.class, id);
    }

    /** Update a context. */
    public ContextUpdateResponse update(String id) {
        return http.put("/v1/contexts/{id}", null, ContextUpdateResponse.class, id);
    }

    /** Delete a context by ID. */
    public void delete(String id) {
        http.delete("/v1/contexts/{id}", id);
    }
}
