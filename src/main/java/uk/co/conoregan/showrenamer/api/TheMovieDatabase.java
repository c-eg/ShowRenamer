package uk.co.conoregan.showrenamer.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.co.conoregan.showrenamer.model.Episode;
import uk.co.conoregan.showrenamer.model.Movie;
import uk.co.conoregan.showrenamer.model.Show;
import uk.co.conoregan.showrenamer.util.UrlBuilder;

import java.io.IOException;
import java.net.Authenticator;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

public class TheMovieDatabase implements MovieDatabase, TvDatabase {
    private final String charset;
    private final HttpClient client;

    public TheMovieDatabase() {
        charset = StandardCharsets.UTF_8.name();
        client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    @Override
    public List<Movie> findMovieResults(String title, int year) throws Exception {
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            JSONObject searchResults = searchMovie(title, year, 1, true);
            JSONArray results = searchResults.getJSONArray("results");

            for (Object result : results) {
                JSONObject itemJson = ((JSONObject) result);

                String resTitle = itemJson.getString("title");
                int resYear = Integer.parseInt(itemJson.getString("release_date").substring(0, 4));

                Movie m = new Movie(resTitle, resYear);
                movies.add(m);
            }
        } catch (ApiRequestException e) {
            e.printStackTrace();
        }

        if (movies.size() == 0)
            throw new ShowNotFoundException(String.format("The movie with the title: '%s', year: '%d' could not be found.", title, year));

        return movies;
    }

    @Override
    public List<Episode> findTvEpisodeResults(String title, int season, int episode) throws Exception {
        ArrayList<Episode> episodes = new ArrayList<>();

        try {
            // search for show
            JSONObject searchResults = searchTv(title, 1, true);
            JSONArray results = searchResults.getJSONArray("results");

            // for each show, search for the episode wanted
            for (Object result : results) {
                JSONObject itemJson = ((JSONObject) result);

                // get name and id of show
                String resTitle = itemJson.getString("name");
                int resId = itemJson.getInt("id");

                try {
                    JSONObject searchTvEpisode = searchTvEpisode(resId, season, episode);

                    String episodeName = searchTvEpisode.getString("name");
                    Episode e = new Episode(resTitle, season, episode, episodeName);
                    episodes.add(e);
                }
                catch (ShowNotFoundException e) {}
            }
        } catch (ApiRequestException e) {
            e.printStackTrace();
        }

        if (episodes.size() == 0) {
            throw new ShowNotFoundException(String.format("The tv episode with the title: '%s', season: '%d', episode: '%d' could not be found.", title, season, episode));
        }

        return episodes;
    }

    private JSONObject searchMovie(String query, int year, int page, boolean includeAdult) throws IOException, InterruptedException, ApiRequestException {
        String url = "https://api.themoviedb.org/3/search/movie";

        // build api request
        Map<String, String> params = new TreeMap<>();
        params.put("api_key", "8abbe4944952ab9d0f00f3c5d70cb8e0");
        params.put("query", query);
        params.put("year", String.valueOf(year));
        params.put("page", String.valueOf(page));
        params.put("include_adult", String.valueOf(includeAdult));

        String apiUrl = UrlBuilder.buildUrl(url, params, "&", this.charset);

        // make api request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("content-type", "application/json")
                .build();
        HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return new JSONObject(response.body());
        }

        throw new ApiRequestException(response.body());
    }

    private JSONObject searchTv(String query, int page, boolean includeAdult) throws IOException, InterruptedException, ApiRequestException {
        String url = "https://api.themoviedb.org/3/search/tv";

        // build api request
        Map<String, String> params = new TreeMap<>();
        params.put("api_key", "8abbe4944952ab9d0f00f3c5d70cb8e0");
        params.put("query", query);
        params.put("page", String.valueOf(page));
        params.put("include_adult", String.valueOf(includeAdult));

        String apiUrl = UrlBuilder.buildUrl(url, params, "&", this.charset);

        // make api request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();
        HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return new JSONObject(response.body());
        }

        throw new ApiRequestException(response.body());
    }

    public JSONObject searchTvEpisode(int id, int season, int episode) throws IOException, InterruptedException, ApiRequestException, ShowNotFoundException {
        String url = String.format("https://api.themoviedb.org/3/tv/%d/season/%d/episode/%d", id, season, episode);

        // build api request
        Map<String, String> params = new TreeMap<>();
        params.put("api_key", "8abbe4944952ab9d0f00f3c5d70cb8e0");

        String apiUrl = UrlBuilder.buildUrl(url, params, "&", this.charset);

        // make api request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();
        HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject res = new JSONObject(response.body());

        if (response.statusCode() == 200) {
            return res;
        }

        if (res.getInt("status_code") == 34) {
            throw new ShowNotFoundException(res.getString("status_message"));
        }

        throw new ApiRequestException(response.body());
    }
}
