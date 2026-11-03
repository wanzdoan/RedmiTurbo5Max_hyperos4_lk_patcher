package com.android.phone.beidou;

import com.xiaomi.internal.telephony.DynamicSarService;

/* JADX INFO: loaded from: classes.dex */
public enum BDSmsSendStatus {
    SEND_STATUS_UNKNOWN(-1),
    SEND_STATUS_SUCC(0),
    SEND_STATUS_SENDING(32),
    SEND_STATUS_FAIL(64),
    SEND_STATUS_SENT(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);

    private final int value;

    BDSmsSendStatus(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }

    public static BDSmsSendStatus getByValue(int i) {
        for (BDSmsSendStatus bDSmsSendStatus : values()) {
            if (bDSmsSendStatus.getValue() == i) {
                return bDSmsSendStatus;
            }
        }
        return SEND_STATUS_UNKNOWN;
    }
}
