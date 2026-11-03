package com.android.internal.telephony;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class UniTeleSmsManager {
    private static UniTeleSmsManager sInstance;

    public int carrierTureError(Context context, int i, Phone phone) {
        return i;
    }

    public int dispatchCarrierTrue(Context context, Phone phone, SmsMessageBase smsMessageBase) {
        return -1;
    }

    public int getMaxSendRetries(Context context, int i) {
        return 3;
    }

    public boolean isFdnBlockDisabled(Context context, int i) {
        return false;
    }

    public boolean isVoLTERegisteredForPhone(int i) {
        return false;
    }

    public void sendOmacpBroadcast(Context context, Intent intent) {
    }

    public void sendSmsOnFailFdn(Context context, String str, int i) {
    }

    public UniTeleSmsManager() {
        Log.d("UniTeleSmsManager", "UniTeleSmsManager create instance!");
    }

    public static UniTeleSmsManager getInstance() {
        UniTeleSmsManager uniTeleSmsManager;
        synchronized (UniTeleSmsManager.class) {
            try {
                if (sInstance == null) {
                    sInstance = UniTelephonyInjectionFactory.getInstance().getUniTeleSmsManager();
                }
                uniTeleSmsManager = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return uniTeleSmsManager;
    }
}
