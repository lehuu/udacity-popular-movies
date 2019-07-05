package com.example.popularmovies.webservice;

import com.example.popularmovies.models.Movie;
import com.example.popularmovies.models.MovieVideo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {
    @GET("movie/popular")
    Call<Movie.MoviePage> getPopularMovies(@Query("page") int page);

    @GET("movie/top_rated")
    Call<Movie.MoviePage> getTopRatedMovies(@Query("page") int page);

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(@Path("movie_id") int id);

    @GET("movie/{movie_id}/videos")
    Call<MovieVideo.MovieVideoPage> getMovieVideos(@Path("movie_id") int id);
}
