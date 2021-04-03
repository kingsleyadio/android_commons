package ng.kingsley.android.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.IntDef;
import ng.kingsley.android.util.NavigationUtils;
import timber.log.Timber;

/**
 * @author ADIO Kingsley O.
 * @since 26 Sep, 2016
 *
 * This class is now deprecated. Use lifecycle components from the Jetpack umbrella instead
 */
@Deprecated
public class AppManager {

    private static final String TAG = AppManager.class.getSimpleName();

    private static final AppManager.Monitor MONITOR = new Monitor();

    public static final int STATE_LAUNCHING = 1;
    public static final int STATE_QUITTING = 2;
    public static final int STATE_FOREGROUND = 3;
    public static final int STATE_BACKGROUND = 4;

    @SuppressLint("StaticFieldLeak")
    private static Context appContext;

    private AppManager() {
    }

    public static void register(Application app) {
        if (appContext != null) {
            Timber.tag(TAG).w("AppManager already registered!");
            return;
        }
        app.registerActivityLifecycleCallbacks(MONITOR);
        appContext = app;
    }

    public static Context applicationContext() {
        return appContext;
    }

    public static int createdActivitiesCount() {
        return MONITOR.createdActivitiesCounter;
    }

    public static int startedActivitiesCount() {
        return MONITOR.startedActivitiesCounter;
    }

    public static int resumedActivitiesCount() {
        return MONITOR.resumedActivitiesCounter;
    }

    public static boolean isAppInForeground() {
        return MONITOR.resumedActivitiesCounter > 0;
    }

    public static boolean areActivitiesStarted() {
        return MONITOR.startedActivitiesCounter > 0;
    }

    public static boolean isAppLaunched() {
        return MONITOR.createdActivitiesCounter > 0;
    }

    public static void registerStateListener(StateListener listener) {
        MONITOR.registerStateListener(listener);
    }

    public static void unregisterStateListener(StateListener listener) {
        MONITOR.unregisterStateListener(listener);
    }

    public static void finishAllActivities() {
        ActivityManager aMan = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.AppTask> appTasks = aMan.getAppTasks();
            for (ActivityManager.AppTask task : appTasks) {
                task.finishAndRemoveTask();
            }
        } else {
            NavigationUtils.badgeActivity(appContext, ShutdownCompatActivity.class);
        }
    }

    public static boolean clearAppData() {
        ActivityManager aMan = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return aMan.clearApplicationUserData();
        } else try {
            // Fallback hack to clear app data
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear " + appContext.getPackageName());
            return true;
        } catch (Exception e) {
            Timber.tag(TAG).e(e, "Unable to clear application data");
            return false;
        }
    }

    public static void killApp() {
        System.exit(0);
    }


    public interface StateListener {

        void onStateChanged(@State int newState);
    }

    @IntDef({STATE_LAUNCHING, STATE_QUITTING, STATE_FOREGROUND, STATE_BACKGROUND})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }


    //region Activity Lifecycle Monitor
    private static class Monitor implements Application.ActivityLifecycleCallbacks {

        int createdActivitiesCounter = 0;
        int startedActivitiesCounter = 0;
        int resumedActivitiesCounter = 0;

        private Handler mHandler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATE_LAUNCHING:
                        dispatchStateChange(STATE_LAUNCHING);
                        break;
                    case STATE_QUITTING:
                        dispatchStateChange(STATE_QUITTING);
                        break;
                    case STATE_BACKGROUND:
                        dispatchStateChange(STATE_BACKGROUND);
                        break;
                    case STATE_FOREGROUND:
                        dispatchStateChange(STATE_FOREGROUND);
                }
            }
        };
        private final Set<StateListener> stateListeners = new HashSet<>();

        private Monitor() {
        }

        void registerStateListener(StateListener listener) {
            synchronized (stateListeners) {
                stateListeners.add(listener);
            }
        }

        void unregisterStateListener(StateListener listener) {
            synchronized (stateListeners) {
                stateListeners.remove(listener);
            }
        }

        private void dispatchStateChange(int newState) {
            StateListener[] listeners = null;
            synchronized (stateListeners) {
                int size = stateListeners.size();
                if (size > 0) {
                    listeners = stateListeners.toArray(new StateListener[size]);
                }
            }

            if (listeners != null) {
                for (StateListener listener : listeners) {
                    listener.onStateChanged(newState);
                }
            }
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if (++createdActivitiesCounter == 1) {
                mHandler.sendEmptyMessage(STATE_LAUNCHING);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            startedActivitiesCounter++;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (++resumedActivitiesCounter == 1) {
                if (mHandler.hasMessages(STATE_BACKGROUND)) {
                    mHandler.removeMessages(STATE_BACKGROUND);

                } else mHandler.sendEmptyMessage(STATE_FOREGROUND);
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if (--resumedActivitiesCounter == 0) {
                mHandler.sendEmptyMessageDelayed(STATE_BACKGROUND, 500);
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            startedActivitiesCounter--;
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (--createdActivitiesCounter == 0) {
                mHandler.removeMessages(STATE_BACKGROUND);
                mHandler.sendEmptyMessage(STATE_QUITTING);
            }
        }
    }
    //endregion
}
