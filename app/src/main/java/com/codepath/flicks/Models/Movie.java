package com.codepath.flicks.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mayajey on 6/21/17.
 */

public class Movie {
    // instance variables that track values from API
    private String title;
    private String overview;
    // not full URL; just ending path to image
    private String posterPath;

    // Constructor; init from JSON data
    public Movie (JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
    }

    // Getters b/c private instance variables
    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
