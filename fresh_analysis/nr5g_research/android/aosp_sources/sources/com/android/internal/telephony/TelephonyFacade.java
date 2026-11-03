package com.android.internal.telephony;

import android.net.TrafficStats;
import android.os.SystemClock;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyFacade {
    public long getElapsedSinceBootMillis() {
        return SystemClock.elapsedRealtime();
    }

    public long getMobileTxBytes() {
        return TrafficStats.getMobileTxBytes();
    }

    public long getMobileRxBytes() {
        return TrafficStats.getMobileRxBytes();
    }
}
