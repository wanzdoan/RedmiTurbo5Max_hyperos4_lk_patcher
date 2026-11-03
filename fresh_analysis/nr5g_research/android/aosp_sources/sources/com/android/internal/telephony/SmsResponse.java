package com.android.internal.telephony;

/* JADX INFO: loaded from: classes.dex */
public class SmsResponse {
    public static final int NO_ERROR_CODE = -1;
    String mAckPdu;
    public int mErrorCode;
    public long mMessageId;
    public int mMessageRef;

    public SmsResponse(int i, String str, int i2) {
        this(i, str, i2, 0L);
    }

    public SmsResponse(int i, String str, int i2, long j) {
        this.mMessageRef = i;
        this.mAckPdu = str;
        this.mErrorCode = i2;
        this.mMessageId = j;
    }

    public String toString() {
        return "{ mMessageRef = " + this.mMessageRef + ", mErrorCode = " + this.mErrorCode + ", mAckPdu = " + this.mAckPdu + ", " + SmsController.formatCrossStackMessageId(this.mMessageId) + "}";
    }
}
