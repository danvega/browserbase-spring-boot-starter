package dev.danvega.browserbase;

import dev.danvega.browserbase.http.BrowserbaseHttpClient;
import dev.danvega.browserbase.resource.*;
import dev.danvega.browserbase.resource.sessions.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@AutoConfiguration
@ConditionalOnProperty(prefix = "browserbase", name = "api-key")
@EnableConfigurationProperties(BrowserbaseProperties.class)
public class BrowserbaseAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public BrowserbaseHttpClient browserbaseHttpClient(
            RestClient.Builder restClientBuilder,
            BrowserbaseProperties properties) {
        return new BrowserbaseHttpClient(restClientBuilder, properties);
    }

    // Session sub-resources

    @Bean
    @ConditionalOnMissingBean
    public DownloadsResource downloadsResource(BrowserbaseHttpClient httpClient) {
        return new DownloadsResource(httpClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public LogsResource logsResource(BrowserbaseHttpClient httpClient) {
        return new LogsResource(httpClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public RecordingResource recordingResource(BrowserbaseHttpClient httpClient) {
        return new RecordingResource(httpClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public UploadsResource uploadsResource(BrowserbaseHttpClient httpClient) {
        return new UploadsResource(httpClient);
    }

    // Top-level resources

    @Bean
    @ConditionalOnMissingBean
    public SessionsResource sessionsResource(BrowserbaseHttpClient httpClient,
                                             DownloadsResource downloads,
                                             LogsResource logs,
                                             RecordingResource recording,
                                             UploadsResource uploads) {
        return new SessionsResource(httpClient, downloads, logs, recording, uploads);
    }

    @Bean
    @ConditionalOnMissingBean
    public ContextsResource contextsResource(BrowserbaseHttpClient httpClient) {
        return new ContextsResource(httpClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public ExtensionsResource extensionsResource(BrowserbaseHttpClient httpClient) {
        return new ExtensionsResource(httpClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public FetchAPIResource fetchAPIResource(BrowserbaseHttpClient httpClient) {
        return new FetchAPIResource(httpClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public ProjectsResource projectsResource(BrowserbaseHttpClient httpClient) {
        return new ProjectsResource(httpClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public SearchResource searchResource(BrowserbaseHttpClient httpClient) {
        return new SearchResource(httpClient);
    }

    // Main client

    @Bean
    @ConditionalOnMissingBean
    public Browserbase browserbase(SessionsResource sessions,
                                   ContextsResource contexts,
                                   ExtensionsResource extensions,
                                   FetchAPIResource fetchAPI,
                                   ProjectsResource projects,
                                   SearchResource search) {
        return new Browserbase(sessions, contexts, extensions, fetchAPI, projects, search);
    }
}
