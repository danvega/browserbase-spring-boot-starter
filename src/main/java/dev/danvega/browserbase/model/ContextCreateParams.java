package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Parameters for creating a new browser context.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ContextCreateParams(
        String projectId
) {

    public ContextCreateParams() {
        this(null);
    }
}
