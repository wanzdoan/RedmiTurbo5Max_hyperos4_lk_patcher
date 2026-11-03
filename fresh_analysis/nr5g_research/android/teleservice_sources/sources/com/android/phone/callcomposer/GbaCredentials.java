package com.android.phone.callcomposer;

/* JADX INFO: loaded from: classes.dex */
public class GbaCredentials {
    private final byte[] mKey;
    private final String mTransactionId;

    public GbaCredentials(String str, byte[] bArr) {
        this.mTransactionId = str;
        this.mKey = bArr;
    }

    public String getTransactionId() {
        return this.mTransactionId;
    }

    public byte[] getKey() {
        return this.mKey;
    }
}
