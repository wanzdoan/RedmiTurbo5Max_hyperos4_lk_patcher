package com.nxp.osu;

/* JADX INFO: loaded from: classes.dex */
public final class SecOsuException extends Exception {
    String mReasonCode;

    public SecOsuException(String str) {
        super(str);
        this.mReasonCode = str;
    }
}
