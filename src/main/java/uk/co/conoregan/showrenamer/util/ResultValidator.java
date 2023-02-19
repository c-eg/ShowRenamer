package uk.co.conoregan.showrenamer.util;

import java.util.List;

public class ResultValidator {
    public static boolean isStringValid(final String string) {
        return string != null && !string.isBlank();
    }

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

    public static boolean isIntegerValid(final Integer integer) {
        return integer != null && integer > 0;
    }

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

    public static <T> boolean isGenericListValid(final List<T> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }

        for (final T item : list) {
            if (item == null) {
                return false;
            }
        }

        return true;
    }
}
