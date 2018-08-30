package com.example.ngenge.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ngenge.popularmovies.R;
import com.example.ngenge.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieHolder> {

    public List<Movie> movies;
    public Context context;
    private OnItemClickListener listener;


    public MoviesAdapter(List<Movie> movies, Context context, OnItemClickListener listener) {
        this.movies = movies;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_layout,parent,false);

   return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {

        Movie movie = movies.get(position);
        holder.bind(movie,listener);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    /**
     * This is an interface for item clicks in recyclerview
     */

    public interface OnItemClickListener{
        void onItemClicked(Movie movie);
    }




    static class MovieHolder extends RecyclerView.ViewHolder {

        public ImageView posterImage;
        private  OnItemClickListener listener;

        public MovieHolder(View itemView) {
            super(itemView);

            posterImage = itemView.findViewById(R.id.imageViewPoster);
        }


        public void bind(final Movie movie, final OnItemClickListener itemClickListener)
        {

            Picasso.get()
                    .load(movie.getPoster_path())
                    .into(posterImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemClickListener.onItemClicked(movie);

                }
            });
        }

    }

}
