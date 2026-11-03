package com.android.phone;

import android.telephony.Rlog;
import com.android.internal.telephony.Phone;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.module.IUtilityModule;
import com.android.phone.networkmode.NetworkMode;
import com.android.phone.networkmode.NetworkModeHandlerAllSlot;
import com.android.phone.networkmode.NetworkModeHandlerBase;
import com.android.phone.networkmode.NetworkModeHandlerDataSlot;
import com.android.phone.networkmode.NetworkModeHandlerMtkDual4G;
import com.android.phone.utils.FiveGUtils;
import com.ot.pubsub.c.d$$ExternalSyntheticBUOutline0;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import miui.enterprise.EnterpriseManagerStub;
import miui.enterprise.PhoneHelperStub;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class NetworkModeManager {
    private static NetworkModeManager sInstance;

    public static NetworkModeManager make() {
        if (sInstance == null) {
            NetworkModeManager networkModeManager = new NetworkModeManager();
            sInstance = networkModeManager;
            return networkModeManager;
        }
        d$$ExternalSyntheticBUOutline0.m("DefaultCardManager.make() should only be called once");
        return null;
    }

    public static NetworkModeManager getInstance() {
        return sInstance;
    }

    private NetworkModeManager() {
        boolean z = ModemProvider.IS_MTK;
        if ((z || ModemProvider.IS_XRING || ModemProvider.IS_SPRD) && MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED) {
            new NetworkModeHandlerMtkDual4G();
            return;
        }
        if (ModemProvider.IS_QCOM) {
            new NetworkModeHandlerAllSlot();
        } else if (z || ModemProvider.IS_XRING || ModemProvider.IS_SPRD) {
            new NetworkModeHandlerDataSlot();
        } else {
            new NetworkModeHandlerBase();
        }
    }

    public static int getPhoneType(int i) {
        return ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getPhoneType(i);
    }

    public static boolean isRemoveNetworkModeSettings(int i) {
        if ((EnterpriseManagerStub.ENTERPRISE_ACTIVATED && PhoneHelperStub.getInstance().isNetworkModesRestriction()) || isRemoveNetworkModeForceNr(MiuiPhoneUtils.getPhone(i)) || MiuiPhoneUtils.hideNetmodeMenuAndUseDefaultRat(i)) {
            return true;
        }
        if (MiuiPhoneUtils.isFiveGCapable()) {
            return false;
        }
        return Build.IS_CM_CUSTOMIZATION || Build.IS_CM_COOPERATION || (Build.IS_CT_CUSTOMIZATION && TelephonyManager.getDefault().isCmccCooperationDevice());
    }

    public static boolean isRemoveNetworkModeSettings(Phone phone) {
        if (EnterpriseManagerStub.ENTERPRISE_ACTIVATED && PhoneHelperStub.getInstance().isNetworkModesRestriction()) {
            return true;
        }
        int phoneId = phone != null ? phone.getPhoneId() : SubscriptionManager.INVALID_PHONE_ID;
        boolean z = !TelephonyManager.isCustFeatureResolveEnabled() ? !(MiuiPhoneUtils.isOnlySupportNrForSlot(phoneId) || MiuiPhoneUtils.isPEMovistarSim(phone) || MiuiPhoneUtils.isTrVersion() || MiuiPhoneUtils.hideNetmodeMenuAndUseDefaultRat(phone) || (!MiuiPhoneUtils.isFiveGCapable() && isNetworkModeLteOnly(phoneId))) : !(HyperOSCustFeatureResolve.getBooleanForPhoneId(phoneId, "remove_network_mode_all_device", false) || (!MiuiPhoneUtils.isFiveGCapable() && HyperOSCustFeatureResolve.getBooleanForPhoneId(phoneId, "remove_network_mode_non_fiveg_device", false)));
        if (isRemoveNetworkModeForceNr(phone) || z) {
            return true;
        }
        if (MiuiPhoneUtils.isFiveGCapable()) {
            return false;
        }
        return Build.IS_CM_CUSTOMIZATION || Build.IS_CM_COOPERATION || (Build.IS_CT_CUSTOMIZATION && TelephonyManager.getDefault().isCmccCooperationDevice() && MiuiPhoneUtils.isCmccSim(phone));
    }

    private static boolean isRemoveNetworkModeForceNr(Phone phone) {
        if (phone != null && !FiveGUtils.isFiveGNetworkForceVisable()) {
            int phoneId = phone.getPhoneId();
            boolean zIsPreferredNetworkTypeInVisibleByCloud = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).isPreferredNetworkTypeInVisibleByCloud(phoneId);
            boolean zIsPreferredNetworkTypeVisibleByCloud = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).isPreferredNetworkTypeVisibleByCloud(phoneId);
            Rlog.d("isRemoveNetworkModeForceNr", "phoneId = " + phoneId + ", isInVisibleByCloud = " + zIsPreferredNetworkTypeInVisibleByCloud + ", isVisibleByCloud = " + zIsPreferredNetworkTypeVisibleByCloud);
            int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
            if (FiveGManagerBase.getInstance() != null && ((phoneId == defaultDataSlotId && FiveGManagerBase.isFiveGCapable()) || (phoneId != defaultDataSlotId && TelephonyManager.getDefault().isDualNrSupported()))) {
                if (zIsPreferredNetworkTypeInVisibleByCloud) {
                    return true;
                }
                if (zIsPreferredNetworkTypeVisibleByCloud) {
                    return false;
                }
                if (FiveGUtils.isForcedRecoveryNr()) {
                    boolean zIsCmccSim = MiuiPhoneUtils.isCmccSim(phone);
                    Rlog.d("NetworkModeManager", ",isCmccSim = " + zIsCmccSim + ", phoneId = " + phoneId);
                    if (zIsCmccSim) {
                        return true;
                    }
                } else if (FiveGUtils.isFiveGInvisableByDefault()) {
                    return FiveGUtils.isNetworkModeInVisableByDevelopOption();
                }
            }
        }
        return false;
    }

    public static void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        NetworkMode.getInstance().dump(fileDescriptor, printWriter, strArr);
    }

    public static boolean isDisableLte() {
        return NetworkMode.isDisableLte();
    }

    public static boolean isRemove2GOnly(int i) {
        return NetworkMode.isRemove2GOnly(i);
    }

    public static boolean isRemovePrefer3G(int i) {
        return NetworkMode.isRemovePrefer3G(i);
    }

    public static boolean isRemoveTdScdma(int i) {
        return NetworkMode.isRemoveTdScdma(i);
    }

    public static boolean isRemoveCdma(int i) {
        return NetworkMode.isRemoveCdma(i);
    }

    public static boolean showWcdmaOnlyNetworkModes(int i) {
        return NetworkMode.showWcdmaOnlyNetworkModes(i);
    }

    public static boolean isShowCdmaUnavailable() {
        return NetworkMode.getInstance().isShowCdmaUnvailable();
    }

    public static void setNetworkModeInDb(int i, int i2) {
        ModemProvider.getInstance().setNetworkModeInDb(i, i2);
    }

    public static boolean shouldShowViceNetworkTypePref() {
        return NetworkMode.shouldShowViceNetworkTypePref();
    }

    public static boolean isNetworkModeLteOnly(int i) {
        return NetworkMode.isNetworkModeLteOnly(i);
    }

    public static boolean isDeviceLockViceNetworkModeForCmcc() {
        return NetworkMode.isDeviceLockViceNetworkModeForCmcc();
    }

    public static int getFiveGNetworkMode() {
        if (MiuiPhoneUtils.isDeviceNotSupportCDMA()) {
            return NetworkMode.NETWORK_MODE_NR_LTE_TDSCDMA_GSM_WCDMA;
        }
        return NetworkMode.NETWORK_MODE_NR_LTE_TDSCDMA_CDMA_EVDO_GSM_WCDMA;
    }

    public static void log(String str) {
        Rlog.i("NetworkModeManager", str);
    }
}
