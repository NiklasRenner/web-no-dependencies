package id.renner.web.library.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class CustomHttpHandler implements HttpHandler {
    private final Map<String, InstanceMethod> router;

    public CustomHttpHandler(Map<String, InstanceMethod> router) {
        this.router = router;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InstanceMethod instanceMethod = router.get(exchange.getRequestURI().getPath());
        instanceMethod.invoke(exchange);
    }
}