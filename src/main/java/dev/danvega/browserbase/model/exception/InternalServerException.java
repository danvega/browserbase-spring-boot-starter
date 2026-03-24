package dev.danvega.browserbase.model.exception;

public class InternalServerException extends BrowserbaseException {

    public InternalServerException(String message, int statusCode, String body) {
        super(message, null, statusCode, body);
    }
}
