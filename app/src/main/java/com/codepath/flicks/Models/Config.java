package com.codepath.flicks.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mayajey on 6/21/17.
 */

public class Config {

    // Secure base url for loading images
    String imageBaseUrl;
    // Poster size
    String posterSize;

    public Config(JSONObject object) throws JSONException {
        // new JSONObject images -- next level of parsing
        JSONObject images = object.getJSONObject("images");
        // URL
        imageBaseUrl = images.getString("secure_base_url");
        // Poster sizes
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        // prevents hardcoding but allows default value
        posterSize = posterSizeOptions.optString(3, "w342");
    }

    // construct image URL -- keep it generic in event of additional size
    public String getImageUrl(String size, String path) {
        return String.format("%s%s%s", imageBaseUrl, size, path);
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }
}
