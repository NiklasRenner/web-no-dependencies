package id.renner.web.library.routing;

import id.renner.web.library.request.RequestExecutor;
import id.renner.web.library.request.SimpleHttpRequest;

public class RequestRouter {
    private final RoutingNode rootNode;

    public RequestRouter() {
        this.rootNode = new RoutingNode();
    }

    public void addRoute(String methodAndPathKey, RequestExecutor requestExecutor) {
        RoutingKey routingKey = new RoutingKey(methodAndPathKey);
        rootNode.insert(routingKey, requestExecutor);
    }

    public RequestExecutor findHandler(SimpleHttpRequest request) {
        RoutingKey routingKey = new RoutingKey(request.getMethod() + request.getPath());
        return rootNode.get(routingKey, request);
    }

    public boolean isEmpty() {
        return rootNode.isEmpty();
    }
}