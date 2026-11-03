package com.android.internal.telephony;

import android.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

/* JADX INFO: loaded from: classes.dex */
public abstract class UserIcons {
    private static final int[] USER_ICON_COLORS = {17171646, 17171647, 17171648, 17171649, 17171650, 17171651, 17171652, 17171653};

    public static Bitmap convertToBitmap(Drawable drawable) {
        return convertToBitmapAtSize(drawable, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

    private static Bitmap convertToBitmapAtSize(Drawable drawable, int i, int i2) {
        if (drawable == null) {
            return null;
        }
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        drawable.setBounds(0, 0, i, i2);
        drawable.draw(canvas);
        return bitmapCreateBitmap;
    }

    public static Drawable getDefaultUserIcon(Resources resources, int i, boolean z) {
        int i2 = z ? 17171655 : 17171654;
        if (i != -10000) {
            int[] iArr = USER_ICON_COLORS;
            i2 = iArr[i % iArr.length];
        }
        return getDefaultUserIconInColor(resources, resources.getColor(i2, null));
    }

    public static Drawable getDefaultUserIconInColor(Resources resources, int i) {
        Drawable baseDefaultUserIcon = getBaseDefaultUserIcon(resources);
        if (baseDefaultUserIcon instanceof LayerDrawable) {
            return baseDefaultUserIcon;
        }
        baseDefaultUserIcon.setColorFilter(i, PorterDuff.Mode.SRC_IN);
        baseDefaultUserIcon.setBounds(0, 0, baseDefaultUserIcon.getIntrinsicWidth(), baseDefaultUserIcon.getIntrinsicHeight());
        return baseDefaultUserIcon;
    }

    public static Drawable getBaseDefaultUserIcon(Resources resources) {
        return resources.getDrawable(R.drawable.ic_btn_round_more, null).mutate();
    }
}
