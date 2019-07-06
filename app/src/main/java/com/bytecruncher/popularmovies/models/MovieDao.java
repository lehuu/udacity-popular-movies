package com.bytecruncher.popularmovies.models;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movies WHERE favorite==1 ORDER BY title")
    DataSource.Factory<Integer, Movie> getFavoriteMoviesPaged();

    @Query("SELECT * FROM movies WHERE id==:id")
    LiveData<Movie> getMovie(int id);

    @Query("SELECT * FROM movies WHERE id==:id LIMIT 1")
    Movie hasMovie(int id);

    @Insert
    void insertMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);
}
