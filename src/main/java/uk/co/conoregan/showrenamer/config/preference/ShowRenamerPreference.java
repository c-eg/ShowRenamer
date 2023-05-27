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

import javax.annotation.Nonnull;

/**
 * Wrapper class for the Preferences API, for Show Renamer preferences.
 */
public enum ShowRenamerPreference {
    RENAME_FORMAT_MOVIE("rename.format.movie", "{title} ({year})"),
    RENAME_FORMAT_TV_SHOW("rename.format.tv.show", "{title} ({year})"),
    RENAME_FORMAT_TV_SHOW_EPISODE("rename.format.tv.show.episode", "{title} ({year}) - S{season}E{episode} - {episodeName}");

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
    ShowRenamerPreference(@Nonnull final String preference, @Nonnull final String defaultValue) {
        this.name = preference;
        this.defaultValue = defaultValue;
    }

    /**
     * Gets name of preference.
     *
     * @return the preference name.
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * Gets default value of preference.
     *
     * @return the default value.
     */
    @Nonnull
    public String getDefaultValue() {
        return defaultValue;
    }
}
