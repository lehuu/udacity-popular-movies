package com.bytecruncher.popularmovies.ui;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//Decorator class used for setting the margin between listItems
public class MarginItemDecoration extends RecyclerView.ItemDecoration {
    private int mItemOffset;
    public MarginItemDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }
    public MarginItemDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if(position == 0)
            outRect.set(0, mItemOffset, mItemOffset, mItemOffset);
        else if(position == state.getItemCount()-1)
            outRect.set(mItemOffset, mItemOffset, 0, mItemOffset);
        else {
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }

    }
}