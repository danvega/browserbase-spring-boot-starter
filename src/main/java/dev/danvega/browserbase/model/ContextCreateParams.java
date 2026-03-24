package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ContextCreateParams(
        String projectId
) {

    public ContextCreateParams() {
        this(null);
    }
}
