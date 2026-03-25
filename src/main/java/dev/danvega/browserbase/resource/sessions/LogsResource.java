package dev.danvega.browserbase.resource.sessions;

import dev.danvega.browserbase.http.BrowserbaseHttpClient;
import dev.danvega.browserbase.model.SessionLog;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Resource for retrieving session logs.
 */
public class LogsResource {

    private static final ParameterizedTypeReference<List<SessionLog>> LOG_LIST_TYPE = new ParameterizedTypeReference<>() {};

    private final BrowserbaseHttpClient http;

    public LogsResource(BrowserbaseHttpClient http) {
        this.http = http;
    }

    /** Get logs for a session. */
    public List<SessionLog> list(String sessionId) {
        return http.get("/v1/sessions/{id}/logs", LOG_LIST_TYPE, sessionId);
    }
}
