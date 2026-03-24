package dev.danvega.browserbase.model.exception;

public class BadRequestException extends BrowserbaseException {

    public BadRequestException(String message, String body) {
        super(message, null, 400, body);
    }
}
