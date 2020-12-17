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

public class TheMovieDBConverter implements APIToShowConverter {
    @Override
    public Movie getMovie(JSONObject movieInfo) {
        return null;
    }

    @Override
    public Season getSeason(JSONObject seasonInfo) {
        return null;
    }

    @Override
    public Episode getEpisode(JSONObject episodeInfo) {
        return null;
    }
}
