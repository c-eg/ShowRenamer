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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PreferenceService}.
 */
public class PreferenceServiceTest {
    /**
     * The preference service.
     */
    private static final PreferenceService PREFERENCE_SERVICE = new PreferenceService();

    @BeforeAll
    public static void beforeAll() {
        PREFERENCE_SERVICE.setPreference("test", "value");
    }

    @AfterAll
    public static void afterAll() {
        PREFERENCE_SERVICE.removePreference("test");
    }

    /**
     * Tests {@link PreferenceService#getPreference(String, String)}} when a preference exists.
     */
    @Test
    public void testGetPreference() {
        final String value = PREFERENCE_SERVICE.getPreference("test", "default");
        Assertions.assertEquals("value", value);
    }

    /**
     * Test {@link PreferenceService#getPreference(String, String)}} when a preference does not exist.
     */
    @Test
    public void testGetPreferenceNotExist() {
        final String value = PREFERENCE_SERVICE.getPreference("non.existing", "default");
        Assertions.assertEquals("default", value);
    }
}
