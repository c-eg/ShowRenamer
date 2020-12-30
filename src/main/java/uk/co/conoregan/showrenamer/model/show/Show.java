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

package uk.co.conoregan.showrenamer.model.show;

/**
 * Abstract Class to represent a Show.
 *
 * @author c-eg
 */
public abstract class Show {
    private String title;
    private String id = null;

    /**
     * Show constructor.
     *
     * @param title title of show
     */
    public Show(final String title) {
        this.title = title;
    }

    /**
     * Gets show title.
     *
     * @return String title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets show id.
     *
     * @return String id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets show id.
     *
     * @param id String
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Show String format.
     *
     * @return the string
     */
    public abstract String toString();
}

