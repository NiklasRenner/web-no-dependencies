package id.renner.web.library.controller.sunshine;

import id.renner.web.library.application.Application;
import id.renner.web.library.application.ApplicationCore;
import id.renner.web.util.TestUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Application
class ControllerSunshineTest {
    private ApplicationCore applicationCore = TestUtil.createApplicationCore(ControllerSunshineTest.class);

    @Test
    void testControllersAreInjected() {
        assertNotNull(applicationCore.getContextHandler().getInstance(FooController.class));
    }

    @Test
    void testControllerManualResponseSunshine() throws IOException, InterruptedException {
        int port = applicationCore.getControllerHandler().getPort();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/foo/bar"))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(httpResponse.statusCode(), 200);
        assertEquals(httpResponse.body(), "foo bar baz baz");
    }

    @Test
    void testControllerResponseFromEntitySunshine() throws IOException, InterruptedException {
        String message = "message";
        String id = "abc123";
        int times = 5;

        int port = applicationCore.getControllerHandler().getPort();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/foo/baz/" + id + "?times=" + times))
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(httpResponse.statusCode(), 200);
        assertEquals(httpResponse.body(), id + message.repeat(5));
    }
}