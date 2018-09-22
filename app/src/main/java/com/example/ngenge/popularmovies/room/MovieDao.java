package com.example.ngenge.popularmovies.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.ngenge.popularmovies.models.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getMovies();

    /**
     * @param movie The movie to insert to favorites list
     *
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Query("DELETE FROM movies WHERE id=:movieId")
    void deleteMovieByID(int movieId);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM movies WHERE id=:mId")
    Movie getMovieById(int mId);

}
