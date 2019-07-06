package id.renner.web.library.injection.sunshine;

import id.renner.web.library.application.ApplicationCore;
import id.renner.web.library.application.ContextHandler;
import id.renner.web.library.injection.Application;
import id.renner.web.util.TestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Application
class InjectSunshineTest {

    @Test
    void testDependencyInjectionSunshine() {
        ApplicationCore applicationCore = TestUtil.createExecutionCore(InjectSunshineTest.class);
        ContextHandler contextHandler = applicationCore.getContextHandler();

        assertNull(contextHandler.getInstance(ApplicationCore.class));
        assertNotNull(contextHandler.getInstance(Foo.class));
        assertNotNull(contextHandler.getInstance(Bar.class));
        assertEquals(contextHandler.getInstance(Foo.class), contextHandler.getInstance(Foo.class));
        assertEquals(contextHandler.getInstance(Bar.class), contextHandler.getInstance(Foo.class).getBar());

        applicationCore.stop();
    }
}