package ng.kingsley.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

/**
 * @author ADIO Kingsley O.
 * @since 31 Oct, 2015
 */
public class NavigationUtils {

    private NavigationUtils() {
    }

    public static void hideSoftInput(@NonNull View anchor) {
        Context context = anchor.getContext();
        InputMethodManager iMan = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        iMan.hideSoftInputFromWindow(anchor.getWindowToken(), 0);
    }

    public static void badgeActivity(Context context, Class<? extends Activity> clas) {
        Intent intent = new Intent(context, clas);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
    }
}
