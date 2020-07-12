import java.io.IOException;

import com.omertron.themoviedbapi.*;
import com.omertron.themoviedbapi.model.movie.MovieInfo;

public class test
{
    public static void main(String[] args) throws MovieDbException
    {
        test();
    }

    public static void test() throws MovieDbException
    {
        // initialises TheMovieDatabase API
        TheMovieDbApi t = new TheMovieDbApi("4858e6f6fc8c1fee583788ee12130340");

        // gets the information about the movie specified
        MovieInfo movieInfo = t.searchMovie("127", 1, "en", true, 0, 0, null).getResults().get(0);

        //int id = movieInfo.getId();

        System.out.println(movieInfo.getTitle());
        System.out.println(movieInfo.getReleaseDate()); // yyyy-mm-dd
    }
}
