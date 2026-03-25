package dev.danvega.browserbase.model.exception;

/**
 * Thrown when the API returns a 5xx server error response. This error is retryable.
 */
public class InternalServerException extends BrowserbaseException {

    public InternalServerException(String message, int statusCode, String body) {
        super(message, null, statusCode, body);
    }
}
