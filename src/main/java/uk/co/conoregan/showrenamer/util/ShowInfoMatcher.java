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
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to match show information from the file name passed.
 */
public class ShowInfoMatcher {
    @Nullable
    public static String matchTitle(final String fileName) {
        final String regex = "(.*?)(\\W| - )(directors(.?)cut|480p|720p|1080p|dvdrip|xvid|cd[0-9]|bluray|dvdscr|brrip|divx|S[0-9]{1,3}E[0-9]{1,3}|Season[\\s,0-9]{1,4}|[{(\\[]?[0-9]{4}).*";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            return matcher.group(1).replace(".", " ").trim();
        } else {
            return null;
        }
    }

    @Nullable
    public static String matchYear(final String fileName) {
        final String regex = "[.\\s](?!^)[1,2]\\d{3}[.\\s]";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            return matcher.group(0).replace(".", "").trim();
        }

        return null;
    }

    @Nullable
    public static String matchResolution(final String fileName) {
        final String regex = "\\d{3,4}p";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            return matcher.group(0);
        }

        return null;
    }

    @Nullable
    public static String matchSource(final String fileName) {
        final String regex = "[.\\s](CAM|(DVD|BD)SCR|SCR|DDC|R5[.\\s]LINE|R5|(DVD|HD|BR|BD|WEB)Rip|DVDR|(HD|PD)TV|WEB-DL|WEBDL|BluRay|Blu-Ray|TS(?!C)|TELESYNC)";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            return matcher.group(0).substring(1);
        }

        return null;
    }

    @Nullable
    public static String matchVideoCodec(final String fileName) {
        final String regex = "[.\\s](NTSC|PAL|[xh][.\\s]?264|[xh][.\\s]?265|H264|H265)";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            return matcher.group(0).substring(1);
        }

        return null;
    }

    @Nullable
    public static String matchAudio(final String fileName) {
        final String regex = "AAC2[.\\s]0|AAC|AC3|DTS|DD5[.\\s]1";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            return matcher.group(0);
        }

        return null;
    }

    @Nullable
    public static String matchLanguage(final String fileName) {
        final String regex = "[.\\s](MULTiSUBS|MULTi|NORDiC|DANiSH|SWEDiSH|NORWEGiAN|GERMAN|iTALiAN|FRENCH|SPANiSH)";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            return matcher.group(0).substring(1);
        }

        return null;
    }

    @Nullable
    public static String matchEdition(final String fileName) {
        final String regex = "UNRATED|DC|(Directors|EXTENDED)[.\\s](CUT|EDITION)|EXTENDED|3D|2D|\\bNF\\b";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            return matcher.group(0);
        }

        return null;
    }

    @Nullable
    public static String matchTags(final String fileName) {
        final String regex = "COMPLETE|LiMiTED|iNTERNAL";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            return matcher.group(0);
        }

        return null;
    }

    @Nullable
    public static String matchReleaseInfo(final String fileName) {
        final String regex = "[.\\s](REAL[.\\s]PROPER|PROPER|REPACK|READNFO|READ[.\\s]NFO|DiRFiX|NFOFiX)";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            return matcher.group(0).substring(1);
        }

        return null;
    }

    @Nullable
    public static Integer matchSeason(final String fileName) {
        final String regex = "s(?:eason)?\\s*(\\d{1,2})";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        return null;
    }

    @Nonnull
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

    @Nullable
    public static String matchReleaseGroup(final String fileName) {
        final String regex = "- ?([^\\-. ]+)$";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            return matcher.group(0).substring(2);
        }

        return null;
    }
}
