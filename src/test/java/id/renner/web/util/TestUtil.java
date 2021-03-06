package id.renner.web.util;

import id.renner.web.library.application.ApplicationCore;

import java.util.concurrent.atomic.AtomicInteger;

// same process can't reuse same port multiple times, so need new port for every new test
public class TestUtil {
    private static AtomicInteger counter = new AtomicInteger(10000);

    public static ApplicationCore createApplicationCore(Class clazz) {
        return new ApplicationCore(clazz, counter.incrementAndGet());
    }
}