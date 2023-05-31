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

package uk.co.conoregan.showrenamer.config.preference;

/**
 * Wrapper class for the Preferences API, for Show Renamer preferences.
 */
public enum ShowRenamerPreference {
    /**
     * Rename format for movies.
     */
    RENAME_FORMAT_MOVIE("rename.format.movie", "{title} ({year})"),

    /**
     * Rename format for tv shows.
     */
    RENAME_FORMAT_TV_SHOW("rename.format.tv.show", "{title} ({year})"),

    /**
     * Rename format for tv show episodes.
     */
    RENAME_FORMAT_TV_SHOW_EPISODE("rename.format.tv.show.episode", "{title} ({year}) - S{season}E{episode} - {episodeName}"),

    /**
     * Allowed file types.
     */
    ALLOWED_FILE_TYPES("allowed.file.types", "mp4,mkv"),

    /**
     * Whether include sub-folders should be checked.
     */
    CHECKBOX_CHECKED_INCLUDE_SUB_FOLDERS("checkbox.checked.include.sub.folders", "false"),

    /**
     * Whether filter file types should be checked.
     */
    CHECKBOX_CHECKED_FILTER_FILE_TYPES("checkbox.checked.filter.file.types", "false");

    /**
     * Preference name.
     */
    private final String name;

    /**
     * Preference default value;
     */
    private final String defaultValue;

    /**
     * Constructor.
     *
     * @param preference the preference key.
     * @param defaultValue the default value.
     */
    ShowRenamerPreference(final String preference, final String defaultValue) {
        this.name = preference;
        this.defaultValue = defaultValue;
    }

    /**
     * Gets name of preference.
     *
     * @return the preference name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets default value of preference.
     *
     * @return the default value.
     */
    public String getDefaultValue() {
        return defaultValue;
    }
}
