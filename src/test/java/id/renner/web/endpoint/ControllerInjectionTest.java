package id.renner.web.endpoint;

import id.renner.web.injection.Application;
import id.renner.web.injection.ApplicationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Application(basePackage = "id.renner.web.endpoint")
class ControllerInjectionTest {

    @Test
    void testControllerAnnotationInjectsToo() {
        ApplicationContext context = new ApplicationContext(ControllerInjectionTest.class);

        assertNotNull(context.getInstance(FooController.class));
    }
}