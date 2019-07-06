package id.renner.web.http;

import java.lang.reflect.Method;

public class MethodExecutor {

    private final Object object;
    private final Method method;

    public MethodExecutor(Object object, Method method) {
        this.object = object;
        this.method = method;
    }

    public Object getObject() {
        return object;
    }

    public Method getMethod() {
        return method;
    }
}