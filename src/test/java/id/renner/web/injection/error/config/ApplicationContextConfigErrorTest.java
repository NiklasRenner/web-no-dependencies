package id.renner.web.injection.error.config;

import id.renner.web.WebApplication;
import id.renner.web.injection.Application;
import id.renner.web.injection.ApplicationContext;
import id.renner.web.injection.InjectionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Application(basePackage = "id.renner.web.injection.error.config")
class ApplicationContextConfigErrorTest {

    @Test
    void testDependencyInjectionFails() { // all constructor parameters for classes marked with @Inject, also need to be marked with @Inject themselves
        assertThrows(InjectionException.class, () -> new ApplicationContext(WebApplication.class));
    }
}