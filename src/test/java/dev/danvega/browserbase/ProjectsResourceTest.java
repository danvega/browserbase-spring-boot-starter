package dev.danvega.browserbase;

import dev.danvega.browserbase.resource.ProjectsResource;
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
class ProjectsResourceTest {

    @Autowired
    private ProjectsResource projects;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void retrieveProject() {
        server.expect(requestTo("https://api.browserbase.com/v1/projects/proj-1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        {
                          "id": "proj-1",
                          "concurrency": 10,
                          "createdAt": "2024-01-01T00:00:00Z",
                          "defaultTimeout": 3600,
                          "name": "My Project",
                          "ownerId": "user-1",
                          "updatedAt": "2024-01-01T00:00:00Z"
                        }
                        """, MediaType.APPLICATION_JSON));

        var project = projects.retrieve("proj-1");

        assertThat(project.id()).isEqualTo("proj-1");
        assertThat(project.name()).isEqualTo("My Project");
        assertThat(project.concurrency()).isEqualTo(10);
    }

    @Test
    void listProjects() {
        server.expect(requestTo("https://api.browserbase.com/v1/projects"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        [
                          {
                            "id": "proj-1",
                            "concurrency": 10,
                            "createdAt": "2024-01-01T00:00:00Z",
                            "defaultTimeout": 3600,
                            "name": "My Project",
                            "ownerId": "user-1",
                            "updatedAt": "2024-01-01T00:00:00Z"
                          }
                        ]
                        """, MediaType.APPLICATION_JSON));

        var result = projects.list();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("My Project");
    }

    @Test
    void getProjectUsage() {
        server.expect(requestTo("https://api.browserbase.com/v1/projects/proj-1/usage"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        {
                          "browserMinutes": 120,
                          "proxyBytes": 5242880
                        }
                        """, MediaType.APPLICATION_JSON));

        var usage = projects.usage("proj-1");

        assertThat(usage.browserMinutes()).isEqualTo(120);
        assertThat(usage.proxyBytes()).isEqualTo(5242880);
    }
}
