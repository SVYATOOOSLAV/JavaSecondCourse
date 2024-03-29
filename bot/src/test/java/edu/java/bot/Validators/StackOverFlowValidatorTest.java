package edu.java.bot.Validators;

import edu.java.bot.linkvalidators.baseValidators.StackOverflowValidator;
import org.junit.jupiter.api.Test;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StackOverFlowValidatorTest {
    @Test
    public void simpleTest() {
        StackOverflowValidator validator = new StackOverflowValidator();
        URI validTestCase = URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c");
        URI invalidTestCase = URI.create("https://github.com/");
        assertTrue(validator.isValidLink(validTestCase));
        assertFalse(validator.isValidLink(invalidTestCase));
    }
}
