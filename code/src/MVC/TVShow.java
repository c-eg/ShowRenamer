package MVC;

import java.io.File;

public class TVShow
{
    private File file;
    private String title;
    private String season;
    private String episode;

    public TVShow(File file, String title, String season, String episode)
    {
        this.file = file;
        this.title = title;
        this.season = season;
        this.episode = episode;
    }

    @Override
    public String toString()
    {
        return (title + " S" + season + " E" + episode);
    }
}
