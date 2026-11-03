package com.android.phone.statistics;

import android.os.SystemClock;
import com.android.phone.statistics.utils.DefaultPreferenceHelper;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.statistics.utils.Utils;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class HikingModemStats extends ModemStats {
    private boolean mIsHiking = false;

    public void updateHikingState(boolean z) {
        if (this.mIsHiking == z) {
            return;
        }
        Utils.logD("HikingModemStats", "hiking state change, isHiking=" + z);
        if (z) {
            startStats();
        } else {
            stopStats();
        }
        this.mIsHiking = z;
    }

    private void startStats() {
        this.mLastBandTime = SystemClock.elapsedRealtime();
        Utils.logD("HikingModemStats", "mLastBandTime=" + this.mLastBandTime);
    }

    private void stopStats() {
        this.mLastBandTime = -2L;
    }

    @Override // com.android.phone.statistics.ModemStats
    public void clearModemStats() {
        this.mBandStats.clear();
        try {
            DefaultPreferenceHelper.getInstance().setHikingBandStats(ObjectSerializer.serialize(this.mBandStats));
        } catch (Exception e) {
            Utils.logE("HikingModemStats", "clearModemStats exception:" + e);
        }
    }

    @Override // com.android.phone.statistics.ModemStats
    public void reloadModemStatsFromSP() {
        try {
            String hikingBandStats = DefaultPreferenceHelper.getInstance().getHikingBandStats();
            if (hikingBandStats.isEmpty()) {
                return;
            }
            this.mBandStats = (ConcurrentHashMap) ObjectSerializer.deserialize(hikingBandStats);
        } catch (Exception e) {
            Utils.logE("HikingModemStats", "reloadModemStatsFromSP exception:" + e);
        }
    }
}
