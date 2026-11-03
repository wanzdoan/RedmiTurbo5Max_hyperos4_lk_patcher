package com.nxp.osu;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public class OSUAPPOmapiChannel {
    private static byte[] SUCCESS_RESP = {-112, 0};
    private static OSUAPPOmapiChannel sOmapiChannel;
    private EuiccAccessStub mEuiccAccess;

    public static OSUAPPOmapiChannel getInstance(Context context) {
        if (sOmapiChannel == null) {
            sOmapiChannel = new OSUAPPOmapiChannel();
        }
        return sOmapiChannel;
    }

    private OSUAPPOmapiChannel() {
        this.mEuiccAccess = null;
        this.mEuiccAccess = EuiccAccessStub.getInstance();
    }

    public byte[] open() {
        return this.mEuiccAccess.open();
    }

    public byte[] openChannel(byte[] bArr) {
        return this.mEuiccAccess.openChannel(bArr);
    }

    public byte[] send(byte[] bArr) {
        return this.mEuiccAccess.send(bArr);
    }

    public void close() {
        this.mEuiccAccess.close();
    }

    public byte[] getATRInfo() {
        return this.mEuiccAccess.getAtr();
    }
}
