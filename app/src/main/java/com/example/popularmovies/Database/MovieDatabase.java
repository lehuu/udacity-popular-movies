package com.example.popularmovies.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.popularmovies.Models.Movie;
import com.example.popularmovies.Models.MovieDao;
import com.example.popularmovies.Utils.DateConverter;

/*
* Local room database for stored movies
 */
@Database(entities = {Movie.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();

    private static volatile MovieDatabase INSTANCE;

    static MovieDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieDatabase.class, "movie_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}