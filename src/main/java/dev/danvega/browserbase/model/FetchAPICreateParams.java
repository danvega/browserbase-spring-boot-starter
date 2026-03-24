package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FetchAPICreateParams(
        String url,
        Boolean allowInsecureSsl,
        Boolean allowRedirects,
        Boolean proxies
) {

    public FetchAPICreateParams(String url) {
        this(url, null, null, null);
    }
}
