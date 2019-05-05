package com.example.popularmovies;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

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
        loadMovies(SortType.POPULARITY);
    }

    /**
     * Loads the movies from the API into the view based on the defined sort type
     * @param sortType The sort type by which the movies should be loaded
     */
    private void loadMovies(SortType sortType) {
        switch(sortType) {
            case RATING:
                new FetchMovieTask().execute(NetworkUtils.TOP_RATED_URL);
                break;
            case POPULARITY:
                new FetchMovieTask().execute(NetworkUtils.POPULAR_URL);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mi_sort) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle(R.string.sort_by);
            String[] types = {getString(R.string.sort_popularity), getString(R.string.sort_rating)};
            alertBuilder.setItems(types, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    switch(which){
                        case 0:
                            mMovies.clear();
                            loadMovies(SortType.POPULARITY);
                            break;
                        case 1:
                            mMovies.clear();
                            loadMovies(SortType.RATING);
                            break;
                    }
                }

            });

            alertBuilder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private enum SortType {
        POPULARITY, RATING
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
