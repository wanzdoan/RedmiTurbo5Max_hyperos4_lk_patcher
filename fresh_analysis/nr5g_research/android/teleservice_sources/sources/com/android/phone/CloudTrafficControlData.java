package com.android.phone;

import android.provider.MiuiSettings;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class CloudTrafficControlData {
    private static String LOG_TAG = "CloudTrafficControlData";
    private String mCustomizedRegion;
    private String mModel;
    private String mSimOperator;
    private String mTcEnabledList;
    private String mTcWhiteList;
    private String mVersionType;
    private static List mTcWhiteListData = new ArrayList();
    private static List mTcEnabledListData = new ArrayList();

    public CloudTrafficControlData(MiuiSettings.SettingsCloudData.CloudData cloudData) {
        this.mModel = cloudData.getString("Model", "NA");
        this.mVersionType = cloudData.getString("DeviceVersionType", "NA");
        this.mCustomizedRegion = cloudData.getString("CustomizedRegion", "NA");
        this.mSimOperator = cloudData.getString("SimOperator", "NA");
        this.mTcWhiteList = cloudData.getString("TrafficControlWhiteList", "NA");
        this.mTcEnabledList = cloudData.getString("TrafficControlEnabledList", "NA");
        if (isBasicInfoMatched()) {
            initTcData();
        }
    }

    private void initTcData() {
        initTcList(this.mTcWhiteList, "mobile_tc_white_list_pkg_name");
        initTcList(this.mTcEnabledList, "mobile_tc_enabled_list_pkg_name");
    }

    private void initTcList(String str, String str2) {
        String[] strArrSplit;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            String strOptString = new JSONObject(str).optString("PackageName");
            if (!TextUtils.isEmpty(strOptString) && (strArrSplit = strOptString.split(",")) != null && strArrSplit.length != 0) {
                HashSet hashSet = new HashSet();
                for (String str3 : strArrSplit) {
                    log("initTcList pkgName =" + str3);
                    if (!"NA".equalsIgnoreCase(str3)) {
                        hashSet.add(str3);
                    }
                }
                FiveGCloudUtils.saveListToDb(hashSet, str2);
            }
        } catch (Exception e) {
            log("initTcList Exception" + e);
        }
    }

    private boolean isBasicInfoMatched() {
        return FiveGCloudUtils.modelMatched(this.mModel) && FiveGCloudUtils.versionTypeMatched(this.mVersionType) && FiveGCloudUtils.customizedRegionMatched(this.mCustomizedRegion) && FiveGCloudUtils.simOperatorMatched(this.mSimOperator);
    }

    private static void log(String str) {
        Log.d(LOG_TAG, str);
    }
}
