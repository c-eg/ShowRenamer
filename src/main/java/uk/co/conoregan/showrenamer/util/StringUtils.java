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

import javax.annotation.Nonnull;

/**
 * Class containing String utility functions.
 */
public class StringUtils {
    /**
     * Replace last occurrence of toReplace in original, using the replacement.
     * If toReplace is not found in the original string, it will return the original string.
     *
     * @param original the original string.
     * @param toReplace substring of original to replace.
     * @param replacement the replacement string.
     * @return replaced string.
     */
    @Nonnull
    public static String replaceLastOccurrence(@Nonnull final String original, @Nonnull final String toReplace,
                                                @Nonnull final String replacement) {
        final int start = original.lastIndexOf(toReplace);

        if (start > -1) {
            return original.substring(0, start) + replacement + original.substring(start + toReplace.length());
        }

        return original;
    }
}
