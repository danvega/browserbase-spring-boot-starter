package dev.danvega.browserbase.model.exception;

public class RateLimitException extends BrowserbaseException {

    public RateLimitException(String message, String body) {
        super(message, null, 429, body);
    }
}
