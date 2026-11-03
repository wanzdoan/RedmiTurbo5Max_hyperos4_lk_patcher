package com.android.phone;

import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.app.ActivityThread;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManager;
import android.location.Country;
import android.location.CountryDetector;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.Vibrator;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.MiStubParam;
import android.telephony.MiuiTelephonyManagerStub;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.telephony.tcpka.IModemTcpKeepAliveIndCallback;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;
import androidx.preference.PreferenceManager;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.InternalTelephonyUtils;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.internal.util.ArrayUtils;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.module.IMiConnectModule;
import com.android.phone.module.IModemModule;
import com.android.phone.module.INetworkModule;
import com.android.phone.module.ISatelliteModule;
import com.android.phone.module.ISettingsModule;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.module.IUtilityModule;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiuiJpOperatorCustimizedUtils;
import com.android.phone.utils.MiuiKoreaOperatorCustimizedUtils;
import com.android.phone.utils.MiuiOpconfigUtils;
import com.android.services.telephony.DeviceState;
import com.android.services.telephony.ims.ImsAdapterBase;
import com.nxp.id.tp.common.HexString$$ExternalSyntheticBUOutline1;
import com.ot.pubsub.a.a;
import com.ot.pubsub.util.s;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.ModemSarUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.DefaultSimManager;
import miui.telephony.EsimManager;
import miui.telephony.FeatureConfiguration;
import miui.telephony.PhoneDebug;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.VirtualSimUtils;
import miui.util.MiuiMultiDisplayTypeInfo;
import miuix.animation.internal.TransitionInfo;
import miuix.core.util.EnvStateManager;
import miuix.os.DeviceHelper;

/* JADX INFO: loaded from: classes.dex */
public class MiuiPhoneUtils {
    public static int DBG_LEVEL = 1;
    public static final boolean IS_ESIM_CAPABLE;
    private static final boolean IS_INDIA;
    public static final String PACKAGE_NAME_DING_TALK;
    private static final long SHOW_ILLEGAL_ME_DIALOG_PERIOD;
    public static final String VICE_SLOT_VOLTE_DATA_ENABLED;
    private static int sQueryFlag;
    public static final int PHONE_COUNT = TelephonyManager.getDefault().getPhoneCount();
    public static final boolean DUAL_VOLTE_SUPPORTED = TelephonyManager.getDefault().isDualVolteSupported();
    public static final int CT_VOLTE_SUPPORTED_MODE = TelephonyManager.getDefault().getCtVolteSupportedMode();
    public static final String CUSTOMIZED_REGION = SystemProperties.get("ro.miui.customized.region", "");

    public static boolean isDynamicBwpSupported() {
        return false;
    }

    public static boolean isHMH2xCmForThreeMode() {
        return false;
    }

    public static boolean isNrNwType(int i) {
        return i == 20 || i == 30;
    }

    public static boolean isOnlySupportCmccBeiDou() {
        return false;
    }

    public static boolean isQuickSwitchMobileNetworkFeatureEnabled() {
        return true;
    }

    public static boolean isTabletDeviceWithVolteCapability() {
        return false;
    }

    public static boolean shouldDisable5GByDefault() {
        return false;
    }

    static {
        IS_ESIM_CAPABLE = SystemProperties.getBoolean("ro.vendor.miui.support_esim", false) || SystemProperties.getBoolean("ro.vendor.miui.use_google_lpa", false);
        IS_INDIA = "in".equalsIgnoreCase(SystemProperties.get("ro.miui.build.region"));
        VICE_SLOT_VOLTE_DATA_ENABLED = (ModemProvider.IS_MTK || ModemProvider.IS_XRING) ? "data_service_enabled" : "vice_slot_volte_data_enabled";
        PACKAGE_NAME_DING_TALK = HyperOSCustFeatureResolve.getString("config_dingding_packages", "");
        sQueryFlag = 269484096;
        SHOW_ILLEGAL_ME_DIALOG_PERIOD = TimeUnit.DAYS.toMillis(HyperOSCustFeatureResolve.getInt("show_illegal_me_dialog_period", 180));
        PhoneDebug.addListener(new PhoneDebug.Listener() { // from class: com.android.phone.MiuiPhoneUtils.1
            public void onDebugChanged() {
                MiuiPhoneUtils.DBG_LEVEL = PhoneDebug.VDBG ? 2 : 1;
            }
        });
    }

    public static Uri getFdnUri(int i) {
        return Uri.parse("content://icc/fdn").buildUpon().appendQueryParameter(SubscriptionManager.SLOT_KEY, String.valueOf(i)).build();
    }

    public static Phone getPhone(int i) {
        try {
            return PhoneFactory.getPhone(i);
        } catch (Exception unused) {
            return null;
        }
    }

    public static Phone[] getPhones() {
        return PhoneFactory.getPhones();
    }

    public static Phone getDataPhone() {
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        if (SubscriptionManager.isRealSlotId(defaultDataSlotId)) {
            return getPhone(defaultDataSlotId);
        }
        return null;
    }

    public static Phone getCdmaPhone() {
        Phone[] phones = PhoneFactory.getPhones();
        for (int i = 0; i < phones.length; i++) {
            if (phones[i].getPhoneType() == 2 && phones[i].getIccCard().hasIccCard()) {
                return phones[i];
            }
        }
        return null;
    }

    public static String getPreferenceKey(String str, int i) {
        if (PHONE_COUNT <= 1) {
            return str;
        }
        return str + "." + i;
    }

    public static boolean isDcOnlyVirtualSim(Context context) {
        return MiuiSettings.VirtualSim.isVirtualSimEnabled(context) && MiuiSettings.VirtualSim.getVirtualSimType(context) == 1;
    }

    public static boolean isVirtualSim(int i) {
        return MiuiSettings.VirtualSim.isVirtualSimEnabled(PhoneGlobals.getInstance()) && i == MiuiSettings.VirtualSim.getVirtualSimSlotId(PhoneGlobals.getInstance());
    }

    public static int getVirtualSimSlot() {
        if (MiuiSettings.VirtualSim.isVirtualSimEnabled(PhoneGlobals.getInstance())) {
            return MiuiSettings.VirtualSim.getVirtualSimSlotId(PhoneGlobals.getInstance());
        }
        return SubscriptionManager.INVALID_SLOT_ID;
    }

    public static int getDcOnlyVirtualSimSlot() {
        return ModemProvider.getInstance().getVirtualSimSlot(PhoneGlobals.getInstance(), true, 1);
    }

