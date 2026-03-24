package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SessionUpdateParams(
        String status,
        String projectId
) {

    public static SessionUpdateParams requestRelease() {
        return new SessionUpdateParams("REQUEST_RELEASE", null);
    }

    public static SessionUpdateParams requestRelease(String projectId) {
        return new SessionUpdateParams("REQUEST_RELEASE", projectId);
    }
}
