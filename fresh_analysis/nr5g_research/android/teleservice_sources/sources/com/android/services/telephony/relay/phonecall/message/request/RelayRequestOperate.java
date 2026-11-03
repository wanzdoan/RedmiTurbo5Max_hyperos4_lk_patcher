package com.android.services.telephony.relay.phonecall.message.request;

/* JADX INFO: loaded from: classes.dex */
public class RelayRequestOperate extends RelayRequestBase {
    public static final int OPERATE_ANSWER = 0;
    public static final int OPERATE_DISCONNECT = 2;
    public static final int OPERATE_HOLD = 3;
    public static final int OPERATE_MUTE = 5;
    public static final int OPERATE_REJECT = 1;
    public static final int OPERATE_UNHOLD = 4;
    public int operateType;
}
