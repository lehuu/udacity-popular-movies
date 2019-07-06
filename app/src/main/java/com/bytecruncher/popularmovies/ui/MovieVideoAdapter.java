package com.bytecruncher.popularmovies.ui;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bytecruncher.popularmovies.R;
import com.bytecruncher.popularmovies.models.MovieVideo;
import com.bytecruncher.popularmovies.utils.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieVideoAdapter extends RecyclerView.Adapter<MovieVideoAdapter.ViewHolder> {
    List<MovieVideo> mMovieVideos;
    RequestManager mGlide;
    OnItemClickListener mOnItemClickListener;

    protected MovieVideoAdapter(RequestManager glide) {
        super();
        mGlide = glide;
    }

    public void setMovieVideos(List<MovieVideo> movieVideos) {
        this.mMovieVideos = movieVideos;
        notifyDataSetChanged();
    }

    public MovieVideo getItemAtPosition(int index){
        if(mMovieVideos == null || index >= mMovieVideos.size())
            return null;
        return mMovieVideos.get(index);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_video_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mMovieVideos.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovieVideos == null ? 0 : mMovieVideos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_movie_trailer) ImageView mTrailerImageView;
        @BindView(R.id.iv_play) ImageView mPlayImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(MovieVideo movieVideo){
            //Set the play button visibility when the thumbnail is loaded so it does not look weird
            mGlide.load(movieVideo.getThumbnailPath())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mTrailerImageView);
        }

        @Override
        public void onClick(View v) {
            if(mOnItemClickListener != null)
                mOnItemClickListener.onListItemClick(getAdapterPosition());
        }
    }

}
