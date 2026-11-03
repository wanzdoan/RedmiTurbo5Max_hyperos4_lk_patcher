package com.android.internal.telephony.emergency;

/* JADX INFO: loaded from: classes.dex */
public class EmergencyConstants {
    public static final int MODE_EMERGENCY_CALLBACK = 3;
    public static final int MODE_EMERGENCY_NONE = 0;
    public static final int MODE_EMERGENCY_WLAN = 2;
    public static final int MODE_EMERGENCY_WWAN = 1;

    public static String emergencyModeToString(int i) {
        if (i == 0) {
            return "NONE";
        }
        if (i == 1) {
            return "WWAN";
        }
        if (i == 2) {
            return "WLAN";
        }
        if (i == 3) {
            return "CALLBACK";
        }
        return "UNKNOWN(" + i + ")";
    }
}
