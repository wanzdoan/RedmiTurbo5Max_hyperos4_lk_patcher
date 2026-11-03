package com.android.phone;

import android.content.om.IOverlayManager;
import android.content.om.OverlayInfo;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class OverlayManagerWrapper {
    private final IOverlayManager mOverlayManager;

    public OverlayManagerWrapper(IOverlayManager iOverlayManager) {
        this.mOverlayManager = iOverlayManager;
    }

    public OverlayManagerWrapper() {
        this(IOverlayManager.Stub.asInterface(ServiceManager.getService("overlay")));
    }

    public boolean isOverlayEnable(String str, int i) {
        OverlayInfo overlayInfo;
        IOverlayManager iOverlayManager = this.mOverlayManager;
        if (iOverlayManager != null) {
            try {
                overlayInfo = iOverlayManager.getOverlayInfo(str, i);
            } catch (RemoteException e) {
                Log.e("OverlayManagerWrapper", "isOverlayEnable: getOverlayInfo fail. overlay = " + str + ", userId = " + i, e);
                overlayInfo = null;
            }
        } else {
            overlayInfo = null;
        }
        return overlayInfo != null && overlayInfo.isEnabled();
    }

    public boolean setEnabled(String str, boolean z, int i) {
        IOverlayManager iOverlayManager = this.mOverlayManager;
        if (iOverlayManager == null) {
            return false;
        }
        try {
            OverlayInfo overlayInfo = iOverlayManager.getOverlayInfo(str, i);
            Log.d("OverlayManagerWrapper", "setEnabled: overlayInfo overlayInfo = " + overlayInfo + ", userId = " + i);
            if (overlayInfo == null) {
                Log.d("OverlayManagerWrapper", "setEnabled: no overlays exist with the requested package name, return");
                return false;
            }
            return this.mOverlayManager.setEnabled(str, z, i);
        } catch (RemoteException e) {
            Log.e("OverlayManagerWrapper", "setEnabled: setEnabled fail. overlay = " + str + "enabled = " + z + ", userId = " + i, e);
            return false;
        }
    }
}
