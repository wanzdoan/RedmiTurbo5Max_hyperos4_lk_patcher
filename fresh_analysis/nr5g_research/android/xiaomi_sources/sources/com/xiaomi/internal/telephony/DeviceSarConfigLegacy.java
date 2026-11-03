package com.xiaomi.internal.telephony;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class DeviceSarConfigLegacy {
    private String mDeviceName = null;
    private String mListeners = null;
    private Map<String, String> mPropList = new HashMap();
    private Map<String, String> mDsiList = new HashMap();

    DeviceSarConfigLegacy() {
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

    public Map<String, String> getDsiList() {
        return this.mDsiList;
    }

    public void setDsiList(String value, String dsi) {
        this.mDsiList.put(value, dsi);
    }
}
