package dev.danvega.browserbase.model;

/**
 * Optional filter parameters for listing sessions.
 */
public record SessionListParams(
        String status,
        String q
) {

    public static SessionListParams ofStatus(String status) {
        return new SessionListParams(status, null);
    }

    public static SessionListParams ofQuery(String q) {
        return new SessionListParams(null, q);
    }
}
