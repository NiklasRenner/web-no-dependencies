package id.renner.web;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import id.renner.web.injection.Application;
import id.renner.web.injection.ApplicationContext;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@Application
public class WebApplication {
    private static final Logger logger = Logger.getLogger(WebApplication.class.getSimpleName());

    public static void main(String[] args) throws IOException {
        new ApplicationContext(WebApplication.class);

        HttpServer server = HttpServerProvider.provider().createHttpServer(new InetSocketAddress("localhost", 8080), 10);
        HttpContext context = server.createContext("/test");
        context.setHandler((HttpExchange exchange) -> {
            logger.info("handling request: " + exchange.getRemoteAddress());
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exchange.getResponseBody()))) {
                String message = "response from application " + LocalDateTime.now();

                // send headers then body
                exchange.sendResponseHeaders(200, message.length());
                writer.append(message);
            }
            logger.info("handled request" + exchange.getRemoteAddress());
        });
        server.start();
    }
}