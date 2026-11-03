package com.mediatek.phone.ext;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/* JADX INFO: loaded from: classes.dex */
public interface IMobileNetworkSettingsExt {
    void customizeMobileNetworkIntentFilter(IntentFilter intentFilter);

    void handleMobileNetworkIntent(Context context, Intent intent);
}
