package dev.danvega.browserbase;

import dev.danvega.browserbase.http.BrowserbaseHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.web.client.RestClient;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class BrowserbaseAutoConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withBean(RestClient.Builder.class, RestClient::builder)
            .withConfiguration(AutoConfigurations.of(BrowserbaseAutoConfiguration.class));

    @Test
    void doesNotActivateWithoutApiKey() {
        runner.run(ctx ->
                assertThat(ctx).doesNotHaveBean(Browserbase.class));
    }

    @Test
    void activatesWhenApiKeyPresent() {
        runner.withPropertyValues("browserbase.api-key=test-key")
                .run(ctx ->
                        assertThat(ctx).hasSingleBean(Browserbase.class));
    }

    @Test
    void createsBothBeans() {
        runner.withPropertyValues("browserbase.api-key=test-key")
                .run(ctx -> {
                    assertThat(ctx).hasSingleBean(Browserbase.class);
                    assertThat(ctx).hasSingleBean(BrowserbaseHttpClient.class);
                });
    }

    @Test
    void defaultPropertiesAreApplied() {
        runner.withPropertyValues("browserbase.api-key=test-key")
                .run(ctx -> {
                    var props = ctx.getBean(BrowserbaseProperties.class);
                    assertThat(props.baseUrl()).isEqualTo("https://api.browserbase.com");
                    assertThat(props.maxRetries()).isEqualTo(2);
                    assertThat(props.timeout()).isEqualTo(Duration.ofSeconds(60));
                });
    }

    @Test
    void customPropertiesAreApplied() {
        runner.withPropertyValues(
                        "browserbase.api-key=my-key",
                        "browserbase.base-url=https://custom.api.com",
                        "browserbase.max-retries=5",
                        "browserbase.timeout=30s"
                )
                .run(ctx -> {
                    var props = ctx.getBean(BrowserbaseProperties.class);
                    assertThat(props.apiKey()).isEqualTo("my-key");
                    assertThat(props.baseUrl()).isEqualTo("https://custom.api.com");
                    assertThat(props.maxRetries()).isEqualTo(5);
                    assertThat(props.timeout()).isEqualTo(Duration.ofSeconds(30));
                });
    }

    @Test
    void userDefinedBrowserbaseBeanTakesPrecedence() {
        runner.withPropertyValues("browserbase.api-key=test-key")
                .withBean(Browserbase.class, () -> new Browserbase(null, null, null, null, null, null))
                .run(ctx ->
                        assertThat(ctx).hasSingleBean(Browserbase.class));
    }
}
