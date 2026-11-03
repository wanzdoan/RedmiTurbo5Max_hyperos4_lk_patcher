package com.mediatek.internal.telephony;

import android.os.RemoteException;
import com.android.internal.telephony.HalVersion;
import vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioEx;

/* JADX INFO: loaded from: classes.dex */
public abstract class MtkRadioExServiceProxy {
    boolean mIsAidl;
    HalVersion mHalVersion = MtkRIL.RADIO_HAL_VERSION_MTK_UNKNOWN;
    volatile IMtkRadioEx mRadioProxyMtk = null;

    abstract void clear();

    abstract boolean isEmpty();

    abstract void responseAcknowledgementMtk() throws RemoteException;

    public boolean isAidl() {
        return this.mIsAidl;
    }

    public void setHidl(HalVersion halVersion, IMtkRadioEx radio) {
        this.mHalVersion = halVersion;
        this.mRadioProxyMtk = radio;
        this.mIsAidl = false;
    }

    public IMtkRadioEx getHidl() {
        return this.mRadioProxyMtk;
    }
}
