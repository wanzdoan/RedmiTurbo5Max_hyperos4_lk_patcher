package com.android.internal.telephony;

/* JADX INFO: loaded from: classes.dex */
public class UniTeleImsManager {
    private static UniTeleImsManager sInstance;

    public boolean isVowifiConnected(int i) {
        return false;
    }

    public static UniTeleImsManager getInstance() {
        if (sInstance == null) {
            sInstance = UniTelephonyInjectionFactory.getInstance().makeUniTeleImsManager();
        }
        return sInstance;
    }
}
