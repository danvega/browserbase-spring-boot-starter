package dev.danvega.browserbase;

import dev.danvega.browserbase.resource.*;
import dev.danvega.browserbase.resource.sessions.SessionsResource;

/**
 * Main client for the Browserbase API.
 * <p>
 * Mirrors the resource-namespaced pattern of the official Node.js and Python SDKs:
 * <pre>{@code
 * browserbase.sessions().create(params)
 * browserbase.search().web("query")
 * browserbase.fetchAPI().create("https://example.com")
 * browserbase.sessions().logs().list(sessionId)
 * }</pre>
 */
public class Browserbase {

    private final SessionsResource sessions;
    private final ContextsResource contexts;
    private final ExtensionsResource extensions;
    private final FetchAPIResource fetchAPI;
    private final ProjectsResource projects;
    private final SearchResource search;

    public Browserbase(SessionsResource sessions,
                       ContextsResource contexts,
                       ExtensionsResource extensions,
                       FetchAPIResource fetchAPI,
                       ProjectsResource projects,
                       SearchResource search) {
        this.sessions = sessions;
        this.contexts = contexts;
        this.extensions = extensions;
        this.fetchAPI = fetchAPI;
        this.projects = projects;
        this.search = search;
    }

    /** Returns the resource for managing browser sessions and their sub-resources. */
    public SessionsResource sessions() {
        return sessions;
    }

    /** Returns the resource for managing reusable browser contexts. */
    public ContextsResource contexts() {
        return contexts;
    }

    /** Returns the resource for managing Chrome extensions. */
    public ExtensionsResource extensions() {
        return extensions;
    }

    /** Returns the resource for fetching web page content without a full browser session. */
    public FetchAPIResource fetchAPI() {
        return fetchAPI;
    }

    /** Returns the resource for retrieving project details and usage metrics. */
    public ProjectsResource projects() {
        return projects;
    }

    /** Returns the resource for performing web searches. */
    public SearchResource search() {
        return search;
    }
}
