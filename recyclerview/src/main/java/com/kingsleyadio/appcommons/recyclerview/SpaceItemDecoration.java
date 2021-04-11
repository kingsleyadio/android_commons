package com.kingsleyadio.appcommons.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * @author ADIO Kingsley O.
 * @since 15 Oct, 2015
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public SpaceItemDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(
            Rect outRect, @NonNull View view, RecyclerView parent,
            @NonNull RecyclerView.State state) {
        int halfSpace = mSpace >> 1;
        outRect.left = halfSpace;
        outRect.right = halfSpace;
        outRect.bottom = mSpace;
        outRect.top = 0;

        int childIndex = parent.getChildLayoutPosition(view);
        int spanCount = getSpanCount(parent);
        int spanIndex = childIndex % spanCount;

        if (spanIndex <= 0) { // Left edge
            outRect.left = mSpace;
        }
        if (spanIndex + 1 >= spanCount) { // Right edge
            outRect.right = mSpace;
        }
        if (childIndex < spanCount) { // Top edge
            outRect.top = mSpace;
        }
    }

    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            return ((GridLayoutManager) manager).getSpanCount();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) manager).getSpanCount();
        } else if (manager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) manager).getOrientation()
                    == LinearLayoutManager.HORIZONTAL) {
                return manager.getChildCount();
            }
        }
        return 1;
    }
}
