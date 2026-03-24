package dev.danvega.browserbase.resource;

import dev.danvega.browserbase.http.BrowserbaseHttpClient;
import dev.danvega.browserbase.model.FetchAPICreateParams;
import dev.danvega.browserbase.model.FetchAPICreateResponse;

/**
 * Resource for fetching web page content without a full browser session.
 */
public class FetchAPIResource {

    private final BrowserbaseHttpClient http;

    public FetchAPIResource(BrowserbaseHttpClient http) {
        this.http = http;
    }

    /** Fetch a page with full parameters. */
    public FetchAPICreateResponse create(FetchAPICreateParams params) {
        return http.post("/v1/fetch", params, FetchAPICreateResponse.class);
    }

    /** Fetch a page by URL with default settings. */
    public FetchAPICreateResponse create(String url) {
        return create(new FetchAPICreateParams(url));
    }
}
