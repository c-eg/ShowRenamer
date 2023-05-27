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
package uk.co.conoregan.showrenamer.api;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.TmdbTvEpisodes;
import info.movito.themoviedbapi.TvResultsPage;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Multi;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeries;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link TMDBResultProvider}.
 */
public class TMDBResultProviderTest {
    /**
     * The movie database api.
     */
    private static final TmdbApi TMDB_API = mock(TmdbApi.class);

    /**
     * The movie database api suggestion provider.
     */
    private static ShowResultProvider SHOW_RESULT_PROVIDER;

    @BeforeAll
    public static void setup() {
        SHOW_RESULT_PROVIDER = new TMDBResultProvider(TMDB_API);
    }

    /**
     * Tests {@link TMDBResultProvider#getShowResult(String, Integer, String)} when no results are found.
     */
    @Test
    public void testGetShowResultNoResult() {
        final TmdbSearch tmdbSearch = mock(TmdbSearch.class);
        when(TMDB_API.getSearch()).thenReturn(tmdbSearch);

        final TmdbSearch.MultiListResultsPage multiListResultsPage = mock(TmdbSearch.MultiListResultsPage.class);
        when(tmdbSearch.searchMulti(anyString(), any(), anyInt())).thenReturn(multiListResultsPage);

        final Optional<ShowResultProvider.ShowResult> suggestion = SHOW_RESULT_PROVIDER.getShowResult("title", null, null);
        Assertions.assertTrue(suggestion.isEmpty());
    }

    /**
     * Tests {@link TMDBResultProvider#getShowResult(String, Integer, String)} when a movie result is returned.
     */
    @Test
    public void testGetShowResultMovie() {
        final TmdbSearch tmdbSearch = mock(TmdbSearch.class);
        when(TMDB_API.getSearch()).thenReturn(tmdbSearch);

        final TmdbSearch.MultiListResultsPage multiListResultsPage = mock(TmdbSearch.MultiListResultsPage.class);
        when(tmdbSearch.searchMulti(anyString(), any(), eq(1))).thenReturn(multiListResultsPage);

        final List<Multi> results = new ArrayList<>();
        when(multiListResultsPage.getResults()).thenReturn(results);

        final MovieDb result = mock(MovieDb.class);
        results.add(result);
        final String date = "2010-02-25";
        final LocalDate localeDate = TMDBResultProvider.parseDate(date);
        when(result.getMediaType()).thenReturn(Multi.MediaType.MOVIE);
        when(result.getTitle()).thenReturn("Movie Title");
        when(result.getReleaseDate()).thenReturn(date);

        final Optional<ShowResultProvider.ShowResult> suggestion = SHOW_RESULT_PROVIDER.getShowResult("Movie title", null, null);
        Assertions.assertTrue(suggestion.isPresent());
        Assertions.assertEquals("Movie Title", suggestion.get().title());
        Assertions.assertEquals(localeDate, suggestion.get().date());
    }

    /**
     * Tests {@link TMDBResultProvider#getShowResult(String, Integer, String)} when a tv series result is returned.
     */
    @Test
    public void testGetShowResultTvSeries() {
        final TmdbSearch tmdbSearch = mock(TmdbSearch.class);
        when(TMDB_API.getSearch()).thenReturn(tmdbSearch);

        final TmdbSearch.MultiListResultsPage multiListResultsPage = mock(TmdbSearch.MultiListResultsPage.class);
        when(tmdbSearch.searchMulti(anyString(), any(), eq(1))).thenReturn(multiListResultsPage);

        final List<Multi> results = new ArrayList<>();
        when(multiListResultsPage.getResults()).thenReturn(results);

        final TvSeries result = mock(TvSeries.class);
        results.add(result);
        final String date = "2010-02-25";
        final LocalDate localeDate = TMDBResultProvider.parseDate(date);
        when(result.getMediaType()).thenReturn(Multi.MediaType.TV_SERIES);
        when(result.getName()).thenReturn("Tv Series Title");
        when(result.getFirstAirDate()).thenReturn(date);

        final Optional<ShowResultProvider.ShowResult> suggestion = SHOW_RESULT_PROVIDER.getShowResult("Tv series title", null, null);
        Assertions.assertTrue(suggestion.isPresent());
        Assertions.assertEquals("Tv Series Title", suggestion.get().title());
        Assertions.assertEquals(localeDate, suggestion.get().date());
    }

    /**
     * Tests {@link TMDBResultProvider#getTvEpisodeResult(String, int, int, Integer, String)} when no results are found.
     */
    @Test
    public void testGetTvEpisodeResultNoResult() {
        final TmdbSearch tmdbSearch = mock(TmdbSearch.class);
        when(TMDB_API.getSearch()).thenReturn(tmdbSearch);

        final TvResultsPage tvResultsPage = mock(TvResultsPage.class);
        when(tmdbSearch.searchTv(anyString(), any(), any(), anyBoolean(), anyInt())).thenReturn(tvResultsPage);

        final Optional<ShowResultProvider.TvEpisodeResult> suggestion = SHOW_RESULT_PROVIDER.getTvEpisodeResult("Tv series title", 1, 1, null, null);
        Assertions.assertTrue(suggestion.isEmpty());
    }

    /**
     * Tests {@link TMDBResultProvider#getTvEpisodeResult(String, int, int, Integer, String)}.
     */
    @Test
    public void testGetTvEpisodeResult() {
        final TmdbSearch tmdbSearch = mock(TmdbSearch.class);
        when(TMDB_API.getSearch()).thenReturn(tmdbSearch);

        final TvResultsPage tvResultsPage = mock(TvResultsPage.class);
        when(tmdbSearch.searchTv(anyString(), any(), any(), anyBoolean(), anyInt())).thenReturn(tvResultsPage);

        // tv show
        final List<TvSeries> results = new ArrayList<>();
        when(tvResultsPage.getResults()).thenReturn(results);

        final TvSeries result = mock(TvSeries.class);
        final String date = "2010-02-25";
        final LocalDate localeDate = TMDBResultProvider.parseDate(date);
        when(result.getName()).thenReturn("Tv Series Title");
        when(result.getFirstAirDate()).thenReturn(date);
        results.add(result);

        // tv episode
        final TmdbTvEpisodes tmdbTvEpisodes = mock(TmdbTvEpisodes.class);
        when(TMDB_API.getTvEpisodes()).thenReturn(tmdbTvEpisodes);

        final TvEpisode tvEpisode = mock(TvEpisode.class);
        when(tmdbTvEpisodes.getEpisode(anyInt(), anyInt(), anyInt(), any())).thenReturn(tvEpisode);
        when(tvEpisode.getName()).thenReturn("Episode Name");

        final Optional<ShowResultProvider.TvEpisodeResult> suggestion =
                SHOW_RESULT_PROVIDER.getTvEpisodeResult("Tv series title", 1, 1, null, null);
        Assertions.assertTrue(suggestion.isPresent());
        Assertions.assertEquals("Tv Series Title", suggestion.get().title());
        Assertions.assertEquals(1, suggestion.get().seasonNumber());
        Assertions.assertEquals(1, suggestion.get().episodeNumber());
        Assertions.assertEquals("Episode Name", suggestion.get().episodeName());
        Assertions.assertEquals(localeDate, suggestion.get().date());
    }
}
