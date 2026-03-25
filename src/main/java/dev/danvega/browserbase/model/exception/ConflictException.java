package dev.danvega.browserbase.model.exception;

/**
 * Thrown when the API returns a 409 Conflict response. This error is retryable.
 */
public class ConflictException extends BrowserbaseException {

    public ConflictException(String message, String body) {
        super(message, null, 409, body);
    }
}
