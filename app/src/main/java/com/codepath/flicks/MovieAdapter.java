package com.codepath.flicks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.flicks.Models.Config;
import com.codepath.flicks.Models.Movie;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by mayajey on 6/21/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

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

        // URL for poster imge
        String imageUrl = config.getImageUrl(config.getPosterSize(), current.getPosterPath());

        // load image using Glide
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 15, 0))
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .into(holder.ivPosterImage);

    }

    // size of entire data set
    @Override
    public int getItemCount() {
        // do not return zero!
        return movies.size();
    }

    // create viewholder as a nested static class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // instance variables -- track view objects
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(View itemView) {
            super(itemView);
            // init instance variables via ID
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);

        }
    }
}
