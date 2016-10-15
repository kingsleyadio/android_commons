package ng.kingsley.android.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

public class SwipeLayout extends SwipeRefreshLayout {

    private AbsListView absListView;
    private boolean resolved = false;

    public SwipeLayout(Context context) {
        super(context);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        if (!resolved) {
            View v = findViewById(android.R.id.list);
            if (v instanceof AbsListView) {
                absListView = (AbsListView) v;
            } else if (v != null) {
                return ViewCompat.canScrollVertically(v, -1);
            }
            resolved = true;
        }
        if (absListView == null) {
            return super.canChildScrollUp();
        }
        return absListView.getChildCount() > 0
          && (absListView.getFirstVisiblePosition() > 0
          || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
    }

}
