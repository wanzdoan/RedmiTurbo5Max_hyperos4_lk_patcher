package com.android.phone.satellite.entitlement;

import com.android.internal.telephony.satellite.SatelliteNetworkInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteEntitlementResult {
    private List mAllowedSatelliteNetworkInfoList;
    private List mBarredPlmnList;
    private int mEntitlementStatus;

    public SatelliteEntitlementResult(int i, List list, List list2) {
        this.mEntitlementStatus = i;
        this.mAllowedSatelliteNetworkInfoList = list;
        this.mBarredPlmnList = list2;
    }

    public int getEntitlementStatus() {
        return this.mEntitlementStatus;
    }

    public List getAllowedPLMNList() {
        return (List) this.mAllowedSatelliteNetworkInfoList.stream().map(new Function() { // from class: com.android.phone.satellite.entitlement.SatelliteEntitlementResult$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((SatelliteNetworkInfo) obj).mPlmn;
            }
        }).collect(Collectors.toList());
    }

    public List getBarredPLMNList() {
        return (List) this.mBarredPlmnList.stream().map(new SatelliteEntitlementResponse$$ExternalSyntheticLambda1()).collect(Collectors.toList());
    }

    public static SatelliteEntitlementResult getDefaultResult() {
        return new SatelliteEntitlementResult(0, new ArrayList(), new ArrayList());
    }

    public Map getDataPlanInfoForPlmnList() {
        HashMap map = new HashMap();
        for (SatelliteNetworkInfo satelliteNetworkInfo : this.mAllowedSatelliteNetworkInfoList) {
            map.put(satelliteNetworkInfo.mPlmn, Integer.valueOf(satelliteNetworkInfo.mDataPlanType.equalsIgnoreCase("unmetered") ? 1 : 0));
        }
        return map;
    }

    public Map getAvailableServiceTypeInfoForPlmnList() {
        HashMap map = new HashMap();
        for (SatelliteNetworkInfo satelliteNetworkInfo : this.mAllowedSatelliteNetworkInfoList) {
            ArrayList arrayList = new ArrayList();
            Map map2 = satelliteNetworkInfo.mAllowedServicesInfo;
            if (map2 != null) {
                for (String str : map2.keySet()) {
                    if (str.equalsIgnoreCase("data")) {
                        arrayList.add(2);
                    } else if (str.equalsIgnoreCase("voice")) {
                        arrayList.add(1);
                    }
                }
                arrayList.add(3);
                map.put(satelliteNetworkInfo.mPlmn, arrayList);
            }
        }
        return map;
    }

    public Map getDataServicePolicyInfoForPlmnList() {
        return getServicePolicyInfoForServiceType("data");
    }

    public Map getVoiceServicePolicyInfoForPlmnList() {
        return getServicePolicyInfoForServiceType("voice");
    }

    public Map getServicePolicyInfoForServiceType(String str) {
        HashMap map = new HashMap();
        for (SatelliteNetworkInfo satelliteNetworkInfo : this.mAllowedSatelliteNetworkInfoList) {
            Map map2 = satelliteNetworkInfo.mAllowedServicesInfo;
            if (map2 != null) {
                for (String str2 : map2.keySet()) {
                    if (str2.equalsIgnoreCase(str)) {
                        String str3 = (String) satelliteNetworkInfo.mAllowedServicesInfo.get(str2);
                        if (str3.equalsIgnoreCase("constrained")) {
                            map.put(satelliteNetworkInfo.mPlmn, 1);
                        } else if (str3.equalsIgnoreCase("unconstrained")) {
                            map.put(satelliteNetworkInfo.mPlmn, 2);
                        }
                    }
                }
            }
        }
        return map;
    }
}
