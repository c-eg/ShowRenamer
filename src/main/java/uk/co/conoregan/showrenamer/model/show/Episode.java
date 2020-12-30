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
 * Class to represent an episode from a tv show.
 *
 * @author c-eg
 */
public class Episode extends Show {
    private String episodeName = null;
    private int seasonNumber;
    private int episodeNumber;

    /**
     * Episode constructor.
     *
     * @param title         show title
     * @param seasonNumber  season number
     * @param episodeNumber episode number
     */
    public Episode(final String title, final int seasonNumber,
                   final int episodeNumber) {
        super(title);
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
    }

    /**
     * Gets episode name.
     *
     * @return String episode name
     */
    public String getEpisodeName() {
        return episodeName;
    }

    /**
     * Sets episode name.
     *
     * @param episodeName String episode name
     */
    public void setEpisodeName(final String episodeName) {
        this.episodeName = episodeName;
    }

    /**
     * Gets season number.
     *
     * @return int season number
     */
    public int getSeasonNumber() {
        return seasonNumber;
    }

    /**
     * Sets seaon number.
     *
     * @param seasonNumber int season number
     */
    public void setSeasonNumber(final int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    /**
     * Gets episode number.
     *
     * @return int episode number
     */
    public int getEpisodeNumber() {
        return episodeNumber;
    }

    /**
     * Sets episode number.
     *
     * @param episodeNumber int episode number
     */
    public void setEpisodeNumber(final int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    /**
     * Episode String format.
     *
     * @return String
     */
    @Override
    public String toString() {
        return String.format("%s - S%02dE%02d - %s",
                super.getTitle(),
                this.getSeasonNumber(),
                this.getEpisodeNumber(),
                this.episodeName);
    }
}
