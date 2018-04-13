package com.example.lsnoussi.img_processing;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by lsnoussi on 13/04/18.
 */

public class Rotation {


    /**
     * Function rotating the image based on a specified angle
     *
     * @param source
     * @param angle
     * @return Bitmap
     */
    public static Bitmap rotateImage(Bitmap source, float angle) {

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}