package MVC.model;

public abstract class Show
{
    private final String title;
    private final String id;

    public Show(String title, String id)
    {
        this.title = title;
        this.id = id;
    }

    public abstract String toString();

    public String getTitle()
    {
        return title;
    }

    public String getId()
    {
        return id;
    }
}
