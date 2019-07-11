package id.renner.web.library.application;

import id.renner.web.library.controller.Controller;
import id.renner.web.library.controller.Endpoint;
import id.renner.web.library.http.HttpRoutingServer;
import id.renner.web.library.json.ConversionHandler;
import id.renner.web.library.request.RequestExecutor;
import id.renner.web.library.routing.RequestRouter;
import id.renner.web.library.util.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class ControllerHandler {
    private static final Logger logger = Logger.getLogger(ControllerHandler.class.getSimpleName());

    private final int port;
    private HttpRoutingServer httpRoutingServer;
    private ConversionHandler conversionHandler = new ConversionHandler(true);

    public ControllerHandler(ContextHandler contextHandler, int port) {
        logger.info("starting controllerHandler");
        this.port = port;

        final RequestRouter requestRouter = new RequestRouter();
        contextHandler
                .getInjectedObjectContext().values().stream()
                .filter(this::isController)
                .map(this::extractRouteMappings)
                .flatMap(Function.identity())
                .forEach((entry) -> requestRouter.addRoute(entry.getKey(), entry.getValue()));

        if (!requestRouter.isEmpty()) {
            this.httpRoutingServer = new HttpRoutingServer(requestRouter, port);
            this.httpRoutingServer.start();
        }
    }

    private boolean isController(Object instance) {
        return AnnotationUtils.hasAnnotation(instance.getClass(), Controller.class);
    }

    private Stream<Map.Entry<String, RequestExecutor>> extractRouteMappings(Object controller) {
        Class controllerClass = controller.getClass();
        Controller controllerAnnotation = AnnotationUtils.getAnnotation(controllerClass, Controller.class);

        return Arrays.stream(controllerClass.getMethods())
                .filter((Method method) -> AnnotationUtils.hasAnnotation(method, Endpoint.class))
                .map((Method method) -> extractRouteMapping(controller, method, controllerAnnotation.path()));
    }

    private Map.Entry<String, RequestExecutor> extractRouteMapping(Object instance, Method method, String prefix) {
        Endpoint endpointAnnotation = AnnotationUtils.getAnnotation(method, Endpoint.class);
        String methodAndPathKey = endpointAnnotation.method().name() + prefix + endpointAnnotation.path();
        return new AbstractMap.SimpleEntry<>(methodAndPathKey, new RequestExecutor(instance, method));
    }

    public void stop() {
        if (httpRoutingServer != null) {
            httpRoutingServer.stop();
        }
    }

    public int getPort() {
        return port;
    }
}