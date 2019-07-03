package com.example.popularmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.popularmovies.Models.Movie;
import com.example.popularmovies.Models.MovieDataSource.SortType;
import com.example.popularmovies.ViewModel.MainViewModel;


public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private EndlessRecyclerViewScrollListener mScrollListener;

    private MainViewModel mMainViewModel;

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
        mAdapter = new MovieAdapter(Glide.with(this));
        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        //Initiate the ViewModel
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        //Get shared preferences from the last time the app was used / before it was rotated to get the sort by data
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int sortInteger = sharedPref.getInt(getString(R.string.sort_by), 0);
        setSortType(sortInteger);
    }

    private void setSortType(int sortInteger) {
        checkConnection();
        SortType sortType = SortType.values()[sortInteger];

        if(mMainViewModel.getMoviesPagedList() != null)
            mMainViewModel.getMoviesPagedList().removeObservers(this);
        mMainViewModel.setSortType(sortType);
        mMainViewModel.getMoviesPagedList().observe(this, movies -> {
            // Update the cached copy of movies in the adapter.
            mAdapter.submitList(movies);
        });

        switch (sortType) {
            case POPULARITY:
                setTitle(getString(R.string.sort_popularity_title));
                break;
            case RATING:
                setTitle(getString(R.string.sort_rating_title));
                break;
            case FAVORITE:
                setTitle(getString(R.string.sort_favorite_title));
                break;
        }
        //Save the selection as sharedpreference for next app startup or rotation
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.sort_by), sortInteger);
        editor.commit();
    }

    /**
     * See if we have an active internet connection and make the connection error view visible or invisible
     */
    private void checkConnection() {
        //Check network connection before calling the API
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork == null || !activeNetwork.isConnected()){
            Toast toast = Toast.makeText(this, R.string.no_connection, Toast.LENGTH_LONG);
            toast.show();
            return;
        }
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
            String[] types = {getString(R.string.sort_popularity), getString(R.string.sort_rating), getString(R.string.sort_favorite)};
            alertBuilder.setItems(types, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    setSortType(which);
                    //mScrollListener.resetState();
                }

            });

            alertBuilder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, movie);
        startActivity(intent);
    }

}
