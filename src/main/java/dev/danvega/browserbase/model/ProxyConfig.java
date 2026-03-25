package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProxyConfig.BrowserbaseProxy.class, name = "browserbase"),
        @JsonSubTypes.Type(value = ProxyConfig.ExternalProxy.class, name = "external"),
        @JsonSubTypes.Type(value = ProxyConfig.NoneProxy.class, name = "none")
})
/**
 * Proxy configuration for a browser session.
 * <p>
 * Supports Browserbase-managed proxies, external proxies, and disabling proxies
 * for specific domain patterns.
 */
public sealed interface ProxyConfig {

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record BrowserbaseProxy(
            String domainPattern,
            Geolocation geolocation
    ) implements ProxyConfig {

        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record Geolocation(
                String country,
                String city,
                String state
        ) {}
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ExternalProxy(
            String server,
            String domainPattern,
            String username,
            String password
    ) implements ProxyConfig {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record NoneProxy(
            String domainPattern
    ) implements ProxyConfig {}
}
