package ng.kingsley.android.extensions

import android.app.Activity
import android.content.Intent

/**
 * @author ADIO Kingsley O.
 * @since 15 May, 2017
 */


val Activity.isStartedByLauncher
    get() = intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == intent.action
