package com.bytecruncher.popularmovies.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.bytecruncher.popularmovies.database.MovieRepository;
import com.bytecruncher.popularmovies.models.Movie;
import com.bytecruncher.popularmovies.models.MovieDataSource.SortType;


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
        //Reload the movie only when a new filter is selected or when our app starts up with an empty list
        if(sortType == mSortType && mMoviesPagedList != null)
            return;

        this.mSortType = sortType;
        mMoviesPagedList = mRepository.getPagedMoviesBySortType(sortType);
    }

}
