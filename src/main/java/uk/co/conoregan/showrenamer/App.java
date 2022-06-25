package uk.co.conoregan.showrenamer;

import uk.co.conoregan.showrenamer.api.ShowNotFoundException;
import uk.co.conoregan.showrenamer.api.TheMovieDatabase;
import uk.co.conoregan.showrenamer.model.Episode;
import uk.co.conoregan.showrenamer.model.Movie;

import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        TheMovieDatabase tmbd = new TheMovieDatabase();

        try {
            List<Movie> movies = tmbd.findMovieResults("2 Fast 2 Furious", 2003);
            for (Movie m : movies) {
                System.out.println(m);
            }
        }
        catch (ShowNotFoundException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("=======================");

        try {
            List<Episode> episodes = tmbd.findTvEpisodeResults("Dexter", 1, 1);
            for (Episode e : episodes) {
                System.out.println(e);
            }
        }
        catch (ShowNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
