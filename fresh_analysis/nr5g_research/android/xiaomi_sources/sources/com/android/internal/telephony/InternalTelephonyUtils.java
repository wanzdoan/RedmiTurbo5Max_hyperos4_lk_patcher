package com.android.internal.telephony;

import android.app.ActivityThread;
import android.content.Context;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.RadioAccessFamily;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCallTracker;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;
import com.android.internal.telephony.uicc.IccRecords;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.CotaOpconfigManager;
import miui.telephony.DefaultSimManager;
import miui.telephony.FeatureConfiguration;
import miui.telephony.MiuiTelephony;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class InternalTelephonyUtils {
    private static final String CN_OPERATOR = "460";
    private static final String ECC_DATA_EXT_PATH = "/mi_ext/product/etc/eccdata";
    private static final String LOG_TAG = "InternalTelephonyUtils";
    private static final String REGION_CHINA_MCC = "460";
    private static final String XIAOMI_ECC_DATA_PATH = "/product/etc/eccdata";

    public static boolean systemNotReady() {
        String voldState = SystemProperties.get("vold.decrypt");
        return "1".equals(voldState) || "trigger_restart_min_framework".equals(voldState);
    }

    public static boolean shouldShowMmsDataIcon(Phone phone) {
        return HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "config_show_mms_data_icon", false);
    }

    public static boolean isSpecialBuild() {
        return Build.IS_INTERNATIONAL_BUILD || Build.IS_CM_CUSTOMIZATION_TEST || Build.IS_CU_CUSTOMIZATION_TEST || Build.IS_CT_CUSTOMIZATION_TEST;
    }

    public static boolean isInCnRegion() {
        TelephonyManager telephonyManager = TelephonyManager.getDefault();
        boolean isExistIccCard = telephonyManager.hasIccCard();
        int numPhones = telephonyManager.getPhoneCount();
        if (isExistIccCard) {
            for (int i = 0; i < numPhones; i++) {
                String networkOperator = telephonyManager.getNetworkOperatorForPhone(i);
                if (isCnFromOperator(networkOperator)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private static boolean isCnFromOperator(String operator) {
        String mcc = "";
        if (!TextUtils.isEmpty(operator) && operator.length() >= 3) {
            mcc = operator.substring(0, 3);
        }
        return "460".equals(mcc);
    }

    public static boolean isChinaSim(int slotIndex) {
        int simCn;
        if (TelephonyManager.getDefault() == null || (simCn = getInsertedSimCount()) < 1) {
            return false;
        }
        if (simCn == 1) {
            for (int i = 0; i < TelephonyManager.getDefault().getActiveModemCount(); i++) {
                if (isChinaSimOperator(i)) {
                    return true;
                }
            }
            return false;
        }
        if (!isChinaSimOperator(slotIndex)) {
            return false;
        }
        return true;
    }

    public static boolean isChinaSimOperator(int slot) {
        String simOperator = TelephonyManager.getDefault().getSimOperatorNumericForPhone(slot);
        if (TextUtils.isEmpty(simOperator) || !simOperator.startsWith("460")) {
            return false;
        }
        return true;
    }

    public static int getInsertedSimCount() {
        int insertedSimCount = 0;
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone.getIccCard() != null && phone.getIccCard().hasIccCard()) {
                insertedSimCount++;
            }
        }
        return insertedSimCount;
    }

    public static boolean isHideStkIcon() {
        boolean isSupportDevice = HyperOSCustFeatureResolve.getBoolean("device_hide_stk_icon", false);
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        return SystemProperties.getInt("ro.miui.ui.version.code", 0) >= 14 || isSupportDevice;
    }

    public static boolean isHideAlwaysReceiveMms() {
        return !Build.IS_INTERNATIONAL_BUILD && SystemProperties.getInt("ro.miui.ui.version.code", 0) >= 15;
    }

    public static boolean isChinaTestBuild() {
        if (Build.IS_CM_CUSTOMIZATION_TEST || Build.IS_CU_CUSTOMIZATION_TEST || Build.IS_CT_CUSTOMIZATION_TEST || Build.IS_CTS_BUILD) {
            return true;
        }
        return false;
    }

    public static boolean isSpecialNetorkOptimizationSupported() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            boolean isSupported = isSpecialNetorkOptimizationSupportedGl();
            return isSupported;
        }
        boolean isSupported2 = isSpecialNetorkOptimizationSupportedCn();
        return isSupported2;
    }

    private static boolean isSpecialNetorkOptimizationSupportedCn() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        boolean isSupportedByConfigCn = false;
        try {
            Context context = ActivityThread.currentApplication();
            isSupportedByConfigCn = context.getResources().getBoolean(285540479);
        } catch (Exception e) {
            Rlog.e(LOG_TAG, "isSupportedByConfigCn " + e);
        }
        boolean isSupportedForQcomCn = MiuiTelephony.IS_QCOM && (TelephonyManagerEx.getDefault().isPlatformX450() || TelephonyManagerEx.getDefault().isPlatformX475() || TelephonyManagerEx.getDefault().isPlatform8550() || TelephonyManagerEx.getDefault().isPlatform7475() || TelephonyManagerEx.getDefault().isPlatform8650() || TelephonyManagerEx.getDefault().isPlatform8635() || TelephonyManagerEx.getDefault().isPlatform8750());
        boolean isSupportedForMtkCn = (MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING) && HyperOSCustFeatureResolve.getBoolean("config_support_special_network_opt_for_Mtk_cn", false);
        boolean isSupportedForExtCn = HyperOSCustFeatureResolve.getBoolean("config_support_special_network_opt_for_cn", false);
        return isSupportedByConfigCn || isSupportedForQcomCn || isSupportedForMtkCn || isSupportedForExtCn;
    }

    private static boolean isSpecialNetorkOptimizationSupportedGl() {
        boolean isSupportedByExt = HyperOSCustFeatureResolve.getBoolean("config_support_special_network_opt_for_global", false);
        return Build.IS_INTERNATIONAL_BUILD && isSupportedByExt;
    }

    public static boolean isModemCellInfoCacheSupported() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            Rlog.d(LOG_TAG, "is international build");
            return false;
        }
        Context context = ActivityThread.currentApplication();
        boolean isModemCellInfoCacheSupported = context.getResources().getBoolean(285540410);
        return isModemCellInfoCacheSupported || HyperOSCustFeatureResolve.getBoolean("config_Modem_cell_info_cache", false);
    }

    public static boolean isSpecialNetworkModeEnabled(Context context) {
        int rst = Settings.Global.getInt(context.getContentResolver(), "special_network_optimization", 1);
        return rst != 0;
    }

    public static boolean isActiveSubId(int phoneSubId, Phone phone) {
        IccRecords records;
        Context ctx = phone.getContext();
        SubscriptionManager subscriptionManager = (SubscriptionManager) ctx.getSystemService("telephony_subscription_service");
        if (!subscriptionManager.isActiveSubId(phoneSubId) || (records = phone.getIccCard().getIccRecords()) == null || TextUtils.isEmpty(records.getIccId())) {
            return false;
        }
        SubscriptionInfo subInfo = subscriptionManager.getActiveSubscriptionInfo(phoneSubId);
        if (subInfo == null || subInfo.getIccId() != records.getIccId()) {
            Rlog.d("PhoneFactory", "isActiveSubId false");
            return false;
        }
        return true;
    }

    public static void updateAllowedNetworkTypesFromDb(Phone phone, Message response) {
        if (!MiuiTelephony.IS_XRING || Build.IS_INTERNATIONAL_BUILD) {
            int type = DefaultSimManager.getPreferredNetworkModeFromDb(phone.getContext(), phone.getPhoneId());
            Rlog.d(LOG_TAG, "updateAllowedNetworkTypesFromDb =" + type);
            phone.setAllowedNetworkTypes(0, RadioAccessFamily.getRafFromNetworkType(type), response);
        }
    }

    public static void notifyServiceStateChanged(Phone phone) {
        phone.notifyServiceStateChangedP(phone.getServiceState());
    }

    public static BufferedInputStream getEccdataInputStream(BufferedInputStream mOriginal) {
        BufferedInputStream original = getXiaomiEccdataInputStream(mOriginal);
        if (!CotaOpconfigManager.isSupportEccdata()) {
            Rlog.d(LOG_TAG, "no support cota eccdata");
            return original;
        }
        BufferedInputStream newInputStream = null;
        try {
            try {
                try {
                    File file = new File(CotaOpconfigManager.getOpconfigPath(CotaOpconfigManager.OpconfigFileType.EccData));
                    newInputStream = new BufferedInputStream(new FileInputStream(file));
                    Rlog.d(LOG_TAG, "load Eccdata from Opconfig");
                    try {
                        original.close();
                    } catch (RuntimeException rethrown) {
                        throw rethrown;
                    }
                } catch (Throwable rethrown2) {
                    if (newInputStream == null) {
                        return original;
                    }
                    try {
                        original.close();
                    } catch (RuntimeException rethrown3) {
                        throw rethrown3;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    throw rethrown2;
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                Rlog.d(LOG_TAG, "get Eccdata InputStream exception " + e2.toString());
                if (newInputStream == null) {
                    return original;
                }
                try {
                    original.close();
                } catch (RuntimeException rethrown4) {
                    throw rethrown4;
                }
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return newInputStream;
    }

    private static BufferedInputStream getXiaomiEccdataInputStream(BufferedInputStream original) {
        BufferedInputStream newInputStream = null;
        String filePath = XIAOMI_ECC_DATA_PATH;
        if (miui.telephony.TelephonyManager.isCustFeatureResolveEnabled() && new File(ECC_DATA_EXT_PATH).exists()) {
            Rlog.d(LOG_TAG, "load eccdata from mi_ext");
            filePath = ECC_DATA_EXT_PATH;
        }
        try {
            try {
                try {
                    File file = new File(filePath);
                    if (file.exists()) {
                        newInputStream = new BufferedInputStream(new FileInputStream(file));
                        Rlog.d(LOG_TAG, "load eccdata from xiaomi product");
                    } else {
                        Rlog.d(LOG_TAG, "getXiaomiEccdataInputStream not exists");
                    }
                    if (newInputStream != null) {
                        try {
                            original.close();
                            return newInputStream;
                        } catch (RuntimeException rethrown) {
                            throw rethrown;
                        }
                    }
                } catch (Throwable th) {
                    if (0 != 0) {
                        try {
                            original.close();
                        } catch (RuntimeException rethrown2) {
                            throw rethrown2;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        throw th;
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            Rlog.d(LOG_TAG, "get xiaomi product eccdata InputStream exception " + e3.toString());
            if (0 != 0) {
                try {
                    original.close();
                } catch (RuntimeException rethrown3) {
                    throw rethrown3;
                }
            }
        }
        Rlog.d(LOG_TAG, "getXiaomiEccdataInputStream newInputStream null");
        return original;
    }

    public static boolean isEmergencyCall(Phone phone) {
        ImsPhone imsPhone = phone.getImsPhone();
        if (imsPhone != null) {
            ImsPhoneCallTracker tracker = imsPhone.getCallTracker();
            for (ImsPhoneConnection connection : tracker.getConnections()) {
                if (connection.isEmergencyCall()) {
                    return true;
                }
            }
        }
        GsmCdmaCallTracker callTracker = phone.getCallTracker();
        if (callTracker == null || callTracker.mConnections == null) {
            return false;
        }
        for (GsmCdmaConnection conn : callTracker.mConnections) {
            if (conn != null && conn.isEmergencyCall()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidMobileNumber(Phone phone, String phoneNumber) {
        String operator = phone.getOperatorNumeric();
        if (PhoneNumberUtils.isChineseOperator(operator)) {
            boolean isValidChinaMobileNumber = PhoneNumberUtils.isChinaMobileNumber(phoneNumber);
            Rlog.d(LOG_TAG, "isValidChinaMobileNumber = " + isValidChinaMobileNumber);
            return isValidChinaMobileNumber;
        }
        if (!TextUtils.isEmpty(phoneNumber) && isKrOperator(operator)) {
            return phoneNumber.startsWith("010");
        }
        return true;
    }

    public static boolean isKrOperator(String operator) {
        return "45005".equals(operator) || "45006".equals(operator) || "45008".equals(operator);
    }

    public static String getImsSourceNumber(Phone phone) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) phone.getContext().getSystemService("telephony_subscription_service");
        if (subscriptionManager == null) {
            return "";
        }
        String number = subscriptionManager.getPhoneNumber(phone.getSubId(), 3);
        return number;
    }

    public static boolean isSupportRadioStall() {
        return (!Build.IS_STABLE_VERSION && MiuiTelephony.IS_QCOM) || FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_RADIO_STALL_DETECT);
    }

    public static boolean isSupportRoamingOptimization() {
        return FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_ROAMING_OPTIMIZATION);
    }

    public static boolean forceSetLteonlyAndIgnoreUIPrefer(int phoneId) {
        if (miui.telephony.TelephonyManager.isCustForJpKd()) {
            String simOperator = TelephonyManager.getDefault().getSimOperatorNumericForPhone(phoneId);
            if (!TextUtils.isEmpty(simOperator) && "44055".equals(simOperator)) {
                return true;
            }
            return false;
        }
        return false;
    }
}
