package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.preference.PreferenceManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.phone.module.INetworkModule;
import com.android.phone.roaming.RoamingParametersManager;
import com.android.phone.utils.FiveGUtils;
import com.android.phone.utils.MiuiKoreaOperatorCustimizedUtils;
import com.android.phone.utils.MiuiOpconfigUtils;
import com.android.services.telephony.Log;
import miui.os.Build;
import miui.telephony.DefaultSimManager;
import miui.telephony.PhoneDebug;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiLog {
    private static int VERBOSE_DEBUG_OFF = 0;
    private static int VERBOSE_DEBUG_ON = 1;
    private static CloudCellDbHelper mDbHelper;

    enum FeatureType {
        Test1(0);

        private int value;

        FeatureType(int i) {
            this.value = i;
        }

        public String getValue() {
            return String.valueOf(this.value);
        }
    }

    public static void register() {
        PhoneDebug.addListener(new PhoneDebug.Listener() { // from class: com.android.phone.MiuiLog.1
            public void onDebugChanged() {
                boolean z = true;
                Log.DEBUG = PhoneDebug.VDBG || Log.isLoggable(3);
                if (!PhoneDebug.VDBG && !Log.isLoggable(2)) {
                    z = false;
                }
                Log.VERBOSE = z;
                if (PhoneDebug.VDBG) {
                    android.util.Log.v("PhoneGlobals", "onDebugChanged");
                }
            }
        });
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.MiuiLog.2
            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String str;
                int i;
                String str2;
                SQLiteDatabase writableDatabase;
                if ("android.provider.Telephony.SECRET_CODE".equals(intent.getAction())) {
                    String host = intent.getData().getHost();
                    if ("74663".equals(host)) {
                        if (PhoneDebug.VDBG) {
                            Settings.System.putInt(context.getContentResolver(), "phone_debug_flag", MiuiLog.VERBOSE_DEBUG_OFF);
                            android.util.Log.i("PhoneGlobals", "Verbose Debug for telephony is closed");
                            Toast.makeText(context, "Debug for telephony is closed.", 1).show();
                            return;
                        } else {
                            android.util.Log.i("PhoneGlobals", "Verbose Debug for telephony is opened");
                            Toast.makeText(context, "Debug for telephony is opened.", 1).show();
                            Settings.System.putInt(context.getContentResolver(), "phone_debug_flag", MiuiLog.VERBOSE_DEBUG_ON);
                            return;
                        }
                    }
                    int i2 = 0;
                    if ("6722362253".equals(host)) {
                        if (!MiuiPhoneUtils.isNrcaModeManagerSupported()) {
                            android.util.Log.i("PhoneGlobals", "NR CA mode manager not supported");
                            return;
                        }
                        INetworkModule iNetworkModule = (INetworkModule) ModuleDelegate.get(INetworkModule.class);
                        for (int i3 = 0; i3 < MiuiPhoneUtils.PHONE_COUNT; i3++) {
                            android.util.Log.i("PhoneGlobals", "getNrCaStatus is " + iNetworkModule.getNrCaStatus(i3));
                        }
                        boolean z = SystemProperties.getBoolean("persist.radio.nrca", false);
                        if (z) {
                            for (int i4 = 0; i4 < MiuiPhoneUtils.PHONE_COUNT; i4++) {
                                iNetworkModule.setNrCaStatus(i4, z, "secret_code");
                            }
                            SystemProperties.set("persist.radio.nrca", String.valueOf(false));
                            Toast.makeText(context, "setNrCaStatus true is going", 1).show();
                            return;
                        }
                        while (i2 < MiuiPhoneUtils.PHONE_COUNT) {
                            iNetworkModule.setNrCaStatus(i2, z, "secret_code");
                            i2++;
                        }
                        SystemProperties.set("persist.radio.nrca", String.valueOf(true));
                        Toast.makeText(context, "setNrCaStatus false is going", 1).show();
                        return;
                    }
                    if ("86583".equals(host)) {
                        MiuiLog.handleCarrierConfigUpdate(context, "VoLTE", CarrierConfigLoaderInjector.isVolteForceEnabled(context));
                        return;
                    }
                    if ("869434".equals(host)) {
                        MiuiLog.handleCarrierConfigUpdate(context, "VoWiFi", CarrierConfigLoaderInjector.isVoWifiForceEnabled(context));
                        return;
                    }
                    if ("6392255".equals(host)) {
                        if (MiuiPhoneUtils.isSupportNewCall()) {
                            boolean zIsNewCallForceEnabled = CarrierConfigLoaderInjector.isNewCallForceEnabled(context);
                            MiuiLog.handleCarrierConfigUpdate(context, "NewCall", zIsNewCallForceEnabled);
                            while (i2 < MiuiPhoneUtils.PHONE_COUNT) {
                                FiveGManagerBase.getInstance().setDataChannelEnable(i2, !zIsNewCallForceEnabled);
                                i2++;
                            }
                            return;
                        }
                        return;
                    }
                    if ("8667".equals(host)) {
                        if (MiuiPhoneUtils.is5gSaSupported() && MiuiPhoneUtils.isSupportSecretCodeForVoNR()) {
                            if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && !FiveGUtils.isVonrSettingSupportedForMtk()) {
                                Toast.makeText(context, "vonr setting unsupported", 1).show();
                                return;
                            } else if (FiveGUtils.isVonrSwitchVisiable(DefaultSimManager.getDefaultDataSlotId())) {
                                Settings.Global.putInt(PhoneGlobals.getInstance().getContentResolver(), "secret_code_vonr_switch_visiable", 0);
                                Toast.makeText(context, "hide vonr switch", 1).show();
                                return;
                            } else {
                                Settings.Global.putInt(PhoneGlobals.getInstance().getContentResolver(), "secret_code_vonr_switch_visiable", 1);
                                Toast.makeText(context, "display vonr switch", 1).show();
                                return;
                            }
                        }
                        return;
                    }
                    if ("547693784".equals(host)) {
                        if (MiuiPhoneUtils.isFiveGCapable()) {
                            if (Settings.Global.getInt(context.getContentResolver(), "fiveg_power_saving_visible", 0) == 1) {
                                FiveGManagerBase.setFiveGPowerSavingVisible(false);
                                Toast.makeText(context, "Invisible 5G power saving switch", 1).show();
                                return;
                            } else {
                                FiveGManagerBase.setFiveGPowerSavingVisible(true);
                                Toast.makeText(context, "visible 5G power saving switch", 1).show();
                                return;
                            }
                        }
                        return;
                    }
                    if ("54638".equals(host)) {
                        if (!MiuiPhoneUtils.isFiveGCapable() || Build.IS_INTERNATIONAL_BUILD) {
                            return;
                        }
                        if (!"NA".equals(SystemProperties.get("persist.radio.cloud.force.5g.version", "NA")) || FiveGUtils.isLastForcedRecoveryNr() || FiveGUtils.isFiveGInvisableByDefault()) {
                            if (FiveGUtils.isFiveGNetworkForceVisable()) {
                                Settings.Global.putInt(PhoneGlobals.getInstance().getContentResolver(), "secret_code_five_g_visible", 0);
                                MiuiSettings.System.putBoolean(PhoneGlobals.getInstance().getContentResolver(), "enable_pref_network_type", false);
                                Toast.makeText(context, "hide 5g switch and network menu", 1).show();
                            } else {
                                Settings.Global.putInt(PhoneGlobals.getInstance().getContentResolver(), "secret_code_five_g_visible", 1);
                                MiuiSettings.System.putBoolean(PhoneGlobals.getInstance().getContentResolver(), "enable_pref_network_type", true);
                                Toast.makeText(context, "display 5g switch and network menu", 1).show();
                            }
                            Settings.Global.putInt(PhoneGlobals.getInstance().getContentResolver(), "secret_code_five_g_network__visible", -1);
                            Settings.Global.putInt(PhoneGlobals.getInstance().getContentResolver(), "secret_code_five_g_cloud", -1);
                            return;
                        }
                        return;
                    }
                    if ("5476937".equals(host)) {
                        if (MiuiPhoneUtils.isFiveGCapable()) {
                            if (Settings.System.getInt(context.getContentResolver(), "power_center_5g_save_mode", 1) == 1) {
                                Settings.System.putInt(context.getContentResolver(), "power_center_5g_save_mode", 0);
                                Toast.makeText(context, "Disable 5G power optimization", 1).show();
                                return;
                            } else {
                                Settings.System.putInt(context.getContentResolver(), "power_center_5g_save_mode", 1);
                                Toast.makeText(context, "Enable 5G power optimization", 1).show();
                                return;
                            }
                        }
                        return;
                    }
                    if ("42636780".equals(host)) {
                        if (MiuiPhoneUtils.isSupportHEGException()) {
                            Settings.Secure.putString(context.getContentResolver(), "hyper_engine_game_optimization_reason", "REASON_SECRET_CODE");
                            Settings.Secure.putInt(context.getContentResolver(), "hyper_engine_game_optimization_enabled", 0);
                            android.util.Log.i("PhoneGlobals", "Disable HyperEngine game optimization by secret code");
                            return;
                        }
                        return;
                    }
                    if ("42636781".equals(host)) {
                        if (MiuiPhoneUtils.isSupportHEGException()) {
                            Settings.Secure.putString(context.getContentResolver(), "hyper_engine_game_optimization_reason", "REASON_SECRET_CODE");
                            Settings.Secure.putInt(context.getContentResolver(), "hyper_engine_game_optimization_enabled", 1);
                            android.util.Log.i("PhoneGlobals", "Enable HyperEngine game optimization by secret code");
                            return;
                        }
                        return;
                    }
                    if ("932428".equals(host)) {
                        String strValueOf = String.valueOf(!SystemProperties.getBoolean("radio.wechat.secretcode", false));
                        try {
                            SystemProperties.set("radio.wechat.secretcode", strValueOf);
                            Toast.makeText(context, Boolean.valueOf(strValueOf).booleanValue() ? "Enable wechat and call coexist function" : "Disable wechat and call coexist function", 1).show();
                            return;
                        } catch (RuntimeException unused) {
                            android.util.Log.d("PhoneGlobals", "Failed to set radio.wechat.secretcode");
                            return;
                        }
                    }
                    if ("7276937".equals(host)) {
                        if (!MiuiPhoneUtils.isSaPowerOptimizationSupported() || FiveGCloudController.is5GSaPowerDisabledByCloud()) {
                            return;
                        }
                        i2 = Settings.System.getInt(context.getContentResolver(), "sa_power_save_mode", 1) == 1 ? 1 : 0;
                        boolean z2 = i2 ^ 1;
                        Settings.System.putInt(context.getContentResolver(), "sa_power_save_mode", z2 ? 1 : 0);
                        if (FiveGPowerController.getInstance() != null) {
                            FiveGPowerController.getInstance().enableSaPowerOptimization(z2);
                            Toast.makeText(context, i2 != 0 ? "Disable SA power optimization" : "Enable SA power optimization", 1).show();
                            return;
                        }
                        return;
                    }
                    if ("34834".equals(host)) {
                        if (MiuiPhoneUtils.isFiveGCapable()) {
                            FiveGManagerBase.set5GSwitchCarrierAvailable(true);
                            Toast.makeText(context, "Show 5G switch", 1).show();
                            return;
                        }
                        return;
                    }
                    if ("726633".equals(host)) {
                        if (MiuiPhoneUtils.is5gSaSupported() && MiuiPhoneUtils.isSupportSecretCodeForSA()) {
                            int i5 = Settings.System.getInt(context.getContentResolver(), "5g_network_mode_selection_visiable", -1);
                            if (i5 == 1) {
                                Settings.System.putInt(context.getContentResolver(), "5g_network_mode_selection_visiable", 0);
                                Toast.makeText(context, "Hide 5G network mode selection", 1).show();
                            } else if (i5 == 0) {
                                Settings.System.putInt(context.getContentResolver(), "5g_network_mode_selection_visiable", -1);
                                Toast.makeText(context, "Clear 5G network mode Secret code control", 1).show();
                            } else {
                                Settings.System.putInt(context.getContentResolver(), "5g_network_mode_selection_visiable", 1);
                                Toast.makeText(context, "Show 5G network mode selection", 1).show();
                            }
                            FiveGManagerBase.getInstance();
                            FiveGManagerBase.fetchSaModeCarrierStatus();
                            while (i2 < MiuiPhoneUtils.PHONE_COUNT) {
                                FiveGManagerBase.getInstance().updateFiveGSaStatus(i2);
                                i2++;
                            }
                            return;
                        }
                        return;
                    }
                    if ("7282583".equals(host)) {
                        if (MiuiPhoneUtils.is5gSaSupported() && MiuiPhoneUtils.isSupportSecretCodeForSAValue()) {
                            int i6 = Settings.System.getInt(context.getContentResolver(), "5g_sa_mode_disabled", -1);
                            if (i6 == 1) {
                                Settings.System.putInt(context.getContentResolver(), "5g_sa_mode_disabled", 0);
                                Toast.makeText(context, "Enable sa mode", 1).show();
                            } else if (i6 == 0) {
                                Settings.System.putInt(context.getContentResolver(), "5g_sa_mode_disabled", -1);
                                Toast.makeText(context, "Clear sa mode Secret code control", 1).show();
                            } else {
                                Settings.System.putInt(context.getContentResolver(), "5g_sa_mode_disabled", 1);
                                Toast.makeText(context, "Disable sa mode", 1).show();
                            }
                            FiveGManagerBase.getInstance();
                            FiveGManagerBase.fetchSaModeCarrierStatus();
                            while (i2 < MiuiPhoneUtils.PHONE_COUNT) {
                                FiveGManagerBase.getInstance().updateFiveGSaStatus(i2);
                                i2++;
                            }
                            return;
                        }
                        return;
                    }
                    if ("842372".equals(host)) {
                        if (MiuiPhoneUtils.isFiveGCapable() && MiuiPhoneUtils.isSupportSecretCodeForSA()) {
                            if (Settings.System.getInt(context.getContentResolver(), "vice_5g_sa_switch_visiable", 0) == 1) {
                                Settings.System.putInt(context.getContentResolver(), "vice_5g_sa_switch_visiable", 0);
                                Toast.makeText(context, "Hide vice sim SA switch", 1).show();
                                return;
                            } else {
                                Settings.System.putInt(context.getContentResolver(), "vice_5g_sa_switch_visiable", 1);
                                Toast.makeText(context, "Show vice SA switch", 1).show();
                                return;
                            }
                        }
                        return;
                    }
                    if ("544636".equals(host)) {
                        if (ModemProvider.IS_QCOM && MiuiPhoneUtils.isFiveGCapable()) {
                            android.util.Log.i("PhoneGlobals", "start to display the NR signal info.");
                            try {
                                Intent intent2 = new Intent("miui.intent.action.NRDISPLAY");
                                intent2.setFlags(268435456);
                                context.startActivity(intent2);
                                return;
                            } catch (Exception e) {
                                android.util.Log.i("PhoneGlobals", "Failed to start NR signal info: " + e);
                                return;
                            }
                        }
                        return;
                    }
                    if ("46361".equals(host)) {
                        boolean zIsShowUPlmnPreference = CarrierConfigLoaderInjector.isShowUPlmnPreference(context);
                        CarrierConfigLoaderInjector.setShowUPlmnPreference(context, !zIsShowUPlmnPreference);
                        Toast.makeText(context, zIsShowUPlmnPreference ? "Disable UPLMN setting display." : "Enable UPLMN setting display.", 1).show();
                        return;
                    }
                    if ("639276".equals(host)) {
                        try {
                            SystemProperties.set("radio.newapn.secretcode", String.valueOf(!SystemProperties.getBoolean("radio.newapn.secretcode", false)));
                            return;
                        } catch (RuntimeException unused2) {
                            android.util.Log.d("PhoneGlobals", "Failed to set radio.newapn.secretcode");
                            return;
                        }
                    }
                    if ("467".equals(host)) {
                        try {
                            SystemProperties.set("radio.editims.secretcode", String.valueOf(!SystemProperties.getBoolean("radio.editims.secretcode", false)));
                            return;
                        } catch (RuntimeException unused3) {
                            android.util.Log.d("PhoneGlobals", "Failed to set radio.editims.secretcode");
                            return;
                        }
                    }
                    if ("5487587682".equals(host)) {
                        MiuiLog.showNumberChangeDialog();
                        return;
                    }
                    if ("67234".equals(host)) {
                        if (MiuiOpconfigUtils.supportCarrierApex() || MiuiOpconfigUtils.supportCarrierCota()) {
                            Toast.makeText(context, "opcfg version:" + MiuiOpconfigUtils.getOpconfigVersion() + " name:" + MiuiOpconfigUtils.getOpconfigName(), 1).show();
                            return;
                        }
                        Toast.makeText(context, "Not support opcfg!!", 1).show();
                        return;
                    }
                    if ("672341".equals(host)) {
                        if (MiuiOpconfigUtils.supportCarrierCota()) {
                            if (TelephonyManager.getDefault().isCotaOpconfigUsed()) {
                                TelephonyManager.getDefault().setCotaOpconfigUsed(false);
                                Toast.makeText(context, "Cota opconfig disabled, please restart!", 1).show();
                                return;
                            } else {
                                if (SystemProperties.getBoolean("persist.radio.opconfig.used", true)) {
                                    return;
                                }
                                TelephonyManager.getDefault().setCotaOpconfigUsed(true);
                                Toast.makeText(context, "Cota opconfig enabled, please restart!", 1).show();
                                return;
                            }
                        }
                        return;
                    }
                    if ("543732".equals(host)) {
                        if (MiuiPhoneUtils.isDSDASupport()) {
                            android.util.Log.d("PhoneGlobals", "support DSDA");
                            if (FiveGManagerBase.getInstance().getDSDAStateBySetting() == 0) {
                                FiveGManagerBase.getInstance().enableDSDA();
                                Toast.makeText(context, "enabled DSDA", 1).show();
                                return;
                            } else {
                                FiveGManagerBase.getInstance().disableDSDA();
                                Toast.makeText(context, "disabled DSDA", 1).show();
                                return;
                            }
                        }
                        android.util.Log.d("PhoneGlobals", "Failed to support DSDA");
                        return;
                    }
                    if ("2976633".equals(host) || "616633".equals(host) || "6286633".equals(host) || "65686633".equals(host)) {
                        if ("2976633".equals(host)) {
                            i = 2;
                            str = "BWP";
                        } else if ("616633".equals(host)) {
                            i = 3;
                            str = "N1 Mode";
                        } else if ("6286633".equals(host)) {
                            str = "N28 Mode";
                            i = 1;
                        } else {
                            str = "65686633".equals(host) ? "N5 and N8 Mode" : null;
                            i = -1;
                        }
                        if (MiuiPhoneUtils.isModemFeatureSupported(i)) {
                            android.util.Log.d("PhoneGlobals", "support featureType : " + i);
                            if (MiuiPhoneUtils.getModemFeatureFromDb(context, i) == 0) {
                                if (i == -1) {
                                    if (ModemProvider.IS_QCOM) {
                                        while (i2 < MiuiPhoneUtils.PHONE_COUNT) {
                                            FiveGManagerBase.getInstance().setModemFeature(5, 1, i2);
                                            FiveGManagerBase.getInstance().setModemFeature(6, 1, i2);
                                            FiveGManagerBase.getInstance().needRefreshMbn(i2);
                                            i2++;
                                        }
                                    } else if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
                                        while (i2 < MiuiPhoneUtils.PHONE_COUNT) {
                                            FiveGManagerBase.getInstance().setModemFeatureOnSecretCode(5, 1, i2);
                                            FiveGManagerBase.getInstance().setModemFeatureOnSecretCode(6, 1, i2);
                                            i2++;
                                        }
                                    }
                                    MiuiPhoneUtils.setModemFeatureInDb(context, i, 1);
                                } else if (ModemProvider.IS_QCOM) {
                                    FiveGManagerBase.getInstance().setModemFeatureMode(i, 1);
                                } else if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
                                    while (i2 < MiuiPhoneUtils.PHONE_COUNT) {
                                        FiveGManagerBase.getInstance().setModemFeatureOnSecretCode(i, 1, i2);
                                        i2++;
                                    }
                                    MiuiPhoneUtils.setModemFeatureInDb(context, i, 1);
                                }
                                Toast.makeText(context, "enabled " + str, 1).show();
                                return;
                            }
                            if (i == -1) {
                                if (ModemProvider.IS_QCOM) {
                                    for (int i7 = 0; i7 < MiuiPhoneUtils.PHONE_COUNT; i7++) {
                                        FiveGManagerBase.getInstance().setModemFeature(5, 0, i7);
                                        FiveGManagerBase.getInstance().setModemFeature(6, 0, i7);
                                        FiveGManagerBase.getInstance().needRefreshMbn(i7);
                                    }
                                } else if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
                                    for (int i8 = 0; i8 < MiuiPhoneUtils.PHONE_COUNT; i8++) {
                                        FiveGManagerBase.getInstance().setModemFeatureOnSecretCode(5, 0, i8);
                                        FiveGManagerBase.getInstance().setModemFeatureOnSecretCode(6, 0, i8);
                                    }
                                }
                                MiuiPhoneUtils.setModemFeatureInDb(context, i, 0);
                            } else if (ModemProvider.IS_QCOM) {
                                FiveGManagerBase.getInstance().setModemFeatureMode(i, 0);
                            } else if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
                                for (int i9 = 0; i9 < MiuiPhoneUtils.PHONE_COUNT; i9++) {
                                    FiveGManagerBase.getInstance().setModemFeatureOnSecretCode(i, 0, i9);
                                }
                                MiuiPhoneUtils.setModemFeatureInDb(context, i, 0);
                            }
                            Toast.makeText(context, "disabled " + str, 1).show();
                            return;
                        }
                        return;
                    }
                    if ("737678".equals(host)) {
                        if (Build.IS_INTERNATIONAL_BUILD) {
                            return;
                        }
                        MiuiPhoneUtils.reportTelephonyStatistic();
                        Toast.makeText(context, "DONE!", 1).show();
                        return;
                    }
                    if ("794824".equals(host)) {
                        android.util.Log.i("PhoneGlobals", "start to display the secret code switch info.");
                        try {
                            Intent intent3 = new Intent("miui.intent.action.SWITCHDEBUGMENU");
                            intent3.setFlags(268435456);
                            context.startActivity(intent3);
                            return;
                        } catch (Exception e2) {
                            android.util.Log.i("PhoneGlobals", "Failed to start secret code switch info: " + e2);
                            return;
                        }
                    }
                    if ("26822277437".equals(host)) {
                        String str3 = SystemProperties.get("persist.sys.cota.carrier", "");
                        if (str3 != null) {
                            Toast.makeText(context, "Operator Channel: ".concat(str3), 1).show();
                            return;
                        } else {
                            Toast.makeText(context, "Operator Channel: Null", 1).show();
                            return;
                        }
                    }
                    if (MiuiPhoneUtils.isCustForZaMt() && "6865625".equals(host)) {
                        android.util.Log.d("PhoneGlobals", "show MTN unlock");
                        IccNetworkDepersonalizationPanel.showDialog(null, IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_NETWORK.ordinal());
                        return;
                    }
                    if (host.startsWith("968353")) {
                        MiuiLog.handleTelephonyFeature(context, host.substring(6));
                        return;
                    }
                    if ("994636".equals(host)) {
                        if (PhoneDebug.VDBG && SystemProperties.getInt("ro.debuggable", 0) == 1 && TelephonyManagerEx.getDefault().isSupportSatelliteByDevice()) {
                            android.util.Log.i("PhoneGlobals", "start to display sat info.");
                            try {
                                Intent intent4 = new Intent("miui.intent.action.SATDISPLAY");
                                intent4.setFlags(268435456);
                                context.startActivity(intent4);
                                return;
                            } catch (Exception e3) {
                                android.util.Log.d("PhoneGlobals", "Failed to start sat info: " + e3);
                                return;
                            }
                        }
                        return;
                    }
                    if ("68584746".equals(host)) {
                        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                        String string = defaultSharedPreferences.getString("key_multisim_config_restore_scene", "");
                        if (!MiSimRepairManager.isSupportMultiSimConfigSelfRecovery() || TextUtils.equals(string, "config_force_restore_scene")) {
                            return;
                        }
                        String string2 = defaultSharedPreferences.getString("key_multisim_config_last", "");
                        String str4 = SystemProperties.get("persist.radio.multisim.config");
                        if (TextUtils.isEmpty(string2) || TextUtils.equals(string2, str4)) {
                            return;
                        }
                        android.util.Log.d("PhoneGlobals", "Use secret code restore last multisimconfig");
                        defaultSharedPreferences.edit().putString("key_multisim_config_restore_scene", "config_force_restore_scene").putString("key_multisim_config_restore", string2).apply();
                        Toast.makeText(context, "SIM configuration reset, restart to take effect", 1).show();
                        return;
                    }
                    if ("636633".equals(host)) {
                        if (MiuiPhoneUtils.isModemFeatureSupported(15)) {
                            android.util.Log.d("PhoneGlobals", "modemFeatureType : N3 Mode");
                            int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
                            int platformModemFeatureStatus = MiuiPhoneUtils.getPlatformModemFeatureStatus(15, defaultDataSlotId);
                            FiveGManagerBase.ModemFeatureStatus modemFeatureStatus = new FiveGManagerBase.ModemFeatureStatus(defaultDataSlotId, 15, platformModemFeatureStatus == 0 ? 1 : 0);
                            if (platformModemFeatureStatus == 0) {
                                FiveGManagerBase.getInstance().setModemFeatureBySlot(modemFeatureStatus);
                                Toast.makeText(context, "enabled N3 Mode", 1).show();
                                return;
                            } else {
                                FiveGManagerBase.getInstance().setModemFeatureBySlot(modemFeatureStatus);
                                Toast.makeText(context, "disable N3 Mode", 1).show();
                                return;
                            }
                        }
                        return;
                    }
                    if (MiuiPhoneUtils.isSignalMapOptimizationSupported() && "744625627".equals(host)) {
                        MiuiLog.mDbHelper = CloudCellDbHelper.getInstance(context);
                        if (MiuiLog.mDbHelper == null || (writableDatabase = MiuiLog.mDbHelper.getWritableDatabase()) == null) {
                            return;
                        }
                        MiuiLog.mDbHelper.clearSignalMapTable(writableDatabase);
                        MiuiLog.mDbHelper.clearSignalMapHstTable(writableDatabase);
                        MiuiLog.mDbHelper.clearSignalMapNOMLTable(writableDatabase);
                        if (Settings.System.getInt(context.getContentResolver(), "clear_signalmap_db", 0) == 0) {
                            Settings.System.putInt(context.getContentResolver(), "clear_signalmap_db", 1);
                            Toast.makeText(context, "signalmap db has been cleared, not supported by default", 1).show();
                            return;
                        } else {
                            Settings.System.putInt(context.getContentResolver(), "clear_signalmap_db", 0);
                            Toast.makeText(context, "signalmap db has been cleared, support by default", 1).show();
                            return;
                        }
                    }
                    if ("76260".equals(host) || "76261".equals(host)) {
                        "76260".equals(host);
                        RoamingParametersManager.getInstance();
                        return;
                    }
                    if ("7469542".equals(host)) {
                        if (Build.IS_INTERNATIONAL_BUILD) {
                            boolean z3 = SystemProperties.getBoolean("persist.secretcode.show_5ga", false);
                            SystemProperties.set("persist.secretcode.show_5ga", String.valueOf(!z3));
                            Toast.makeText(context, z3 ? "Hide 5GA in developer mode" : "Show 5GA in developer mode", 1).show();
                            return;
                        }
                        return;
                    }
                    if ("728355483".equals(host) && TelephonyManagerEx.getDefault().isSupportSatelliteByDevice()) {
                        String strValueOf2 = String.valueOf(!SystemProperties.getBoolean("persist.radio.satellite.sim.enabled", false));
                        try {
                            SystemProperties.set("persist.radio.satellite.sim.enabled", strValueOf2);
                            if (Boolean.valueOf(strValueOf2).booleanValue()) {
                                str2 = "Allow all cards to use satellite is opened.";
                            } else {
                                str2 = "Allow all cards to use satellite is closed.";
                            }
                            Toast.makeText(context, str2, 1).show();
                            return;
                        } catch (Exception unused4) {
                            android.util.Log.i("PhoneGlobals", "Failed to set persist.radio.satellite.sim.enabled");
                            return;
                        }
                    }
                    if ("5200805".equals(host)) {
                        if (!ModemProvider.IS_QCOM || ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).isModemDataCollecting()) {
                            return;
                        }
                        ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).startModemDataCollectorTest();
                        return;
                    }
                    if ("5200806".equals(host)) {
                        if (ModemProvider.IS_QCOM) {
                            ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).stopModemDataCollectorTest();
                        }
                    } else if ("94649264".equals(host)) {
                        if (!TelephonyManager.getDefault().isSupportSatelliteByDevice()) {
                            android.util.Log.d("PhoneGlobals", "not support satellite");
                            return;
                        }
                        boolean z4 = SystemProperties.getBoolean("radio.satellite.xingwang", false);
                        try {
                            SystemProperties.set("radio.satellite.xingwang", String.valueOf(!z4));
                        } catch (RuntimeException unused5) {
                            android.util.Log.d("PhoneGlobals", "Failed to set radio.satellite.xingwang");
                        }
                        Toast.makeText(context, "xingwang 1 ".concat(!z4 ? "enable" : "disable"), 1).show();
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SECRET_CODE");
        intentFilter.addDataScheme("android_secret_code");
        PhoneGlobals.getInstance().registerReceiver(broadcastReceiver, intentFilter, 2);
    }

    private static String getFeatureType(String str) {
        for (FeatureType featureType : FeatureType.values()) {
            if (featureType.getValue().equals(str)) {
                return String.valueOf(featureType);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleTelephonyFeature(Context context, String str) {
        String featureType = getFeatureType(str);
        if (TextUtils.isEmpty(featureType)) {
            return;
        }
        String[] strArrSplit = SystemProperties.get("persist.radio.telepony.feature.secretcode", "").split(",");
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (String str2 : strArrSplit) {
            if (!TextUtils.isEmpty(str2)) {
                if (str2.equals(featureType)) {
                    Toast.makeText(context, "close " + featureType, 1).show();
                    z = false;
                } else {
                    sb.append(str2);
                    sb.append(",");
                }
            }
        }
        if (z) {
            Toast.makeText(context, "open " + featureType, 1).show();
            sb.append(featureType);
            sb.append(",");
        }
        String string = sb.toString();
        SystemProperties.set("persist.radio.telepony.feature.secretcode", string.equals("") ? "" : string.substring(0, string.length() - 1));
    }

    public static void showNumberChangeDialog() {
        if (MiuiKoreaOperatorCustimizedUtils.isCustForKrLgu()) {
            Phone phone = MiuiPhoneUtils.getPhone(DefaultSimManager.getDefaultDataSlotId());
            boolean roaming = (phone == null || phone.getServiceState() == null) ? false : phone.getServiceState().getRoaming();
            PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
            Intent intent = new Intent(phoneGlobals, (Class<?>) MiuiErrorDialogActivity.class);
            intent.setFlags(268435456);
            intent.putExtra("dialog_type", 12);
            intent.putExtra("is_roaming", roaming);
            phoneGlobals.startActivity(intent);
            android.util.Log.i("PhoneGlobals", "showNumberChangeDialog isRoaming=" + roaming);
        }
    }

    public static void handleCarrierConfigUpdate(Context context, String str, boolean z) {
        StringBuilder sb;
        String str2;
        if (MiuiImsPhoneUtils.supportSecretCodeForVolteAndWfc()) {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                if (MiuiPhoneUtils.getIccRecordsLoaded(i)) {
                    if ("VoLTE".equals(str)) {
                        CarrierConfigLoaderInjector.setVolteForceEnabled(context, !z);
                    } else if ("VoWiFi".equals(str)) {
                        CarrierConfigLoaderInjector.setVoWifiForceEnabled(context, !z);
                    } else if ("NewCall".equals(str)) {
                        CarrierConfigLoaderInjector.setNewCallForceEnabled(context, !z);
                    }
                    PhoneProxy.updateConfigForPhoneId(context, i, "LOADED");
                }
            }
            if (z) {
                sb = new StringBuilder();
                sb.append(str);
                str2 = " carrier check was enabled.";
            } else {
                sb = new StringBuilder();
                sb.append(str);
                str2 = " carrier check was disabled.";
            }
            sb.append(str2);
            String string = sb.toString();
            android.util.Log.i("PhoneGlobals", string);
            Toast.makeText(context, string, 1).show();
        }
    }
}
