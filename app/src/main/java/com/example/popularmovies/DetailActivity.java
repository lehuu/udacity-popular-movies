package com.example.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.popularmovies.Models.Movie;

public class DetailActivity extends AppCompatActivity {
    private Movie mMovie;
    private ImageView mPosterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mMovie = getIntent().getParcelableExtra(Intent.EXTRA_TEXT);

        //Set the poster
        mPosterImageView = findViewById(R.id.iv_movie_poster);
        Glide.with(this).load(mMovie.getPosterPath()).into(mPosterImageView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(mMovie.getTitle());
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
