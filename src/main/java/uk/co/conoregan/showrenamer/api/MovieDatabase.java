package uk.co.conoregan.showrenamer.api;

import uk.co.conoregan.showrenamer.model.Movie;

import java.util.List;

public interface MovieDatabase {
    /**
     * Searches a show database API for movie results.
     *
     * @param title title of show.
     * @param year  year of show.
     * @return A list of movie search results.
     * @throws Exception when an error occurs related to the api.
     */
    List<Movie> findMovieResults(String title, int year) throws Exception;
}
