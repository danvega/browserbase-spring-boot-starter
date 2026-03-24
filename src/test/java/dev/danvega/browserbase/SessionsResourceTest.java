package dev.danvega.browserbase;

import dev.danvega.browserbase.model.SessionCreateParams;
import dev.danvega.browserbase.model.SessionUpdateParams;
import dev.danvega.browserbase.resource.sessions.SessionsResource;
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
class SessionsResourceTest {

    @Autowired
    private SessionsResource sessions;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void createSession() {
        server.expect(requestTo("https://api.browserbase.com/v1/sessions"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("""
                        {
                          "id": "sess-123",
                          "status": "RUNNING",
                          "connectUrl": "wss://connect.browserbase.com/sess-123",
                          "seleniumRemoteUrl": "https://connect.browserbase.com/webdriver/sess-123",
                          "signingKey": "sk-123",
                          "projectId": "proj-1",
                          "region": "us-west-2",
                          "createdAt": "2024-01-01T00:00:00Z",
                          "expiresAt": "2024-01-01T01:00:00Z",
                          "startedAt": "2024-01-01T00:00:00Z",
                          "updatedAt": "2024-01-01T00:00:00Z",
                          "keepAlive": false,
                          "proxyBytes": 0
                        }
                        """, MediaType.APPLICATION_JSON));

        var session = sessions.create(SessionCreateParams.builder().build());

        assertThat(session.id()).isEqualTo("sess-123");
        assertThat(session.status()).isEqualTo("RUNNING");
        assertThat(session.connectUrl()).isEqualTo("wss://connect.browserbase.com/sess-123");
        assertThat(session.seleniumRemoteUrl()).isEqualTo("https://connect.browserbase.com/webdriver/sess-123");
    }

    @Test
    void retrieveSession() {
        server.expect(requestTo("https://api.browserbase.com/v1/sessions/sess-123"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        {
                          "id": "sess-123",
                          "status": "COMPLETED",
                          "projectId": "proj-1",
                          "region": "us-west-2",
                          "createdAt": "2024-01-01T00:00:00Z",
                          "expiresAt": "2024-01-01T01:00:00Z",
                          "startedAt": "2024-01-01T00:00:00Z",
                          "updatedAt": "2024-01-01T00:00:01Z",
                          "keepAlive": false,
                          "proxyBytes": 1024
                        }
                        """, MediaType.APPLICATION_JSON));

        var session = sessions.retrieve("sess-123");

        assertThat(session.id()).isEqualTo("sess-123");
        assertThat(session.status()).isEqualTo("COMPLETED");
        assertThat(session.proxyBytes()).isEqualTo(1024);
    }

    @Test
    void listSessions() {
        server.expect(requestTo("https://api.browserbase.com/v1/sessions"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        [
                          {
                            "id": "sess-1",
                            "status": "RUNNING",
                            "projectId": "proj-1",
                            "region": "us-west-2",
                            "createdAt": "2024-01-01T00:00:00Z",
                            "expiresAt": "2024-01-01T01:00:00Z",
                            "startedAt": "2024-01-01T00:00:00Z",
                            "updatedAt": "2024-01-01T00:00:00Z",
                            "keepAlive": false,
                            "proxyBytes": 0
                          }
                        ]
                        """, MediaType.APPLICATION_JSON));

        var results = sessions.list();

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().id()).isEqualTo("sess-1");
    }

    @Test
    void updateSession() {
        server.expect(requestTo("https://api.browserbase.com/v1/sessions/sess-123"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("""
                        {
                          "id": "sess-123",
                          "status": "COMPLETED",
                          "projectId": "proj-1",
                          "region": "us-west-2",
                          "createdAt": "2024-01-01T00:00:00Z",
                          "expiresAt": "2024-01-01T01:00:00Z",
                          "startedAt": "2024-01-01T00:00:00Z",
                          "updatedAt": "2024-01-01T00:00:01Z",
                          "keepAlive": false,
                          "proxyBytes": 0
                        }
                        """, MediaType.APPLICATION_JSON));

        var session = sessions.update("sess-123", SessionUpdateParams.requestRelease());

        assertThat(session.status()).isEqualTo("COMPLETED");
    }

    @Test
    void debugSession() {
        server.expect(requestTo("https://api.browserbase.com/v1/sessions/sess-123/debug"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        {
                          "debuggerFullscreenUrl": "https://debug.browserbase.com/full/sess-123",
                          "debuggerUrl": "https://debug.browserbase.com/sess-123",
                          "wsUrl": "wss://debug.browserbase.com/ws/sess-123",
                          "pages": [
                            {
                              "id": "page-1",
                              "debuggerFullscreenUrl": "https://debug.browserbase.com/full/page-1",
                              "debuggerUrl": "https://debug.browserbase.com/page-1",
                              "faviconUrl": "https://example.com/favicon.ico",
                              "title": "Example",
                              "url": "https://example.com"
                            }
                          ]
                        }
                        """, MediaType.APPLICATION_JSON));

        var liveUrls = sessions.debug("sess-123");

        assertThat(liveUrls.debuggerUrl()).isEqualTo("https://debug.browserbase.com/sess-123");
        assertThat(liveUrls.pages()).hasSize(1);
        assertThat(liveUrls.pages().getFirst().title()).isEqualTo("Example");
    }

    @Test
    void getSessionLogs() {
        server.expect(requestTo("https://api.browserbase.com/v1/sessions/sess-123/logs"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        [
                          {
                            "method": "Page.navigate",
                            "pageId": 1,
                            "sessionId": "sess-123",
                            "timestamp": 1704067200000
                          }
                        ]
                        """, MediaType.APPLICATION_JSON));

        var logs = sessions.logs().list("sess-123");

        assertThat(logs).hasSize(1);
        assertThat(logs.getFirst().method()).isEqualTo("Page.navigate");
        assertThat(logs.getFirst().sessionId()).isEqualTo("sess-123");
    }
}
