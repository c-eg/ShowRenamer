import java.io.IOException;

import com.omertron.themoviedbapi.*;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.model.tv.TVBasic;
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo;
import com.omertron.themoviedbapi.model.tv.TVInfo;
import com.omertron.themoviedbapi.results.ResultList;

public class test
{
    public static void main(String[] args) throws MovieDbException
    {
        test();
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
        ResultList<TVBasic> tvShows = tmdbAPI.searchTV("Misfits", 1, "en", 0, null);
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
