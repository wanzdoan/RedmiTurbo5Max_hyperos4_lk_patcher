package com.android.internal.telephony.metrics;

import com.android.internal.telephony.TelephonyStatsLog;

/* JADX INFO: loaded from: classes.dex */
public class RadioPowerStateStats {
    private static int radioPowerStateToProtoEnum(int i) {
        if (i == 0) {
            return 1;
        }
        if (i != 1) {
            return i != 2 ? 0 : 3;
        }
        return 2;
    }

    public static void onRadioStateChanged(int i) {
        TelephonyStatsLog.write(713, radioPowerStateToProtoEnum(i));
    }
}
