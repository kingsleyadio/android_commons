package ng.kingsley.android.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import java.util.List;

import ng.kingsley.android.util.Log;
import ng.kingsley.android.util.NavigationUtils;

/**
 * @author ADIO Kingsley O.
 * @since 26 Sep, 2016
 */

public class AppManager {

    private static final String TAG = AppManager.class.getSimpleName();

    public static final AppManager.Monitor MONITOR = new Monitor();

    private AppManager() {
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

    public static void finishAllActivities(Context context) {
        Context appContext = context.getApplicationContext();
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

    public static boolean clearAppData(Context context) {
        ActivityManager aMan = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return aMan.clearApplicationUserData();
        } else try {
            // Fallback hack to clear app data
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear " + context.getPackageName());
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Unable to clear application data", e);
            return false;
        }
    }

    public static void killApp() {
        System.exit(0);
    }


    //region Activity Lifecycle Monitor
    private static class Monitor implements Application.ActivityLifecycleCallbacks {

        private int createdActivitiesCounter = 0;
        private int startedActivitiesCounter = 0;
        private int resumedActivitiesCounter = 0;

        private Monitor() {
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            createdActivitiesCounter++;
        }

        @Override
        public void onActivityStarted(Activity activity) {
            startedActivitiesCounter++;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            resumedActivitiesCounter++;
        }

        @Override
        public void onActivityPaused(Activity activity) {
            resumedActivitiesCounter--;
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
            createdActivitiesCounter--;
        }
    }
    //endregion
}
