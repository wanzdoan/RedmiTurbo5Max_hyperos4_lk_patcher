package com.xiaomi.distaudiokit;

import com.miui.audiomonitor.distaudio.data.DistAudioDeviceInfo;

/* JADX INFO: loaded from: classes.dex */
public interface DistAudioCallback {
    void onConnectionStatus(DistAudioDeviceInfo distAudioDeviceInfo, int i, String str);

    void onDistAudioDeviceAdded(DistAudioDeviceInfo distAudioDeviceInfo);

    void onDistAudioDeviceRemoved(DistAudioDeviceInfo distAudioDeviceInfo);
}
