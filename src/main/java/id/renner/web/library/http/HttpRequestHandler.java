package id.renner.web.library.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import id.renner.web.library.request.RequestExecutor;
import id.renner.web.library.request.SimpleHttpRequest;
import id.renner.web.library.response.ResponseEntity;
import id.renner.web.library.routing.RequestRouter;

import java.io.IOException;
import java.util.logging.Logger;

public class HttpRequestHandler implements HttpHandler {
    private static final Logger logger = Logger.getLogger(HttpRequestHandler.class.getSimpleName());

    private final RequestRouter requestRouter;

    public HttpRequestHandler(RequestRouter requestRouter) {
        this.requestRouter = requestRouter;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.info("handling request from [" + exchange.getRemoteAddress() + "]");

        SimpleHttpRequest request = new SimpleHttpRequest(exchange);
        RequestExecutor requestExecutor = requestRouter.findHandler(request);
        if (requestExecutor == null) {
            handleNotFound(request);
        } else {
            try {
                ResponseEntity responseEntity = requestExecutor.invoke(request);
                request.sendResponse(responseEntity.getResponse(), responseEntity.getResponseCode());
            } catch (Exception ex) {
                handleInternalError(request, ex);
            }
        }

        logger.info("finished handling request from [" + exchange.getRemoteAddress() + "]");
    }

    private void handleInternalError(SimpleHttpRequest request, Exception exception) {
        request.sendResponse(exception.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void handleNotFound(SimpleHttpRequest request) {
        request.sendResponse("[" + request.getPath() + "] not found", HttpStatus.NOT_FOUND);
    }
}