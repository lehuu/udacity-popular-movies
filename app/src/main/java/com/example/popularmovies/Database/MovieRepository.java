package com.example.popularmovies.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.popularmovies.Models.Movie;
import com.example.popularmovies.Models.MovieDao;
import com.example.popularmovies.Models.MovieDataSource;
import com.example.popularmovies.Models.MovieDataSourceFactory;
import com.example.popularmovies.Utils.NetworkUtils;
import com.example.popularmovies.Webservice.MovieService;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
* MovieRepository class used for backend calls
* and responsible for deciding between network or local database loading
 */
public class MovieRepository {

    private final MovieService webservice;
    private final MovieDao movieDao;
    private final Executor executor;

    public MovieRepository(Application application) {
        this.webservice = NetworkUtils.getRetrofitInstance().create(MovieService.class);
        this.movieDao = MovieDatabase.getDatabase(application).movieDao();
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Load the movies based on the OptionsMenu Categories
     * @param sortType The type of Movielist to load
     * @return A live data list with the movies loaded from DB or Network
     */
    public LiveData<PagedList<Movie>> getPagedMoviesBySortType(MovieDataSource.SortType sortType){
        MovieDataSourceFactory dataSourceFactory = new MovieDataSourceFactory(sortType);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPrefetchDistance(10)
                .build();

        LiveData<PagedList<Movie>> moviesPagedList = new LivePagedListBuilder(dataSourceFactory, config).build();
        return moviesPagedList;
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return movieDao.getFavoriteMovies();
    }

    public void insertMovie(Movie movie) {
        executor.execute(() -> {
            movieDao.insertMovie(movie);
        });
    }
}
