package com.android.services.telephony.relay.phonecall.message.response;

import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.android.services.telephony.relay.phonecall.message.GsonUtil;
import com.xiaomi.continuity.networking.NetworkingManager;

/* JADX INFO: loaded from: classes.dex */
public class ResponseObject {
    public static final String DEFAULT_MESSAGE_FAILED = "error";
    public static final String DEFAULT_MESSAGE_SUCCEED = "ok";
    public static final String DEFAULT_MESSAGE_TIMEOUT = "timeout";
    public static final int RELAY_FAILED_CODE = 500;
    public static final int RELAY_SUCCESS_CODE = 200;
    public static final int RELAY_TIMEOUT_CODE = 408;
    public final String address;
    public int callstate;
    public int code;
    public String msg;
    public final String responseDeviceId;

    public ResponseObject(int i, String str, String str2) {
        this.responseDeviceId = NetworkingManager.getInstance(RelayUtils.System.getContext()).getLocalDeviceInfo().getDeviceId();
        this.callstate = -1;
        this.code = i;
        this.msg = str;
        this.address = str2;
    }

    public ResponseObject(int i, String str, String str2, int i2) {
        this.responseDeviceId = NetworkingManager.getInstance(RelayUtils.System.getContext()).getLocalDeviceInfo().getDeviceId();
        this.code = i;
        this.msg = str;
        this.address = str2;
        this.callstate = i2;
    }

    public String toJson() {
        return GsonUtil.toJson(this);
    }

    public static ResponseObject getDefaultSuccessResponse(String str) {
        return new ResponseObject(200, DEFAULT_MESSAGE_SUCCEED, str);
    }

    public static ResponseObject getDefaultFailedResponse(String str) {
        return new ResponseObject(RELAY_FAILED_CODE, DEFAULT_MESSAGE_FAILED, str);
    }

    public static ResponseObject getDefaultTimeoutResponse(String str) {
        return new ResponseObject(RELAY_TIMEOUT_CODE, DEFAULT_MESSAGE_TIMEOUT, str);
    }

    public static ResponseObject getDefaultSuccessResponseWithCallState(String str, int i) {
        return new ResponseObject(200, DEFAULT_MESSAGE_SUCCEED, str, i);
    }

    public static ResponseObject getDefaultFailedResponseWithCallState(String str, int i) {
        return new ResponseObject(RELAY_FAILED_CODE, DEFAULT_MESSAGE_FAILED, str, i);
    }

    public static ResponseObject getDefaultTimeoutResponseWithCallState(String str, int i) {
        return new ResponseObject(RELAY_TIMEOUT_CODE, DEFAULT_MESSAGE_TIMEOUT, str, i);
    }
}
