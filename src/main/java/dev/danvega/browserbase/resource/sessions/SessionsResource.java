package dev.danvega.browserbase.resource.sessions;

import dev.danvega.browserbase.http.BrowserbaseHttpClient;
import dev.danvega.browserbase.model.*;
import org.springframework.core.ParameterizedTypeReference;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resource for managing Browserbase browser sessions.
 */
public class SessionsResource {

    private static final ParameterizedTypeReference<List<Session>> SESSION_LIST_TYPE = new ParameterizedTypeReference<>() {};

    private final BrowserbaseHttpClient http;
    private final DownloadsResource downloads;
    private final LogsResource logs;
    private final RecordingResource recording;
    private final UploadsResource uploads;

    public SessionsResource(BrowserbaseHttpClient http,
                            DownloadsResource downloads,
                            LogsResource logs,
                            RecordingResource recording,
                            UploadsResource uploads) {
        this.http = http;
        this.downloads = downloads;
        this.logs = logs;
        this.recording = recording;
        this.uploads = uploads;
    }

    /** Create a new browser session. */
    public SessionCreateResponse create(SessionCreateParams params) {
        return http.post("/v1/sessions", params, SessionCreateResponse.class);
    }

    /** Retrieve a session by ID. */
    public SessionRetrieveResponse retrieve(String id) {
        return http.get("/v1/sessions/{id}", SessionRetrieveResponse.class, id);
    }

    /** Update a session (e.g., request release). */
    public Session update(String id, SessionUpdateParams params) {
        return http.post("/v1/sessions/{id}", params, Session.class, id);
    }

    /** List all sessions. */
    public List<Session> list() {
        return http.get("/v1/sessions", SESSION_LIST_TYPE);
    }

    /** List sessions with optional filters. */
    public List<Session> list(SessionListParams params) {
        Map<String, Object> queryParams = new LinkedHashMap<>();
        queryParams.put("status", params.status());
        queryParams.put("q", params.q());
        return http.get("/v1/sessions", queryParams, SESSION_LIST_TYPE);
    }

    /** Get live debug URLs for a running session. */
    public SessionLiveUrls debug(String id) {
        return http.get("/v1/sessions/{id}/debug", SessionLiveUrls.class, id);
    }

    public DownloadsResource downloads() { return downloads; }
    public LogsResource logs() { return logs; }
    public RecordingResource recording() { return recording; }
    public UploadsResource uploads() { return uploads; }
}
