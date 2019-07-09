package id.renner.web.application.controller;

import id.renner.web.application.service.TestService;
import id.renner.web.library.controller.Controller;
import id.renner.web.library.controller.Endpoint;
import id.renner.web.library.controller.PathElement;
import id.renner.web.library.controller.QueryParameter;
import id.renner.web.library.controller.RequestBody;
import id.renner.web.library.http.HttpMethod;
import id.renner.web.library.http.HttpStatus;
import id.renner.web.library.response.ResponseEntity;

@Controller(path = "/test")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @Endpoint(path = "/get")
    public ResponseEntity test() {
        return ResponseEntity.of("abstraction is key - " + testService.getTimeString(), HttpStatus.OK);
    }

    @Endpoint(path = "/echo/{message}")
    public ResponseEntity echo(@PathElement(name = "message") String message, @QueryParameter(name = "times", defaultValue = "1") String times) {
        return ResponseEntity.of(message.repeat(Integer.parseInt(times)), HttpStatus.OK);
    }

    @Endpoint(path = "/echo", method = HttpMethod.POST)
    public ResponseEntity echoPost(@RequestBody String body) {
        return ResponseEntity.of(body, HttpStatus.OK);
    }
}