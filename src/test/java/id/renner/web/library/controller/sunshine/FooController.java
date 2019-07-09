package id.renner.web.library.controller.sunshine;

import id.renner.web.library.controller.Controller;
import id.renner.web.library.controller.Endpoint;
import id.renner.web.library.http.CustomHttpRequest;

@Controller(path = "/foo")
public class FooController {
    private final Foo foo;

    public FooController(Foo foo) {
        this.foo = foo;
    }

    @Endpoint(path = "/bar")
    public void bar(CustomHttpRequest request) {
        request.sendResponse(foo.foo(), 200);
    }
}