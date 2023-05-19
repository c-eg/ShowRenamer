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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

/**
 * Wrapper class for the Property API, for Show Renamer properties.
 */
public class PropertyService {
    /**
     * The path to the properties file.
     */
    private static final String PROPERTIES_PATH = "/properties/show-renamer.properties";

    /**
     * Properties cache to load properties once.
     */
    private static final Cache<ShowRenamerProperty, String> PROPERTY_CACHE;

    static {
        PROPERTY_CACHE = Caffeine.newBuilder()
                .maximumSize(1_000)
                .expireAfterWrite(Duration.ofMinutes(10))
                .build();

    }

    /**
     * Gets a property.
     *
     * @param showRenamerProperty the property
     * @return the value.
     */
    @Nonnull
    public String getProperty(@Nonnull final ShowRenamerProperty showRenamerProperty) {
        return PROPERTY_CACHE.get(showRenamerProperty, this::loadProperty);
    }

    /**
     * Loads a property from the properties file.
     *
     * @param showRenamerProperty the property.
     * @return the value.
     */
    @Nonnull
    private String loadProperty(@Nonnull final ShowRenamerProperty showRenamerProperty) {
        final InputStream res = getClass().getResourceAsStream(PROPERTIES_PATH);
        final Properties properties = new Properties();

        try {
            properties.load(res);
        } catch (IOException e) {
            // file does not exist
            throw new RuntimeException(e);
        }

        final String property = properties.getProperty(showRenamerProperty.getName());

        if (property == null) {
            // property does not exist
            throw new RuntimeException(String.format("The property: '%s' was not found in the properties file: '%s'",
                    showRenamerProperty.getName(), PROPERTIES_PATH));
        }

        return property;
    }
}
