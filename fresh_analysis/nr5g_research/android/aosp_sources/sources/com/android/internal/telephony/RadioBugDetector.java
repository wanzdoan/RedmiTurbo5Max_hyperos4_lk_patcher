package com.android.internal.telephony;

import android.content.Context;
import android.provider.Settings;
import android.telephony.AnomalyReporter;
import com.android.telephony.Rlog;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class RadioBugDetector {
    protected static final int RADIO_BUG_REPETITIVE_SYSTEM_ERROR = 2;
    private Context mContext;
    private int mSlotId;
    private int mContinuousWakelockTimoutCount = 0;
    private int mRadioBugStatus = 0;
    private int mWakelockTimeoutThreshold = 0;
    private int mSystemErrorThreshold = 0;
    private HashMap mSysErrRecord = new HashMap();

    public RadioBugDetector(Context context, int i) {
        this.mContext = context;
        this.mSlotId = i;
        init();
    }

    private void init() {
        this.mWakelockTimeoutThreshold = Settings.Global.getInt(this.mContext.getContentResolver(), "radio_bug_wakelock_timeout_count_threshold", 10);
        this.mSystemErrorThreshold = Settings.Global.getInt(this.mContext.getContentResolver(), "radio_bug_system_error_count_threshold", 100);
    }

    public synchronized void detectRadioBug(int i, int i2) {
        try {
            this.mContinuousWakelockTimoutCount = 0;
            HashMap map = this.mSysErrRecord;
            if (i2 == 39) {
                this.mSysErrRecord.put(Integer.valueOf(i), Integer.valueOf(((Integer) map.getOrDefault(Integer.valueOf(i), 0)).intValue() + 1));
                broadcastBug(true);
            } else {
                map.remove(Integer.valueOf(i));
                if (!isFrequentSystemError()) {
                    this.mRadioBugStatus = 0;
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public void processWakelockTimeout() {
        this.mContinuousWakelockTimoutCount++;
        broadcastBug(false);
    }

    private synchronized void broadcastBug(boolean z) {
        try {
            if (z) {
                if (!isFrequentSystemError()) {
                    return;
                }
            } else if (this.mContinuousWakelockTimoutCount < this.mWakelockTimeoutThreshold) {
                return;
            }
            if (this.mRadioBugStatus == 0) {
                this.mRadioBugStatus = z ? 2 : 1;
                String str = "Repeated radio error " + this.mRadioBugStatus + " on slot " + this.mSlotId;
                Rlog.d("RadioBugDetector", str);
                Phone phone = PhoneFactory.getPhone(this.mSlotId);
                AnomalyReporter.reportAnomaly(UUID.fromString("d264ead0-3f05-11ea-b77f-2e728ce88125"), str, phone == null ? -1 : phone.getCarrierId());
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private boolean isFrequentSystemError() {
        Iterator it = this.mSysErrRecord.values().iterator();
        int iIntValue = 0;
        while (it.hasNext()) {
            iIntValue += ((Integer) it.next()).intValue();
            if (iIntValue >= this.mSystemErrorThreshold) {
                return true;
            }
        }
        return false;
    }

    public int getRadioBugStatus() {
        return this.mRadioBugStatus;
    }

    public int getWakelockTimeoutThreshold() {
        return this.mWakelockTimeoutThreshold;
    }

    public int getSystemErrorThreshold() {
        return this.mSystemErrorThreshold;
    }

    public int getWakelockTimoutCount() {
        return this.mContinuousWakelockTimoutCount;
    }
}
