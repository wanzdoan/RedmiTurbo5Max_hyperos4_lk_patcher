package com.android.phone.networkmode;

/* JADX INFO: loaded from: classes.dex */
public class NetworkModeHandlerMtkDual4G extends NetworkModeHandlerAllSlot {
    @Override // com.android.phone.networkmode.NetworkModeHandlerBase
    public void onDataSlotReady(boolean z) {
        removeMessages(0);
        sendEmptyMessageDelayed(0, 100L);
    }
}
