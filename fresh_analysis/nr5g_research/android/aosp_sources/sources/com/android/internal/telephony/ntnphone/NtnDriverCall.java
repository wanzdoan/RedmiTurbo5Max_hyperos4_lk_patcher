package com.android.internal.telephony.ntnphone;

import com.android.internal.telephony.ATParseEx;
import com.android.internal.telephony.DriverCall;

/* JADX INFO: loaded from: classes.dex */
public class NtnDriverCall extends DriverCall {
    public State state;

    public enum State {
        ACTIVE,
        HOLDING,
        DIALING,
        ALERTING,
        INCOMING,
        WAITING,
        DISCONNECTED_LOCAL,
        DISCONNECTED_REMOTE
    }

    public static State stateFromCLCCForNtn(int i) throws ATParseEx {
        switch (i) {
            case 0:
                return State.ACTIVE;
            case 1:
                return State.HOLDING;
            case 2:
                return State.DIALING;
            case 3:
                return State.ALERTING;
            case 4:
                return State.INCOMING;
            case 5:
                return State.WAITING;
            case 6:
                return State.DISCONNECTED_LOCAL;
            case 7:
                return State.DISCONNECTED_REMOTE;
            default:
                throw new ATParseEx("illegal call state " + i);
        }
    }

    @Override // com.android.internal.telephony.DriverCall
    public String toString() {
        StringBuilder sb = new StringBuilder("id=");
        sb.append(this.index);
        sb.append(",");
        sb.append(this.state);
        sb.append(",toa=");
        sb.append(this.TOA);
        sb.append(",");
        sb.append(this.isMpty ? "conf" : "norm");
        sb.append(",");
        sb.append(this.isMT ? "mt" : "mo");
        sb.append(",");
        sb.append(this.als);
        sb.append(",");
        sb.append(this.isVoice ? "voc" : "nonvoc");
        sb.append(",");
        sb.append(this.isVoicePrivacy ? "evp" : "noevp");
        sb.append(",,cli=");
        sb.append(this.numberPresentation);
        sb.append(",,");
        sb.append(this.namePresentation);
        sb.append(",audioQuality=");
        sb.append(this.audioQuality);
        return sb.toString();
    }
}
