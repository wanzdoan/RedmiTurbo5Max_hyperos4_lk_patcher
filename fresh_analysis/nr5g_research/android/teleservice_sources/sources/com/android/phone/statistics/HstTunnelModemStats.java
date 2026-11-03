package com.android.phone.statistics;

import android.os.SystemClock;
import com.android.phone.statistics.utils.Utils;

/* JADX INFO: loaded from: classes.dex */
public class HstTunnelModemStats extends ModemStats {
    private boolean mIsHstTunnel = false;

    public void updateHstTunnelState(boolean z) {
        if (this.mIsHstTunnel == z) {
            return;
        }
        this.mIsHstTunnel = z;
        Utils.logD("HstTunnelModemStats", "hstTunnel state change, isHstTunnel=" + z);
        if (z) {
            startStats();
        } else {
            stopStats();
        }
    }

    private void startStats() {
        this.mLastRatTime = SystemClock.elapsedRealtime();
        Utils.logD("HstTunnelModemStats", "mLastRatTime=" + this.mLastRatTime);
    }

    private void stopStats() {
        updateRatStats(this.mLastRatPlmn, this.mLastRatDataState);
    }

    public synchronized void updateRatState(String str, String str2) {
        try {
            Utils.logD("HstTunnelModemStats", "updateRatState plmn =" + str + ", ratDataState=" + str2);
            if (this.mIsHstTunnel) {
                updateRatStats(str, str2);
            } else {
                this.mLastRatPlmn = str;
                this.mLastRatDataState = str2;
            }
        } catch (Throwable th) {
            throw th;
        }
    }
}
