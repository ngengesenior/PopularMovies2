package com.example.ngenge.popularmovies.room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.ngenge.popularmovies.models.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MoviesRepository moviesRepository;
    private LiveData<List<Movie>> allMovies;
    public MovieViewModel(@NonNull Application application) {
        super(application);
        moviesRepository = new MoviesRepository(application);
        allMovies = moviesRepository.getAllMovies();
    }

    public void insert(Movie movie)
    {
        moviesRepository.insert(movie);
    }

    public void delete(Movie movie)
    {
        moviesRepository.delete(movie);
    }

    public Movie getMovie(Integer integer)
    {
        return moviesRepository.getMovie(integer);
    }

    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }

    public boolean isMovieFavorited(int id)
    {
        return this.getMovie(id) != null;
    }
}
