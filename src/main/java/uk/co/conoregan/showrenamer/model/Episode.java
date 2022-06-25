package uk.co.conoregan.showrenamer.model;

public class Episode extends Show {
    private final int season;
    private final int episode;
    private final String name;

    public Episode(String title, int season, int episode, String name) {
        super(title);
        this.season = season;
        this.episode = episode;
        this.name = name;
    }

    public int getSeason() {
        return season;
    }

    public int getEpisode() {
        return episode;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "title=" + super.getTitle() +
                ", season=" + season +
                ", episode=" + episode +
                ", name='" + name + '\'' +
                '}';
    }
}
