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

package uk.co.conoregan.showrenamer.util.show;

import org.json.JSONObject;
import uk.co.conoregan.showrenamer.exception.ShowNotFoundException;
import uk.co.conoregan.showrenamer.model.api.ResultContainer;
import uk.co.conoregan.showrenamer.model.show.Show;
import uk.co.conoregan.showrenamer.model.show.TVShow;
import uk.co.conoregan.showrenamer.util.api.TheMovieDatabase.TheMovieDB;
import uk.co.conoregan.showrenamer.util.api.TheMovieDatabase.TheMovieDBConverter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowManager {

    private final ArrayList<Show> shows;

    public ShowManager(List<File> files) {
        this.shows = new ArrayList<>();

        for (File f : files) {
            // get show title from ShowInfoMatcher
            ShowInfoMatcher showInfoMatcher = new ShowInfoMatcher(f.getName().substring(0, f.getName().lastIndexOf('.')));
            String title = showInfoMatcher.getTitle();

            // search for show
            TheMovieDB tmdb = new TheMovieDB();
            JSONObject result = null;
            try {
                result = tmdb.getShowsSearch(title);
            } catch (ShowNotFoundException | IOException e) {
                e.printStackTrace();
            }

            // get results
            TheMovieDBConverter theMovieDBConverter = new TheMovieDBConverter();
            ArrayList<ResultContainer> data = theMovieDBConverter.getResults(result);

            // if movie
            if (data.get(0).getType() == ResultContainer.ShowType.MOVIE) {
                try {
                    // api call for movie
                    JSONObject movieInfo = tmdb.getMovieInfo(data.get(0).getId());
                    Show m = theMovieDBConverter.getMovie(movieInfo);
                    shows.add(m);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // if tv
            else if (data.get(0).getType() == ResultContainer.ShowType.TV) {
                // check if show exists
                // TODO start here, maybe make a function to check if a tv show already exisits
                //  and then add to it if it does, if not create it
                TVShow tvShow = new TVShow(data.get(1).getName(), data.get(1).getId());
                System.out.println(tvShow.getTitle());

                // check if season exists
                // create episode and add
                // create season and episode and add
                // create show, season and episode
            }


            // once all files have been read
            // if tv
            // for each season in a show, create api call
            // update objects from api data
        }
    }

    public static void main(String[] args) {
        ArrayList<File> files = new ArrayList<>();
        files.add(new File("Z:\\TV Shows\\Corporate\\Season 1\\Corporate - S01E01 - The Void.mp4"));

        ShowManager sm = new ShowManager(files);
    }
}
