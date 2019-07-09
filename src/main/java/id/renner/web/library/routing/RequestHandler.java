package id.renner.web.library.routing;

import java.lang.reflect.Method;

public class RequestHandler {
    private final Object object;
    private final Method method;

    public RequestHandler(Object object, Method method) {
        this.object = object;
        this.method = method;
    }

    public void invoke(Object... args) {
        try {
            method.invoke(object, args);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }
}