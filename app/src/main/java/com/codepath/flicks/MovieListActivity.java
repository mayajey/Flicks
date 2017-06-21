package com.codepath.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.codepath.flicks.Models.Config;
import com.codepath.flicks.Models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    // Constants
    // base URL for API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // API key string
    public final static String API_KEY_PARAM = "api_key";
    // Tag for logging calls
    public final static String TAG = "MovieListsActivity";

    // Instance variables
    AsyncHttpClient client;
    // ArrayList of movies
    ArrayList<Movie> movies;
    // Recycler view
    RecyclerView rvMovies;
    // Adapter wired to recycler view
    MovieAdapter adapter;
    // image config
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        // initialize list of movies
        movies = new ArrayList<>();
        // initialize adapter; after this, movies ArrayList cannot be reinitialized
        adapter = new MovieAdapter(movies);
        // resolve recycler view
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        // layout manager for recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        // set adapter to movies adapter
        rvMovies.setAdapter(adapter);
        // initialize http client every time we make API call
        client = new AsyncHttpClient();
        // get configuration on app creation
        getConfiguration();
    }

    // get list of currently playing movies
    private void getNowPlaying() {
        // Create URL
        String url = API_BASE_URL + "/movie/now_playing";
        // Set request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        // Make GET request; override methods in handler
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    // load array of now playing results
                    JSONArray results = response.getJSONArray("results");
                    // pass every object in results to list of movies
                    for (int i = 0; i < results.length(); i++) {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        // notify data has been changed
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies successfully", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now playing endpoint", throwable, true);
            }
        });
    }

    // get configuration from API
    private void getConfiguration() {
        // Create URL
        String url = API_BASE_URL + "/configuration";
        // Set request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        // Make GET request; override methods in handler
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // get image base url and poster size array --> index 3
                try {
                    // get config
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl and posterSize successfully"));
                    // update config to adapter
                    adapter.setConfig(config);
                    // get the now playing movie list
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);
            }
        });

    }

    // alert user to errors
    private void logError(String message, Throwable error, boolean alertUser) {
        // first log the error
        Log.e(TAG, message, error);
        // alert user if necessary
        if (alertUser) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
