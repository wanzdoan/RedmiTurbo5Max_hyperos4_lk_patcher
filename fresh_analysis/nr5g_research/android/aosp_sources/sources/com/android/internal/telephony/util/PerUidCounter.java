package com.android.internal.telephony.util;

import android.util.SparseIntArray;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class PerUidCounter {
    private final int mMaxCountPerUid;
    final SparseIntArray mUidToCount = new SparseIntArray();

    public PerUidCounter(int i) {
        if (i <= 0) {
            Phone$$ExternalSyntheticBUOutline0.m("Maximum counter value must be positive");
            throw null;
        }
        this.mMaxCountPerUid = i;
    }

    public synchronized void incrementCountOrThrow(int i) {
        long j = ((long) this.mUidToCount.get(i, 0)) + 1;
        if (j > this.mMaxCountPerUid) {
            throw new IllegalStateException("Uid " + i + " exceeded its allowed limit");
        }
        this.mUidToCount.put(i, (int) j);
    }

    public synchronized void decrementCountOrThrow(int i) {
        try {
            int i2 = this.mUidToCount.get(i, 0) - 1;
            if (i2 < 0) {
                throw new IllegalStateException("BUG: too small count " + i2 + " for UID " + i);
            }
            SparseIntArray sparseIntArray = this.mUidToCount;
            if (i2 == 0) {
                sparseIntArray.delete(i);
            } else {
                sparseIntArray.put(i, i2);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized int get(int i) {
        return this.mUidToCount.get(i, 0);
    }
}
