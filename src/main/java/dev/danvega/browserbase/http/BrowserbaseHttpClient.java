package dev.danvega.browserbase.http;

import dev.danvega.browserbase.BrowserbaseProperties;
import dev.danvega.browserbase.model.exception.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.retry.RetryException;
import org.springframework.core.retry.RetryPolicy;
import org.springframework.core.retry.RetryTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;

/**
 * HTTP client for the Browserbase API built on Spring's {@link RestClient}.
 * <p>
 * Handles authentication, maps error status codes to typed {@link BrowserbaseException}
 * subclasses, and retries transient failures using Spring Framework's {@link RetryTemplate}
 * with exponential backoff.
 * <p>
 * Retryable errors: 408 (timeout), 409 (conflict), 429 (rate limit), 500+ (server errors),
 * and network failures ({@link APIConnectionException}).
 */
public class BrowserbaseHttpClient {

    private static final int MAX_ERROR_BODY_LENGTH = 1000;
    private static final String USER_AGENT = loadUserAgent();

    private final RestClient restClient;
    private final RetryTemplate retryTemplate;

    public BrowserbaseHttpClient(RestClient.Builder restClientBuilder, BrowserbaseProperties props) {
        this.restClient = restClientBuilder
                .baseUrl(props.baseUrl())
                .defaultHeader("X-BB-API-Key", props.apiKey())
                .defaultHeader("User-Agent", USER_AGENT)
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    handleErrorResponse(response);
                })
                .build();

        this.retryTemplate = new RetryTemplate(RetryPolicy.builder()
                .maxRetries(props.maxRetries())
                .delay(Duration.ofSeconds(1))
                .multiplier(2.0)
                .maxDelay(Duration.ofSeconds(8))
                .includes(
                        RateLimitException.class,
                        ConflictException.class,
                        InternalServerException.class,
                        APIConnectionException.class
                )
                .build());
    }

    /** GET with a Class response type. URI template variables are supported. */
    public <T> T get(String uriTemplate, Class<T> responseType, Object... uriVars) {
        return retry(() -> restClient.get().uri(uriTemplate, uriVars).retrieve().body(responseType));
    }

    /** GET with a ParameterizedTypeReference response type. URI template variables are supported. */
    public <T> T get(String uriTemplate, ParameterizedTypeReference<T> responseType, Object... uriVars) {
        return retry(() -> restClient.get().uri(uriTemplate, uriVars).retrieve().body(responseType));
    }

    /** GET with query parameters and a ParameterizedTypeReference response type. */
    public <T> T get(String path, Map<String, Object> queryParams, ParameterizedTypeReference<T> responseType) {
        return retry(() -> restClient.get()
                .uri(path, uriBuilder -> {
                    queryParams.forEach((key, value) -> {
                        if (value != null) {
                            uriBuilder.queryParam(key, value);
                        }
                    });
                    return uriBuilder.build();
                })
                .retrieve()
                .body(responseType));
    }

    /** GET that returns raw bytes (e.g., for file downloads). */
    public byte[] getBytes(String uriTemplate, Object... uriVars) {
        return retry(() -> restClient.get().uri(uriTemplate, uriVars).retrieve().body(byte[].class));
    }

    /** POST with a JSON body and Class response type. */
    public <T> T post(String uriTemplate, Object body, Class<T> responseType, Object... uriVars) {
        return retry(() -> restClient.post()
                .uri(uriTemplate, uriVars)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(responseType));
    }

    /** POST with a JSON body and ParameterizedTypeReference response type. */
    public <T> T post(String uriTemplate, Object body, ParameterizedTypeReference<T> responseType, Object... uriVars) {
        return retry(() -> restClient.post()
                .uri(uriTemplate, uriVars)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(responseType));
    }

    /** PUT with a JSON body. */
    public <T> T put(String uriTemplate, Object body, Class<T> responseType, Object... uriVars) {
        return retry(() -> {
            var spec = restClient.put().uri(uriTemplate, uriVars).contentType(MediaType.APPLICATION_JSON);
            if (body != null) {
                spec.body(body);
            }
            return spec.retrieve().body(responseType);
        });
    }

    /** DELETE request. */
    public void delete(String uriTemplate, Object... uriVars) {
        retry(() -> {
            restClient.delete().uri(uriTemplate, uriVars).retrieve().toBodilessEntity();
            return null;
        });
    }

    /** POST with multipart file upload. */
    public <T> T postMultipart(String uriTemplate, Path file, Class<T> responseType, Object... uriVars) {
        return retry(() -> {
            var builder = new MultipartBodyBuilder();
            builder.part("file", new FileSystemResource(file));
            return restClient.post()
                    .uri(uriTemplate, uriVars)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(builder.build())
                    .retrieve()
                    .body(responseType);
        });
    }

    private <T> T retry(RetryCallback<T> callback) {
        try {
            return retryTemplate.execute(callback::call);
        } catch (RetryException e) {
            if (e.getCause() instanceof BrowserbaseException bbe) {
                throw bbe;
            }
            throw new BrowserbaseException("Request failed after retries", e.getCause());
        }
    }

    @FunctionalInterface
    private interface RetryCallback<T> {
        T call();
    }

    private void handleErrorResponse(ClientHttpResponse response) throws IOException {
        int status = response.getStatusCode().value();
        var bytes = response.getBody().readAllBytes();
        String body = bytes.length > 0 ? truncate(new String(bytes)) : "";
        throwForStatus(status, body);
    }

    private void throwForStatus(int status, String body) {
        switch (status) {
            case 400 -> throw new BadRequestException("Bad request (400)", body);
            case 401 -> throw new AuthenticationException("Authentication failed (401)", body);
            case 403 -> throw new PermissionDeniedException("Permission denied (403)", body);
            case 404 -> throw new NotFoundException("Resource not found (404)", body);
            case 408 -> throw new APIConnectionException("Request timeout (408)", null);
            case 409 -> throw new ConflictException("Conflict (409)", body);
            case 422 -> throw new UnprocessableEntityException("Unprocessable entity (422)", body);
            case 429 -> throw new RateLimitException("Rate limit exceeded (429)", body);
            default -> {
                if (status >= 500) {
                    throw new InternalServerException("Server error (" + status + ")", status, body);
                }
                throw new BrowserbaseException("Unexpected status: " + status + " — " + body);
            }
        }
    }

    private static String truncate(String s) {
        return s.length() > MAX_ERROR_BODY_LENGTH
                ? s.substring(0, MAX_ERROR_BODY_LENGTH) + "... [truncated]"
                : s;
    }

    private static String loadUserAgent() {
        String version = BrowserbaseHttpClient.class.getPackage().getImplementationVersion();
        if (version != null) {
            return "browserbase-spring-boot-starter/" + version;
        }
        try (InputStream is = BrowserbaseHttpClient.class.getResourceAsStream(
                "/META-INF/maven/dev.danvega/browserbase-spring-boot-starter/pom.properties")) {
            if (is != null) {
                var props = new Properties();
                props.load(is);
                return "browserbase-spring-boot-starter/" + props.getProperty("version", "unknown");
            }
        } catch (IOException ignored) {
        }
        return "browserbase-spring-boot-starter/unknown";
    }
}
