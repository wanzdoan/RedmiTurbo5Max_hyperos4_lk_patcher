package com.nxp.osu;

import com.xiaomi.internal.telephony.DynamicSarService;

/* JADX INFO: loaded from: classes.dex */
public enum SecOsuStatusCode {
    OSU_STATUS_SUCCESS(0, "SUCCESS"),
    OSU_STATUS_FAILED(1, "FAILED"),
    OSU_STATUS_BUSY(2, "BUSY"),
    OSU_STATUS_UNKNOWN(15, "UNKNOWN ERROR"),
    OSU_STATUS_INVALID_UPDATE(3, "INVALID UPDATE"),
    OSU_STATUS_ALREADY_UPDATED(4, "ALREADY UPDATED"),
    OSU_STATUS_SHUTTING_DOWN(5, "SHUTTING_DOWN"),
    OSU_STATUS_NO_MEM(6, "OUT OF MEMORY"),
    OSU_STATUS_PRECONDITION_FAILED(7, "PRECONDITION FAILURE"),
    OSU_STATUS_GETSTEP_FAILED(8, "GET STEP FAILURE"),
    OSU_STATUS_GET_DATA_FAILED(9, "GET DATA FAILED"),
    OSU_STATUS_FILE_NOT_FOUND(3, "FILE NOT FOUND"),
    OSU_STATUS_INVALID_FILE(4, "Invalid File"),
    OSU_STATUS_INCOMPLETE_UPDATE(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY, "INCOMPLETE UPDATE");

    private final String mMsg;
    private byte[] mResponse = null;
    private final int mValue;

    SecOsuStatusCode(int i, String str) {
        this.mValue = i;
        this.mMsg = str;
    }

    public int getValue() {
        return this.mValue;
    }

    public String getMessage() {
        return this.mMsg;
    }

    public void setResponseCode(byte[] bArr) {
        this.mResponse = bArr;
    }

    public byte[] getResponseCode() {
        return this.mResponse;
    }
}
