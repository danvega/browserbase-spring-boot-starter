package dev.danvega.browserbase.model.exception;

/**
 * Thrown when the API returns a 404 Not Found response.
 */
public class NotFoundException extends BrowserbaseException {

    public NotFoundException(String message, String body) {
        super(message, null, 404, body);
    }
}
