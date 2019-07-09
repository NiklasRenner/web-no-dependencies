package id.renner.web.library.routing;

import id.renner.web.library.http.CustomHttpRequest;

import java.util.HashMap;

class RoutingNode extends HashMap<String, RoutingNode> {
    private RequestHandler requestHandler;
    private boolean endNode;
    private String pathKey;
    private boolean wildcardNode;

    RoutingNode() {
        this.requestHandler = null;
        this.endNode = false;
        this.pathKey = null;
        this.wildcardNode = false;
    }

    RequestHandler get(RoutingKey routingKey, CustomHttpRequest httpContext) {
        if (!routingKey.hasMoreTokens()) {
            return requestHandler;
        } else {
            String childKey = routingKey.nextToken();
            RoutingNode childNode = getNode(childKey);
            if (childNode == null) {
                return null;
            } else {
                if (childNode.isWildCardNode()) {
                    httpContext.putPathElement(childNode.getPathKey(), childKey);
                }
                return childNode.get(routingKey, httpContext);
            }
        }
    }

    void insert(RoutingKey routingKey, RequestHandler requestHandler) {
        if (!routingKey.hasMoreTokens()) {
            if (!isEndNode()) {
                setEndNode(requestHandler);
            } else {
                throw new RuntimeException("path already has end node");
            }
        } else {
            String keyPart = routingKey.nextToken();
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
        if (isWildCardNode()) {
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

    private void setWildcardNode(String key) {
        this.pathKey = key;
        this.wildcardNode = true;
    }

    private void setEndNode(RequestHandler requestHandler) {
        this.endNode = true;
        this.requestHandler = requestHandler;
    }

    private boolean isWildCardNode() {
        return wildcardNode;
    }

    private boolean isEndNode() {
        return endNode;
    }

    private String getPathKey() {
        return this.pathKey;
    }

    private boolean isPathElementKey(String key) {
        return key.contains("{"); // TODO do proper validation
    }
}