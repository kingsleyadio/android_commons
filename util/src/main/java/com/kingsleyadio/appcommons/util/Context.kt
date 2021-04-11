package com.kingsleyadio.appcommons.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.DrawableCompat

/**
 * @author ADIO Kingsley O.
 * @since 06 Jun, 2016
 */

inline fun <reified T : Any> Context.requireSystemService(): T {
    return getSystemService() ?: error("Could not locate system service: ${T::class.java}")
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

fun Context.badgeActivity(clas: Class<out Activity>) {
    val intent = Intent(this, clas)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    applicationContext.startActivity(intent)
}
