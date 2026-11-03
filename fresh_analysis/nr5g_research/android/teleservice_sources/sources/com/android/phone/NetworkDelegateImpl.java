package com.android.phone;

import android.content.Intent;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.util.Log;
import com.android.phone.dsda.MiuiDualDataController;
import com.android.phone.freqopt.core.FreqOptController;
import com.android.phone.module.INetworkModule;
import com.android.phone.network.MiuiNetworkMonitor;
import com.android.phone.network.ModemNetworkPerception;
import com.android.phone.network.ModemQoeMetrics;
import com.android.phone.performance.HingeNetworkManager;
import com.android.phone.performance.SelfStudyControllerBase;
import com.android.phone.signalMap.SignalMapController;
import com.android.phone.signalMap.SignalMapHSTController;
import com.android.phone.signalMap.SignalMapNOMLController;
import com.android.phone.smarttravel.DriveModeController;
import com.android.phone.streaming.StreamingOptimizationController;
import com.android.phone.utils.NetworkSpeedTracker;
import com.android.services.telephony.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class NetworkDelegateImpl implements INetworkModule {
    private static volatile NetworkDelegateImpl sInstance;
    private Lazy mNetworkMetrics = new Lazy();
    private Lazy mHstMode = new Lazy();
    private Lazy mDualData = new Lazy();
    private Lazy mHingeNetwork = new Lazy();
    private Lazy mModemNetworkPerception = new Lazy();
    private Lazy mAirportMode = new Lazy();
    private Lazy mHikingMode = new Lazy();
    private Lazy mSubwayMode = new Lazy();
    private Lazy mCellPrefer = new Lazy();
    private Lazy mMobileNetworkProber = new Lazy();
    private Lazy mSelfStudy = new Lazy();
    private Lazy mNetworkRecovery = new Lazy();
    private Lazy mModemDataCollector = new Lazy();
    private Lazy mNetworkOptimizer = new Lazy();
    private Lazy mQmsMsgTunnel = new Lazy();
    private Lazy mFileUploadMonitor = new Lazy();
    private Lazy mNetworkFastRecovery = new Lazy();
    private Lazy mCommonDataRecover = new Lazy();
    private Lazy mSpecialSim = new Lazy();
    private Lazy mNetworkSpeed = new Lazy();
    private Lazy mNetworkModeRecognition = new Lazy();
    private Lazy mSignalMap = new Lazy();
    private Lazy mSignalMapHST = new Lazy();
    private Lazy mSignalMapNOML = new Lazy();
    private Lazy mDriveMode = new Lazy();
    private Lazy mModemQoeMetrics = new Lazy();
    private Lazy mMiuiNetworkMonitor = new Lazy();
    private Lazy mHighSpeedTrainSmartSwitcher = new Lazy();

    public static NetworkDelegateImpl init() {
        if (sInstance == null) {
            synchronized (NetworkDelegateImpl.class) {
                try {
                    if (sInstance == null) {
                        NetworkDelegateImpl networkDelegateImpl = new NetworkDelegateImpl();
                        ModuleDelegate.register(INetworkModule.class, networkDelegateImpl);
                        sInstance = networkDelegateImpl;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    private NetworkDelegateImpl() {
    }

    public void setNetworkMetrics(NetworkMetricsController networkMetricsController) {
        this.mNetworkMetrics.set(networkMetricsController);
    }

    public void setHstMode(HighSpeedTrainModeController highSpeedTrainModeController) {
        this.mHstMode.set(highSpeedTrainModeController);
    }

    public void setDualData(MiuiDualDataController miuiDualDataController) {
        this.mDualData.set(miuiDualDataController);
    }

    public void setHingeNetwork(HingeNetworkManager hingeNetworkManager) {
        this.mHingeNetwork.set(hingeNetworkManager);
    }

    public void setModemNetworkPerception(ModemNetworkPerception modemNetworkPerception) {
        this.mModemNetworkPerception.set(modemNetworkPerception);
    }

    public void setAirportMode(AirportModeController airportModeController) {
        this.mAirportMode.set(airportModeController);
    }

    public void setHikingMode(HikingModeController hikingModeController) {
        this.mHikingMode.set(hikingModeController);
    }

    public void setSubwayMode(SubwayModeController subwayModeController) {
        this.mSubwayMode.set(subwayModeController);
    }

    public void setCellPrefer(CellPreferManager cellPreferManager) {
        this.mCellPrefer.set(cellPreferManager);
    }

    public void setMobileNetworkProber(MobileNetworkProber mobileNetworkProber) {
        this.mMobileNetworkProber.set(mobileNetworkProber);
    }

    public void setSelfStudy(SelfStudyControllerBase selfStudyControllerBase) {
        this.mSelfStudy.set(selfStudyControllerBase);
    }

    public void setNetworkRecovery(NetworkRecoveryStrategyBase networkRecoveryStrategyBase) {
        this.mNetworkRecovery.set(networkRecoveryStrategyBase);
    }

    public void setModemDataCollector(ModemDataCollectorBase modemDataCollectorBase) {
        this.mModemDataCollector.set(modemDataCollectorBase);
    }

    public void setNetworkOptimizer(NetworkOptimizerBase networkOptimizerBase) {
        this.mNetworkOptimizer.set(networkOptimizerBase);
    }

    public void setQmsMsgTunnel(QmsMsgTunnelControllerBase qmsMsgTunnelControllerBase) {
        this.mQmsMsgTunnel.set(qmsMsgTunnelControllerBase);
    }

    public void setFileUploadMonitor(FileUploadMonitor fileUploadMonitor) {
        this.mFileUploadMonitor.set(fileUploadMonitor);
    }

    public void setNetworkFastRecovery(NetworkFastRecovery networkFastRecovery) {
        this.mNetworkFastRecovery.set(networkFastRecovery);
    }

    public void setCommonDataRecover(CommonDataRecoverFlow commonDataRecoverFlow) {
        this.mCommonDataRecover.set(commonDataRecoverFlow);
    }

    public void setSpecialSim(SpecialSimManager specialSimManager) {
        this.mSpecialSim.set(specialSimManager);
    }

    public void setNetworkSpeed(NetworkSpeedTracker networkSpeedTracker) {
        this.mNetworkSpeed.set(networkSpeedTracker);
    }

    public void setNetworkModeRecognition(NetworkModeRecognition networkModeRecognition) {
        this.mNetworkModeRecognition.set(networkModeRecognition);
    }

    public void setSignalMap(SignalMapController signalMapController) {
        this.mSignalMap.set(signalMapController);
    }

    public void setSignalMapHST(SignalMapHSTController signalMapHSTController) {
        this.mSignalMapHST.set(signalMapHSTController);
    }

    public void setSignalMapNOML(SignalMapNOMLController signalMapNOMLController) {
        this.mSignalMapNOML.set(signalMapNOMLController);
    }

    public void setDriveMode(DriveModeController driveModeController) {
        this.mDriveMode.set(driveModeController);
    }

    public void setModemQoeMetrics(ModemQoeMetrics modemQoeMetrics) {
        this.mModemQoeMetrics.set(modemQoeMetrics);
    }

    public void setMiuiNetworkMonitor(MiuiNetworkMonitor miuiNetworkMonitor) {
        this.mMiuiNetworkMonitor.set(miuiNetworkMonitor);
    }

    public void setHighSpeedTrainSmartSwitcher(HighSpeedTrainSmartSwitcher highSpeedTrainSmartSwitcher) {
        this.mHighSpeedTrainSmartSwitcher.set(highSpeedTrainSmartSwitcher);
    }

    @Override // com.android.phone.module.INetworkModule
    public void showCdmaUnavailableAlertDialog() {
        CdmaUnavailableAlertDialog.showDialogIfNeeded();
    }

    @Override // com.android.phone.module.INetworkModule
    public void recordApnDetectNotification(int i) {
        SpecialSimManager specialSimManager = (SpecialSimManager) this.mSpecialSim.get();
        if (specialSimManager == null) {
            return;
        }
        specialSimManager.recordApnDetectNotification(i);
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean isNoPhoneBalance(int i) {
        SpecialSimManager specialSimManager = (SpecialSimManager) this.mSpecialSim.get();
        if (specialSimManager == null) {
            return false;
        }
        return specialSimManager.isNoPhoneBalance(i);
    }

    @Override // com.android.phone.module.INetworkModule
    public long getCurrentDlRateBps() {
        NetworkSpeedTracker networkSpeedTracker = (NetworkSpeedTracker) this.mNetworkSpeed.get();
        if (networkSpeedTracker == null) {
            return 0L;
        }
        return networkSpeedTracker.getCurrentDlRateBps();
    }

    @Override // com.android.phone.module.INetworkModule
    public long getCurrentUlRateBps() {
        NetworkSpeedTracker networkSpeedTracker = (NetworkSpeedTracker) this.mNetworkSpeed.get();
        if (networkSpeedTracker == null) {
            return 0L;
        }
        return networkSpeedTracker.getCurrentUlRateBps();
    }

    @Override // com.android.phone.module.INetworkModule
    public void onOutElevatorOrBasementEvent(SensorEvent sensorEvent) {
        if (ElevatorModeController.getInstance() == null) {
            return;
        }
        ElevatorModeController.getInstance().onOutElevatorOrBasementEvent(sensorEvent);
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean isHighSpeedTrain() {
        if (this.mNetworkMetrics.get() == null) {
            return false;
        }
        return ((NetworkMetricsController) this.mNetworkMetrics.get()).isHighSpeedTrain();
    }

    @Override // com.android.phone.module.INetworkModule
    public void localLog(String str, String str2, String str3) {
        if (this.mNetworkMetrics.get() == null) {
            return;
        }
        NetworkMetricsController.localLog(str, str2, str3);
    }

    @Override // com.android.phone.module.INetworkModule
    public String getMobileNetworkCapability(int i) {
        return FiveGGameController.getMobileNetworkCapability(i);
    }

    @Override // com.android.phone.module.INetworkModule
    public void setMobileNetworkCapability(String str) {
        FiveGGameController.setMobileNetworkCapability(str);
    }

    @Override // com.android.phone.module.INetworkModule
    public void showSmartSwitchDualSimDialog() {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        if (phoneGlobals == null) {
            return;
        }
        Intent intent = new Intent(phoneGlobals, (Class<?>) SmartSwitchDualSimDialog.class);
        intent.setFlags(268435456);
        phoneGlobals.startActivity(intent);
    }

    @Override // com.android.phone.module.INetworkModule
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        HighSpeedTrainSmartSwitcher highSpeedTrainSmartSwitcher;
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported() && this.mNetworkMetrics.get() != null) {
            ((NetworkMetricsController) this.mNetworkMetrics.get()).dump(fileDescriptor, printWriter, strArr);
        }
        if (this.mHstMode.get() != null) {
            ((HighSpeedTrainModeController) this.mHstMode.get()).dump(fileDescriptor, printWriter, strArr);
        }
        if (MiuiPhoneUtils.isDualDataSupported() && this.mDualData.get() != null) {
            ((MiuiDualDataController) this.mDualData.get()).dump(fileDescriptor, printWriter, strArr);
        }
        if (MiuiPhoneUtils.isQuickSwitchMobileNetworkFeatureEnabled() && this.mHingeNetwork.get() != null) {
            ((HingeNetworkManager) this.mHingeNetwork.get()).dump(fileDescriptor, printWriter, strArr);
        }
        ModemQoeMetrics modemQoeMetrics = (ModemQoeMetrics) this.mModemQoeMetrics.get();
        if (modemQoeMetrics != null) {
            modemQoeMetrics.dump(fileDescriptor, printWriter, strArr);
        }
        if (MiuiPhoneUtils.isHighSpeedTrainSmartSwitcherSupported() && (highSpeedTrainSmartSwitcher = (HighSpeedTrainSmartSwitcher) this.mHighSpeedTrainSmartSwitcher.get()) != null) {
            highSpeedTrainSmartSwitcher.dump(fileDescriptor, printWriter, strArr);
        }
        SignalMapHSTController signalMapHSTController = (SignalMapHSTController) this.mSignalMapHST.get();
        if (signalMapHSTController != null) {
            signalMapHSTController.dump(fileDescriptor, printWriter, strArr);
        }
        if (this.mMobileNetworkProber.get() != null) {
            ((MobileNetworkProber) this.mMobileNetworkProber.get()).dump(fileDescriptor, printWriter, strArr);
        }
        SelfStudyControllerBase selfStudyControllerBase = (SelfStudyControllerBase) this.mSelfStudy.get();
        if (selfStudyControllerBase != null) {
            selfStudyControllerBase.dump(fileDescriptor, printWriter, strArr);
        }
        if (MiuiPhoneUtils.isFileUploadMonitorSupported() && this.mFileUploadMonitor.get() != null) {
            ((FileUploadMonitor) this.mFileUploadMonitor.get()).dump(fileDescriptor, printWriter, strArr);
        }
        StreamingOptimizationController streamingOptimizationController = StreamingOptimizationController.getInstance();
        if (streamingOptimizationController != null) {
            streamingOptimizationController.dump(fileDescriptor, printWriter, strArr);
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public String getCurrentPoiId() {
        CoreTravelSceneService.getInstance();
        return "";
    }

    @Override // com.android.phone.module.INetworkModule
    public String getCurrentPoiName() {
        CoreTravelSceneService.getInstance();
        return "";
    }

    @Override // com.android.phone.module.INetworkModule
    public String getLastPoiId() {
        CoreTravelSceneService.getInstance();
        return "";
    }

    @Override // com.android.phone.module.INetworkModule
    public String getLastPoiName() {
        CoreTravelSceneService.getInstance();
        return "";
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean isInPoi() {
        CoreTravelSceneService.getInstance();
        return false;
    }

    @Override // com.android.phone.module.INetworkModule
    public void processForegroundInHstTunnel(boolean z) {
        if (this.mHstMode.get() != null) {
            ((HighSpeedTrainModeController) this.mHstMode.get()).processForegroundInHstTunnel(z);
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void processSignalStrengthInHstTunnel(boolean z) {
        if (this.mModemNetworkPerception.get() != null) {
            ((ModemNetworkPerception) this.mModemNetworkPerception.get()).processSignalStrengthInHstTunnel(z);
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void recordHstTunnel(String str, String str2, long j) {
        if (this.mModemNetworkPerception.get() != null) {
            ((ModemNetworkPerception) this.mModemNetworkPerception.get()).recordHstTunnel(str, str2, j);
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public String getCurrentHitLine() {
        HighSpeedTrainModeController highSpeedTrainModeController = (HighSpeedTrainModeController) this.mHstMode.get();
        if (highSpeedTrainModeController != null) {
            return highSpeedTrainModeController.getCurrentHitLine();
        }
        return "";
    }

    @Override // com.android.phone.module.INetworkModule
    public long getPeakUlRateBps() {
        NetworkSpeedTracker networkSpeedTracker = (NetworkSpeedTracker) this.mNetworkSpeed.get();
        if (networkSpeedTracker != null) {
            return networkSpeedTracker.getPeakUlRateBps();
        }
        return 0L;
    }

    @Override // com.android.phone.module.INetworkModule
    public long getPeakDlRateBps() {
        NetworkSpeedTracker networkSpeedTracker = (NetworkSpeedTracker) this.mNetworkSpeed.get();
        if (networkSpeedTracker != null) {
            return networkSpeedTracker.getPeakDlRateBps();
        }
        return 0L;
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean isInSubwayState() {
        NetworkModeRecognition networkModeRecognition = (NetworkModeRecognition) this.mNetworkModeRecognition.get();
        if (networkModeRecognition == null) {
            return false;
        }
        return networkModeRecognition.isInSubwayState();
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean isHikingStateUpdate(int i) {
        NetworkModeRecognition networkModeRecognition = (NetworkModeRecognition) this.mNetworkModeRecognition.get();
        if (networkModeRecognition == null) {
            return false;
        }
        return networkModeRecognition.isHikingStateUpdate(i);
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean isSubwayStartStopStateUpdate(int i) {
        NetworkModeRecognition networkModeRecognition = (NetworkModeRecognition) this.mNetworkModeRecognition.get();
        if (networkModeRecognition == null) {
            return false;
        }
        return networkModeRecognition.isSubwayStartStopStateUpdate(i);
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean isHikingIn(int i) {
        NetworkModeRecognition networkModeRecognition = (NetworkModeRecognition) this.mNetworkModeRecognition.get();
        if (networkModeRecognition == null) {
            return false;
        }
        return networkModeRecognition.isHikingIn(i);
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean isHikingOut(int i) {
        NetworkModeRecognition networkModeRecognition = (NetworkModeRecognition) this.mNetworkModeRecognition.get();
        if (networkModeRecognition == null) {
            return false;
        }
        return networkModeRecognition.isHikingOut(i);
    }

    @Override // com.android.phone.module.INetworkModule
    public ArrayList getMaxNetworkByteList() {
        NetworkSpeedTracker networkSpeedTracker = (NetworkSpeedTracker) this.mNetworkSpeed.get();
        if (networkSpeedTracker != null) {
            return networkSpeedTracker.getMaxNetworkByteList();
        }
        return new ArrayList();
    }

    @Override // com.android.phone.module.INetworkModule
    public void clearMaxNetworkByteList() {
        NetworkSpeedTracker networkSpeedTracker = (NetworkSpeedTracker) this.mNetworkSpeed.get();
        if (networkSpeedTracker != null) {
            networkSpeedTracker.clearMaxNetworkByteList();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void reloadStatisticDataFromSP() {
        DriveModeController driveModeController;
        if (MiuiPhoneUtils.isAirportOptimizationSupported() && this.mAirportMode.get() != null) {
            ((AirportModeController) this.mAirportMode.get()).reloadAirportModeStatsFromSP();
        }
        if (MiuiPhoneUtils.isHikingOptimizationSupported() && this.mHikingMode.get() != null) {
            ((HikingModeController) this.mHikingMode.get()).reloadHikingModeStatsFromSP();
        }
        if (!MiuiPhoneUtils.isDriveOptimizationSupported() || (driveModeController = (DriveModeController) this.mDriveMode.get()) == null) {
            return;
        }
        driveModeController.reloadDriveModeStatsFromSP();
    }

    @Override // com.android.phone.module.INetworkModule
    public void clearPeakRateBps() {
        NetworkSpeedTracker networkSpeedTracker = (NetworkSpeedTracker) this.mNetworkSpeed.get();
        if (networkSpeedTracker != null) {
            networkSpeedTracker.clearPeakRateBps();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public String getTopOneRegion() {
        SignalMapNOMLController signalMapNOMLController = (SignalMapNOMLController) this.mSignalMapNOML.get();
        if (signalMapNOMLController != null) {
            return signalMapNOMLController.getTopOneRegion();
        }
        return "";
    }

    @Override // com.android.phone.module.INetworkModule
    public long getUnsolPeakUlRateBps() {
        NetworkSpeedTracker networkSpeedTracker = (NetworkSpeedTracker) this.mNetworkSpeed.get();
        if (networkSpeedTracker != null) {
            return networkSpeedTracker.getUnsolPeakUlRateBps();
        }
        return 0L;
    }

    @Override // com.android.phone.module.INetworkModule
    public long getUnsolPeakDlRateBps() {
        NetworkSpeedTracker networkSpeedTracker = (NetworkSpeedTracker) this.mNetworkSpeed.get();
        if (networkSpeedTracker != null) {
            return networkSpeedTracker.getUnsolPeakDlRateBps();
        }
        return 0L;
    }

    @Override // com.android.phone.module.INetworkModule
    public void clearUnsolPeakRateBps() {
        NetworkSpeedTracker networkSpeedTracker = (NetworkSpeedTracker) this.mNetworkSpeed.get();
        if (networkSpeedTracker != null) {
            networkSpeedTracker.clearUnsolPeakRateBps();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void reportStatistics() {
        DriveModeController driveModeController;
        SignalMapNOMLController signalMapNOMLController;
        if (MiuiPhoneUtils.isSupportSmartNetworkGameOptimization() && SmartNetworkGameController.getInstance() != null) {
            SmartNetworkGameController.getInstance().reportGameStatistic();
        }
        if (MiuiPhoneUtils.isSignalMapOptimizationSupported()) {
            SignalMapController signalMapController = (SignalMapController) this.mSignalMap.get();
            if (signalMapController != null) {
                signalMapController.reportStatistics();
            }
            SignalMapHSTController signalMapHSTController = (SignalMapHSTController) this.mSignalMapHST.get();
            if (signalMapHSTController != null) {
                signalMapHSTController.reportStatistics();
            }
        }
        if (MiuiPhoneUtils.isSignalMapNOMLSupported() && (signalMapNOMLController = (SignalMapNOMLController) this.mSignalMapNOML.get()) != null) {
            signalMapNOMLController.reportStatistics();
        }
        if (MiuiPhoneUtils.isHikingOptimizationSupported() && this.mHikingMode.get() != null) {
            ((HikingModeController) this.mHikingMode.get()).reportStatistics();
        }
        if (MiuiPhoneUtils.isDriveOptimizationSupported() && (driveModeController = (DriveModeController) this.mDriveMode.get()) != null) {
            driveModeController.reportStatistics();
        }
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            if (ElevatorModeController.getInstance() != null) {
                ElevatorModeController.getInstance().reportStatistics();
            }
            if (this.mNetworkMetrics.get() != null) {
                ((NetworkMetricsController) this.mNetworkMetrics.get()).reportStatistics();
            }
        }
        if (this.mHstMode.get() != null) {
            ((HighSpeedTrainModeController) this.mHstMode.get()).reportStatistics();
        }
        if (MiuiPhoneUtils.isAirportOptimizationSupported() && this.mAirportMode.get() != null) {
            ((AirportModeController) this.mAirportMode.get()).reportStatistics();
        }
        MiuiNetworkMonitor miuiNetworkMonitor = (MiuiNetworkMonitor) this.mMiuiNetworkMonitor.get();
        if (miuiNetworkMonitor != null) {
            miuiNetworkMonitor.reportStatistics();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public int getNRRsrp(int i) {
        return FiveGGameController.getNRRsrp(i);
    }

    @Override // com.android.phone.module.INetworkModule
    public void requestNetworkBySlot(int i) {
        if (this.mDualData.get() != null) {
            ((MiuiDualDataController) this.mDualData.get()).requestNetworkBySlot(i);
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void releaseNetworkBySlot(int i) {
        if (this.mDualData.get() != null) {
            ((MiuiDualDataController) this.mDualData.get()).releaseNetworkBySlot(i);
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean isNsaOffInRecoveryAction(int i) {
        if (NetworkSelfRecovery.getInstance() == null) {
            return false;
        }
        return NetworkSelfRecovery.getInstance().isNsaOffInRecoveryAction(i);
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean isSaOffInRecoveryAction(int i) {
        if (NetworkSelfRecovery.getInstance() == null) {
            return false;
        }
        return NetworkSelfRecovery.getInstance().isSaOffInRecoveryAction(i);
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean isSpecialSimSaDisabled(int i) {
        if (SpecialSimBase.getInstance() == null) {
            return false;
        }
        return SpecialSimBase.getInstance().isSpecialSimSaDisabled(i);
    }

    @Override // com.android.phone.module.INetworkModule
    public void execPowerSaveOptimization(boolean z) {
        if (this.mNetworkMetrics.get() != null) {
            ((NetworkMetricsController) this.mNetworkMetrics.get()).execPowerSaveOptimization(z);
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void reportSubwayFdd2TddStatistic() {
        if (this.mCellPrefer.get() != null) {
            ((CellPreferManager) this.mCellPrefer.get()).reportSubwayFdd2TddStatistic();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void reportCellHdrStatistic() {
        if (this.mCellPrefer.get() != null) {
            ((CellPreferManager) this.mCellPrefer.get()).reportCellHdrStatistic();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void updateCallEndReason(String str, String str2, HashMap map) {
        ModemQoeMetrics modemQoeMetrics = (ModemQoeMetrics) this.mModemQoeMetrics.get();
        if (modemQoeMetrics != null) {
            String strValueOf = String.valueOf(modemQoeMetrics.getDefaultCallEndReasonType());
            String strValueOf2 = String.valueOf(modemQoeMetrics.getDefaultCallEndReason());
            map.put("bp_call_end_reason_type", strValueOf);
            map.put("bp_call_end_reason", strValueOf2);
            ModemQoeMetrics.localLog(ModemQoeMetrics.class.getSimpleName().concat(".CallEndReason"), "apn type = " + str + ", cause(AP) = " + str2 + ", call end reason type(BP) = " + strValueOf + ", call end reason(BP) = " + strValueOf2, 50);
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void processDataStatisticChange(TelephonyStatAdapter.DataStatType dataStatType) {
        MobileNetworkProber mobileNetworkProber = (MobileNetworkProber) this.mMobileNetworkProber.get();
        if (mobileNetworkProber != null) {
            mobileNetworkProber.processDataStatisticChange(dataStatType);
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public String getFakeDataTrigger() {
        MobileNetworkProber mobileNetworkProber = (MobileNetworkProber) this.mMobileNetworkProber.get();
        if (mobileNetworkProber != null) {
            mobileNetworkProber.getFakeDataTrigger();
            return "";
        }
        return "";
    }

    @Override // com.android.phone.module.INetworkModule
    public int getCurApptype() {
        if (this.mNetworkMetrics.get() == null) {
            return 0;
        }
        return ((NetworkMetricsController) this.mNetworkMetrics.get()).getCurApptype().ordinal();
    }

    @Override // com.android.phone.module.INetworkModule
    public Bundle getModuleInfos() {
        Bundle bundle = new Bundle();
        SubwayModeController subwayModeController = (SubwayModeController) this.mSubwayMode.get();
        bundle.putBoolean("is_in_subway", subwayModeController != null && subwayModeController.isInSubway());
        return bundle;
    }

    @Override // com.android.phone.module.INetworkModule
    public void handleModemResetEvent() {
        NetworkFastRecovery networkFastRecovery;
        if (!MiuiPhoneUtils.isPoorNetworkFastRecoverySupported() || (networkFastRecovery = (NetworkFastRecovery) this.mNetworkFastRecovery.get()) == null) {
            return;
        }
        networkFastRecovery.notifyModemReset();
    }

    @Override // com.android.phone.module.INetworkModule
    public void reportCommonDataOptStatistics() {
        CommonDataRecoverFlow.reportCommonDataOptStatistics();
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean isCloudGlobalStreamMediaOn() {
        NetworkOptimizerBase networkOptimizerBase = (NetworkOptimizerBase) this.mNetworkOptimizer.get();
        if (networkOptimizerBase != null) {
            return networkOptimizerBase.isCloudGlobalStreamMediaOn();
        }
        return true;
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean isInHomeScence() {
        SelfStudyControllerBase selfStudyControllerBase = (SelfStudyControllerBase) this.mSelfStudy.get();
        if (selfStudyControllerBase == null) {
            return false;
        }
        return selfStudyControllerBase.isInHomeScence();
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean isInCompanyScence() {
        SelfStudyControllerBase selfStudyControllerBase = (SelfStudyControllerBase) this.mSelfStudy.get();
        if (selfStudyControllerBase == null) {
            return false;
        }
        return selfStudyControllerBase.isInCompanyScence();
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean isCurrentCellResident() {
        SelfStudyControllerBase selfStudyControllerBase = (SelfStudyControllerBase) this.mSelfStudy.get();
        if (selfStudyControllerBase == null) {
            return false;
        }
        return selfStudyControllerBase.isCurrentCellResident();
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean isResidentCellScene() {
        SelfStudyControllerBase selfStudyControllerBase = (SelfStudyControllerBase) this.mSelfStudy.get();
        if (selfStudyControllerBase == null) {
            return false;
        }
        return selfStudyControllerBase.isResidentCellScene();
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean performActionForVonrOpt(String str, int i) {
        SelfStudyControllerBase selfStudyControllerBase = (SelfStudyControllerBase) this.mSelfStudy.get();
        if (selfStudyControllerBase == null) {
            return false;
        }
        return selfStudyControllerBase.performActionForVonrOpt(str, i);
    }

    @Override // com.android.phone.module.INetworkModule
    public void updateResidentCellDbForVonrOpt(String str, int i, boolean z, String str2, long j) {
        SelfStudyControllerBase selfStudyControllerBase = (SelfStudyControllerBase) this.mSelfStudy.get();
        if (selfStudyControllerBase == null) {
            return;
        }
        selfStudyControllerBase.updateResidentCellDbForVonrOpt(str, i, z, str2, j);
    }

    @Override // com.android.phone.module.INetworkModule
    public void reSetBaredCells() {
        SelfStudyControllerBase selfStudyControllerBase = (SelfStudyControllerBase) this.mSelfStudy.get();
        if (selfStudyControllerBase == null) {
            return;
        }
        selfStudyControllerBase.reSetBaredCells();
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean isProcessedWechatVideoEnter() {
        SelfStudyControllerBase selfStudyControllerBase = (SelfStudyControllerBase) this.mSelfStudy.get();
        if (selfStudyControllerBase == null) {
            return false;
        }
        return selfStudyControllerBase.isProcessedWechatVideoEnter();
    }

    @Override // com.android.phone.module.INetworkModule
    public String getGameCellResidenceAndLagTime() {
        SelfStudyControllerBase selfStudyControllerBase = (SelfStudyControllerBase) this.mSelfStudy.get();
        if (selfStudyControllerBase == null) {
            return null;
        }
        return selfStudyControllerBase.getGameCellResidenceAndLagTime();
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean getMIsDdsChange() {
        SelfStudyControllerBase selfStudyControllerBase = (SelfStudyControllerBase) this.mSelfStudy.get();
        if (selfStudyControllerBase == null) {
            return false;
        }
        return selfStudyControllerBase.mIsDdsChange;
    }

    @Override // com.android.phone.module.INetworkModule
    public void setMIsDdsChange(boolean z) {
        SelfStudyControllerBase selfStudyControllerBase = (SelfStudyControllerBase) this.mSelfStudy.get();
        if (selfStudyControllerBase == null) {
            return;
        }
        selfStudyControllerBase.mIsDdsChange = z;
    }

    @Override // com.android.phone.module.INetworkModule
    public void reportVideoAndGameAiBenefitStatistic() {
        SelfStudyControllerBase selfStudyControllerBase = (SelfStudyControllerBase) this.mSelfStudy.get();
        if (selfStudyControllerBase != null) {
            selfStudyControllerBase.reportVideoAndGameAiBenefitStatistic();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void reportVideoStatistic() {
        SelfStudyControllerBase selfStudyControllerBase = (SelfStudyControllerBase) this.mSelfStudy.get();
        if (selfStudyControllerBase != null) {
            selfStudyControllerBase.reportVideoStatistic();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void reportGameResidentCellStatistic() {
        SelfStudyControllerBase selfStudyControllerBase = (SelfStudyControllerBase) this.mSelfStudy.get();
        if (selfStudyControllerBase != null) {
            selfStudyControllerBase.reportGameResidentCellStatistic();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void reportStreamMediaStatistic() {
        SelfStudyControllerBase selfStudyControllerBase = (SelfStudyControllerBase) this.mSelfStudy.get();
        if (selfStudyControllerBase != null) {
            selfStudyControllerBase.reportStreamMediaStatistic();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void reportSafStatistic() {
        SelfStudyControllerBase selfStudyControllerBase = (SelfStudyControllerBase) this.mSelfStudy.get();
        if (selfStudyControllerBase != null) {
            selfStudyControllerBase.reportSafStatistic();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void reportStallStatistic() {
        SelfStudyControllerBase selfStudyControllerBase = (SelfStudyControllerBase) this.mSelfStudy.get();
        if (selfStudyControllerBase != null) {
            selfStudyControllerBase.reportStallStatistic();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean getModemDataCollectorCallEndInfo() {
        ModemDataCollectorBase modemDataCollectorBase = (ModemDataCollectorBase) this.mModemDataCollector.get();
        if (modemDataCollectorBase != null) {
            return modemDataCollectorBase.getCallEndInfo();
        }
        return false;
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean isModemDataCollecting() {
        ModemDataCollectorBase modemDataCollectorBase = (ModemDataCollectorBase) this.mModemDataCollector.get();
        if (modemDataCollectorBase != null) {
            return modemDataCollectorBase.mIsCollecting;
        }
        return false;
    }

    @Override // com.android.phone.module.INetworkModule
    public void startModemDataCollectorTest() {
        ModemDataCollectorBase modemDataCollectorBase = (ModemDataCollectorBase) this.mModemDataCollector.get();
        if (modemDataCollectorBase != null) {
            modemDataCollectorBase.startTest();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void stopModemDataCollectorTest() {
        ModemDataCollectorBase modemDataCollectorBase = (ModemDataCollectorBase) this.mModemDataCollector.get();
        if (modemDataCollectorBase != null) {
            modemDataCollectorBase.stopTest();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void reportNetworkOptimizerGlobalSafStatistic() {
        NetworkOptimizerBase networkOptimizerBase = (NetworkOptimizerBase) this.mNetworkOptimizer.get();
        if (networkOptimizerBase != null) {
            networkOptimizerBase.reportGlobalSafStatistic();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void reportNetworkOptimizerGlobalVideoBenefitStatistic() {
        NetworkOptimizerBase networkOptimizerBase = (NetworkOptimizerBase) this.mNetworkOptimizer.get();
        if (networkOptimizerBase != null) {
            networkOptimizerBase.reportGlobalVideoBenefitStatistic();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void reportNetworkOptimizerGlobalSafDetailStatistic() {
        NetworkOptimizerBase networkOptimizerBase = (NetworkOptimizerBase) this.mNetworkOptimizer.get();
        if (networkOptimizerBase != null) {
            networkOptimizerBase.reportGlobalSafDetailStatistic();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void sendQmsRequestRawAsync(int i, int i2, Object obj, String str) {
        QmsMsgTunnelControllerBase.sendQmsRequestSafe(i, i2, obj, str);
    }

    @Override // com.android.phone.module.INetworkModule
    public void dumpQmsMsgTunnel(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        QmsMsgTunnelControllerBase qmsMsgTunnelControllerBase = (QmsMsgTunnelControllerBase) this.mQmsMsgTunnel.get();
        if (qmsMsgTunnelControllerBase != null) {
            qmsMsgTunnelControllerBase.dump(fileDescriptor, printWriter, strArr);
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void reloadSubwayModeStatsFromSP() {
        SubwayModeController subwayModeController = (SubwayModeController) this.mSubwayMode.get();
        if (subwayModeController != null) {
            subwayModeController.reloadSubwayModeStatsFromSP();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void reportSubwayModeStatistics() {
        SubwayModeController subwayModeController = (SubwayModeController) this.mSubwayMode.get();
        if (subwayModeController != null) {
            subwayModeController.reportStatistics();
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void enableNetworkRecoverySa(boolean z, int i) {
        NetworkRecoveryStrategyBase networkRecoveryStrategyBase = (NetworkRecoveryStrategyBase) this.mNetworkRecovery.get();
        if (networkRecoveryStrategyBase != null) {
            networkRecoveryStrategyBase.enableSa(z, i);
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public void enableNetworkRecoveryNsa(boolean z, int i) {
        NetworkRecoveryStrategyBase networkRecoveryStrategyBase = (NetworkRecoveryStrategyBase) this.mNetworkRecovery.get();
        if (networkRecoveryStrategyBase != null) {
            networkRecoveryStrategyBase.enableNsa(z, i);
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean setNrCaStatus(int i, boolean z, String str) {
        NetworkRecoveryStrategyBase networkRecoveryStrategyBase = (NetworkRecoveryStrategyBase) this.mNetworkRecovery.get();
        if (networkRecoveryStrategyBase != null) {
            return networkRecoveryStrategyBase.setNrCaStatus(i, z, str);
        }
        Log.d("NetworkDelegate", "mNetworkRecovery is null");
        return false;
    }

    @Override // com.android.phone.module.INetworkModule
    public boolean getNrCaStatus(int i) {
        NetworkRecoveryStrategyBase networkRecoveryStrategyBase = (NetworkRecoveryStrategyBase) this.mNetworkRecovery.get();
        if (networkRecoveryStrategyBase != null) {
            return networkRecoveryStrategyBase.getNrCaStatus(i);
        }
        Log.d("NetworkDelegate", "mNetworkRecovery is null");
        return false;
    }

    @Override // com.android.phone.module.INetworkModule
    public void videoAppsPolicyNotify(int i, int i2, int i3, int i4) {
        NetworkRecoveryStrategyBase networkRecoveryStrategyBase = (NetworkRecoveryStrategyBase) this.mNetworkRecovery.get();
        if (networkRecoveryStrategyBase != null) {
            networkRecoveryStrategyBase.videoAppsPolicyNotify(i, i2, i3, i4);
        }
    }

    @Override // com.android.phone.module.INetworkModule
    public Bundle getModemQosParams(int i) {
        ModemQoeMetrics modemQoeMetrics = (ModemQoeMetrics) this.mModemQoeMetrics.get();
        if (modemQoeMetrics == null) {
            return new Bundle();
        }
        return modemQoeMetrics.getModemQosParams(i);
    }

    @Override // com.android.phone.module.INetworkModule
    public void updateFreqOptCloudConfig(Map map) {
        if (FreqOptController.getInstance() != null) {
            FreqOptController.getInstance().updateFreqOptCloudConfig(map);
        }
    }
}
