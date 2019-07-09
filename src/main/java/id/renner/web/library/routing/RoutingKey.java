package id.renner.web.library.routing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class RoutingKey extends LinkedList<String> {

    public RoutingKey(String key) {
        super(Arrays.stream(key.split("/"))
                .skip(1)
                .collect(Collectors.toList()));
    }
}