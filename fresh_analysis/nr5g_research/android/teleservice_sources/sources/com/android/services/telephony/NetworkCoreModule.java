package com.android.services.telephony;

import android.content.Context;
import android.content.Intent;
import android.os.ServiceManager;
import com.android.phone.AirportModeController;
import com.android.phone.BuildingModeManager;
import com.android.phone.CdmaDataOptimizeManager;
import com.android.phone.CellPingPongManager;
import com.android.phone.CellPreferManager;
import com.android.phone.CommonDataRecoverFlow;
import com.android.phone.ElevatorModeController;
import com.android.phone.FileUploadMonitor;
import com.android.phone.FiveGGameController;
import com.android.phone.GameDelayEnhance;
import com.android.phone.HighSpeedTrainModeController;
import com.android.phone.HighSpeedTrainPreferFreq;
import com.android.phone.HighSpeedTrainSmartSwitcher;
import com.android.phone.HikingModeController;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.MobileNetworkProber;
import com.android.phone.ModemDataCollector;
import com.android.phone.ModemProvider;
import com.android.phone.MtkModemBaseModelInitializer;
import com.android.phone.MtkNetworkOptimizer;
import com.android.phone.MtkPingPongOptimizer;
import com.android.phone.MtkSceneIdentify;
import com.android.phone.MtkSelfStudyController;
import com.android.phone.NetworkDelegateImpl;
import com.android.phone.NetworkFastRecovery;
import com.android.phone.NetworkMetricsController;
import com.android.phone.NetworkModeRecognition;
import com.android.phone.NetworkRecoveryOptimize;
import com.android.phone.NetworkRecoveryStrategyImpl;
import com.android.phone.NrcaModeController;
import com.android.phone.OemDataEnhance;
import com.android.phone.QcSelfStudyController;
import com.android.phone.QmsAidlClient;
import com.android.phone.QmsClient;
import com.android.phone.QmsMsgTunnelController;
import com.android.phone.R;
import com.android.phone.ServiceStateMonitor;
import com.android.phone.SignalMapManager;
import com.android.phone.SmartNetworkRecoveryController;
import com.android.phone.SpecialSimEx;
import com.android.phone.SpecialSimManager;
import com.android.phone.SubwayModeController;
import com.android.phone.XrAppSceneNetworkOptimizer;
import com.android.phone.dsda.MiuiDsdaSmartSwitcher;
import com.android.phone.dsda.MiuiDualDataController;
import com.android.phone.freqopt.core.FreqOptController;
import com.android.phone.freqopt.util.FreqOptUtils;
import com.android.phone.network.MiuiNetworkMonitor;
import com.android.phone.network.ModemNetworkPerception;
import com.android.phone.network.ModemQoeMetrics;
import com.android.phone.network.ModemQoePrediction;
import com.android.phone.network.QcWeakSignalPrediction;
import com.android.phone.network.WeakSignalPrediction;
import com.android.phone.performance.HingeNetworkManager;
import com.android.phone.performance.SelfStudyControllerBase;
import com.android.phone.signalMap.SignalMapController;
import com.android.phone.signalMap.SignalMapHSTController;
import com.android.phone.signalMap.SignalMapNOMLController;
import com.android.phone.signalMap.SignalMapSnsController;
import com.android.phone.smarttravel.DriveModeController;
import com.android.phone.streaming.StreamingOptimizationController;
import com.android.phone.utils.NetworkSpeedTracker;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import java.util.function.Supplier;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class NetworkCoreModule implements ICoreModule {
    private Context mContext;
    private NetworkDelegateImpl mDelegate;

    @Override // com.android.services.telephony.ICoreModule
    public String getModuleId() {
        return getClass().getSimpleName();
    }

    @Override // com.android.services.telephony.ICoreModule
    public void onCreate(Context context) {
        android.util.Log.d("NetworkCoreModule", "onCreate");
        this.mContext = context;
        this.mDelegate = NetworkDelegateImpl.init();
    }

    @Override // com.android.services.telephony.ICoreModule
    public void onSimPresent() {
        android.util.Log.d("NetworkCoreModule", "onSimPresent");
        boolean z = ModemProvider.IS_MTK;
        if (z) {
            this.mDelegate.setNetworkOptimizer(MtkNetworkOptimizer.make());
            MtkPingPongOptimizer.make();
            MtkSceneIdentify.make();
            if (MiuiPhoneUtils.isSupportInitModemBaseModel()) {
                MtkModemBaseModelInitializer.make();
            }
        }
        this.mDelegate.setNetworkRecovery(NetworkRecoveryStrategyImpl.make());
        if (MiuiPhoneUtils.isCdmaDataOptimizeSupported()) {
            CdmaDataOptimizeManager.init();
        }
        if (MiuiPhoneUtils.isQuickSwitchMobileNetworkFeatureEnabled()) {
            this.mDelegate.setHingeNetwork(HingeNetworkManager.getInstance());
        }
        if (MiuiPhoneUtils.isSupportQcCallEndReason() || MiuiPhoneUtils.isSupportQcMDC()) {
            this.mDelegate.setModemDataCollector(ModemDataCollector.make());
        }
        this.mDelegate.setModemQoeMetrics(ModemQoeMetrics.getInstance());
        if (MiuiPhoneUtils.isQcLatency2FeatureEnabled()) {
            Intent intent = new Intent();
            intent.setPackage(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME);
            if (MiuiPhoneUtils.isSupportLowLatencyAidlService()) {
                android.util.Log.d("NetworkCoreModule", "LLM Version Aidl");
                intent.setAction("com.android.phone.service.LowLatencyAidlService");
            } else {
                android.util.Log.d("NetworkCoreModule", "LLM Version Others");
                intent.setAction("com.android.phone.service.DataLowLatencyServiceEnhanced");
            }
            this.mContext.startService(intent);
        } else if (ModemProvider.IS_XRING) {
            Intent intent2 = new Intent();
            intent2.setPackage(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME);
            android.util.Log.d("NetworkCoreModule", "LLM Version Aidl");
            intent2.setAction("com.android.phone.service.LowLatencyAidlService");
            this.mContext.startService(intent2);
        }
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            this.mDelegate.setNetworkMetrics(NetworkMetricsController.getInstance());
            this.mDelegate.setSpecialSim(SpecialSimManager.getInstance());
            this.mDelegate.setNetworkSpeed(NetworkSpeedTracker.getInstance());
            this.mDelegate.setNetworkModeRecognition(NetworkModeRecognition.getInstance());
            SpecialSimEx.make();
            ElevatorModeController.init(this.mContext);
            if (MiuiPhoneUtils.isBuildingModeSupported()) {
                BuildingModeManager.make();
            }
            this.mDelegate.setHstMode(HighSpeedTrainModeController.getInstance());
            if (MiuiPhoneUtils.isAirportOptimizationSupported()) {
                this.mDelegate.setAirportMode(AirportModeController.getInstance());
            }
            if (MiuiPhoneUtils.isSubwayOptimizationSupported()) {
                this.mDelegate.setSubwayMode(SubwayModeController.make());
            }
            if (MiuiPhoneUtils.isHikingOptimizationSupported()) {
                this.mDelegate.setHikingMode(HikingModeController.getInstance());
            }
            if (MiuiPhoneUtils.isDriveOptimizationSupported()) {
                this.mDelegate.setDriveMode(DriveModeController.getInstance());
            }
            if (MiuiPhoneUtils.isSnsOptimizationSupported()) {
                CellPingPongManager.init();
            }
            if (MiuiPhoneUtils.isCellPreferOptSupported()) {
                this.mDelegate.setCellPrefer(CellPreferManager.getInstance());
            }
            SignalMapManager.getInstance();
            if (MiuiPhoneUtils.isSignalMapOptimizationSupported()) {
                this.mDelegate.setSignalMap(SignalMapController.getInstance());
                this.mDelegate.setSignalMapHST(SignalMapHSTController.getInstance());
            }
            if (TelephonyManagerEx.getDefault().isSupportSatelliteByDevice() && MiuiPhoneUtils.isSignalMapNOMLSupported()) {
                this.mDelegate.setSignalMapNOML(SignalMapNOMLController.getInstance());
            }
            if (MiuiPhoneUtils.isModemNetworkPerceptionSupported()) {
                this.mDelegate.setModemNetworkPerception(ModemNetworkPerception.getInstance());
                SmartNetworkRecoveryController.getInstance();
            }
            if (MiuiPhoneUtils.isSupportModemAiSignalSupported()) {
                if (z) {
                    WeakSignalPrediction.make();
                } else if (ModemProvider.IS_QCOM) {
                    QcWeakSignalPrediction.make();
                    ModemQoePrediction.getInstance();
                }
            }
            if (MiuiPhoneUtils.isHighSpeedTrainSmartSwitcherSupported()) {
                this.mDelegate.setHighSpeedTrainSmartSwitcher(HighSpeedTrainSmartSwitcher.getInstance());
            }
            if (MiuiPhoneUtils.isSnsOptimizationSupported()) {
                this.mDelegate.setQmsMsgTunnel(QmsMsgTunnelController.make());
                if (ServiceManager.isDeclared(QmsAidlClient.QMS_SERVICE_INTERFACE)) {
                    QmsAidlClient.make();
                    android.util.Log.d("NetworkCoreModule", "makeQmsClient, using QmsAidlClient");
                } else {
                    QmsClient.make();
                    android.util.Log.d("NetworkCoreModule", "makeQmsClient, using QmsClient");
                }
            }
            if (MiuiPhoneUtils.isHighSpeedTrainPreferFreqSupported()) {
                HighSpeedTrainPreferFreq.getInstance();
            }
        }
        if (MiuiPhoneUtils.isImsHighPrioritySupported()) {
            OemDataEnhance.init(this.mContext);
        }
        FiveGGameController.init();
        SignalMapSnsController.make();
        if (MiuiPhoneUtils.isSelfStudySupported()) {
            android.util.Log.d("NetworkCoreModule", "support_self_study");
            if (ModemProvider.IS_QCOM) {
                QcSelfStudyController.make();
                this.mDelegate.setSelfStudy(SelfStudyControllerBase.getInstance());
            } else if (z) {
                MtkSelfStudyController.make();
                this.mDelegate.setSelfStudy(SelfStudyControllerBase.getInstance());
            }
        }
        if (MiuiPhoneUtils.isDualDataSupported()) {
            this.mDelegate.setDualData(MiuiDualDataController.getInstance());
        }
        if (MiuiPhoneUtils.isDeviceDsdaSupportedByQcom()) {
            MiuiDsdaSmartSwitcher.getInstance();
        }
        if (!Build.IS_INTERNATIONAL_BUILD) {
            ServiceStateMonitor.getInstance();
        }
        if (MiuiPhoneUtils.isSupportNetworkOptimization()) {
            this.mDelegate.setMiuiNetworkMonitor(MiuiNetworkMonitor.getInstance());
            MiuiNetworkMonitor.getInstance().init();
        }
        if (MiuiPhoneUtils.isSupportNetworkRecoveryOptimization(this.mContext)) {
            for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                new NetworkRecoveryOptimize(i);
            }
        }
        this.mDelegate.setMobileNetworkProber(MobileNetworkProber.make(this.mContext));
        this.mDelegate.setCommonDataRecover(CommonDataRecoverFlow.make());
        if (MiuiPhoneUtils.isPoorNetworkFastRecoverySupported()) {
            this.mDelegate.setNetworkFastRecovery(NetworkFastRecovery.init());
        }
        if (ModemProvider.IS_QCOM) {
            GameDelayEnhance.make();
        }
        if (MiuiPhoneUtils.isFileUploadMonitorSupported()) {
            this.mDelegate.setFileUploadMonitor(FileUploadMonitor.init());
        }
        if (MiuiPhoneUtils.isStreamingOptimizationSupported() && (MiuiPhoneUtils.isSupportCongestionOptimization() || MiuiPhoneUtils.isSupportInternationalTravelDataOptimization())) {
            StreamingOptimizationController.init();
            StreamingOptimizationController.setResidentCellProvider(new Supplier() { // from class: com.android.services.telephony.NetworkCoreModule$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final Object get() {
                    return this.f$0.lambda$onSimPresent$0();
                }
            });
        }
        if (ModemProvider.IS_XRING) {
            XrAppSceneNetworkOptimizer.make(this.mContext);
        }
        if (MiuiPhoneUtils.isNrcaModeManagerSupported()) {
            NrcaModeController.getInstance();
        }
        if (FreqOptUtils.isFreqCtlEnabled()) {
            FreqOptController.init();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$onSimPresent$0() {
        return Boolean.valueOf(this.mDelegate.isResidentCellScene());
    }

    @Override // com.android.services.telephony.ICoreModule
    public boolean isEnabled(Context context) {
        return HyperOSCustFeatureResolve.getBoolean("config_support_network_module", context.getResources().getBoolean(R.bool.network_module_enabled));
    }
}
