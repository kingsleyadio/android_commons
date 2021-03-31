package ng.kingsley.android.recyclerview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import ng.kingsley.android.recyclerview.R;

/**
 * @author ADIO Kingsley O.
 * @since 15 Oct, 2015
 */
public class AutomoreAdapter extends WrapperAdapter {

    public interface OnMoreListener {
        void onMoreRequested(AutomoreAdapter adapter);
    }

    private static final int TYPE_PROGRESS = 10001;

    private static final int TOLERANCE_DEFAULT = 3;

    private int mProgressResource;
    private AtomicInteger mTolerance;
    private AtomicBoolean mLoading;
    private OnMoreListener mMoreListener;
    private AtomicBoolean moreEnabled;

    public AutomoreAdapter(RecyclerView.Adapter wrapped) {
        this(wrapped, R.layout.list_progress);
    }

    public AutomoreAdapter(RecyclerView.Adapter wrapped, @LayoutRes int progressResource) {
        this(wrapped, progressResource, true);
    }

    public AutomoreAdapter(RecyclerView.Adapter wrapped, boolean progressEnabled) {
        this(wrapped, R.layout.list_progress, progressEnabled);
    }

    public AutomoreAdapter(
            RecyclerView.Adapter wrapped,
            @LayoutRes int progressResource,
            boolean progressEnabled) {
        super(wrapped);
        mTolerance = new AtomicInteger(TOLERANCE_DEFAULT);
        mLoading = new AtomicBoolean(false);
        moreEnabled = new AtomicBoolean(progressEnabled);
        mProgressResource = progressResource;
    }

    public void setOnMoreListener(OnMoreListener listener) {
        mMoreListener = listener;
    }

    public OnMoreListener getOnMoreListener() {
        return mMoreListener;
    }

    public void setMoreCompleted() {
        mLoading.set(false);
    }

    public boolean isMoreCompleted() {
        return mLoading.get();
    }

    public void setTolerance(int tolerance) {
        if (tolerance < 0) {
            throw new IllegalArgumentException("tolerance cannot be less than 0");
        }
        mTolerance.set(tolerance);
    }

    public int getTolerance() {
        return mTolerance.get();
    }

    public void setMoreEnabled(boolean enabled) {
        boolean same = enabled == moreEnabled.get();
        if (same) {
            return;
        }
        moreEnabled.set(enabled);
        if (enabled) {
            notifyItemInserted(super.getItemCount());
        } else {
            notifyItemRemoved(super.getItemCount());
        }
    }

    public boolean getMoreEnabled() {
        return moreEnabled.get();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_PROGRESS) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(mProgressResource, parent, false);
            return new ProgressViewHolder(v);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_PROGRESS) {
            ProgressViewHolder pvh = (ProgressViewHolder) holder;
            ViewGroup.LayoutParams lp = pvh.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(true);
            }
            // TODO: Set span full row for progress view in grid layout manager too

            pvh.progress.setIndeterminate(true);
        } else {
            super.onBindViewHolder(holder, position);

            if (!mLoading.get() && moreEnabled.get()
                    && position + mTolerance.get() + 1 >= super.getItemCount()) {
                if (mMoreListener != null) {
                    mLoading.set(true);
                    mMoreListener.onMoreRequested(AutomoreAdapter.this);
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= super.getItemCount()) {
            return TYPE_PROGRESS;
        }
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        if (position >= super.getItemCount()) {
            return -1;
        }
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (moreEnabled.get() ? 1 : 0);
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progress;

        ProgressViewHolder(View itemView) {
            super(itemView);
            progress = itemView.findViewById(R.id.progress);
        }
    }
}
