package com.android.phone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import com.android.ims.ImsManager;
import com.android.internal.telephony.PhoneFactory;

/* JADX INFO: loaded from: classes.dex */
public abstract class ImsUtil {
    private static boolean sImsPhoneSupported = false;

    static {
        PhoneGlobals.getInstance();
        sImsPhoneSupported = true;
    }

    public static boolean isWfcEnabled(Context context, int i) {
        ImsManager imsManager = ImsManager.getInstance(context, i);
        return imsManager.isWfcEnabledByPlatform() && imsManager.isWfcEnabledByUser();
    }

    public static boolean isWfcModeWifiOnly(Context context, int i) {
        return isWfcEnabled(context, i) && (ImsManager.getInstance(context, i).getWfcMode() == 0);
    }

    public static boolean shouldPromoteWfc(Context context, int i) {
        ConnectivityManager connectivityManager;
        NetworkInfo activeNetworkInfo;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService("carrier_config");
        ImsManager imsManager = ImsManager.getInstance(context, i);
        return imsManager.isWfcEnabledByPlatform() && imsManager.isWfcProvisionedOnDevice() && carrierConfigManager != null && carrierConfigManager.getConfigForSubId(getSubId(i)).getBoolean("carrier_promote_wfc_on_call_fail_bool") && (!getLastKnownRoamingState(i) || imsManager.isWfcRoamingEnabledByUser()) && (connectivityManager = (ConnectivityManager) context.getSystemService("connectivity")) != null && (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) != null && activeNetworkInfo.isConnected() && activeNetworkInfo.getType() == 1 && !isWfcEnabled(context, i);
    }

    private static int getSubId(int i) {
        return SubscriptionManager.getSubscriptionId(i);
    }

    private static boolean getLastKnownRoamingState(int i) {
        try {
            return PhoneFactory.getPhone(i).getImsPhone().getLastKnownRoamingState();
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }
}
