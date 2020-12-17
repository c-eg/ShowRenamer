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

package uk.co.conoregan.showrenamer.utils.converters;

import org.json.JSONObject;
import uk.co.conoregan.showrenamer.model.show.Movie;
import uk.co.conoregan.showrenamer.model.show.TVShow;

import java.util.ArrayList;

public interface APIToShowConverter {
    /**
     * Converts the JSONObject of movieInfo passed to Movie object
     * @param movieInfo movieInfo resuls from api call
     * @return Movie
     */
    Movie getMovie(JSONObject movieInfo);

    /**
     * Converts the JSONObject of seasonInfo passed to an ArrayList of TVShow object
     * @param seasonInfo seasonInfo resuls from api call
     * @return TVShows
     */
    ArrayList<TVShow> getTVSeason(JSONObject seasonInfo);

    /**
     * Converts the JSONObject of episodeInfo passed to TVShow object
     * @param episodeInfo episodeInfo resuls from api call
     * @return TVShow
     */
    TVShow getTVShow(JSONObject episodeInfo);
}
