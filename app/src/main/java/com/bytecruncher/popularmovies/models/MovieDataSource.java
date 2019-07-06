package com.bytecruncher.popularmovies.models;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.bytecruncher.popularmovies.utils.NetworkUtils;
import com.bytecruncher.popularmovies.webservice.MovieService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {
    private static final int FIRST_PAGE = 1;
    private SortType mSortType;

    public MovieDataSource(SortType sortType){
        mSortType = sortType;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Movie> callback) {
        Callback<Movie.MoviePage> finishCallback = new Callback<Movie.MoviePage>() {
            @Override
            public void onResponse(Call<Movie.MoviePage> call, Response<Movie.MoviePage> response) {
                if (response.body() != null) {
                    if(!params.placeholdersEnabled)
                        callback.onResult(response.body().getResults(), null, FIRST_PAGE + 1);
                    else {
                        Movie.MoviePage moviePage = response.body();
                        callback.onResult(moviePage.getResults(), 0 , moviePage.getTotalResults(), null, FIRST_PAGE + 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<Movie.MoviePage> call, Throwable t) {
            }
        };

        switch (mSortType) {
            case POPULARITY:
                NetworkUtils.getRetrofitInstance().create(MovieService.class).getPopularMovies(FIRST_PAGE).enqueue(finishCallback);
                break;
            case RATING:
                NetworkUtils.getRetrofitInstance().create(MovieService.class).getTopRatedMovies(FIRST_PAGE).enqueue(finishCallback);
                break;
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {
        NetworkUtils.getRetrofitInstance().create(MovieService.class).getPopularMovies(params.key).enqueue(new Callback<Movie.MoviePage>() {
            @Override
            public void onResponse(Call<Movie.MoviePage> call, Response<Movie.MoviePage> response) {
                Integer adjacentKey = (params.key > FIRST_PAGE) ? params.key - 1 : null;
                if (response.body() != null) {

                    //passing the loaded data
                    //and the previous page key
                    callback.onResult(response.body().getResults(), adjacentKey);
                }
            }

            @Override
            public void onFailure(Call<Movie.MoviePage> call, Throwable t) {
            }
        });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {
        NetworkUtils.getRetrofitInstance().create(MovieService.class).getPopularMovies(params.key).enqueue(new Callback<Movie.MoviePage>() {
            @Override
            public void onResponse(Call<Movie.MoviePage> call, Response<Movie.MoviePage> response) {
                if (response.body() != null) {
                    //if the response has next page
                    //incrementing the next page number
                    Integer key = response.body().getTotalPages() > params.key ? params.key + 1 : null;
                    //passing the loaded data and next page value
                    callback.onResult(response.body().getResults(), key);
                }
            }

            @Override
            public void onFailure(Call<Movie.MoviePage> call, Throwable t) {
            }
        });
    }


    public enum SortType {
        POPULARITY, RATING, FAVORITE
    }
}
