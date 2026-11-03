package com.android.phone.statistics.utils;

import android.telephony.MiuiSignalStrengthStub;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirror.synergy.MiuiSynergySdk;

/* JADX INFO: loaded from: classes.dex */
public abstract class Constants {
    public static final int MIUI_NUM_SIGNAL_STRENGTH_BARS;
    public static final int NUM_LTE_RSRP_BIN;
    public static final int[] RSRP_LENIENT;
    public static final int[] SCENE_RSRP_LENIENT;
    public static final int[] SCENE_RSRQ_LENIENT;
    public static final int[] SCENE_SNR_LENIENT;
    public static final int[] SNR_LENIENT;
    public static final int[] SSRSRP_THRESH;

    static {
        int i = MiuiSignalStrengthStub.MIUI_NUM_SIGNAL_STRENGTH_BARS;
        MIUI_NUM_SIGNAL_STRENGTH_BARS = i;
        NUM_LTE_RSRP_BIN = i + 1;
        RSRP_LENIENT = MiuiSignalStrengthStub.getSignalDisplayThresholds(3, 3, new int[]{-125, -115, -110, -105});
        SSRSRP_THRESH = MiuiSignalStrengthStub.getSignalDisplayThresholds(6, 6, new int[]{-125, -115, -110, -105});
        SNR_LENIENT = new int[]{130, 45, 10, -30, -200};
        SCENE_RSRP_LENIENT = new int[]{-140, -130, -125, -120, -115, -110, -106, MiuiSynergySdk.SEND_APP_FAIL_REMOTE_BUSY_MIRROR, -95, -44};
        SCENE_SNR_LENIENT = new int[]{-200, -100, -30, -10, 0, 10, 45, 130, MiRilHook.HOOK_ID_START_URC_BY_AT};
        SCENE_RSRQ_LENIENT = new int[]{-20, -18, -15, -11, -7, -3};
    }
}
