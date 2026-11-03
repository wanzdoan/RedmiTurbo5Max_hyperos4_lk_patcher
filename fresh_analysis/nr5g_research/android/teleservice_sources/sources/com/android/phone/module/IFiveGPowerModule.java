package com.android.phone.module;

/* JADX INFO: loaded from: classes.dex */
public interface IFiveGPowerModule {
    default void bindServiceAndEnableEndc(boolean z, int i) {
    }

    default void enableSaAsync(boolean z, int i) {
    }

    default void enableSaForNetRecovery(boolean z, int i) {
    }

    default void enableSaForStreamMedia(boolean z) {
    }

    default void enableSaForWechat(boolean z) {
    }

    default int getBatteryLevel() {
        return 0;
    }

    default int getCurrentOosType(int i) {
        return 0;
    }

    default boolean getFiveGHostedModeStatus() {
        return false;
    }

    default long getMimoSceneChangeRxCount() {
        return 0L;
    }

    default long getModemSSRCausedByBwpCount() {
        return 0L;
    }

    default long getModemSSRCausedByN1Count() {
        return 0L;
    }

    default long getModemSSRCausedByN28Count() {
        return 0L;
    }

    default long getNsaScgPoorFreezeRecoverCounts() {
        return 0L;
    }

    default long getNsaScgPoorOptimizationCounts() {
        return 0L;
    }

    default boolean getSaThermalFlag() {
        return false;
    }

    default int getSwitchSceneCount(int i) {
        return 0;
    }

    default int getUlMimoStatus() {
        return 0;
    }

    default void limitNwCA(boolean z, int i) {
    }

    default void processLimitLteCaOptimizeByCloud() {
    }

    default void reduceIdleNeighborMea(boolean z, int i) {
    }

    default void reportStatistics() {
    }

    default void resetARDOptimizationToDefault() {
    }

    default void resetFakeStationList() {
    }

    default void resetGameResidentNetworkOptimization(boolean z) {
    }

    default void resetSleepProtectModeByCloud() {
    }

    default void resetThermalThrottle() {
    }

    default void sendMessage(int i, Object obj) {
    }

    default void setNetworkCapability(String str) {
    }

    default void setUlMimoStatus(boolean z) {
    }

    default void switchRxMode(boolean z) {
    }

    default void triggerGameResidentNetworkOptimization(boolean z) {
    }

    default void triggerSmartNetworkOptimization(boolean z) {
    }

    default void updateThermalThrottleSASnr(int i) {
    }

    default String getSaOptimizeScene() {
        return "";
    }

    default String getModemCrashByNodeFile() {
        return "";
    }

    default boolean[] getEndcPowerEnable() {
        return new boolean[0];
    }

    default boolean[] getSaPowerEnable() {
        return new boolean[0];
    }

    default String getSilentDisableSaCountForStatis() {
        return "";
    }

    default String getNightModeDuration() {
        return "";
    }

    default String getReportedMessageIDs() {
        return "";
    }

    default String getNightReportedMessageIDs() {
        return "";
    }

    default String getScreenOffReportedMessageIDs() {
        return "";
    }

    default String getNightModeNoSleepDuration() {
        return "";
    }

    default String getScreenOffDuration() {
        return "";
    }

    default String getAllModeModemSleepSeconds() {
        return "";
    }

    default String getAvgMinMaxModemSleepRatio() {
        return "";
    }

    default String getThermalThrottleInfo() {
        return "";
    }

    default String getUlMimoInfo() {
        return "";
    }

    default String getModemPowerSaveInfo() {
        return "";
    }

    default String getRILMessageIDs(int i) {
        return "";
    }
}
