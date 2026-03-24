package dev.danvega.browserbase;

import dev.danvega.browserbase.model.exception.*;
import dev.danvega.browserbase.resource.SearchResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RestClientTest
@Import(BrowserbaseAutoConfiguration.class)
@TestPropertySource(properties = "browserbase.api-key=test-api-key")
class ErrorHandlingTest {

    @Autowired
    private SearchResource search;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void badRequestThrows400() {
        server.expect(requestTo("https://api.browserbase.com/v1/search"))
                .andRespond(withBadRequest().body("invalid query"));

        assertThatThrownBy(() -> search.web("test"))
                .isInstanceOf(BadRequestException.class)
                .satisfies(ex -> {
                    var bbe = (BrowserbaseException) ex;
                    assertThat(bbe.getStatusCode()).isEqualTo(400);
                    assertThat(bbe.getBody()).isEqualTo("invalid query");
                });
    }

    @Test
    void unauthorizedThrows401() {
        server.expect(requestTo("https://api.browserbase.com/v1/search"))
                .andRespond(withUnauthorizedRequest().body("bad key"));

        assertThatThrownBy(() -> search.web("test"))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    void forbiddenThrows403() {
        server.expect(requestTo("https://api.browserbase.com/v1/search"))
                .andRespond(withForbiddenRequest().body("not allowed"));

        assertThatThrownBy(() -> search.web("test"))
                .isInstanceOf(PermissionDeniedException.class);
    }

    @Test
    void notFoundThrows404() {
        server.expect(requestTo("https://api.browserbase.com/v1/search"))
                .andRespond(withResourceNotFound().body("not found"));

        assertThatThrownBy(() -> search.web("test"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void conflictThrows409() {
        // 409 is retryable — expect 3 total attempts (1 initial + 2 retries)
        for (int i = 0; i < 3; i++) {
            server.expect(requestTo("https://api.browserbase.com/v1/search"))
                    .andRespond(withRequestConflict().body("conflict"));
        }

        assertThatThrownBy(() -> search.web("test"))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void unprocessableEntityThrows422() {
        server.expect(requestTo("https://api.browserbase.com/v1/search"))
                .andRespond(withRawStatus(422).body("invalid"));

        assertThatThrownBy(() -> search.web("test"))
                .isInstanceOf(UnprocessableEntityException.class);
    }

    @Test
    void rateLimitThrows429() {
        // With maxRetries=2, expect 3 total attempts (1 initial + 2 retries)
        for (int i = 0; i < 3; i++) {
            server.expect(requestTo("https://api.browserbase.com/v1/search"))
                    .andRespond(withTooManyRequests().body("rate limited"));
        }

        assertThatThrownBy(() -> search.web("test"))
                .isInstanceOf(RateLimitException.class);
    }

    @Test
    void serverErrorThrows500() {
        for (int i = 0; i < 3; i++) {
            server.expect(requestTo("https://api.browserbase.com/v1/search"))
                    .andRespond(withServerError().body("internal error"));
        }

        assertThatThrownBy(() -> search.web("test"))
                .isInstanceOf(InternalServerException.class)
                .satisfies(ex -> {
                    var ise = (InternalServerException) ex;
                    assertThat(ise.getStatusCode()).isEqualTo(500);
                });
    }
}
