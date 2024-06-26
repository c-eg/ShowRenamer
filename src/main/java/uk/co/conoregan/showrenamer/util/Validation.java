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

import java.util.List;

/**
 * Class for validation.
 */
public class Validation {
    /**
     * Checks if a string is valid: not null and not blank.
     *
     * @param string the string to check.
     * @return true if valid, false if not.
     */
    public static boolean isStringValid(final String string) {
        return string != null && !string.isBlank();
    }

    /**
     * Checks if all strings are valid: not null and not blank.
     *
     * @param strings the strings to check.
     * @return true if all valid, false if not.
     */
    public static boolean isStringVarargsValid(final String... strings) {
        if (strings == null || strings.length == 0) {
            return false;
        }

        for (final String string : strings) {
            if (!isStringValid(string)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if a list of strings are valid: not null and not empty.
     *
     * @param strings the strings to check.
     * @return true if valid, false if not.
     */
    public static boolean isStringListValid(final List<String> strings) {
        if (strings == null || strings.isEmpty()) {
            return false;
        }

        for (final String integer : strings) {
            if (!isStringValid(integer)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if an integer is valid: not null and greater than 0.
     *
     * @param integer the integer to check.
     * @return true if valid, false if not.
     */
    public static boolean isIntegerValid(final Integer integer) {
        return integer != null && integer > 0;
    }

    /**
     * Checks if all integers are valid: not null and greater than 0.
     *
     * @param integers the integers to check.
     * @return true if valid, false if not.
     */
    public static boolean isIntegerVarargsValid(final Integer... integers) {
        if (integers == null || integers.length == 0) {
            return false;
        }

        for (final Integer integer : integers) {
            if (!isIntegerValid(integer)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if a list of integers are valid: not null and not empty.
     *
     * @param integers the integers to check.
     * @return true if valid, false if not.
     */
    public static boolean isIntegerListValid(final List<Integer> integers) {
        if (integers == null || integers.isEmpty()) {
            return false;
        }

        for (final Integer integer : integers) {
            if (!isIntegerValid(integer)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if a list of generic objects are valid: not null and not empty.
     *
     * @param genericObjects the generic objects to check.
     * @return true if valid, false if not.
     */
    public static <T> boolean isGenericListValid(final List<T> genericObjects) {
        if (genericObjects == null || genericObjects.isEmpty()) {
            return false;
        }

        for (final T item : genericObjects) {
            if (item == null) {
                return false;
            }
        }

        return true;
    }
}
