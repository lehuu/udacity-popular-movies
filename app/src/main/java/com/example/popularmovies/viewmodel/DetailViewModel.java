package com.example.popularmovies.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.popularmovies.database.MovieRepository;
import com.example.popularmovies.models.Movie;

public class DetailViewModel extends AndroidViewModel {
    private MutableLiveData<Movie> mMovie;

    public DetailViewModel(@NonNull Application application, int movieId) {
        super(application);
        MovieRepository repository = new MovieRepository(application);
        mMovie = repository.getMovieDetails(movieId);
    }

    public LiveData<Movie> getMovie() {
        return mMovie;
    }

    public void switchFavorite(){
        Movie movie = mMovie.getValue();
        movie.setFavorite(!movie.isFavorite());
        mMovie.setValue(movie);
    }
}