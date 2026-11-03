package com.android.services.telephony.relay.phonecall.message.request;

/* JADX INFO: loaded from: classes.dex */
public class RelayRequestRelease extends RelayRequestBase {
    public static final int RELEASE_LOCAL_ANSWERED = 0;
    public static final int RELEASE_OTHER_RELAY_ANSWERED = 3;
    public static final int RELEASE_OTHER_RELAY_REJECTED = 4;
    public static final int RELEASE_RELAY_END = 7;
    public static final int RELEASE_RELAY_NOT_SUPPORTED = 5;
    public static final int RELEASE_RELAY_UPGRADE_VIDEO = 6;
    public static final int RELEASE_SWITCH_PHONE_AUDIO = 1;
    public static final int RELEASE_TRANSFER_RELAY = 2;
    public int releaseType;

    public static String getDisconnectCause(int i) {
        switch (i) {
            case 0:
                return "Answered on the local phone";
            case 1:
                return "Switch to the phone audio channel";
            case 2:
                return "Transfer to other device";
            case 3:
                return "Answered by other relay device";
            case 4:
                return "Rejected by other relay device";
            case 5:
                return "Relay is not supported";
            case 6:
                return "Upgrade video";
            case 7:
                return "Relay end";
            default:
                return "Unknown";
        }
    }
}
