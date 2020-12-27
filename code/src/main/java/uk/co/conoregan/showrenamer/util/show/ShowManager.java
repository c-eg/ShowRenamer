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
import uk.co.conoregan.showrenamer.model.show.Episode;
import uk.co.conoregan.showrenamer.model.show.Season;
import uk.co.conoregan.showrenamer.model.show.Show;
import uk.co.conoregan.showrenamer.model.show.TVShow;
import uk.co.conoregan.showrenamer.util.api.TheMovieDatabase.TheMovieDB;
import uk.co.conoregan.showrenamer.util.api.TheMovieDatabase.TheMovieDBConverter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ShowManager {

    private final HashSet<Show> shows;

    public ShowManager(List<File> files) {
        this.shows = new HashSet<>();

        for (File f : files) {
            // get show title from ShowInfoMatcher
            ShowInfoMatcher showInfoMatcher = new ShowInfoMatcher(
                    f.getName().substring(0, f.getName().lastIndexOf('.')));
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

            int index = 0;

            // if movie
            if (data.get(index).getType() == ResultContainer.ShowType.MOVIE) {
                try {
                    // api call for movie
                    JSONObject movieInfo = tmdb.getMovieInfo(data.get(index).getId());
                    Show m = theMovieDBConverter.getMovie(movieInfo);
                    shows.add(m);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // if tv
            else if (data.get(index).getType() == ResultContainer.ShowType.TV) {
                // create new TVShow
                TVShow newShow = new TVShow(data.get(index).getName(), data.get(index).getId());
                Season season = new Season(Integer.parseInt(showInfoMatcher.getSeason()));
                Episode episode = new Episode(Integer.parseInt(showInfoMatcher.getEpisode()));

                season.add(episode);
                newShow.add(season);

                // merge if exists
                if (shows.contains(newShow)) {
                    for (Show show : shows) {
                        if (show.equals(newShow))
                            ((TVShow) show).merge(newShow);
                    }
                }
                // add if doesn't exist
                else {
                    shows.add(newShow);
                }
            }
        }

        // loop through all shows
        // if tv
        // for each season in a show, create api call
        // update objects from api data

        // testing
        for (Show s : shows) {
            System.out.println(s);
        }
    }

    public static void main(String[] args) {
        ArrayList<File> files = new ArrayList<>();
        String path1 = "Z:\\TV Shows\\Corporate\\Season 1\\Corporate - S01E01 - The Void.mp4";
        String path2 = "Z:\\TV Shows\\Corporate\\Season 1\\Corporate - S01E02 - The Powerpoint of Death.mp4";
        String path3 = "Z:\\TV Shows\\Corporate\\Season 2\\Corporate - S02E01 - The One Who's There.mp4";
        files.add(new File(path1));
        files.add(new File(path2));
        files.add(new File(path3));

        ShowManager sm = new ShowManager(files);
    }
}
