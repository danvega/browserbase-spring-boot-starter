package dev.danvega.browserbase;

import dev.danvega.browserbase.resource.SearchResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
class SearchResourceTest {

    @Autowired
    private SearchResource search;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void searchReturnsResults() {
        server.expect(requestTo("https://api.browserbase.com/v1/search"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("X-BB-API-Key", "test-api-key"))
                .andRespond(withSuccess("""
                        {
                          "requestId": "req-123",
                          "query": "spring boot",
                          "results": [
                            {
                              "id": "r1",
                              "url": "https://spring.io",
                              "title": "Spring Framework"
                            }
                          ]
                        }
                        """, MediaType.APPLICATION_JSON));

        var response = search.web("spring boot");

        assertThat(response.requestId()).isEqualTo("req-123");
        assertThat(response.results()).hasSize(1);
        assertThat(response.results().getFirst().url()).isEqualTo("https://spring.io");
        assertThat(response.results().getFirst().title()).isEqualTo("Spring Framework");
    }

    @Test
    void searchWithNumResults() {
        server.expect(requestTo("https://api.browserbase.com/v1/search"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("""
                        {
                          "requestId": "req-456",
                          "query": "java 21",
                          "results": []
                        }
                        """, MediaType.APPLICATION_JSON));

        var response = search.web("java 21", 5);

        assertThat(response.requestId()).isEqualTo("req-456");
        assertThat(response.results()).isEmpty();
    }
}
