package id.renner.web.library.controller.error;

import id.renner.web.library.application.Application;
import id.renner.web.library.routing.RoutingException;
import id.renner.web.util.TestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Application
class AmbiguousRouteMappingTest {

    @Test
    void testAmbiguousRouteMappingFails() {
        assertThrows(RoutingException.class, () -> TestUtil.createApplicationCore(AmbiguousRouteMappingTest.class));
    }
}