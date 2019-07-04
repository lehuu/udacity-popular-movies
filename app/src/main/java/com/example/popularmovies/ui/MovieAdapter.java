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
import com.example.popularmovies.R;
import com.example.popularmovies.models.Movie;

public class MovieAdapter extends PagedListAdapter<Movie, MovieAdapter.ViewHolder> {
    private final RequestManager mGlide;

    private ListItemClickListener mClickListener;

    protected MovieAdapter(RequestManager glide) {
        super(DIFF_CALLBACK);
        mGlide = glide;
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

    public void setClickListener(ListItemClickListener clickListener){
        mClickListener = clickListener;
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(Movie movie);
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
        TextView mTitleTextView;
        ImageView mPosterImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTextView = itemView.findViewById(R.id.tv_movie_title);
            mPosterImageView = itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        void bind(Movie movie) {
            mTitleTextView.setText(movie.getTitle());
            mGlide.load(movie.getPosterPath()).into(mPosterImageView);
        }

        @Override
        public void onClick(View v) {
            mClickListener.onListItemClick(getItem(getAdapterPosition()));
        }
    }

}
