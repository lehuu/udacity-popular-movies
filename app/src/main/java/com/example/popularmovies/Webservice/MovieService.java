package com.example.popularmovies.Webservice;

import com.example.popularmovies.Models.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("movie/popular")
    Call<Movie.MoviePage> getPopularMovies(@Query("page") int page);

    @GET("movie/top_rated")
    Call<Movie.MoviePage> getTopRatedMovies(@Query("page") int page);
}