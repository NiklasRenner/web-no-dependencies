package id.renner.web.application;

import id.renner.web.library.application.ApplicationCore;
import id.renner.web.library.injection.Application;

@Application
public class WebApplication {

    public static void main(String[] args) {
        new ApplicationCore(WebApplication.class);
    }
}