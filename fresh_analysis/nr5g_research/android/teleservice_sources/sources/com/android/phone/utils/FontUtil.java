package com.android.phone.utils;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.widget.TextView;
import java.util.regex.Pattern;
import miui.os.Build;
import miuix.core.util.SystemProperties;

/* JADX INFO: loaded from: classes.dex */
public abstract class FontUtil {
    private static final Pattern DIGIT_PATTERN;
    public static final String FONT_MI = SystemProperties.get("ro.miui.ui.font.mi_font_path", "system/fonts/MiLanProVF.ttf");

    static {
        Pattern patternCompile;
        Paint paint = new Paint(1);
        paint.setTypeface(createTypeface(true, "regular"));
        Rect rect = new Rect();
        paint.getTextBounds("*", 0, 1, rect);
        if (rect.width() > 0) {
            patternCompile = Pattern.compile("^[0-9\\*\\+\\-\\,\\s#:]*$");
        } else {
            patternCompile = Pattern.compile("^[0-9\\+\\-\\,\\s:]*$");
        }
        DIGIT_PATTERN = patternCompile;
    }

    public static boolean isSupportFont() {
        return !Build.IS_INTERNATIONAL_BUILD;
    }

    public static Typeface createTypeface(boolean z, String str) {
        StringBuilder sb = z ? new StringBuilder("mitype-") : new StringBuilder("mipro-");
        sb.append(str);
        return Typeface.create(sb.toString(), 0);
    }

    public static void setTextTypeface(TextView textView) {
        if (textView == null || !isSupportFont()) {
            return;
        }
        textView.setTypeface(createTypeface(false, "regular"));
    }
}
