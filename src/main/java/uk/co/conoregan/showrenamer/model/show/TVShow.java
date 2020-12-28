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

import java.util.*;

public class TVShow extends Show implements Iterable<Season> {
    private final TreeMap<Integer, Season> seasons;

    public TVShow(String title, String id) {
        super(title, id);
        seasons = new TreeMap<>();
    }

    public TVShow(String title, String id, Collection<Season> seasons) {
        this(title, id);
        for (Season season : seasons) {
            this.seasons.put(season.getNumber(), season);
        }
    }

    public Season getSeason(int number) {
        return this.seasons.get(number);
    }

    public void add(Season other) {
        this.seasons.merge(other.getNumber(), other, Season::merge);
    }

    public TVShow merge(TVShow other) {
        for (Season season : other.seasons.values()) {
            this.seasons.merge(season.getNumber(), season, Season::merge);
        }

        return this;
    }

    @Override
    public String toString() {
        return this.seasons.values().toString();
    }

    @Override
    public Iterator<Season> iterator() {
        return this.seasons.values().iterator();
    }
}
