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
    private final TreeSet<Season> seasons;

    public TVShow(String title, String id) {
        super(title, id);
        this.seasons = new TreeSet<>(new Season.CompareSeasonNumber());
    }

    public Season getSeason(int number) {
        for (Season season : seasons) {
            if (season.getNumber() == number)
                return season;
        }

        // TODO return SeasonNotFoundException
        return null;
    }

    public void addSeason(Season season) {
        seasons.add(season);
    }

    public void addTVShow(TVShow show) {

    }

    public boolean containsSeason(Season other) {
        return this.seasons.contains(other);
    }

    @Override
    public Iterator<Season> iterator() {
        return seasons.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        else if (!(o instanceof TVShow))
            return false;
        else {
            TVShow other = (TVShow) o;
            return this.getId().equals(other.getId()) &&
                    this.getTitle().equals(other.getTitle());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getTitle());
    }
}
