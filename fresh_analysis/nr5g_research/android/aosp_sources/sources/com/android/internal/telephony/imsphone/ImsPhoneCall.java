package com.android.internal.telephony.imsphone;

import android.telephony.ims.ImsStreamMediaProfile;
import com.android.ims.ImsCall;
import com.android.ims.ImsException;
import com.android.ims.internal.ConferenceParticipant;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ImsPhoneCall extends Call {
    public static final String CONTEXT_BACKGROUND = "BG";
    public static final String CONTEXT_FOREGROUND = "FG";
    public static final String CONTEXT_HANDOVER = "HO";
    public static final String CONTEXT_RINGING = "RG";
    public static final String CONTEXT_UNKNOWN = "UK";
    private static final boolean DBG = Rlog.isLoggable("ImsPhoneCall", 3);
    private static final boolean VDBG = Rlog.isLoggable("ImsPhoneCall", 2);
    private final String mCallContext;
    protected boolean mIsRingbackTonePlaying;
    protected ImsPhoneCallTracker mOwner;

    protected ImsPhoneCall() {
        this.mIsRingbackTonePlaying = false;
        this.mCallContext = CONTEXT_UNKNOWN;
    }

    public ImsPhoneCall(ImsPhoneCallTracker imsPhoneCallTracker, String str) {
        this.mIsRingbackTonePlaying = false;
        this.mOwner = imsPhoneCallTracker;
        this.mCallContext = str;
    }

    public void dispose() {
        int i = 0;
        try {
            this.mOwner.hangup(this);
        } catch (CallStateException unused) {
        } finally {
            ArrayList<Connection> connections = getConnections();
            int size = connections.size();
            while (i < size) {
                Connection connection = connections.get(i);
                i++;
                connection.onDisconnect(14);
            }
        }
    }

    @Override // com.android.internal.telephony.Call
    public ArrayList<Connection> getConnections() {
        return super.getConnections();
    }

    @Override // com.android.internal.telephony.Call
    public Phone getPhone() {
        return this.mOwner.getPhone();
    }

    @Override // com.android.internal.telephony.Call
    public boolean isMultiparty() {
        ImsCall imsCall = getImsCall();
        if (imsCall == null) {
            return false;
        }
        return imsCall.isMultiparty();
    }

    @Override // com.android.internal.telephony.Call
    public void hangup() throws CallStateException {
        this.mOwner.hangup(this);
    }

    @Override // com.android.internal.telephony.Call
    public void hangup(int i) throws CallStateException {
        this.mOwner.hangup(this, i);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[ImsPhoneCall ");
        ArrayList<Connection> connections = getConnections();
        sb.append(this.mCallContext);
        sb.append(" state: ");
        sb.append(this.mState.toString());
        sb.append(" ");
        if (connections.size() > 1) {
            sb.append(" ERROR_MULTIPLE ");
        }
        int size = connections.size();
        int i = 0;
        while (i < size) {
            Connection connection = connections.get(i);
            i++;
            sb.append(connection);
            sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override // com.android.internal.telephony.Call
    public List<ConferenceParticipant> getConferenceParticipants() {
        ImsCall imsCall;
        if (this.mOwner.isConferenceEventPackageEnabled() && (imsCall = getImsCall()) != null) {
            return imsCall.getConferenceParticipants();
        }
        return null;
    }

    public void attach(Connection connection) {
        if (VDBG) {
            Rlog.v("ImsPhoneCall", "attach : " + this.mCallContext + " conn = " + connection);
        }
        clearDisconnected();
        addConnection(connection);
        this.mOwner.logState();
    }

    public void attach(Connection connection, Call.State state) {
        if (VDBG) {
            Rlog.v("ImsPhoneCall", "attach : " + this.mCallContext + " state = " + state.toString());
        }
        attach(connection);
        this.mState = state;
    }

    public void attachFake(Connection connection, Call.State state) {
        attach(connection, state);
    }

    public boolean connectionDisconnected(ImsPhoneConnection imsPhoneConnection) {
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
            synchronized (this) {
                this.mState = Call.State.DISCONNECTED;
            }
            if (!VDBG) {
                return true;
            }
            Rlog.v("ImsPhoneCall", "connectionDisconnected : " + this.mCallContext + " state = " + this.mState);
            return true;
        }
        return false;
    }

    public void detach(ImsPhoneConnection imsPhoneConnection) {
        if (VDBG) {
            Rlog.v("ImsPhoneCall", "detach : " + this.mCallContext + " conn = " + imsPhoneConnection);
        }
        removeConnection(imsPhoneConnection);
        clearDisconnected();
        this.mOwner.logState();
    }

    boolean isFull() {
        return getConnectionsCount() == 5;
    }

    public void onHangupLocal() {
        ArrayList<Connection> connections = getConnections();
        int size = connections.size();
        int i = 0;
        while (i < size) {
            Connection connection = connections.get(i);
            i++;
            ((ImsPhoneConnection) connection).onHangupLocal();
        }
        synchronized (this) {
            try {
                if (this.mState.isAlive()) {
                    this.mState = Call.State.DISCONNECTING;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (VDBG) {
            Rlog.v("ImsPhoneCall", "onHangupLocal : " + this.mCallContext + " state = " + this.mState);
        }
    }

    public ImsPhoneConnection getFirstConnection() {
        ArrayList<Connection> connections = getConnections();
        if (connections.size() == 0) {
            return null;
        }
        return (ImsPhoneConnection) connections.get(0);
    }

    public void setMute(boolean z) {
        ImsPhoneConnection firstConnection = getFirstConnection();
        ImsCall imsCall = firstConnection == null ? null : firstConnection.getImsCall();
        if (imsCall != null) {
            try {
                imsCall.setMute(z);
            } catch (ImsException e) {
                Rlog.e("ImsPhoneCall", "setMute failed : " + e.getMessage());
            }
        }
    }

    public void merge(ImsPhoneCall imsPhoneCall, Call.State state) {
        ImsPhoneConnection firstConnection = getFirstConnection();
        if (firstConnection != null) {
            long conferenceConnectTime = firstConnection.getConferenceConnectTime();
            if (conferenceConnectTime > 0) {
                firstConnection.setConnectTime(conferenceConnectTime);
                firstConnection.setConnectTimeReal(firstConnection.getConnectTimeReal());
            } else if (DBG) {
                Rlog.d("ImsPhoneCall", "merge: conference connect time is 0");
            }
        }
        if (DBG) {
            Rlog.d("ImsPhoneCall", "merge(" + this.mCallContext + "): " + imsPhoneCall + "state = " + state);
        }
    }

    public ImsCall getImsCall() {
        ImsPhoneConnection firstConnection = getFirstConnection();
        if (firstConnection == null) {
            return null;
        }
        return firstConnection.getImsCall();
    }

    public String getCallSessionId() {
        if ((getImsCall() == null ? null : getImsCall().getSession()) == null) {
            return null;
        }
        return getImsCall().getSession().getCallId();
    }

    public int getServiceType() {
        if (getFirstConnection() == null) {
            return 0;
        }
        return getFirstConnection().isEmergencyCall() ? 2 : 1;
    }

    public int getCallType() {
        if (getImsCall() == null) {
            return 0;
        }
        return getImsCall().isVideoCall() ? 4 : 2;
    }

    public static boolean isLocalTone(ImsCall imsCall) {
        boolean z = false;
        if (imsCall != null && imsCall.getCallProfile() != null && imsCall.getCallProfile().mMediaProfile != null) {
            ImsStreamMediaProfile imsStreamMediaProfile = imsCall.getCallProfile().mMediaProfile;
            z = imsStreamMediaProfile.mAudioDirection == 0;
            Rlog.i("ImsPhoneCall", "isLocalTone: audioDirection=" + imsStreamMediaProfile.mAudioDirection + ", playRingback=" + z);
        }
        return z;
    }

    public boolean update(ImsPhoneConnection imsPhoneConnection, ImsCall imsCall, Call.State state) {
        Call.State state2 = this.mState;
        maybeChangeRingbackState(imsCall, state);
        boolean z = true;
        if (state != this.mState && state != Call.State.DISCONNECTED) {
            this.mState = state;
        } else if (state != Call.State.DISCONNECTED) {
            z = false;
        }
        if (VDBG) {
            Rlog.v("ImsPhoneCall", "update : " + this.mCallContext + " state: " + state2 + " --> " + this.mState);
        }
        return z;
    }

    public void maybeChangeRingbackState(ImsCall imsCall) {
        maybeChangeRingbackState(imsCall, this.mState);
    }

    private void maybeChangeRingbackState(ImsCall imsCall, Call.State state) {
        Rlog.i("ImsPhoneCall", "maybeChangeRingbackState: state=" + state);
        Call.State state2 = Call.State.ALERTING;
        boolean z = this.mIsRingbackTonePlaying;
        if (state != state2) {
            if (z) {
                Rlog.i("ImsPhoneCall", "maybeChangeRingbackState: stop ringback");
                getPhone().stopRingbackTone();
                this.mIsRingbackTonePlaying = false;
                return;
            }
            return;
        }
        if (z && !isLocalTone(imsCall)) {
            Rlog.i("ImsPhoneCall", "maybeChangeRingbackState: stop ringback");
            getPhone().stopRingbackTone();
            this.mIsRingbackTonePlaying = false;
        } else {
            if (this.mIsRingbackTonePlaying || !isLocalTone(imsCall)) {
                return;
            }
            Rlog.i("ImsPhoneCall", "maybeChangeRingbackState: start ringback");
            getPhone().startRingbackTone();
            this.mIsRingbackTonePlaying = true;
        }
    }

    public void switchWith(ImsPhoneCall imsPhoneCall) {
        if (VDBG) {
            Rlog.v("ImsPhoneCall", "switchWith : switchCall = " + this + " withCall = " + imsPhoneCall);
        }
        synchronized (ImsPhoneCall.class) {
            ImsPhoneCall imsPhoneCallMakeTempImsPhoneCall = makeTempImsPhoneCall();
            imsPhoneCallMakeTempImsPhoneCall.takeOver(this);
            takeOver(imsPhoneCall);
            imsPhoneCall.takeOver(imsPhoneCallMakeTempImsPhoneCall);
        }
        this.mOwner.logState();
    }

    public void maybeStopRingback() {
        if (this.mIsRingbackTonePlaying) {
            getPhone().stopRingbackTone();
            this.mIsRingbackTonePlaying = false;
        }
    }

    public boolean isRingbackTonePlaying() {
        return this.mIsRingbackTonePlaying;
    }

    public void maybeClearRemotelyHeldStatus() {
        ArrayList<Connection> connections = getConnections();
        int size = connections.size();
        int i = 0;
        while (i < size) {
            Connection connection = connections.get(i);
            i++;
            ImsPhoneConnection imsPhoneConnection = (ImsPhoneConnection) connection;
            if (imsPhoneConnection.isHeldByRemote()) {
                imsPhoneConnection.setRemotelyUnheld();
            }
        }
    }

    public void takeOver(ImsPhoneCall imsPhoneCall) {
        copyConnectionFrom(imsPhoneCall);
        this.mState = imsPhoneCall.mState;
        ArrayList<Connection> connections = getConnections();
        int size = connections.size();
        int i = 0;
        while (i < size) {
            Connection connection = connections.get(i);
            i++;
            ((ImsPhoneConnection) connection).changeParent(this);
        }
    }

    protected ImsPhoneCall makeTempImsPhoneCall() {
        return new ImsPhoneCall();
    }
}
