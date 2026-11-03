package com.android.phone.statistics;

import android.os.SystemClock;
import com.android.phone.statistics.utils.DefaultPreferenceHelper;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.statistics.utils.Utils;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class HstModemStats extends ModemStats {
    private boolean mIsHst = false;

    public void updateHstState(boolean z) {
        if (this.mIsHst == z) {
            return;
        }
        Utils.logD("HstModemStats", "hst state change, isHst=" + z);
        if (z) {
            startStats();
        } else {
            stopStats();
        }
        this.mIsHst = z;
    }

    private void startStats() {
        this.mLastBandTime = SystemClock.elapsedRealtime();
        this.mLastArfcnTime = SystemClock.elapsedRealtime();
        Utils.logD("HstModemStats", "mLastBandTime=" + this.mLastBandTime + ", mLastArfcnTime=" + this.mLastArfcnTime);
    }

    private void stopStats() {
        this.mLastBandTime = -2L;
        this.mLastArfcnTime = -2L;
    }

    @Override // com.android.phone.statistics.ModemStats
    public void clearModemStats() {
        this.mBandStats.clear();
        this.mArfcnStats.clear();
        try {
            DefaultPreferenceHelper.getInstance().setHstBandStats(ObjectSerializer.serialize(this.mBandStats));
            DefaultPreferenceHelper.getInstance().setHstArfcnStats(ObjectSerializer.serialize(this.mArfcnStats));
        } catch (Exception e) {
            Utils.logE("HstModemStats", "clearModemStats exception:" + e);
        }
    }

    @Override // com.android.phone.statistics.ModemStats
    public void reloadModemStatsFromSP() {
        try {
            String hstBandStats = DefaultPreferenceHelper.getInstance().getHstBandStats();
            if (!hstBandStats.isEmpty()) {
                this.mBandStats = (ConcurrentHashMap) ObjectSerializer.deserialize(hstBandStats);
            }
            String hstArfcnStats = DefaultPreferenceHelper.getInstance().getHstArfcnStats();
            if (hstArfcnStats.isEmpty()) {
                return;
            }
            this.mArfcnStats = (ConcurrentHashMap) ObjectSerializer.deserialize(hstArfcnStats);
        } catch (Exception e) {
            Utils.logE("HstModemStats", "reloadModemStatsFromSP exception:" + e);
        }
    }
}
