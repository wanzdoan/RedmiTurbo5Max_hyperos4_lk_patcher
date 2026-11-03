package com.xiaomi.dist.statusbar;

import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class ConnectionStatus {
    private static final String[] TYPE_NAMES = {"unknown", "phone", "pad", "tv", "pc", "car", "wireless_headset", "sound_box", "sound_box_group"};
    final String deviceId;
    final String deviceName;
    final int deviceType;
    final String deviceTypeName;
    final int status;
    final String tag;

    public ConnectionStatus(String str, String str2, int i, String str3, int i2, String str4) {
        this.deviceId = str;
        Objects.requireNonNull(str);
        this.deviceName = str2;
        Objects.requireNonNull(str2);
        this.deviceTypeName = str3;
        Objects.requireNonNull(str3);
        this.tag = str4;
        Objects.requireNonNull(str4);
        this.deviceType = i;
        this.status = i2;
    }

    public static String getDeviceTypeName(int i) {
        if (i >= 0 && i <= 8) {
            return TYPE_NAMES[i];
        }
        return TYPE_NAMES[0];
    }
}
