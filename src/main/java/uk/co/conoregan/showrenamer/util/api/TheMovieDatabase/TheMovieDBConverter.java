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

package uk.co.conoregan.showrenamer.util.api.TheMovieDatabase;

import org.json.JSONArray;
import org.json.JSONObject;
import uk.co.conoregan.showrenamer.model.api.ResultContainer;
import uk.co.conoregan.showrenamer.model.show.Episode;
import uk.co.conoregan.showrenamer.model.show.Movie;
import uk.co.conoregan.showrenamer.model.show.Season;
import uk.co.conoregan.showrenamer.util.api.APIToShowConverter;

import java.util.ArrayList;

public class TheMovieDBConverter implements APIToShowConverter {
    @Override
    public ArrayList<ResultContainer> getResults(JSONObject showSearch) {
        ArrayList<ResultContainer> shows = new ArrayList<>();

        String title = null;
        String id;
        ResultContainer.ShowType type;

        JSONArray resultsArray = showSearch.getJSONArray("results");

        for (int i = 0; i < 3 && i < resultsArray.length(); i++) {
            String typeString = resultsArray.getJSONObject(i).get("media_type").toString().toUpperCase();

            if (typeString.equals("MOVIE") || typeString.equals("TV")) {
                type = ResultContainer.ShowType.valueOf(resultsArray.getJSONObject(i).get("media_type").toString().toUpperCase());
                id = resultsArray.getJSONObject(i).get("id").toString();

                if (type == ResultContainer.ShowType.MOVIE) {
                    title = (String) resultsArray.getJSONObject(i).get("title");
                }
                else if (type == ResultContainer.ShowType.TV) {
                    title = (String) resultsArray.getJSONObject(i).get("name");
                }

                shows.add(new ResultContainer(title, id, type));
            }
        }

        return shows;
    }

    @Override
    public Movie getMovie(JSONObject movieInfo) {
        String title = movieInfo.get("title").toString();
        String id = movieInfo.get("id").toString();
        String releaseDate = movieInfo.get("release_date").toString();

        return new Movie(title, id, releaseDate);
    }

    @Override
    public Season getSeason(JSONObject seasonInfo) {
        String id = seasonInfo.get("id").toString();
        String name = seasonInfo.get("name").toString();
        int number = (int) seasonInfo.get("season_number");

        JSONArray episodes = seasonInfo.getJSONArray("episodes");
        ArrayList<Episode> episodesList = new ArrayList<>();

        for (int i = 0; i < episodes.length(); i++) {
            episodesList.add(this.getEpisode((JSONObject) episodes.get(i)));
        }

        return new Season(id, name, number, episodesList);
    }

    @Override
    public Episode getEpisode(JSONObject episodeInfo) {
        String id = episodeInfo.get("id").toString();
        String name = episodeInfo.get("name").toString();
        int number = (int) episodeInfo.get("episode_number");

        return new Episode(id, name, number);
    }
}
