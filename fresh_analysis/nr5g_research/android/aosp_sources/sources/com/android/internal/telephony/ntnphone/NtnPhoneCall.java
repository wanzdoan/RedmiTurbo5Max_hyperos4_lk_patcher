package com.android.internal.telephony.ntnphone;

import com.android.internal.telephony.Call;
import com.android.internal.telephony.Call$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class NtnPhoneCall extends Call {
    NtnPhoneCallTracker mOwner;

    public NtnPhoneCall(NtnPhoneCallTracker ntnPhoneCallTracker) {
        this.mOwner = ntnPhoneCallTracker;
    }

    @Override // com.android.internal.telephony.Call
    public Phone getPhone() {
        return this.mOwner.getPhone();
    }

    @Override // com.android.internal.telephony.Call
    public boolean isMultiparty() {
        return getConnectionsCount() > 1;
    }

    @Override // com.android.internal.telephony.Call
    public void hangup() throws CallStateException {
        this.mOwner.hangup(this);
    }

    @Override // com.android.internal.telephony.Call
    public void hangup(int i) throws CallStateException {
        this.mOwner.hangup(this);
    }

    public String toString() {
        return this.mState.toString();
    }

    public void attach(Connection connection, NtnDriverCall ntnDriverCall) {
        addConnection(connection);
        this.mState = stateFromDCStateForNtn(ntnDriverCall.state);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.ntnphone.NtnPhoneCall$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$ntnphone$NtnDriverCall$State;

        static {
            int[] iArr = new int[NtnDriverCall.State.values().length];
            $SwitchMap$com$android$internal$telephony$ntnphone$NtnDriverCall$State = iArr;
            try {
                iArr[NtnDriverCall.State.ACTIVE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnDriverCall$State[NtnDriverCall.State.HOLDING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnDriverCall$State[NtnDriverCall.State.DIALING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnDriverCall$State[NtnDriverCall.State.ALERTING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnDriverCall$State[NtnDriverCall.State.INCOMING.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnDriverCall$State[NtnDriverCall.State.WAITING.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnDriverCall$State[NtnDriverCall.State.DISCONNECTED_LOCAL.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnDriverCall$State[NtnDriverCall.State.DISCONNECTED_REMOTE.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }

    public static Call.State stateFromDCStateForNtn(NtnDriverCall.State state) {
        switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$ntnphone$NtnDriverCall$State[state.ordinal()]) {
            case 1:
                return Call.State.ACTIVE;
            case 2:
                return Call.State.HOLDING;
            case 3:
                return Call.State.DIALING;
            case 4:
                return Call.State.ALERTING;
            case 5:
                return Call.State.INCOMING;
            case 6:
                return Call.State.WAITING;
            case 7:
            case 8:
                return Call.State.DISCONNECTED;
            default:
                Call$$ExternalSyntheticBUOutline0.m("illegal call state:", state);
                return null;
        }
    }

    public void attachFake(Connection connection, Call.State state) {
        addConnection(connection);
        this.mState = state;
    }

    public boolean connectionDisconnected(NtnPhoneConnection ntnPhoneConnection) {
        if (this.mState != Call.State.DISCONNECTED) {
            ArrayList<Connection> connections = getConnections();
            int size = connections.size();
            int i = 0;
            while (i < size) {
                Connection connection = connections.get(i);
                i++;
                if (connection.getState() != Call.State.DISCONNECTED) {
                }
            }
            this.mState = Call.State.DISCONNECTED;
            return true;
        }
        return false;
    }

    public void detach(NtnPhoneConnection ntnPhoneConnection) {
        removeConnection(ntnPhoneConnection);
        if (getConnectionsCount() == 0) {
            this.mState = Call.State.IDLE;
        }
    }

    boolean update(NtnPhoneConnection ntnPhoneConnection, NtnDriverCall ntnDriverCall) {
        Call.State stateStateFromDCStateForNtn = stateFromDCStateForNtn(ntnDriverCall.state);
        if (stateStateFromDCStateForNtn == this.mState) {
            return false;
        }
        this.mState = stateStateFromDCStateForNtn;
        return true;
    }

    boolean isFull() {
        return getConnectionsCount() == this.mOwner.getMaxConnectionsPerCall();
    }

    public void onHangupLocal() {
        if (this.mState.isAlive()) {
            ArrayList<Connection> connections = getConnections();
            int size = connections.size();
            int i = 0;
            while (i < size) {
                Connection connection = connections.get(i);
                i++;
                ((NtnPhoneConnection) connection).onHangupLocal();
            }
            this.mState = Call.State.DISCONNECTING;
        }
    }
}
