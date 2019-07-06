package com.bytecruncher.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReview implements Parcelable {
    private String id;
    private String author;
    private String content;
    private String url;

    protected MovieReview(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    @NonNull
    @Override
    public String toString() {
        return id + "\n" + author + "\n" + content + "\n" + url;
    }

    public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };

    /**
     * MovieReviewPage class represents the json result returned from the MovieDB API containing an array of reviews with additional page data
     */
    public class MovieReviewPage {
        private int id;
        @SerializedName("total_results")
        private int totalResults;
        @SerializedName("total_pages")
        private int totalPages;
        private List<MovieReview> results;

        public List<MovieReview> getResults() {
            return results;
        }

        @Override
        public String toString() {

            String resultsString = "";
            for(MovieReview movie : results) {
                resultsString += "\n" + movie.toString();
            }

            return "Movie Reviews: " + id + resultsString;
        }
    }
}
