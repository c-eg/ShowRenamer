package MVC.model;

import java.util.ArrayList;

public class TVShow extends Show
{
    private ArrayList<ArrayList<String>> episodeNames;

    public TVShow(String title, String id, ArrayList<ArrayList<String>> episodeNames)
    {
        super(title, id);
        this.episodeNames = episodeNames;
    }

    @Override
    public String toString()
    {
        return super.getTitle();
    }

    public ArrayList<ArrayList<String>> getAllEpisodeNames()
    {
        return episodeNames;
    }

    public ArrayList<String> getSeasonEpisodeNames(int season)
    {
        return episodeNames.get(season - 1);
    }

    public String getEpisodeName(int season, int eposide)
    {
        return episodeNames.get(season - 1).get(eposide - 1);
    }
}
