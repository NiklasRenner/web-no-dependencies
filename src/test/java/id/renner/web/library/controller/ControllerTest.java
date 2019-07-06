package id.renner.web.library.controller;

import id.renner.web.library.application.ApplicationCore;
import id.renner.web.library.injection.Application;
import id.renner.web.util.TestApplicationContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Application
class ControllerTest {
    private ApplicationCore applicationCore;

    @BeforeEach
    void before() {
        applicationCore = TestApplicationContext.createExecutionCore(ControllerTest.class);
    }

    @AfterEach
    void after() {
        applicationCore.stop();
    }

    @Test
    void testControllersAreInjected() {
        assertNotNull(applicationCore.getContextHandler().getInstance(FooController.class));
    }

    @Test
    void testControllerIsCallable() throws IOException, InterruptedException {
        int port = applicationCore.getControllerHandler().getPort();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/foo/bar"))
                .build();

        HttpResponse httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(httpResponse.statusCode(), 200);
        assertEquals(httpResponse.body().toString(), "baz");
    }
}