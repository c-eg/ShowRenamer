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

package uk.co.conoregan.showrenamer.util.api;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TheMovieDB {
    public TheMovieDB() {
    }

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

            return jsonObject.get("TheMovieDatabase_API").toString();
        } catch (Exception e) {
            System.out.println("API KEY file not found, please create one on themoviedb.org\n");
            e.printStackTrace();
            return null;
        }
    }

    public JSONArray getMovieResults(String query, String year, String language, boolean adult) throws IOException {
        // set up api request
        final String API_KEY = getAPI_KEY();

        StringBuilder urlName = new StringBuilder("https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&");

        urlName.append("query=").append(query.replace(" ", "%20")).append("&");
        urlName.append("include_adult=").append(adult).append("&");

        if (year != null) {
            urlName.append("year=").append(year).append("&");
        }
        if (language != null) {
            urlName.append("language=").append(language).append("&");
        }

        urlName.append("page=1");

        URL url = new URL(urlName.toString());

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
            JSONObject result = new JSONObject(response.toString());
            return result.getJSONArray("results");
        }
        // if the request returns nothing
        else {
            System.out.println("HTTP response was not OK");
            return null;
        }
    }

    public JSONObject getTVId(String query) throws IOException {
        // format the request properly
        // replace all occurrences of " " with "%20"
        String reformattedTitle = query.replaceAll("[ ]", "%20");

        // set up api request
        final String API_KEY = getAPI_KEY();
        URL url = new URL("https://api.themoviedb.org/3/search/tv?api_key=" + API_KEY + "&language=en-US&query=" + reformattedTitle + "&page=1&include_adult=true");
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

            return new JSONObject(response.toString());
        }
        // if the request returns nothing
        else {
            System.out.println("HTTP response was not OK");
            return null;
        }
    }

    public JSONObject getEpisodeResults(String id, int season, int episode) throws IOException {
        // set up api request
        final String API_KEY = getAPI_KEY();

        URL url = new URL("https://api.themoviedb.org/3/tv/" + id +
                "/season/" + season +
                "/episode/" + episode +
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

    public static void main(String[] args) throws IOException {
        TheMovieDB test = new TheMovieDB();
        JSONObject result = test.getTVId("the walking dead");
        System.out.println(result.toString());
    }
}
