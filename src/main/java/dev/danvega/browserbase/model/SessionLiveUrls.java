package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SessionLiveUrls(
        String debuggerFullscreenUrl,
        String debuggerUrl,
        String wsUrl,
        List<Page> pages
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Page(
            String id,
            String debuggerFullscreenUrl,
            String debuggerUrl,
            String faviconUrl,
            String title,
            String url
    ) {}
}
