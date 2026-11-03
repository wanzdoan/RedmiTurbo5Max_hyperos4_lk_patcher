package com.xiaomi.internal.telephony;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class DeviceSarConfig {
    public static final String DEFAULT_STR = "null";
    private String mDeviceName = "null";
    private String mListeners = "null";
    private Map mPropList = new HashMap();
    private Map mExtendPropList = new HashMap();
    private Map mDsiList = new HashMap();
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

    public void setDeviceName(String str) {
        this.mDeviceName = str;
    }

    public String getListeners() {
        return this.mListeners;
    }

    public void setListeners(String str) {
        this.mListeners = str;
    }

    public Map getPropList() {
        return this.mPropList;
    }

    public void setPropList(String str, String str2) {
        this.mPropList.put(str, str2);
    }

    public Map getExtendPropList() {
        return this.mExtendPropList;
    }

    public void setExtendPropList(String str, String str2) {
        this.mExtendPropList.put(str, str2);
    }

    public Map getDsiList() {
        return this.mDsiList;
    }

    public void setDsiList(String str, String str2) {
        this.mDsiList.put(str, str2);
    }

    public String getAntennaNums() {
        return this.mAntennaNums;
    }

    public void setAntennaNums(String str) {
        this.mAntennaNums = str;
    }

    public String getAntennaGroup() {
        return this.mAntennaGroup;
    }

    public void setAntennaGroup(String str) {
        this.mAntennaGroup = str;
    }

    public String getDefaultGroup() {
        return this.mDefaultGroup;
    }

    public void setDefaultGroup(String str) {
        this.mDefaultGroup = str;
    }

    public String getRxDefaultDsi() {
        return this.mRxDefaultDsi;
    }

    public void setRxDefaultDsi(String str) {
        this.mRxDefaultDsi = str;
    }

    public String getSimMccList() {
        return this.mSimMccList;
    }

    public void setSimMccList(String str) {
        this.mSimMccList = str;
    }
}
