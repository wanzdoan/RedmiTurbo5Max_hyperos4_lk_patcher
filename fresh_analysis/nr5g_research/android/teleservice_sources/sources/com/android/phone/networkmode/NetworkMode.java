package com.android.phone.networkmode;

import android.telephony.Rlog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.NetworkModeManager;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.utils.MiuiKoreaOperatorCustimizedUtils;
import com.ot.pubsub.i.a;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import miui.enterprise.EnterpriseManagerStub;
import miui.enterprise.PhoneHelperStub;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.DefaultSimManager;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class NetworkMode {
    public static final int[] INDIA_MCC_ARRAY = {a.c, 405};
    public static final int[] INDONESIA_MCC_ARRAY = {510};
    public static int NETWORK_MODE_NR_LTE = 24;
    public static int NETWORK_MODE_NR_LTE_CDMA_EVDO = 25;
    public static int NETWORK_MODE_NR_LTE_CDMA_EVDO_GSM_WCDMA = 27;
    public static int NETWORK_MODE_NR_LTE_GSM_WCDMA = 26;
    public static int NETWORK_MODE_NR_LTE_TDSCDMA = 29;
    public static int NETWORK_MODE_NR_LTE_TDSCDMA_CDMA_EVDO_GSM_WCDMA = 33;
    public static int NETWORK_MODE_NR_LTE_TDSCDMA_GSM = 30;
    public static int NETWORK_MODE_NR_LTE_TDSCDMA_GSM_WCDMA = 32;
    public static int NETWORK_MODE_NR_LTE_TDSCDMA_WCDMA = 31;
    public static int NETWORK_MODE_NR_LTE_WCDMA = 28;
    public static int NETWORK_MODE_NR_ONLY = 23;
    public static int sLockNetworkType;
    private static volatile NetworkMode sNetworkMode;
    private ArrayList mFilters = new ArrayList(4);

    public boolean isShowCdmaUnvailable() {
        return false;
    }

    private static NetworkMode init() {
        sLockNetworkType = PhoneGlobals.getInstance().getResources().getInteger(R.integer.config_lock_network_type);
        if (ModemProvider.IS_QCOM) {
            sNetworkMode = new QcNetworkMode();
        } else if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
            sNetworkMode = new MtkNetworkMode();
        } else if (ModemProvider.IS_SPRD) {
            sNetworkMode = new SprdNetworkMode();
        } else {
            sNetworkMode = new NetworkMode();
        }
        return sNetworkMode;
    }

    public static NetworkMode getInstance() {
        if (sNetworkMode == null) {
            synchronized (NetworkMode.class) {
                try {
                    if (sNetworkMode == null) {
                        sNetworkMode = init();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sNetworkMode;
    }

    protected NetworkMode() {
        addFilter(new NMFilterAddNr());
        addFilter(new NMFilterVirtualSim());
        if (Build.IS_CM_CUSTOMIZATION && isDeviceLockViceNetworkModeForCmcc()) {
            addFilter(new NMFilterCmccViceSlot());
        }
        boolean z = Build.IS_INTERNATIONAL_BUILD;
        if (z && TelephonyManager.getDefault().isDisableLte(false)) {
            addFilter(new NMFilterDisableLte());
        }
        if (isGlobalDeviceSupportWcdmaVice()) {
            addFilter(new NMFilterIndiaVicePref());
        }
        if (z) {
            addFilter(new NMFilterLteOnly());
            addFilter(new NMFilterAddWcdmaOnly());
        }
        addFilter(new NMFilterRemove2gOnly());
        addFilter(new NMFilterRemovePrefer3g());
        if (!MiuiPhoneUtils.isRemoveDual4GSwitch() && MiuiPhoneUtils.DUAL_VOLTE_SUPPORTED) {
            addFilter(new NMFilterDual4GDisabled());
        }
        addFilter(new NMFilterCloudSim());
        addFilter(new NMFilterRemoveCdma());
        addFilter(new NMFilterRemoveTdscdma());
        addFilter(new NMFilterCmccPreferred());
    }

    public void addFilter(NMFilter nMFilter) {
        this.mFilters.add(nMFilter);
    }

    public static int getPhoneTypesAccordingSimType(int i) {
        Phone phone;
        int phoneType = NetworkModeManager.getPhoneType(i);
        return (phoneType != 0 || (phone = PhoneFactory.getPhone(i)) == null) ? phoneType : phone.getPhoneType();
    }

    public static int[] getPhoneTypesAccordingSimType() {
        Phone phone;
        int[] iArr = new int[MiuiPhoneUtils.PHONE_COUNT];
        Phone[] phones = PhoneFactory.getPhones();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            int phoneType = NetworkModeManager.getPhoneType(i);
            iArr[i] = phoneType;
            if (phoneType == 0 && phones != null && i < phones.length && (phone = phones[i]) != null) {
                iArr[i] = phone.getPhoneType();
            }
        }
        return iArr;
    }

    public int[][] getSupportModes() {
        int[][] operatorSupportModes = getOperatorSupportModes(getDeviceSupportModes());
        ArrayList arrayList = this.mFilters;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            operatorSupportModes = ((NMFilter) obj).filter(operatorSupportModes);
        }
        if (EnterpriseManagerStub.ENTERPRISE_ACTIVATED && PhoneHelperStub.getInstance().isNetworkModesRestriction()) {
            for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                operatorSupportModes[i2][0] = PhoneHelperStub.getInstance().getAllowNetworkMode();
            }
        }
        MiuiPhoneUtils.printArray(operatorSupportModes, "NetworkMode getSupportModes");
        return operatorSupportModes;
    }

    public int[][] getOperatorSupportModes(int[][] iArr) {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (MiuiPhoneUtils.isOnlySupportLteNrForSlot(i)) {
                if ((MiuiPhoneUtils.isFiveGCapable() && i == DefaultSimManager.getDefaultDataSlotId()) || TelephonyManager.getDefault().isDualNrSupported()) {
                    iArr[i] = getOnlySupportModesForLteNr(true);
                } else {
                    iArr[i] = getOnlySupportModesForLteNr(false);
                }
            } else if (MiuiPhoneUtils.isOnlySupportNrForSlot(i)) {
                iArr[i] = getOnlySupportModesForNr();
            }
        }
        return iArr;
    }

    public int[] getOnlySupportModesForLteNr(boolean z) {
        if (z) {
            return PhoneGlobals.getInstance().getResources().getIntArray(R.array.support_networktypes_gsm_lte_nr);
        }
        return PhoneGlobals.getInstance().getResources().getIntArray(R.array.support_networktypes_gsm_lte);
    }

    public int[] getOnlySupportModesForNr() {
        return PhoneGlobals.getInstance().getResources().getIntArray(R.array.support_networktypes_nr);
    }

    public int[][] getDeviceSupportModes() {
        Rlog.e("NetworkMode", "getDeviceSupportModes error");
        return (int[][]) Array.newInstance((Class<?>) Integer.TYPE, MiuiPhoneUtils.PHONE_COUNT, 1);
    }

    public int[] getGsmMasterSupportModes() {
        Rlog.e("NetworkMode", "getGsmMasterSupportModes error");
        return new int[0];
    }

    public int[] getCdmaMasterSupportModes() {
        Rlog.e("NetworkMode", "getCdmaMasterSupportModes error");
        return new int[0];
    }

    public int[] getGsmViceSupportModes() {
        if (TelephonyManager.getDefault().isDualNrSupported()) {
            return getGsmMasterSupportModes();
        }
        return PhoneGlobals.getInstance().getResources().getIntArray(R.array.support_networktypes_gsm_vice);
    }

    public int[] getCdmaViceSupportModes() {
        if (TelephonyManager.getDefault().isDualNrSupported()) {
            return getCdmaMasterSupportModes();
        }
        return PhoneGlobals.getInstance().getResources().getIntArray(R.array.support_networktypes_cdma_vice);
    }

    public static boolean isDisableLte() {
        return TelephonyManager.getDefault().isDisableLte(false) && hasCardByMcc(INDONESIA_MCC_ARRAY);
    }

    public static boolean isNetworkModeLteOnly(int i) {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return HyperOSCustFeatureResolve.getBooleanForPhoneId(i, "support_lte_and_above_only", false);
        }
        return false;
    }

    public static boolean shouldShowViceNetworkTypePref() {
        int i = DefaultSimManager.getDefaultDataSlotId() == 1 ? 0 : 1;
        if (TelephonyManager.getDefault().isDualNrSupported()) {
            return true;
        }
        if (isNetworkModeLteOnly(i)) {
            return false;
        }
        if (MiuiPhoneUtils.isRemoveDual4GSwitch()) {
            return (isRemoveCdma(i) && MiuiPhoneUtils.isChinaOrMacauTelecomSim(MiuiPhoneUtils.getPhone(i))) ? false : true;
        }
        if (!((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDual4GEnabled() || ServiceProviderUtils.isChinaMobile(ModemProvider.getInstance().getSimImsi(i)) || getPhoneTypesAccordingSimType(i) == 2) {
            return isGlobalDeviceSupportWcdmaVice() && hasCardByMcc(INDIA_MCC_ARRAY) && getPhoneTypesAccordingSimType(i) == 1;
        }
        return true;
    }

    public static boolean isGlobalDeviceSupportWcdmaVice() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return !isViceSlotOnlySupport2G();
        }
        return false;
    }

    public static boolean isViceSlotOnlySupport2G() {
        return !MiuiPhoneUtils.isMultiSimEnabled();
    }

    public static boolean hasCardByMcc(int[] iArr) {
        if (iArr != null && iArr.length != 0) {
            List<SubscriptionInfo> subscriptionInfoList = SubscriptionManager.getDefault().getSubscriptionInfoList();
            if (subscriptionInfoList != null) {
                for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
                    for (int i : iArr) {
                        if (subscriptionInfo.getMcc() == i) {
                            return true;
                        }
                    }
                }
            }
            for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                String simImsi = ModemProvider.getInstance().getSimImsi(i2);
                if (simImsi != null) {
                    for (int i3 : iArr) {
                        if (simImsi.startsWith(Integer.toString(i3))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean isRemove2GOnly(int i) {
        if (!SubscriptionManager.isRealSlotId(i)) {
            return false;
        }
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (TelephonyManager.isCustFeatureResolveEnabled()) {
            if (!HyperOSCustFeatureResolve.getBooleanForPhoneId(i, "config_prefer_2g_bool", true)) {
                return true;
            }
        } else {
            if (TelephonyManager.isCustForEsVodafone() && (MiuiPhoneUtils.isEsVodafoneSim(phone) || MiuiPhoneUtils.isPtVodafoneSim(phone))) {
                return true;
            }
            if ((TelephonyManager.isCustForHkH3g() && (MiuiPhoneUtils.is3UkSim(phone) || (MiuiPhoneUtils.isH3GItSim(phone) && !MiuiPhoneUtils.isDeviceSupport3g2gOnly()))) || MiuiPhoneUtils.isIEVDFSim(phone) || MiuiPhoneUtils.isUkO2Sim(phone) || MiuiPhoneUtils.isAisSim(phone) || TelephonyManager.isCustForJpKd() || MiuiKoreaOperatorCustimizedUtils.isKrSktSim(phone) || MiuiKoreaOperatorCustimizedUtils.isKrKtSim(phone) || "334090".equals(MiuiPhoneUtils.getOperatorNumeric(phone)) || MiuiKoreaOperatorCustimizedUtils.isKrLguSim(phone) || MiuiPhoneUtils.isVodacomSouthAfricaSim(phone) || MiuiPhoneUtils.isH3GSeSim(phone) || MiuiPhoneUtils.isZaMtnSim(phone) || MiuiPhoneUtils.isUkEeSim(phone)) {
                return true;
            }
        }
        if (Build.IS_CM_CUSTOMIZATION && MiuiPhoneUtils.isFiveGCapable() && ServiceProviderUtils.isChinaMobile(ModemProvider.getInstance().getSimImsi(i))) {
            return true;
        }
        return (MiuiPhoneUtils.isChinaTelecomSim(phone) && isRemoveCdma(i)) || !CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_prefer_2g_bool", true);
    }

    public static boolean isRemovePrefer3G(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (TelephonyManager.isCustFeatureResolveEnabled()) {
            if (!HyperOSCustFeatureResolve.getBooleanForPhoneId(i, "config_show_prefer_3g", true)) {
                return true;
            }
        } else {
            if (MiuiPhoneUtils.isVodacomSouthAfricaSim(phone) || MiuiPhoneUtils.isZaMtnSim(phone) || MiuiPhoneUtils.isUkEeSim(phone) || MiuiPhoneUtils.is3UkSim(phone) || MiuiPhoneUtils.isH3GSeSim(phone) || MiuiKoreaOperatorCustimizedUtils.isKrLguSim(phone)) {
                return true;
            }
            if (MiuiKoreaOperatorCustimizedUtils.isCustForKrKt() && MiuiKoreaOperatorCustimizedUtils.isKrKtSim(phone)) {
                return true;
            }
        }
        if (MiuiPhoneUtils.isChinaTelecomSim(phone) && isRemoveCdma(i)) {
            return true;
        }
        return !CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_show_prefer_3g", true);
    }

    public static boolean isRemoveTdScdma(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        boolean booleanForPhoneId = HyperOSCustFeatureResolve.getBooleanForPhoneId(i, "config_remove_tdscdma", true);
        if (MiuiPhoneUtils.isFiveGCapable() && booleanForPhoneId) {
            return MiuiPhoneUtils.isCmccSim(phone) || MiuiPhoneUtils.isCbnSim(phone);
        }
        return false;
    }

    public static boolean isRemoveCdma(int i) {
        if (TelephonyManagerEx.getDefault().isPlatformX450() || TelephonyManagerEx.getDefault().isPlatformX475() || TelephonyManagerEx.getDefault().isPlatform8550() || TelephonyManagerEx.getDefault().isPlatform7475() || MiuiPhoneUtils.shouldRemoveCdma() || Build.IS_CT_CUSTOMIZATION_TEST || MiuiPhoneUtils.isDeviceNotSupportCDMA() || MiuiKoreaOperatorCustimizedUtils.isKrLguSim(MiuiPhoneUtils.getPhone(i))) {
            return true;
        }
        return MiuiPhoneUtils.isFiveGCapable() && i != DefaultSimManager.getDefaultDataSlotId() && MiuiPhoneUtils.isDualCTSim();
    }

    public static boolean showWcdmaOnlyNetworkModes(int i) {
        boolean booleanForPhoneId;
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null) {
            return false;
        }
        if (TelephonyManager.isCustFeatureResolveEnabled()) {
            booleanForPhoneId = HyperOSCustFeatureResolve.getBooleanForPhoneId(i, "config_show_3g_only", false);
        } else {
            booleanForPhoneId = !(!TelephonyManager.isCustForEsVodafone() || MiuiPhoneUtils.isPtVodafoneSim(phone) || MiuiPhoneUtils.isEEAVDFSim(phone) || MiuiPhoneUtils.isIEVDFSim(phone) || MiuiPhoneUtils.isNLVodafoneSim(phone)) || !(!TelephonyManager.isCustForHkH3g() || MiuiPhoneUtils.is3atH3GSim(phone) || MiuiPhoneUtils.is3UkSim(phone) || MiuiPhoneUtils.isH3GSeSim(phone) || (MiuiPhoneUtils.isH3GItSim(phone) && !MiuiPhoneUtils.isDeviceSupport3g2gOnly())) || TelephonyManager.isCustForMxAt() || TelephonyManager.isCustForFrSfr();
        }
        return booleanForPhoneId || CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_show_3g_only", false);
    }

    public static boolean isDeviceLockViceNetworkModeForCmcc() {
        return sLockNetworkType == 1;
    }

    public static boolean shouldAddNrNetworkModes(int i) {
        return MiuiPhoneUtils.showFiveGNetworkMode(i);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder sb = new StringBuilder(DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER);
        sb.append("NetworkMode: ");
        ArrayList arrayList = this.mFilters;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            sb.append(((NMFilter) obj).getClass().getSimpleName());
            sb.append(" ");
        }
        printWriter.println(sb.toString());
    }

    public static int[] getSupportNetworkMode(int i) {
        switch (i) {
            case 0:
                return new int[]{1};
            case 1:
                return new int[]{0, 1, 2};
            case 2:
                return new int[]{16, 1, 13};
            case 3:
                return new int[]{7, 4};
            case 4:
                return new int[]{17, 16, 1};
            case 5:
                return new int[]{9, 0, 1};
            case 6:
                return new int[]{10, 7};
            case 7:
                if (ModemProvider.IS_QCOM) {
                    return new int[]{20, 17, 11, 10, 9};
                }
                return new int[]{11, 10, 9};
            case 8:
                return new int[]{9, 3, 1};
            case 9:
                return new int[]{NETWORK_MODE_NR_ONLY, NETWORK_MODE_NR_LTE, NETWORK_MODE_NR_LTE_CDMA_EVDO, NETWORK_MODE_NR_LTE_GSM_WCDMA, NETWORK_MODE_NR_LTE_CDMA_EVDO_GSM_WCDMA, NETWORK_MODE_NR_LTE_WCDMA, NETWORK_MODE_NR_LTE_TDSCDMA, NETWORK_MODE_NR_LTE_TDSCDMA_GSM, NETWORK_MODE_NR_LTE_TDSCDMA_WCDMA, NETWORK_MODE_NR_LTE_TDSCDMA_GSM_WCDMA, NETWORK_MODE_NR_LTE_TDSCDMA_CDMA_EVDO_GSM_WCDMA};
            case 10:
                if (ModemProvider.IS_MTK || ModemProvider.IS_XRING || ModemProvider.IS_SPRD) {
                    return new int[]{0, 3};
                }
                return new int[]{16, 13, 18};
            case 11:
                return new int[]{0, 3, 7, 14, 18, 21};
            default:
                return new int[]{1};
        }
    }

    public int getNrNetworkModeValue(int i) {
        if (shouldAddNrNetworkModes(i)) {
            return getDesignatedNetworkModeValueForSlot(i, 9);
        }
        return getLteNetworkModeValue(i);
    }

    public int getLteNetworkModeValue(int i) {
        return getDesignatedNetworkModeValueForSlot(i, 7);
    }

    public int getDesignatedNetworkModeValueForSlot(int i, int i2) {
        int[][] supportModes = getInstance().getSupportModes();
        if (supportModes == null || i < 0 || i >= supportModes.length) {
            return -1;
        }
        int[] iArr = supportModes[i];
        if (iArr.length <= 0) {
            return -1;
        }
        int[] supportNetworkMode = getSupportNetworkMode(i2);
        for (int i3 : iArr) {
            for (int i4 : supportNetworkMode) {
                if (i3 == i4) {
                    return i3;
                }
            }
        }
        return -1;
    }

    public static int[] removeDeviceNrMode(int[] iArr, int i) {
        return shouldAddNrNetworkModes(i) ? iArr : NMFilter.removeNr(iArr);
    }

    public static boolean loadCustomRegionNetworkMode(int[][] iArr) {
        if (!TelephonyManager.isCustForLmClaro()) {
            return false;
        }
        for (int i = 0; i < iArr.length; i++) {
            int[] iArr2 = {NETWORK_MODE_NR_LTE_TDSCDMA_GSM_WCDMA, 9, 0, 2, 1};
            iArr[i] = iArr2;
            iArr[i] = removeDeviceNrMode(iArr2, i);
        }
        return true;
    }

    public static boolean isSupport3GAbilityHide3GSwitch(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_support_prefer_3g_when_hide_3g_switch", false);
    }

    public static boolean isNrNetworkModeKey(String str) {
        try {
            int i = Integer.parseInt(str);
            return i >= NETWORK_MODE_NR_ONLY && i <= NETWORK_MODE_NR_LTE_TDSCDMA_CDMA_EVDO_GSM_WCDMA;
        } catch (NumberFormatException unused) {
            Rlog.e("NetworkMode", "isNrNetworkModeKey : Invalid key format: " + str);
            return false;
        }
    }
}
