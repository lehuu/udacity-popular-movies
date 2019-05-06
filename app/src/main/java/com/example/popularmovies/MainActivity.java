package com.example.popularmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.popularmovies.Models.Movie;
import com.example.popularmovies.Utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener{
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private EndlessRecyclerViewScrollListener mScrollListener;
    private TextView mNoConnectionTextView;

    private List<Movie> mMovies = new ArrayList<>();
    private SortType mSortType = SortType.POPULARITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initiate Recyclerview, adapter and layout manager
        mRecyclerView = findViewById(R.id.rv_movie_list);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridlayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridlayoutManager);
        mAdapter = new MovieAdapter(mMovies, Glide.with(this));
        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mNoConnectionTextView = findViewById(R.id.tv_no_connection);

        //Create a scroll listener to load more data when the end of page is reached
        mScrollListener = new EndlessRecyclerViewScrollListener(gridlayoutManager) {
            @Override
            public void onLoadMore(int page) {
                loadMovies(page);
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);

        //Fetch the Movies
        loadMovies(1);
    }

    /**
     * Loads the movies from the API into the view based on the current mSortType
     */
    private void loadMovies(int page) {
        //Check network connection before calling the API
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork == null || !activeNetwork.isConnected()){
            mNoConnectionTextView.setVisibility(View.VISIBLE);
            return;
        }
        mNoConnectionTextView.setVisibility(View.INVISIBLE);

        URL url = null;

        switch(mSortType) {
            case RATING:
                url = NetworkUtils.buildURL(NetworkUtils.TOP_RATED_URL, NetworkUtils.PAGE_PARAM, Integer.toString(page));
                break;
            case POPULARITY:
                url = NetworkUtils.buildURL(NetworkUtils.POPULAR_URL, NetworkUtils.PAGE_PARAM, Integer.toString(page));
                break;
        }
        new FetchMovieTask().execute(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * When the filter option item is selected, a popup window is opened to select by which way to sort the movies
     * @param item the option item clicked
     * @return if the click was handled
     */
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
                            mSortType = SortType.POPULARITY;
                            break;
                        case 1:
                            mSortType = SortType.RATING;
                            break;
                    }
                    mMovies.clear();
                    mScrollListener.resetState();
                    loadMovies(1);
                }

            });

            alertBuilder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, mMovies.get(clickedItemIndex));
        startActivity(intent);
    }

    private enum SortType {
        POPULARITY, RATING
    }

    public class FetchMovieTask extends AsyncTask<URL, Void, String> {

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
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            Movie.MoviePage page = gson.fromJson(response, Movie.MoviePage.class);
            //Load the movies into the recyclerview
            mMovies.addAll(Arrays.asList(page.getResults()));
            mAdapter.notifyDataSetChanged();
        }
    }
}
