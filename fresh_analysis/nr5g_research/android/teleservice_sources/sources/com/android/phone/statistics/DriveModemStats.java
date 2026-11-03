package com.android.phone.statistics;

import android.os.SystemClock;
import com.android.phone.statistics.utils.DefaultPreferenceHelper;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.statistics.utils.Utils;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class DriveModemStats extends ModemStats {
    private boolean mIsDriving = false;

    public void updateDriveState(boolean z) {
        if (this.mIsDriving == z) {
            return;
        }
        Utils.logD("DriveModemStats", "drive state change, isDriving=" + z);
        if (z) {
            startStats();
        } else {
            stopStats();
        }
        this.mIsDriving = z;
    }

    private void startStats() {
        this.mLastBandTime = SystemClock.elapsedRealtime();
        Utils.logD("DriveModemStats", "mLastBandTime=" + this.mLastBandTime);
    }

    private void stopStats() {
        this.mLastBandTime = -2L;
    }

    @Override // com.android.phone.statistics.ModemStats
    public void clearModemStats() {
        this.mBandStats.clear();
        try {
            DefaultPreferenceHelper.getInstance().setDriveBandStats(ObjectSerializer.serialize(this.mBandStats));
        } catch (Exception e) {
            Utils.logE("DriveModemStats", "clearModemStats exception:" + e);
        }
    }

    @Override // com.android.phone.statistics.ModemStats
    public void reloadModemStatsFromSP() {
        try {
            String driveBandStats = DefaultPreferenceHelper.getInstance().getDriveBandStats();
            if (driveBandStats.isEmpty()) {
                return;
            }
            this.mBandStats = (ConcurrentHashMap) ObjectSerializer.deserialize(driveBandStats);
        } catch (Exception e) {
            Utils.logE("DriveModemStats", "reloadModemStatsFromSP exception:" + e);
        }
    }
}
