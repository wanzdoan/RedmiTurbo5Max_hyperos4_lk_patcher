package com.android.phone;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.telephony.RadioAccessFamily;
import android.text.TextUtils;
import android.util.Log;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.module.INetworkModule;
import com.android.phone.module.ISatelliteModule;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.module.IUtilityModule;
import com.android.phone.utils.FiveGUtils;
import com.android.phone.utils.ModemUtils;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import com.xiaomi.telephony.ExtTelephonyManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import miuix.animation.internal.AnimTask;

/* JADX INFO: loaded from: classes.dex */
public class FiveGCloudController implements LocationListener {
    private static String LOG_TAG = "FiveGCloudController";
    private static String mCity;
    private static String mCloudCdmaDataOptimizeEnable;
    private static String mCloudCdmaDataOptimizeTime;
    private static Context mContext;
    private static String mLastCity;
    private static String mLastProvince;
    private static CloudModemComponentData mModemComponentData;
    private static CloudPowerSaveData mPowerSaveData;
    private static String mProvince;
    private static FiveGCloudSmartSwitchData mSmartSwitchData;
    private static CloudSpecialNetworkData mSpecialNetworkData;
    private static CloudTrafficControlData mTrafficControlData;
    private static FiveGCloudController sInstance;
    private LocationListener mCrowdSignalListener;
    private ModemUtils mHydra;
    private static HashMap sSaSimOperatorStatusMap = new HashMap();
    private static HashMap sSaLastEffectiveSimOperatorStatusMap = new HashMap();
    private static HashMap sVonrOperatorStatusMap = new HashMap();
    private static HashMap sVonrLastEffectiveSimOperatorStatusMap = new HashMap();
    private static HashMap sFeatureCloudStatusMap = new HashMap();
    private static HashMap sFeatureCloudInfoMap = new HashMap();
    private static HashMap sLastFeatureCloudInfoMap = new HashMap();
    private static HashMap mModefeatureOperatorStatusMap = new HashMap() { // from class: com.android.phone.FiveGCloudController.1
        {
            put(1, null);
            put(3, null);
            put(2, null);
        }
    };
    private static final String[] DUAL_DATA_CONCURRENT_MODE_WHITE_LIST_DEFAULT = {"com.android.providers.downloads.ui", "com.android.providers.downloads", "com.xiaomi.market", "org.zwanoo.android.speedtest", "com.xiaomi.mipicks"};
    private static final String[] DUAL_DATA_REDUNDANT_MODE_WHITE_LIST_DEFAULT = {"com.tencent.tmgp.sgame", "com.tencent.tmgp.pubgmhd", "com.tencent.lolm", "com.miHoYo.ys.mi", "com.tencent.tmgp.cf"};
    private static final String[] HIKING_MODE_CITY_WHITE_LIST_DEFAULT = {"Beijing", "Nanjing", "Xian", "Shenzhen", "Guangzhou", "Chengdu", "Wuhan", "Suzhou", "Zhengzhou", "Tianjin", "Changsha", "Dongguan", "Ningbo", "Foshan", "Hefei", "Qingdao"};
    private static final String[] SUBWAY_MODE_SIGNAL_THRESHOLD_DEFAULT = {"-108", "-100", "-90", "-110", "-90"};
    private static final String[] FEATURE_CLOUD_STATISTIC_DEFAULT = {"Telephony5GDsdaSwitch", "Telephony5GSaSwitch", "TelephonyN1Mode", "TelephonyN28Mode", "TelephonyBWPMode", "TelephonyVonrSwitch", "TelephonyViceSaSwitch"};
    private static int sFiveGSwitchCloudStatus = -1;
    private static int sFiveGPowerCloudStatus = -1;
    private static int sSAPowerCloudStatus = -1;
    private static int sSAPowerCloudEnableScene = 0;
    private static int sFiveGGameCloudStatus = -1;
    private static int sSAPowerCloudCdrxFequencyCheckTime = -1;
    private static int sViceSASwitchCloudStatus = -1;
    private static String sModemCrashReasonFromCloud = "";
    private static int sFiveGDsdaCloudStatus = -1;
    private static int sULMimoOptimizationCloudStatus = -1;
    private static int sMtkPingPongCloudStatus = -1;
    private static int sModemMeasureRelaxCloudStatus = -1;
    private static final String[] FREQ_OPT_CONFIG_KEYS = {"freq_opt_framework_enable", "freq_opt_rsrp_threshold", "freq_opt_sinr_threshold", "freq_opt_rate_threshold", "freq_opt_min_interval", "freq_opt_handover_timeout", "freq_opt_fdd_tdd_enable", "freq_opt_n79_hotspot_enable", "freq_opt_nr_lte_enable", "freq_opt_nr_lte_temp_timeout", "freq_opt_nr_lte_temp_min_duration", "freq_opt_nr_lte_learn_threshold", "freq_opt_nr_lte_ttl", "freq_opt_nr_lte_rat_check_delay", "freq_opt_nr_lte_rat_check_retries", "freq_opt_nr_lte_stutter_window", "freq_opt_nr_lte_qoe_threshold"};
    private boolean mWlanDisSADisabledByCloud = false;
    private boolean mARDOptimizationDisabledByCloud = false;
    private boolean mThermalThrottleDisabledByCloud = false;
    private boolean mLimitLTECAForNightModeDisabledByCloud = false;
    private boolean mModemNonddsRrcDisabledByCloud = false;
    private String mLocationProvider = null;
    private final ExecutorService mCrowdSignalExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() { // from class: com.android.phone.FiveGCloudController$$ExternalSyntheticLambda0
        @Override // java.util.concurrent.ThreadFactory
        public final Thread newThread(Runnable runnable) {
            return FiveGCloudController.m563$r8$lambda$Xh3h0IaBShskCz5Epxm8kKSos(runnable);
        }
    });
    private volatile long mLastCrowdSignalTimeMs = 0;
    private volatile long mLastGeocodeTimeMs = -1800000;
    private volatile String mCachedGeocodeCity = "";

    @Override // android.location.LocationListener
    public void onProviderDisabled(String str) {
    }

    @Override // android.location.LocationListener
    public void onProviderEnabled(String str) {
    }

    @Override // android.location.LocationListener
    public void onStatusChanged(String str, int i, Bundle bundle) {
    }

    /* JADX INFO: renamed from: $r8$lambda$Xh-3h0IaBShs-kCz5Epxm8kKSos, reason: not valid java name */
    public static /* synthetic */ Thread m563$r8$lambda$Xh3h0IaBShskCz5Epxm8kKSos(Runnable runnable) {
        return new Thread(runnable, "crowd-signal-worker");
    }

    public static void init() {
        if (sInstance == null) {
            FiveGCloudController fiveGCloudController = new FiveGCloudController();
            sInstance = fiveGCloudController;
            fiveGCloudController.startCrowdSignalUpdates();
        }
    }

    public static FiveGCloudController getInstance() {
        return sInstance;
    }

    private FiveGCloudController() {
        mContext = PhoneGlobals.getInstance();
    }

    public int fetch5GSwitchCloudStatus() {
        return fetch5GCloudStatus("Telephony5GSwitch");
    }

    private int fetch5GPowerCloudStatus() {
        return fetch5GCloudStatus("Telephony5GPower");
    }

    private void fetchULMimoOptimizationCloudStatus() {
        int iFetch5GCloudStatus = fetch5GCloudStatus("TelephonyULMimoOptimization");
        sULMimoOptimizationCloudStatus = iFetch5GCloudStatus;
        if (iFetch5GCloudStatus != 0 || ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getUlMimoStatus() == 0) {
            return;
        }
        ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).setUlMimoStatus(true);
    }

    public void fetchSAPowerCloudStatus() {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "TelephonySAPower");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    log("fetchSAPowerCloudStatus data=" + cloudData.toString());
                    FiveGCloudData fiveGCloudData = new FiveGCloudData(cloudData);
                    int fiveGCloudStatus = fiveGCloudData.getFiveGCloudStatus("TelephonySAPower");
                    sSAPowerCloudCdrxFequencyCheckTime = fiveGCloudData.getCloudCdrxFequencyCheckTime();
                    if (fiveGCloudStatus != -1) {
                        sSAPowerCloudStatus = fiveGCloudStatus;
                    }
                    int i = sSAPowerCloudStatus;
                    if (i == 1) {
                        int cloudEnableScene = fiveGCloudData.getCloudEnableScene();
                        if (cloudEnableScene != 0) {
                            sSAPowerCloudEnableScene = cloudEnableScene;
                        }
                    } else if (i == 0) {
                        sSAPowerCloudEnableScene = 0;
                    }
                }
                return;
            }
            log("fetchSAPowerCloudStatus cloudList is empty");
        } catch (Exception e) {
            log("fetchSAPowerCloudStatus exception" + e);
        }
    }

    public void fetchAll5GCloudStatus(boolean z) {
        log("fetchAll5GCloudStatus Enter");
        sFiveGSwitchCloudStatus = fetch5GSwitchCloudStatus();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            FiveGManagerBase.setFiveGSwitchVisible(MiuiPhoneUtils.isFiveGSettingViewVisible(i), i);
        }
        sFiveGPowerCloudStatus = fetch5GPowerCloudStatus();
        fetchSAPowerCloudStatus();
        fetch5GCloudSmartSwitchData();
        sFiveGGameCloudStatus = fetch5GGameCloudStatus();
        fetchTrafficControlData();
        fetch5GSaCloudStatus("Telephony5GSaSwitch", z);
        fetchWhiteList("TelephonyDsda2DsdsWhiteList", z, "scene_dsda_change_dsds_white_list_pkg_name", "com.android.phone.intent.action.CLOUD_DSDA_WHITE_LIST_UPDATE_DONE");
        fetchWhiteList("TelephonySmartSwitchWhiteList", z, "smart_switch_alert_dialog_white_list_pkg_name", "com.android.phone.intent.action.CLOUD_SMART_SWITCH_WHITE_LIST_UPDATE_DONE");
        fetchWhiteList("TelephonyLteNrPingPongWhiteList", z, "ltenr_ping_pong_white_list_pkg_name", "com.android.phone.intent.action.CLOUD_PING_PONG_LIST_DONE");
        fetchWhiteList("TelephonyCommonSignalWhiteList", z, "common_signal_white_list_pkg_name", "com.android.phone.intent.action.CLOUD_COMMON_SIGNAL_LIST_DONE");
        fetchWhiteList("TelephonyMeetingOptimizationWhiteList", z, "meeting_optimization_white_list_pkg_name", "com.android.phone.intent.action.CLOUD_MEETING_LIST_DONE");
        fetchWhiteList("TelephonyDualDataConcurrentModeWhiteList", z, "dual_data_concurrent_mode_white_list_pkg_name", "com.android.phone.intent.action.DUAL_DATA_CONCURRENT_MODE_WHITE_LIST_DONE");
        fetchWhiteList("TelephonyDualDataConcurrentLimitedWhiteList", z, "dual_data_concurrent_limited_white_list_pkg_name", "com.android.phone.intent.action.DUAL_DATA_CONCURRENT_LIMITED_WHITE_LIST_DONE");
        fetchWhiteList("TelephonyDualDataRedundantModeWhiteList", z, "dual_data_redundant_mode_white_list_pkg_name", "com.android.phone.intent.action.DUAL_DATA_REDUNDANT_MODE_WHITE_LIST_DONE");
        fetchWhiteList("TelephonyHikingModeCityWhiteList", z, "hiking_mode_city_white_list", "com.android.phone.intent.action.CLOUD_HIKING_MODE_CITY_LIST_DONE");
        fetchSwitchStatus("TelephonyHstPreferFreqSwitch", z, "HST_PREFER_FREQ_SWITCH", "com.android.phone.intent.action.CLOUD_HST_PREFER_FREQ_SWITCH");
        fetchSwitchStatus("TelephonyHyperEngineGameSwitch", z, "HYPER_ENGINE_GAME_SWITCH", "com.android.phone.intent.action.CLOUD_HYPER_ENGINE_GAME_SWITCH");
        fetchSwitchStatus("TelephonyHyperEngineHstSwitch", z, "HYPER_ENGINE_HST_SWITCH", "com.android.phone.intent.action.CLOUD_HYPER_ENGINE_HST_SWITCH");
        fetchSwitchStatus("TelephonyHyperEngineSmartHOSwitch", z, "HYPER_ENGINE_SMART_HO_SWITCH", "com.android.phone.intent.action.CLOUD_HYPER_ENGINE_SMARTHO_SWITCH");
        fetchWhiteList("TelephonyResolutionVideoAppsWhiteList", z, "resolution_video_apps_white_list_pkg_name", "com.android.phone.intent.action.ACTION_CLOUD_RESOLUTION_VIDEO_APPS_LIST_DONE");
        fetchWhiteList("TelephonySubwayModeSignalThreshold", z, "subway_mode_signal_threshold", "com.android.phone.intent.action.ACTION_CLOUD_SUBWAY_MODE_SIGNAL_THRESHOLD_DONE");
        fetchWhiteList("TelephonyPreloadVideoAppsWhiteList", z, "preload_video_apps_white_list_pkg_name", "com.android.phone.intent.action.ACTION_CLOUD_PRELOAD_VIDEO_APPS_LIST_DONE");
        fetchWhiteList("TelephonyHyperEngineGameWhiteList", z, "hyper_engine_game_white_list_pkg_name", "com.android.phone.intent.action.ACTION_CLOUD_HYPER_ENGINE_GAME_WHITE_LIST_DONE");
        fetchWhiteList("CellularNetworkBoostSdkWhiteList", z, "cellular_networkboost_sdk_white_list_pkg_name", "com.android.phone.intent.action.CLOUD_CELLULAR_NETWORKBOOST_SDK_LIST_DONE");
        fetchSwitchStatus("TelephonyAirportModeSwitch", z, "airport_mode_switch", "com.android.phone.intent.action.CLOUD_AIRPORT_MODE_SWITCH");
        fetchSwitchStatus("TelephonyArrearSimNwDeniedMode", z, "arrear_sim_nw_denied_mode", "com.android.phone.intent.action.ACTION_CLOUD_ARREAR_SIM_NW_DENIED_DONE");
        fetchSwitchStatus("TelephonyHstTunnelSmartlink", z, "hst_tunnel_smartlink", "com.android.phone.intent.action.ACTION_CLOUD_HST_TUNNEL_SMARTLINK_DONE");
        fetchSwitchStatus("TelephonySmartNetworkCongestionOpt", z, "smart_network_switch", "com.android.phone.intent.action.CLOUD_SMART_NETWORK_SWITCH");
        fetchSwitchStatus("TelephonyFivegLBStreamOpt", z, "fiveg_lb_stream_opt", "com.android.phone.intent.action.ACTION_CLOUD_FIVEG_LB_STREAM_OPT_DONE");
        fetchQmsSwitchCloudStatus("TelephonyQmsSwitch", z);
        fetchSignalMapSnsCellBlackList("SignalMapSnsCellBlackList", z, "signal_map_sns_cell_black_list", "com.android.phone.intent.action.CLOUD_CATEGORY_SIGNAL_MAP_SNS_CELL_BLACK_LIST_DONE");
        fetchSwitchStatus("TelephonyOperatorQosSwitch", z, "operator_qos_switch", "");
        fetchSwitchStatus("TelephonyOperatorQosUrl", z, "operator_qos_url", "");
        fetchSwitchStatus("TelephonyFileUploadSwitch", z, "file_upload_switch", "com.android.phone.intent.action.CLOUD_FILE_UPLOAD_SWITCH");
        if (MiuiPhoneUtils.isDSDASupport()) {
            fetch5GDsdaCloudStatus();
        } else if (MiuiPhoneUtils.isDeviceDsdaSupportedByQcom() && MiuiPhoneUtils.getInsertedSimCount() == 2) {
            fetch5GDsdaCloudStatusByQcom(z);
        }
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            fetchSpecialNetworkCloudStatus();
        }
        fetchViceSASwitchCloudStatus(z);
        if (TelephonyManagerEx.getDefault().isN1Supported()) {
            fetchModemFeatureCloudStatus(3, z);
        }
        if (TelephonyManagerEx.getDefault().isN5Supported()) {
            fetchModemFeatureCloudStatus(5, z);
        }
        if (TelephonyManagerEx.getDefault().isN8Supported()) {
            fetchModemFeatureCloudStatus(6, z);
        }
        if (TelephonyManagerEx.getDefault().isN28Supported()) {
            fetchModemFeatureCloudStatus(1, z);
        }
        if (TelephonyManagerEx.getDefault().isBWPSupported()) {
            fetchModemFeatureCloudStatus(2, z);
        }
        if (MiuiPhoneUtils.isUlMimoOptimizationSupported()) {
            fetchULMimoOptimizationCloudStatus();
        }
        fetchVonrSwitchCloudStatus(z);
        if (MiuiPhoneUtils.isSupportModemFeature()) {
            sModemCrashReasonFromCloud = fetchModemCrashReasonCloudStatus();
        }
        if (TelephonyManagerEx.getDefault().isCdmaDataOptimizeSupported()) {
            fetchCdmaDataOptimize();
        }
        fetchWlanDisSaCloudStatus();
        if (MiuiPhoneUtils.isModemComponentExceptionSupported()) {
            fetchModemComponentCloudData();
        }
        if (MiuiPhoneUtils.isModemLimitRilMsgFuncSupported()) {
            fetchModemLimitRilMsgFuncCloudData();
        }
        if (MiuiPhoneUtils.isModemNonDdsRrcSupported()) {
            fetchModemNonddsRrcCloudStatus();
        }
        if (MiuiPhoneUtils.isARDOptimizationSupported()) {
            fetchARDOptimizationCloudStatus();
        }
        if (MiuiPhoneUtils.isLimitLTECAForNightModeSupported()) {
            fetchLimitLTECAForNightModeCloudStatus();
        }
        fetchTelephonyCommonFeature();
        setModemFeatureEx();
        fetchAllModemFeatures(z);
        if (MiuiPhoneUtils.isThermalThrottleSupported()) {
            fetchThermalThrottleFeatureCloudStatus();
        }
        if (MiuiPhoneUtils.isSupportPowerSaveV2()) {
            fetchPowerSaveCloudStatus();
        }
        fetchMtkPingPongCloudStatus();
        fetchModemMeasureRelaxCloudStatus();
        fetchGestureController();
        fetchFreqOptCloudStatus();
        notifyCloudChangeListener();
        fetchForceSwitch5GCloud();
        fetch5GNewCallCloudStatus();
        try {
            ExtTelephonyManager.getInstance(mContext).notifyCloudChanged();
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "notifyCloudChanged ", e);
        }
        FiveGCloudSyncJobService.initCloudSyncJobScheduler(mContext);
    }

    private void fetchFreqOptCloudStatus() {
        log("fetchFreqOptCloudStatus category:TelephonyFreqOpt");
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "TelephonyFreqOpt");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                HashMap map = new HashMap();
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    log("fetchFreqOptCloudStatus data=" + cloudData.toString());
                    FiveGCloudData fiveGCloudData = new FiveGCloudData(cloudData);
                    if (fiveGCloudData.isBasicInfoMatched() && fiveGCloudData.isLocationMatched("TelephonyFreqOpt") && FiveGCloudUtils.simOperatorMatched(fiveGCloudData.simOperator)) {
                        collectConfigValues(cloudData, map);
                    }
                }
                if (map.isEmpty()) {
                    return;
                }
                applyFreqOptCloudConfig(map);
                return;
            }
            log("fetchFreqOptCloudStatus cloudList is empty");
        } catch (Exception e) {
            log("fetchFreqOptCloudStatus exception" + e);
        }
    }

    private void collectConfigValues(MiuiSettings.SettingsCloudData.CloudData cloudData, Map map) {
        for (String str : FREQ_OPT_CONFIG_KEYS) {
            String string = cloudData.getString(str, "NA");
            if (!"NA".equals(string) && !TextUtils.isEmpty(string)) {
                map.put(str, string);
            }
        }
    }

    private void applyFreqOptCloudConfig(Map map) {
        ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).updateFreqOptCloudConfig(map);
        log("fetchFreqOptCloudStatus applied config: " + map);
    }

    public void fetchModemComponentCloudData() {
        log("fetchModemComponentCloudData category:TelephonyModemComponent");
        try {
            List cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "TelephonyModemComponent");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                Iterator it = cloudDataList.iterator();
                while (it.hasNext()) {
                    mModemComponentData = new CloudModemComponentData((MiuiSettings.SettingsCloudData.CloudData) it.next());
                }
                notifyModemComponentByCloud();
                return;
            }
            log("fetchModemComponentCloudData cloudList is empty");
        } catch (Exception e) {
            log("fetchModemComponentCloudData exception" + e);
        }
    }

    private void notifyModemComponentByCloud() {
        if (mModemComponentData != null) {
            ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).notifyQet7100CloudData();
        }
    }

    public static int getQet7100StatusByCloud() {
        CloudModemComponentData cloudModemComponentData = mModemComponentData;
        if (cloudModemComponentData != null) {
            return cloudModemComponentData.getQet7100Status();
        }
        return -1;
    }

    public static int getQet7100DayCountByCloud() {
        CloudModemComponentData cloudModemComponentData = mModemComponentData;
        if (cloudModemComponentData != null) {
            return cloudModemComponentData.getQet7100DayCount();
        }
        return -1;
    }

    public static int getQet7100TotalCountByCloud() {
        CloudModemComponentData cloudModemComponentData = mModemComponentData;
        if (cloudModemComponentData != null) {
            return cloudModemComponentData.getQet7100TotalCount();
        }
        return -1;
    }

    public void fetchCdmaDataOptimize() {
        log("fetchCdmaDataOptimize category:TelephonyCdmaDataOptimize");
    }

    public static boolean isCdmaDataOptimizeDisableByCloud() {
        return getCloudCdmaDataOptimizeEnable() == 0;
    }

    public static boolean isCdmaDataOptimizeTimeByCloud() {
        return -1 != getCloudCdmaDataOptimizeTime();
    }

    public static int getCloudCdmaDataOptimizeEnable() {
        int i;
        if ("true".equals(mCloudCdmaDataOptimizeEnable)) {
            i = 1;
        } else {
            i = "false".equals(mCloudCdmaDataOptimizeEnable) ? 0 : -1;
        }
        log("fetchCdmaDataOptimize  getCloudCdmaDataOptimizeEnable retValue = " + i);
        return i;
    }

    public static int getCloudCdmaDataOptimizeTime() {
        int i = -1;
        if (mCloudCdmaDataOptimizeEnable != null && !"NA".equals(mCloudCdmaDataOptimizeTime)) {
            try {
                i = Integer.parseInt(mCloudCdmaDataOptimizeTime);
            } catch (NumberFormatException e) {
                log("getCloudCdmaDataOptimizeTime NumberFormatException: " + e);
            }
        }
        log("fetchCdmaDataOptimize getCloudCdmaDataOptimizeTime retValue = " + i);
        return i;
    }

    public void updateCloudCdmaDataState(MiuiSettings.SettingsCloudData.CloudData cloudData) {
        mCloudCdmaDataOptimizeEnable = cloudData.getString("CdmaDataOptimizeEnable", "NA");
        mCloudCdmaDataOptimizeTime = cloudData.getString("CdmaDataOptimizeTime", "NA");
        log("fetchCdmaDataOptimize updateCloudCdmaDataState: " + mCloudCdmaDataOptimizeEnable + ", " + mCloudCdmaDataOptimizeTime);
    }

    public void fetchSpecialNetworkCloudStatus() {
        log("fetchSpecialNetworkCloudStatus Enter");
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "SpecialNetwork");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    log("fetchSpecialNetworkCloudStatus data=" + cloudData.toString());
                    mSpecialNetworkData = new CloudSpecialNetworkData(cloudData);
                }
                onSpecialNetworkDataDone();
                return;
            }
            log("fetchSpecialNetworkCloudStatus cloudList is empty");
        } catch (Exception e) {
            log("fetchSpecialNetworkCloudStatus exception" + e);
        }
    }

    public void onSpecialNetworkDataDone() {
        int cloudSpecialNetworkStatus = getCloudSpecialNetworkStatus();
        if (cloudSpecialNetworkStatus != -1) {
            Settings.Global.putInt(mContext.getContentResolver(), "special_network_opt_cloud", cloudSpecialNetworkStatus);
        }
        TelephonySensorManager telephonySensorManager = TelephonySensorManager.getInstance();
        if (telephonySensorManager != null) {
            telephonySensorManager.updateCloudSensorState();
        }
    }

    public List fetchSelfStudyCloudStatus() {
        try {
            List cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "TelephonySelfStudy");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                return cloudDataList;
            }
            log("fetchSelfStudyCloudStatus cloudList is empty");
        } catch (Exception e) {
            log("fetchSelfStudyCloudStatus exception" + e);
        }
        return new ArrayList();
    }

    public void fetch5GSaCloudStatus(String str, boolean z) {
        log("fetch5GSaCloudStatus category:" + str);
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), str);
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    FiveGManagerBase.localLog("fetch5GSaCloudStatus", "fetch5GSaCloudStatus data=" + cloudData.toString());
                    new FiveGCloudData(cloudData).addFiveGSaCloudStatus();
                }
                boolean zEquals = sSaSimOperatorStatusMap.equals(sSaLastEffectiveSimOperatorStatusMap);
                FiveGManagerBase.localLog("fetch5GSaCloudStatus", "isCloudSync: " + z + ", isSameData: " + zEquals + ", sSaLastEffectiveSimOperatorStatusMap: " + sSaLastEffectiveSimOperatorStatusMap + ", sSaSimOperatorStatusMap: " + sSaSimOperatorStatusMap);
                if (zEquals) {
                    return;
                }
                sSaLastEffectiveSimOperatorStatusMap = (HashMap) sSaSimOperatorStatusMap.clone();
                if (z) {
                    for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                        FiveGManagerBase.getInstance().updateFiveGSaStatus(i);
                    }
                    return;
                }
                return;
            }
            FiveGManagerBase.localLog("fetch5GSaCloudStatus", "fetch5GSaCloudStatus cloudList is empty");
        } catch (Exception e) {
            log("get5GCloudStatus exception" + e);
        }
    }

    public int fetch5GDsdaCloudStatus() {
        int iFetch5GCloudStatus = fetch5GCloudStatus("Telephony5GDsdaSwitch");
        log("fetch5GDsdaCloudStatus status:" + iFetch5GCloudStatus);
        if (iFetch5GCloudStatus == 1) {
            FiveGManagerBase.getInstance().enableDSDA();
            return iFetch5GCloudStatus;
        }
        if (iFetch5GCloudStatus == 0) {
            FiveGManagerBase.getInstance().disableDSDA();
        }
        return iFetch5GCloudStatus;
    }

    public void fetchForceSwitch5GCloud() {
        log("fetchForceSwitch5GCloud category:TelephonyForceSwitch5G");
        try {
            List cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "TelephonyForceSwitch5G");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                String str = SystemProperties.get("persist.radio.cloud.force.5g.version", "NA");
                FiveGCloudData fiveGCloudData = new FiveGCloudData((MiuiSettings.SettingsCloudData.CloudData) cloudDataList.get(cloudDataList.size() - 1));
                String featureVersion = fiveGCloudData.getFeatureVersion();
                if ((!TextUtils.isEmpty(mCity) || !TextUtils.isEmpty(mProvince) || fiveGCloudData.isLocationMatched("TelephonyForceSwitch5G")) && !str.equals(featureVersion)) {
                    fiveGCloudData.addPreferredNetworkTypeVisibleCloudStatus();
                    fiveGCloudData.addFiveGStatusCloudStatus();
                    fiveGCloudData.setFiveGSwitchVisible();
                    SystemProperties.set("persist.radio.cloud.force.5g.version", featureVersion);
                }
                for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                    String str2 = SystemProperties.get("persist.radio.cloud.force.5g.version" + i, "NA");
                    log("CLOUD_CATEGORY_FORCE_SWITCH_5G, lastForceSwitchFiveGCloudVersionByPhoneId = " + str2 + ", currentForceSwitchFiveGCloudVersion = " + featureVersion + ", phoneId = " + i);
                    if (!str2.equals(featureVersion)) {
                        handleForceSwitchFiveG(i, featureVersion);
                    }
                }
                return;
            }
            log("fetchForceSwitch5GCloud cloudList is empty");
        } catch (Exception e) {
            log("fetchForceSwitch5GCloud exception" + e);
        }
    }

    private synchronized void fetchWhiteList(String str, boolean z, String str2, String str3) {
        try {
            log("fetchWhiteList category:" + str);
            try {
                List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), str);
                if (cloudDataList == null || cloudDataList.size() == 0) {
                    log("fetchWhiteList cloudList is empty");
                } else {
                    for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                        log("fetchWhiteList data=" + cloudData.toString());
                        String statusFromCloud = new FiveGCloudData(cloudData).getStatusFromCloud();
                        if (!statusFromCloud.equals("NA") && !TextUtils.isEmpty(statusFromCloud) && z) {
                            String[] strArrSplit = statusFromCloud.split(",");
                            if (strArrSplit != null && strArrSplit.length != 0) {
                                FiveGCloudUtils.saveListToDb(Arrays.asList(strArrSplit), str2);
                                broadcastCloudWhiteListUpdateDone(str3);
                            }
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                log("fetchWhiteList exception" + e);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void fetchPowerSaveCloudStatus() {
        FeatureCloudInfo featureInfoByCloud = getFeatureInfoByCloud("TelephonyPowerSave");
        if (featureInfoByCloud == null) {
            return;
        }
        log("fetchPowerSaveCloudStatus");
        mPowerSaveData = new CloudPowerSaveData(featureInfoByCloud);
    }

    private void fetchMtkPingPongCloudStatus() {
        FeatureCloudInfo featureInfoByCloud = getFeatureInfoByCloud("TelephonyMtkPingPong");
        if (featureInfoByCloud == null) {
            return;
        }
        log("fetchMtkPingPongCloudStatus");
        sMtkPingPongCloudStatus = featureInfoByCloud.getStatus();
    }

    private void fetchModemMeasureRelaxCloudStatus() {
        FeatureCloudInfo featureInfoByCloud = getFeatureInfoByCloud("TelephonyModemMeasureRelax");
        if (featureInfoByCloud == null) {
            return;
        }
        log("fetchModemMeasureRelaxCloudStatus");
        sModemMeasureRelaxCloudStatus = featureInfoByCloud.getStatus();
    }

    private synchronized void fetchSwitchStatus(String str, boolean z, String str2, String str3) {
        try {
            log("fetchSwitchStatus category:" + str);
            try {
                List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), str);
                if (cloudDataList == null || cloudDataList.size() == 0) {
                    log("fetchSwitchStatus cloudList is empty");
                } else {
                    for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                        log("fetchSwitchStatus data=" + cloudData.toString());
                        String statusFromCloud = new FiveGCloudData(cloudData).getStatusFromCloud();
                        if (!statusFromCloud.equals("NA") && !TextUtils.isEmpty(statusFromCloud) && z) {
                            FiveGCloudUtils.saveStatusToDb(statusFromCloud, str2);
                            broadcastCloudStatusUpdateDone(str3);
                        }
                    }
                }
            } catch (Exception e) {
                log("fetchSwitchStatus exception" + e);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized void fetchSignalMapSnsCellBlackList(String str, boolean z, String str2, String str3) {
        try {
            log("fetchSignalMapSnsCellBlackList category:" + str);
            try {
                List cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), str);
                if (cloudDataList == null || cloudDataList.size() == 0) {
                    log("fetchSignalMapSnsCellBlackList cloudList is empty");
                } else {
                    Iterator it = cloudDataList.iterator();
                    while (it.hasNext()) {
                        String statusFromCloud = new FiveGCloudData((MiuiSettings.SettingsCloudData.CloudData) it.next()).getStatusFromCloud();
                        if (!TextUtils.isEmpty(statusFromCloud) && !statusFromCloud.equals("NA") && z) {
                            FiveGCloudUtils.saveStatusToDb(statusFromCloud, str2);
                            broadcastCloudStatusUpdateDone(str3);
                        }
                    }
                }
            } catch (Exception e) {
                log("fetchSignalMapSnsCellBlackList exception" + e);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public void fetchQmsSwitchCloudStatus(String str, boolean z) {
        log("fetchQmsSwitchCloudStatus category:" + str);
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), str);
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    log("fetchQmsSwitchCloudStatus data=" + cloudData.toString());
                    String statusFromCloud = new FiveGCloudData(cloudData).getStatusFromCloud();
                    if (!statusFromCloud.equals("NA") && !TextUtils.isEmpty(statusFromCloud) && z) {
                        if (!statusFromCloud.equals("1") && !statusFromCloud.equals("0")) {
                            return;
                        } else {
                            Settings.Global.putInt(mContext.getContentResolver(), "qms_switch_status", Integer.parseInt(statusFromCloud));
                        }
                    }
                }
                return;
            }
            log("fetchQmsSwitchCloudStatus cloudList is empty");
        } catch (Exception e) {
            log("fetchQmsSwitchCloudStatus exception" + e);
        }
    }

    public int fetch5GDsdaCloudStatusByQcom(boolean z) {
        int iFetch5GCloudStatus = fetch5GCloudStatus("Telephony5GDsdaSwitch");
        log("fetch5GDsdaCloudStatusByQcom status:" + iFetch5GCloudStatus);
        sFiveGDsdaCloudStatus = iFetch5GCloudStatus;
        if (iFetch5GCloudStatus != -1) {
            boolean z2 = iFetch5GCloudStatus == 1;
            if (z) {
                if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isDsdaEnabled() == z2) {
                    log("fetch5GDsdaCloudStatusByQcom no changed, return.");
                    return iFetch5GCloudStatus;
                }
                ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setDsdaEnabled(z2, 2);
            }
        }
        return iFetch5GCloudStatus;
    }

    public int fetch5GCloudStatus(String str) {
        log("get5GCloudStatus category:" + str);
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), str);
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    log("get5GCloudStatus data=" + cloudData.toString());
                    int fiveGCloudStatus = new FiveGCloudData(cloudData).getFiveGCloudStatus(str);
                    if (fiveGCloudStatus != -1) {
                        return fiveGCloudStatus;
                    }
                }
                return -1;
            }
            log("get5GCloudStatus cloudList is empty");
            return -1;
        } catch (Exception e) {
            log("get5GCloudStatus exception" + e);
            return -1;
        }
    }

    private int fetch5GGameCloudStatus() {
        return fetch5GCloudStatus("Telephony5GGame");
    }

    public void fetch5GCloudSmartSwitchData() {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "SmartSwitch");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    log("fetch5GCloudSmartSwitchData data=" + cloudData.toString());
                    mSmartSwitchData = new FiveGCloudSmartSwitchData(cloudData);
                }
                broadcastCloudSmartSwitchDone();
                return;
            }
            log("fetch5GCloudSmartSwitchData cloudList is empty");
            initSpeedWhiteListDefaultData();
        } catch (Exception e) {
            log("fetch5GCloudSmartSwitchData exception" + e);
            initSpeedWhiteListDefaultData();
        }
    }

    public void fetchViceSASwitchCloudStatus(boolean z) {
        sViceSASwitchCloudStatus = fetch5GCloudStatus("TelephonyViceSaSwitch");
        int viceSlotId = FiveGUtils.getViceSlotId();
        if (!z || sViceSASwitchCloudStatus == -1 || viceSlotId == -1 || MiuiPhoneUtils.getInsertedSimCount() != MiuiPhoneUtils.PHONE_COUNT) {
            return;
        }
        FiveGManagerBase.getInstance().updateFiveGSaStatus(viceSlotId);
    }

    public void fetchModemFeatureCloudStatus(int i, boolean z) {
        String str;
        if (i == 1) {
            str = "TelephonyN28Mode";
        } else if (i == 3) {
            str = "TelephonyN1Mode";
        } else if (i == 2) {
            str = "TelephonyBWPMode";
        } else if (i == 5) {
            str = "TelephonyN5Mode";
        } else {
            str = i == 6 ? "TelephonyN8Mode" : null;
        }
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), str);
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    FiveGManagerBase.localLog("fetchModemFeatureCloudStatus", "fetchModeFeatureCloudStatus data=" + cloudData.toString());
                    new FiveGCloudData(cloudData).addModemFeatureCloudStatus(i);
                }
                if (z) {
                    for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                        int subscriptionIdForSlot = SubscriptionManager.getDefault().getSubscriptionIdForSlot(i2);
                        if (SubscriptionManager.isValidSubscriptionId(subscriptionIdForSlot)) {
                            int modemFeatureCloudStatus = getModemFeatureCloudStatus(i, i2);
                            if (modemFeatureCloudStatus == -1) {
                                log("updateModemFeatureStatusByCloud: No cloud control data, subId = " + subscriptionIdForSlot);
                            } else if (MiuiPhoneUtils.getPlatformModemFeatureStatus(i, i2) == modemFeatureCloudStatus) {
                                log("fetchModemFeatureCloudStatus: Cloud control data is in the same status as the platform and will not be updated, subId = " + subscriptionIdForSlot);
                            } else {
                                FiveGManagerBase.ModemFeatureStatus modemFeatureStatus = new FiveGManagerBase.ModemFeatureStatus(i2, i, modemFeatureCloudStatus);
                                log("modemFeatureStatus = " + modemFeatureStatus.toString());
                                FiveGManagerBase.getInstance().setModemFeatureBySlot(modemFeatureStatus);
                                FiveGManagerBase.getInstance().needRefreshMbn(i2);
                            }
                        } else {
                            log("fetchModemFeatureCloudStatus: Invalid SIM card, subId = " + subscriptionIdForSlot);
                        }
                    }
                    return;
                }
                return;
            }
            log("fetchModemFeatureCloudStatus cloudList is empty");
        } catch (Exception e) {
            log("fetchModeFeatureCloudStatus exception" + e);
        }
    }

    public void fetchVonrSwitchCloudStatus(boolean z) {
        if (!FiveGUtils.isVoNRSupported()) {
            log("the device not support vonr");
            return;
        }
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "TelephonyVonrSwitch");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    log("fetchVonrSwitchCloudStatus data=" + cloudData.toString());
                    new FiveGCloudData(cloudData).addFiveGVonrCloudStatus();
                }
                FiveGManagerBase.localLog("fetchVonrCloudStatus", "sVonrOperatorStatusMap: " + sVonrOperatorStatusMap);
                if (z) {
                    for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                        if (getFiveGVonrCloudStatus(i) != -1) {
                            FiveGManagerBase fiveGManagerBase = FiveGManagerBase.getInstance();
                            boolean z2 = true;
                            if (getFiveGVonrCloudStatus(i) != 1) {
                                z2 = false;
                            }
                            fiveGManagerBase.setUserVoNREnabled(i, z2);
                        }
                    }
                    return;
                }
                return;
            }
            log("fetchVonrSwitchCloudStatus cloudList is empty");
        } catch (Exception e) {
            log("fetchVonrSwitchCloudStatus exception" + e);
            e.printStackTrace();
        }
    }

    public String fetchModemCrashReasonCloudStatus() {
        log("fetchModemCrashReasonCloudStatus");
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "TelephonyModemCrashReason");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    log("fetchModemCrashReasonCloudStatus data=" + cloudData.toString());
                    String statusFromCloud = new FiveGCloudData(cloudData).getStatusFromCloud();
                    if (!statusFromCloud.equals("NA")) {
                        return statusFromCloud;
                    }
                }
                return "";
            }
            log("fetchModemCrashReasonCloudStatus cloudList is empty");
            return "";
        } catch (Exception e) {
            log("fetchModemCrashReasonCloudStatus exception" + e);
            return "";
        }
    }

    public void fetchWlanDisSaCloudStatus() {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "Telephony5GWlanDisSa");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    log("fetchWlanDisSaCloudStatus data=" + cloudData.toString());
                    this.mWlanDisSADisabledByCloud = new FiveGCloudData(cloudData).getFiveGCloudStatus("Telephony5GWlanDisSa") == 0;
                }
                return;
            }
            log("fetchWlanDisSaCloudStatus cloudList is empty");
        } catch (Exception e) {
            log("fetchWlanDisSaCloudStatus exception" + e);
        }
    }

    public void fetchModemLimitRilMsgFuncCloudData() {
        log("fetchModemLimitRilMsgFuncCloudData category:ModemLimitRilMsgFunc");
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "ModemLimitRilMsgFunc");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                this.mHydra = ModemUtils.init(mContext);
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    log("fetchModemLimitRilMsgFuncCloudData data=" + cloudData.toString());
                    int fiveGCloudStatus = new FiveGCloudData(cloudData).getFiveGCloudStatus("ModemLimitRilMsgFunc");
                    ModemUtils modemUtils = this.mHydra;
                    if (modemUtils != null) {
                        boolean z = true;
                        if (fiveGCloudStatus != 1) {
                            z = false;
                        }
                        modemUtils.setModemLimitRilMsgFlag(z);
                    }
                }
                return;
            }
            log("fetchModemLimitRilMsgFuncCloudData cloudList is empty");
        } catch (Exception e) {
            log("fetchModemComponentCloudData exception" + e);
        }
    }

    public void fetchModemNonddsRrcCloudStatus() {
        log("fetchModemNonddsRrcCloudStatus category:ModemNonddsRrcSwitch");
        FeatureCloudInfo featureInfoByCloud = getFeatureInfoByCloud("ModemNonddsRrcSwitch");
        if (featureInfoByCloud == null) {
            log("fetchModemNonddsRrcCloudStatus feature is null");
            return;
        }
        int status = featureInfoByCloud.getStatus();
        log("fetchModemNonddsRrcCloudStatus status =" + status);
        this.mModemNonddsRrcDisabledByCloud = status == 0;
        ModemUtils modemUtilsInit = ModemUtils.init(mContext);
        this.mHydra = modemUtilsInit;
        if (modemUtilsInit != null) {
            if (status == 1) {
                modemUtilsInit.callQcrilHookSet("onHookModemPowerAPCommand", Integer.valueOf(ModemUtils.featureOptType.SWITCH_XIAOMI_NONDDS_RRC.getType()), ModemUtils.modem_power_enable);
            } else if (status == 0) {
                modemUtilsInit.callQcrilHookSet("onHookModemPowerAPCommand", Integer.valueOf(ModemUtils.featureOptType.SWITCH_XIAOMI_NONDDS_RRC.getType()), ModemUtils.modem_power_disable);
            }
        }
    }

    public boolean isModemNonddsRrcDisabledByCloud() {
        return this.mModemNonddsRrcDisabledByCloud;
    }

    public void fetchARDOptimizationCloudStatus() {
        log("fetchARDOptimizationCloudStatus category:TelephonyARDOptimize");
        int iFetch5GCloudStatus = fetch5GCloudStatus("TelephonyARDOptimize");
        if (!this.mARDOptimizationDisabledByCloud && iFetch5GCloudStatus == 0) {
            ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).resetARDOptimizationToDefault();
        }
        this.mARDOptimizationDisabledByCloud = iFetch5GCloudStatus == 0;
    }

    public void fetchLimitLTECAForNightModeCloudStatus() {
        log("fetchLimitLTECAForNightModeCloudStatus category:TelephonyLimitLTECAForNightMode");
        int iFetch5GCloudStatus = fetch5GCloudStatus("TelephonyLimitLTECAForNightMode");
        if (!this.mLimitLTECAForNightModeDisabledByCloud && iFetch5GCloudStatus == 0) {
            ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).processLimitLteCaOptimizeByCloud();
        }
        this.mLimitLTECAForNightModeDisabledByCloud = iFetch5GCloudStatus == 0;
    }

    public boolean isARDOptimizationDisabledByCloud() {
        return this.mARDOptimizationDisabledByCloud;
    }

    public void fetchGestureController() {
        int iFetch5GCloudStatus = fetch5GCloudStatus("GestureControl");
        boolean z = 1 == iFetch5GCloudStatus;
        boolean z2 = iFetch5GCloudStatus == 0;
        boolean z3 = SystemProperties.getBoolean("persist.radio.me.orientsensor", true);
        log("fetchGestureController isGestureEnabled:" + z3 + ", isEnableGesture :" + z + ", isDisableGesture = " + z2);
        if (!z3 && z) {
            broadcastCloudGestureControl(true);
        } else if (z3 && z2) {
            broadcastCloudGestureControl(false);
        }
    }

    public boolean isLimitLTECAForNightModeDisabledByCloud() {
        return this.mLimitLTECAForNightModeDisabledByCloud;
    }

    public void fetchTelephonyCommonFeature() {
        log("fetchTelephonyCommonFeature category:TelephonyCommonFeature");
        try {
            List cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "TelephonyCommonFeature");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                sFeatureCloudInfoMap.clear();
                Iterator it = cloudDataList.iterator();
                while (it.hasNext()) {
                    overlayFeatureCloudInfo(new FiveGCloudData((MiuiSettings.SettingsCloudData.CloudData) it.next()).getFeatureCloudInfo());
                }
                log("fetchTelephonyCommonFeature sFeatureCloudInfoMap:" + sFeatureCloudInfoMap);
                boolean zEquals = sFeatureCloudInfoMap.equals(sLastFeatureCloudInfoMap);
                log("fetchTelephonyCommonFeature isSameData:" + zEquals);
                if (zEquals) {
                    return;
                }
                notifyTeleFeatureCloudInfoChanged();
                sLastFeatureCloudInfoMap = cloneFeatureInfo(sFeatureCloudInfoMap);
                return;
            }
            log("fetchTelephonyCommonFeature cloudList is empty");
        } catch (Exception e) {
            log("fetchTelephonyCommonFeature exception" + e);
        }
    }

    public void fetch5GNewCallCloudStatus() {
        log("fetch5GNewCallCloudStatus category: Telephony5GNewCall");
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "Telephony5GNewCall");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    log("fetch5GNewCallCloudStatus data=" + cloudData.toString());
                    FiveGCloudData fiveGCloudData = new FiveGCloudData(cloudData);
                    if (!TextUtils.isEmpty(mCity) || !TextUtils.isEmpty(mProvince) || fiveGCloudData.isLocationMatched("Telephony5GNewCall")) {
                        fiveGCloudData.addFiveGNewCallStatusCloudStatus();
                    }
                }
                handleFiveGNewStatusChanged(DefaultSimManager.getDefaultDataSlotId());
                return;
            }
            log("fetch5GNewCallCloudStatus cloudList is empty");
        } catch (Exception e) {
            log("fetch5GNewCallCloudStatus exception" + e);
        }
    }

    private void handleFiveGNewStatusChanged(int i) {
        if (SubscriptionManager.isValidSlotId(i)) {
            log("handleFiveGNewStatusChanged, phoneId = " + i);
            boolean zIsFiveGNewCallEnableByCloud = isFiveGNewCallEnableByCloud(i);
            boolean zIsFiveNewCallDisableByCloud = isFiveNewCallDisableByCloud(i);
            boolean zIsDataChannelEnable = FiveGManagerBase.getInstance().isDataChannelEnable(i);
            if (zIsFiveGNewCallEnableByCloud && !zIsDataChannelEnable) {
                FiveGManagerBase.getInstance().setDataChannelEnable(i, true);
                log("handleFiveGNewStatusChanged, enable new call");
            } else if (zIsFiveNewCallDisableByCloud && zIsDataChannelEnable) {
                FiveGManagerBase.getInstance().setDataChannelEnable(i, false);
                log("handleFiveGNewStatusChanged, disable new call");
            }
        }
    }

    private void overlayFeatureCloudInfo(FeatureCloudInfo featureCloudInfo) {
        if (featureCloudInfo == null || TextUtils.isEmpty(featureCloudInfo.featureType)) {
            return;
        }
        if (!sFeatureCloudInfoMap.containsKey(featureCloudInfo.featureType)) {
            sFeatureCloudInfoMap.put(featureCloudInfo.featureType, featureCloudInfo);
            return;
        }
        FeatureCloudInfo featureCloudInfo2 = (FeatureCloudInfo) sFeatureCloudInfoMap.get(featureCloudInfo.featureType);
        featureCloudInfo2.status = featureCloudInfo.status;
        featureCloudInfo2.params1 = featureCloudInfo.params1;
        featureCloudInfo2.params2 = featureCloudInfo.params2;
    }

    private void handleForceSwitchFiveG(int i, String str) {
        int lteNetworkModeValue;
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        boolean zIsFiveGEnableByCloud = isFiveGEnableByCloud(i);
        boolean zIsFiveGDisableByCloud = isFiveGDisableByCloud(i);
        if (FiveGManagerBase.getInstance() != null) {
            if (!(i == defaultDataSlotId && FiveGManagerBase.isFiveGCapable()) && (i == defaultDataSlotId || !TelephonyManager.getDefault().isDualNrSupported())) {
                return;
            }
            int preferredNetworkModeFromDb = ModemProvider.getInstance().getPreferredNetworkModeFromDb(i);
            if (zIsFiveGEnableByCloud) {
                log("handleForceSwitchFiveG, force open 5G, phoneId = " + i);
                FiveGManagerBase.setFivegEnabled(true, i);
                lteNetworkModeValue = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getNrNetworkModeValue(i);
                SystemProperties.set("persist.radio.cloud.force.5g.version" + i, str);
            } else if (zIsFiveGDisableByCloud) {
                log("handleForceSwitchFiveG, force close 5G, phoneId = " + i);
                FiveGManagerBase.setFivegEnabled(false, i);
                lteNetworkModeValue = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getLteNetworkModeValue(i);
                SystemProperties.set("persist.radio.cloud.force.5g.version" + i, str);
            } else {
                lteNetworkModeValue = preferredNetworkModeFromDb;
            }
            if (lteNetworkModeValue != preferredNetworkModeFromDb) {
                ModemProvider.getInstance().setNetworkModeInDb(i, lteNetworkModeValue);
                MiuiPhoneUtils.getPhone(i).setAllowedNetworkTypes(0, RadioAccessFamily.getRafFromNetworkType(lteNetworkModeValue), (Message) null);
            }
        }
    }

    public void fetchThermalThrottleFeatureCloudStatus() {
        FeatureCloudInfo featureInfoByCloud = getFeatureInfoByCloud("TelephonyThermalThrottle");
        if (featureInfoByCloud == null) {
            return;
        }
        int status = featureInfoByCloud.getStatus();
        int i = featureInfoByCloud.getParams1() != null ? Integer.parseInt(featureInfoByCloud.getParams1()) : 0;
        boolean z = status == 0;
        this.mThermalThrottleDisabledByCloud = z;
        if (z) {
            ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).resetThermalThrottle();
        }
        if (i != 0) {
            ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).updateThermalThrottleSASnr(i);
        }
    }

    public boolean isThermalThrottleDisabledByCloud() {
        return this.mThermalThrottleDisabledByCloud;
    }

    private void setModemFeatureEx() {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "ModemFeatures");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                final HashMap map = new HashMap();
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    log("setModemFeatureEx data=" + cloudData.toString());
                    ModemQmiMessageInfo modemQmiMessageCloudInfo = new FiveGCloudData(cloudData).getModemQmiMessageCloudInfo();
                    if (modemQmiMessageCloudInfo != null) {
                        map.put(Integer.valueOf(modemQmiMessageCloudInfo.featureType), modemQmiMessageCloudInfo);
                    }
                }
                log("setModemFeatureEx modemQmiMessageInfoMap = " + map);
                new Thread(new Runnable() { // from class: com.android.phone.FiveGCloudController.2
                    @Override // java.lang.Runnable
                    public void run() {
                        for (ModemQmiMessageInfo modemQmiMessageInfo : map.values()) {
                            FiveGCloudController.log("setModemFeatureEx , featureType = " + modemQmiMessageInfo.featureType + ", featureValue = " + modemQmiMessageInfo.featureValue);
                            FiveGCloudController.this.mHydra = ModemUtils.init(FiveGCloudController.mContext);
                            if (FiveGCloudController.this.mHydra != null) {
                                FiveGCloudController.this.mHydra.callQcrilHookSet("onHookModemFeatureSet", Integer.valueOf(modemQmiMessageInfo.featureType), Integer.valueOf(modemQmiMessageInfo.featureValue));
                            }
                        }
                    }
                }).start();
                return;
            }
            log("setModemFeatureEx cloudList is empty");
        } catch (Exception e) {
            log("setModemFeatureEx exception" + e);
            e.printStackTrace();
        }
    }

    private void fetchAllModemFeatures(boolean z) {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "ModemFeatures");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                final HashMap map = new HashMap();
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    log("fetchAllModemFeatures data=" + cloudData.toString());
                    ModemFeatureCloudInfo modemFeatureCloudInfo = new FiveGCloudData(cloudData).getModemFeatureCloudInfo();
                    if (modemFeatureCloudInfo != null) {
                        map.put(Integer.valueOf(modemFeatureCloudInfo.featureType), modemFeatureCloudInfo);
                    }
                }
                log("fetchAllModemFeatures modemFeatureInfoMap = " + map);
                new Thread(new Runnable(this) { // from class: com.android.phone.FiveGCloudController.3
                    @Override // java.lang.Runnable
                    public void run() {
                        boolean z2 = false;
                        for (ModemFeatureCloudInfo modemFeatureCloudInfo2 : map.values()) {
                            if (modemFeatureCloudInfo2.featureType >= 0 && modemFeatureCloudInfo2.featureValue >= 0) {
                                for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                                    int platformModemFeatureStatus = MiuiPhoneUtils.getPlatformModemFeatureStatus(modemFeatureCloudInfo2.featureType, i);
                                    FiveGCloudController.log("fetchAllModemFeatures lastStatus = " + platformModemFeatureStatus + ", featureType = " + modemFeatureCloudInfo2.featureType + ", slotId = " + i);
                                    if (platformModemFeatureStatus != modemFeatureCloudInfo2.featureValue) {
                                        FiveGManagerBase.getInstance().setModemFeature(modemFeatureCloudInfo2.featureType, modemFeatureCloudInfo2.featureValue, i);
                                        z2 |= modemFeatureCloudInfo2.isRefreshMbn;
                                        try {
                                            Thread.sleep(1000L);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                        if (z2) {
                            for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                                FiveGManagerBase.getInstance().needRefreshMbn(i2);
                            }
                        }
                    }
                }).start();
                return;
            }
            log("fetchAllModemFeatures cloudList is empty");
        } catch (Exception e) {
            log("fetchAllModemFeatures exception" + e);
            e.printStackTrace();
        }
    }

    public int getCloudSpecialNetworkStatus() {
        CloudSpecialNetworkData cloudSpecialNetworkData = mSpecialNetworkData;
        if (cloudSpecialNetworkData != null) {
            return cloudSpecialNetworkData.getSpecialNetworkStatus();
        }
        return -1;
    }

    public int getElevatorStatus() {
        CloudSpecialNetworkData cloudSpecialNetworkData = mSpecialNetworkData;
        if (cloudSpecialNetworkData != null) {
            return cloudSpecialNetworkData.getElevatorStatus();
        }
        return -1;
    }

    public int getGeographyFenceStatus() {
        CloudSpecialNetworkData cloudSpecialNetworkData = mSpecialNetworkData;
        if (cloudSpecialNetworkData != null) {
            return cloudSpecialNetworkData.getGeographyFenceStatus();
        }
        return -1;
    }

    private void initSpeedWhiteListDefaultData() {
        List featureConfig = MiuiPhoneUtils.getFeatureConfig("speed_white_list");
        log("speed_white_list = " + featureConfig);
        FiveGCloudUtils.saveListToDb(new HashSet(featureConfig), "fiveg_speed_white_list_pkg_name");
    }

    private static boolean initDsda2DsdsWhiteListDefaultData() {
        List stringArray = HyperOSCustFeatureResolve.getStringArray("scene_dsda_change_dsds_white_list_default");
        if (stringArray == null) {
            log("initDsda2DsdsWhiteListDefaultData fail");
            return false;
        }
        FiveGCloudUtils.saveListToDb(new HashSet(stringArray), "scene_dsda_change_dsds_white_list_pkg_name");
        return true;
    }

    private static boolean initSmartSwitchWhiteListDefaultData() {
        List stringArray = HyperOSCustFeatureResolve.getStringArray("smart_switch_alert_dialog_white_list_default");
        if (stringArray == null) {
            log("initSmartSwitchWhiteListDefaultData fail");
            return false;
        }
        FiveGCloudUtils.saveListToDb(new HashSet(stringArray), "smart_switch_alert_dialog_white_list_pkg_name");
        return true;
    }

    private static void initPingPongWhiteListDefaultData() {
        List featureConfig = MiuiPhoneUtils.getFeatureConfig("ltenr_ping_pong_white_list_default");
        log("ltenr_ping_pong_white_list_default = " + featureConfig);
        FiveGCloudUtils.saveListToDb(new HashSet(featureConfig), "ltenr_ping_pong_white_list_pkg_name");
    }

    private static void initCommonSignalWhiteListDefaultData() {
        List featureConfig = MiuiPhoneUtils.getFeatureConfig("common_signal_white_list_default");
        log("common_signal_white_list_default = " + featureConfig);
        FiveGCloudUtils.saveListToDb(new HashSet(featureConfig), "common_signal_white_list_pkg_name");
    }

    private static void initDualDataConcurrentModeWhiteListDefaultData() {
        FiveGCloudUtils.saveListToDb(new HashSet(Arrays.asList(DUAL_DATA_CONCURRENT_MODE_WHITE_LIST_DEFAULT)), "dual_data_concurrent_mode_white_list_pkg_name");
    }

    private static void initDualDataConcurrentLimitedWhiteListDefaultData() {
        List featureConfig = MiuiPhoneUtils.getFeatureConfig("dual_data_concurrent_limited_white_list_default");
        log("dual_data_concurrent_limited_white_list_default = " + featureConfig);
        FiveGCloudUtils.saveListToDb(new HashSet(featureConfig), "dual_data_concurrent_limited_white_list_pkg_name");
    }

    private static void initDualDataRedundantModeWhiteListDefaultData() {
        FiveGCloudUtils.saveListToDb(new HashSet(Arrays.asList(DUAL_DATA_REDUNDANT_MODE_WHITE_LIST_DEFAULT)), "dual_data_redundant_mode_white_list_pkg_name");
    }

    private static void initHikingModeCityWhiteListDefaultData() {
        FiveGCloudUtils.saveListToDb(new ArrayList(Arrays.asList(HIKING_MODE_CITY_WHITE_LIST_DEFAULT)), "hiking_mode_city_white_list");
    }

    private static boolean initResolutionVideoAppsWhiteListDefaultData() {
        List stringArray = HyperOSCustFeatureResolve.getStringArray("resolution_video_apps_white_list_default");
        if (stringArray == null) {
            log("initResolutionVideoAppsWhiteListDefaultData fail");
            return false;
        }
        FiveGCloudUtils.saveListToDb(new HashSet(stringArray), "resolution_video_apps_white_list_pkg_name");
        return true;
    }

    private static void initSubwayModeSignalThresholdDefaultData() {
        FiveGCloudUtils.saveListToDb(new ArrayList(Arrays.asList(SUBWAY_MODE_SIGNAL_THRESHOLD_DEFAULT)), "subway_mode_signal_threshold");
    }

    private static boolean initPreloadVideoAppsWhiteListDefaultData() {
        List stringArray = HyperOSCustFeatureResolve.getStringArray("preload_video_apps_white_list_default");
        if (stringArray == null) {
            log("initPreloadVideoAppsWhiteListDefaultData fail");
            return false;
        }
        FiveGCloudUtils.saveListToDb(new HashSet(stringArray), "preload_video_apps_white_list_pkg_name");
        return true;
    }

    private static boolean initHyperEngineGameWhiteListDefaultData() {
        List stringArray = HyperOSCustFeatureResolve.getStringArray("hyper_engine_game_white_list_default");
        if (stringArray == null) {
            log("initHyperEngineGameWhiteListDefaultData fail");
            return false;
        }
        FiveGCloudUtils.saveListToDb(new HashSet(stringArray), "hyper_engine_game_white_list_pkg_name");
        return true;
    }

    private static void initSignalMapSnsCellBlackListDefaultData() {
        FiveGCloudUtils.saveStatusToDb("", "signal_map_sns_cell_black_list");
    }

    private synchronized void broadcastCloudWhiteListUpdateDone(String str) {
        mContext.sendStickyBroadcast(new Intent(str));
    }

    private void broadcastCloudSmartSwitchDone() {
        mContext.sendStickyBroadcast(new Intent("com.android.phone.intent.action.CLOUD_SMART_SWITCH_DONE"));
    }

    private void broadcastCloudGestureControl(boolean z) {
        Intent intent = new Intent("com.android.phone.intent.action.GESTURE_CONTROL");
        intent.putExtra("ENABLE_FEATURE", z);
        mContext.sendStickyBroadcast(intent);
    }

    private synchronized void broadcastCloudStatusUpdateDone(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        mContext.sendStickyBroadcast(new Intent(str));
    }

    public static boolean is5GSwitchClosedByCloud() {
        return sFiveGSwitchCloudStatus == 0;
    }

    public static boolean isModemFeatureEnabledByCloud(int i, int i2) {
        return 1 == getModemFeatureCloudStatus(i, i2);
    }

    public static boolean isModemFeatureDisabledByCloud(int i, int i2) {
        return getModemFeatureCloudStatus(i, i2) == 0;
    }

    public static boolean is5GSaModeDisabledByCloud(int i) {
        return getFiveGSaCloudStatus(i) == 0;
    }

    public static boolean is5GSaModeEnabledByCloud(int i) {
        return 1 == getFiveGSaCloudStatus(i);
    }

    public static boolean is5GSaSwitchUnhandledByCloud(int i) {
        return -1 == getFiveGSaCloudStatus(i);
    }

    public static boolean isViceSAModeEnabledByCloud() {
        return 1 == sViceSASwitchCloudStatus;
    }

    public static boolean isViceSAModeDisabledByCloud() {
        return sViceSASwitchCloudStatus == 0;
    }

    public static boolean isVonrSwitchEnabledByCloud(int i) {
        return 1 == getFiveGVonrCloudStatus(i);
    }

    public static boolean isVonrSwitchDisabledByCloud(int i) {
        return getFiveGVonrCloudStatus(i) == 0;
    }

    public static String getModemCrashReasonByCloud() {
        return sModemCrashReasonFromCloud;
    }

    public boolean isWlanDisSADisabledByCloud() {
        return this.mWlanDisSADisabledByCloud;
    }

    public static boolean is5GDsdaSwitchUnhandledByCloud() {
        return -1 == sFiveGDsdaCloudStatus;
    }

    public static boolean is5GDsdaEnabledByCloud() {
        return 1 == sFiveGDsdaCloudStatus;
    }

    public static FeatureCloudInfo getFeatureInfoByCloud(String str) {
        HashMap map = sFeatureCloudInfoMap;
        if (map == null) {
            return null;
        }
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            if (((String) it.next()).equals(str)) {
                return (FeatureCloudInfo) sFeatureCloudInfoMap.get(str);
            }
        }
        return null;
    }

    public static Intent getFeatureInfoIntentByCloud(String str) {
        HashMap map = sFeatureCloudInfoMap;
        if (map == null) {
            return null;
        }
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            if (((String) it.next()).equals(str)) {
                FeatureCloudInfo featureCloudInfo = (FeatureCloudInfo) sFeatureCloudInfoMap.get(str);
                if (featureCloudInfo == null) {
                    return null;
                }
                Intent intent = new Intent();
                intent.putExtra("FeatureType", featureCloudInfo.getFeatureType());
                intent.putExtra("Status", featureCloudInfo.getStatus());
                intent.putExtra("Params1", featureCloudInfo.getParams1());
                intent.putExtra("Params2", featureCloudInfo.getParams2());
                return intent;
            }
        }
        return null;
    }

    public static HashMap getFeatureCloudInfoMap() {
        return sFeatureCloudInfoMap;
    }

    public static boolean isFeatureDisabledByCloud(String str) {
        HashMap map = sFeatureCloudInfoMap;
        return (map == null || map.get(str) == null || ((FeatureCloudInfo) sFeatureCloudInfoMap.get(str)).status != 0) ? false : true;
    }

    public static boolean isFeatureEnabledByCloud(String str) {
        HashMap map = sFeatureCloudInfoMap;
        return (map == null || map.get(str) == null || ((FeatureCloudInfo) sFeatureCloudInfoMap.get(str)).status != 1) ? false : true;
    }

    public static boolean is5GSaPowerDisabledByCloud() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (is5GSaModeDisabledByCloud(i)) {
                return true;
            }
        }
        return getSAPowerCloudStatus() == 0;
    }

    public static int getFiveGSaCloudStatus(int i) {
        String simOperatorForSlot = TelephonyManager.getDefault().getSimOperatorForSlot(i);
        log("getFiveGSaCloudStatus slotId: " + i + ", operator: " + simOperatorForSlot);
        if (TextUtils.isEmpty(simOperatorForSlot)) {
            return -1;
        }
        if (sSaSimOperatorStatusMap.containsKey("All")) {
            return ((Integer) sSaSimOperatorStatusMap.get("All")).intValue();
        }
        if (sSaSimOperatorStatusMap.containsKey(simOperatorForSlot)) {
            return ((Integer) sSaSimOperatorStatusMap.get(simOperatorForSlot)).intValue();
        }
        return -1;
    }

    public static int getFiveGVonrCloudStatus(int i) {
        String simOperatorForSlot = TelephonyManager.getDefault().getSimOperatorForSlot(i);
        log("getFiveGVonrCloudStatus slotId: " + i + ", operator: " + simOperatorForSlot);
        if (TextUtils.isEmpty(simOperatorForSlot)) {
            return -1;
        }
        if (sVonrOperatorStatusMap.containsKey("All")) {
            return ((Integer) sVonrOperatorStatusMap.get("All")).intValue();
        }
        if (sVonrOperatorStatusMap.containsKey(simOperatorForSlot)) {
            return ((Integer) sVonrOperatorStatusMap.get(simOperatorForSlot)).intValue();
        }
        return -1;
    }

    public static int getModemFeatureCloudStatus(int i, int i2) {
        HashMap map;
        String simOperatorForSlot = TelephonyManager.getDefault().getSimOperatorForSlot(i2);
        log("getModemFeatureCloudStatus slotId: " + i2 + ", operator: " + simOperatorForSlot);
        if (TextUtils.isEmpty(simOperatorForSlot) || (map = (HashMap) mModefeatureOperatorStatusMap.get(Integer.valueOf(i))) == null || map.isEmpty()) {
            return -1;
        }
        if (map.containsKey("All")) {
            return ((Integer) map.get("All")).intValue();
        }
        if (map.containsKey(simOperatorForSlot)) {
            return ((Integer) map.get(simOperatorForSlot)).intValue();
        }
        return -1;
    }

    /* JADX WARN: Code duplicated, block: B:4:0x0007 A[PHI: r1
  0x0007: PHI (r1v6 boolean) = (r1v0 boolean), (r1v0 boolean), (r1v1 boolean) binds: [B:3:0x0005, B:6:0x000b, B:8:0x000e] A[DONT_GENERATE, DONT_INLINE]] */
    public static boolean get5GPowerCloudStatus(boolean z) {
        int i;
        boolean z2 = false;
        if (is5GSwitchClosedByCloud() || (i = sFiveGPowerCloudStatus) == 0) {
            z = z2;
        } else {
            z2 = true;
            if (i == 1) {
                z = z2;
            }
        }
        log("get5GPowerCloudStatus sFiveGSwitchCloudStatus: " + sFiveGSwitchCloudStatus + ", sFiveGPowerCloudStatus: " + sFiveGPowerCloudStatus);
        return z;
    }

    public static int getSAPowerCloudStatus() {
        log("getSAPowerCloudStatus, sSAPowerCloudStatus: " + sSAPowerCloudStatus);
        return sSAPowerCloudStatus;
    }

    public static int getSAPowerCloudEnableScene() {
        log("getSAPowerCloudEnableScene, sSAPowerCloudEnableScene: " + sSAPowerCloudEnableScene);
        return sSAPowerCloudEnableScene;
    }

    public static int getSAPowerCloudCdrxFequencyCheckTime() {
        log("getSAPowerCloudCdrxFequencyCheckTime, sSAPowerCloudCdrxFequencyCheckTime: " + sSAPowerCloudCdrxFequencyCheckTime);
        return sSAPowerCloudCdrxFequencyCheckTime;
    }

    public static boolean is5GGameEnabledByCloud() {
        return 1 == sFiveGGameCloudStatus;
    }

    public static int getSpeedInterval() {
        FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData = mSmartSwitchData;
        return fiveGCloudSmartSwitchData != null ? fiveGCloudSmartSwitchData.getSpeedInterval() : AnimTask.MAX_MAIN_THREAD_TASK_SIZE;
    }

    public static int getSignalQosWeakSignalBin() {
        FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData = mSmartSwitchData;
        if (fiveGCloudSmartSwitchData != null) {
            return fiveGCloudSmartSwitchData.getSignalQosWeakSignalBin();
        }
        return 4;
    }

    public static int getSignalQosVgSignalCn() {
        FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData = mSmartSwitchData;
        if (fiveGCloudSmartSwitchData != null) {
            return fiveGCloudSmartSwitchData.getSignalQosVgSignalCn();
        }
        return 15;
    }

    public static int getSignalQosSentPacketsCn() {
        FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData = mSmartSwitchData;
        if (fiveGCloudSmartSwitchData != null) {
            return fiveGCloudSmartSwitchData.getSignalQosSentPacketsCn();
        }
        return 10;
    }

    public static int getSignalQosNrUplink() {
        FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData = mSmartSwitchData;
        if (fiveGCloudSmartSwitchData != null) {
            return fiveGCloudSmartSwitchData.getSignalQosNrUplink();
        }
        return 2621440;
    }

    public static int getSignalQosNrDownlink() {
        FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData = mSmartSwitchData;
        if (fiveGCloudSmartSwitchData != null) {
            return fiveGCloudSmartSwitchData.getSignalQosNrDownlink();
        }
        return 2621440;
    }

    public static int getSignalQosLteUplink() {
        FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData = mSmartSwitchData;
        if (fiveGCloudSmartSwitchData != null) {
            return fiveGCloudSmartSwitchData.getSignalQosLteUplink();
        }
        return 131072;
    }

    public static int getSignalQosLteDownlink() {
        FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData = mSmartSwitchData;
        if (fiveGCloudSmartSwitchData != null) {
            return fiveGCloudSmartSwitchData.getSignalQosLteDownlink();
        }
        return 131072;
    }

    public static int getLowBatteryLevel() {
        FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData = mSmartSwitchData;
        if (fiveGCloudSmartSwitchData != null) {
            return fiveGCloudSmartSwitchData.getLowBatteryLevel();
        }
        return 30;
    }

    public static int getLowBatteryHighSpeed() {
        FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData = mSmartSwitchData;
        if (fiveGCloudSmartSwitchData != null) {
            return fiveGCloudSmartSwitchData.getLowBatteryHighSpeed();
        }
        return 655360;
    }

    public static int getLowBatteryLowSpeed() {
        FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData = mSmartSwitchData;
        if (fiveGCloudSmartSwitchData != null) {
            return fiveGCloudSmartSwitchData.getLowBatteryLowSpeed();
        }
        return 131072;
    }

    public static int getSpeedWhitelistHighSpeed(String str) {
        FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData = mSmartSwitchData;
        if (fiveGCloudSmartSwitchData != null) {
            return fiveGCloudSmartSwitchData.getSpeedWhitelistHighSpeed(str);
        }
        return 655360;
    }

    public static int getSpeedWhitelistLowSpeed(String str) {
        FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData = mSmartSwitchData;
        if (fiveGCloudSmartSwitchData != null) {
            return fiveGCloudSmartSwitchData.getSpeedWhitelistLowSpeed(str);
        }
        return 131072;
    }

    public static int getSpeedOnlyStatus() {
        FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData = mSmartSwitchData;
        if (fiveGCloudSmartSwitchData != null) {
            return fiveGCloudSmartSwitchData.getSpeedOnlyStatus();
        }
        return 1;
    }

    public static int getSpeedOnlyHighSpeed() {
        FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData = mSmartSwitchData;
        if (fiveGCloudSmartSwitchData != null) {
            return fiveGCloudSmartSwitchData.getSpeedOnlyHighSpeed();
        }
        return 655360;
    }

    public static int getSpeedOnlyLowSpeed() {
        FiveGCloudSmartSwitchData fiveGCloudSmartSwitchData = mSmartSwitchData;
        if (fiveGCloudSmartSwitchData != null) {
            return fiveGCloudSmartSwitchData.getSpeedOnlyLowSpeed();
        }
        return 131072;
    }

    public static String getPowerSaveOptStatus() {
        CloudPowerSaveData cloudPowerSaveData = mPowerSaveData;
        if (cloudPowerSaveData != null) {
            return cloudPowerSaveData.getOptStatus();
        }
        return null;
    }

    public static boolean isBalanceModeSupport() {
        CloudPowerSaveData cloudPowerSaveData = mPowerSaveData;
        if (cloudPowerSaveData != null) {
            return cloudPowerSaveData.isBalanceModeSupport();
        }
        return false;
    }

    public static int getMtkPingPongCloudStatus() {
        return sMtkPingPongCloudStatus;
    }

    public static int getModemMeasureRelaxCloudStatus() {
        return sModemMeasureRelaxCloudStatus;
    }

    public static List getDsda2DsdsWhiteList(Context context) {
        String string = Settings.Global.getString(context.getContentResolver(), "scene_dsda_change_dsds_white_list_pkg_name");
        if (TextUtils.isEmpty(string) && initDsda2DsdsWhiteListDefaultData()) {
            string = Settings.Global.getString(context.getContentResolver(), "scene_dsda_change_dsds_white_list_pkg_name");
        }
        log("getDsda2DsdsWhiteList pkgNames=" + string);
        if (TextUtils.isEmpty(string)) {
            return new ArrayList();
        }
        return new ArrayList(Arrays.asList(string.split(",")));
    }

    public static List getSmartSwitchWhiteList(Context context) {
        String string = Settings.Global.getString(context.getContentResolver(), "smart_switch_alert_dialog_white_list_pkg_name");
        if (TextUtils.isEmpty(string) && initSmartSwitchWhiteListDefaultData()) {
            string = Settings.Global.getString(context.getContentResolver(), "smart_switch_alert_dialog_white_list_pkg_name");
        }
        log("getSmartSwitchWhiteList pkgNames=" + string);
        if (TextUtils.isEmpty(string)) {
            return new ArrayList();
        }
        return new ArrayList(Arrays.asList(string.split(",")));
    }

    public static List getPingPongWhiteList(Context context) {
        String string = Settings.Global.getString(context.getContentResolver(), "ltenr_ping_pong_white_list_pkg_name");
        if (TextUtils.isEmpty(string)) {
            initPingPongWhiteListDefaultData();
            string = Settings.Global.getString(context.getContentResolver(), "ltenr_ping_pong_white_list_pkg_name");
        }
        log("getPingPongWhiteList pkgNames=" + string);
        if (TextUtils.isEmpty(string)) {
            return new ArrayList();
        }
        return new ArrayList(Arrays.asList(string.split(",")));
    }

    public static List getCommonSignalWhiteList(Context context) {
        String string = Settings.Global.getString(context.getContentResolver(), "common_signal_white_list_pkg_name");
        if (TextUtils.isEmpty(string)) {
            initCommonSignalWhiteListDefaultData();
            string = Settings.Global.getString(context.getContentResolver(), "common_signal_white_list_pkg_name");
        }
        log("getCommonSignalWhiteList pkgNames=" + string);
        if (TextUtils.isEmpty(string)) {
            return new ArrayList();
        }
        return new ArrayList(Arrays.asList(string.split(",")));
    }

    public static List getDualDataConcurrentModeWhiteList(Context context) {
        String string = Settings.Global.getString(context.getContentResolver(), "dual_data_concurrent_mode_white_list_pkg_name");
        if (TextUtils.isEmpty(string)) {
            initDualDataConcurrentModeWhiteListDefaultData();
            string = Settings.Global.getString(context.getContentResolver(), "dual_data_concurrent_mode_white_list_pkg_name");
        }
        log("getDualDataConcurrentModeWhiteList pkgNames=" + string);
        if (TextUtils.isEmpty(string)) {
            return new ArrayList();
        }
        return new ArrayList(Arrays.asList(string.split(",")));
    }

    public static List getDualDataConcurrentLimitedWhiteList(Context context) {
        String string = Settings.Global.getString(context.getContentResolver(), "dual_data_concurrent_limited_white_list_pkg_name");
        if (TextUtils.isEmpty(string)) {
            initDualDataConcurrentLimitedWhiteListDefaultData();
            string = Settings.Global.getString(context.getContentResolver(), "dual_data_concurrent_limited_white_list_pkg_name");
        }
        log("getDualDataConcurrentLimitedWhiteList pkgNames=" + string);
        if (TextUtils.isEmpty(string)) {
            return new ArrayList();
        }
        return new ArrayList(Arrays.asList(string.split(",")));
    }

    public static List getDualDataRedundantModeWhiteList(Context context) {
        String string = Settings.Global.getString(context.getContentResolver(), "dual_data_redundant_mode_white_list_pkg_name");
        if (TextUtils.isEmpty(string)) {
            initDualDataRedundantModeWhiteListDefaultData();
            string = Settings.Global.getString(context.getContentResolver(), "dual_data_redundant_mode_white_list_pkg_name");
        }
        log("getDualDataRedundantModeWhiteList pkgNames=" + string);
        if (TextUtils.isEmpty(string)) {
            return new ArrayList();
        }
        return new ArrayList(Arrays.asList(string.split(",")));
    }

    public static List getHikingModeCityWhiteList(Context context) {
        String string = Settings.Global.getString(context.getContentResolver(), "hiking_mode_city_white_list");
        if (TextUtils.isEmpty(string)) {
            initHikingModeCityWhiteListDefaultData();
            string = Settings.Global.getString(context.getContentResolver(), "hiking_mode_city_white_list");
        }
        log("getHikingModeCityWhiteList cityNames=" + string);
        if (TextUtils.isEmpty(string)) {
            return new ArrayList();
        }
        return new ArrayList(Arrays.asList(string.split(",")));
    }

    public static boolean getHstPreferFreqSwitch(Context context) {
        if (context == null) {
            return true;
        }
        String string = Settings.Global.getString(context.getContentResolver(), "HST_PREFER_FREQ_SWITCH");
        return TextUtils.isEmpty(string) || "1".equals(string) || !"0".equals(string);
    }

    public static boolean getHyperEngineGameSwitch(Context context) {
        if (context == null) {
            return true;
        }
        String string = Settings.Global.getString(context.getContentResolver(), "HYPER_ENGINE_GAME_SWITCH");
        return TextUtils.isEmpty(string) || "1".equals(string) || !"0".equals(string);
    }

    public static boolean getHyperEngineHstSwitch(Context context) {
        if (context == null) {
            return true;
        }
        String string = Settings.Global.getString(context.getContentResolver(), "HYPER_ENGINE_HST_SWITCH");
        return TextUtils.isEmpty(string) || "1".equals(string) || !"0".equals(string);
    }

    public static boolean getHyperEngineSmartHoSwitch(Context context) {
        if (context == null) {
            return true;
        }
        String string = Settings.Global.getString(context.getContentResolver(), "HYPER_ENGINE_SMART_HO_SWITCH");
        return TextUtils.isEmpty(string) || "1".equals(string) || !"0".equals(string);
    }

    public static List getResolutionVideoAppsWhiteList(Context context) {
        String string = Settings.Global.getString(context.getContentResolver(), "resolution_video_apps_white_list_pkg_name");
        if (TextUtils.isEmpty(string) && initResolutionVideoAppsWhiteListDefaultData()) {
            string = Settings.Global.getString(context.getContentResolver(), "resolution_video_apps_white_list_pkg_name");
        }
        log("getResolutionVideoAppsWhiteList pkgNames=" + string);
        if (TextUtils.isEmpty(string)) {
            return new ArrayList();
        }
        return new ArrayList(Arrays.asList(string.split(",")));
    }

    public static List getSubwayModeSignalThreshold(Context context) {
        String string = Settings.Global.getString(context.getContentResolver(), "subway_mode_signal_threshold");
        if (TextUtils.isEmpty(string)) {
            initSubwayModeSignalThresholdDefaultData();
            string = Settings.Global.getString(context.getContentResolver(), "subway_mode_signal_threshold");
        }
        log("getSubwayModeSignalThreshold value=" + string);
        if (TextUtils.isEmpty(string)) {
            return new ArrayList();
        }
        return new ArrayList(Arrays.asList(string.split(",")));
    }

    public static List getPreloadVideoAppsWhiteList(Context context) {
        String string = Settings.Global.getString(context.getContentResolver(), "preload_video_apps_white_list_pkg_name");
        if (TextUtils.isEmpty(string) && initPreloadVideoAppsWhiteListDefaultData()) {
            string = Settings.Global.getString(context.getContentResolver(), "preload_video_apps_white_list_pkg_name");
        }
        log("getPreloadVideoAppsWhiteList pkgNames=" + string);
        if (TextUtils.isEmpty(string)) {
            return new ArrayList();
        }
        return new ArrayList(Arrays.asList(string.split(",")));
    }

    public static List getHyperEngineGameWhiteList(Context context) {
        String string = Settings.Global.getString(context.getContentResolver(), "hyper_engine_game_white_list_pkg_name");
        if (TextUtils.isEmpty(string) && initHyperEngineGameWhiteListDefaultData()) {
            string = Settings.Global.getString(context.getContentResolver(), "hyper_engine_game_white_list_pkg_name");
        }
        log("getHyperEngineGameWhiteList pkgNames=" + string);
        if (TextUtils.isEmpty(string)) {
            return new ArrayList();
        }
        return new ArrayList(Arrays.asList(string.split(",")));
    }

    public static String getSignalMapSnsCellBlackList(Context context) {
        String string = Settings.Global.getString(context.getContentResolver(), "signal_map_sns_cell_black_list");
        if (TextUtils.isEmpty(string)) {
            initSignalMapSnsCellBlackListDefaultData();
            string = Settings.Global.getString(context.getContentResolver(), "signal_map_sns_cell_black_list");
        }
        log("getSignalMapSnsCellBlackList =" + string);
        return string;
    }

    public static boolean getAirportModeSwitch(Context context) {
        if (context == null) {
            return true;
        }
        String string = Settings.Global.getString(context.getContentResolver(), "airport_mode_switch");
        return TextUtils.isEmpty(string) || "1".equals(string) || !"0".equals(string);
    }

    public static boolean getFileUploadSwitch(Context context) {
        if (context == null) {
            return true;
        }
        String string = Settings.Global.getString(context.getContentResolver(), "file_upload_switch");
        return TextUtils.isEmpty(string) || "1".equals(string) || !"0".equals(string);
    }

    public static boolean getArrearSimNwDeniedMode(Context context) {
        if (context == null) {
            return true;
        }
        String string = Settings.Global.getString(context.getContentResolver(), "arrear_sim_nw_denied_mode");
        return TextUtils.isEmpty(string) || "1".equals(string) || !"0".equals(string);
    }

    public static boolean getHstTunnelSmartlink(Context context) {
        if (context == null) {
            return true;
        }
        String string = Settings.Global.getString(context.getContentResolver(), "hst_tunnel_smartlink");
        return TextUtils.isEmpty(string) || "1".equals(string) || !"0".equals(string);
    }

    public static boolean getSmartNetworkSwitch(Context context) {
        if (context == null) {
            return true;
        }
        String string = Settings.Global.getString(context.getContentResolver(), "smart_network_switch");
        return TextUtils.isEmpty(string) || "1".equals(string) || !"0".equals(string);
    }

    public static boolean getFivegLBStreamOpt(Context context) {
        if (context == null) {
            return true;
        }
        String string = Settings.Global.getString(context.getContentResolver(), "fiveg_lb_stream_opt");
        return TextUtils.isEmpty(string) || "1".equals(string) || !"0".equals(string);
    }

    public void notifyTeleFeatureCloudInfoChanged() {
        log("notifyTeleFeatureCloudInfoChanged");
        mContext.sendStickyBroadcast(new Intent("com.android.phone.intent.action.CLOUD_TELE_FEATURE_INFO_CHANGED"));
    }

    public static int getPreferredNetworkTypeVisibleByCloud(int i) {
        String simOperatorForSlot = TelephonyManager.getDefault().getSimOperatorForSlot(i);
        int preferredNetworkTypeVisibleSp = -1;
        if (TextUtils.isEmpty(simOperatorForSlot)) {
            return -1;
        }
        if (getPreferredNetworkTypeVisibleSp("All") != -1) {
            preferredNetworkTypeVisibleSp = getPreferredNetworkTypeVisibleSp("All");
        } else if (getPreferredNetworkTypeVisibleSp(simOperatorForSlot) != -1) {
            preferredNetworkTypeVisibleSp = getPreferredNetworkTypeVisibleSp(simOperatorForSlot);
        }
        log("getPreferredNetworkTypeVisibleByCloud, statusVal: " + preferredNetworkTypeVisibleSp);
        return preferredNetworkTypeVisibleSp;
    }

    public static boolean isPreferredNetworkTypeVisibleByCloud(int i) {
        return 1 == getPreferredNetworkTypeVisibleByCloud(i);
    }

    public static boolean isPreferredNetworkTypeInVisibleByCloud(int i) {
        return getPreferredNetworkTypeVisibleByCloud(i) == 0;
    }

    private static int getFiveGStatusByCloud(int i) {
        String simOperatorForSlot = TelephonyManager.getDefault().getSimOperatorForSlot(i);
        int fiveGSwitchStatusSp = -1;
        if (TextUtils.isEmpty(simOperatorForSlot)) {
            return -1;
        }
        if (getFiveGSwitchStatusSp("All") != -1) {
            fiveGSwitchStatusSp = getFiveGSwitchStatusSp("All");
        } else if (getFiveGSwitchStatusSp(simOperatorForSlot) != -1) {
            fiveGSwitchStatusSp = getFiveGSwitchStatusSp(simOperatorForSlot);
        }
        log("getFiveGStatusByCloud, statusVal: " + fiveGSwitchStatusSp);
        return fiveGSwitchStatusSp;
    }

    private static int getFiveGNewCallStatusByCloud(int i) {
        String simOperatorForSlot = TelephonyManager.getDefault().getSimOperatorForSlot(i);
        int fiveGNewCallVisibleSp = -1;
        if (TextUtils.isEmpty(simOperatorForSlot)) {
            return -1;
        }
        if (getFiveGNewCallVisibleSp("All") != -1) {
            fiveGNewCallVisibleSp = getFiveGNewCallVisibleSp("All");
        } else if (getFiveGNewCallVisibleSp(simOperatorForSlot) != -1) {
            fiveGNewCallVisibleSp = getFiveGNewCallVisibleSp(simOperatorForSlot);
        }
        log("getFiveGNewCallStatusByCloud, statusVal: " + fiveGNewCallVisibleSp);
        return fiveGNewCallVisibleSp;
    }

    public static boolean isFiveGEnableByCloud(int i) {
        return 1 == getFiveGStatusByCloud(i);
    }

    public static boolean isFiveGDisableByCloud(int i) {
        return getFiveGStatusByCloud(i) == 0;
    }

    public static boolean isFiveGNewCallEnableByCloud(int i) {
        return 1 == getFiveGNewCallStatusByCloud(i);
    }

    public static boolean isFiveNewCallDisableByCloud(int i) {
        return getFiveGNewCallStatusByCloud(i) == 0;
    }

    public static boolean isFiveGSwitchVisibleByCloud() {
        return 1 == getFiveGSwitchVisibleSp();
    }

    public static boolean isFiveGSwitchInVisibleByCloud() {
        return getFiveGSwitchVisibleSp() == 0;
    }

    public static int getPreferredNetworkTypeVisibleSp(String str) {
        Context context = mContext;
        if (context == null) {
            return -1;
        }
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("preferred_network_type_visible_status" + str, -1);
    }

    public static int getFiveGSwitchVisibleSp() {
        Context context = mContext;
        if (context == null) {
            return -1;
        }
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("five_g_switch_visible_status", -1);
    }

    public static int getFiveGSwitchStatusSp(String str) {
        Context context = mContext;
        if (context == null) {
            return -1;
        }
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("five_g_switch_status" + str, -1);
    }

    public static boolean isUlMimoOptimizationDisabledByCloud() {
        return sULMimoOptimizationCloudStatus == 0;
    }

    private void startCrowdSignalUpdates() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService("location");
        if (locationManager == null) {
            log("startCrowdSignalUpdates: LocationManager is null");
            return;
        }
        if (!locationManager.isProviderEnabled("passive")) {
            log("startCrowdSignalUpdates: PASSIVE_PROVIDER not enabled");
            return;
        }
        LocationListener locationListener = new LocationListener() { // from class: com.android.phone.FiveGCloudController.4
            @Override // android.location.LocationListener
            public void onProviderDisabled(String str) {
            }

            @Override // android.location.LocationListener
            public void onProviderEnabled(String str) {
            }

            @Override // android.location.LocationListener
            public void onStatusChanged(String str, int i, Bundle bundle) {
            }

            @Override // android.location.LocationListener
            public void onLocationChanged(Location location) {
                if (location != null) {
                    FiveGCloudController.this.writeCrowdSignalData(location);
                }
            }
        };
        this.mCrowdSignalListener = locationListener;
        locationManager.requestLocationUpdates("passive", 0L, 0.0f, locationListener, Looper.getMainLooper());
        log("startCrowdSignalUpdates: registered on PASSIVE_PROVIDER");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeCrowdSignalData(Location location) {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (jElapsedRealtime - this.mLastCrowdSignalTimeMs < MirrorDesktopHelper.TIMEOUT_MILLIS) {
            return;
        }
        this.mLastCrowdSignalTimeMs = jElapsedRealtime;
        final double latitude = location.getLatitude();
        final double longitude = location.getLongitude();
        final float accuracy = location.getAccuracy();
        final float speed = location.getSpeed();
        final long time = location.getTime();
        final String provider = location.getProvider();
        this.mCrowdSignalExecutor.execute(new Runnable() { // from class: com.android.phone.FiveGCloudController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$writeCrowdSignalData$1(latitude, longitude, accuracy, speed, time, provider);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$writeCrowdSignalData$1(double d, double d2, float f, float f2, long j, String str) {
        Address address;
        String str2 = this.mCachedGeocodeCity;
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (jElapsedRealtime - this.mLastGeocodeTimeMs >= 1800000) {
            try {
                List<Address> fromLocation = new Geocoder(mContext, Locale.CHINA).getFromLocation(d, d2, 1);
                if (fromLocation != null && !fromLocation.isEmpty() && (address = fromLocation.get(0)) != null) {
                    String locality = address.getLocality();
                    if (locality == null) {
                        locality = "";
                    }
                    str2 = locality;
                    this.mCachedGeocodeCity = str2;
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "writeCrowdSignalData: geocode error", e);
            }
            this.mLastGeocodeTimeMs = jElapsedRealtime;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("loc_precision", Float.valueOf(f));
        contentValues.put("grid_x", Double.valueOf(d));
        contentValues.put("grid_y", Double.valueOf(d2));
        contentValues.put("velocity", Float.valueOf(f2));
        contentValues.put("sample_ts", Long.valueOf(j));
        contentValues.put("source_type", str);
        contentValues.put("zone", str2);
        try {
            mContext.getContentResolver().insert(LinkCrowdContract.CONTENT_URI, contentValues);
        } catch (Exception e2) {
            Log.e(LOG_TAG, "writeCrowdSignalData: insert error", e2);
        }
    }

    protected void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService("location");
        if (locationManager != null) {
            String str = "network";
            if (!locationManager.isProviderEnabled("network")) {
                str = "passive";
                if (!locationManager.isProviderEnabled("passive")) {
                    str = "gps";
                    if (!locationManager.isProviderEnabled("gps")) {
                        str = null;
                    }
                }
            }
            log("provider : " + str);
            if (TextUtils.isEmpty(str)) {
                return;
            }
            locationManager.getCurrentLocation(str, null, mContext.getMainExecutor(), new Consumer() { // from class: com.android.phone.FiveGCloudController$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.onLocationChanged((Location) obj);
                }
            });
        }
    }

    public static int getFiveGNewCallVisibleSp(String str) {
        Context context = mContext;
        if (context == null) {
            return -1;
        }
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("five_g_new_call_status" + str, -1);
    }

    private void getAddressFromLocation(final Location location) {
        final double latitude = location.getLatitude();
        final double longitude = location.getLongitude();
        new Thread() { // from class: com.android.phone.FiveGCloudController.5
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                List<Address> fromLocation;
                try {
                    fromLocation = new Geocoder(FiveGCloudController.mContext, Locale.ENGLISH).getFromLocation(latitude, longitude, 1);
                } catch (Exception e) {
                    Log.e(FiveGCloudController.LOG_TAG, "Get Location Error");
                    e.printStackTrace();
                    fromLocation = null;
                }
                List<Address> list = fromLocation;
                if (list == null || list.isEmpty()) {
                    return;
                }
                synchronized (list) {
                    try {
                        for (Address address : list) {
                            if (address != null) {
                                FiveGCloudController.mCity = FiveGCloudController.this.formatAddress(address.getLocality());
                                FiveGCloudController.mProvince = FiveGCloudController.this.formatAddress(address.getAdminArea());
                                if (!FiveGCloudController.mProvince.equals(FiveGCloudController.mLastProvince) || !FiveGCloudController.mCity.equals(FiveGCloudController.mLastCity)) {
                                    FiveGManagerBase.localLog("getAddressFromLocation", "getLocality = " + address.getLocality() + ", getAdminArea = " + address.getAdminArea() + ",  mCity = " + FiveGCloudController.mCity.toString() + ", mProvince = " + FiveGCloudController.mProvince.toString());
                                    FiveGCloudController.this.fetch5GSaCloudStatus("Telephony5GSaSwitch", true);
                                    FiveGCloudController.this.fetchVonrSwitchCloudStatus(true);
                                    FiveGCloudController.this.fetchForceSwitch5GCloud();
                                    if (MiuiPhoneUtils.isDeviceDsdaSupportedByQcom() && MiuiPhoneUtils.getInsertedSimCount() == 2) {
                                        FiveGCloudController.this.fetch5GDsdaCloudStatusByQcom(true);
                                    }
                                    ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).updateAddressInfo(location, address);
                                    FiveGCloudController.mLastCity = FiveGCloudController.mCity;
                                    FiveGCloudController.mLastProvince = FiveGCloudController.mProvince;
                                }
                            }
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String formatAddress(String str) {
        if (str != null) {
            return str.contains(" ") ? str.substring(0, str.indexOf(" ")) : str;
        }
        return "";
    }

    public void fetchTrafficControlData() {
        log("fetchTrafficControlData Enter");
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(mContext.getContentResolver(), "TelephonyTrafficControl");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    log("fetchTrafficControlData data=" + cloudData.toString());
                    mTrafficControlData = new CloudTrafficControlData(cloudData);
                }
                return;
            }
            log("fetchTrafficControlData cloudList is empty");
            initTrafficControlDefaultData();
        } catch (Exception e) {
            log("fetchTrafficControlData exception" + e);
            initTrafficControlDefaultData();
        }
    }

    private void initTrafficControlDefaultData() {
        List featureConfig = MiuiPhoneUtils.getFeatureConfig("traffic_control_white_list_default");
        log("initTrafficControlDefaultData whitelist=" + featureConfig);
        FiveGCloudUtils.saveListToDb(new HashSet(featureConfig), "mobile_tc_white_list_pkg_name");
        List featureConfig2 = MiuiPhoneUtils.getFeatureConfig("traffic_control_enabled_list_default");
        log("initTrafficControlDefaultData enabledlist=" + featureConfig2);
        FiveGCloudUtils.saveListToDb(new HashSet(featureConfig2), "mobile_tc_enabled_list_pkg_name");
    }

    public HashMap getCloudStatus() {
        int i = 0;
        while (true) {
            String[] strArr = FEATURE_CLOUD_STATISTIC_DEFAULT;
            if (i < strArr.length) {
                HashMap map = sFeatureCloudStatusMap;
                String str = strArr[i];
                map.put(str, getCloudStatus(str));
                i++;
            } else {
                return sFeatureCloudStatusMap;
            }
        }
    }

    public static HashMap cloneFeatureInfo(HashMap map) {
        HashMap map2 = new HashMap();
        for (String str : map.keySet()) {
            try {
                map2.put(str, ((FeatureCloudInfo) map.get(str)).m589clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return map2;
    }

    public String getCity() {
        return TextUtils.isEmpty(mCity) ? "" : mCity;
    }

    public String getProvince() {
        return TextUtils.isEmpty(mProvince) ? "" : mProvince;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:67:0x00b8 A[RETURN] */
    private String getCloudStatus(String str) {
        log("getCloudStatus category = " + str);
        str.hashCode();
        int i = 3;
        int i2 = 0;
        switch (str) {
            case "TelephonyBWPMode":
            case "TelephonyN28Mode":
            case "TelephonyN1Mode":
                if (str != "TelephonyN1Mode") {
                    if (str == "TelephonyN28Mode") {
                        i = 1;
                    } else {
                        i = str == "TelephonyBWPMode" ? 2 : -1;
                    }
                }
                while (i2 < MiuiPhoneUtils.PHONE_COUNT) {
                    if (getModemFeatureCloudStatus(i, i2) != -1) {
                        return "1";
                    }
                    i2++;
                }
                return "0";
            case "Telephony5GDsdaSwitch":
                if (fetch5GCloudStatus(str) != -1) {
                    return "1";
                }
                return "0";
            case "Telephony5GSaSwitch":
                while (i2 < MiuiPhoneUtils.PHONE_COUNT) {
                    if (!is5GSaSwitchUnhandledByCloud(i2)) {
                        return "1";
                    }
                    i2++;
                }
                return "0";
            case "TelephonyVonrSwitch":
                while (i2 < MiuiPhoneUtils.PHONE_COUNT) {
                    if (getFiveGVonrCloudStatus(i2) != -1) {
                        return "1";
                    }
                    i2++;
                }
                return "0";
            case "TelephonyViceSaSwitch":
                if (-1 != sViceSASwitchCloudStatus) {
                    return "1";
                }
                return "0";
            default:
                return "0";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d(LOG_TAG, str);
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(Location location) {
        if (location != null) {
            log("onLocationChanged: provider=" + location.getProvider() + " accuracy=" + location.getAccuracy() + " time=" + location.getTime());
            getAddressFromLocation(location);
        }
    }

    public class FeatureCloudInfo {
        private String featureType;
        private String params1;
        private String params2;
        private int status;

        public FeatureCloudInfo(String str, int i, String str2, String str3) {
            this.featureType = str;
            this.status = i;
            this.params1 = str2;
            this.params2 = str3;
        }

        public String getFeatureType() {
            return this.featureType;
        }

        public int getStatus() {
            return this.status;
        }

        public String getParams1() {
            return this.params1;
        }

        public String getParams2() {
            return this.params2;
        }

        public String toString() {
            return "[FeatureCloudInfo] - featureType = " + this.featureType + ", status = " + this.status + ", params1 = " + this.params1 + ", params2 = " + this.params2;
        }

        public int hashCode() {
            return Objects.hash(this.featureType, Integer.valueOf(this.status), this.params1, this.params2);
        }

        public boolean equals(Object obj) {
            if (obj instanceof FeatureCloudInfo) {
                FeatureCloudInfo featureCloudInfo = (FeatureCloudInfo) obj;
                if (this.featureType.equals(featureCloudInfo.featureType) && this.status == featureCloudInfo.status && this.params1.equals(featureCloudInfo.params1) && this.params2.equals(featureCloudInfo.params2)) {
                    return true;
                }
            }
            return false;
        }

        /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
        public FeatureCloudInfo m589clone() {
            return FiveGCloudController.this.new FeatureCloudInfo(this.featureType, this.status, this.params1, this.params2);
        }
    }

    class ModemFeatureCloudInfo {
        private int featureType;
        private int featureValue;
        private boolean isRefreshMbn;

        ModemFeatureCloudInfo(FiveGCloudController fiveGCloudController, int i, int i2, boolean z) {
            this.featureType = i;
            this.featureValue = i2;
            this.isRefreshMbn = z;
        }

        public String toString() {
            return "[ModemFeatureCloudInfo] - featureType = " + this.featureType + ", featureValue = " + this.featureValue + ", isRefreshMbn = " + this.isRefreshMbn;
        }
    }

    class ModemQmiMessageInfo {
        private int featureType;
        private int featureValue;

        ModemQmiMessageInfo(FiveGCloudController fiveGCloudController, int i, int i2) {
            this.featureType = i;
            this.featureValue = i2;
        }

        public String toString() {
            return "[ModemQmiMessageInfo] - featureType = " + this.featureType + ", featureValue = " + this.featureValue;
        }
    }

    class FiveGCloudData {
        private String cdrxFequencyCheckTime;
        private String city;
        private String customizedRegion;
        private String enableScene;
        private String featureType;
        private String featureVersion;
        private String fiveGSwitchVisible;
        private String isRefreshMbn;
        private String model;
        private String modemFeatureType;
        private String modemFeatureValue;
        private String params1;
        private String params2;
        private String preferredNetworkTypeVisible;
        private String province;
        private String qmiFeatureType;
        private String qmiFeatureValue;
        private String simOperator;
        private String status;
        private String versionType;

        public void clearFiveGNewCallCloudStatusSp() {
        }

        public FiveGCloudData(MiuiSettings.SettingsCloudData.CloudData cloudData) {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(FiveGCloudController.mContext);
            SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
            for (String str : defaultSharedPreferences.getAll().keySet()) {
                if (str.indexOf("five_g_new_call_status") != -1) {
                    editorEdit.remove(str);
                }
            }
            editorEdit.commit();
            this.model = cloudData.getString("Model", "NA");
            this.versionType = cloudData.getString("DeviceVersionType", "NA");
            this.customizedRegion = cloudData.getString("CustomizedRegion", "NA");
            this.simOperator = cloudData.getString("SimOperator", "NA");
            this.enableScene = cloudData.getString("EnableVersion", "NA");
            this.province = cloudData.getString("Province", "NA");
            this.city = cloudData.getString("City", "NA");
            this.status = cloudData.getString("Status", "NA");
            this.cdrxFequencyCheckTime = cloudData.getString("CdrxFrequencyCheckTime", "NA");
            this.featureType = cloudData.getString("FeatureType", "NA");
            this.params1 = cloudData.getString("Params1", "NA");
            this.params2 = cloudData.getString("Params2", "NA");
            this.modemFeatureType = cloudData.getString("ModemFeatureType", "NA");
            this.modemFeatureValue = cloudData.getString("ModemFeatureValue", "NA");
            this.qmiFeatureType = cloudData.getString("QmiFeatureType", "NA");
            this.qmiFeatureValue = cloudData.getString("QmiFeatureValue", "NA");
            this.isRefreshMbn = cloudData.getString("Refreshmbn", "NA");
            this.preferredNetworkTypeVisible = cloudData.getString("PreferredNetworkTypeVisible", "NA");
            this.fiveGSwitchVisible = cloudData.getString("FiveGSwitchVisible", "NA");
            this.featureVersion = cloudData.getString("FeatureVersion", "NA");
        }

        public void addFiveGSaCloudStatus() {
            String str;
            if (isBasicInfoMatched() && isLocationMatched("Telephony5GSaSwitch") && (str = this.simOperator) != "NA") {
                String[] strArrSplit = str.split(",");
                for (int i = 0; i < strArrSplit.length; i++) {
                    int cloudStatus = getCloudStatus();
                    FiveGCloudController.sSaSimOperatorStatusMap.put(strArrSplit[i], Integer.valueOf(cloudStatus));
                    FiveGCloudController.log("addFiveGSaCloudStatus mccmnc=" + strArrSplit[i] + ", status=" + cloudStatus);
                }
            }
        }

        public void addFiveGVonrCloudStatus() {
            String str;
            if (isBasicInfoMatched() && isLocationMatched("TelephonyVonrSwitch") && (str = this.simOperator) != "NA") {
                String[] strArrSplit = str.split(",");
                for (int i = 0; i < strArrSplit.length; i++) {
                    int cloudStatus = getCloudStatus();
                    FiveGCloudController.sVonrOperatorStatusMap.put(strArrSplit[i], Integer.valueOf(cloudStatus));
                    FiveGCloudController.log("addFiveGVonrCloudStatus mccmnc=" + strArrSplit[i] + ", status=" + cloudStatus);
                }
            }
        }

        public void addModemFeatureCloudStatus(int i) {
            String str;
            String str2;
            if (i == 1) {
                str = "TelephonyN28Mode";
            } else if (i == 3) {
                str = "TelephonyN1Mode";
            } else if (i == 2) {
                str = "TelephonyBWPMode";
            } else if (i == 5) {
                str = "TelephonyN5Mode";
            } else {
                str = i == 6 ? "TelephonyN8Mode" : null;
            }
            if (isBasicInfoMatched() && isLocationMatched(str) && (str2 = this.simOperator) != "NA") {
                String[] strArrSplit = str2.split(",");
                HashMap map = new HashMap();
                for (int i2 = 0; i2 < strArrSplit.length; i2++) {
                    int cloudStatus = getCloudStatus();
                    map.put(strArrSplit[i2], Integer.valueOf(cloudStatus));
                    FiveGCloudController.log("addModemFeatureCloudStatus mccmnc=" + strArrSplit[i2] + ", status=" + cloudStatus);
                }
                FiveGCloudController.mModefeatureOperatorStatusMap.put(Integer.valueOf(i), map);
            }
        }

        public void addPreferredNetworkTypeVisibleCloudStatus() {
            if (isBasicInfoMatched() && isLocationMatched("TelephonyForceSwitch5G") && !"NA".equals(this.simOperator)) {
                clearPreferredNetworkTypeVisibleSp();
                setPreferredNetworkTypeVisibleSp();
            }
        }

        public void addFiveGStatusCloudStatus() {
            if (isBasicInfoMatched() && isLocationMatched("TelephonyForceSwitch5G") && !"NA".equals(this.simOperator)) {
                clearFiveGStatusCloudStatusSp();
                setFiveGStatusCloudStatusSp();
            }
        }

        private void setPreferredNetworkTypeVisibleSp() {
            SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(FiveGCloudController.mContext).edit();
            String[] strArrSplit = this.simOperator.split(",");
            for (int i = 0; i < strArrSplit.length; i++) {
                int preferredNetworkTypeVisible = getPreferredNetworkTypeVisible();
                if ("All".equalsIgnoreCase(strArrSplit[i])) {
                    strArrSplit[i] = "All";
                }
                editorEdit.putInt("preferred_network_type_visible_status" + strArrSplit[i], preferredNetworkTypeVisible);
                FiveGCloudController.log("setPreferredNetworkTypeVisibleSp mccmnc=" + strArrSplit[i] + ", status=" + preferredNetworkTypeVisible);
            }
            editorEdit.commit();
        }

        public void addFiveGNewCallStatusCloudStatus() {
            if (isBasicInfoMatched() && isLocationMatched("Telephony5GNewCall") && !"NA".equals(this.simOperator)) {
                clearFiveGNewCallCloudStatusSp();
                setFiveGNewCallCloudStatusSp();
            }
        }

        private void setFiveGSwitchVisibleSp(int i) {
            PreferenceManager.getDefaultSharedPreferences(FiveGCloudController.mContext).edit().putInt("five_g_switch_visible_status", i).commit();
        }

        private void setFiveGStatusCloudStatusSp() {
            SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(FiveGCloudController.mContext).edit();
            String[] strArrSplit = this.simOperator.split(",");
            for (int i = 0; i < strArrSplit.length; i++) {
                int cloudStatus = getCloudStatus();
                if ("All".equalsIgnoreCase(strArrSplit[i])) {
                    strArrSplit[i] = "All";
                }
                editorEdit.putInt("five_g_switch_status" + strArrSplit[i], cloudStatus);
                FiveGCloudController.log("setFiveGStatusCloudStatusSp mccmnc=" + strArrSplit[i] + ", status=" + cloudStatus);
            }
            editorEdit.commit();
        }

        private void setFiveGNewCallCloudStatusSp() {
            SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(FiveGCloudController.mContext).edit();
            String[] strArrSplit = this.simOperator.split(",");
            for (int i = 0; i < strArrSplit.length; i++) {
                int cloudStatus = getCloudStatus();
                if ("All".equalsIgnoreCase(strArrSplit[i])) {
                    strArrSplit[i] = "All";
                }
                editorEdit.putInt("five_g_new_call_status" + strArrSplit[i], cloudStatus);
                FiveGCloudController.log("setFiveGNewCallCloudStatusSp status=" + cloudStatus);
            }
            editorEdit.commit();
        }

        private void clearPreferredNetworkTypeVisibleSp() {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(FiveGCloudController.mContext);
            SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
            for (String str : defaultSharedPreferences.getAll().keySet()) {
                if (str.indexOf("preferred_network_type_visible_status") != -1) {
                    editorEdit.remove(str);
                }
            }
            editorEdit.commit();
        }

        private void clearFiveGStatusCloudStatusSp() {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(FiveGCloudController.mContext);
            SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
            for (String str : defaultSharedPreferences.getAll().keySet()) {
                if (str.indexOf("five_g_switch_status") != -1) {
                    editorEdit.remove(str);
                }
            }
            editorEdit.commit();
        }

        public boolean isBasicInfoMatched() {
            return FiveGCloudUtils.modelMatched(this.model) && FiveGCloudUtils.versionTypeMatched(this.versionType) && FiveGCloudUtils.customizedRegionMatched(this.customizedRegion);
        }

        public boolean isLocationMatched(String str) {
            FiveGManagerBase.localLog(FiveGCloudController.LOG_TAG, "isLocationMatched", "category = " + str + ": province = " + this.province + ", city = " + this.city + " ; mProvince = " + FiveGCloudController.mProvince + ", mCity = " + FiveGCloudController.mCity);
            String str2 = this.province;
            return (str2 == "NA" && this.city == "NA") || "All".equals(str2) || this.province.contains(FiveGCloudController.mProvince) || this.city.contains(FiveGCloudController.mCity);
        }

        public int getFiveGCloudStatus(String str) {
            if (!isMatched()) {
                return -1;
            }
            if (str.equals("SpecialNetwork") || str.equals("TelephonyTrafficControl") || str.equals("SmartSwitch")) {
                return getCloudStatus();
            }
            if (isLocationMatched(str)) {
                return getCloudStatus();
            }
            return -1;
        }

        public String getFeatureVersion() {
            return this.featureVersion;
        }

        public int getPreferredNetworkTypeVisible() {
            if ("NA".equals(this.preferredNetworkTypeVisible)) {
                return -1;
            }
            try {
                return Integer.parseInt(this.preferredNetworkTypeVisible);
            } catch (Exception unused) {
                return -1;
            }
        }

        public void setFiveGSwitchVisible() {
            if (isBasicInfoMatched() && isLocationMatched("TelephonyForceSwitch5G") && !"NA".equals(this.simOperator)) {
                if ("NA".equals(this.fiveGSwitchVisible)) {
                    setFiveGSwitchVisibleSp(-1);
                } else {
                    try {
                        setFiveGSwitchVisibleSp(Integer.parseInt(this.fiveGSwitchVisible));
                    } catch (Exception unused) {
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public FeatureCloudInfo getFeatureCloudInfo() {
            String str;
            String str2;
            String str3;
            String str4;
            if (!isMatched()) {
                return null;
            }
            FiveGCloudController fiveGCloudController = FiveGCloudController.this;
            if (TextUtils.equals(this.featureType, "NA")) {
                str = "";
                str2 = str;
            } else {
                str = this.featureType;
                str2 = "";
            }
            int cloudStatus = getCloudStatus();
            String str5 = TextUtils.equals(this.params1, "NA") ? str2 : this.params1;
            if (TextUtils.equals(this.params2, "NA")) {
                String str6 = str5;
                str4 = str2;
                str3 = str6;
            } else {
                str3 = str5;
                str4 = this.params2;
            }
            return fiveGCloudController.new FeatureCloudInfo(str, cloudStatus, str3, str4);
        }

        public String getStatusFromCloud() {
            if (isMatched()) {
                return this.status;
            }
            return "NA";
        }

        public int getCloudEnableScene() {
            if ("NA".equals(this.enableScene)) {
                return 0;
            }
            try {
                return Integer.parseInt(this.enableScene);
            } catch (Exception unused) {
                return 0;
            }
        }

        public int getCloudCdrxFequencyCheckTime() {
            if ("NA".equals(this.cdrxFequencyCheckTime)) {
                return -1;
            }
            try {
                return Integer.parseInt(this.cdrxFequencyCheckTime);
            } catch (Exception unused) {
                return -1;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public ModemFeatureCloudInfo getModemFeatureCloudInfo() {
            int i;
            int i2;
            if (!isMatched()) {
                return null;
            }
            boolean z = false;
            int i3 = -1;
            try {
                i2 = !"NA".equals(this.modemFeatureType) ? Integer.parseInt(this.modemFeatureType) : -1;
                try {
                    i3 = "NA".equals(this.modemFeatureValue) ? -1 : Integer.parseInt(this.modemFeatureValue);
                    if (!"NA".equals(this.isRefreshMbn) && Integer.parseInt(this.isRefreshMbn) == 1) {
                        z = true;
                    }
                } catch (Exception unused) {
                    i = i3;
                    i3 = i2;
                    i2 = i3;
                    i3 = i;
                }
            } catch (Exception unused2) {
                i = -1;
            }
            return new ModemFeatureCloudInfo(FiveGCloudController.this, i2, i3, z);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public ModemQmiMessageInfo getModemQmiMessageCloudInfo() {
            int i;
            if (!isMatched()) {
                return null;
            }
            int i2 = -1;
            try {
                i = !"NA".equals(this.qmiFeatureType) ? Integer.parseInt(this.qmiFeatureType) : -1;
                try {
                    if (!"NA".equals(this.qmiFeatureValue)) {
                        i2 = Integer.parseInt(this.qmiFeatureValue);
                    }
                } catch (Exception e) {
                    e = e;
                    FiveGCloudController.log("getModemQmiMessageCloudInfo exception" + e);
                    e.printStackTrace();
                }
            } catch (Exception e2) {
                e = e2;
                i = -1;
            }
            return new ModemQmiMessageInfo(FiveGCloudController.this, i, i2);
        }

        public boolean isMatched() {
            return FiveGCloudUtils.modelMatched(this.model) && FiveGCloudUtils.versionTypeMatched(this.versionType) && FiveGCloudUtils.customizedRegionMatched(this.customizedRegion) && FiveGCloudUtils.simOperatorMatched(this.simOperator) && isLocationMatched("ModemFeatures");
        }

        private int getCloudStatus() {
            try {
                int i = Integer.parseInt(this.status);
                if (i == 1 || i == 0) {
                    return i;
                }
                return -1;
            } catch (Exception unused) {
                return -1;
            }
        }

        public String toString() {
            return "FiveGCloudData[" + this.model + ", " + this.versionType + ", " + this.customizedRegion + ", " + this.simOperator + ", " + this.province + ", " + this.city + ", " + this.status + ", ]";
        }
    }

    private void notifyCloudChangeListener() {
        Bundle bundle = new Bundle();
        bundle.putString("City", mCity);
        bundle.putString("Province", mProvince);
        EventHubManager.getInstance().postEvent(new TelephonyEvent.CloudEvent("cloud_update", bundle));
    }

    public static boolean geolocationMatched(String str, String str2) {
        if ("All".equalsIgnoreCase(str) || TextUtils.equals(str, mProvince)) {
            return "All".equalsIgnoreCase(str2) || TextUtils.equals(str2, mCity);
        }
        return false;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("FiveGCloudController: ".concat(" sFeatureCloudInfoMap=" + sFeatureCloudInfoMap + "\n"));
    }
}
