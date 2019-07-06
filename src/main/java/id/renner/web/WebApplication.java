package id.renner.web;

import id.renner.web.application.ApplicationContext;
import id.renner.web.injection.Application;

@Application
public class WebApplication {

    public static void main(String[] args){
        new ApplicationContext(WebApplication.class);
    }
}