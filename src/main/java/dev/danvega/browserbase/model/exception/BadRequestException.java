package dev.danvega.browserbase.model.exception;

/**
 * Thrown when the API returns a 400 Bad Request response.
 */
public class BadRequestException extends BrowserbaseException {

    public BadRequestException(String message, String body) {
        super(message, null, 400, body);
    }
}
