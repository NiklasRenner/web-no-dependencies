package id.renner.web.library.request;

import id.renner.web.library.controller.ControllerMappingException;
import id.renner.web.library.controller.PathElement;
import id.renner.web.library.controller.QueryParameter;
import id.renner.web.library.controller.RequestBody;
import id.renner.web.library.http.HttpStatus;
import id.renner.web.library.response.ResponseEntity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RequestExecutor {
    private final Object object;
    private final Method method;
    private final List<ParameterConverter> parameterParameterConverters;
    private ResponseConverter responseConverter;

    public RequestExecutor(Object object, Method method) {
        this.object = object;
        this.method = method;
        this.parameterParameterConverters = new ArrayList<>();

        init();
    }

    private void init() {
        Class[] parameterClasses = method.getParameterTypes();
        for (int i = 0; i < parameterClasses.length; i++) {
            Class parameterClass = parameterClasses[i];
            Annotation[] parameterAnnotations = method.getParameterAnnotations()[i];
            if (parameterAnnotations.length == 0) {
                if (parameterClass == SimpleHttpRequest.class) {
                    parameterParameterConverters.add((request -> request));
                } else {
                    throw new ControllerMappingException("unexpected parameter-type [" + parameterClass.getSimpleName() + "] for controller method");
                }
            } else {
                Annotation parameterAnnotation = parameterAnnotations[0]; // only handle first annotation for now, maybe expand later with more complex functionality if needed.
                if (parameterAnnotation instanceof PathElement) {
                    String name = ((PathElement) parameterAnnotation).name();
                    parameterParameterConverters.add((request) -> request.getPathElement(name));
                } else if (parameterAnnotation instanceof QueryParameter) {
                    QueryParameter queryParameter = ((QueryParameter) parameterAnnotation);
                    parameterParameterConverters.add((request) -> request.getQueryParameterOrDefault(queryParameter.name(), queryParameter.defaultValue()));
                } else if (parameterAnnotation instanceof RequestBody) {
                    parameterParameterConverters.add(SimpleHttpRequest::getBody);
                } else {
                    throw new ControllerMappingException("unexpected parameter-type [" + parameterClass.getSimpleName() + "] for controller method");
                }
            }
        }

        Class responseClass = method.getReturnType();
        if (responseClass == ResponseEntity.class) {
            responseConverter = (input) -> (ResponseEntity) input;
        } else if (responseClass == Void.TYPE) {
            responseConverter = (input -> new ResponseEntity("", HttpStatus.OK));
        } else {
            throw new ControllerMappingException("unexpected return-type [" + responseClass.getSimpleName() + "] for controller method");
        }
    }

    private Object[] buildParams(SimpleHttpRequest request) {
        Object[] params = new Object[parameterParameterConverters.size()];

        for (int i = 0; i < parameterParameterConverters.size(); i++) {
            params[i] = parameterParameterConverters.get(i).convert(request);
        }

        return params;
    }

    public ResponseEntity invoke(SimpleHttpRequest request) {
        try {
            Object responseHolder = method.invoke(object, buildParams(request));
            return responseConverter.convert(responseHolder);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @FunctionalInterface
    interface ParameterConverter {
        Object convert(SimpleHttpRequest request);
    }

    @FunctionalInterface
    interface ResponseConverter {
        ResponseEntity convert(Object input);
    }
}