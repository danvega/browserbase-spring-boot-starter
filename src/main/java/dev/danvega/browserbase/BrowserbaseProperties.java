package dev.danvega.browserbase;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

@ConfigurationProperties(prefix = "browserbase")
public record BrowserbaseProperties(
        String apiKey,
        @DefaultValue("https://api.browserbase.com") String baseUrl,
        @DefaultValue("2") int maxRetries,
        @DefaultValue("60s") Duration timeout
) {}
