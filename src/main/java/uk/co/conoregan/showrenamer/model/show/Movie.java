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
 * Class to represent an movie.
 *
 * @author c-eg
 */
public class Movie extends Show {
    /**
     * The Release date.
     */
    private String releaseDate = null;

    /**
     * Movie constructor.
     *
     * @param title title of movie
     */
    public Movie(String title) {
        super(title);
    }

    /**
     * Movie constructor.
     *
     * @param title       title of movie
     * @param releaseDate year of movie
     */
    public Movie(String title, String releaseDate) {
        super(title);
        this.releaseDate = releaseDate;
    }

    /**
     * Gets movie release date.
     *
     * @return String release date
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Sets release date.
     *
     * @param releaseDate String release date
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * Movie String format.
     *
     * @return String string
     */
    public String toString() {
        return String.format("%s (%s)", super.getTitle(), this.releaseDate.substring(0, 4));
    }
}
