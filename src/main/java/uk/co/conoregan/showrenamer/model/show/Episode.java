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

public class Episode {
    private String episodeId = null;
    private String name = null;
    private int number;

    /*
     * Constructors
     */
    public Episode(int number) {
        this.number = number;
    }

    public Episode(String episodeId, int number) {
        this.episodeId = episodeId;
        this.number = number;
    }

    public Episode(String episodeId, String name, int number) {
        this.episodeId = episodeId;
        this.name = name;
        this.number = number;
    }
    /*
     * End of Constructors
     */

    /*
     * Accessor Methods
     */
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getEpisodeId() {
        return this.episodeId;
    }
    /*
     * End of Accessor Methods
     */

    public Episode merge(Episode other) {
        if (this.episodeId == null && other.getEpisodeId() != null)
            this.episodeId = other.episodeId;
        if (this.name == null && other.getName() != null) {
            this.name = other.name;
        }

        return this;
    }

    @Override
    public String toString() {
        return String.valueOf(this.number);
    }
}
