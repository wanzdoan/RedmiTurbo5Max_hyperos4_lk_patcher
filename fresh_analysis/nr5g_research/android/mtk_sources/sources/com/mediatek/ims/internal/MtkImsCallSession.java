package com.mediatek.ims.internal;

import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsCallSession;
import android.util.Log;
import com.android.ims.internal.IImsCallSession;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class MtkImsCallSession extends ImsCallSession {
    private static final String TAG = "MtkImsCallSession";
    private final IMtkImsCallSession miMtkSession;

    public MtkImsCallSession(IImsCallSession iSession, IMtkImsCallSession iMtkSession) {
        this.miMtkSession = iMtkSession;
        this.miSession = iSession;
        if (iMtkSession == null || iSession == null) {
            this.mClosed = true;
            return;
        }
        try {
            this.miMtkSession.setListener(new IMtkImsCallSessionListenerProxy());
        } catch (RemoteException e) {
        }
        try {
            this.miSession.setListener(new ImsCallSession.IImsCallSessionListenerProxy(this));
        } catch (RemoteException e2) {
        }
    }

    public synchronized void close() {
        if (this.mClosed) {
            return;
        }
        try {
            this.miMtkSession.close();
            this.mClosed = true;
        } catch (RemoteException e) {
        }
    }

    public boolean isIncomingCallMultiparty() {
        if (this.mClosed) {
            return false;
        }
        try {
            return this.miMtkSession.isIncomingCallMultiparty();
        } catch (RemoteException e) {
            return false;
        }
    }

    public void approveEccRedial(boolean isAprroved) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miMtkSession.approveEccRedial(isAprroved);
        } catch (RemoteException e) {
        }
    }

    public void setImsCallMode(int mode) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miMtkSession.setImsCallMode(mode);
        } catch (RemoteException e) {
            Log.e(TAG, "setImsCallMode: RemoteException!");
        }
    }

    public void removeLastParticipant() {
        if (this.mClosed) {
            return;
        }
        try {
            this.miMtkSession.removeLastParticipant();
        } catch (RemoteException e) {
            Log.e(TAG, "removeLastParticipant: RemoteException!");
        }
    }

    public String getHeaderCallId() {
        if (this.mClosed) {
            return null;
        }
        try {
            return this.miMtkSession.getHeaderCallId();
        } catch (RemoteException e) {
            Log.e(TAG, "getCallId: RemoteException!");
            return null;
        }
    }

    public void videoScreenOperation(int type, String operation, String duration) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miMtkSession.videoScreenOperation(type, operation, duration);
        } catch (RemoteException e) {
            Log.e(TAG, "videoScreenOperation: RemoteException!");
        }
    }

    public class IMtkImsCallSessionListenerProxy extends IMtkImsCallSessionListener.Stub {
        public IMtkImsCallSessionListenerProxy() {
        }

        public void callSessionTextCapabilityChanged(IMtkImsCallSession session, int localCapability, int remoteCapability, int localTextStatus, int realRemoteCapability) {
            if (MtkImsCallSession.this.mListener != null) {
                MtkImsCallSession.this.mListener.callSessionTextCapabilityChanged(MtkImsCallSession.this, localCapability, remoteCapability, localTextStatus, realRemoteCapability);
            }
        }

        public void callSessionRttEventReceived(IMtkImsCallSession session, int event) {
            if (MtkImsCallSession.this.mListener != null) {
                MtkImsCallSession.this.mListener.callSessionRttEventReceived(MtkImsCallSession.this, event);
            }
        }

        public void callSessionRinging(IMtkImsCallSession session, ImsCallProfile profile) {
            if (MtkImsCallSession.this.mListener != null) {
                MtkImsCallSession.this.mListener.callSessionRinging(MtkImsCallSession.this, profile);
            }
        }

        public void callSessionBusy(IMtkImsCallSession session) {
            if (MtkImsCallSession.this.mListener != null) {
                MtkImsCallSession.this.mListener.callSessionBusy(MtkImsCallSession.this);
            }
        }

        public void callSessionCalling(IMtkImsCallSession session) {
            if (MtkImsCallSession.this.mListener != null) {
                MtkImsCallSession.this.mListener.callSessionCalling(MtkImsCallSession.this);
            }
        }

        public void callSessionMergeStarted(IMtkImsCallSession session, IMtkImsCallSession newSession, ImsCallProfile profile) {
            Log.d(MtkImsCallSession.TAG, "callSessionMergeStarted");
        }

        public void callSessionMergeComplete(IMtkImsCallSession newSession) {
            if (MtkImsCallSession.this.mListener != null) {
                if (newSession == null) {
                    MtkImsCallSession.this.mListener.callSessionMergeComplete((ImsCallSession) null);
                    return;
                }
                ImsCallSession validActiveSession = MtkImsCallSession.this;
                try {
                    if (!Objects.equals(MtkImsCallSession.this.miSession.getCallId(), newSession.getCallId())) {
                        IImsCallSession aospImsCallSession = newSession.getIImsCallSession();
                        validActiveSession = new MtkImsCallSession(aospImsCallSession, newSession);
                    }
                } catch (RemoteException e) {
                    Log.e(MtkImsCallSession.TAG, "callSessionMergeComplete: exception for getCallId!");
                }
                MtkImsCallSession.this.mListener.callSessionMergeComplete(validActiveSession);
            }
        }

        public void callSessionRedialEcc(IMtkImsCallSession session, boolean isNeedUserConfirm) {
            Log.d(MtkImsCallSession.TAG, "callSessionRedialEcc: isNeedUserConfirm = " + isNeedUserConfirm);
            if (MtkImsCallSession.this.mListener != null) {
                MtkImsCallSession.this.mListener.callSessionRedialEcc(MtkImsCallSession.this, isNeedUserConfirm);
            }
        }

        public void callSessionVideoRingtoneEventReceived(IMtkImsCallSession session, int eventType, String event) {
            Log.d(MtkImsCallSession.TAG, "callSessionVideoRingtoneEventReceived: eventType = " + eventType + ", event = " + event);
            if (MtkImsCallSession.this.mListener != null) {
                MtkImsCallSession.this.mListener.callSessionVideoRingtoneEventReceived(MtkImsCallSession.this, eventType, event);
            }
        }

        public void callSessionNotificationRingtoneReceived(IMtkImsCallSession session, int causeNum, String causeText) {
            Log.d(MtkImsCallSession.TAG, "callSessionNotificationRingtoneReceived: causeNum = " + causeNum + ", causeText = " + causeText);
            if (MtkImsCallSession.this.mListener != null) {
                MtkImsCallSession.this.mListener.callSessionNotificationRingtoneReceived(MtkImsCallSession.this, causeNum, causeText);
            }
        }
    }
}
