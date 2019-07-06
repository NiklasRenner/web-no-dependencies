package id.renner.web.library.http;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomHttpServer {
    private final HttpServer httpServer;
    private final ExecutorService executorService;

    public CustomHttpServer(Map<String, InstanceMethod> routes, int port) {
        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(port), 10);

            this.executorService = Executors.newFixedThreadPool(10);
            this.httpServer.setExecutor(executorService);

            this.httpServer
                    .createContext("/")
                    .setHandler(new CustomHttpHandler(routes));
        } catch (Exception ex) {
            throw new RuntimeException("couldn't start http server: " + ex.getMessage(), ex);
        }
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
        executorService.shutdownNow();
    }
}