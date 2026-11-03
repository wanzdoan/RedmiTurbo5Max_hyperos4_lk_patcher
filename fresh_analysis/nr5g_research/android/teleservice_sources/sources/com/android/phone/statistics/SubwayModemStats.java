package com.android.phone.statistics;

import android.os.SystemClock;
import com.android.phone.statistics.utils.DefaultPreferenceHelper;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.statistics.utils.Utils;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class SubwayModemStats extends ModemStats {
    private boolean mInSubway = false;

    public void updateSubwayState(boolean z) {
        if (this.mInSubway == z) {
            return;
        }
        Utils.logD("SubwayModemStats", "Subway state change, inSubway=" + z);
        if (z) {
            startStats();
        } else {
            stopStats();
        }
        this.mInSubway = z;
    }

    private void startStats() {
        this.mLastBandTime = SystemClock.elapsedRealtime();
        Utils.logD("SubwayModemStats", "mLastBandTime=" + this.mLastBandTime);
    }

    private void stopStats() {
        this.mLastBandTime = -2L;
    }

    @Override // com.android.phone.statistics.ModemStats
    public void clearModemStats() {
        this.mBandStats.clear();
        try {
            DefaultPreferenceHelper.getInstance().setSubwayBandStats(ObjectSerializer.serialize(this.mBandStats));
        } catch (Exception e) {
            Utils.logE("SubwayModemStats", "clearModemStats exception:" + e);
        }
    }

    @Override // com.android.phone.statistics.ModemStats
    public void reloadModemStatsFromSP() {
        try {
            String subwayBandStats = DefaultPreferenceHelper.getInstance().getSubwayBandStats();
            if (subwayBandStats.isEmpty()) {
                return;
            }
            this.mBandStats = (ConcurrentHashMap) ObjectSerializer.deserialize(subwayBandStats);
        } catch (Exception e) {
            Utils.logE("SubwayModemStats", "reloadModemStatsFromSP exception:" + e);
        }
    }
}
