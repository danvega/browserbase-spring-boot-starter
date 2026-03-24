package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SessionRecording(
        Map<String, Object> data,
        String sessionId,
        long timestamp,
        int type
) {}
