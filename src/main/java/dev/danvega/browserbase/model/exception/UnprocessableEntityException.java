package dev.danvega.browserbase.model.exception;

/**
 * Thrown when the API returns a 422 Unprocessable Entity response.
 */
public class UnprocessableEntityException extends BrowserbaseException {

    public UnprocessableEntityException(String message, String body) {
        super(message, null, 422, body);
    }
}
