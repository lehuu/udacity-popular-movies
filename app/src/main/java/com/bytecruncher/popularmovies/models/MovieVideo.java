package com.bytecruncher.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.bytecruncher.popularmovies.utils.NetworkUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

public class MovieVideo implements Parcelable {

    private static final String SITE_YOUTUBE = "YouTube";
    private static final String TYPE_TRAILER = "Trailer";
    private static final String TYPE_TEASER = "Teaser";

    private String id;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public MovieVideo(String id, String key, String name, String site, int size, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    protected MovieVideo(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = in.readInt();
        this.type = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumbnailPath(){
        return String.format(NetworkUtils.YOUTUBE_THUMBNAIL_URL, key);
    }

    public String getYoutubeLink(){
        if(isYoutubeVideo()) {
           return String.format(NetworkUtils.YOUTUBE_VIDEO_URL, key);
        }
        return "";
    }

    public boolean isYoutubeVideo() {
        return site.toLowerCase(Locale.US).equals(SITE_YOUTUBE.toLowerCase(Locale.US));
    }

    public boolean isTrailer() {
        return type.toLowerCase(Locale.US).equals(TYPE_TRAILER.toLowerCase(Locale.US));
    }

    public boolean isTeaser() {
        return type.toLowerCase(Locale.US).equals(TYPE_TEASER.toLowerCase(Locale.US));
    }

    @Override
    public String toString() {
        return id + ", " + key + ", " + name + ", " + site + ", " + size + ", " + type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(!(obj instanceof MovieVideo))
            return false;

        MovieVideo other = (MovieVideo) obj;

        return other.getId().equals(id)
                && other.getKey().equals(key)
                && other.getName().equals(name)
                && other.getSite().equals(site)
                && other.getSize() == size
                && other.getType().equals(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeInt(this.size);
        dest.writeString(this.type);
    }

    public static final Parcelable.Creator<MovieVideo> CREATOR = new Parcelable.Creator<MovieVideo>() {
        @Override
        public MovieVideo createFromParcel(Parcel source) {
            return new MovieVideo(source);
        }

        @Override
        public MovieVideo[] newArray(int size) {
            return new MovieVideo[size];
        }
    };


    /**
     * MovieVideoPage class represents the json result returned from the MovieDB API containing an array of videos with additional page data
     */
    public class MovieVideoPage {
        private int id;
        private List<MovieVideo> results;

        public List<MovieVideo> getResults() {
            return results;
        }

        @Override
        public String toString() {

            String resultsString = "";
            for(MovieVideo movie : results) {
                resultsString += "\n" + movie.toString();
            }

            return "Movie Videos: " + id + resultsString;
        }
    }
}