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

public class Season implements Iterable<Episode>, Comparable<Season> {

    private final TreeSet<Episode> episodes;
    private String seasonId;
    private String name = null;
    private int number;

    public Season() {
        this.episodes = new TreeSet<>(new Episode.CompareEpisodeNumber());
    }

    public Season(int number) {
        this();
        this.number = number;
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

    public Episode getEpisode(int number) {
        for (Episode episode : episodes) {
            if (episode.getNumber() == number) {
                return episode;
            }
        }

        // TODO return EpisodeNotFoundException
        return null;
    }

    public String getName() {
        return this.name;
    }

    public String getSeasonId() {
        return this.seasonId;
    }

    public int getNumber() {
        return this.number;
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
        return Objects.hash(number);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        else if (!(o instanceof Season))
            return false;
        else {
            Season other = (Season) o;
            return this.number == other.number;
        }
    }

    /**
     * Comparator class to order by episode number
     */
    public static class CompareSeasonNumber implements Comparator<Season> {
        @Override
        public int compare(Season one, Season two) {
            return Integer.compare(one.getNumber(), two.getNumber());
        }
    }
}
