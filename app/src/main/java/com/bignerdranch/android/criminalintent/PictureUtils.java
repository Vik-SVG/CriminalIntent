package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path, int destWidth,
                                         int destHeight){
        //reading the picture sizes on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float scrWidth = options.outWidth;
        float scrHeight = options.outHeight;

        // calculationg the scales
        int inSampleSize = 1;
        if(scrHeight > destHeight || scrWidth>destWidth){
            float heightScale = scrHeight/destHeight;
            float widthScale = scrWidth/destWidth;
            inSampleSize = Math.round(heightScale>widthScale ? heightScale: widthScale);
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path,options);
    }

    public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay()
                .getSize(size);
        return getScaledBitmap(path,size.x, size.y);
    }
}
