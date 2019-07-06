package id.renner.web.injection.error.constructor;

import id.renner.web.TestApplicationContext;
import id.renner.web.injection.Application;
import id.renner.web.injection.InjectionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Application
class ApplicationContextConstructorErrorTest {

    @Test
    void testDependencyInjectionFails() { // multiple constructors in class marked with @Inject is not supported
        assertThrows(InjectionException.class, () -> TestApplicationContext.createContext(ApplicationContextConstructorErrorTest.class));
    }
}