    public static String getVirtualSimCarrierName() {
        Bundle bundleCall = null;
        try {
            bundleCall = PhoneGlobals.getInstance().getContentResolver().call(Uri.parse("content://com.miui.virtualsim.provider.virtualsimInfo"), "getCarrierName", (String) null, (Bundle) null);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "getVirtualSimCarrierName e" + e);
        }
        return bundleCall == null ? getUkelinkCloudSimSpn() : bundleCall.getString("carrierName");
    }

    public static Intent getVirtualSimIntent() {
        Bundle bundleCall;
        try {
            bundleCall = PhoneGlobals.getInstance().getContentResolver().call(Uri.parse("content://com.miui.virtualsim.provider.virtualsimInfo"), "getSettingsEntranceIntent", (String) null, (Bundle) null);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "getVirtualSimIntent e" + e);
            bundleCall = null;
        }
        if (bundleCall == null) {
            String ukelinkCloudSimHomeAction = getUkelinkCloudSimHomeAction();
            if (TextUtils.isEmpty(ukelinkCloudSimHomeAction)) {
                return null;
            }
            Intent intent = new Intent();
            intent.setPackage("com.ucloudlink.cloudsim.xiaomi");
            intent.setAction(ukelinkCloudSimHomeAction);
            return intent;
        }
        return (Intent) bundleCall.getParcelable("setting_entrance");
    }

    public static Bundle getVirtualSimEntranceInfo() {
        try {
            return PhoneGlobals.getInstance().getContentResolver().call(Uri.parse("content://com.miui.virtualsim.provider.virtualsimInfo"), "getSettingEntranceInfo", (String) null, (Bundle) null);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "getVirtualSimEntranceInfo e" + e);
            return null;
        }
    }

    public static String getUkelinkVirtualSimIccid() {
        return Settings.Global.getString(PhoneGlobals.getInstance().getContentResolver(), "virtual_seed_sim_iccid");
    }

    public static int getUkelinkVirtualSimSlot() {
        String ukelinkVirtualSimIccid = getUkelinkVirtualSimIccid();
        List<SubscriptionInfo> subscriptionInfoList = SubscriptionManager.getDefault().getSubscriptionInfoList();
        if (subscriptionInfoList != null && subscriptionInfoList.size() > 0 && !TextUtils.isEmpty(ukelinkVirtualSimIccid)) {
            for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
                if (ukelinkVirtualSimIccid.equalsIgnoreCase(subscriptionInfo.getIccId())) {
                    return subscriptionInfo.getSlotId();
                }
            }
        }
        return SubscriptionManager.INVALID_SLOT_ID;
    }

    public static int getUkelinkVirtualSimNetworkType() {
        try {
            return Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "virtual_seed_sim_perferred_nw");
        } catch (Settings.SettingNotFoundException e) {
            Log.e("MiuiPhoneUtils", "getUkelinkVirtualSimNetworkType" + e);
            return 20;
        }
    }

    public static boolean isMiSim(int i) {
        String[] strArr = {"小米通信", "Mi Mobile", "Mi", "小米移动", "小米", "小米移動", "小米"};
        Phone phone = getPhone(i);
        if (phone == null) {
            return false;
        }
        String operatorNumeric = getOperatorNumeric(phone);
        String simOperatorNameForSlot = TelephonyManager.getDefault().getSimOperatorNameForSlot(i);
        if (TextUtils.isEmpty(simOperatorNameForSlot)) {
            return false;
        }
        if (ServiceProviderUtils.isChinaUnicom(operatorNumeric) || ServiceProviderUtils.isChinaTelecom(operatorNumeric)) {
            for (int i2 = 0; i2 < 7; i2++) {
                if (simOperatorNameForSlot.contains(strArr[i2])) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getUkelinkCloudSimIccid() {
        return Settings.Global.getString(PhoneGlobals.getInstance().getContentResolver(), "virtual_vsim_sim_iccid");
    }

    public static int getUkelinkCloudSimSlot() {
        String ukelinkCloudSimIccid = getUkelinkCloudSimIccid();
        List<SubscriptionInfo> subscriptionInfoList = SubscriptionManager.getDefault().getSubscriptionInfoList();
        if (subscriptionInfoList != null && subscriptionInfoList.size() > 0 && !TextUtils.isEmpty(ukelinkCloudSimIccid)) {
            for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
                if (ukelinkCloudSimIccid.equalsIgnoreCase(subscriptionInfo.getIccId())) {
                    return subscriptionInfo.getSlotId();
                }
            }
        }
        return SubscriptionManager.INVALID_SLOT_ID;
    }

    public static int getUkelinkCloudSimNetworkType() {
        try {
            return Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "virtual_vsim_sim_perferred_nw");
        } catch (Settings.SettingNotFoundException e) {
            Log.e("MiuiPhoneUtils", "getUkelinkCloudSimNetworkType" + e);
            return 20;
        }
    }

    public static String getUkelinkCloudSimSpn() {
        return Settings.Global.getString(PhoneGlobals.getInstance().getContentResolver(), "cloudsim_spn");
    }

    public static String getUkelinkCloudSimHomeAction() {
        return Settings.Global.getString(PhoneGlobals.getInstance().getContentResolver(), "cloudsim_home_action");
    }

    public static boolean isIccCardActivated(int i) {
        SubscriptionInfo availableSubscriptionInfoBySlot = getAvailableSubscriptionInfoBySlot(i);
        return availableSubscriptionInfoBySlot == null || availableSubscriptionInfoBySlot.isActivated();
    }

    public static CallManager getCallManager() {
        return PhoneGlobals.getInstance().getCallManager();
    }

    public static int getInsertedSimCount() {
        Phone[] phones = PhoneFactory.getPhones();
        if (phones == null) {
            return 0;
        }
        int i = 0;
        for (Phone phone : phones) {
            if (phone.getIccCard().hasIccCard()) {
                i++;
            }
        }
        return i;
    }

    public static void openedDual4GConfirmPromptWithSlot(int i) {
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).openedDual4GConfirmPromptWithSlot(i);
    }

    public static void enterEsimManager(boolean z) {
        try {
            PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
            if (Build.IS_TABLET && HyperOSCustFeatureResolve.getBoolean("config_should_use_columns_layout", false)) {
                Intent intent = new Intent("action_pad_enter_lpa");
                intent.setPackage(phoneGlobals.getPackageName());
                intent.putExtra("isAutoEnter", z);
                phoneGlobals.sendBroadcast(intent);
                return;
            }
            Intent intent2 = new Intent();
            intent2.setAction("android.telephony.euicc.action.MANAGE_EMBEDDED_SUBSCRIPTIONS");
            if (SystemProperties.getBoolean("ro.vendor.miui.use_google_lpa", false)) {
                intent2.setFlags(335544320);
            } else {
                intent2.setFlags(268435456);
            }
            intent2.putExtra("isAutoEnter", z);
            phoneGlobals.startActivity(intent2);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "Enter euicc manager failed.", e);
        }
    }

    public static boolean isSupportMep() {
        try {
            return PhoneGlobals.getInstance().getPackageManager().hasSystemFeature("android.hardware.telephony.euicc.mep");
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportMep " + e);
            return false;
        }
    }

    public static boolean showEsimSupportedCarriersSetting() {
        if (TelephonyManager.isCustForMxTelcel() || TelephonyManager.isCustForMxAt()) {
            return false;
        }
        return (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getResources().getBoolean(R.bool.config_show_esim_supported_setting);
    }

    public static boolean isValidIccId(String str) {
        if (str == null) {
            return false;
        }
        return !str.toLowerCase().startsWith("fffffffff");
    }

    public static boolean isMiRoamingSupportEsim() {
        try {
            ApplicationInfo applicationInfo = PhoneGlobals.getInstance().getPackageManager().getApplicationInfo("com.miui.virtualsim", DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
            if (applicationInfo == null) {
                Log.d("MiuiPhoneUtils", "isMiRoamingSupportEsim appInfo is null");
                return false;
            }
            Bundle bundle = applicationInfo.metaData;
            if (bundle != null) {
                return bundle.getInt("virtualsim_is_support_vsim", 0) >= 1;
            }
            Log.d("MiuiPhoneUtils", "isMiRoamingSupportEsim return false");
            return false;
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isMiRoamingSupportEsim " + e);
        }
    }

    public static boolean isSimPresent(int i) {
        if (i < 0 || i >= PHONE_COUNT) {
            Log.d("MiuiPhoneUtils", "Invalid slotId: " + i);
            return false;
        }
        UiccSlot[] uiccSlots = UiccController.getInstance().getUiccSlots();
        if (uiccSlots != null && uiccSlots.length > i) {
            UiccSlot uiccSlot = uiccSlots[i];
            if (uiccSlot != null) {
                if (uiccSlot.getCardState() == IccCardStatus.CardState.CARDSTATE_PRESENT) {
                    Log.d("MiuiPhoneUtils", "SIM" + (i + 1) + " is CARDSTATE_PRESENT");
                    return true;
                }
                Log.d("MiuiPhoneUtils", "SIM" + (i + 1) + " is not CARDSTATE_PRESENT");
            }
            Log.d("MiuiPhoneUtils", "SIM" + (i + 1) + " slotInfo is null");
        }
        return false;
    }

    public enum DualSimType {
        OTHER(-1),
        NO_VSIM(0),
        VSIM_SLOT_0(1),
        VSIM_SLOT_1(2);

        private final int value;

        DualSimType(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static DualSimType checkCurrentDualSimType() {
        if (isEsimEnabled()) {
            Log.d("MiuiPhoneUtils", "esim already enabled");
            return DualSimType.OTHER;
        }
        int virtualSimSlot = getVirtualSimSlot();
        Log.d("MiuiPhoneUtils", "vsim slot: " + virtualSimSlot);
        if (virtualSimSlot != SubscriptionManager.INVALID_SLOT_ID) {
            return virtualSimSlot == 0 ? DualSimType.VSIM_SLOT_0 : DualSimType.VSIM_SLOT_1;
        }
        Log.d("MiuiPhoneUtils", "no vsim enabled");
        return DualSimType.NO_VSIM;
    }

    private static boolean isEsimEnabled() {
        return ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getEsimSecureValue() == 1;
    }

    public static boolean stopVsimBeforeEnableEsim() {
        Log.d("MiuiPhoneUtils", "Stopping VSIM before enabling eSIM...");
        stopVsim();
        int i = 0;
        boolean z = false;
        while (i < 5 && !z) {
            i++;
            try {
                Thread.sleep(500L);
                Bundle bundleIsVsimEnabled = isVsimEnabled();
                z = (bundleIsVsimEnabled == null || bundleIsVsimEnabled.getBoolean(a.L)) ? false : true;
                Log.d("MiuiPhoneUtils", "Polling VSIM status: attempt=" + i + ", stopped=" + z);
            } catch (InterruptedException e) {
                Log.e("MiuiPhoneUtils", "Polling interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
        if (!z) {
            Log.w("MiuiPhoneUtils", "Failed to stop VSIM after 5 attempts.");
            return false;
        }
        Log.d("MiuiPhoneUtils", "VSIM stopped successfully.");
        return true;
    }

    private static void stopVsim() {
        Log.d("MiuiPhoneUtils", "stopVsim...");
        try {
            String str = isMiRoamingSupportEsim() ? "com.miui.virtualsim.provider.virtualsimInfo" : "vsimcore.modem";
            Log.d("MiuiPhoneUtils", "stopVsim authority=".concat(str));
            new Bundle();
            PhoneGlobals.getInstance().getContentResolver().call(Uri.parse("content://".concat(str)), "stopVsim", (String) null, (Bundle) null);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "stopVsim " + e);
        }
    }

    private static Bundle isVsimEnabled() {
        Log.d("MiuiPhoneUtils", "isVsimEnabled...");
        Bundle bundleCall = null;
        try {
            String str = isMiRoamingSupportEsim() ? "com.miui.virtualsim.provider.virtualsimInfo" : "vsimcore.modem";
            Log.d("MiuiPhoneUtils", "isVsimEnabled authority=".concat(str));
            bundleCall = PhoneGlobals.getInstance().getContentResolver().call(Uri.parse("content://".concat(str)), "isVsimEnabled", (String) null, (Bundle) null);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isVsimEnabled " + e);
        }
        Log.d("MiuiPhoneUtils", "isVsimEnabled result=" + bundleCall);
        return bundleCall;
    }

    public static void openedDual4GConfirmPrompt() {
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).openedDual4GConfirmPrompt();
    }

    public static void showSmartSwitchDualSimDialog() {
        ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).showSmartSwitchDualSimDialog();
    }

    public static void addWindowLable(Intent intent, CharSequence charSequence) {
        intent.putExtra(":miui:starting_window_label", charSequence);
    }

    public static void setNetworkModeInDb(List list, int i, int i2) {
        SubscriptionInfo subscriptionInfoBySlot;
        ContentResolver contentResolver = PhoneGlobals.getInstance().getContentResolver();
        android.telephony.TelephonyManager.putIntAtIndex(contentResolver, "preferred_network_mode", i, i2);
        if (list == null) {
            subscriptionInfoBySlot = SubscriptionManager.getDefault().getSubscriptionInfoForSlot(i);
        } else {
            subscriptionInfoBySlot = getSubscriptionInfoBySlot(list, i);
        }
        if (subscriptionInfoBySlot != null) {
            int subscriptionId = subscriptionInfoBySlot.getSubscriptionId();
            Settings.Global.putInt(contentResolver, "preferred_network_mode" + subscriptionId, i2);
            Rlog.i("MiuiPhoneUtils", "setNetworkModeInDb slot=" + i + " sub=" + subscriptionId + " : " + i2);
            return;
        }
        Rlog.i("MiuiPhoneUtils", "setNetworkModeInDb slot=" + i + " but no valid sub : " + i2);
    }

    public static int getIccCardCountExcludeSlot(int i) {
        int i2 = 0;
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone.getPhoneId() != i && phone.getIccCard().hasIccCard()) {
                i2++;
            }
        }
        return i2;
    }

    public static boolean isMultiSimEnabled() {
        return PHONE_COUNT > 1;
    }

    public static SubscriptionInfo getSubscriptionInfoBySlot(List list, int i) {
        if (list == null) {
            return null;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            SubscriptionInfo subscriptionInfo = (SubscriptionInfo) it.next();
            if (subscriptionInfo.getSlotId() == i) {
                return subscriptionInfo;
            }
        }
        return null;
    }

    public static SubscriptionInfo getAvailableSubscriptionInfoBySlot(int i) {
        return getSubscriptionInfoBySlot(SubscriptionManager.getDefault().getAvailableSubscriptionInfoList(), i);
    }

    static void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        dumpNetworkSelection(fileDescriptor, printWriter, strArr);
        dumpImsFeatureValues(fileDescriptor, printWriter, strArr);
        if (MiuiOpconfigUtils.supportCarrierApex() || MiuiOpconfigUtils.supportCarrierCota()) {
            dumpOpConfigValues(fileDescriptor, printWriter, strArr);
        }
        dumpMiuiLogValues(fileDescriptor, printWriter, strArr);
        if (getInsertedSimCount() > 0) {
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).dumpNetworkMode(fileDescriptor, printWriter, strArr);
        }
        SubscriptionManager.getDefault().dump(fileDescriptor, printWriter, strArr);
        ((IModemModule) ModuleDelegate.get(IModemModule.class)).dumpModemTcpKa(fileDescriptor, printWriter, strArr);
        ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).dump(fileDescriptor, printWriter, strArr);
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).dump(fileDescriptor, printWriter, strArr);
        ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).dump(fileDescriptor, printWriter, strArr);
        ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).dump(fileDescriptor, printWriter, strArr);
        ((ISettingsModule) ModuleDelegate.get(ISettingsModule.class)).dump(fileDescriptor, printWriter, strArr);
        ((IMiConnectModule) ModuleDelegate.get(IMiConnectModule.class)).dump(fileDescriptor, printWriter, strArr);
        ((IModemModule) ModuleDelegate.get(IModemModule.class)).dump(fileDescriptor, printWriter, strArr);
    }

    public static void showSatelliteAlertDialog(int i) {
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).showSatelliteAlertDialog(i);
    }

    public static boolean isJioSim(String str) {
        String[] strArr = {"405840", "405854", "405855", "405856", "405857", "405858", "405859", "405860", "405861", "405862", "405863", "405864", "405865", "405866", "405867", "405868", "405869", "405870", "405871", "405872", "405873", "405874"};
        for (int i = 0; i < 22; i++) {
            if (strArr[i].equals(str)) {
                Rlog.i("MiuiPhoneUtils", "isJioSim");
                return true;
            }
        }
        return false;
    }

    public static boolean isAirtelSim(String str) {
        String[] strArr = {"40402", "40403", "40410", "40416", "40431", "40440", "40445", "40449", "40470", "40490", "40492", "40493", "40494", "40495", "40496", "40497", "40498", "40551", "40552", "40553", "40554", "40555", "40556"};
        for (int i = 0; i < 23; i++) {
            if (strArr[i].equals(str)) {
                Rlog.i("MiuiPhoneUtils", "isAirtelSim");
                return true;
            }
        }
        return false;
    }

    public static boolean isMPNSim(Phone phone) {
        return "26892".equals(getOperatorNumeric(phone));
    }

    public static boolean isGermanyDTByVPLMN(String str) {
        if (str == null) {
            return false;
        }
        String[] strArr = {"26201", "26206", "26277", "26203"};
        for (int i = 0; i < 4; i++) {
            if (strArr[i].equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSpainVdfByVPLMN(String str) {
        if (str == null) {
            return false;
        }
        String[] strArr = {"21401", "21406"};
        for (int i = 0; i < 2; i++) {
            if (strArr[i].equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSpainMasByVPLMN(String str) {
        if (str == null) {
            return false;
        }
        String[] strArr = {"21403", "21404", "21405", "21419", "21421"};
        for (int i = 0; i < 5; i++) {
            if (strArr[i].equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFranceMasByVPLMN(String str) {
        if (str == null) {
            return false;
        }
        String[] strArr = {"20809", "20810"};
        for (int i = 0; i < 2; i++) {
            if (strArr[i].equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIraqFastlinkSim(String str) {
        return "41866".equals(str);
    }

    public static boolean isSeatelSim(String str) {
        return "45611".equals(str);
    }

    public static boolean isYTLSim(String str) {
        return "502152".equals(str);
    }

    public static boolean isSmartfrenSim(String str) {
        return "51009".equals(str) || "51028".equals(str);
    }

    public static boolean is4GOnlySim(String str) {
        return isJioSim(str) || isIraqFastlinkSim(str) || isSeatelSim(str) || isYTLSim(str) || isSmartfrenSim(str);
    }

    public static boolean isSSOperatedByUt(Phone phone) {
        return phone.isImsRegistered() || ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isUtEnabled(phone);
    }

    public static boolean isCTSimInViceSlot() {
        for (int i = 0; i < PHONE_COUNT; i++) {
            if (isCTSimInViceSlot(getPhone(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCTSimInViceSlot(Phone phone) {
        return (isChinaOrMacauTelecomSim(phone) || isHKTelecomSim(phone)) && phone.getPhoneId() != DefaultSimManager.getDefaultDataSlotId();
    }

    public static boolean isKrLguSimInViceSlot() {
        for (int i = 0; i < PHONE_COUNT; i++) {
            if (isKrLguSimInViceSlot(getPhone(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isKrLguSimInViceSlot(Phone phone) {
        return MiuiKoreaOperatorCustimizedUtils.isKrLguSim(phone) && phone.getPhoneId() != DefaultSimManager.getDefaultDataSlotId();
    }

    public static boolean isCmdaCwOperatedByUt(Phone phone) {
        return phone.isImsRegistered();
    }

    public static boolean isCmccSim(Phone phone) {
        return ServiceProviderUtils.isChinaMobile(getOperatorNumeric(phone));
    }

    public static boolean isCnOperatorSim(Phone phone) {
        return ServiceProviderUtils.isChineseServiceProvider(getOperatorNumeric(phone));
    }

    public static boolean isOnlySupportLteNrForSlot(int i) {
        if (TelephonyManager.isCustFeatureResolveEnabled()) {
            return HyperOSCustFeatureResolve.getBooleanForPhoneId(i, "support_lte_and_nr_only", false);
        }
        Phone phone = getPhone(i);
        return phone != null && (isCbnSim(phone) || isCTMOSim(phone));
    }

    public static boolean isCbnSim(Phone phone) {
        return "46015".equals(getOperatorNumeric(phone));
    }

    public static boolean isCTMOSim(Phone phone) {
        return "45507".equals(getOperatorNumeric(phone));
    }

    public static boolean isOnlySupportNrForSlot(int i) {
        if (TelephonyManager.isCustFeatureResolveEnabled()) {
            return HyperOSCustFeatureResolve.getBooleanForPhoneId(i, "support_nr_only", false);
        }
        Phone phone = getPhone(i);
        return phone != null && (isBESTSim(phone) || isMPNSim(phone));
    }

    public static boolean isBESTSim(Phone phone) {
        return "45707".equals(getOperatorNumeric(phone));
    }

    public static boolean isCbnSim(String str) {
        return "46015".equals(str);
    }

    public static boolean isCMCCSim(String str) {
        return "46000".equals(str) || "46002".equals(str) || "46004".equals(str) || "46007".equals(str) || "46008".equals(str) || "46013".equals(str);
    }

    public static boolean isCnOperator(String str) {
        return str != null && str.startsWith("460");
    }

    public static boolean isChinaOrMacauTelecomSim(Phone phone) {
        String operatorNumeric = getOperatorNumeric(phone);
        return ServiceProviderUtils.isChinaTelecom(operatorNumeric) || ServiceProviderUtils.isMacauTelecom(operatorNumeric);
    }

    public static boolean isChinaTelecomSim(Phone phone) {
        return ServiceProviderUtils.isChinaTelecom(getOperatorNumeric(phone));
    }

    public static boolean isChinaUnicomSim(Phone phone) {
        return ServiceProviderUtils.isChinaUnicom(getOperatorNumeric(phone));
    }

    public static boolean isHKTelecomSim(Phone phone) {
        return "45431".equals(getOperatorNumeric(phone));
    }

    public static boolean isSupportModemFeature() {
        return TelephonyManagerEx.getDefault().isN1Supported() || TelephonyManagerEx.getDefault().isN28Supported() || TelephonyManagerEx.getDefault().isBWPSupported();
    }

    public static boolean isSupportEnable2GSwitch(Phone phone) {
        if (phone == null || phone.getContext() == null) {
            return false;
        }
        return is2GAndVolteSupportConfig(phone);
    }

    public static boolean is2GAndVolteSupportConfig(Phone phone) {
        if (phone == null) {
            return false;
        }
        return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_show_2g_switch", false) && PhoneGlobals.getInstance().getCarrierConfigForSubId(phone.getSubId()).getBoolean("carrier_volte_available_bool");
    }

    public static boolean isModemNonDdsRrcSupported() {
        boolean z;
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        try {
            z = PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_modem_nondds_rrc_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isModemNonDdsRrcSupported " + e);
            z = false;
        }
        return z || TelephonyManagerEx.getDefault().isPlatform8550();
    }

    public static boolean isNsaScgPoorOptimizationSupported() {
        boolean z;
        String str = SystemProperties.get("ro.miui.build.region");
        if (!"eea".equalsIgnoreCase(str) && !"IN".equalsIgnoreCase(str)) {
            return false;
        }
        try {
            z = PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_nsa_scg_poor_optimization_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isNsaScgPoorOptimizationSupported " + e);
            z = false;
        }
        return z || isNsaScgPoorOptimizationV2Supported();
    }

    public static boolean isFakeSaStationDetectSupported() {
        if (Build.IS_INTERNATIONAL_BUILD || ModemProvider.IS_QCOM) {
            return false;
        }
        return HyperOSCustFeatureResolve.getBoolean("config_fake_sa_station_detect_supported", false);
    }

    public static boolean isModemNonddsRrcDisabledByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getBoolean("non_dds_rrc_off", false);
        }
        return false;
    }

    public static String getOperatorNumeric(Phone phone) {
        try {
            String operatorNumeric = phone.getIccCard().getIccRecords().getOperatorNumeric();
            Log.i("MiuiPhoneUtils", "getOperatorNumeric phone:" + phone.getPhoneId() + " numeric:" + operatorNumeric);
            return operatorNumeric;
        } catch (NullPointerException e) {
            Log.e("MiuiPhoneUtils", "getOperatorNumeric: " + e.getMessage());
            return "";
        }
    }

    public static String getNetworkNumeric(int i) {
        return isValidatePhoneId(i) ? android.telephony.TelephonyManager.getTelephonyProperty(i, "gsm.operator.numeric", "") : "";
    }

    public static boolean isSpecialTestEnv(int i) {
        boolean z = false;
        if (Build.IS_INTERNATIONAL_BUILD) {
            Log.i("MiuiPhoneUtils", "isSpecialTestEnv international build, skip");
            return false;
        }
        boolean booleanForPhoneId = HyperOSCustFeatureResolve.getBooleanForPhoneId(i, "lab_white_sim_hplmn_list", false);
        List stringArray = HyperOSCustFeatureResolve.getStringArray("lab_white_sim_ehplmn_list");
        String networkNumeric = getNetworkNumeric(i);
        boolean z2 = stringArray != null && stringArray.contains(networkNumeric);
        if (booleanForPhoneId && z2) {
            z = true;
        }
        Log.i("MiuiPhoneUtils", "isSpecialTestEnv phoneId:" + i + " isHplmnValid:" + booleanForPhoneId + " networkNumeric:" + networkNumeric + " isEhplmnValid:" + z2 + " result:" + z);
        return z;
    }

    public static String getSimOperatorName(int i) {
        Phone phone = getPhone(i);
        if (phone == null || phone.getIccCard() == null) {
            return null;
        }
        String serviceProviderName = phone.getIccCard().getServiceProviderName();
        Log.i("MiuiPhoneUtils", "getSimOperatorName phoneId:" + i + " name:" + serviceProviderName);
        return serviceProviderName;
    }

    public static String getIccId(int i) {
        Phone phone = getPhone(i);
        if (phone != null) {
            return phone.getFullIccSerialNumber();
        }
        return null;
    }

    public static String getImsi(int i) {
        Phone phone = getPhone(i);
        if (phone == null || phone.getIccCard() == null || phone.getIccCard().getIccRecords() == null) {
            return null;
        }
        return phone.getIccCard().getIccRecords().getIMSI();
    }

    /* JADX INFO: renamed from: com.android.phone.MiuiPhoneUtils$3, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$IccCardConstants$State;

        static {
            int[] iArr = new int[IccCardConstants.State.values().length];
            $SwitchMap$com$android$internal$telephony$IccCardConstants$State = iArr;
            try {
                iArr[IccCardConstants.State.ABSENT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.PIN_REQUIRED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.PUK_REQUIRED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.NETWORK_LOCKED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.READY.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.NOT_READY.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.PERM_DISABLED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.CARD_IO_ERROR.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.CARD_RESTRICTED.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$IccCardConstants$State[IccCardConstants.State.LOADED.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    public static String getIccStateIntentString(IccCardConstants.State state) {
        switch (AnonymousClass3.$SwitchMap$com$android$internal$telephony$IccCardConstants$State[state.ordinal()]) {
            case 1:
                return "ABSENT";
            case 2:
            case 3:
            case 4:
                return "LOCKED";
            case 5:
                return "READY";
            case 6:
                return "NOT_READY";
            case 7:
                return "LOCKED";
            case 8:
                return "CARD_IO_ERROR";
            case 9:
                return "CARD_RESTRICTED";
            case 10:
                return "LOADED";
            default:
                return "UNKNOWN";
        }
    }

    public static boolean isUkO2Sim(Phone phone) {
        return "23410".equals(getOperatorNumeric(phone));
    }

    public static boolean setCallWaitingEnabled() {
        for (int i = 0; i < PHONE_COUNT; i++) {
            if (HyperOSCustFeatureResolve.getBooleanForPhoneId(i, "set_call_waiting_enabled", false)) {
                return true;
            }
        }
        return false;
    }

    public static boolean is3UkSim(Phone phone) {
        return "23420".equals(getOperatorNumeric(phone)) || "23594".equals(getOperatorNumeric(phone));
    }

    public static boolean isOrangePolandSim(Phone phone) {
        return "26003".equals(getOperatorNumeric(phone));
    }

    public static boolean isOrangeFranceSim(Phone phone) {
        return "20801".equals(getOperatorNumeric(phone));
    }

    public static boolean isPEMovistarSim(Phone phone) {
        return "71606".equals(getOperatorNumeric(phone));
    }

    public static boolean isChileClaroSim(Phone phone) {
        return "73003".equals(getOperatorNumeric(phone));
    }

    public static boolean shouldEnableVideoSuppService(Phone phone) {
        if (phone != null && isSSOperatedByUt(phone) && ImsAdapterBase.getInstance().isVideoEnabled(phone)) {
            return isVideoSuppServiceSupport(phone);
        }
        return false;
    }

    public static boolean isVideoSuppServiceSupport(Phone phone) {
        if (phone == null) {
            return false;
        }
        return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_video_supp_service", false);
    }

    public static boolean isRemoveStkSettings(int i) {
        ContentResolver contentResolver = PhoneGlobals.getInstance().getContentResolver();
        StringBuilder sb = new StringBuilder("stk_app_is_install");
        sb.append(i);
        return Build.IS_INTERNATIONAL_BUILD || Settings.Global.getInt(contentResolver, sb.toString(), 0) == 0;
    }

    public static CharSequence getDisplayName(SubscriptionInfo subscriptionInfo) {
        android.telephony.SubscriptionInfo activeSubscriptionInfoForSimSlotIndex;
        if (subscriptionInfo == null) {
            return "";
        }
        if (isVirtualSim(subscriptionInfo.getSlotId())) {
            return VirtualSimUtils.getVirtualSimCarrierName(PhoneGlobals.getInstance());
        }
        CharSequence displayName = subscriptionInfo.getDisplayName();
        return (!TextUtils.isEmpty(displayName) || (activeSubscriptionInfoForSimSlotIndex = android.telephony.SubscriptionManager.from(PhoneGlobals.getInstance()).getActiveSubscriptionInfoForSimSlotIndex(subscriptionInfo.getSlotId())) == null) ? displayName : activeSubscriptionInfoForSimSlotIndex.getCarrierName();
    }

    public static int getSimIconResId(int i, boolean z) {
        if (isVirtualSim(i)) {
            return z ? R.drawable.mi_big_sim_icon : R.drawable.mi_small_sim_icon;
        }
        if (i == 1) {
            return z ? R.drawable.big_sim_icon_slot_2 : R.drawable.small_sim_icon_slot_2;
        }
        return z ? R.drawable.big_sim_icon_slot_1 : R.drawable.small_sim_icon_slot_1;
    }

    public static boolean isRegionSupportVoLTE(Context context, String str) {
        if (CarrierConfigLoaderInjector.isVolteForceEnabled(context)) {
            return true;
        }
        String[] stringArray = context.getResources().getStringArray(R.array.region_support_volte);
        if (stringArray == null) {
            return false;
        }
        for (String str2 : stringArray) {
            if (isRegionOrDeviceConfiged(str2, str)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isRegionOrDeviceConfiged(String str, String str2) {
        String[] strArrSplit;
        if (!TextUtils.isEmpty(str) && (strArrSplit = str.split(";")) != null && strArrSplit.length != 0 && str2.equals(strArrSplit[0])) {
            if (strArrSplit.length == 1) {
                return true;
            }
            Log.d("MiuiPhoneUtils", "isRegionOrDeviceConfiged: region: " + strArrSplit[0] + " devices: " + strArrSplit[1]);
            String str3 = Build.DEVICE;
            String[] strArrSplit2 = strArrSplit[1].split(",");
            if (strArrSplit2 != null && strArrSplit2.length != 0) {
                for (String str4 : strArrSplit2) {
                    if (str4 != null && str4.trim().equals(str3)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean getIccRecordsLoaded(int i) {
        Phone phone;
        if (SubscriptionManager.isRealSlotId(i) && (phone = getPhone(i)) != null) {
            return phone.getIccRecordsLoaded();
        }
        return false;
    }

    private static void dumpNetworkSelection(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        List activeSubscriptionInfoList = SubscriptionManager.getDefault().getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList != null) {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance());
            Iterator it = activeSubscriptionInfoList.iterator();
            while (it.hasNext()) {
                int subscriptionId = ((SubscriptionInfo) it.next()).getSubscriptionId();
                printWriter.println(" network_selection_name_key" + subscriptionId + "=" + defaultSharedPreferences.getString("network_selection_name_key" + subscriptionId, ""));
                StringBuilder sb = new StringBuilder("network_selection_key");
                sb.append(subscriptionId);
                printWriter.println(" network_selection_key" + subscriptionId + "=" + defaultSharedPreferences.getString(sb.toString(), ""));
            }
        }
        printWriter.flush();
    }

    private static void dumpImsFeatureValues(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        StringBuilder sb = new StringBuilder(DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER);
        sb.append("ImsValues: ");
        for (int i = 0; i < PHONE_COUNT; i++) {
            boolean enhanced4gLteModeSetting = ImsAdapterBase.getInstance().getEnhanced4gLteModeSetting(phoneGlobals, i);
            boolean zIsWfcEnabledByUser = ImsAdapterBase.getInstance().isWfcEnabledByUser(phoneGlobals, i);
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getUserSelectedVolteState(SubscriptionManager.getDefault().getSubscriptionIdForSlot(i));
            sb.append("\nPhone" + i + ":");
            sb.append("\t volteEnabled=" + enhanced4gLteModeSetting + " ");
            sb.append(" vowifiEnabled=" + zIsWfcEnabledByUser + " ");
        }
        printWriter.println(sb.toString());
    }

    private static void dumpMiuiLogValues(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        StringBuilder sb = new StringBuilder(DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER);
        sb.append("MiuiLogSecretCode: ");
        boolean zIsVolteForceEnabled = CarrierConfigLoaderInjector.isVolteForceEnabled(phoneGlobals);
        boolean zIsVoWifiForceEnabled = CarrierConfigLoaderInjector.isVoWifiForceEnabled(phoneGlobals);
        sb.append("\n volteCode=" + zIsVolteForceEnabled + " ");
        sb.append("\t vowifiCode=" + zIsVoWifiForceEnabled + " ");
        printWriter.println(sb.toString());
    }

    private static void dumpOpConfigValues(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder sb = new StringBuilder(DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER);
        sb.append("OpCfg: ");
        sb.append("\t version=" + MiuiOpconfigUtils.getOpconfigVersion());
        sb.append("\t name=" + MiuiOpconfigUtils.getOpconfigName());
        printWriter.println(sb.toString());
    }

    public static PersistableBundle getCarrierConfigMap(int i, String str) {
        PersistableBundle carrierConfigForSubId = PhoneGlobals.getInstance().getCarrierConfigForSubId(i);
        if (carrierConfigForSubId != null) {
            return carrierConfigForSubId.getPersistableBundle(str);
        }
        return null;
    }

    public static boolean isLte(int i) {
        int networkTypeForSubscription = TelephonyManagerEx.getDefault().getNetworkTypeForSubscription(i);
        long networkClass = TelephonyManagerEx.getDefault().getNetworkClass(networkTypeForSubscription);
        Log.i("MiuiPhoneUtils", "isLte...subId = " + i + "networkType = " + networkTypeForSubscription + " , networkClass = " + networkClass);
        return 397312 == networkClass;
    }

    public static boolean maybeShowDialogForSuppService(Phone phone, int i) {
        return ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).maybeShowDialogForSuppService(phone, i);
    }

    public static boolean isDataEnableNeededForUt(Phone phone, int i) {
        if (i == 2 || ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDataEnabled(getDataPhone())) {
            return false;
        }
        if (!phone.isImsRegistered() && !((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isUtEnabled(phone)) {
            return false;
        }
        if (isCmccSim(phone) || isChinaOrMacauTelecomSim(phone)) {
            return true;
        }
        return isChinaUnicomSim(phone) && isVolteEnabledByPlatform(phone);
    }

    public static boolean maybeShowTurnOffVolteForCall(Phone phone) {
        return ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).maybeShowTurnOffVolteForCall(phone);
    }

    public static boolean isVolteEnabledByPlatform(Phone phone) {
        if (DUAL_VOLTE_SUPPORTED) {
            Log.d("MiuiPhoneUtils", "phoneId=" + phone.getPhoneId());
            return TelephonyManager.getDefault().isVolteEnabledByPlatform(phone.getPhoneId());
        }
        return TelephonyManager.getDefault().isVolteEnabledByPlatform();
    }

    public static int getSupportSatelliteSlotId() {
        int i;
        int i2 = 0;
        int i3 = -1;
        int i4 = 0;
        while (true) {
            i = PHONE_COUNT;
            if (i2 >= i) {
                break;
            }
            if (isSatelliteSim(getPhone(i2))) {
                i4++;
                i3 = i2;
            }
            i2++;
        }
        if (i4 == i) {
            TelephonyManager.getDefault();
            if (!TelephonyManager.isCustSingleSimDevice()) {
                return i4;
            }
        }
        return i3;
    }

    public static Map compensateSatAntenna(float f, float f2, float f3, float f4) {
        double radians;
        float radians2 = (float) Math.toRadians(f);
        float radians3 = (float) Math.toRadians(f2);
        float radians4 = (float) Math.toRadians(-f3);
        double d = radians2;
        double d2 = radians3;
        double dSin = Math.sin(d) * Math.cos(d2);
        double dSin2 = Math.sin(d) * Math.sin(d2);
        double dCos = Math.cos(d);
        double dAtan2 = Math.atan2(dSin, dCos);
        double d3 = radians4;
        double dSin3 = Math.sin(d3);
        double dPow = Math.pow(Math.cos(d3), 2.0d) - (dSin2 * dSin2);
        if (dPow >= 0.0d) {
            radians = Math.atan2(dSin3, Math.sqrt(dPow));
        } else {
            radians = Math.toRadians(90.0d) - dAtan2;
        }
        double d4 = radians + dAtan2;
        double radians5 = Math.toRadians(90.0d - Math.toDegrees(d4));
        double dAtan3 = Math.atan2(dSin2, (dSin * Math.cos(radians5)) + (dCos * Math.sin(radians5)));
        HashMap map = new HashMap();
        map.put("pitchAnt", Float.valueOf(-((float) Math.toDegrees(dAtan2))));
        map.put("azimuthAnt", Float.valueOf((float) Math.toDegrees(dAtan3)));
        map.put("pitchSat", Float.valueOf(-((float) Math.toDegrees(d4))));
        map.put("azimuthSat", Float.valueOf(((f4 + ((float) Math.toDegrees(dAtan3))) + 360.0f) % 360.0f));
        return map;
    }

    public static boolean isSatelliteEnable(Context context) {
        try {
            boolean z = true;
            if (Settings.System.getInt(context.getContentResolver(), "satellite_state", 0) == 0 && Settings.System.getInt(context.getContentResolver(), "xingwang_satellite_state", 0) != 1) {
                z = false;
            }
            Log.d("MiuiPhoneUtils", "isSatelliteEnable = " + z);
            return z;
        } catch (Exception e) {
            Log.d("MiuiPhoneUtils", "isSatelliteEnable e: " + e);
            return false;
        }
    }

    public static boolean isSatelliteSim(Phone phone) {
        if (SystemProperties.getBoolean("persist.radio.satellite.sim.enabled", false) && phone.getIccCard().hasIccCard()) {
            return true;
        }
        return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_satellite_support", false);
    }

    public static boolean shouldShowSatelliteButton(Context context) {
        boolean zIsSupportSatelliteByDevice = TelephonyManagerEx.getDefault().isSupportSatelliteByDevice();
        boolean zIsKidMode = isKidMode(context);
        boolean zIsSecondSpaceMode = isSecondSpaceMode(context);
        Log.d("MiuiPhoneUtils", "shouldShowSatelliteButton device = " + zIsSupportSatelliteByDevice + ", kid = " + zIsKidMode + ", isSecondSpace = " + zIsSecondSpaceMode);
        if (zIsKidMode || zIsSecondSpaceMode) {
            return false;
        }
        return zIsSupportSatelliteByDevice;
    }

    public static boolean shouldShowSatelliteSignalBoostButton() {
        return TelephonyManagerEx.getDefault().isSupportSatelliteByDevice() && TelephonyManagerEx.getDefault().isSupportSatelliteSignalBoost();
    }

    public static boolean shouldShowSatelliteBusinessEntrance() {
        for (int i = 0; i < PHONE_COUNT; i++) {
            Phone phone = getPhone(i);
            boolean booleanForPhoneId = HyperOSCustFeatureResolve.getBooleanForPhoneId(i, "show_satelliste_business_entry", false);
            if (isChinaTelecomSim(phone) || booleanForPhoneId) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSatelliteButtonEditable(Context context) {
        boolean zIsSupportSatelliteByCarrier = TelephonyManagerEx.getDefault().isSupportSatelliteByCarrier();
        boolean z = isDuringCall() || isSatelliteDuringCall(context);
        boolean zIsExitSatelliteInProgress = isExitSatelliteInProgress();
        boolean z2 = Settings.System.getInt(PhoneGlobals.getInstance().getContentResolver(), "airplane_mode_on", -1) == 1 && !SystemProperties.getBoolean("persist.radio.ntn_airplane_on", false);
        Log.d("MiuiPhoneUtils", "isSatelliteButtonEditable isCarrierSupport = " + zIsSupportSatelliteByCarrier + ", isDuringCall = " + z + ", isAirPlaneEnabled = " + z2 + ", isExitSatelliteInProgress" + zIsExitSatelliteInProgress);
        return (z2 || z || zIsExitSatelliteInProgress) ? false : true;
    }

    public static boolean isExitSatelliteInProgress() {
        Bundle ntnModuleInfo = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getNtnModuleInfo();
        if (ntnModuleInfo != null) {
            return ntnModuleInfo.getBoolean("satellite_exit_in_progress", false);
        }
        return false;
    }

    public static boolean isSuperSavePowerMode(Context context) {
        return Settings.System.getInt(context.getContentResolver(), "power_supersave_mode_open", 0) == 1;
    }

    public static boolean isKidMode(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "kid_mode_status", 0) == 1;
    }

    public static boolean isSecondSpaceMode(Context context) {
        return Settings.Secure.getInt(context.getContentResolverForUser(UserHandle.OWNER), "second_user_id", -10000) == UserHandle.myUserId();
    }

    public static boolean isDualCTSim() {
        int i;
        if (getInsertedSimCount() < 2) {
            return false;
        }
        int i2 = 0;
        int i3 = 0;
        while (true) {
            i = PHONE_COUNT;
            if (i2 >= i) {
                break;
            }
            Phone phone = getPhone(i2);
            if (isChinaOrMacauTelecomSim(phone) || isHKTelecomSim(phone)) {
                i3++;
            }
            i2++;
        }
        return i3 == i;
    }

    public static boolean isViceSlotOfDualCTSim(int i) {
        return isDualCTSim() && i != DefaultSimManager.getDefaultDataSlotId();
    }

    public static boolean isDefaultDataSlotId(int i) {
        return i == DefaultSimManager.getDefaultDataSlotId();
    }

    public static boolean isSupportSmartNetworkGameOptimization() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_smart_network_game_optimization_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportedSmartNetworkGameOptimization " + e);
            return false;
        }
    }

    public static boolean isSupportVideoDataStallOptimization() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_video_data_stall_optimization_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportVideoDataStallOptimization " + e);
            return false;
        }
    }

    public static boolean isSupportWechatVideoOptimization() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_wechat_video_optimization_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportWechatVideoOptimization " + e);
            return false;
        }
    }

    public static boolean isSupportMtkVideoSbpOptimization() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_mtk_video_sbp_optimization_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportMtkVideoSbpOptimization " + e);
            return false;
        }
    }

    public static boolean isStreamingOptimizationSupported() {
        return HyperOSCustFeatureResolve.getBoolean("config_support_stream_opt", false);
    }

    public static boolean isSupportInternationalTravelDataOptimization() {
        return HyperOSCustFeatureResolve.getBoolean("config_support_internation_travel_data_opt", false);
    }

    public static boolean isSupportCongestionOptimization() {
        return HyperOSCustFeatureResolve.getBoolean("config_support_congestion_opt", false);
    }

    public static boolean isDataRoaming(int i) {
        ServiceState serviceState;
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || (serviceState = phone.getServiceState()) == null) {
            return false;
        }
        return serviceState.getRoaming();
    }

    public static int getDataRat(int i) {
        ServiceState serviceState;
        Phone phone = getPhone(i);
        if (phone == null || (serviceState = phone.getServiceState()) == null) {
            return 0;
        }
        return serviceState.getRilDataRadioTechnology();
    }

    public static boolean isSupportLowPowerModeOptimization() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        return HyperOSCustFeatureResolve.getBoolean("config_low_power_mode_optimization_supported", false);
    }

    public static boolean isSupportInitModemBaseModel() {
        return HyperOSCustFeatureResolve.getBoolean("config_init_modem_base_model", false);
    }

    public static boolean isSupportMtkDataOptimization() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_mtk_data_optimization_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportMtkDataOptimization " + e);
            return false;
        }
    }

    public static boolean isSupportCallMessageOptimization() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        return HyperOSCustFeatureResolve.getBoolean("config_call_message_optimization_supported", false);
    }

    public static boolean isSupportQcGameResidentCellOptimization() {
        boolean z;
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        try {
            z = PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_qc_game_resident_cell_opt_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportQcGameResidentCellOptimization " + e);
            z = false;
        }
        return z || isSupportGameResidentCellOptForOS4();
    }

    public static boolean isSupportGameResidentCellOptForOS4() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        return HyperOSCustFeatureResolve.getBoolean("config_game_resident_cell_opt_supported_for_os4", false);
    }

    public static boolean isSupportDfxDataStallStat() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        return HyperOSCustFeatureResolve.getBoolean("config_dfx_data_stall_stat_supported", false);
    }

    public static boolean isSupportModemAiOptimization() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_is_modem_ai_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportModemAiOptimization " + e);
            return false;
        }
    }

    public static boolean isSupportVolteGlobalization() {
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_mtk_volte_globalization);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportVolteGlobalization " + e);
            return false;
        }
    }

    public static boolean isSupportMtkGlobalDataOptimization() {
        boolean z = false;
        try {
            z = HyperOSCustFeatureResolve.getBoolean("config_global_data_opt_v1", false);
            Log.i("MiuiPhoneUtils", "isSupportMtkGlobalDataOptimization isSupport=" + z);
            return z;
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportMtkGlobalDataOptimization " + e);
            return z;
        }
    }

    public static boolean isSupportMtkGlobalDataOptimizationV2() {
        boolean z = false;
        try {
            z = HyperOSCustFeatureResolve.getBoolean("config_global_data_opt_v2", false);
            Log.i("MiuiPhoneUtils", "isSupportMtkGlobalDataOptimizationV2 isSupport=" + z);
            return z;
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportMtkGlobalDataOptimizationV2 " + e);
            return z;
        }
    }

    public static boolean isSupportMtkGlobalCallOptimization() {
        if (Build.IS_INTERNATIONAL_BUILD && ModemProvider.IS_MTK) {
            return HyperOSCustFeatureResolve.getBoolean("config_mtk_global_call_optimization_supported", false);
        }
        return false;
    }

    public static boolean isSupportMtkCallDropStats() {
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_mtk_call_drop_stats_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportMtkCallDropStats " + e);
            return false;
        }
    }

    public static boolean isVolteRegionFeatureEnabled() {
        return Build.IS_INTERNATIONAL_BUILD && SystemProperties.getInt("persist.dbg.volte_avail_ovr", 0) == 1;
    }

    public static boolean isSupportHangUpoptimize3_0() {
        return FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_HNAGUP_OPTIMIZATION_V3);
    }

    public static boolean shouldWakeUpESE() {
        return HyperOSCustFeatureResolve.getBoolean("config_should_wake_up_ese", false);
    }

    public static boolean shouldWakeUpESEonNewPlatform() {
        return HyperOSCustFeatureResolve.getBoolean("config_should_wake_up_ese_on_new_platform", false);
    }

    public static boolean isSupportEuiccBinding() {
        return HyperOSCustFeatureResolve.getBoolean("config_esim_euicc_binding_support", false);
    }

    public static boolean maybeShowDataInUseForViceCard(Phone phone, int i) {
        if ((isChinaOrMacauTelecomSim(phone) || isCmccSim(phone)) && i != 2 && DefaultSimManager.getDefaultDataSlotId() != phone.getPhoneId()) {
            if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
                if (phone.isImsRegistered()) {
                    return true;
                }
            } else if (isSSOperatedByUt(phone)) {
                return true;
            }
        }
        return false;
    }

    public static boolean maybeShowSwitchDDSDialog(Phone phone, int i) {
        if (isCmccLockedMode()) {
            return false;
        }
        if ((isChinaOrMacauTelecomSim(phone) || isCmccSim(phone)) && i != 2 && DefaultSimManager.getDefaultDataSlotId() != phone.getPhoneId()) {
            if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
                if (!phone.isImsRegistered()) {
                    return true;
                }
            } else if (!isSSOperatedByUt(phone)) {
                return true;
            }
        }
        return false;
    }

    public static boolean shouldSwitchToCdmaCallForward(Phone phone, Throwable th) {
        if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
            if (th == null || !(th instanceof CommandException)) {
                return false;
            }
            return ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isUTError((CommandException) th);
        }
        return !isSSOperatedByUt(phone);
    }

    public static boolean useUtForCdmaCallForward(Phone phone) {
        if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && Build.IS_CM_CUSTOMIZATION) {
            return false;
        }
        if (isSSOperatedByUt(phone)) {
            return true;
        }
        return isCTSimInViceSlot(phone) && !isCmccLockedMode();
    }

    public static boolean isCmccLockedMode() {
        return ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDeviceLockViceNetworkModeForCmcc() && SubscriptionManager.INVALID_SUBSCRIPTION_ID != ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getDataSlotForCmcc();
    }

    public static boolean shouldAddCountryCodeForCdmaCallForward(int i) {
        Phone phone = getPhone(i);
        if (phone == null || phone.getServiceState() == null) {
            return false;
        }
        return ServiceProviderUtils.isMacauTelecom(phone.getServiceState().getOperatorNumeric());
    }

    public static void sendEnhanced4GLteModeChangeBroadcast(int i, int i2) {
        String str;
        boolean enhanced4gLteModeSetting;
        if (SubscriptionManager.isRealSlotId(i)) {
            PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
            if (i == 0) {
                str = "miui.intent.action.ACTION_ENHANCED_4G_LTE_MODE_CHANGE_FOR_SLOT1";
            } else {
                str = "miui.intent.action.ACTION_ENHANCED_4G_LTE_MODE_CHANGE_FOR_SLOT2";
            }
            Intent intent = new Intent(str);
            if (i2 == -1) {
                enhanced4gLteModeSetting = ImsAdapterBase.getInstance().getEnhanced4gLteModeSetting(phoneGlobals, i);
            } else {
                enhanced4gLteModeSetting = i2 == 1;
            }
            intent.putExtra("extra_is_enhanced_4g_lte_on", enhanced4gLteModeSetting);
            android.telephony.SubscriptionManager.putPhoneIdAndSubIdExtra(intent, i);
            Log.d("MiuiPhoneUtils", "send ACTION_ENHANCED_4G_LTE_MODE_CHANGE volteEnabled:" + enhanced4gLteModeSetting + " phoneId:" + i);
            ActivityManagerNative.broadcastStickyIntent(intent, "android.permission.READ_PHONE_STATE", -1);
        }
    }

    public static void queryVoNrStateAndSendModeChangeBroadcast(int i) {
        FiveGManagerBase fiveGManagerBase = FiveGManagerBase.getInstance();
        if (fiveGManagerBase != null) {
            fiveGManagerBase.sendMessageQueryVonrState(i);
        }
    }

    public static boolean shouldTurnVolteOnByDefaultAfterOta(Phone phone) {
        if (phone == null) {
            return false;
        }
        return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_volte_on_by_default_after_ota", false);
    }

    public static boolean shouldHideEnhanced4gLteSettings(Context context, Phone phone) {
        if (!DUAL_VOLTE_SUPPORTED) {
            phone = getDataPhone();
        }
        if (phone == null) {
            return false;
        }
        boolean zIsVolteSupportedByDevice = ImsAdapterBase.getInstance().isVolteSupportedByDevice(context);
        boolean z = !Build.IS_INTERNATIONAL_BUILD || isRegionSupportVoLTE(context, Build.getRegion());
        boolean zIsVolteRegionFeatureEnabled = isVolteRegionFeatureEnabled();
        boolean z2 = phone.getPhoneId() == getDcOnlyVirtualSimSlot();
        Log.d("MiuiPhoneUtils", "hideEnhanced4gLteSettings deviceSupported=" + zIsVolteSupportedByDevice + ", regionSupported=" + z + ", isVolteRegionFeatureEnabled=" + zIsVolteRegionFeatureEnabled + ", isDcOnlyVirtualSim=" + z2);
        return !zIsVolteSupportedByDevice || (zIsVolteRegionFeatureEnabled && !z) || (!(zIsVolteRegionFeatureEnabled || isVolteEnabledByPlatform(phone)) || MiuiImsPhoneUtils.hideVolteOptionForOperatorCustimized(phone.getSubId()) || z2);
    }

    public static boolean shouldEnableEnhanced4gLteSettings(Context context, Phone phone) {
        if (!DUAL_VOLTE_SUPPORTED) {
            phone = getDataPhone();
        }
        if (phone == null || Settings.System.getInt(context.getContentResolver(), "airplane_mode_on", -1) == 1) {
            return false;
        }
        boolean zIsVolteEnabledByPlatform = isVolteEnabledByPlatform(phone);
        Log.d("MiuiPhoneUtils", "shouldEnableEnhanced4gLteSettings simSupported=" + zIsVolteEnabledByPlatform);
        if (zIsVolteEnabledByPlatform) {
            SubscriptionInfo subscriptionInfoForSlot = SubscriptionManager.getDefault().getSubscriptionInfoForSlot(phone.getPhoneId());
            if (!isNetworkNotSupportTurnOffVoLTE(phone) && subscriptionInfoForSlot != null && subscriptionInfoForSlot.isActivated()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDeviceSupportCDMA() {
        boolean z;
        try {
            z = PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_support_cdma);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isDeviceSupportCDMA " + e);
            z = true;
        }
        return SystemProperties.getBoolean("persist.vendor.radio.cdma_cap", z);
    }

    public static boolean isDeviceSupport3g2gOnly() {
        return HyperOSCustFeatureResolve.getBoolean("config_support_3gonly2gonly", false);
    }

    public static boolean isDeviceNotSupportCDMA() {
        return !isDeviceSupportCDMA() || Build.IS_INTERNATIONAL_BUILD;
    }

    public static boolean isNetworkNotSupportTurnOffVoLTE(Phone phone) {
        if (phone != null && SubscriptionManager.isValidSubscriptionId(phone.getSubId()) && isEnhanced4gLteModeSettingEnabledByUser(phone)) {
            return !CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "editable_enhanced_4g_lte_bool", true);
        }
        return false;
    }

    public static boolean isEnhanced4gLteModeSettingEnabledByUser(Phone phone) {
        if (phone == null) {
            return false;
        }
        return TelephonyManager.getDefault().isVolteEnabledByUser(phone.getPhoneId());
    }

    public static boolean isEmulatingSinglePartyCallEnabled(Phone phone) {
        if (phone == null) {
            return true;
        }
        return !CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "disable_emulating_single_party_call_bool", false);
    }

    public static boolean isSimReady(int i) {
        return TelephonyManager.getDefault().getSimStateForSlot(i) == 5;
    }

    public static boolean isSimLocked(int i) {
        int simStateForSlot = TelephonyManager.getDefault().getSimStateForSlot(i);
        return simStateForSlot == 2 || simStateForSlot == 3;
    }

    public static boolean isOOSPowerOptimizeSupported() {
        return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_oos_power_optimization) || TelephonyManagerEx.getDefault().isPlatformX350() || TelephonyManagerEx.getDefault().isPlatformX450() || TelephonyManagerEx.getDefault().isPlatformX475() || TelephonyManagerEx.getDefault().isPlatform8550() || TelephonyManagerEx.getDefault().isPlatform7475();
    }

    public static boolean shouldRemoveCdma() {
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_remove_cdma);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "shouldRemoveCdma " + e);
            return false;
        }
    }

    public static boolean shouldDeprioSaDuringPingPongErrorInNightMode() {
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_enable_deprioritize_sa_during_pingpong_in_night_mode);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "shouldDeprioSaDuringPingPongErrorInNightMode " + e);
            return false;
        }
    }

    public static boolean isSupportDatFeature() {
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_enable_dat_feature) && ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).isSupportDynamicAntennaTuningByCloud();
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportDatFeature Exception" + e);
            return false;
        }
    }

    public static boolean isSupportBasementOptimization() {
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_support_basement_optimization);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportBasementOptimization Exception" + e);
            return false;
        }
    }

    public static boolean isModemLimitRilMsgFuncSupported() {
        return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_modem_limit_ril_msg_optimization) || TelephonyManagerEx.getDefault().isPlatformX475() || TelephonyManagerEx.getDefault().isPlatform8550() || TelephonyManagerEx.getDefault().isPlatform7475();
    }

    public static boolean isModemUtilsSupport() {
        PhoneGlobals.getInstance();
        return HyperOSCustFeatureResolve.getBoolean("config_modem_utils_supported", false) || TelephonyManagerEx.getDefault().isPlatformX350() || TelephonyManagerEx.getDefault().isPlatformX450() || TelephonyManagerEx.getDefault().isPlatformX475() || TelephonyManagerEx.getDefault().isPlatform8550() || TelephonyManagerEx.getDefault().isPlatform7475() || isEsimCapable();
    }

    public static boolean isRfExceptionStatsSupported() {
        return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_rf_execption_state_supported) || TelephonyManagerEx.getDefault().isPlatformX475() || TelephonyManagerEx.getDefault().isPlatform8550() || TelephonyManagerEx.getDefault().isPlatformX350() || TelephonyManagerEx.getDefault().isPlatformX450() || TelephonyManagerEx.getDefault().isPlatform7475();
    }

    public static boolean isQcLatency2FeatureEnabled() {
        return HyperOSCustFeatureResolve.getBoolean("config_is_llm_support", false) || SystemProperties.getBoolean("ro.vendor.radio.llm", false) || isQcLowLatencyDelaySupported();
    }

    public static boolean isQcSaNetwork() {
        return ModemProvider.IS_QCOM && is5gSaSupported();
    }

    public static boolean hideVolteUiForH3GCustimized(int i) {
        return hideHDIcon(i);
    }

    public static void hideOOS(String str) {
        Intent intent = new Intent("miui.intent.action.ACTIVE_RE_REGISTRATION_NETWORK");
        intent.putExtra("RE_REGISTRATION_NETWORK_REASON", str);
        PhoneGlobals.getInstance().sendBroadcast(intent);
    }

    public static void hideOOS(String str, int i) {
        Intent intent = new Intent("miui.intent.action.ACTIVE_RE_REGISTRATION_NETWORK");
        intent.putExtra("RE_REGISTRATION_NETWORK_REASON", str);
        intent.putExtra("android.telephony.extra.SLOT_INDEX", i);
        PhoneGlobals.getInstance().sendBroadcast(intent);
    }

    public static boolean hideHDIcon(int i) {
        return !CarrierConfigManagerEx.getBooleanConfigForSubId(PhoneGlobals.getInstance(), i, "display_hd_audio_property_bool", true);
    }

    public static int getPlatformModemFeatureStatus(int i, int i2) {
        byte[] platformModemFeatureStatus = FiveGManagerBase.getInstance().getPlatformModemFeatureStatus(i, 11, i2);
        if (platformModemFeatureStatus != null) {
            return bytesToInt(platformModemFeatureStatus, 0, platformModemFeatureStatus.length);
        }
        return -1;
    }

    public static boolean isViceSlotActivatedAndSupportVolte() {
        for (int i = 0; i < PHONE_COUNT; i++) {
            if (i != DefaultSimManager.getDefaultDataSlotId()) {
                return isIccCardActivated(i) && isVolteEnabledByPlatform(getPhone(i));
            }
        }
        return false;
    }

    public static void sendCdmaCallRealConnectedBroadcast(int i) {
        if (SubscriptionManager.isRealSlotId(i)) {
            PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
            Intent intent = new Intent("miui.intent.action.ACTION_CDMA_CALL_REAL_CONNECTED");
            android.telephony.SubscriptionManager.putPhoneIdAndSubIdExtra(intent, i);
            Log.d("MiuiPhoneUtils", "sendCdmaCallRealConnectedBroadcast: phoneId" + i);
            phoneGlobals.sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.READ_PHONE_STATE");
        }
    }

    public static boolean isPamRequired(int i) {
        if (TelephonyManager.isCustFeatureResolveEnabled()) {
            boolean z = HyperOSCustFeatureResolve.getBooleanForPhoneId(i, "is_pam_required", false) && !TelephonyManagerEx.getDefault().isNetworkRoamingForSlot(i);
            Log.i("MiuiPhoneUtils", "isPamRequired =" + z + ", phoneId=" + i);
            return z;
        }
        String[] strArr = {"23205", "23210", "23420"};
        String simOperator = PhoneProxy.getSimOperator(i);
        for (int i2 = 0; i2 < 3; i2++) {
            String str = strArr[i2];
            if (simOperator != null && simOperator.equals(str) && !TelephonyManagerEx.getDefault().isNetworkRoamingForSlot(i)) {
                Log.i("MiuiPhoneUtils", "isPamRequired numeric=" + str + ", phoneId=" + i);
                return true;
            }
        }
        return false;
    }

    public static boolean isDelRatForManualList(int i) {
        String simOperator = PhoneProxy.getSimOperator(i);
        if (simOperator == null) {
            return false;
        }
        if (!simOperator.equals("22288") && !simOperator.equals("22299") && !simOperator.equals("28405")) {
            return false;
        }
        Log.i("MiuiPhoneUtils", "isDelRatForManualList simNumeric=" + simOperator + ";phoneId=" + i);
        return true;
    }

    public static OperatorInfo delRatForMTK(OperatorInfo operatorInfo) {
        if (!ModemProvider.IS_MTK && !ModemProvider.IS_XRING) {
            return operatorInfo;
        }
        String operatorAlphaLong = operatorInfo.getOperatorAlphaLong();
        String operatorAlphaShort = operatorInfo.getOperatorAlphaShort();
        int iLastIndexOf = operatorAlphaLong.lastIndexOf(" ");
        if (iLastIndexOf > -1) {
            operatorAlphaLong = operatorAlphaLong.substring(0, iLastIndexOf);
        }
        int iLastIndexOf2 = operatorAlphaShort.lastIndexOf(" ");
        if (iLastIndexOf2 > -1) {
            operatorAlphaShort = operatorAlphaShort.substring(0, iLastIndexOf2);
        }
        return new OperatorInfo(operatorAlphaLong, operatorAlphaShort, operatorInfo.getOperatorNumeric(), getOperatorInfoStateStr(operatorInfo.getState()));
    }

    private static String getOperatorInfoStateStr(OperatorInfo.State state) {
        if (OperatorInfo.State.AVAILABLE.equals(state)) {
            return "available";
        }
        if (OperatorInfo.State.CURRENT.equals(state)) {
            return "current";
        }
        if (OperatorInfo.State.FORBIDDEN.equals(state)) {
            return "forbidden";
        }
        OperatorInfo.State.UNKNOWN.equals(state);
        return "unknown";
    }

    public static boolean isVirtualSeedSimBySlot(Context context, int i) {
        String string = Settings.Global.getString(context.getContentResolver(), "virtual_seed_sim_iccid");
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        return string.equals(getIccId(i));
    }

    public static String getVolteEnabledStatus(Context context) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (true) {
            int i2 = PHONE_COUNT;
            if (i < i2) {
                sb.append(ImsAdapterBase.getInstance().getEnhanced4gLteModeSetting(context, i));
                if (i != i2 - 1) {
                    sb.append(",");
                }
                i++;
            } else {
                return sb.toString();
            }
        }
    }

    public static String getVoWiFiEnabledStatus(Context context) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (true) {
            int i2 = PHONE_COUNT;
            if (i < i2) {
                if (isCnOperator(getOperatorNumeric(PhoneFactory.getPhone(i))) && ImsAdapterBase.getInstance().isWfcEnabledByPlatform(context, i)) {
                    sb.append(ImsAdapterBase.getInstance().isWfcEnabledByUser(context, i));
                    if (i != i2 - 1) {
                        sb.append(",");
                    }
                }
                i++;
            } else {
                return sb.toString();
            }
        }
    }

    public static boolean disableSSOverWfcEnableAirplane() {
        int insertedSimCount = getInsertedSimCount();
        if (insertedSimCount <= 1) {
            if (insertedSimCount == 1) {
                return disableSSOverWfcEnableAirplane(getDataPhone());
            }
            return false;
        }
        for (Phone phone : PhoneFactory.getPhones()) {
            if (!disableSSOverWfcEnableAirplane(phone)) {
                return false;
            }
        }
        return true;
    }

    public static boolean disableSSOverWfcEnableAirplane(Phone phone) {
        if (phone == null) {
            return false;
        }
        return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_disable_ss_over_wfc_when_airplane_enabled", false);
    }

    public static boolean isLteNrNwMode(int i) {
        int preferredNwModeFromDb = Utils.getPreferredNwModeFromDb(i);
        return (preferredNwModeFromDb == 0 || preferredNwModeFromDb == 1 || preferredNwModeFromDb == 2 || preferredNwModeFromDb == 3 || preferredNwModeFromDb == 5 || preferredNwModeFromDb == 6 || preferredNwModeFromDb == 7 || preferredNwModeFromDb == 13 || preferredNwModeFromDb == 14 || preferredNwModeFromDb == 16 || preferredNwModeFromDb == 18 || preferredNwModeFromDb == 21) ? false : true;
    }

    public static boolean shouldHideViceSlotVolteDataButton() {
        return (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDeviceLockViceNetworkModeForCmcc() && SubscriptionManager.INVALID_SUBSCRIPTION_ID != ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getDataSlotForCmcc()) || !DUAL_VOLTE_SUPPORTED || ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDisableLte() || isTabletDeviceWithVolteCapability() || getInsertedSimCount() < 2 || !isViceSlotActivatedAndSupportVolte();
    }

    public static boolean shouldShowCallBarring() {
        if (HyperOSCustFeatureResolve.getBoolean("config_remove_call_barring", false) || needRemoveCallBarring()) {
            return false;
        }
        return Build.IS_CM_CUSTOMIZATION || Build.IS_CU_CUSTOMIZATION_TEST || Build.IS_INTERNATIONAL_BUILD;
    }

    public static boolean needRemoveCallBarring() {
        if (getInsertedSimCount() > 1) {
            return (CarrierConfigManagerEx.getBooleanConfigForSubId(getPhone(0).getContext(), getPhone(0).getSubId(), "call_barring_visibility_bool", true) || CarrierConfigManagerEx.getBooleanConfigForSubId(getPhone(1).getContext(), getPhone(1).getSubId(), "call_barring_visibility_bool", true)) ? false : true;
        }
        return !CarrierConfigManagerEx.getBooleanConfigForSubId(getPhone(SubscriptionManager.getDefault().getDefaultSlotId()).getContext(), getPhone(SubscriptionManager.getDefault().getDefaultSlotId()).getSubId(), "call_barring_visibility_bool", true);
    }

    public static boolean isEsVodafoneSim(Phone phone) {
        return "21401".equals(getOperatorNumeric(phone));
    }

    public static boolean isPtVodafoneSim(Phone phone) {
        return "26801".equals(getOperatorNumeric(phone));
    }

    public static boolean isNLVodafoneSim(Phone phone) {
        return "20404".equals(getOperatorNumeric(phone));
    }

    public static boolean isAisSim(Phone phone) {
        return "52003".equals(getOperatorNumeric(phone)) || "52001".equals(getOperatorNumeric(phone));
    }

    public static boolean isShowCallForwardingCFNRc(Phone phone) {
        SubscriptionInfo subscriptionInfoForSlot = SubscriptionManager.getDefault().getSubscriptionInfoForSlot(phone.getPhoneId());
        if (subscriptionInfoForSlot == null || subscriptionInfoForSlot.getIccId() == null || !subscriptionInfoForSlot.getIccId().startsWith("8988605")) {
            return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_call_forwarding_CFNRc_visibility_bool", true);
        }
        return false;
    }

    public static boolean isDeO2Sim(Phone phone) {
        return HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "config_vowifi_promt_supported", false);
    }

    public static boolean isClaroSim(Phone phone) {
        return HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "config_claro_vowifi_promt_supported", false);
    }

    public static boolean isVodacomSouthAfricaSim(Phone phone) {
        return "65501".equals(getOperatorNumeric(phone));
    }

    public static boolean isH3GSeSim(Phone phone) {
        return "24002".equals(getOperatorNumeric(phone));
    }

    public static boolean isH3GItSim(Phone phone) {
        return "22299".equals(getOperatorNumeric(phone)) || "22288".equals(getOperatorNumeric(phone));
    }

    public static boolean is3atH3GSim(Phone phone) {
        return "23205".equals(getOperatorNumeric(phone)) || "23210".equals(getOperatorNumeric(phone)) || "23214".equals(getOperatorNumeric(phone));
    }

    public static boolean isIEVDFSim(Phone phone) {
        return "27201".equals(getOperatorNumeric(phone));
    }

    public static boolean isEEAVDFSim(Phone phone) {
        return "23415".equals(getOperatorNumeric(phone)) || "23003".equals(getOperatorNumeric(phone)) || "21401".equals(getOperatorNumeric(phone)) || "26202".equals(getOperatorNumeric(phone)) || "26209".equals(getOperatorNumeric(phone)) || "26801".equals(getOperatorNumeric(phone)) || "20205".equals(getOperatorNumeric(phone));
    }

    public static boolean isZaMtnSim(Phone phone) {
        return "65510".equals(getOperatorNumeric(phone));
    }

    public static boolean isUkEeSim(Phone phone) {
        return "23430".equals(getOperatorNumeric(phone)) || "23433".equals(getOperatorNumeric(phone));
    }

    public static boolean hideNetmodeMenuAndUseDefaultRat(Phone phone) {
        return phone != null && hideNetmodeMenuAndUseDefaultRat(phone.getPhoneId());
    }

    public static boolean hideNetmodeMenuAndUseDefaultRat(int i) {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return HyperOSCustFeatureResolve.getBooleanForPhoneId(i, "config_hide_network_and_use_default_rat", false);
        }
        return false;
    }

    public static boolean isFiveGCapable() {
        return SystemProperties.getInt("ro.vendor.radio.5g", 0) > 0;
    }

    public static boolean is5gSaSupported() {
        return SystemProperties.getInt("ro.vendor.radio.5g", 0) >= 2;
    }

    public static boolean is5gSaOnlyDevice() {
        return SystemProperties.getInt("ro.vendor.radio.5g", 0) == 2;
    }

    public static boolean isRemoveDual4GSwitch() {
        return isFiveGCapable() || HyperOSCustFeatureResolve.getBoolean("remove_dual_4g_switch", false);
    }

    public static boolean shouldDisable5GSwitchByDefault() {
        return HyperOSCustFeatureResolve.getBoolean("config_disable_5g_switch_by_default", false);
    }

    public static boolean shouldDisableSaSwitchByDefault() {
        return Build.IS_INTERNATIONAL_BUILD;
    }

    public static int getFiveGSwitchType() {
        return Build.IS_INTERNATIONAL_BUILD ? 2 : 3;
    }

    public static boolean showFiveGSwitch(int i) {
        if (isFiveGCapable() && (getFiveGSwitchType() & 1) == 1 && FiveGManagerBase.is5GSwitchCarrierAvailable(i)) {
            return (i == DefaultSimManager.getDefaultDataSlotId() || TelephonyManager.getDefault().isDualNrSupported()) && !FiveGManagerBase.is5GSwitchClosedByCloud();
        }
        return false;
    }

    public static boolean showFiveGNetworkMode(int i) {
        if (isFiveGCapable() && (getFiveGSwitchType() & 2) == 2 && FiveGManagerBase.is5GSwitchCarrierAvailable(i)) {
            return (i == DefaultSimManager.getDefaultDataSlotId() || TelephonyManager.getDefault().isDualNrSupported()) && !FiveGManagerBase.is5GSwitchClosedByCloud();
        }
        return false;
    }

    public static boolean isFiveGSettingViewVisible(int i) {
        return showFiveGSwitch(i) || showFiveGNetworkMode(i);
    }

    public static boolean shouldUseSAoption3forMtk() {
        return TelephonyManagerEx.getDefault().isUseSAoption3forMtk();
    }

    public static boolean showFiveGCarrierAggregation() {
        boolean z;
        try {
            z = PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_is_show_nrca_switch);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "showFiveGCarrierAggregation " + e);
            z = false;
        }
        return TelephonyManager.getDefault().isNrCaSupported() && z;
    }

    public static void setFiveGCarrierAggregationInDb(Context context, int i, int i2) {
        int subId = getPhone(i2).getSubId();
        Settings.Global.putInt(context.getContentResolver(), "button_5G_carrier_aggregation" + subId, i);
        Rlog.i("MiuiPhoneUtils", "setFiveGCarrierAggregationInDb subId = " + subId + ", mode=" + i);
    }

    public static int getFiveGCarrierAggregationFromDb(Context context) {
        int subId = getPhone(DefaultSimManager.getDefaultDataSlotId()).getSubId();
        int i = Settings.Global.getInt(context.getContentResolver(), "button_5G_carrier_aggregation" + subId, -1);
        Rlog.i("MiuiPhoneUtils", "getFiveGCarrierAggregationFromDb rst=" + i);
        return i;
    }

    public static boolean isModemFeatureSupported(int i) {
        if (i == 1) {
            return TelephonyManagerEx.getDefault().isN28Supported();
        }
        if (i == 3) {
            return TelephonyManagerEx.getDefault().isN1Supported();
        }
        if (i == 2) {
            return TelephonyManagerEx.getDefault().isBWPSupported();
        }
        if (i == -1) {
            return TelephonyManagerEx.getDefault().isN5Supported() || TelephonyManagerEx.getDefault().isN8Supported();
        }
        if (i == 15) {
            return ModemProvider.IS_QCOM;
        }
        return false;
    }

    public static void setModemFeatureInDb(Context context, int i, int i2) {
        if (i == 1) {
            Settings.Global.putInt(context.getContentResolver(), "button_n28_mode", i2);
        } else if (i == 3) {
            Settings.Global.putInt(context.getContentResolver(), "button_n1_mode", i2);
        } else if (i == 2) {
            Settings.Global.putInt(context.getContentResolver(), "button_bwp", i2);
        } else if (i == -1) {
            Settings.Global.putInt(context.getContentResolver(), "button_n5_n8_mode", i2);
        }
        Rlog.i("MiuiPhoneUtils", "setModemFeatureInDb featureType = " + i + " , mode = " + i2);
    }

    public static int getModemFeatureFromDb(Context context, int i) {
        int i2 = 0;
        if (i == 1) {
            i2 = Settings.Global.getInt(context.getContentResolver(), "button_n28_mode", 0);
        } else if (i == 3) {
            i2 = Settings.Global.getInt(context.getContentResolver(), "button_n1_mode", 0);
        } else if (i == 2) {
            i2 = Settings.Global.getInt(context.getContentResolver(), "button_bwp", 0);
        } else if (i == -1) {
            i2 = Settings.Global.getInt(context.getContentResolver(), "button_n5_n8_mode", 0);
        }
        Rlog.i("MiuiPhoneUtils", "getModemFeatureFromDb featureType = " + i + " , mode = " + i2);
        return i2;
    }

    public static boolean shouldRadioOffBeforeChangeFiveGState(Phone phone) {
        return HyperOSCustFeatureResolve.getBooleanForPhoneId(phone != null ? phone.getPhoneId() : SubscriptionManager.INVALID_PHONE_ID, "radio_off_before_change_fiveg_state", false);
    }

    public static boolean isDeviceSupportModemRestartDisableEndc() {
        if (isOpTestVersion() || "INDIA".equalsIgnoreCase(SystemProperties.get(ModemSarUtils.PROPERTY_DEVICE_HWC)) || "GLOBAL".equalsIgnoreCase(SystemProperties.get(ModemSarUtils.PROPERTY_DEVICE_HWC))) {
            return false;
        }
        return !Build.IS_INTERNATIONAL_BUILD;
    }

    public static boolean isMimoSceneFiveGOptimizeSupported() {
        return (Build.IS_INTERNATIONAL_BUILD || !TelephonyManagerEx.getDefault().isMimoSupported() || isThermalThrottleSupported() || isSupportPowerSaveV2()) ? false : true;
    }

    public static boolean isGameFiveGOptimizeSupported() {
        boolean z;
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        try {
            z = PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_game_fiveg_optimization_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isGameFiveGOptimizeSupported " + e);
            z = true;
        }
        return isFiveGCapable() && z;
    }

    public static boolean isByA1Sim(Phone phone) {
        return "25701".equals(getOperatorNumeric(phone));
    }

    public static boolean isNovaSim(Phone phone) {
        return "20210".equals(getOperatorNumeric(phone));
    }

    public static boolean isBGA1Sim(Phone phone) {
        return "28401".equals(getOperatorNumeric(phone));
    }

    public static boolean isMTNSim(Phone phone) {
        return "65510".equals(getOperatorNumeric(phone));
    }

    public static boolean supportChinaMobilePalm() {
        return (Build.IS_CM_CUSTOMIZATION || Build.IS_CM_CUSTOMIZATION_TEST) && PhoneGlobals.getInstance().getPackageManager().resolveActivity(new Intent("miui.intent.action.YELLOWPAGE_CHINAMOBILE_PALM"), 0) != null;
    }

    public static boolean hasCmccSim() {
        for (int i = 0; i < PHONE_COUNT; i++) {
            if (isCmccSim(getPhone(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean cannotDisableDual4G() {
        if (isDeviceNotSupportCDMA() && isCTSimInViceSlot()) {
            return true;
        }
        return (isFiveGCapable() && isDualCTSim()) || isKrLguSimInViceSlot();
    }

    public static boolean isDevicesProvisioned(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "device_provisioned", 0) != 0;
    }

    public static boolean needRemoveCallForwardOption() {
        if (TelephonyManager.isCustForJpSb() || TelephonyManager.isCustForJpKd() || isCustForJpRk()) {
            return true;
        }
        if (getInsertedSimCount() > 1) {
            return (CarrierConfigManagerEx.getBooleanConfigForSubId(getPhone(0).getContext(), getPhone(0).getSubId(), "call_forwarding_visibility_bool", true) || CarrierConfigManagerEx.getBooleanConfigForSubId(getPhone(1).getContext(), getPhone(1).getSubId(), "call_forwarding_visibility_bool", true)) ? false : true;
        }
        return !CarrierConfigManagerEx.getBooleanConfigForSubId(PhoneFactory.getDefaultPhone().getContext(), SubscriptionManager.getDefault().getDefaultSubscriptionId(), "call_forwarding_visibility_bool", true);
    }

    public static boolean needRemoveCallWaitingOption() {
        if (TelephonyManager.isCustForJpSb() || isCustForJpRk()) {
            return true;
        }
        if (getInsertedSimCount() > 1) {
            return (CarrierConfigManagerEx.getBooleanConfigForSubId(getPhone(0).getContext(), getPhone(0).getSubId(), "additional_settings_call_waiting_visibility_bool", true) || CarrierConfigManagerEx.getBooleanConfigForSubId(getPhone(1).getContext(), getPhone(1).getSubId(), "additional_settings_call_waiting_visibility_bool", true)) ? false : true;
        }
        return !CarrierConfigManagerEx.getBooleanConfigForSubId(PhoneFactory.getDefaultPhone().getContext(), SubscriptionManager.getDefault().getDefaultSubscriptionId(), "additional_settings_call_waiting_visibility_bool", true);
    }

    public static boolean hideSimCallBack(Context context) {
        if (context == null) {
            Log.i("MiuiPhoneUtils", "Context is null when execute hideSimCallBack!");
            return true;
        }
        if (hasDcOnlyEsim(context)) {
            return true;
        }
        Intent intent = new Intent("miui.intent.action.LAST_SIM_CALLBACK");
        intent.setPackage("com.android.contacts");
        return context.getPackageManager().resolveActivity(intent, 65536) == null;
    }

    public static boolean hidePrefSMSSettings() {
        if (!TelephonyManager.isGoogleCsp()) {
            return true;
        }
        for (Phone phone : PhoneFactory.getPhones()) {
            if (!CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_hide_pref_sms_settings", true)) {
                return false;
            }
        }
        return true;
    }

    public static String printArray(int[][] iArr, String str) {
        if (iArr == null || TextUtils.isEmpty(str)) {
            Log.i("MiuiPhoneUtils", "print array is null");
            return null;
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < iArr.length; i++) {
            int i2 = 0;
            while (true) {
                int[] iArr2 = iArr[i];
                if (i2 >= iArr2.length) {
                    break;
                }
                sb.append(iArr2[i2]);
                if (i2 != iArr[i].length - 1) {
                    sb.append(", ");
                }
                i2++;
            }
            if (i != iArr.length - 1) {
                sb.append("], [");
            } else {
                sb.append("]");
            }
        }
        String string = sb.toString();
        Log.i("MiuiPhoneUtils", str + " : " + string);
        return string;
    }

    public static LinkProperties getMobileLinkProperties(Context context) {
        NetworkCapabilities networkCapabilities;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork == null || (networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)) == null || !networkCapabilities.hasTransport(0)) {
            return null;
        }
        return connectivityManager.getLinkProperties(activeNetwork);
    }

    public static String getMobileIpAddresses(LinkProperties linkProperties) {
        if (linkProperties == null) {
            return null;
        }
        Iterator it = linkProperties.getAddresses().iterator();
        if (!it.hasNext()) {
            return null;
        }
        String strConcat = "";
        while (it.hasNext()) {
            strConcat = strConcat + ((InetAddress) it.next()).getHostAddress();
            if (it.hasNext()) {
                strConcat = strConcat.concat("\n");
            }
        }
        return strConcat;
    }

    public static String getMobileDnsAddresses(LinkProperties linkProperties) {
        if (linkProperties == null) {
            return null;
        }
        Iterator<InetAddress> it = linkProperties.getDnsServers().iterator();
        if (!it.hasNext()) {
            return null;
        }
        String strConcat = "";
        while (it.hasNext()) {
            strConcat = strConcat + it.next();
            if (it.hasNext()) {
                strConcat = strConcat.concat("\n");
            }
        }
        return strConcat;
    }

    public static boolean isMtk5gDevice() {
        return isFiveGCapable() && ModemProvider.IS_MTK;
    }

    public static boolean isIgnoreEmergencyNumber(Phone phone, String str) {
        if (phone == null) {
            Rlog.w("MiuiPhoneUtils", "isIgnoreEmergencyNumber phone is null");
            return false;
        }
        if ("IN".equalsIgnoreCase(getCountryIso(phone.getContext()))) {
            String[] strArr = {"100", "101", "102", "108"};
            for (int i = 0; i < 4; i++) {
                if (strArr[i].equals(str)) {
                    Rlog.d("MiuiPhoneUtils", "isIgnoreEmergencyNumber :number:" + str);
                    return true;
                }
            }
        }
        return false;
    }

    public static String getCountryIso(Context context) {
        Country countryDetectCountry;
        CountryDetector countryDetector = (CountryDetector) context.getSystemService("country_detector");
        String countryIso = (countryDetector == null || (countryDetectCountry = countryDetector.detectCountry()) == null) ? null : countryDetectCountry.getCountryIso();
        if (countryIso != null) {
            return countryIso;
        }
        String country = context.getResources().getConfiguration().locale.getCountry();
        Rlog.w("MiuiPhoneUtils", "No CountryDetector; falling back to countryIso based on locale: " + country);
        return country;
    }

    public static boolean isFiveGHostedModeSupported() {
        boolean z;
        if (Build.IS_INTERNATIONAL_BUILD && !isFivegHostedModeIndia()) {
            return false;
        }
        try {
            z = PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_fiveg_hosted_mode_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isFiveGHostedModeSupported" + e);
            z = true;
        }
        return showFiveGNetworkMode(DefaultSimManager.getDefaultDataSlotId()) && z;
    }

    public static boolean isFivegHostedModeIndia() {
        return HyperOSCustFeatureResolve.getBoolean("config_fiveg_hosted_mode", false);
    }

    public static boolean isFivegHostedModeIndiaDefaultOff() {
        return HyperOSCustFeatureResolve.getBoolean("config_fiveg_hosted_mode_gl_default_off", false);
    }

    public static boolean isSupportMepForQcom() {
        return isSupportMep() && ModemProvider.IS_QCOM;
    }

    public static boolean isNeedDelaySlotMappingTime() {
        return HyperOSCustFeatureResolve.getBoolean("config_need_delay_slot_mapping_time", false);
    }

    public static boolean ShouldSwitchToRemovableSlot() {
        return HyperOSCustFeatureResolve.getBoolean("config_should_switch_to_removable_slot", true);
    }

    public static boolean isSaPowerOptimizationSupported() {
        if (TelephonyManagerEx.getDefault().isSaPowerOptimizationSupported() || isWlanDisSASupportedForMtk()) {
            return !Build.IS_INTERNATIONAL_BUILD || isSaPowerOptimizationSupportedForSKU();
        }
        return false;
    }

    private static boolean isSaPowerOptimizationSupportedForSKU() {
        return (!Build.IS_STABLE_VERSION && HyperOSCustFeatureResolve.getBoolean("support_sa_power_optimization", false)) || HyperOSCustFeatureResolve.getBoolean("support_sa_power_optimization_special_version", false);
    }

    public static boolean isWlanDisSASupportedForMtk() {
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_wlan_disable_sa_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isWlanDisSASupportedForMtk" + e);
            return false;
        }
    }

    public static boolean isDeprioritizeSaNotSupported() {
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_deprioritize_sa_not_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isDeprioritizeSaNotSupported" + e);
            return false;
        }
    }

    public static boolean isCloudCloseWlanDisableSaFeature() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getBoolean("disable_wlan_sa", false);
        }
        return false;
    }

    public static boolean isSupportMDC() {
        return isP21AndBeforeHwLevel() && HyperOSCustFeatureResolve.getBoolean("config_support_mdc", false) && !Build.IS_STABLE_VERSION;
    }

    public static boolean isSupportQcCallEndReason() {
        if (HyperOSCustFeatureResolve.getBoolean("config_is_support_call_end_reason", false)) {
            return true;
        }
        return TelephonyManagerEx.getDefault().isPlatform8850() && !Build.IS_INTERNATIONAL_BUILD && ModemProvider.IS_QCOM;
    }

    public static boolean isSupportQcMDC() {
        return HyperOSCustFeatureResolve.getBoolean("config_is_support_modem_data_collector", false) && ModemProvider.IS_QCOM;
    }

    public static boolean isDualDataSupported() {
        if (!ModemProvider.IS_QCOM) {
            return false;
        }
        if (!Build.IS_INTERNATIONAL_BUILD || IS_INDIA) {
            return TelephonyManagerEx.getDefault().isDualDataSupported();
        }
        return false;
    }

    public static boolean isLicenseDualDataSupported() {
        if (!ModemProvider.IS_QCOM) {
            return false;
        }
        if (!Build.IS_INTERNATIONAL_BUILD || IS_INDIA) {
            return TelephonyManagerEx.getDefault().isLicenseDualDataSupported();
        }
        return false;
    }

    public static boolean isCdmaDataOptimizeSupported() {
        return TelephonyManagerEx.getDefault().isCdmaDataOptimizeSupported() && !isCdmaDataOptimizeDisableByCloud();
    }

    public static boolean isNsaThermalControlSupported() {
        boolean z;
        try {
            z = PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_is_nsa_thermal_control_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isNsaThermalControlSupported" + e);
            z = false;
        }
        return z && HyperOSCustFeatureResolve.getBoolean("support_nsa_thermal_control", false);
    }

    public static boolean isSaThermalControlSupported() {
        boolean z;
        try {
            z = PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_is_sa_thermal_control_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSaThermalControlSupported" + e);
            z = false;
        }
        return TelephonyManagerEx.getDefault().isPlatform8550() || TelephonyManagerEx.getDefault().isPlatform7475() || (!Build.IS_STABLE_VERSION && TelephonyManagerEx.getDefault().isPlatformX475()) || HyperOSCustFeatureResolve.getBoolean("support_sa_thermal_control", false) || z;
    }

    public static boolean isSaPowerWechatVoipSupported() {
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_is_sa_power_wechat_voip_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSaPowerWechatVoipSupported" + e);
            return true;
        }
    }

    public static boolean isThermalThrottleSupported() {
        boolean z = PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_therma_throttle_optimization);
        if (isThermalThrottleSupportedForSKU()) {
            return TelephonyManagerEx.getDefault().isPlatform8550() || isMtkDx1Platform() || z;
        }
        return false;
    }

    public static boolean isThermalThrottleSupportedForSKU() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return HyperOSCustFeatureResolve.getBoolean("support_thermal_throttle", false);
        }
        return true;
    }

    public static boolean isThermalThrottleQmiApiSupported() {
        return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_thermal_throttle_qmi_api_supported);
    }

    public static boolean isThermalThrottleDisableByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getBoolean("thermal_throttle_off", false);
        }
        return false;
    }

    public static boolean shouldDisalbeNsaOptimization() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_disable_nsa_optimization_for_dsda);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "shouldDisalbeNsaOptimization " + e);
            return false;
        }
    }

    public static boolean isCdmaDataOptimizeDisableByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getBoolean("cdma_data_off", false);
        }
        return false;
    }

    public static boolean isSaPowerDisabledBySecretCode() {
        return Settings.System.getInt(PhoneGlobals.getInstance().getContentResolver(), "sa_power_save_mode", -1) == 0;
    }

    public static int bytesToInt(byte[] bArr, int i, int i2) {
        int i3;
        if (i2 > 4) {
            HexString$$ExternalSyntheticBUOutline1.m("length must be <= 4 (only 32-bit integer supported): ", i2);
            return 0;
        }
        if (i < 0 || i2 < 0 || (i3 = i + i2) > bArr.length) {
            throw new IndexOutOfBoundsException("Out of the bounds: src=[" + bArr.length + "], offset=" + i + ", length=" + i2);
        }
        int i4 = i3 - 1;
        int i5 = 0;
        for (int i6 = 0; i6 < i2; i6++) {
            i5 = (i5 << 8) | (bArr[i4 - i6] & TransitionInfo.INIT);
        }
        if (i5 >= 0) {
            return i5;
        }
        HexString$$ExternalSyntheticBUOutline1.m("src cannot be parsed as a positive integer: ", i5);
        return 0;
    }

    public static boolean is5GSaRegistered() {
        for (int i = 0; i < PHONE_COUNT; i++) {
            if (is5GSaRegistered(i)) {
                return true;
            }
        }
        return false;
    }

    public static boolean is5GSaRegistered(int i) {
        Phone phone = getPhone(i);
        return phone != null && phone.getServiceState().getRilDataRadioTechnology() == 20;
    }

    private static boolean isViceSlotEnabled() {
        for (int i = 0; i < PHONE_COUNT; i++) {
            if (i != DefaultSimManager.getDefaultDataSlotId()) {
                return isIccCardActivated(i) && !isSimLocked(i);
            }
        }
        return false;
    }

    public static boolean isDSDASupport() {
        boolean z = ModemProvider.IS_MTK;
        if ((z || ModemProvider.IS_XRING) && isMtkDx1Platform()) {
            return true;
        }
        return (z || ModemProvider.IS_XRING) && TelephonyManagerEx.getDefault().isDsdaSupported();
    }

    public static boolean isDeviceDsdaSupportedByQcom() {
        return ModemProvider.IS_QCOM && TelephonyManagerEx.getDefault().isDsdaSupported();
    }

    public static boolean isSNSV3SupportedByQcom() {
        return ModemProvider.IS_QCOM && (TelephonyManagerEx.getDefault().isPlatform8750() || TelephonyManagerEx.getDefault().isPlatform8850() || HyperOSCustFeatureResolve.getBoolean("config_support_sns_v3", false));
    }

    public static boolean isSNSV4SupportedByQcom() {
        return ModemProvider.IS_QCOM && (TelephonyManagerEx.getDefault().isPlatform8850() || HyperOSCustFeatureResolve.getBoolean("config_support_sns_v4", false));
    }

    public static boolean isSNSV5SupportedByQcom() {
        return ModemProvider.IS_QCOM && HyperOSCustFeatureResolve.getBoolean("config_support_sns_v5", false);
    }

    public static boolean isNrcaModeManagerSupported() {
        return HyperOSCustFeatureResolve.getBoolean("config_support_nrca_mode_manager", false);
    }

    public static boolean shouldHideSmartDualSimButton() {
        TelephonyManager.getDefault();
        return (TelephonyManager.isCustSingleSimDevice() || isTabletDeviceWithVolteCapability()) || (getInsertedSimCount() < 2 || !isViceSlotEnabled()) || !isSmartDualSimSwitchSupported() || (Build.IS_INTERNATIONAL_BUILD && !isCustForInternational());
    }

    public static boolean isCustForInternational() {
        for (Phone phone : PhoneFactory.getPhones()) {
            if (HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "support_smart_dual_sim", false)) {
                return true;
            }
        }
        if (!isSmartDualSimGlobalSupportedByDevice()) {
            return false;
        }
        String str = SystemProperties.get("ro.miui.region", "unknow");
        if (TextUtils.isEmpty(str) || "unknow".equals(str)) {
            return false;
        }
        return ArrayUtils.contains(HyperOSCustFeatureResolve.getStringArray("support_smart_dual_sim_region"), str);
    }

    private static boolean isSmartDualSimGlobalSupportedByDevice() {
        return Build.IS_INTERNATIONAL_BUILD && (HyperOSCustFeatureResolve.getBoolean("config_is_smart_dual_sim_global_support", false) || isPlatformDE50() || isPlatformDE70() || isPlatformDE90());
    }

    public static boolean isSensorSupported(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
        boolean z = sensorManager.getDefaultSensor(9) != null;
        boolean z2 = sensorManager.getDefaultSensor(1) != null;
        Log.d("MiuiPhoneUtils", "isSensorSupported: gravitySensorSupported=" + z + ", accelerometerSensorSupported=" + z2 + ", linearAccSensorSupported=" + (sensorManager.getDefaultSensor(10) != null) + ", gyroscopeSensorSupported=" + (sensorManager.getDefaultSensor(4) != null));
        return z || z2;
    }

    public static boolean isSmartSwitchDualSimSupported() {
        SystemProperties.getInt("ro.miui.ui.version.code", 0);
        return true;
    }

    public static void smartSwitchDualSimDbInit(Context context) {
        if (Settings.Global.getInt(context.getContentResolver(), "smart_dual_sim", 0) == 0 || Settings.Global.getInt(context.getContentResolver(), VICE_SLOT_VOLTE_DATA_ENABLED, 0) == 0) {
            Settings.Global.putInt(context.getContentResolver(), "smart_dual_sim", 0);
            Settings.Global.putInt(context.getContentResolver(), VICE_SLOT_VOLTE_DATA_ENABLED, 0);
        }
    }

    public static void intelligentDualSimDbInit(Context context) {
        if (Settings.Global.getInt(context.getContentResolver(), "smart_dual_sim", 0) == 0 || Settings.Global.getInt(context.getContentResolver(), VICE_SLOT_VOLTE_DATA_ENABLED, 0) == 0 || Settings.Global.getInt(context.getContentResolver(), "dual_mobile_data", 0) == 0) {
            resetIntelligentDualSimDb(context);
        }
    }

    public static void resetIntelligentDualSimDb(Context context) {
        Log.d("MiuiPhoneUtils", "resetIntelligentDualSimDb");
        Settings.Global.putInt(context.getContentResolver(), "smart_dual_sim", 0);
        Settings.Global.putInt(context.getContentResolver(), VICE_SLOT_VOLTE_DATA_ENABLED, 0);
        Settings.Global.putInt(context.getContentResolver(), "dual_mobile_data", 0);
    }

    public static boolean shouldHideIntelligentDualSimButton() {
        return !isDualDataSupported() || !isSmartSwitchDualSimSupported() || shouldHideViceSlotVolteDataButton() || shouldHideSmartDualSimButton();
    }

    private static int getCommonFeatures() {
        return SystemProperties.getInt("ro.vendor.radio.features_common", 0);
    }

    public static boolean isSmartDualSimSwitchSupported() {
        return HyperOSCustFeatureResolve.getBoolean("isSmartDualSimSwitchDevicesSupported", false) || (getCommonFeatures() & 2) != 0;
    }

    public static boolean isQcLowLatencyDelaySupported() {
        return (getCommonFeatures() & 1) != 0;
    }

    public static boolean overMiuiFifteen() {
        return SystemProperties.getInt("ro.miui.ui.version.code", 0) >= 15;
    }

    public static boolean isTrafficControlSupported() {
        return !Build.IS_INTERNATIONAL_BUILD;
    }

    public static boolean isTrafficModeEnabled(Context context) {
        int i = Settings.Global.getInt(context.getContentResolver(), "mobile_tc_user_enable", 1);
        Rlog.i("MiuiPhoneUtils", "isTrafficModeEnabled rst=" + i);
        return i != 0;
    }

    public static void setTrafficModeInDb(Context context, int i) {
        Settings.Global.putInt(context.getContentResolver(), "mobile_tc_user_enable", i);
        Rlog.i("MiuiPhoneUtils", "setTrafficModeInDb mode=" + i);
    }

    public static boolean isSupportFakeCellUsingRilHookNV() {
        if (!ModemProvider.IS_QCOM || Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        return TelephonyManagerEx.getDefault().isPlatformX450() || TelephonyManagerEx.getDefault().isPlatformX475() || TelephonyManagerEx.getDefault().isPlatform7475();
    }

    public static boolean isSpecialNetorkOptimizationSupported() {
        return InternalTelephonyUtils.isSpecialNetorkOptimizationSupported();
    }

    public static boolean isSnsOptimizationSupported() {
        if (ModemProvider.IS_QCOM && (!Build.IS_INTERNATIONAL_BUILD || IS_INDIA || TelephonyManagerEx.getDefault().isPlatform8850())) {
            if (!HyperOSCustFeatureResolve.getBoolean("config_support_modem_qms", false) && !TelephonyManagerEx.getDefault().isQmsSupported()) {
                return false;
            }
            try {
                return Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "qms_switch_status", 1) == 1;
            } catch (Exception e) {
                Log.e("MiuiPhoneUtils", "isSnsOptimizationSupported" + e);
            }
        }
        return false;
    }

    public static boolean isOperatorQosAccelerateSupported() {
        return HyperOSCustFeatureResolve.getBoolean("config_support_operator_qos_accelerate", false);
    }

    public static boolean isSelfStudySupported() {
        boolean z;
        if (Build.IS_INTERNATIONAL_BUILD && ModemProvider.IS_QCOM) {
            return false;
        }
        boolean z2 = HyperOSCustFeatureResolve.getBoolean("config_is_self_study_support", false);
        try {
            z = PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_self_study_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSelfStudySupported " + e);
            z = false;
        }
        return z2 || z;
    }

    public static boolean isPoorNetworkFastRecoverySupported() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_poor_network_fast_recovery_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "config_poor_network_recovery_supported " + e);
            return false;
        }
    }

    public static boolean isResidentCellsOptimizationSupported() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_resident_cells_optimization_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "config_resident_cells_optimization_supported " + e);
            return false;
        }
    }

    public static boolean isHighSpeedTrainSensorSupported() {
        boolean z = PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_high_speed_train_sensors_supported);
        if (Build.IS_INTERNATIONAL_BUILD || !ModemProvider.IS_QCOM) {
            return false;
        }
        return TelephonyManagerEx.getDefault().isPlatformX450() || TelephonyManagerEx.getDefault().isPlatformX475() || TelephonyManagerEx.getDefault().isPlatform8550() || TelephonyManagerEx.getDefault().isPlatform7475() || z;
    }

    public static boolean isHighSpeedTrainPreferFreqSupported() {
        return HyperOSCustFeatureResolve.getBoolean("support_HighPriorityCellSelection_config", false);
    }

    public static boolean isGeoLocationSupported() {
        if (ModemProvider.IS_QCOM && !Build.IS_INTERNATIONAL_BUILD) {
            try {
                return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_geolocation_supported);
            } catch (Exception e) {
                Log.e("MiuiPhoneUtils", "isGeoLocationSupported " + e);
            }
        }
        return false;
    }

    public static boolean isSpecialNetworkModeEnabled(Context context) {
        int i = Settings.Global.getInt(context.getContentResolver(), "special_network_optimization", 1);
        Rlog.i("MiuiPhoneUtils", "isSpecialNetworkModeEnabled rst=" + i);
        return i != 0;
    }

    public static void setSpecialNetworkModeInDb(Context context, int i) {
        Settings.Global.putInt(context.getContentResolver(), "special_network_optimization", i);
        Rlog.i("MiuiPhoneUtils", "setSpecialNetworkModeInDb mode=" + i);
    }

    public static void initTrafficControlDefaultData() {
        Rlog.i("MiuiPhoneUtils", "initTrafficControlDefaultData");
        List featureConfig = getFeatureConfig("traffic_control_white_list_default");
        Rlog.i("MiuiPhoneUtils", "initTrafficControlDefaultData whitelist=" + featureConfig);
        saveListToDb(new HashSet(featureConfig), "mobile_tc_white_list_pkg_name");
        List featureConfig2 = getFeatureConfig("traffic_control_enabled_list_default");
        Rlog.i("MiuiPhoneUtils", "initTrafficControlDefaultData enabledlist=" + featureConfig2);
        saveListToDb(new HashSet(featureConfig2), "mobile_tc_enabled_list_pkg_name");
    }

    public static boolean isWifiConnected() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) PhoneGlobals.getInstance().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.getType() == 1;
    }

    public static boolean isWifiEnabled() {
        WifiManager wifiManager = (WifiManager) PhoneGlobals.getInstance().getSystemService("wifi");
        return wifiManager != null && wifiManager.getWifiState() == 3;
    }

    public static boolean isMobileDataConnected() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) PhoneGlobals.getInstance().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.getType() == 0;
    }

    public static Phone getActiveDataPhone() {
        int activeDataSubscriptionId = android.telephony.SubscriptionManager.getActiveDataSubscriptionId();
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone.getSubId() == activeDataSubscriptionId) {
                return phone;
            }
        }
        Rlog.w("MiuiPhoneUtils", "phone not initialized");
        return null;
    }

    public static boolean isQcGwsdSupport() {
        return TelephonyManager.isViceImsAutoRejectSupport();
    }

    public static boolean isGwsdSupport() {
        if (ModemProvider.IS_QCOM) {
            return isQcGwsdSupport();
        }
        return TelephonyManager.getDefault().isGwsdSupport();
    }

    public static void setHealthStatusInDb(Context context, int i) {
        Settings.Global.putInt(context.getContentResolver(), "support_health_in_emergency_dialer", i);
        Rlog.i("MiuiPhoneUtils", "setHealthStatusInDb status=" + i);
    }

    public static boolean isLabTestOn() {
        return SystemProperties.getBoolean("persist.sys.labtest_flag", false);
    }

    public static boolean isDeviceSupportFold() {
        return MiuiMultiDisplayTypeInfo.isFoldDevice();
    }

    public static boolean isDeviceSupportFlip() {
        return MiuiMultiDisplayTypeInfo.isFlipDevice();
    }

    public static boolean isPad() {
        return Build.IS_TABLET && !isDeviceSupportFold();
    }

    public static boolean isSupportSplit() {
        if (UserHandle.myUserId() != 0) {
            return false;
        }
        try {
            Context phoneGlobals = PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication();
            EnvStateManager.markEnvStateDirty(phoneGlobals);
            boolean zIsWideScreen = DeviceHelper.isWideScreen(phoneGlobals);
            Log.d("MiuiPhoneUtils", "isLargeScreen: " + zIsWideScreen);
            if (Build.IS_TABLET) {
                return true;
            }
            return isDeviceSupportFold() && zIsWideScreen;
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportSplit Exception" + e);
            return false;
        }
    }

    public static boolean isTrVersion() {
        return "tr".equalsIgnoreCase(SystemProperties.get("ro.miui.build.region"));
    }

    public static boolean isCustForZaMt() {
        TelephonyManager.getDefault();
        return TelephonyManager.isCustForMTN();
    }

    public static boolean isCustForJpRk() {
        return "jp_rk".equals(CUSTOMIZED_REGION);
    }

    public static boolean shouldUseCnapPresentation(Phone phone) {
        if (phone == null) {
            return false;
        }
        return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_use_cnap_presentation", false);
    }

    public static boolean isDuringCall() {
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone.getState() != PhoneConstants.State.IDLE) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDualSimDuringCall() {
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone == null || phone.getState() == PhoneConstants.State.IDLE) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSatelliteDuringCall(Context context) {
        Phone ntnPhone = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getNtnPhone(context);
        return (ntnPhone == null || ntnPhone.getState() == PhoneConstants.State.IDLE) ? false : true;
    }

    public static boolean isViceSlotPhoneInCall() {
        Phone[] phones = PhoneFactory.getPhones();
        for (int i = 0; i < phones.length; i++) {
            Phone phone = phones[i];
            if (phone != null && phone.getPhoneId() != DefaultSimManager.getDefaultDataSlotId() && phones[i].getState() != PhoneConstants.State.IDLE) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidatePhoneId(int i) {
        return i >= 0 && i < PHONE_COUNT;
    }

    public static int getActiveDataPhoneId() {
        int activeDataSubscriptionId = android.telephony.SubscriptionManager.getActiveDataSubscriptionId();
        if (!android.telephony.SubscriptionManager.isValidSubscriptionId(activeDataSubscriptionId)) {
            return 0;
        }
        int phoneId = android.telephony.SubscriptionManager.getPhoneId(activeDataSubscriptionId);
        if (isValidatePhoneId(phoneId)) {
            return phoneId;
        }
        return 0;
    }

    public static boolean isMobileDataEnabled(Context context) {
        android.telephony.TelephonyManager telephonyManagerCreateForSubscriptionId;
        android.telephony.TelephonyManager telephonyManager = (android.telephony.TelephonyManager) context.getSystemService("phone");
        if (telephonyManager == null || (telephonyManagerCreateForSubscriptionId = telephonyManager.createForSubscriptionId(android.telephony.SubscriptionManager.getDefaultDataSubscriptionId())) == null) {
            return false;
        }
        return telephonyManagerCreateForSubscriptionId.isDataEnabled();
    }

    public static boolean isMobileDataConnected(int i) {
        Phone phone = getPhone(i);
        return (phone == null || phone.getDataNetworkController() == null || phone.getDataNetworkController().getInternetDataNetworkState() != 2) ? false : true;
    }

    public static String maskSensitive(String str, int i) {
        if (str == null || str.length() <= i || PhoneDebug.VDBG) {
            return str;
        }
        int length = str.length() - i;
        return "*".repeat(length) + str.substring(length);
    }

    public static boolean isNsaConnected(int i) {
        ServiceState serviceState;
        Phone phone = getPhone(i);
        return phone != null && (serviceState = phone.getServiceState()) != null && serviceState.getRilDataRadioTechnology() == 14 && serviceState.getNrState() == 3;
    }

    public static int getNrRsrp() {
        SignalStrength signalStrength = PhoneFactory.getPhone(DefaultSimManager.getDefaultDataSlotId()).getSignalStrength();
        int ssRsrp = 0;
        if (signalStrength != null) {
            for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
                if (cellSignalStrength instanceof CellSignalStrengthNr) {
                    ssRsrp = ((CellSignalStrengthNr) cellSignalStrength).getSsRsrp();
                }
            }
        }
        return ssRsrp;
    }

    public static void showArrearsSimDialog(int i) {
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).showArrearsSimDialog(i);
    }

    public static void showNoFiveGDialog(int i) {
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).showNoFiveGDialog(i);
    }

    public static void reportTelephonyStatistic() {
        ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).reportTelephonyStatistic();
    }

    public static boolean supportCallForwardTimeDialog(Phone phone) {
        return phone != null && CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_support_call_forward_time_dialog", false);
    }

    public static boolean isSupportedSmartMultinetworkAcceleration() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_smart_multinetwork_acceleratation_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportedSmartMultinetworkAcceleration " + e);
            return false;
        }
    }

    public static boolean isRemoveFdn() {
        return MiuiJpOperatorCustimizedUtils.isCustForJpKd();
    }

    public static boolean canSetSmscAddress(int i) {
        Phone phone = getPhone(i);
        boolean z = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getPhoneType(i) != 2;
        if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isRemoveCdma(i) && isChinaTelecomSim(getPhone(i))) {
            z = false;
        }
        if (!z && (!Build.IS_CM_CUSTOMIZATION_TEST ? !(!Build.IS_CT_CUSTOMIZATION_TEST || !TelephonyManagerEx.getDefault().isNetworkRoamingForSlot(i)) : Utils.isCdmaLte(TelephonyManager.getDefault().getNetworkTypeForSlot(i)))) {
            z = true;
        }
        return z && isSmscAddressEnable(phone);
    }

    public static boolean isSmscAddressEnable(Phone phone) {
        boolean booleanForPhoneId;
        if (TelephonyManager.isCustFeatureResolveEnabled()) {
            booleanForPhoneId = HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "disable_smsc_setting_update", false);
        } else {
            booleanForPhoneId = !(phone == null || phone.getIccCard() == null || phone.getIccCard().getIccRecords() == null || !"20820".equals(phone.getIccCard().getIccRecords().getOperatorNumeric())) || TelephonyManager.isCustForMxTelcel() || TelephonyManager.isCustForHkH3g() || TelephonyManager.isCustForLmClaro() || TelephonyManager.isCustForClEntel() || TelephonyManager.isCustForBYG();
        }
        return !booleanForPhoneId;
    }

    public static boolean isImsHighPrioritySupported() {
        return ModemProvider.IS_QCOM && (HyperOSCustFeatureResolve.getBoolean("config_is_ims_high_priority_support", false) || TelephonyManagerEx.getDefault().isPlatform8550() || TelephonyManagerEx.getDefault().isPlatform7475() || TelephonyManagerEx.getDefault().isPlatformX475() || TelephonyManagerEx.getDefault().isPlatformX450() || TelephonyManagerEx.getDefault().isPlatformX350() || TelephonyManagerEx.getDefault().isPlatformX250() || TelephonyManagerEx.getDefault().isPlatform8635() || TelephonyManagerEx.getDefault().isPlatform8650() || TelephonyManagerEx.getDefault().isPlatform8750() || TelephonyManagerEx.getDefault().isPlatform8850() || PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_ims_high_prop_supported));
    }

    public static String getSimOperatorNumeric() {
        return android.telephony.TelephonyManager.getTelephonyProperty("gsm.sim.operator.numeric", "");
    }

    public static String getSimOperatorNumeric(int i) {
        return isValidatePhoneId(i) ? android.telephony.TelephonyManager.getTelephonyProperty(i, "gsm.sim.operator.numeric", "") : "";
    }

    public static boolean isModemComponentExceptionSupported() {
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_modem_component_exception_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isModemComponentExceptionSupported " + e);
            return false;
        }
    }

    public static boolean shouldSkipDeprioSaExceptThermal() {
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_skip_deprioritize_sa_except_thermal_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "shouldSkipDeprioSaExceptThermal" + e);
            return false;
        }
    }

    public static boolean isInternationalStatsSupported() {
        return TelephonyManagerEx.getDefault().isInternationalStatsSupported();
    }

    public static boolean isModemHydraStatSupported() {
        boolean z;
        try {
            z = PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_modem_hydra_stat_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isModemHydraStatSupported " + e);
            z = false;
        }
        return z || TelephonyManagerEx.getDefault().isPlatformX450() || TelephonyManagerEx.getDefault().isPlatformX475() || TelephonyManagerEx.getDefault().isPlatform8550() || TelephonyManagerEx.getDefault().isPlatform7475();
    }

    public static boolean isSpeedWarningFeatureSupported() {
        return ModemProvider.IS_QCOM && !Build.IS_INTERNATIONAL_BUILD;
    }

    public static boolean isMtkDx1Platform() {
        return HyperOSCustFeatureResolve.getBoolean("config_is_mtk_dx1_platform", false);
    }

    public static boolean isUseENVBSE() {
        return HyperOSCustFeatureResolve.getBoolean("config_is_use_envbse", false);
    }

    public static boolean isDataRegistered(int i) {
        Phone phone;
        ServiceState serviceState;
        return isValidatePhoneId(i) && (phone = PhoneFactory.getPhone(i)) != null && (serviceState = phone.getServiceState()) != null && serviceState.getDataRegistrationState() == 0;
    }

    public static boolean isWifiCallHelperSupported(Phone phone) {
        if (phone == null) {
            return false;
        }
        return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_wfc_disable_sa", false);
    }

    public static boolean isFastDormancySupported() {
        return SystemProperties.getBoolean("ro.vendor.radio.fastdormancy", false);
    }

    public static boolean isSupportGoogleLPAEsim() {
        return EsimManager.getInstance() != null && EsimManager.getInstance().isSupportEsim();
    }

    public static boolean isEsimCapable() {
        return IS_ESIM_CAPABLE;
    }

    public static boolean isProjectSupportEsim() {
        return SystemProperties.getBoolean("ro.vendor.miui.support_esim", false);
    }

    public static boolean shouldSkipGlpa() {
        return ((isEsimCapable() && (Settings.Secure.getInt(PhoneGlobals.getInstance().getContentResolver(), "is_enable_esim_for_user", -1) == 1) && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isSupportForRegion()) || (Settings.Secure.getInt(PhoneGlobals.getInstance().getContentResolver(), "device_provisioned", 0) == 1)) ? false : true;
    }

    public static int getNetworkType(Context context) {
        NetworkInfo activeNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || !activeNetworkInfo.isAvailable()) {
            return -1;
        }
        return activeNetworkInfo.getType();
    }

    public static boolean isARDOptimizationSupported() {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        boolean z = HyperOSCustFeatureResolve.getBoolean("is_ard_optimization_supported", false);
        boolean z2 = phoneGlobals.getResources().getBoolean(R.bool.config_ard_optimization);
        if (Build.IS_INTERNATIONAL_BUILD) {
            z2 = z2 && TelephonyManagerEx.getDefault().isPlatform8750();
        }
        return z2 || z;
    }

    public static boolean isARDOptimizationDisabledByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getBoolean("ard_optmize_off", false);
        }
        return false;
    }

    public static boolean isUlMimoOptimizationEnabled() {
        return (Build.IS_INTERNATIONAL_BUILD || !isUlMimoOptimizationSupported() || isUlMimoOptimizationDisabledByCloud()) ? false : true;
    }

    public static boolean isUlMimoOptimizationSupported() {
        return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_is_ul_mimo_optimization_supported);
    }

    public static boolean isUlMimoOptimizationDisabledByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getBoolean("ul_mimo_optmize_off", false);
        }
        return false;
    }

    public static boolean isAirportOptimizationSupported() {
        return (TelephonyManagerEx.getDefault().isAirportOptimizationSupported() || HyperOSCustFeatureResolve.getBoolean("config_support_modem_airport", false)) && isSpecialNetorkOptimizationSupported();
    }

    public static boolean isSubwayOptimizationSupported() {
        return TelephonyManagerEx.getDefault().isSubwayOptimizationSupported() && isSpecialNetorkOptimizationSupported();
    }

    public static boolean isHikingOptimizationSupported() {
        return TelephonyManagerEx.getDefault().isHikingOptimizationSupported() && isSpecialNetorkOptimizationSupported();
    }

    public static boolean isSignalMapOptimizationSupported() {
        return TelephonyManagerEx.getDefault().isSignalMapOptimizationSupported() && isSpecialNetorkOptimizationSupported();
    }

    public static boolean isDriveOptimizationSupported() {
        return (TelephonyManagerEx.getDefault().isDriveOptimizationSupported() || HyperOSCustFeatureResolve.getBoolean("config_support_modem_drive", false)) && isSpecialNetorkOptimizationSupported();
    }

    public static boolean isSignalMapNOMLSupported() {
        return !Build.IS_INTERNATIONAL_BUILD && TelephonyManagerEx.getDefault().isSignalMapNOMLSupported();
    }

    public static boolean isAiInfoSupported() {
        return !Build.IS_STABLE_VERSION && SystemProperties.getInt("persist.radio.ai.info", 0) == 1;
    }

    public static boolean isPlatformDE30() {
        return ModemProvider.IS_QCOM && TelephonyManagerEx.getDefault().isPlatform8550();
    }

    public static boolean isSendATcommand() {
        return ModemProvider.IS_MTK && "eea".equalsIgnoreCase(SystemProperties.get("ro.miui.build.region"));
    }

    public static boolean isPlatformDE50() {
        if (ModemProvider.IS_QCOM) {
            return TelephonyManagerEx.getDefault().isPlatform8650() || TelephonyManagerEx.getDefault().isPlatform8635();
        }
        return false;
    }

    public static boolean isPlatformDE70() {
        if (ModemProvider.IS_QCOM) {
            return TelephonyManagerEx.getDefault().isPlatform8750() || TelephonyManagerEx.getDefault().isPlatform8735();
        }
        return false;
    }

    public static boolean isPlatformDE90() {
        return ModemProvider.IS_QCOM && TelephonyManagerEx.getDefault().isPlatform8850();
    }

    public static boolean isSupportLowLatencyAidlService() {
        return TelephonyManagerEx.getDefault().isSupportLowLatencyAidlService();
    }

    public static boolean isSupportFakeCellV2() {
        return TelephonyManagerEx.getDefault().isSupportFakeCellV2();
    }

    public static boolean isSupportFakeCellV1() {
        return TelephonyManagerEx.getDefault().isSupportFakeCellV1();
    }

    public static boolean isLimitLTECAForNightModeSupported() {
        return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_limit_lteca_for_night_optimization) || TelephonyManagerEx.getDefault().isPlatform8550() || TelephonyManagerEx.getDefault().isPlatform7475();
    }

    public static boolean isLimitLTECAForNightModeDisabledByCloud() {
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            return cloudData.getBoolean("limit_lteca_for_night_off", false);
        }
        return false;
    }

    public static boolean isScreenOn(Context context) {
        Display[] displays = ((DisplayManager) context.getSystemService("display")).getDisplays();
        if (displays == null) {
            Log.d("MiuiPhoneUtils", "no displays found");
            return false;
        }
        for (Display display : displays) {
            if (display.getState() == 2) {
                Log.d("MiuiPhoneUtils", "screen on for display = " + display);
                return true;
            }
        }
        Log.d("MiuiPhoneUtils", "screens all off");
        return false;
    }

    public static boolean isScreenOff(Context context) {
        return ((DisplayManager) context.getSystemService("display")).getDisplay(0).getState() == 1;
    }

    public static boolean isSupportDisableCrbt() {
        return FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_DISABLE_CRBT);
    }

    public static boolean callBarringShouldUseVoiceClass(Phone phone) {
        boolean booleanForPhoneId = HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "use_voice_class_for_cb", false);
        Log.d("MiuiPhoneUtils", "callBarringShouldUseVoiceClass: useVoiceClassForCb=" + booleanForPhoneId);
        return booleanForPhoneId;
    }

    public static String getModemSleepCount(String str) {
        String str2 = SystemProperties.get(str, "0");
        SystemProperties.set(str, "0");
        return str2;
    }

    public static boolean maybeShowNoCallServiceToast(Context context, Phone phone) {
        ServiceState serviceState = phone.getServiceState();
        if (new DeviceState().isAirplaneModeOn(context) || serviceState == null) {
            return false;
        }
        boolean z = (HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "show_no_call_service_toast", false) || (ServiceProviderUtils.isChinaTelecom(getOperatorNumeric(phone)) && serviceState.getRilVoiceRadioTechnology() != 6)) && serviceState.getDataRegState() == 0 && !phone.isImsRegistered() && !serviceState.getVoiceRoaming();
        Log.d("MiuiPhoneUtils", "maybeShowNoCallServiceToast shouldShowToast " + z);
        if (z) {
            Toast.makeText(context, R.string.no_voice_capability, 0).show();
        }
        return z;
    }

    public static boolean isModemLogUploadSupported() {
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_is_modem_log_upload_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isModemLogUploadSupported" + e);
            return false;
        }
    }

    public static boolean isSupportIratPingpongOptimization() {
        try {
            return (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getResources().getBoolean(R.bool.config_irat_pingpong_optimization_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportIratPingpongOptimization Exception" + e);
            return false;
        }
    }

    public static boolean isSupportTieredPowerSaving() {
        if (Build.IS_INTERNATIONAL_BUILD || !((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).isSupportPowerSavingOptByCloud()) {
            return false;
        }
        try {
            return (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getResources().getBoolean(R.bool.config_smart_tiered_powersaving_optimization_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportTieredPowerSaving Exception" + e);
            return false;
        }
    }

    public static boolean isSupportBadCellOptimization() {
        if (Build.IS_INTERNATIONAL_BUILD || ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).isSupportBadCellOptByCloud()) {
            return false;
        }
        try {
            return (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getResources().getBoolean(R.bool.config_bad_cell_optimization_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportBadCellOptimization Exception" + e);
            return false;
        }
    }

    public static boolean isSupportMtnLocked() {
        if (isCustForZaMt()) {
            return ModemProvider.IS_MTK || ModemProvider.IS_XRING;
        }
        return false;
    }

    public static boolean isOpTestVersion() {
        return Build.IS_CM_CUSTOMIZATION_TEST || Build.IS_CU_CUSTOMIZATION_TEST || Build.IS_CT_CUSTOMIZATION_TEST;
    }

    public static boolean isMtkForceSearchNrSupported() {
        boolean z;
        try {
            z = PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_is_mtk_force_search_nr_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isMtkForceSearchNrSupported" + e);
            z = false;
        }
        return (ModemProvider.IS_MTK || ModemProvider.IS_XRING) && z;
    }

    public static boolean isSupportSNANetworkRecovery() {
        try {
            return (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getResources().getBoolean(R.bool.config_sna_network_recovery_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportSNANetworkRecovery Exception" + e);
            return false;
        }
    }

    public static boolean isSupportProfessionModeOptimization() {
        try {
            return (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getResources().getBoolean(R.bool.config_profession_mode_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportProfessionModeOptimization Exception" + e);
            return false;
        }
    }

    public static boolean isNotSupportUnfoldCloseCameraFeature() {
        return HyperOSCustFeatureResolve.getBoolean("unsupport_unfold_close_camera_feature", false);
    }

    public static boolean isSupportSecretCodeForSA() {
        for (int i = 0; i < PHONE_COUNT; i++) {
            if (getIccRecordsLoaded(i) && isSupportSecretCodeForSA(i)) {
                Log.i("MiuiPhoneUtils", "isSupportSecretCodeForSA: SecretCode is supported!");
                return true;
            }
        }
        Log.i("MiuiPhoneUtils", "isSupportSecretCodeForSA: SecretCode is not supported!");
        return false;
    }

    public static boolean isSupportSecretCodeForSA(int i) {
        Phone phone = getPhone(i);
        if (phone == null) {
            return false;
        }
        if (!Build.IS_STABLE_VERSION) {
            Log.i("MiuiPhoneUtils", "isSupportSecretCodeForSA: SecretCode is supported due to non-stable SW");
            return true;
        }
        return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_support_secret_code_for_sa_switch", true);
    }

    public static boolean isSupportSecretCodeForSAValue() {
        for (int i = 0; i < PHONE_COUNT; i++) {
            if (getIccRecordsLoaded(i) && isSupportSecretCodeForSAValue(i)) {
                Log.i("MiuiPhoneUtils", "isSupportSecretCodeForSAValue: SecretCode is supported!");
                return true;
            }
        }
        Log.i("MiuiPhoneUtils", "isSupportSecretCodeForSAValue: SecretCode is not supported!");
        return false;
    }

    public static boolean isSupportSecretCodeForSAValue(int i) {
        Phone phone = getPhone(i);
        if (phone == null) {
            return false;
        }
        if (Build.IS_STABLE_VERSION) {
            return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_support_secret_code_for_sa_value", true);
        }
        return true;
    }

    public static boolean isSupportSecretCodeForVoNR() {
        for (int i = 0; i < PHONE_COUNT; i++) {
            if (getIccRecordsLoaded(i) && isSupportSecretCodeForVoNR(i)) {
                Log.i("MiuiPhoneUtils", "isSupportSecretCodeForVoNR: SecretCode is supported!");
                return true;
            }
        }
        Log.i("MiuiPhoneUtils", "isSupportSecretCodeForVoNR: SecretCode is not supported!");
        return false;
    }

    public static boolean isSupportSecretCodeForVoNR(int i) {
        Phone phone = getPhone(i);
        if (phone == null) {
            return false;
        }
        if (!Build.IS_STABLE_VERSION) {
            Log.i("MiuiPhoneUtils", "isSupportSecretCodeForVoNR: SecretCode is supported due to non-stable SW");
            return true;
        }
        return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_support_secret_code_for_vonr_switch", true);
    }

    public static String getCdmaImsi(int i) {
        String simIMSI = getSimIMSI(i, IccCardApplicationStatus.AppType.APPTYPE_RUIM);
        return TextUtils.isEmpty(simIMSI) ? getSimIMSI(i, IccCardApplicationStatus.AppType.APPTYPE_CSIM) : simIMSI;
    }

    private static String getSimIMSI(int i, IccCardApplicationStatus.AppType appType) {
        UiccCardApplication validApp = getValidApp(appType, getUiccProfile(i));
        if (validApp != null) {
            return validApp.getIccRecords().getIMSI();
        }
        return null;
    }

    private static UiccCardApplication getValidApp(IccCardApplicationStatus.AppType appType, UiccProfile uiccProfile) {
        int numApplications = uiccProfile != null ? uiccProfile.getNumApplications() : 0;
        for (int i = 0; i < numApplications; i++) {
            UiccCardApplication applicationIndex = uiccProfile.getApplicationIndex(i);
            if (applicationIndex != null && applicationIndex.getType() != IccCardApplicationStatus.AppType.APPTYPE_UNKNOWN && applicationIndex.getType() == appType) {
                return applicationIndex;
            }
        }
        return null;
    }

    private static UiccProfile getUiccProfile(int i) {
        if (!TelephonyManager.getDefault().isMultiSimEnabled()) {
            return UiccController.getInstance().getUiccProfileForPhone(0);
        }
        return UiccController.getInstance().getUiccProfileForPhone(i);
    }

    public static String getCellId(int i) {
        CellIdentity currentCellIdentity;
        Phone phone = PhoneFactory.getPhone(i);
        UiccCard uiccCard = getUiccCard(i);
        if (TelephonyManager.getDefault().getSimStateForSlot(i) != 5) {
            Log.d("AutoReg_Utils", "slotId: " + i + " sim not ready");
            return "";
        }
        if (uiccCard == null || phone == null) {
            return "";
        }
        String lteCellID = getLteCellID(phone);
        if ((lteCellID != null && !lteCellID.equals("-1") && !lteCellID.equals("0")) || (currentCellIdentity = phone.getCurrentCellIdentity()) == null) {
            return lteCellID;
        }
        CellLocation cellLocationAsCellLocation = currentCellIdentity.asCellLocation();
        if (cellLocationAsCellLocation instanceof GsmCellLocation) {
            return String.valueOf(((GsmCellLocation) cellLocationAsCellLocation).getCid());
        }
        return cellLocationAsCellLocation instanceof CdmaCellLocation ? String.valueOf(((CdmaCellLocation) cellLocationAsCellLocation).getBaseStationId()) : lteCellID;
    }

    public static int getPciByPhoneId(int i) {
        CellIdentity currentCellIdentity = PhoneFactory.getPhone(i).getCurrentCellIdentity();
        if (currentCellIdentity == null) {
            return 0;
        }
        if (currentCellIdentity instanceof CellIdentityLte) {
            return ((CellIdentityLte) currentCellIdentity).getPci();
        }
        if (currentCellIdentity instanceof CellIdentityNr) {
            return ((CellIdentityNr) currentCellIdentity).getPci();
        }
        return 0;
    }

    public static Bundle getPhoneStatus(int i, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putString("fivegEnable", String.valueOf(TelephonyManager.getDefault().isUserFiveGEnabled(i)));
        bundle.putString("saEnable", String.valueOf(TelephonyManager.getDefault().isUserFiveGSaEnabled(i)));
        bundle.putString("volteEnable", String.valueOf(TelephonyManager.getDefault().isVolteEnabledByUser(i)));
        bundle.putString("prefNetMode", String.valueOf(Utils.getPreferredNwModeFromDb(i)));
        bundle.putString("fivegOptimizeScene", String.valueOf(((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getSaOptimizeScene()));
        bundle.putString("fivegOptimizeSceneSwitchCount", String.valueOf(String.valueOf(((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getSwitchSceneCount(i))));
        Bundle cloudData = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getCloudData();
        if (cloudData != null) {
            bundle.putString("p", cloudData.getString("province"));
            bundle.putString("c", cloudData.getString("city"));
        }
        if (isSpecialNetorkOptimizationSupported()) {
            Bundle moduleInfos = ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getModuleInfos();
            boolean z2 = moduleInfos != null ? moduleInfos.getBoolean("is_in_subway") : false;
            Bundle moduleInfos2 = ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).getModuleInfos();
            boolean z3 = moduleInfos2 != null ? moduleInfos2.getBoolean("in_elevator") : false;
            if (((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isHighSpeedTrain()) {
                bundle.putString("ueNetworkScene", "isHighSpeedTrain");
            } else if (z2) {
                bundle.putString("ueNetworkScene", "isInSubway");
            } else if (z3) {
                bundle.putString("ueNetworkScene", "isInElevator");
            } else {
                bundle.putString("ueNetworkScene", "null");
            }
        } else {
            bundle.putString("ueNetworkScene", "null");
        }
        bundle.putString("imsReg", String.valueOf(TelephonyManager.getDefault().isImsRegistered(i)));
        bundle.putString("registeredCellInfo", getRegisteredCellInfo(i));
        if (!z) {
            bundle.putString("noSaInfo", ((IModemModule) ModuleDelegate.get(IModemModule.class)).processUeNoSaInfo(i));
        }
        return bundle;
    }

    private static String getRegisteredCellInfo(int i) {
        long ci;
        int tac;
        String str;
        int rsrp;
        String str2;
        int i2;
        Phone phone = getPhone(i);
        String str3 = "";
        if (phone == null) {
            return "";
        }
        List<CellInfo> allCellInfo = phone.getAllCellInfo();
        if (allCellInfo == null) {
            Log.i("MiuiPhoneUtils", "getRegisteredCellInfo cellInfos is null");
            return "";
        }
        for (CellInfo cellInfo : allCellInfo) {
            if (cellInfo != null && cellInfo.isRegistered()) {
                CellIdentity cellIdentity = cellInfo.getCellIdentity();
                if (cellIdentity == null) {
                    return "";
                }
                int type = cellIdentity.getType();
                if (type != 3) {
                    if (type != 6) {
                        i2 = Integer.MAX_VALUE;
                        str2 = "UNKNOWN";
                        ci = 2147483647L;
                        rsrp = Integer.MAX_VALUE;
                    } else {
                        CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
                        ci = cellIdentityNr.getNci();
                        tac = cellIdentityNr.getTac();
                        str = cellIdentityNr.getMccString() + cellIdentityNr.getMncString();
                        rsrp = ((CellSignalStrengthNr) cellInfo.getCellSignalStrength()).getSsRsrp();
                        str2 = "NR";
                    }
                    return "[mType = " + str2 + " ,mCi = " + ci + " ,mTac = " + i2 + " ,mOperator = " + str3 + " ,mRsrp = " + rsrp + "]";
                }
                CellIdentityLte cellIdentityLte = (CellIdentityLte) cellIdentity;
                ci = cellIdentityLte.getCi();
                tac = cellIdentityLte.getTac();
                str = cellIdentityLte.getMccString() + cellIdentityLte.getMncString();
                rsrp = ((CellSignalStrengthLte) cellInfo.getCellSignalStrength()).getRsrp();
                str2 = "LTE";
                int i3 = tac;
                str3 = str;
                i2 = i3;
                return "[mType = " + str2 + " ,mCi = " + ci + " ,mTac = " + i2 + " ,mOperator = " + str3 + " ,mRsrp = " + rsrp + "]";
            }
        }
        return "";
    }

    private static String getLteCellID(Phone phone) {
        CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
        if (currentCellIdentity == null) {
            return null;
        }
        if (currentCellIdentity instanceof CellIdentityLte) {
            return String.valueOf(((CellIdentityLte) currentCellIdentity).getCi());
        }
        if (currentCellIdentity instanceof CellIdentityNr) {
            return String.valueOf(((CellIdentityNr) currentCellIdentity).getNci());
        }
        return null;
    }

    public static UiccCard getUiccCard(int i) {
        if (!TelephonyManager.getDefault().isMultiSimEnabled()) {
            return UiccController.getInstance().getUiccCard(0);
        }
        return UiccController.getInstance().getUiccCard(i);
    }

    public static String getTopAppName() {
        ActivityManager activityManager = (ActivityManager) PhoneGlobals.getInstance().getSystemService("activity");
        if (activityManager == null) {
            Log.i("MiuiPhoneUtils", "getTopAppName activityManager is null");
            return "";
        }
        try {
            if (activityManager.getRunningTasks(1) != null && activityManager.getRunningTasks(1).get(0) != null) {
                return activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
            }
        } catch (Exception e) {
            Log.i("MiuiPhoneUtils", "getTopAppName exception:" + e.toString());
        }
        return "";
    }

    public static boolean isSupportNetworkRecoveryOptimization(Context context) {
        return context.getResources().getBoolean(R.bool.config_supported_network_recovery_optimization);
    }

    public static long getLteCellId(int i) {
        CellIdentity currentCellIdentity = PhoneFactory.getPhone(i).getCurrentCellIdentity();
        if (currentCellIdentity == null) {
            return -1L;
        }
        if (currentCellIdentity instanceof CellIdentityLte) {
            int ci = ((CellIdentityLte) currentCellIdentity).getCi();
            if (ci == Integer.MAX_VALUE || ci == -1) {
                return -1L;
            }
            return ci;
        }
        if (currentCellIdentity instanceof CellIdentityNr) {
            return ((CellIdentityNr) currentCellIdentity).getNci();
        }
        return -1L;
    }

    public static int getLteLac(int i) {
        int tac;
        CellIdentity currentCellIdentity = PhoneFactory.getPhone(i).getCurrentCellIdentity();
        if (currentCellIdentity == null) {
            return -1;
        }
        if (currentCellIdentity instanceof CellIdentityLte) {
            int tac2 = ((CellIdentityLte) currentCellIdentity).getTac();
            if (tac2 != Integer.MAX_VALUE && tac2 != -1) {
                return tac2;
            }
        } else if ((currentCellIdentity instanceof CellIdentityNr) && (tac = ((CellIdentityNr) currentCellIdentity).getTac()) != Integer.MAX_VALUE) {
            return tac;
        }
        return -1;
    }

    public static boolean isImsSignalOptimizationSupported(Context context) {
        return context.getResources().getBoolean(R.bool.config_ims_signal_optimization_supported);
    }

    public static boolean isSupportedDFS() {
        return TelephonyManagerEx.getDefault().isModemDFSSupported();
    }

    public static boolean isODM4GPlatformSupportModemDump() {
        return HyperOSCustFeatureResolve.getBoolean("support_ODM_4G_ModemDump", false);
    }

    public static boolean isSupportTxsDsdaNonDdsVtOptimizeFeature() {
        return FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_TXSDSDA_NONDDS_VT_OPTIMIZATION);
    }

    public static boolean isSupportVoiceLinkFeature() {
        try {
            return (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getResources().getBoolean(R.bool.config_voice_link_feature_support);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSupportVoiceLinkFeature Exception" + e);
            return false;
        }
    }

    public static boolean isSupportPowerSaveV2() {
        if (!TelephonyManager.isCustForJpSb() && !TelephonyManager.isCustForJpKd()) {
            try {
                return (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getResources().getBoolean(R.bool.config_power_save_mode_supported);
            } catch (Exception e) {
                Log.e("MiuiPhoneUtils", "isSupportPowerSaveV2 Exception" + e);
            }
        }
        return false;
    }

    public static boolean isUseMipcForThrottleLevel() {
        try {
            return (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getResources().getBoolean(R.bool.config_is_use_mipc_for_throttle_level);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isUseMipcForThrottleLevel Exception" + e);
            return false;
        }
    }

    public static boolean isMtkModemStatusTrackSupported() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        try {
            return (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getResources().getBoolean(R.bool.config_mtk_modem_status_track_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isMtkModemStatusTrackSupported Exception" + e);
            return false;
        }
    }

    public static boolean isSatelliteInThremalState() {
        Bundle ntnModuleInfo = ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getNtnModuleInfo();
        if (ntnModuleInfo != null) {
            return ntnModuleInfo.getBoolean("is_satellite_thermal_state", false);
        }
        return false;
    }

    public static int startTcpMonitorRegister(String str, int i, String str2, int i2, int i3) {
        return ((IModemModule) ModuleDelegate.get(IModemModule.class)).startTcpMonitorRegister(str, i, str2, i2, i3);
    }

    public static int stopTcpMonitorRegister(int i) {
        return ((IModemModule) ModuleDelegate.get(IModemModule.class)).stopTcpMonitorRegister(i);
    }

    public static int startTcpKeepAlive(String str, int i, String str2, int i2, int i3, int i4) {
        return ((IModemModule) ModuleDelegate.get(IModemModule.class)).startTcpKeepAlive(str, i, str2, i2, i3, i4);
    }

    public static int stopTcpKeepAlive(int i) {
        return ((IModemModule) ModuleDelegate.get(IModemModule.class)).stopTcpKeepAlive(i);
    }

    public static void registerModemTcpKaCallback(IModemTcpKeepAliveIndCallback iModemTcpKeepAliveIndCallback) {
        ((IModemModule) ModuleDelegate.get(IModemModule.class)).registerModemTcpKaCallback(iModemTcpKeepAliveIndCallback);
    }

    public static void unregisterModemTcpKaCallback(IModemTcpKeepAliveIndCallback iModemTcpKeepAliveIndCallback) {
        ((IModemModule) ModuleDelegate.get(IModemModule.class)).unregisterModemTcpKaCallback(iModemTcpKeepAliveIndCallback);
    }

    public static boolean isDeviceModemTcpKaSupported() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        try {
            return (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getResources().getBoolean(R.bool.config_modem_tcp_keepalive_supported) || HyperOSCustFeatureResolve.getBoolean("support_modem_tcp_keepalive", false);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isDeviceModemTcpKaSupported Exception" + e);
            return false;
        }
    }

    /* JADX WARN: Code duplicated, block: B:30:0x0055 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    public static String getTemperature() throws Throwable {
        BufferedReader bufferedReader;
        IOException e;
        String str = "null";
        BufferedReader bufferedReader2 = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(new File("/sys/class/thermal/thermal_message/board_sensor_temp")));
            try {
                try {
                    String line = bufferedReader.readLine();
                    if (line != null) {
                        str = String.format("%.1f", Float.valueOf((float) (Long.parseLong(line) / 1000.0d)));
                    }
                    bufferedReader.close();
                } catch (IOException e2) {
                    e = e2;
                    e.printStackTrace();
                    if (bufferedReader != null) {
                    }
                    return str;
                }
            } catch (Throwable th) {
                th = th;
                bufferedReader2 = bufferedReader;
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (IOException e4) {
            bufferedReader = null;
            e = e4;
        } catch (Throwable th2) {
            th = th2;
            if (bufferedReader2 != null) {
                bufferedReader2.close();
            }
            throw th;
        }
        try {
            bufferedReader.close();
        } catch (IOException e5) {
            e5.printStackTrace();
        }
        return str;
    }

    public static boolean isVibratorSupported() {
        try {
            Vibrator vibrator = (Vibrator) (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getSystemService("vibrator");
            if (vibrator == null) {
                return false;
            }
            return vibrator.hasVibrator();
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isVibratorSupported Exception" + e);
            return false;
        }
    }

    public static boolean isSensorProximitySupported() {
        try {
            SensorManager sensorManager = (SensorManager) (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getSystemService("sensor");
            return (sensorManager == null || sensorManager.getDefaultSensor(8) == null) ? false : true;
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isSensorProximitySupported Exception" + e);
            return false;
        }
    }

    public static boolean isSupportNewCall() {
        return FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_5G_NEW_CALL);
    }

    public static boolean isModemNetworkPerceptionSupported() {
        if (!Build.IS_INTERNATIONAL_BUILD || isInternationalModemNetPerceptionSupported()) {
            return ModemProvider.IS_QCOM && (HyperOSCustFeatureResolve.getBoolean("config_support_modem_network_perception", false) || TelephonyManagerEx.getDefault().isPlatform8550() || TelephonyManagerEx.getDefault().isPlatform8650() || TelephonyManagerEx.getDefault().isPlatform8750() || TelephonyManagerEx.getDefault().isPlatform8850());
        }
        return false;
    }

    public static boolean isInternationalModemNetPerceptionSupported() {
        return ModemProvider.IS_QCOM && (HyperOSCustFeatureResolve.getBoolean("config_support_modem_network_perception_global", false) || TelephonyManagerEx.getDefault().isPlatform8850());
    }

    public static boolean isFileUploadMonitorSupported() {
        String str = SystemProperties.get("persist.sys.file.upload.monitor.enable", "");
        if (!TextUtils.isEmpty(str)) {
            return Boolean.parseBoolean(str);
        }
        return HyperOSCustFeatureResolve.getBoolean("file_upload_monitor_supported", false);
    }

    public static boolean isHighSpeedTrainSmartSwitcherSupported() {
        String str = SystemProperties.get("persist.sys.hst.smart.switcher.enable", "");
        if (TextUtils.isEmpty(str)) {
            return HyperOSCustFeatureResolve.getBoolean("config_is_hst_cell_smart_switcher_supported", false) || FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_HST_CELL_SMART_SWITCHER);
        }
        return Boolean.parseBoolean(str);
    }

    public static boolean isSupportModemAiSignalSupported() {
        return TelephonyManagerEx.getDefault().isSupportModemAiSignalSupported();
    }

    public static boolean isSupportModemAiV1Supported() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        return ModemProvider.IS_QCOM && (HyperOSCustFeatureResolve.getBoolean("config_support_modem_ai_prediction_v1", false) || TelephonyManagerEx.getDefault().isPlatform8650());
    }

    public static boolean isBasementV3Supported() {
        return ModemProvider.IS_QCOM && (HyperOSCustFeatureResolve.getBoolean("config_support_basement_v3", false) || TelephonyManagerEx.getDefault().isPlatform8750() || TelephonyManagerEx.getDefault().isPlatform8850());
    }

    public static boolean isPhoneInImsCall(int i) {
        Phone imsPhone;
        if (!SubscriptionManager.isValidPhoneId(i) || (imsPhone = PhoneFactory.getPhone(i).getImsPhone()) == null) {
            return false;
        }
        Call.State state = imsPhone.getForegroundCall().getState();
        Call.State state2 = imsPhone.getBackgroundCall().getState();
        Call.State state3 = imsPhone.getRingingCall().getState();
        Call.State state4 = Call.State.ACTIVE;
        return state == state4 || state2 == state4 || state3 == state4;
    }

    public static CellSignalStrengthNr getNrSignalStrength(SignalStrength signalStrength) {
        List cellSignalStrengths = signalStrength.getCellSignalStrengths(CellSignalStrengthNr.class);
        if (cellSignalStrengths == null || cellSignalStrengths.size() <= 0) {
            return null;
        }
        return (CellSignalStrengthNr) cellSignalStrengths.get(0);
    }

    public static boolean isManualSelection(int i) {
        ServiceState serviceState;
        Phone phone = PhoneFactory.getPhone(i);
        return (phone == null || (serviceState = phone.getServiceState()) == null || !serviceState.getIsManualSelection()) ? false : true;
    }

    public static boolean isAirPlaneMode() {
        return Settings.System.getInt(PhoneGlobals.getInstance().getContentResolver(), "airplane_mode_on", -1) == 1;
    }

    public static boolean isPhoneInCall() {
        return PhoneConstants.State.IDLE != getCallManager().getState();
    }

    public static boolean isSupportOtaLteUpdate() {
        if (TelephonyManager.isCustFeatureResolveEnabled()) {
            return HyperOSCustFeatureResolve.getBoolean("support_ota_lte_update", false);
        }
        return TelephonyManager.isCustForLmClaro();
    }

    public static boolean isMtkPingPongOptSupported() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        try {
            return (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getResources().getBoolean(R.bool.config_mtk_ping_pong_opt_supported);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isMtkPingPongOptSupported Exception" + e);
            return false;
        }
    }

    public static boolean isMtkDefaultDisableN8AndN28Supported() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        try {
            return (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getResources().getBoolean(R.bool.config_mtk_disable_n8_n28_supported) || isMtkDx1Platform() || HyperOSCustFeatureResolve.getBoolean("config_mtk_default_disable_n8_n28", false);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isMtkDefaultDisableN8AndN28Supported Exception" + e);
            return false;
        }
    }

    public static boolean isMtkCloudModemFeatureSupported() {
        try {
            return (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getResources().getBoolean(R.bool.config_mtk_cloud_modem_feature_supported) || isMtkDx1Platform() || HyperOSCustFeatureResolve.getBoolean("config_mtk_cloud_modem_feature_supported", false);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isMtkCloudModemFeatureSupported Exception" + e);
            return false;
        }
    }

    public static boolean shouldShowBeiDouButton(Context context) {
        boolean zIsSupportBeiDouByDevice = TelephonyManagerEx.getDefault().isSupportBeiDouByDevice();
        boolean zIsKidMode = isKidMode(context);
        boolean zIsSecondSpaceMode = isSecondSpaceMode(context);
        Log.d("MiuiPhoneUtils", "shouldShowBeiDouButton device = " + zIsSupportBeiDouByDevice + ", kid = " + zIsKidMode + ", isSecondSpace = " + zIsSecondSpaceMode);
        if (zIsKidMode || zIsSecondSpaceMode) {
            return false;
        }
        return zIsSupportBeiDouByDevice;
    }

    public static boolean isConnectedToInternet(Context context) {
        Network activeNetwork;
        NetworkCapabilities networkCapabilities;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null || (activeNetwork = connectivityManager.getActiveNetwork()) == null || (networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)) == null) {
            return false;
        }
        return networkCapabilities.hasCapability(12);
    }

    public static boolean isSupportFontdoDcDevice() {
        return HyperOSCustFeatureResolve.getBoolean("support_fontdo_dc", false);
    }

    public static boolean isSupportSixRX() {
        return !Build.IS_INTERNATIONAL_BUILD && FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_FEATURE_SUPPORT_6RX_OPTIMIZATION);
    }

    public static boolean isSupportSatelliteXingwangByDevice(Context context) {
        boolean zIsSupportSatelliteXingwangByDevice = TelephonyManagerEx.getDefault().isSupportSatelliteXingwangByDevice();
        boolean zIsKidMode = isKidMode(context);
        boolean zIsSecondSpaceMode = isSecondSpaceMode(context);
        Log.d("MiuiPhoneUtils", "isSupportSatelliteXingwangByDevice shouldShow = " + zIsSupportSatelliteXingwangByDevice + ", kid = " + zIsKidMode + ", isSecondSpace = " + zIsSecondSpaceMode);
        if (zIsKidMode || zIsSecondSpaceMode) {
            return false;
        }
        return zIsSupportSatelliteXingwangByDevice;
    }

    public static boolean isRadioPowerOn(int i) {
        Phone phone = getPhone(i);
        return phone != null && phone.mCi.getRadioState() == 1;
    }

    public static boolean isCustMmsUserAgentSupport() {
        if (TelephonyManager.isCustFeatureResolveEnabled()) {
            return HyperOSCustFeatureResolve.getBoolean("support_cust_mms_user_agent", false);
        }
        return TelephonyManager.isCustForEsTelefonica();
    }

    public static boolean isTiantongSatelliteDataSupported() {
        return TelephonyManagerEx.getDefault().isSupportSatelliteByDevice() && TelephonyManagerEx.getDefault().isTiantongSatelliteDataSupported();
    }

    public static String getPackageVersionName(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 0);
            if (packageInfo != null) {
                Log.d("MiuiPhoneUtils", "getPackageVersionName: " + packageInfo.versionName);
                return packageInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("MiuiPhoneUtils", "getPackageVersionName error: " + e);
        }
        return null;
    }

    public static int compareAppVersions(String str, String str2) {
        String[] strArrSplit = str.split(s.a);
        String[] strArrSplit2 = str2.split(s.a);
        int iMax = Math.max(strArrSplit.length, strArrSplit2.length);
        int i = 0;
        while (i < iMax) {
            int i2 = i < strArrSplit.length ? Integer.parseInt(strArrSplit[i]) : 0;
            int i3 = i < strArrSplit2.length ? Integer.parseInt(strArrSplit2[i]) : 0;
            if (i2 > i3) {
                return 1;
            }
            if (i2 < i3) {
                return -1;
            }
            i++;
        }
        return 0;
    }

    public static boolean isSatelliteVersionDingTalkInstalled(Context context) {
        String packageVersionName = getPackageVersionName(context, PACKAGE_NAME_DING_TALK);
        return !TextUtils.isEmpty(packageVersionName) && compareAppVersions(packageVersionName, "7.6.50") >= 0;
    }

    public static boolean isMPDevice() {
        return "MP".equalsIgnoreCase(SystemProperties.get("ro.boot.hwlevel"));
    }

    public static boolean findConversionSupportComponent() {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        Intent intent = new Intent("android.service.euicc.action.CONVERT_TO_EMBEDDED_SUBSCRIPTION");
        PackageManager packageManager = phoneGlobals.getPackageManager();
        List<ResolveInfo> listQueryIntentActivities = packageManager.queryIntentActivities(intent, sQueryFlag);
        if (listQueryIntentActivities == null || listQueryIntentActivities.isEmpty()) {
            Log.d("MiuiPhoneUtils", "findConversionSupportComponent: resolveInfoList is null or empty");
            return false;
        }
        for (ResolveInfo resolveInfo : listQueryIntentActivities) {
            if (isValidEuiccComponent(packageManager, resolveInfo)) {
                Log.d("MiuiPhoneUtils", "findConversionSupportComponent: " + resolveInfo.activityInfo.name);
                break;
            }
        }
        return true;
    }

    private static boolean isValidEuiccComponent(PackageManager packageManager, ResolveInfo resolveInfo) {
        String str;
        ComponentInfo componentInfo = TelephonyUtils.getComponentInfo(resolveInfo);
        String packageName = new ComponentName(componentInfo.packageName, componentInfo.name).getPackageName();
        Log.d("MiuiPhoneUtils", "isValidEuiccComponent: " + packageName);
        if (packageManager.checkPermission("android.permission.WRITE_EMBEDDED_SUBSCRIPTIONS", packageName) != 0) {
            Log.d("MiuiPhoneUtils", "isValidEuiccComponent: " + packageName + " is not privileged");
            return false;
        }
        if (componentInfo instanceof ServiceInfo) {
            str = ((ServiceInfo) componentInfo).permission;
        } else if (componentInfo instanceof ActivityInfo) {
            str = ((ActivityInfo) componentInfo).permission;
        } else {
            Log.d("MiuiPhoneUtils", "isValidEuiccComponent: " + packageName + " is not a service or activity");
            return false;
        }
        Log.d("MiuiPhoneUtils", "isValidEuiccComponent: " + packageName + " permission = " + str);
        if (!TextUtils.equals(str, "android.permission.BIND_EUICC_SERVICE")) {
            Log.d("MiuiPhoneUtils", "isValidEuiccComponent: " + packageName + " is not privileged with BIND_EUICC_SERVICE");
            return false;
        }
        IntentFilter intentFilter = resolveInfo.filter;
        if (intentFilter != null && intentFilter.getPriority() != 0) {
            return true;
        }
        Log.d("MiuiPhoneUtils", "isValidEuiccComponent: " + packageName + " does not have a priority");
        return false;
    }

    public static boolean isTelephonyXiaoAiSupported() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        try {
            return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_support_telephony_xiaoai);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "isTelephonyXiaoAiSupported " + e);
            return false;
        }
    }

    public static boolean isCellPreferOptSupported() {
        if (Build.IS_INTERNATIONAL_BUILD && !IS_INDIA) {
            return false;
        }
        if (HyperOSCustFeatureResolve.getBoolean("config_is_cellprefer_opt_support", false)) {
            return true;
        }
        return isSnsOptimizationSupported() && (TelephonyManagerEx.getDefault().isPlatform8750() || TelephonyManagerEx.getDefault().isPlatform8850());
    }

    public static boolean isGUTISupported() {
        return HyperOSCustFeatureResolve.getBoolean("support_guti", false);
    }

    public static boolean isDcStatMonitorSupported() {
        return HyperOSCustFeatureResolve.getBoolean("support_dc_stat_monitor", false);
    }

    public static boolean isSupportLocationStats() {
        return HyperOSCustFeatureResolve.getBoolean("support_location_stats", false);
    }

    public static boolean isNsaScgPoorOptimizationV2Supported() {
        return HyperOSCustFeatureResolve.getBoolean("support_nsa_scg_poor_optimization_v2", false);
    }

    public static boolean isModemPowerEstimateSupported() {
        if (!Build.IS_INTERNATIONAL_BUILD && !ModemProvider.IS_QCOM && !Build.IS_STABLE_VERSION) {
            try {
                return PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_support_modem_power_ai);
            } catch (Exception e) {
                Log.e("MiuiPhoneUtils", "isModemPowerEstimateSupported " + e);
            }
        }
        return false;
    }

    public static boolean supportXiaomiRtt(Context context) {
        return isRttSupported() && isContactsSupportRtt(context) && isInCallUiSupportRtt(context);
    }

    private static boolean isRttSupported() {
        boolean z = true;
        int[] activeSubIdList = SubscriptionManagerService.getInstance().getActiveSubIdList(true);
        int length = activeSubIdList == null ? 0 : activeSubIdList.length;
        Log.i("MiuiPhoneUtils", "isRttSupported, " + length);
        if (length > 0) {
            for (int i : activeSubIdList) {
                if (!PhoneGlobals.getInstance().phoneMgr.isRttSupported(i)) {
                }
            }
            z = false;
        } else {
            z = false;
        }
        Log.i("MiuiPhoneUtils", "isRttSupported, " + z);
        return z;
    }

    public static boolean isHstPreferCellSupported() {
        return (TelephonyManagerEx.getDefault().isPlatform8850() || HyperOSCustFeatureResolve.getBoolean("config_is_hst_prefer_cell_supported", false)) && SystemProperties.getInt("persist.radio.hst.prefercell", 1) == 1;
    }

    private static boolean isContactsSupportRtt(Context context) {
        Bundle bundle;
        boolean z = false;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo("com.android.contacts", DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
            if (applicationInfo != null && (bundle = applicationInfo.metaData) != null) {
                z = bundle.getBoolean("miui.supportRtt", false);
            }
            Log.i("MiuiPhoneUtils", "contactsSupportRtt=" + z);
            return z;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.i("MiuiPhoneUtils", "Failed to get rtt status.");
            return z;
        }
    }

    private static boolean isInCallUiSupportRtt(Context context) {
        Bundle bundle;
        boolean z = false;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo("com.android.incallui", DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
            if (applicationInfo != null && (bundle = applicationInfo.metaData) != null) {
                z = bundle.getBoolean("miui.supportRtt", false);
            }
            Log.i("MiuiPhoneUtils", "inCallUiSupportRtt=" + z);
            return z;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.i("MiuiPhoneUtils", "Failed to get inCallUi rtt status.");
            return z;
        }
    }

    public static void setCellBroadCastEnabled(Context context, boolean z) {
        if (Build.IS_INTERNATIONAL_BUILD) {
            Log.i("MiuiPhoneUtils", "do not support cellbroadcast, ignore set");
            return;
        }
        try {
            if (HyperOSCustFeatureResolve.getBoolean("support_cellbroadcast_cn", false)) {
                Log.i("MiuiPhoneUtils", "support cellbroadcast cn");
                z = true;
            }
            context.getPackageManager().setComponentEnabledSetting(new ComponentName("com.android.cellbroadcastreceiver", "com.android.cellbroadcastreceiver.CellBroadcastReceiver"), z ? 1 : 2, 1);
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "setCellBroadCastEnabled failed", e);
        }
    }

    public static int getCellBroadCastEnabled(Context context) {
        if (Build.IS_INTERNATIONAL_BUILD) {
            Log.i("MiuiPhoneUtils", "do not support cellbroadcast, ignore get");
            return 2;
        }
        try {
            return context.getPackageManager().getComponentEnabledSetting(new ComponentName("com.android.cellbroadcastreceiver", "com.android.cellbroadcastreceiver.CellBroadcastReceiver"));
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "getCellBroadCastEnabled failed", e);
            return 2;
        }
    }

    public static void switchCellBroadCastEnabled(Context context, ServiceState serviceState) {
        boolean z = false;
        if (!HyperOSCustFeatureResolve.getBoolean("support_cellbroadcast", false)) {
            Log.i("MiuiPhoneUtils", "do not support cellbroadcast, ignore switch");
            return;
        }
        int cellBroadCastEnabled = getCellBroadCastEnabled(context);
        String operatorNumeric = serviceState.getOperatorNumeric();
        if (operatorNumeric != null && operatorNumeric.length() >= 3) {
            z = !"460".equals(operatorNumeric.substring(0, 3));
        }
        Rlog.d("MiuiPhoneUtils", "switchCellBroadCastEnabled currentState: " + cellBroadCastEnabled + ", desiredEnabled: " + z + ", operatorNumeric: " + operatorNumeric);
        if (cellBroadCastEnabled != (z ? 1 : 2)) {
            setCellBroadCastEnabled(context, z);
        }
    }

    public static boolean isSupportMtkHighSpeedSbpOpt() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        return HyperOSCustFeatureResolve.getBoolean("support_mtk_high_speed_sbp_opt", false);
    }

    public static boolean isSupportShowDialogWhenIllegalME(int i, int i2, String str) {
        if (!Build.IS_INTERNATIONAL_BUILD && i == 6) {
            List stringArrayForPhoneId = HyperOSCustFeatureResolve.getStringArrayForPhoneId(i2, "support_show_dialog_when_illegal_me_mcc_list");
            if (TextUtils.isEmpty(str) || !ArrayUtils.contains(stringArrayForPhoneId, str)) {
                Rlog.d("MiuiPhoneUtils", "isSupportShowDialogWhenIllegalME do not support network mcc: " + str);
            } else {
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance());
                boolean z = defaultSharedPreferences.getBoolean("show_illegal_me_dialog", false);
                long j = defaultSharedPreferences.getLong("last_time_show_illegal_me_dialog_in_" + str, 0L);
                boolean z2 = !z || j == 0 || System.currentTimeMillis() - j >= SHOW_ILLEGAL_ME_DIALOG_PERIOD;
                Rlog.d("MiuiPhoneUtils", "isSupportShowDialogWhenIllegalME - showBefore: " + z + ", lastShowTime: " + j + ", shouldShow: " + z2);
                return z2;
            }
        }
        return false;
    }

    public static boolean isP21AndBeforeHwLevel() {
        return ArrayUtils.contains(new String[]{"P0", "P0.1", "P01", "P1", "P1.0", "P1.1", "P11", "P2", "P2.0", "P2.1", "P21"}, SystemProperties.get("ro.boot.hwlevel", ""));
    }

    public static final void setShowDialogWhenIllegalMEDetails(String str, long j) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).edit();
        editorEdit.putBoolean("show_illegal_me_dialog", true);
        editorEdit.putLong("last_time_show_illegal_me_dialog_in_" + str, j);
        editorEdit.apply();
    }

    public static final void resetShowDialogWhenIllegalME() {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).edit();
        editorEdit.putBoolean("show_illegal_me_dialog", false);
        editorEdit.apply();
    }

    public static boolean isHstStationCellPreferSupported() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        return (HyperOSCustFeatureResolve.getBoolean("config_is_hst_station_cellprefer_support", false) || TelephonyManagerEx.getDefault().isPlatform8850()) && SystemProperties.getInt("persist.radio.hst.hstStationCellPrefer", 1) == 1;
    }

    public static boolean isDnsPrefetchSupported() {
        return !Build.IS_INTERNATIONAL_BUILD && TelephonyManagerEx.getDefault().isDnsPrefetchSupported() && SystemProperties.getInt("persist.radio.hst.dnsPrefetch", 1) == 1;
    }

    public static boolean isHstSmartlinkOptSupported() {
        return !Build.IS_INTERNATIONAL_BUILD && (TelephonyManagerEx.getDefault().isPlatform8850() || HyperOSCustFeatureResolve.getBoolean("config_is_hst_smartlink_opt_supported", false)) && SystemProperties.getInt("persist.radio.hst.smartLink", 1) == 1;
    }

    public static boolean isFivegLBStreamMediaOptSupported() {
        return !Build.IS_INTERNATIONAL_BUILD && HyperOSCustFeatureResolve.getBoolean("is_fiveg_lb_stream_media_opt_supported", false);
    }

    public static boolean isHighPriorityCellSelectionSupported() {
        return HyperOSCustFeatureResolve.getBoolean("support_HighPriorityCellSelection_config", false);
    }

    public static boolean shouldDeprioSaInNightMode() {
        try {
            boolean z = PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_enable_deprioritize_sa_in_night_mode);
            Log.i("MiuiPhoneUtils", "shouldDeprioSaInNightMode : shouldDeprioSaInNight =" + z);
            return z;
        } catch (Exception e) {
            Log.e("MiuiPhoneUtils", "shouldDeprioSaInNightMode : " + e);
            return false;
        }
    }

    public static boolean isCTVolteSupportedByDevice() {
        return Build.IS_CT_CUSTOMIZATION_TEST || CT_VOLTE_SUPPORTED_MODE > 0;
    }

    public static boolean isCTVoLTESupport(Phone phone) {
        if (isChinaOrMacauTelecomSim(phone)) {
            return isCTVolteSupportedByDevice();
        }
        return false;
    }

    public static boolean isCtVoLTEOnlySupport(Phone phone) {
        if (isChinaOrMacauTelecomSim(phone)) {
            return isDeviceNotSupportCDMA() || Build.IS_CT_CUSTOMIZATION_TEST || CT_VOLTE_SUPPORTED_MODE == 1;
        }
        Log.d("MiuiPhoneUtils", "isCtVoLTEOnlySupport not ct sim!");
        return false;
    }

    public static boolean isCTDualVolteSupported() {
        return DUAL_VOLTE_SUPPORTED && isCTVolteSupportedByDevice();
    }

    public static boolean shouldOpenVolteForCt() {
        return CT_VOLTE_SUPPORTED_MODE == 2 || isFiveGCapable();
    }

    public static boolean isHideAlwaysReceiveMms() {
        return !Build.IS_INTERNATIONAL_BUILD && SystemProperties.getInt("ro.miui.ui.version.code", 0) >= 15;
    }

    public static boolean isHideStkIcon() {
        String[] strArr = {"ziyi", "yunluo", "ruby", "moonstone", "nuwa", "fuxi"};
        boolean zEquals = false;
        for (int i = 0; i < 6 && !zEquals; i++) {
            zEquals = strArr[i].equals(Build.DEVICE);
        }
        return !Build.IS_INTERNATIONAL_BUILD && (SystemProperties.getInt("ro.miui.ui.version.code", 0) >= 14 || zEquals);
    }

    public static String getNetworkType(int i) {
        int nrConfigType;
        int allocated;
        if (!isValidatePhoneId(i)) {
            return "UNKNOW";
        }
        FiveGServiceInfo fiveGServiceInfo = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(i);
        if (fiveGServiceInfo != null) {
            nrConfigType = fiveGServiceInfo.getNrConfigType();
            allocated = fiveGServiceInfo.getAllocated();
        } else {
            nrConfigType = -1;
            allocated = 0;
        }
        if (nrConfigType == 0 && allocated == 1) {
            return "NSA";
        }
        if (nrConfigType == 1) {
            return "SA";
        }
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || phone.getServiceState() == null) {
            return "UNKNOW";
        }
        return TelephonyManagerEx.getDefault().getNetworkClassName(phone.getServiceState().getDataNetworkType());
    }

    public static boolean isSupportNetworkOptimization() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return ModemProvider.IS_QCOM && TelephonyManagerEx.getDefault().isPlatform8850();
        }
        return true;
    }

    public static Dialog displayMMIInitiate(Phone phone, Context context, MmiCode mmiCode, Message message, Dialog dialog) {
        Log.d("MiuiPhoneUtils", "displayMMIInitiate: " + Rlog.pii("MiuiPhoneUtils", mmiCode.toString()));
        if (dialog != null) {
            dialog.dismiss();
        }
        MiStubParam miStubParamObtain = MiStubParam.obtain();
        if (MiPhoneAppStub.get().displayMMIInitiate(phone, context, mmiCode, message, dialog, miStubParamObtain)) {
            return (Dialog) miStubParamObtain.obj1;
        }
        if (!mmiCode.isCancelable()) {
            Log.d("MiuiPhoneUtils", "displayMMIInitiate: not a USSD code, displaying status toast.");
            Toast.makeText(context, context.getText(R.string.mmiStarted), 0).show();
            return null;
        }
        Log.d("MiuiPhoneUtils", "displayMMIInitiate: running USSD code, displaying intermediate progress.");
        ProgressDialog progressDialog = new ProgressDialog(context, android.R.style.Theme.DeviceDefault.Dialog.Alert);
        progressDialog.setMessage(context.getText(R.string.ussdRunning));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().addFlags(2);
        progressDialog.show();
        return progressDialog;
    }

    public static boolean isSupportFakeStationOptimization() {
        return HyperOSCustFeatureResolve.getBoolean("support_fake_station_optimization", false);
    }

    public static List getFeatureConfig(String str) {
        List stringArray = HyperOSCustFeatureResolve.getStringArray(str);
        return stringArray == null ? Collections.EMPTY_LIST : stringArray;
    }

    public static boolean isContainsPackage(String str, String str2) {
        return ArrayUtils.contains(getFeatureConfig(str), str2);
    }

    public static boolean isTopAppForFeatureConfig(String str) {
        return ArrayUtils.contains(getFeatureConfig(str), getTopAppName());
    }

    public static synchronized void saveListToDb(List list, String str) {
        if (list != null) {
            try {
                if (list.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    Iterator it = list.iterator();
                    boolean z = false;
                    while (it.hasNext()) {
                        String str2 = (String) it.next();
                        if (z) {
                            sb.append(",");
                        } else {
                            z = true;
                        }
                        sb.append(str2);
                    }
                    Settings.Global.putString(PhoneGlobals.getInstance().getContentResolver(), str, sb.toString());
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static synchronized void saveListToDb(Set set, String str) {
        if (set != null) {
            try {
                if (set.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    Iterator it = set.iterator();
                    boolean z = false;
                    while (it.hasNext()) {
                        String str2 = (String) it.next();
                        if (z) {
                            sb.append(",");
                        } else {
                            z = true;
                        }
                        sb.append(str2);
                    }
                    Settings.Global.putString(PhoneGlobals.getInstance().getContentResolver(), str, sb.toString());
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static boolean isSupportFivegaOptimization() {
        return HyperOSCustFeatureResolve.getBoolean("support_fivega_optimization", false);
    }

    public static boolean isSupportSleepProtectOptimization() {
        return HyperOSCustFeatureResolve.getBoolean("support_sleep_protect_optimization", false);
    }

    public static boolean isSupportNoSimPowerOptimization() {
        return HyperOSCustFeatureResolve.getBoolean("support_no_sim_power_optimization", false);
    }

    public static boolean isHyperEngineSupported() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return false;
        }
        return HyperOSCustFeatureResolve.getBoolean("is_hyper_engine_optimization_supported", false);
    }

    public static boolean isSubwayEnhancedSupported() {
        return HyperOSCustFeatureResolve.getBoolean("is_subway_enhanced_supported", false) || isSNSV4SupportedByQcom();
    }

    public static boolean isBuildingModeSupported() {
        if (isSpecialNetorkOptimizationSupported()) {
            return HyperOSCustFeatureResolve.getBoolean("is_building_mode_supported", false) || isPlatformDE30() || isPlatformDE50() || isPlatformDE70() || isPlatformDE90();
        }
        return false;
    }

    public static boolean isNetworkQoeSupported() {
        return HyperOSCustFeatureResolve.getBoolean("config_support_network_qoe_optimization", false);
    }

    public static boolean isDualDataConnected() {
        return SystemProperties.getInt("ril.multisim.dualdata_status", 0) == 1;
    }

    public static boolean isSupportOsuFeature() {
        if (Build.IS_INTERNATIONAL_BUILD) {
            return HyperOSCustFeatureResolve.getBoolean("config_is_support_osu_feature", false);
        }
        return false;
    }

    public static boolean isDataOnlySlot(int i) {
        int virtualSimSlot;
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        if (MiuiTelephonyManagerStub.get().isSupportEuiccBinding() && (MiuiTelephonyManagerStub.get().isEsimActive() || MiuiTelephonyManagerStub.get().getEsimGPIOState() == 0)) {
            return i == getEsimSlotId(phoneGlobals);
        }
        if (PhoneFactory.getPhones().length > 1) {
            virtualSimSlot = VirtualSimUtils.getVirtualSimSlot(phoneGlobals, true, 1);
        } else {
            virtualSimSlot = SubscriptionManager.INVALID_SLOT_ID;
        }
        return i == virtualSimSlot;
    }

    public static int getEsimSlotId(Context context) {
        try {
            if (!IS_ESIM_CAPABLE) {
                return SubscriptionManager.INVALID_SLOT_ID;
            }
            UiccController uiccController = UiccController.getInstance();
            for (int i = 0; i < uiccController.getUiccSlots().length; i++) {
                UiccSlot uiccSlot = uiccController.getUiccSlot(i);
                if (uiccSlot != null && uiccSlot.isEuicc()) {
                    return i;
                }
            }
            List<SubscriptionInfo> subscriptionInfoList = SubscriptionManager.getDefault().getSubscriptionInfoList();
            if (subscriptionInfoList != null && !subscriptionInfoList.isEmpty()) {
                for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
                    if (subscriptionInfo != null && subscriptionInfo.isEmbedded()) {
                        return subscriptionInfo.getSlotId();
                    }
                }
            }
            return SubscriptionManager.INVALID_SLOT_ID;
        } catch (SecurityException unused) {
            return SubscriptionManager.INVALID_SLOT_ID;
        } catch (Exception unused2) {
            return SubscriptionManager.INVALID_SLOT_ID;
        }
    }

    public static boolean hasDcOnlyEsim(Context context) {
        if (!MiuiTelephonyManagerStub.get().isSupportEuiccBinding()) {
            return false;
        }
        if (MiuiTelephonyManagerStub.get().isEsimActive() || MiuiTelephonyManagerStub.get().getEsimGPIOState() == 0) {
            return true;
        }
        int phoneCount = MiuiTelephonyManagerStub.get().getPhoneCount();
        boolean z = false;
        for (int i = 0; i < phoneCount; i++) {
            if (isDataOnlySlot(i)) {
                z = true;
            }
        }
        return z;
    }

    public static boolean isSprdDevice() {
        String str = Build.SOC_MANUFACTURER;
        if (str == null) {
            return false;
        }
        String lowerCase = str.toLowerCase();
        return lowerCase.contains("unisoc") || lowerCase.contains("spreadtrum") || lowerCase.contains("sprd");
    }

    public static boolean isConfigSprdModemDumpEnabled() {
        return HyperOSCustFeatureResolve.getBoolean("config_sprd_modem_dump_enabled", false);
    }

    public static boolean isSprdSupportModemDump() {
        return isConfigSprdModemDumpEnabled() && isSprdDevice();
    }

    public static boolean isSupportHEGException() {
        return HyperOSCustFeatureResolve.getBoolean("support_heg_exception", false);
    }

    public static boolean isSupportFeatureQueryCmn() {
        if (ModemProvider.IS_QCOM) {
            return HyperOSCustFeatureResolve.getBoolean("support_feature_query_cmn", false);
        }
        return false;
    }
}
