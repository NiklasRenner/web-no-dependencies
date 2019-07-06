package id.renner.web.http;

import com.sun.net.httpserver.HttpServer;
import id.renner.web.endpoint.Controller;
import id.renner.web.endpoint.Endpoint;
import id.renner.web.util.AnnotationUtils;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getSimpleName());

    private final Map<String, MethodExecutor> router;
    private final HttpServer httpServer;
    private final ExecutorService executorService;

    public Server(int port) {
        try {
            this.router = new HashMap<>();
            this.httpServer = HttpServer.create(new InetSocketAddress(port), 10);

            this.executorService = Executors.newFixedThreadPool(10);
            this.httpServer.setExecutor(executorService);

            this.httpServer
                    .createContext("/")
                    .setHandler(new PathRequestHandler(this.router));
        } catch (Exception ex) {
            throw new RuntimeException("couldn't start http server: " + ex.getMessage(), ex);
        }
    }

    public void createRoutes(Object controller) {
        // get reflection data
        Class controllerClass = controller.getClass();
        Controller controllerAnnotation = AnnotationUtils.getAnnotation(controllerClass, Controller.class);

        // setup route bindings
        router.putAll(Arrays.stream(controllerClass.getMethods())
                .filter((Method method) -> AnnotationUtils.hasAnnotation(method, Endpoint.class))
                .map((Method method) -> createBinding(controller, method, controllerAnnotation.path()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    private Map.Entry<String, MethodExecutor> createBinding(Object instance, Method method, String prefix) {
        Endpoint endpointAnnotation = AnnotationUtils.getAnnotation(method, Endpoint.class);
        return new AbstractMap.SimpleEntry<>(prefix + endpointAnnotation.path(), new MethodExecutor(instance, method));
    }

    public boolean hasEndpoints() {
        return !router.isEmpty();
    }

    public void start() {
        httpServer.start();
    }

    public void close() {
        httpServer.stop(1);
        executorService.shutdownNow();
    }
}