package ng.kingsley.android.helper;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author ADIO Kingsley O.
 * @since 31 Oct, 2015
 */
@Singleton
public class ResourceHelper {

    private final Context mContext;

    @Inject
    public ResourceHelper(Application context) {
        mContext = context;
    }

    /**
     * Returns the size in pixels of an attribute dimension
     *
     * @param attr is the attribute dimension we want to know the size from
     * @return the size in pixels of an attribute dimension
     */
    public int attrToDimenPx(int attr) {
        TypedArray a = null;
        try {
            a = mContext.getTheme().obtainStyledAttributes(new int[]{attr});
            return a.getDimensionPixelSize(0, 0);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    /**
     * Returns the screen (width, height) in pixels
     *
     * @return the screen size in pixels
     */
    public int[] getScreenSize() {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }

    public int dpToPx(int dp) {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        return (int) ((dp * metrics.density) + 0.5);
    }

    public int pxToDp(int px) {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        return (int) ((px / metrics.density) + 0.5);
    }

    @Deprecated
    public boolean isWideScreen() {
        Configuration c = mContext.getResources().getConfiguration();
        int screenCat = c.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        return screenCat >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
