package id.renner.web.library.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import id.renner.web.library.routing.RequestHandler;
import id.renner.web.library.routing.RequestRouter;

import java.io.IOException;
import java.util.logging.Logger;

public class CustomHttpHandler implements HttpHandler {
    private static final Logger logger = Logger.getLogger(CustomHttpHandler.class.getSimpleName());

    private final RequestRouter requestRouter;

    public CustomHttpHandler(RequestRouter requestRouter) {
        this.requestRouter = requestRouter;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.info("handling request from [" + exchange.getRemoteAddress() + "]");

        CustomHttpRequest request = new CustomHttpRequest(exchange);
        RequestHandler requestHandler = requestRouter.get(request);
        if (requestHandler == null) {
            request.sendResponse("[" + exchange.getRequestURI().getPath() + "] not found", 404);
        } else {
            requestHandler.invoke(request);
        }

        logger.info("finished handling request from [" + exchange.getRemoteAddress() + "]");
    }
}