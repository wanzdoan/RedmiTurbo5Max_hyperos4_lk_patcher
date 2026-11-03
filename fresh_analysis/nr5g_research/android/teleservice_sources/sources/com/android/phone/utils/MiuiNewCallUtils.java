package com.android.phone.utils;

import android.os.UserHandle;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.module.IUtilityModule;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiNewCallUtils {
    public static boolean isNewCallCarrierAvailable() {
        if (UserHandle.myUserId() != 0) {
            return false;
        }
        if (!ModemProvider.IS_QCOM && !ModemProvider.IS_XRING && !ModemProvider.IS_MTK) {
            return false;
        }
        int phoneCount = TelephonyManager.getDefault().getPhoneCount();
        for (int i = 0; i < phoneCount; i++) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null && CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "carrier_new_call_available_bool", false)) {
                Log.d("MiuiNewCallUtils", "new call feature is support for slot " + i);
                return true;
            }
        }
        Log.d("MiuiNewCallUtils", "new call feature is not support");
        return false;
    }

    public static boolean isNewCallFeatureEnabled() {
        return isNewCallCarrierAvailable() || ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).isFiveGNewCallEnableByCloud(DefaultSimManager.getDefaultDataSlotId());
    }

    public static boolean isNewCallSdkInstalled() {
        return Utils.isInstalled(PhoneGlobals.getInstance(), "com.newcall");
    }
}
