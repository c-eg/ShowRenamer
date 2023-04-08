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
package uk.co.conoregan.showrenamer.suggestion;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Multi;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link TMDBSuggestionProvider}.
 */
public class TMDBSuggestionProviderTest {
    private static final ShowSuggestionProvider SHOW_SUGGESTION_PROVIDER = new TMDBSuggestionProvider();
    private static final TmdbApi TMDB_API = mock(TmdbApi.class);

    @BeforeAll
    public static void setup() throws IllegalAccessException {
        FieldUtils.writeField(SHOW_SUGGESTION_PROVIDER, "tmdbApi", TMDB_API, true);
    }

    @Test
    public void testGetSuggestionEmptyTitle() {
        final String fileName = "1080p.x264";
        final Optional<String> suggestion = SHOW_SUGGESTION_PROVIDER.getSuggestion(fileName);
        Assertions.assertFalse(suggestion.isPresent());
    }

    @Test
    public void testGetSuggestionEmptyResults() {
        final TmdbSearch tmdbSearch = mock(TmdbSearch.class);
        when(TMDB_API.getSearch()).thenReturn(tmdbSearch);
        final TmdbSearch.MultiListResultsPage multiListResultsPage = mock(TmdbSearch.MultiListResultsPage.class);
        when(tmdbSearch.searchMulti(anyString(), eq("en-US"), eq(1))).thenReturn(multiListResultsPage);

        final String fileName = "movie.title.2010.1080p.bluray";
        final Optional<String> suggestion = SHOW_SUGGESTION_PROVIDER.getSuggestion(fileName);
        Assertions.assertFalse(suggestion.isPresent());
    }

    @Test
    public void testGetSuggestionMovie() {
        final TmdbSearch tmdbSearch = mock(TmdbSearch.class);
        when(TMDB_API.getSearch()).thenReturn(tmdbSearch);

        final TmdbSearch.MultiListResultsPage multiListResultsPage = mock(TmdbSearch.MultiListResultsPage.class);
        when(tmdbSearch.searchMulti(anyString(), eq("en-US"), eq(1))).thenReturn(multiListResultsPage);

        final List<Multi> results = new ArrayList<>();
        when(multiListResultsPage.getResults()).thenReturn(results);

        final MovieDb result = mock(MovieDb.class);
        results.add(result);
        when(result.getMediaType()).thenReturn(Multi.MediaType.MOVIE);
        when(result.getTitle()).thenReturn("Movie Title");
        when(result.getReleaseDate()).thenReturn("2010");

        final String fileName = "movie.title.2010.1080p.bluray";
        final Optional<String> suggestion = SHOW_SUGGESTION_PROVIDER.getSuggestion(fileName);
        Assertions.assertTrue(suggestion.isPresent());
        Assertions.assertEquals(suggestion.get(), "Movie Title (2010)");
    }
}
