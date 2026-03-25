package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Parameters for performing a web search.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SearchWebParams(
        String query,
        Integer numResults
) {

    public SearchWebParams(String query) {
        this(query, null);
    }
}
