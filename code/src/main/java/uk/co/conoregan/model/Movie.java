package uk.co.conoregan.model;

public class Movie extends Show
{
    private final String releaseDate;

    public Movie(String title, String id, String releaseDate)
    {
        super(title, id);
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }

    @Override
    public String toString()
    {
        return super.getTitle() + " (" + releaseDate.substring(0, 4) + ")";
    }
}
