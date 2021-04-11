package com.kingsleyadio.appcommons.util

import android.graphics.*

/**
 * @author ADIO Kingsley O.
 * @since 16 Feb, 2017
 */

fun Bitmap.trimCircle(): Bitmap {
    val self = this
    val side = minOf(self.width, self.height)
    return Bitmap.createBitmap(side, side, Bitmap.Config.ARGB_8888).apply {
        with(Canvas(this)) {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.shader = BitmapShader(self, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            val radius = side / 2F
            drawCircle(radius, radius, radius, paint)
        }
    }
}

fun Bitmap.scaled(width: Int, height: Int): Bitmap {
    return Bitmap.createScaledBitmap(this, width, height, false)
}

fun Bitmap.rotateBitmap(rotationAngle: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(rotationAngle.toFloat())
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
}
