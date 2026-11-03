package com.android.internal.telephony.satellite;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteNetworkInfo {
    public Map<String, String> mAllowedServicesInfo;
    public String mDataPlanType;
    public String mPlmn;

    public SatelliteNetworkInfo(String str, String str2, Map<String, String> map) {
        this.mPlmn = str;
        this.mDataPlanType = str2;
        this.mAllowedServicesInfo = map != null ? new HashMap(map) : null;
    }
}
