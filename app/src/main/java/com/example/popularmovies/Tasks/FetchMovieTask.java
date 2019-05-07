package com.example.popularmovies.Tasks;

import android.os.AsyncTask;

import com.example.popularmovies.Models.Movie;
import com.example.popularmovies.Utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;
import java.util.Arrays;

public class FetchMovieTask extends AsyncTask<URL, Void, String> {
    private OnCompleteListener mOnCompleteListener;

    public FetchMovieTask(OnCompleteListener onCompleteListener){
        mOnCompleteListener = onCompleteListener;
    }

    @Override
    protected String doInBackground(URL... urls) {
        return NetworkUtils.getResponseFromHttpUrl(urls[0]);
    }

    /**
     * Load the response into a MoviePage object and put it into view
     * @param response The result which is returned from the http connection
     */
    @Override
    protected void onPostExecute(String response) {
        mOnCompleteListener.onComplete(response);
    }

    public interface OnCompleteListener {
        void onComplete(String response);
    }
}
