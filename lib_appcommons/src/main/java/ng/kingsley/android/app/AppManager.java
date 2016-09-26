package ng.kingsley.android.app;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Process;

import java.util.List;

import ng.kingsley.android.util.Log;
import ng.kingsley.android.util.NavigationUtils;

/**
 * @author ADIO Kingsley O.
 * @since 26 Sep, 2016
 */

public class AppManager {

    private AppManager() {
    }

    private static final String TAG = AppManager.class.getSimpleName();

    static int createdActivitiesCounter = 0;
    static int startedActivitiesCounter = 0;
    static int resumedActivitiesCounter = 0;

    public static int createdActivitiesCount() {
        return createdActivitiesCounter;
    }

    public static int startedActivitiesCount() {
        return startedActivitiesCounter;
    }

    public static int resumedActivitiesCount() {
        return resumedActivitiesCounter;
    }

    public static boolean isAppInForeground() {
        return resumedActivitiesCounter > 0;
    }

    public static boolean areActivitiesStarted() {
        return startedActivitiesCounter > 0;
    }

    public static boolean isAppLaunched() {
        return createdActivitiesCounter > 0;
    }

    public static void finishAllActivities(Context context) {
        ActivityManager aMan = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.AppTask> appTasks = aMan.getAppTasks();
            for (ActivityManager.AppTask task : appTasks) {
                task.finishAndRemoveTask();
            }
        } else {
            NavigationUtils.badgeActivity(context, ShutdownCompatActivity.class);
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
        Process.sendSignal(Process.myPid(), Process.SIGNAL_QUIT);
//        System.exit(0);
    }
}
