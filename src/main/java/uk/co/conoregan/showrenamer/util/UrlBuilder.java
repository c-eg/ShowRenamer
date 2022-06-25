package uk.co.conoregan.showrenamer.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

public class UrlBuilder {
    /**
     * Builds a URL request from in the format "{baseUrl}?{key}={value}{delimiter}{key}={value}"
     *
     * @param baseUrl   the base url, e.g. https://www.google.co.uk/
     * @param values    map containing the parameters and their values.
     * @param delimiter the delimiter to split the values map by.
     * @return built url.
     */
    public static String buildUrl(String baseUrl, Map<String, String> values, String delimiter, String charset) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder(baseUrl).append("?");
        ArrayList<String> parameters = new ArrayList<>();

        for (Map.Entry<String, String> entry : values.entrySet()) {
            parameters.add(String.format("%s=%s", entry.getKey(), URLEncoder.encode(entry.getValue(), charset)));
        }

        url.append(String.join(delimiter, parameters));
        return url.toString();
    }
}
