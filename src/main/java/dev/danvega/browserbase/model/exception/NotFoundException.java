package dev.danvega.browserbase.model.exception;

public class NotFoundException extends BrowserbaseException {

    public NotFoundException(String message, String body) {
        super(message, null, 404, body);
    }
}
