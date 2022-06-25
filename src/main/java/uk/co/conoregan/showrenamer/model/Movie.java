package uk.co.conoregan.showrenamer.model;

public class Movie extends Show {
    private final int year;

    public Movie(String title, int year) {
        super(title);
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title=" + super.getTitle() +
                ", year=" + year +
                '}';
    }
}
