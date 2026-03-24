# Browserbase Spring Boot Starter

A Spring Boot starter for the [Browserbase](https://www.browserbase.com/) API. Add one dependency, set one property, and inject `Browserbase` anywhere in your Spring application.

The API design mirrors the official [Node.js](https://github.com/browserbase/sdk-node) and [Python](https://github.com/browserbase/sdk-python) SDKs — same resource structure, same method names — so developers moving between languages feel at home.

## Requirements

- Java 25+
- Spring Boot 4.x

## Getting Started

### 1. Add the dependency

```xml
<dependency>
    <groupId>dev.danvega</groupId>
    <artifactId>browserbase-spring-boot-starter</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

### 2. Set your API key

Get an API key from [browserbase.com/settings](https://www.browserbase.com/settings) and add it to `application.properties`:

```properties
browserbase.api-key=${BROWSERBASE_API_KEY}
```

### 3. Inject and use

```java
import dev.danvega.browserbase.Browserbase;
import dev.danvega.browserbase.model.SearchWebResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResearchService {

    private final Browserbase browserbase;

    public ResearchService(Browserbase browserbase) {
        this.browserbase = browserbase;
    }

    public List<SearchWebResponse.Result> search(String query) {
        return browserbase.search().web(query).results();
    }

    public String fetchPage(String url) {
        return browserbase.fetchAPI().create(url).content();
    }
}
```

That's it. The starter auto-configures a `Browserbase` client bean when it detects your API key.

## Configuration

```yaml
browserbase:
  # Required
  api-key: ${BROWSERBASE_API_KEY}

  # Optional (defaults shown)
  base-url: https://api.browserbase.com
  max-retries: 2
  timeout: 60s
```

All properties support Spring Boot's relaxed binding (`api-key`, `apiKey`, `API_KEY`).

### Virtual Threads

The starter uses Spring's `RestClient`, which automatically runs on virtual threads when enabled:

```properties
spring.threads.virtual.enabled=true
```

All blocking HTTP calls and retry sleeps park the virtual thread instead of blocking a platform thread — no code changes needed.

## API Reference

The client exposes 6 resource groups, matching the official Node.js and Python SDKs:

```java
browserbase.sessions()     // Browser sessions
browserbase.contexts()     // Reusable browser contexts
browserbase.extensions()   // Chrome extensions
browserbase.fetchAPI()     // Page content fetching
browserbase.projects()     // Project management
browserbase.search()       // Web search
```

### Search

Search the web and get structured results.

```java
import dev.danvega.browserbase.model.SearchWebParams;

// Simple search
var results = browserbase.search().web("spring boot").results();

// With result count
var results = browserbase.search().web("spring boot", 5).results();

// With params object
var response = browserbase.search().web(new SearchWebParams("spring boot", 25));
```

### Fetch

Fetch a web page's content without spinning up a full browser session.

```java
import dev.danvega.browserbase.model.FetchAPICreateParams;

// Fetch page content (returns markdown by default)
var page = browserbase.fetchAPI().create("https://example.com");
System.out.println(page.content());
System.out.println(page.statusCode());

// With options
var page = browserbase.fetchAPI().create(
    new FetchAPICreateParams("https://example.com", null, true, true)
);
```

### Sessions

Create and manage headless browser sessions.

```java
import dev.danvega.browserbase.model.*;
import java.nio.file.Path;

// Create a session with options
var session = browserbase.sessions().create(
    SessionCreateParams.builder()
        .projectId("your-project-id")
        .keepAlive(true)
        .region("us-west-2")
        .browserSettings(BrowserSettings.builder()
            .blockAds(true)
            .solveCaptchas(true)
            .viewport(1920, 1080)
            .build())
        .build()
);

System.out.println(session.connectUrl());        // WebSocket URL
System.out.println(session.seleniumRemoteUrl()); // Selenium URL

// Retrieve a session
var session = browserbase.sessions().retrieve("session-id");

// List all sessions
var allSessions = browserbase.sessions().list();

// List running sessions only
var running = browserbase.sessions().list(SessionListParams.ofStatus("RUNNING"));

// Release a session
browserbase.sessions().update("session-id", SessionUpdateParams.requestRelease());

// Get live debug URLs
var debug = browserbase.sessions().debug("session-id");
System.out.println(debug.debuggerUrl());
```

#### Session Sub-Resources

```java
// Get session logs
var logs = browserbase.sessions().logs().list("session-id");

// Get session recording
var recording = browserbase.sessions().recording().retrieve("session-id");

// Download session files as ZIP
byte[] zip = browserbase.sessions().downloads().list("session-id");

// Upload a file to a session
var result = browserbase.sessions().uploads().create("session-id", Path.of("file.txt"));
```

### Contexts

Contexts allow browser state (cookies, local storage) to persist across sessions.

```java
import dev.danvega.browserbase.model.*;

// Create a new context
var ctx = browserbase.contexts().create();

// Create with a specific project
var ctx = browserbase.contexts().create(new ContextCreateParams("project-id"));

// Retrieve a context
var ctx = browserbase.contexts().retrieve("context-id");

// Delete a context
browserbase.contexts().delete("context-id");

// Use a context in a session
var session = browserbase.sessions().create(
    SessionCreateParams.builder()
        .browserSettings(BrowserSettings.builder()
            .context("context-id", true)  // persist = true
            .build())
        .build()
);
```

### Extensions

Upload and manage Chrome extensions.

```java
import java.nio.file.Path;

// Upload a Chrome extension
var ext = browserbase.extensions().create(Path.of("extension.zip"));

// Retrieve extension details
var ext = browserbase.extensions().retrieve("extension-id");

// Delete an extension
browserbase.extensions().delete("extension-id");
```

### Projects

```java
// List all projects
var projects = browserbase.projects().list();

// Get project details
var project = browserbase.projects().retrieve("project-id");

// Get usage metrics
var usage = browserbase.projects().usage("project-id");
System.out.println(usage.browserMinutes());
System.out.println(usage.proxyBytes());
```

## Proxy Configuration

The starter supports all three Browserbase proxy types using Java's sealed interfaces:

```java
import dev.danvega.browserbase.model.ProxyConfig;
import java.util.List;

// Browserbase managed proxy with geolocation
var session = browserbase.sessions().create(
    SessionCreateParams.builder()
        .proxies(List.of(
            new ProxyConfig.BrowserbaseProxy(
                null,
                new ProxyConfig.BrowserbaseProxy.Geolocation("US", "San Francisco", "CA")
            )
        ))
        .build()
);

// External proxy
var session = browserbase.sessions().create(
    SessionCreateParams.builder()
        .proxies(List.of(
            new ProxyConfig.ExternalProxy("http://proxy.example.com:8080", null, "user", "pass")
        ))
        .build()
);

// Simple boolean proxy toggle
var session = browserbase.sessions().create(
    SessionCreateParams.builder()
        .proxies(true)
        .build()
);
```

## Error Handling

All exceptions extend `BrowserbaseException` (unchecked) and mirror the official SDK error hierarchy:

| Exception | HTTP Status | Retried? |
|-----------|-------------|----------|
| `BadRequestException` | 400 | No |
| `AuthenticationException` | 401 | No |
| `PermissionDeniedException` | 403 | No |
| `NotFoundException` | 404 | No |
| `ConflictException` | 409 | Yes |
| `UnprocessableEntityException` | 422 | No |
| `RateLimitException` | 429 | Yes |
| `InternalServerException` | 500+ | Yes |
| `APIConnectionException` | Network errors | Yes |

Transient failures are automatically retried using Spring Framework's `RetryTemplate` with exponential backoff (1s, 2s, 4s...). The maximum number of retries is configurable via `browserbase.max-retries` (default: 2). If all retries are exhausted, the original exception is thrown.

```java
import dev.danvega.browserbase.model.exception.*;

try {
    browserbase.search().web("query");
} catch (RateLimitException e) {
    // All retries exhausted
    System.err.println("Rate limit exceeded: " + e.getBody());
} catch (AuthenticationException e) {
    // Not retried — check your API key
    System.err.println("Invalid API key");
} catch (BrowserbaseException e) {
    // Catch-all for any Browserbase API error
    System.err.println("API error: " + e.getMessage());
}
```

## Overriding Beans

Every bean is registered with `@ConditionalOnMissingBean`, so you can replace any component:

```java
import dev.danvega.browserbase.Browserbase;
import dev.danvega.browserbase.resource.*;
import dev.danvega.browserbase.resource.sessions.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomBrowserbaseConfig {

    // Override a single resource
    @Bean
    public SearchResource searchResource(BrowserbaseHttpClient httpClient) {
        return new MyCustomSearchResource(httpClient);
    }

    // Or override the entire client
    @Bean
    public Browserbase browserbase(SessionsResource sessions,
                                   ContextsResource contexts,
                                   ExtensionsResource extensions,
                                   FetchAPIResource fetchAPI,
                                   ProjectsResource projects,
                                   SearchResource search) {
        return new Browserbase(sessions, contexts, extensions, fetchAPI, projects, search);
    }
}
```

## Built With

- [Spring Boot 4](https://spring.io/projects/spring-boot) — Auto-configuration, `RestClient`, `RetryTemplate`
- [Java 25](https://openjdk.org/projects/jdk/25/) — Records, sealed interfaces, virtual threads
- [Browserbase API](https://docs.browserbase.com/reference/api/overview) — Headless browser infrastructure

## License

Apache License 2.0
