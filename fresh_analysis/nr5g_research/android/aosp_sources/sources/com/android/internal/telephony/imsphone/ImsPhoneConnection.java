package com.android.internal.telephony.imsphone;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.telecom.VideoProfile;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.ims.AudioCodecAttributes;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsStreamMediaProfile;
import android.telephony.ims.RtpHeaderExtension;
import android.telephony.ims.RtpHeaderExtensionType;
import android.text.TextUtils;
import com.android.ims.ImsCall;
import com.android.ims.ImsException;
import com.android.ims.internal.ImsVideoCallProviderWrapper;
import com.android.internal.hidden_from_bootclasspath.android.telecom.flags.Flags;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallManager$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.emergency.EmergencyNumberTracker;
import com.android.internal.telephony.satellite.SatelliteSessionController;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class ImsPhoneConnection extends Connection implements ImsVideoCallProviderWrapper.ImsVideoProviderWrapperCallback {
    static final int PAUSE_DELAY_MILLIS = 3000;
    private long mConferenceConnectTime;
    private long mDisconnectTime;
    protected boolean mDisconnected;
    private int mDtmfToneDelay;
    private final Bundle mExtras;
    private Handler mHandler;
    private final Messenger mHandlerMessenger;
    protected ImsCall mImsCall;
    private ImsReasonInfo mImsReasonInfo;
    private ImsVideoCallProviderWrapper mImsVideoCallProviderWrapper;
    private boolean mIsBusinessComposerFeatureEnabled;
    private boolean mIsEmergency;
    private boolean mIsHeldByRemote;
    private boolean mIsLocalVideoCapable;
    private boolean mIsMergeInProcess;
    private boolean mIsRttEnabledForCall;
    private boolean mIsWpsCall;
    protected ImsPhoneCallTracker mOwner;
    protected ImsPhoneCall mParent;
    private PowerManager.WakeLock mPartialWakeLock;
    private int mPreciseDisconnectCause;
    public ImsRttTextHandler mRttTextHandler;
    public android.telecom.Connection.RttTextStream mRttTextStream;
    private boolean mShouldIgnoreVideoStateChanges;
    private UUSInfo mUusInfo;

    public static int toTelecomVerificationStatus(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                return 0;
            }
        }
        return i2;
    }

    protected boolean allowedUpdateMOAddress() {
        return false;
    }

    protected int applyVideoRingtoneCapabilities(ImsCallProfile imsCallProfile, int i) {
        return i;
    }

    public void dispose() {
    }

    @Override // com.android.internal.telephony.Connection
    public Connection getOrigConnection() {
        return null;
    }

    @Override // com.android.internal.telephony.Connection
    public String getVendorDisconnectCause() {
        return null;
    }

    protected boolean skipSwitchingCallToForeground() {
        return false;
    }

    protected void switchCallToBackgroundIfNecessary() {
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
                        ImsPhoneConnection.this.releaseWakeLock();
                        return;
                    } else if (i != 5) {
                        return;
                    }
                }
                ImsPhoneConnection.this.processNextPostDialChar();
                return;
            }
            ImsPhoneConnection.this.mHandler.sendMessageDelayed(ImsPhoneConnection.this.mHandler.obtainMessage(5), ImsPhoneConnection.this.mDtmfToneDelay);
        }
    }

    public ImsPhoneConnection(Phone phone, ImsCall imsCall, ImsPhoneCallTracker imsPhoneCallTracker, ImsPhoneCall imsPhoneCall, boolean z) {
        super(5);
        this.mExtras = new Bundle();
        this.mConferenceConnectTime = 0L;
        this.mDtmfToneDelay = 0;
        this.mIsEmergency = false;
        this.mIsWpsCall = false;
        this.mShouldIgnoreVideoStateChanges = false;
        this.mPreciseDisconnectCause = 0;
        this.mIsRttEnabledForCall = false;
        this.mIsMergeInProcess = false;
        this.mIsLocalVideoCapable = true;
        this.mIsHeldByRemote = false;
        this.mIsBusinessComposerFeatureEnabled = false;
        createWakeLock(phone.getContext());
        acquireWakeLock();
        this.mOwner = imsPhoneCallTracker;
        this.mHandler = new MyHandler(this.mOwner.getLooper());
        this.mHandlerMessenger = new Messenger(this.mHandler);
        this.mImsCall = imsCall;
        this.mIsAdhocConference = isMultiparty();
        if (imsCall != null && imsCall.getCallProfile() != null) {
            this.mAddress = imsCall.getCallProfile().getCallExtra("oi");
            this.mCnapName = imsCall.getCallProfile().getCallExtra("cna");
            this.mNumberPresentation = ImsCallProfile.OIRToPresentation(imsCall.getCallProfile().getCallExtraInt("oir"));
            this.mCnapNamePresentation = ImsCallProfile.OIRToPresentation(imsCall.getCallProfile().getCallExtraInt("cnap"));
            setNumberVerificationStatus(toTelecomVerificationStatus(imsCall.getCallProfile().getCallerNumberVerificationStatus()));
            updateMediaCapabilities(imsCall);
        } else {
            this.mNumberPresentation = 3;
            this.mCnapNamePresentation = 3;
        }
        this.mIsIncoming = !z;
        this.mCreateTime = System.currentTimeMillis();
        this.mUusInfo = null;
        setIsBusinessComposerFeatureEnabled(phone);
        updateExtras(imsCall);
        this.mParent = imsPhoneCall;
        imsPhoneCall.attach(this, this.mIsIncoming ? Call.State.INCOMING : Call.State.DIALING);
        fetchDtmfToneDelay(phone);
        if (phone.getContext().getResources().getBoolean(17892074)) {
            setAudioModeIsVoip(true);
        }
    }

    public ImsPhoneConnection(Phone phone, String str, ImsPhoneCallTracker imsPhoneCallTracker, ImsPhoneCall imsPhoneCall, boolean z, boolean z2, ImsPhone.ImsDialArgs imsDialArgs) {
        super(5);
        this.mExtras = new Bundle();
        this.mConferenceConnectTime = 0L;
        this.mDtmfToneDelay = 0;
        this.mIsEmergency = false;
        this.mIsWpsCall = false;
        this.mShouldIgnoreVideoStateChanges = false;
        this.mPreciseDisconnectCause = 0;
        this.mIsRttEnabledForCall = false;
        this.mIsMergeInProcess = false;
        this.mIsLocalVideoCapable = true;
        this.mIsHeldByRemote = false;
        this.mIsBusinessComposerFeatureEnabled = false;
        createWakeLock(phone.getContext());
        acquireWakeLock();
        this.mOwner = imsPhoneCallTracker;
        this.mHandler = new MyHandler(this.mOwner.getLooper());
        this.mHandlerMessenger = new Messenger(this.mHandler);
        this.mDialString = str;
        this.mAddress = PhoneNumberUtils.extractNetworkPortionAlt(str);
        this.mPostDialString = PhoneNumberUtils.extractPostDialPortion(str);
        this.mIsIncoming = false;
        this.mCnapName = null;
        this.mCnapNamePresentation = 1;
        this.mNumberPresentation = 1;
        this.mCreateTime = System.currentTimeMillis();
        this.mParent = imsPhoneCall;
        imsPhoneCall.attachFake(this, Call.State.DIALING);
        this.mIsEmergency = z;
        if (z) {
            setEmergencyCallInfo(this.mOwner, imsDialArgs);
            if (getEmergencyNumberInfo() == null) {
                setNonDetectableEmergencyCallInfo(imsDialArgs.eccCategory, new ArrayList());
            }
        }
        this.mIsWpsCall = z2;
        fetchDtmfToneDelay(phone);
        if (phone.getContext().getResources().getBoolean(17892074)) {
            setAudioModeIsVoip(true);
        }
    }

    public ImsPhoneConnection(Phone phone, String[] strArr, ImsPhoneCallTracker imsPhoneCallTracker, ImsPhoneCall imsPhoneCall, boolean z) {
        super(5);
        this.mExtras = new Bundle();
        this.mConferenceConnectTime = 0L;
        this.mDtmfToneDelay = 0;
        this.mIsEmergency = false;
        this.mIsWpsCall = false;
        this.mShouldIgnoreVideoStateChanges = false;
        this.mPreciseDisconnectCause = 0;
        this.mIsRttEnabledForCall = false;
        this.mIsMergeInProcess = false;
        this.mIsLocalVideoCapable = true;
        this.mIsHeldByRemote = false;
        this.mIsBusinessComposerFeatureEnabled = false;
        createWakeLock(phone.getContext());
        acquireWakeLock();
        this.mOwner = imsPhoneCallTracker;
        this.mHandler = new MyHandler(this.mOwner.getLooper());
        this.mHandlerMessenger = new Messenger(this.mHandler);
        this.mAddress = Connection.ADHOC_CONFERENCE_ADDRESS;
        this.mDialString = Connection.ADHOC_CONFERENCE_ADDRESS;
        this.mParticipantsToDial = strArr;
        this.mIsAdhocConference = true;
        this.mIsIncoming = false;
        this.mCnapName = null;
        this.mCnapNamePresentation = 1;
        this.mNumberPresentation = 1;
        this.mCreateTime = System.currentTimeMillis();
        this.mParent = imsPhoneCall;
        imsPhoneCall.attachFake(this, Call.State.DIALING);
        if (phone.getContext().getResources().getBoolean(17892074)) {
            setAudioModeIsVoip(true);
        }
    }

    static boolean equalsBaseDialString(String str, String str2) {
        if (str == null) {
            return str2 == null;
        }
        return str2 != null && str.startsWith(str2);
    }

    protected int applyLocalCallCapabilities(ImsCallProfile imsCallProfile, int i) {
        Rlog.i("ImsPhoneConnection", "applyLocalCallCapabilities - localProfile = " + imsCallProfile);
        int iRemoveCapability = Connection.removeCapability(i, 4);
        if (!this.mIsLocalVideoCapable) {
            Rlog.i("ImsPhoneConnection", "applyLocalCallCapabilities - disabling video (overidden)");
            return iRemoveCapability;
        }
        int i2 = imsCallProfile.mCallType;
        return (i2 == 3 || i2 == 4) ? Connection.addCapability(iRemoveCapability, 4) : iRemoveCapability;
    }

    private static int applyRemoteCallCapabilities(ImsCallProfile imsCallProfile, int i) {
        Rlog.w("ImsPhoneConnection", "applyRemoteCallCapabilities - remoteProfile = " + imsCallProfile);
        int iRemoveCapability = Connection.removeCapability(Connection.removeCapability(i, 8), 64);
        int i2 = imsCallProfile.mCallType;
        if (i2 == 3 || i2 == 4) {
            iRemoveCapability = Connection.addCapability(iRemoveCapability, 8);
        }
        return imsCallProfile.getMediaProfile().getRttMode() == 1 ? Connection.addCapability(iRemoveCapability, 64) : iRemoveCapability;
    }

    @Override // com.android.internal.telephony.Connection
    public String getOrigDialString() {
        return this.mDialString;
    }

    @Override // com.android.internal.telephony.Connection
    public ImsPhoneCall getCall() {
        return this.mParent;
    }

    @Override // com.android.internal.telephony.Connection
    public long getDisconnectTime() {
        return this.mDisconnectTime;
    }

    @Override // com.android.internal.telephony.Connection
    public long getHoldingStartTime() {
        return this.mHoldingStartTime;
    }

    @Override // com.android.internal.telephony.Connection
    public long getHoldDurationMillis() {
        if (getState() != Call.State.HOLDING) {
            return 0L;
        }
        return SystemClock.elapsedRealtime() - this.mHoldingStartTime;
    }

    public void setDisconnectCause(int i) {
        Rlog.d("ImsPhoneConnection", "setDisconnectCause: cause=" + i);
        this.mCause = i;
    }

    @Override // com.android.internal.telephony.Connection
    public int getDisconnectCause() {
        Rlog.d("ImsPhoneConnection", "getDisconnectCause: cause=" + this.mCause);
        return this.mCause;
    }

    public boolean isIncomingCallAutoRejected() {
        return this.mCause == 81;
    }

    public ImsPhoneCallTracker getOwner() {
        return this.mOwner;
    }

    @Override // com.android.internal.telephony.Connection
    public Call.State getState() {
        if (this.mDisconnected) {
            return Call.State.DISCONNECTED;
        }
        return super.getState();
    }

    @Override // com.android.internal.telephony.Connection
    public void deflect(String str) throws CallStateException {
        if (this.mParent.getState().isRinging()) {
            try {
                ImsCall imsCall = this.mImsCall;
                if (imsCall != null) {
                    imsCall.deflect(str);
                    return;
                }
                throw new CallStateException("no valid ims call to deflect");
            } catch (ImsException unused) {
                CallManager$$ExternalSyntheticBUOutline0.m("cannot deflect call");
                return;
            }
        }
        CallManager$$ExternalSyntheticBUOutline0.m("phone not ringing");
    }

    @Override // com.android.internal.telephony.Connection
    public void transfer(String str, boolean z) throws CallStateException {
        try {
            ImsCall imsCall = this.mImsCall;
            if (imsCall != null) {
                imsCall.transfer(str, z);
                return;
            }
            throw new CallStateException("no valid ims call to transfer");
        } catch (ImsException unused) {
            CallManager$$ExternalSyntheticBUOutline0.m("cannot transfer call");
        }
    }

    @Override // com.android.internal.telephony.Connection
    public void consultativeTransfer(Connection connection) throws CallStateException {
        try {
            ImsCall imsCall = this.mImsCall;
            if (imsCall != null) {
                imsCall.consultativeTransfer(((ImsPhoneConnection) connection).getImsCall());
                return;
            }
            throw new CallStateException("no valid ims call to transfer");
        } catch (ImsException unused) {
            CallManager$$ExternalSyntheticBUOutline0.m("cannot transfer call");
        }
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
    public void separate() throws CallStateException {
        throw new CallStateException("not supported");
    }

    @Override // com.android.internal.telephony.Connection
    public void proceedAfterWaitChar() {
        if (this.mPostDialState != Connection.PostDialState.WAIT) {
            Rlog.w("ImsPhoneConnection", "ImsPhoneConnection.proceedAfterWaitChar(): Expected getPostDialState() to be WAIT but was " + this.mPostDialState);
        } else {
            setPostDialState(Connection.PostDialState.STARTED);
            processNextPostDialChar();
        }
    }

    @Override // com.android.internal.telephony.Connection
    public void proceedAfterWildChar(String str) {
        if (this.mPostDialState != Connection.PostDialState.WILD) {
            Rlog.w("ImsPhoneConnection", "ImsPhoneConnection.proceedAfterWaitChar(): Expected getPostDialState() to be WILD but was " + this.mPostDialState);
            return;
        }
        setPostDialState(Connection.PostDialState.STARTED);
        this.mPostDialString = str + this.mPostDialString.substring(this.mNextPostDialChar);
        this.mNextPostDialChar = 0;
        Rlog.d("ImsPhoneConnection", "proceedAfterWildChar: new postDialString is " + this.mPostDialString);
        processNextPostDialChar();
    }

    @Override // com.android.internal.telephony.Connection
    public void cancelPostDial() {
        setPostDialState(Connection.PostDialState.CANCELLED);
    }

    public void onHangupLocal() {
        this.mCause = 3;
    }

    @Override // com.android.internal.telephony.Connection
    public boolean onDisconnect(int i) {
        Rlog.d("ImsPhoneConnection", "onDisconnect: cause=" + i);
        if (this.mCause != 3 || i == 16) {
            this.mCause = i;
        }
        return onDisconnect();
    }

    public boolean onDisconnect() {
        boolean zConnectionDisconnected = false;
        if (!this.mDisconnected) {
            this.mDisconnectTime = System.currentTimeMillis();
            this.mDuration = SystemClock.elapsedRealtime() - this.mConnectTimeReal;
            this.mDisconnected = true;
            this.mOwner.mPhone.notifyDisconnect(this);
            notifyDisconnect(this.mCause);
            ImsPhoneCall imsPhoneCall = this.mParent;
            if (imsPhoneCall != null) {
                zConnectionDisconnected = imsPhoneCall.connectionDisconnected(this);
            } else {
                Rlog.d("ImsPhoneConnection", "onDisconnect: no parent");
            }
            synchronized (this) {
                try {
                    ImsRttTextHandler imsRttTextHandler = this.mRttTextHandler;
                    if (imsRttTextHandler != null) {
                        imsRttTextHandler.tearDown();
                    }
                    ImsCall imsCall = this.mImsCall;
                    if (imsCall != null) {
                        imsCall.close();
                    }
                    this.mImsCall = null;
                    ImsVideoCallProviderWrapper imsVideoCallProviderWrapper = this.mImsVideoCallProviderWrapper;
                    if (imsVideoCallProviderWrapper != null) {
                        imsVideoCallProviderWrapper.tearDown();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        releaseWakeLock();
        return zConnectionDisconnected;
    }

    void onConnectedInOrOut() {
        this.mConnectTime = System.currentTimeMillis();
        this.mConnectTimeReal = SystemClock.elapsedRealtime();
        this.mDuration = 0L;
        Rlog.d("ImsPhoneConnection", "onConnectedInOrOut: connectTime=" + this.mConnectTime);
        if (!this.mIsIncoming) {
            processNextPostDialChar();
        }
        releaseWakeLock();
    }

    protected void onStartedHolding() {
        this.mHoldingStartTime = SystemClock.elapsedRealtime();
    }

    private boolean processPostDialChar(char c) {
        if (PhoneNumberUtils.is12Key(c)) {
            Message messageObtainMessage = this.mHandler.obtainMessage(1);
            messageObtainMessage.replyTo = this.mHandlerMessenger;
            this.mOwner.sendDtmf(c, messageObtainMessage);
        } else if (c == ',') {
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(2), SatelliteSessionController.DEMO_MODE_SATELLITE_STAY_AT_LISTENING_MILLIS);
        } else if (c == ';') {
            setPostDialState(Connection.PostDialState.WAIT);
        } else {
            if (c != 'N') {
                return false;
            }
            setPostDialState(Connection.PostDialState.WILD);
        }
        return true;
    }

    public void finalize() {
        releaseWakeLock();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNextPostDialChar() {
        char cCharAt;
        Message messageMessageForRegistrant;
        if (this.mPostDialState == Connection.PostDialState.CANCELLED) {
            return;
        }
        String str = this.mPostDialString;
        if (str == null || str.length() <= this.mNextPostDialChar) {
            setPostDialState(Connection.PostDialState.COMPLETE);
            cCharAt = 0;
        } else {
            setPostDialState(Connection.PostDialState.STARTED);
            String str2 = this.mPostDialString;
            int i = this.mNextPostDialChar;
            this.mNextPostDialChar = i + 1;
            cCharAt = str2.charAt(i);
            if (!processPostDialChar(cCharAt)) {
                this.mHandler.obtainMessage(3).sendToTarget();
                Rlog.e("ImsPhoneConnection", "processNextPostDialChar: c=" + cCharAt + " isn't valid!");
                return;
            }
        }
        notifyPostDialListenersNextChar(cCharAt);
        Registrant postDialHandler = this.mOwner.mPhone.getPostDialHandler();
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

    private void setPostDialState(Connection.PostDialState postDialState) {
        Connection.PostDialState postDialState2 = this.mPostDialState;
        Connection.PostDialState postDialState3 = Connection.PostDialState.STARTED;
        if (postDialState2 != postDialState3 && postDialState == postDialState3) {
            acquireWakeLock();
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(4), 60000L);
        } else if (postDialState2 == postDialState3 && postDialState != postDialState3) {
            this.mHandler.removeMessages(4);
            releaseWakeLock();
        }
        this.mPostDialState = postDialState;
        notifyPostDialListeners();
    }

    private void createWakeLock(Context context) {
        this.mPartialWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, "ImsPhoneConnection");
    }

    private void acquireWakeLock() {
        Rlog.d("ImsPhoneConnection", "acquireWakeLock");
        this.mPartialWakeLock.acquire();
    }

    public void releaseWakeLock() {
        PowerManager.WakeLock wakeLock = this.mPartialWakeLock;
        if (wakeLock != null) {
            synchronized (wakeLock) {
                try {
                    if (this.mPartialWakeLock.isHeld()) {
                        Rlog.d("ImsPhoneConnection", "releaseWakeLock");
                        this.mPartialWakeLock.release();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private void fetchDtmfToneDelay(Phone phone) {
        PersistableBundle configForSubId = ((CarrierConfigManager) phone.getContext().getSystemService("carrier_config")).getConfigForSubId(phone.getSubId());
        if (configForSubId != null) {
            this.mDtmfToneDelay = configForSubId.getInt("ims_dtmf_tone_delay_int");
        }
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
    public synchronized boolean isMultiparty() {
        ImsCall imsCall;
        imsCall = this.mImsCall;
        return imsCall != null && imsCall.isMultiparty();
    }

    @Override // com.android.internal.telephony.Connection
    public synchronized boolean isConferenceHost() {
        ImsCall imsCall;
        imsCall = this.mImsCall;
        return imsCall != null && imsCall.isConferenceHost();
    }

    @Override // com.android.internal.telephony.Connection
    public boolean isMemberOfPeerConference() {
        return !isConferenceHost();
    }

    public synchronized ImsCall getImsCall() {
        return this.mImsCall;
    }

    public synchronized void setImsCall(ImsCall imsCall) {
        this.mImsCall = imsCall;
    }

    public void changeParent(ImsPhoneCall imsPhoneCall) {
        this.mParent = imsPhoneCall;
    }

    public boolean update(ImsCall imsCall, Call.State state) {
        if (state == Call.State.ACTIVE) {
            if (imsCall.isPendingHold()) {
                Rlog.w("ImsPhoneConnection", "update : state is ACTIVE, but call is pending hold, skipping");
                return false;
            }
            if (this.mParent.getState().isRinging() || this.mParent.getState().isDialing()) {
                onConnectedInOrOut();
            }
            if ((this.mParent.getState().isRinging() || this.mParent == this.mOwner.mBackgroundCall) && !skipSwitchingCallToForeground()) {
                this.mParent.detach(this);
                ImsPhoneCall imsPhoneCall = this.mOwner.mForegroundCall;
                this.mParent = imsPhoneCall;
                imsPhoneCall.attach(this);
            }
        } else if (state == Call.State.HOLDING) {
            switchCallToBackgroundIfNecessary();
            onStartedHolding();
        }
        return this.mParent.update(this, imsCall, state) || updateAddressDisplay(imsCall) || updateMediaCapabilities(imsCall) || updateExtras(imsCall);
    }

    public void maybeChangeRingbackState() {
        Rlog.i("ImsPhoneConnection", "maybeChangeRingbackState");
        this.mParent.maybeChangeRingbackState(this.mImsCall);
    }

    @Override // com.android.internal.telephony.Connection
    public int getPreciseDisconnectCause() {
        return this.mPreciseDisconnectCause;
    }

    public void setPreciseDisconnectCause(int i) {
        this.mPreciseDisconnectCause = i;
    }

    @Override // com.android.internal.telephony.Connection
    public void onDisconnectConferenceParticipant(Uri uri) {
        ImsCall imsCall = getImsCall();
        if (imsCall == null) {
            return;
        }
        try {
            imsCall.removeParticipants(new String[]{uri.toString()});
        } catch (ImsException unused) {
            Rlog.e("ImsPhoneConnection", "onDisconnectConferenceParticipant: no session in place. Failed to disconnect endpoint = " + uri);
        }
    }

    public void setConferenceConnectTime(long j) {
        this.mConferenceConnectTime = j;
    }

    public long getConferenceConnectTime() {
        return this.mConferenceConnectTime;
    }

    public boolean updateAddressDisplay(ImsCall imsCall) {
        boolean z = false;
        if (imsCall == null) {
            return false;
        }
        ImsCallProfile callProfile = imsCall.getCallProfile();
        if (callProfile != null && (isIncoming() || allowedUpdateMOAddress())) {
            String callExtra = callProfile.getCallExtra("oi");
            String callExtra2 = callProfile.getCallExtra("cna");
            int iCallNumberPresentation = callNumberPresentation(callProfile);
            int iOIRToPresentation = ImsCallProfile.OIRToPresentation(callProfile.getCallExtraInt("cnap"));
            Rlog.d("ImsPhoneConnection", "updateAddressDisplay: callId = " + getTelecomCallId() + " address = " + Rlog.pii("ImsPhoneConnection", callExtra) + " name = " + Rlog.pii("ImsPhoneConnection", callExtra2) + " nump = " + iCallNumberPresentation + " namep = " + iOIRToPresentation);
            if (!this.mIsMergeInProcess) {
                if (needUpdateAddress(callExtra)) {
                    this.mAddress = callExtra;
                    z = true;
                }
                boolean zIsEmpty = TextUtils.isEmpty(callExtra2);
                String str = this.mCnapName;
                if (zIsEmpty) {
                    if (!TextUtils.isEmpty(str)) {
                        this.mCnapName = "";
                        z = true;
                    }
                } else if (!callExtra2.equals(str)) {
                    this.mCnapName = callExtra2;
                    z = true;
                }
                if (this.mNumberPresentation != iCallNumberPresentation) {
                    this.mNumberPresentation = iCallNumberPresentation;
                    z = true;
                }
                if (this.mCnapNamePresentation != iOIRToPresentation) {
                    this.mCnapNamePresentation = iOIRToPresentation;
                    return true;
                }
            }
        }
        return z;
    }

    public boolean updateMediaCapabilities(ImsCall imsCall) {
        boolean z;
        int iRemoveCapability;
        boolean z2;
        int i;
        boolean z3 = false;
        if (imsCall == null) {
            return false;
        }
        try {
            ImsCallProfile callProfile = imsCall.getCallProfile();
            if (callProfile != null) {
                int videoState = getVideoState();
                int videoStateFromImsCallProfile = ImsCallProfile.getVideoStateFromImsCallProfile(callProfile);
                if (videoState != videoStateFromImsCallProfile) {
                    if (VideoProfile.isPaused(videoState) && !VideoProfile.isPaused(videoStateFromImsCallProfile)) {
                        this.mShouldIgnoreVideoStateChanges = false;
                    }
                    if (this.mShouldIgnoreVideoStateChanges) {
                        Rlog.d("ImsPhoneConnection", "updateMediaCapabilities - ignoring video state change due to paused state.");
                        z = false;
                    } else {
                        updateVideoState(videoStateFromImsCallProfile);
                        z = true;
                    }
                    try {
                        if (!VideoProfile.isPaused(videoState) && VideoProfile.isPaused(videoStateFromImsCallProfile)) {
                            this.mShouldIgnoreVideoStateChanges = true;
                        }
                    } catch (ImsException unused) {
                        return z;
                    }
                } else {
                    z = false;
                }
                ImsStreamMediaProfile imsStreamMediaProfile = callProfile.mMediaProfile;
                if (imsStreamMediaProfile != null) {
                    boolean zIsRttCall = imsStreamMediaProfile.isRttCall();
                    this.mIsRttEnabledForCall = zIsRttCall;
                    if (zIsRttCall && this.mRttTextHandler == null) {
                        Rlog.d("ImsPhoneConnection", "updateMediaCapabilities -- turning RTT on, profile=" + callProfile);
                        startRttTextProcessing();
                        onRttInitiated();
                        try {
                            this.mOwner.getPhone().getVoiceCallSessionStats().onRttStarted(this);
                        } catch (ImsException unused2) {
                            return true;
                        }
                    } else if (!zIsRttCall && this.mRttTextHandler != null) {
                        Rlog.d("ImsPhoneConnection", "updateMediaCapabilities -- turning RTT off, profile=" + callProfile);
                        this.mRttTextHandler.tearDown();
                        this.mRttTextHandler = null;
                        this.mRttTextStream = null;
                        onRttTerminated();
                    }
                    z = true;
                }
            } else {
                z = false;
            }
            int connectionCapabilities = getConnectionCapabilities();
            if (this.mOwner.isCarrierDowngradeOfVtCallSupported()) {
                iRemoveCapability = Connection.addCapability(connectionCapabilities, 3);
            } else {
                iRemoveCapability = Connection.removeCapability(connectionCapabilities, 3);
            }
            ImsCallProfile localCallProfile = imsCall.getLocalCallProfile();
            Rlog.v("ImsPhoneConnection", "update localCallProfile=" + localCallProfile);
            if (localCallProfile != null) {
                iRemoveCapability = applyLocalCallCapabilities(localCallProfile, iRemoveCapability);
            }
            ImsCallProfile remoteCallProfile = imsCall.getRemoteCallProfile();
            Rlog.v("ImsPhoneConnection", "update remoteCallProfile=" + remoteCallProfile);
            if (remoteCallProfile != null) {
                iRemoveCapability = applyRemoteCallCapabilities(remoteCallProfile, iRemoveCapability);
            }
            if (getConnectionCapabilities() != iRemoveCapability) {
                setConnectionCapabilities(iRemoveCapability);
                z = true;
            }
            if (!this.mOwner.isViLteDataMetered()) {
                Rlog.v("ImsPhoneConnection", "data is not metered");
            } else {
                ImsVideoCallProviderWrapper imsVideoCallProviderWrapper = this.mImsVideoCallProviderWrapper;
                if (imsVideoCallProviderWrapper != null) {
                    imsVideoCallProviderWrapper.setIsVideoEnabled(hasCapabilities(4));
                }
            }
            if (localCallProfile == null || (i = localCallProfile.mMediaProfile.mAudioQuality) == this.mAudioCodec) {
                z2 = false;
                z3 = z;
            } else {
                this.mAudioCodec = i;
                this.mOwner.getPhone().getVoiceCallSessionStats().onAudioCodecChanged(this, this.mAudioCodec);
                z2 = true;
                z3 = true;
            }
            if (localCallProfile != null && localCallProfile.mMediaProfile.getAudioCodecAttributes() != null) {
                AudioCodecAttributes audioCodecAttributes = localCallProfile.mMediaProfile.getAudioCodecAttributes();
                if (Math.abs(this.mAudioCodecBitrateKbps - ((Float) audioCodecAttributes.getBitrateRangeKbps().getUpper()).floatValue()) > 0.01f) {
                    this.mAudioCodecBitrateKbps = ((Float) audioCodecAttributes.getBitrateRangeKbps().getUpper()).floatValue();
                    z2 = true;
                    z3 = true;
                }
                if (Math.abs(this.mAudioCodecBandwidthKhz - ((Float) audioCodecAttributes.getBandwidthRangeKhz().getUpper()).floatValue()) > 0.01f) {
                    this.mAudioCodecBandwidthKhz = ((Float) audioCodecAttributes.getBandwidthRangeKhz().getUpper()).floatValue();
                    z2 = true;
                    z3 = true;
                }
            }
            if (z2) {
                Rlog.i("ImsPhoneConnection", "updateMediaCapabilities: mediate attributes changed: codec = " + this.mAudioCodec + ", bitRate=" + this.mAudioCodecBitrateKbps + ", bandwidth=" + this.mAudioCodecBandwidthKhz);
                notifyMediaAttributesChanged();
            }
            int audioQualityFromCallProfile = getAudioQualityFromCallProfile(localCallProfile, remoteCallProfile);
            if (getAudioQuality() == audioQualityFromCallProfile) {
                return z3;
            }
            setAudioQuality(audioQualityFromCallProfile);
            return true;
        } catch (ImsException unused3) {
            return z3;
        }
    }

    private void updateVideoState(int i) {
        ImsVideoCallProviderWrapper imsVideoCallProviderWrapper = this.mImsVideoCallProviderWrapper;
        if (imsVideoCallProviderWrapper != null) {
            imsVideoCallProviderWrapper.onVideoStateChanged(i);
        }
        setVideoState(i);
        this.mOwner.getPhone().getVoiceCallSessionStats().onVideoStateChange(this, i);
    }

    public void startRtt(android.telecom.Connection.RttTextStream rttTextStream) {
        ImsCall imsCall = getImsCall();
        if (imsCall == null) {
            Rlog.w("ImsPhoneConnection", "startRtt failed, imsCall is null");
        } else {
            imsCall.sendRttModifyRequest(true);
            setCurrentRttTextStream(rttTextStream);
        }
    }

    public void stopRtt() {
        ImsCall imsCall = getImsCall();
        if (imsCall == null) {
            Rlog.w("ImsPhoneConnection", "stopRtt failed, imsCall is null");
        } else {
            imsCall.sendRttModifyRequest(false);
        }
    }

    public void sendRttModifyResponse(android.telecom.Connection.RttTextStream rttTextStream) {
        boolean z = rttTextStream != null;
        ImsCall imsCall = getImsCall();
        if (imsCall == null) {
            Rlog.w("ImsPhoneConnection", "sendRttModifyResponse failed, imsCall is null");
            return;
        }
        imsCall.sendRttModifyResponse(z);
        if (!z) {
            Rlog.e("ImsPhoneConnection", "sendRttModifyResponse: foreground call has no connections");
        } else {
            setCurrentRttTextStream(rttTextStream);
        }
    }

    public void onRttMessageReceived(String str) {
        synchronized (this) {
            try {
                if (this.mRttTextHandler == null) {
                    Rlog.w("ImsPhoneConnection", "onRttMessageReceived: RTT text handler not available. Attempting to create one.");
                    if (this.mRttTextStream == null) {
                        Rlog.e("ImsPhoneConnection", "onRttMessageReceived: Unable to process incoming message. No textstream available");
                        return;
                    }
                    createRttTextHandler();
                }
                this.mRttTextHandler.sendToInCall(str);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void onRttAudioIndicatorChanged(ImsStreamMediaProfile imsStreamMediaProfile) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("android.telecom.extra.IS_RTT_AUDIO_PRESENT", imsStreamMediaProfile.isReceivingRttAudio());
        onConnectionEvent("android.telecom.event.RTT_AUDIO_INDICATION_CHANGED", bundle);
    }

    public void setCurrentRttTextStream(android.telecom.Connection.RttTextStream rttTextStream) {
        synchronized (this) {
            try {
                this.mRttTextStream = rttTextStream;
                if (this.mIsRttEnabledForCall) {
                    ImsRttTextHandler imsRttTextHandler = this.mRttTextHandler;
                    if (imsRttTextHandler != null) {
                        imsRttTextHandler.tearDown();
                        this.mRttTextHandler = null;
                    }
                    Rlog.i("ImsPhoneConnection", "setCurrentRttTextStream: Creating a text handler");
                    createRttTextHandler();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public EmergencyNumberTracker getEmergencyNumberTracker() {
        ImsPhone phone;
        ImsPhoneCallTracker imsPhoneCallTracker = this.mOwner;
        if (imsPhoneCallTracker == null || (phone = imsPhoneCallTracker.getPhone()) == null) {
            return null;
        }
        return phone.getEmergencyNumberTracker();
    }

    public boolean hasRttTextStream() {
        return this.mRttTextStream != null;
    }

    public boolean isRttEnabledForCall() {
        return this.mIsRttEnabledForCall;
    }

    public void startRttTextProcessing() {
        synchronized (this) {
            try {
                if (this.mRttTextStream == null) {
                    Rlog.w("ImsPhoneConnection", "startRttTextProcessing: no RTT text stream. Ignoring.");
                } else if (this.mRttTextHandler != null) {
                    Rlog.w("ImsPhoneConnection", "startRttTextProcessing: RTT text handler already exists");
                } else {
                    createRttTextHandler();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void resetRttTextHandler() {
        synchronized (this) {
            try {
                if (this.mRttTextHandler != null) {
                    Rlog.i("ImsPhoneConnection", "resetRttTextHandler: tearing down old RTT handler");
                    this.mRttTextHandler.tearDown();
                    this.mRttTextHandler = null;
                }
                if (this.mIsRttEnabledForCall && this.mRttTextStream != null) {
                    Rlog.i("ImsPhoneConnection", "resetRttTextHandler: re-creating RTT handler for conference");
                    createRttTextHandler();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void createRttTextHandler() {
        ImsRttTextHandler imsRttTextHandler = new ImsRttTextHandler(Looper.getMainLooper(), new ImsRttTextHandler.NetworkWriter() { // from class: com.android.internal.telephony.imsphone.ImsPhoneConnection$$ExternalSyntheticLambda0
            @Override // com.android.internal.telephony.imsphone.ImsRttTextHandler.NetworkWriter
            public final void write(String str) {
                this.f$0.lambda$createRttTextHandler$0(str);
            }
        });
        this.mRttTextHandler = imsRttTextHandler;
        imsRttTextHandler.initialize(this.mRttTextStream);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createRttTextHandler$0(String str) {
        ImsCall imsCall = getImsCall();
        if (imsCall != null) {
            imsCall.sendRttMessage(str);
        } else {
            Rlog.w("ImsPhoneConnection", "createRttTextHandler: imsCall is null");
        }
    }

    private void updateImsCallRatFromExtras(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        if (bundle.containsKey("android.telephony.ims.extra.CALL_NETWORK_TYPE") || bundle.containsKey("CallRadioTech") || bundle.containsKey("callRadioTech")) {
            ImsCall imsCall = getImsCall();
            setCallRadioTech(ServiceState.networkTypeToRilRadioTechnology(imsCall != null ? imsCall.getNetworkType() : 0));
        }
    }

    private void updateEmergencyCallFromExtras(Bundle bundle) {
        if (bundle != null && bundle.getBoolean("e_call")) {
            setIsNetworkIdentifiedEmergencyCall(true);
        }
    }

    private void updateForwardedNumberFromExtras(Bundle bundle) {
        String[] stringArray;
        if (bundle == null || !bundle.containsKey("android.telephony.ims.extra.FORWARDED_NUMBER") || (stringArray = bundle.getStringArray("android.telephony.ims.extra.FORWARDED_NUMBER")) == null) {
            return;
        }
        this.mForwardedNumber = new ArrayList<>(Arrays.asList(stringArray));
    }

    boolean updateExtras(ImsCall imsCall) {
        if (imsCall == null) {
            return false;
        }
        ImsCallProfile callProfile = imsCall.getCallProfile();
        Bundle bundle = callProfile != null ? callProfile.mCallExtras : null;
        if (bundle == null) {
            Rlog.d("ImsPhoneConnection", "Call profile extras are null.");
        }
        boolean zAreBundlesEqual = areBundlesEqual(bundle, this.mExtras);
        boolean z = !zAreBundlesEqual;
        if (zAreBundlesEqual) {
            return z;
        }
        updateImsCallRatFromExtras(bundle);
        updateEmergencyCallFromExtras(bundle);
        updateForwardedNumberFromExtras(bundle);
        synchronized (this.mExtras) {
            try {
                this.mExtras.clear();
                if (bundle != null) {
                    this.mExtras.putAll(bundle);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        maybeInjectBusinessComposerExtras(this.mExtras);
        if (Flags.isUsingVideoRingback()) {
            maybeInjectIsUsingVideoRingbackExtras(this.mExtras);
        }
        if (Flags.isUsingUnidirectionalVideoService()) {
            maybeInjectIsUsingUnidirectionalVideoServiceExtras(this.mExtras);
        }
        if (Flags.isUsingCrs()) {
            maybeInjectVideoCustomizedRingingSignalCallExtras(this.mExtras);
        }
        setConnectionExtras(this.mExtras);
        return z;
    }

    public void maybeInjectBusinessComposerExtras(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        if (!this.mIsBusinessComposerFeatureEnabled) {
            Rlog.i("ImsPhoneConnection", "mIBCE: business composer feature is NOT enabled");
            return;
        }
        try {
            if (bundle.containsKey("android.telephony.ims.extra.IS_BUSINESS_CALL") && !bundle.containsKey("android.telecom.extra.IS_BUSINESS_CALL")) {
                boolean z = bundle.getBoolean("android.telephony.ims.extra.IS_BUSINESS_CALL");
                Rlog.i("ImsPhoneConnection", String.format("mIBCE: EXTRA_IS_BUSINESS_CALL=[%s]", Boolean.valueOf(z)));
                bundle.putBoolean("android.telecom.extra.IS_BUSINESS_CALL", z);
            }
            if (!bundle.containsKey("android.telephony.ims.extra.ASSERTED_DISPLAY_NAME") || bundle.containsKey("android.telecom.extra.ASSERTED_DISPLAY_NAME")) {
                return;
            }
            String string = bundle.getString("android.telephony.ims.extra.ASSERTED_DISPLAY_NAME");
            Rlog.i("ImsPhoneConnection", String.format("mIBCE: ASSERTED_DISPLAY_NAME=[%s]", string));
            bundle.putString("android.telecom.extra.ASSERTED_DISPLAY_NAME", string);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void maybeInjectIsUsingVideoRingbackExtras(Bundle bundle) {
        if (bundle != null && isVideoRingbackEnabledByConfig(this.mOwner.getPhone())) {
            try {
                if (bundle.containsKey("android.telephony.ims.extra.IS_USING_VIDEO_RINGBACK")) {
                    boolean z = bundle.getBoolean("android.telephony.ims.extra.IS_USING_VIDEO_RINGBACK");
                    Rlog.i("ImsPhoneConnection", String.format("EXTRA_IS_USING_VIDEO_RINGBACK=[%s]", Boolean.valueOf(z)));
                    bundle.putBoolean("android.telecom.extra.IS_USING_VIDEO_RINGBACK", z);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void maybeInjectIsUsingUnidirectionalVideoServiceExtras(Bundle bundle) {
        if (bundle != null && isUnidirectionalVideoServiceEnabledByConfig(this.mOwner.getPhone())) {
            try {
                if (bundle.containsKey("android.telephony.ims.extra.IS_USING_UNIDIRECTIONAL_VIDEO_SERVICE")) {
                    boolean z = bundle.getBoolean("android.telephony.ims.extra.IS_USING_UNIDIRECTIONAL_VIDEO_SERVICE");
                    Rlog.i("ImsPhoneConnection", String.format("EXTRA_IS_USING_UNIDIRECTIONAL_VIDEO_SERVICE=[%s]", Boolean.valueOf(z)));
                    bundle.putBoolean("android.telecom.extra.IS_USING_UNIDIRECTIONAL_VIDEO_SERVICE", z);
                }
            } catch (Exception e) {
                Rlog.e("ImsPhoneConnection", "maybeInjectIsUsingUVSExtras: Exception " + e);
            }
        }
    }

    public void maybeInjectVideoCustomizedRingingSignalCallExtras(Bundle bundle) {
        if (bundle != null && isVideoCustomizedRingingSignalEnabledByConfig(this.mOwner.getPhone())) {
            try {
                if (bundle.containsKey("android.telephony.ims.extra.CRS_MEDIA_TYPE")) {
                    int i = bundle.getInt("android.telephony.ims.extra.CRS_MEDIA_TYPE");
                    Rlog.i("ImsPhoneConnection", String.format("mICRS: EXTRA_CRS_MEDIA_TYPE=[%s]", Integer.valueOf(i)));
                    bundle.putInt("android.telecom.extra.CRS_MEDIA_TYPE", i);
                }
                if (bundle.containsKey("android.telephony.ims.extra.CRS_AUDIO_MODE")) {
                    int i2 = bundle.getInt("android.telephony.ims.extra.CRS_AUDIO_MODE");
                    Rlog.i("ImsPhoneConnection", String.format("mICRS: EXTRA_CRS_MODE=[%s]", Integer.valueOf(i2)));
                    bundle.putInt("android.telecom.extra.CRS_AUDIO_MODE", i2);
                }
            } catch (Exception e) {
                Rlog.e("ImsPhoneConnection", "mICRS: maybeInjectVideoCustomizedRingingSignalCallExtras: Exception " + e);
            }
        }
    }

    public boolean getIsBusinessComposerFeatureEnabled() {
        return this.mIsBusinessComposerFeatureEnabled;
    }

    public void setIsBusinessComposerFeatureEnabled(Phone phone) {
        boolean z = isBusinessComposerEnabledByConfig(phone) && isBusinessOnlyCallComposerEnabledByUser(phone);
        this.mIsBusinessComposerFeatureEnabled = z;
        Rlog.i("ImsPhoneConnection", String.format("setIsBusinessComposerFeatureEnabled:  mIsBusinessComposerFeatureEnabled=[%b], phone=[%s]", Boolean.valueOf(z), phone));
    }

    public boolean isBusinessComposerEnabledByConfig(Phone phone) {
        return isCarrierEnabledByConfig(phone, "supports_business_call_composer_bool");
    }

    private boolean isCarrierEnabledByConfig(Phone phone, String str) {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService(CarrierConfigManager.class);
        PersistableBundle configForSubId = carrierConfigManager != null ? carrierConfigManager.getConfigForSubId(phone.getSubId()) : null;
        if (configForSubId != null) {
            return configForSubId.getBoolean(str);
        }
        return CarrierConfigManager.getDefaultConfig().getBoolean(str);
    }

    private boolean isVideoRingbackEnabledByConfig(Phone phone) {
        return isCarrierEnabledByConfig(phone, "supports_video_back_tone_bool");
    }

    private boolean isUnidirectionalVideoServiceEnabledByConfig(Phone phone) {
        return isCarrierEnabledByConfig(phone, "supports_unidirectional_video_service_bool");
    }

    public boolean isBusinessOnlyCallComposerEnabledByUser(Phone phone) {
        if (phone == null || phone.getContext() == null) {
            return false;
        }
        TelephonyManager telephonyManager = (TelephonyManager) phone.getContext().getSystemService("phone");
        if (telephonyManager != null) {
            return telephonyManager.getCallComposerStatus() == 2 || telephonyManager.getCallComposerStatus() == 1;
        }
        Rlog.e("ImsPhoneConnection", "isBusinessOnlyCallComposerEnabledByUser: TelephonyManager is null");
        return false;
    }

    private static boolean areBundlesEqual(Bundle bundle, Bundle bundle2) {
        if (bundle == null || bundle2 == null) {
            return bundle == bundle2;
        }
        if (bundle.size() != bundle2.size()) {
            return false;
        }
        for (String str : bundle.keySet()) {
            if (str != null && !Objects.equals(bundle.get(str), bundle2.get(str))) {
                return false;
            }
        }
        return true;
    }

    private int getAudioQualityFromCallProfile(ImsCallProfile imsCallProfile, ImsCallProfile imsCallProfile2) {
        ImsStreamMediaProfile imsStreamMediaProfile;
        if (imsCallProfile != null && imsCallProfile2 != null && (imsStreamMediaProfile = imsCallProfile.mMediaProfile) != null) {
            int i = imsStreamMediaProfile.mAudioQuality;
            boolean z = i == 18 || i == 19 || i == 20;
            if ((i == 2 || i == 6 || z) && imsCallProfile2.getRestrictCause() == 0) {
                return 2;
            }
        }
        return 1;
    }

    @Override // com.android.internal.telephony.Connection
    public String toString() {
        StringBuilder sb = new StringBuilder("[ImsPhoneConnection objId: ");
        sb.append(System.identityHashCode(this));
        sb.append(" telecomCallID: ");
        sb.append(getTelecomCallId());
        sb.append(" address: ");
        sb.append(Rlog.pii("ImsPhoneConnection", getAddress()));
        sb.append(" isAdhocConf: ");
        sb.append(isAdhocConference() ? "Y" : "N");
        sb.append(" ImsCall: ");
        synchronized (this) {
            try {
                ImsCall imsCall = this.mImsCall;
                if (imsCall == null) {
                    sb.append("null");
                } else {
                    sb.append(imsCall);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override // com.android.internal.telephony.Connection
    public void setVideoProvider(android.telecom.Connection.VideoProvider videoProvider) {
        super.setVideoProvider(videoProvider);
        if (videoProvider instanceof ImsVideoCallProviderWrapper) {
            this.mImsVideoCallProviderWrapper = (ImsVideoCallProviderWrapper) videoProvider;
        }
    }

    public boolean isEmergency() {
        return this.mIsEmergency;
    }

    protected boolean isWpsCall() {
        return this.mIsWpsCall;
    }

    public boolean isCrossSimCall() {
        ImsCall imsCall = this.mImsCall;
        if (imsCall == null) {
            return this.mExtras.getBoolean("android.telephony.ims.extra.IS_CROSS_SIM_CALL");
        }
        return imsCall.isCrossSimCall();
    }

    public void onReceiveSessionModifyResponse(int i, VideoProfile videoProfile, VideoProfile videoProfile2) {
        if (i == 1 && this.mShouldIgnoreVideoStateChanges) {
            int videoState = getVideoState();
            int videoState2 = videoProfile2.getVideoState();
            int i2 = (videoState ^ videoState2) & 3;
            if (i2 == 0) {
                return;
            }
            int i3 = (videoState & (~(i2 & videoState))) | (videoState2 & i2);
            Rlog.d("ImsPhoneConnection", "onReceiveSessionModifyResponse : received " + VideoProfile.videoStateToString(videoProfile.getVideoState()) + " / " + VideoProfile.videoStateToString(videoProfile2.getVideoState()) + " while paused ; sending new videoState = " + VideoProfile.videoStateToString(i3));
            setVideoState(i3);
        }
    }

    public void pauseVideo(int i) {
        ImsVideoCallProviderWrapper imsVideoCallProviderWrapper = this.mImsVideoCallProviderWrapper;
        if (imsVideoCallProviderWrapper == null) {
            return;
        }
        imsVideoCallProviderWrapper.pauseVideo(getVideoState(), i);
    }

    public void resumeVideo(int i) {
        ImsVideoCallProviderWrapper imsVideoCallProviderWrapper = this.mImsVideoCallProviderWrapper;
        if (imsVideoCallProviderWrapper == null) {
            return;
        }
        imsVideoCallProviderWrapper.resumeVideo(getVideoState(), i);
    }

    public boolean wasVideoPausedFromSource(int i) {
        ImsVideoCallProviderWrapper imsVideoCallProviderWrapper = this.mImsVideoCallProviderWrapper;
        if (imsVideoCallProviderWrapper == null) {
            return false;
        }
        return imsVideoCallProviderWrapper.wasVideoPausedFromSource(i);
    }

    public void handleMergeStart() {
        this.mIsMergeInProcess = true;
        onConnectionEvent("android.telecom.event.MERGE_START", null);
    }

    public void handleMergeComplete() {
        this.mIsMergeInProcess = false;
    }

    public void setRemotelyHeld() {
        this.mIsHeldByRemote = true;
        onConnectionEvent("android.telecom.event.CALL_REMOTELY_HELD", null);
    }

    public void setRemotelyUnheld() {
        this.mIsHeldByRemote = false;
        onConnectionEvent("android.telecom.event.CALL_REMOTELY_UNHELD", null);
    }

    public boolean isHeldByRemote() {
        Rlog.i("ImsPhoneConnection", "isHeldByRemote=" + this.mIsHeldByRemote);
        return this.mIsHeldByRemote;
    }

    public void changeToPausedState() {
        int videoState = getVideoState() | 4;
        Rlog.i("ImsPhoneConnection", "ImsPhoneConnection: changeToPausedState - setting paused bit; newVideoState=" + VideoProfile.videoStateToString(videoState));
        updateVideoState(videoState);
        this.mShouldIgnoreVideoStateChanges = true;
    }

    public void changeToUnPausedState() {
        int videoState = getVideoState() & (-5);
        Rlog.i("ImsPhoneConnection", "ImsPhoneConnection: changeToUnPausedState - unsetting paused bit; newVideoState=" + VideoProfile.videoStateToString(videoState));
        updateVideoState(videoState);
        this.mShouldIgnoreVideoStateChanges = false;
    }

    public void setLocalVideoCapable(boolean z) {
        this.mIsLocalVideoCapable = z;
        Rlog.i("ImsPhoneConnection", "setLocalVideoCapable: mIsLocalVideoCapable = " + this.mIsLocalVideoCapable + "; updating local video availability.");
        updateMediaCapabilities(getImsCall());
    }

    public void sendRtpHeaderExtensions(Set<RtpHeaderExtension> set) {
        if (this.mImsCall == null) {
            Rlog.w("ImsPhoneConnection", "sendRtpHeaderExtensions: Not an IMS call");
            return;
        }
        Rlog.i("ImsPhoneConnection", "sendRtpHeaderExtensions: numExtensions = " + set.size());
        this.mImsCall.sendRtpHeaderExtensions(set);
    }

    public Set<RtpHeaderExtensionType> getAcceptedRtpHeaderExtensions() {
        ImsCall imsCall = this.mImsCall;
        if (imsCall == null || imsCall.getCallProfile() == null) {
            return Collections.EMPTY_SET;
        }
        return this.mImsCall.getCallProfile().getAcceptedRtpHeaderExtensionTypes();
    }

    public void setImsReasonInfo(ImsReasonInfo imsReasonInfo) {
        this.mImsReasonInfo = imsReasonInfo;
    }

    public ImsReasonInfo getImsReasonInfo() {
        return this.mImsReasonInfo;
    }

    protected int getCallPriority() {
        if (isEmergency()) {
            return 2;
        }
        return isWpsCall() ? 1 : 0;
    }

    private boolean isVideoCustomizedRingingSignalEnabledByConfig(Phone phone) {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(phone.getSubId())) == null) {
            return CarrierConfigManager.getDefaultConfig().getBoolean("supports_customized_ringing_signal_bool");
        }
        return configForSubId.getBoolean("supports_customized_ringing_signal_bool");
    }

    protected int callNumberPresentation(ImsCallProfile imsCallProfile) {
        return ImsCallProfile.OIRToPresentation(imsCallProfile.getCallExtraInt("oir"));
    }

    protected boolean needUpdateAddress(String str) {
        return !equalsBaseDialString(this.mAddress, str);
    }
}
