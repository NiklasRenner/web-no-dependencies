package id.renner.web.library.http;

import java.lang.reflect.Method;

public class InstanceMethod {

    private final Object object;
    private final Method method;

    public InstanceMethod(Object object, Method method) {
        this.object = object;
        this.method = method;
    }

    public Object getObject() {
        return object;
    }

    public Method getMethod() {
        return method;
    }

    public void invoke(Object... args) {
        try {
            method.invoke(object, args);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }
}