package com.hyper.internal.telephony;

import android.telephony.ims.ImsReasonInfo;
import com.android.ims.ImsCall;
import com.android.internal.telephony.IHyperCallManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.emergency.RadioOnStateListener;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;

/* JADX INFO: loaded from: classes.dex */
public class HyperCallManagerImpl implements IHyperCallManager {
    private static HyperCallManagerImpl sInstance;

    private HyperCallManagerImpl() {
    }

    public static HyperCallManagerImpl getInstance() {
        HyperCallManagerImpl hyperCallManagerImpl;
        synchronized (HyperCallManagerImpl.class) {
            if (sInstance == null) {
                sInstance = new HyperCallManagerImpl();
            }
            hyperCallManagerImpl = sInstance;
        }
        return hyperCallManagerImpl;
    }

    public boolean isInCsCall(boolean isEmergency, int phoneId) {
        return HyperTelephonyInternalManager.getInstance().getHyperCallManager().isInCsCall(isEmergency, phoneId);
    }

    public boolean anyPhoneIsOkToCall(Phone phone, RadioOnStateListener.Callback callback, int serviceState, boolean imsVoiceCapable) {
        return HyperTelephonyInternalManager.getInstance().getHyperCallManager().anyPhoneIsOkToCall(phone, callback, serviceState, imsVoiceCapable);
    }

    public boolean onImsCallStartFailed(ImsCall imsCall, ImsReasonInfo reasonInfo, ImsPhone imsPhone) {
        return HyperTelephonyInternalManager.getInstance().getHyperCallManager().onImsCallStartFailed(imsCall, reasonInfo, imsPhone);
    }

    public int getEccClirMode(ImsPhone imsPhone, int clirMode, String origNumber) {
        return HyperTelephonyInternalManager.getInstance().getHyperCallManager().getEccClirMode(imsPhone, clirMode, origNumber);
    }

    public void onImsCallUpdated(ImsCall imsCall, ImsPhoneConnection conn, boolean isLocalTone, ImsPhone imsPhone) {
        if (imsPhone != null) {
            HyperTelephonyInternalManager.getInstance().getHyperCallManager().onImsCallUpdated(imsCall, conn, isLocalTone, imsPhone);
        }
    }
}
