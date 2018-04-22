package ng.kingsley.android.extensions

import android.app.Activity
import android.content.Intent
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity

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
    supportTitle = if (titleRes > 0) getText(titleRes) else null
}
