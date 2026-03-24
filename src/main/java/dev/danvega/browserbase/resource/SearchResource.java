package dev.danvega.browserbase.resource;

import dev.danvega.browserbase.http.BrowserbaseHttpClient;
import dev.danvega.browserbase.model.SearchWebParams;
import dev.danvega.browserbase.model.SearchWebResponse;

/**
 * Resource for Browserbase web search.
 */
public class SearchResource {

    private final BrowserbaseHttpClient http;

    public SearchResource(BrowserbaseHttpClient http) {
        this.http = http;
    }

    /** Search the web with full parameters. */
    public SearchWebResponse web(SearchWebParams params) {
        return http.post("/v1/search", params, SearchWebResponse.class);
    }

    /** Search the web with a query string (default result count). */
    public SearchWebResponse web(String query) {
        return web(new SearchWebParams(query));
    }

    /** Search the web with a query string and result count. */
    public SearchWebResponse web(String query, int numResults) {
        return web(new SearchWebParams(query, numResults));
    }
}
