package com.android.phone;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import com.android.phone.CloudController.TelephonyCloudController;
import com.android.phone.module.INetworkModule;
import com.android.phone.module.IUtilityModule;
import com.android.phone.statistics.MiuiTelephonyStatistic;
import com.android.phone.statistics.ModemComponentStats;
import com.android.phone.statistics.SignalStrengthMonitor;
import com.android.phone.statistics.SimStatistic;
import com.android.phone.utils.MiEventUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.OneTrackImpl;
import com.android.phone.utils.PubSubTrackImpl;
import com.android.services.telephony.Lazy;
import com.miui.misight.MiEvent;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class UtilityDelegateImpl implements IUtilityModule {
    private static volatile UtilityDelegateImpl sInstance;
    private Lazy mSensorManager = new Lazy();
    private Lazy mStatistic = new Lazy();
    private Lazy mCloudCarrierConfig = new Lazy();

    public static UtilityDelegateImpl init() {
        if (sInstance == null) {
            synchronized (UtilityDelegateImpl.class) {
                try {
                    if (sInstance == null) {
                        UtilityDelegateImpl utilityDelegateImpl = new UtilityDelegateImpl();
                        ModuleDelegate.register(IUtilityModule.class, utilityDelegateImpl);
                        sInstance = utilityDelegateImpl;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    private UtilityDelegateImpl() {
    }

    public void setSensorManager(TelephonySensorManager telephonySensorManager) {
        this.mSensorManager.set(telephonySensorManager);
    }

    public void setStatistic(MiuiTelephonyStatistic miuiTelephonyStatistic) {
        this.mStatistic.set(miuiTelephonyStatistic);
    }

    public void setCloudCarrierConfig(MiuiCloudCarrierConfigManager miuiCloudCarrierConfigManager) {
        this.mCloudCarrierConfig.set(miuiCloudCarrierConfigManager);
    }

    @Override // com.android.phone.module.IUtilityModule
    public void notifyQet7100CloudData() {
        ModemComponentStats modemComponentStats = ModemComponentStats.getInstance();
        if (modemComponentStats != null) {
            modemComponentStats.notifyQet7100CloudData();
        }
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean isSupportImsRegOptByCloud() {
        return !TelephonyCloudController.CLOUD_CONTROLLER_IMS_IMSREGISTER_V1_DISABLED;
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean isSupportVoiceLinkByCloud() {
        return !TelephonyCloudController.CLOUD_VOICE_LINK_FEATURE_DISABLED;
    }

    @Override // com.android.phone.module.IUtilityModule
    public void resetByCause(Context context, int i) {
        MiuiTelephonyStatistic.resetByCause(context, i);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean isSupportFdsLogUploadByCloud() {
        return !TelephonyCloudController.CLOUD_FDS_LOG_UPLOAD_DISABLED;
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean isSupportBasementByCloud() {
        return !TelephonyCloudController.CLOUD_CONFIG_BASEMENT_FEATURE_DISABLED;
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean isSupportBadCellOptByCloud() {
        return !TelephonyCloudController.CLOUD_BAD_CELL_OPT_DISABLED;
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean isSupportDynamicAntennaTuningByCloud() {
        return TelephonyCloudController.CLOUD_CONFIG_DYNAMIC_ANTENNA_TUNING_FEATURE_ENABLED;
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean isSupportPowerSavingOptByCloud() {
        return !TelephonyCloudController.CLOUD_TIERED_POWER_SAVING_OPT_DISABLED;
    }

    @Override // com.android.phone.module.IUtilityModule
    public void updateCloudEsimCountryList(String str) {
        CloudEsimCountryListController.getInstance();
    }

    @Override // com.android.phone.module.IUtilityModule
    public void reportTelephonyStatistic() throws Throwable {
        MiuiTelephonyStatistic miuiTelephonyStatistic = (MiuiTelephonyStatistic) this.mStatistic.get();
        if (miuiTelephonyStatistic != null) {
            miuiTelephonyStatistic.reportTelephonyStatisticData();
        }
    }

    @Override // com.android.phone.module.IUtilityModule
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (MiuiCloudDataManager.getInstance() != null) {
            MiuiCloudDataManager.getInstance().dump(fileDescriptor, printWriter, strArr);
        }
        if (this.mStatistic.get() != null) {
            ((MiuiTelephonyStatistic) this.mStatistic.get()).dump(fileDescriptor, printWriter, strArr);
        }
        if (this.mCloudCarrierConfig.get() != null) {
            ((MiuiCloudCarrierConfigManager) this.mCloudCarrierConfig.get()).dump(fileDescriptor, printWriter, strArr);
        }
        if (FiveGCloudController.getInstance() != null) {
            FiveGCloudController.getInstance().dump(fileDescriptor, printWriter, strArr);
        }
        SignalStrengthMonitor.dump(fileDescriptor, printWriter, strArr);
        SimStatistic.dump(fileDescriptor, printWriter, strArr);
        if (MiuiPhoneUtils.isSnsOptimizationSupported()) {
            ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).dumpQmsMsgTunnel(fileDescriptor, printWriter, strArr);
        }
    }

    @Override // com.android.phone.module.IUtilityModule
    public void trackerDSDACallExceptionMiEvent(int i) {
        MiEvent miEventConstructEvent = MiEventUtils.constructEvent(915200003);
        if (i == 1) {
            miEventConstructEvent.addStr("EventReason", "InWeakNrRsrp");
        } else if (i == 2) {
            miEventConstructEvent.addStr("EventReason", "InInternational");
        } else if (i == 3) {
            miEventConstructEvent.addStr("EventReason", "InSwitchDsds");
        } else if (i != 4) {
            return;
        } else {
            miEventConstructEvent.addStr("EventReason", "InPseudoDsda");
        }
        Log.d("UtilityDelegate", "trackerDSDACallExceptionMiEvent action: " + i);
        MiEventUtils.sendEvent(miEventConstructEvent);
    }

    @Override // com.android.phone.module.IUtilityModule
    public void syncCloudCarrierConfig(String str, PersistableBundle persistableBundle) {
        if (this.mCloudCarrierConfig.get() == null) {
            return;
        }
        ((MiuiCloudCarrierConfigManager) this.mCloudCarrierConfig.get()).syncCloudCarrierConfig(str, persistableBundle);
    }

    @Override // com.android.phone.module.IUtilityModule
    public void recordSatelliteBusinessEntrance(HashMap map) {
        MiuiTelephonyStatistic.recordSatelliteBusinessEntrance(map);
    }

    @Override // com.android.phone.module.IUtilityModule
    public void recordSatelliteEnabled(HashMap map) {
        MiuiTelephonyStatistic.recordSatelliteEnabled(map);
    }

    @Override // com.android.phone.module.IUtilityModule
    public void recordSatelliteHighPenetration(HashMap map) {
        MiuiTelephonyStatistic.recordSatelliteHighPenetration(map);
    }

    @Override // com.android.phone.module.IUtilityModule
    public void recordSatelliteOta(HashMap map) {
        MiuiTelephonyStatistic.recordSatelliteOta(map);
    }

    @Override // com.android.phone.module.IUtilityModule
    public void trackerMiEvent(int i, int i2) {
        MiEventUtils.trackerMiEvent(i, i2);
    }

    @Override // com.android.phone.module.IUtilityModule
    public void oneTrackEvent(String str, Map map) {
        OneTrackImpl.trackEvent(str, map);
    }

    @Override // com.android.phone.module.IUtilityModule
    public void enableOnetrackStatAccessNetwork(Context context, boolean z) {
        if (z) {
            OneTrackImpl.enableOnetrackStatAccessNetwork(context);
        } else {
            PubSubTrackImpl.enableOnetrackStatAccessNetwork(context);
        }
    }

    @Override // com.android.phone.module.IUtilityModule
    public Bundle getCloudData() {
        Bundle bundle = new Bundle();
        if (FiveGCloudController.getInstance() != null) {
            bundle.putString("province", FiveGCloudController.getInstance().getProvince());
            bundle.putString("city", FiveGCloudController.getInstance().getCity());
            FiveGCloudController.FeatureCloudInfo featureInfoByCloud = FiveGCloudController.getFeatureInfoByCloud("fds_log_upload_config");
            bundle.putString("fdsparam1", featureInfoByCloud != null ? featureInfoByCloud.getParams1() : "");
            bundle.putBoolean("5g_switch_invisible", FiveGCloudController.isFiveGSwitchInVisibleByCloud());
            bundle.putBoolean("5g_switch_visible", FiveGCloudController.isFiveGSwitchInVisibleByCloud());
            bundle.putBoolean("non_dds_rrc_off", FiveGCloudController.getInstance().isModemNonddsRrcDisabledByCloud());
            bundle.putBoolean("disable_wlan_sa", FiveGCloudController.getInstance().isWlanDisSADisabledByCloud());
            bundle.putBoolean("thermal_throttle_off", FiveGCloudController.getInstance().isThermalThrottleDisabledByCloud());
            bundle.putBoolean("cdma_data_off", FiveGCloudController.isCdmaDataOptimizeDisableByCloud());
            bundle.putBoolean("ard_optmize_off", FiveGCloudController.getInstance().isARDOptimizationDisabledByCloud());
            bundle.putBoolean("ul_mimo_optmize_off", FiveGCloudController.isUlMimoOptimizationDisabledByCloud());
            bundle.putBoolean("limit_lteca_for_night_off", FiveGCloudController.getInstance().isLimitLTECAForNightModeDisabledByCloud());
            bundle.putBoolean("5g_switch_off", FiveGCloudController.is5GSwitchClosedByCloud());
            bundle.putBoolean("vice_sa_on", FiveGCloudController.isViceSAModeEnabledByCloud());
            bundle.putBoolean("vice_sa_off", FiveGCloudController.isViceSAModeDisabledByCloud());
            bundle.putInt("5g_speed_interval", FiveGCloudController.getSpeedInterval());
            bundle.putInt("qos_weak_signal_bin", FiveGCloudController.getSignalQosWeakSignalBin());
            bundle.putInt("signal_qos_vg", FiveGCloudController.getSignalQosVgSignalCn());
            bundle.putInt("signal_qos_sent_packet", FiveGCloudController.getSignalQosSentPacketsCn());
            bundle.putInt("signal_qos_nr_uplink", FiveGCloudController.getSignalQosNrUplink());
            bundle.putInt("signal_qos_nr_downlink", FiveGCloudController.getSignalQosNrDownlink());
            bundle.putInt("signal_qos_lte_uplink", FiveGCloudController.getSignalQosLteUplink());
            bundle.putInt("signal_qos_lte_downlink", FiveGCloudController.getSignalQosLteDownlink());
            bundle.putInt("low_battery_level", FiveGCloudController.getLowBatteryLevel());
            bundle.putInt("low_battery_high_speed", FiveGCloudController.getLowBatteryHighSpeed());
            bundle.putInt("low_battery_low_speed", FiveGCloudController.getLowBatteryLowSpeed());
            bundle.putInt("speed_only_status", FiveGCloudController.getSpeedOnlyStatus());
            bundle.putInt("speed_only_high_speed", FiveGCloudController.getSpeedOnlyHighSpeed());
            bundle.putInt("speed_only_low_speed", FiveGCloudController.getSpeedOnlyLowSpeed());
            bundle.putInt("elevator_status", FiveGCloudController.getInstance().getElevatorStatus());
            bundle.putInt("special_network_status", FiveGCloudController.getInstance().getCloudSpecialNetworkStatus());
        }
        return bundle;
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean isFiveGEnableByCloud(int i) {
        return FiveGCloudController.isFiveGEnableByCloud(i);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean isFiveGNewCallEnableByCloud(int i) {
        return FiveGCloudController.isFiveGNewCallEnableByCloud(i);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean is5GSaModeDisabledByCloud(int i) {
        return FiveGCloudController.is5GSaModeDisabledByCloud(i);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean is5GSaModeEnabledByCloud(int i) {
        return FiveGCloudController.is5GSaModeEnabledByCloud(i);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean isVonrSwitchEnabledByCloud(int i) {
        return FiveGCloudController.isVonrSwitchEnabledByCloud(i);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean isVonrSwitchDisabledByCloud(int i) {
        return FiveGCloudController.isVonrSwitchDisabledByCloud(i);
    }

    @Override // com.android.phone.module.IUtilityModule
    public int getSpeedWhitelistHighSpeedByCloud(String str) {
        return FiveGCloudController.getSpeedWhitelistHighSpeed(str);
    }

    @Override // com.android.phone.module.IUtilityModule
    public int getSpeedWhitelistLowSpeedByCloud(String str) {
        return FiveGCloudController.getSpeedWhitelistLowSpeed(str);
    }

    @Override // com.android.phone.module.IUtilityModule
    public List getDsda2DsdsWhiteListByCloud(Context context) {
        return FiveGCloudController.getDsda2DsdsWhiteList(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public List getSmartSwitchWhiteListByCloud(Context context) {
        return FiveGCloudController.getSmartSwitchWhiteList(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean getAirportModeSwitchByCloud(Context context) {
        return FiveGCloudController.getAirportModeSwitch(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean getFileUploadSwitchByCloud(Context context) {
        return FiveGCloudController.getFileUploadSwitch(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean getArrearSimNwDeniedModeByCloud(Context context) {
        return FiveGCloudController.getArrearSimNwDeniedMode(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean getHstTunnelSmartlinkByCloud(Context context) {
        return FiveGCloudController.getHstTunnelSmartlink(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean getSmartNetworkSwitchByCloud(Context context) {
        return FiveGCloudController.getSmartNetworkSwitch(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean getFivegLBStreamOptByCloud(Context context) {
        return FiveGCloudController.getFivegLBStreamOpt(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean getHstPreferFreqSwitchByCloud(Context context) {
        return FiveGCloudController.getHstPreferFreqSwitch(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean getHyperEngineGameSwitchByCloud(Context context) {
        return FiveGCloudController.getHyperEngineGameSwitch(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean getHyperEngineHstSwitchByCloud(Context context) {
        return FiveGCloudController.getHyperEngineHstSwitch(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean getHyperEngineSmartHoSwitchByCloud(Context context) {
        return FiveGCloudController.getHyperEngineSmartHoSwitch(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public String getSignalMapSnsCellBlackListByCloud(Context context) {
        return FiveGCloudController.getSignalMapSnsCellBlackList(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public List getPingPongWhiteListByCloud(Context context) {
        return FiveGCloudController.getPingPongWhiteList(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public List getCommonSignalWhiteListByCloud(Context context) {
        return FiveGCloudController.getCommonSignalWhiteList(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public List getDualDataConcurrentModeWhiteListByCloud(Context context) {
        return FiveGCloudController.getDualDataConcurrentModeWhiteList(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public List getDualDataConcurrentLimitedWhiteListByCloud(Context context) {
        return FiveGCloudController.getDualDataConcurrentLimitedWhiteList(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public List getDualDataRedundantModeWhiteListByCloud(Context context) {
        return FiveGCloudController.getDualDataRedundantModeWhiteList(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public List getHikingModeCityWhiteListByCloud(Context context) {
        return FiveGCloudController.getHikingModeCityWhiteList(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public List getResolutionVideoAppsWhiteListByCloud(Context context) {
        return FiveGCloudController.getResolutionVideoAppsWhiteList(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public List getSubwayModeSignalThresholdByCloud(Context context) {
        return FiveGCloudController.getSubwayModeSignalThreshold(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public List getPreloadVideoAppsWhiteListByCloud(Context context) {
        return FiveGCloudController.getPreloadVideoAppsWhiteList(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public List getHyperEngineGameWhiteListByCloud(Context context) {
        return FiveGCloudController.getHyperEngineGameWhiteList(context);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean isModemFeatureEnabledByCloud(int i, int i2) {
        return FiveGCloudController.isModemFeatureEnabledByCloud(i, i2);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean isModemFeatureDisabledByCloud(int i, int i2) {
        return FiveGCloudController.isModemFeatureDisabledByCloud(i, i2);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean isPreferredNetworkTypeInVisibleByCloud(int i) {
        return FiveGCloudController.isPreferredNetworkTypeInVisibleByCloud(i);
    }

    @Override // com.android.phone.module.IUtilityModule
    public boolean isPreferredNetworkTypeVisibleByCloud(int i) {
        return FiveGCloudController.isPreferredNetworkTypeVisibleByCloud(i);
    }

    @Override // com.android.phone.module.IUtilityModule
    public Bundle getModuleInfos() {
        Bundle bundle = new Bundle();
        if (this.mSensorManager.get() != null) {
            bundle.putBoolean("in_elevator", ((TelephonySensorManager) this.mSensorManager.get()).isInElevator());
        }
        return bundle;
    }

    @Override // com.android.phone.module.IUtilityModule
    public void recordObjectEvent(String str, String str2, Map map) {
        MiStatInterfaceUtil.recordObjectEvent(str, str2, map);
    }

    @Override // com.android.phone.module.IUtilityModule
    public void recordCountEvent(String str, String str2, Map map) {
        MiStatInterfaceUtil.recordCountEvent(str, str2, map);
    }
}
