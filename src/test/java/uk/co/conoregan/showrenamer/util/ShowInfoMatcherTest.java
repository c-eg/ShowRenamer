package uk.co.conoregan.showrenamer.util;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.*;

public class ShowInfoMatcherTest {
    /*
     * Title tests.
     */

    @Test
    public void testMatchTitle() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.x264";
        String titleMatch = ShowInfoMatcher.matchTitle(testCase);
        assertEquals("some random movie", titleMatch);
    }

    @Test
    public void testMatchTitleContainingYear() {
        String testCase = "some.random.movie.2012.2010.1080p.blue-ray.x264";
        String titleMatch = ShowInfoMatcher.matchTitle(testCase);
        assertEquals("some random movie 2012", titleMatch);
    }

    /*
     * Year tests.
     */

    @Test
    public void testMatchYear() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.x264";
        String yearMatch = ShowInfoMatcher.matchYear(testCase);
        assertEquals("2010", yearMatch);
    }

    @Test
    public void testMatchYearWithTitleNumber() {
        String testCase = "some.random.movie.2012.2010.1080p.blue-ray.x264";
        String yearMatch = ShowInfoMatcher.matchYear(testCase);
        assertEquals("2010", yearMatch);
    }

    /*
     * Resolution tests.
     */

    @Test
    public void testMatchResolution480p() {
        String testCase = "some.random.movie.2010.480p.blue-ray.x264";
        String resolutionMatch = ShowInfoMatcher.matchResolution(testCase);
        assertEquals("480p", resolutionMatch);
    }

    @Test
    public void testMatchResolution720p() {
        String testCase = "some.random.movie.2010.720p.blue-ray.x264";
        String resolutionMatch = ShowInfoMatcher.matchResolution(testCase);
        assertEquals("720p", resolutionMatch);
    }

    @Test
    public void testMatchResolution1080p() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.x264";
        String resolutionMatch = ShowInfoMatcher.matchResolution(testCase);
        assertEquals("1080p", resolutionMatch);
    }

    /*
     * Source tests.
     */

    @Test
    public void testMatchSource() {

    }

    /*
     * Video codec tests.
     */

    @Test
    public void testMatchVideoCodec() {

    }

    /*
     * Audio tests.
     */

    @Test
    public void testMatchAudioAac() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.aac.x264";
        String audioMatch = ShowInfoMatcher.matchAudio(testCase);
        assertEquals("aac", audioMatch);
    }

    @Test
    public void testMatchAudioAac2_0() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.aac2.0.x264";
        String audioMatch = ShowInfoMatcher.matchAudio(testCase);
        assertEquals("aac2.0", audioMatch);
    }

    @Test
    public void testMatchAudioAc3() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.ac3.x264";
        String audioMatch = ShowInfoMatcher.matchAudio(testCase);
        assertEquals("ac3", audioMatch);
    }

    @Test
    public void testMatchAudioDts() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dts.x264";
        String audioMatch = ShowInfoMatcher.matchAudio(testCase);
        assertEquals("dts", audioMatch);
    }

    @Test
    public void testMatchAudioDd5_1() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264";
        String audioMatch = ShowInfoMatcher.matchAudio(testCase);
        assertEquals("dd5.1", audioMatch);
    }

    /*
     * Language tests.
     */

    @Test
    public void testMatchLanguageMultiSubs() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.MULTiSUBS";
        String languageMatch = ShowInfoMatcher.matchLanguage(testCase);
        assertEquals("MULTiSUBS", languageMatch);
    }

    @Test
    public void testMatchLanguageMulti() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.MULTi";
        String languageMatch = ShowInfoMatcher.matchLanguage(testCase);
        assertEquals("MULTi", languageMatch);
    }

    @Test
    public void testMatchLanguageNordic() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.NORDiC";
        String languageMatch = ShowInfoMatcher.matchLanguage(testCase);
        assertEquals("NORDiC", languageMatch);
    }

    @Test
    public void testMatchLanguageDanish() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.DANiSH";
        String languageMatch = ShowInfoMatcher.matchLanguage(testCase);
        assertEquals("DANiSH", languageMatch);
    }

    @Test
    public void testMatchLanguageSwedish() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.SWEDiSH";
        String languageMatch = ShowInfoMatcher.matchLanguage(testCase);
        assertEquals("SWEDiSH", languageMatch);
    }

    @Test
    public void testMatchLanguageNorwegian() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.NORWEGiAN";
        String languageMatch = ShowInfoMatcher.matchLanguage(testCase);
        assertEquals("NORWEGiAN", languageMatch);
    }

    @Test
    public void testMatchLanguageGerman() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.GERMAN";
        String languageMatch = ShowInfoMatcher.matchLanguage(testCase);
        assertEquals("GERMAN", languageMatch);
    }

    @Test
    public void testMatchLanguageItalian() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.iTALiAN";
        String languageMatch = ShowInfoMatcher.matchLanguage(testCase);
        assertEquals("iTALiAN", languageMatch);
    }

    @Test
    public void testMatchLanguageFrench() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.FRENCH";
        String languageMatch = ShowInfoMatcher.matchLanguage(testCase);
        assertEquals("FRENCH", languageMatch);
    }

    @Test
    public void testMatchLanguageSpanish() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.SPANiSH";
        String languageMatch = ShowInfoMatcher.matchLanguage(testCase);
        assertEquals("SPANiSH", languageMatch);
    }

    /*
     * Edition tests.
     */

    @Test
    public void testMatchEditionUnrated() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.SPANiSH.UNRATED";
        String editionMatch = ShowInfoMatcher.matchEdition(testCase);
        assertEquals("UNRATED", editionMatch);
    }

    @Test
    public void testMatchEditionDc() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.SPANiSH.DC";
        String editionMatch = ShowInfoMatcher.matchEdition(testCase);
        assertEquals("DC", editionMatch);
    }

    @Test
    public void testMatchEditionDirectorsCut() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.SPANiSH.Directors.CUT";
        String editionMatch = ShowInfoMatcher.matchEdition(testCase);
        assertEquals("Directors.CUT", editionMatch);
    }

    @Test
    public void testMatchEditionDirectorsEdition() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.SPANiSH.Directors.EDITION";
        String editionMatch = ShowInfoMatcher.matchEdition(testCase);
        assertEquals("Directors.EDITION", editionMatch);
    }

    @Test
    public void testMatchEditionExtendedCut() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.SPANiSH.EXTENDED.CUT";
        String editionMatch = ShowInfoMatcher.matchEdition(testCase);
        assertEquals("EXTENDED.CUT", editionMatch);
    }

    @Test
    public void testMatchEditionExtendedEdition() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.SPANiSH.EXTENDED.EDITION";
        String editionMatch = ShowInfoMatcher.matchEdition(testCase);
        assertEquals("EXTENDED.EDITION", editionMatch);
    }

    @Test
    public void testMatchEditionExtended() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.SPANiSH.EXTENDED";
        String editionMatch = ShowInfoMatcher.matchEdition(testCase);
        assertEquals("EXTENDED", editionMatch);
    }

    @Test
    public void testMatchEdition3d() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.SPANiSH.3D";
        String editionMatch = ShowInfoMatcher.matchEdition(testCase);
        assertEquals("3D", editionMatch);
    }

    @Test
    public void testMatchEdition2d() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.SPANiSH.2D";
        String editionMatch = ShowInfoMatcher.matchEdition(testCase);
        assertEquals("2D", editionMatch);
    }

    @Test
    public void testMatchEditionNf() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.SPANiSH.NF";
        String editionMatch = ShowInfoMatcher.matchEdition(testCase);
        assertEquals("NF", editionMatch);
    }

    /*
     * Tags tests.
     */

    @Test
    public void testMatchTagsComplete() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.SPANiSH.NF.COMPLETE";
        String tagsMatch = ShowInfoMatcher.matchTags(testCase);
        assertEquals("COMPLETE", tagsMatch);
    }

    @Test
    public void testMatchTagsLimited() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.SPANiSH.NF.LiMiTED";
        String tagsMatch = ShowInfoMatcher.matchTags(testCase);
        assertEquals("LiMiTED", tagsMatch);
    }

    @Test
    public void testMatchTagsInternal() {
        String testCase = "some.random.movie.2010.1080p.blue-ray.dd5.1.x264.SPANiSH.NF.iNTERNAL";
        String tagsMatch = ShowInfoMatcher.matchTags(testCase);
        assertEquals("iNTERNAL", tagsMatch);
    }

    /*
     * Release info tests.
     */

    @Test
    public void testMatchReleaseInfoRealProper() {
        String testCase = "some.random.movie.2010.1080p.REAL.PROPER";
        String matchReleaseInfo = ShowInfoMatcher.matchReleaseInfo(testCase);
        assertEquals("REAL.PROPER", matchReleaseInfo);
    }

    @Test
    public void testMatchReleaseInfoProper() {
        String testCase = "some.random.movie.2010.1080p.PROPER";
        String matchReleaseInfo = ShowInfoMatcher.matchReleaseInfo(testCase);
        assertEquals("PROPER", matchReleaseInfo);
    }

    @Test
    public void testMatchReleaseInfoRepack() {
        String testCase = "some.random.movie.2010.1080p.REPACK";
        String matchReleaseInfo = ShowInfoMatcher.matchReleaseInfo(testCase);
        assertEquals("REPACK", matchReleaseInfo);
    }

    @Test
    public void testMatchReleaseInfoReadnfo() {
        String testCase = "some.random.movie.2010.1080p.READNFO";
        String matchReleaseInfo = ShowInfoMatcher.matchReleaseInfo(testCase);
        assertEquals("READNFO", matchReleaseInfo);
    }

    @Test
    public void testMatchReleaseInfoReadNfo() {
        String testCase = "some.random.movie.2010.1080p.READ.NFO";
        String matchReleaseInfo = ShowInfoMatcher.matchReleaseInfo(testCase);
        assertEquals("READ.NFO", matchReleaseInfo);
    }

    @Test
    public void testMatchReleaseInfoDirFix() {
        String testCase = "some.random.movie.2010.1080p.DiRFiX";
        String matchReleaseInfo = ShowInfoMatcher.matchReleaseInfo(testCase);
        assertEquals("DiRFiX", matchReleaseInfo);
    }

    @Test
    public void testMatchReleaseInfoNfoFix() {
        String testCase = "some.random.movie.2010.1080p.NFOFiX";
        String matchReleaseInfo = ShowInfoMatcher.matchReleaseInfo(testCase);
        assertEquals("NFOFiX", matchReleaseInfo);
    }

    /*
     * Season tests.
     */

    @Test
    public void testMatchSeasonS() {
        String testCase = "some.random.tv.show.s01e01.1080p.blu-ray.x264";
        int matchSeason = ShowInfoMatcher.matchSeason(testCase);
        assertEquals(1, matchSeason);
    }

    @Test
    public void testMatchSeasonSeason() {
        String testCase = "some.random.tv.show.season01e01.1080p.blu-ray.x264";
        int matchSeason = ShowInfoMatcher.matchSeason(testCase);
        assertEquals(1, matchSeason);
    }

    @Test
    public void testMatchSeasonSeasonWithSpace() {
        String testCase = "some.random.tv.show.season 01e01.1080p.blu-ray.x264";
        int matchSeason = ShowInfoMatcher.matchSeason(testCase);
        assertEquals(1, matchSeason);
    }

    @Test
    public void testMatchSeasonTwoDigits() {
        String testCase = "some.random.tv.show.s10e01.1080p.blu-ray.x264";
        int matchSeason = ShowInfoMatcher.matchSeason(testCase);
        assertEquals(10, matchSeason);
    }

    /*
     * Episode tests.
     */

    @Test
    public void testMatchEpisodeNoSpace() {
        String testCase = "some.random.tv.show.s01episode01.1080p.blu-ray.x264";
        ArrayList<Integer> matchEpisode = ShowInfoMatcher.matchEpisode(testCase);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        assertEquals(expected, matchEpisode);
    }

    @Test
    public void testMatchEpisodeSpace() {
        String testCase = "some.random.tv.show.s01episode 01.1080p.blu-ray.x264";
        ArrayList<Integer> matchEpisode = ShowInfoMatcher.matchEpisode(testCase);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        assertEquals(expected, matchEpisode);
    }

    @Test
    public void testMatchEpisodeNoZeroPadding() {
        String testCase = "some.random.tv.show.s01episode1.1080p.blu-ray.x264";
        ArrayList<Integer> matchEpisode = ShowInfoMatcher.matchEpisode(testCase);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        assertEquals(expected, matchEpisode);
    }

    @Test
    public void testMatchEpisodeEpisodeSpaceNoZeroPadding() {
        String testCase = "some.random.tv.show.s01episode 1.1080p.blu-ray.x264";
        ArrayList<Integer> matchEpisode = ShowInfoMatcher.matchEpisode(testCase);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        assertEquals(expected, matchEpisode);
    }

    @Test
    public void testMatchEpisodeENoSpace() {
        String testCase = "some.random.tv.show.s01e01.1080p.blu-ray.x264";
        ArrayList<Integer> matchEpisode = ShowInfoMatcher.matchEpisode(testCase);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        assertEquals(expected, matchEpisode);
    }

    @Test
    public void testMatchEpisodeENoZeroPadding() {
        String testCase = "some.random.tv.show.s01e1.1080p.blu-ray.x264";
        ArrayList<Integer> matchEpisode = ShowInfoMatcher.matchEpisode(testCase);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        assertEquals(expected, matchEpisode);
    }

    @Test
    public void testMatchEpisodeEDualNoSpace() {
        String testCase = "some.random.tv.show.s01e0102.1080p.blu-ray.x264";
        ArrayList<Integer> matchEpisode = ShowInfoMatcher.matchEpisode(testCase);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        assertEquals(expected, matchEpisode);
    }

    @Test
    public void testMatchEpisodeEDualHyphen() {
        String testCase = "some.random.tv.show.s01e01-02.1080p.blu-ray.x264";
        ArrayList<Integer> matchEpisode = ShowInfoMatcher.matchEpisode(testCase);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        assertEquals(expected, matchEpisode);
    }

    @Test
    public void testMatchEpisodeEDualWithE() {
        String testCase = "some.random.tv.show.s01e01e02.1080p.blu-ray.x264";
        ArrayList<Integer> matchEpisode = ShowInfoMatcher.matchEpisode(testCase);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        assertEquals(expected, matchEpisode);
    }

    @Test
    public void testMatchEDualHiphenE() {
        String testCase = "some.random.tv.show.s01e01-e02.1080p.blu-ray.x264";
        ArrayList<Integer> matchEpisode = ShowInfoMatcher.matchEpisode(testCase);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        assertEquals(expected, matchEpisode);
    }

    @Test
    public void testMatchEpisodeE3Digits() {
        String testCase = "some.random.tv.show.s01e199.1080p.blu-ray.x264";
        ArrayList<Integer> matchEpisode = ShowInfoMatcher.matchEpisode(testCase);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(199);
        assertEquals(expected, matchEpisode);
    }

    @Test
    public void testMatchEpisodeEDual3Digits() {
        String testCase = "some.random.tv.show.s01e001004.1080p.blu-ray.x264";
        ArrayList<Integer> matchEpisode = ShowInfoMatcher.matchEpisode(testCase);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(4);
        assertEquals(expected, matchEpisode);
    }

    @Test
    public void testMatchEpisodeEDualNoZeroPaddingHyphen() {
        String testCase = "some.random.tv.show.s01e1-e2.1080p.blu-ray.x264";
        ArrayList<Integer> matchEpisode = ShowInfoMatcher.matchEpisode(testCase);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        assertEquals(expected, matchEpisode);
    }

    @Test
    public void testMatchEpisodeEDual3DigitsHyphenE() {
        String testCase = "some.random.tv.show.s01e001-e002.1080p.blu-ray.x264";
        ArrayList<Integer> matchEpisode = ShowInfoMatcher.matchEpisode(testCase);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        assertEquals(expected, matchEpisode);
    }

    @Test
    public void testMatchEpisodeNoMatch() {
        String testCase = "some.random.tv.show.s01.1080p.blu-ray.x264";
        ArrayList<Integer> matchEpisode = ShowInfoMatcher.matchEpisode(testCase);
        assertNull(matchEpisode);
    }
}
