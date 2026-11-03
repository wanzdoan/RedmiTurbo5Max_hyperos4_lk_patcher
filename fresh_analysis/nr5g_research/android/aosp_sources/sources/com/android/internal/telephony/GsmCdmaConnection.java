package com.android.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import com.android.internal.telephony.emergency.EmergencyNumberTracker;
import com.android.internal.telephony.satellite.SatelliteSessionController;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Collections;

/* JADX INFO: loaded from: classes.dex */
public class GsmCdmaConnection extends Connection {
    public static final String OTASP_NUMBER = "*22899";
    long mDisconnectTime;
    boolean mDisconnected;
    private int mDtmfToneDelay;
    Handler mHandler;
    public int mIndex;
    Connection mOrigConnection;
    protected GsmCdmaCallTracker mOwner;
    GsmCdmaCall mParent;
    private PowerManager.WakeLock mPartialWakeLock;
    int mPreciseCause;
    UUSInfo mUusInfo;
    String mVendorCause;

    private int getAudioQualityFromDC(int i) {
        return (i == 2 || i == 9) ? 2 : 1;
    }

    private static boolean isPause(char c) {
        return c == ',';
    }

    private static boolean isWait(char c) {
        return c == ';';
    }

    private static boolean isWild(char c) {
        return c == 'N';
    }

    public boolean isOtaspCall() {
        return false;
    }

