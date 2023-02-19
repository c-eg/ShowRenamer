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

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for ShowInfoMatcher.
 */
public class ShowInfoMatcherTest {
    @Test
    public void testMatchTitle() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264";
        final String title = ShowInfoMatcher.matchTitle(testCase);
        Assertions.assertEquals("some random movie", title);
    }

    @Test
    public void testMatchTitleContainingYear() {
        final String testCase = "some.random.movie.2012.2010.1080p.blueray.x264";
        final String title = ShowInfoMatcher.matchTitle(testCase);
        Assertions.assertEquals("some random movie 2012", title);
    }

    @Test
    public void testMatchYear() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264";
        final String year = ShowInfoMatcher.matchYear(testCase);
        Assertions.assertEquals("2010", year);
    }
    
    @Test
    public void testMatchYearContainingYearInTitle() {
        final String testCase = "some.random.movie.2012.2010.1080p.blueray.x264";
        final String year = ShowInfoMatcher.matchYear(testCase);
        Assertions.assertEquals("2010", year);
    }

    @Test
    public void testMatchResolution480p() {
        final String testCase = "some.random.movie.2010.480p.blueray.x264";
        final String resolution = ShowInfoMatcher.matchResolution(testCase);
        Assertions.assertEquals("480p", resolution);
    }

    @Test
    public void testMatchResolution720p() {
        final String testCase = "some.random.movie.2010.720p.blueray.x264";
        final String resolution = ShowInfoMatcher.matchResolution(testCase);
        Assertions.assertEquals("720p", resolution);
    }

    @Test
    public void testMatchResolution1080p() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264";
        final String resolution = ShowInfoMatcher.matchResolution(testCase);
        Assertions.assertEquals("1080p", resolution);
    }

    @Test
    public void testMatchSource() {
        Assertions.assertTrue(true);
    }

    @Test
    public void testMatchVideoCodec() {
        Assertions.assertTrue(true);
    }

    @Test
    public void testMatchAudioAac() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.aac";
        final String audio = ShowInfoMatcher.matchAudio(testCase);
        Assertions.assertEquals("aac", audio);
    }

    @Test
    public void testMatchAudioAac2_0() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.aac2.0";
        final String audio = ShowInfoMatcher.matchAudio(testCase);
        Assertions.assertEquals("aac2.0", audio);
    }

    @Test
    public void testMatchAudioAc3() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.ac3";
        final String audio = ShowInfoMatcher.matchAudio(testCase);
        Assertions.assertEquals("ac3", audio);
    }

    @Test
    public void testMatchAudioDts() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.dts";
        final String audio = ShowInfoMatcher.matchAudio(testCase);
        Assertions.assertEquals("dts", audio);
    }

    @Test
    public void testMatchAudioDd5_1() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.dd5.1";
        final String audio = ShowInfoMatcher.matchAudio(testCase);
        Assertions.assertEquals("dd5.1", audio);
    }

    @Test
    public void testMatchLanguageMultisubs() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.MULTiSUBS";
        final String language = ShowInfoMatcher.matchLanguage(testCase);
        Assertions.assertEquals("MULTiSUBS", language);
    }

    @Test
    public void testMatchLanguageMulti() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.MULTi";
        final String language = ShowInfoMatcher.matchLanguage(testCase);
        Assertions.assertEquals("MULTi", language);
    }

    @Test
    public void testMatchLanguageNordic() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.NORDiC";
        final String language = ShowInfoMatcher.matchLanguage(testCase);
        Assertions.assertEquals("NORDiC", language);
    }

    @Test
    public void testMatchLanguageDanish() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.DANiSH";
        final String language = ShowInfoMatcher.matchLanguage(testCase);
        Assertions.assertEquals("DANiSH", language);
    }

    @Test
    public void testMatchLanguageSwedish() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.SWEDiSH";
        final String language = ShowInfoMatcher.matchLanguage(testCase);
        Assertions.assertEquals("SWEDiSH", language);
    }

    @Test
    public void testMatchLanguageNorwegian() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.NORWEGiAN";
        final String language = ShowInfoMatcher.matchLanguage(testCase);
        Assertions.assertEquals("NORWEGiAN", language);
    }

    @Test
    public void testMatchLanguageGerman() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.GERMAN";
        final String language = ShowInfoMatcher.matchLanguage(testCase);
        Assertions.assertEquals("GERMAN", language);
    }

    @Test
    public void testMatchLanguageItalian() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.iTALiAN";
        final String language = ShowInfoMatcher.matchLanguage(testCase);
        Assertions.assertEquals("iTALiAN", language);
    }

    @Test
    public void testMatchLanguageFrench() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.FRENCH";
        final String language = ShowInfoMatcher.matchLanguage(testCase);
        Assertions.assertEquals("FRENCH", language);
    }

    @Test
    public void testMatchLanguageSpanish() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.SPANiSH";
        final String language = ShowInfoMatcher.matchLanguage(testCase);
        Assertions.assertEquals("SPANiSH", language);
    }

    @Test
    public void testMatchEditionUnrated() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.UNRATED";
        final String edition = ShowInfoMatcher.matchEdition(testCase);
        Assertions.assertEquals("UNRATED", edition);
    }

    @Test
    public void testMatchEditionDc() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.DC";
        final String edition = ShowInfoMatcher.matchEdition(testCase);
        Assertions.assertEquals("DC", edition);
    }

    @Test
    public void testMatchEditionDirectorsCut() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.Directors.CUT";
        final String edition = ShowInfoMatcher.matchEdition(testCase);
        Assertions.assertEquals("Directors.CUT", edition);
    }

    @Test
    public void testMatchEditionDirectorsEdition() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.Directors.EDITION";
        final String edition = ShowInfoMatcher.matchEdition(testCase);
        Assertions.assertEquals("Directors.EDITION", edition);
    }

    @Test
    public void testMatchEditionExtendedCut() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.EXTENDED.CUT";
        final String edition = ShowInfoMatcher.matchEdition(testCase);
        Assertions.assertEquals("EXTENDED.CUT", edition);
    }

    @Test
    public void testMatchEditionExtendedEdition() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.EXTENDED.EDITION";
        final String edition = ShowInfoMatcher.matchEdition(testCase);
        Assertions.assertEquals("EXTENDED.EDITION", edition);
    }

    @Test
    public void testMatchEditionExtended() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.EXTENDED";
        final String edition = ShowInfoMatcher.matchEdition(testCase);
        Assertions.assertEquals("EXTENDED", edition);
    }

    @Test
    public void testMatchEdition3d() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.3D";
        final String edition = ShowInfoMatcher.matchEdition(testCase);
        Assertions.assertEquals("3D", edition);
    }

    @Test
    public void testMatchEdition2d() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.2D";
        final String edition = ShowInfoMatcher.matchEdition(testCase);
        Assertions.assertEquals("2D", edition);
    }

    @Test
    public void testMatchEditionNf() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.NF";
        final String edition = ShowInfoMatcher.matchEdition(testCase);
        Assertions.assertEquals("NF", edition);
    }

    @Test
    public void testMatchTagsComplete() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.COMPLETE";
        final String tags = ShowInfoMatcher.matchTags(testCase);
        Assertions.assertEquals("COMPLETE", tags);
    }

    @Test
    public void testMatchTagsLimited() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.LiMiTED";
        final String tags = ShowInfoMatcher.matchTags(testCase);
        Assertions.assertEquals("LiMiTED", tags);
    }

    @Test
    public void testMatchTagsInternal() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.iNTERNAL";
        final String tags = ShowInfoMatcher.matchTags(testCase);
        Assertions.assertEquals("iNTERNAL", tags);
    }

    @Test
    public void testMatchReleaseInfoRealProper() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.REAL.PROPER";
        final String releaseInfo = ShowInfoMatcher.matchReleaseInfo(testCase);
        Assertions.assertEquals("REAL.PROPER", releaseInfo);
    }

    @Test
    public void testMatchReleaseInfoProper() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.PROPER";
        final String releaseInfo = ShowInfoMatcher.matchReleaseInfo(testCase);
        Assertions.assertEquals("PROPER", releaseInfo);
    }

    @Test
    public void testMatchReleaseInfoRepack() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.REPACK";
        final String releaseInfo = ShowInfoMatcher.matchReleaseInfo(testCase);
        Assertions.assertEquals("REPACK", releaseInfo);
    }

    @Test
    public void testMatchReleaseInfoReadNfo() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.READNFO";
        final String releaseInfo = ShowInfoMatcher.matchReleaseInfo(testCase);
        Assertions.assertEquals("READNFO", releaseInfo);
    }

    @Test
    public void testMatchReleaseInfoReadNfo2() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.READ.NFO";
        final String releaseInfo = ShowInfoMatcher.matchReleaseInfo(testCase);
        Assertions.assertEquals("READ.NFO", releaseInfo);
    }

    @Test
    public void testMatchReleaseInfoDirFix() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.DiRFiX";
        final String releaseInfo = ShowInfoMatcher.matchReleaseInfo(testCase);
        Assertions.assertEquals("DiRFiX", releaseInfo);
    }

    @Test
    public void testMatchReleaseInfoNfoFix() {
        final String testCase = "some.random.movie.2010.1080p.blueray.x264.NFOFIX";
        final String releaseInfo = ShowInfoMatcher.matchReleaseInfo(testCase);
        Assertions.assertEquals("NFOFIX", releaseInfo);
    }

    @Test
    public void testMatchSeasonS() {
        final String testCase = "some.random.tv.show.s01e01.1080p.bluray.x264";
        final Integer season = ShowInfoMatcher.matchSeason(testCase);
        Assertions.assertEquals(1, season);
    }

    @Test
    public void testMatchSeasonSeason() {
        final String testCase = "some.random.tv.show.season01e01.1080p.bluray.x264";
        final Integer season = ShowInfoMatcher.matchSeason(testCase);
        Assertions.assertEquals(1, season);
    }

    @Test
    public void testMatchSeasonWithSpace() {
        final String testCase = "some.random.tv.show.season 01e01.1080p.bluray.x264";
        final Integer season = ShowInfoMatcher.matchSeason(testCase);
        Assertions.assertEquals(1, season);
    }

    @Test
    public void testMatchSeasonTwoDigits() {
        final String testCase = "some.random.tv.show.s10e01.1080p.bluray.x264";
        final Integer season = ShowInfoMatcher.matchSeason(testCase);
        Assertions.assertEquals(10, season);
    }

    @Test
    public void testMatchEpisodesEpisodeNoSpace() {
        final String testCase = "some.random.tv.show.s01episode01.1080p.bluray.x264";
        final List<Integer> episodes = ShowInfoMatcher.matchEpisodes(testCase);
        final List<Integer> expected = new ArrayList<>();
        expected.add(1);
        Assertions.assertEquals(expected, episodes);
    }

    @Test
    public void testMatchEpisodesEpisodeSpace() {
        final String testCase = "some.random.tv.show.s01episode 01.1080p.bluray.x264";
        final List<Integer> episodes = ShowInfoMatcher.matchEpisodes(testCase);
        final List<Integer> expected = new ArrayList<>();
        expected.add(1);
        Assertions.assertEquals(expected, episodes);
    }

    @Test
    public void testMatchEpisodesEpisodeNoZeroPadding() {
        final String testCase = "some.random.tv.show.s01episode1.1080p.bluray.x264";
        final List<Integer> episodes = ShowInfoMatcher.matchEpisodes(testCase);
        final List<Integer> expected = new ArrayList<>();
        expected.add(1);
        Assertions.assertEquals(expected, episodes);
    }

    @Test
    public void testMatchEpisodesEpisodeSpaceNoZeroPadding() {
        final String testCase = "some.random.tv.show.s01episode 1.1080p.bluray.x264";
        final List<Integer> episodes = ShowInfoMatcher.matchEpisodes(testCase);
        final List<Integer> expected = new ArrayList<>();
        expected.add(1);
        Assertions.assertEquals(expected, episodes);
    }

    @Test
    public void testMatchEpisodesENoSpace() {
        final String testCase = "some.random.tv.show.s01e01.1080p.bluray.x264";
        final List<Integer> episodes = ShowInfoMatcher.matchEpisodes(testCase);
        final List<Integer> expected = new ArrayList<>();
        expected.add(1);
        Assertions.assertEquals(expected, episodes);
    }

    @Test
    public void testMatchEpisodesENoZeroPadding() {
        final String testCase = "some.random.tv.show.s01e1.1080p.bluray.x264";
        final List<Integer> episodes = ShowInfoMatcher.matchEpisodes(testCase);
        final List<Integer> expected = new ArrayList<>();
        expected.add(1);
        Assertions.assertEquals(expected, episodes);
    }

    @Test
    public void testMatchEpisodesEDualNoSpace() {
        final String testCase = "some.random.tv.show.s01e0102.1080p.bluray.x264";
        final List<Integer> episodes = ShowInfoMatcher.matchEpisodes(testCase);
        final List<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        Assertions.assertEquals(expected, episodes);
    }

    @Test
    public void testMatchEpisodesEDualHiphen() {
        final String testCase = "some.random.tv.show.s01e01-02.1080p.bluray.x264";
        final List<Integer> episodes = ShowInfoMatcher.matchEpisodes(testCase);
        final List<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        Assertions.assertEquals(expected, episodes);
    }

    @Test
    public void testMatchEpisodesEDualE() {
        final String testCase = "some.random.tv.show.s01e01e02.1080p.bluray.x264";
        final List<Integer> episodes = ShowInfoMatcher.matchEpisodes(testCase);
        final List<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        Assertions.assertEquals(expected, episodes);
    }

    @Test
    public void testMatchEpisodesEDualEHiphen() {
        final String testCase = "some.random.tv.show.s01e01-e02.1080p.bluray.x264";
        final List<Integer> episodes = ShowInfoMatcher.matchEpisodes(testCase);
        final List<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        Assertions.assertEquals(expected, episodes);
    }

    @Test
    public void testMatchEpisodesE3Digits() {
        final String testCase = "some.random.tv.show.s01e199.1080p.bluray.x264";
        final List<Integer> episodes = ShowInfoMatcher.matchEpisodes(testCase);
        final List<Integer> expected = new ArrayList<>();
        expected.add(199);
        Assertions.assertEquals(expected, episodes);
    }

    @Test
    public void testMatchEpisodesEDual3Digits() {
        final String testCase = "some.random.tv.show.s01e001004.1080p.bluray.x264";
        final List<Integer> episodes = ShowInfoMatcher.matchEpisodes(testCase);
        final List<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(4);
        Assertions.assertEquals(expected, episodes);
    }

    @Test
    public void testMatchEpisodesEDualNoZeroPaddingHiphen() {
        final String testCase = "some.random.tv.show.s01e1-e2.1080p.bluray.x264";
        final List<Integer> episodes = ShowInfoMatcher.matchEpisodes(testCase);
        final List<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        Assertions.assertEquals(expected, episodes);
    }

    @Test
    public void testMatchEpisodesEDual3DigitsHiphen() {
        final String testCase = "some.random.tv.show.s01e001-e002.1080p.bluray.x264";
        final List<Integer> episodes = ShowInfoMatcher.matchEpisodes(testCase);
        final List<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        Assertions.assertEquals(expected, episodes);
    }

    @Test
    public void testMatchEpisodesNoMatch() {
        final String testCase = "some.random.tv.show.s01.1080p.bluray.x264";
        final List<Integer> episodes = ShowInfoMatcher.matchEpisodes(testCase);
        final List<Integer> expected = new ArrayList<>();
        Assertions.assertEquals(expected, episodes);
    }

    @Test
    public void testMatchReleaseGroup() {
        Assertions.assertTrue(true);
    }
}
