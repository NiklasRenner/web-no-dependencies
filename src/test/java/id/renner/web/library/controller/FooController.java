package id.renner.web.library.controller;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

@Controller(path = "/foo")
public class FooController {
    private final Foo foo;

    public FooController(Foo foo) {
        this.foo = foo;
    }

    @Endpoint(path = "/bar")
    public void bar(HttpExchange httpExchange) throws IOException {
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            String message = foo.foo();
            httpExchange.sendResponseHeaders(200, message.length());
            outputStream.write(message.getBytes());
        }
    }
}