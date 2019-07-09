package id.renner.web.library.routing;

import java.util.StringTokenizer;

class RoutingKey extends StringTokenizer {

    RoutingKey(String key) {
        super(key, "/");
    }
}