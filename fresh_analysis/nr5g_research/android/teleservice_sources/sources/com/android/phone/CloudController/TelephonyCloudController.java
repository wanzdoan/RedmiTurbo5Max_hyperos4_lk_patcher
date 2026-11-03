package com.android.phone.CloudController;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import com.android.phone.CloudModemVolteController;
import com.android.phone.FiveGCloudController;
import com.android.phone.FiveGManagerBase;
import com.android.phone.MiuiCloudCarrierConfigManager;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.badcells.BadCellStats;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.module.IModemModule;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.statistics.utils.DeviceUtil;
import com.android.services.telephony.ims.ImsAdapterBase;
import com.xiaomi.mirror.synergy.MiuiSynergySdk;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyCloudController extends Handler {
    public static boolean CLOUD_BAD_CELL_OPT_DISABLED;
    public static boolean CLOUD_CONFIG_BASEMENT_FEATURE_DISABLED;
    public static boolean CLOUD_CONFIG_DYNAMIC_ANTENNA_TUNING_FEATURE_ENABLED;
    public static boolean CLOUD_CONTROLLER_CALL_DSDA_OPTIMIZATION_DISCONNECT_SCENE_DISABLED;
    public static boolean CLOUD_CONTROLLER_CALL_END_V2_DISABLED;
    public static boolean CLOUD_CONTROLLER_CALL_END_V3_DISABLED;
    public static boolean CLOUD_CONTROLLER_CRBT_FORCE_ENABLE;
    public static boolean CLOUD_CONTROLLER_IMS_IMSREGISTER_V1_DISABLED;
    public static boolean CLOUD_CONTROLLER_NETWORK_RECOVERY_DISABLED;
    public static boolean CLOUD_CONTROLLER_SMART_CALL_FORWARD_DISABLED;
    private static volatile boolean CLOUD_FAKESASTION_OPT_DISABLED;
    public static boolean CLOUD_FDS_LOG_UPLOAD_DISABLED;
    private static volatile boolean CLOUD_FIVEGA_OPT_DISABLED;
    public static volatile boolean CLOUD_IMS_SIGNAL_OPT_DISABLED;
    public static boolean CLOUD_IRAT_PINGPONG_OPT_DISABLED;
    private static volatile boolean CLOUD_MEASURE_RELAX_OPT_DISABLED;
    public static volatile boolean CLOUD_NETWORK_FAST_RECOVERY_OPT_DISABLED;
    public static volatile boolean CLOUD_NSA_SCG_POOR_OPT_DISABLED;
    public static boolean CLOUD_OPTIMIZATION_ACTIVITY_INFO_REQUEST_DISABLED;
    public static boolean CLOUD_SA_POWER_SCENE_THERMAL_MODE_DISABLED;
    private static volatile boolean CLOUD_SLEEP_PROTECT_OPT_DISABLED;
    public static volatile boolean CLOUD_SMART_NETWORK_GAME_OPT_DISABLED;
    public static boolean CLOUD_SNA_DETECT_OPT_DISABLED;
    public static boolean CLOUD_SNA_NETWORK_RECOVERY_DISABLED;
    public static boolean CLOUD_SUPPORT_MULTISIM_CONFIG_SELF_RECOVERY_DISABLED;
    public static boolean CLOUD_SUPPORT_SIM_ABNORMAL_SELF_RECOVERY_DISABLED;
    public static boolean CLOUD_TIERED_POWER_SAVING_OPT_DISABLED;
    public static boolean CLOUD_VOICE_LINK_FEATURE_DISABLED;
    private static Context mContext;
    private static TelephonyCloudController mInstance;
    public static volatile int[] mNsaScgPoorThreshold = {MiuiSynergySdk.SEND_APP_FAIL_REMOTE_BUSY_WORLD, 8, -6};
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.CloudController.TelephonyCloudController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            TelephonyCloudController.log("onReceive intent: " + action);
            if (action.equals("com.android.phone.intent.action.CLOUD_TELE_FEATURE_INFO_CHANGED")) {
                TelephonyCloudController.this.sendEmptyMessage(1);
            }
        }
    };

    public void init() {
        mContext = PhoneGlobals.getInstance();
        Log.d("TelephonyCloudController", "TelephonyCloudController init");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.CLOUD_TELE_FEATURE_INFO_CHANGED");
        mContext.registerReceiver(this.mReceiver, intentFilter, 2);
    }

    public static synchronized TelephonyCloudController getInstance() {
        try {
            if (mInstance == null) {
                mInstance = new TelephonyCloudController();
            }
        } catch (Throwable th) {
            throw th;
        }
        return mInstance;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (message.what == 1) {
            log("cloud controller changed");
            updateCloudControlInfo();
        } else {
            log("into default");
        }
    }

    private static void updateCloudControlInfo() {
        BadCellStats badCellStats;
        if (MiuiPhoneUtils.isSupportHangUpoptimize3_0()) {
            CLOUD_CONTROLLER_CALL_END_V2_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("optimization_call_end2");
            CLOUD_CONTROLLER_CALL_END_V3_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("optimization_call_end3");
        }
        if (MiuiPhoneUtils.isDeviceDsdaSupportedByQcom()) {
            CLOUD_CONTROLLER_CALL_DSDA_OPTIMIZATION_DISCONNECT_SCENE_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("optimization_call_dsda_optimization_disconnect_scene");
        }
        if (MiuiPhoneUtils.isSupportDisableCrbt()) {
            boolean zIsFeatureDisabledByCloud = FiveGCloudController.isFeatureDisabledByCloud("optimization_config_crbt_feature");
            CLOUD_CONTROLLER_CRBT_FORCE_ENABLE = zIsFeatureDisabledByCloud;
            if (zIsFeatureDisabledByCloud && Settings.Global.getInt(mContext.getContentResolver(), "button_crbt_mode", 1) == 0) {
                FiveGManagerBase.getInstance().setCrbtDisable(false);
            }
        }
        if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isSupportSmartCallForwardByDevice()) {
            CLOUD_CONTROLLER_SMART_CALL_FORWARD_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("optimization_config_smart_call_forward_feature");
        }
        if (MiuiPhoneUtils.isSaThermalControlSupported()) {
            CLOUD_SA_POWER_SCENE_THERMAL_MODE_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("optimization_sa_power_scene_thermal_mode");
        }
        if (MiuiPhoneUtils.isSupportBasementOptimization()) {
            CLOUD_CONFIG_BASEMENT_FEATURE_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("optimization_config_basement_feature");
        }
        if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isSupportImsRegOptByDevice(mContext)) {
            CLOUD_CONTROLLER_IMS_IMSREGISTER_V1_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("optimization_ims_register1");
        }
        CLOUD_OPTIMIZATION_ACTIVITY_INFO_REQUEST_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("optimization_activity_info_request");
        CLOUD_CONFIG_DYNAMIC_ANTENNA_TUNING_FEATURE_ENABLED = FiveGCloudController.isFeatureEnabledByCloud("optimization_config_dynamic_antenna_tuning_feature");
        CLOUD_SUPPORT_MULTISIM_CONFIG_SELF_RECOVERY_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("support_multisim_config_self_recovery");
        CLOUD_SUPPORT_SIM_ABNORMAL_SELF_RECOVERY_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("support_sim_abnormal_self_recovery");
        CLOUD_IRAT_PINGPONG_OPT_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("optimization_irat_pingpong_feature");
        CLOUD_TIERED_POWER_SAVING_OPT_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("optimization_tierer_powersaving");
        CLOUD_BAD_CELL_OPT_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("optimization_bad_cell");
        CLOUD_SNA_DETECT_OPT_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("optimization_sna_detect_feature");
        CLOUD_SNA_NETWORK_RECOVERY_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("optimization_sna_network_recovery");
        CLOUD_FDS_LOG_UPLOAD_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("fds_log_upload_config");
        Settings.Global.putInt(mContext.getContentResolver(), "nrca_5g_plus", FiveGCloudController.isFeatureEnabledByCloud("nrca_show_5g_plus") ? 1 : 0);
        boolean zIsFeatureDisabledByCloud2 = FiveGCloudController.isFeatureDisabledByCloud("voice_link_feature");
        CLOUD_VOICE_LINK_FEATURE_DISABLED = zIsFeatureDisabledByCloud2;
        if (zIsFeatureDisabledByCloud2) {
            ImsAdapterBase.getInstance().setCrossSimCallingDisabledByCloud(mContext);
        }
        MiuiCloudCarrierConfigManager.getInstance().cloudChanged();
        if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && (DeviceUtil.isModemVolteEnabled() || MiuiPhoneUtils.isSupportVolteGlobalization())) {
            CloudModemVolteController.getInstance().cloudChanged();
        }
        FiveGCloudController.FeatureCloudInfo featureInfoByCloud = FiveGCloudController.getFeatureInfoByCloud("optimization_config_dynamic_bad_cell_event_weight");
        if (featureInfoByCloud != null && (badCellStats = BadCellStats.getInstance()) != null) {
            badCellStats.changeWeightsByCloudInfo(featureInfoByCloud);
        }
        if (MiuiPhoneUtils.isSupportNetworkRecoveryOptimization(mContext)) {
            CLOUD_CONTROLLER_NETWORK_RECOVERY_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("support_network_recovery");
        }
        if (MiuiPhoneUtils.isSupportFakeStationOptimization()) {
            CLOUD_FAKESASTION_OPT_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("fakesastion_opt");
            if (CLOUD_FAKESASTION_OPT_DISABLED) {
                ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).resetFakeStationList();
            }
        }
        if (MiuiPhoneUtils.isSupportSmartNetworkGameOptimization()) {
            CLOUD_SMART_NETWORK_GAME_OPT_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("smart_network_game_feature");
        }
        if (MiuiPhoneUtils.isPoorNetworkFastRecoverySupported()) {
            CLOUD_NETWORK_FAST_RECOVERY_OPT_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("network_fast_recovery_feature");
        }
        if (MiuiPhoneUtils.isSupportPowerSaveV2()) {
            CLOUD_MEASURE_RELAX_OPT_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("measure_relax_opt");
            if (CLOUD_MEASURE_RELAX_OPT_DISABLED) {
                ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).reduceIdleNeighborMea(false, 16);
            }
        }
        if (MiuiPhoneUtils.isImsSignalOptimizationSupported(mContext)) {
            CLOUD_IMS_SIGNAL_OPT_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("ims_signal_opt");
            if (CLOUD_IMS_SIGNAL_OPT_DISABLED) {
                ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).resetImsSignalOptimizationToDefault();
            }
        }
        if (MiuiPhoneUtils.isSupportSleepProtectOptimization()) {
            CLOUD_SLEEP_PROTECT_OPT_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("sleep_protect_opt");
            if (CLOUD_SLEEP_PROTECT_OPT_DISABLED) {
                ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).resetSleepProtectModeByCloud();
            }
        }
        if (MiuiPhoneUtils.isNsaScgPoorOptimizationSupported()) {
            CLOUD_NSA_SCG_POOR_OPT_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("nsa_scg_poor_opt");
            FiveGCloudController.FeatureCloudInfo featureInfoByCloud2 = FiveGCloudController.getFeatureInfoByCloud("nsa_scg_poor_opt");
            if (featureInfoByCloud2 != null) {
                String[] strArrSplit = featureInfoByCloud2.getParams1().split(",");
                if (strArrSplit.length == 3) {
                    try {
                        mNsaScgPoorThreshold[0] = Integer.parseInt(strArrSplit[0].trim());
                        mNsaScgPoorThreshold[1] = Integer.parseInt(strArrSplit[1].trim());
                        mNsaScgPoorThreshold[2] = Integer.parseInt(strArrSplit[2].trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (MiuiPhoneUtils.isSupportFivegaOptimization()) {
            CLOUD_FIVEGA_OPT_DISABLED = FiveGCloudController.isFeatureDisabledByCloud("fivega_opt");
            if (CLOUD_FIVEGA_OPT_DISABLED) {
                ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).limitNwCA(false, 1);
            }
        }
        if (MiuiPhoneUtils.isDeviceModemTcpKaSupported()) {
            ((IModemModule) ModuleDelegate.get(IModemModule.class)).onModemTcpKaCloudChanged(FiveGCloudController.isFeatureDisabledByCloud("modem_tcp_keepalive"));
        }
    }

    public static boolean getCloudControllerFivegaOptDisabled() {
        return CLOUD_FIVEGA_OPT_DISABLED;
    }

    public static boolean getCloudControllerFakeStationOptDisable() {
        return CLOUD_FAKESASTION_OPT_DISABLED;
    }

    public static boolean getCloudControllerSleepProtectOptDisabled() {
        return CLOUD_SLEEP_PROTECT_OPT_DISABLED;
    }

    public static boolean getCloudControllerMeasureRelaxOptDisabled() {
        return CLOUD_MEASURE_RELAX_OPT_DISABLED;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("TelephonyCloudController", str);
    }
}
