package com.example.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.popularmovies.Utils.NetworkUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Movie implements Parcelable {
    private int id;
    private String title;
    @SerializedName("release_date")
    private Date releaseDate;
    @SerializedName("vote_average")
    private float voteAverage;
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;
    private boolean favorite;

    Movie(Parcel parcel){
        id = parcel.readInt();
        title = parcel.readString();
        releaseDate = (Date) parcel.readSerializable();
        voteAverage = parcel.readFloat();
        overview = parcel.readString();
        posterPath = parcel.readString();
        favorite = parcel.readByte() != 0;
    }

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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return id + ", " + title + ", " + releaseDate + ", " + voteAverage + ", " + overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeSerializable(releaseDate);
        dest.writeFloat(voteAverage);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeByte((byte) (favorite ? 1 : 0));
    }

    static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    /**
     * MoviePage class represents the json result returned from the MovieDB API containing an array of movies with additional page data
     */
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
