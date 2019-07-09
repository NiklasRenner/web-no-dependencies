package id.renner.web.library.controller;

import id.renner.web.library.http.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Endpoint {
    String path();

    HttpMethod method() default HttpMethod.GET;
}
