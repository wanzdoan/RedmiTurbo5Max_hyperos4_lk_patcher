package com.nxp.osu;

import com.nxp.id.tp.common.HexString;

/* JADX INFO: loaded from: classes.dex */
public class OSUInfo {
    private byte[] configureId;
    private String fileName;
    private UAIQueryResponse uaiInfo;

    OSUInfo(String str, byte[] bArr, UAIQueryResponse uAIQueryResponse) {
        this.fileName = str;
        this.configureId = bArr;
        this.uaiInfo = uAIQueryResponse;
    }

    public String getFileName() {
        return this.fileName;
    }

    public UAIQueryResponse getOSInfo() {
        return this.uaiInfo;
    }

    public String getConfigurationIdHexValue() {
        if (this.configureId == null) {
            return null;
        }
        return HexString.fromByteArray(this.configureId, 0) + "";
    }
}
