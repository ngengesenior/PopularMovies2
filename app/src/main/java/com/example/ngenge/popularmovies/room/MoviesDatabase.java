package com.example.ngenge.popularmovies.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.ngenge.popularmovies.models.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase{

    private static MoviesDatabase sInstance;
    public abstract MovieDao movieDao();

    static MoviesDatabase getDatabase(final Context context) {
        if (sInstance == null) {
            synchronized (MoviesDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            MoviesDatabase.class, "movies_database")
                            .build();

                }
            }
        }
        return sInstance;
    }


}
