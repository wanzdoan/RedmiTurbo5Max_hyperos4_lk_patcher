package com.xiaomi.mirror.synergy;

import com.xiaomi.mirror.MirrorDesktopInfo;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface MirrorDesktopCallback {
    void onDeviceListUpdate(List list);

    void onMirrorDeskOpenOrClose(boolean z);

    void onMirrorDesktopConfigChanged(MirrorDesktopInfo mirrorDesktopInfo);

    void onMirrorDesktopHide();

    void onMirrorDesktopIconChanged(boolean z);

    void onMirrorDesktopShow(MirrorDesktopInfo mirrorDesktopInfo);

    void onMirrorSeatChanged(int i);
}
