package ng.kingsley.android.api;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.Service;
import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author ADIO Kingsley O.
 * @since 29 Sep, 2015
 */
public abstract class ContextFutureCallback<T> implements FutureCallback<T> {

    private WeakReference<Context> cReference;
    private WeakReference<Fragment> fReference;
    private WeakReference<android.support.v4.app.Fragment> sReference;

    public ContextFutureCallback(Context context) {
        this.cReference = new WeakReference<>(context);
    }

    public ContextFutureCallback(Fragment fragment) {
        this.fReference = new WeakReference<>(fragment);
    }

    public ContextFutureCallback(android.support.v4.app.Fragment fragment) {
        this.sReference = new WeakReference<>(fragment);
    }

    private boolean checkContext() {
        Context c = cReference.get();
        if (c != null) {
            if (c instanceof Activity) {
                Activity a = (Activity) c;
                return !a.isFinishing();
            } else if (c instanceof Service) {
                ActivityManager aManager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningServiceInfo> services = aManager.getRunningServices(Integer.MAX_VALUE);
                if (services != null) {
                    for (ActivityManager.RunningServiceInfo info : services) {
                        if (c.getClass().getName().equals(info.service.getClassName())) {
                            return true;
                        }
                    }
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean checkFragment() {
        Fragment f = fReference.get();
        return f != null && f.isAdded();
    }

    private boolean checkSupportFragment() {
        android.support.v4.app.Fragment f = sReference.get();
        return f != null && f.isAdded();
    }

    @Override
    public void onCompleted(Exception e, T result) {
        if ((cReference != null && checkContext())
          || (fReference != null && checkFragment())
          || (sReference != null && checkSupportFragment())) {
            onComplete(e, result);
        }
    }

    public abstract void onComplete(Exception e, T result);
}
