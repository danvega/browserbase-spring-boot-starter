package dev.danvega.browserbase.model.exception;

/**
 * Base exception for all Browserbase API errors.
 */
public class BrowserbaseException extends RuntimeException {

    private final Integer statusCode;
    private final String body;

    public BrowserbaseException(String message) {
        this(message, null, null, null);
    }

    public BrowserbaseException(String message, Throwable cause) {
        this(message, cause, null, null);
    }

    public BrowserbaseException(String message, Throwable cause, Integer statusCode, String body) {
        super(message, cause);
        this.statusCode = statusCode;
        this.body = body;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }
}
