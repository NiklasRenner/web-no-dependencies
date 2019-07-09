package id.renner.web.library.routing;

import id.renner.web.library.controller.PathElement;
import id.renner.web.library.controller.QueryParameter;
import id.renner.web.library.controller.RequestBody;
import id.renner.web.library.http.CustomHttpRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RequestHandler {
    private final Object object;
    private final Method method;
    private final List<Converter> converters;

    public RequestHandler(Object object, Method method) {
        this.object = object;
        this.method = method;
        this.converters = new ArrayList<>();

        init();
    }

    private void init() {
        Class[] parameterClasses = method.getParameterTypes();

        for (int i = 0; i < parameterClasses.length; i++) {
            Class parameterClass = parameterClasses[i];
            Annotation[] parameterAnnotations = method.getParameterAnnotations()[i];

            if (parameterAnnotations.length == 0) {
                if (parameterClass == CustomHttpRequest.class) {
                    converters.add((request -> request));
                } else {
                    throw new RuntimeException("unexpected parameter");
                }
            } else {
                Annotation parameterAnnotation = parameterAnnotations[0];
                if (parameterAnnotation instanceof PathElement) {
                    String name = ((PathElement) parameterAnnotation).name();
                    converters.add((request) -> request.getPathElement(name));
                } else if (parameterAnnotation instanceof QueryParameter) {
                    QueryParameter queryParameter = ((QueryParameter) parameterAnnotation);
                    converters.add((request) -> request.getQueryParameterOrDefault(queryParameter.name(), queryParameter.defaultValue()));
                } else if (parameterAnnotation instanceof RequestBody) {
                    converters.add(CustomHttpRequest::getBody);
                } else {
                    throw new RuntimeException("unexpected parameter");
                }
            }
        }
    }

    private Object[] buildParams(CustomHttpRequest httpRequest) {
        Object[] params = new Object[converters.size()];

        for (int i = 0; i < converters.size(); i++) {
            params[i] = converters.get(i).convert(httpRequest);
        }

        return params;
    }

    public void invoke(CustomHttpRequest httpRequest) {
        try {
            method.invoke(object, buildParams(httpRequest));
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @FunctionalInterface
    interface Converter {
        Object convert(CustomHttpRequest request);
    }
}