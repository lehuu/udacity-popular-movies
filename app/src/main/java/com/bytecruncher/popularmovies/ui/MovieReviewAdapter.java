package com.bytecruncher.popularmovies.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bytecruncher.popularmovies.R;
import com.bytecruncher.popularmovies.models.MovieReview;
import com.bytecruncher.popularmovies.utils.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder> {
    List<MovieReview> mMovieReviews;
    OnItemClickListener mOnItemClickListener;

    public void setMovieReviews(List<MovieReview> movieReviews){
        mMovieReviews = movieReviews;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public MovieReview getItemAtPosition(int index){
        if(mMovieReviews == null || index >= mMovieReviews.size())
            return null;
        return mMovieReviews.get(index);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_review_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mMovieReviews.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovieReviews == null ? 0 : mMovieReviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_content) TextView mContentTextView;
        @BindView(R.id.tv_author) TextView mAuthorTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(MovieReview review) {
            mContentTextView.setText(review.getContent());
            mAuthorTextView.setText(review.getAuthor());
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
