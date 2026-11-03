package com.android.internal.telephony;

import android.R;
import android.app.ActivityThread;
import android.app.timezonedetector.TelephonyTimeZoneSuggestion;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemProperties;
import android.os.TimestampedValue;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.service.euicc.GetEuiccProfileInfoListResult;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.emergency.EmergencyNumber;
import android.text.TextUtils;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.ntndata.SatellitNetworkManager;
import com.android.internal.telephony.ntnphone.SatelliteNetworkFactory;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.utils.MiEventUtils;
import com.android.internal.telephony.utils.MiuiRILUtils;
import com.android.telephony.Rlog;
import com.miui.misight.MiEventStub;
import com.xiaomi.cdc.CdcManager;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.security.SecurityManager;
import miui.telephony.MiuiTelephony;
import miui.telephony.PhoneDebug;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyUtils;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyCommonUtilsImpl implements TelephonyCommonUtilsStub.ITelephonyCommonUtils {
    private static final String CN_OPERATOR = "460";
    private static final int DIFF_TIME_ZONE = -2;
    private static final String DISABLE_SECURITY_BY_MISHOW = "disable_security_by_mishow";
    private static final int ERROR_NITZ_INFO = -1;
    private static final int EXCEPTION_TIME_VALUE = -3;
    private static final String LOG_TAG = "TelephonyCommonUtilsImpl";
    private static final int NORMAL_NITZ = 0;
    private static final String RADIO_POWER_CONTROL = "radio_power_control";
    private static final String REGION_CHINA_MCC = "460";
    private static final int RELATIVE_SAME_NITZ_INFO = 1;
    private static TelephonyCommonUtilsImpl sInstance;
    private static SatelliteNetworkFactory[] sSatelliteNetworkFactories;
    private TimestampedValue<NitzData> mAnchorNitzSignal;
    private TelephonyTimeZoneSuggestion mLatestTimeZone;
    private static long NITZ_UPDATE_DIFF_TIME = 120000;
    private static long OFFSET_TIME = 60000;
    private static String CHINA_TIME_ZONE = "Asia/Shanghai";
    private boolean mSameTimeZone = false;
    private boolean mIsChinaTimeZone = false;

    public TelephonyCommonUtilsImpl() {
        sInstance = this;
    }

    public static TelephonyCommonUtilsImpl getInstance() {
        if (sInstance == null) {
            throw new RuntimeException("TelephonyCommonUtilsImpl should not be null.");
        }
        return sInstance;
    }

    private void logi(String msg) {
        Rlog.i(LOG_TAG, msg);
    }

    private void logi(String tag, String msg) {
        Rlog.i(tag, msg);
    }

    private void logi(String msg, Exception e) {
        Rlog.i(LOG_TAG, msg, e);
    }

    public void initMiuiTelephony(Context context, FeatureFlags featureFlags, Object... arg) {
        MiuiTelephony.getInstance().init(context, featureFlags);
        int numPhones = TelephonyManager.getDefault().getActiveModemCount();
        sSatelliteNetworkFactories = new SatelliteNetworkFactory[numPhones];
        for (int i = 0; i < numPhones; i++) {
            sSatelliteNetworkFactories[i] = new SatelliteNetworkFactory(Looper.myLooper(), PhoneFactory.getPhone(i), featureFlags);
        }
        new SatellitNetworkManager(Looper.myLooper(), PhoneFactory.getDefaultPhone());
    }

    public boolean checkCallingOrSelfPermissionGranted(int uid) {
        return miui.telephony.TelephonyManager.checkCallingOrSelfPermissionGranted(Binder.getCallingUid());
    }

    public void checkCallingPermission(Context context, int uid, String permission, String message) {
        if (checkCallingOrSelfPermissionGranted(uid)) {
            return;
        }
        context.enforceCallingPermission(permission, message);
    }

    public void checkCallingOrSelfPermission(Context context, int uid, String permission, String message) {
        if (checkCallingOrSelfPermissionGranted(uid)) {
            return;
        }
        context.enforceCallingOrSelfPermission(permission, message);
    }

    public void restoreSettings(Phone phone) {
        MiuiNetworkControllerImpl.Processor.restoreSettings(phone);
    }

    public String pii(String info) {
        return canShowPrivateInfo() ? info : TelephonyUtils.pii(info);
    }

    public String piiIP(String original) {
        if (!TextUtils.isEmpty(original)) {
            String reg = "(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}|(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)";
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(original);
            while (matcher.find()) {
                try {
                    int length = 0;
                    String str = matcher.group(0);
                    if (str != null) {
                        length = str.length();
                    }
                    if (length != 0) {
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < length - 1; i++) {
                            char nextChar = str.charAt(i + 1);
                            if (nextChar == '.' || nextChar == ':') {
                                builder.append('x');
                            } else {
                                builder.append(str.charAt(i));
                            }
                        }
                        builder.append('x');
                        String piiStr = builder.toString();
                        original = original.replace(str, piiStr);
                    }
                } catch (Exception e) {
                    Rlog.e(LOG_TAG, "piiIP e: " + e);
                }
            }
            return original;
        }
        return original;
    }

    public boolean isCustSingleSimDevice() {
        return miui.telephony.TelephonyManager.isCustSingleSimDevice();
    }

    public boolean isChinaTestBuild() {
        if (Build.IS_CM_CUSTOMIZATION_TEST || Build.IS_CU_CUSTOMIZATION_TEST || Build.IS_CT_CUSTOMIZATION_TEST || Build.IS_CTS_BUILD) {
            return true;
        }
        return false;
    }

    public boolean isDemoBuild() {
        Context context = ActivityThread.currentApplication();
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        return Build.IS_DEMO_BUILD || Settings.Secure.getInt(context.getContentResolver(), DISABLE_SECURITY_BY_MISHOW, 0) == 1;
    }

    public static boolean isSimLock(int phoneId) {
        Context context = ActivityThread.currentApplication();
        if (context == null) {
            return false;
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isMtnRadioState = sp.getBoolean(RADIO_POWER_CONTROL + phoneId, true);
        return !isMtnRadioState;
    }

    public void processForCustSingleSimDevice(IccCardStatus status, Integer index) {
        MiuiRILUtils.processForCustSingleSimDevice(status, index);
    }

    public String[] handlePii(int request, String[] strings) {
        return MiuiRILUtils.handlePii(request, strings);
    }

    public String handlePii(int request, Object result) {
        return MiuiRILUtils.handlePii(request, result);
    }

    public boolean isInCnRegion() {
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

    public boolean isSpecialBuild() {
        return Build.IS_INTERNATIONAL_BUILD || Build.IS_CM_CUSTOMIZATION_TEST || Build.IS_CU_CUSTOMIZATION_TEST || Build.IS_CT_CUSTOMIZATION_TEST;
    }

    public String getCallingPackage() {
        Context context = ActivityThread.currentApplication();
        SecurityManager securityManager = (SecurityManager) context.getSystemService("security");
        String packageName = securityManager.getPackageNameByPid(Binder.getCallingPid());
        if (!TextUtils.isEmpty(packageName)) {
            return packageName;
        }
        return context.getPackageManager().getPackagesForUid(Binder.getCallingUid())[0];
    }

    public String getMiuiString(Context context, int originalId) {
        int miuiId;
        switch (originalId) {
            case R.string.deleted_key:
                miuiId = 286195877;
                break;
            case R.string.demo_restarting_message:
                miuiId = 286195878;
                break;
            default:
                miuiId = originalId;
                break;
        }
        return context.getResources().getString(miuiId);
    }

    public boolean isMiuiRom() {
        return (TextUtils.isEmpty(SystemProperties.get("ro.miui.ui.version.name", "")) && TextUtils.isEmpty(SystemProperties.get("ro.miui.ui.version.code", ""))) ? false : true;
    }

    private boolean isChinaSim(int slotIndex) {
        boolean rst = false;
        if (TelephonyManager.getDefault() != null) {
            int simCn = getInsertedSimCount();
            if (simCn < 1) {
                Rlog.d(LOG_TAG, "simCn:" + simCn);
                return false;
            }
            if (simCn == 1) {
                for (int i = 0; i < TelephonyManager.getDefault().getActiveModemCount(); i++) {
                    if (isChinaSimOperator(i)) {
                        rst = true;
                        break;
                    }
                }
            } else if (isChinaSimOperator(slotIndex)) {
                rst = true;
            }
        }
        Rlog.d(LOG_TAG, "isChinaSim rst:" + rst + ",slotIndex=" + slotIndex);
        return rst;
    }

    private boolean isChinaSimOperator(int slot) {
        String simOperator = TelephonyManager.getDefault().getSimOperatorNumericForPhone(slot);
        if (TextUtils.isEmpty(simOperator) || !simOperator.startsWith("460")) {
            return false;
        }
        return true;
    }

    private int getInsertedSimCount() {
        int insertedSimCount = 0;
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone.getIccCard() != null && phone.getIccCard().hasIccCard()) {
                insertedSimCount++;
            }
        }
        return insertedSimCount;
    }

    public void localLog(String key, String log) {
        StringBuilder sb;
        String key2 = "TelephonyCommonUtilsImpl." + key;
        try {
            PhoneFactory.addLocalLog(key2, 30);
            sb = new StringBuilder();
        } catch (IllegalArgumentException e) {
            sb = new StringBuilder();
        } finally {
            PhoneFactory.localLog(key2, "callingUid = " + Binder.getCallingUid() + " callingPid = " + Binder.getCallingPid() + log);
        }
        StringBuilder sbAppend = sb.append("callingUid = ");
    }

    public void processForCustSingleSimDevice() {
        MiuiNetworkControllerImpl.Processor.processForCustSingleSimDevice();
    }

    public boolean isSupportDomesticRoaming() {
        return miui.telephony.TelephonyManager.isSupportDomesticRoaming();
    }

    public int disconnectCauseFromCode(int causeCode) {
        return MiuiCallControllerImpl.Processor.disconnectCauseFromCode(causeCode);
    }

    public boolean isDataRatChanged(int previousRat, int newRat) {
        String preNetworkClassStr = TelephonyManagerEx.getDefault().getNetworkClassName(ServiceState.rilRadioTechnologyToAccessNetworkType(previousRat));
        String newNetworkClassStr = TelephonyManagerEx.getDefault().getNetworkClassName(ServiceState.rilRadioTechnologyToAccessNetworkType(newRat));
        Rlog.d(LOG_TAG, "RAT CHANGED, " + previousRat + "->" + newRat + ",preNetworkClassStr=" + preNetworkClassStr + ",newNetworkClassStr," + newNetworkClassStr);
        if (!preNetworkClassStr.equals(newNetworkClassStr)) {
            return true;
        }
        return false;
    }

    public BufferedInputStream getEccdataInputStream(BufferedInputStream original) {
        return InternalTelephonyUtils.getEccdataInputStream(original);
    }

    public boolean isRemoveEccData() {
        Context context = ActivityThread.currentApplication();
        return context.getResources().getBoolean(285540503);
    }

    public void updateEccdata(List<EmergencyNumber> emergencyNumberList) {
        ActivityThread.currentApplication();
        String cloudEccDataInfo = TelephonyManagerEx.getDefault().getCloudEccData();
        Rlog.d(LOG_TAG, "EccData from cloud: " + cloudEccDataInfo);
        if (TextUtils.isEmpty(cloudEccDataInfo) || emergencyNumberList.isEmpty() || emergencyNumberList.get(0) == null) {
            return;
        }
        String[] eccDataFromCloud = cloudEccDataInfo.split(";");
        for (String eccData : eccDataFromCloud) {
            if (!TextUtils.isEmpty(eccData)) {
                try {
                    JSONObject jsonObject = new JSONObject(eccData);
                    String ecc_plmn = jsonObject.getString("plmn").substring(0, 3);
                    String ecc_countryIso = MccTable.countryCodeForMcc(ecc_plmn);
                    String ecc_number = jsonObject.getString("ecc");
                    for (int i = 0; i < emergencyNumberList.size(); i++) {
                        if (emergencyNumberList.get(i).getCountryIso().equals(ecc_countryIso) && emergencyNumberList.get(i).getNumber().equals(ecc_number)) {
                            emergencyNumberList.remove(i);
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Rlog.d(LOG_TAG, "AssetEmergencyNumberList after cloudControl update: " + emergencyNumberList);
    }

    public boolean isSoftbankCarrier() {
        return "jp_sb".equals(SystemProperties.get("ro.miui.customized.region"));
    }

    public void sendEvent(MiEventStub event) {
        MiEventUtils.sendEvent(event);
    }

    private boolean canShowPrivateInfo() {
        return PhoneDebug.VDBG;
    }

    public boolean setMobileData(Context context, boolean z) {
        if (SystemProperties.get("ro.product.mod_device", "").contains("_global")) {
            GlobalSettingsHelper.setInt(context, "mobile_data", -1, z ? 1 : 0);
            return true;
        }
        return true;
    }

    public void setEuiccProvisioned(Context context, GetEuiccProfileInfoListResult result) {
        if (Settings.Global.getInt(context.getContentResolver(), "euicc_provisioned", 0) != 1 && result.getProfiles().size() > 0) {
            Settings.Global.putInt(context.getContentResolver(), "euicc_provisioned", 1);
        }
    }

    public boolean isCustForJpKd() {
        return miui.telephony.TelephonyManager.isCustForJpKd();
    }

    public boolean isSatelliteEnable(Context context) {
        try {
            boolean enable = true;
            if (Settings.System.getInt(context.getContentResolver(), "satellite_state", 0) == 0 && Settings.System.getInt(context.getContentResolver(), "xingwang_satellite_state", 0) != 1) {
                enable = false;
            }
            Rlog.d(LOG_TAG, "isSatelliteEnable = " + enable);
            return enable;
        } catch (Exception e) {
            Rlog.e(LOG_TAG, "isSatelliteEnable e: " + e);
            return false;
        }
    }

    public void filterRadioCapabilities(Set<String> capabilities) {
        if (capabilities == null || capabilities.isEmpty()) {
            return;
        }
        try {
            logi("Restore CAPABILITY_USES_ALLOWED_NETWORK_TYPES_BITMASK");
            if (SystemProperties.getInt("ro.vendor.radio.5g", 0) == 2) {
                logi("sa only device, remove nr-dc cap");
                capabilities.remove("CAPABILITY_NR_DUAL_CONNECTIVITY_CONFIGURATION_AVAILABLE");
            }
            logi("ok in filterRadioCapabilities()");
        } catch (Exception ex) {
            logi("error in filterRadioCapabilities() " + ex);
        }
    }

    public void updateCustEmergencyNumber(List<EmergencyNumber> emergencyNumberList, String countryIso) {
        if (isCustForJpKd() && HyperOSCustFeatureResolve.getBoolean("config_update_cust_emergency_number", false) && "jp".equals(countryIso)) {
            logi("updateCustEmergencyNumber for countryIso: " + countryIso);
            emergencyNumberList.add(new EmergencyNumber("110", countryIso, "", 1, new ArrayList(), 16, 0));
        }
    }

    public boolean notifySignalStrength(Phone phone) {
        MiuiDefaultPhoneNotifierImpl phoneNotifierImpl = (MiuiDefaultPhoneNotifierImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDefaultPhoneNotifierImpl.class, phone.getPhoneId());
        if (phoneNotifierImpl == null || !phoneNotifierImpl.notifySignalStrength(phone)) {
            return false;
        }
        return true;
    }

    public boolean reportDataToCdc(int eventId, Bundle data) {
        if (CdcManager.getDefault() != null) {
            return CdcManager.getDefault().reportDataToCdc(eventId, data);
        }
        return false;
    }
}
