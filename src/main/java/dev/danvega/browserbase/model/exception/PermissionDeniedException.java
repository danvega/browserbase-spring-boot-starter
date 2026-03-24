package dev.danvega.browserbase.model.exception;

public class PermissionDeniedException extends BrowserbaseException {

    public PermissionDeniedException(String message, String body) {
        super(message, null, 403, body);
    }
}
