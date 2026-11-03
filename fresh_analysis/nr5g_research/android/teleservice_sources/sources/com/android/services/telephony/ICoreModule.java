package com.android.services.telephony;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public interface ICoreModule {
    String getModuleId();

    default int getPriority() {
        return 0;
    }

    boolean isEnabled(Context context);

    void onCreate(Context context);

    void onSimPresent();
}
