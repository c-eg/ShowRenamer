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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.prefs.Preferences;

/**
 * Wrapper class for the Preferences API, for Show Renamer preferences.
 */
public enum ShowRenamerPreference {
    RENAME_FORMAT_MOVIE("RENAME_FORMAT_MOVIE", "{title} ({year})"),
    RENAME_FORMAT_TV_SHOW("RENAME_FORMAT_TV_SHOW", "{title} - S{season}E{episode} - {episodeName}");

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ShowRenamerPreference.class);

    /**
     * The user preferences.
     */
    private static final Preferences userPreferences = Preferences.userNodeForPackage(ShowRenamerPreference.class);

    /**
     * Preference name.
     */
    private final String name;

    /**
     * Preference default value;
     */
    private final String defaultValue; //todo try to make generic

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
     * Gets the preference if set, or the default value.
     *
     * @return the preference.
     */
    @Nonnull
    public String getPreference() {
        return userPreferences.get(getName(), getDefaultValue());
    }

    /**
     * Sets a preference.
     *
     * @param value the value.
     */
    public void setPreference(@Nonnull final String value) {
        userPreferences.put(getName(), value);
        LOGGER.info(String.format("Preference %s, set to: '%s'", getName(), value));
    }

    /**
     * Removes the preference stored in the user preferences.
     */
    public void removePreference() {
        userPreferences.remove(getName());
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

    /**
     * Returns the preference name.
     *
     * @return to string.
     */
    @Nonnull
    @Override
    public String toString() {
        return name;
    }
}
