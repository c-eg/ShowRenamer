import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class ShowInfoFromAPI
{
    public static void main(String[] args) throws IOException
    {
        JSONObject showInformation = getShowInformation("the inbetweeners");
        //JSONObject showInformation = getShowInformation("misfits");
        JSONObject tvShowInformation = null;

        if (showInformation.get("media_type").equals("tv"))
        {
            tvShowInformation = getTVShowInformation((int) showInformation.get("id"), 5, 1);
        }

        System.out.println(showInformation);
        System.out.println(tvShowInformation);

        //System.out.println("\n\n\n" + generateFilmFileName(showInformation));
        //System.out.println("\n\n\n" + generateTVFilename(tvShowInformation));
    }

    public static JSONObject getShowInformation(String show) throws IOException
    {
        // format show name into url format
        show = show.replaceAll("[ ]", "%20");

        final String API_KEY = "4858e6f6fc8c1fee583788ee12130340";
        URL url = new URL("https://api.themoviedb.org/3/search/multi?api_key=" + API_KEY + "&language=en-US&query=" + show + "&page=1&include_adult=true");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        String readLine;

        JSONObject output = new JSONObject();

        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            while ((readLine = in.readLine()) != null)
            {
                response.append(readLine);
            }
            in.close();

            JSONObject result = new JSONObject(response.toString());

            JSONArray jArray = (JSONArray) result.get("results");
            JSONObject obj2 = (JSONObject) jArray.get(0);

            if (obj2.get("media_type").equals("movie"))
            {
                output.put("media_type", "movie");
                output.put("title", obj2.get("title").toString());
                output.put("release_date", obj2.get("release_date").toString());
                output.put("id", obj2.get("id"));
            }
            else if (obj2.get("media_type").equals("tv"))
            {
                output.put("media_type", "tv");
                output.put("name", obj2.get("name").toString());
                output.put("id", obj2.get("id"));
            }
        }
        else
        {
            System.out.println("HTTP response was not OK");
        }

        return output;
    }

    public static JSONObject getTVShowInformation(int id, int season, int episode) throws IOException
    {
        final String API_KEY = "4858e6f6fc8c1fee583788ee12130340";
        URL url = new URL("https://api.themoviedb.org/3/tv/" + id + "/season/" + season + "/episode/" + episode + "?api_key=" + API_KEY + "&language=en-US");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        String readLine;

        JSONObject output = new JSONObject();

        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            while ((readLine = in.readLine()) != null)
            {
                response.append(readLine);
            }
            in.close();

            JSONObject result = new JSONObject(response.toString());

            output.put("season_number", result.get("season_number"));
            output.put("episode_number", result.get("episode_number"));
            output.put("name", result.get("name"));
        }
        else
        {
            System.out.println("HTTP response was not OK");
        }

        return output;
    }

    public static String generateTVFilename(JSONObject tvShowInformation) {
        String result = "";

        String showName = tvShowInformation.get("name").toString();

        String season = tvShowInformation.get("season_number").toString();
        int seasonNumber = Integer.parseInt(season);

        String episode = tvShowInformation.get("episode_number").toString();
        int episodeNumber = Integer.parseInt(episode);

        String episodeName = tvShowInformation.get("name").toString();

        result = String.format("%s - S%02dE%02d - %s%n", showName, seasonNumber, episodeNumber, episodeName);

        return result;
    }

    public static String generateFilmFileName(JSONObject filmInformation) {
        String result = "";

        String filmName = filmInformation.get("title").toString();

        String dateString = filmInformation.get("release_date").toString();

        LocalDate dateReleased = LocalDate.parse(dateString);
        int yearReleased = dateReleased.getYear();

        result = String.format("%s (%d)", filmName, yearReleased);

        return result;
    }
}
