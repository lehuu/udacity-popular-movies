package com.bytecruncher.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.*;

import com.bytecruncher.popularmovies.utils.NetworkUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

@Entity(tableName = "movies")
public class Movie implements Parcelable {
    @PrimaryKey
    @NonNull
    private int id;
    private String title;
    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    private Date releaseDate;
    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    private float voteAverage;
    private String overview;
    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    private String posterPath;
    @SerializedName("backdrop_path")
    @ColumnInfo(name = "backdrop_path")
    private String backdropPath;
    private boolean favorite;
    private int runtime;
    private boolean video;

    public Movie(int id, String title, Date releaseDate,
                 float voteAverage, String overview,
                 String posterPath, String backdropPath, boolean favorite,
                 int runtime, boolean video) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.favorite = favorite;
        this.runtime = runtime;
        this.video = video;
    }

    Movie(Parcel parcel){
        id = parcel.readInt();
        title = parcel.readString();
        releaseDate = (Date) parcel.readSerializable();
        voteAverage = parcel.readFloat();
        overview = parcel.readString();
        posterPath = parcel.readString();
        backdropPath = parcel.readString();
        favorite = parcel.readByte() != 0;
        runtime = parcel.readInt();
        video = parcel.readByte() != 0;
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
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getFullPosterPath() {
        return NetworkUtils.POSTER_URL + posterPath;
    }

    public String getFullBackdropPath() {
        return NetworkUtils.BACKDROP_URL + backdropPath;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public int getRuntime() {
        return runtime;
    }

    public boolean hasVideo() {
        return video;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return id + ", " + title + ", " + releaseDate + ", " + voteAverage + ", " + overview;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(!(obj instanceof Movie))
            return false;

        Movie other = (Movie) obj;

        return other.getId() == id
                && other.getOverview().equals(overview)
                && other.getFullPosterPath().equals(posterPath)
                && other.getFullBackdropPath().equals(backdropPath)
                && other.getReleaseDate() == releaseDate
                && other.getTitle().equals(title)
                && other.getVoteAverage() == voteAverage
                && other.getRuntime() == runtime
                && other.hasVideo() == video;
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
        dest.writeString(backdropPath);
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeInt(runtime);
        dest.writeByte((byte) (video ? 1 : 0));
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
        private List<Movie> results;

        public List<Movie> getResults() {
            return results;
        }

        public int getTotalResults() {
            return totalResults;
        }

        public int getPage() {
            return page;
        }

        public int getTotalPages() {
            return totalPages;
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
