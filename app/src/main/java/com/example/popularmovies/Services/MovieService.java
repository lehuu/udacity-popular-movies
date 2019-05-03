package com.example.popularmovies.Services;

import com.example.popularmovies.Utils.NetworkUtils;

public class MovieService {
    private static String POPULAR_URL = "movie/popular";
    private static String TOP_RATED_URL = "movie/top_rated";

    public static String getPopularMovies() {
        return NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildURL(POPULAR_URL));
    }

    public static String getTopRatedMovies() {
        return NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildURL(TOP_RATED_URL));
    }
}
