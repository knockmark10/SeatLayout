package library.knockmark.com.library.manager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class BitmapManager {

    public BitmapManager() {
    }

    public static Bitmap convertDrawableToBitmap(Resources resources, int resId) {
        Drawable drawable = resources.getDrawable(resId);
        drawable.setColorFilter(Color.parseColor("#99CCFF"), PorterDuff.Mode.SRC_ATOP);
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                bitmap = bitmapDrawable.getBitmap();
            }
        } else {
            if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }
        }
        return bitmap;
    }

    public static Bitmap changeBitmapSize(Bitmap sourceBitmap, int desiredWidth, int desiredHeight) {
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();
        float scaleWidth = ((float) desiredWidth) / width;
        float scaleHeight = ((float) desiredHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(sourceBitmap, 0, 0, width, height, matrix, false);
    }

}
