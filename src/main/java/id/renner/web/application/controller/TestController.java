package id.renner.web.application.controller;

import id.renner.web.application.service.TestService;
import id.renner.web.library.controller.Controller;
import id.renner.web.library.controller.Endpoint;
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
    public void echo(CustomHttpRequest request) {
        request.sendResponse(request.getPathElement("message"), 200);
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