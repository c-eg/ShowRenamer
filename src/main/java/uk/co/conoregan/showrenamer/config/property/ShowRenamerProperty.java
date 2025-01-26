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

/**
 * Show renamer properties enum.
 * If you add an entry in show-renamer.properties, you should add an entry to this enum.
 */
public enum ShowRenamerProperty {
    /** The movie database api key, version 3. */
    TMDB_API_KEY("tmdb.api.key.v3");

    /** Property name. */
    private final String name;

    /**
     * Constructor.
     *
     * @param name the name of the property.
     */
    ShowRenamerProperty(final String name) {
        this.name = name;
    }

    /**
     * Gets the name of the property.
     *
     * @return the name.
     */
    public String getName() {
        return this.name;
    }
}
