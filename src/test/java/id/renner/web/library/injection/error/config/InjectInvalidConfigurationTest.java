package id.renner.web.library.injection.error.config;

import id.renner.web.library.application.Application;
import id.renner.web.library.injection.InjectionException;
import id.renner.web.util.TestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Application
class InjectInvalidConfigurationTest {

    @Test
    void testDependencyInjectionFails() { // all constructor parameters for classes marked with @Inject, also need to be marked with @Inject themselves
        assertThrows(InjectionException.class, () -> TestUtil.createApplicationCore(InjectInvalidConfigurationTest.class));
    }
}