package com.example.popularmovies.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DetailViewModelFactory extends ViewModelProvider.AndroidViewModelFactory  {
    private int mMovieId;
    @NonNull
    private final Application application;

    public DetailViewModelFactory(@NonNull Application application, int movieId) {
        super(application);
        this.application = application;
        this.mMovieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailViewModel(application, mMovieId);
    }
}
