package dev.danvega.browserbase.model.exception;

public class AuthenticationException extends BrowserbaseException {

    public AuthenticationException(String message, String body) {
        super(message, null, 401, body);
    }
}
