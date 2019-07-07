package id.renner.web.library.injection.error.circular;

import id.renner.web.library.injection.Application;
import id.renner.web.util.TestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Application
public class InjectCircularDependencyTest {

    @Test
    void testInjectionFailsWithCircularDependencies() {
        assertThrows(StackOverflowError.class, () -> TestUtil.createExecutionCore(InjectCircularDependencyTest.class));
    }
}