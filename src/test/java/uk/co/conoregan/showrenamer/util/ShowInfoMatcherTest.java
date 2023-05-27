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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Tests for {@link ShowInfoMatcher}.
 */
public class ShowInfoMatcherTest {
    @Test
    public void testMatchTitle() {
        final String testCase = "some.random.movie.2010.1080p.bluray.x264";
        final Optional<String> title = ShowInfoMatcher.matchTitle(testCase);
        Assertions.assertTrue(title.isPresent());
        Assertions.assertEquals("some random movie", title.get());
    }

//    @Test
//    public void testMatchTitleContainingYear() {
//        final String testCase = "some.random.movie.2012.2010.1080p.bluray.x264";
//        final Optional<String> title = ShowInfoMatcher.matchTitle(testCase);
//        Assertions.assertTrue(title.isPresent());
//        Assertions.assertEquals("some random movie 2012", title.get());
//    }

    @Test
    public void testMatchTitleOnlyYear() {
        final String testCase = "2012.2010.1080p.bluray.x264";
        final Optional<String> title = ShowInfoMatcher.matchTitle(testCase);
        Assertions.assertTrue(title.isPresent());
        Assertions.assertEquals("2012", title.get());
    }

    @Test
    public void testMatchTitleNoMatch() {
        final String testCase = "1080p.x264";
        final Optional<String> title = ShowInfoMatcher.matchTitle(testCase);
        Assertions.assertFalse(title.isPresent());
    }

    @Test
    public void testMatchYear() {
        final String testCase = "some.random.movie.2010.1080p.bluray.x264";
        final Optional<Integer> year = ShowInfoMatcher.matchYear(testCase);
        Assertions.assertTrue(year.isPresent());
        Assertions.assertEquals(2010, year.get());
    }

    @Test
    public void testMatchYearInBrackets() {
        final String testCase = "some.random.movie (2010) 1080p.bluray.x264";
        final Optional<Integer> year = ShowInfoMatcher.matchYear(testCase);
        Assertions.assertTrue(year.isPresent());
        Assertions.assertEquals(2010, year.get());
    }

    @Test
    public void testMatchYearContainingYearInTitle() {
        final String testCase = "some.random.movie.2012.2010.1080p.bluray.x264";
        final Optional<Integer> year = ShowInfoMatcher.matchYear(testCase);
        Assertions.assertTrue(year.isPresent());
        Assertions.assertEquals(2010, year.get());
    }

    @Test
    public void testMatchYearOnlyYearInTitle() {
        final String testCase = "2012.2010.1080p.bluray.x264";
        final Optional<Integer> year = ShowInfoMatcher.matchYear(testCase);
        Assertions.assertTrue(year.isPresent());
        Assertions.assertEquals(2010, year.get());
    }

    @Test
    public void testMatchYearWithResolution() {
        final String testCase = "movie.title.2010.1080p.bluray.x264";
        final Optional<Integer> year = ShowInfoMatcher.matchYear(testCase);
        Assertions.assertTrue(year.isPresent());
        Assertions.assertEquals(2010, year.get());
    }

    @Test
    public void testMatchYearNoMatchWithResolution() {
        final String testCase = "movie.title.1080p.bluray.x264";
        final Optional<Integer> year = ShowInfoMatcher.matchYear(testCase);
        Assertions.assertFalse(year.isPresent());
    }

    @Test
    public void testMatchYearNoMatch() {
        final String testCase = "1080p.bluray.x264";
        final Optional<Integer> year = ShowInfoMatcher.matchYear(testCase);
        Assertions.assertFalse(year.isPresent());
    }

    @Test
    public void testMatchSeasonS() {
        final String testCase = "some.random.tv.show.s01e01.1080p.bluray.x264";
        final Optional<Integer> season = ShowInfoMatcher.matchSeason(testCase);
        Assertions.assertTrue(season.isPresent());
        Assertions.assertEquals(1, season.get());
    }

    @Test
    public void testMatchSeasonSeason() {
        final String testCase = "some.random.tv.show.season01e01.1080p.bluray.x264";
        final Optional<Integer> season = ShowInfoMatcher.matchSeason(testCase);
        Assertions.assertTrue(season.isPresent());
        Assertions.assertEquals(1, season.get());
    }

    @Test
    public void testMatchSeasonWithSpace() {
        final String testCase = "some.random.tv.show.season 01e01.1080p.bluray.x264";
        final Optional<Integer> season = ShowInfoMatcher.matchSeason(testCase);
        Assertions.assertTrue(season.isPresent());
        Assertions.assertEquals(1, season.get());
    }

    @Test
    public void testMatchSeasonTwoDigits() {
        final String testCase = "some.random.tv.show.s10e01.1080p.bluray.x264";
        final Optional<Integer> season = ShowInfoMatcher.matchSeason(testCase);
        Assertions.assertTrue(season.isPresent());
        Assertions.assertEquals(10, season.get());
    }

    @Test
    public void testMatchSeasonNoMatch() {
        final String testCase = "some.random.tv.show.e01.1080p.bluray.x264";
        final Optional<Integer> season = ShowInfoMatcher.matchSeason(testCase);
        Assertions.assertFalse(season.isPresent());
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
    public void testMatchEpisodesEDualHyphen() {
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
    public void testMatchEpisodesEDualEHyphen() {
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
    public void testMatchEpisodesEDualNoZeroPaddingHyphen() {
        final String testCase = "some.random.tv.show.s01e1-e2.1080p.bluray.x264";
        final List<Integer> episodes = ShowInfoMatcher.matchEpisodes(testCase);
        final List<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        Assertions.assertEquals(expected, episodes);
    }

    @Test
    public void testMatchEpisodesEDual3DigitsHyphen() {
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
}
