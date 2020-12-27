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

package uk.co.conoregan.showrenamer.util.api;

import org.json.JSONObject;
import uk.co.conoregan.showrenamer.exception.ShowNotFoundException;

import java.io.IOException;

public interface ShowDatabaseAPI {
    /**
     * Searches the api for shows matching the String
     *
     * @param title title of show to search for
     * @return show names with an associated unique id
     * e.g. but can be in any form
     * {
     * "results": [
     * {
     * "show_name": SHOW_NAME,
     * "show_id": UNIQUE_ID_OF_SHOW,
     * }, etc.
     * ]
     * }
     */
    JSONObject getShowsSearch(String title) throws ShowNotFoundException, IOException;

    /**
     * Gets the movie information of a movie from the api
     *
     * @param id id of movie to search for
     * @return show names with an associated unique id
     * e.g. but can be in any form
     * {
     * "results": [
     * {
     * "show_name": MOVIE_NAME,
     * "show_id": UNIQUE_ID_OF_SHOW,
     * "movie_year" RELEASE_DATE_OF_MOVIE
     * }, etc.
     * ]
     * }
     */
    JSONObject getMovieInfo(String id) throws IOException;

    /**
     * Gets the season information of a tv show from the api
     *
     * @param id           id of show in api
     * @param seasonNumber season number of show
     * @return season information
     * e.g. but can be in any form
     * {
     * "show_title": SHOW_TITLE,
     * "show_id": SHOW_ID,
     * "season_name": SEASON_NAME,
     * "episodes": [
     * {
     * "episode_name": EPISODE_NAME,
     * "season_number": SEASON_NUMBER,
     * "episode_number": EPISODE_NUMBER
     * }, etc.
     * ]
     * }
     */
    JSONObject getTVShowSeasonInfo(String id, int seasonNumber) throws IOException;

    /**
     * Gets the episode information of a tv show from the api
     *
     * @param id            id of show in api
     * @param seasonNumber  season number of show
     * @param episodeNumber episode number of show
     * @return episode information
     * e.g. but can be in any form
     * {
     * "show_title": SHOW_TITLE,
     * "show_id": SHOW_ID,
     * "episode_name": EPISODE_NAME,
     * "season_number": SEASON_NUMBER,
     * "episode_number": EPISODE_NUMBER
     * }
     */
    JSONObject getTVShowEpisodeInfo(String id, int seasonNumber, int episodeNumber) throws IOException;
}
