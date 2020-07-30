package MVC;

import MVC.model.Movie;
import MVC.model.Show;
import MVC.model.TVShow;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ShowInfoFromAPI
{
    private static String getAPI_KEY()
    {
        try
        {
            JSONParser p = new JSONParser();

            org.json.simple.JSONObject a = (org.json.simple.JSONObject) p.parse(new FileReader("src/MVC/utils/api_key.json"));
            return a.get("api_key").toString();
        }
        catch (Exception e)
        {
            System.out.println("API KEY file not found, please contact c-eg\n");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Testing
     */
    public static void main(String[] args) throws IOException, ParseException
    {
        //ArrayList<Show> testShows = getShows("the umbrella academy");
        ArrayList<Show> testShows = getShows("hello");

        Show s = testShows.get(0);

        if (s instanceof TVShow)
        {
            for (int i = 0; i < ((TVShow) s).getAllEpisodeNames().size(); i++)
            {
                System.out.println(((TVShow) s).getAllEpisodeNames().get(i));
            }
        }
        else
        {
            System.out.println(s);
        }
    }

    /**
     * Function to get a list of Shows from the name passed
     *
     * @param name title/name of the show being searched
     * @return An ArrayList of Shows that match the searched name
     */
    public static ArrayList<Show> getShows(String name) throws IOException
    {
        ArrayList<Show> shows = new ArrayList<>();

        // format the request properly
        // replace all occurrences of " " with "%20"
        name = name.replaceAll("[ ]", "%20");

        // set up api request
        final String API_KEY = getAPI_KEY();
        URL url = new URL("https://api.themoviedb.org/3/search/multi?api_key=" + API_KEY + "&language=en-US&query=" + name + "&page=1&include_adult=true");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        String readLine;

        // if the request returns something
        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            // reading the response from the api
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            while ((readLine = in.readLine()) != null)
            {
                response.append(readLine);
            }
            in.close();

            // creating a json object from the response
            JSONObject result = new JSONObject(response.toString());

            JSONArray jArray = (JSONArray) result.get("results");
            JSONObject temp;

            // looping through each show returned
            for (int i = 0; i < jArray.length(); i++)
            {
                temp = (JSONObject) jArray.get(i);

                // if the show is a movie
                if (temp.get("media_type").equals("movie"))
                {
                    shows.add(new Movie(temp.get("title").toString(),
                            temp.get("id").toString(),
                            temp.get("release_date").toString())
                    );
                }
                // if the show is a tv show
                else if (temp.get("media_type").equals("tv"))
                {
                    String id = temp.get("id").toString();
                    shows.add(new TVShow(temp.get("name").toString(),
                            id,
                            getEpisodeNames(id))
                    );
                }
            }
        }
        // if the request returns nothing
        else
        {
            System.out.println("HTTP response was not OK");
        }

        return shows;
    }

    /**
     * Function get a list of the episode names for all seasons of the show
     *
     * @param id id of the show (TheMovieDatabase.org id)
     * @return A 2D ArrayList of all episode names,
     * index 0, 0 would be season 1 episode 1
     */
    private static ArrayList<ArrayList<String>> getEpisodeNames(String id) throws IOException
    {
        ArrayList<ArrayList<String>> episodeNames = new ArrayList<>();

        // set up api request
        final String API_KEY = getAPI_KEY();
        URL url = new URL("https://api.themoviedb.org/3/tv/" + id + "?api_key=" + API_KEY + "&language=en-US");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        String readLine;

        // if the request returns something
        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            // reading the response from the api
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            while ((readLine = in.readLine()) != null)
            {
                response.append(readLine);
            }
            in.close();

            // creating a json object from the response
            JSONObject result = new JSONObject(response.toString());
            JSONArray jArray = (JSONArray) result.get("seasons");

            // looping through each season returned
            for (int i = 1; i < jArray.length(); i++)
            {
                // get the episode names from that season
                episodeNames.add(getSeasonInfo(id, i));
            }
        }
        // if the request returns nothing
        else
        {
            System.out.println("HTTP response was not OK");
        }

        return episodeNames;
    }

    /**
     * Function to get the episode names of a specific season
     *
     * @param id     id of the show (TheMovieDatabase.org id)
     * @param season season to search for
     * @return An ArrayList of episode names for the season passed
     */
    private static ArrayList<String> getSeasonInfo(String id, int season) throws IOException
    {
        ArrayList<String> episodeNames = new ArrayList<>();

        // set up api request
        final String API_KEY = getAPI_KEY();
        URL url = new URL("https://api.themoviedb.org/3/tv/" + id + "/season/" + season + "?api_key=" + API_KEY);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        String readLine;

        // if the request returns something
        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            // reading the response from the api
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            while ((readLine = in.readLine()) != null)
            {
                response.append(readLine);
            }
            in.close();

            // creating a json object from the response
            JSONObject result = new JSONObject(response.toString());

            JSONArray jArray = (JSONArray) result.get("episodes");
            JSONObject temp = null;

            // looping through each episode returned
            for (int i = 0; i < jArray.length(); i++)
            {
                // add the episode name to the array list
                temp = (JSONObject) jArray.get(i);
                episodeNames.add(temp.get("name").toString());
            }
        }

        return episodeNames;
    }

//    public static String generateTVFilename(JSONObject tvShowInformation)
//    {
//        String result;
//
//        String showName = tvShowInformation.get("name").toString();
//
//        String season = tvShowInformation.get("season_number").toString();
//        int seasonNumber = Integer.parseInt(season);
//
//        String episode = tvShowInformation.get("episode_number").toString();
//        int episodeNumber = Integer.parseInt(episode);
//
//        String episodeName = tvShowInformation.get("name").toString();
//
//        result = String.format("%s - S%02dE%02d - %s%n", showName, seasonNumber, episodeNumber, episodeName);
//
//        return result;
//    }
//
//    public static String generateMovieFileName(JSONObject filmInformation)
//    {
//        String result;
//
//        String filmName = filmInformation.get("title").toString();
//
//        String dateString = filmInformation.get("release_date").toString();
//
//        LocalDate dateReleased = LocalDate.parse(dateString);
//        int yearReleased = dateReleased.getYear();
//
//        result = String.format("%s (%d)", filmName, yearReleased);
//
//        return result;
//    }
}
