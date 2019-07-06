package id.renner.web.library.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class CustomHttpHandler implements HttpHandler {
    private static final Logger logger = Logger.getLogger(CustomHttpHandler.class.getSimpleName());

    private final Map<String, InstanceMethod> router;

    public CustomHttpHandler(Map<String, InstanceMethod> router) {
        this.router = router;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.info("handling request from [" + exchange.getRemoteAddress() + "]");
        InstanceMethod instanceMethod = router.get(exchange.getRequestURI().getPath());
        instanceMethod.invoke(exchange);
        logger.info("finished handling request from [" + exchange.getRemoteAddress() + "]");
    }
}