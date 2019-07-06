package id.renner.web.library.application;

import id.renner.web.library.controller.Controller;
import id.renner.web.library.controller.Endpoint;
import id.renner.web.library.http.CustomHttpServer;
import id.renner.web.library.http.InstanceMethod;
import id.renner.web.library.util.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ControllerHandler {
    private static final Logger logger = Logger.getLogger(ControllerHandler.class.getSimpleName());

    private final int port;
    private CustomHttpServer customHttpServer;

    public ControllerHandler(ContextHandler contextHandler, int port) {
        logger.info("starting controllerHandler");
        this.port = port;

        Map<String, InstanceMethod> routes = contextHandler.getObjectInstances().values().stream()
                .filter(this::isController)
                .map(this::createRoutes)
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (!routes.isEmpty()) {
            this.customHttpServer = new CustomHttpServer(routes, port);
            customHttpServer.start();
        }
    }

    private boolean isController(Object instance) {
        return AnnotationUtils.hasAnnotation(instance.getClass(), Controller.class);
    }

    private Map<String, InstanceMethod> createRoutes(Object controller) {
        // get reflection data
        Class controllerClass = controller.getClass();
        Controller controllerAnnotation = AnnotationUtils.getAnnotation(controllerClass, Controller.class);

        // setup route bindings
        return Arrays.stream(controllerClass.getMethods())
                .filter((Method method) -> AnnotationUtils.hasAnnotation(method, Endpoint.class))
                .map((Method method) -> createBinding(controller, method, controllerAnnotation.path()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<String, InstanceMethod> createBinding(Object instance, Method method, String prefix) {
        Endpoint endpointAnnotation = AnnotationUtils.getAnnotation(method, Endpoint.class);
        return new AbstractMap.SimpleEntry<>(prefix + endpointAnnotation.path(), new InstanceMethod(instance, method));
    }

    public void stop() {
        if (customHttpServer != null) {
            customHttpServer.stop();
        }
    }

    public int getPort() {
        return port;
    }
}