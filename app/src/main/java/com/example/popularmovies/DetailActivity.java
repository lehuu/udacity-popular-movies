package com.example.popularmovies;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.popularmovies.ViewModel.DetailViewModel;
import com.example.popularmovies.ViewModel.DetailViewModelFactory;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.iv_movie_poster) ImageView mPosterImageView;
    @BindView(R.id.tv_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.tv_overview) TextView mOverviewTextView;
    @BindView(R.id.tv_runtime) TextView mRuntimeTextView;
    @BindView(R.id.floating_action_button) FloatingActionButton mFab;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mToolbarLayout;
    @OnClick(R.id.floating_action_button) void markFavorite(){
        if(mDetailViewModel != null)
            mDetailViewModel.switchFavorite();
    }

    private DetailViewModel mDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        int movieId = getIntent().getIntExtra(Intent.EXTRA_UID,0);
        DetailViewModelFactory modelFactory = new DetailViewModelFactory(getApplication(), movieId);
        mDetailViewModel = ViewModelProviders.of(this, modelFactory).get(DetailViewModel.class);

        //Setting up the toolbar with the movie title and the up button
        setSupportActionBar(findViewById(R.id.toolbar));
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        populateUI();
    }

    private void populateUI(){
        //Load the movie and onLoad populate the UI
        mDetailViewModel.getMovie().observe(this, movie -> {
            //Setting the Movie attributes to the textviews
            mReleaseDateTextView.setText(String.format(Locale.US, "%1$tB %1$te, %1$tY" , movie.getReleaseDate()));
            mOverviewTextView.setText(movie.getOverview());
            mRuntimeTextView.setText(getString(R.string.runtime, movie.getRuntime()));
            mToolbarLayout.setTitle(movie.getTitle());

            //Set the poster
            Glide.with(this).load(movie.getPosterPath()).into(mPosterImageView);

            //Adding the observer again to listen only to changes in the favorite attribute
            mDetailViewModel.getMovie().observe(this, favoriteMovie -> {
                //Initialize the Floating action button and the click listener for when it is marked favorite or not
                if(movie.isFavorite()) {
                    mFab.setColorFilter(getResources().getColor(R.color.favoriteYellow));
                } else {
                    mFab.setColorFilter(getResources().getColor(R.color.white));
                }
            });
        });
    }
}
