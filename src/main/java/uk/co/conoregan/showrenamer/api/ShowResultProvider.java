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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Interface to implement for a movie/tv show api.
 */
public interface ShowResultProvider {
    /**
     * Gets a show result. Can be movie or tv show.
     *
     * @param title the title.
     * @param year the release date year.
     * @param language the language.
     * @return the results.
     */
    Optional<ShowResult> getShowResult(@Nonnull final String title, @Nullable final Integer year, @Nullable final String language);

    /**
     * Gets a tv show episode result.
     *
     * @param title the title.
     * @param seasonNumber the season number.
     * @param episodeNumber the episode number.
     * @param year the release date year.
     * @param language the language.
     * @return the new suggested file name.
     */
    Optional<TvEpisodeResult> getTvEpisodeResult(@Nonnull final String title, final int seasonNumber, final int episodeNumber,
                                                 @Nullable final Integer year, @Nullable final String language);

    /**
     * Type of show.
     */
    enum ShowType {
        MOVIE,
        TV
    }

    /**
     * Generic show result, can be movie or tv show.
     *
     * @param title the title of the show.
     * @param date the date the show was released.
     * @param type the type of show.
     */
    record ShowResult(String title, LocalDate date, ShowType type) {
    }

    /**
     * Tv episode result.
     *
     * @param title the title of the show.
     * @param date the date the show was released.
     * @param episodeName the episode's name.
     * @param seasonNumber the season number.
     * @param episodeNumber the episode number.
     */
    record TvEpisodeResult(String title, LocalDate date, String episodeName, int seasonNumber, int episodeNumber) {
    }
}
