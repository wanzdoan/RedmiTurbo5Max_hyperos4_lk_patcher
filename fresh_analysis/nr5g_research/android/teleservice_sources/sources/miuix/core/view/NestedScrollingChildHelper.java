package miuix.core.view;

import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public abstract class NestedScrollingChildHelper extends androidx.core.view.NestedScrollingChildHelper {
    public static androidx.core.view.NestedScrollingChildHelper obtain(View view) {
        return new androidx.core.view.NestedScrollingChildHelper(view);
    }
}
