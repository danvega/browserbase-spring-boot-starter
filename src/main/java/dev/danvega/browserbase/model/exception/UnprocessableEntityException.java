package dev.danvega.browserbase.model.exception;

public class UnprocessableEntityException extends BrowserbaseException {

    public UnprocessableEntityException(String message, String body) {
        super(message, null, 422, body);
    }
}
