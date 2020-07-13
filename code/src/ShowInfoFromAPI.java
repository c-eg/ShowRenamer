import java.io.IOException;
import java.util.ArrayList;

import com.omertron.themoviedbapi.*;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.model.tv.TVBasic;
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo;
import com.omertron.themoviedbapi.model.tv.TVInfo;
import com.omertron.themoviedbapi.results.ResultList;

public class ShowInfoFromAPI
{
    /**
     * Think about:
     *  How should I make the functions work?
     *      Should the api just be used in the main class when the files are read in?
     *      Should the api be used in ShowInfoFromAPI and called in Movie and TVShow classes?
     *
     */


    private TheMovieDbApi tmdbAPI = new TheMovieDbApi("4858e6f6fc8c1fee583788ee12130340");

    public ShowInfoFromAPI() throws MovieDbException
    {
    }

    public static void main(String[] args) throws MovieDbException
    {
        //test();
        ShowInfoFromAPI api = new ShowInfoFromAPI();
        System.out.println(api.getMovieInformation("avengers").get(0));
    }

    /**
     * Gets the information about a movie passed
     * @param movieTitle title of the movie
     * @return list of information: 0: title, 1: release date, 2: runtime
     */
    public ArrayList<String> getMovieInformation(String movieTitle) throws MovieDbException
    {
        ArrayList<String> information = new ArrayList<>();

        MovieInfo movieInfo = tmdbAPI.searchMovie(movieTitle, 1, "en", true, 0, 0, null).getResults().get(0);
        information.add(movieInfo.getTitle());
        information.add(movieInfo.getReleaseDate());
        information.add(String.valueOf(movieInfo.getRuntime()));

        return information;
    }



    public static void test() throws MovieDbException
    {
        /*
         * Movie stuff
         */
        // initialises TheMovieDatabase API
        TheMovieDbApi tmdbAPI = new TheMovieDbApi("4858e6f6fc8c1fee583788ee12130340");

        // gets the information about the movie specified
        MovieInfo movieInfo = tmdbAPI.searchMovie("127", 1, "en", true, 0, 0, null).getResults().get(0);

        // gets the tv title and release date
        System.out.println(movieInfo.getTitle());
        System.out.println(movieInfo.getReleaseDate()); // yyyy-mm-dd

        /*
         * TV stuff
         */
        // gets the tv show
        ResultList<TVBasic> tvShows = tmdbAPI.searchTV("misfits.s01e05.HDTV.x264", 1, "en", 0, null);
        TVBasic tvBasicInfo = tvShows.getResults().get(0);
        int tvId = tvBasicInfo.getId();

        // gets the name
        TVInfo tvInfo = tmdbAPI.getTVInfo(tvId, "en");
        System.out.println(tvInfo.getName());

        // gets the episode name
        TVEpisodeInfo episodeInfo = tmdbAPI.getEpisodeInfo(tvId, 1, 1, "en"); // S, E
        System.out.println(episodeInfo.getName());
    }
}
