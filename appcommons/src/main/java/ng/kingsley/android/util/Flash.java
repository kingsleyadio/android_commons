package ng.kingsley.android.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.support.annotation.StringRes;
import android.widget.Toast;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author ADIO Kingsley O.
 * @since 12 Oct, 2015
 */
@Singleton
public class Flash {

    private final Toast mToast;

    @SuppressLint("ShowToast")
    @Inject
    Flash(Application context) {
        mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
    }

    private void show(CharSequence message, int duration) {
        if (mToast.getView().isShown()) {
            mToast.cancel();
        }
        mToast.setText(message);
        mToast.setDuration(duration);
        mToast.show();
    }

    private void show(@StringRes int resId, int duration) {
        if (mToast.getView().isShown()) {
            mToast.cancel();
        }
        mToast.setText(resId);
        mToast.setDuration(duration);
        mToast.show();
    }

    public void s(@StringRes int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    public void s(CharSequence message) {
        show(message, Toast.LENGTH_SHORT);
    }

    public void l(@StringRes int resId) {
        show(resId, Toast.LENGTH_LONG);
    }

    public void l(CharSequence message) {
        show(message, Toast.LENGTH_LONG);
    }
}
