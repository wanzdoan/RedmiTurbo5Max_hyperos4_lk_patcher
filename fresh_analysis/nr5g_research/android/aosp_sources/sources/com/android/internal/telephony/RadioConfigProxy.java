package com.android.internal.telephony;

import android.hardware.radio.config.V1_1.IRadioConfig;
import android.hardware.radio.config.V1_1.ModemsConfig;
import android.os.IBinder;
import android.os.IHwBinder;
import android.os.RemoteException;
import android.telephony.UiccSlotMapping;
import com.android.telephony.Rlog;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class RadioConfigProxy {
    private boolean mIsAidl;
    private final RadioConfigAidlServiceDeathRecipient mRadioConfigAidlServiceDeathRecipient;
    private final RadioConfigHidlServiceDeathRecipient mRadioConfigHidlServiceDeathRecipient;
    private HalVersion mRadioHalVersion;
    private volatile IRadioConfig mHidlRadioConfigProxy = null;
    private volatile android.hardware.radio.config.IRadioConfig mAidlRadioConfigProxy = null;
    private HalVersion mRadioConfigHalVersion = RIL.RADIO_HAL_VERSION_UNKNOWN;

    public RadioConfigProxy(RadioConfig radioConfig, HalVersion halVersion) {
        this.mRadioHalVersion = halVersion;
        this.mRadioConfigAidlServiceDeathRecipient = new RadioConfigAidlServiceDeathRecipient(radioConfig);
        this.mRadioConfigHidlServiceDeathRecipient = new RadioConfigHidlServiceDeathRecipient(radioConfig);
    }

    public void setHidl(HalVersion halVersion, IRadioConfig iRadioConfig) {
        this.mRadioConfigHalVersion = halVersion;
        this.mHidlRadioConfigProxy = iRadioConfig;
        this.mIsAidl = false;
        this.mRadioConfigHidlServiceDeathRecipient.setService(iRadioConfig);
    }

    public IRadioConfig getHidl11() {
        return this.mHidlRadioConfigProxy;
    }

    public android.hardware.radio.config.V1_3.IRadioConfig getHidl13() {
        return (android.hardware.radio.config.V1_3.IRadioConfig) this.mHidlRadioConfigProxy;
    }

    public void setAidl(android.hardware.radio.config.IRadioConfig iRadioConfig) {
        try {
            this.mRadioConfigHalVersion = RIL.getServiceHalVersion(iRadioConfig.getInterfaceVersion());
            Rlog.d("RadioConfigProxy", "setAidl: setting HAL version to version = " + this.mRadioConfigHalVersion);
        } catch (RemoteException e) {
            Rlog.e("RadioConfigProxy", "setAidl: " + e);
        }
        this.mAidlRadioConfigProxy = iRadioConfig;
        this.mIsAidl = true;
        this.mRadioConfigAidlServiceDeathRecipient.setService(iRadioConfig.asBinder());
    }

    public android.hardware.radio.config.IRadioConfig getAidl() {
        return this.mAidlRadioConfigProxy;
    }

    public void clear() {
        this.mRadioConfigHalVersion = RIL.RADIO_HAL_VERSION_UNKNOWN;
        this.mHidlRadioConfigProxy = null;
        this.mAidlRadioConfigProxy = null;
        this.mRadioConfigHidlServiceDeathRecipient.clear();
        this.mRadioConfigAidlServiceDeathRecipient.clear();
    }

    public void linkToDeath(long j) throws RemoteException {
        if (isAidl()) {
            this.mRadioConfigAidlServiceDeathRecipient.linkToDeath((int) j);
        } else {
            this.mRadioConfigHidlServiceDeathRecipient.linkToDeath(j);
        }
    }

    public boolean isEmpty() {
        return this.mAidlRadioConfigProxy == null && this.mHidlRadioConfigProxy == null;
    }

    public boolean isAidl() {
        return this.mIsAidl;
    }

    public HalVersion getVersion() {
        return this.mRadioConfigHalVersion;
    }

    public void setResponseFunctions(RadioConfig radioConfig) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mAidlRadioConfigProxy.setResponseFunctions(new RadioConfigResponseAidl(radioConfig, this.mRadioHalVersion), new RadioConfigIndicationAidl(radioConfig));
        } else {
            this.mHidlRadioConfigProxy.setResponseFunctions(new RadioConfigResponseHidl(radioConfig, this.mRadioHalVersion), new RadioConfigIndicationHidl(radioConfig));
        }
    }

    public Set<String> getFullCapabilitySet() {
        return RILUtils.getCaps(this.mRadioHalVersion, false);
    }

    public void getSimSlotStatus(int i) throws RemoteException {
        if (isAidl()) {
            getAidl().getSimSlotsStatus(i);
        } else {
            getHidl11().getSimSlotsStatus(i);
        }
    }

    public void setPreferredDataModem(int i, int i2) throws RemoteException {
        if (isAidl()) {
            getAidl().setPreferredDataModem(i, (byte) i2);
        } else {
            getHidl11().setPreferredDataModem(i, (byte) i2);
        }
    }

    public void getPhoneCapability(int i) throws RemoteException {
        if (isAidl()) {
            getAidl().getPhoneCapability(i);
        } else {
            getHidl11().getPhoneCapability(i);
        }
    }

    public void setSimSlotsMapping(int i, List<UiccSlotMapping> list) throws RemoteException {
        if (isAidl()) {
            getAidl().setSimSlotsMapping(i, RILUtils.convertSimSlotsMapping(list));
        } else {
            getHidl11().setSimSlotsMapping(i, RILUtils.convertSlotMappingToList(list));
        }
    }

    public void updateSimultaneousCallingSupport(int i) throws RemoteException {
        if (isAidl()) {
            getAidl().getSimultaneousCallingSupport(i);
        }
    }

    public void setNumOfLiveModems(int i, int i2) throws RemoteException {
        if (isAidl()) {
            getAidl().setNumOfLiveModems(i, (byte) i2);
            return;
        }
        ModemsConfig modemsConfig = new ModemsConfig();
        modemsConfig.numOfLiveModems = (byte) i2;
        getHidl11().setModemsConfig(i, modemsConfig);
    }

    public void getHalDeviceCapabilities(int i) throws RemoteException {
        if (isAidl()) {
            getAidl().getHalDeviceCapabilities(i);
        } else {
            getHidl13().getHalDeviceCapabilities(i);
        }
    }

    public void setSimType(int i, int[] iArr) throws RemoteException {
        if (isAidl()) {
            getAidl().setSimType(i, iArr);
        }
    }

    public void getSimTypeInfo(int i) throws RemoteException {
        if (isAidl()) {
            getAidl().getSimTypeInfo(i);
        }
    }

    public void rebootModem(int i) throws RemoteException {
        if (isAidl()) {
            getAidl().rebootModem(i);
        }
    }

    private static class RadioConfigHidlServiceDeathRecipient implements IHwBinder.DeathRecipient {
        private final RadioConfig mRadioConfig;
        private IRadioConfig mService;

        RadioConfigHidlServiceDeathRecipient(RadioConfig radioConfig) {
            this.mRadioConfig = radioConfig;
        }

        public void setService(IRadioConfig iRadioConfig) {
            this.mService = iRadioConfig;
        }

        public void linkToDeath(long j) throws RemoteException {
            IRadioConfig iRadioConfig = this.mService;
            if (iRadioConfig != null) {
                iRadioConfig.linkToDeath(this, j);
            } else {
                Rlog.w("RadioConfigHidlSDR", "linkToDeath: skipping since mService is null");
            }
        }

        public void clear() {
            this.mService = null;
        }

        public void serviceDied(long j) {
            Rlog.e("RadioConfigHidlSDR", "serviceDied");
            RadioConfig radioConfig = this.mRadioConfig;
            radioConfig.sendMessage(radioConfig.obtainMessage(1, Long.valueOf(j)));
        }
    }

    private static class RadioConfigAidlServiceDeathRecipient implements IBinder.DeathRecipient {
        private final RadioConfig mRadioConfig;
        private IBinder mService;

        RadioConfigAidlServiceDeathRecipient(RadioConfig radioConfig) {
            this.mRadioConfig = radioConfig;
        }

        public void setService(IBinder iBinder) {
            this.mService = iBinder;
        }

        public void linkToDeath(int i) throws RemoteException {
            IBinder iBinder = this.mService;
            if (iBinder != null) {
                iBinder.linkToDeath(this, i);
            } else {
                Rlog.w("RadioConfigAidlSDR", "linkToDeath: skipping since mService is null");
            }
        }

        public void clear() {
            this.mService = null;
        }

        public synchronized void unlinkToDeath() {
            IBinder iBinder = this.mService;
            if (iBinder != null) {
                iBinder.unlinkToDeath(this, 0);
                this.mService = null;
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Rlog.e("RadioConfigAidlSDR", "service died.");
            unlinkToDeath();
            RadioConfig radioConfig = this.mRadioConfig;
            radioConfig.sendMessage(radioConfig.obtainMessage(2));
        }
    }

    public String toString() {
        return "RadioConfigProxy[mRadioHalVersion=" + this.mRadioHalVersion + ", mRadioConfigHalVersion=" + this.mRadioConfigHalVersion + ']';
    }
}
