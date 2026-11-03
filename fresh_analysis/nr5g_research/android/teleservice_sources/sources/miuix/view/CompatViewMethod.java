package miuix.view;

import android.app.Activity;
import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public abstract class CompatViewMethod {
    public static void setForceDarkAllowed(View view, boolean z) {
        view.setForceDarkAllowed(z);
    }

    public static void setActivityTranslucent(Activity activity, boolean z) {
        activity.setTranslucent(z);
    }
}
