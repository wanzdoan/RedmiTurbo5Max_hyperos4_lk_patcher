package com.android.phone;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.util.Log;
import androidx.preference.PreferenceManager;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.utils.Utils;
import com.android.services.telephony.ims.ImsAdapterBase;
import miui.os.Build;
import miui.telephony.DefaultSimManager;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public abstract class CarrierConfigLoaderInjector {
    public static long[] mSimLoadedTimes = new long[MiuiPhoneUtils.PHONE_COUNT];
    private static DefaultSimManager.DataSlotListener sDataSlotListener = new DefaultSimManager.DataSlotListener() { // from class: com.android.phone.CarrierConfigLoaderInjector.1
        public void onDataSlotReady(boolean z) {
            if (MiuiPhoneUtils.isCTDualVolteSupported()) {
                CarrierConfigLoaderInjector.enableViceSlotVolteForDualCtSim();
            }
        }
    };
    private static EnableDual4GForDualCTSim sEnableDual4G = new EnableDual4GForDualCTSim(3);

    public static void updateVoLTECarrierConfig(Context context, PersistableBundle persistableBundle, String str) {
        if (Build.IS_GLOBAL_BUILD) {
            String string = context.getString(android.R.string.factorytest_failed);
            Log.d("CarrierConfigLoaderInjector", "defaultSmsPkg = " + string);
            if (string != null && string.equals("com.google.android.apps.messaging")) {
                persistableBundle.putString("config_ims_package_override_string", "com.google.android.ims");
            }
        }
        if (persistableBundle != null && persistableBundle.getBoolean("config_disable_wfc_when_enable_wifi_ap", false)) {
            boolean z = 13 == TelephonyManagerEx.getDefault().getWifiApState();
            Log.i("CarrierConfigLoaderInjector", "updateVoLTECarrierConfig wifiApState = " + z);
            PhoneProxy.setCarrierConfigValue(persistableBundle, "carrier_wfc_ims_available_bool", z ^ true);
        }
        if (!MiuiImsPhoneUtils.supportSecretCodeForVolteAndWfc(persistableBundle)) {
            if (isVolteForceEnabled(context)) {
                setVolteForceEnabled(context, false);
            }
            if (isVoWifiForceEnabled(context)) {
                setVoWifiForceEnabled(context, false);
                return;
            }
            return;
        }
        if (isNewCallForceEnabled(context) && MiuiPhoneUtils.isCMCCSim(str)) {
            Log.d("CarrierConfigLoaderInjector", "set new call carrier enable");
            PhoneProxy.setCarrierConfigValue(persistableBundle, "carrier_new_call_available_bool", true);
        }
        if (isVoWifiForceEnabled(context)) {
            PhoneProxy.setCarrierConfigValue(persistableBundle, "carrier_wfc_ims_available_bool", true);
            PhoneProxy.setCarrierConfigValue(persistableBundle, "hide_wfc_mode_option", false);
            PhoneProxy.setCarrierConfigValue(persistableBundle, "editable_wfc_mode_bool", true);
            PhoneProxy.setCarrierConfigValue(persistableBundle, "carrier_hide_wfc_bool", false);
        }
        if (isVolteForceEnabled(context)) {
            PhoneProxy.setVolteCarrierConfigValue(persistableBundle, true);
            PhoneProxy.setCarrierConfigValue(persistableBundle, "hide_enhanced_4g_lte_bool", false);
            PhoneProxy.setCarrierConfigValue(persistableBundle, "editable_enhanced_4g_lte_bool", true);
        } else if (Build.IS_CT_CUSTOMIZATION_TEST && (ServiceProviderUtils.isChinaTelecom(str) || ServiceProviderUtils.isMacauTelecom(str))) {
            PhoneProxy.setVolteCarrierConfigValue(persistableBundle, true);
        } else if (Build.IS_CU_CUSTOMIZATION_TEST && ServiceProviderUtils.isChinaUnicom(str)) {
            PhoneProxy.setVolteCarrierConfigValue(persistableBundle, true);
        }
    }

    public static void updateVoLTECarrierConfig(Context context, PersistableBundle persistableBundle, String str, int i) {
        if (i == MiuiPhoneUtils.getDcOnlyVirtualSimSlot()) {
            PhoneProxy.setCarrierConfigValue(persistableBundle, "enhanced_4g_lte_on_by_default_bool", false);
        }
        updateVoLTECarrierConfig(context, persistableBundle, str);
    }

    public static boolean isVolteForceEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("force_enable_volte", false);
    }

    public static void setVolteForceEnabled(Context context, boolean z) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editorEdit.putBoolean("force_enable_volte", z);
        editorEdit.apply();
    }

    public static boolean isVoWifiForceEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("force_enable_vowifi", false);
    }

    public static void setVoWifiForceEnabled(Context context, boolean z) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editorEdit.putBoolean("force_enable_vowifi", z);
        editorEdit.apply();
    }

    public static boolean isNewCallForceEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("force_enable_new_call", false);
    }

    public static void setNewCallForceEnabled(Context context, boolean z) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editorEdit.putBoolean("force_enable_new_call", z);
        editorEdit.apply();
    }

    class EnableDual4GForDualCTSim implements Utils.Dual4GCallBack {
        private int mMaxRetryTimes;
        private int mTimesRetried;

        public EnableDual4GForDualCTSim(int i) {
            this.mMaxRetryTimes = i;
        }

        public void start() {
            this.mTimesRetried = 0;
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setDual4GMode(true, this);
        }

        public void stop() {
            this.mTimesRetried = this.mMaxRetryTimes;
        }

        @Override // com.android.phone.utils.Utils.Dual4GCallBack
        public void onSetDual4GResult(boolean z, int i) {
            Log.d("CarrierConfigLoaderInjector", "onSetDual4GResult enabled=" + z + ", resultCode=" + i + ", mTimesRetried=" + this.mTimesRetried);
            int i2 = this.mTimesRetried + 1;
            this.mTimesRetried = i2;
            if (i != 0 && i2 < this.mMaxRetryTimes) {
                if (!((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDual4GEnabled()) {
                    ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setDual4GMode(true, this);
                    return;
                } else {
                    this.mTimesRetried = this.mMaxRetryTimes;
                    return;
                }
            }
            this.mTimesRetried = this.mMaxRetryTimes;
        }
    }

    public static void updateEnhanced4GLteDefaultValue(Context context, int i) {
        if (!SubscriptionManager.isRealSlotId(i)) {
            Log.d("CarrierConfigLoaderInjector", "No need to updateEnhanced4GLteDefaultValue!");
            return;
        }
        if (shouldDisableVolteByDefault(context, i)) {
            disableVolteByDefault(context, i);
            return;
        }
        if (MiuiPhoneUtils.shouldTurnVolteOnByDefaultAfterOta(MiuiPhoneUtils.getPhone(i)) && !isVolteForceEnabled(context)) {
            enableVolteByDefault(context, i);
        } else if (MiuiPhoneUtils.isChinaOrMacauTelecomSim(MiuiPhoneUtils.getPhone(i))) {
            handleCtVolteByDefault(context, i);
        }
    }

    public static void enableViceSlotVolteForDualCtSim() {
        if (!isAllSimLoaded() || !MiuiPhoneUtils.isDualCTSim()) {
            Log.d("CarrierConfigLoaderInjector", "not all sim loaded");
            return;
        }
        if (!DefaultSimManager.getInstance().isDataSlotReady()) {
            Log.d("CarrierConfigLoaderInjector", "data slot is not ready");
            DefaultSimManager.getInstance().addDataSlotListener(sDataSlotListener);
            return;
        }
        if (!((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDual4GEnabled()) {
            Log.d("CarrierConfigLoaderInjector", "dual ct sim, enable dual 4g for non-dds slot...");
            sEnableDual4G.start();
        }
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        Log.d("CarrierConfigLoaderInjector", "dds slot=" + defaultDataSlotId);
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (i != defaultDataSlotId && i != MiuiPhoneUtils.getDcOnlyVirtualSimSlot()) {
                Log.d("CarrierConfigLoaderInjector", "dual ct sim, turn on volte for non-dds slot...");
                ImsAdapterBase.getInstance().setEnhanced4gLteModeSetting(PhoneGlobals.getInstance(), true, i);
            }
        }
    }

    private static boolean isAllSimLoaded() {
        int i;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            i = MiuiPhoneUtils.PHONE_COUNT;
            if (i2 >= i) {
                break;
            }
            if (mSimLoadedTimes[i2] > 0) {
                i3++;
            }
            i2++;
        }
        return i3 == i;
    }

    public static void onIccAbsent(int i) {
        mSimLoadedTimes[i] = 0;
        Log.d("CarrierConfigLoaderInjector", "mSimLoadedTimes[" + i + "]=" + mSimLoadedTimes[i]);
        DefaultSimManager.getInstance().removeDataSlotListener(sDataSlotListener);
        sEnableDual4G.stop();
    }

    public static void onIccLoaded(int i) {
        long[] jArr = mSimLoadedTimes;
        jArr[i] = jArr[i] + 1;
        Log.d("CarrierConfigLoaderInjector", "mSimLoadedTimes[" + i + "]=" + mSimLoadedTimes[i]);
    }

    private static void handleCtVolteByDefault(Context context, int i) {
        if (mSimLoadedTimes[i] > 1) {
            Log.d("CarrierConfigLoaderInjector", "handleCtVolteByDefault: not first loaded, return");
            return;
        }
        int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(i);
        int userSelectedVolteState = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getUserSelectedVolteState(subscriptionIdForSlot);
        if (!MiuiPhoneUtils.shouldOpenVolteForCt()) {
            if (userSelectedVolteState == -1 || DefaultSimManager.getInstance().getSimInsertStates(i) == 2 || DefaultSimManager.getInstance().getSimInsertStates(i) == 4) {
                if (SubscriptionManager.isValidSubscriptionId(subscriptionIdForSlot)) {
                    ImsAdapterBase.getInstance().setEnhanced4gLteModeSettingDB(context, subscriptionIdForSlot, 0);
                }
                if (userSelectedVolteState != -1) {
                    ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).clearUserSelectedVolteState(subscriptionIdForSlot);
                }
                Log.d("CarrierConfigLoaderInjector", "handleCtVolteByDefault subId:" + subscriptionIdForSlot + ", phoneId:" + i);
            }
        } else if (userSelectedVolteState == -1 && !ImsAdapterBase.getInstance().getEnhanced4gLteModeSetting(context, i)) {
            if (SubscriptionManager.isValidSubscriptionId(subscriptionIdForSlot)) {
                ImsAdapterBase.getInstance().setEnhanced4gLteModeSettingDB(context, subscriptionIdForSlot, 1);
            }
            Log.d("CarrierConfigLoaderInjector", "handleCtVolteByDefault open volte by default, subId:" + subscriptionIdForSlot + ", phoneId:" + i);
        }
        if (MiuiPhoneUtils.isCTDualVolteSupported()) {
            enableViceSlotVolteForDualCtSim();
        }
    }

    public static void enableVolteByDefault(Context context, int i) {
        if (MiuiPhoneUtils.isVolteRegionFeatureEnabled()) {
            return;
        }
        int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(i);
        if (SubscriptionManager.isValidSubscriptionId(subscriptionIdForSlot) && !ImsAdapterBase.getInstance().getEnhanced4gLteModeSetting(context, i)) {
            if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getUserSelectedVolteState(subscriptionIdForSlot) == -1) {
                ImsAdapterBase.getInstance().setEnhanced4gLteModeSettingDB(context, subscriptionIdForSlot, 1);
            } else if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getForceSelectedVolteState(subscriptionIdForSlot) == -1) {
                ImsAdapterBase.getInstance().setEnhanced4gLteModeSettingDB(context, subscriptionIdForSlot, 1);
                ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).saveForceSelectedVolteState(subscriptionIdForSlot, true);
            }
        }
    }

    public static void disableVolteByDefault(Context context, int i) {
        if (ImsAdapterBase.getInstance().getEnhanced4gLteModeSetting(context, i)) {
            int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(i);
            Log.d("CarrierConfigLoaderInjector", "disableVolteByDefault subId:" + subscriptionIdForSlot + ", phoneId:" + i);
            if (subscriptionIdForSlot != SubscriptionManager.INVALID_SUBSCRIPTION_ID) {
                ImsAdapterBase.getInstance().setEnhanced4gLteModeSettingDB(context, subscriptionIdForSlot, 0);
            }
        }
    }

    public static boolean shouldDisableVolteByDefault(Context context, int i) {
        if (i != MiuiPhoneUtils.getDcOnlyVirtualSimSlot()) {
            return false;
        }
        Log.d("CarrierConfigLoaderInjector", "shouldDisableVolteByDefault phoneId:" + i + " is virtual sim");
        return true;
    }

    public static boolean isShowUPlmnPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("support_show_uplmn_preference", false);
    }

    public static void setShowUPlmnPreference(Context context, boolean z) {
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editorEdit.putBoolean("support_show_uplmn_preference", z);
        editorEdit.apply();
    }
}
