package ng.kingsley.android.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import ng.kingsley.android.app.BaseApplication

/**
 * @author ADIO Kingsley O.
 * @since 06 Jun, 2016
 */

inline fun <reified C> Context.getAppComponent(): C {
    return (applicationContext as BaseApplication<*>).component as C
}

inline fun <reified C> Fragment.getAppComponent(): C {
    return (requireActivity().application as BaseApplication<*>).component as C
}

inline fun <reified T> Context.systemService(serviceName: String): T {
    return getSystemService(serviceName) as T
}

fun Context.color(@ColorRes res: Int): Int = ContextCompat.getColor(this, res)

fun Context.drawable(@DrawableRes res: Int): Drawable? {
    return ContextCompat.getDrawable(this, res)
}

fun Context.tintedDrawable(@DrawableRes res: Int, @ColorInt tint: Int): Drawable? {
    val drawable = drawable(res)?.mutate() ?: return null
    DrawableCompat.setTint(DrawableCompat.wrap(drawable), tint)
    return drawable
}
