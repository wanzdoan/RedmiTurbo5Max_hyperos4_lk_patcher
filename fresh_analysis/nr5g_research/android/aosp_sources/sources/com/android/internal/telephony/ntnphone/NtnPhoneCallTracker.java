package com.android.internal.telephony.ntnphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.media.AudioManager;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.MiuiTelephonyManagerStub;
import android.telephony.TelephonyManager;
import android.util.EventLog;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallManager$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CallTracker;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.EcbmHandler;
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.GsmCdmaCallTracker$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.LastCallFailCause;
import com.android.internal.telephony.MiuiCallControllerStub;
import com.android.internal.telephony.NtnRIL;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline2;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneInternalInterface;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.satellite.SatelliteSessionController;
import com.android.internal.telephony.util.NetUtils$$ExternalSyntheticBUOutline0;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class NtnPhoneCallTracker extends CallTracker {
    public static final int MAX_CONNECTIONS_GSM = 4;
    private int m3WayCallFlashDelay;
    public NtnPhoneCall mBackgroundCall;
    private RegistrantList mCallWaitingRegistrants;
    public NtnPhoneConnection[] mConnections;
    public NtnPhoneConnection[] mConnectionsDisconnected;
    private boolean mDesiredMute;
    private ArrayList mDroppedDuringPoll;
    private BroadcastReceiver mEcmExitReceiver;
    public NtnPhoneCall mForegroundCall;
    private boolean mHangupPendingMO;
    private int mIsChannelState;
    private boolean mIsDataConnectionActive;
    private boolean mIsInEmergencyCall;
    public NtnRIL mNtnRil;
    private int mPendingCallClirMode;
    private boolean mPendingCallInEcm;
    private boolean mPendingCallInSchedle;
    private UUSInfo mPendingCallUusInfo;
    private boolean mPendingExitEcbmReq;
    private boolean mPendingExitScbmReq;
    private NtnPhoneConnection mPendingMO;
    private NtnPhone mPhone;
    public NtnPhoneCall mRingingCall;
    public PhoneConstants.State mState;
    private RegistrantList mVoiceCallEndedRegistrants;
    private RegistrantList mVoiceCallStartedRegistrants;

    public void setIsInEmergencyCall() {
    }

    public NtnPhoneCallTracker(NtnPhone ntnPhone, FeatureFlags featureFlags) {
        super(featureFlags);
        this.mVoiceCallEndedRegistrants = new RegistrantList();
        this.mVoiceCallStartedRegistrants = new RegistrantList();
        this.mDroppedDuringPoll = new ArrayList(4);
        this.mRingingCall = new NtnPhoneCall(this);
        this.mForegroundCall = new NtnPhoneCall(this);
        this.mBackgroundCall = new NtnPhoneCall(this);
        this.mDesiredMute = false;
        this.mState = PhoneConstants.State.IDLE;
        this.mCallWaitingRegistrants = new RegistrantList();
        this.mIsChannelState = 0;
        this.mEcmExitReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.ntnphone.NtnPhoneCallTracker.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED")) {
                    int i = 0;
                    boolean booleanExtra = intent.getBooleanExtra("android.telephony.extra.PHONE_IN_ECM_STATE", false);
                    NtnPhoneCallTracker.this.log("Received ACTION_EMERGENCY_CALLBACK_MODE_CHANGED isInEcm = " + booleanExtra);
                    if (booleanExtra) {
                        return;
                    }
                    ArrayList arrayList = new ArrayList();
                    arrayList.addAll(NtnPhoneCallTracker.this.mRingingCall.getConnections());
                    arrayList.addAll(NtnPhoneCallTracker.this.mForegroundCall.getConnections());
                    arrayList.addAll(NtnPhoneCallTracker.this.mBackgroundCall.getConnections());
                    if (NtnPhoneCallTracker.this.mPendingMO != null) {
                        arrayList.add(NtnPhoneCallTracker.this.mPendingMO);
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
        this.mPhone = ntnPhone;
        CommandsInterface commandsInterface = ntnPhone.mCi;
        this.mCi = commandsInterface;
        commandsInterface.registerForCallStateChanged(this, 2, null);
        this.mCi.registerForOn(this, 9, null);
        this.mCi.registerForNotAvailable(this, 10, null);
        this.mCi.setOnCallRing(this, NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING, null);
        NtnRIL ntnRIL = ntnPhone.mNtnRil;
        this.mNtnRil = ntnRIL;
        ntnRIL.registerChannelState(this, NtnRILConstants.MI_NTN_RIL_REQUEST_ISIM_AUTHENTICATION, null);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED");
        this.mPhone.getContext().registerReceiver(this.mEcmExitReceiver, intentFilter, 2);
        updatePhoneType(true);
        MiuiCallControllerStub.get(this.mPhone.getPhoneId()).init(ntnPhone, new Object[0]);
    }

    public void updatePhoneType() {
        updatePhoneType(false);
    }

    private void updatePhoneType(boolean z) {
        if (!z) {
            reset();
            pollCallsWhenSafe();
        }
        if (this.mPhone.isPhoneTypeGsm()) {
            this.mConnections = new NtnPhoneConnection[4];
            this.mConnectionsDisconnected = new NtnPhoneConnection[4];
            return;
        }
        this.mConnections = new NtnPhoneConnection[2];
        this.mConnectionsDisconnected = new NtnPhoneConnection[2];
        this.mPendingCallInEcm = false;
        this.mIsInEmergencyCall = false;
        this.mPendingCallClirMode = 0;
        this.mPendingCallUusInfo = null;
        this.mPhone.setEcmCanceledForEmergency(false);
        this.m3WayCallFlashDelay = 0;
    }

    private void reset() {
        Rlog.d("NtnPhoneCallTracker", "reset");
        MiuiCallControllerStub.get(this.mPhone.getPhoneId()).removeAllHangupPendingConnectionIndex(this.mPhone);
        for (NtnPhoneConnection ntnPhoneConnection : this.mConnections) {
            if (ntnPhoneConnection != null) {
                ntnPhoneConnection.onDisconnect(36);
                ntnPhoneConnection.dispose();
            }
        }
        NtnPhoneConnection ntnPhoneConnection2 = this.mPendingMO;
        if (ntnPhoneConnection2 != null) {
            ntnPhoneConnection2.onDisconnect(36);
            this.mPendingMO.dispose();
        }
        this.mConnections = null;
        clearDisconnectedConnections();
        this.mConnectionsDisconnected = null;
        this.mPendingMO = null;
        clearDisconnected();
    }

    private boolean isPhoneInEcbm() {
        return EcbmHandler.getInstance() != null && EcbmHandler.getInstance().isInEcm();
    }

    private boolean canExitScbm() {
        return this.mPhone.isInScbm() && this.mPhone.isExitScbmFeatureSupported();
    }

    private boolean isPhoneInEmergencyMode() {
        return isPhoneInEcbm() || canExitScbm();
    }

    protected void finalize() {
        Rlog.d("NtnPhoneCallTracker", "NtnPhoneCallTracker finalized");
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
            ((NtnPhoneConnection) connection).fakeHoldBeforeDial();
        }
    }

    private void exitEmergencyMode() {
        boolean zIsPhoneInEcbm = isPhoneInEcbm();
        boolean zCanExitScbm = canExitScbm();
        if (zIsPhoneInEcbm) {
            EcbmHandler ecbmHandler = EcbmHandler.getInstance();
            try {
                ecbmHandler.exitEmergencyCallbackMode();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ecbmHandler.setOnEcbModeExitResponse(this, 14, null);
            this.mPendingExitEcbmReq = true;
        }
        if (zCanExitScbm) {
            try {
                this.mPhone.exitScbm();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            this.mPhone.setOnScbmExitResponse(this, 19, null);
            this.mPendingExitScbmReq = true;
        }
    }

    public synchronized Connection dialGsm(String str, PhoneInternalInterface.DialArgs dialArgs) throws Throwable {
        Throwable th;
        NtnPhoneConnection[] ntnPhoneConnectionArr;
        try {
            try {
                int i = dialArgs.clirMode;
                UUSInfo uUSInfo = dialArgs.uusInfo;
                Bundle bundle = dialArgs.intentExtras;
                boolean z = dialArgs.isEmergency;
                if (z) {
                    try {
                        log("dial gsm emergency call, set clirModIe=2");
                        i = 2;
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
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
                this.mPendingMO = new NtnPhoneConnection(this.mPhone, strConvertNumberIfNecessary, this, this.mForegroundCall, dialArgs);
                if (bundle != null) {
                    Rlog.d("NtnPhoneCallTracker", "dialGsm - emergency dialer: " + bundle.getBoolean("android.telecom.extra.IS_USER_INTENT_EMERGENCY_CALL"));
                    this.mPendingMO.setHasKnownUserIntentEmergency(bundle.getBoolean("android.telecom.extra.IS_USER_INTENT_EMERGENCY_CALL"));
                }
                this.mHangupPendingMO = false;
                this.mPhone.getVoiceCallSessionStats().onRilDial(this.mPendingMO);
                boolean z2 = this.mIsDataConnectionActive;
                if (z2 || ((ntnPhoneConnectionArr = this.mConnectionsDisconnected) != null && ntnPhoneConnectionArr.length > 0 && ntnPhoneConnectionArr[0] != null)) {
                    if (z2) {
                        Rlog.d("NtnPhoneCallTracker", "mIsDataConnectionActive = true , close satellite data.");
                        MiuiTelephonyManagerStub.get().setSatelliteDataEnableByUser(false);
                        if (this.mPendingMO != null && bundle != null) {
                            bundle.putBoolean("isCallBeforeSatelliteDataEnabled", true);
                            this.mPendingMO.setConnectionExtras(bundle);
                        }
                    }
                    this.mPendingCallClirMode = i;
                    this.mPendingCallUusInfo = uUSInfo;
                    this.mPendingCallInSchedle = true;
                    sendEmptyMessageDelayed(101, SatelliteSessionController.DEFAULT_SATELLITE_STAY_AT_LISTENING_FROM_RECEIVING_MILLIS);
                    log("Record pending mo call: " + this.mPendingMO);
                } else {
                    dial(i, this.mIsInEmergencyCall, uUSInfo);
                }
                if (this.mNumberConverted) {
                    this.mPendingMO.restoreDialedNumberAfterConversion(str);
                    this.mNumberConverted = false;
                }
                updatePhoneState();
                this.mPhone.notifyPreciseCallStateChanged();
                return this.mPendingMO;
            } catch (Throwable th3) {
                th = th3;
                th = th;
            }
        } catch (Throwable th4) {
            th = th4;
        }
        throw th;
    }

    public void updateDataConnectionState(boolean z) {
        this.mIsDataConnectionActive = z;
        Rlog.d("NtnPhoneCallTracker", "updateDataConnectionState = " + this.mIsDataConnectionActive);
    }

    public void updateChannelState(AsyncResult asyncResult) {
        if (asyncResult.exception == null) {
            this.mIsChannelState = ((Integer) asyncResult.result).intValue();
        }
        log("updateChannelState mIsChannelState = " + this.mIsChannelState + ", mIsDataConnectionActive = " + this.mIsDataConnectionActive + ", mPendingCallInSchedle = " + this.mPendingCallInSchedle);
        if (!this.mIsDataConnectionActive && this.mPendingCallInSchedle && isIdleForChannelState()) {
            sendEmptyMessage(NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE);
        }
    }

    public boolean isIdleForChannelState() {
        return this.mIsChannelState == 0;
    }

    public Connection dial(String str, PhoneInternalInterface.DialArgs dialArgs) throws CallStateException {
        return dialGsm(str, dialArgs);
    }

    public Connection dialGsm(String str, UUSInfo uUSInfo, Bundle bundle) throws CallStateException {
        return dialGsm(str, new PhoneInternalInterface.DialArgs.Builder().setUusInfo(uUSInfo).setClirMode(0).setIntentExtras(bundle).build());
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
            if (isPhoneTypeGsm()) {
                setMute(false);
            } else {
                NtnPhoneConnection ntnPhoneConnection = (NtnPhoneConnection) this.mRingingCall.getLatestConnection();
                ntnPhoneConnection.updateParent(this.mRingingCall, this.mForegroundCall);
                ntnPhoneConnection.onConnectedInOrOut();
                updatePhoneState();
            }
            switchWaitingOrHoldingAndActive();
            return;
        }
        CallManager$$ExternalSyntheticBUOutline0.m("phone not ringing");
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
        if (TelephonyProperties.disable_call().orElse(Boolean.FALSE).booleanValue()) {
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
        if (isPhoneTypeGsm() && this.mForegroundCall.getState().isAlive() && this.mBackgroundCall.getState().isAlive()) {
            Phone$$ExternalSyntheticBUOutline2.m(6, "There is already a foreground and background call.");
            return;
        }
        if (!isPhoneTypeGsm() && this.mForegroundCall.getState().isAlive() && this.mForegroundCall.getState() != Call.State.ACTIVE && this.mBackgroundCall.getState().isAlive()) {
            Phone$$ExternalSyntheticBUOutline2.m(6, "There is already a foreground and background call.");
        } else {
            if (z || !isInOtaspCall()) {
                return;
            }
            Phone$$ExternalSyntheticBUOutline2.m(7, "OTASP provisioning is in process.");
        }
    }

    public boolean canTransfer() {
        if (isPhoneTypeGsm()) {
            return (this.mForegroundCall.getState() == Call.State.ACTIVE || this.mForegroundCall.getState() == Call.State.ALERTING || this.mForegroundCall.getState() == Call.State.DIALING) && this.mBackgroundCall.getState() == Call.State.HOLDING;
        }
        Rlog.e("NtnPhoneCallTracker", "canTransfer: not possible in CDMA");
        return false;
    }

    private void internalClearDisconnected() {
        this.mRingingCall.clearDisconnected();
        this.mForegroundCall.clearDisconnected();
        this.mBackgroundCall.clearDisconnected();
    }

    private Message obtainCompleteMessage() {
        return obtainCompleteMessage(4);
    }

    private Message obtainCompleteMessage(int i) {
        this.mPendingOperations++;
        this.mLastRelevantPoll = null;
        this.mNeedsPoll = true;
        log("obtainCompleteMessage: pendingOperations=" + this.mPendingOperations + ", needsPoll=" + this.mNeedsPoll);
        return obtainMessage(i);
    }

    private void operationComplete() {
        this.mPendingOperations--;
        log("operationComplete: pendingOperations=" + this.mPendingOperations + ", needsPoll=" + this.mNeedsPoll);
        int i = this.mPendingOperations;
        if (i == 0 && this.mNeedsPoll) {
            Message messageObtainMessage = obtainMessage(1);
            this.mLastRelevantPoll = messageObtainMessage;
            this.mCi.getCurrentCalls(messageObtainMessage);
        } else if (i < 0) {
            Rlog.e("NtnPhoneCallTracker", "NtnPhoneCallTracker.pendingOperations < 0");
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

    /* JADX WARN: Code duplicated, block: B:111:0x02f8 A[Catch: all -> 0x0013, TryCatch #1 {all -> 0x0013, blocks: (B:4:0x0005, B:6:0x000e, B:12:0x0021, B:13:0x0037, B:15:0x003c, B:17:0x0040, B:19:0x004c, B:21:0x0050, B:24:0x0078, B:26:0x007c, B:28:0x0082, B:30:0x00aa, B:32:0x00b3, B:34:0x00bb, B:35:0x00c3, B:37:0x00df, B:124:0x0328, B:41:0x00ec, B:43:0x0137, B:45:0x0149, B:47:0x014d, B:49:0x0153, B:51:0x0162, B:53:0x016d, B:54:0x0173, B:56:0x0179, B:58:0x01a5, B:60:0x01c6, B:50:0x015b, B:61:0x01d2, B:63:0x01df, B:66:0x01e7, B:68:0x01ee, B:69:0x01f1, B:77:0x0202, B:79:0x0208, B:86:0x027a, B:80:0x020e, B:82:0x021b, B:83:0x0244, B:85:0x0251, B:89:0x028a, B:91:0x0290, B:93:0x0296, B:95:0x02b2, B:101:0x02c2, B:103:0x02c8, B:107:0x02d3, B:117:0x0304, B:119:0x030a, B:121:0x030e, B:123:0x0316, B:110:0x02e1, B:111:0x02f8, B:125:0x032e, B:127:0x0332, B:129:0x0336, B:132:0x0367, B:133:0x0373, B:135:0x0386, B:137:0x0395, B:139:0x039f, B:143:0x03a9, B:155:0x041b, B:161:0x0429, B:145:0x03e5, B:147:0x03eb, B:150:0x03f3, B:152:0x03fb, B:154:0x0409, B:162:0x042e, B:164:0x0434, B:165:0x0445, B:166:0x044b, B:168:0x0451, B:170:0x0481, B:172:0x048b, B:171:0x0486, B:173:0x048f, B:175:0x0497, B:180:0x04aa, B:182:0x04b4, B:184:0x04ba, B:186:0x04c1, B:187:0x04e3, B:193:0x04fa, B:195:0x0502, B:197:0x050a, B:199:0x050f, B:201:0x0513, B:191:0x04ee, B:179:0x04a7, B:9:0x0016, B:11:0x001c, B:204:0x051c), top: B:211:0x0005, inners: #0 }] */
    /* JADX WARN: Code duplicated, block: B:116:0x0303  */
    /* JADX WARN: Code duplicated, block: B:20:0x004f  */
    @Override // com.android.internal.telephony.CallTracker
    protected synchronized void handlePollCalls(AsyncResult asyncResult) {
        List arrayList;
        boolean z;
        NtnPhoneConnection ntnPhoneConnection;
        Phone imsPhone;
        boolean zOnDisconnect;
        boolean z2;
        NtnDriverCall ntnDriverCall;
        List list;
        NtnPhoneConnection[] ntnPhoneConnectionArr;
        try {
            log("handlePollCalls");
            Throwable th = asyncResult.exception;
            if (th == null) {
                arrayList = (List) asyncResult.result;
            } else if (isCommandExceptionRadioNotAvailable(th)) {
                arrayList = new ArrayList();
            } else {
                pollCallsAfterDelay();
                return;
            }
            ArrayList arrayList2 = new ArrayList();
            int size = this.mHandoverConnections.size();
            int size2 = arrayList.size();
            int i = 0;
            int i2 = 0;
            Connection connectionCheckMtFindNewRinging = null;
            boolean z3 = false;
            NtnPhoneConnection ntnPhoneConnection2 = null;
            boolean z4 = false;
            boolean z5 = false;
            while (true) {
                NtnPhoneConnection[] ntnPhoneConnectionArr2 = this.mConnections;
                if (i < ntnPhoneConnectionArr2.length) {
                    NtnPhoneConnection ntnPhoneConnection3 = ntnPhoneConnectionArr2[i];
                    if (i2 < size2) {
                        ntnDriverCall = (NtnDriverCall) arrayList.get(i2);
                        if (ntnDriverCall.index == i + 1) {
                            i2++;
                        } else {
                            ntnDriverCall = null;
                        }
                    } else {
                        ntnDriverCall = null;
                    }
                    log("poll: conn[i=" + i + "]=" + ntnPhoneConnection3 + ", dc=" + ntnDriverCall);
                    if (ntnPhoneConnection3 != null || ntnDriverCall == null) {
                        list = arrayList;
                        if (ntnPhoneConnection3 != null && ntnDriverCall == null) {
                            if (isPhoneTypeGsm()) {
                                this.mDroppedDuringPoll.add(ntnPhoneConnection3);
                            } else {
                                ArrayList<Connection> connections = this.mForegroundCall.getConnections();
                                int size3 = connections.size();
                                int i3 = 0;
                                while (i3 < size3) {
                                    Connection connection = connections.get(i3);
                                    i3++;
                                    Connection connection2 = connection;
                                    log("adding fgCall cn " + connection2 + "to droppedDuringPoll");
                                    this.mDroppedDuringPoll.add((NtnPhoneConnection) connection2);
                                }
                                ArrayList<Connection> connections2 = this.mRingingCall.getConnections();
                                int size4 = connections2.size();
                                int i4 = 0;
                                while (i4 < size4) {
                                    Connection connection3 = connections2.get(i4);
                                    i4++;
                                    Connection connection4 = connection3;
                                    log("adding rgCall cn " + connection4 + "to droppedDuringPoll");
                                    this.mDroppedDuringPoll.add((NtnPhoneConnection) connection4);
                                }
                            }
                            this.mConnections[i] = null;
                            clearDisconnectedConnections();
                            z5 = true;
                        } else if (ntnPhoneConnection3 != null && ntnDriverCall != null && !ntnPhoneConnection3.compareTo(ntnDriverCall) && isPhoneTypeGsm()) {
                            this.mDroppedDuringPoll.add(ntnPhoneConnection3);
                            this.mConnections[i] = new NtnPhoneConnection(this.mPhone, ntnDriverCall, this, i);
                            if (this.mConnections[i].getCall() == this.mRingingCall) {
                                connectionCheckMtFindNewRinging = this.mConnections[i];
                                if (hangupWaitingCallSilently(i)) {
                                    return;
                                }
                            }
                        } else if (ntnPhoneConnection3 != null && ntnDriverCall != null) {
                            if (isPhoneTypeGsm()) {
                                boolean zUpdate = ntnPhoneConnection3.update(ntnDriverCall);
                                if (z3) {
                                    z3 = true;
                                } else {
                                    z3 = true;
                                }
                            } else {
                                boolean zIsIncoming = ntnPhoneConnection3.isIncoming();
                                boolean z6 = ntnDriverCall.isMT;
                                if (zIsIncoming == z6) {
                                    boolean zUpdate2 = ntnPhoneConnection3.update(ntnDriverCall);
                                    if (z3 || zUpdate2) {
                                        z3 = true;
                                    } else {
                                        z3 = false;
                                    }
                                } else if (z6) {
                                    this.mDroppedDuringPoll.add(ntnPhoneConnection3);
                                    connectionCheckMtFindNewRinging = checkMtFindNewRinging(ntnDriverCall, i);
                                    if (connectionCheckMtFindNewRinging == null) {
                                        ntnPhoneConnection2 = ntnPhoneConnection3;
                                        z4 = true;
                                    }
                                } else {
                                    Rlog.e("NtnPhoneCallTracker", "Error in RIL, Phantom call appeared " + ntnDriverCall);
                                }
                            }
                            if (ntnDriverCall.state == NtnDriverCall.State.DISCONNECTED_REMOTE && (ntnPhoneConnectionArr = this.mConnectionsDisconnected) != null && ntnPhoneConnectionArr[i] != this.mConnections[i]) {
                                log("already disconnected to app, just get_current_call!");
                                this.mDroppedDuringPoll.add(ntnPhoneConnection3);
                                this.mConnectionsDisconnected[i] = this.mConnections[i];
                            }
                        }
                        i++;
                        arrayList = list;
                    } else {
                        NtnPhoneConnection ntnPhoneConnection4 = this.mPendingMO;
                        if (ntnPhoneConnection4 != null && ntnPhoneConnection4.compareTo(ntnDriverCall)) {
                            log("poll: pendingMO=" + this.mPendingMO);
                            NtnPhoneConnection[] ntnPhoneConnectionArr3 = this.mConnections;
                            NtnPhoneConnection ntnPhoneConnection5 = this.mPendingMO;
                            ntnPhoneConnectionArr3[i] = ntnPhoneConnection5;
                            ntnPhoneConnection5.mIndex = i;
                            ntnPhoneConnection5.update(ntnDriverCall);
                            this.mPendingMO = null;
                            if (this.mHangupPendingMO) {
                                this.mHangupPendingMO = false;
                                if (!isPhoneTypeGsm() && this.mPhone.isEcmCanceledForEmergency()) {
                                    EcbmHandler.getInstance().handleTimerInEmergencyCallbackMode(0);
                                }
                                try {
                                    log("poll: hangupPendingMO, hangup conn " + i);
                                    hangup(this.mConnections[i]);
                                } catch (CallStateException unused) {
                                    Rlog.e("NtnPhoneCallTracker", "unexpected error on hangup");
                                }
                                return;
                            }
                            list = arrayList;
                        } else {
                            log("pendingMo=" + this.mPendingMO + ", dc=" + ntnDriverCall);
                            this.mConnections[i] = new NtnPhoneConnection(this.mPhone, ntnDriverCall, this, i);
                            StringBuilder sb = new StringBuilder();
                            sb.append("New connection is not mPendingMO. Creating new NtnPhoneConnection, objId=");
                            sb.append(System.identityHashCode(this.mConnections[i]));
                            log(sb.toString());
                            Connection hoConnection = getHoConnection(ntnDriverCall);
                            if (hoConnection != null) {
                                log("Handover connection found.");
                                this.mConnections[i].migrateFrom(hoConnection);
                                Call.State state = hoConnection.mPreHandoverState;
                                if (state != Call.State.ACTIVE && state != Call.State.HOLDING && ntnDriverCall.state == NtnDriverCall.State.ACTIVE) {
                                    this.mConnections[i].onConnectedInOrOut();
                                } else {
                                    this.mConnections[i].onConnectedConnectionMigrated();
                                }
                                this.mHandoverConnections.remove(hoConnection);
                                if (isPhoneTypeGsm()) {
                                    Iterator<Connection> it = this.mHandoverConnections.iterator();
                                    while (it.hasNext()) {
                                        Connection next = it.next();
                                        StringBuilder sb2 = new StringBuilder();
                                        List list2 = arrayList;
                                        sb2.append("HO Conn state is ");
                                        sb2.append(next.mPreHandoverState);
                                        Rlog.i("NtnPhoneCallTracker", sb2.toString());
                                        if (next.mPreHandoverState == this.mConnections[i].getState()) {
                                            Rlog.i("NtnPhoneCallTracker", "Removing HO conn " + hoConnection + next.mPreHandoverState);
                                            it.remove();
                                        }
                                        arrayList = list2;
                                    }
                                }
                                list = arrayList;
                                this.mPhone.notifyHandoverStateChanged(this.mConnections[i]);
                            } else {
                                list = arrayList;
                                log("New connection is not mPendingMO nor a pending handover.");
                                connectionCheckMtFindNewRinging = checkMtFindNewRinging(ntnDriverCall, i);
                                if (connectionCheckMtFindNewRinging == null) {
                                    boolean zIsPhoneTypeGsm = isPhoneTypeGsm();
                                    NtnPhoneConnection[] ntnPhoneConnectionArr4 = this.mConnections;
                                    if (zIsPhoneTypeGsm) {
                                        arrayList2.add(ntnPhoneConnectionArr4[i]);
                                    } else {
                                        ntnPhoneConnection2 = ntnPhoneConnectionArr4[i];
                                    }
                                    z4 = true;
                                } else if (hangupWaitingCallSilently(i)) {
                                    return;
                                }
                            }
                        }
                    }
                    z3 = true;
                    i++;
                    arrayList = list;
                } else {
                    if (this.mPendingMO == null || this.mPendingCallInSchedle) {
                        z = false;
                        ntnPhoneConnection = null;
                    } else {
                        Rlog.d("NtnPhoneCallTracker", "Pending MO dropped before poll fg state:" + this.mForegroundCall.getState());
                        this.mDroppedDuringPoll.add(this.mPendingMO);
                        ntnPhoneConnection = null;
                        this.mPendingMO = null;
                        z = false;
                        this.mHangupPendingMO = false;
                        isPhoneTypeGsm();
                    }
                    if (connectionCheckMtFindNewRinging != null) {
                        connectionCheckMtFindNewRinging.setActiveCallDisconnectedOnAnswer(isPseudoDsdaCall());
                        this.mPhone.notifyNewRingingConnection(connectionCheckMtFindNewRinging);
                    }
                    ArrayList arrayList3 = new ArrayList();
                    boolean z7 = z;
                    for (int size5 = this.mDroppedDuringPoll.size() - 1; size5 >= 0; size5--) {
                        NtnPhoneConnection ntnPhoneConnection6 = (NtnPhoneConnection) this.mDroppedDuringPoll.get(size5);
                        if (ntnPhoneConnection6.isIncoming() && ntnPhoneConnection6.getConnectTime() == 0) {
                            int i5 = ntnPhoneConnection6.getCause() == 3 ? 16 : 1;
                            log("missed/rejected call, conn.cause=" + ntnPhoneConnection6.getCause());
                            log("setting cause to " + i5);
                            this.mDroppedDuringPoll.remove(size5);
                            zOnDisconnect = ntnPhoneConnection6.onDisconnect(i5) | z7;
                            arrayList3.add(ntnPhoneConnection6);
                        } else if (ntnPhoneConnection6.getCause() == 3 || ntnPhoneConnection6.getCause() == 7) {
                            this.mDroppedDuringPoll.remove(size5);
                            zOnDisconnect = ntnPhoneConnection6.onDisconnect(ntnPhoneConnection6.getCause()) | z7;
                            arrayList3.add(ntnPhoneConnection6);
                        } else {
                            if (ntnPhoneConnection6.getState() == Call.State.DISCONNECTED) {
                                zOnDisconnect = ntnPhoneConnection6.onDisconnect(2) | z7;
                                log("remote hangup for ntn call!!");
                            } else {
                                z2 = false;
                            }
                            if (isPhoneTypeGsm() && z2 && z4 && ntnPhoneConnection6 == ntnPhoneConnection2) {
                                ntnPhoneConnection2 = ntnPhoneConnection;
                                z4 = false;
                            }
                        }
                        z7 = zOnDisconnect;
                        z2 = true;
                        if (isPhoneTypeGsm()) {
                        }
                    }
                    if (arrayList3.size() > 0) {
                        ((NtnVoiceCallSessionStats) this.mPhone.getVoiceCallSessionStats()).onRilCallListChanged(arrayList3, this.mPhone.getPhoneId());
                    }
                    Iterator<Connection> it2 = this.mHandoverConnections.iterator();
                    while (it2.hasNext()) {
                        Connection next2 = it2.next();
                        log("handlePollCalls - disconnect hoConn= " + next2 + " hoConn.State= " + next2.getState());
                        if (next2.getState().isRinging()) {
                            next2.onDisconnect(1);
                        } else {
                            next2.onDisconnect(-1);
                        }
                        it2.remove();
                    }
                    if (this.mDroppedDuringPoll.size() > 0) {
                        this.mCi.getLastCallFailCause(obtainNoPollCompleteMessage(5));
                    }
                    if (connectionCheckMtFindNewRinging != null || z3 || z7) {
                        internalClearDisconnected();
                    }
                    log("handlePollCalls calling updatePhoneState()");
                    updatePhoneState();
                    if (z4) {
                        if (isPhoneTypeGsm()) {
                            int size6 = arrayList2.size();
                            int i6 = 0;
                            while (i6 < size6) {
                                Object obj = arrayList2.get(i6);
                                i6++;
                                Connection connection5 = (Connection) obj;
                                log("Notify unknown for " + connection5);
                                this.mPhone.notifyUnknownConnection(connection5);
                            }
                        } else {
                            this.mPhone.notifyUnknownConnection(ntnPhoneConnection2);
                        }
                    }
                    if (z3 || connectionCheckMtFindNewRinging != null || z7) {
                        this.mPhone.notifyPreciseCallStateChanged();
                        updateMetrics(this.mConnections);
                    }
                    if (size > 0 && this.mHandoverConnections.size() == 0 && (imsPhone = this.mPhone.getImsPhone()) != null) {
                        imsPhone.callEndCleanupHandOverCallIfAny();
                    }
                    if (z5 && this.mPendingCallInSchedle) {
                        sendEmptyMessageDelayed(102, 1000L);
                    }
                    return;
                }
            }
        } catch (Throwable th2) {
            throw th2;
        }
    }

    private void updateMetrics(NtnPhoneConnection[] ntnPhoneConnectionArr) {
        ArrayList arrayList = new ArrayList();
        for (NtnPhoneConnection ntnPhoneConnection : ntnPhoneConnectionArr) {
            if (ntnPhoneConnection != null) {
                arrayList.add(ntnPhoneConnection);
            }
        }
        ((NtnVoiceCallSessionStats) this.mPhone.getVoiceCallSessionStats()).onRilCallListChanged(arrayList, this.mPhone.getPhoneId());
    }

    private void handleRadioNotAvailable() {
        pollCallsWhenSafe();
    }

    public void hangup(NtnPhoneConnection ntnPhoneConnection) throws CallStateException {
        if (ntnPhoneConnection.mOwner != this) {
            GsmCdmaCallTracker$$ExternalSyntheticBUOutline0.m("NtnPhoneConnection ", ntnPhoneConnection, "does not belong to NtnPhoneCallTracker ", this);
            return;
        }
        if (ntnPhoneConnection == this.mPendingMO) {
            if (this.mPhone.isEcmCanceledForEmergency()) {
                EcbmHandler.getInstance().handleTimerInEmergencyCallbackMode(0);
            }
            if (!this.mPendingCallInSchedle) {
                log("hangup conn with callId '-1' as there is no DIAL response yet ");
                this.mCi.hangupConnection(-1, obtainCompleteMessage());
            } else {
                log("No need to hang up since the PendingMO is not actually dialed out.");
            }
        } else {
            if (!isPhoneTypeGsm()) {
                NtnPhoneCall call = ntnPhoneConnection.getCall();
                NtnPhoneCall ntnPhoneCall = this.mRingingCall;
                if (call == ntnPhoneCall && ntnPhoneCall.getState() == Call.State.WAITING) {
                    ntnPhoneConnection.onLocalDisconnect();
                    updatePhoneState();
                    this.mPhone.notifyPreciseCallStateChanged();
                    return;
                }
            }
            try {
                this.mCi.hangupConnection(ntnPhoneConnection.getNtnPhoneIndex(), obtainCompleteMessage());
            } catch (CallStateException unused) {
                Rlog.w("NtnPhoneCallTracker", "NtnPhoneCallTracker WARN: hangup() on absent connection " + ntnPhoneConnection);
            }
        }
        ntnPhoneConnection.onHangupLocal();
        updateFakeHangupState(ntnPhoneConnection, false);
    }

    public void separate(NtnPhoneConnection ntnPhoneConnection) throws CallStateException {
        if (ntnPhoneConnection.mOwner != this) {
            GsmCdmaCallTracker$$ExternalSyntheticBUOutline0.m("NtnPhoneConnection ", ntnPhoneConnection, "does not belong to NtnPhoneCallTracker ", this);
            return;
        }
        try {
            this.mCi.separateConnection(ntnPhoneConnection.getNtnPhoneIndex(), obtainCompleteMessage(12));
        } catch (CallStateException unused) {
            Rlog.w("NtnPhoneCallTracker", "NtnPhoneCallTracker WARN: separate() on absent connection " + ntnPhoneConnection);
        }
    }

    public void setMute(boolean z) {
        this.mDesiredMute = z;
        AudioManager audioManager = (AudioManager) this.mPhone.getContext().getSystemService("audio");
        if (audioManager != null) {
            audioManager.setParameters("tiantong_mute=" + this.mDesiredMute);
        }
    }

    public boolean getMute() {
        return this.mDesiredMute;
    }

    public void hangup(NtnPhoneCall ntnPhoneCall) throws CallStateException {
        if (ntnPhoneCall.getConnectionsCount() == 0) {
            CallManager$$ExternalSyntheticBUOutline0.m("no connections in call");
            return;
        }
        if (this.mPendingCallInSchedle) {
            log("No need to hang up since the PendingMO is not actually dialed out.");
        } else {
            NtnPhoneCall ntnPhoneCall2 = this.mRingingCall;
            if (ntnPhoneCall == ntnPhoneCall2) {
                log("(ringing) hangup waiting or background");
                logHangupEvent(ntnPhoneCall);
                hangup((NtnPhoneConnection) ntnPhoneCall.getConnections().get(0));
            } else if (ntnPhoneCall == this.mForegroundCall) {
                if (ntnPhoneCall.isDialingOrAlerting()) {
                    log("(foregnd) hangup dialing or alerting...");
                    hangup((NtnPhoneConnection) ntnPhoneCall.getConnections().get(0));
                } else if (isPhoneTypeGsm() && this.mRingingCall.isRinging()) {
                    log("hangup all conns in active/background call, without affecting ringing call");
                    hangupAllConnections(ntnPhoneCall);
                } else {
                    logHangupEvent(ntnPhoneCall);
                    hangup((NtnPhoneConnection) ntnPhoneCall.getConnections().get(0));
                }
            } else if (ntnPhoneCall == this.mBackgroundCall) {
                if (ntnPhoneCall2.isRinging()) {
                    log("hangup all conns in background call");
                    hangupAllConnections(ntnPhoneCall);
                } else {
                    hangup((NtnPhoneConnection) ntnPhoneCall.getConnections().get(0));
                }
            } else {
                throw new RuntimeException("NtnPhoneCall " + ntnPhoneCall + "does not belong to NtnPhoneCallTracker " + this);
            }
        }
        ntnPhoneCall.onHangupLocal();
        this.mPhone.notifyPreciseCallStateChanged();
        if (ntnPhoneCall.getConnections() == null || ntnPhoneCall.getConnections().isEmpty()) {
            log("No connection need to be disconnected for: " + ntnPhoneCall);
            return;
        }
        updateFakeHangupState((NtnPhoneConnection) ntnPhoneCall.getConnections().get(0), false);
    }

    private void logHangupEvent(NtnPhoneCall ntnPhoneCall) {
        ArrayList<Connection> connections = ntnPhoneCall.getConnections();
        int size = connections.size();
        int i = 0;
        while (i < size) {
            Connection connection = connections.get(i);
            i++;
            try {
                ((NtnPhoneConnection) connection).getNtnPhoneIndex();
            } catch (CallStateException unused) {
            }
        }
        Rlog.v("NtnPhoneCallTracker", "logHangupEvent logged " + ntnPhoneCall.getConnectionsCount() + " Connections ");
    }

    public void hangupWaitingOrBackground() {
        log("hangupWaitingOrBackground");
        throw new RuntimeException("NtnPhone only support one call, need check hangupWaitingOrBackground!!");
    }

    public void hangupForegroundResumeBackground() {
        log("hangupForegroundResumeBackground");
        throw new RuntimeException("NtnPhone only support one call, need check hangupForegroundResumeBackground!!");
    }

    public void hangupConnectionByIndex(NtnPhoneCall ntnPhoneCall, int i) throws CallStateException {
        ArrayList<Connection> connections = ntnPhoneCall.getConnections();
        int size = connections.size();
        int i2 = 0;
        while (i2 < size) {
            Connection connection = connections.get(i2);
            i2++;
            NtnPhoneConnection ntnPhoneConnection = (NtnPhoneConnection) connection;
            if (!ntnPhoneConnection.mDisconnected && ntnPhoneConnection.getNtnPhoneIndex() == i) {
                this.mCi.hangupConnection(i, obtainCompleteMessage());
                return;
            }
        }
        CallManager$$ExternalSyntheticBUOutline0.m("no NtnPhone index found");
    }

    public void hangupAllConnections(NtnPhoneCall ntnPhoneCall) {
        try {
            ArrayList<Connection> connections = ntnPhoneCall.getConnections();
            int size = connections.size();
            int i = 0;
            while (i < size) {
                Connection connection = connections.get(i);
                i++;
                NtnPhoneConnection ntnPhoneConnection = (NtnPhoneConnection) connection;
                if (!ntnPhoneConnection.mDisconnected) {
                    this.mCi.hangupConnection(ntnPhoneConnection.getNtnPhoneIndex(), obtainCompleteMessage());
                }
            }
        } catch (CallStateException e) {
            Rlog.e("NtnPhoneCallTracker", "hangupConnectionByIndex caught " + e);
        }
    }

    public NtnPhoneConnection getConnectionByIndex(NtnPhoneCall ntnPhoneCall, int i) throws CallStateException {
        ArrayList<Connection> connections = ntnPhoneCall.getConnections();
        int size = connections.size();
        int i2 = 0;
        while (i2 < size) {
            Connection connection = connections.get(i2);
            i2++;
            NtnPhoneConnection ntnPhoneConnection = (NtnPhoneConnection) connection;
            if (!ntnPhoneConnection.mDisconnected && ntnPhoneConnection.getNtnPhoneIndex() == i) {
                return ntnPhoneConnection;
            }
        }
        return null;
    }

    private void handlePendingMoCall() {
        if (this.mPendingMO == null) {
            log("handlePendingMoCall mPendingMO is null!!!");
            return;
        }
        if (this.mPendingCallInEcm && !this.mPendingExitEcbmReq && !this.mPendingExitScbmReq) {
            boolean zIsPhoneTypeGsm = isPhoneTypeGsm();
            CommandsInterface commandsInterface = this.mCi;
            if (zIsPhoneTypeGsm) {
                commandsInterface.dial(this.mPendingMO.getAddress(), this.mPendingMO.isEmergencyCall(), this.mPendingMO.getEmergencyNumberInfo(), this.mPendingMO.hasKnownUserIntentEmergency(), this.mPendingCallClirMode, this.mPendingCallUusInfo, null);
            } else {
                commandsInterface.dial(this.mPendingMO.getAddress(), this.mPendingMO.isEmergencyCall(), this.mPendingMO.getEmergencyNumberInfo(), this.mPendingMO.hasKnownUserIntentEmergency(), this.mPendingCallClirMode, null);
            }
            this.mPendingCallInEcm = false;
            return;
        }
        if (isPhoneTypeGsm() && this.mPendingCallInSchedle) {
            this.mPendingCallInSchedle = false;
            removeMessages(101);
            dial(this.mPendingCallClirMode, this.mPendingMO.isEmergencyCall(), this.mPendingCallUusInfo);
            log("Dial pending MO call. " + this.mPendingMO);
        }
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

    @Override // com.android.internal.telephony.CallTracker, android.os.Handler
    public void handleMessage(Message message) {
        int i;
        Connection latestConnection;
        Connection latestConnection2;
        int i2 = message.what;
        String string = null;
        if (i2 == 1) {
            Rlog.d("NtnPhoneCallTracker", "Event EVENT_POLL_CALLS_RESULT Received");
            if (message == this.mLastRelevantPoll) {
                log("handle EVENT_POLL_CALL_RESULT: set needsPoll=F");
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
                    int i3 = AnonymousClass2.$SwitchMap$com$android$internal$telephony$CommandException$Error[commandException.getCommandError().ordinal()];
                    if (i3 == 1 || i3 == 2 || i3 == 3 || i3 == 4) {
                        string = commandException.getCommandError().toString();
                        i = 65535;
                    }
                } else {
                    Rlog.i("NtnPhoneCallTracker", "Exception during getLastCallFailCause, assuming normal disconnect");
                }
            } else {
                LastCallFailCause lastCallFailCause = (LastCallFailCause) asyncResult.result;
                i = lastCallFailCause.causeCode;
                string = lastCallFailCause.vendorCause;
            }
            Rlog.i("NtnPhoneCallTracker", "getLastCallFailCause, causeCode:" + i);
            if (i == 34 || i == 41 || i == 42 || i == 44 || i == 49 || i == 58 || i == 65535) {
                EventLog.writeEvent(EventLogTags.CALL_DROP, Integer.valueOf(i), -1, Integer.valueOf(TelephonyManager.getDefault().getNetworkType()));
            }
            int size = this.mDroppedDuringPoll.size();
            for (int i4 = 0; i4 < size; i4++) {
                ((NtnPhoneConnection) this.mDroppedDuringPoll.get(i4)).onRemoteDisconnect(i, string);
            }
            updatePhoneState();
            this.mPhone.notifyPreciseCallStateChanged();
            ((NtnVoiceCallSessionStats) this.mPhone.getVoiceCallSessionStats()).onRilCallListChanged(this.mDroppedDuringPoll, this.mPhone.getPhoneId());
            this.mDroppedDuringPoll.clear();
            return;
        }
        if (i2 != 19) {
            switch (i2) {
                case 8:
                case 12:
                case 13:
                    break;
                case 9:
                    handleRadioAvailable();
                    return;
                case 10:
                    handleRadioNotAvailable();
                    return;
                case 11:
                    if (isPhoneTypeGsm() && ((AsyncResult) message.obj).exception != null && (latestConnection2 = this.mForegroundCall.getLatestConnection()) != null) {
                        latestConnection2.onConferenceMergeFailed();
                    }
                    break;
                case 14:
                    this.mPendingExitEcbmReq = false;
                    handlePendingMoCall();
                    EcbmHandler.getInstance().unsetOnEcbModeExitResponse(this);
                    return;
                default:
                    switch (i2) {
                        case 101:
                            if (this.mPendingCallInSchedle) {
                                this.mPendingCallInSchedle = false;
                                clearConnections();
                                updateFakeHangupState(this.mPendingMO, true);
                                checkPendingOperations();
                                log("The call is not dialed out after timeout, hangup directly. " + this.mPendingMO);
                            }
                            break;
                        case 102:
                            handlePendingMoCall();
                            break;
                        case NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING /* 103 */:
                            log("Event EVENT_CALL_RING Received. ");
                            clearConnections();
                            checkPendingOperations();
                            break;
                        case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                            log("EVENT_CLOSE_DATA_TO_ACTUAL_CALL Received.");
                            handlePendingMoCall();
                            break;
                        case NtnRILConstants.MI_NTN_RIL_REQUEST_ISIM_AUTHENTICATION /* 105 */:
                            log("Event EVENT_CHANNEL_STATE_CHANGE Received. ");
                            updateChannelState((AsyncResult) message.obj);
                            break;
                        default:
                            NetUtils$$ExternalSyntheticBUOutline0.m("unexpected event ", message.what, " not handled by phone type ", this.mPhone.getPhoneType());
                            break;
                    }
                    return;
            }
            if (isPhoneTypeGsm()) {
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
            if (message.what == 8) {
                return;
            }
            NetUtils$$ExternalSyntheticBUOutline0.m("unexpected event ", message.what, " not handled by phone type ", this.mPhone.getPhoneType());
            return;
        }
        this.mPendingExitScbmReq = false;
        handlePendingMoCall();
        this.mPhone.unsetOnScbmExitResponse(this);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.ntnphone.NtnPhoneCallTracker$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
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
        NtnPhoneConnection[] ntnPhoneConnectionArr = this.mConnections;
        if (ntnPhoneConnectionArr == null) {
            log("dispatchCsCallRadioTech: mConnections is null");
            return;
        }
        for (NtnPhoneConnection ntnPhoneConnection : ntnPhoneConnectionArr) {
            if (ntnPhoneConnection != null) {
                ntnPhoneConnection.setCallRadioTech(i);
            }
        }
    }

    private Connection checkMtFindNewRinging(NtnDriverCall ntnDriverCall, int i) {
        if (this.mConnections[i].getCall() == this.mRingingCall) {
            NtnPhoneConnection ntnPhoneConnection = this.mConnections[i];
            log("Notify new ring " + ntnDriverCall);
            return ntnPhoneConnection;
        }
        Rlog.e("NtnPhoneCallTracker", "Phantom call appeared " + ntnDriverCall);
        NtnDriverCall.State state = ntnDriverCall.state;
        if (state != NtnDriverCall.State.ALERTING && state != NtnDriverCall.State.DIALING) {
            this.mConnections[i].onConnectedInOrOut();
            if (ntnDriverCall.state == NtnDriverCall.State.HOLDING) {
                this.mConnections[i].onStartedHolding();
            }
        }
        return null;
    }

    public boolean isInEmergencyCall() {
        return this.mIsInEmergencyCall;
    }

    public boolean isInOtaspCall() {
        NtnPhoneConnection ntnPhoneConnection = this.mPendingMO;
        return (ntnPhoneConnection != null && ntnPhoneConnection.isOtaspCall()) || this.mForegroundCall.getConnections().stream().filter(new Predicate() { // from class: com.android.internal.telephony.ntnphone.NtnPhoneCallTracker$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return NtnPhoneCallTracker.$r8$lambda$S7JG3nKFWc5FXqIkAJMxrAADJrc((Connection) obj);
            }
        }).count() > 0;
    }

    public static /* synthetic */ boolean $r8$lambda$S7JG3nKFWc5FXqIkAJMxrAADJrc(Connection connection) {
        return (connection instanceof NtnPhoneConnection) && ((NtnPhoneConnection) connection).isOtaspCall();
    }

    private boolean isPhoneTypeGsm() {
        return this.mPhone.getPhoneType() == 1 || this.mPhone.getPhoneType() == 7;
    }

    @Override // com.android.internal.telephony.CallTracker
    public NtnPhone getPhone() {
        return this.mPhone;
    }

    @Override // com.android.internal.telephony.CallTracker
    protected void log(String str) {
        Rlog.d("NtnPhoneCallTracker", "[" + this.mPhone.getPhoneId() + "] " + str);
    }

    @Override // com.android.internal.telephony.CallTracker
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("NtnPhoneCallTracker extends:");
        super.dump(fileDescriptor, printWriter, strArr);
        printWriter.println("mConnections: length=" + this.mConnections.length);
        for (int i = 0; i < this.mConnections.length; i++) {
            printWriter.printf("  mConnections[%d]=%s\n", Integer.valueOf(i), this.mConnections[i]);
        }
        printWriter.println(" mVoiceCallEndedRegistrants=" + this.mVoiceCallEndedRegistrants);
        printWriter.println(" mVoiceCallStartedRegistrants=" + this.mVoiceCallStartedRegistrants);
        if (!isPhoneTypeGsm()) {
            printWriter.println(" mCallWaitingRegistrants=" + this.mCallWaitingRegistrants);
        }
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
        if (isPhoneTypeGsm()) {
            return;
        }
        printWriter.println(" mPendingCallInEcm=" + this.mPendingCallInEcm);
        printWriter.println(" mIsInEmergencyCall=" + this.mIsInEmergencyCall);
        printWriter.println(" mPendingCallClirMode=" + this.mPendingCallClirMode);
    }

    @Override // com.android.internal.telephony.CallTracker
    public PhoneConstants.State getState() {
        return this.mState;
    }

    public int getMaxConnectionsPerCall() {
        return this.mPhone.isPhoneTypeGsm() ? 2 : 1;
    }

    @Override // com.android.internal.telephony.CallTracker
    public void cleanupCalls() {
        pollCallsWhenSafe();
    }

    private boolean hangupWaitingCallSilently(int i) {
        NtnPhoneConnection ntnPhoneConnection;
        if (i >= 0) {
            NtnPhoneConnection[] ntnPhoneConnectionArr = this.mConnections;
            if (i < ntnPhoneConnectionArr.length && (ntnPhoneConnection = ntnPhoneConnectionArr[i]) != null && this.mPhone.getTerminalBasedCallWaitingState(true) == 0 && ntnPhoneConnection.getState() == Call.State.WAITING) {
                Rlog.d("NtnPhoneCallTracker", "hangupWaitingCallSilently");
                ntnPhoneConnection.dispose();
                this.mConnections[i] = null;
                this.mCi.hangupWaitingOrBackground(obtainCompleteMessage());
                return true;
            }
        }
        return false;
    }

    private boolean clearDisconnectedConnections() {
        int i = 0;
        if (this.mConnectionsDisconnected == null) {
            return false;
        }
        boolean z = false;
        while (true) {
            NtnPhoneConnection[] ntnPhoneConnectionArr = this.mConnectionsDisconnected;
            if (i >= ntnPhoneConnectionArr.length) {
                return z;
            }
            if (ntnPhoneConnectionArr[i] != null) {
                Rlog.d("NtnPhoneCallTracker", "clearDisconnectedConnections mConnectionsDisconnected[i]: " + this.mConnectionsDisconnected[i]);
                this.mConnectionsDisconnected[i].dispose();
                this.mConnectionsDisconnected[i] = null;
                z = true;
            }
            i++;
        }
    }

    private void updateFakeHangupState(NtnPhoneConnection ntnPhoneConnection, boolean z) {
        if (ntnPhoneConnection == null) {
            log("No connection need to be disconnected!!!");
            return;
        }
        ntnPhoneConnection.onDisconnect((ntnPhoneConnection.isIncoming() && ntnPhoneConnection.getConnectTime() == 0) ? 16 : 3);
        if (this.mConnectionsDisconnected != null && !z && hasValidConnections()) {
            this.mConnectionsDisconnected[0] = ntnPhoneConnection;
        } else {
            clearDisconnectedConnections();
        }
        this.mPendingMO = null;
        clearDisconnected();
        log("Quick hangup!!! " + ntnPhoneConnection);
    }

    private void dial(int i, boolean z, UUSInfo uUSInfo) {
        if (this.mPendingMO.getAddress() == null || this.mPendingMO.getAddress().length() == 0 || this.mPendingMO.getAddress().indexOf(78) >= 0) {
            this.mPendingMO.setCause(7);
            pollCallsWhenSafe();
            return;
        }
        setMute(false);
        boolean zIsPhoneInEmergencyMode = isPhoneInEmergencyMode();
        if (!zIsPhoneInEmergencyMode || (zIsPhoneInEmergencyMode && z)) {
            this.mCi.dial(this.mPendingMO.getAddress(), this.mPendingMO.isEmergencyCall(), this.mPendingMO.getEmergencyNumberInfo(), this.mPendingMO.hasKnownUserIntentEmergency(), i, null);
            return;
        }
        exitEmergencyMode();
        this.mPendingCallClirMode = i;
        this.mPendingCallUusInfo = uUSInfo;
        this.mPendingCallInEcm = true;
    }

    private void clearConnections() {
        if (this.mConnections == null) {
            return;
        }
        int i = 0;
        while (true) {
            NtnPhoneConnection[] ntnPhoneConnectionArr = this.mConnections;
            if (i >= ntnPhoneConnectionArr.length) {
                return;
            }
            NtnPhoneConnection ntnPhoneConnection = ntnPhoneConnectionArr[i];
            if (ntnPhoneConnection != null && ntnPhoneConnection.getState() == Call.State.DISCONNECTED) {
                Rlog.d("NtnPhoneCallTracker", "clearConnections mConnections[i]: " + this.mConnections[i]);
                this.mConnections[i].dispose();
                this.mConnections[i] = null;
            }
            i++;
        }
    }

    private void checkPendingOperations() {
        if (this.mPendingOperations > 0 && this.mForegroundCall.isIdle() && this.mBackgroundCall.isIdle() && this.mRingingCall.isIdle()) {
            this.mPendingOperations = 0;
            log("Clear pending operations. ");
        }
    }

    private boolean hasValidConnections() {
        if (this.mConnections == null) {
            return false;
        }
        int i = 0;
        while (true) {
            NtnPhoneConnection[] ntnPhoneConnectionArr = this.mConnections;
            if (i >= ntnPhoneConnectionArr.length) {
                return false;
            }
            if (ntnPhoneConnectionArr[i] != null) {
                return true;
            }
            i++;
        }
    }
}
