package uk.co.conoregan.showrenamer.model;

public class TVShow extends Show
{
    public TVShow(String title, String id)
    {
        super(title, id);
    }

    @Override
    public String toString()
    {
        return super.getTitle();
    }
}
