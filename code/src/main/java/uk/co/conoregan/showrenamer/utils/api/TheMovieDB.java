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

package uk.co.conoregan.showrenamer.utils.api;

import org.json.JSONObject;
import uk.co.conoregan.showrenamer.exception.ShowNotFoundException;

public class TheMovieDB implements ShowDatabaseAPI {
    @Override
    public JSONObject getShowsSearch(String title) throws ShowNotFoundException {
        return null;
    }

    @Override
    public JSONObject getMovieInfo(String title) throws ShowNotFoundException {
        return null;
    }

    @Override
    public JSONObject getTVShowSeasonInfo(String id, int seasonNumber) {
        return null;
    }

    @Override
    public JSONObject getTVShowEpisodeInfo(String id, int seasonNumber, int episodeNumber) {
        return null;
    }
}
