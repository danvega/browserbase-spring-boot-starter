package dev.danvega.browserbase;

import dev.danvega.browserbase.resource.ContextsResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RestClientTest
@Import(BrowserbaseAutoConfiguration.class)
@TestPropertySource(properties = "browserbase.api-key=test-api-key")
class ContextsResourceTest {

    @Autowired
    private ContextsResource contexts;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void createContext() {
        server.expect(requestTo("https://api.browserbase.com/v1/contexts"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("""
                        {
                          "id": "ctx-123",
                          "cipherAlgorithm": "AES-256-CBC",
                          "initializationVectorSize": 16,
                          "publicKey": "-----BEGIN PUBLIC KEY-----",
                          "uploadUrl": "https://upload.browserbase.com/ctx-123"
                        }
                        """, MediaType.APPLICATION_JSON));

        var ctx = contexts.create();

        assertThat(ctx.id()).isEqualTo("ctx-123");
        assertThat(ctx.cipherAlgorithm()).isEqualTo("AES-256-CBC");
        assertThat(ctx.uploadUrl()).startsWith("https://upload.browserbase.com");
    }

    @Test
    void retrieveContext() {
        server.expect(requestTo("https://api.browserbase.com/v1/contexts/ctx-123"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        {
                          "id": "ctx-123",
                          "createdAt": "2024-01-01T00:00:00Z",
                          "projectId": "proj-1",
                          "updatedAt": "2024-01-01T00:00:00Z"
                        }
                        """, MediaType.APPLICATION_JSON));

        var ctx = contexts.retrieve("ctx-123");

        assertThat(ctx.id()).isEqualTo("ctx-123");
        assertThat(ctx.projectId()).isEqualTo("proj-1");
    }

    @Test
    void deleteContext() {
        server.expect(requestTo("https://api.browserbase.com/v1/contexts/ctx-123"))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withNoContent());

        contexts.delete("ctx-123");

        server.verify();
    }
}
