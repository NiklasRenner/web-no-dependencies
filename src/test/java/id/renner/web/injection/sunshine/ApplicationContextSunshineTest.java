package id.renner.web.injection.sunshine;

import id.renner.web.WebApplication;
import id.renner.web.injection.Application;
import id.renner.web.injection.ApplicationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Application(basePackage = "id.renner.web.injection.sunshine")
class ApplicationContextSunshineTest {

    @Test
    void testDependencyInjectionIsDone() {
        ApplicationContext context = new ApplicationContext(ApplicationContextSunshineTest.class);

        assertNull(context.getInstance(ApplicationContext.class));
        assertNotNull(context.getInstance(Foo.class));
        assertNotNull(context.getInstance(Bar.class));
        assertEquals(context.getInstance(Foo.class), context.getInstance(Foo.class));
        assertEquals(context.getInstance(Bar.class), context.getInstance(Foo.class).getBar());
    }
}