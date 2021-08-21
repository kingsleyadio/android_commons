package com.kingsleyadio.appcommons.datetime.util

import android.content.Context
import androidx.annotation.AttrRes
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.use
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red


internal fun Context.themeColor(@AttrRes attrId: Int): Int {
    return obtainStyledAttributes(intArrayOf(attrId)).use {
        it.getColorOrThrow(0)
    }
}

internal fun Int.applyAlpha(factor: Float): Int {
    return ((255 * factor).toInt() shl 24) or (this and 0x00ffffff)
}

internal fun Int.compositeOver(background: Int): Int {
    val bgA = background.alpha / 255f
    val fgA = alpha / 255f
    val a = fgA + (bgA * (1f - fgA))

    val r = compositeComponent(red, background.red, fgA, bgA, a)
    val g = compositeComponent(green, background.green, fgA, bgA, a)
    val b = compositeComponent(blue, background.blue, fgA, bgA, a)

    return (a * 255).toInt() and 0xff shl 24 or (r and 0xff shl 16) or (g and 0xff shl 8) or (b and 0xff)
}

/**
 * Composites the given [foreground component][fgC] over the [background component][bgC], with
 * foreground and background alphas of [fgA] and [bgA] respectively.
 *
 * This uses a pre-calculated composite destination alpha of [a] for efficiency.
 */
@Suppress("NOTHING_TO_INLINE")
private inline fun compositeComponent(
    fgC: Int,
    bgC: Int,
    fgA: Float,
    bgA: Float,
    a: Float
): Int = (if (a == 0f) 0f else ((fgC * fgA) + ((bgC * bgA) * (1f - fgA))) / a).toInt()
