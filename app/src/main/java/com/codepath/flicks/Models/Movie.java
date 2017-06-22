package com.codepath.flicks.Models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by mayajey on 6/21/17.
 */

@Parcel // annotation indicates class is Parcelable
public class Movie {
    // instance variables that track values from API
    String title;
    String overview;
    // not full URL; just ending path to image
    String posterPath;
    // backdrop partial path
    String backdropPath;

    // no-arg, empty constructor required for Parceler
    public Movie() {}

    // Constructor; init from JSON data
    public Movie (JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
}
