package com.android.phone.settings.hiddenmenu;

import android.os.PersistableBundle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/* JADX INFO: loaded from: classes.dex */
public class PhoneInfoSharedViewModel extends ViewModel {
    private final MutableLiveData[] mDataSignalStrength = {new MutableLiveData(), new MutableLiveData()};
    private final MutableLiveData[] mDataNetworkTypeDisplay = {new MutableLiveData(), new MutableLiveData()};
    private final MutableLiveData[] mSimulateOutOfService = {new MutableLiveData(), new MutableLiveData()};
    private final MutableLiveData[] mSatelliteEnabled = {new MutableLiveData(), new MutableLiveData()};
    private final MutableLiveData[] mSatelliteDataEnabled = {new MutableLiveData(), new MutableLiveData()};
    private final MutableLiveData[] mSatelliteDataMode = {new MutableLiveData(), new MutableLiveData()};
    private final MutableLiveData[] mSatelliteForceCamped = {new MutableLiveData(), new MutableLiveData()};
    private final PersistableBundle[] mCarrierSatelliteOriginalBundle = new PersistableBundle[2];
    private final PersistableBundle[] mSatelliteDataOriginalBundle = new PersistableBundle[2];
    private final PersistableBundle[] mOriginalSystemChannels = new PersistableBundle[2];
    private final PersistableBundle[] mPreviousSatelliteBand = new PersistableBundle[2];
    private final int[] mSelectedManualOverrideBandIndex = new int[2];

    public PhoneInfoSharedViewModel() {
        resetToDefaults();
    }

    public void setDataSignalStrength(int i, int i2) {
        this.mDataSignalStrength[i2].setValue(Integer.valueOf(i));
    }

    public LiveData getDataSignalStrength(int i) {
        return this.mDataSignalStrength[i];
    }

    public void setDataNetworkTypeDisplay(int i, int i2) {
        this.mDataNetworkTypeDisplay[i2].setValue(Integer.valueOf(i));
    }

    public LiveData getDataNetworkTypeDisplay(int i) {
        return this.mDataNetworkTypeDisplay[i];
    }

    public void setSimulateOutOfService(boolean z, int i) {
        this.mSimulateOutOfService[i].setValue(Boolean.valueOf(z));
    }

    public LiveData getSimulateOutOfService(int i) {
        return this.mSimulateOutOfService[i];
    }

    public void setSatelliteEnabled(boolean z, int i) {
        this.mSatelliteEnabled[i].setValue(Boolean.valueOf(z));
    }

    public LiveData getSatelliteEnabled(int i) {
        return this.mSatelliteEnabled[i];
    }

    public void setSatelliteEnabledBundle(PersistableBundle persistableBundle, int i) {
        this.mCarrierSatelliteOriginalBundle[i] = persistableBundle;
    }

    public PersistableBundle getSatelliteEnabledBundle(int i) {
        return this.mCarrierSatelliteOriginalBundle[i];
    }

    public void setSatelliteDataModeEnabled(boolean z, int i) {
        this.mSatelliteDataEnabled[i].setValue(Boolean.valueOf(z));
    }

    public LiveData getSatelliteDataEnabled(int i) {
        return this.mSatelliteDataEnabled[i];
    }

    public void setSatelliteDataMode(int i, int i2) {
        this.mSatelliteDataMode[i2].setValue(Integer.valueOf(i));
    }

    public LiveData getSatelliteDataMode(int i) {
        return this.mSatelliteDataMode[i];
    }

    public void setSatelliteDataModeBundle(PersistableBundle persistableBundle, int i) {
        this.mSatelliteDataOriginalBundle[i] = persistableBundle;
    }

    public PersistableBundle getSatelliteDataModeBundle(int i) {
        return this.mSatelliteDataOriginalBundle[i];
    }

    public void setSatelliteForceCamped(boolean z, int i) {
        this.mSatelliteForceCamped[i].setValue(Boolean.valueOf(z));
    }

    public LiveData getSatelliteForceCamped(int i) {
        return this.mSatelliteForceCamped[i];
    }

    public void setOriginalSystemChannels(PersistableBundle persistableBundle, int i) {
        this.mOriginalSystemChannels[i] = persistableBundle;
    }

    public PersistableBundle getOriginalSystemChannels(int i) {
        return this.mOriginalSystemChannels[i];
    }

    public void setPreviousSatelliteBand(PersistableBundle persistableBundle, int i) {
        this.mPreviousSatelliteBand[i] = persistableBundle;
    }

    public PersistableBundle getPreviousSatelliteBand(int i) {
        return this.mPreviousSatelliteBand[i];
    }

    public void setSelectedManualOverrideBandIndex(int i, int i2) {
        this.mSelectedManualOverrideBandIndex[i2] = i;
    }

    public int getSelectedManualOverrideBandIndex(int i) {
        return this.mSelectedManualOverrideBandIndex[i];
    }

    public void resetToDefaults() {
        this.mDataSignalStrength[0].setValue(-113);
        this.mDataNetworkTypeDisplay[0].setValue(-1);
        MutableLiveData mutableLiveData = this.mSimulateOutOfService[0];
        Boolean bool = Boolean.FALSE;
        mutableLiveData.setValue(bool);
        this.mDataSignalStrength[1].setValue(-113);
        this.mDataNetworkTypeDisplay[1].setValue(-1);
        this.mSimulateOutOfService[1].setValue(bool);
        this.mSatelliteEnabled[0].setValue(bool);
        this.mSatelliteDataEnabled[0].setValue(bool);
        this.mSatelliteDataMode[0].setValue(-1);
        this.mSatelliteEnabled[1].setValue(bool);
        this.mSatelliteDataEnabled[1].setValue(bool);
        this.mSatelliteDataMode[1].setValue(-1);
        PersistableBundle[] persistableBundleArr = this.mCarrierSatelliteOriginalBundle;
        persistableBundleArr[0] = null;
        persistableBundleArr[1] = null;
        PersistableBundle[] persistableBundleArr2 = this.mSatelliteDataOriginalBundle;
        persistableBundleArr2[0] = null;
        persistableBundleArr2[1] = null;
        PersistableBundle[] persistableBundleArr3 = this.mOriginalSystemChannels;
        persistableBundleArr3[0] = null;
        persistableBundleArr3[1] = null;
        PersistableBundle[] persistableBundleArr4 = this.mPreviousSatelliteBand;
        persistableBundleArr4[0] = null;
        persistableBundleArr4[1] = null;
        int[] iArr = this.mSelectedManualOverrideBandIndex;
        iArr[0] = 0;
        iArr[1] = 0;
    }
}
