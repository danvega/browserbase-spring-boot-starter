package dev.danvega.browserbase;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

/**
 * Configuration properties for the Browserbase Spring Boot starter.
 * <p>
 * All properties are bound under the {@code browserbase} prefix.
 *
 * @param apiKey      the Browserbase API key (required)
 * @param baseUrl     the API base URL (defaults to {@code https://api.browserbase.com})
 * @param maxRetries  maximum number of retries for transient failures (defaults to 2)
 * @param timeout     request timeout duration (defaults to 60 seconds)
 */
@ConfigurationProperties(prefix = "browserbase")
public record BrowserbaseProperties(
        String apiKey,
        @DefaultValue("https://api.browserbase.com") String baseUrl,
        @DefaultValue("2") int maxRetries,
        @DefaultValue("60s") Duration timeout
) {}
