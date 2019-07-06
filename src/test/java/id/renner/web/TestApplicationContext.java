package id.renner.web;

import id.renner.web.application.ApplicationContext;

import java.util.concurrent.atomic.AtomicInteger;

public class TestApplicationContext {
    private static AtomicInteger counter = new AtomicInteger(10000);

    public static ApplicationContext createContext(Class clazz){
        return new ApplicationContext(clazz, counter.incrementAndGet());
    }
}