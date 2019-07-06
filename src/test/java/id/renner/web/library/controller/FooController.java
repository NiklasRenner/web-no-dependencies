package id.renner.web.library.controller;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

@Controller(path = "/foo")
public class FooController {

    @Endpoint(path = "/bar")
    public void bar(HttpExchange httpExchange) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(httpExchange.getResponseBody()))) {
            String message = "baz";
            httpExchange.sendResponseHeaders(200, message.length());
            writer.append(message);
        }
    }
}