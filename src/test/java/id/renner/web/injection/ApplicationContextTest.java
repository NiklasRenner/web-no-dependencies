package id.renner.web.injection;

import id.renner.web.WebApplication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ApplicationContextTest {

    @Test
    void testDependencyInjectionIsDone() {
        ApplicationContext context = new ApplicationContext(WebApplication.class);

        assertNull(context.getInstance(ApplicationContext.class));
        assertNotNull(context.getInstance(First.class));
        assertNotNull(context.getInstance(Second.class));
        assertEquals(context.getInstance(First.class), context.getInstance(First.class));
        assertEquals(context.getInstance(Second.class), context.getInstance(First.class).getSecond());
    }
}