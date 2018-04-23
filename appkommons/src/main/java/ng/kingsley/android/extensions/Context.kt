package ng.kingsley.android.extensions

import android.app.Fragment
import android.content.Context
import android.graphics.drawable.Drawable
import ng.kingsley.android.app.BaseApplication

/**
 * @author ADIO Kingsley O.
 * @since 06 Jun, 2016
 */

inline fun <reified C> Context.getAppComponent(): C {
    return (this.applicationContext as BaseApplication<*>).component as C
}

inline fun <reified C> Fragment.getAppComponent(): C {
    return (activity.application as BaseApplication<*>).component as C
}

inline fun <reified C> SupportFragment.getAppComponent(): C{
    return (requireActivity().application as BaseApplication<*>).component as C
}

inline fun <reified T> Context.systemService(serviceName: String): T {
    return getSystemService(serviceName) as T
}

fun Context.color(@ColorRes res: Int): Int = ContextCompat.getColor(this, res)

fun Context.drawable(@DrawableRes res: Int): Drawable? {
    return ContextCompat.getDrawable(this, res)
}

fun Context.tintedDrawable(@DrawableRes res: Int, tint: Int): Drawable? {
    val drawable = drawable(res)?.mutate() ?: return null
    DrawableCompat.setTint(DrawableCompat.wrap(drawable), tint)
    return drawable
}
