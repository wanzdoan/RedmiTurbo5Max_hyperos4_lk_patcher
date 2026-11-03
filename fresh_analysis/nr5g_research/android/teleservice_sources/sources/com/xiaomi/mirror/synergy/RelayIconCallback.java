package com.xiaomi.mirror.synergy;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface RelayIconCallback {
    void disableRelayIcon(boolean z, List list);

    void onIconHide();

    void onIconListUpdate(List list);

    void onIconShow();

    void onRelayComplete(int i);

    void onRelayError(int i, String str);
}
