package id.renner.web.controller;

import com.sun.net.httpserver.HttpExchange;
import id.renner.web.endpoint.Controller;
import id.renner.web.endpoint.Endpoint;
import id.renner.web.service.InfoService;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

@Controller(path = "/test")
public class TestController {
    private final InfoService infoService;

    public TestController(InfoService infoService) {
        this.infoService = infoService;
    }

    @Endpoint(path = "/get")
    public void test(HttpExchange httpExchange) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(httpExchange.getResponseBody()))) {
            String message = "abstraction is key - " + infoService.getInfo();

            // send headers then body
            httpExchange.sendResponseHeaders(200, message.length());
            writer.append(message);
        }
    }
}