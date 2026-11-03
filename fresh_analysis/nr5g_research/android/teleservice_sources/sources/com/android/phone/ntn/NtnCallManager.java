package com.android.phone.ntn;

import com.android.internal.telephony.Call;
import com.android.internal.telephony.Phone;

/* JADX INFO: loaded from: classes.dex */
public class NtnCallManager {
    private static NtnCallManager sInstance;

    public static NtnCallManager getInstance() {
        NtnCallManager ntnCallManager = sInstance;
        return ntnCallManager == null ? new NtnCallManager() : ntnCallManager;
    }

    public void onBtConnectStateChange(int i, int i2) {
        if (NtnBoxServiceManager.getInstance().isBtTypeChanged()) {
            maybeClearCalls();
            NtnLogUtils.logi("NtnCallManager", "Hangup call since the BT type changed.");
        }
    }

    public boolean maybeClearCalls() {
        if (!isInCall()) {
            return false;
        }
        hangupNtnCall();
        NtnLogUtils.logi("NtnCallManager", "clear calls.");
        return true;
    }

    public void hangupNtnCall() {
        try {
            Phone ntnPhone = NtnPhoneProxyBase.getInstance().getNtnPhone();
            if (ntnPhone != null) {
                Call foregroundCall = ntnPhone.getForegroundCall();
                Call ringingCall = ntnPhone.getRingingCall();
                if (ringingCall != null && !ringingCall.isIdle()) {
                    ringingCall.hangup();
                } else {
                    if (foregroundCall == null || foregroundCall.isIdle()) {
                        return;
                    }
                    foregroundCall.hangup();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isInCall() {
        try {
            Phone ntnPhone = NtnPhoneProxyBase.getInstance().getNtnPhone();
            if (ntnPhone != null) {
                return ntnPhone.getForegroundCall().getState().isAlive() || ntnPhone.getRingingCall().getState().isAlive();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
