package com.example.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.popularmovies.Models.Movie;
import com.example.popularmovies.Utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;

    private List<Movie> mMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initiate Recyclerview, adapter and layout manager
        mRecyclerView = findViewById(R.id.rv_movie_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new MovieAdapter(mMovies, Glide.with(this));
        mRecyclerView.setAdapter(mAdapter);

        //Fetch the Movies
        new FetchMovieTask().execute(NetworkUtils.POPULAR_URL);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildURL(urls[0]));
        }

        /**
         * Load the response into a MoviePage object and put it into view
         * @param response The result which is returned from the http connection
         */
        @Override
        protected void onPostExecute(String response) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            Movie.MoviePage page = gson.fromJson(response, Movie.MoviePage.class);
            //Load the movies into the recyclerview
            mMovies.addAll(Arrays.asList(page.getResults()));
            mAdapter.notifyDataSetChanged();
        }
    }
}
