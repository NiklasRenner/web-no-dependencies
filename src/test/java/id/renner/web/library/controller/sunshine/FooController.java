package id.renner.web.library.controller.sunshine;

import id.renner.web.library.controller.Controller;
import id.renner.web.library.controller.Endpoint;
import id.renner.web.library.http.HttpStatus;
import id.renner.web.library.response.ResponseEntity;

@Controller(path = "/foo")
public class FooController {
    private final Foo foo;

    public FooController(Foo foo) {
        this.foo = foo;
    }

    @Endpoint(path = "/bar")
    public ResponseEntity bar() {
        return ResponseEntity.of(foo.foo(), HttpStatus.OK);
    }
}