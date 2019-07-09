package id.renner.web.library.routing;

import id.renner.web.library.http.CustomHttpContext;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class RoutingNode extends HashMap<String, RoutingNode> {
    private RequestHandler requestHandler;
    private boolean endNode;
    private String pathKey;
    private boolean wildcardNode;

    public RoutingNode() {
        this.requestHandler = null;
        this.endNode = false;
        this.pathKey = null;
        this.wildcardNode = false;
    }

    public void handle(RoutingKey routingKey, CustomHttpContext httpContext) {
        if (routingKey.isEmpty()) {
            this.requestHandler.invoke(httpContext);
        } else {
            String childKey = routingKey.pop();
            RoutingNode childNode = getNode(childKey);
            if (childNode == null) {
                handleNotFound(httpContext);
            } else {
                if (childNode.isWildCardNode()) {
                    httpContext.putPathElement(childNode.getPathKey(), childKey);
                }
                childNode.handle(routingKey, httpContext);
            }
        }
    }

    public void insert(RoutingKey routingKey, RequestHandler requestHandler) {
        if (routingKey.isEmpty()) {
            if (!isEndNode()) {
                setEndNode(requestHandler);
            } else {
                throw new RuntimeException("path already has end node");
            }
        } else {
            String keyPart = routingKey.pop();
            if (isPathElementKey(keyPart)) {
                RoutingNode wildcardNode = getOrCreateChild("*");
                wildcardNode.setWildcardNode(keyPart.substring(1, keyPart.length() - 1));
                wildcardNode.insert(routingKey, requestHandler);
            } else {
                getOrCreateChild(keyPart).insert(routingKey, requestHandler);
            }
        }
    }

    private void putNode(String key, RoutingNode childNode) {
        if (hasWildcardNode()) {
            throw new RuntimeException("path already has wildcard node");
        }

        put(key, childNode);
    }

    private RoutingNode getNode(Object key) {
        RoutingNode node = get(key);

        if (node == null) {
            node = get("*");
        }

        return node;
    }


    private RoutingNode getOrCreateChild(String key) {
        RoutingNode child = getNode(key);

        if (child == null) {
            child = new RoutingNode();
            putNode(key, child);
        }

        return child;
    }

    public void handleNotFound(CustomHttpContext httpContext) {
        try (OutputStream output = httpContext.getExchange().getResponseBody()) {
            String message = "[" + httpContext.getExchange().getRequestURI().getPath() + "] not found";
            httpContext.getExchange().sendResponseHeaders(404, message.length());
            output.write(message.getBytes());
        } catch (IOException ex) {
            throw new RuntimeException("except trying to respond to thing");
        }
    }

    public boolean hasWildcardNode() {
        return containsKey("*");
    }

    public void setWildcardNode(String key) {
        this.pathKey = key;
        this.wildcardNode = true;
    }

    public void setEndNode(RequestHandler requestHandler) {
        this.endNode = true;
        this.requestHandler = requestHandler;
    }

    public boolean isWildCardNode() {
        return wildcardNode;
    }

    public boolean isEndNode() {
        return endNode;
    }

    public String getPathKey() {
        return this.pathKey;
    }

    private boolean isPathElementKey(String key) {
        return key.contains("{"); // TODO do proper validation
    }
}