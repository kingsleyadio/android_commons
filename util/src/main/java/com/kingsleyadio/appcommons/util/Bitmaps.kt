package com.kingsleyadio.appcommons.util

import android.graphics.*
import androidx.exifinterface.media.ExifInterface
import java.io.FileDescriptor

/**
 * @author ADIO Kingsley O.
 * @since 19 Apr, 2015
 */
object Bitmaps {

    fun getNormalizedBitmap(fileDescriptor: FileDescriptor, width: Int = 640, height: Int = width): Bitmap {
        // Calculate options for decoding bitmap
        val options = getBitmapDecodeOptions(fileDescriptor, width, height)
        var bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)

        // Rotate bitmap, maybe
        val rotationAngle = getBitmapRotationAngle(fileDescriptor)
        if (rotationAngle != 0) {
            val rotated = bitmap.rotateBitmap(rotationAngle)
            if (bitmap != rotated) {
                bitmap.recycle()
                bitmap = rotated
            }
        }
        return bitmap
    }

    fun getBitmapDecodeOptions(
        fileDescriptor: FileDescriptor,
        outWidth: Int,
        outHeight: Int
    ): BitmapFactory.Options {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)
        options.inSampleSize = calculateSampleSize(options, outWidth, outHeight)
        options.inJustDecodeBounds = false
        return options
    }

    fun getBitmapRotationAngle(fileDescriptor: FileDescriptor): Int {
        val ei = ExifInterface(fileDescriptor)

        return when (ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    private fun calculateSampleSize(options: BitmapFactory.Options, dstWidth: Int, dstHeight: Int): Int {
        // Calculate inSampleSize
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > dstHeight || width > dstWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize > dstHeight && halfWidth / inSampleSize > dstWidth) {
                inSampleSize = inSampleSize shl 1
            }
        }
        return inSampleSize
    }
}
