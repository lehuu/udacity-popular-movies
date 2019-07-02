package com.example.popularmovies;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.popularmovies.Models.Movie;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private Movie mMovie;
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mOverviewTextView;
    private TextView mVotesTextView;
    private FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mMovie = getIntent().getParcelableExtra(Intent.EXTRA_TEXT);

        //Set the poster
        mPosterImageView = findViewById(R.id.iv_movie_poster);
        Glide.with(this).load(mMovie.getPosterPath()).into(mPosterImageView);

        //Setting up the toolbar with the movie title and the up button
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(mMovie.getTitle());
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setting the Movie attributes to the textviews
        mReleaseDateTextView = findViewById(R.id.tv_release_date);
        mOverviewTextView = findViewById(R.id.tv_overview);
        mVotesTextView = findViewById(R.id.tv_votes);
        mFab = findViewById(R.id.floating_action_button);

        mReleaseDateTextView.setText(String.format(Locale.US, "%1$tB %1$te, %1$tY" , mMovie.getReleaseDate()));
        mOverviewTextView.setText(mMovie.getOverview());
        mVotesTextView.setText(String.format(Locale.US,"%.1f", mMovie.getVoteAverage()));

        if(mMovie.isFavorite()) {
            mFab.setColorFilter(getResources().getColor(R.color.favoriteYellow));
        }

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMovie.setFavorite(!mMovie.isFavorite());
                if(mMovie.isFavorite()) {
                    mFab.setColorFilter(getResources().getColor(R.color.favoriteYellow));
                } else {
                    mFab.setColorFilter(getResources().getColor(R.color.white));
                }
            }
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
