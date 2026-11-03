package com.android.phone;

import android.provider.MiuiSettings;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import miuix.animation.internal.AnimTask;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class FiveGCloudSmartSwitchData {
    private static String LOG_TAG = "FiveGCloudSmartSwitchData";
    private String mCustomizedRegion;
    private String mLowBattery;
    private LowBatteryData mLowBatteryData;
    private String mModel;
    private String mSignalQos;
    private SignalQosData mSignalQosData;
    private String mSimOperator;
    private String mSpeedInterval;
    private String mSpeedOnly;
    private SpeedOnlyData mSpeedOnlyData;
    private String mSpeedWhiteList;
    private List mSpeedWhiteListData = new ArrayList();
    private String mVersionType;

    public FiveGCloudSmartSwitchData(MiuiSettings.SettingsCloudData.CloudData cloudData) {
        this.mModel = cloudData.getString("Model", "NA");
        this.mVersionType = cloudData.getString("DeviceVersionType", "NA");
        this.mCustomizedRegion = cloudData.getString("CustomizedRegion", "NA");
        this.mSimOperator = cloudData.getString("SimOperator", "NA");
        this.mSpeedInterval = cloudData.getString("SpeedInterval", "NA");
        this.mSignalQos = cloudData.getString("SignalQos", "NA");
        this.mLowBattery = cloudData.getString("LowBattery", "NA");
        this.mSpeedWhiteList = cloudData.getString("SpeedAppWhiteList", "NA");
        this.mSpeedOnly = cloudData.getString("SpeedOnly", "NA");
        if (isMatched()) {
            initObjectData();
        }
    }

    public void initObjectData() {
        this.mSignalQosData = initSignalQos(this.mSignalQos);
        this.mLowBatteryData = initLowBattery(this.mLowBattery);
        this.mSpeedWhiteListData = initSpeedWhiteList(this.mSpeedWhiteList);
        this.mSpeedOnlyData = initSpeedOnly(this.mSpeedOnly);
    }

    private SignalQosData initSignalQos(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            return new SignalQosData(this, jSONObject.optString("WeakSignalBin"), jSONObject.optString("MaxVgSignalCn"), jSONObject.optString("MaxSentPacketsCn"), jSONObject.optString("NrUplinkThreshold"), jSONObject.optString("NrDownlinkThreshold"), jSONObject.optString("LteUplinkThreshold"), jSONObject.optString("LteDownlinkThreshold"));
        } catch (Exception e) {
            log("initSignalQos Exception" + e);
            return null;
        }
    }

    private LowBatteryData initLowBattery(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            return new LowBatteryData(this, jSONObject.optString("LowBatteryLevel"), jSONObject.optString("HighSpeedThreshold"), jSONObject.optString("LowSpeedThreshold"));
        } catch (Exception e) {
            log("initLowBattery Exception" + e);
            return null;
        }
    }

    private List initSpeedWhiteList(String str) {
        String[] strArrSplit;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            JSONArray jSONArray = new JSONArray(str);
            ArrayList arrayList = new ArrayList();
            HashSet hashSet = new HashSet();
            int i = 0;
            while (i < jSONArray.length()) {
                JSONObject jSONObjectOptJSONObject = jSONArray.optJSONObject(i);
                if (jSONObjectOptJSONObject != null) {
                    String strOptString = jSONObjectOptJSONObject.optString("Catogary");
                    String strOptString2 = jSONObjectOptJSONObject.optString("HighSpeedThreshold");
                    String strOptString3 = jSONObjectOptJSONObject.optString("LowSpeedThreshold");
                    String strOptString4 = jSONObjectOptJSONObject.optString("PackageName");
                    if (!TextUtils.isEmpty(strOptString4) && (strArrSplit = strOptString4.split(",")) != null && strArrSplit.length != 0) {
                        int length = strArrSplit.length;
                        int i2 = 0;
                        while (i2 < length) {
                            String str2 = strArrSplit[i2];
                            FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData = this;
                            arrayList.add(new SpeedWhiteListData(fiveGCloudSmartSwitchData, strOptString, str2, strOptString2, strOptString3));
                            log("initSpeedWhiteList pkgName =" + str2);
                            if (!"NA".equalsIgnoreCase(str2)) {
                                hashSet.add(str2);
                            }
                            i2++;
                            this = fiveGCloudSmartSwitchData;
                        }
                    }
                }
                i++;
                this = this;
            }
            FiveGCloudUtils.saveListToDb(hashSet, "fiveg_speed_white_list_pkg_name");
            return arrayList;
        } catch (Exception e) {
            log("initSpeedWhiteList Exception" + e);
            return null;
        }
    }

    public boolean isMatched() {
        return FiveGCloudUtils.modelMatched(this.mModel) && FiveGCloudUtils.versionTypeMatched(this.mVersionType) && FiveGCloudUtils.customizedRegionMatched(this.mCustomizedRegion) && FiveGCloudUtils.simOperatorMatched(this.mSimOperator);
    }

    public int getSpeedInterval() {
        int i;
        try {
            return (!isMatched() || (i = Integer.parseInt(this.mSpeedInterval)) <= 0) ? AnimTask.MAX_MAIN_THREAD_TASK_SIZE : i;
        } catch (Exception e) {
            log("getSpeedInterval Exception" + e);
            return AnimTask.MAX_MAIN_THREAD_TASK_SIZE;
        }
    }

    public int getSignalQosWeakSignalBin() {
        SignalQosData signalQosData;
        if (!isMatched() || (signalQosData = this.mSignalQosData) == null) {
            return 4;
        }
        return signalQosData.getWeakSignalBin();
    }

    public int getSignalQosVgSignalCn() {
        SignalQosData signalQosData;
        if (!isMatched() || (signalQosData = this.mSignalQosData) == null) {
            return 15;
        }
        return signalQosData.getMaxVgSignalCn();
    }

    public int getSignalQosSentPacketsCn() {
        SignalQosData signalQosData;
        if (!isMatched() || (signalQosData = this.mSignalQosData) == null) {
            return 10;
        }
        return signalQosData.getMaxSentPacketsCn();
    }

    public int getSignalQosNrUplink() {
        SignalQosData signalQosData;
        if (!isMatched() || (signalQosData = this.mSignalQosData) == null) {
            return 2621440;
        }
        return signalQosData.getNrUplinkThreshold();
    }

    public int getSignalQosNrDownlink() {
        SignalQosData signalQosData;
        if (!isMatched() || (signalQosData = this.mSignalQosData) == null) {
            return 2621440;
        }
        return signalQosData.getNrDownlinkThreshold();
    }

    public int getSignalQosLteUplink() {
        SignalQosData signalQosData;
        if (!isMatched() || (signalQosData = this.mSignalQosData) == null) {
            return 131072;
        }
        return signalQosData.getLteUplinkThreshold();
    }

    public int getSignalQosLteDownlink() {
        SignalQosData signalQosData;
        if (!isMatched() || (signalQosData = this.mSignalQosData) == null) {
            return 131072;
        }
        return signalQosData.getLteDownlinkThreshold();
    }

    public int getLowBatteryLevel() {
        LowBatteryData lowBatteryData;
        if (!isMatched() || (lowBatteryData = this.mLowBatteryData) == null) {
            return 30;
        }
        return lowBatteryData.getLowBatteryLevel();
    }

    public int getLowBatteryHighSpeed() {
        LowBatteryData lowBatteryData;
        if (!isMatched() || (lowBatteryData = this.mLowBatteryData) == null) {
            return 655360;
        }
        return lowBatteryData.getHighSpeedThreshold();
    }

    public int getLowBatteryLowSpeed() {
        LowBatteryData lowBatteryData;
        if (!isMatched() || (lowBatteryData = this.mLowBatteryData) == null) {
            return 131072;
        }
        return lowBatteryData.getLowSpeedThreshold();
    }

    public int getSpeedWhitelistHighSpeed(String str) {
        List list;
        if (!isMatched() || (list = this.mSpeedWhiteListData) == null || list.size() <= 0 || TextUtils.isEmpty(str)) {
            return 655360;
        }
        for (SpeedWhiteListData speedWhiteListData : this.mSpeedWhiteListData) {
            if (str.equals(speedWhiteListData.getPackegeName())) {
                return speedWhiteListData.getHighSpeedThreshold();
            }
        }
        return 655360;
    }

    public int getSpeedWhitelistLowSpeed(String str) {
        List list;
        if (!isMatched() || (list = this.mSpeedWhiteListData) == null || list.size() <= 0 || TextUtils.isEmpty(str)) {
            return 131072;
        }
        for (SpeedWhiteListData speedWhiteListData : this.mSpeedWhiteListData) {
            if (str.equals(speedWhiteListData.getPackegeName())) {
                return speedWhiteListData.getLowSpeedThreshold();
            }
        }
        return 131072;
    }

    private SpeedOnlyData initSpeedOnly(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            return new SpeedOnlyData(this, jSONObject.optString("SpeedOnlyStatus"), jSONObject.optString("HighSpeedThreshold"), jSONObject.optString("LowSpeedThreshold"));
        } catch (Exception e) {
            log("initSpeedOnly Exception" + e);
            return null;
        }
    }

    public int getSpeedOnlyStatus() {
        SpeedOnlyData speedOnlyData;
        if (!isMatched() || (speedOnlyData = this.mSpeedOnlyData) == null) {
            return 1;
        }
        return speedOnlyData.getSpeedOnlyStatus();
    }

    public int getSpeedOnlyHighSpeed() {
        SpeedOnlyData speedOnlyData;
        if (!isMatched() || (speedOnlyData = this.mSpeedOnlyData) == null) {
            return 655360;
        }
        return speedOnlyData.getHighSpeedThreshold();
    }

    public int getSpeedOnlyLowSpeed() {
        SpeedOnlyData speedOnlyData;
        if (!isMatched() || (speedOnlyData = this.mSpeedOnlyData) == null) {
            return 131072;
        }
        return speedOnlyData.getLowSpeedThreshold();
    }

    public String toString() {
        return "FiveGCloudData[" + this.mModel + ", " + this.mVersionType + ", " + this.mCustomizedRegion + ", " + this.mSimOperator + ", " + this.mSpeedInterval + ", " + this.mSignalQosData + ", " + this.mLowBatteryData + ", " + this.mSpeedWhiteListData + ", " + this.mSpeedOnlyData + ", ]";
    }

    class SignalQosData {
        private String lteDownlinkThreshold;
        private String lteUplinkThreshold;
        private String maxSentPacketsCn;
        private String maxVgSignalCn;
        private String nrDownlinkThreshold;
        private String nrUplinkThreshold;
        private String weakSignalBin;

        public SignalQosData(FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData, String str, String str2, String str3, String str4, String str5, String str6, String str7) {
            this.weakSignalBin = str;
            this.maxVgSignalCn = str2;
            this.maxSentPacketsCn = str3;
            this.nrUplinkThreshold = str4;
            this.nrDownlinkThreshold = str5;
            this.lteUplinkThreshold = str6;
            this.lteDownlinkThreshold = str7;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getWeakSignalBin() {
            try {
                int i = Integer.parseInt(this.weakSignalBin);
                if (i > 0) {
                    return i;
                }
                return 4;
            } catch (Exception e) {
                FiveGCloudSmartSwitchData.log("getWeakSignalBin Exception" + e);
                return 4;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getMaxVgSignalCn() {
            try {
                int i = Integer.parseInt(this.maxVgSignalCn);
                if (i > 0) {
                    return i;
                }
                return 15;
            } catch (Exception e) {
                FiveGCloudSmartSwitchData.log("getMaxVgSignalCn Exception" + e);
                return 15;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getMaxSentPacketsCn() {
            try {
                int i = Integer.parseInt(this.maxSentPacketsCn);
                if (i > 0) {
                    return i;
                }
                return 10;
            } catch (Exception e) {
                FiveGCloudSmartSwitchData.log("getMaxSentPacketsCn Exception" + e);
                return 10;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getNrUplinkThreshold() {
            try {
                int i = Integer.parseInt(this.nrUplinkThreshold);
                if (i > 0) {
                    return i;
                }
                return 2621440;
            } catch (Exception e) {
                FiveGCloudSmartSwitchData.log("getNrUplinkThreshold Exception" + e);
                return 2621440;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getNrDownlinkThreshold() {
            try {
                int i = Integer.parseInt(this.nrDownlinkThreshold);
                if (i > 0) {
                    return i;
                }
                return 2621440;
            } catch (Exception e) {
                FiveGCloudSmartSwitchData.log("getNrDownlinkThreshold Exception" + e);
                return 2621440;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getLteUplinkThreshold() {
            try {
                int i = Integer.parseInt(this.lteUplinkThreshold);
                if (i > 0) {
                    return i;
                }
                return 131072;
            } catch (Exception e) {
                FiveGCloudSmartSwitchData.log("getLteUplinkThreshold Exception" + e);
                return 131072;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getLteDownlinkThreshold() {
            try {
                int i = Integer.parseInt(this.lteDownlinkThreshold);
                if (i > 0) {
                    return i;
                }
                return 131072;
            } catch (Exception e) {
                FiveGCloudSmartSwitchData.log("getLteDownlinkThreshold Exception" + e);
                return 131072;
            }
        }

        public String toString() {
            return "SignalQosData[" + this.weakSignalBin + ", " + this.maxVgSignalCn + ", " + this.maxSentPacketsCn + ", " + this.nrUplinkThreshold + ", " + this.nrDownlinkThreshold + ", " + this.lteUplinkThreshold + ", " + this.lteDownlinkThreshold + ", ]";
        }
    }

    class LowBatteryData {
        private String highSpeedThreshold;
        private String lowBatteryLevel;
        private String lowSpeedThreshold;

        public LowBatteryData(FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData, String str, String str2, String str3) {
            this.lowBatteryLevel = str;
            this.highSpeedThreshold = str2;
            this.lowSpeedThreshold = str3;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getLowBatteryLevel() {
            try {
                int i = Integer.parseInt(this.lowBatteryLevel);
                if (i > 0) {
                    return i;
                }
                return 30;
            } catch (Exception unused) {
                return 30;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getHighSpeedThreshold() {
            try {
                int i = Integer.parseInt(this.highSpeedThreshold);
                if (i > 0) {
                    return i;
                }
                return 655360;
            } catch (Exception unused) {
                return 655360;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getLowSpeedThreshold() {
            try {
                int i = Integer.parseInt(this.lowSpeedThreshold);
                if (i > 0) {
                    return i;
                }
                return 131072;
            } catch (Exception unused) {
                return 131072;
            }
        }

        public String toString() {
            return "LowBatteryData[" + this.lowBatteryLevel + ", " + this.highSpeedThreshold + ", " + this.lowSpeedThreshold + ", ]";
        }
    }

    class SpeedWhiteListData {
        private String category;
        private String highSpeedThreshold;
        private String lowSpeedThreshold;
        private String pkgName;

        public SpeedWhiteListData(FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData, String str, String str2, String str3, String str4) {
            this.category = str;
            this.pkgName = str2;
            this.highSpeedThreshold = str3;
            this.lowSpeedThreshold = str4;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getPackegeName() {
            return this.pkgName;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getHighSpeedThreshold() {
            try {
                int i = Integer.parseInt(this.highSpeedThreshold);
                if (i > 0) {
                    return i;
                }
                return 655360;
            } catch (Exception unused) {
                return 655360;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getLowSpeedThreshold() {
            try {
                int i = Integer.parseInt(this.lowSpeedThreshold);
                if (i > 0) {
                    return i;
                }
                return 131072;
            } catch (Exception unused) {
                return 131072;
            }
        }

        public String toString() {
            return "SpeedWhiteListData[" + this.category + ", " + this.pkgName + ", " + this.highSpeedThreshold + ", " + this.lowSpeedThreshold + ", ]";
        }
    }

    class SpeedOnlyData {
        private String enableStatus;
        private String highSpeedThreshold;
        private String lowSpeedThreshold;

        public SpeedOnlyData(FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData, String str, String str2, String str3) {
            this.enableStatus = str;
            this.highSpeedThreshold = str2;
            this.lowSpeedThreshold = str3;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getSpeedOnlyStatus() {
            try {
                int i = Integer.parseInt(this.enableStatus);
                if (i >= 0) {
                    return i;
                }
                return 1;
            } catch (Exception unused) {
                return 1;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getHighSpeedThreshold() {
            try {
                int i = Integer.parseInt(this.highSpeedThreshold);
                if (i > 0) {
                    return i;
                }
                return 655360;
            } catch (Exception unused) {
                return 655360;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getLowSpeedThreshold() {
            try {
                int i = Integer.parseInt(this.lowSpeedThreshold);
                if (i > 0) {
                    return i;
                }
                return 131072;
            } catch (Exception unused) {
                return 131072;
            }
        }

        public String toString() {
            return "SpeedOnlyData[" + this.enableStatus + ", " + this.highSpeedThreshold + ", " + this.lowSpeedThreshold + ", ]";
        }
    }

    protected static void log(String str) {
        Log.d(LOG_TAG, str);
    }
}
