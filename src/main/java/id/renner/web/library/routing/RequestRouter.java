package id.renner.web.library.routing;

import id.renner.web.library.http.CustomHttpRequest;

public class RequestRouter {
    private final RoutingNode rootNode;

    public RequestRouter() {
        this.rootNode = new RoutingNode();
    }

    public void addRoute(String path, RequestHandler handler) {
        RoutingKey routingKey = new RoutingKey(path);
        rootNode.insert(routingKey, handler);
    }

    public RequestHandler get(CustomHttpRequest httpContext) {
        RoutingKey routingKey = new RoutingKey(httpContext.getPath());
        return rootNode.get(routingKey, httpContext);
    }

    public boolean isEmpty() {
        return rootNode.isEmpty();
    }
}