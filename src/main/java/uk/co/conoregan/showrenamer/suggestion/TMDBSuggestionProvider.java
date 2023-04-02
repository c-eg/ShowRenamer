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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Multi;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.conoregan.showrenamer.controller.RenameController;
import uk.co.conoregan.showrenamer.util.ResultValidator;
import uk.co.conoregan.showrenamer.util.ShowInfoMatcher;

import javax.annotation.Nonnull;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class TMDBSuggestionProvider implements ShowSuggestionProvider {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TMDBSuggestionProvider.class);

    /**
     * The movie database api wrapper object.
     */
    private static final TmdbApi TMDB_API;

    private static final Cache<String, List<Multi>> SHOW_RESULTS_CACHE;

    private static final int SHOW_RESULT_CACHE_MAX_SIZE = 10_000;

    static {
        // load properties config
        final String apiKeysPath = "properties/api_keys.properties";
        final URL res = RenameController.class.getClassLoader().getResource(apiKeysPath);
        if (res == null) {
            throw new UncheckedIOException(new FileNotFoundException(apiKeysPath));
        }

        final URI uri;
        try {
            uri = res.toURI();
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }

        final Properties properties = new Properties();
        try (InputStream is = Files.newInputStream(Paths.get(uri))) {
            properties.load(is);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load resource", e);
        }

        // make tmdb api
        final String tmdbApiKeyPropertyName = "TMDB_API_KEY_V3";
        final String tmdbApiKey = properties.getProperty(tmdbApiKeyPropertyName);
        if (tmdbApiKey == null) {
            LOGGER.error(String.format("The property: '%s' was not found in the properties file: %s.",
                    tmdbApiKeyPropertyName, apiKeysPath));
            System.exit(0);
        }
        TMDB_API = new TmdbApi(tmdbApiKey);

        SHOW_RESULTS_CACHE = Caffeine.newBuilder()
                .maximumSize(SHOW_RESULT_CACHE_MAX_SIZE)
                .build();
    }

    @Override
    public Optional<String> getSuggestion(@Nonnull final String fileName) {
        final Optional<String> matchedTitle = ShowInfoMatcher.matchTitle(fileName);

        if (matchedTitle.isEmpty()) {
            LOGGER.info(String.format("No title match found for file name: %s", fileName));
            return Optional.empty();
        }

        // todo: check this is working as expected as it is run by a thread.
        final List<Multi> results = SHOW_RESULTS_CACHE.get(matchedTitle.get(), t ->
                TMDB_API.getSearch().searchMulti(matchedTitle.get(), "en-US", 1).getResults());

        if (!ResultValidator.isGenericListValid(results)) {
            LOGGER.info(String.format("No result found for title: %s", matchedTitle.get()));
            return Optional.empty();
        }

        final Multi result = results.get(0);
        final Multi.MediaType mediaType = result.getMediaType();

        if (mediaType == Multi.MediaType.MOVIE) {
            final MovieDb movie = (MovieDb) result;
            final String title = movie.getTitle();
            final String releaseDate = movie.getReleaseDate();

            if (ResultValidator.isStringVarargsValid(title, releaseDate)) {
                return String.format("%s (%s)", title, releaseDate).describeConstable();
            }
        } else if (mediaType == Multi.MediaType.TV_SERIES) {
            final Optional<Integer> matchedSeason = ShowInfoMatcher.matchSeason(fileName);
            final List<Integer> matchedEpisodes = ShowInfoMatcher.matchEpisodes(fileName);
            if (matchedSeason.isEmpty() || !ResultValidator.isIntegerValid(matchedSeason.get())
                    || !ResultValidator.isIntegerListValid(matchedEpisodes)) {
                LOGGER.info(String.format("No valid season or episode could be matched from the file name: %s", fileName));
                return Optional.empty();
            }

            final TvSeries tvSeries = (TvSeries) result;
            final TvEpisode episode = TMDB_API.getTvEpisodes().getEpisode(
                    tvSeries.getId(), matchedSeason.get(), matchedEpisodes.get(0), "en-US");
            final String seriesName = tvSeries.getName();
            final String episodeName = episode.getName();
            final int seasonNumber = episode.getSeasonNumber();
            final int episodeNumber = episode.getEpisodeNumber();

            if (ResultValidator.isIntegerVarargsValid(seasonNumber, episodeNumber)
                    && ResultValidator.isStringVarargsValid(seriesName, episodeName)) {
                return String.format("%s - S%02dE%02d - %s", seriesName, seasonNumber, episodeNumber, episodeName).describeConstable();
            }
        }

        return Optional.empty();
    }
}
