package id.renner.web.library.routing;

import com.sun.net.httpserver.HttpExchange;
import id.renner.web.library.http.CustomHttpContext;

public class RequestRouter {
    private final RoutingNode rootNode;

    public RequestRouter() {
        this.rootNode = new RoutingNode();
    }

    public void addRoute(String path, RequestHandler handler) {
        rootNode.insert(new RoutingKey(path), handler);
    }

    public void handle(HttpExchange httpExchange) {
        String path = httpExchange.getRequestURI().getPath();
        rootNode.handle(new RoutingKey(path), new CustomHttpContext(httpExchange));
    }

    public boolean isEmpty() {
        return rootNode.isEmpty();
    }
}