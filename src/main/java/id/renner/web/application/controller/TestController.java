package id.renner.web.application.controller;

import id.renner.web.application.service.TestService;
import id.renner.web.library.controller.Controller;
import id.renner.web.library.controller.Endpoint;
import id.renner.web.library.http.CustomHttpRequest;

@Controller(path = "/test")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @Endpoint(path = "/get")
    public void test(CustomHttpRequest httpContext) {
        httpContext.sendResponse("abstraction is key - " + testService.getTimeString(), 200);
    }

    @Endpoint(path = "/echo/{message}")
    public void echo(CustomHttpRequest httpContext) {
        httpContext.sendResponse(httpContext.getPathElement("message"), 200);
    }

    @Endpoint(path = "/wait")
    public void wait(CustomHttpRequest httpContext) throws Exception {
        Thread.sleep(5000);
        httpContext.sendResponse("returned", 200);
    }
}