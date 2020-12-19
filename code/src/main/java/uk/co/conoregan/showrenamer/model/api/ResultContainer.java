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

package uk.co.conoregan.showrenamer.model.api;

public class ResultContainer {
    private final String name;
    private final String id;
    private final ShowType type;

    /**
     * Constructor
     * @param name name of show
     * @param id id of show
     * @param type type of show (enum)
     */
    public ResultContainer(String name, String id, ShowType type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }

    /**
     * ShowType enum to differenciate between different types of show
     */
    public enum ShowType {
        MOVIE, TV;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public ShowType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return String.format("%s, %d, %s", this.name, this.id, this.type.toString());
    }
}
