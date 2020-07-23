import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.model.tv.TVBasic;
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo;
import com.omertron.themoviedbapi.model.tv.TVInfo;
import com.omertron.themoviedbapi.results.ResultList;

import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShowInfoFromAPI
{
    private TheMovieDbApi tmdbAPI = new TheMovieDbApi("4858e6f6fc8c1fee583788ee12130340");

    public ShowInfoFromAPI() throws MovieDbException
    {
    }

    public static void main(String[] args) throws MovieDbException, IOException
    {
        URL url = new URL("https://api.themoviedb.org/3/search/multi?api_key=4858e6f6fc8c1fee583788ee12130340&language=en-US&query=misfits&page=1&include_adult=true");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        String readLine;

        JSONObject jobj;

        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();

            while ((readLine = in.readLine()) != null)
            {
                response.append(readLine);
            }
            in.close();

            jobj = new JSONObject(response.toString());

            JSONArray jArray = (JSONArray) jobj.get("results");
            JSONObject obj2 = (JSONObject) jArray.get(0);

            if (obj2.get("media_type").equals("movie"))
            {
                String title = obj2.get("title").toString();
                String year = obj2.get("release_date").toString().substring(0, 4);

                System.out.println(title + " (" + year + ")");
            }
            else if (obj2.get("media_type").equals("tv"))
            {
                String name = obj2.get("original_name").toString();
                int id = Integer.parseInt(obj2.get("id").toString());
                String episode = null;
                String season = null;
                String episodeName = null;

                URL url2 = new URL("https://api.themoviedb.org/3/tv/" + id + "/season/1/episode/1?api_key=4858e6f6fc8c1fee583788ee12130340&language=en-US");
                HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
                con2.setRequestMethod("GET");
                int responseCode2 = con2.getResponseCode();
                String readLine2;

                JSONObject json2;

                if (responseCode2 == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader in2 = new BufferedReader(new InputStreamReader(con2.getInputStream()));
                    StringBuilder response2 = new StringBuilder();

                    while ((readLine2 = in2.readLine()) != null)
                    {
                        response2.append(readLine2);
                    }
                    in2.close();

                    json2 = new JSONObject(response2.toString());

                    System.out.println(json2.toString());

                    season = json2.get("season_number").toString();
                    episode = json2.get("episode_number").toString();
                    episodeName = json2.get("name").toString();
                }

                System.out.printf("%s - S%dE%d - %s%n", name, season, episode, episodeName);
            }
            else
            {
                System.out.println(obj2);
            }
        }
        else
        {
            System.out.println("GET NOT WORKED");
        }



//        TheMovieDbApi tmdbAPI = new TheMovieDbApi("4858e6f6fc8c1fee583788ee12130340");
//        System.out.println(tmdbAPI.searchMulti("127 hours", 1, "en", true).getResults().get(0).getMediaType());
//        System.out.println(tmdbAPI.getTVInfo(44115, "en"));

//        ShowInfoFromAPI t = new ShowInfoFromAPI();
//        System.out.println(t.getTVEpisodeInformation("Breaking bad", 1, 2).getName());
//
//        System.out.println(t.getMovieInformation("fury").getTitle() + " " + t.getMovieInformation("fury").getReleaseDate().substring(0, 4));
    }

    /**
     * Gets the information about a movie passed
     * @param movieTitle title of the movie
     * @return MovieInfo object containing information about movie
     */
    public MovieInfo getMovieInformation(String movieTitle) throws MovieDbException
    {
        return tmdbAPI.searchMovie(movieTitle, 1, "en", true, 0, 0, null).getResults().get(0);
    }

    /**
     * Gets the information about a tv show passed
     * @param tvName name of the tv show
     * @return TVInfo object containing information about the movie
     */
    public TVInfo getTVInformation(String tvName) throws MovieDbException
    {
        ResultList<TVBasic> tvShows = tmdbAPI.searchTV(tvName, 1, "en", 0, null);
        TVBasic tvBasicInfo = tvShows.getResults().get(0);
        int tvId = tvBasicInfo.getId();

        // gets the info
        return tmdbAPI.getTVInfo(tvId, "en");
    }

    /**
     * Gets the information about the tv show passed
     * @param tvName name of the tv show
     * @param seasonNumber season number of tv show
     * @param episodeNumber episode number of tv show
     * @return TVEpisodeInfo object containing information about the tv episode
     */
    public TVEpisodeInfo getTVEpisodeInformation(String tvName, int seasonNumber, int episodeNumber) throws MovieDbException
    {
        // gets the info
        TVInfo tvInfo = getTVInformation(tvName);
        int tvId = tvInfo.getId();

        // gets the episode info
        return tmdbAPI.getEpisodeInfo(tvId, seasonNumber, episodeNumber, "en"); // S, E
    }

//    public static void test() throws MovieDbException
//    {
//        /*
//         * Movie stuff
//         */
//        // initialises TheMovieDatabase API
//        TheMovieDbApi tmdbAPI = new TheMovieDbApi("4858e6f6fc8c1fee583788ee12130340");
//
//        // gets the information about the movie specified
//        MovieInfo movieInfo = tmdbAPI.searchMovie("127", 1, "en", true, 0, 0, null).getResults().get(0);
//
//        // gets the tv title and release date
//        System.out.println(movieInfo.getTitle());
//        System.out.println(movieInfo.getReleaseDate()); // yyyy-mm-dd
//
//        /*
//         * TV stuff
//         */
//        // gets the tv show
//        ResultList<TVBasic> tvShows = tmdbAPI.searchTV("misfits.s01e05.HDTV.x264", 1, "en", 0, null);
//        TVBasic tvBasicInfo = tvShows.getResults().get(0);
//        int tvId = tvBasicInfo.getId();
//
//        // gets the name
//        TVInfo tvInfo = tmdbAPI.getTVInfo(tvId, "en");
//        System.out.println(tvInfo.getName());
//
//        // gets the episode name
//        TVEpisodeInfo episodeInfo = tmdbAPI.getEpisodeInfo(tvId, 1, 1, "en"); // S, E
//        System.out.println(episodeInfo.getName());
//    }
}
