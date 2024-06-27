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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Validation}.
 */
public class ValidationTest {
    @Test
    public void testIsStringValid() {
        final String testCase = "test";
        final boolean isValid = Validation.isStringValid(testCase);
        Assertions.assertTrue(isValid);
    }

    @Test
    public void testIsStringValidNotValidBlank() {
        final String testCase = " ";
        final boolean isValid = Validation.isStringValid(testCase);
        Assertions.assertFalse(isValid);
    }

    @Test
    public void testIsStringVarargsValid() {
        final String testCase1 = "test1";
        final String testCase2 = "test2";
        final boolean isValid = Validation.isStringVarargsValid(testCase1, testCase2);
        Assertions.assertTrue(isValid);
    }

    @Test
    public void testIsStringVarargsValidNotValid() {
        final boolean isValid = Validation.isStringVarargsValid();
        Assertions.assertFalse(isValid);
    }

    @Test
    public void testIsStringVarargsValidNotValidNull() {
        final boolean isValid = Validation.isStringVarargsValid(null, null);
        Assertions.assertFalse(isValid);
    }

    @Test
    public void testIsStringListValid() {
        final String testCase1 = "test1";
        final String testCase2 = "test2";
        final List<String> list = new ArrayList<>();
        list.add(testCase1);
        list.add(testCase2);
        final boolean isValid = Validation.isStringListValid(list);
        Assertions.assertTrue(isValid);
    }

    @Test
    public void testIsStringListValidNotValidEmpty() {
        final List<String> list = new ArrayList<>();
        final boolean isValid = Validation.isStringListValid(list);
        Assertions.assertFalse(isValid);
    }

    @Test
    public void testIsStringListValidNotValidNull() {
        final List<String> list = new ArrayList<>();
        list.add(null);
        final boolean isValid = Validation.isStringListValid(list);
        Assertions.assertFalse(isValid);
    }

    @Test
    public void testIsIntegerValid() {
        final int testCase = 1;
        final boolean isValid = Validation.isIntegerValid(testCase);
        Assertions.assertTrue(isValid);
    }

    @Test
    public void testIsIntegerValidNotValidZero() {
        final int testCase = 0;
        final boolean isValid = Validation.isIntegerValid(testCase);
        Assertions.assertFalse(isValid);
    }

    @Test
    public void testIsIntegerValidNotValidNull() {
        final Integer testCase = null;
        final boolean isValid = Validation.isIntegerValid(testCase);
        Assertions.assertFalse(isValid);
    }

    @Test
    public void testIsIntegerVarargsValid() {
        final int testCase1 = 1;
        final int testCase2 = 2;
        final boolean isValid = Validation.isIntegerVarargsValid(testCase1, testCase2);
        Assertions.assertTrue(isValid);
    }

    @Test
    public void testIsIntegerVarargsValidNotValidNull() {
        final boolean isValid = Validation.isIntegerVarargsValid(null, null);
        Assertions.assertFalse(isValid);
    }

    @Test
    public void testIsIntegerVarargsValidNotValid() {
        final int testCase1 = 1;
        final int testCase2 = 0;
        final boolean isValid = Validation.isIntegerVarargsValid(testCase1, testCase2);
        Assertions.assertFalse(isValid);
    }

    @Test
    public void testIsIntegerListValid() {
        final List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        final boolean isValid = Validation.isIntegerListValid(list);
        Assertions.assertTrue(isValid);
    }

    @Test
    public void testIsIntegerListValidNotValidEmpty() {
        final List<Integer> list = new ArrayList<>();
        final boolean isValid = Validation.isIntegerListValid(list);
        Assertions.assertFalse(isValid);
    }

    @Test
    public void testIsIntegerListValidNotValidNull() {
        final List<Integer> list = new ArrayList<>();
        list.add(null);
        final boolean isValid = Validation.isIntegerListValid(list);
        Assertions.assertFalse(isValid);
    }

    @Test
    public void testIsGenericListValid() {
        final List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        final boolean isValid = Validation.isGenericListValid(list);
        Assertions.assertTrue(isValid);
    }

    @Test
    public void testIsGenericListValidNotValidEmpty() {
        final List<Integer> list = new ArrayList<>();
        final boolean isValid = Validation.isGenericListValid(list);
        Assertions.assertFalse(isValid);
    }

    @Test
    public void testIsGenericListValidNotValidNull() {
        final List<Integer> list = new ArrayList<>();
        list.add(null);
        final boolean isValid = Validation.isGenericListValid(list);
        Assertions.assertFalse(isValid);
    }
}
