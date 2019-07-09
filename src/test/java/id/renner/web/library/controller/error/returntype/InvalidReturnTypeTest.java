package id.renner.web.library.controller.error.returntype;

import id.renner.web.library.application.Application;
import id.renner.web.library.controller.ControllerMappingException;
import id.renner.web.util.TestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Application
class InvalidReturnTypeTest {

    @Test
    void testInvalidReturnTypeFails() {
        assertThrows(ControllerMappingException.class, () -> TestUtil.createApplicationCore(InvalidReturnTypeTest.class));
    }
}