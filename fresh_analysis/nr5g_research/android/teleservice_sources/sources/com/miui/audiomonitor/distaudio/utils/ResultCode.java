package com.miui.audiomonitor.distaudio.utils;

/* JADX INFO: loaded from: classes.dex */
public enum ResultCode {
    RESULT_OK(0, "success"),
    RESULT_DEVICE_CONNECT_FAILED(-1001, "connect device failed");

    private int code;
    private String msg;

    ResultCode(int i, String str) {
        this.code = i;
        this.msg = str;
    }

    public int getCode() {
        return this.code;
    }
}
