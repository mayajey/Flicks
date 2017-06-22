package com.codepath.flicks;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.flicks.Models.Config;
import com.codepath.flicks.Models.Movie;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by mayajey on 6/21/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    // list of movies
    ArrayList<Movie> movies;
    // config for URL extraction
    Config config;
    // context
    Context context;

    // initialize with list
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    // creates and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create inflater via getting context from parent
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create view object using inflater; return that wrapped w/ new ViewHolder
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // associates a created or inflated view with a new item at a specific position of a list
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // extract element from specified position
        Movie current = movies.get(position);
        // populate view with data from this movie
        holder.tvTitle.setText(current.getTitle());
        holder.tvOverview.setText(current.getOverview());

        // determine orientation
        boolean isPortrait = ( context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT );
        String imageUrl = null;

        // portrait -- load poster; landscape -- load backdrop
        if (isPortrait) {
           imageUrl = config.getImageUrl(config.getPosterSize(), current.getPosterPath());
        }
        else {
            imageUrl = config.getImageUrl(config.getBackdropSize(), current.getBackdropPath());
        }

        // load correct placeholder and imageview depending on orientation
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;

        // load image using Glide
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 15, 0))
                .placeholder(placeholderId)
                .error(placeholderId)
                .into(imageView);
    }

    // size of entire data set
    @Override
    public int getItemCount() {
        // do not return zero!
        return movies.size();
    }

    // create viewholder as a nested class -- cannot be static for parceling
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // instance variables -- track view objects
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivBackdropImage;

        public ViewHolder(View itemView) {
            super(itemView);
            // init instance variables via ID
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage);
            // when we click on the image, it gives more information (see below)
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Get position of movie being clicked, ensure it's valid
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // Get the movie at that position
                Movie movie = movies.get(position);
                // Communicate btwn activities -- adapter & showing details
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // use parceler to wrap movie
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}
