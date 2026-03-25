package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

/**
 * Parameters for creating a new browser session.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SessionCreateParams(
        String projectId,
        String extensionId,
        Boolean keepAlive,
        Integer timeout,
        String region,
        BrowserSettings browserSettings,
        Object proxies,
        Map<String, Object> userMetadata
) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String projectId;
        private String extensionId;
        private Boolean keepAlive;
        private Integer timeout;
        private String region;
        private BrowserSettings browserSettings;
        private Object proxies;
        private Map<String, Object> userMetadata;

        public Builder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder extensionId(String extensionId) {
            this.extensionId = extensionId;
            return this;
        }

        public Builder keepAlive(boolean keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder region(String region) {
            this.region = region;
            return this;
        }

        public Builder browserSettings(BrowserSettings browserSettings) {
            this.browserSettings = browserSettings;
            return this;
        }

        public Builder proxies(boolean proxies) {
            this.proxies = proxies;
            return this;
        }

        public Builder proxies(List<ProxyConfig> proxies) {
            this.proxies = proxies;
            return this;
        }

        public Builder userMetadata(Map<String, Object> userMetadata) {
            this.userMetadata = userMetadata;
            return this;
        }

        public SessionCreateParams build() {
            return new SessionCreateParams(projectId, extensionId, keepAlive,
                    timeout, region, browserSettings, proxies, userMetadata);
        }
    }
}
