package com.xiaomi.modem.modemipc;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public class ModemIpcCallbackInfo {
    public String mBcName;
    public byte[] mRequest;
    public int mSub;

    public ModemIpcCallbackInfo(String bcName, int sub, byte[] request) {
        this.mBcName = bcName;
        this.mSub = sub;
        this.mRequest = request;
    }
}
