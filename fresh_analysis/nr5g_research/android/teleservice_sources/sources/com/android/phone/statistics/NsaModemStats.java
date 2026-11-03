package com.android.phone.statistics;

import android.os.SystemClock;
import com.android.phone.statistics.utils.DefaultPreferenceHelper;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.statistics.utils.Utils;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class NsaModemStats extends ModemStats {
    private boolean mIsNsaMode = false;

    public void updateNsaState(boolean z) {
        if (this.mIsNsaMode == z) {
            return;
        }
        Utils.logD("NsaModemStats", "nsa state change, isNsaMode=" + z);
        if (z) {
            startStats();
        } else {
            stopStats();
        }
        this.mIsNsaMode = z;
    }

    private void startStats() {
        this.mLastBandTime = SystemClock.elapsedRealtime();
        Utils.logD("NsaModemStats", "mLastBandTime=" + this.mLastBandTime);
    }

    private void stopStats() {
        this.mLastBandTime = -2L;
    }

    @Override // com.android.phone.statistics.ModemStats
    public void clearModemStats() {
        this.mBandStats.clear();
        try {
            DefaultPreferenceHelper.getInstance().setNsaBandStats(ObjectSerializer.serialize(this.mBandStats));
        } catch (Exception e) {
            Utils.logE("NsaModemStats", "clearModemStats exception:" + e);
        }
    }

    @Override // com.android.phone.statistics.ModemStats
    public void reloadModemStatsFromSP() {
        try {
            String nsaBandStats = DefaultPreferenceHelper.getInstance().getNsaBandStats();
            if (nsaBandStats.isEmpty()) {
                return;
            }
            this.mBandStats = (ConcurrentHashMap) ObjectSerializer.deserialize(nsaBandStats);
        } catch (Exception e) {
            Utils.logE("NsaModemStats", "reloadModemStatsFromSP exception:" + e);
        }
    }
}
