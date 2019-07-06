package com.example.popularmovies.ui;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.popularmovies.R;
import com.example.popularmovies.models.Movie;
import com.example.popularmovies.utils.OnItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends PagedListAdapter<Movie, MovieAdapter.ViewHolder> {
    private final RequestManager mGlide;

    private OnItemClickListener mClickListener;

    protected MovieAdapter(RequestManager glide) {
        super(DIFF_CALLBACK);
        mGlide = glide;
    }

    public Movie getItemAtPosition(int index){
        return getItem(index);
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
        Movie movie = getItem(position);
        holder.bind(movie);
    }

    public void setClickListener(OnItemClickListener clickListener){
        mClickListener = clickListener;
    }

    private static DiffUtil.ItemCallback<Movie> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Movie>() {
                @Override
                public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_movie_title) TextView mTitleTextView;
        @BindView(R.id.iv_movie_poster) ImageView mPosterImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(Movie movie) {
            if(movie == null)
                return;
            mTitleTextView.setText(movie.getTitle());
            mGlide.load(movie.getFullPosterPath())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mPosterImageView);
        }

        @Override
        public void onClick(View v) {
            mClickListener.onListItemClick(getAdapterPosition());
        }
    }

}
