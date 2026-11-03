package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.EventLog;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.domainselection.DomainSelectionResolver;
import com.android.internal.telephony.nano.TelephonyProto$TelephonyEvent;
import com.android.internal.telephony.util.NetUtils$$ExternalSyntheticBUOutline0;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class GsmCdmaCallTracker extends CallTracker {
    public static final int MAX_CONNECTIONS_GSM = 19;
    public GsmCdmaCall mBackgroundCall;
    private RegistrantList mCallWaitingRegistrants;
    public final GsmCdmaConnection[] mConnections;
    private boolean mDesiredMute;
    private ArrayList mDroppedDuringPoll;
    private BroadcastReceiver mEcmExitReceiver;
    public GsmCdmaCall mForegroundCall;
    private boolean mHangupPendingMO;
    private boolean mHasMissedCall;
    private IHyperTeleImsManager mIHyperTeleImsManager;
    private boolean mIsInEmergencyCall;
    protected GsmCdmaConnection mPendingMO;
    public GsmCdmaPhone mPhone;
    public GsmCdmaCall mRingingCall;
    public PhoneConstants.State mState;
    private RegistrantList mVoiceCallEndedRegistrants;
    private RegistrantList mVoiceCallStartedRegistrants;

    private void onInitialPollRequest(boolean z) {
    }

    public int getMaxConnectionsPerCall() {
        return 5;
    }

    public boolean isInEmergencyCall() {
        return false;
    }

    public boolean isInOtaspCall() {
        return false;
    }

    protected boolean onGetCallListDone(List list) {
        return false;
    }

    public GsmCdmaCallTracker(GsmCdmaPhone gsmCdmaPhone, FeatureFlags featureFlags) {
        super(featureFlags);
        this.mConnections = new GsmCdmaConnection[19];
        this.mVoiceCallEndedRegistrants = new RegistrantList();
        this.mVoiceCallStartedRegistrants = new RegistrantList();
        this.mDroppedDuringPoll = new ArrayList(19);
        this.mRingingCall = new GsmCdmaCall(this);
        this.mForegroundCall = new GsmCdmaCall(this);
        this.mBackgroundCall = new GsmCdmaCall(this);
        this.mDesiredMute = false;
        this.mState = PhoneConstants.State.IDLE;
        this.mCallWaitingRegistrants = new RegistrantList();
        this.mHasMissedCall = false;
        this.mEcmExitReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.GsmCdmaCallTracker.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED")) {
                    int i = 0;
                    boolean booleanExtra = intent.getBooleanExtra("android.telephony.extra.PHONE_IN_ECM_STATE", false);
                    GsmCdmaCallTracker.this.log("Received ACTION_EMERGENCY_CALLBACK_MODE_CHANGED isInEcm = " + booleanExtra);
                    if (booleanExtra) {
                        return;
                    }
                    ArrayList arrayList = new ArrayList();
                    arrayList.addAll(GsmCdmaCallTracker.this.mRingingCall.getConnections());
                    arrayList.addAll(GsmCdmaCallTracker.this.mForegroundCall.getConnections());
                    arrayList.addAll(GsmCdmaCallTracker.this.mBackgroundCall.getConnections());
                    GsmCdmaConnection gsmCdmaConnection = GsmCdmaCallTracker.this.mPendingMO;
                    if (gsmCdmaConnection != null) {
                        arrayList.add(gsmCdmaConnection);
                    }
                    int size = arrayList.size();
                    while (i < size) {
                        Object obj = arrayList.get(i);
                        i++;
                        Connection connection = (Connection) obj;
                        if (connection != null) {
                            connection.onExitedEcmMode();
                        }
                    }
                }
            }
        };
        this.mPhone = gsmCdmaPhone;
        CommandsInterface commandsInterface = gsmCdmaPhone.mCi;
        this.mCi = commandsInterface;
        commandsInterface.registerForCallStateChanged(this, 2, null);
        this.mCi.registerForOn(this, 9, null);
        this.mCi.registerForNotAvailable(this, 10, null);
        this.mCi.registerForRilConnected(this, TelephonyProto$TelephonyEvent.RilSetupDataCallResponse.RilDataCallFailCause.PDP_FAIL_MIP_HA_FOREIGN_AGENT_AUTHENTICATION_FAILURE, null);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED");
        this.mPhone.getContext().registerReceiver(this.mEcmExitReceiver, intentFilter);
        onInitialPollRequest(true);
        MiuiCallControllerStub.get(this.mPhone.getPhoneId()).init(gsmCdmaPhone, new Object[0]);
        this.mIHyperTeleImsManager = (IHyperTeleImsManager) HyperTelephonyFactory.getInstance().initModule(IHyperTeleImsManager.DEFAULT, new Object[0]);
    }

    public void requestInitialPollState() {
        onInitialPollRequest(false);
    }

    protected void reset() {
        Rlog.d("GsmCdmaCallTracker", "reset");
        MiuiCallControllerStub.get(this.mPhone.getPhoneId()).removeAllHangupPendingConnectionIndex(this.mPhone);
        for (GsmCdmaConnection gsmCdmaConnection : this.mConnections) {
            if (gsmCdmaConnection != null) {
                gsmCdmaConnection.onDisconnect(36);
                gsmCdmaConnection.dispose();
            }
        }
        GsmCdmaConnection gsmCdmaConnection2 = this.mPendingMO;
        if (gsmCdmaConnection2 != null) {
            gsmCdmaConnection2.onDisconnect(36);
            this.mPendingMO.dispose();
        }
        this.mPendingMO = null;
        clearDisconnected();
    }

    protected void finalize() {
        Rlog.d("GsmCdmaCallTracker", "GsmCdmaCallTracker finalized");
    }

    @Override // com.android.internal.telephony.CallTracker
    public void registerForVoiceCallStarted(Handler handler, int i, Object obj) {
        Registrant registrant = new Registrant(handler, i, obj);
        this.mVoiceCallStartedRegistrants.add(registrant);
        if (this.mState != PhoneConstants.State.IDLE) {
            registrant.notifyRegistrant(new AsyncResult((Object) null, (Object) null, (Throwable) null));
        }
    }

    @Override // com.android.internal.telephony.CallTracker
    public void unregisterForVoiceCallStarted(Handler handler) {
        this.mVoiceCallStartedRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CallTracker
    public void registerForVoiceCallEnded(Handler handler, int i, Object obj) {
        this.mVoiceCallEndedRegistrants.add(new Registrant(handler, i, obj));
    }

    @Override // com.android.internal.telephony.CallTracker
    public void unregisterForVoiceCallEnded(Handler handler) {
        this.mVoiceCallEndedRegistrants.remove(handler);
    }

    public void registerForCallWaiting(Handler handler, int i, Object obj) {
        this.mCallWaitingRegistrants.add(new Registrant(handler, i, obj));
    }

    public void unregisterForCallWaiting(Handler handler) {
        this.mCallWaitingRegistrants.remove(handler);
    }

    private void fakeHoldForegroundBeforeDial() {
        ArrayList<Connection> connections = this.mForegroundCall.getConnections();
        int size = connections.size();
        int i = 0;
        while (i < size) {
            Connection connection = connections.get(i);
            i++;
            ((GsmCdmaConnection) connection).fakeHoldBeforeDial();
        }
    }

    public synchronized Connection dialGsm(String str, PhoneInternalInterface.DialArgs dialArgs) throws CallStateException {
        try {
            int i = dialArgs.clirMode;
            UUSInfo uUSInfo = dialArgs.uusInfo;
            Bundle bundle = dialArgs.intentExtras;
            boolean z = dialArgs.isEmergency;
            if (z) {
                log("dial gsm emergency call, set clirModIe=2");
                i = 2;
            }
            int i2 = i;
            clearDisconnected();
            checkForDialIssues(z);
            String strConvertNumberIfNecessary = convertNumberIfNecessary(this.mPhone, str);
            if (this.mForegroundCall.getState() == Call.State.ACTIVE) {
                switchWaitingOrHoldingAndActive();
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException unused) {
                }
                fakeHoldForegroundBeforeDial();
            }
            if (this.mForegroundCall.getState() != Call.State.IDLE) {
                throw new CallStateException("cannot dial in current state");
            }
            this.mPendingMO = onCreateGsmCdmaConnection(this.mPhone, strConvertNumberIfNecessary, this, this.mForegroundCall, dialArgs);
            if (bundle != null) {
                Rlog.d("GsmCdmaCallTracker", "dialGsm - emergency dialer: " + bundle.getBoolean("android.telecom.extra.IS_USER_INTENT_EMERGENCY_CALL"));
                this.mPendingMO.setHasKnownUserIntentEmergency(bundle.getBoolean("android.telecom.extra.IS_USER_INTENT_EMERGENCY_CALL"));
            }
            this.mHangupPendingMO = false;
            this.mPhone.getVoiceCallSessionStats().onRilDial(this.mPendingMO);
            if (this.mPendingMO.getAddress() == null || this.mPendingMO.getAddress().length() == 0 || this.mPendingMO.getAddress().indexOf(78) >= 0) {
                this.mPendingMO.mCause = 7;
                pollCallsWhenSafe();
            } else {
                setMute(false);
                this.mCi.dial(this.mPendingMO.getAddress(), this.mPendingMO.isEmergencyCall(), this.mPendingMO.getEmergencyNumberInfo(), this.mPendingMO.hasKnownUserIntentEmergency(), i2, uUSInfo, obtainCompleteMessage());
            }
            if (this.mNumberConverted) {
                this.mPendingMO.restoreDialedNumberAfterConversion(str);
                this.mNumberConverted = false;
            }
            updatePhoneState();
            this.mPhone.notifyPreciseCallStateChanged();
        } catch (Throwable th) {
            throw th;
        }
        return this.mPendingMO;
    }

    private void disableDataCallInEmergencyCall(String str) {
        if (((TelephonyManager) this.mPhone.getContext().getSystemService("phone")).isEmergencyNumber(str)) {
            log("disableDataCallInEmergencyCall");
            setIsInEmergencyCall();
        }
    }

    public void setIsInEmergencyCall() {
        this.mIsInEmergencyCall = true;
        this.mPhone.notifyEmergencyCallRegistrants(true);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.GsmCdmaCallTracker$2, reason: invalid class name */
    class AnonymousClass2 implements Runnable {
        final /* synthetic */ GsmCdmaCallTracker this$0;
        final /* synthetic */ int val$finalClirMode;

        @Override // java.lang.Runnable
        public void run() {
            GsmCdmaCallTracker gsmCdmaCallTracker = this.this$0;
            gsmCdmaCallTracker.mCi.dial(gsmCdmaCallTracker.mPendingMO.getAddress(), this.this$0.mPendingMO.isEmergencyCall(), this.this$0.mPendingMO.getEmergencyNumberInfo(), this.this$0.mPendingMO.hasKnownUserIntentEmergency(), this.val$finalClirMode, this.this$0.obtainCompleteMessage());
        }
    }

    public Connection dial(String str, PhoneInternalInterface.DialArgs dialArgs) throws CallStateException {
        return dialGsm(str, dialArgs);
    }

    public Connection dialGsm(String str, int i, UUSInfo uUSInfo, Bundle bundle) throws CallStateException {
        return dialGsm(str, new PhoneInternalInterface.DialArgs.Builder().setClirMode(i).setUusInfo(uUSInfo).setIntentExtras(bundle).build());
    }

    public void acceptCall() throws CallStateException {
        if (this.mRingingCall.getState() == Call.State.INCOMING) {
            Rlog.i("phone", "acceptCall: incoming...");
            setMute(false);
            this.mPhone.getVoiceCallSessionStats().onRilAcceptCall(this.mRingingCall.getConnections());
            this.mCi.acceptCall(obtainCompleteMessage());
            return;
        }
        if (this.mRingingCall.getState() == Call.State.WAITING) {
            setMute(false);
            switchWaitingOrHoldingAndActive();
        } else {
            CallManager$$ExternalSyntheticBUOutline0.m("phone not ringing");
        }
    }

    public void rejectCall() throws CallStateException {
        if (this.mRingingCall.getState().isRinging()) {
            this.mCi.rejectCall(obtainCompleteMessage());
        } else {
            CallManager$$ExternalSyntheticBUOutline0.m("phone not ringing");
        }
    }

    public void switchWaitingOrHoldingAndActive() throws CallStateException {
        if (this.mRingingCall.getState() == Call.State.INCOMING) {
            CallManager$$ExternalSyntheticBUOutline0.m("cannot be in the incoming state");
        } else {
            this.mCi.switchWaitingOrHoldingAndActive(obtainCompleteMessage(8));
        }
    }

    public void conference() {
        this.mCi.conference(obtainCompleteMessage(11));
    }

    public void explicitCallTransfer() {
        this.mCi.explicitCallTransfer(obtainCompleteMessage(13));
    }

    public void clearDisconnected() {
        internalClearDisconnected();
        updatePhoneState();
        this.mPhone.notifyPreciseCallStateChanged();
    }

    public boolean canConference() {
        return this.mForegroundCall.getState() == Call.State.ACTIVE && this.mBackgroundCall.getState() == Call.State.HOLDING && !this.mBackgroundCall.isFull() && !this.mForegroundCall.isFull();
    }

    public void checkForDialIssues(boolean z) throws CallStateException {
        boolean zBooleanValue = TelephonyProperties.disable_call().orElse(Boolean.FALSE).booleanValue();
        if (this.mCi.getRadioState() != 1) {
            Phone$$ExternalSyntheticBUOutline2.m(2, "Modem not powered");
            return;
        }
        if (zBooleanValue) {
            Phone$$ExternalSyntheticBUOutline2.m(5, "Calling disabled via ro.telephony.disable-call property");
            return;
        }
        if (this.mPendingMO != null) {
            Phone$$ExternalSyntheticBUOutline2.m(3, "A call is already dialing.");
            return;
        }
        if (this.mRingingCall.isRinging()) {
            Phone$$ExternalSyntheticBUOutline2.m(4, "Can't call while a call is ringing.");
            return;
        }
        if (this.mForegroundCall.getState().isAlive() && this.mBackgroundCall.getState().isAlive()) {
            Phone$$ExternalSyntheticBUOutline2.m(6, "There is already a foreground and background call.");
        } else {
            if (z || !isInOtaspCall()) {
                return;
            }
            Phone$$ExternalSyntheticBUOutline2.m(7, "OTASP provisioning is in process.");
        }
    }

    public boolean canTransfer() {
        return (this.mForegroundCall.getState() == Call.State.ACTIVE || this.mForegroundCall.getState() == Call.State.ALERTING || this.mForegroundCall.getState() == Call.State.DIALING) && this.mBackgroundCall.getState() == Call.State.HOLDING;
    }

    private void internalClearDisconnected() {
        this.mRingingCall.clearDisconnected();
        this.mForegroundCall.clearDisconnected();
        this.mBackgroundCall.clearDisconnected();
    }

    protected Message obtainCompleteMessage() {
        return obtainCompleteMessage(4);
    }

    private Message obtainCompleteMessage(int i) {
        this.mPendingOperations++;
        this.mLastRelevantPoll = null;
        this.mNeedsPoll = true;
        return obtainMessage(i);
    }

    private void operationComplete() {
        int i = this.mPendingOperations - 1;
        this.mPendingOperations = i;
        if (i == 0 && this.mNeedsPoll) {
            Message messageObtainMessage = obtainMessage(1);
            this.mLastRelevantPoll = messageObtainMessage;
            this.mCi.getCurrentCalls(messageObtainMessage);
        } else if (i < 0) {
            Rlog.e("GsmCdmaCallTracker", "GsmCdmaCallTracker.pendingOperations < 0");
            this.mPendingOperations = 0;
        }
        MiuiCallControllerStub.get(this.mPhone.getPhoneId()).removeAllHangupPendingConnectionIndex(this.mPhone);
    }

    private void updatePhoneState() {
        PhoneConstants.State state = this.mState;
        if (this.mRingingCall.isRinging()) {
            this.mState = PhoneConstants.State.RINGING;
        } else if (this.mPendingMO != null || !this.mForegroundCall.isIdle() || !this.mBackgroundCall.isIdle()) {
            this.mState = PhoneConstants.State.OFFHOOK;
        } else {
            Phone imsPhone = this.mPhone.getImsPhone();
            if (this.mState == PhoneConstants.State.OFFHOOK && imsPhone != null) {
                imsPhone.callEndCleanupHandOverCallIfAny();
            }
            this.mState = PhoneConstants.State.IDLE;
        }
        PhoneConstants.State state2 = this.mState;
        PhoneConstants.State state3 = PhoneConstants.State.IDLE;
        if (state2 == state3 && state != state2) {
            this.mVoiceCallEndedRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
        } else if (state == state3 && state != state2) {
            this.mVoiceCallStartedRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
        }
        log("update phone state, old=" + state + " new=" + this.mState);
        if (this.mState != state) {
            this.mPhone.notifyPhoneStateChanged();
        }
    }

    /* JADX WARN: Code duplicated, block: B:160:0x01df A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:66:0x0199 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:67:0x019b A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:69:0x01a7 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:70:0x01a9 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:79:0x01d1 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:80:0x01d3 A[ADDED_TO_REGION] */
    @Override // com.android.internal.telephony.CallTracker
    protected synchronized void handlePollCalls(AsyncResult asyncResult) {
        List arrayList;
        int i;
        Phone imsPhone;
        int i2;
        boolean z;
        DriverCall driverCall;
        try {
            Throwable th = asyncResult.exception;
            if (th == null) {
                arrayList = (List) asyncResult.result;
            } else if (isCommandExceptionRadioNotAvailable(th)) {
                arrayList = new ArrayList();
            } else {
                pollCallsAfterDelay();
                return;
            }
            if (onGetCallListDone(arrayList)) {
                return;
            }
            ArrayList arrayList2 = new ArrayList();
            int size = this.mHandoverConnections.size();
            int size2 = arrayList.size();
            boolean z2 = false;
            Connection connectionCheckMtFindNewRinging = null;
            int i3 = 0;
            int i4 = 0;
            boolean z3 = false;
            boolean z4 = false;
            while (true) {
                GsmCdmaConnection[] gsmCdmaConnectionArr = this.mConnections;
                if (i3 < gsmCdmaConnectionArr.length) {
                    GsmCdmaConnection gsmCdmaConnection = gsmCdmaConnectionArr[i3];
                    if (i4 < size2) {
                        driverCall = (DriverCall) arrayList.get(i4);
                        z = true;
                        if (driverCall.index == i3 + 1) {
                            i4++;
                        }
                        if (gsmCdmaConnection == null || driverCall == null) {
                            if (gsmCdmaConnection == null && driverCall == null) {
                                this.mDroppedDuringPoll.add(gsmCdmaConnection);
                                this.mConnections[i3] = null;
                            } else if (gsmCdmaConnection == null && driverCall != null && !gsmCdmaConnection.compareTo(driverCall)) {
                                this.mDroppedDuringPoll.add(gsmCdmaConnection);
                                onCreateGsmCdmaConnectionForMismatch(driverCall, this, i3);
                                if (this.mConnections[i3].getCall() == this.mRingingCall) {
                                    connectionCheckMtFindNewRinging = this.mConnections[i3];
                                    if (hangupWaitingCallSilently(i3)) {
                                        return;
                                    }
                                }
                            } else if (gsmCdmaConnection == null && driverCall != null) {
                                boolean zUpdate = gsmCdmaConnection.update(driverCall);
                                if (!z3 && !zUpdate) {
                                    z3 = false;
                                }
                            }
                            i3++;
                            z2 = false;
                        } else {
                            GsmCdmaConnection gsmCdmaConnection2 = this.mPendingMO;
                            if (gsmCdmaConnection2 != null && gsmCdmaConnection2.compareTo(driverCall)) {
                                GsmCdmaConnection[] gsmCdmaConnectionArr2 = this.mConnections;
                                GsmCdmaConnection gsmCdmaConnection3 = this.mPendingMO;
                                gsmCdmaConnectionArr2[i3] = gsmCdmaConnection3;
                                gsmCdmaConnection3.mIndex = i3;
                                gsmCdmaConnection3.update(driverCall);
                                this.mPendingMO = null;
                                if (this.mHangupPendingMO) {
                                    this.mHangupPendingMO = z2;
                                    try {
                                        log("poll: hangupPendingMO, hangup conn " + i3);
                                        hangup(this.mConnections[i3]);
                                    } catch (CallStateException unused) {
                                        Rlog.e("GsmCdmaCallTracker", "unexpected error on hangup");
                                    }
                                    return;
                                }
                            } else {
                                log("pendingMo=" + this.mPendingMO + ", dc=" + driverCall);
                                this.mConnections[i3] = onCreateGsmCdmaConnection(this.mPhone, driverCall, this, i3);
                                StringBuilder sb = new StringBuilder();
                                sb.append("New connection is not mPendingMO. Creating new GsmCdmaConnection, objId=");
                                sb.append(System.identityHashCode(this.mConnections[i3]));
                                log(sb.toString());
                                Connection hoConnection = getHoConnection(driverCall);
                                if (hoConnection != null) {
                                    log("Handover connection found.");
                                    this.mConnections[i3].migrateFrom(hoConnection);
                                    Call.State state = hoConnection.mPreHandoverState;
                                    if (state != Call.State.ACTIVE && state != Call.State.HOLDING && driverCall.state == DriverCall.State.ACTIVE) {
                                        this.mConnections[i3].onConnectedInOrOut();
                                    } else {
                                        this.mConnections[i3].onConnectedConnectionMigrated();
                                    }
                                    this.mHandoverConnections.remove(hoConnection);
                                    Iterator<Connection> it = this.mHandoverConnections.iterator();
                                    while (it.hasNext()) {
                                        Connection next = it.next();
                                        Rlog.i("GsmCdmaCallTracker", "HO Conn state is " + next.mPreHandoverState);
                                        if (next.mPreHandoverState == this.mConnections[i3].getState()) {
                                            Rlog.i("GsmCdmaCallTracker", "Removing HO conn " + hoConnection + next.mPreHandoverState);
                                            it.remove();
                                        }
                                    }
                                    this.mPhone.notifyHandoverStateChanged(this.mConnections[i3]);
                                } else {
                                    log("New connection is not mPendingMO nor a pending handover.");
                                    connectionCheckMtFindNewRinging = checkMtFindNewRinging(driverCall, i3);
                                    if (connectionCheckMtFindNewRinging == null) {
                                        arrayList2.add(this.mConnections[i3]);
                                        z4 = z;
                                    } else if (hangupWaitingCallSilently(i3)) {
                                        return;
                                    }
                                }
                            }
                        }
                        z3 = z;
                        i3++;
                        z2 = false;
                    } else {
                        z = true;
                    }
                    driverCall = null;
                    if (gsmCdmaConnection == null) {
                        if (gsmCdmaConnection == null) {
                            if (gsmCdmaConnection == null) {
                                if (gsmCdmaConnection == null) {
                                }
                            } else if (gsmCdmaConnection == null) {
                            }
                        } else if (gsmCdmaConnection == null) {
                            if (gsmCdmaConnection == null) {
                            }
                        } else if (gsmCdmaConnection == null) {
                        }
                    } else if (gsmCdmaConnection == null) {
                        if (gsmCdmaConnection == null) {
                            if (gsmCdmaConnection == null) {
                            }
                        } else if (gsmCdmaConnection == null) {
                        }
                    } else if (gsmCdmaConnection == null) {
                        if (gsmCdmaConnection == null) {
                        }
                    } else if (gsmCdmaConnection == null) {
                    }
                    i3++;
                    z2 = false;
                } else {
                    int i5 = 1;
                    if (this.mPendingMO != null) {
                        Rlog.d("GsmCdmaCallTracker", "Pending MO dropped before poll fg state:" + this.mForegroundCall.getState());
                        this.mDroppedDuringPoll.add(this.mPendingMO);
                        this.mPendingMO = null;
                        i = 0;
                        this.mHangupPendingMO = false;
                    } else {
                        i = 0;
                    }
                    if (connectionCheckMtFindNewRinging != null) {
                        this.mPhone.notifyNewRingingConnection(connectionCheckMtFindNewRinging);
                    }
                    ArrayList arrayList3 = new ArrayList();
                    int i6 = i;
                    for (int size3 = this.mDroppedDuringPoll.size() - 1; size3 >= 0; size3--) {
                        GsmCdmaConnection gsmCdmaConnection4 = (GsmCdmaConnection) this.mDroppedDuringPoll.get(size3);
                        if (gsmCdmaConnection4.isIncoming() && gsmCdmaConnection4.getConnectTime() == 0) {
                            int i7 = gsmCdmaConnection4.mCause == 3 ? 16 : 1;
                            log("missed/rejected call, conn.cause=" + gsmCdmaConnection4.mCause);
                            log("setting cause to " + i7);
                            this.mDroppedDuringPoll.remove(size3);
                            i6 |= gsmCdmaConnection4.onDisconnect(i7) ? 1 : 0;
                            arrayList3.add(gsmCdmaConnection4);
                        } else {
                            int i8 = gsmCdmaConnection4.mCause;
                            if (i8 == 3 || i8 == 7) {
                                this.mDroppedDuringPoll.remove(size3);
                                i6 |= gsmCdmaConnection4.onDisconnect(gsmCdmaConnection4.mCause) ? 1 : 0;
                                arrayList3.add(gsmCdmaConnection4);
                            }
                        }
                    }
                    if (arrayList3.size() > 0) {
                        this.mPhone.getVoiceCallSessionStats().onRilCallListChanged(arrayList3);
                    }
                    Iterator<Connection> it2 = this.mHandoverConnections.iterator();
                    while (it2.hasNext()) {
                        Connection next2 = it2.next();
                        log("handlePollCalls - disconnect hoConn= " + next2 + " hoConn.State= " + next2.getState());
                        if (next2.getState().isRinging()) {
                            i2 = i5;
                            next2.onDisconnect(i2);
                        } else {
                            i2 = i5;
                            next2.onDisconnect(-1);
                        }
                        it2.remove();
                        i5 = i2;
                    }
                    if (this.mDroppedDuringPoll.size() > 0) {
                        this.mCi.getLastCallFailCause(obtainNoPollCompleteMessage(5));
                    }
                    if (connectionCheckMtFindNewRinging != null || z3 || i6 != 0) {
                        internalClearDisconnected();
                    }
                    updatePhoneState();
                    if (z4) {
                        int size4 = arrayList2.size();
                        while (i < size4) {
                            Object obj = arrayList2.get(i);
                            i++;
                            Connection connection = (Connection) obj;
                            log("Notify unknown for " + connection);
                            this.mPhone.notifyUnknownConnection(connection);
                        }
                    }
                    if (z3 || connectionCheckMtFindNewRinging != null || i6 != 0) {
                        this.mPhone.notifyPreciseCallStateChanged();
                        updateMetrics(this.mConnections);
                    }
                    if (size > 0 && this.mHandoverConnections.size() == 0 && (imsPhone = this.mPhone.getImsPhone()) != null) {
                        imsPhone.callEndCleanupHandOverCallIfAny();
                    }
                    return;
                }
            }
        } catch (Throwable th2) {
            throw th2;
        }
    }

    private void updateMetrics(GsmCdmaConnection[] gsmCdmaConnectionArr) {
        ArrayList arrayList = new ArrayList();
        for (GsmCdmaConnection gsmCdmaConnection : gsmCdmaConnectionArr) {
            if (gsmCdmaConnection != null) {
                arrayList.add(gsmCdmaConnection);
            }
        }
        this.mPhone.getVoiceCallSessionStats().onRilCallListChanged(arrayList);
    }

    private void handleRadioNotAvailable() {
        pollCallsWhenSafe();
    }

    public void hangup(GsmCdmaConnection gsmCdmaConnection) throws CallStateException {
        if (gsmCdmaConnection.mOwner != this) {
            GsmCdmaCallTracker$$ExternalSyntheticBUOutline0.m("GsmCdmaConnection ", gsmCdmaConnection, "does not belong to GsmCdmaCallTracker ", this);
            return;
        }
        if (gsmCdmaConnection == this.mPendingMO) {
            log("hangup: set hangupPendingMO to true");
            this.mHangupPendingMO = true;
        } else {
            try {
                this.mCi.hangupConnection(gsmCdmaConnection.getGsmCdmaIndex(), obtainCompleteMessage());
            } catch (CallStateException unused) {
                Rlog.w("GsmCdmaCallTracker", "GsmCdmaCallTracker WARN: hangup() on absent connection " + gsmCdmaConnection);
            }
        }
        gsmCdmaConnection.onHangupLocal();
    }

    public void separate(GsmCdmaConnection gsmCdmaConnection) throws CallStateException {
        if (gsmCdmaConnection.mOwner != this) {
            GsmCdmaCallTracker$$ExternalSyntheticBUOutline0.m("GsmCdmaConnection ", gsmCdmaConnection, "does not belong to GsmCdmaCallTracker ", this);
            return;
        }
        try {
            this.mCi.separateConnection(gsmCdmaConnection.getGsmCdmaIndex(), obtainCompleteMessage(12));
        } catch (CallStateException unused) {
            Rlog.w("GsmCdmaCallTracker", "GsmCdmaCallTracker WARN: separate() on absent connection " + gsmCdmaConnection);
        }
    }

    public void setMute(boolean z) {
        this.mDesiredMute = z;
        this.mCi.setMute(z, null);
    }

    public boolean getMute() {
        return this.mDesiredMute;
    }

    public void hangup(GsmCdmaCall gsmCdmaCall) throws CallStateException {
        if (gsmCdmaCall.getConnectionsCount() == 0) {
            CallManager$$ExternalSyntheticBUOutline0.m("no connections in call");
            return;
        }
        GsmCdmaCall gsmCdmaCall2 = this.mRingingCall;
        if (gsmCdmaCall == gsmCdmaCall2) {
            log("(ringing) hangup waiting or background");
            logHangupEvent(gsmCdmaCall);
            this.mCi.hangupWaitingOrBackground(obtainCompleteMessage());
        } else if (gsmCdmaCall == this.mForegroundCall) {
            if (gsmCdmaCall.isDialingOrAlerting()) {
                log("(foregnd) hangup dialing or alerting...");
                hangup((GsmCdmaConnection) gsmCdmaCall.getConnections().get(0));
            } else if (this.mRingingCall.isRinging()) {
                log("hangup all conns in active/background call, without affecting ringing call");
                hangupAllConnections(gsmCdmaCall);
            } else {
                logHangupEvent(gsmCdmaCall);
                hangupForegroundResumeBackground();
            }
        } else if (gsmCdmaCall == this.mBackgroundCall) {
            if (gsmCdmaCall2.isRinging()) {
                log("hangup all conns in background call");
                hangupAllConnections(gsmCdmaCall);
            } else {
                hangupWaitingOrBackground();
            }
        } else {
            throw new RuntimeException("GsmCdmaCall " + gsmCdmaCall + "does not belong to GsmCdmaCallTracker " + this);
        }
        gsmCdmaCall.onHangupLocal();
        this.mPhone.notifyPreciseCallStateChanged();
    }

    private void logHangupEvent(GsmCdmaCall gsmCdmaCall) {
        ArrayList<Connection> connections = gsmCdmaCall.getConnections();
        int size = connections.size();
        int i = 0;
        while (i < size) {
            Connection connection = connections.get(i);
            i++;
            try {
                ((GsmCdmaConnection) connection).getGsmCdmaIndex();
            } catch (CallStateException unused) {
            }
        }
    }

    public void hangupWaitingOrBackground() {
        log("hangupWaitingOrBackground");
        logHangupEvent(this.mBackgroundCall);
        this.mCi.hangupWaitingOrBackground(obtainCompleteMessage());
    }

    public void hangupForegroundResumeBackground() {
        log("hangupForegroundResumeBackground");
        this.mCi.hangupForegroundResumeBackground(obtainCompleteMessage());
    }

    public void hangupConnectionByIndex(GsmCdmaCall gsmCdmaCall, int i) throws CallStateException {
        ArrayList<Connection> connections = gsmCdmaCall.getConnections();
        int size = connections.size();
        int i2 = 0;
        while (i2 < size) {
            Connection connection = connections.get(i2);
            i2++;
            GsmCdmaConnection gsmCdmaConnection = (GsmCdmaConnection) connection;
            if (!gsmCdmaConnection.mDisconnected && gsmCdmaConnection.getGsmCdmaIndex() == i) {
                this.mCi.hangupConnection(i, obtainCompleteMessage());
                return;
            }
        }
        CallManager$$ExternalSyntheticBUOutline0.m("no GsmCdma index found");
    }

    public void hangupAllConnections(GsmCdmaCall gsmCdmaCall) {
        try {
            ArrayList<Connection> connections = gsmCdmaCall.getConnections();
            int size = connections.size();
            int i = 0;
            while (i < size) {
                Connection connection = connections.get(i);
                i++;
                GsmCdmaConnection gsmCdmaConnection = (GsmCdmaConnection) connection;
                if (!gsmCdmaConnection.mDisconnected) {
                    this.mCi.hangupConnection(gsmCdmaConnection.getGsmCdmaIndex(), obtainCompleteMessage());
                }
            }
        } catch (CallStateException e) {
            Rlog.e("GsmCdmaCallTracker", "hangupConnectionByIndex caught " + e);
        }
    }

    public GsmCdmaConnection getConnectionByIndex(GsmCdmaCall gsmCdmaCall, int i) throws CallStateException {
        ArrayList<Connection> connections = gsmCdmaCall.getConnections();
        int size = connections.size();
        int i2 = 0;
        while (i2 < size) {
            Connection connection = connections.get(i2);
            i2++;
            GsmCdmaConnection gsmCdmaConnection = (GsmCdmaConnection) connection;
            if (!gsmCdmaConnection.mDisconnected && gsmCdmaConnection.getGsmCdmaIndex() == i) {
                return gsmCdmaConnection;
            }
        }
        return null;
    }

    private PhoneInternalInterface.SuppService getFailedService(int i) {
        if (i == 8) {
            return PhoneInternalInterface.SuppService.SWITCH;
        }
        switch (i) {
            case 11:
                return PhoneInternalInterface.SuppService.CONFERENCE;
            case 12:
                return PhoneInternalInterface.SuppService.SEPARATE;
            case 13:
                return PhoneInternalInterface.SuppService.TRANSFER;
            default:
                return PhoneInternalInterface.SuppService.UNKNOWN;
        }
    }

    /* JADX WARN: Code duplicated, block: B:83:0x0148  */
    @Override // com.android.internal.telephony.CallTracker, android.os.Handler
    public void handleMessage(Message message) {
        int i;
        int baseStationId;
        Connection latestConnection;
        Connection latestConnection2;
        int i2 = message.what;
        String string = null;
        if (i2 == 1) {
            if (message == this.mLastRelevantPoll) {
                this.mNeedsPoll = false;
                this.mLastRelevantPoll = null;
                handlePollCalls((AsyncResult) message.obj);
                return;
            }
            return;
        }
        if (i2 == 2 || i2 == 3) {
            pollCallsWhenSafe();
            return;
        }
        if (i2 == 4) {
            operationComplete();
            return;
        }
        if (i2 == 5) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            operationComplete();
            Throwable th = asyncResult.exception;
            if (th != null) {
                i = 16;
                if (th instanceof CommandException) {
                    CommandException commandException = (CommandException) th;
                    int i3 = AnonymousClass3.$SwitchMap$com$android$internal$telephony$CommandException$Error[commandException.getCommandError().ordinal()];
                    if (i3 == 1 || i3 == 2 || i3 == 3 || i3 == 4) {
                        string = commandException.getCommandError().toString();
                        i = 65535;
                    }
                } else {
                    Rlog.i("GsmCdmaCallTracker", "Exception during getLastCallFailCause, assuming normal disconnect");
                }
            } else {
                LastCallFailCause lastCallFailCause = (LastCallFailCause) asyncResult.result;
                i = lastCallFailCause.causeCode;
                string = lastCallFailCause.vendorCause;
            }
            if (i == 34 || i == 41 || i == 42 || i == 44 || i == 49 || i == 58 || i == 65535) {
                CellLocation cellLocationAsCellLocation = this.mPhone.getCurrentCellIdentity().asCellLocation();
                if (cellLocationAsCellLocation == null) {
                    baseStationId = -1;
                } else if (cellLocationAsCellLocation instanceof GsmCellLocation) {
                    baseStationId = ((GsmCellLocation) cellLocationAsCellLocation).getCid();
                } else if (cellLocationAsCellLocation instanceof CdmaCellLocation) {
                    baseStationId = ((CdmaCellLocation) cellLocationAsCellLocation).getBaseStationId();
                } else {
                    baseStationId = -1;
                }
                EventLog.writeEvent(EventLogTags.CALL_DROP, Integer.valueOf(i), Integer.valueOf(baseStationId), Integer.valueOf(TelephonyManager.getDefault().getNetworkType()));
            }
            if (isEmcRetryCause(i) && this.mPhone.useImsForEmergency()) {
                ArrayList<Connection> arrayList = this.mForegroundCall.mConnections;
                int size = arrayList.size();
                String str = "";
                int i4 = 0;
                while (i4 < size) {
                    Connection connection = arrayList.get(i4);
                    i4++;
                    GsmCdmaConnection gsmCdmaConnection = (GsmCdmaConnection) connection;
                    String origDialString = gsmCdmaConnection.getOrigDialString();
                    gsmCdmaConnection.getCall().detach(gsmCdmaConnection);
                    this.mDroppedDuringPoll.remove(gsmCdmaConnection);
                    str = origDialString;
                }
                this.mPhone.notifyVolteSilentRedial(str, i);
                updatePhoneState();
                if (this.mDroppedDuringPoll.isEmpty()) {
                    log("LAST_CALL_FAIL_CAUSE - no Dropped normal Call");
                    return;
                }
            }
            int size2 = this.mDroppedDuringPoll.size();
            for (int i5 = 0; i5 < size2; i5++) {
                ((GsmCdmaConnection) this.mDroppedDuringPoll.get(i5)).onRemoteDisconnect(i, string);
            }
            this.mHasMissedCall = false;
            updatePhoneState();
            this.mPhone.notifyPreciseCallStateChanged();
            this.mPhone.getVoiceCallSessionStats().onRilCallListChanged(this.mDroppedDuringPoll);
            this.mDroppedDuringPoll.clear();
            return;
        }
        if (i2 != 19 && i2 != 2023) {
            switch (i2) {
                case 8:
                case 12:
                case 13:
                    break;
                case 9:
                    IHyperTeleImsManager iHyperTeleImsManager = this.mIHyperTeleImsManager;
                    if (iHyperTeleImsManager != null && iHyperTeleImsManager.isVowifiConnected(this.mPhone.getPhoneId())) {
                        Rlog.i("GsmCdmaCallTracker", "Event EVENT_RADIO_AVAILABLE PhoneId = " + this.mPhone.getPhoneId() + " is Vowifi Connected.");
                        return;
                    }
                    handleRadioAvailable();
                    return;
                case 10:
                    handleRadioNotAvailable();
                    return;
                case 11:
                    if (((AsyncResult) message.obj).exception != null && (latestConnection2 = this.mForegroundCall.getLatestConnection()) != null) {
                        latestConnection2.onConferenceMergeFailed();
                    }
                    break;
                case 14:
                    break;
                default:
                    NetUtils$$ExternalSyntheticBUOutline0.m("unexpected event ", message.what, " not handled by phone type ", this.mPhone.getPhoneType());
                    return;
            }
            if (((AsyncResult) message.obj).exception != null) {
                if (message.what == 8 && (latestConnection = this.mForegroundCall.getLatestConnection()) != null) {
                    if (this.mBackgroundCall.getState() != Call.State.HOLDING) {
                        latestConnection.onConnectionEvent("android.telecom.event.CALL_HOLD_FAILED", null);
                    } else {
                        latestConnection.onConnectionEvent("android.telecom.event.CALL_SWITCH_FAILED", null);
                    }
                }
                this.mPhone.notifySuppServiceFailed(getFailedService(message.what));
            }
            operationComplete();
            return;
        }
        log("EVENT_RIL_CONNECTED " + this.mPendingOperations);
        this.mPendingOperations = 0;
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.GsmCdmaCallTracker$3, reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$CommandException$Error;

        static {
            int[] iArr = new int[CommandException.Error.values().length];
            $SwitchMap$com$android$internal$telephony$CommandException$Error = iArr;
            try {
                iArr[CommandException.Error.RADIO_NOT_AVAILABLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.NO_MEMORY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.INTERNAL_ERR.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.NO_RESOURCES.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public void dispatchCsCallRadioTech(int i) {
        GsmCdmaConnection[] gsmCdmaConnectionArr = this.mConnections;
        if (gsmCdmaConnectionArr == null) {
            log("dispatchCsCallRadioTech: mConnections is null");
            return;
        }
        for (GsmCdmaConnection gsmCdmaConnection : gsmCdmaConnectionArr) {
            if (gsmCdmaConnection != null) {
                gsmCdmaConnection.setCallRadioTech(i);
            }
        }
    }

    private Connection checkMtFindNewRinging(DriverCall driverCall, int i) {
        if (this.mConnections[i].getCall() == this.mRingingCall) {
            GsmCdmaConnection gsmCdmaConnection = this.mConnections[i];
            log("Notify new ring " + driverCall);
            return gsmCdmaConnection;
        }
        Rlog.e("GsmCdmaCallTracker", "Phantom call appeared " + driverCall);
        DriverCall.State state = driverCall.state;
        if (state != DriverCall.State.ALERTING && state != DriverCall.State.DIALING) {
            this.mConnections[i].onConnectedInOrOut();
            if (driverCall.state == DriverCall.State.HOLDING) {
                this.mConnections[i].onStartedHolding();
            }
        }
        return null;
    }

    @Override // com.android.internal.telephony.CallTracker
    public GsmCdmaPhone getPhone() {
        return this.mPhone;
    }

    private boolean isEmcRetryCause(int i) {
        if (!DomainSelectionResolver.getInstance().isDomainSelectionSupported()) {
            return i == 3001 || i == 3002;
        }
        log("isEmcRetryCause AP based domain selection ignores the cause");
        return false;
    }

    @Override // com.android.internal.telephony.CallTracker
    protected void log(String str) {
        Rlog.d("GsmCdmaCallTracker", "[" + this.mPhone.getPhoneId() + "] " + str);
    }

    @Override // com.android.internal.telephony.CallTracker
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("GsmCdmaCallTracker extends:");
        super.dump(fileDescriptor, printWriter, strArr);
        printWriter.println("mConnections: length=" + this.mConnections.length);
        for (int i = 0; i < this.mConnections.length; i++) {
            printWriter.printf("  mConnections[%d]=%s\n", Integer.valueOf(i), this.mConnections[i]);
        }
        printWriter.println(" mVoiceCallEndedRegistrants=" + this.mVoiceCallEndedRegistrants);
        printWriter.println(" mVoiceCallStartedRegistrants=" + this.mVoiceCallStartedRegistrants);
        printWriter.println(" mDroppedDuringPoll: size=" + this.mDroppedDuringPoll.size());
        for (int i2 = 0; i2 < this.mDroppedDuringPoll.size(); i2++) {
            printWriter.printf("  mDroppedDuringPoll[%d]=%s\n", Integer.valueOf(i2), this.mDroppedDuringPoll.get(i2));
        }
        printWriter.println(" mRingingCall=" + this.mRingingCall);
        printWriter.println(" mForegroundCall=" + this.mForegroundCall);
        printWriter.println(" mBackgroundCall=" + this.mBackgroundCall);
        printWriter.println(" mPendingMO=" + this.mPendingMO);
        printWriter.println(" mHangupPendingMO=" + this.mHangupPendingMO);
        printWriter.println(" mPhone=" + this.mPhone);
        printWriter.println(" mDesiredMute=" + this.mDesiredMute);
        printWriter.println(" mState=" + this.mState);
    }

    @Override // com.android.internal.telephony.CallTracker
    public PhoneConstants.State getState() {
        return this.mState;
    }

    @Override // com.android.internal.telephony.CallTracker
    public void cleanupCalls() {
        pollCallsWhenSafe();
    }

    private boolean hangupWaitingCallSilently(int i) {
        GsmCdmaConnection gsmCdmaConnection;
        if (i >= 0) {
            GsmCdmaConnection[] gsmCdmaConnectionArr = this.mConnections;
            if (i < gsmCdmaConnectionArr.length && (gsmCdmaConnection = gsmCdmaConnectionArr[i]) != null && this.mPhone.getTerminalBasedCallWaitingState(true) == 0 && gsmCdmaConnection.getState() == Call.State.WAITING) {
                Rlog.d("GsmCdmaCallTracker", "hangupWaitingCallSilently");
                gsmCdmaConnection.dispose();
                this.mConnections[i] = null;
                this.mCi.hangupWaitingOrBackground(obtainCompleteMessage());
                return true;
            }
        }
        return false;
    }

    protected GsmCdmaConnection onCreateGsmCdmaConnection(GsmCdmaPhone gsmCdmaPhone, String str, GsmCdmaCallTracker gsmCdmaCallTracker, GsmCdmaCall gsmCdmaCall, PhoneInternalInterface.DialArgs dialArgs) {
        return new GsmCdmaConnection(gsmCdmaPhone, str, gsmCdmaCallTracker, gsmCdmaCall, dialArgs);
    }

    protected GsmCdmaConnection onCreateGsmCdmaConnection(GsmCdmaPhone gsmCdmaPhone, DriverCall driverCall, GsmCdmaCallTracker gsmCdmaCallTracker, int i) {
        return new GsmCdmaConnection(gsmCdmaPhone, driverCall, gsmCdmaCallTracker, i);
    }

    protected void onCreateGsmCdmaConnectionForMismatch(DriverCall driverCall, GsmCdmaCallTracker gsmCdmaCallTracker, int i) {
        this.mConnections[i] = new GsmCdmaConnection(this.mPhone, driverCall, this, i);
    }
}
