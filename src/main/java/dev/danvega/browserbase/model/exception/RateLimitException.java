package dev.danvega.browserbase.model.exception;

/**
 * Thrown when the API returns a 429 Too Many Requests response. This error is retryable.
 */
public class RateLimitException extends BrowserbaseException {

    public RateLimitException(String message, String body) {
        super(message, null, 429, body);
    }
}
