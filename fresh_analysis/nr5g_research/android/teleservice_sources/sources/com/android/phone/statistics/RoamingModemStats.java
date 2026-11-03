package com.android.phone.statistics;

import android.os.SystemClock;
import com.android.phone.statistics.utils.DefaultPreferenceHelper;
import com.android.phone.statistics.utils.DeviceUtil;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.statistics.utils.Utils;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class RoamingModemStats extends ModemStats {
    private boolean mRoaming = false;
    private boolean mLte = false;

    public void updateRoamingState(boolean z) throws Throwable {
        if (z == this.mRoaming) {
            return;
        }
        Utils.logD("RoamingModemStats", "roam state change, current roaming is" + z);
        if (this.mRoaming) {
            startStats();
        } else {
            stopStats();
        }
        this.mRoaming = z;
    }

    private void startStats() {
        this.mLastRatTime = SystemClock.elapsedRealtime();
        this.mLastBandTime = SystemClock.elapsedRealtime();
    }

    private void stopStats() throws Throwable {
        updateRatStats(this.mLastRatPlmn, this.mLastRatDataState);
        updateBandStats();
    }

    public synchronized void updateRatState(String str, String str2) {
        try {
            Utils.logD("RoamingModemStats", "update rat mLastRatPlmn = " + this.mLastRatPlmn + ", lastRat = " + this.mLastRatDataState + ", mRoaming = " + this.mRoaming);
            if (this.mRoaming) {
                updateRatStats(str, str2);
            } else {
                this.mLastRatPlmn = str;
                this.mLastRatDataState = str2;
            }
            processLteState(str2);
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized void updateBandStats() throws Throwable {
        RoamingModemStats roamingModemStats;
        try {
            try {
                long jElapsedRealtime = SystemClock.elapsedRealtime();
                String str = "Rsrp" + getRsrpBin();
                String ddsPlmn = getDdsPlmn();
                Utils.logD("RoamingModemStats", "updateNwSignalStats lastPlmn: " + ddsPlmn + ", rsrpBin=" + str);
                if (this.mRoaming && this.mLte) {
                    roamingModemStats = this;
                    roamingModemStats.storeBand(ddsPlmn, str, jElapsedRealtime, true);
                } else {
                    roamingModemStats = this;
                }
                return;
            } catch (Throwable th) {
                th = th;
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            throw th;
        }
        throw th;
    }

    public synchronized void updateBandStats(String str, int i, int i2) {
        updateBandStats();
        flashBandInfo(str, i, i2);
    }

    private void flashBandInfo(String str, int i, int i2) {
        setDdsPlmn(str);
        setRsrpBin(i);
        setBand(i2);
    }

    private void processLteState(String str) throws Throwable {
        String[] strArrSplit = str.split("_");
        if (strArrSplit.length != 2) {
            return;
        }
        boolean zIsLte = isLte(DeviceUtil.IS_INTERNATIONAL_BUILD ? strArrSplit[1] : strArrSplit[0]);
        if (zIsLte == this.mLte) {
            return;
        }
        Utils.logD("RoamingModemStats", "rat change, lte = " + zIsLte);
        this.mLte = zIsLte;
        lteStateChange();
    }

    private void lteStateChange() throws Throwable {
        if (this.mLte) {
            this.mLastBandTime = SystemClock.elapsedRealtime();
        } else {
            updateBandStats();
        }
    }

    private boolean isLte(String str) {
        if (str == null) {
            return false;
        }
        return "4G".equals(str) || "13".equals(str) || "19".equals(str);
    }

    @Override // com.android.phone.statistics.ModemStats
    public void clearModemStats() {
        this.mRatStats.clear();
        this.mBandStats.clear();
        this.mCallStats.clear();
        try {
            DefaultPreferenceHelper.getInstance().setRoamingRatStats(ObjectSerializer.serialize(this.mRatStats));
            DefaultPreferenceHelper.getInstance().setRoamingBandStats(ObjectSerializer.serialize(this.mBandStats));
            DefaultPreferenceHelper.getInstance().setRoamingCallStats(ObjectSerializer.serialize(this.mCallStats));
        } catch (Exception e) {
            Utils.logE("RoamingModemStats", "clearModemStats exception:" + e);
        }
    }

    @Override // com.android.phone.statistics.ModemStats
    public void reloadModemStatsFromSP() {
        DefaultPreferenceHelper defaultPreferenceHelper = DefaultPreferenceHelper.getInstance();
        try {
            String roamingBandStats = defaultPreferenceHelper.getRoamingBandStats();
            if (!roamingBandStats.isEmpty()) {
                this.mBandStats = (ConcurrentHashMap) ObjectSerializer.deserialize(roamingBandStats);
            }
            String roamingRatStats = defaultPreferenceHelper.getRoamingRatStats();
            if (!roamingRatStats.isEmpty()) {
                this.mRatStats = (ConcurrentHashMap) ObjectSerializer.deserialize(roamingRatStats);
            }
            String roamingCallStats = defaultPreferenceHelper.getRoamingCallStats();
            if (roamingCallStats.isEmpty()) {
                return;
            }
            this.mCallStats = (ConcurrentHashMap) ObjectSerializer.deserialize(roamingCallStats);
        } catch (Exception e) {
            Utils.logE("RoamingModemStats", "reloadModemStatsFromSP exception:" + e);
        }
    }
}
