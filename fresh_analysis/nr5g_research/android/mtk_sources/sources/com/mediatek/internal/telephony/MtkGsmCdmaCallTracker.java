package com.mediatek.internal.telephony;

import android.os.AsyncResult;
import android.os.Message;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.DriverCall;
import com.android.internal.telephony.GsmCdmaCall;
import com.android.internal.telephony.GsmCdmaCallTracker;
import com.android.internal.telephony.GsmCdmaConnection;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.PhoneInternalInterface;
import com.android.telephony.Rlog;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MtkGsmCdmaCallTracker extends GsmCdmaCallTracker {
    protected static final int EVENT_CALL_ADDITIONAL_INFO = 1006;
    protected static final int EVENT_MTK_BASE = 1000;
    private static final String PROP_LOG_TAG = "GsmCdmaCallTkr";
    public MtkGsmCdmaCallTrackerHelper mHelper;
    public MtkRIL mMtkCi;

    /* JADX WARN: Multi-variable type inference failed */
    public MtkGsmCdmaCallTracker(GsmCdmaPhone phone, FeatureFlags featureFlags) {
        super(phone, featureFlags);
        this.mMtkCi = null;
        this.mRingingCall = new GsmCdmaCall(this);
        this.mForegroundCall = new GsmCdmaCall(this);
        this.mBackgroundCall = new GsmCdmaCall(this);
        this.mMtkCi = this.mCi;
        this.mMtkCi.registerForCallAdditionalInfo(this, 1006, null);
        this.mHelper = new MtkGsmCdmaCallTrackerHelper(phone.getContext(), this);
    }

    public void handleMessage(Message msg) {
        this.mHelper.LogerMessage(msg.what);
        switch (msg.what) {
            case 1006:
                this.mHelper.handleCallAdditionalInfo((AsyncResult) msg.obj);
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }

    public void hangupAll() throws CallStateException {
        Rlog.d(PROP_LOG_TAG, "hangupAll");
        this.mMtkCi.hangupAll(obtainCompleteMessage());
        if (!this.mRingingCall.isIdle()) {
            this.mRingingCall.onHangupLocal();
        }
        if (!this.mForegroundCall.isIdle()) {
            this.mForegroundCall.onHangupLocal();
        }
        if (!this.mBackgroundCall.isIdle()) {
            this.mBackgroundCall.onHangupLocal();
        }
    }

    public int getHandoverConnectionSize() {
        return this.mHandoverConnections.size();
    }

    protected GsmCdmaConnection onCreateGsmCdmaConnection(GsmCdmaPhone phone, String dialString, GsmCdmaCallTracker ct, GsmCdmaCall parent, PhoneInternalInterface.DialArgs dialArgs) {
        return new MtkGsmCdmaConnection(phone, dialString, ct, parent, dialArgs);
    }

    protected GsmCdmaConnection onCreateGsmCdmaConnection(GsmCdmaPhone phone, DriverCall dc, GsmCdmaCallTracker ct, int index) {
        return new MtkGsmCdmaConnection(phone, dc, ct, index);
    }

    protected boolean onGetCallListDone(List polledCalls) {
        if (polledCalls.size() == 0) {
            this.mHelper.setGwsdCall(false);
        }
        return false;
    }

    protected void onCreateGsmCdmaConnectionForMismatch(DriverCall dc, GsmCdmaCallTracker ct, int index) {
        this.mConnections[index] = new MtkGsmCdmaConnection(this.mPhone, dc, this, index);
    }

    private boolean isPhoneTypeGsm() {
        return ((MtkGsmCdmaPhone) this.mPhone).isPhoneTypeGsm();
    }
}
