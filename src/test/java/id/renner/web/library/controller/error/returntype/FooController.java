package id.renner.web.library.controller.error.returntype;

import id.renner.web.library.controller.Controller;
import id.renner.web.library.controller.Endpoint;
import id.renner.web.library.controller.sunshine.Foo;

import java.util.Collections;
import java.util.List;

@Controller(path = "/foo")
public class FooController {
    private final Foo foo;

    public FooController(Foo foo) {
        this.foo = foo;
    }

    @Endpoint(path = "/bar")
    public List<String> bar() {
        return Collections.emptyList();
    }
}