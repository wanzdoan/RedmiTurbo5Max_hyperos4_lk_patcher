package com.android.internal.telephony;

import android.os.RemoteException;
import com.android.telephony.Rlog;
import java.util.List;
import vendor.xiaomi.hardware.radio.IXmRadio;

/* JADX INFO: loaded from: classes.dex */
public class XmRadioProxy {
    private static final String TAG = "XmRadioProxy";
    private HalVersion mHalVersion = XmRIL.XM_RADIO_HAL_VERSION_UNKNOWN;
    private volatile IXmRadio mRadioProxy = null;

    public HalVersion setAidl(HalVersion halVersion, IXmRadio radio) {
        HalVersion version = halVersion;
        try {
            version = XmRIL.getServiceHalVersion(radio.getInterfaceVersion());
        } catch (RemoteException e) {
            Rlog.e(TAG, "setAidl: " + e);
        }
        this.mHalVersion = version;
        this.mRadioProxy = radio;
        Rlog.d(TAG, "AIDL initialized mHalVersion=" + this.mHalVersion);
        return this.mHalVersion;
    }

    public IXmRadio getAidl() {
        return this.mRadioProxy;
    }

    public void clear() {
        this.mHalVersion = XmRIL.XM_RADIO_HAL_VERSION_UNKNOWN;
        this.mRadioProxy = null;
    }

    public boolean isEmpty() {
        return this.mRadioProxy == null;
    }

    public void sendCommMsg(int serial, int msgId, byte[] data) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.sendCommMsg(serial, msgId, data);
    }

    public void setSarDsi(int serial, int dsi) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.setSarDsi(serial, dsi);
    }

    public void setSarDsiGroup(int serial, int[] dsi) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.setSarDsiGroup(serial, dsi);
    }

    public void getSarDsi(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.getSarDsi(serial);
    }

    public void getSarDsiGroup(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.getSarDsiGroup(serial);
    }

    public void setDeprioritizeSA(int serial, boolean enable) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.setDeprioritizeSA(serial, enable);
    }

    public void setNrMode(int serial, int nrStatus) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.setNrMode(serial, nrStatus);
    }

    public void enableEndc(int serial, boolean enable) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.enableEndc(serial, enable);
    }

    public void setDataStallMode(int serial, int mode, int operation, int bearerType, int scene) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.setDataStallMode(serial, mode, operation, bearerType, scene);
    }

    public void setMobilityOptParams(int serial, boolean enable, int rat, int featureId) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.setMobilityOptParams(serial, enable, rat, featureId);
    }

    public void setPreferBandParams(int serial, int rat, int[] bands, int[] arfcns, int[] rsrpOffsets, int preferType) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.setPreferBandParams(serial, rat, bands, arfcns, rsrpOffsets, preferType);
    }

    public void promoteBSR(int serial, int level) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.promoteBSR(serial, level);
    }

    public void getDdsAssistantInfo(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.getDdsAssistantInfo(serial);
    }

    public void getModemScene(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.getModemScene(serial);
    }

    public void getModemReason(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.getModemReason(serial);
    }

    public void setModemModelCfg(int serial, int[] modelCfg) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.setModemModelCfg(serial, modelCfg);
    }

    public void setModemCallbackSwitch(int serial, boolean enable) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.setModemCallbackSwitch(serial, enable);
    }

    public void setModemEventIndSwitch(int serial, int event, boolean isRegister) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.setModemEventIndSwitch(serial, event, isRegister);
    }

    public void setFiveTupleBlackList(int serial, String rmnetData, List<String> ipList, int mode) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        this.mRadioProxy.setFiveTupleBlackList(serial, rmnetData, ipList, mode);
    }
}
