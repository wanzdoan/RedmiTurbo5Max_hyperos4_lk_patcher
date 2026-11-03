package com.android.services.telephony;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiCallStateMonitor {
    public static MiuiCallStateMonitor getIntance() {
        return null;
    }

    public static void setCallCommandMessage(String str, int i, int i2, int i3, boolean z) {
        getIntance();
    }

    public static void setCallCommandMessage(String str, int i, int i2, int i3) {
        setCallCommandMessage(str, i, i2, i3, false);
    }
}
