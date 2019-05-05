package com.example.popularmovies.Models;

import com.example.popularmovies.Utils.NetworkUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Movie {
    private int id;
    private String title;
    @SerializedName("release_date")
    private Date releaseDate;
    @SerializedName("vote_average")
    private float voteAverage;
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return NetworkUtils.POSTER_URL + posterPath;
    }

    @Override
    public String toString() {
        return id + ", " + title + ", " + releaseDate + ", " + voteAverage + ", " + overview;
    }

    public class MoviePage {
        private int page;
        @SerializedName("total_results")
        private int totalResults;
        @SerializedName("total_pages")
        private int totalPages;
        private Movie[] results;

        public Movie[] getResults() {
            return results;
        }

        @Override
        public String toString() {

            String resultsString = "";
            for(Movie movie : results) {
                resultsString += "\n" + movie.toString();
            }

            return "page: " + page + " out of " + totalPages + ", results: " + totalResults + ": " + resultsString;
        }
    }
}
