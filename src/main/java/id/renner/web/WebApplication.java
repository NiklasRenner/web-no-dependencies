package id.renner.web;

import id.renner.web.injection.Application;
import id.renner.web.injection.ApplicationContext;

@Application
public class WebApplication {

    public static void main(String[] args) {
        new ApplicationContext(WebApplication.class);
    }
}