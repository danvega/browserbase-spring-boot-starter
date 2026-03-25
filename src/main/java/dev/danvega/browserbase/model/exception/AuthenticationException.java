package dev.danvega.browserbase.model.exception;

/**
 * Thrown when the API returns a 401 Unauthorized response.
 */
public class AuthenticationException extends BrowserbaseException {

    public AuthenticationException(String message, String body) {
        super(message, null, 401, body);
    }
}
