package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

/**
 * A log entry from a browser session, including request and response details.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SessionLog(
        String method,
        int pageId,
        String sessionId,
        String frameId,
        String loaderId,
        Long timestamp,
        Request request,
        Response response
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
            Map<String, Object> params,
            String rawBody,
            Long timestamp
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            String rawBody,
            Map<String, Object> result,
            Long timestamp
    ) {}
}
