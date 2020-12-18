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

package uk.co.conoregan.showrenamer.utils.api.converters;

import org.json.JSONObject;
import uk.co.conoregan.showrenamer.model.show.Episode;
import uk.co.conoregan.showrenamer.model.show.Movie;
import uk.co.conoregan.showrenamer.model.show.Season;
import uk.co.conoregan.showrenamer.utils.api.ResultContainer;

import java.util.ArrayList;

public interface APIToShowConverter {
    /**
     * Converts the JSONObject of the searched title to a ResultContainer
     *
     * @param showSearch showSearch results from api call
     * @return ResultContainer
     */
    ArrayList<ResultContainer> getResults(JSONObject showSearch);

    /**
     * Converts the JSONObject of movieInfo passed to Movie
     *
     * @param movieInfo movieInfo resuls from api call
     * @return Movie
     */
    Movie getMovie(JSONObject movieInfo);

    /**
     * Converts the JSONObject of seasonInfo passed to a Season
     *
     * @param seasonInfo seasonInfo resuls from api call
     * @return Season containing episodes
     */
    Season getSeason(JSONObject seasonInfo);

    /**
     * Converts the JSONObject of episodeInfo passed to Episode
     *
     * @param episodeInfo episodeInfo resuls from api call
     * @return Episode
     */
    Episode getEpisode(JSONObject episodeInfo);
}
