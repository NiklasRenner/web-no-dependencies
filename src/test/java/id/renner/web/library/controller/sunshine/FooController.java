package id.renner.web.library.controller.sunshine;

import id.renner.web.library.controller.Controller;
import id.renner.web.library.controller.Endpoint;
import id.renner.web.library.http.CustomHttpContext;

import java.io.IOException;
import java.io.OutputStream;

@Controller(path = "/foo")
public class FooController {
    private final Foo foo;

    public FooController(Foo foo) {
        this.foo = foo;
    }

    @Endpoint(path = "/bar")
    public void bar(CustomHttpContext httpContext) throws IOException {
        try (OutputStream outputStream = httpContext.getExchange().getResponseBody()) {
            String message = foo.foo();
            httpContext.getExchange().sendResponseHeaders(200, message.length());
            outputStream.write(message.getBytes());
        }
    }
}