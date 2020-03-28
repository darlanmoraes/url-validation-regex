package br.com.darlan.urlvalidation.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RegexValidatorTest {

    private RegexValidator validator = new RegexValidator();

    @Test
    public void givenInvalidRegexShouldReturnFalse() {
        assertFalse(validator.isValid("http://.***", null));
    }

    @Test
    public void givenValidRegexShouldReturnTrue() {
        assertTrue(validator.isValid("http://.*", null));
    }

}
