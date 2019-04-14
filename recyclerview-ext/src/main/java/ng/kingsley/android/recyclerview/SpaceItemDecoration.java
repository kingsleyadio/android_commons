package ng.kingsley.android.recyclerview;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * @author ADIO Kingsley O.
 * @since 15 Oct, 2015
 */
public class SpaceItemDecoration extends android.support.v7.widget.RecyclerView.ItemDecoration {

    private int mSpace;

    public SpaceItemDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, @NonNull View view, android.support.v7.widget.RecyclerView parent,
      @NonNull android.support.v7.widget.RecyclerView.State state) {
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

    private int getSpanCount(android.support.v7.widget.RecyclerView parent) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            return ((GridLayoutManager) manager).getSpanCount();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) manager).getSpanCount();
        } else if (manager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) manager).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                return manager.getChildCount();
            }
        }
        return 1;
    }
}
