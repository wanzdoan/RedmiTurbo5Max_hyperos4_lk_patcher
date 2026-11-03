package com.xiaomi.internal.telephony;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class DeviceSarConfigLegacy {
    private String mDeviceName = null;
    private String mListeners = null;
    private Map mPropList = new HashMap();
    private Map mDsiList = new HashMap();

    DeviceSarConfigLegacy() {
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

    public Map getDsiList() {
        return this.mDsiList;
    }

    public void setDsiList(String str, String str2) {
        this.mDsiList.put(str, str2);
    }
}
