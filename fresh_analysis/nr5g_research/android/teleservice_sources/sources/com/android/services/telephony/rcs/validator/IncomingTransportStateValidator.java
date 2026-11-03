package com.android.services.telephony.rcs.validator;

import android.telephony.ims.SipDelegateManager;
import android.telephony.ims.SipMessage;
import android.util.ArrayMap;

/* JADX INFO: loaded from: classes.dex */
public class IncomingTransportStateValidator implements SipMessageValidator {
    private static final ArrayMap ENUM_TO_STRING_MAP;
    private int mState = 0;
    private int mReason = 2;

    static {
        ArrayMap arrayMap = new ArrayMap(2);
        ENUM_TO_STRING_MAP = arrayMap;
        arrayMap.append(0, "CLOSED");
        arrayMap.append(1, "OPEN");
    }

    public void open() {
        this.mState = 1;
        this.mReason = -1;
    }

    public void close(int i) {
        this.mState = 0;
        this.mReason = i;
    }

    @Override // com.android.services.telephony.rcs.validator.SipMessageValidator
    public ValidationResult validate(SipMessage sipMessage) {
        if (this.mState != 1) {
            return new ValidationResult(this.mReason, "incoming transport closed");
        }
        return ValidationResult.SUCCESS;
    }

    public String toString() {
        return "Incoming Transport State: " + ((String) ENUM_TO_STRING_MAP.getOrDefault(Integer.valueOf(this.mState), String.valueOf(this.mState))) + ", reason: " + ((String) SipDelegateManager.MESSAGE_FAILURE_REASON_STRING_MAP.getOrDefault(Integer.valueOf(this.mReason), String.valueOf(this.mReason)));
    }
}
