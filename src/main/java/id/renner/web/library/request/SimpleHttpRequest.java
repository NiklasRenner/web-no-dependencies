package id.renner.web.library.request;

import com.sun.net.httpserver.HttpExchange;
import id.renner.web.library.http.HttpStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class SimpleHttpRequest {
    private final HttpExchange exchange;
    private final Map<String, String> pathElements;
    private final Map<String, String> queryParameters;

    private boolean hasResponded;

    public SimpleHttpRequest(HttpExchange exchange) {
        this.exchange = exchange;
        this.pathElements = new HashMap<>();
        this.queryParameters = new HashMap<>();

        this.hasResponded = false;

        init();
    }

    private void init() {
        String rawQueryString = exchange.getRequestURI().getQuery();

        if (rawQueryString != null) {
            StringTokenizer queryStrings = new StringTokenizer(rawQueryString, "&");
            while (queryStrings.hasMoreElements()) {
                String queryString = queryStrings.nextToken();
                StringTokenizer queryPair = new StringTokenizer(queryString, "=");
                queryParameters.put(queryPair.nextToken(), queryPair.nextToken());
            }
        }
    }

    public void putPathElement(String key, String value) {
        pathElements.put(key, value);
    }

    public String getPathElement(String key) {
        return pathElements.get(key);
    }

    public String getPath() {
        return exchange.getRequestURI().getPath();
    }

    public String getMethod() {
        return exchange.getRequestMethod();
    }

    public String getQueryParameterOrDefault(String key, String defaultValue) {
        return queryParameters.getOrDefault(key, defaultValue);
    }

    public String getBody() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean hasResponded() {
        return hasResponded;
    }

    public void sendResponse(String message, HttpStatus status) {
        if (hasResponded) {
            throw new IllegalStateException("can't respond to same request twice");
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody()))) {
            exchange.sendResponseHeaders(status.value(), message.length());
            writer.append(message);
            hasResponded = true;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}