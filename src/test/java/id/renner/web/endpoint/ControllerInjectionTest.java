package id.renner.web.endpoint;

import id.renner.web.TestApplicationContext;
import id.renner.web.application.ApplicationContext;
import id.renner.web.injection.Application;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Application
class ControllerInjectionTest {

    @Test
    void testControllerAnnotationInjectsToo() {
        ApplicationContext context = TestApplicationContext.createContext(ControllerInjectionTest.class);

        assertNotNull(context.getInstance(FooController.class));

        context.close();
    }
}