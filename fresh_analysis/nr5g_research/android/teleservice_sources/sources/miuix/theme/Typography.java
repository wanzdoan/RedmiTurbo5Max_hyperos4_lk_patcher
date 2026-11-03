package miuix.theme;

import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;

/* JADX INFO: loaded from: classes2.dex */
public abstract class Typography {
    public static Typeface MISANS_LIGHT;

    public static void applyMiSansLight(TextView textView) {
        if (textView == null) {
            return;
        }
        try {
            if (MISANS_LIGHT == null) {
                MISANS_LIGHT = Typeface.create("sans-serif-light", 0);
            }
            textView.setTypeface(MISANS_LIGHT);
        } catch (Exception e) {
            Log.w("Typography", "Failed to set [sans-serif-medium normal] typeface!", e);
            textView.setTypeface(Typeface.DEFAULT);
        }
    }
}
