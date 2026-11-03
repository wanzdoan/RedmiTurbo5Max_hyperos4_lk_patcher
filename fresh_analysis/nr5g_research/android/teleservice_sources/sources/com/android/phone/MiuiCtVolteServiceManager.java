package com.android.phone;

import android.content.Context;
import android.provider.Settings;
import android.telephony.ServiceState;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.services.telephony.ims.ImsAdapterBase;
import miui.os.Build;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiCtVolteServiceManager {
    public static final int CT_VOLTE_SUPPORTED_MODE = TelephonyManager.getDefault().getCtVolteSupportedMode();
    private static final Context sContext = PhoneGlobals.getInstance();

    public static MiuiCtVolteServiceManager getInstance() {
        return null;
    }

    public static boolean isCdmaVolteNotInService(Phone phone) {
        if (isCtVoLTEOnlySupport(phone) && !MiuiPhoneUtils.isVirtualSim(phone.getPhoneId()) && MiuiPhoneUtils.isIccCardActivated(phone.getPhoneId())) {
            boolean z = MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED;
            if (!z && phone.getSubId() != SubscriptionManager.getDefault().getDefaultDataSubscriptionId()) {
                Log.d("MiuiCtServiceManager", "isCdmaVolteNotInService: DUAL_VOLTE_SUPPORTED:" + z + " phone.getSubId():" + phone.getSubId() + " defaultDataSub:" + SubscriptionManager.getDefault().getDefaultDataSubscriptionId());
                return false;
            }
            Context context = sContext;
            boolean z2 = Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0) == 1;
            ServiceState serviceState = phone.getServiceState();
            if (!z2 && serviceState != null && serviceState.getRilVoiceRadioTechnology() != 6 && !serviceState.getRoaming() && !PhoneProxy.isVolteEnabled(phone) && !MiuiPhoneInterfaceManager.getInstance().isWifiCallingAvailable(phone.getPhoneId()) && MiuiPhoneUtils.isVolteEnabledByPlatform(phone) && ImsAdapterBase.getInstance().getEnhanced4gLteModeSetting(context, phone.getPhoneId())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCtVoLTEOnlySupport(Phone phone) {
        if (MiuiPhoneUtils.isChinaOrMacauTelecomSim(phone)) {
            return MiuiPhoneUtils.isDeviceNotSupportCDMA() || Build.IS_CT_CUSTOMIZATION_TEST || CT_VOLTE_SUPPORTED_MODE == 1;
        }
        Log.d("MiuiCtServiceManager", "isCtVoLTEOnlySupport not ct sim!");
        return false;
    }
}
