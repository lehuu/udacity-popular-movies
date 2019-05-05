package com.example.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.popularmovies.Models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final RequestManager mGlide;
    private List<Movie> mMovieList;

    public MovieAdapter(List<Movie> movieList, RequestManager glide) {
        this.mGlide = glide;
        this.mMovieList = movieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.movie_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mMovieList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTextView;
        ImageView mPosterImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTextView = itemView.findViewById(R.id.tv_movie_title);
            mPosterImageView = itemView.findViewById(R.id.iv_movie_poster);
        }

        void bind(Movie movie) {
            mTitleTextView.setText(movie.getTitle());
        }
    }
}
