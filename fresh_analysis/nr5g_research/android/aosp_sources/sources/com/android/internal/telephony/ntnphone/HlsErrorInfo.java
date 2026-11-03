package com.android.internal.telephony.ntnphone;

/* JADX INFO: loaded from: classes.dex */
public class HlsErrorInfo implements Comparable<HlsErrorInfo> {
    public int class_info;
    public int error_cause;

    @Override // java.lang.Comparable
    public int compareTo(HlsErrorInfo hlsErrorInfo) {
        return 0;
    }

    HlsErrorInfo() {
    }

    public String toString() {
        return "class_info = " + this.class_info + "error_cause = " + this.error_cause;
    }
}
