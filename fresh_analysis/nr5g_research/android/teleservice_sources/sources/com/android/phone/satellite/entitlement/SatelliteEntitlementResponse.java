package com.android.phone.satellite.entitlement;

import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.satellite.SatelliteNetworkInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteEntitlementResponse {
    private int mEntitlementStatus = 0;
    private List mPlmnAllowedList = new ArrayList();
    private List mPlmnBarredList = new ArrayList();

    public SatelliteEntitlementResponse(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        parsingResponse(str);
    }

    public int getEntitlementStatus() {
        return this.mEntitlementStatus;
    }

    public static /* synthetic */ SatelliteNetworkInfo $r8$lambda$FczxY18Ce3poCyWbU2h_rBJltaE(SatelliteNetworkInfo satelliteNetworkInfo) {
        return new SatelliteNetworkInfo(satelliteNetworkInfo.mPlmn, satelliteNetworkInfo.mDataPlanType, satelliteNetworkInfo.mAllowedServicesInfo);
    }

    public List getPlmnAllowed() {
        return (List) this.mPlmnAllowedList.stream().map(new Function() { // from class: com.android.phone.satellite.entitlement.SatelliteEntitlementResponse$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SatelliteEntitlementResponse.$r8$lambda$FczxY18Ce3poCyWbU2h_rBJltaE((SatelliteNetworkInfo) obj);
            }
        }).collect(Collectors.toList());
    }

    public List getPlmnBarredList() {
        return (List) this.mPlmnBarredList.stream().map(new SatelliteEntitlementResponse$$ExternalSyntheticLambda1()).collect(Collectors.toList());
    }

    private void parsingResponse(String str) {
        String str2;
        HashMap map;
        String str3 = "ServiceType";
        String str4 = "AllowedServicesInfo";
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.has("ap2016")) {
                loge("parsingResponse failed with no app");
                return;
            }
            JSONObject jSONObject2 = jSONObject.getJSONObject("ap2016");
            if (jSONObject2.has("EntitlementStatus")) {
                String string = jSONObject2.getString("EntitlementStatus");
                if (string == null) {
                    loge("parsingResponse EntitlementStatus is null");
                    return;
                }
                this.mEntitlementStatus = Integer.valueOf(string).intValue();
            }
            if (jSONObject2.has("PLMNAllowed")) {
                JSONArray jSONArray = jSONObject2.getJSONArray("PLMNAllowed");
                this.mPlmnAllowedList = new ArrayList();
                int i = 0;
                while (i < jSONArray.length()) {
                    String string2 = jSONArray.getJSONObject(i).has("DataPlanType") ? jSONArray.getJSONObject(i).getString("DataPlanType") : "";
                    if (jSONArray.getJSONObject(i).has(str4)) {
                        map = new HashMap();
                        JSONArray jSONArray2 = jSONArray.getJSONObject(i).getJSONArray(str4);
                        int i2 = 0;
                        while (i2 < jSONArray2.length()) {
                            String str5 = str3;
                            map.put(jSONArray2.getJSONObject(i2).getJSONObject("AllowedServices").has(str3) ? jSONArray2.getJSONObject(i2).getJSONObject("AllowedServices").getString(str3) : "", jSONArray2.getJSONObject(i2).getJSONObject("AllowedServices").has("ServicePolicy") ? jSONArray2.getJSONObject(i2).getJSONObject("AllowedServices").getString("ServicePolicy") : "");
                            i2++;
                            str3 = str5;
                        }
                        str2 = str3;
                    } else {
                        str2 = str3;
                        map = null;
                    }
                    String string3 = jSONArray.getJSONObject(i).getString("PLMN");
                    logd("parsingResponse: plmn=" + string3 + " dataplan=" + string2 + " allowedServices=" + map);
                    if (!TextUtils.isEmpty(string3)) {
                        this.mPlmnAllowedList.add(new SatelliteNetworkInfo(string3, string2, map));
                    }
                    i++;
                    str4 = str4;
                    str3 = str2;
                }
            }
            if (jSONObject2.has("PLMNBarred")) {
                this.mPlmnBarredList = new ArrayList();
                JSONArray jSONArray3 = jSONObject2.getJSONArray("PLMNBarred");
                for (int i3 = 0; i3 < jSONArray3.length(); i3++) {
                    String string4 = jSONArray3.getJSONObject(i3).getString("PLMN");
                    if (!TextUtils.isEmpty(string4)) {
                        this.mPlmnBarredList.add(string4);
                    }
                }
            }
        } catch (NumberFormatException e) {
            loge("parsingResponse: failed NumberFormatException", e);
        } catch (JSONException e2) {
            loge("parsingResponse: failed JSONException", e2);
        }
    }

    private static void logd(String str) {
        Log.d("SatelliteEntitlementResponse", str);
    }

    private static void loge(String str) {
        Log.e("SatelliteEntitlementResponse", str);
    }

    private static void loge(String str, Exception exc) {
        Log.e("SatelliteEntitlementResponse", str, exc);
    }
}
