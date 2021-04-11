package ng.kingsley.android.extensions

import android.app.Activity
import android.content.Intent
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

/**
 * @author ADIO Kingsley O.
 * @since 15 May, 2017
 */

val Activity.isStartedByLauncher
    get() = intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == intent.action

var AppCompatActivity.supportTitle: CharSequence?
    get() = supportActionBar?.title
    set(value) {
        supportActionBar?.title = value
    }

fun AppCompatActivity.setSupportTitle(@StringRes titleRes: Int) {
    supportTitle = if (titleRes == 0) null else getText(titleRes)
}
