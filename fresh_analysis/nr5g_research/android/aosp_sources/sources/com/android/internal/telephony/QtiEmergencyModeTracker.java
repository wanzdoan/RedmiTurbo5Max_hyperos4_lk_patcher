package com.android.internal.telephony;

import android.os.Handler;

/* JADX INFO: loaded from: classes.dex */
public class QtiEmergencyModeTracker {
    private boolean mPendingExitEcbmReq;
    private boolean mPendingExitScbmReq;

    public boolean isPhoneInEcbm() {
        return EcbmHandler.getInstance() != null && EcbmHandler.getInstance().isInEcm();
    }

    public boolean canExitScbm(Phone phone) {
        return phone != null && phone.isInScbm() && phone.isExitScbmFeatureSupported();
    }

    public boolean isPhoneInEmergencyMode(Phone phone) {
        return isPhoneInEcbm() || canExitScbm(phone);
    }

    public void exitEmergencyMode(Handler handler, Phone phone) throws Exception {
        if (phone == null) {
            return;
        }
        boolean zIsPhoneInEcbm = isPhoneInEcbm();
        boolean zCanExitScbm = canExitScbm(phone);
        if (zIsPhoneInEcbm) {
            EcbmHandler.getInstance().exitEmergencyCallbackMode();
            EcbmHandler.getInstance().setOnEcbModeExitResponse(handler, 14, null);
            this.mPendingExitEcbmReq = true;
        }
        if (zCanExitScbm) {
            phone.exitScbm();
            phone.setOnScbmExitResponse(handler, 19, null);
            this.mPendingExitScbmReq = true;
        }
    }

    public void setPendingExitEcbmReq(boolean z) {
        this.mPendingExitEcbmReq = z;
    }

    public boolean getPendingExitEcbmReq() {
        return this.mPendingExitEcbmReq;
    }

    public void setPendingExitScbmReq(boolean z) {
        this.mPendingExitScbmReq = z;
    }

    public boolean getPendingExitScbmReq() {
        return this.mPendingExitScbmReq;
    }
}
