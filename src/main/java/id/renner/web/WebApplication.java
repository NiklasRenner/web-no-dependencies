package id.renner.web;

import id.renner.web.injection.Application;
import id.renner.web.injection.ApplicationContext;

@Application(basePackage = "id.renner.web")
public class WebApplication {

    public static void main(String[] args) {
        new ApplicationContext(WebApplication.class);
    }
}