package dev.danvega.browserbase.resource;

import dev.danvega.browserbase.http.BrowserbaseHttpClient;
import dev.danvega.browserbase.model.Project;
import dev.danvega.browserbase.model.ProjectUsage;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Resource for retrieving Browserbase project details and usage metrics.
 */
public class ProjectsResource {

    private static final ParameterizedTypeReference<List<Project>> PROJECT_LIST_TYPE = new ParameterizedTypeReference<>() {};

    private final BrowserbaseHttpClient http;

    public ProjectsResource(BrowserbaseHttpClient http) {
        this.http = http;
    }

    /** Retrieve a project by ID. */
    public Project retrieve(String id) {
        return http.get("/v1/projects/{id}", Project.class, id);
    }

    /** List all projects. */
    public List<Project> list() {
        return http.get("/v1/projects", PROJECT_LIST_TYPE);
    }

    /** Get usage metrics for a project. */
    public ProjectUsage usage(String id) {
        return http.get("/v1/projects/{id}/usage", ProjectUsage.class, id);
    }
}
