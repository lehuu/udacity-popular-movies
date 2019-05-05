package com.example.popularmovies.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Movie {
//Title, release date, movie poster, vote average, plot synopsis
    private int id;
    private String title;
    @SerializedName("release_date")
    private Date releaseDate;
    @SerializedName("vote_average")
    private float voteAverage;
    private String overview;

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
