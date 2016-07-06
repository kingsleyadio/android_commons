package ng.kingsley.android.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.graphics.drawable.DrawableCompat

/**
 * @author ADIO Kingsley O.
 * @since 06 Jun, 2016
 */


fun Context.color(@ColorRes res: Int): Int = ContextCompat.getColor(this, res)

fun Context.drawable(@DrawableRes res: Int, theme: Resources.Theme? = null): Drawable? {
    return ResourcesCompat.getDrawable(resources, res, theme)
}

fun Context.tintedDrawable(@DrawableRes res: Int, tint: Int, theme: Resources.Theme? = null): Drawable? {
    return drawable(res, theme)?.mutate()?.apply {
        DrawableCompat.setTint(DrawableCompat.wrap(this), tint)
    }
}