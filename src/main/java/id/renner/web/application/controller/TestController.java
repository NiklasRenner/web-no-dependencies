package id.renner.web.application.controller;

import com.sun.net.httpserver.HttpExchange;
import id.renner.web.application.service.TestService;
import id.renner.web.library.controller.Controller;
import id.renner.web.library.controller.Endpoint;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

@Controller(path = "/test")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @Endpoint(path = "/get")
    public void test(HttpExchange httpExchange) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(httpExchange.getResponseBody()))) {
            String message = "abstraction is key - " + testService.getTimeString();

            // send headers then body
            httpExchange.sendResponseHeaders(200, message.length());
            writer.append(message);
        }
    }
}