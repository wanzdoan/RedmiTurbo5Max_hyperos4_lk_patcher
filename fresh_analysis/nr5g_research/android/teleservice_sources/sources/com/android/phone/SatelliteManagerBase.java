package com.android.phone;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.IRemoteCallback;
import android.os.ResultReceiver;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public abstract class SatelliteManagerBase {
    protected static volatile SatelliteManagerBase mInstance;

    public abstract String citRequest(int i, int i2, String str);

    public abstract void closeSatellite(int i, int i2);

    public abstract float getAltitude();

    public abstract ArrayList getCalibrationInformation();

    public abstract Location getCurrentLocationWhenTurnOnSatellite();

    public abstract String getDataVersion();

    public abstract int getEnabledSatelliteSlotId();

    public abstract int getErrorCause();

    public abstract int getErrorClass();

    public abstract int getFer();

    public abstract int getFreqOff();

    public abstract String getLocationType();

    public abstract int getNtnBandId();

    public abstract int getNtnBeamId();

    public abstract String getNtnPosInfo();

    public abstract int getRealTxPwr();

    public abstract int getRetRssi();

    public abstract int getRetSnr();

    public abstract void getSarMode();

    public abstract int getSatId();

    public abstract int getSatelliteState(int i);

    public abstract int getSaveBeamId();

    public abstract String getSpeechBitrate();

    public abstract int getTarTxPwr();

    public abstract int getUsedCityInfoIndex();

    public abstract boolean hasCellInfoWhenTurnOnSatellite();

    public abstract boolean isCalibrated();

    public abstract boolean isExitSatelliteInProgress();

    public abstract boolean isHardSimSwapForTT();

    public abstract boolean isHighPenetrationEnabled();

    public abstract boolean isPrivacyStatementAgree();

    public abstract boolean isSatelliteDataEnabled();

    public abstract boolean isSatelliteEnabled();

    public abstract boolean isSatelliteSignalBoostEnabled();

    public abstract boolean isSatelliteXingwangEnabled();

    public abstract void openSatellite(int i, int i2);

    public abstract void receiveNtnGps(double d, double d2, double d3);

    public abstract boolean registSatelliteLocationCallback(IRemoteCallback iRemoteCallback, int i, int i2);

    public abstract void satelliteDisplay();

    public abstract void sendCitRequest(int i, int i2, String str, ResultReceiver resultReceiver);

    public abstract void sendEnableSatelliteBroadcast(int i, boolean z, boolean z2);

    public abstract void sendSatelliteBusinessEntranceDot();

    public abstract void setCalibrationInformation(Bundle bundle);

    public abstract void setSarMode(int i);

    public abstract void setSatelliteDataEnableByUser(Context context, boolean z);

    public abstract boolean setSatelliteEnable(int i, boolean z);

    public abstract void setSatelliteEnableByUser(Context context, boolean z, int i);

    public abstract void setSatelliteSignalBoostEnable(boolean z, int i);

    public abstract void setSatelliteXingwangEnableByUser(Context context, boolean z, int i);

    public abstract void stopCitTest();

    public abstract void unRegistSatelliteLocationCallback(IRemoteCallback iRemoteCallback);

    public static SatelliteManagerBase getInstance() {
        return mInstance;
    }
}
