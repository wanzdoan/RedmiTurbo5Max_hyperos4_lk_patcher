package com.android.phone.slice;

import android.os.PersistableBundle;
import android.provider.DeviceConfig;
import android.telephony.AnomalyReporter;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.libraries.entitlement.CarrierConfig;
import com.android.libraries.entitlement.ServiceEntitlement;
import com.android.libraries.entitlement.ServiceEntitlementException;
import com.android.libraries.entitlement.ServiceEntitlementRequest;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class PremiumNetworkEntitlementApi {
    private final Phone mPhone;
    private final ServiceEntitlement mServiceEntitlement;

    public PremiumNetworkEntitlementApi(Phone phone, PersistableBundle persistableBundle) {
        this.mPhone = phone;
        if (isBypassEapAkaAuthForSlicePurchaseEnabled()) {
            this.mServiceEntitlement = new ServiceEntitlement(phone.getContext(), getEntitlementServerCarrierConfig(persistableBundle), phone.getSubId(), true, "Default EAP AKA response");
        } else {
            this.mServiceEntitlement = new ServiceEntitlement(phone.getContext(), getEntitlementServerCarrierConfig(persistableBundle), phone.getSubId());
        }
    }

    public PremiumNetworkEntitlementResponse checkEntitlementStatus(int i) {
        String strQueryEntitlementStatus;
        String string;
        Log.d("PremiumNwEntitlementApi", "checkEntitlementStatus subId=" + this.mPhone.getSubId());
        ServiceEntitlementRequest.Builder builder = ServiceEntitlementRequest.builder();
        builder.setTerminalVendor("vendorX");
        builder.setTerminalModel("modelY");
        builder.setTerminalSoftwareVersion("versionZ");
        builder.setAcceptContentType("application/json");
        builder.setBoostType(getBoostTypeFromPremiumCapability(i));
        ServiceEntitlementRequest serviceEntitlementRequestBuild = builder.build();
        PremiumNetworkEntitlementResponse premiumNetworkEntitlementResponse = new PremiumNetworkEntitlementResponse();
        try {
            strQueryEntitlementStatus = this.mServiceEntitlement.queryEntitlementStatus("ap2010", serviceEntitlementRequestBuild);
        } catch (ServiceEntitlementException e) {
            Log.e("PremiumNwEntitlementApi", "queryEntitlementStatus failed", e);
            reportAnomaly("f2b0661a-9114-4b1b-9add-a8d338f9c054", "checkEntitlementStatus failed with ServiceEntitlementException");
            strQueryEntitlementStatus = null;
        }
        if (strQueryEntitlementStatus == null) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(strQueryEntitlementStatus);
            if (jSONObject.has("ap2010")) {
                JSONObject jSONObject2 = jSONObject.getJSONObject("ap2010");
                if (jSONObject2.has("EntitlementStatus")) {
                    String string2 = jSONObject2.getString("EntitlementStatus");
                    if (string2 == null) {
                        return null;
                    }
                    premiumNetworkEntitlementResponse.mEntitlementStatus = Integer.parseInt(string2);
                }
                if (jSONObject2.has("ProvStatus") && (string = jSONObject2.getString("ProvStatus")) != null) {
                    premiumNetworkEntitlementResponse.mProvisionStatus = Integer.parseInt(string);
                }
                if (jSONObject2.has("ServiceFlow_URL")) {
                    premiumNetworkEntitlementResponse.mServiceFlowURL = jSONObject2.getString("ServiceFlow_URL");
                }
                if (jSONObject2.has("ServiceFlow_UserData")) {
                    premiumNetworkEntitlementResponse.mServiceFlowUserData = jSONObject2.getString("ServiceFlow_UserData");
                }
                if (jSONObject2.has("ServiceFlow_ContentsType")) {
                    premiumNetworkEntitlementResponse.mServiceFlowContentsType = jSONObject2.getString("ServiceFlow_ContentsType");
                }
            } else {
                Log.e("PremiumNwEntitlementApi", "queryEntitlementStatus failed with no app");
            }
        } catch (NumberFormatException e2) {
            Log.e("PremiumNwEntitlementApi", "queryEntitlementStatus failed", e2);
            reportAnomaly("f2b0661a-9114-4b1b-9add-a8d338f9c054", "checkEntitlementStatus failed with NumberFormatException");
        } catch (JSONException e3) {
            Log.e("PremiumNwEntitlementApi", "queryEntitlementStatus failed", e3);
            reportAnomaly("f2b0661a-9114-4b1b-9add-a8d338f9c054", "checkEntitlementStatus failed with JSONException");
        }
        Log.d("PremiumNwEntitlementApi", "queryEntitlementStatus succeeded with response: " + premiumNetworkEntitlementResponse);
        return premiumNetworkEntitlementResponse;
    }

    private void reportAnomaly(String str, String str2) {
        AnomalyReporter.reportAnomaly(UUID.fromString(str), str2);
    }

    public static String getEntitlementServerUrl(PersistableBundle persistableBundle) {
        return persistableBundle.getString("imsserviceentitlement.entitlement_server_url_string", "");
    }

    private CarrierConfig getEntitlementServerCarrierConfig(PersistableBundle persistableBundle) {
        return CarrierConfig.builder().setServerUrl(getEntitlementServerUrl(persistableBundle)).build();
    }

    private boolean isBypassEapAkaAuthForSlicePurchaseEnabled() {
        return DeviceConfig.getBoolean("telephony", "bypass_eap_aka_auth_for_slice_purchase_enabled", false);
    }

    private String getBoostTypeFromPremiumCapability(int i) {
        if (i == 34) {
            return "0";
        }
        return "";
    }
}
