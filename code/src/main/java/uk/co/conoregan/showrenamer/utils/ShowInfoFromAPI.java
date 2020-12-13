/*
 * This file is part of ShowRenamer.
 *
 * ShowRenamer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ShowRenamer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ShowRenamer.  If not, see <https://www.gnu.org/licenses/>.
 */

package uk.co.conoregan.showrenamer.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import uk.co.conoregan.showrenamer.model.Movie;
import uk.co.conoregan.showrenamer.model.Show;
import uk.co.conoregan.showrenamer.model.TVShow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ShowInfoFromAPI
{
    /**
     * Function to get the API key from the api_key.json file
     *
     * @return String of API key
     */
    private static String getAPI_KEY()
    {
        try
        {
            JSONParser p = new JSONParser();

            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("config/api_key.json");

            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) p.parse(new InputStreamReader(is, StandardCharsets.UTF_8));

            return jsonObject.get("api_key").toString();
        }
        catch (Exception e)
        {
            System.out.println("API KEY file not found, please create one on themoviedb.org\n");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Function to get a list of Shows from the name passed in
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

            // looping through top 3 shows or until there are no shows left
            for (int i = 0; i < jArray.length() && i < 2; i++)
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
                    shows.add(new TVShow(temp.get("name").toString(), id));
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
     * Function to get the episode name of a tv show
     *
     * @param id        id of the show (TheMovieDatabase.org id)
     * @param seasonNo  season number of show
     * @param episodeNo episode number of show
     * @return String episode name
     */
    public static String getEpisodeName(String id, int seasonNo, int episodeNo) throws IOException
    {
        // set up api request
        final String API_KEY = getAPI_KEY();
        URL url = new URL("https://api.themoviedb.org/3/tv/" + id +
                "/season/" + seasonNo +
                "/episode/" + episodeNo +
                "?api_key=" + API_KEY + "&language=en-US");
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
            return result.get("name").toString();
        }
        // if the request returns nothing
        else
        {
            System.out.println("HTTP response was not OK");
        }

        return null;
    }
}
