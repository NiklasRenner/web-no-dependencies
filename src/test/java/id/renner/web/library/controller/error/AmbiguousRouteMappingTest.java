package id.renner.web.library.controller.error;

import id.renner.web.library.application.Application;
import id.renner.web.library.injection.error.circular.InjectCircularDependencyTest;
import id.renner.web.util.TestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Application
public class AmbiguousRouteMappingTest {

    @Test
    public void test() {
        assertThrows(RuntimeException.class, () -> TestUtil.createApplicationCore(InjectCircularDependencyTest.class));
    }
}