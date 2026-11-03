package com.android.internal.telephony.call;

import android.telephony.ims.ImsReasonInfo;
import com.android.ims.ImsCall;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.UniTelephonyInjectionFactory;
import com.android.internal.telephony.emergency.RadioOnStateListener;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;

/* JADX INFO: loaded from: classes.dex */
public class UniCallManager {
    private static UniCallManager sInstance;

    public int getEccClirMode(ImsPhone imsPhone, int i, String str) {
        return i;
    }

    public void handleMergeComplete(ImsPhoneConnection imsPhoneConnection, ImsPhoneConnection imsPhoneConnection2, ImsPhone imsPhone) {
    }

    public boolean isInCsCall(boolean z, int i) {
        return false;
    }

    public void onImsCallHoldFailed(ImsCall imsCall, ImsReasonInfo imsReasonInfo, ImsPhone imsPhone) {
    }

    public void onImsCallMergeFailed(ImsCall imsCall, ImsReasonInfo imsReasonInfo, ImsPhone imsPhone) {
    }

    public void onImsCallResumeFailed(ImsCall imsCall, ImsReasonInfo imsReasonInfo, ImsPhone imsPhone) {
    }

    public boolean onImsCallStartFailed(ImsCall imsCall, ImsReasonInfo imsReasonInfo, ImsPhone imsPhone) {
        return false;
    }

    public boolean onImsCallTerminated(ImsCall imsCall, ImsReasonInfo imsReasonInfo, ImsPhone imsPhone) {
        return false;
    }

    public void onImsCallUpdated(ImsCall imsCall, ImsPhoneConnection imsPhoneConnection, boolean z, ImsPhone imsPhone) {
    }

    public void processImsIncomingCall(ImsCall imsCall, ImsPhone imsPhone) {
    }

    public void setNoNeedRedial(ImsPhone imsPhone) {
    }

    public boolean switchWaitingOrHoldingAndActive(ImsPhone imsPhone) throws CallStateException {
        return false;
    }

    public static UniCallManager getInstance() {
        if (sInstance == null) {
            sInstance = UniTelephonyInjectionFactory.getInstance().makeUniCallManager();
        }
        return sInstance;
    }

    public boolean anyPhoneIsOkToCall(Phone phone, RadioOnStateListener.Callback callback, int i, boolean z) {
        if (callback == null) {
            return false;
        }
        return callback.isOkToCall(phone, i, z);
    }
}
