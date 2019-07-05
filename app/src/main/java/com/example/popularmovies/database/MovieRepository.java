package com.example.popularmovies.database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.popularmovies.models.*;
import com.example.popularmovies.utils.NetworkUtils;
import com.example.popularmovies.webservice.MovieService;

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
        return mMovieDao.getFavoriteMovies();
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
