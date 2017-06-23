package com.codepath.flicks;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.flicks.Models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

import static com.codepath.flicks.MovieListActivity.API_BASE_URL;
import static com.codepath.flicks.MovieListActivity.API_KEY_PARAM;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    Movie movie;
    // Client for API
    AsyncHttpClient client = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        client = new AsyncHttpClient();
        getVideo();
    }

    // API call to youtube ID (first ID if existing)
    public void getVideo() {

        // Set request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, "787f5f4b449efac476d72f5401f14664");
        String url = API_BASE_URL + "/movie/" + movie.getId() + "/videos";
        // Make GET request; override methods in handler
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    // load the movie's youtube id and make the url
                    JSONArray results = response.getJSONArray("results");
                    String key = null;
                    // get the first one whose site is youtube
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject currentVideo = results.getJSONObject(i);
                        // check if it's youtube
                        String actualSite = currentVideo.getString("site");
                        if (actualSite.equals("YouTube")) {
                            key = currentVideo.getString("key");
                            break;
                        }
                    }

                    final String videoId = key;

                    // resolve the player view from the layout
                    YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

                    // initialize with API key stored in secrets.xml
                    playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            YouTubePlayer youTubePlayer, boolean b) {
                            // do any work here to cue video, play video, etc.
                            youTubePlayer.cueVideo(videoId);
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult youTubeInitializationResult) {
                            // log the error
                            Log.e("MovieTrailerActivity", "Error initializing YouTube player");
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // do something here
                Log.e("MovieTrailerActivity", "Json failure");
            }
        });
    }

    // alert user to errors
    private void logError(String message, Throwable error, boolean alertUser) {
        // first log the error
        Log.e("MOVIE_TRAILER_ACTIVITY", message, error);
        // alert user if necessary
        if (alertUser) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }


}
