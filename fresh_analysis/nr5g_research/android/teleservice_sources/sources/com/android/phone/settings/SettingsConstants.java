package com.android.phone.settings;

import android.R;
import android.app.Activity;
import android.view.View;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/* JADX INFO: loaded from: classes.dex */
public abstract class SettingsConstants {
    public static void setupEdgeToEdge(Activity activity) {
        ViewCompat.setOnApplyWindowInsetsListener(activity.findViewById(R.id.content), new OnApplyWindowInsetsListener() { // from class: com.android.phone.settings.SettingsConstants$$ExternalSyntheticLambda0
            @Override // androidx.core.view.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                return SettingsConstants.$r8$lambda$UHw_oGDGFZukYh29NYWlqJU8Y_8(view, windowInsetsCompat);
            }
        });
    }

    public static /* synthetic */ WindowInsetsCompat $r8$lambda$UHw_oGDGFZukYh29NYWlqJU8Y_8(View view, WindowInsetsCompat windowInsetsCompat) {
        Insets insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
        view.setPadding(insets.left, insets.top, insets.right, insets.bottom);
        return WindowInsetsCompat.CONSUMED;
    }
}
