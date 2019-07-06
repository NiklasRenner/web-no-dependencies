package id.renner.web.endpoint;

import id.renner.web.application.ApplicationContext;
import id.renner.web.injection.Application;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Application
class ControllerInjectionTest {
    private ApplicationContext context = new ApplicationContext(ControllerInjectionTest.class);

    //@Test
    void testControllerAnnotationInjectsToo() {
        assertNotNull(context.getInstance(FooController.class));
    }
}