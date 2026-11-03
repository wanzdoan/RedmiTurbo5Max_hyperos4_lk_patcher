package com.xiaomi.internal.telephony;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class DeviceSarConfig {
    public static final String DEFAULT_STR = "null";
    private String mDeviceName = "null";
    private String mListeners = "null";
    private Map<String, String> mPropList = new HashMap();
    private Map<String, String> mExtendPropList = new HashMap();
    private Map<String, String> mDsiList = new HashMap();
    private String mAntennaNums = "null";
    private String mAntennaGroup = "null";
    private String mDefaultGroup = "null";
    private String mRxDefaultDsi = "null";
    private String mSimMccList = "null";

    DeviceSarConfig() {
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public void setDeviceName(String deviceName) {
        this.mDeviceName = deviceName;
    }

    public String getListeners() {
        return this.mListeners;
    }

    public void setListeners(String listener) {
        this.mListeners = listener;
    }

    public Map<String, String> getPropList() {
        return this.mPropList;
    }

    public void setPropList(String proName, String value) {
        this.mPropList.put(proName, value);
    }

    public Map<String, String> getExtendPropList() {
        return this.mExtendPropList;
    }

    public void setExtendPropList(String propName, String value) {
        this.mExtendPropList.put(propName, value);
    }

    public Map<String, String> getDsiList() {
        return this.mDsiList;
    }

    public void setDsiList(String value, String dsi) {
        this.mDsiList.put(value, dsi);
    }

    public String getAntennaNums() {
        return this.mAntennaNums;
    }

    public void setAntennaNums(String value) {
        this.mAntennaNums = value;
    }

    public String getAntennaGroup() {
        return this.mAntennaGroup;
    }

    public void setAntennaGroup(String value) {
        this.mAntennaGroup = value;
    }

    public String getDefaultGroup() {
        return this.mDefaultGroup;
    }

    public void setDefaultGroup(String value) {
        this.mDefaultGroup = value;
    }

    public String getRxDefaultDsi() {
        return this.mRxDefaultDsi;
    }

    public void setRxDefaultDsi(String value) {
        this.mRxDefaultDsi = value;
    }

    public String getSimMccList() {
        return this.mSimMccList;
    }

    public void setSimMccList(String mcc) {
        this.mSimMccList = mcc;
    }
}
