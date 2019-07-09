package id.renner.web.library.controller.sunshine;

import id.renner.web.library.controller.Controller;
import id.renner.web.library.controller.Endpoint;
import id.renner.web.library.controller.PathElement;
import id.renner.web.library.controller.QueryParameter;
import id.renner.web.library.controller.RequestBody;
import id.renner.web.library.http.HttpMethod;
import id.renner.web.library.http.HttpStatus;
import id.renner.web.library.request.SimpleHttpRequest;
import id.renner.web.library.response.ResponseEntity;

@Controller(path = "/foo")
public class FooController {
    private final Foo foo;

    public FooController(Foo foo) {
        this.foo = foo;
    }

    @Endpoint(path = "/bar")
    public void bar(SimpleHttpRequest request) {
        request.sendResponse(foo.foo(), HttpStatus.OK);
    }

    @Endpoint(path = "/baz/{id}", method = HttpMethod.POST)
    public ResponseEntity baz(@RequestBody String body, @PathElement(name = "id") String id, @QueryParameter(name = "times", defaultValue = "1") String times) {
        return ResponseEntity.of(id + body.repeat(Integer.parseInt(times)), HttpStatus.OK);
    }
}