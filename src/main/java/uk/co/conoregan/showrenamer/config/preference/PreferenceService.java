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

import java.util.prefs.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper class for the Preferences API, for Show Renamer preferences.
 * This class should be used for settings that can be modified while the application is running.
 */
public class PreferenceService {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PreferenceService.class);

    /**
     * The user preferences.
     */
    private static final Preferences USER_PREFERENCES = Preferences.userNodeForPackage(PreferenceService.class);

    /**
     * Gets a preference value.
     *
     * @param preference the preference.
     * @return the preference, or default value if preference does not exist.
     */
    public String getPreference(final ShowRenamerPreference preference) {
        return getPreference(preference.getName(), preference.getDefaultValue());
    }

    /**
     * Gets a preference value.
     *
     * @param preference the preference key.
     * @param defaultValue the default value.
     * @return the preference, or default value if preference does not exist.
     */
    public String getPreference(final String preference, final String defaultValue) {
        return USER_PREFERENCES.get(preference, defaultValue);
    }

    /**
     * Sets a preference value.
     *
     * @param preference the preference.
     * @param value the value.
     */
    public void setPreference(final ShowRenamerPreference preference, final String value) {
        setPreference(preference.getName(), value);
    }

    /**
     * Sets a preference value.
     *
     * @param preference the preference.
     * @param value the value.
     */
    public void setPreference(final String preference, final String value) {
        USER_PREFERENCES.put(preference, value);
        LOGGER.info("Preference {}, set to: '{}'", preference, value);
    }

    /**
     * Remove a preference.
     *
     * @param preference the preference.
     */
    public void removePreference(final ShowRenamerPreference preference) {
        removePreference(preference.getName());
    }

    /**
     * Remove a preference.
     *
     * @param preference the preference.
     */
    public void removePreference(final String preference) {
        USER_PREFERENCES.remove(preference);
        LOGGER.info("Preference {} was removed", preference);
    }
}
