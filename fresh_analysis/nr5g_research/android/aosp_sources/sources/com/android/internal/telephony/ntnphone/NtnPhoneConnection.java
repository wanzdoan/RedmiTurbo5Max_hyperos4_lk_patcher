package com.android.internal.telephony.ntnphone;

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
import com.android.internal.telephony.Call;
import com.android.internal.telephony.Call$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.CallFailCause;
import com.android.internal.telephony.CallManager$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.PhoneInternalInterface;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.cdma.CdmaCallWaitingNotification;
import com.android.internal.telephony.emergency.EmergencyNumberTracker;
import com.android.internal.telephony.satellite.SatelliteSessionController;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Collections;

/* JADX INFO: loaded from: classes.dex */
public class NtnPhoneConnection extends Connection {
    public static final String OTASP_NUMBER = "*22899";
    long mAlertingStartTime;
    long mDialingStartTime;
    long mDisconnectTime;
    boolean mDisconnected;
    private int mDtmfToneDelay;
    Handler mHandler;
    int mIndex;
    Connection mOrigConnection;
    NtnPhoneCallTracker mOwner;
    NtnPhoneCall mParent;
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
                        NtnPhoneConnection.this.releaseWakeLock();
                        return;
                    } else if (i != 5) {
                        return;
                    }
                }
                NtnPhoneConnection.this.processNextPostDialChar();
                return;
            }
            Handler handler = NtnPhoneConnection.this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(5), NtnPhoneConnection.this.mDtmfToneDelay);
        }
    }

    public NtnPhoneConnection(NtnPhone ntnPhone, NtnDriverCall ntnDriverCall, NtnPhoneCallTracker ntnPhoneCallTracker, int i) {
        super(ntnPhone.getPhoneType());
        this.mPreciseCause = 0;
        this.mDtmfToneDelay = 0;
        createWakeLock(ntnPhone.getContext());
        acquireWakeLock();
        this.mOwner = ntnPhoneCallTracker;
        this.mHandler = new MyHandler(this.mOwner.getLooper());
        this.mAddress = ntnDriverCall.number;
        setEmergencyCallInfo(this.mOwner, null);
        String str = TextUtils.isEmpty(ntnDriverCall.forwardedNumber) ? null : ntnDriverCall.forwardedNumber;
        Rlog.i("NtnPhoneConnection", "create, forwardedNumber=" + Rlog.pii("NtnPhoneConnection", str));
        this.mForwardedNumber = str != null ? new ArrayList<>(Collections.singletonList(ntnDriverCall.forwardedNumber)) : null;
        this.mIsIncoming = ntnDriverCall.isMT;
        this.mCreateTime = System.currentTimeMillis();
        this.mCnapName = ntnDriverCall.name;
        this.mCnapNamePresentation = ntnDriverCall.namePresentation;
        this.mNumberPresentation = ntnDriverCall.numberPresentation;
        this.mUusInfo = ntnDriverCall.uusInfo;
        this.mIndex = i;
        NtnPhoneCall ntnPhoneCallParentFromDCState = parentFromDCState(ntnDriverCall.state);
        this.mParent = ntnPhoneCallParentFromDCState;
        ntnPhoneCallParentFromDCState.attach(this, ntnDriverCall);
        fetchDtmfToneDelay(ntnPhone);
        setAudioQuality(getAudioQualityFromDC(ntnDriverCall.audioQuality));
        setCallRadioTech(this.mOwner.getPhone().getCsCallRadioTech());
    }

    public NtnPhoneConnection(NtnPhone ntnPhone, String str, NtnPhoneCallTracker ntnPhoneCallTracker, NtnPhoneCall ntnPhoneCall, PhoneInternalInterface.DialArgs dialArgs) {
        super(ntnPhone.getPhoneType());
        this.mPreciseCause = 0;
        this.mDtmfToneDelay = 0;
        createWakeLock(ntnPhone.getContext());
        acquireWakeLock();
        this.mOwner = ntnPhoneCallTracker;
        this.mHandler = new MyHandler(this.mOwner.getLooper());
        this.mDialString = str;
        if (!isPhoneTypeGsm()) {
            Rlog.d("NtnPhoneConnection", "[NtnPhoneConn] NtnPhoneConnection: dialString=" + maskDialString(str));
            str = formatDialString(str);
            Rlog.d("NtnPhoneConnection", "[NtnPhoneConn] NtnPhoneConnection:formated dialString=" + maskDialString(str));
        }
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
        if (ntnPhoneCall != null) {
            this.mParent = ntnPhoneCall;
            if (isPhoneTypeGsm()) {
                ntnPhoneCall.attachFake(this, Call.State.DIALING);
            } else {
                Call.State state = ntnPhoneCall.mState;
                Call.State state2 = Call.State.ACTIVE;
                if (state == state2) {
                    ntnPhoneCall.attachFake(this, state2);
                } else {
                    ntnPhoneCall.attachFake(this, Call.State.DIALING);
                }
            }
        }
        fetchDtmfToneDelay(ntnPhone);
        setCallRadioTech(this.mOwner.getPhone().getCsCallRadioTech());
    }

    public NtnPhoneConnection(Context context, CdmaCallWaitingNotification cdmaCallWaitingNotification, NtnPhoneCallTracker ntnPhoneCallTracker, NtnPhoneCall ntnPhoneCall) {
        super(ntnPhoneCall.getPhone().getPhoneType());
        this.mPreciseCause = 0;
        this.mDtmfToneDelay = 0;
        createWakeLock(context);
        acquireWakeLock();
        this.mOwner = ntnPhoneCallTracker;
        this.mHandler = new MyHandler(this.mOwner.getLooper());
        this.mAddress = cdmaCallWaitingNotification.number;
        this.mNumberPresentation = cdmaCallWaitingNotification.numberPresentation;
        this.mCnapName = cdmaCallWaitingNotification.name;
        this.mCnapNamePresentation = cdmaCallWaitingNotification.namePresentation;
        this.mIndex = -1;
        this.mIsIncoming = true;
        this.mCreateTime = System.currentTimeMillis();
        this.mConnectTime = 0L;
        this.mParent = ntnPhoneCall;
        ntnPhoneCall.attachFake(this, Call.State.WAITING);
        setCallRadioTech(this.mOwner.getPhone().getCsCallRadioTech());
    }

    public void dispose() {
        clearPostDialListeners();
        NtnPhoneCall ntnPhoneCall = this.mParent;
        if (ntnPhoneCall != null) {
            ntnPhoneCall.detach(this);
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

    public static String formatDialString(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < length) {
            char cCharAt = str.charAt(i);
            if (isPause(cCharAt) || isWait(cCharAt)) {
                int i2 = length - 1;
                if (i < i2) {
                    int iFindNextPCharOrNonPOrNonWCharIndex = findNextPCharOrNonPOrNonWCharIndex(str, i);
                    if (iFindNextPCharOrNonPOrNonWCharIndex < length) {
                        sb.append(findPOrWCharToAppend(str, i, iFindNextPCharOrNonPOrNonWCharIndex));
                        if (iFindNextPCharOrNonPOrNonWCharIndex > i + 1) {
                            i = iFindNextPCharOrNonPOrNonWCharIndex - 1;
                        }
                    } else if (iFindNextPCharOrNonPOrNonWCharIndex == length) {
                        i = i2;
                    }
                }
            } else {
                sb.append(cCharAt);
            }
            i++;
        }
        return PhoneNumberUtils.cdmaCheckAndProcessPlusCode(sb.toString());
    }

    boolean compareTo(NtnDriverCall ntnDriverCall) {
        if (!this.mIsIncoming && !ntnDriverCall.isMT) {
            return true;
        }
        if (!isPhoneTypeGsm() || this.mOrigConnection == null) {
            return this.mIsIncoming == ntnDriverCall.isMT && equalsHandlesNulls(this.mAddress, PhoneNumberUtils.stringFromStringAndTOA(ntnDriverCall.number, ntnDriverCall.TOA));
        }
        return true;
    }

    @Override // com.android.internal.telephony.Connection
    public String getOrigDialString() {
        return this.mDialString;
    }

    @Override // com.android.internal.telephony.Connection
    public NtnPhoneCall getCall() {
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
            Rlog.w("NtnPhoneConnection", "NtnPhoneConnection.proceedAfterWaitChar(): Expected getPostDialState() to be WAIT but was " + this.mPostDialState);
        } else {
            setPostDialState(Connection.PostDialState.STARTED);
            processNextPostDialChar();
        }
    }

    @Override // com.android.internal.telephony.Connection
    public void proceedAfterWildChar(String str) {
        if (this.mPostDialState != Connection.PostDialState.WILD) {
            Rlog.w("NtnPhoneConnection", "NtnPhoneConnection.proceedAfterWaitChar(): Expected getPostDialState() to be WILD but was " + this.mPostDialState);
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
        int iDisconnectCauseFromCode = TelephonyCommonUtilsStub.get().disconnectCauseFromCode(i);
        if (iDisconnectCauseFromCode != -1) {
            return iDisconnectCauseFromCode;
        }
        if (i == 1) {
            return 25;
        }
        if (i == 3) {
            return NtnRILConstants.MI_NTN_RIL_REQUEST_ISIM_AUTHENTICATION;
        }
        if (i == 6) {
            return 111;
        }
        if (i == 49) {
            return 123;
        }
        if (i == 50) {
            return 124;
        }
        if (i == 57) {
            return NtnRILConstants.MI_NTN_RIL_REQUEST_SET_UNSOL_CELL_INFO_LIST_RATE;
        }
        if (i == 58) {
            return 83;
        }
        if (i == 87) {
            return 90;
        }
        if (i == 88) {
            return 91;
        }
        if (i == 240) {
            return 20;
        }
        if (i == 241) {
            return 21;
        }
        switch (i) {
            case 6:
                return 111;
            case 8:
                return 106;
            case 34:
                return NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE;
            case 38:
                return 117;
            case 47:
                return 122;
            case 55:
                return 129;
            case 63:
                return 84;
            case 65:
                return 85;
            case 79:
                return 88;
            case 81:
                return 89;
            case 91:
                return 92;
            case 95:
                return 93;
            case 96:
                return 94;
            case 97:
                return 95;
            case 98:
                return 96;
            case 99:
                return 97;
            case 100:
                return 98;
            case 101:
                return 99;
            case 102:
                return 100;
            case 111:
                return 101;
            case 127:
                return 102;
            case CallFailCause.NO_VALID_SIM /* 249 */:
                return 19;
            case CallFailCause.EMERGENCY_TEMP_FAILURE /* 325 */:
                return 63;
            case CallFailCause.EMERGENCY_PERM_FAILURE /* 326 */:
                return 64;
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
            case CallFailCause.LOCAL_NETWORK_NO_SERVICE /* 1207 */:
            case CallFailCause.LOCAL_SERVICE_UNAVAILABLE /* 1211 */:
                return 18;
            default:
                switch (i) {
                    case 17:
                        return 4;
                    case 18:
                        return NtnRILConstants.MI_NTN_RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS;
                    case 19:
                        return 13;
                    case 20:
                        return NtnRILConstants.MI_NTN_RIL_REQUEST_VOICE_RADIO_TECH;
                    case 21:
                        return 112;
                    case 22:
                        return 113;
                    default:
                        switch (i) {
                            case 25:
                                return 114;
                            case 26:
                                return NtnRILConstants.MI_NTN_RIL_REQUEST_GET_DC_RT_INFO;
                            case 27:
                                return 109;
                            case 28:
                                return 7;
                            case 29:
                                return 115;
                            case 30:
                                return 116;
                            case 31:
                                return 65;
                            default:
                                switch (i) {
                                    case 41:
                                        return 118;
                                    case 42:
                                        return 119;
                                    case 43:
                                        return 120;
                                    case 44:
                                        return 121;
                                    default:
                                        switch (i) {
                                            case 68:
                                                return 15;
                                            case 69:
                                                return 86;
                                            case 70:
                                                return 87;
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
                                                        int state = this.mOwner.getPhone().getServiceState().getState();
                                                        if (state == 3) {
                                                            return 17;
                                                        }
                                                        if (!isEmergencyCall() && (state == 1 || state == 2)) {
                                                            return 18;
                                                        }
                                                        if (isPhoneTypeGsm() && (i == 65535 || i == 260)) {
                                                            return 23;
                                                        }
                                                        if (i == 16) {
                                                            return 2;
                                                        }
                                                        if (i == -1) {
                                                            return 36;
                                                        }
                                                        return i;
                                                }
                                        }
                                }
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
            Rlog.d("NtnPhoneConnection", "onDisconnect: cause=" + i);
            this.mOwner.getPhone().notifyDisconnect(this);
            notifyDisconnect(i);
            NtnPhoneCall ntnPhoneCall = this.mParent;
            zConnectionDisconnected = ntnPhoneCall != null ? ntnPhoneCall.connectionDisconnected(this) : false;
            this.mOrigConnection = null;
        }
        clearPostDialListeners();
        releaseWakeLock();
        return zConnectionDisconnected;
    }

    void onLocalDisconnect() {
        if (!this.mDisconnected) {
            doDisconnect();
            NtnPhoneCall ntnPhoneCall = this.mParent;
            if (ntnPhoneCall != null) {
                ntnPhoneCall.detach(this);
            }
        }
        releaseWakeLock();
    }

    /* JADX WARN: Code duplicated, block: B:31:0x0090  */
    /* JADX WARN: Code duplicated, block: B:33:0x0093  */
    /* JADX WARN: Code duplicated, block: B:34:0x0096  */
    /* JADX WARN: Code duplicated, block: B:38:0x00ab  */
    /* JADX WARN: Code duplicated, block: B:41:0x00c5  */
    /* JADX WARN: Code duplicated, block: B:42:0x00c7  */
    /* JADX WARN: Code duplicated, block: B:46:0x00e3  */
    /* JADX WARN: Code duplicated, block: B:49:0x00f6  */
    /* JADX WARN: Code duplicated, block: B:52:0x0106  */
    /* JADX WARN: Code duplicated, block: B:54:0x010e  */
    /* JADX WARN: Code duplicated, block: B:56:0x0114  */
    /* JADX WARN: Code duplicated, block: B:58:0x011e  */
    /* JADX WARN: Code duplicated, block: B:61:0x0142 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:62:0x0144  */
    /* JADX WARN: Code duplicated, block: B:65:0x014e  */
    /* JADX WARN: Code duplicated, block: B:72:0x016d  */
    /* JADX WARN: Code duplicated, block: B:73:0x016f  */
    /* JADX WARN: Code duplicated, block: B:76:0x01a0  */
    /* JADX WARN: Code duplicated, block: B:80:0x01ab A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:85:0x01b8 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:90:0x01c5 A[ADDED_TO_REGION] */
    public boolean update(NtnDriverCall ntnDriverCall) {
        boolean z;
        int audioQualityFromDC;
        int i;
        String str;
        ArrayList<String> arrayList;
        NtnPhoneCall ntnPhoneCall;
        boolean z2;
        boolean z3;
        String str2;
        boolean zIsConnectingInOrOut = isConnectingInOrOut();
        Call.State state = getState();
        Call.State state2 = Call.State.HOLDING;
        boolean z4 = state == state2;
        Call.State state3 = getState();
        Call.State state4 = Call.State.DIALING;
        boolean z5 = state3 == state4;
        Call.State state5 = getState();
        Call.State state6 = Call.State.ALERTING;
        boolean z6 = state5 == state6;
        NtnPhoneCall ntnPhoneCallParentFromDCState = parentFromDCState(ntnDriverCall.state);
        log("parent= " + this.mParent + ", newParent= " + ntnPhoneCallParentFromDCState);
        if (isPhoneTypeGsm() && this.mOrigConnection != null) {
            log("update: mOrigConnection is not null");
        } else {
            if (isIncoming() && !equalsBaseDialString(this.mAddress, ntnDriverCall.number) && (!this.mNumberConverted || !equalsBaseDialString(this.mConvertedNumber, ntnDriverCall.number))) {
                log("update: phone # changed!");
                this.mAddress = ntnDriverCall.number;
                z = true;
            }
            audioQualityFromDC = getAudioQualityFromDC(ntnDriverCall.audioQuality);
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
            i = ntnDriverCall.audioQuality;
            if (i != this.mAudioCodec) {
                this.mAudioCodec = i;
                this.mOwner.getPhone().getVoiceCallSessionStats().onAudioCodecChanged(this, ntnDriverCall.audioQuality);
            }
            if (TextUtils.isEmpty(ntnDriverCall.forwardedNumber)) {
                str = null;
            } else {
                str = ntnDriverCall.forwardedNumber;
            }
            Rlog.i("NtnPhoneConnection", "update: forwardedNumber=" + Rlog.pii("NtnPhoneConnection", str));
            arrayList = str != null ? new ArrayList<>(Collections.singletonList(ntnDriverCall.forwardedNumber)) : null;
            if (!equalsHandlesNulls(this.mForwardedNumber, arrayList)) {
                log("update: mForwardedNumber, # changed");
                this.mForwardedNumber = arrayList;
                z = true;
            }
            if (TextUtils.isEmpty(ntnDriverCall.name)) {
                if (!TextUtils.isEmpty(this.mCnapName)) {
                    this.mCnapName = "";
                    z = true;
                }
            } else if (!ntnDriverCall.name.equals(this.mCnapName)) {
                this.mCnapName = ntnDriverCall.name;
                z = true;
            }
            log("--dssds----" + this.mCnapName);
            this.mCnapNamePresentation = ntnDriverCall.namePresentation;
            this.mNumberPresentation = ntnDriverCall.numberPresentation;
            ntnPhoneCall = this.mParent;
            if (ntnPhoneCallParentFromDCState != ntnPhoneCall) {
                if (ntnPhoneCall != null) {
                    ntnPhoneCall.detach(this);
                }
                ntnPhoneCallParentFromDCState.attach(this, ntnDriverCall);
                this.mParent = ntnPhoneCallParentFromDCState;
            } else {
                boolean zUpdate = ntnPhoneCall.update(this, ntnDriverCall);
                if (z && !zUpdate) {
                    z2 = false;
                }
                StringBuilder sb = new StringBuilder("update: parent=");
                sb.append(this.mParent);
                sb.append(", hasNewParent=");
                if (ntnPhoneCallParentFromDCState != this.mParent) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                sb.append(z3);
                sb.append(", wasConnectingInOrOut=");
                sb.append(zIsConnectingInOrOut);
                sb.append(", wasHolding=");
                sb.append(z4);
                sb.append(", isConnectingInOrOut=");
                sb.append(isConnectingInOrOut());
                sb.append(", changed=");
                sb.append(z2);
                log(sb.toString());
                if (zIsConnectingInOrOut && !isConnectingInOrOut()) {
                    onConnectedInOrOut();
                }
                if (z2 && !z4 && getState() == state2) {
                    onStartedHolding();
                }
                if (z2 && !z5 && getState() == state4) {
                    onStartedDialing();
                }
                if (z2 && !z6 && getState() == state6) {
                    onStartedAlerting();
                }
                return z2;
            }
            z2 = true;
            StringBuilder sb2 = new StringBuilder("update: parent=");
            sb2.append(this.mParent);
            sb2.append(", hasNewParent=");
            if (ntnPhoneCallParentFromDCState != this.mParent) {
                z3 = true;
            } else {
                z3 = false;
            }
            sb2.append(z3);
            sb2.append(", wasConnectingInOrOut=");
            sb2.append(zIsConnectingInOrOut);
            sb2.append(", wasHolding=");
            sb2.append(z4);
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
            if (z2) {
                onStartedDialing();
            }
            if (z2) {
                onStartedAlerting();
            }
            return z2;
        }
        z = false;
        audioQualityFromDC = getAudioQualityFromDC(ntnDriverCall.audioQuality);
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
        i = ntnDriverCall.audioQuality;
        if (i != this.mAudioCodec) {
            this.mAudioCodec = i;
            this.mOwner.getPhone().getVoiceCallSessionStats().onAudioCodecChanged(this, ntnDriverCall.audioQuality);
        }
        if (TextUtils.isEmpty(ntnDriverCall.forwardedNumber)) {
            str = null;
        } else {
            str = ntnDriverCall.forwardedNumber;
        }
        Rlog.i("NtnPhoneConnection", "update: forwardedNumber=" + Rlog.pii("NtnPhoneConnection", str));
        if (str != null) {
        }
        if (!equalsHandlesNulls(this.mForwardedNumber, arrayList)) {
            log("update: mForwardedNumber, # changed");
            this.mForwardedNumber = arrayList;
            z = true;
        }
        if (TextUtils.isEmpty(ntnDriverCall.name)) {
            if (!TextUtils.isEmpty(this.mCnapName)) {
                this.mCnapName = "";
                z = true;
            }
        } else if (!ntnDriverCall.name.equals(this.mCnapName)) {
            this.mCnapName = ntnDriverCall.name;
            z = true;
        }
        log("--dssds----" + this.mCnapName);
        this.mCnapNamePresentation = ntnDriverCall.namePresentation;
        this.mNumberPresentation = ntnDriverCall.numberPresentation;
        ntnPhoneCall = this.mParent;
        if (ntnPhoneCallParentFromDCState != ntnPhoneCall) {
            if (ntnPhoneCall != null) {
                ntnPhoneCall.detach(this);
            }
            ntnPhoneCallParentFromDCState.attach(this, ntnDriverCall);
            this.mParent = ntnPhoneCallParentFromDCState;
        } else {
            boolean zUpdate2 = ntnPhoneCall.update(this, ntnDriverCall);
            if (z) {
            }
            StringBuilder sb3 = new StringBuilder("update: parent=");
            sb3.append(this.mParent);
            sb3.append(", hasNewParent=");
            if (ntnPhoneCallParentFromDCState != this.mParent) {
                z3 = true;
            } else {
                z3 = false;
            }
            sb3.append(z3);
            sb3.append(", wasConnectingInOrOut=");
            sb3.append(zIsConnectingInOrOut);
            sb3.append(", wasHolding=");
            sb3.append(z4);
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
            if (z2) {
                onStartedDialing();
            }
            if (z2) {
                onStartedAlerting();
            }
            return z2;
        }
        z2 = true;
        StringBuilder sb4 = new StringBuilder("update: parent=");
        sb4.append(this.mParent);
        sb4.append(", hasNewParent=");
        if (ntnPhoneCallParentFromDCState != this.mParent) {
            z3 = true;
        } else {
            z3 = false;
        }
        sb4.append(z3);
        sb4.append(", wasConnectingInOrOut=");
        sb4.append(zIsConnectingInOrOut);
        sb4.append(", wasHolding=");
        sb4.append(z4);
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
        if (z2) {
            onStartedDialing();
        }
        if (z2) {
            onStartedAlerting();
        }
        return z2;
    }

    void fakeHoldBeforeDial() {
        NtnPhoneCall ntnPhoneCall = this.mParent;
        if (ntnPhoneCall != null) {
            ntnPhoneCall.detach(this);
        }
        NtnPhoneCall ntnPhoneCall2 = this.mOwner.mBackgroundCall;
        this.mParent = ntnPhoneCall2;
        ntnPhoneCall2.attachFake(this, Call.State.HOLDING);
        onStartedHolding();
    }

    int getNtnPhoneIndex() {
        int i = this.mIndex;
        if (i >= 0) {
            return i + 1;
        }
        CallManager$$ExternalSyntheticBUOutline0.m("NtnPhone index not yet assigned");
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

    void onStartedDialing() {
        this.mDialingStartTime = SystemClock.elapsedRealtime();
    }

    void onStartedAlerting() {
        this.mAlertingStartTime = SystemClock.elapsedRealtime();
    }

    void onStartedHolding() {
        this.mHoldingStartTime = SystemClock.elapsedRealtime();
    }

    private boolean processPostDialChar(char c) {
        if (PhoneNumberUtils.is12Key(c)) {
            this.mOwner.mCi.sendDtmf(c, this.mHandler.obtainMessage(1));
        } else if (isPause(c)) {
            if (!isPhoneTypeGsm()) {
                setPostDialState(Connection.PostDialState.PAUSE);
            }
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(2), isPhoneTypeGsm() ? SatelliteSessionController.DEMO_MODE_SATELLITE_STAY_AT_LISTENING_MILLIS : 2000L);
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
        String remainingPostDialString = super.getRemainingPostDialString();
        if (!isPhoneTypeGsm() && !TextUtils.isEmpty(remainingPostDialString)) {
            int iIndexOf = remainingPostDialString.indexOf(59);
            int iIndexOf2 = remainingPostDialString.indexOf(44);
            if (iIndexOf > 0 && (iIndexOf < iIndexOf2 || iIndexOf2 <= 0)) {
                return remainingPostDialString.substring(0, iIndexOf);
            }
            if (iIndexOf2 > 0) {
                return remainingPostDialString.substring(0, iIndexOf2);
            }
        }
        return remainingPostDialString;
    }

    public void updateParent(NtnPhoneCall ntnPhoneCall, NtnPhoneCall ntnPhoneCall2) {
        if (ntnPhoneCall2 != ntnPhoneCall) {
            if (ntnPhoneCall != null) {
                ntnPhoneCall.detach(this);
            }
            ntnPhoneCall2.attachFake(this, Call.State.ACTIVE);
            this.mParent = ntnPhoneCall2;
        }
    }

    protected void finalize() {
        PowerManager.WakeLock wakeLock = this.mPartialWakeLock;
        if (wakeLock != null && wakeLock.isHeld()) {
            Rlog.e("NtnPhoneConnection", "UNEXPECTED; mPartialWakeLock is held when finalizing.");
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
                Rlog.e("NtnPhoneConnection", "processNextPostDialChar: c=" + cCharAt + " isn't valid!");
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
        NtnPhoneCall ntnPhoneCall = this.mParent;
        return ntnPhoneCall == null || ntnPhoneCall == this.mOwner.mRingingCall || (state = ntnPhoneCall.mState) == Call.State.DIALING || state == Call.State.ALERTING;
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.ntnphone.NtnPhoneConnection$1, reason: invalid class name */
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
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnDriverCall$State[NtnDriverCall.State.DIALING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnDriverCall$State[NtnDriverCall.State.ALERTING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnDriverCall$State[NtnDriverCall.State.DISCONNECTED_LOCAL.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnDriverCall$State[NtnDriverCall.State.DISCONNECTED_REMOTE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnDriverCall$State[NtnDriverCall.State.HOLDING.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnDriverCall$State[NtnDriverCall.State.INCOMING.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$ntnphone$NtnDriverCall$State[NtnDriverCall.State.WAITING.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }

    private NtnPhoneCall parentFromDCState(NtnDriverCall.State state) {
        switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$ntnphone$NtnDriverCall$State[state.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return this.mOwner.mForegroundCall;
            case 6:
                return this.mOwner.mBackgroundCall;
            case 7:
            case 8:
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
        this.mPartialWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, "NtnPhoneConnection");
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

    private static int findNextPCharOrNonPOrNonWCharIndex(String str, int i) {
        boolean zIsWait = isWait(str.charAt(i));
        int i2 = i + 1;
        int length = str.length();
        int i3 = i2;
        while (i3 < length) {
            char cCharAt = str.charAt(i3);
            if (isWait(cCharAt)) {
                zIsWait = true;
            }
            if (!isWait(cCharAt) && !isPause(cCharAt)) {
                break;
            }
            i3++;
        }
        return (i3 >= length || i3 <= i2 || zIsWait || !isPause(str.charAt(i))) ? i3 : i2;
    }

    private static char findPOrWCharToAppend(String str, int i, int i2) {
        char c = isPause(str.charAt(i)) ? ',' : ';';
        if (i2 > i + 1) {
            return ';';
        }
        return c;
    }

    private String maskDialString(String str) {
        return "<MASKED>";
    }

    private void fetchDtmfToneDelay(NtnPhone ntnPhone) {
        PersistableBundle configForSubId = ((CarrierConfigManager) ntnPhone.getContext().getSystemService("carrier_config")).getConfigForSubId(ntnPhone.getSubId());
        if (configForSubId != null) {
            this.mDtmfToneDelay = configForSubId.getInt(ntnPhone.getDtmfToneDelayKey());
        }
    }

    private boolean isPhoneTypeGsm() {
        return this.mOwner.getPhone().getPhoneType() == 1;
    }

    private void log(String str) {
        Rlog.d("NtnPhoneConnection", "[NtnPhoneConn] " + str);
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
        NtnPhone phone;
        NtnPhoneCallTracker ntnPhoneCallTracker = this.mOwner;
        if (ntnPhoneCallTracker == null || (phone = ntnPhoneCallTracker.getPhone()) == null) {
            return null;
        }
        return phone.getEmergencyNumberTracker();
    }

    public boolean isOtaspCall() {
        String str = this.mAddress;
        return str != null && "*22899".equals(str);
    }

    public int getCause() {
        return this.mCause;
    }

    public void setCause(int i) {
        this.mCause = i;
    }

    public long getCallSetupDuration() {
        return this.mAlertingStartTime - this.mDialingStartTime;
    }
}
