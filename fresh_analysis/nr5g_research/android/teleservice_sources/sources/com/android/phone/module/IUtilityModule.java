package com.android.phone.module;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface IUtilityModule {
    default void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    default void enableOnetrackStatAccessNetwork(Context context, boolean z) {
    }

    default boolean getAirportModeSwitchByCloud(Context context) {
        return false;
    }

    default boolean getArrearSimNwDeniedModeByCloud(Context context) {
        return false;
    }

    default boolean getFileUploadSwitchByCloud(Context context) {
        return false;
    }

    default boolean getFivegLBStreamOptByCloud(Context context) {
        return false;
    }

    default boolean getHstPreferFreqSwitchByCloud(Context context) {
        return false;
    }

    default boolean getHstTunnelSmartlinkByCloud(Context context) {
        return false;
    }

    default boolean getHyperEngineGameSwitchByCloud(Context context) {
        return false;
    }

    default boolean getHyperEngineHstSwitchByCloud(Context context) {
        return false;
    }

    default boolean getHyperEngineSmartHoSwitchByCloud(Context context) {
        return false;
    }

    default boolean getSmartNetworkSwitchByCloud(Context context) {
        return false;
    }

    default int getSpeedWhitelistHighSpeedByCloud(String str) {
        return 0;
    }

    default int getSpeedWhitelistLowSpeedByCloud(String str) {
        return 0;
    }

    default boolean is5GSaModeDisabledByCloud(int i) {
        return false;
    }

    default boolean is5GSaModeEnabledByCloud(int i) {
        return false;
    }

    default boolean isActivityInfoOptimizeSupported() {
        return false;
    }

    default boolean isFiveGEnableByCloud(int i) {
        return false;
    }

    default boolean isFiveGNewCallEnableByCloud(int i) {
        return false;
    }

    default boolean isModemFeatureDisabledByCloud(int i, int i2) {
        return false;
    }

    default boolean isModemFeatureEnabledByCloud(int i, int i2) {
        return false;
    }

    default boolean isPreferredNetworkTypeInVisibleByCloud(int i) {
        return false;
    }

    default boolean isPreferredNetworkTypeVisibleByCloud(int i) {
        return false;
    }

    default boolean isSupportBadCellOptByCloud() {
        return false;
    }

    default boolean isSupportBasementByCloud() {
        return false;
    }

    default boolean isSupportDynamicAntennaTuningByCloud() {
        return false;
    }

    default boolean isSupportFdsLogUploadByCloud() {
        return false;
    }

    default boolean isSupportImsRegOptByCloud() {
        return false;
    }

    default boolean isSupportPowerSavingOptByCloud() {
        return false;
    }

    default boolean isSupportVoiceLinkByCloud() {
        return false;
    }

    default boolean isVonrSwitchDisabledByCloud(int i) {
        return false;
    }

    default boolean isVonrSwitchEnabledByCloud(int i) {
        return false;
    }

    default void notifyQet7100CloudData() {
    }

    default void oneTrackEvent(String str, Map map) {
    }

    default void recordCountEvent(String str, String str2, Map map) {
    }

    default void recordObjectEvent(String str, String str2, Map map) {
    }

    default void recordSatelliteBusinessEntrance(HashMap map) {
    }

    default void recordSatelliteEnabled(HashMap map) {
    }

    default void recordSatelliteHighPenetration(HashMap map) {
    }

    default void recordSatelliteOta(HashMap map) {
    }

    default void reportTelephonyStatistic() {
    }

    default void resetByCause(Context context, int i) {
    }

    default void syncCloudCarrierConfig(String str, PersistableBundle persistableBundle) {
    }

    default void trackerDSDACallExceptionMiEvent(int i) {
    }

    default void trackerMiEvent(int i, int i2) {
    }

    default void updateCloudEsimCountryList(String str) {
    }

    default Bundle getCloudData() {
        return new Bundle();
    }

    default List getDsda2DsdsWhiteListByCloud(Context context) {
        return new ArrayList();
    }

    default List getSmartSwitchWhiteListByCloud(Context context) {
        return new ArrayList();
    }

    default String getSignalMapSnsCellBlackListByCloud(Context context) {
        return "";
    }

    default List getPingPongWhiteListByCloud(Context context) {
        return new ArrayList();
    }

    default List getCommonSignalWhiteListByCloud(Context context) {
        return new ArrayList();
    }

    default List getDualDataConcurrentModeWhiteListByCloud(Context context) {
        return new ArrayList();
    }

    default List getDualDataConcurrentLimitedWhiteListByCloud(Context context) {
        return new ArrayList();
    }

    default List getDualDataRedundantModeWhiteListByCloud(Context context) {
        return new ArrayList();
    }

    default List getHikingModeCityWhiteListByCloud(Context context) {
        return new ArrayList();
    }

    default List getResolutionVideoAppsWhiteListByCloud(Context context) {
        return new ArrayList();
    }

    default List getSubwayModeSignalThresholdByCloud(Context context) {
        return new ArrayList();
    }

    default List getPreloadVideoAppsWhiteListByCloud(Context context) {
        return new ArrayList();
    }

    default List getHyperEngineGameWhiteListByCloud(Context context) {
        return new ArrayList();
    }

    default Bundle getModuleInfos() {
        return new Bundle();
    }
}
