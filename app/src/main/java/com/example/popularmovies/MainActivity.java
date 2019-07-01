package com.example.popularmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.popularmovies.Tasks.FetchMovieTask;
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
        //if in landscape mode create a grid with 4 columns otherwise 2 columns
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            orientation = 4;
        } else {
            orientation = 2;
        }

        GridLayoutManager gridlayoutManager = new GridLayoutManager(this, orientation);
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

        //Get shared preferences from the last time the app was used / before it was rotated to get the sort by data
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int sortInteger = sharedPref.getInt(getString(R.string.sort_by), 0);
        setSortType(sortInteger);

        //Fetch the Movies
        loadMovies(1);
    }

    private void setSortType(int sortInteger) {
        mSortType = SortType.values()[sortInteger];
        switch (mSortType) {
            case POPULARITY:
                setTitle(getString(R.string.sort_popularity_title));
                break;
            case RATING:
                setTitle(getString(R.string.sort_rating_title));
                break;
        }
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
        //Save the selection as sharedpreference for next app startup or rotation
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.sort_by),  mSortType.ordinal());
        editor.commit();

        new FetchMovieTask(new FetchMovieTask.OnCompleteListener() {
            @Override
            public void onComplete(String response) {
                if(response == null || response.length() == 0)
                    return;
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                Movie.MoviePage page = gson.fromJson(response, Movie.MoviePage.class);
                //Load the movies into the recyclerview
                mMovies.addAll(Arrays.asList(page.getResults()));
                mAdapter.notifyDataSetChanged();
            }
        }).execute(url);
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
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            alertBuilder.setTitle(R.string.sort_by);
            String[] types = {getString(R.string.sort_popularity), getString(R.string.sort_rating)};
            alertBuilder.setItems(types, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    setSortType(which);
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
}
