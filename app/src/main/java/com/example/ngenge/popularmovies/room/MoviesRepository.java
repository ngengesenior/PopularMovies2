package com.example.ngenge.popularmovies.room;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.ngenge.popularmovies.models.Movie;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MoviesRepository {
    private MovieDao movieDao;
    private LiveData<List<Movie>> allMovies;

    public MoviesRepository(Application application) {

        MoviesDatabase db = MoviesDatabase.getDatabase(application);
        movieDao = db.movieDao();
        allMovies = movieDao.getMovies();
    }


    LiveData<List<Movie>> getAllMovies()
    {
        return allMovies;
    }


    public void insert(Movie movie)
    {
        new insertTask(movieDao)
                .execute(movie);
    }


    public void delete(Movie movie)
    {
        new removeTask(movieDao)
                .execute(movie);
    }


    public Movie getMovie(Integer integer)
    {
        try {
            return new getMovieTask(movieDao).execute(integer).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;

    }





    private static class insertTask extends AsyncTask<Movie, Void, Void> {

        private MovieDao asyncTaskDao;

        insertTask(MovieDao movieDao) {
            asyncTaskDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            asyncTaskDao.insertMovie(movies[0]);
            return null;
        }
    }


    private static class removeTask extends AsyncTask<Movie,Void,Void>{

        private MovieDao asyncTaskDao;

        removeTask(MovieDao movieDao)
        {
            asyncTaskDao = movieDao;
        }
        @Override
        protected Void doInBackground(Movie... movies) {
            asyncTaskDao.deleteMovie(movies[0]);
            return null;
        }
    }


    private static class getMovieTask extends AsyncTask<Integer,Void,Movie>
    {
        private MovieDao asyncTaskDao;
        getMovieTask(MovieDao movieDao)
        {
            asyncTaskDao = movieDao;
        }

        @Override
        protected Movie doInBackground(Integer... integers) {
            return asyncTaskDao.getMovieById(integers[0]);

        }

        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);

        }
    }



}
