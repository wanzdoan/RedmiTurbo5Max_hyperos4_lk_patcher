package com.android.phone.module;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IRemoteCallback;
import android.os.ResultReceiver;
import com.android.internal.telephony.Phone;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface ISatelliteModule {
    default void closeBdService() {
    }

    default void closeSatellite(int i, int i2) {
    }

    default void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    default int getAllowedBDSmsReceiveTime() {
        return 0;
    }

    default int getAllowedBDSmsSendTime() {
        return 0;
    }

    default float getAltitude() {
        return 0.0f;
    }

    default boolean getBdServiceStatus(int i) {
        return false;
    }

    default boolean getBdSwitchState() {
        return false;
    }

    default int getBdSysStatus() {
        return 0;
    }

    default int getBeanSwitchedStatus() {
        return 0;
    }

    default int getEnabledSatelliteSlotId() {
        return 0;
    }

    default int getErrorCause() {
        return 0;
    }

    default int getErrorClass() {
        return 0;
    }

    default int getFer() {
        return 0;
    }

    default int getFreqOff() {
        return 0;
    }

    default Location getLastKnownLocation() {
        return null;
    }

    default int getNtnBandId() {
        return 0;
    }

    default int getNtnBeamId() {
        return 0;
    }

    default Phone getNtnPhone() {
        return null;
    }

    default int getPhoneBdServiceState() {
        return 0;
    }

    default int getRealTxPwr() {
        return 0;
    }

    default int getRemainBDSmsCount(int i) {
        return 0;
    }

    default int getRetRssi() {
        return 0;
    }

    default int getRetSnr() {
        return 0;
    }

    default void getSarMode() {
    }

    default int getSatId() {
        return 0;
    }

    default int getSatelliteState(int i) {
        return 0;
    }

    default int getSaveBeamId() {
        return 0;
    }

    default int getTarTxPwr() {
        return 0;
    }

    default int getUsedCityInfoIndex() {
        return 0;
    }

    default boolean isCalibrated() {
        return false;
    }

    default boolean isHardSimSwapForTT() {
        return false;
    }

    default boolean isHighPenetrationEnabled() {
        return false;
    }

    default boolean isPrivacyStatementAgree() {
        return false;
    }

    default boolean isSatelliteDataEnabled() {
        return false;
    }

    default boolean isSatelliteEnabled() {
        return false;
    }

    default boolean isSatelliteSignalBoostEnabled() {
        return false;
    }

    default boolean isSatelliteXingwangEnabled() {
        return false;
    }

    default boolean isSupportedBDAudio() {
        return false;
    }

    default boolean isSupportedBDPicture() {
        return false;
    }

    default int preLoad(int i) {
        return 0;
    }

    default int receiveBDMessage(int i) {
        return 0;
    }

    default void receiveNtnGps(double d, double d2, double d3) {
    }

    default void recoverNtnNetwork() {
    }

    default boolean registSatelliteLocationCallback(IRemoteCallback iRemoteCallback, int i, int i2) {
        return false;
    }

    default void reportStatistics() {
    }

    default int sendBDAudioMessage(int i, int[] iArr, Uri uri, Uri uri2, int i2, List list) {
        return 0;
    }

    default int sendBDAudioMessageWithLocation(int i, int[] iArr, Uri uri, Uri uri2, int i2, List list, double d, double d2) {
        return 0;
    }

    default int sendBDMessage(int i, int[] iArr, String str, int i2, List list) {
        return 0;
    }

    default int sendBDMessageWithLocation(int i, int[] iArr, String str, int i2, List list, double d, double d2) {
        return 0;
    }

    default int sendBDPictureMessage(int i, int[] iArr, Uri uri, Uri uri2, int i2, List list) {
        return 0;
    }

    default int sendBDPictureMessageWithLocation(int i, int[] iArr, Uri uri, Uri uri2, int i2, List list, double d, double d2) {
        return 0;
    }

    default void sendCitRequest(int i, int i2, String str, ResultReceiver resultReceiver) {
    }

    default void sendSatelliteBusinessEntranceDot() {
    }

    default void setBeiDouEnableByUser(Context context, boolean z) {
    }

    default void setCalibrationInformation(Bundle bundle) {
    }

    default void setSarMode(int i) {
    }

    default void setSatelliteDataEnableByUser(Context context, boolean z) {
    }

    default boolean setSatelliteEnable(int i, boolean z) {
        return false;
    }

    default void setSatelliteEnableByUser(Context context, boolean z, int i) {
    }

    default void setSatelliteSignalBoostEnable(boolean z, int i) {
    }

    default void setSatelliteXingwangEnableByUser(Context context, boolean z, int i) {
    }

    default void setSlotId(int i) {
    }

    default void startConnectSatelliteService(int i) {
    }

    default void stopCitTest() {
    }

    default void syncBDMail(int i) {
    }

    default void unRegistSatelliteLocationCallback(IRemoteCallback iRemoteCallback) {
    }

    default void updateAddressInfo(Location location, Address address) {
    }

    default void updateCurrentLocation(String str) {
    }

    default Bundle getNtnLocationInfo() {
        return new Bundle();
    }

    default Bundle getNtnModuleInfo() {
        return new Bundle();
    }

    default String citRequest(int i, int i2, String str) {
        return "";
    }

    default String getSpeechBitrate() {
        return "";
    }

    default ArrayList getCalibrationInformation() {
        return new ArrayList();
    }

    default String getDataVersion() {
        return "";
    }

    default String getLocationType() {
        return "";
    }

    default String getNtnPosInfo() {
        return "";
    }
}
