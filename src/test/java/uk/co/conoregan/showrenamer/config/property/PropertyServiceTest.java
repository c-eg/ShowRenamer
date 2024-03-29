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

package uk.co.conoregan.showrenamer.config.property;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PropertyService}.
 */
public class PropertyServiceTest {
    /**
     * The property service.
     */
    private static final PropertyService PROPERTY_SERVICE = new PropertyService();

    /**
     * Tests {@link PropertyService#getProperty(String)} when the property exists.
     */
    @Test
    public void testGetProperty() {
        final String value = PROPERTY_SERVICE.getProperty("test");
        Assertions.assertEquals("value", value);
    }

    /**
     * Tests {@link PropertyService#getProperty(String)} when the property does not exist.
     */
    @Test
    public void testGetPropertyNotExist() {
        Assertions.assertThrows(RuntimeException.class, () -> PROPERTY_SERVICE.getProperty("does.not.exist"));
    }
}
