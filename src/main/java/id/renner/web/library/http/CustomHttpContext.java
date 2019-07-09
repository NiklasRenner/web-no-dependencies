package id.renner.web.library.http;

import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;
import java.util.Map;

public class CustomHttpContext {
    private final HttpExchange exchange;
    private final Map<String, String> pathElements;

    public CustomHttpContext(HttpExchange httpExchange) {
        this.exchange = httpExchange;
        this.pathElements = new HashMap<>();
    }

    public void putPathElement(String key, String value) {
        pathElements.put(key, value);
    }

    public String getPathElement(String key) {
        return pathElements.get(key);
    }

    public HttpExchange getExchange() {
        return exchange;
    }
}