package ng.kingsley.android.util;

/**
 * @author ADIO Kingsley O.
 * @since 28 Jul, 2015
 */
public final class Log {

    private static final String LOG_PREFIX = "";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;

    private static int logLevel = android.util.Log.DEBUG;

    public static int getLogLevel() {
        return logLevel;
    }

    public static void setLogLevel(int logLevel) {
        Log.logLevel = logLevel;
    }

    private static boolean shouldLog(int logLevel) {
        return logLevel >= Log.logLevel;
    }

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }

        return LOG_PREFIX + str;
    }

    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static void v(final String tag, String message) {
        if (shouldLog(android.util.Log.VERBOSE)) android.util.Log.v(tag, message);
    }

    public static void v(final String tag, String message, Throwable cause) {
        if (shouldLog(android.util.Log.VERBOSE)) android.util.Log.v(tag, message, cause);
    }

    public static void d(final String tag, String message) {
        if (shouldLog(android.util.Log.DEBUG)) android.util.Log.d(tag, message);
    }

    public static void d(final String tag, String message, Throwable cause) {
        if (shouldLog(android.util.Log.DEBUG)) android.util.Log.d(tag, message, cause);
    }

    public static void i(final String tag, String message) {
        if (shouldLog(android.util.Log.INFO)) android.util.Log.i(tag, message);
    }

    public static void i(final String tag, String message, Throwable cause) {
        if (shouldLog(android.util.Log.INFO)) android.util.Log.i(tag, message, cause);
    }

    public static void w(final String tag, String message) {
        if (shouldLog(android.util.Log.WARN)) android.util.Log.w(tag, message);
    }

    public static void w(final String tag, String message, Throwable cause) {
        if (shouldLog(android.util.Log.WARN)) android.util.Log.w(tag, message, cause);
    }

    public static void e(final String tag, String message) {
        if (shouldLog(android.util.Log.ERROR)) android.util.Log.e(tag, message);
    }

    public static void e(final String tag, String message, Throwable cause) {
        if (shouldLog(android.util.Log.ERROR)) android.util.Log.e(tag, message, cause);
    }

    public static void wtf(final String tag, String message) {
        if (shouldLog(android.util.Log.ERROR)) android.util.Log.wtf(tag, message);
    }

    public static void wtf(final String tag, String message, Throwable cause) {
        if (shouldLog(android.util.Log.ERROR)) android.util.Log.wtf(tag, message, cause);
    }
}
