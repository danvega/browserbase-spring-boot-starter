package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Usage metrics for a Browserbase project.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProjectUsage(
        long browserMinutes,
        long proxyBytes
) {}
