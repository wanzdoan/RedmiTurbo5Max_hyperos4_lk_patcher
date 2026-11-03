package com.android.services.telephony.relay.phonecall.message.response;

/* JADX INFO: loaded from: classes.dex */
public class RelayResponseDial extends ResponseObject {
    public String hostDeviceName;

    public RelayResponseDial(ResponseObject responseObject, String str) {
        super(responseObject.code, responseObject.msg, responseObject.address);
        this.hostDeviceName = str;
    }
}
