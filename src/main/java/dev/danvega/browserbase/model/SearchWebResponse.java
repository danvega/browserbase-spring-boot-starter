package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Response from a web search, containing a list of results.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SearchWebResponse(
        String query,
        String requestId,
        List<Result> results
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(
            String id,
            String title,
            String url,
            String author,
            String favicon,
            String image,
            String publishedDate
    ) {}
}
