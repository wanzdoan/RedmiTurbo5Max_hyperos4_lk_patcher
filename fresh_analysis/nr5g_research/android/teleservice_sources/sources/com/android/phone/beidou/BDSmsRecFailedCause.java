package com.android.phone.beidou;

/* JADX INFO: loaded from: classes.dex */
public enum BDSmsRecFailedCause {
    UNKNOWN(0),
    FAILED_TRACK_RD(1),
    ACK_TIMEOUT(2),
    DOWN_MAIL_REPLY_TIMEOUT(3),
    FAILED_DECRYPT(4);

    private final int value;

    BDSmsRecFailedCause(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }

    public static BDSmsRecFailedCause getByValue(int i) {
        for (BDSmsRecFailedCause bDSmsRecFailedCause : values()) {
            if (bDSmsRecFailedCause.getValue() == i) {
                return bDSmsRecFailedCause;
            }
        }
        return UNKNOWN;
    }
}
