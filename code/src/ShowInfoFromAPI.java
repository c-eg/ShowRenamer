import com.omertron.themoviedbapi.*;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.model.tv.TVBasic;
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo;
import com.omertron.themoviedbapi.model.tv.TVInfo;
import com.omertron.themoviedbapi.results.ResultList;

public class ShowInfoFromAPI
{
    private TheMovieDbApi tmdbAPI = new TheMovieDbApi("4858e6f6fc8c1fee583788ee12130340");

    public ShowInfoFromAPI() throws MovieDbException
    {
    }

    public static void main(String[] args) throws MovieDbException
    {
        Movie m = new Movie(null, "Avengers endgame", null);
        System.out.println(m);
    }

    /**
     * Gets the information about a movie passed
     * @param movieTitle title of the movie
     * @return MovieInfo object containing information about movie
     */
    public MovieInfo getMovieInformation(String movieTitle) throws MovieDbException
    {
        return tmdbAPI.searchMovie(movieTitle, 1, "en", true, 0, 0, null).getResults().get(0);
    }

    /**
     * Gets the information about a tv show passed
     * @param tvName name of the tv show
     * @return TVInfo object containing information about the movie
     */
    public TVInfo getTVInformation(String tvName) throws MovieDbException
    {
        ResultList<TVBasic> tvShows = tmdbAPI.searchTV(tvName, 1, "en", 0, null);
        TVBasic tvBasicInfo = tvShows.getResults().get(0);
        int tvId = tvBasicInfo.getId();

        // gets the info
        return tmdbAPI.getTVInfo(tvId, "en");
    }

    /**
     * Gets the information about the tv show passed
     * @param tvName name of the tv show
     * @param seasonNumber season number of tv show
     * @param episodeNumber episode number of tv show
     * @return TVEpisodeInfo object containing information about the tv episode
     */
    public TVEpisodeInfo getTVEpisodeInformation(String tvName, int seasonNumber, int episodeNumber) throws MovieDbException
    {
        // gets the info
        TVInfo tvInfo = getTVInformation(tvName);
        int tvId = tvInfo.getId();

        // gets the episode info
        return tmdbAPI.getEpisodeInfo(tvId, seasonNumber, episodeNumber, "en"); // S, E
    }

//    public static void test() throws MovieDbException
//    {
//        /*
//         * Movie stuff
//         */
//        // initialises TheMovieDatabase API
//        TheMovieDbApi tmdbAPI = new TheMovieDbApi("4858e6f6fc8c1fee583788ee12130340");
//
//        // gets the information about the movie specified
//        MovieInfo movieInfo = tmdbAPI.searchMovie("127", 1, "en", true, 0, 0, null).getResults().get(0);
//
//        // gets the tv title and release date
//        System.out.println(movieInfo.getTitle());
//        System.out.println(movieInfo.getReleaseDate()); // yyyy-mm-dd
//
//        /*
//         * TV stuff
//         */
//        // gets the tv show
//        ResultList<TVBasic> tvShows = tmdbAPI.searchTV("misfits.s01e05.HDTV.x264", 1, "en", 0, null);
//        TVBasic tvBasicInfo = tvShows.getResults().get(0);
//        int tvId = tvBasicInfo.getId();
//
//        // gets the name
//        TVInfo tvInfo = tmdbAPI.getTVInfo(tvId, "en");
//        System.out.println(tvInfo.getName());
//
//        // gets the episode name
//        TVEpisodeInfo episodeInfo = tmdbAPI.getEpisodeInfo(tvId, 1, 1, "en"); // S, E
//        System.out.println(episodeInfo.getName());
//    }
}
