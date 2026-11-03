package com.android.internal.telephony.imsphone;

import android.telephony.AccessNetworkConstants;
import android.telephony.ServiceState;
import com.android.internal.telephony.Call;

/* JADX INFO: loaded from: classes.dex */
public class ImsCallInfo {
    private final int mIndex;
    private ImsPhoneConnection mConnection = null;
    private Call.State mState = Call.State.IDLE;
    private boolean mIsHeldByRemote = false;
    private boolean mShouldIgnoreUpdate = false;
    private int mCallRadioTech = 0;

    public ImsCallInfo(int i) {
        this.mIndex = i;
    }

    public void reset() {
        this.mConnection = null;
        this.mState = Call.State.IDLE;
        this.mIsHeldByRemote = false;
        this.mShouldIgnoreUpdate = false;
        this.mCallRadioTech = 0;
    }

    public void init(ImsPhoneConnection imsPhoneConnection) {
        this.mConnection = imsPhoneConnection;
        this.mState = imsPhoneConnection.getState();
        this.mCallRadioTech = getCallRadioTech(imsPhoneConnection);
        this.mShouldIgnoreUpdate = !isIncoming();
    }

    /* JADX WARN: Code duplicated, block: B:21:0x0033  */
    /* JADX WARN: Code duplicated, block: B:23:0x0039 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:24:0x003a  */
    /* JADX WARN: Code duplicated, block: B:26:0x003d A[RETURN] */
    public boolean update(ImsPhoneConnection imsPhoneConnection, boolean z, boolean z2) {
        Call.State state = imsPhoneConnection.getState();
        int callRadioTech = getCallRadioTech(imsPhoneConnection);
        boolean z3 = (this.mState == state && this.mCallRadioTech == callRadioTech) ? false : true;
        this.mState = state;
        this.mCallRadioTech = callRadioTech;
        if (z && !this.mIsHeldByRemote) {
            this.mIsHeldByRemote = true;
        } else {
            if (z2 && this.mIsHeldByRemote) {
                this.mIsHeldByRemote = false;
            }
            if (shouldIgnoreUpdate()) {
                return z3;
            }
            if (!imsPhoneConnection.isAlive()) {
                return false;
            }
            this.mShouldIgnoreUpdate = false;
            return true;
        }
        z3 = true;
        if (shouldIgnoreUpdate()) {
            return z3;
        }
        if (!imsPhoneConnection.isAlive()) {
            return false;
        }
        this.mShouldIgnoreUpdate = false;
        return true;
    }

    public void onDisconnect() {
        this.mState = Call.State.DISCONNECTED;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public Call.State getCallState() {
        return this.mState;
    }

    public boolean isHeldByRemote() {
        return this.mIsHeldByRemote;
    }

    public boolean isIncoming() {
        return this.mConnection.isIncoming();
    }

    public boolean isEmergencyCall() {
        return this.mConnection.isEmergencyCall();
    }

    public boolean shouldIgnoreUpdate() {
        return this.mShouldIgnoreUpdate;
    }

    public int getCallRadioTech() {
        return this.mCallRadioTech;
    }

    public String toString() {
        return "[ id=" + this.mIndex + ", state=" + this.mState + ", callRadioTech=" + AccessNetworkConstants.AccessNetworkType.toString(this.mCallRadioTech) + ", isMT=" + isIncoming() + ", heldByRemote=" + this.mIsHeldByRemote + " ]";
    }

    private static int getCallRadioTech(ImsPhoneConnection imsPhoneConnection) {
        return ServiceState.rilRadioTechnologyToAccessNetworkType(imsPhoneConnection.getCallRadioTech());
    }
}
