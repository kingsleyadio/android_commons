package ng.kingsley.android.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.media.ExifInterface;
import android.util.Log;

/**
 * @author ADIO Kingsley O.
 * @since 19 Apr, 2015
 */
public class BitmapUtils {

    private static final String TAG = BitmapUtils.class.getSimpleName();
    private static final Matrix mMatrix = new Matrix();
    private static final int BITMAP_SIZE_DEFAULT = 640;

    public static Bitmap getProcessedBitmap(String filePath) {
        return getProcessedBitmap(filePath, BITMAP_SIZE_DEFAULT, BITMAP_SIZE_DEFAULT);
    }

    public static Bitmap getProcessedBitmap(String filePath, int width, int height) {
        // Calculate options for decoding bitmap
        final BitmapFactory.Options options = getBitmapDecodeOptions(filePath, width, height);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        // Rotate bitmap, maybe
        int rotationAngle = getRotationAngle(filePath);
        if (rotationAngle != 0) {
            bitmap = rotateBitmap(bitmap, rotationAngle);
        }
        return bitmap;
    }

    public static BitmapFactory.Options getBitmapDecodeOptions(String imagePath, int outWidth, int outHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        options.inSampleSize = calculateSampleSize(options, outWidth, outHeight);
        options.inJustDecodeBounds = false;
        return options;
    }

    public static int getRotationAngle(String imagePath) {
        try {
            ExifInterface ei = new ExifInterface(imagePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
              ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
            }
        } catch (Exception e) {
            Log.e(TAG, "Could not detect image rotation angle", e);
        }
        return 0;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int rotationAngle) {
        mMatrix.reset();
        mMatrix.postRotate(rotationAngle);
        return Bitmap.createBitmap(bitmap, 0, 0,
          bitmap.getWidth(), bitmap.getHeight(), mMatrix, false);
    }

    private static int calculateSampleSize(BitmapFactory.Options options, int dstWidth, int dstHeight) {
        // Calculate inSampleSize
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > dstHeight || width > dstWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > dstHeight
              && (halfWidth / inSampleSize) > dstWidth) {
                inSampleSize <<= 1;
            }
        }
        return inSampleSize;
    }
}
