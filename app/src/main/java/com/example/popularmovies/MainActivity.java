package com.example.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.popularmovies.Models.Movie;
import com.example.popularmovies.Utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;

    private Movie[] mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Recyclerview and layout manager
        mRecyclerView = findViewById(R.id.rv_movie_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

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
         * @param response
         */
        @Override
        protected void onPostExecute(String response) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            Movie.MoviePage page = gson.fromJson(response, Movie.MoviePage.class);
            //Load the movies into the recyclerview
            mMovies = page.getResults();
            mAdapter = new MovieAdapter(mMovies);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
