package id.renner.web.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class PathRequestHandler implements HttpHandler {
    private static final Logger logger = Logger.getLogger(PathRequestHandler.class.getSimpleName());

    private final Map<String, MethodExecutor> router;

    public PathRequestHandler(Map<String, MethodExecutor> router) {
        this.router = router;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            logger.info("handling request: " + exchange.getRemoteAddress());
            MethodExecutor methodExecutor = router.get(exchange.getRequestURI().getPath());
            methodExecutor.getMethod().invoke(methodExecutor.getObject(), exchange);
            logger.info("handled request" + exchange.getRemoteAddress());
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException("couldn't execute method");
        }
    }
}