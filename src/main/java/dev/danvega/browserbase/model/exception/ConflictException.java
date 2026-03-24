package dev.danvega.browserbase.model.exception;

public class ConflictException extends BrowserbaseException {

    public ConflictException(String message, String body) {
        super(message, null, 409, body);
    }
}
