package id.renner.web.library.injection.error.constructor;

import id.renner.web.library.injection.Application;
import id.renner.web.library.injection.InjectionException;
import id.renner.web.util.TestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Application
class InjectAmbiguousConstructorTest {

    @Test
    void testDependencyInjectionFails() { // multiple constructors in class marked with @Inject is not supported
        assertThrows(InjectionException.class, () -> TestUtil.createExecutionCore(InjectAmbiguousConstructorTest.class));
    }
}