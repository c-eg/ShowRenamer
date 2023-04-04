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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to match show information from the file name passed.
 */
public class ShowInfoMatcher {
    /**
     * Matches a show title from a file name.
     *
     * @param fileName the file name.
     * @return optional title.
     */
    public static Optional<String> matchTitle(final String fileName) {
        final String regex = "(.*?)(\\W| - )(directors(.?)cut|480p|720p|1080p|dvdrip|xvid|cd[0-9]|bluray|dvdscr|brrip|divx|S[0-9]{1,3}E[0-9]{1,3}|Season[\\s,0-9]{1,4}|[{(\\[]?[0-9]{4}).*";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            return matcher.group(1).replace(".", " ").trim().describeConstable();
        }

        return Optional.empty();
    }

    /**
     * Matches a show year from a file name.
     *
     * @param fileName the file name.
     * @return optional year.
     */
    public static Optional<String> matchYear(final String fileName) {
        final String regex = "[.\\s](?!^)[1,2]\\d{3}[.\\s]";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            return matcher.group(0).replace(".", "").trim().describeConstable();
        }

        return Optional.empty();
    }

    /**
     * Matches a show season from a file name.
     *
     * @param fileName the file name.
     * @return optional season number.
     */
    public static Optional<Integer> matchSeason(final String fileName) {
        final String regex = "s(?:eason)?\\s*(\\d{1,2})";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            return Optional.of(Integer.parseInt(matcher.group(1)));
        }

        return Optional.empty();
    }

    /**
     * Matches show episodes from a file name.
     *
     * @param fileName the file name.
     * @return list of episode numbers.
     */
    public static List<Integer> matchEpisodes(final String fileName) {
        final String regex = "e(?:pisode\\s*)?\\s*(\\d{1,3}(?!\\d)|\\d\\d\\d??)(?:-?e?(\\d{1,3}))?(?!\\d)";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(fileName);

        final List<Integer> episodes = new ArrayList<>();

        if (matcher.find()) {
            for (int group = 1; group <= matcher.groupCount(); group++) {
                String matched = matcher.group(group);

                if (matched == null || matched.trim().isEmpty()) {
                    continue;
                }

                episodes.add(Integer.valueOf(matched));
            }
        }

        return episodes;
    }
}
