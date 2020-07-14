import com.omertron.themoviedbapi.MovieDbException;

import java.io.File;
import java.util.ArrayList;

public class Movie
{
    private File file;
    private String title;
    private String year;

    public Movie(File file, String title, String year)
    {
        this.file = file;
        this.title = title;
        this.year = year;
    }

    @Override
    public String toString()
    {
        try
        {
            ShowInfoFromAPI api = new ShowInfoFromAPI();
            ArrayList<String> info = api.getMovieInformation(title);
            return info.get(0) + " (" + info.get(1).substring(0, 4) + ")";
        }
        catch (MovieDbException e)
        {
            e.printStackTrace();
        }
        return (title + " " + "(" + year + ")");
    }
}
