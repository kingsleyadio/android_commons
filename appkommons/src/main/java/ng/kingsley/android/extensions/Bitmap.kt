package ng.kingsley.android.extensions

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader

/**
 * @author ADIO Kingsley O.
 * @since 16 Feb, 2017
 */

fun Bitmap.trimCircle(): Bitmap {
    val self = this
    val side = Math.min(self.width, self.height)
    return Bitmap.createBitmap(side, side, Bitmap.Config.ARGB_8888).apply {
        with(Canvas(this@apply)) {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.shader = BitmapShader(self, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            val radius = side / 2F
            drawCircle(radius, radius, radius, paint)
        }
        if (this != this@trimCircle) this@trimCircle.recycle()
    }
}

fun Bitmap.scaled(width: Int, height: Int): Bitmap {
    return Bitmap.createScaledBitmap(this, width, height, false).also {
        if (it != this) this.recycle()
    }
}
