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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.prefs.Preferences;

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
     * Cache for preferences.
     */
    private static final Cache<String, String> PREFERENCE_CACHE;

    /**
     * The user preferences.
     */
    private static final Preferences USER_PREFERENCES = Preferences.userNodeForPackage(PreferenceService.class);

    static {
        PREFERENCE_CACHE = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(Duration.ofMinutes(10))
                .build();

    }

    /**
     * Gets a preference value.
     *
     * @param preference the preference.
     * @return the preference, or default value if preference does not exist.
     */
    @Nonnull
    public String getPreference(@Nonnull final ShowRenamerPreference preference) {
        return getPreference(preference.getName(), preference.getDefaultValue());
    }

    /**
     * Gets a preference value.
     *
     * @param preference the preference key.
     * @param defaultValue the default value.
     * @return the preference, or default value if preference does not exist.
     */
    @Nonnull
    public String getPreference(@Nonnull final String preference, @Nonnull final String defaultValue) {
        return PREFERENCE_CACHE.get(preference, key -> loadPreference(preference, defaultValue));
    }

    /**
     * Sets a preference value.
     *
     * @param preference the preference.
     * @param value the value.
     */
    public void setPreference(@Nonnull final ShowRenamerPreference preference, @Nonnull final String value) {
        setPreference(preference.getName(), value);
    }

    /**
     * Sets a preference value.
     *
     * @param preference the preference.
     * @param value the value.
     */
    public void setPreference(@Nonnull final String preference, @Nonnull final String value) {
        USER_PREFERENCES.put(preference, value);
        PREFERENCE_CACHE.put(preference, value);
        LOGGER.info(String.format("Preference %s, set to: '%s'", preference, value));
    }

    /**
     * Remove a preference.
     *
     * @param preference the preference.
     */
    public void removePreference(@Nonnull final ShowRenamerPreference preference) {
        removePreference(preference.getName());
    }

    /**
     * Remove a preference.
     *
     * @param preference the preference.
     */
    public void removePreference(@Nonnull final String preference) {
        USER_PREFERENCES.remove(preference);
        PREFERENCE_CACHE.invalidate(preference);
        LOGGER.info(String.format("Preference %s was removed", preference));
    }

    /**
     * Loads the preference from the user preferences.
     *
     * @param preference the preference.
     * @param defaultValue the default value.
     * @return the preference, or default value if preference does not exist.
     */
    @Nonnull
    private String loadPreference(@Nonnull final String preference, @Nonnull final String defaultValue) {
        return USER_PREFERENCES.get(preference, defaultValue);
    }
}
