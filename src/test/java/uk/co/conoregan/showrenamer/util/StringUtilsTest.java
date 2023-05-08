package uk.co.conoregan.showrenamer.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StringUtils}.
 */
public class StringUtilsTest {
    @Test
    public void testReplaceLastOccurrence() {
        final String original = "TestTest";
        final String toReplace = "Test";
        final String replacement = "Replaced";

        final String result = StringUtils.replaceLastOccurrence(original, toReplace, replacement);

        Assertions.assertEquals("TestReplaced", result);
    }

    @Test
    public void testReplaceLastOccurrenceToReplaceNotExisting() {
        final String original = "TestTest";
        final String toReplace = "Not Existing";
        final String replacement = "Replaced";

        final String result = StringUtils.replaceLastOccurrence(original, toReplace, replacement);

        Assertions.assertEquals("TestTest", result);
    }
}
