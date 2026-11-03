package com.android.phone;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.AccessNetworkUtils;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.phone.module.ITelephonyModule;
import java.util.List;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public abstract class PhoneProxy {
    public static boolean isBandForEarfcnEnabled() {
        return true;
    }

    public static CommandsInterface getCommandsInterface(Phone phone) {
        if (phone != null) {
            return phone.mCi;
        }
        return null;
    }

    public static ServiceStateTracker getServiceStateTracker(Phone phone) {
        if (phone != null) {
            return phone.getServiceStateTracker();
        }
        return null;
    }

    public static String getSimOperator(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null) {
            return null;
        }
        return phone.getOperatorNumeric();
    }

    public static boolean isVolteEnabled(Phone phone) {
        return phone != null && phone.isVolteEnabled();
    }

    public static void getCallBarringOverIms(ImsPhone imsPhone, String str, String str2, Message message, int i) {
        imsPhone.getCallBarring(str, str2, message, i);
    }

    public static void setCallBarringOverIms(ImsPhone imsPhone, String str, boolean z, String str2, Message message, int i) {
        imsPhone.setCallBarring(str, z, str2, message, i);
    }

    public static void selectNetworkManually(OperatorInfo operatorInfo, Message message, Phone phone) {
        phone.selectNetworkManually(operatorInfo, true, message);
    }

    public static int getVideoCFServiceClass() {
        return ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getVideoCFServiceClass();
    }

    public static void getCallForwardingOption(Phone phone, int i, int i2, Message message) {
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getCallForwardingOption(phone, i, i2, message);
    }

    public static void setCallForwardingOption(Phone phone, int i, int i2, String str, int i3, int i4, Message message) {
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setCallForwardingOption(phone, i, i2, str, i3, i4, message);
    }

    public static void updateConfigForPhoneId(Context context, int i, String str) {
        if (PhoneGlobals.getInstance().configLoader != null) {
            PhoneGlobals.getInstance().configLoader.updateConfigForPhoneId(i, str);
        }
    }

    public static void setVolteCarrierConfigValue(PersistableBundle persistableBundle, boolean z) {
        persistableBundle.putBoolean("carrier_volte_available_bool", z);
        persistableBundle.putBoolean("carrier_vt_available_bool", z);
    }

    public static boolean getVolteCarrierConfigValue(PersistableBundle persistableBundle) {
        return persistableBundle.getBoolean("carrier_volte_available_bool");
    }

    public static void setCarrierConfigValue(PersistableBundle persistableBundle, String str, boolean z) {
        persistableBundle.putBoolean(str, z);
    }

    public static int getOperatingBandForEarfcn(ServiceState serviceState) {
        if (serviceState != null) {
            return AccessNetworkUtils.getOperatingBandForEarfcn(serviceState.getChannelNumber());
        }
        return -2;
    }

    public static int getOperatingBandForEarfcn(int i) {
        return AccessNetworkUtils.getOperatingBandForEarfcn(i);
    }

    public static boolean isChannelNumberValid(ServiceState serviceState) {
        return (serviceState == null || serviceState.getChannelNumber() == -1) ? false : true;
    }

    public static int getLteEarfcn(int i) {
        List<CellInfo> allCellInfoForSlot = TelephonyManagerEx.getDefault().getAllCellInfoForSlot(i);
        if (allCellInfoForSlot == null) {
            return -1;
        }
        for (CellInfo cellInfo : allCellInfoForSlot) {
            if (cellInfo instanceof CellInfoLte) {
                return ((CellInfoLte) cellInfo).getCellIdentity().getEarfcn();
            }
        }
        return -1;
    }

    public static int getCsRejectCause(ServiceState serviceState) {
        NetworkRegistrationInfo networkRegistrationInfo;
        if (serviceState == null || (networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(1, 1)) == null) {
            return -1;
        }
        return networkRegistrationInfo.getRejectCause();
    }

    public static String getOpconfigVersion() {
        try {
            return Long.toString(PhoneGlobals.getInstance().getPackageManager().getPackageInfo("com.miui.opconfig", 1073741824).getLongVersionCode());
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public static String getOpconfigName() {
        try {
            return PhoneGlobals.getInstance().getPackageManager().getPackageInfo("com.miui.opconfig", 1073741824).versionName;
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }
}
