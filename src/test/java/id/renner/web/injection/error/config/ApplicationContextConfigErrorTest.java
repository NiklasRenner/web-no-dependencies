package id.renner.web.injection.error.config;

import id.renner.web.WebApplication;
import id.renner.web.application.ApplicationContext;
import id.renner.web.injection.Application;
import id.renner.web.injection.InjectionException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Application
class ApplicationContextConfigErrorTest {

    //@Test
    void testDependencyInjectionFails() { // all constructor parameters for classes marked with @Inject, also need to be marked with @Inject themselves
        assertThrows(InjectionException.class, () -> new ApplicationContext(WebApplication.class));
    }
}