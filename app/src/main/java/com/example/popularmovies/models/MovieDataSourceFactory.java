package com.example.popularmovies.models;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class MovieDataSourceFactory extends DataSource.Factory<Integer, Movie> {
    //creating the mutable live data
    private MediatorLiveData<MovieDataSource> mMovieLifeDataSource = new MediatorLiveData <>();
    private MovieDataSource mDataSource;
    private MovieDataSource.SortType mSortType;

    public MovieDataSourceFactory(MovieDataSource.SortType sortType) {
        this.mSortType = sortType;
    }

    @Override
    public DataSource<Integer, Movie> create() {
        //getting our data source object
        MovieDataSource dataSource = new MovieDataSource(mSortType);

        //posting the datasource to get the values
        mMovieLifeDataSource.postValue(dataSource);

        //returning the datasource
        return dataSource;
    }


    //getter
    public MutableLiveData<MovieDataSource> getMovieLifeDataSource() {
        return mMovieLifeDataSource;
    }
}
