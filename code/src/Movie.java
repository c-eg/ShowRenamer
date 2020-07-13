import com.omertron.themoviedbapi.MovieDbException;

import java.io.File;

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
        }
        catch (MovieDbException e)
        {
            e.printStackTrace();
        }
        return (title + " " + "(" + year + ")");
    }
}
