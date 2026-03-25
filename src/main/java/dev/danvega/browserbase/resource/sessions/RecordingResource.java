package dev.danvega.browserbase.resource.sessions;

import dev.danvega.browserbase.http.BrowserbaseHttpClient;
import dev.danvega.browserbase.model.SessionRecording;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Resource for retrieving session recordings.
 */
public class RecordingResource {

    private static final ParameterizedTypeReference<List<SessionRecording>> RECORDING_LIST_TYPE = new ParameterizedTypeReference<>() {};

    private final BrowserbaseHttpClient http;

    public RecordingResource(BrowserbaseHttpClient http) {
        this.http = http;
    }

    /** Get the recording for a session. */
    public List<SessionRecording> retrieve(String sessionId) {
        return http.get("/v1/sessions/{id}/recording", RECORDING_LIST_TYPE, sessionId);
    }
}
