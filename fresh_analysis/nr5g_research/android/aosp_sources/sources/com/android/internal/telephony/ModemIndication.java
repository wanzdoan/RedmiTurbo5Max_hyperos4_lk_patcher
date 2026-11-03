package com.android.internal.telephony;

import android.hardware.radio.modem.IRadioModemIndication;
import android.hardware.radio.modem.ImeiInfo;
import android.hardware.radio.modem.RadioState$$;
import android.os.AsyncResult;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class ModemIndication extends IRadioModemIndication.Stub {
    private final RIL mRil;

    public int getInterfaceVersion() {
        return 5;
    }

    public ModemIndication(RIL ril) {
        this.mRil = ril;
    }

    public void hardwareConfigChanged(int i, android.hardware.radio.modem.HardwareConfig[] hardwareConfigArr) {
        this.mRil.processIndication(3, i);
        ArrayList<HardwareConfig> arrayListConvertHalHardwareConfigList = RILUtils.convertHalHardwareConfigList(hardwareConfigArr);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_HARDWARE_CONFIG_CHANGED, arrayListConvertHalHardwareConfigList);
        }
        this.mRil.mHardwareConfigChangeRegistrants.notifyRegistrants(new AsyncResult((Object) null, arrayListConvertHalHardwareConfigList, (Throwable) null));
    }

    public void modemReset(int i, String str) {
        this.mRil.processIndication(3, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_MODEM_RESTART, str);
        }
        MiuiNetworkControllerStub.get(this.mRil.mPhoneId.intValue()).updateModemResetFlag(true);
        this.mRil.writeMetricsModemRestartEvent(str);
        this.mRil.mModemResetRegistrants.notifyRegistrants(new AsyncResult((Object) null, str, (Throwable) null));
    }

    public void radioCapabilityIndication(int i, android.hardware.radio.modem.RadioCapability radioCapability) {
        this.mRil.processIndication(3, i);
        RadioCapability radioCapabilityConvertHalRadioCapability = RILUtils.convertHalRadioCapability(radioCapability, this.mRil);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogRet(NtnRILConstants.MI_NTN_RIL_UNSOL_RADIO_CAPABILITY, radioCapabilityConvertHalRadioCapability);
        }
        this.mRil.mPhoneRadioCapabilityChangedRegistrants.notifyRegistrants(new AsyncResult((Object) null, radioCapabilityConvertHalRadioCapability, (Throwable) null));
    }

    public void radioStateChanged(int i, int i2) {
        this.mRil.processIndication(3, i);
        int iConvertHalRadioState = RILUtils.convertHalRadioState(i2);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogMore(1000, "radioStateChanged: " + RadioState$$.toString(i2));
        }
        boolean zSkipRadioStateIfNeed = MiuiRadioControllerStub.get(this.mRil.mPhoneId.intValue()).skipRadioStateIfNeed(iConvertHalRadioState);
        RIL ril = this.mRil;
        if (zSkipRadioStateIfNeed) {
            if (ril.isLogOrTrace()) {
                this.mRil.riljLog("skip radio state changed: " + iConvertHalRadioState);
                return;
            }
            return;
        }
        ril.setRadioState(iConvertHalRadioState, false);
    }

    public void rilConnected(int i) {
        this.mRil.processIndication(3, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLog(NtnRILConstants.MI_NTN_RIL_UNSOL_RIL_CONNECTED);
        }
        this.mRil.setRadioPower(false, null);
        this.mRil.notifyRegistrantsRilConnectionChanged(15);
    }

    public String getInterfaceHash() {
        return "4a8f76d92aa7ad468df8def63815c0bce7332fbd";
    }

    public void onImeiMappingChanged(int i, ImeiInfo imeiInfo) {
        this.mRil.processIndication(3, i);
        if (this.mRil.isLogOrTrace()) {
            this.mRil.unsljLogMore(1110, "ImeiMappingChanged");
        }
        this.mRil.notifyRegistrantsImeiMappingChanged(imeiInfo);
    }
}
