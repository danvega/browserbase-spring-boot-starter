package dev.danvega.browserbase.model.exception;

/**
 * Thrown when the API returns a 403 Forbidden response.
 */
public class PermissionDeniedException extends BrowserbaseException {

    public PermissionDeniedException(String message, String body) {
        super(message, null, 403, body);
    }
}
