package com.android.internal.telephony.ntnphone;

/* JADX INFO: loaded from: classes.dex */
public class NtnSmsSendResult implements Comparable<NtnSmsSendResult> {
    public int error;
    public int messageRef;
    public int status;

    public String toString() {
        return "messageRef = " + this.messageRef + ", status = " + this.status + ",error = " + this.error;
    }

    @Override // java.lang.Comparable
    public int compareTo(NtnSmsSendResult ntnSmsSendResult) {
        int i = this.messageRef;
        int i2 = ntnSmsSendResult.messageRef;
        if (i < i2) {
            return -1;
        }
        return i == i2 ? 0 : 1;
    }
}
