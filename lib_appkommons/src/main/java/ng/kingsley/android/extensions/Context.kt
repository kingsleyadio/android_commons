package ng.kingsley.android.extensions

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat

/**
 * @author ADIO Kingsley O.
 * @since 06 Jun, 2016
 */


fun Context.color(@ColorRes res: Int): Int = ContextCompat.getColor(this, res)