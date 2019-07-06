package com.bytecruncher.popularmovies.database;

import android.app.Application;
import android.text.Editable;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.bytecruncher.popularmovies.models.*;
import com.bytecruncher.popularmovies.utils.NetworkUtils;
import com.bytecruncher.popularmovies.webservice.MovieService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
* MovieRepository class used for backend calls
* and responsible for deciding between network or local database loading
 */
public class MovieRepository {

    private final MovieService mWebService;
    private final MovieDao mMovieDao;
    private final Executor mExecutor;

    public MovieRepository(Application application) {
        this.mWebService = NetworkUtils.getRetrofitInstance().create(MovieService.class);
        this.mMovieDao = MovieDatabase.getDatabase(application).movieDao();
        this.mExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     * Load the movies based on the OptionsMenu Categories
     * @param sortType The type of Movielist to load
     * @return A live data list with the movies loaded from DB or Network
     */
    public LiveData<PagedList<Movie>> getPagedMoviesBySortType(MovieDataSource.SortType sortType){
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setInitialLoadSizeHint(20)
                .setPageSize(20)
                .build();
        LiveData<PagedList<Movie>> moviesPagedList;

        //if we want favorite movies, we load them from Room otherwise from web
        DataSource.Factory dataSourceFactory = sortType == MovieDataSource.SortType.FAVORITE ?
                mMovieDao.getFavoriteMoviesPaged():
                new MovieDataSourceFactory(sortType);

        moviesPagedList = new LivePagedListBuilder(dataSourceFactory, config).build();
        return moviesPagedList;
    }

    public MutableLiveData<Movie> getMovieDetails(int id){
        MutableLiveData<Movie> result = new MutableLiveData<>();

        mExecutor.execute(() -> {
            Movie movie = mMovieDao.hasMovie(id);
            if(movie != null)
                result.postValue(movie);
            else
                mWebService.getMovieDetails(id).enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful()){
                            result.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        result.setValue(null);
                    }
                });
        });

        return result;
    }

    public void insertMovie(Movie movie) {
        mExecutor.execute(() -> {
            mMovieDao.insertMovie(movie);
        });
    }

    public void deleteMovie(Movie movie) {
        mExecutor.execute(()-> {
            mMovieDao.deleteMovie(movie);
        });
    }

    public MutableLiveData<List<MovieVideo>> getMovieVideos(int id) {
        MutableLiveData<List<MovieVideo>> result = new MutableLiveData<>();

        mWebService.getMovieVideos(id).enqueue(new Callback<MovieVideo.MovieVideoPage>() {
            @Override
            public void onResponse(Call<MovieVideo.MovieVideoPage> call, Response<MovieVideo.MovieVideoPage> response) {
                if (response.isSuccessful() && response.body() != null){
                    List<MovieVideo> filteredResult  = getFilteredMovieVideos(response.body().getResults());
                    result.setValue(filteredResult);
                }
            }

            @Override
            public void onFailure(Call<MovieVideo.MovieVideoPage> call, Throwable t) {
                result.setValue(null);
            }
        });
        return result;
    }

    private static List<MovieVideo> getFilteredMovieVideos(List<MovieVideo> videos) {
        List<MovieVideo> result = new ArrayList<>();
        for (MovieVideo video : videos) {
            if(video.isYoutubeVideo() && (video.isTeaser() || video.isTrailer())){
                result.add(video);
            }
        }
        return result;
    }

    public MutableLiveData<List<MovieReview>> getMovieReviews(int id) {
        MutableLiveData<List<MovieReview>> result = new MutableLiveData<>();

        mWebService.getMovieReviews(id).enqueue(new Callback<MovieReview.MovieReviewPage>() {
            @Override
            public void onResponse(Call<MovieReview.MovieReviewPage> call, Response<MovieReview.MovieReviewPage> response) {
                if (response.isSuccessful() && response.body() != null){
                    result.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<MovieReview.MovieReviewPage> call, Throwable t) {
                result.setValue(null);
            }
        });
        return result;
    }
}
