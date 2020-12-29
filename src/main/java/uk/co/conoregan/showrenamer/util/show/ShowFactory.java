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
import uk.co.conoregan.showrenamer.controller.RenameController;
import uk.co.conoregan.showrenamer.exception.ShowNotFoundException;
import uk.co.conoregan.showrenamer.model.api.ResultContainer;
import uk.co.conoregan.showrenamer.model.show.*;
import uk.co.conoregan.showrenamer.util.api.TheMovieDatabase.TheMovieDB;
import uk.co.conoregan.showrenamer.util.api.TheMovieDatabase.TheMovieDBConverter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ShowFactory {
    private final ArrayList<Show> shows;

    public ShowFactory(List<File> files) throws IOException {
        this.shows = new ArrayList<>();

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
            }
            catch (ShowNotFoundException | IOException e) {
                e.printStackTrace();
            }

            // get results
            TheMovieDBConverter theMovieDBConverter = new TheMovieDBConverter();
            ArrayList<ResultContainer> data = theMovieDBConverter.getResults(result);

            int index = 0; // FOR TESTING PURPOSES ONLY

            if (data.size() > 0) {
                // if movie
                if (data.get(index).getType() == ResultContainer.ShowType.MOVIE) {
                    try {
                        // api call for movie
                        JSONObject movieInfo = tmdb.getMovieInfo(data.get(index).getId());
                        Show m = theMovieDBConverter.getMovie(movieInfo);
                        shows.add(m);
                    }
                    catch (IOException e) {
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
            else {
                shows.add(null);
            }
        }

        TheMovieDB theMovieDB = new TheMovieDB();
        TheMovieDBConverter theMovieDBConverter = new TheMovieDBConverter();

        // loop through all shows
        for (Show show : shows) {
            if (show instanceof TVShow) {
                for (Iterator<Season> it = ((TVShow) show).iterator(); it.hasNext();) {
                    Season season = it.next();

                    // get the season from the api
                    JSONObject tempObj = theMovieDB.getTVShowSeasonInfo(show.getId(), season.getNumber());
                    Season newSeason = theMovieDBConverter.getSeason(tempObj);

                    ArrayList<Episode> toRemove = new ArrayList<>();

                    // remove episodes not already in season
                    for (Episode e : newSeason) {
                        if (season.getEpisode(e.getNumber()) == null) {
                            toRemove.add(e);
                        }
                    }

                    season.merge(newSeason); // merge updated api season to original
                    season.remove(toRemove); // remove episodes not in original
                }
            }
        }
    }

    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();

        for (Show show : shows) {
            if (show == null) {
                names.add(RenameController.ERROR_MESSAGE);
            }
            else if (show instanceof Movie) {
                Movie m = (Movie) show;
                names.add(String.format("%s (%s)", m.getTitle(), m.getReleaseDate().substring(0, 4)));
            }
            else if (show instanceof TVShow) {
                TVShow tvShow = (TVShow) show;

                for (Season season : tvShow) {
                    for (Episode episode : season) {
                        names.add(String.format("%s - S%02dE%02d - %s", tvShow.getTitle(),
                                season.getNumber(), episode.getNumber(), episode.getName()));
                    }
                }
            }
        }

        return names;
    }
}
