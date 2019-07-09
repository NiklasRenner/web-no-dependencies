package id.renner.web.library.http;

public enum HttpMethod {
    GET("GET"),
    PUT("PUT"),
    POST("POST"),
    DELETE("DELETE");

    private final String name;

    HttpMethod(String name) {
        this.name = name;
    }

    public static HttpMethod fromString(String name) {
        for (HttpMethod value : values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }

        return null;
    }
}
