package dev.danvega.browserbase.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Browser configuration for a session, including stealth, proxy, captcha, and viewport settings.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BrowserSettings(
        Boolean advancedStealth,
        Boolean blockAds,
        Boolean solveCaptchas,
        Boolean logSession,
        Boolean recordSession,
        String os,
        Viewport viewport,
        ContextConfig context,
        String extensionId,
        String captchaImageSelector,
        String captchaInputSelector
) {

    public record Viewport(int width, int height) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ContextConfig(String id, Boolean persist) {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Boolean advancedStealth;
        private Boolean blockAds;
        private Boolean solveCaptchas;
        private Boolean logSession;
        private Boolean recordSession;
        private String os;
        private Viewport viewport;
        private ContextConfig context;
        private String extensionId;
        private String captchaImageSelector;
        private String captchaInputSelector;

        public Builder advancedStealth(boolean advancedStealth) {
            this.advancedStealth = advancedStealth;
            return this;
        }

        public Builder blockAds(boolean blockAds) {
            this.blockAds = blockAds;
            return this;
        }

        public Builder solveCaptchas(boolean solveCaptchas) {
            this.solveCaptchas = solveCaptchas;
            return this;
        }

        public Builder logSession(boolean logSession) {
            this.logSession = logSession;
            return this;
        }

        public Builder recordSession(boolean recordSession) {
            this.recordSession = recordSession;
            return this;
        }

        public Builder os(String os) {
            this.os = os;
            return this;
        }

        public Builder viewport(int width, int height) {
            this.viewport = new Viewport(width, height);
            return this;
        }

        public Builder context(String id) {
            this.context = new ContextConfig(id, null);
            return this;
        }

        public Builder context(String id, boolean persist) {
            this.context = new ContextConfig(id, persist);
            return this;
        }

        public Builder extensionId(String extensionId) {
            this.extensionId = extensionId;
            return this;
        }

        public Builder captchaImageSelector(String selector) {
            this.captchaImageSelector = selector;
            return this;
        }

        public Builder captchaInputSelector(String selector) {
            this.captchaInputSelector = selector;
            return this;
        }

        public BrowserSettings build() {
            return new BrowserSettings(advancedStealth, blockAds, solveCaptchas,
                    logSession, recordSession, os, viewport, context, extensionId,
                    captchaImageSelector, captchaInputSelector);
        }
    }
}
