package id.renner.web.library.controller.error;

import id.renner.web.library.controller.Controller;
import id.renner.web.library.controller.Endpoint;
import id.renner.web.library.controller.PathElement;
import id.renner.web.library.controller.sunshine.Foo;

@Controller(path = "/foo")
public class FooController {
    private final Foo foo;

    public FooController(Foo foo) {
        this.foo = foo;
    }

    @Endpoint(path = "/bar/{id}")
    public void bar(@PathElement(name = "id") String id) {
    }

    @Endpoint(path = "/bar/baz")
    public void baz() {
    }
}