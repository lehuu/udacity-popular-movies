package com.bytecruncher.popularmovies.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bytecruncher.popularmovies.database.MovieRepository;
import com.bytecruncher.popularmovies.models.Movie;
import com.bytecruncher.popularmovies.models.MovieReview;
import com.bytecruncher.popularmovies.models.MovieVideo;

import java.util.List;

public class DetailViewModel extends AndroidViewModel {
    private int mMovieId;
    private MovieRepository mRepository;

    private MutableLiveData<Movie> mMovie;
    private MutableLiveData<List<MovieVideo>> mMovieVideos;
    private MutableLiveData<List<MovieReview>> mMovieReviews;

    public DetailViewModel(@NonNull Application application, int movieId) {
        super(application);
        mMovieId = movieId;
        mRepository = new MovieRepository(application);
    }

    public LiveData<Movie> getMovie() {
        if(mMovie == null)
            mMovie = mRepository.getMovieDetails(mMovieId);
        return mMovie;
    }

    public LiveData<List<MovieVideo>> getMovieVideos() {
        if(mMovieVideos == null)
            mMovieVideos = mRepository.getMovieVideos(mMovieId);
        return mMovieVideos;
    }

    public LiveData<List<MovieReview>> getMovieReviews() {
        if(mMovieReviews == null)
            mMovieReviews = mRepository.getMovieReviews(mMovieId);
        return mMovieReviews;
    }

    public void switchFavorite(){
        Movie movie = mMovie.getValue();
        movie.setFavorite(!movie.isFavorite());
        mMovie.setValue(movie);

        if(movie.isFavorite()) {
            mRepository.insertMovie(movie);
        } else {
            mRepository.deleteMovie(movie);
        }
    }
}