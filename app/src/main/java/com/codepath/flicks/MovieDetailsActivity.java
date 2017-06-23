package com.codepath.flicks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.flicks.Models.Config;
import com.codepath.flicks.Models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    // movie to show details about + instance variables
    Movie movie;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageButton ibBackdrop;
    String imageUrl;
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        // retrieve & unwrap movie
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        config = (Config) Parcels.unwrap(getIntent().getParcelableExtra(Config.class.getSimpleName()));
        // log that we are showing info about this movie
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));
        // find info by ID
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        // set backdrop
        ibBackdrop = (ImageButton) findViewById(R.id.ibBackdrop);
        imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        // load backdrop image using Glide
        Glide.with(this)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(this, 15, 0))
                //.placeholder(placeholderId)
                //.error(placeholderId)
                .into(ibBackdrop);

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        ibBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             onClickBackdrop();
            }
        });
    }

    protected void onClickBackdrop() {
        // Communicate btwn activities -- adapter & showing details
        Intent intent = new Intent(this, MovieTrailerActivity.class);
        // use parceler to wrap movie
        intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
        intent.putExtra(Config.class.getSimpleName(), Parcels.wrap(config));
        // show the activity
        startActivity(intent);
    }
}
