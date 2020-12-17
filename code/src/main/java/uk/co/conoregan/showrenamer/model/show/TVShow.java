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

public class TVShow extends Show {
    private String episodeName;
    private int seasonNumber;
    private int episodeNumber;

    public TVShow(String title, String id) {
        super(title, id);
        this.seasonNumber = -1;
        this.episodeNumber = -1;
        this.episodeName = null;
    }

    public TVShow(String title, String id, int seasonNumber, int episodeNumber) {
        super(title, id);
        this.episodeName = null;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
    }

    public TVShow(String title, String id, String episodeName, int seasonNumber, int episodeNumber) {
        super(title, id);
        this.episodeName = episodeName;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
    }

    public String getEpisodeName() {
        return this.episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    public int getSeasonNumber() {
        return this.seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public int getEpisodeNumber() {
        return this.episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    @Override
    public String toString() {
        return super.getTitle();
    }
}
