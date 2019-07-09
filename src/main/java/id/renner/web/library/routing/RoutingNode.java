package id.renner.web.library.routing;

import id.renner.web.library.request.RequestExecutor;
import id.renner.web.library.request.SimpleHttpRequest;

import java.util.HashMap;

class RoutingNode extends HashMap<String, RoutingNode> {
    private static final String PATH_ELEMENT_ROUTING_KEY = "*";
    private static final String PATH_ELEMENT_START = "{";
    private static final String PATH_ELEMENT_END = "}";

    private RequestExecutor requestExecutor;
    private String pathElementKey;
    private boolean pathElementNode;

    RoutingNode() {
        super(1);
        this.requestExecutor = null;
        this.pathElementKey = null;
        this.pathElementNode = false;
    }

    void insert(RoutingKey routingKey, RequestExecutor requestExecutor) {
        if (!routingKey.hasMoreTokens()) {
            if (this.requestExecutor != null) {
                throw new RoutingException("can't have multiple request handlers for one path mapping");
            }

            this.requestExecutor = requestExecutor;
            return;
        }

        String keyPart = routingKey.nextToken();
        RoutingNode child;

        if (isPathElementKey(keyPart)) {
            if (!isEmpty()) {
                throw new RoutingException("can´t have method with path element and fixed path on same level");
            }
            child = getOrCreateChild(PATH_ELEMENT_ROUTING_KEY);
            child.setPathElementNode(keyPart);
        } else {
            child = getOrCreateChild(keyPart);
            if (child.isPathElementNode()) {
                throw new RoutingException("can´t have method with path element and fixed path on same level");
            }
        }

        child.insert(routingKey, requestExecutor);
    }

    private RoutingNode getOrCreateChild(String key) {
        RoutingNode child = getNode(key);

        if (child == null) {
            child = new RoutingNode();
            put(key, child);
        }

        return child;
    }

    RequestExecutor get(RoutingKey routingKey, SimpleHttpRequest request) {
        if (!routingKey.hasMoreTokens()) {
            return requestExecutor;
        }

        String childKey = routingKey.nextToken();
        RoutingNode childNode = getNode(childKey);
        if (childNode == null) {
            return null;
        }

        if (childNode.isPathElementNode()) { // set path element in request, so it can be used later
            request.putPathElement(childNode.getPathElementKey(), childKey);
        }

        return childNode.get(routingKey, request);
    }

    private RoutingNode getNode(Object key) {
        RoutingNode node = get(key);

        if (node == null) { // if path can't be matched, check if there is path element route
            node = get(PATH_ELEMENT_ROUTING_KEY);
        }

        return node;
    }

    private void setPathElementNode(String key) {
        this.pathElementKey = getPathElementKey(key);
        pathElementNode = true;
    }

    private boolean isPathElementNode() {
        return pathElementNode;
    }

    private String getPathElementKey() {
        return this.pathElementKey;
    }

    private boolean isPathElementKey(String key) {
        return key.startsWith(PATH_ELEMENT_START) && key.endsWith(PATH_ELEMENT_END);
    }

    private String getPathElementKey(String key) {
        return key.substring(PATH_ELEMENT_START.length(), key.length() - PATH_ELEMENT_END.length());
    }
}