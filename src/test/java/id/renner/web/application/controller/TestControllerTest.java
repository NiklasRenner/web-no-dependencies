package id.renner.web.application.controller;

import id.renner.web.application.WebApplication;
import id.renner.web.library.application.ApplicationCore;
import id.renner.web.library.http.HttpStatus;
import id.renner.web.util.TestUtil;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestControllerTest {

    @Test
    public void test() throws Exception {
        ApplicationCore applicationCore = TestUtil.createApplicationCore(WebApplication.class);

        int port = applicationCore.getControllerHandler().getPort();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/test/echo/message"))
                .build();

        HttpResponse httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(httpResponse.statusCode(), HttpStatus.OK.value());
        assertEquals(httpResponse.body().toString(), "message");
    }
}