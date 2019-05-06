package com.example.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.popularmovies.Models.Movie;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private Movie mMovie;
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mOverviewTextView;
    private TextView mVotesTextView;


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

        mReleaseDateTextView.setText(String.format(Locale.US, "%1$tB %1$te, %1$tY" , mMovie.getReleaseDate()));
        mOverviewTextView.setText(mMovie.getOverview());
        mVotesTextView.setText(String.format(Locale.US,"%.1f", mMovie.getVoteAverage()));
    }
}
