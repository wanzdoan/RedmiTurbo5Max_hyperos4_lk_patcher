package com.android.services.telephony.relay.phonecall.message.api;

/* JADX INFO: loaded from: classes.dex */
public enum RelayApi {
    RING("ring"),
    TRANSFER("transfer"),
    UPDATE_CALL_STATE("update_call_state"),
    RELEASE("release"),
    OPERATE("operate"),
    UPDATE_CALL_INFO("update_call_info"),
    DIAL("dial"),
    CALL_STATE_IDLE("call_state_idle"),
    WANT_RELAY("wantRelay");

    private final String mMethodName;

    RelayApi(String str) {
        this.mMethodName = str;
    }

    public String getMethodName() {
        return this.mMethodName;
    }
}
