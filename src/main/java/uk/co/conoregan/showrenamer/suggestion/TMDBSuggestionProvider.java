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
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Multi;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.conoregan.showrenamer.util.Validation;
import uk.co.conoregan.showrenamer.util.ShowInfoMatcher;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * The Movie Database api show provider.
 */
public class TMDBSuggestionProvider implements ShowSuggestionProvider {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TMDBSuggestionProvider.class);

    /**
     * The movie database api wrapper object.
     */
    private final TmdbApi tmdbApi;

    /**
     * Should not use, only for testing.
     */
    protected TMDBSuggestionProvider(@Nonnull final TmdbApi tmdbApi) {
        this.tmdbApi = tmdbApi;
    }

    /**
     * Constructor.
     */
    public TMDBSuggestionProvider(@Nonnull final String apikey) {
        tmdbApi = new TmdbApi(apikey);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Optional<String> getSuggestion(@Nonnull final String fileName) {
        final Optional<String> matchedTitle = ShowInfoMatcher.matchTitle(fileName);

        if (matchedTitle.isEmpty()) {
            LOGGER.info(String.format("No title match found for file name: %s", fileName));
            return Optional.empty();
        }

        final List<Multi> results = tmdbApi.getSearch().searchMulti(matchedTitle.get(), "en-US", 1).getResults();

        if (!Validation.isGenericListValid(results)) {
            LOGGER.info(String.format("No result found for title: %s", matchedTitle.get()));
            return Optional.empty();
        }

        final Multi result = results.get(0);
        final Multi.MediaType mediaType = result.getMediaType();

        if (mediaType == Multi.MediaType.MOVIE) {
            final MovieDb movie = (MovieDb) result;
            final String title = movie.getTitle();
            final String releaseDate = movie.getReleaseDate();

            if (Validation.isStringVarargsValid(title, releaseDate)) {
                return String.format("%s (%s)", title, releaseDate.substring(0, 4)).describeConstable();
            }
        } else if (mediaType == Multi.MediaType.TV_SERIES) {
            final Optional<Integer> matchedSeason = ShowInfoMatcher.matchSeason(fileName);
            final List<Integer> matchedEpisodes = ShowInfoMatcher.matchEpisodes(fileName);
            if (matchedSeason.isEmpty() || !Validation.isIntegerValid(matchedSeason.get())
                    || !Validation.isIntegerListValid(matchedEpisodes)) {
                LOGGER.info(String.format("No valid season or episode could be matched from the file name: %s", fileName));
                return Optional.empty();
            }

            final TvSeries tvSeries = (TvSeries) result;
            final TvEpisode episode = tmdbApi.getTvEpisodes().getEpisode(
                    tvSeries.getId(), matchedSeason.get(), matchedEpisodes.get(0), "en-US");
            final String seriesName = tvSeries.getName();
            final String episodeName = episode.getName();
            final int seasonNumber = episode.getSeasonNumber();
            final int episodeNumber = episode.getEpisodeNumber();

            if (Validation.isIntegerVarargsValid(seasonNumber, episodeNumber)
                    && Validation.isStringVarargsValid(seriesName, episodeName)) {
                return String.format("%s - S%02dE%02d - %s", seriesName, seasonNumber, episodeNumber, episodeName).describeConstable();
            }
        }

        return Optional.empty();
    }
}
