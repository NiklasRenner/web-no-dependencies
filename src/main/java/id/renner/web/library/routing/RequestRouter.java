package id.renner.web.library.routing;

import id.renner.web.library.http.CustomHttpRequest;

public class RequestRouter {
    private final RoutingNode rootNode;

    public RequestRouter() {
        this.rootNode = new RoutingNode();
    }

    public void addRoute(String methodAndPathKey, RequestHandler handler) {
        RoutingKey routingKey = new RoutingKey(methodAndPathKey);
        rootNode.insert(routingKey, handler);
    }

    public RequestHandler findHandler(CustomHttpRequest request) {
        RoutingKey routingKey = new RoutingKey(request.getMethod() + request.getPath());
        return rootNode.get(routingKey, request);
    }

    public boolean isEmpty() {
        return rootNode.isEmpty();
    }
}