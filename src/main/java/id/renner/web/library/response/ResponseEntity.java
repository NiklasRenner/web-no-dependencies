package id.renner.web.library.response;

import id.renner.web.library.http.HttpStatus;

public class ResponseEntity {
    private final String response;
    private final HttpStatus responseCode;

    public ResponseEntity(String response, HttpStatus responseCode) {
        this.response = response;
        this.responseCode = responseCode;
    }

    public String getResponse() {
        return response;
    }

    public HttpStatus getResponseCode() {
        return responseCode;
    }

    public static ResponseEntity of(String response, HttpStatus responseCode) {
        return new ResponseEntity(response, responseCode);
    }
}