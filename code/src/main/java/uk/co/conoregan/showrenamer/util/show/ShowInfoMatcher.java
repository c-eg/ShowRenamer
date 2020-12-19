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

package uk.co.conoregan.showrenamer.util.show;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowInfoMatcher
{
    private final String title;
    private final String year;
    private final String resolution;
    private final String type;
    private final String video;
    private final String audio;
    private final String language;
    private final String edition;
    private final String tags;
    private final String release;
    private final String season;
    private final String episode;

    /**
     * Constructor for ShowInfo
     *
     * @param showFile show string to get info from
     *                 e.g.  Movie.Name.2017.1080p.BluRay.x264
     */
    public ShowInfoMatcher(String showFile)
    {
        title = matchTitle(showFile);
        year = matchYear(showFile);
        resolution = matchResolution(showFile);
        type = matchType(showFile);
        video = matchVideo(showFile);
        audio = matchAudio(showFile);
        language = matchLanguage(showFile);
        edition = matchEdition(showFile);
        tags = matchTags(showFile);
        release = matchRelease(showFile);
        season = matchSeason(showFile);
        episode = matchEpisode(showFile);
    }

    /**
     * Function to match the regex passed against the file
     *
     * @param showFile show string to get info from
     * @param regex    regex to match against showFile
     * @return String with result of match from regex
     */
    private static String matchRegex(String showFile, String regex)
    {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(showFile);

        if (m.find())
        {
            return m.group();
        }
        else
            return null;
    }

    /**
     * Function to match the regex passed against the file
     *
     * @param showFile     show string to get info from
     * @param regex        regex to match against showFile
     * @param regexPattern regex flag condition
     * @return String with result of match from regex
     */
    private static String matchRegex(String showFile, String regex, int regexPattern)
    {
        Pattern p = Pattern.compile(regex, regexPattern);
        Matcher m = p.matcher(showFile);

        if (m.find())
        {
            return m.group();
        }
        else
            return null;
    }

    /**
     * Function to match the title from the showFile passed
     *
     * @param showFile show string to get info from
     * @return title of show
     */
    private static String matchTitle(String showFile)
    {
        Pattern moviePattern = Pattern.compile("(.*?)(\\W| - )(directors(.?)cut|480p|720p|1080p|dvdrip|xvid|cd[0-9]|bluray|dvdscr|brrip|divx|S[0-9]{1,3}E[0-9]{1,3}|Season[\\s,0-9]{1,4}|[\\{\\(\\[]?[0-9]{4}).*", Pattern.CASE_INSENSITIVE);
        Matcher movieMatcher = moviePattern.matcher(showFile);
        String name;

        if (movieMatcher.find() && movieMatcher.group(1) != null)
        {
            name = movieMatcher.group(1).replaceAll("\\.", " ").trim();
        }
        else
        {
            name = showFile;
        }

        return name;
    }

    /**
     * Function to match the year from the showFile passed
     *
     * @param showFile show string to get info from
     * @return year of show, or null it if doesn't exist
     */
    private static String matchYear(String showFile)
    {
        String s = matchRegex(showFile, "[\\.\\s](?!^)[1,2]\\d{3}[\\.\\s]");
        if (s != null)
        {
            return s.substring(1, s.length() - 1);
        }
        else
            return null;
    }

    /**
     * Function to match the resolution from the showFile passed
     *
     * @param showFile show string to get info from
     * @return resolution of show, or null it if doesn't exist
     */
    private static String matchResolution(String showFile)
    {
        return matchRegex(showFile, "\\d{3,4}p", Pattern.CASE_INSENSITIVE);
    }

    /**
     * Function to match the type from the showFile passed
     *
     * @param showFile show string to get info from
     * @return type of show, or null it if doesn't exist
     */
    private static String matchType(String showFile)
    {
        String s = matchRegex(showFile, "[\\.\\s](CAM|(DVD|BD)SCR|SCR|DDC|R5[\\.\\s]LINE|R5|(DVD|HD|BR|BD|WEB)Rip|DVDR|(HD|PD)TV|WEB-DL|WEBDL|BluRay|TS(?!C)|TELESYNC)", Pattern.CASE_INSENSITIVE);
        if (s != null)
        {
            return s.substring(1);
        }
        else
            return null;
    }

    /**
     * Function to match the video format from the showFile passed
     *
     * @param showFile show string to get info from
     * @return video format of show, or null it if doesn't exist
     */
    private static String matchVideo(String showFile)
    {
        String s = matchRegex(showFile, "[\\.\\s](NTSC|PAL|[xh][\\.\\s]?264|H264)", Pattern.CASE_INSENSITIVE);
        if (s != null)
        {
            return s.substring(1);
        }
        else
            return null;
    }

    /**
     * Function to match the audio format from the showFile passed
     *
     * @param showFile show string to get info from
     * @return audio format of show, or null it if doesn't exist
     */
    private static String matchAudio(String showFile)
    {
        return matchRegex(showFile, "AAC2[\\.\\s]0|AAC|AC3|DTS|DD5[\\.\\s]1", Pattern.CASE_INSENSITIVE);
    }

    /**
     * Function to match the language from the showFile passed
     *
     * @param showFile show string to get info from
     * @return language of show, or null it if doesn't exist
     */
    private static String matchLanguage(String showFile)
    {
        String s = matchRegex(showFile, "[\\.\\s](MULTiSUBS|MULTi|NORDiC|DANiSH|SWEDiSH|NORWEGiAN|GERMAN|iTALiAN|FRENCH|SPANiSH)", Pattern.CASE_INSENSITIVE);
        if (s != null)
        {
            return s.substring(1);
        }
        else
            return null;
    }

    /**
     * Function to match the edition from the showFile passed
     *
     * @param showFile show string to get info from
     * @return edition of show, or null it if doesn't exist
     */
    private static String matchEdition(String showFile)
    {
        String s = matchRegex(showFile, "UNRATED|DC|(Directors|EXTENDED)[\\.\\s](CUT|EDITION)|EXTENDED|3D|2D|\\bNF\\b", Pattern.CASE_INSENSITIVE);
        if (s != null)
        {
            return s.substring(1);
        }
        else
            return null;
    }

    /**
     * Function to match the tags from the showFile passed
     *
     * @param showFile show string to get info from
     * @return tags of show, or null it if doesn't exist
     */
    private static String matchTags(String showFile)
    {
        return matchRegex(showFile, "COMPLETE|LiMiTED|iNTERNAL", Pattern.CASE_INSENSITIVE);
    }

    /**
     * Function to match the release from the showFile passed
     *
     * @param showFile show string to get info from
     * @return release of show, or null it if doesn't exist
     */
    private static String matchRelease(String showFile)
    {
        String s = matchRegex(showFile, "[\\.\\s](REAL[\\.\\s]PROPER|PROPER|REPACK|READNFO|READ[\\.\\s]NFO|DiRFiX|NFOFiX)", Pattern.CASE_INSENSITIVE);
        if (s != null)
        {
            return s.substring(1);
        }
        else
            return null;
    }

    /**
     * Function to match the season number from the showFile passed
     *
     * @param showFile show string to get info from
     * @return season number of show, or null it if doesn't exist
     * <p>
     * works in formats:
     * - {season/s}XXX
     */
    private static String matchSeason(String showFile)
    {
        String s = matchRegex(showFile, "(?:s|season)(\\d{1,3})", Pattern.CASE_INSENSITIVE);
        if (s != null)
        {
            return s.substring(1);
        }
        else
            return null;
    }

    /**
     * Function to match the episode number from the showFile passed
     *
     * @param showFile show string to get info from
     * @return episode number of show, or null it if doesn't exist
     * <p>
     * works in formats:
     * - {episode/e}XXX
     * - {episode/e}XXXeXXXeXXX
     * - {episode/e}XXX-XXX-XXX
     */
    private static String matchEpisode(String showFile)
    {
        String s = matchRegex(showFile, "e(?:(\\d{1,3})|(\\d{1,3}([e-]\\d{1,3})+))", Pattern.CASE_INSENSITIVE);
        if (s != null)
        {
            s = s.substring(1);
            return s.replaceAll("e|-", ",");
        }
        else
            return null;
    }

    public String getTitle()
    {
        return title;
    }

    public String getYear()
    {
        return year;
    }

    public String getResolution()
    {
        return resolution;
    }

    public String getType()
    {
        return type;
    }

    public String getVideo()
    {
        return video;
    }

    public String getAudio()
    {
        return audio;
    }

    public String getLanguage()
    {
        return language;
    }

    public String getEdition()
    {
        return edition;
    }

    public String getTags()
    {
        return tags;
    }

    public String getRelease()
    {
        return release;
    }

    public String getSeason()
    {
        return season;
    }

    public String getEpisode()
    {
        return episode;
    }

    @Override
    public String toString()
    {
        return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s", this.title, this.year, this.resolution, this.type, this.video, this.audio, this.language, this.edition, this.tags, this.release, this.season, this.episode);
    }

    /*
     * Testing example
     */
    public static void main(String[] args)
    {
        ShowInfoMatcher showInfoMatcher = new ShowInfoMatcher("Wynonna Earp - S04E04");
        System.out.println(showInfoMatcher);
    }
}
