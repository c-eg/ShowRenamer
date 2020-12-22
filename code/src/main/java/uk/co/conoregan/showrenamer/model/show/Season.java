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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Season implements Iterable<Episode>, Comparable<Season> {

    private final ArrayList<Episode> episodes;
    private String seasonId;
    private String name = null;
    private int number;

    public Season() {
        this.episodes = new ArrayList<>();
    }

    public Season(String seasonId, String name, int number) {
        this();
        this.seasonId = seasonId;
        this.name = name;
        this.number = number;
    }

    public Season(String seasonId, int number) {
        this();
        this.seasonId = seasonId;
        this.number = number;
    }

    public void addEpisode(Episode episode) {
        this.episodes.add(episode);
    }

    public Episode getEpisode(int index) {
        return this.episodes.get(index);
    }

    public String getName() {
        return this.name;
    }

    public String getSeasonId() {
        return this.seasonId;
    }

    @Override
    public Iterator<Episode> iterator() {
        return episodes.iterator();
    }

    @Override
    public int compareTo(Season other) {
        return Integer.compare(this.number, other.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seasonId, name, number);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        else if (!(o instanceof Season))
            return false;
        else {
            Season other = (Season) o;
            return this.seasonId.equals(other.seasonId) &&
                    this.number == other.number;
        }
    }
}
