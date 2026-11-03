package com.android.internal.telephony.uicc;

import android.text.TextUtils;
import com.android.telephony.Rlog;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiUiccCommonUtils {
    private static final String LOG_TAG = "MiuiUiccCommonUtils";

    public static String overrideSpnFromConfig(int phoneId, String carrierName, IccRecords records) {
        if (TelephonyManager.isCustFeatureResolveEnabled()) {
            String spnName = HyperOSCustFeatureResolve.getStringForPhoneId(phoneId, "spn_override_name", "");
            if (TextUtils.isEmpty(spnName)) {
                return carrierName;
            }
            Rlog.d(LOG_TAG, "overrideSpnFromConfig, spn_override_name = " + spnName);
            records.setServiceProviderName(spnName);
            return spnName;
        }
        if (records == null) {
            return carrierName;
        }
        String numeric = records.getOperatorNumeric();
        SpnOverride spnOverride = SpnOverride.getInstance();
        if (spnOverride == null || !spnOverride.containsCarrier(numeric)) {
            return carrierName;
        }
        String newCarrierName = spnOverride.getSpn(numeric);
        Rlog.d(LOG_TAG, "overrideSpnFromConfig, overrideSpnFromConfig = " + newCarrierName);
        records.setServiceProviderName(newCarrierName);
        return newCarrierName;
    }

    public static void setSpnFromEons(String carrierName, IccRecords records) {
        if (records != null) {
            Rlog.d(LOG_TAG, "setSpnFromEons = " + carrierName);
            records.setServiceProviderName(carrierName);
        }
    }
}
