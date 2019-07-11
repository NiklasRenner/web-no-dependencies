package id.renner.web.library.json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConversionHandlerTest {

    @Test
    void testConversion() {
        ConversionHandler conversionHandler = new ConversionHandler();

        Person person = new Person();
        person.setName("Niklas");
        person.setAge(25);

        conversionHandler.createIfMissingAndGet(Person.class, String.class);

        System.out.println(conversionHandler.convert(person, String.class));
        assertNotNull(conversionHandler.convert(person, String.class));
    }

}