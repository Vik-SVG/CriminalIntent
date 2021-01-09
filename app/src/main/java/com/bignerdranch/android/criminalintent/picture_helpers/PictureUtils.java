package com.bignerdranch.android.criminalintent.picture_helpers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.IOException;

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path, int destWidth,
                                         int destHeight){
        //reading the picture sizes on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float scrWidth = options.outWidth;
        float scrHeight = options.outHeight;
//        float scrHeight = options.outHeight;

        // calculationg the scales
        int inSampleSize = 1;
        if(scrHeight > destHeight || scrWidth>destWidth){
            float heightScale = scrHeight/destHeight;
            float widthScale = scrWidth/destWidth;
            inSampleSize = Math.round(heightScale>widthScale ? heightScale : widthScale);

        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

//        return BitmapFactory.decodeFile(path,options);


        int angle = readPictureDegree(path);
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        bitmap = rotatingImageView(angle, bitmap);
        return bitmap;

    }

    public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay()
                .getSize(size);
        return getScaledBitmap(path,size.x, size.y);
    }

    public static boolean removePhoto(String path) {
        File file = new File(path);
        return file.delete();
    }

   public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotatingImageView(int angle, Bitmap bitmap) {
        Bitmap newBitmap = null;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (newBitmap == null) {
            newBitmap = bitmap;
        }
        if (bitmap != newBitmap) {
            bitmap.recycle();
        }
        return newBitmap;
    }

}
