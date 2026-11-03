package com.android.phone.beidou;

/* JADX INFO: loaded from: classes.dex */
public enum BDSysStatus {
    SYS_STATUS_ENABLE(1),
    SYS_STATUS_DISABLE(2),
    SYS_STATUS_WAIT_SEARCH_SATELLITE(3),
    SYS_STATUS_SEARCHING_SATELLITE(4),
    SYS_STATUS_SENDING(5),
    SYS_STATUS_RESERVED(6),
    SYS_STATUS_RESOURCE_PREPARE(7),
    SYS_STATUS_WAIT_ACK(8),
    SYS_STATUS_WAIT_MSG_RECEIVE(9);

    private final int value;

    BDSysStatus(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }

    public static BDSysStatus getByValue(int i) {
        for (BDSysStatus bDSysStatus : values()) {
            if (bDSysStatus.getValue() == i) {
                return bDSysStatus;
            }
        }
        return null;
    }
}
