package com.android.phone.module;

import android.hardware.SensorEvent;
import android.os.Bundle;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public interface INetworkModule {
    default void clearMaxNetworkByteList() {
    }

    default void clearPeakRateBps() {
    }

    default void clearUnsolPeakRateBps() {
    }

    default void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    default void dumpQmsMsgTunnel(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    default void enableNetworkRecoveryNsa(boolean z, int i) {
    }

    default void enableNetworkRecoverySa(boolean z, int i) {
    }

    default void execPowerSaveOptimization(boolean z) {
    }

    default int getCurApptype() {
        return 0;
    }

    default long getCurrentDlRateBps() {
        return 0L;
    }

    default long getCurrentUlRateBps() {
        return 0L;
    }

    default boolean getMIsDdsChange() {
        return false;
    }

    default boolean getModemDataCollectorCallEndInfo() {
        return false;
    }

    default int getNRRsrp(int i) {
        return 0;
    }

    default boolean getNrCaStatus(int i) {
        return false;
    }

    default long getPeakDlRateBps() {
        return 0L;
    }

    default long getPeakUlRateBps() {
        return 0L;
    }

    default long getUnsolPeakDlRateBps() {
        return 0L;
    }

    default long getUnsolPeakUlRateBps() {
        return 0L;
    }

    default void handleModemResetEvent() {
    }

    default boolean isCloudGlobalStreamMediaOn() {
        return false;
    }

    default boolean isCurrentCellResident() {
        return false;
    }

    default boolean isHighSpeedTrain() {
        return false;
    }

    default boolean isHikingIn(int i) {
        return false;
    }

    default boolean isHikingOut(int i) {
        return false;
    }

    default boolean isHikingStateUpdate(int i) {
        return false;
    }

    default boolean isInCompanyScence() {
        return false;
    }

    default boolean isInHomeScence() {
        return false;
    }

    default boolean isInPoi() {
        return false;
    }

    default boolean isInSubwayState() {
        return false;
    }

    default boolean isModemDataCollecting() {
        return false;
    }

    default boolean isNoPhoneBalance(int i) {
        return false;
    }

    default boolean isNsaOffInRecoveryAction(int i) {
        return false;
    }

    default boolean isProcessedWechatVideoEnter() {
        return false;
    }

    default boolean isResidentCellScene() {
        return false;
    }

    default boolean isSaOffInRecoveryAction(int i) {
        return false;
    }

    default boolean isSpecialSimSaDisabled(int i) {
        return false;
    }

    default boolean isSubwayStartStopStateUpdate(int i) {
        return false;
    }

    default void localLog(String str, String str2, String str3) {
    }

    default void onOutElevatorOrBasementEvent(SensorEvent sensorEvent) {
    }

    default boolean performActionForVonrOpt(String str, int i) {
        return false;
    }

    default void processDataStatisticChange(TelephonyStatAdapter.DataStatType dataStatType) {
    }

    default void processForegroundInHstTunnel(boolean z) {
    }

    default void processSignalStrengthInHstTunnel(boolean z) {
    }

    default void reSetBaredCells() {
    }

    default void recordApnDetectNotification(int i) {
    }

    default void recordHstTunnel(String str, String str2, long j) {
    }

    default void releaseNetworkBySlot(int i) {
    }

    default void reloadStatisticDataFromSP() {
    }

    default void reloadSubwayModeStatsFromSP() {
    }

    default void reportCellHdrStatistic() {
    }

    default void reportCommonDataOptStatistics() {
    }

    default void reportGameResidentCellStatistic() {
    }

    default void reportNetworkOptimizerGlobalSafDetailStatistic() {
    }

    default void reportNetworkOptimizerGlobalSafStatistic() {
    }

    default void reportNetworkOptimizerGlobalVideoBenefitStatistic() {
    }

    default void reportSafStatistic() {
    }

    default void reportStallStatistic() {
    }

    default void reportStatistics() {
    }

    default void reportStreamMediaStatistic() {
    }

    default void reportSubwayFdd2TddStatistic() {
    }

    default void reportSubwayModeStatistics() {
    }

    default void reportVideoAndGameAiBenefitStatistic() {
    }

    default void reportVideoStatistic() {
    }

    default void requestNetworkBySlot(int i) {
    }

    default void sendQmsRequestRawAsync(int i, int i2, Object obj, String str) {
    }

    default void setMIsDdsChange(boolean z) {
    }

    default void setMobileNetworkCapability(String str) {
    }

    default boolean setNrCaStatus(int i, boolean z, String str) {
        return false;
    }

    default void showCdmaUnavailableAlertDialog() {
    }

    default void showSmartSwitchDualSimDialog() {
    }

    default void startModemDataCollectorTest() {
    }

    default void stopModemDataCollectorTest() {
    }

    default void updateCallEndReason(String str, String str2, HashMap map) {
    }

    default void updateFreqOptCloudConfig(Map map) {
    }

    default void updateResidentCellDbForVonrOpt(String str, int i, boolean z, String str2, long j) {
    }

    default void videoAppsPolicyNotify(int i, int i2, int i3, int i4) {
    }

    default String getMobileNetworkCapability(int i) {
        return "";
    }

    default String getCurrentPoiId() {
        return "";
    }

    default String getCurrentPoiName() {
        return "";
    }

    default String getLastPoiId() {
        return "";
    }

    default String getLastPoiName() {
        return "";
    }

    default String getCurrentHitLine() {
        return "";
    }

    default ArrayList getMaxNetworkByteList() {
        return new ArrayList();
    }

    default String getTopOneRegion() {
        return "";
    }

    default Bundle getModuleInfos() {
        return new Bundle();
    }

    default String getFakeDataTrigger() {
        return "";
    }

    default String getGameCellResidenceAndLagTime() {
        return "";
    }

    default Bundle getModemQosParams(int i) {
        return new Bundle();
    }
}
