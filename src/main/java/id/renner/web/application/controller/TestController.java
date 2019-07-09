package id.renner.web.application.controller;

import id.renner.web.application.service.TestService;
import id.renner.web.library.controller.Controller;
import id.renner.web.library.controller.Endpoint;
import id.renner.web.library.controller.PathElement;
import id.renner.web.library.controller.QueryParameter;
import id.renner.web.library.controller.RequestBody;
import id.renner.web.library.http.CustomHttpRequest;
import id.renner.web.library.http.HttpMethod;

@Controller(path = "/test")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @Endpoint(path = "/get")
    public void test(CustomHttpRequest request) {
        request.sendResponse("abstraction is key - " + testService.getTimeString(), 200);
    }

    @Endpoint(path = "/echo/{message}")
    public void echo(CustomHttpRequest request, @PathElement(name = "message") String message, @QueryParameter(name = "times", defaultValue = "1") String times) {
        request.sendResponse(message.repeat(Integer.parseInt(times)), 200);
    }

    @Endpoint(path = "/echo", method = HttpMethod.POST)
    public void echoPost(CustomHttpRequest request, @RequestBody String body) {
        request.sendResponse(body, 200);
    }

    @Endpoint(path = "/methods", method = HttpMethod.PUT)
    public void put(CustomHttpRequest request) {
        request.sendResponse("put", 200);
    }

    @Endpoint(path = "/methods")
    public void get(CustomHttpRequest request) {
        request.sendResponse("get", 200);
    }
}