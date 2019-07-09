package id.renner.web.library.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class CustomHttpRequest {
    private final HttpExchange exchange;
    private final Map<String, String> pathElements;
    private final String path;


    public CustomHttpRequest(HttpExchange httpExchange) {
        this.exchange = httpExchange;
        this.pathElements = new HashMap<>();
        this.path = httpExchange.getRequestURI().getPath();
    }

    public void putPathElement(String key, String value) {
        pathElements.put(key, value);
    }

    public String getPathElement(String key) {
        return pathElements.get(key);
    }

    public String getPath() {
        return path;
    }

    private OutputStream getOutputStream() {
        return exchange.getResponseBody();
    }

    private void sendResponseHeaders(int code, int messageLength) throws IOException {
        exchange.sendResponseHeaders(code, messageLength);
    }

    public void sendResponse(String message, int code) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getOutputStream()))) {

            // send headers then body
            sendResponseHeaders(code, message.length());
            writer.append(message);
        } catch (IOException ex) {
            throw new RuntimeException("failed when trying to respond to request");
        }
    }
}