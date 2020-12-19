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

package uk.co.conoregan.showrenamer.util.api.TheMovieDatabase;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import uk.co.conoregan.showrenamer.exception.ShowNotFoundException;
import uk.co.conoregan.showrenamer.util.api.ShowDatabaseAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TheMovieDB implements ShowDatabaseAPI {
    public TheMovieDB() {}

    /**
     * Function to get the API key from the api_key.json file
     *
     * @return String of API key
     */
    private static String getAPI_KEY() {
        try {
            JSONParser p = new JSONParser();

            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("config/api_key.json");

            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) p.parse(new InputStreamReader(is, StandardCharsets.UTF_8));

            return jsonObject.get("api_key").toString();
        } catch (Exception e) {
            System.out.println("API KEY file not found, please create one on themoviedb.org\n");
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getShowsSearch(String title) throws ShowNotFoundException, IOException {
        // format the request properly
        // replace all occurrences of " " with "%20"
        String reformattedTitle = title.replaceAll("[ ]", "%20");

        // set up api request
        final String API_KEY = getAPI_KEY();
        URL url = new URL("https://api.themoviedb.org/3/search/multi?api_key=" + API_KEY + "&language=en-US&query=" + reformattedTitle + "&page=1&include_adult=true");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        String readLine;

        // if the request returns something
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // reading the response from the api
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();

            // creating a json object from the response
            return new JSONObject(response.toString());
        }
        // if the request returns nothing
        else {
            throw new ShowNotFoundException(title);
        }
    }

    @Override
    public JSONObject getMovieInfo(String id) throws IOException {
        // set up api request
        final String API_KEY = getAPI_KEY();

        URL url = new URL("https://api.themoviedb.org/3/movie/" + id + "?api_key=" + API_KEY + "&language=en-US");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        String readLine;

        // if the request returns something
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // reading the response from the api
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();

            // creating a json object from the response
            return new JSONObject(response.toString());
        }
        // if the request returns nothing
        else {
            System.out.println("HTTP response was not OK");
            return null;
        }
    }

    @Override
    public JSONObject getTVShowSeasonInfo(String id, int seasonNumber) throws IOException {
        // set up api request
        final String API_KEY = getAPI_KEY();

        URL url = new URL("https://api.themoviedb.org/3/tv/" + id + "/season/" + seasonNumber + "?api_key=" + API_KEY + "&language=en-US");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        String readLine;

        // if the request returns something
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // reading the response from the api
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();

            // creating a json object from the response
            return new JSONObject(response.toString());
        }
        // if the request returns nothing
        else {
            System.out.println("HTTP response was not OK");
            return null;
        }
    }

    @Override
    public JSONObject getTVShowEpisodeInfo(String id, int seasonNumber, int episodeNumber) throws IOException {
        // set up api request
        final String API_KEY = getAPI_KEY();

        URL url = new URL("https://api.themoviedb.org/3/tv/" + id +
                "/season/" + seasonNumber +
                "/episode/" + episodeNumber +
                "?api_key=" + API_KEY + "&language=en-US");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        String readLine;

        // if the request returns something
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // reading the response from the api
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();

            // creating a json object from the response
            return new JSONObject(response.toString());
        }
        // if the request returns nothing
        else {
            System.out.println("HTTP response was not OK");
            return null;
        }
    }
}
