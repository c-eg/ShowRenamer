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

import java.util.Objects;

public class Episode implements Comparable<Episode> {
    private String episodeId;
    private String name;
    private int number;

    public Episode(String episodeId, String name, int number) {
        this.episodeId = episodeId;
        this.name = name;
        this.number = number;
    }

    public Episode(String episodeId, int number) {
        this.episodeId = episodeId;
        this.number = number;
    }

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

    @Override
    public int compareTo(Episode other) {
        return Integer.compare(this.number, other.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(episodeId, number);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        else if (!(o instanceof Episode))
            return false;
        else {
            Episode other = (Episode) o;
            return this.episodeId.equals(other.episodeId) &&
                    this.number == other.number;
        }
    }
}
