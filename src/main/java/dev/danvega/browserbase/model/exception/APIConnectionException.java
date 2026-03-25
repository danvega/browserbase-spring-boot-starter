package dev.danvega.browserbase.model.exception;

/**
 * Thrown when a network-level connection failure occurs. This error is retryable.
 */
public class APIConnectionException extends BrowserbaseException {

    public APIConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
