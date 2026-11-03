package com.android.phone.statistics;

import android.os.SystemClock;
import android.text.TextUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneProxy;
import com.android.phone.module.INetworkModule;
import com.android.phone.statistics.utils.DefaultPreferenceHelper;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiuiCurrentStateMonitor;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionInfo;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ModemStats implements Serializable {
    private static MiuiCurrentStateMonitor mMiuiCurrentStateMonitor;
    private String[] mBand;
    private StatsFiveGInfo mFiveGSignal;
    private StatsSignalInfo mGameSignal;
    private int mLastBandEuiccStatus;
    private String mLastDdsPlmn;
    private String mLastLteNrSignalDataLossState;
    private String mLastLteNrSignalLossPlmn;
    private long mLastLteNrSignalLossTime;
    private String mLastNonDdsNwClass;
    private String mLastNonDdsPlmn;
    protected String mLastRatDataState;
    private int mLastRatEuiccStatus;
    protected String mLastRatPlmn;
    protected long mLastRatTime;
    private String[] mLastVoWifiPlmn;
    private boolean[] mLastVoWifiState;
    private long[] mLastVoWifiTime;
    private String[] mLastVoltePlmn;
    private boolean[] mLastVolteState;
    private long[] mLastVolteTime;
    private StatsSignalInfo mWechatSignal;
    protected ConcurrentHashMap mCallStats = new ConcurrentHashMap();
    private ConcurrentHashMap mGameStats = new ConcurrentHashMap();
    private ConcurrentHashMap mSignalStats = new ConcurrentHashMap();
    protected ConcurrentHashMap mBandStats = new ConcurrentHashMap();
    protected ConcurrentHashMap mArfcnStats = new ConcurrentHashMap();
    private ConcurrentHashMap mNonDdsStats = new ConcurrentHashMap();
    protected ConcurrentHashMap mRatStats = new ConcurrentHashMap();
    private ConcurrentHashMap mLteNrSignalLossStats = new ConcurrentHashMap();
    private ConcurrentHashMap mFiveGStats = new ConcurrentHashMap();
    private ConcurrentHashMap mWechatStats = new ConcurrentHashMap();
    private ConcurrentHashMap mDualSimBands = new ConcurrentHashMap();
    private ConcurrentHashMap mVoWifiStats = new ConcurrentHashMap();
    private ConcurrentHashMap mVolteStats = new ConcurrentHashMap();
    private int mLastSnrBin = -2;
    private int mLastRsrpBin = -2;
    private int mLastBand = -2;
    private String mLastStrBand = "-1";
    private long mLastSnrTime = -2;
    protected long mLastBandTime = -2;
    private int mArfcnCount = 0;
    private int MAX_ARFCN_COUNT = 50;
    private int mLastArfcn = -1;
    private String mLastArfcnPlmn = "";
    protected long mLastArfcnTime = -2;
    private long mLastDualBandTime = -1;

    public boolean isInvalidArfcn(int i) {
        return i == -1 || i == 0;
    }

    public ModemStats() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mBand = new String[i];
        this.mLastDdsPlmn = "";
        this.mLastNonDdsPlmn = "";
        this.mLastNonDdsNwClass = "";
        this.mLastRatTime = -2L;
        this.mLastRatPlmn = "";
        this.mLastRatDataState = "";
        this.mLastLteNrSignalLossTime = -2L;
        this.mLastLteNrSignalLossPlmn = "";
        this.mLastLteNrSignalDataLossState = "";
        this.mGameSignal = new StatsSignalInfo();
        this.mWechatSignal = new StatsSignalInfo();
        this.mFiveGSignal = new StatsFiveGInfo();
        this.mLastVoWifiState = new boolean[i];
        this.mLastVoWifiTime = new long[i];
        this.mLastVoWifiPlmn = new String[i];
        this.mLastVolteState = new boolean[i];
        this.mLastVolteTime = new long[i];
        this.mLastVoltePlmn = new String[i];
        this.mLastBandEuiccStatus = -2;
        this.mLastRatEuiccStatus = -2;
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            this.mBand[i2] = "";
            initVoWifiStatsState(i2);
            initVolteStatsState(i2);
        }
        if (mMiuiCurrentStateMonitor == null) {
            mMiuiCurrentStateMonitor = MiuiCurrentStateMonitor.getInstance();
        }
    }

    public ConcurrentHashMap getSignalStats() {
        return this.mSignalStats;
    }

    public ConcurrentHashMap getCallStats() {
        return this.mCallStats;
    }

    public ConcurrentHashMap getGameStats() {
        return this.mGameStats;
    }

    public ConcurrentHashMap getBandStats() {
        return this.mBandStats;
    }

    public ConcurrentHashMap getArfcnStats() {
        return this.mArfcnStats;
    }

    public ConcurrentHashMap getNonDdsStats() {
        return this.mNonDdsStats;
    }

    public ConcurrentHashMap getRatStats() {
        return this.mRatStats;
    }

    public ConcurrentHashMap getLteNrSignalLossStats() {
        return this.mLteNrSignalLossStats;
    }

    public ConcurrentHashMap getFiveGStats() {
        return this.mFiveGStats;
    }

    public ConcurrentHashMap getWechatStats() {
        return this.mWechatStats;
    }

    public ConcurrentHashMap getDualSimbands() {
        return this.mDualSimBands;
    }

    public ConcurrentHashMap getVoWifiStats() {
        return this.mVoWifiStats;
    }

    public ConcurrentHashMap getVolteStats() {
        return this.mVolteStats;
    }

    public String getDdsPlmn() {
        return this.mLastDdsPlmn;
    }

    public void setDdsPlmn(String str) {
        this.mLastDdsPlmn = str;
    }

    public int getBand() {
        return this.mLastBand;
    }

    public int getArfcn() {
        return this.mLastArfcn;
    }

    public int getRsrpBin() {
        return this.mLastRsrpBin;
    }

    public int getSnrBin() {
        return this.mLastSnrBin;
    }

    public void setBand(int i) {
        this.mLastBand = i;
    }

    public void setStrBand(String str) {
        this.mLastStrBand = str;
    }

    public void setArfcn(int i) {
        this.mLastArfcn = i;
    }

    public void setRsrpBin(int i) {
        this.mLastRsrpBin = i;
    }

    public void setSnrBin(int i) {
        this.mLastSnrBin = i;
    }

    public void setNonDdsPlmn(String str) {
        this.mLastNonDdsPlmn = str;
    }

    public void setNonDdsNwClass(String str) {
        this.mLastNonDdsNwClass = str;
    }

    public void setArfcnDdsPlmn(String str) {
        this.mLastArfcnPlmn = str;
    }

    public synchronized void updateCallStats(int i, String str, boolean z, String str2, int i2) {
        MiuiCurrentStateMonitor miuiCurrentStateMonitor;
        try {
            if (TextUtils.isEmpty(str2)) {
                return;
            }
            if (i2 == 1) {
                str2 = str2 + "Drop";
            } else if (i2 == 2) {
                str2 = str2 + "ConnFail";
            }
            int customSimTypeForStatistic = Utils.getCustomSimTypeForStatistic(i);
            String strConcat = (z ? "MO_" : "MT_") + str2 + "_simType" + customSimTypeForStatistic;
            if ((MiuiPhoneUtils.isDeviceSupportFlip() || MiuiPhoneUtils.isDeviceSupportFold()) && (miuiCurrentStateMonitor = mMiuiCurrentStateMonitor) != null) {
                if (miuiCurrentStateMonitor.getFoldState()) {
                    strConcat = strConcat.concat("_foldState1");
                } else {
                    strConcat = strConcat.concat("_foldState0");
                }
            }
            ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mCallStats.get(str);
            if (concurrentHashMap == null) {
                concurrentHashMap = new ConcurrentHashMap();
                this.mCallStats.put(str, concurrentHashMap);
            }
            long jLongValue = concurrentHashMap.containsKey(strConcat) ? ((Long) concurrentHashMap.get(strConcat)).longValue() : 0L;
            Utils.logD("ModemStats", "updateCallStats oldCn=" + jLongValue);
            concurrentHashMap.put(strConcat, Long.valueOf(jLongValue + 1));
            try {
                if (this instanceof RoamingModemStats) {
                    DefaultPreferenceHelper.getInstance().setRoamingCallStats(ObjectSerializer.serialize(this.mCallStats));
                } else {
                    DefaultPreferenceHelper.getInstance().setCallStats(ObjectSerializer.serialize(this.mCallStats));
                }
            } catch (Exception e) {
                Utils.logE("ModemStats", "updateCallStats exception:" + e);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void storeNrSignal(ConcurrentHashMap concurrentHashMap, String str, String str2, long j) {
        if (j <= 0) {
            return;
        }
        concurrentHashMap.put(str, Long.valueOf((concurrentHashMap.containsKey(str) ? ((Long) concurrentHashMap.get(str)).longValue() : 0L) + j));
        concurrentHashMap.put(str2, Long.valueOf((concurrentHashMap.containsKey(str2) ? ((Long) concurrentHashMap.get(str2)).longValue() : 0L) + j));
        Utils.logD("ModemStats", "NR sceneStats = " + concurrentHashMap.toString());
    }

    private void storeLteSignal(ConcurrentHashMap concurrentHashMap, String str, String str2, String str3, long j) {
        if (j <= 0) {
            return;
        }
        concurrentHashMap.put(str, Long.valueOf((concurrentHashMap.containsKey(str) ? ((Long) concurrentHashMap.get(str)).longValue() : 0L) + j));
        concurrentHashMap.put(str2, Long.valueOf((concurrentHashMap.containsKey(str2) ? ((Long) concurrentHashMap.get(str2)).longValue() : 0L) + j));
        concurrentHashMap.put(str3, Long.valueOf((concurrentHashMap.containsKey(str3) ? ((Long) concurrentHashMap.get(str3)).longValue() : 0L) + j));
        Utils.logD("ModemStats", "LTE sceneStats = " + concurrentHashMap.toString());
    }

    private boolean updateSceneSignalStats(StatsSignalInfo statsSignalInfo, StatsSignalInfo statsSignalInfo2, String str, long j, boolean z, ConcurrentHashMap concurrentHashMap) {
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        if (!statsSignalInfo.isSceneSignalChanged(statsSignalInfo2) && !z) {
            return false;
        }
        if (statsSignalInfo.getSceneState()) {
            str2 = "SceneNRRsrp" + statsSignalInfo.getNrRsrpBin();
            str3 = "SceneNRSnr" + statsSignalInfo.getNrSnrBin();
            str4 = "SceneLTERsrp" + statsSignalInfo.getLteRsrpBin();
            str5 = "SceneLTESnr" + statsSignalInfo.getLteSnrBin();
            str6 = "SceneLTERsrq" + statsSignalInfo.getLteRsrqBin();
        } else {
            str2 = "NRRsrp" + statsSignalInfo.getNrRsrpBin();
            str3 = "NRSnr" + statsSignalInfo.getNrSnrBin();
            str4 = "LTERsrp" + statsSignalInfo.getLteRsrpBin();
            str5 = "LTESnr" + statsSignalInfo.getLteSnrBin();
            str6 = "LTERsrq" + statsSignalInfo.getLteRsrqBin();
        }
        String str7 = str2;
        String str8 = str3;
        String str9 = str4;
        String str10 = str5;
        String str11 = str6;
        Utils.logD("ModemStats", "nrRsrpKey = " + str7 + ", lteRsrpKey = " + str9);
        long lastTime = (j - statsSignalInfo.getLastTime()) / 1000;
        ConcurrentHashMap concurrentHashMap2 = (ConcurrentHashMap) concurrentHashMap.get(str);
        if (concurrentHashMap2 == null) {
            concurrentHashMap2 = new ConcurrentHashMap();
            concurrentHashMap.put(str, concurrentHashMap2);
            Utils.logD("ModemStats", "updateSceneSignalStats stats is null");
        }
        ConcurrentHashMap concurrentHashMap3 = concurrentHashMap2;
        storeNrSignal(concurrentHashMap3, str7, str8, lastTime);
        storeLteSignal(concurrentHashMap3, str9, str10, str11, lastTime);
        statsSignalInfo.setLastTime(Long.valueOf(j));
        return true;
    }

    /* JADX WARN: Code duplicated, block: B:14:0x0025  */
    private void storeNetowrkTypeChangedStats(StatsSignalInfo statsSignalInfo, StatsSignalInfo statsSignalInfo2, ConcurrentHashMap concurrentHashMap) {
        String str;
        if (statsSignalInfo.isNetworkTypeChanged(statsSignalInfo2) && statsSignalInfo.getSceneState()) {
            if (statsSignalInfo2.isValidNetworkType()) {
                if (statsSignalInfo2.getNetworkType() == 0) {
                    str = "WifiToLTE";
                } else if (statsSignalInfo2.getNetworkType() == 1) {
                    str = "LTEToWifi";
                } else {
                    str = "";
                }
            } else {
                str = "";
            }
            Utils.logD("ModemStats", "networkSwitchKey = ".concat(str));
            if (TextUtils.isEmpty(str)) {
                return;
            }
            long jLongValue = concurrentHashMap.containsKey(str) ? ((Long) concurrentHashMap.get(str)).longValue() : 0L;
            concurrentHashMap.put(str, Long.valueOf(1 + jLongValue));
            Utils.logD("ModemStats", "isNetworkTypeChanged oldCount =" + jLongValue);
        }
    }

    /* JADX WARN: Code duplicated, block: B:16:0x002b  */
    private void storeNetworkDurationStats(StatsSignalInfo statsSignalInfo, StatsSignalInfo statsSignalInfo2, ConcurrentHashMap concurrentHashMap, long j) {
        String str;
        if (statsSignalInfo.isNetworkTypeChanged(statsSignalInfo2) || statsSignalInfo.isSceneStatusChanged(statsSignalInfo2)) {
            if (!statsSignalInfo2.isValidNetworkType() || !statsSignalInfo2.getSceneState()) {
                if (statsSignalInfo.getNetworkType() == 0) {
                    str = "LTEConTime";
                } else if (statsSignalInfo.getNetworkType() == 1) {
                    str = "WifiConTime";
                } else {
                    str = "";
                }
            } else {
                str = "";
            }
            Utils.logD("ModemStats", "networkDurationKey = ".concat(str));
            if (TextUtils.isEmpty(str)) {
                return;
            }
            long networkConTime = (j - statsSignalInfo.getNetworkConTime()) / 1000;
            long jLongValue = concurrentHashMap.containsKey(str) ? ((Long) concurrentHashMap.get(str)).longValue() : 0L;
            concurrentHashMap.put(str, Long.valueOf(networkConTime + jLongValue));
            Utils.logD("ModemStats", "isNetworkTypeChanged oldDuration =" + jLongValue);
            statsSignalInfo.setNetworkConTime(j);
        }
    }

    private void storeNetworkRatChangedStats(StatsSignalInfo statsSignalInfo, StatsSignalInfo statsSignalInfo2, ConcurrentHashMap concurrentHashMap) {
        if (statsSignalInfo.isNetworkClassChanged(statsSignalInfo2) && statsSignalInfo.getSceneState()) {
            long jLongValue = concurrentHashMap.containsKey("ratChanged") ? ((Long) concurrentHashMap.get("ratChanged")).longValue() : 0L;
            concurrentHashMap.put("ratChanged", Long.valueOf(1 + jLongValue));
            Utils.logD("ModemStats", "isRatChanged oldCount =" + jLongValue);
        }
    }

    private boolean updateSceneNetworkStats(StatsSignalInfo statsSignalInfo, StatsSignalInfo statsSignalInfo2, String str, long j, ConcurrentHashMap concurrentHashMap) {
        if (!statsSignalInfo.isSceneNetworkChanged(statsSignalInfo2)) {
            return false;
        }
        ConcurrentHashMap concurrentHashMap2 = (ConcurrentHashMap) concurrentHashMap.get(str);
        if (concurrentHashMap2 == null) {
            concurrentHashMap2 = new ConcurrentHashMap();
            concurrentHashMap.put(str, concurrentHashMap2);
            Utils.logD("ModemStats", "updateSceneSignalStats stats is null");
        }
        ConcurrentHashMap concurrentHashMap3 = concurrentHashMap2;
        storeNetworkRatChangedStats(statsSignalInfo, statsSignalInfo2, concurrentHashMap3);
        storeNetowrkTypeChangedStats(statsSignalInfo, statsSignalInfo2, concurrentHashMap3);
        if ((!statsSignalInfo.isValidNetworkType() || statsSignalInfo.getNetworkConTime() < 0) && statsSignalInfo2.isValidNetworkType()) {
            Utils.logD("ModemStats", "network changed from none to connected when gaming");
            statsSignalInfo.setNetworkConTime(j);
            return true;
        }
        storeNetworkDurationStats(statsSignalInfo, statsSignalInfo2, concurrentHashMap3, j);
        return true;
    }

    public void updateSceneStats(StatsSignalInfo statsSignalInfo, boolean z) {
        StatsSignalInfo statsSignalInfo2;
        ConcurrentHashMap concurrentHashMap;
        ConcurrentHashMap concurrentHashMap2;
        if (statsSignalInfo.getSceneType() == 1) {
            concurrentHashMap2 = this.mGameStats;
            statsSignalInfo2 = this.mGameSignal;
        } else {
            if (statsSignalInfo.getSceneType() == 2) {
                concurrentHashMap2 = this.mWechatStats;
                statsSignalInfo2 = this.mWechatSignal;
            } else {
                statsSignalInfo2 = null;
                concurrentHashMap = null;
            }
            if (concurrentHashMap != null || statsSignalInfo2 == null) {
            }
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            String plmn = statsSignalInfo.getPlmn();
            Utils.logD("ModemStats", "status: " + statsSignalInfo.getSceneState() + ", currentTime: " + jElapsedRealtime);
            if (statsSignalInfo2.getLastTime() < 0 || (statsSignalInfo.getSceneState() && !statsSignalInfo2.getSceneState())) {
                statsSignalInfo2.copy(statsSignalInfo);
                statsSignalInfo2.setLastTime(Long.valueOf(jElapsedRealtime));
                if (statsSignalInfo.isValidNetworkType()) {
                    statsSignalInfo2.setNetworkConTime(jElapsedRealtime);
                    return;
                }
                return;
            }
            ConcurrentHashMap concurrentHashMap3 = concurrentHashMap;
            boolean zUpdateSceneSignalStats = updateSceneSignalStats(statsSignalInfo2, statsSignalInfo, plmn, jElapsedRealtime, z, concurrentHashMap3);
            boolean zUpdateSceneNetworkStats = updateSceneNetworkStats(statsSignalInfo2, statsSignalInfo, plmn, jElapsedRealtime, concurrentHashMap3);
            if (zUpdateSceneSignalStats || zUpdateSceneNetworkStats) {
                statsSignalInfo2.copy(statsSignalInfo);
            }
            try {
                if (statsSignalInfo.getSceneType() == 1) {
                    DefaultPreferenceHelper.getInstance().setGameStats(ObjectSerializer.serialize(concurrentHashMap3));
                    return;
                } else {
                    if (statsSignalInfo.getSceneType() == 2) {
                        DefaultPreferenceHelper.getInstance().setWechatStats(ObjectSerializer.serialize(concurrentHashMap3));
                        return;
                    }
                    return;
                }
            } catch (Exception e) {
                Utils.logE("ModemStats", "updateSceneSignalStats exception:" + e);
                return;
            }
        }
        concurrentHashMap = concurrentHashMap2;
        if (concurrentHashMap != null) {
        }
    }

    public synchronized void updateNwSignalStats(String str, int i, int i2, int i3, int i4, int i5, boolean z) throws Throwable {
        ModemStats modemStats;
        try {
            try {
                long jElapsedRealtime = SystemClock.elapsedRealtime();
                String str2 = "Rsrp" + getRsrpBin();
                String ddsPlmn = getDdsPlmn();
                Utils.logD("ModemStats", "updateNwSignalStats lastPlmn:" + ddsPlmn + ",rsrpBin=" + str2 + ",policy=" + i5);
                if (i5 == 1) {
                    modemStats = this;
                    modemStats.storeSnrBin(ddsPlmn, str2, jElapsedRealtime, z);
                    modemStats.storeBand(ddsPlmn, str2, jElapsedRealtime, z);
                    modemStats.storeArfcn(ddsPlmn, str2, jElapsedRealtime, z);
                } else if (i5 == 2) {
                    modemStats = this;
                    modemStats.storeSnrBin(ddsPlmn, str2, jElapsedRealtime, z);
                } else if (i5 == 3) {
                    modemStats = this;
                    modemStats.storeBand(ddsPlmn, str2, jElapsedRealtime, z);
                } else if (i5 != 4) {
                    modemStats = this;
                } else {
                    modemStats = this;
                    modemStats.storeArfcn(ddsPlmn, str2, jElapsedRealtime, z);
                }
                modemStats.setDdsPlmn(str);
                modemStats.setRsrpBin(i);
                modemStats.setSnrBin(i2);
                modemStats.setBand(i3);
                modemStats.setArfcn(i4);
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

    private void storeSnrBin(String str, String str2, long j, boolean z) {
        MiuiCurrentStateMonitor miuiCurrentStateMonitor;
        long j2 = this.mLastSnrTime;
        if (j2 == -2 || j - j2 < 1000 || TextUtils.isEmpty(str)) {
            this.mLastSnrTime = j;
            return;
        }
        Utils.logD("ModemStats", "storeSnrBin plmn:" + str + ",rsrpBin=" + str2 + ",curTime=" + j + ",mLastSnrTime=" + this.mLastSnrTime);
        long j3 = (j - this.mLastSnrTime) / 1000;
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append("_Snr");
        sb.append(getSnrBin());
        String string = sb.toString();
        if ((MiuiPhoneUtils.isDeviceSupportFlip() || MiuiPhoneUtils.isDeviceSupportFold()) && (miuiCurrentStateMonitor = mMiuiCurrentStateMonitor) != null) {
            if (miuiCurrentStateMonitor.getFoldState()) {
                string = string.concat("_foldState1");
            } else {
                string = string.concat("_foldState0");
            }
        }
        ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mSignalStats.get(str);
        if (concurrentHashMap == null) {
            concurrentHashMap = new ConcurrentHashMap();
            this.mSignalStats.put(str, concurrentHashMap);
        }
        long jLongValue = concurrentHashMap.containsKey(string) ? ((Long) concurrentHashMap.get(string)).longValue() : 0L;
        long j4 = j3 + jLongValue;
        concurrentHashMap.put(string, Long.valueOf(j4));
        Utils.logD("ModemStats", "updateLteSignalStats oldDuration=" + jLongValue + ",whole=" + j4);
        try {
            DefaultPreferenceHelper.getInstance().setSignalStats(ObjectSerializer.serialize(this.mSignalStats));
            Utils.logD("ModemStats", "signal stats = " + this.mSignalStats.toString());
        } catch (Exception e) {
            Utils.logE("ModemStats", "updateLteSignalStats exception:" + e);
        }
        this.mLastSnrTime = j;
    }

    protected void storeBand(String str, String str2, long j, boolean z) {
        if (PhoneProxy.isBandForEarfcnEnabled()) {
            long j2 = this.mLastBandTime;
            if (j2 == -2 || j - j2 < 1000 || TextUtils.isEmpty(str)) {
                this.mLastBandTime = j;
                return;
            }
            Utils.logD("ModemStats", "storeBand plmn:" + str + ",rsrpBin=" + str2 + ",curTime=" + j + ",mLastBandTime=" + this.mLastBandTime);
            long j3 = (j - this.mLastBandTime) / 1000;
            String str3 = str2 + "_B" + getBand() + "_simType" + Utils.getCustomSimTypeForStatistic(getDefaultDataSlotId());
            int euiccStatus = this.mLastBandEuiccStatus;
            if (euiccStatus == -2) {
                euiccStatus = getEuiccStatus();
            }
            ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mBandStats.get(str);
            if (concurrentHashMap == null) {
                concurrentHashMap = new ConcurrentHashMap();
                this.mBandStats.put(str, concurrentHashMap);
            }
            long jLongValue = concurrentHashMap.containsKey(str3) ? ((Long) concurrentHashMap.get(str3)).longValue() : 0L;
            if (euiccStatus == 1) {
                String strConcat = "eSIM_".concat(str3);
                concurrentHashMap.put(strConcat, Long.valueOf((concurrentHashMap.containsKey(strConcat) ? ((Long) concurrentHashMap.get(strConcat)).longValue() : 0L) + j3));
            }
            long j4 = j3 + jLongValue;
            concurrentHashMap.put(str3, Long.valueOf(j4));
            Utils.logD("ModemStats", "updateLteBandStats oldDuration=" + jLongValue + ",whole=" + j4);
            try {
                if (this instanceof RoamingModemStats) {
                    DefaultPreferenceHelper.getInstance().setRoamingBandStats(ObjectSerializer.serialize(this.mBandStats));
                } else {
                    DefaultPreferenceHelper.getInstance().setBandStats(ObjectSerializer.serialize(this.mBandStats));
                }
                Utils.logD("ModemStats", "band stats = " + this.mBandStats.toString());
            } catch (Exception e) {
                Utils.logE("ModemStats", "updateLteSignalStats exception:" + e);
            }
            this.mLastBandTime = j;
            this.mLastBandEuiccStatus = getEuiccStatus();
        }
    }

    private void storeArfcn(String str, String str2, long j, boolean z) {
        long jLongValue;
        long j2 = this.mLastArfcnTime;
        if (j2 == -2 || j - j2 < 1000 || TextUtils.isEmpty(str)) {
            this.mLastArfcnTime = j;
            return;
        }
        Utils.logD("ModemStats", "storeArfcn plmn:" + str + ",rsrpBin=" + str2 + ",curTime=" + j + ",mLastArfcnTime=" + this.mLastArfcnTime);
        long j3 = (j - this.mLastArfcnTime) / 1000;
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append("_Arfcn");
        sb.append(getArfcn());
        String string = sb.toString();
        ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mArfcnStats.get(str);
        if (concurrentHashMap == null) {
            concurrentHashMap = new ConcurrentHashMap();
            this.mArfcnStats.put(str, concurrentHashMap);
        }
        if (concurrentHashMap.containsKey(string)) {
            jLongValue = ((Long) concurrentHashMap.get(string)).longValue();
            concurrentHashMap.put(string, Long.valueOf(jLongValue + j3));
        } else {
            if (this.mArfcnCount < this.MAX_ARFCN_COUNT) {
                concurrentHashMap.put(string, Long.valueOf(j3));
                this.mArfcnCount++;
                Utils.logD("ModemStats", "mArfcnCount = " + this.mArfcnCount);
            }
            jLongValue = 0;
        }
        Utils.logD("ModemStats", "updateLteArfcnStats oldDuration=" + jLongValue + ",whole=" + (jLongValue + j3));
        try {
            if (this.mArfcnCount < this.MAX_ARFCN_COUNT) {
                DefaultPreferenceHelper.getInstance().setArfcnStats(ObjectSerializer.serialize(this.mArfcnStats));
                Utils.logD("ModemStats", "arfcn stats = " + this.mArfcnStats.toString());
            }
        } catch (Exception e) {
            Utils.logE("ModemStats", "updateLteArfcnStats exception:" + e);
        }
        this.mLastArfcnTime = j;
    }

    public void clearModemStats() {
        this.mSignalStats.clear();
        this.mBandStats.clear();
        this.mArfcnStats.clear();
        this.mCallStats.clear();
        this.mGameStats.clear();
        this.mNonDdsStats.clear();
        this.mRatStats.clear();
        this.mLteNrSignalLossStats.clear();
        this.mFiveGStats.clear();
        this.mWechatStats.clear();
        this.mDualSimBands.clear();
        this.mVoWifiStats.clear();
        this.mVolteStats.clear();
        this.mArfcnCount = 0;
        try {
            DefaultPreferenceHelper.getInstance().setSignalStats(ObjectSerializer.serialize(this.mSignalStats));
            DefaultPreferenceHelper.getInstance().setBandStats(ObjectSerializer.serialize(this.mBandStats));
            DefaultPreferenceHelper.getInstance().setArfcnStats(ObjectSerializer.serialize(this.mArfcnStats));
            DefaultPreferenceHelper.getInstance().setCallStats(ObjectSerializer.serialize(this.mCallStats));
            DefaultPreferenceHelper.getInstance().setGameStats(ObjectSerializer.serialize(this.mGameStats));
            DefaultPreferenceHelper.getInstance().setNonDdsStats(ObjectSerializer.serialize(this.mNonDdsStats));
            DefaultPreferenceHelper.getInstance().setRatStats(ObjectSerializer.serialize(this.mRatStats));
            DefaultPreferenceHelper.getInstance().setLteNrSignalLossStats(ObjectSerializer.serialize(this.mLteNrSignalLossStats));
            DefaultPreferenceHelper.getInstance().setFiveGStats(ObjectSerializer.serialize(this.mFiveGStats));
            DefaultPreferenceHelper.getInstance().setWechatStats(ObjectSerializer.serialize(this.mWechatStats));
            DefaultPreferenceHelper.getInstance().setVoWifiStats(ObjectSerializer.serialize(this.mVoWifiStats));
            DefaultPreferenceHelper.getInstance().setVolteStats(ObjectSerializer.serialize(this.mVolteStats));
            DefaultPreferenceHelper.getInstance().setStringPreference("event_dual_sim_band", ObjectSerializer.serialize(this.mDualSimBands));
        } catch (Exception e) {
            Utils.logE("ModemStats", "clearModemStats exception:" + e);
        }
    }

    public boolean isNwSignalChanged(String str, int i) {
        return (TextUtils.equals(str, this.mLastDdsPlmn) && i == this.mLastRsrpBin) ? false : true;
    }

    public boolean isOnlyBandChanged(int i) {
        return i != this.mLastBand;
    }

    public boolean isOnlySnrChanged(int i) {
        return i != this.mLastSnrBin;
    }

    public boolean isOnlyArfcnChanged(int i) {
        return i != this.mLastArfcn;
    }

    public synchronized void updateNonDdsStats(String str, String str2) {
        Utils.logD("ModemStats", "updateNonDdsStats plmn=" + str + ",category=" + str2);
        setNonDdsPlmn(str);
        setNonDdsNwClass(str2);
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            storeNonDds(str, str2);
            return;
        }
        Utils.logE("ModemStats", "updateNonDdsStats is empty!!!");
    }

    private void storeNonDds(String str, String str2) {
        Utils.logD("ModemStats", "storeNonDds plmn:" + str + ",category=" + str2);
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            Utils.logE("ModemStats", "storeNonDds is empty!!!");
            return;
        }
        this.mNonDdsStats.put("Nondds_Network", str);
        this.mNonDdsStats.put("Nondds_Rat", str2);
        try {
            DefaultPreferenceHelper.getInstance().setNonDdsStats(ObjectSerializer.serialize(this.mNonDdsStats));
        } catch (Exception e) {
            Utils.logE("ModemStats", "storeNonDds exception:" + e);
        }
    }

    public boolean isNonDdsNwChanged(String str, String str2) {
        return (TextUtils.equals(str, this.mLastNonDdsPlmn) && TextUtils.equals(str2, this.mLastNonDdsNwClass)) ? false : true;
    }

    public void reloadModemStatsFromSP() {
        DefaultPreferenceHelper defaultPreferenceHelper = DefaultPreferenceHelper.getInstance();
        try {
            String signalStats = defaultPreferenceHelper.getSignalStats();
            if (!signalStats.isEmpty()) {
                this.mSignalStats = (ConcurrentHashMap) ObjectSerializer.deserialize(signalStats);
            }
            String bandStats = defaultPreferenceHelper.getBandStats();
            if (!bandStats.isEmpty()) {
                this.mBandStats = (ConcurrentHashMap) ObjectSerializer.deserialize(bandStats);
            }
            String arfcnStats = defaultPreferenceHelper.getArfcnStats();
            if (!arfcnStats.isEmpty()) {
                this.mArfcnStats = (ConcurrentHashMap) ObjectSerializer.deserialize(arfcnStats);
            }
            String gameStats = defaultPreferenceHelper.getGameStats();
            if (!gameStats.isEmpty()) {
                this.mGameStats = (ConcurrentHashMap) ObjectSerializer.deserialize(gameStats);
            }
            String callStats = defaultPreferenceHelper.getCallStats();
            if (!callStats.isEmpty()) {
                this.mCallStats = (ConcurrentHashMap) ObjectSerializer.deserialize(callStats);
            }
            String nonDdsStats = defaultPreferenceHelper.getNonDdsStats();
            if (!nonDdsStats.isEmpty()) {
                this.mNonDdsStats = (ConcurrentHashMap) ObjectSerializer.deserialize(nonDdsStats);
            }
            String ratStats = defaultPreferenceHelper.getRatStats();
            if (!ratStats.isEmpty()) {
                this.mRatStats = (ConcurrentHashMap) ObjectSerializer.deserialize(ratStats);
            }
            String lteNrSignalLossStats = defaultPreferenceHelper.getLteNrSignalLossStats();
            if (!lteNrSignalLossStats.isEmpty()) {
                this.mLteNrSignalLossStats = (ConcurrentHashMap) ObjectSerializer.deserialize(lteNrSignalLossStats);
            }
            String fiveGStats = defaultPreferenceHelper.getFiveGStats();
            if (!fiveGStats.isEmpty()) {
                this.mFiveGStats = (ConcurrentHashMap) ObjectSerializer.deserialize(fiveGStats);
            }
            String wechatStats = defaultPreferenceHelper.getWechatStats();
            if (!wechatStats.isEmpty()) {
                this.mWechatStats = (ConcurrentHashMap) ObjectSerializer.deserialize(wechatStats);
            }
            String voWifiStats = defaultPreferenceHelper.getVoWifiStats();
            if (!voWifiStats.isEmpty()) {
                this.mVoWifiStats = (ConcurrentHashMap) ObjectSerializer.deserialize(voWifiStats);
            }
            String volteStats = defaultPreferenceHelper.getVolteStats();
            if (!volteStats.isEmpty()) {
                this.mVolteStats = (ConcurrentHashMap) ObjectSerializer.deserialize(volteStats);
            }
            String stringPreference = defaultPreferenceHelper.getStringPreference("event_dual_sim_band");
            if (TextUtils.isEmpty(stringPreference)) {
                return;
            }
            this.mDualSimBands = (ConcurrentHashMap) ObjectSerializer.deserialize(stringPreference);
        } catch (Exception e) {
            Utils.logE("ModemStats", "reloadModemStatsFromSP exception:" + e);
        }
    }

    public synchronized boolean isRatOrDataStateChanged(String str, String str2) {
        return (TextUtils.equals(str, this.mLastRatPlmn) && TextUtils.equals(str2, this.mLastRatDataState)) ? false : true;
    }

    public synchronized void updateRatStats(String str, String str2) {
        String str3;
        try {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            long j = (jElapsedRealtime - this.mLastRatTime) / 1000;
            String str4 = TextUtils.isEmpty(this.mLastRatPlmn) ? str : this.mLastRatPlmn;
            int customSimTypeForStatistic = Utils.getCustomSimTypeForStatistic(getDefaultDataSlotId());
            if (TextUtils.isEmpty(this.mLastRatDataState)) {
                str3 = str2;
            } else {
                str3 = this.mLastRatDataState + "_simType" + customSimTypeForStatistic;
            }
            int euiccStatus = this.mLastRatEuiccStatus;
            if (euiccStatus == -2) {
                euiccStatus = getEuiccStatus();
            }
            Utils.logD("ModemStats", "updateRatStats lastPlmn:" + str4 + ",ratDataState=" + str2 + ",mLastRatDataState=" + this.mLastRatDataState);
            ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mRatStats.get(str4);
            if (concurrentHashMap == null) {
                concurrentHashMap = new ConcurrentHashMap();
                this.mRatStats.put(str4, concurrentHashMap);
            }
            long jLongValue = concurrentHashMap.containsKey(str3) ? ((Long) concurrentHashMap.get(str3)).longValue() : 0L;
            if (euiccStatus == 1) {
                String str5 = "eSIM_" + str3;
                concurrentHashMap.put(str5, Long.valueOf((concurrentHashMap.containsKey(str5) ? ((Long) concurrentHashMap.get(str5)).longValue() : 0L) + j));
            }
            long j2 = jLongValue;
            long j3 = j2 + j;
            concurrentHashMap.put(str3, Long.valueOf(j3));
            Utils.logD("ModemStats", "updateRatStats oldDuration=" + j2 + ",whole=" + j3);
            try {
                if (this instanceof RoamingModemStats) {
                    DefaultPreferenceHelper.getInstance().setRoamingRatStats(ObjectSerializer.serialize(this.mRatStats));
                } else if (this instanceof HstTunnelModemStats) {
                    ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).processForegroundInHstTunnel(true);
                    ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).processSignalStrengthInHstTunnel(true);
                    ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).recordHstTunnel(str4, str3, j);
                } else {
                    DefaultPreferenceHelper.getInstance().setRatStats(ObjectSerializer.serialize(this.mRatStats));
                }
            } catch (Exception e) {
                Utils.logE("ModemStats", "updateRatStats exception:" + e);
            }
            this.mLastRatTime = jElapsedRealtime;
            this.mLastRatPlmn = str;
            this.mLastRatDataState = str2;
            this.mLastRatEuiccStatus = getEuiccStatus();
        } catch (Throwable th) {
            throw th;
        }
    }

    private void initVoWifiStatsState(int i) {
        this.mLastVoWifiState[i] = false;
        this.mLastVoWifiTime[i] = 0;
        this.mLastVoWifiPlmn[i] = "";
    }

    private void initVolteStatsState(int i) {
        this.mLastVolteState[i] = false;
        this.mLastVolteTime[i] = 0;
        this.mLastVoltePlmn[i] = "";
    }

    public synchronized void updateVoWifiStats(String str, boolean z, int i) {
        try {
            Utils.logD("ModemStats", "updateVoWifiStats plmn = " + str + ", voWifi = " + z + ", phoneId = " + i);
            if (z == this.mLastVoWifiState[i]) {
                return;
            }
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            if (z) {
                this.mLastVoWifiPlmn[i] = str;
                this.mLastVoWifiTime[i] = jElapsedRealtime;
                this.mLastVoWifiState[i] = z;
            } else {
                long j = (jElapsedRealtime - this.mLastVoWifiTime[i]) / 1000;
                Utils.logD("ModemStats", "updateVoWifiStats duration=" + j);
                ConcurrentHashMap concurrentHashMap = this.mVoWifiStats;
                concurrentHashMap.put(this.mLastVoWifiPlmn[i], Long.valueOf(((Long) concurrentHashMap.getOrDefault(str, 0L)).longValue() + j));
                initVoWifiStatsState(i);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void updateVolteStats(String str, boolean z, int i) {
        try {
            Utils.logD("ModemStats", "updateVolteStats plmn = " + str + ", register = " + z + ", phoneId = " + i);
            if (z == this.mLastVolteState[i]) {
                return;
            }
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            if (z) {
                this.mLastVoltePlmn[i] = str;
                this.mLastVolteTime[i] = jElapsedRealtime;
                this.mLastVolteState[i] = z;
            } else {
                long j = (jElapsedRealtime - this.mLastVolteTime[i]) / 1000;
                Utils.logD("ModemStats", "updateVolteStats duration=" + j);
                ConcurrentHashMap concurrentHashMap = this.mVolteStats;
                concurrentHashMap.put(this.mLastVoltePlmn[i], Long.valueOf(((Long) concurrentHashMap.getOrDefault(str, 0L)).longValue() + j));
                initVolteStatsState(i);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void updateNoLteNrSignalStats(String str, String str2, boolean z) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.mLastLteNrSignalLossTime;
        if (j == -2) {
            if (!z) {
                recordCurrentSignalInfo(jElapsedRealtime, str, str2);
            }
            return;
        }
        long j2 = (jElapsedRealtime - j) / 1000;
        if (j2 >= 30) {
            ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mLteNrSignalLossStats.get(this.mLastLteNrSignalLossPlmn);
            if (concurrentHashMap == null) {
                concurrentHashMap = new ConcurrentHashMap();
                this.mLteNrSignalLossStats.put(this.mLastLteNrSignalLossPlmn, concurrentHashMap);
            }
            long jLongValue = concurrentHashMap.containsKey(this.mLastLteNrSignalDataLossState) ? ((Long) concurrentHashMap.get(this.mLastLteNrSignalDataLossState)).longValue() : 0L;
            long j3 = j2 + jLongValue;
            concurrentHashMap.put(this.mLastLteNrSignalDataLossState, Long.valueOf(j3));
            Utils.logD("ModemStats", "updateRatStats oldDuration=" + jLongValue + ",whole=" + j3);
            try {
                DefaultPreferenceHelper.getInstance().setRatStats(ObjectSerializer.serialize(this.mLteNrSignalLossStats));
            } catch (Exception e) {
                Utils.logE("ModemStats", "updateNoLteNrSignalStats exception:" + e);
            }
        }
        if (z) {
            recordCurrentSignalInfo(-2L, "", "");
        } else {
            recordCurrentSignalInfo(jElapsedRealtime, str, str2);
        }
    }

    private void recordCurrentSignalInfo(long j, String str, String str2) {
        this.mLastLteNrSignalLossTime = j;
        this.mLastLteNrSignalLossPlmn = str;
        this.mLastLteNrSignalDataLossState = str2;
    }

    private boolean isShowFiveGIconChanged(StatsFiveGInfo statsFiveGInfo) {
        if (statsFiveGInfo == null) {
            return false;
        }
        return (TextUtils.equals(statsFiveGInfo.getOperator(), this.mFiveGSignal.getOperator()) && statsFiveGInfo.isShowFiveGIcon() == this.mFiveGSignal.isShowFiveGIcon()) ? false : true;
    }

    private boolean isFiveGRsrpChanged(StatsFiveGInfo statsFiveGInfo) {
        if (statsFiveGInfo == null) {
            return false;
        }
        return (statsFiveGInfo.getRsrpLevel() == this.mFiveGSignal.getRsrpLevel() && statsFiveGInfo.getSnrLevel() == this.mFiveGSignal.getSnrLevel() && statsFiveGInfo.getConfigType() == this.mFiveGSignal.getConfigType() && TextUtils.equals(statsFiveGInfo.getOperator(), this.mFiveGSignal.getOperator())) ? false : true;
    }

    private boolean isFiveGBandChanged(StatsFiveGInfo statsFiveGInfo) {
        if (statsFiveGInfo == null) {
            return false;
        }
        return (statsFiveGInfo.getRsrpLevel() == this.mFiveGSignal.getRsrpLevel() && statsFiveGInfo.getBand() == this.mFiveGSignal.getBand() && statsFiveGInfo.getConfigType() == this.mFiveGSignal.getConfigType() && TextUtils.equals(statsFiveGInfo.getOperator(), this.mFiveGSignal.getOperator())) ? false : true;
    }

    private boolean isFiveGArfcnChanged(StatsFiveGInfo statsFiveGInfo) {
        if (statsFiveGInfo == null) {
            return false;
        }
        return (statsFiveGInfo.getRsrpLevel() == this.mFiveGSignal.getRsrpLevel() && statsFiveGInfo.getArfcn() == this.mFiveGSignal.getArfcn() && statsFiveGInfo.getConfigType() == this.mFiveGSignal.getConfigType() && TextUtils.equals(statsFiveGInfo.getOperator(), this.mFiveGSignal.getOperator())) ? false : true;
    }

    public synchronized void updateFiveGStats(String str, StatsFiveGInfo statsFiveGInfo, boolean z) {
        try {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            Utils.logD("ModemStats", "updateFiveGStats currentTime = " + jElapsedRealtime);
            ConcurrentHashMap fiveGStatByPlmn = getFiveGStatByPlmn(str);
            if (statsFiveGInfo == null) {
                statsFiveGInfo = this.mFiveGSignal;
            }
            if (z) {
                updateShowFiveGIconDuration(fiveGStatByPlmn, statsFiveGInfo, jElapsedRealtime);
                updateFiveGBandDuration(str, statsFiveGInfo, jElapsedRealtime);
                updateFiveGArfcnDuration(str, statsFiveGInfo, jElapsedRealtime);
                updateFiveGRsrpDuration(fiveGStatByPlmn, statsFiveGInfo, jElapsedRealtime);
                storeFiveGStat();
                Utils.logD("ModemStats", "updateFiveGStats force update done!");
            } else {
                boolean zIsShowFiveGIconChanged = isShowFiveGIconChanged(statsFiveGInfo);
                boolean zIsFiveGRsrpChanged = isFiveGRsrpChanged(statsFiveGInfo);
                boolean zIsFiveGBandChanged = isFiveGBandChanged(statsFiveGInfo);
                boolean zIsFiveGArfcnChanged = isFiveGArfcnChanged(statsFiveGInfo);
                Utils.logD("ModemStats", "updateFiveGStats fiveGIconChanged =" + zIsShowFiveGIconChanged + ", fiveGRsrpChanged = " + zIsFiveGRsrpChanged + ", fiveGBandChanged = " + zIsFiveGBandChanged + ", fiveGArfcnChanged = " + zIsFiveGArfcnChanged);
                if (zIsShowFiveGIconChanged) {
                    updateShowFiveGIconDuration(fiveGStatByPlmn, statsFiveGInfo, jElapsedRealtime);
                }
                if (zIsFiveGBandChanged) {
                    updateFiveGBandDuration(str, statsFiveGInfo, jElapsedRealtime);
                }
                if (zIsFiveGArfcnChanged) {
                    updateFiveGArfcnDuration(str, statsFiveGInfo, jElapsedRealtime);
                }
                if (zIsFiveGRsrpChanged) {
                    updateFiveGRsrpDuration(fiveGStatByPlmn, statsFiveGInfo, jElapsedRealtime);
                }
                if (zIsShowFiveGIconChanged || zIsFiveGBandChanged || zIsFiveGRsrpChanged || zIsFiveGArfcnChanged) {
                    this.mFiveGSignal.setOperator(statsFiveGInfo.getOperator());
                }
                if (zIsFiveGBandChanged || zIsFiveGRsrpChanged || zIsFiveGArfcnChanged) {
                    this.mFiveGSignal.setConfigType(statsFiveGInfo.getConfigType());
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void updateShowFiveGIconDuration(ConcurrentHashMap concurrentHashMap, StatsFiveGInfo statsFiveGInfo, long j) {
        if (this.mFiveGSignal.getShowIconTime() == -2) {
            Utils.logD("ModemStats", "ignore 5gIcon duration!");
        } else {
            long showIconTime = j - this.mFiveGSignal.getShowIconTime();
            if (showIconTime >= 1000) {
                Utils.logD("ModemStats", "lastShowFiveGIcon = " + this.mFiveGSignal.isShowFiveGIcon());
                saveFiveGDuration(concurrentHashMap, this.mFiveGSignal.isShowFiveGIcon() ? "FiveGIcon" : "NonFiveGIcon", Long.valueOf(showIconTime / 1000));
            }
        }
        this.mFiveGSignal.setShowFiveGIcon(statsFiveGInfo.isShowFiveGIcon());
        this.mFiveGSignal.setShowIconTime(j);
    }

    private void updateFiveGRsrpDuration(ConcurrentHashMap concurrentHashMap, StatsFiveGInfo statsFiveGInfo, long j) {
        MiuiCurrentStateMonitor miuiCurrentStateMonitor;
        if (this.mFiveGSignal.getRsrpTime() == -2) {
            Utils.logD("ModemStats", "ignore 5grsrp duration!");
        } else {
            long rsrpTime = j - this.mFiveGSignal.getRsrpTime();
            if (this.mFiveGSignal.getRsrpLevel() > 0 && rsrpTime >= 1000) {
                Utils.logD("ModemStats", "rsrpLevel = " + this.mFiveGSignal.getRsrpLevel() + ", snrLevel " + this.mFiveGSignal.getSnrLevel());
                String strConcat = (this.mFiveGSignal.getConfigType() == 1 ? "SaRsrp" : "Rsrp") + this.mFiveGSignal.getRsrpLevel() + "_Snr" + this.mFiveGSignal.getSnrLevel();
                if ((MiuiPhoneUtils.isDeviceSupportFlip() || MiuiPhoneUtils.isDeviceSupportFold()) && (miuiCurrentStateMonitor = mMiuiCurrentStateMonitor) != null) {
                    if (miuiCurrentStateMonitor.getFoldState()) {
                        strConcat = strConcat.concat("_foldState1");
                    } else {
                        strConcat = strConcat.concat("_foldState0");
                    }
                }
                saveFiveGDuration(concurrentHashMap, strConcat, Long.valueOf(rsrpTime / 1000));
            }
        }
        this.mFiveGSignal.setRsrpTime(j);
        this.mFiveGSignal.setRsrpLevel(statsFiveGInfo.getRsrpLevel());
        this.mFiveGSignal.setSnrLevel(statsFiveGInfo.getSnrLevel());
    }

    private void updateFiveGBandDuration(String str, StatsFiveGInfo statsFiveGInfo, long j) {
        if (this.mFiveGSignal.getConfigType() != 1) {
            return;
        }
        int customSimTypeForStatistic = Utils.getCustomSimTypeForStatistic(getDefaultDataSlotId());
        ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mBandStats.get(str);
        if (concurrentHashMap == null) {
            concurrentHashMap = new ConcurrentHashMap();
            this.mBandStats.put(str, concurrentHashMap);
        }
        if (this.mFiveGSignal.getBandTime() == -2) {
            Utils.logD("ModemStats", "ignore 5gband duration!");
        } else {
            long bandTime = j - this.mFiveGSignal.getBandTime();
            if (bandTime >= 1000) {
                Utils.logD("ModemStats", "rsrpLevel = " + this.mFiveGSignal.getRsrpLevel() + ", band " + this.mFiveGSignal.getBand());
                saveFiveGDuration(concurrentHashMap, "Rsrp" + this.mFiveGSignal.getRsrpLevel() + "_N" + this.mFiveGSignal.getBand() + "_simType" + customSimTypeForStatistic, Long.valueOf(bandTime / 1000));
                try {
                    DefaultPreferenceHelper.getInstance().setBandStats(ObjectSerializer.serialize(this.mBandStats));
                    Utils.logD("ModemStats", "band stats = " + this.mBandStats.toString());
                } catch (Exception e) {
                    Utils.logE("ModemStats", "updateFiveGBandDuration exception:" + e);
                }
            }
        }
        this.mFiveGSignal.setBand(statsFiveGInfo.getBand());
        this.mFiveGSignal.setBandTime(j);
    }

    private void updateFiveGArfcnDuration(String str, StatsFiveGInfo statsFiveGInfo, long j) {
        long jLongValue;
        if (this.mFiveGSignal.getConfigType() != 1) {
            return;
        }
        ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mArfcnStats.get(str);
        if (concurrentHashMap == null) {
            concurrentHashMap = new ConcurrentHashMap();
            this.mArfcnStats.put(str, concurrentHashMap);
        }
        if (this.mFiveGSignal.getArfcnTime() == -2) {
            Utils.logD("ModemStats", "ignore 5garfcn duration!");
        } else {
            long arfcnTime = j - this.mFiveGSignal.getArfcnTime();
            if (arfcnTime >= 1000) {
                Utils.logD("ModemStats", "rsrpLevel = " + this.mFiveGSignal.getRsrpLevel() + ", arfcn " + this.mFiveGSignal.getArfcn());
                String str2 = "Rsrp" + this.mFiveGSignal.getRsrpLevel() + "_Arfcn" + this.mFiveGSignal.getArfcn();
                if (concurrentHashMap.containsKey(str2)) {
                    jLongValue = ((Long) concurrentHashMap.get(str2)).longValue();
                    concurrentHashMap.put(str2, Long.valueOf((arfcnTime / 1000) + jLongValue));
                } else {
                    if (this.mArfcnCount < this.MAX_ARFCN_COUNT) {
                        concurrentHashMap.put(str2, Long.valueOf(arfcnTime / 1000));
                        this.mArfcnCount++;
                        Utils.logD("ModemStats", "mArfcnCount = " + this.mArfcnCount);
                    }
                    jLongValue = 0;
                }
                Utils.logD("ModemStats", str2 + "updateFiveGArfcnDuration oldDuration=" + jLongValue + ", duration=" + (arfcnTime / 1000));
                try {
                    if (this.mArfcnCount < this.MAX_ARFCN_COUNT) {
                        DefaultPreferenceHelper.getInstance().setArfcnStats(ObjectSerializer.serialize(this.mArfcnStats));
                        Utils.logD("ModemStats", "arfcn stats = " + this.mArfcnStats.toString());
                    }
                } catch (Exception e) {
                    Utils.logE("ModemStats", "updateFiveGArfcnDuration exception:" + e);
                }
            }
        }
        this.mFiveGSignal.setArfcn(statsFiveGInfo.getArfcn());
        this.mFiveGSignal.setArfcnTime(j);
    }

    private ConcurrentHashMap getFiveGStatByPlmn(String str) {
        if (!TextUtils.isEmpty(this.mFiveGSignal.getOperator())) {
            str = this.mFiveGSignal.getOperator();
        }
        ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mFiveGStats.get(str);
        if (concurrentHashMap != null) {
            return concurrentHashMap;
        }
        ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
        this.mFiveGStats.put(str, concurrentHashMap2);
        return concurrentHashMap2;
    }

    private void storeFiveGStat() {
        try {
            DefaultPreferenceHelper.getInstance().setFiveGStats(ObjectSerializer.serialize(this.mFiveGStats));
            Utils.logD("ModemStats", "fiveG stats = " + new JSONObject(this.mFiveGStats).toString());
        } catch (Exception e) {
            Utils.logE("ModemStats", "setFiveGStats exception:" + e);
        }
    }

    private void saveFiveGDuration(ConcurrentHashMap concurrentHashMap, String str, Long l) {
        Long l2 = concurrentHashMap.containsKey(str) ? (Long) concurrentHashMap.get(str) : 0L;
        try {
            concurrentHashMap.put(str, Long.valueOf(l2.longValue() + l.longValue()));
            Utils.logD("ModemStats", str + ": oldDuration=" + l2 + ", duration=" + l);
        } catch (Exception e) {
            Utils.logE("ModemStats", "fiveGStats exception:" + e);
        }
    }

    private boolean isInvalidBand(String str) {
        return TextUtils.isEmpty(str) || "B-1".equals(str) || "N-1".equals(str);
    }

    public synchronized void updateDualSimNwBands(int i, String str, boolean z) {
        try {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            if (!isInvalidBand(this.mBand[i]) && this.mLastDualBandTime != -1) {
                char c = i == 0 ? (char) 1 : (char) 0;
                if ((!this.mBand[i].equals(str) || z) && !isInvalidBand(this.mBand[c])) {
                    String str2 = this.mBand[0] + "_" + this.mBand[1];
                    long j = (jElapsedRealtime - this.mLastDualBandTime) / 1000;
                    long jLongValue = 0;
                    if (this.mDualSimBands.containsKey(str2) && this.mDualSimBands.get(str2) != null) {
                        jLongValue = ((Long) this.mDualSimBands.get(str2)).longValue();
                    }
                    this.mDualSimBands.put(str2, Long.valueOf(j + jLongValue));
                    Utils.logD("ModemStats", "dual band: " + str2 + ", duration:" + j + ", oldDuration:" + jLongValue);
                    this.mBand[i] = str;
                    this.mLastDualBandTime = jElapsedRealtime;
                    if (z) {
                        try {
                            DefaultPreferenceHelper.getInstance().setStringPreference("event_dual_sim_band", ObjectSerializer.serialize(this.mDualSimBands));
                            Utils.logD("ModemStats", "dual sim band stats = " + this.mDualSimBands.toString());
                        } catch (Exception e) {
                            Utils.logE("ModemStats", "updateDualSimNwBands exception:" + e);
                        }
                    }
                    return;
                }
                return;
            }
            if (!isInvalidBand(str)) {
                this.mBand[i] = str;
                this.mLastDualBandTime = jElapsedRealtime;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void updateNwBands(String str, String str2, long j, boolean z) {
        try {
            String str3 = TextUtils.isEmpty(str) ? "" : str;
            String str4 = str2;
            if (isInvalidBand(str4)) {
                str4 = "-1";
            }
            long j2 = this.mLastBandTime;
            if (j2 != -2 && j - j2 >= 1000) {
                if (TextUtils.equals(str3, this.mLastDdsPlmn) && TextUtils.equals(str4, this.mLastStrBand) && !z) {
                    Utils.logD("ModemStats", "updateNwBands network info not change");
                    return;
                }
                Utils.logD("ModemStats", "updateNwBands newPlmn=" + str3 + ", lastPlmn=" + this.mLastDdsPlmn + ", newBand=" + str4 + ", lastBand=" + this.mLastStrBand + ", curTime=" + j + ", mLastBandTime=" + this.mLastBandTime);
                long j3 = (j - this.mLastBandTime) / 1000;
                int euiccStatus = this.mLastBandEuiccStatus;
                if (euiccStatus == -2) {
                    euiccStatus = getEuiccStatus();
                }
                ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mBandStats.get(this.mLastDdsPlmn);
                if (concurrentHashMap == null) {
                    concurrentHashMap = new ConcurrentHashMap();
                }
                long jLongValue = concurrentHashMap.containsKey(this.mLastStrBand) ? ((Long) concurrentHashMap.get(this.mLastStrBand)).longValue() : 0L;
                long j4 = jLongValue + j3;
                concurrentHashMap.put(this.mLastStrBand, Long.valueOf(j4));
                if (euiccStatus == 1) {
                    String str5 = "eSIM_" + this.mLastStrBand;
                    concurrentHashMap.put(str5, Long.valueOf((concurrentHashMap.containsKey(str5) ? ((Long) concurrentHashMap.get(str5)).longValue() : 0L) + j3));
                }
                this.mBandStats.put(this.mLastDdsPlmn, concurrentHashMap);
                Utils.logD("ModemStats", "updateNwBands oldDuration=" + jLongValue + ",whole=" + j4);
                try {
                    if (this instanceof HikingModemStats) {
                        DefaultPreferenceHelper.getInstance().setHikingBandStats(ObjectSerializer.serialize(this.mBandStats));
                    } else if (this instanceof HstModemStats) {
                        DefaultPreferenceHelper.getInstance().setHstBandStats(ObjectSerializer.serialize(this.mBandStats));
                    } else if (this instanceof SubwayModemStats) {
                        DefaultPreferenceHelper.getInstance().setSubwayBandStats(ObjectSerializer.serialize(this.mBandStats));
                    } else if (this instanceof DriveModemStats) {
                        DefaultPreferenceHelper.getInstance().setDriveBandStats(ObjectSerializer.serialize(this.mBandStats));
                    } else if (this instanceof NsaModemStats) {
                        DefaultPreferenceHelper.getInstance().setNsaBandStats(ObjectSerializer.serialize(this.mBandStats));
                    }
                } catch (Exception e) {
                    Utils.logE("ModemStats", "updateNwBands exception:" + e);
                }
                this.mLastBandTime = j;
                this.mLastBandEuiccStatus = getEuiccStatus();
                updatePlmnBandInfo(str3, str4);
                return;
            }
            updatePlmnBandInfo(str3, str4);
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized void updatePlmnBandInfo(String str, String str2) {
        setDdsPlmn(str);
        setStrBand(str2);
    }

    public synchronized void updateNwArfcn(String str, int i, long j, boolean z) {
        try {
            if (TextUtils.isEmpty(str)) {
                str = "";
            }
            if (!isInvalidArfcn(i)) {
                long j2 = this.mLastArfcnTime;
                if (j2 != -2 && j - j2 >= 1000) {
                    if (TextUtils.equals(str, this.mLastArfcnPlmn) && i == this.mLastArfcn && !z) {
                        Utils.logD("ModemStats", "updateNwArfcn network info not change");
                        return;
                    }
                    Utils.logD("ModemStats", "updateNwArfcn newPlmn=" + str + ", lastPlmn=" + this.mLastArfcnPlmn + ", newArfcn=" + i + ", lastArfcn=" + this.mLastArfcn + ", curTime=" + j + ", mLastArfcnTime=" + this.mLastArfcnTime);
                    long j3 = (j - this.mLastArfcnTime) / 1000;
                    ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mArfcnStats.get(this.mLastArfcnPlmn);
                    if (concurrentHashMap == null) {
                        concurrentHashMap = new ConcurrentHashMap();
                    }
                    long jLongValue = concurrentHashMap.containsKey(Integer.valueOf(this.mLastArfcn)) ? ((Long) concurrentHashMap.get(Integer.valueOf(this.mLastArfcn))).longValue() : 0L;
                    long j4 = j3 + jLongValue;
                    concurrentHashMap.put(String.valueOf(this.mLastArfcn), Long.valueOf(j4));
                    this.mArfcnStats.put(this.mLastArfcnPlmn, concurrentHashMap);
                    Utils.logD("ModemStats", "updateNwArfcn oldDuration=" + jLongValue + ",whole=" + j4);
                    try {
                        if (this instanceof HstModemStats) {
                            DefaultPreferenceHelper.getInstance().setHstArfcnStats(ObjectSerializer.serialize(this.mArfcnStats));
                        }
                    } catch (Exception e) {
                        Utils.logE("ModemStats", "updateNwArfcn exception:" + e);
                    }
                    this.mLastArfcnTime = j;
                    updatePlmnArfcnInfo(str, i);
                    return;
                }
            }
            this.mLastArfcnTime = j;
            updatePlmnArfcnInfo(str, i);
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized void updatePlmnArfcnInfo(String str, int i) {
        setArfcnDdsPlmn(str);
        setArfcn(i);
    }

    public boolean isNwArfcnChanged(String str, int i) {
        return (TextUtils.equals(str, this.mLastArfcnPlmn) && i == this.mLastArfcn) ? false : true;
    }

    private int getEuiccStatus() {
        SubscriptionInfo availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(getDefaultDataSlotId());
        if (availableSubscriptionInfoBySlot != null) {
            return availableSubscriptionInfoBySlot.isEmbedded() ? 1 : 0;
        }
        return 0;
    }

    private int getDefaultDataSlotId() {
        return DefaultSimManager.getDefaultDataSlotId();
    }
}
