package com.android.internal.telephony.ntnphone;

/* JADX INFO: loaded from: classes.dex */
public class NtnSmsStatusReport implements Comparable<NtnSmsStatusReport> {
    public int messageRef;
    public int status;
    public int tpStatus;

    public String toString() {
        return "messageRef = " + this.messageRef + ", status = " + this.status + ",tpStatus = " + this.tpStatus;
    }

    @Override // java.lang.Comparable
    public int compareTo(NtnSmsStatusReport ntnSmsStatusReport) {
        int i = this.messageRef;
        int i2 = ntnSmsStatusReport.messageRef;
        if (i < i2) {
            return -1;
        }
        return i == i2 ? 0 : 1;
    }
}
