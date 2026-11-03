package com.android.internal.telephony;

import android.telephony.SignalStrength;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class UniSignalStrengthManager {
    private static UniSignalStrengthManager sInstance;

    public void checkAndNotifyFirstValidSs(int i, SignalStrength signalStrength, int i2, AtomicBoolean atomicBoolean) {
    }

    public static UniSignalStrengthManager getInstance() {
        if (sInstance == null) {
            sInstance = UniTelephonyInjectionFactory.getInstance().makeUniSignalStrengthManager();
        }
        return sInstance;
    }
}
