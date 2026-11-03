package com.xiaomi.mirror.synergy;

import android.text.TextUtils;
import android.util.Log;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public class PinAppInfo {
    private static final String SPLIT_TAG = "_&mirror&_";
    private static final String TAG = "PinAppInfo";
    private String appName;
    private String deviceId;
    private String iconUri;
    private String packageName;

    public PinAppInfo(String str, String str2, String str3, String str4) {
        this.deviceId = str;
        this.packageName = str2;
        this.appName = str3;
        this.iconUri = str4;
    }

    public static PinAppInfo pareByString(String str) {
        if (TextUtils.isEmpty(str)) {
            Log.e(TAG, "str is empty ");
            return null;
        }
        String[] strArrSplit = str.split(SPLIT_TAG);
        if (strArrSplit.length >= 4 && !TextUtils.isEmpty(strArrSplit[0]) && !TextUtils.isEmpty(strArrSplit[1]) && !TextUtils.isEmpty(strArrSplit[2]) && !TextUtils.isEmpty(strArrSplit[3])) {
            return new PinAppInfo(strArrSplit[0], strArrSplit[1], strArrSplit[2], strArrSplit[3]);
        }
        Log.e(TAG, "error : ".concat(str));
        return null;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            PinAppInfo pinAppInfo = (PinAppInfo) obj;
            if (Objects.equals(this.deviceId, pinAppInfo.deviceId) && Objects.equals(this.packageName, pinAppInfo.packageName) && Objects.equals(this.appName, pinAppInfo.appName) && Objects.equals(this.iconUri, pinAppInfo.iconUri)) {
                return true;
            }
        }
        return false;
    }

    public String getAppName() {
        return this.appName;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getIconUri() {
        return this.iconUri;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getStr() {
        return this.deviceId + SPLIT_TAG + this.packageName + SPLIT_TAG + this.appName + SPLIT_TAG + this.iconUri;
    }

    public int hashCode() {
        return Objects.hash(this.deviceId, this.packageName, this.appName, this.iconUri);
    }

    public void setAppName(String str) {
        this.appName = str;
    }

    public void setDeviceId(String str) {
        this.deviceId = str;
    }

    public void setIconUri(String str) {
        this.iconUri = str;
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }

    public String toString() {
        return "PinAppInfo{deviceId='" + this.deviceId + "', packageName='" + this.packageName + "', title='" + this.appName + "', iconUri='" + this.iconUri + "'}";
    }
}
