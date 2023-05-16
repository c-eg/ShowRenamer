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

package uk.co.conoregan.showrenamer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.prefs.Preferences;

/**
 * Wrapper class for the Preferences API, for Show Renamer preferences.
 */
public class ShowRenamerPreferences {
    /**
     * The default movie rename format.
     */
    public static final String DEFAULT_MOVIE_RENAME_FORMAT = "{title} ({year})";

    /**
     * The default tv show rename format.
     */
    public static final String DEFAULT_TV_SHOW_RENAME_FORMAT = "{title} - S{season}E{episode} - {episodeName}";

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ShowRenamerPreferences.class);

    /**
     * The user preferences.
     */
    private static final Preferences userPreferences = Preferences.userNodeForPackage(ShowRenamerPreferences.class);

    /**
     * The movie rename format preference name.
     */
    private static final String MOVIE_RENAME_FORMAT_PREFERENCE_NAME = "RENAME_FORMAT_MOVIE";
    
    /**
     * The tv show rename format preference name.
     */
    private static final String TV_SHOW_FORMAT_PREFERENCE_NAME = "RENAME_FORMAT_TV_SHOW";

    /**
     * Gets the movie rename format preference.
     *
     * @return the movie rename format
     */
    @Nonnull
    public static String getMovieRenameFormat() {
        return userPreferences.get(MOVIE_RENAME_FORMAT_PREFERENCE_NAME, DEFAULT_MOVIE_RENAME_FORMAT);
    }

    /**
     * Sets the movie rename format preference.
     *
     * @param format the movie rename format.
     */
    public static void setMovieRenameFormat(@Nonnull final String format) {
        userPreferences.put(MOVIE_RENAME_FORMAT_PREFERENCE_NAME, format);
        LOGGER.info(String.format("Rename format for movies set: '%s'", format));
    }

    /**
     * Clears the movie rename format preference.
     */
    public static void clearMovieRenameFormat() {
        userPreferences.remove(MOVIE_RENAME_FORMAT_PREFERENCE_NAME);
        LOGGER.info("Rename format for movies reset");
    }

    /**
     * Gets the tv show rename format preference.
     *
     * @return the tv show rename format
     */
    @Nonnull
    public static String getTvShowRenameFormat() {
        return userPreferences.get(TV_SHOW_FORMAT_PREFERENCE_NAME, DEFAULT_TV_SHOW_RENAME_FORMAT);
    }

    /**
     * Sets the tv show rename format preference.
     *
     * @param format the tv show rename format.
     */
    public static void setTvShowRenameFormat(@Nonnull final String format) {
        userPreferences.put(TV_SHOW_FORMAT_PREFERENCE_NAME, format);
        LOGGER.info(String.format("Rename format for tv shows set: '%s'", format));
    }

    /**
     * Clears the tv show rename format preference.
     */
    public static void clearTvShowRenameFormat() {
        userPreferences.remove(TV_SHOW_FORMAT_PREFERENCE_NAME);
        LOGGER.info("Rename format for tv shows reset");
    }
}
