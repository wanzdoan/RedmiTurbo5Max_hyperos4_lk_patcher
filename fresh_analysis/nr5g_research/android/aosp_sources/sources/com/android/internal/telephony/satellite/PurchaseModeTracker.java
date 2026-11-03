package com.android.internal.telephony.satellite;

import android.util.Pair;
import com.android.internal.telephony.Phone;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class PurchaseModeTracker {
    public static final Pair<Boolean, Integer> DEFAULT_PURCHASE_MODE_STATE = new Pair<>(Boolean.FALSE, 1);
    private final Object mSatellitePurchaseModeLock = new Object();
    private ConcurrentHashMap mSatellitePurchaseModeStatus;

    public PurchaseModeTracker(ConcurrentHashMap<Integer, Pair<Boolean, Integer>> concurrentHashMap) {
        new ConcurrentHashMap();
        this.mSatellitePurchaseModeStatus = concurrentHashMap;
    }

    public PurchaseModeTracker() {
        this.mSatellitePurchaseModeStatus = new ConcurrentHashMap();
        this.mSatellitePurchaseModeStatus = new ConcurrentHashMap();
    }

    public void setSatellitePurchaseModeInProgress(int i, boolean z, int i2) {
        synchronized (this.mSatellitePurchaseModeLock) {
            this.mSatellitePurchaseModeStatus.put(Integer.valueOf(i), new Pair(Boolean.valueOf(z), Integer.valueOf(i2)));
        }
    }

    public void notifySatellitePurchaseModeInProgress(Phone phone) {
        int subId = phone.getSubId();
        synchronized (this.mSatellitePurchaseModeLock) {
            Pair pair = (Pair) this.mSatellitePurchaseModeStatus.getOrDefault(Integer.valueOf(subId), DEFAULT_PURCHASE_MODE_STATE);
            phone.notifySatellitePurchaseModeChanged(((Boolean) pair.first).booleanValue(), ((Integer) pair.second).intValue());
        }
    }
}
