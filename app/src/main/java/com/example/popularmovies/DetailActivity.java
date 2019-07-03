package com.example.popularmovies;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.popularmovies.Models.Movie;
import com.example.popularmovies.ViewModel.DetailViewModel;
import com.example.popularmovies.ViewModel.DetailViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mOverviewTextView;
    private TextView mRuntimeTextView;
    private FloatingActionButton mFab;

    private DetailViewModel mDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int movieId = getIntent().getIntExtra(Intent.EXTRA_UID,0);
        DetailViewModelFactory modelFactory = new DetailViewModelFactory(getApplication(), movieId);
        mDetailViewModel = ViewModelProviders.of(this, modelFactory).get(DetailViewModel.class);

        //Get the view references
        mPosterImageView = findViewById(R.id.iv_movie_poster);
        mReleaseDateTextView = findViewById(R.id.tv_release_date);
        mOverviewTextView = findViewById(R.id.tv_overview);
        mRuntimeTextView = findViewById(R.id.tv_runtime);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mFab = findViewById(R.id.floating_action_button);


        //Setting up the toolbar with the movie title and the up button
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Load the movie and onLoad populate the UI
        mDetailViewModel.getMovie().observe(this, movie -> {
            //Setting the Movie attributes to the textviews
            mReleaseDateTextView.setText(String.format(Locale.US, "%1$tB %1$te, %1$tY" , movie.getReleaseDate()));
            mOverviewTextView.setText(movie.getOverview());
            mRuntimeTextView.setText(movie.getRuntime() + " min");
            toolbar.setTitle(movie.getTitle());
            //Set the poster
            Glide.with(this).load(movie.getPosterPath()).into(mPosterImageView);
            //Initialize the Floating action button and the click listener for when it is marked favorite or not
            if(movie.isFavorite()) {
                mFab.setColorFilter(getResources().getColor(R.color.favoriteYellow));
            }

            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movie.setFavorite(!movie.isFavorite());
                    if(movie.isFavorite()) {
                        mFab.setColorFilter(getResources().getColor(R.color.favoriteYellow));
                    } else {
                        mFab.setColorFilter(getResources().getColor(R.color.white));
                    }
                }
            });
        });


    }

    /**
     * Overriding the up button in the toolbar to behave like the back button to keep scroll level on the MainActivity
     * @param item the Menu item pressed, for now only the up button
     * @return if the up button click was handled
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }
}
