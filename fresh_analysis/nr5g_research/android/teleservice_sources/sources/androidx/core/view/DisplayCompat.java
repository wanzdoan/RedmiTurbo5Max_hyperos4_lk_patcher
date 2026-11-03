package androidx.core.view;

import android.view.Display;

/* JADX INFO: loaded from: classes.dex */
public abstract class DisplayCompat {
    public static RoundedCornerCompat getRoundedCorner(Display display, int i) {
        return RoundedCornerCompat.toRoundedCornerCompat(display.getRoundedCorner(i));
    }
}
