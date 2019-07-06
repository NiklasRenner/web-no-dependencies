package id.renner.web.controller;

import com.sun.net.httpserver.HttpExchange;
import id.renner.web.endpoint.Controller;
import id.renner.web.endpoint.Endpoint;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@Controller(path = "/test")
public class TestController {
    private static final Logger logger = Logger.getLogger(TestController.class.getSimpleName());

    @Endpoint(path = "/get")
    public void test(HttpExchange httpExchange) throws Exception {
        logger.info("attempting to respond to exchange - " + httpExchange.getRemoteAddress());
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(httpExchange.getResponseBody()))) {
            String message = "abstraction is key - " + LocalDateTime.now();

            // send headers then body
            httpExchange.sendResponseHeaders(200, message.length());
            writer.append(message);
        }
        logger.info("responded to exchange - " + httpExchange.getRemoteAddress());
    }
}