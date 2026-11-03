package com.mediatek.internal.telephony;

import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.DriverCall;
import com.android.internal.telephony.GsmCdmaCall;
import com.android.internal.telephony.GsmCdmaCallTracker;
import com.android.internal.telephony.GsmCdmaConnection;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.PhoneInternalInterface;

/* JADX INFO: loaded from: classes.dex */
public class MtkGsmCdmaConnection extends GsmCdmaConnection {
    private boolean mIsIncomingCallGwsd;

    public synchronized boolean isIncomingCallGwsd() {
        log("isIncomingCallGwsd: " + this.mIsIncomingCallGwsd);
        return this.mIsIncomingCallGwsd;
    }

    public MtkGsmCdmaConnection(GsmCdmaPhone phone, String dialString, GsmCdmaCallTracker ct, GsmCdmaCall parent, PhoneInternalInterface.DialArgs dialArgs) {
        super(phone, dialString, ct, parent, dialArgs);
        this.mIsIncomingCallGwsd = false;
    }

    public MtkGsmCdmaConnection(GsmCdmaPhone phone, DriverCall dc, GsmCdmaCallTracker ct, int index) {
        super(phone, dc, ct, index);
        this.mIsIncomingCallGwsd = false;
        if (((MtkGsmCdmaCallTracker) ct).mHelper.isGwsdCall()) {
            this.mIsIncomingCallGwsd = true;
            ((MtkGsmCdmaCallTracker) ct).mHelper.setGwsdCall(false);
        }
    }

    public boolean compareTo(DriverCall c) {
        return super.compareTo(c) || isIncomingCallGwsd();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.CallStateException */
    public void consultativeTransfer(Connection other) throws CallStateException {
        GsmCdmaPhone phone = this.mOwner.getPhone();
        if (phone != null) {
            if (phone.canTransfer()) {
                phone.explicitCallTransfer();
                return;
            }
            throw new CallStateException("cs can transfer check fail");
        }
        throw new CallStateException("cs ect fail (phone null)");
    }
}
