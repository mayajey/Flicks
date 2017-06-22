package com.codepath.flicks.Models;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import cz.msebera.android.httpclient.Header;

import static com.codepath.flicks.MovieListActivity.API_KEY_PARAM;

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
    // average rating of a movie
    Double voteAverage;
    // video ID
    Integer id;
    // Client for API
    AsyncHttpClient client;
    // URL of the current movie's video
    String url = "https://api.themoviedb.org/3/movie/";

    // no-arg, empty constructor required for Parceler
    public Movie() {}

    // Constructor; init from JSON data
    public Movie (JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");
        id = object.getInt("id");
        url = url + id.toString() + "/";
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

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Integer getId() {
        return id;
    }

    // API call to youtube ID (first ID if existing)
    public void getVideo() {
        // Set request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, "787f5f4b449efac476d72f5401f14664");
        // Make GET request; override methods in handler
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    // load the movie's youtube id and make the url
                    JSONArray results = response.getJSONArray("results");
                    // extract first one if existing
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //
            }
        });
    }
}
