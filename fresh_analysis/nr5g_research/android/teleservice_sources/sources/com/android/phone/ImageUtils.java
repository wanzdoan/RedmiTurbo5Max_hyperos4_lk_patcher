package com.android.phone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import miuix.graphics.BitmapFactory;

/* JADX INFO: loaded from: classes.dex */
public abstract class ImageUtils {
    public static Bitmap getEmergencyBackground(Context context, Bitmap bitmap, boolean z) {
        Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 8, bitmap.getHeight() / 8, false);
        if (z) {
            return bitmapCreateScaledBitmap;
        }
        Bitmap bitmapFastBlur = BitmapFactory.fastBlur(context, bitmapCreateScaledBitmap, context.getResources().getDimensionPixelSize(R.dimen.blur_dimen_radius));
        new Canvas(bitmapFastBlur).drawColor(context.getResources().getColor(R.color.blur_background_mask));
        return bitmapFastBlur;
    }
}
