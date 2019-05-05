package com.example.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.popularmovies.Models.Movie;
import com.example.popularmovies.Utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity {

    private Movie[] movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new FetchMovieTask().execute(NetworkUtils.POPULAR_URL);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildURL(urls[0]));
        }

        @Override
        protected void onPostExecute(String response) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            Movie.MoviePage page = gson.fromJson(response, Movie.MoviePage.class);
            movies = page.getResults();
        }
    }
}
