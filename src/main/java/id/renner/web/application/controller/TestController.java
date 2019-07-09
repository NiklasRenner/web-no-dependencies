package id.renner.web.application.controller;

import id.renner.web.application.service.TestService;
import id.renner.web.library.controller.Controller;
import id.renner.web.library.controller.Endpoint;
import id.renner.web.library.http.CustomHttpContext;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

@Controller(path = "/test")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @Endpoint(path = "/get")
    public void test(CustomHttpContext httpContext) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(httpContext.getExchange().getResponseBody()))) {
            String message = "abstraction is key - " + testService.getTimeString();

            // send headers then body
            httpContext.getExchange().sendResponseHeaders(200, message.length());
            writer.append(message);
        }
    }

    @Endpoint(path = "/echo/{message}")
    public void echo(CustomHttpContext httpContext) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(httpContext.getExchange().getResponseBody()))) {
            String message = httpContext.getPathElement("message");

            // send headers then body
            httpContext.getExchange().sendResponseHeaders(200, message.length());
            writer.append(message);
        }
    }

    @Endpoint(path = "/wait")
    public void wait(CustomHttpContext httpContext) throws Exception {
        Thread.sleep(5000);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(httpContext.getExchange().getResponseBody()))) {
            String message = "returned";

            // send headers then body
            httpContext.getExchange().sendResponseHeaders(200, message.length());
            writer.append(message);
        }
    }
}