    class MyHandler extends Handler {
        MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 1) {
                if (i != 2 && i != 3) {
                    if (i == 4) {
                        GsmCdmaConnection.this.releaseWakeLock();
                        return;
                    } else if (i != 5) {
                        return;
                    }
                }
                GsmCdmaConnection.this.processNextPostDialChar();
                return;
            }
            Handler handler = GsmCdmaConnection.this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(5), GsmCdmaConnection.this.mDtmfToneDelay);
        }
    }

    public GsmCdmaConnection(GsmCdmaPhone gsmCdmaPhone, DriverCall driverCall, GsmCdmaCallTracker gsmCdmaCallTracker, int i) {
        super(gsmCdmaPhone.getPhoneType());
        this.mPreciseCause = 0;
        this.mDtmfToneDelay = 0;
        createWakeLock(gsmCdmaPhone.getContext());
        acquireWakeLock();
        this.mOwner = gsmCdmaCallTracker;
        this.mHandler = new MyHandler(this.mOwner.getLooper());
        this.mAddress = driverCall.number;
        setEmergencyCallInfo(this.mOwner, null);
        String str = TextUtils.isEmpty(driverCall.forwardedNumber) ? null : driverCall.forwardedNumber;
        Rlog.i("GsmCdmaConnection", "create, forwardedNumber=" + Rlog.pii("GsmCdmaConnection", str));
        this.mForwardedNumber = str != null ? new ArrayList<>(Collections.singletonList(driverCall.forwardedNumber)) : null;
        this.mIsIncoming = driverCall.isMT;
        this.mCreateTime = System.currentTimeMillis();
        this.mCnapName = driverCall.name;
        this.mCnapNamePresentation = driverCall.namePresentation;
        this.mNumberPresentation = driverCall.numberPresentation;
        this.mUusInfo = driverCall.uusInfo;
        this.mIndex = i;
        GsmCdmaCall gsmCdmaCallParentFromDCState = parentFromDCState(driverCall.state);
        this.mParent = gsmCdmaCallParentFromDCState;
        gsmCdmaCallParentFromDCState.attach(this, driverCall);
        fetchDtmfToneDelay(gsmCdmaPhone);
        setAudioQuality(getAudioQualityFromDC(driverCall.audioQuality));
        setCallRadioTech(this.mOwner.getPhone().getCsCallRadioTech());
    }

    public GsmCdmaConnection(GsmCdmaPhone gsmCdmaPhone, String str, GsmCdmaCallTracker gsmCdmaCallTracker, GsmCdmaCall gsmCdmaCall, PhoneInternalInterface.DialArgs dialArgs) {
        super(gsmCdmaPhone.getPhoneType());
        this.mPreciseCause = 0;
        this.mDtmfToneDelay = 0;
        createWakeLock(gsmCdmaPhone.getContext());
        acquireWakeLock();
        this.mOwner = gsmCdmaCallTracker;
        this.mHandler = new MyHandler(this.mOwner.getLooper());
        this.mDialString = str;
        this.mAddress = PhoneNumberUtils.extractNetworkPortionAlt(str);
        if (dialArgs.isEmergency) {
            setEmergencyCallInfo(this.mOwner, dialArgs);
            if (getEmergencyNumberInfo() == null && !TelephonyCommonUtilsStub.get().isMiuiRom()) {
                setNonDetectableEmergencyCallInfo(dialArgs.eccCategory, new ArrayList());
            }
        }
        this.mPostDialString = PhoneNumberUtils.extractPostDialPortion(str);
        this.mIndex = -1;
        this.mIsIncoming = false;
        this.mCnapName = null;
        this.mCnapNamePresentation = 1;
        this.mNumberPresentation = 1;
        this.mCreateTime = System.currentTimeMillis();
        if (gsmCdmaCall != null) {
            this.mParent = gsmCdmaCall;
            gsmCdmaCall.attachFake(this, Call.State.DIALING);
        }
        fetchDtmfToneDelay(gsmCdmaPhone);
        setCallRadioTech(this.mOwner.getPhone().getCsCallRadioTech());
    }

    public void dispose() {
        clearPostDialListeners();
        GsmCdmaCall gsmCdmaCall = this.mParent;
        if (gsmCdmaCall != null) {
            gsmCdmaCall.detach(this);
        }
        releaseAllWakeLocks();
    }

    static boolean equalsHandlesNulls(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        }
        return obj.equals(obj2);
    }

    static boolean equalsBaseDialString(String str, String str2) {
        if (str == null) {
            return str2 == null;
        }
        return str2 != null && str.startsWith(str2);
    }

    public boolean compareTo(DriverCall driverCall) {
        if ((this.mIsIncoming || driverCall.isMT) && this.mOrigConnection == null) {
            return this.mIsIncoming == driverCall.isMT && equalsHandlesNulls(this.mAddress, PhoneNumberUtils.stringFromStringAndTOA(driverCall.number, driverCall.TOA));
        }
        return true;
    }

    @Override // com.android.internal.telephony.Connection
    public String getOrigDialString() {
        return this.mDialString;
    }

    @Override // com.android.internal.telephony.Connection
    public GsmCdmaCall getCall() {
        return this.mParent;
    }

    @Override // com.android.internal.telephony.Connection
    public long getDisconnectTime() {
        return this.mDisconnectTime;
    }

    @Override // com.android.internal.telephony.Connection
    public long getHoldDurationMillis() {
        if (getState() != Call.State.HOLDING) {
            return 0L;
        }
        return SystemClock.elapsedRealtime() - this.mHoldingStartTime;
    }

    @Override // com.android.internal.telephony.Connection
    public Call.State getState() {
        if (this.mDisconnected) {
            return Call.State.DISCONNECTED;
        }
        return super.getState();
    }

    @Override // com.android.internal.telephony.Connection
    public void hangup() throws CallStateException {
        if (!this.mDisconnected) {
            this.mOwner.hangup(this);
        } else {
            CallManager$$ExternalSyntheticBUOutline0.m("disconnected");
        }
    }

    @Override // com.android.internal.telephony.Connection
    public void deflect(String str) throws CallStateException {
        throw new CallStateException("deflect is not supported for CS");
    }

    @Override // com.android.internal.telephony.Connection
    public void transfer(String str, boolean z) throws CallStateException {
        throw new CallStateException("Transfer is not supported for CS");
    }

    @Override // com.android.internal.telephony.Connection
    public void consultativeTransfer(Connection connection) throws CallStateException {
        throw new CallStateException("Transfer is not supported for CS");
    }

    @Override // com.android.internal.telephony.Connection
    public void separate() throws CallStateException {
        if (!this.mDisconnected) {
            this.mOwner.separate(this);
        } else {
            CallManager$$ExternalSyntheticBUOutline0.m("disconnected");
        }
    }

    @Override // com.android.internal.telephony.Connection
    public void proceedAfterWaitChar() {
        if (this.mPostDialState != Connection.PostDialState.WAIT) {
            Rlog.w("GsmCdmaConnection", "GsmCdmaConnection.proceedAfterWaitChar(): Expected getPostDialState() to be WAIT but was " + this.mPostDialState);
        } else {
            setPostDialState(Connection.PostDialState.STARTED);
            processNextPostDialChar();
        }
    }

    @Override // com.android.internal.telephony.Connection
    public void proceedAfterWildChar(String str) {
        if (this.mPostDialState != Connection.PostDialState.WILD) {
            Rlog.w("GsmCdmaConnection", "GsmCdmaConnection.proceedAfterWaitChar(): Expected getPostDialState() to be WILD but was " + this.mPostDialState);
            return;
        }
        setPostDialState(Connection.PostDialState.STARTED);
        this.mPostDialString = str + this.mPostDialString.substring(this.mNextPostDialChar);
        this.mNextPostDialChar = 0;
        log("proceedAfterWildChar: new postDialString is " + this.mPostDialString);
        processNextPostDialChar();
    }

    @Override // com.android.internal.telephony.Connection
    public void cancelPostDial() {
        setPostDialState(Connection.PostDialState.CANCELLED);
    }

    void onHangupLocal() {
        this.mCause = 3;
        this.mPreciseCause = 0;
        this.mVendorCause = null;
    }

    public int disconnectCauseFromCode(int i) {
        IccCardApplicationStatus.AppState state;
        int iDisconnectCauseFromCode = TelephonyCommonUtilsStub.get().disconnectCauseFromCode(i);
        if (iDisconnectCauseFromCode != -1) {
            return iDisconnectCauseFromCode;
        }
        if (i == 41 || i == 42) {
            return 5;
        }
        if (i == 240) {
            return 20;
        }
        if (i == 241) {
            return 21;
        }
        if (i == 325) {
            return 63;
        }
        if (i == 326) {
            return 64;
        }
        switch (i) {
            case 1:
                return 25;
            case 8:
                return 20;
            case 17:
                return 4;
            case 19:
                return 13;
            case 31:
                return 65;
            case 34:
            case 44:
            case 49:
            case 58:
                return 5;
            case 68:
                return 15;
            case CallFailCause.NO_VALID_SIM /* 249 */:
                return 19;
            case CallFailCause.LOCAL_NETWORK_NO_SERVICE /* 1207 */:
            case CallFailCause.LOCAL_SERVICE_UNAVAILABLE /* 1211 */:
                return 18;
            default:
                switch (i) {
                    case 243:
                        return 58;
                    case 244:
                        return 46;
                    case 245:
                        return 47;
                    case 246:
                        return 48;
                    case CallFailCause.RADIO_OFF /* 247 */:
                        return 17;
                    default:
                        switch (i) {
                            case 1000:
                                return 26;
                            case 1001:
                                return 27;
                            case 1002:
                                return 28;
                            case 1003:
                                return 29;
                            case 1004:
                                return 30;
                            case 1005:
                                return 31;
                            case 1006:
                                return 32;
                            case 1007:
                                return 33;
                            case 1008:
                                return 34;
                            case 1009:
                                return 35;
                            default:
                                GsmCdmaPhone phone = this.mOwner.getPhone();
                                int state2 = phone.getServiceState().getState();
                                UiccCardApplication uiccCardApplication = phone.getUiccCardApplication();
                                if (uiccCardApplication != null) {
                                    state = uiccCardApplication.getState();
                                } else {
                                    state = IccCardApplicationStatus.AppState.APPSTATE_UNKNOWN;
                                }
                                if (state2 == 3) {
                                    return 17;
                                }
                                if (!isEmergencyCall()) {
                                    if (state2 == 1 || state2 == 2) {
                                        return 18;
                                    }
                                    if (state != IccCardApplicationStatus.AppState.APPSTATE_READY) {
                                        return 19;
                                    }
                                }
                                if (i == 65535 || i == 260) {
                                    if (phone.mSST.mRestrictedState.isCsRestricted()) {
                                        return 22;
                                    }
                                    if (phone.mSST.mRestrictedState.isCsEmergencyRestricted()) {
                                        return 24;
                                    }
                                    if (phone.mSST.mRestrictedState.isCsNormalRestricted()) {
                                        return 23;
                                    }
                                }
                                return i == 16 ? 2 : 36;
                        }
                }
        }
    }

    void onRemoteDisconnect(int i, String str) {
        this.mPreciseCause = i;
        this.mVendorCause = str;
        onDisconnect(disconnectCauseFromCode(i));
    }

    @Override // com.android.internal.telephony.Connection
    public boolean onDisconnect(int i) {
        this.mCause = i;
        boolean zConnectionDisconnected = false;
        if (!this.mDisconnected) {
            doDisconnect();
            Rlog.d("GsmCdmaConnection", "onDisconnect: cause=" + i);
            this.mOwner.getPhone().notifyDisconnect(this);
            notifyDisconnect(i);
            GsmCdmaCall gsmCdmaCall = this.mParent;
            zConnectionDisconnected = gsmCdmaCall != null ? gsmCdmaCall.connectionDisconnected(this) : false;
            this.mOrigConnection = null;
        }
        clearPostDialListeners();
        releaseWakeLock();
        return zConnectionDisconnected;
    }

    /* JADX WARN: Code duplicated, block: B:21:0x0072  */
    /* JADX WARN: Code duplicated, block: B:23:0x0075  */
    /* JADX WARN: Code duplicated, block: B:24:0x0078  */
    /* JADX WARN: Code duplicated, block: B:28:0x008d  */
    /* JADX WARN: Code duplicated, block: B:31:0x00a7  */
    /* JADX WARN: Code duplicated, block: B:32:0x00a9  */
    /* JADX WARN: Code duplicated, block: B:36:0x00c5  */
    /* JADX WARN: Code duplicated, block: B:39:0x00d8  */
    /* JADX WARN: Code duplicated, block: B:42:0x00e8  */
    /* JADX WARN: Code duplicated, block: B:44:0x00f0  */
    /* JADX WARN: Code duplicated, block: B:46:0x00f6  */
    /* JADX WARN: Code duplicated, block: B:48:0x0100  */
    /* JADX WARN: Code duplicated, block: B:51:0x0124 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:52:0x0126  */
    /* JADX WARN: Code duplicated, block: B:55:0x0130  */
    /* JADX WARN: Code duplicated, block: B:63:0x0150  */
    /* JADX WARN: Code duplicated, block: B:66:0x0181  */
    /* JADX WARN: Code duplicated, block: B:70:0x018c A[ADDED_TO_REGION] */
    public boolean update(DriverCall driverCall) {
        boolean z;
        int audioQualityFromDC;
        int i;
        String str;
        ArrayList<String> arrayList;
        GsmCdmaCall gsmCdmaCall;
        boolean z2;
        String str2;
        boolean zIsConnectingInOrOut = isConnectingInOrOut();
        Call.State state = getState();
        Call.State state2 = Call.State.HOLDING;
        boolean z3 = state == state2;
        GsmCdmaCall gsmCdmaCallParentFromDCState = parentFromDCState(driverCall.state);
        log("parent= " + this.mParent + ", newParent= " + gsmCdmaCallParentFromDCState);
        if (this.mOrigConnection != null) {
            log("update: mOrigConnection is not null");
        } else {
            if (isIncoming() && !equalsBaseDialString(this.mAddress, driverCall.number) && (!this.mNumberConverted || !equalsBaseDialString(this.mConvertedNumber, driverCall.number))) {
                log("update: phone # changed!");
                this.mAddress = driverCall.number;
                z = true;
            }
            audioQualityFromDC = getAudioQualityFromDC(driverCall.audioQuality);
            if (getAudioQuality() != audioQualityFromDC) {
                if (audioQualityFromDC == 2) {
                    str2 = "high";
                } else {
                    str2 = "standard";
                }
                log("update: audioQuality # changed!:  ".concat(str2));
                setAudioQuality(audioQualityFromDC);
                z = true;
            }
            i = driverCall.audioQuality;
            if (i != this.mAudioCodec) {
                this.mAudioCodec = i;
                this.mOwner.getPhone().getVoiceCallSessionStats().onAudioCodecChanged(this, driverCall.audioQuality);
            }
            if (TextUtils.isEmpty(driverCall.forwardedNumber)) {
                str = null;
            } else {
                str = driverCall.forwardedNumber;
            }
            Rlog.i("GsmCdmaConnection", "update: forwardedNumber=" + Rlog.pii("GsmCdmaConnection", str));
            arrayList = str != null ? new ArrayList<>(Collections.singletonList(driverCall.forwardedNumber)) : null;
            if (!equalsHandlesNulls(this.mForwardedNumber, arrayList)) {
                log("update: mForwardedNumber, # changed");
                this.mForwardedNumber = arrayList;
                z = true;
            }
            if (TextUtils.isEmpty(driverCall.name)) {
                if (!TextUtils.isEmpty(this.mCnapName)) {
                    this.mCnapName = "";
                    z = true;
                }
            } else if (!driverCall.name.equals(this.mCnapName)) {
                this.mCnapName = driverCall.name;
                z = true;
            }
            log("--dssds----" + this.mCnapName);
            this.mCnapNamePresentation = driverCall.namePresentation;
            this.mNumberPresentation = driverCall.numberPresentation;
            gsmCdmaCall = this.mParent;
            if (gsmCdmaCallParentFromDCState != gsmCdmaCall) {
                if (gsmCdmaCall != null) {
                    gsmCdmaCall.detach(this);
                }
                gsmCdmaCallParentFromDCState.attach(this, driverCall);
                this.mParent = gsmCdmaCallParentFromDCState;
            } else {
                boolean zUpdate = gsmCdmaCall.update(this, driverCall);
                if (z && !zUpdate) {
                    z2 = false;
                }
                StringBuilder sb = new StringBuilder("update: parent=");
                sb.append(this.mParent);
                sb.append(", hasNewParent=");
                sb.append(gsmCdmaCallParentFromDCState != this.mParent);
                sb.append(", wasConnectingInOrOut=");
                sb.append(zIsConnectingInOrOut);
                sb.append(", wasHolding=");
                sb.append(z3);
                sb.append(", isConnectingInOrOut=");
                sb.append(isConnectingInOrOut());
                sb.append(", changed=");
                sb.append(z2);
                log(sb.toString());
                if (zIsConnectingInOrOut && !isConnectingInOrOut()) {
                    onConnectedInOrOut();
                }
                if (z2 && !z3 && getState() == state2) {
                    onStartedHolding();
                }
                return z2;
            }
            z2 = true;
            StringBuilder sb2 = new StringBuilder("update: parent=");
            sb2.append(this.mParent);
            sb2.append(", hasNewParent=");
            sb2.append(gsmCdmaCallParentFromDCState != this.mParent);
            sb2.append(", wasConnectingInOrOut=");
            sb2.append(zIsConnectingInOrOut);
            sb2.append(", wasHolding=");
            sb2.append(z3);
            sb2.append(", isConnectingInOrOut=");
            sb2.append(isConnectingInOrOut());
            sb2.append(", changed=");
            sb2.append(z2);
            log(sb2.toString());
            if (zIsConnectingInOrOut) {
                onConnectedInOrOut();
            }
            if (z2) {
                onStartedHolding();
            }
            return z2;
        }
        z = false;
        audioQualityFromDC = getAudioQualityFromDC(driverCall.audioQuality);
        if (getAudioQuality() != audioQualityFromDC) {
            if (audioQualityFromDC == 2) {
                str2 = "high";
            } else {
                str2 = "standard";
            }
            log("update: audioQuality # changed!:  ".concat(str2));
            setAudioQuality(audioQualityFromDC);
            z = true;
        }
        i = driverCall.audioQuality;
        if (i != this.mAudioCodec) {
            this.mAudioCodec = i;
            this.mOwner.getPhone().getVoiceCallSessionStats().onAudioCodecChanged(this, driverCall.audioQuality);
        }
        if (TextUtils.isEmpty(driverCall.forwardedNumber)) {
            str = null;
        } else {
            str = driverCall.forwardedNumber;
        }
        Rlog.i("GsmCdmaConnection", "update: forwardedNumber=" + Rlog.pii("GsmCdmaConnection", str));
        if (str != null) {
        }
        if (!equalsHandlesNulls(this.mForwardedNumber, arrayList)) {
            log("update: mForwardedNumber, # changed");
            this.mForwardedNumber = arrayList;
            z = true;
        }
        if (TextUtils.isEmpty(driverCall.name)) {
            if (!TextUtils.isEmpty(this.mCnapName)) {
                this.mCnapName = "";
                z = true;
            }
        } else if (!driverCall.name.equals(this.mCnapName)) {
            this.mCnapName = driverCall.name;
            z = true;
        }
        log("--dssds----" + this.mCnapName);
        this.mCnapNamePresentation = driverCall.namePresentation;
        this.mNumberPresentation = driverCall.numberPresentation;
        gsmCdmaCall = this.mParent;
        if (gsmCdmaCallParentFromDCState != gsmCdmaCall) {
            if (gsmCdmaCall != null) {
                gsmCdmaCall.detach(this);
            }
            gsmCdmaCallParentFromDCState.attach(this, driverCall);
            this.mParent = gsmCdmaCallParentFromDCState;
        } else {
            boolean zUpdate2 = gsmCdmaCall.update(this, driverCall);
            if (z) {
            }
            StringBuilder sb3 = new StringBuilder("update: parent=");
            sb3.append(this.mParent);
            sb3.append(", hasNewParent=");
            sb3.append(gsmCdmaCallParentFromDCState != this.mParent);
            sb3.append(", wasConnectingInOrOut=");
            sb3.append(zIsConnectingInOrOut);
            sb3.append(", wasHolding=");
            sb3.append(z3);
            sb3.append(", isConnectingInOrOut=");
            sb3.append(isConnectingInOrOut());
            sb3.append(", changed=");
            sb3.append(z2);
            log(sb3.toString());
            if (zIsConnectingInOrOut) {
                onConnectedInOrOut();
            }
            if (z2) {
                onStartedHolding();
            }
            return z2;
        }
        z2 = true;
        StringBuilder sb4 = new StringBuilder("update: parent=");
        sb4.append(this.mParent);
        sb4.append(", hasNewParent=");
        sb4.append(gsmCdmaCallParentFromDCState != this.mParent);
        sb4.append(", wasConnectingInOrOut=");
        sb4.append(zIsConnectingInOrOut);
        sb4.append(", wasHolding=");
        sb4.append(z3);
        sb4.append(", isConnectingInOrOut=");
        sb4.append(isConnectingInOrOut());
        sb4.append(", changed=");
        sb4.append(z2);
        log(sb4.toString());
        if (zIsConnectingInOrOut) {
            onConnectedInOrOut();
        }
        if (z2) {
            onStartedHolding();
        }
        return z2;
    }

    void fakeHoldBeforeDial() {
        GsmCdmaCall gsmCdmaCall = this.mParent;
        if (gsmCdmaCall != null) {
            gsmCdmaCall.detach(this);
        }
        GsmCdmaCall gsmCdmaCall2 = this.mOwner.mBackgroundCall;
        this.mParent = gsmCdmaCall2;
        gsmCdmaCall2.attachFake(this, Call.State.HOLDING);
        onStartedHolding();
    }

    public int getGsmCdmaIndex() throws CallStateException {
        int i = this.mIndex;
        if (i >= 0) {
            return i + 1;
        }
        CallManager$$ExternalSyntheticBUOutline0.m("GsmCdma index not yet assigned");
        return 0;
    }

    void onConnectedInOrOut() {
        this.mConnectTime = System.currentTimeMillis();
        this.mConnectTimeReal = SystemClock.elapsedRealtime();
        this.mDuration = 0L;
        log("onConnectedInOrOut: connectTime=" + this.mConnectTime);
        if (!this.mIsIncoming) {
            processNextPostDialChar();
        } else {
            releaseWakeLock();
        }
    }

    void onConnectedConnectionMigrated() {
        releaseWakeLock();
    }

    private void doDisconnect() {
        this.mIndex = -1;
        this.mDisconnectTime = System.currentTimeMillis();
        this.mDuration = SystemClock.elapsedRealtime() - this.mConnectTimeReal;
        this.mDisconnected = true;
        clearPostDialListeners();
    }

    void onStartedHolding() {
        this.mHoldingStartTime = SystemClock.elapsedRealtime();
    }

    private boolean processPostDialChar(char c) {
        if (PhoneNumberUtils.is12Key(c)) {
            this.mOwner.mCi.sendDtmf(c, this.mHandler.obtainMessage(1));
        } else if (isPause(c)) {
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(2), SatelliteSessionController.DEMO_MODE_SATELLITE_STAY_AT_LISTENING_MILLIS);
        } else if (isWait(c)) {
            setPostDialState(Connection.PostDialState.WAIT);
        } else {
            if (!isWild(c)) {
                return false;
            }
            setPostDialState(Connection.PostDialState.WILD);
        }
        return true;
    }

    @Override // com.android.internal.telephony.Connection
    public String getRemainingPostDialString() {
        return super.getRemainingPostDialString();
    }

    protected void finalize() {
        PowerManager.WakeLock wakeLock = this.mPartialWakeLock;
        if (wakeLock != null && wakeLock.isHeld()) {
            Rlog.e("GsmCdmaConnection", "UNEXPECTED; mPartialWakeLock is held when finalizing.");
        }
        clearPostDialListeners();
        releaseWakeLock();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNextPostDialChar() {
        char cCharAt;
        Message messageMessageForRegistrant;
        if (this.mPostDialState == Connection.PostDialState.CANCELLED) {
            releaseWakeLock();
            return;
        }
        String str = this.mPostDialString;
        if (str == null || str.length() <= this.mNextPostDialChar) {
            setPostDialState(Connection.PostDialState.COMPLETE);
            releaseWakeLock();
            cCharAt = 0;
        } else {
            setPostDialState(Connection.PostDialState.STARTED);
            String str2 = this.mPostDialString;
            int i = this.mNextPostDialChar;
            this.mNextPostDialChar = i + 1;
            cCharAt = str2.charAt(i);
            if (!processPostDialChar(cCharAt)) {
                this.mHandler.obtainMessage(3).sendToTarget();
                Rlog.e("GsmCdmaConnection", "processNextPostDialChar: c=" + cCharAt + " isn't valid!");
                return;
            }
        }
        notifyPostDialListenersNextChar(cCharAt);
        Registrant postDialHandler = this.mOwner.getPhone().getPostDialHandler();
        if (postDialHandler == null || (messageMessageForRegistrant = postDialHandler.messageForRegistrant()) == null) {
            return;
        }
        Connection.PostDialState postDialState = this.mPostDialState;
        AsyncResult asyncResultForMessage = AsyncResult.forMessage(messageMessageForRegistrant);
        asyncResultForMessage.result = this;
        asyncResultForMessage.userObj = postDialState;
        messageMessageForRegistrant.arg1 = cCharAt;
        messageMessageForRegistrant.sendToTarget();
    }

    private boolean isConnectingInOrOut() {
        Call.State state;
        GsmCdmaCall gsmCdmaCall = this.mParent;
        return gsmCdmaCall == null || gsmCdmaCall == this.mOwner.mRingingCall || (state = gsmCdmaCall.mState) == Call.State.DIALING || state == Call.State.ALERTING;
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.GsmCdmaConnection$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$DriverCall$State;

        static {
            int[] iArr = new int[DriverCall.State.values().length];
            $SwitchMap$com$android$internal$telephony$DriverCall$State = iArr;
            try {
                iArr[DriverCall.State.ACTIVE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$DriverCall$State[DriverCall.State.DIALING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$DriverCall$State[DriverCall.State.ALERTING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$DriverCall$State[DriverCall.State.HOLDING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$DriverCall$State[DriverCall.State.INCOMING.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$DriverCall$State[DriverCall.State.WAITING.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    private GsmCdmaCall parentFromDCState(DriverCall.State state) {
        switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$DriverCall$State[state.ordinal()]) {
            case 1:
            case 2:
            case 3:
                return this.mOwner.mForegroundCall;
            case 4:
                return this.mOwner.mBackgroundCall;
            case 5:
            case 6:
                return this.mOwner.mRingingCall;
            default:
                Call$$ExternalSyntheticBUOutline0.m("illegal call state: ", state);
                return null;
        }
    }

    private void setPostDialState(Connection.PostDialState postDialState) {
        if (postDialState == Connection.PostDialState.STARTED || postDialState == Connection.PostDialState.PAUSE) {
            synchronized (this.mPartialWakeLock) {
                try {
                    if (this.mPartialWakeLock.isHeld()) {
                        this.mHandler.removeMessages(4);
                    } else {
                        acquireWakeLock();
                    }
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(4), 60000L);
                } catch (Throwable th) {
                    throw th;
                }
            }
        } else {
            this.mHandler.removeMessages(4);
            releaseWakeLock();
        }
        this.mPostDialState = postDialState;
        notifyPostDialListeners();
    }

    private void createWakeLock(Context context) {
        this.mPartialWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, "GsmCdmaConnection");
    }

    private void acquireWakeLock() {
        PowerManager.WakeLock wakeLock = this.mPartialWakeLock;
        if (wakeLock != null) {
            synchronized (wakeLock) {
                log("acquireWakeLock");
                this.mPartialWakeLock.acquire();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseWakeLock() {
        PowerManager.WakeLock wakeLock = this.mPartialWakeLock;
        if (wakeLock != null) {
            synchronized (wakeLock) {
                try {
                    if (this.mPartialWakeLock.isHeld()) {
                        log("releaseWakeLock");
                        this.mPartialWakeLock.release();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private void releaseAllWakeLocks() {
        PowerManager.WakeLock wakeLock = this.mPartialWakeLock;
        if (wakeLock != null) {
            synchronized (wakeLock) {
                while (this.mPartialWakeLock.isHeld()) {
                    try {
                        this.mPartialWakeLock.release();
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        }
    }

    private void fetchDtmfToneDelay(GsmCdmaPhone gsmCdmaPhone) {
        PersistableBundle configForSubId = ((CarrierConfigManager) gsmCdmaPhone.getContext().getSystemService("carrier_config")).getConfigForSubId(gsmCdmaPhone.getSubId());
        if (configForSubId != null) {
            this.mDtmfToneDelay = configForSubId.getInt(gsmCdmaPhone.getDtmfToneDelayKey());
        }
    }

    protected void log(String str) {
        Rlog.d("GsmCdmaConnection", "[GsmCdmaConn] " + str);
    }

    @Override // com.android.internal.telephony.Connection
    public int getNumberPresentation() {
        return this.mNumberPresentation;
    }

    @Override // com.android.internal.telephony.Connection
    public UUSInfo getUUSInfo() {
        return this.mUusInfo;
    }

    @Override // com.android.internal.telephony.Connection
    public int getPreciseDisconnectCause() {
        return this.mPreciseCause;
    }

    @Override // com.android.internal.telephony.Connection
    public String getVendorDisconnectCause() {
        return this.mVendorCause;
    }

    @Override // com.android.internal.telephony.Connection
    public void migrateFrom(Connection connection) {
        if (connection == null) {
            return;
        }
        super.migrateFrom(connection);
        this.mUusInfo = connection.getUUSInfo();
        setUserData(connection.getUserData());
    }

    @Override // com.android.internal.telephony.Connection
    public Connection getOrigConnection() {
        return this.mOrigConnection;
    }

    @Override // com.android.internal.telephony.Connection
    public boolean isMultiparty() {
        Connection connection = this.mOrigConnection;
        if (connection != null) {
            return connection.isMultiparty();
        }
        return false;
    }

    public EmergencyNumberTracker getEmergencyNumberTracker() {
        GsmCdmaPhone phone;
        GsmCdmaCallTracker gsmCdmaCallTracker = this.mOwner;
        if (gsmCdmaCallTracker == null || (phone = gsmCdmaCallTracker.getPhone()) == null) {
            return null;
        }
        return phone.getEmergencyNumberTracker();
    }
}
