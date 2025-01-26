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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Wrapper class for the Property API, for Show Renamer properties.
 * This class should be used for settings that should not be modified while the application is running.
 */
public class PropertyService {
    /** The path to the properties file. */
    private static final String PROPERTIES_PATH = "/properties/show-renamer.properties";

    /**
     * Gets a property.
     *
     * @param property the property.
     * @return the value.
     */
    public String getProperty(final ShowRenamerProperty property) {
        return getProperty(property.getName());
    }

    /**
     * Gets a property.
     *
     * @param property the property.
     * @return the value.
     */
    public String getProperty(final String property) {
        final InputStream res = getClass().getResourceAsStream(PROPERTIES_PATH);
        final Properties properties = new Properties();

        try {
            properties.load(res);
        }
        catch (IOException e) {
            // file does not exist
            throw new RuntimeException(e);
        }

        final String value = properties.getProperty(property);

        if (value == null) {
            // property does not exist
            throw new RuntimeException(String.format("The property: '%s' was not found in the properties file: '%s'",
                    property, PROPERTIES_PATH));
        }

        return value;
    }
}
