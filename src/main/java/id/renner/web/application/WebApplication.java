package id.renner.web.application;

import id.renner.web.library.application.Application;
import id.renner.web.library.application.ApplicationCore;

@Application
public class WebApplication {

    public static void main(String[] args) {
        new ApplicationCore(WebApplication.class);
    }
}