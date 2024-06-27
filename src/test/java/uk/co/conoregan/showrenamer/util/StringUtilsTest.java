/*
 * This file is part of ShowRenamer.
 *
 * ShowRenamer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ShowRenamer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ShowRenamer.  If not, see <https://www.gnu.org/licenses/>.
 */

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
