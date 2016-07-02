package ng.kingsley.android.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat

/**
 * @author ADIO Kingsley O.
 * @since 06 Jun, 2016
 */


fun Context.color(@ColorRes res: Int): Int = ContextCompat.getColor(this, res)

fun Context.drawable(@DrawableRes res: Int, theme: Resources.Theme? = null): Drawable? = ResourcesCompat.getDrawable(resources, res, theme)