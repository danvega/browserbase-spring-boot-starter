package dev.danvega.browserbase;

import dev.danvega.browserbase.model.FetchAPICreateParams;
import dev.danvega.browserbase.resource.FetchAPIResource;
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
class FetchAPIResourceTest {

    @Autowired
    private FetchAPIResource fetchAPI;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void fetchPageByUrl() {
        server.expect(requestTo("https://api.browserbase.com/v1/fetch"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("""
                        {
                          "id": "fetch-123",
                          "content": "# Example Page\\n\\nHello world",
                          "contentType": "text/html",
                          "encoding": "utf-8",
                          "headers": { "content-type": "text/html" },
                          "statusCode": 200
                        }
                        """, MediaType.APPLICATION_JSON));

        var response = fetchAPI.create("https://example.com");

        assertThat(response.id()).isEqualTo("fetch-123");
        assertThat(response.content()).contains("Hello world");
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void fetchPageWithParams() {
        server.expect(requestTo("https://api.browserbase.com/v1/fetch"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("""
                        {
                          "id": "fetch-456",
                          "content": "<html>test</html>",
                          "contentType": "text/html",
                          "encoding": "utf-8",
                          "headers": {},
                          "statusCode": 200
                        }
                        """, MediaType.APPLICATION_JSON));

        var response = fetchAPI.create(
                new FetchAPICreateParams("https://example.com", null, true, null));

        assertThat(response.id()).isEqualTo("fetch-456");
    }
}
