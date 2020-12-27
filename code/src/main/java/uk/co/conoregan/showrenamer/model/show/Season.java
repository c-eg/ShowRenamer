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

public class Season {
    private final TreeMap<Integer, Episode> episodes;
    private String seasonId = null;
    private String name = null;
    private int number;

    /*
     * Constructors
     */
    private Season() {
        this.episodes = new TreeMap<>();
    }

    public Season(int number) {
        this();
        this.number = number;
    }

    public Season(String seasonId, int number) {
        this();
        this.seasonId = seasonId;
        this.number = number;
    }

    public Season(String seasonId, String name, int number) {
        this();
        this.seasonId = seasonId;
        this.name = name;
        this.number = number;
    }

    public Season(String seasonId, int number, Episode... episodes) {
        this();
        this.seasonId = seasonId;
        this.number = number;

        for (Episode episode : episodes) {
            this.episodes.put(episode.getNumber(), episode);
        }
    }

    public Season(String seasonId, String name, int number, Episode... episodes) {
        this();
        this.seasonId = seasonId;
        this.name = name;
        this.number = number;

        for (Episode episode : episodes) {
            this.episodes.put(episode.getNumber(), episode);
        }
    }
    /*
     * End of Constructors
     */

    public String getSeasonId() {
        return this.seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
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

    public Episode getEpisode(int number) {
        return this.episodes.get(number);
    }

    public void add(Episode other) {
        this.episodes.merge(other.getNumber(), other, Episode::merge);
    }

    public Season merge(Season other) {
        for (Episode episode : other.episodes.values()) {
            this.episodes.merge(episode.getNumber(), episode, Episode::merge);
        }

        return this;
    }

    @Override
    public String toString() {
        return this.number + ": " + this.episodes.values();
    }
}
