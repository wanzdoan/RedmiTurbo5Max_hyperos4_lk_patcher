package com.android.phone.beidou;

/* JADX INFO: loaded from: classes.dex */
public enum BDSmsSendFailedCause {
    UNKNOWN(0),
    INVALID_INPUT(1),
    INVALID_SENDER_NUMBER(2),
    SYSTEM_RES_UNREADY(3),
    OTHER_SYSTEM_CLOSED(4),
    OWNER_SEND_FAILED(5),
    USER_CLOSED(6),
    DISABLE_SEND(7),
    NEAR_THRESHOLD(55);

    private final int value;

    BDSmsSendFailedCause(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }

    public static BDSmsSendFailedCause getByValue(int i) {
        for (BDSmsSendFailedCause bDSmsSendFailedCause : values()) {
            if (bDSmsSendFailedCause.getValue() == i) {
                return bDSmsSendFailedCause;
            }
        }
        return null;
    }
}
