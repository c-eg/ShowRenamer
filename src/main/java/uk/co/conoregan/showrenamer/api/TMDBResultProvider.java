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
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Multi;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * The Movie Database api show provider.
 */
public class TMDBResultProvider implements ShowResultProvider {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TMDBResultProvider.class);

    /**
     * The movie database api wrapper object.
     */
    private final TmdbApi tmdbApi;

    /**
     * Should not use, only for testing.
     */
    protected TMDBResultProvider(final TmdbApi tmdbApi) {
        this.tmdbApi = tmdbApi;
    }

    /**
     * Constructor.
     */
    public TMDBResultProvider(final String apikey) {
        tmdbApi = new TmdbApi(apikey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ShowResult> getShowResult(final String title, final Integer year, final String language) {
        final List<Multi> tmdbMultiResults = tmdbApi.getSearch().searchMulti(title, language, 1).getResults();

        if (tmdbMultiResults.isEmpty()) {
            return Optional.empty();
        }

        final Multi tmdbMultiResult = tmdbMultiResults.get(0);
        ShowResult showResult = null;

        switch (tmdbMultiResult.getMediaType()) {
            case MOVIE -> {
                final MovieDb movieDb = ((MovieDb) tmdbMultiResult);
                final String movieDbTitle = movieDb.getTitle();
                final LocalDate movieReleaseDate = parseDate(movieDb.getReleaseDate());
                showResult = new ShowResult(movieDbTitle, movieReleaseDate, ShowType.MOVIE);
            }
            case TV_SERIES -> {
                final TvSeries tvSeries = ((TvSeries) tmdbMultiResult);
                final String tvSeriesName = tvSeries.getName();
                final LocalDate tvSeriesReleaseDate = parseDate(tvSeries.getFirstAirDate());
                showResult = new ShowResult(tvSeriesName, tvSeriesReleaseDate, ShowType.TV);
            }
        }

        return Optional.ofNullable(showResult);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<TvEpisodeResult> getTvEpisodeResult(final String title, final int seasonNumber, final int episodeNumber,
                                                        final Integer year, final String language) {
        final int tmdbYear = year == null ? 0 : year;
        final List<TvSeries> tmdbTvEpisodeResults = tmdbApi.getSearch().searchTv(title, tmdbYear, language, true, 1).getResults();

        if (tmdbTvEpisodeResults.isEmpty()) {
            return Optional.empty();
        }

        final TvSeries tvSeries = tmdbTvEpisodeResults.get(0);
        final TvEpisode episode = tmdbApi.getTvEpisodes().getEpisode(tvSeries.getId(), seasonNumber, episodeNumber, language);

        final String seriesName = tvSeries.getName();
        final LocalDate date = parseDate(tvSeries.getFirstAirDate());
        final String episodeName = episode.getName();

        final TvEpisodeResult tvEpisodeResult = new TvEpisodeResult(seriesName, date, episodeName, seasonNumber, episodeNumber);
        return Optional.of(tvEpisodeResult);
    }

    /**
     * Parses a date retrieved from the movie/tv show api to a LocaleDate.
     *
     * @param date the date.
     * @return the locale date.
     */
    public static LocalDate parseDate(final String date) {
        return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    }
}
