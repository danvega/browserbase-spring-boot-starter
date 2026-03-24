package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SearchWebParams(
        String query,
        Integer numResults
) {

    public SearchWebParams(String query) {
        this(query, null);
    }
}
