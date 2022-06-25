package uk.co.conoregan.showrenamer.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowInfoMatcher {
    private static Matcher match(String regex, String fileName) {
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return p.matcher(fileName);
    }

    public static String matchTitle(String fileName) {
        String regex = "(.*?)(\\W| - )(directors(.?)cut|480p|720p|1080p|dvdrip|xvid|cd\\d|bluray|blu-ray|dvdscr|brrip|divx|S\\d{1,3}E\\d{1,3}|Season[\\s,0-9]{1,4}|[\\{\\(\\[]?\\d{4}).*";
        Matcher m = match(regex, fileName);

        if (m.find()) {
            return m.group(1).replace(".", " ").strip();
        }

        return null;
    }

    public static String matchYear(String fileName) {
        String regex = "[\\.\\s](?!^)[1,2]\\d{3}[\\.\\s]";
        Matcher m = match(regex, fileName);

        if (m.find()) {
            return m.group(0).replace(".", "");
        }

        return null;
    }

    public static String matchResolution(String fileName) {
        String regex = "\\d{3,4}p";
        Matcher m = match(regex, fileName);

        if (m.find()) {
            return m.group(0);
        }

        return null;
    }

    public static String matchSource(String fileName) {
        String regex = "[\\.\\s](CAM|(DVD|BD)SCR|SCR|DDC|R5[\\.\\s]LINE|R5|(DVD|HD|BR|BD|WEB)Rip|DVDR|(HD|PD)TV|WEB-DL|WEBDL|BluRay|Blu-Ray|TS(?!C)|TELESYNC)";
        Matcher m = match(regex, fileName);

        if (m.find()) {
            return m.group(0).substring(1);
        }

        return null;
    }

    public static String matchVideoCodec(String fileName) {
        String regex = "[\\.\\s](NTSC|PAL|[xh][\\.\\s]?264|[xh][\\.\\s]?265|H264|H265)";
        Matcher m = match(regex, fileName);

        if (m.find()) {
            return m.group(0).substring(1);
        }

        return null;
    }

    public static String matchAudio(String fileName) {
        String regex = "AAC2[\\.\\s]0|AAC|AC3|DTS|DD5[\\.\\s]1";
        Matcher m = match(regex, fileName);

        if (m.find()) {
            return m.group(0);
        }

        return null;
    }

    public static String matchLanguage(String fileName) {
        String regex = "[\\.\\s](MULTiSUBS|MULTi|NORDiC|DANiSH|SWEDiSH|NORWEGiAN|GERMAN|iTALiAN|FRENCH|SPANiSH)";
        Matcher m = match(regex, fileName);

        if (m.find()) {
            return m.group(0).substring(1);
        }

        return null;
    }

    public static String matchEdition(String fileName) {
        String regex = "UNRATED|DC|(Directors|EXTENDED)[\\.\\s](CUT|EDITION)|EXTENDED|3D|2D|\\bNF\\b";
        Matcher m = match(regex, fileName);

        if (m.find()) {
            return m.group(0);
        }

        return null;
    }

    public static String matchTags(String fileName) {
        String regex = "COMPLETE|LiMiTED|iNTERNAL";
        Matcher m = match(regex, fileName);

        if (m.find()) {
            return m.group(0);
        }

        return null;
    }

    public static String matchReleaseInfo(String fileName) {
        String regex = "[\\.\\s](REAL[\\.\\s]PROPER|PROPER|REPACK|READNFO|READ[\\.\\s]NFO|DiRFiX|NFOFiX)";
        Matcher m = match(regex, fileName);

        if (m.find()) {
            return m.group(0).substring(1);
        }

        return null;
    }

    public static Integer matchSeason(String fileName) {
        String regex = "s(?:eason)?\\s*(\\d{1,2})";
        Matcher m = match(regex, fileName);

        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }

        return null;
    }

    public static ArrayList<Integer> matchEpisode(String fileName) {
        String regex = "e(?:pisode\\s*)?(\\d{1,3}(?!\\d)|\\d\\d\\d??)(?:-?e?(\\d{1,3}))?(?!\\d)";
        Matcher m = match(regex, fileName);

        // TODO: make this better
        ArrayList<Integer> episodes = new ArrayList<>();
        if (m.find()) {
            for (int i = 1; i <= m.groupCount(); i++) {
                if (m.group(i) != null)
                    episodes.add(Integer.valueOf(m.group(i)));
            }
        }

        if (episodes.size() > 0) {
            return episodes;
        }

        return null;
    }

    public static String matchReleaseGroup(String fileName) {
        String regex = "- ?([^\\-. ]+)$";
        Matcher m = match(regex, fileName);

        if (m.find()) {
            return m.group(0).substring(1);
        }

        return null;
    }
}
