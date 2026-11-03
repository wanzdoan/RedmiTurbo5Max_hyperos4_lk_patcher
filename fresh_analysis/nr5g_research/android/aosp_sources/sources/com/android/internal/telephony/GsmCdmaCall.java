package com.android.internal.telephony;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class GsmCdmaCall extends Call {
    GsmCdmaCallTracker mOwner;

    public GsmCdmaCall(GsmCdmaCallTracker gsmCdmaCallTracker) {
        this.mOwner = gsmCdmaCallTracker;
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

    public void attach(Connection connection, DriverCall driverCall) {
        addConnection(connection);
        this.mState = Call.stateFromDCState(driverCall.state);
    }

    public void attachFake(Connection connection, Call.State state) {
        addConnection(connection);
        this.mState = state;
    }

    public boolean connectionDisconnected(GsmCdmaConnection gsmCdmaConnection) {
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

    public void detach(GsmCdmaConnection gsmCdmaConnection) {
        removeConnection(gsmCdmaConnection);
        if (getConnectionsCount() == 0) {
            this.mState = Call.State.IDLE;
        }
    }

    boolean update(GsmCdmaConnection gsmCdmaConnection, DriverCall driverCall) {
        Call.State stateStateFromDCState = Call.stateFromDCState(driverCall.state);
        if (stateStateFromDCState == this.mState) {
            return false;
        }
        this.mState = stateStateFromDCState;
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
                ((GsmCdmaConnection) connection).onHangupLocal();
            }
            this.mState = Call.State.DISCONNECTING;
        }
    }
}
