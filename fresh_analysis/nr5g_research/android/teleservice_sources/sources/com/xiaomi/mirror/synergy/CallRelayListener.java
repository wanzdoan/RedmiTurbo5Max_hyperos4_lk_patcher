package com.xiaomi.mirror.synergy;

/* JADX INFO: loaded from: classes2.dex */
public interface CallRelayListener {
    void onAddressUpdate(String str);

    void onLost();

    void onMessage(String str);
}
