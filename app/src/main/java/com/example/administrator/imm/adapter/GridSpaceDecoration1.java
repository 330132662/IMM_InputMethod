package com.example.administrator.imm.adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpaceDecoration1 extends RecyclerView.ItemDecoration {
    private int space = 10;

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
        int spanCount = gridLayoutManager.getSpanCount();
        if ((position + 1) % spanCount == 0) {
            outRect.right = space;
        }
        if (position < spanCount) {
            outRect.top = space;
        }
        outRect.bottom = space;
        outRect.left = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, int itemPosition, @NonNull RecyclerView parent) {
        super.getItemOffsets(outRect, itemPosition, parent);
    }
}
