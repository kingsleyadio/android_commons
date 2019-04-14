package ng.kingsley.android.util;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Author:  adiksonline
 * Date:    3/19/15
 */

public class Inputs {

    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    public static String hexToString(String e) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < e.length(); i = i + 2) {
            String c = e.substring(i, i + 2);
            char j = (char) Integer.parseInt(c, 16);
            sb.append(j);
        }
        return sb.toString();
    }

    public static boolean isValidName(CharSequence target) {
        return !TextUtils.isEmpty(target) && Pattern.matches("^[\\p{L} .'-]+$", target);
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidUrl(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.WEB_URL.matcher(target).matches();
    }

    public static boolean hasBlank(Object... objs) {
        for (Object o : objs) {
            if (o == null || (o instanceof CharSequence && TextUtils.isEmpty((CharSequence) o))) {
                return true;
            }
        }
        return false;
    }

    public static <T> String join(List<T> list, String delimiter) {
        String del = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(del).append(list.get(i));
            if (!del.contentEquals(delimiter)) {
                del = delimiter;
            }
        }
        return sb.toString();
    }
}
