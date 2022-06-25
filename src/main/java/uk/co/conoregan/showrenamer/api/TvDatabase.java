package uk.co.conoregan.showrenamer.api;

import uk.co.conoregan.showrenamer.model.Episode;

import java.util.List;

public interface TvDatabase {
    /**
     * Searches a show database API for episode results.
     *
     * @param title   title of show.
     * @param season  season of show.
     * @param episode episode of show.
     * @return a list of tv show search results.
     * @throws Exception when an error occurs related to the api.
     */
    List<Episode> findTvEpisodeResults(String title, int season, int episode) throws Exception;
}
