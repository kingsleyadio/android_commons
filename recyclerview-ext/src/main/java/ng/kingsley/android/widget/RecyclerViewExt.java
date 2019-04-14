package ng.kingsley.android.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author ADIO Kingsley O.
 * @since 02 Aug, 2015
 */
public class RecyclerViewExt extends RecyclerView {

    public RecyclerViewExt(Context context) {
        super(context);
    }

    public RecyclerViewExt(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewExt(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        if (direction < 1) { // Scrolling up
            boolean original = super.canScrollVertically(direction);
            return original || getChildAt(0) != null && getChildAt(0).getTop() < 0;
        }
        return super.canScrollVertically(direction);

        //<editor-fold desc="Alternative Implementation" defaultstate="collapsed">
        /*
        if (direction > 0) {
            return super.canScrollVertically(direction);
        }
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int position = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            return position != 0;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] positions = ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(null);
            for (int i = 0; i < positions.length; i++) {
                if (positions[i] == 0) {
                    return false;
                }
            }
        }
        return true;
        */
        //</editor-fold>
    }

    private void checkIfEmpty() {
        if (emptyView != null && getAdapter() != null) {
            final boolean emptyViewVisible = getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
            setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }

    private View emptyView;
    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };
}
