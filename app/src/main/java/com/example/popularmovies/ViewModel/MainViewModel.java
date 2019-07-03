package com.example.popularmovies.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.example.popularmovies.Database.MovieRepository;
import com.example.popularmovies.Models.Movie;
import com.example.popularmovies.Models.MovieDataSource.SortType;


public class MainViewModel extends AndroidViewModel {
    private MovieRepository mRepository;
    private SortType mSortType = SortType.POPULARITY;

    private LiveData<PagedList<Movie>> mMoviesPagedList;

    public MainViewModel (Application application) {
        super(application);
        mRepository = new MovieRepository(application);
    }

    public LiveData<PagedList<Movie>> getMoviesPagedList() {
        return mMoviesPagedList;
    }

    public void setSortType(SortType sortType) {
        this.mSortType = sortType;

        switch (mSortType) {
            case FAVORITE:
                break;
                default:
                    mMoviesPagedList = mRepository.getPagedMoviesBySortType(sortType);

        }
    }

}
