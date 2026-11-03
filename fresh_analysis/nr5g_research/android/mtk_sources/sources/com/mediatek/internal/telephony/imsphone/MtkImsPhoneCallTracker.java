package com.mediatek.internal.telephony.imsphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telecom.VideoProfile;
import android.telephony.CallQuality;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyLocalConnection;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsStreamMediaProfile;
import android.telephony.ims.ImsSuppServiceNotification;
import android.telephony.ims.RtpHeaderExtension;
import android.telephony.ims.feature.ImsFeature;
import android.text.TextUtils;
import com.android.ims.FeatureConnector;
import com.android.ims.ImsCall;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;
import com.android.ims.ImsUtInterface;
import com.android.ims.internal.ConferenceParticipant;
import com.android.ims.internal.IImsCallSession;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.SettingsObserver;
import com.android.internal.telephony.SomeArgs;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCall;
import com.android.internal.telephony.imsphone.ImsPhoneCallTracker;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;
import com.android.internal.telephony.imsphone.ImsPullCall;
import com.mediatek.ims.MtkImsCall;
import com.mediatek.ims.MtkImsConnectionStateListener;
import com.mediatek.ims.internal.MtkImsManager;
import com.mediatek.internal.telephony.MtkGsmCdmaPhone;
import com.mediatek.internal.telephony.MtkIncomingCallChecker;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import mediatek.telecom.FormattedLog;
import mediatek.telephony.MtkDisconnectCause;
import mediatek.telephony.MtkTelephony;

/* JADX INFO: loaded from: classes.dex */
public class MtkImsPhoneCallTracker extends ImsPhoneCallTracker implements ImsPullCall {
    private static final int CODE_WFC_403_NOT_ALLOWED_FROM_THIS_REGION = 1606;
    private static final int EVENT_RESUME_BACKROUND_CALL = 104;
    private static final int EVENT_RETRY_DATA_ENABLED_CHANGED = 105;
    private static final int EVENT_ROAMING_OFF = 102;
    private static final int EVENT_ROAMING_ON = 101;
    private static final int EVENT_ROAMING_SETTING_CHANGE = 103;
    public static final int IMS_SESSION_MODIFY_OPERATION_FLAG = 32768;
    private static final int IMS_VIDEO_CALL = 21;
    private static final int IMS_VIDEO_CONF = 23;
    private static final int IMS_VIDEO_CONF_PARTS = 25;
    private static final int IMS_VOICE_CALL = 20;
    private static final int IMS_VOICE_CONF = 22;
    private static final int IMS_VOICE_CONF_PARTS = 24;
    private static final int INVALID_CALL_MODE = 255;
    static final String LOG_TAG = "MtkImsPhoneCallTracker";
    private static final String PROP_FORCE_DEBUG_KEY = "persist.vendor.log.tel_dbg";
    private static final boolean SENLOG = TextUtils.equals(Build.TYPE, "user");
    private static final boolean TELDBG;
    private RegistrantList mCallsDisconnectedDuringSrvccRegistrants;
    private boolean mCarrierSwitchWfcModeRequired;
    private boolean mDialAsECC;
    private boolean mIgnoreClirWhenEcc;
    private boolean mIgnoreDataRoaming;
    protected BroadcastReceiver mImsBaseReceiver;
    private int mImsRegistrationErrorCode;
    private MtkImsConnectionStateListener mImsStateListener;
    private MtkIncomingCallChecker mIncomingCallCheker;
    private IncomingCallEventRecevier mIndicationReceiver;
    private boolean mIsDataRoaming;
    private boolean mIsDataRoamingSettingEnabled;
    private boolean mIsImsEccSupported;
    private boolean mIsOnCallResumed;
    private int mLastDataEnabledReason;
    private ImsCall.Listener mMtkImsCallListener;
    private BroadcastReceiver mMultiSimConfigChangedReceiver;
    protected final SubscriptionManager.OnSubscriptionsChangedListener mOnSubscriptionsChangedListener;
    private boolean mRoamingVariablesInited;
    private final SettingsObserver mSettingsObserver;
    private SubscriptionManager mSubscriptionManager;
    private int mWifiPdnOOSState;

    static {
        TELDBG = SystemProperties.getInt(PROP_FORCE_DEBUG_KEY, 0) == 1;
    }

    public boolean isSupportImsEcc() {
        return this.mIsImsEccSupported;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public MtkImsPhoneCallTracker(ImsPhone phone, FeatureFlags featureFlags) {
        super(phone, new ImsPhoneCallTracker.ConnectorFactory() { // from class: com.mediatek.internal.telephony.imsphone.MtkImsPhoneCallTracker$$ExternalSyntheticLambda0
            public final FeatureConnector create(Context context, int i, String str, FeatureConnector.Listener listener, Executor executor) {
                return ImsManager.getConnector(context, i, str, listener, executor);
            }
        }, featureFlags);
        this.mRingingCall = new MtkImsPhoneCall(this, "RG");
        this.mForegroundCall = new MtkImsPhoneCall(this, "FG");
        this.mBackgroundCall = new MtkImsPhoneCall(this, "BG");
        this.mHandoverCall = new MtkImsPhoneCall(this, "HO");
        this.mDialAsECC = false;
        this.mIsOnCallResumed = false;
        this.mIsImsEccSupported = false;
        this.mWifiPdnOOSState = 2;
        this.mRoamingVariablesInited = false;
        this.mOnSubscriptionsChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.mediatek.internal.telephony.imsphone.MtkImsPhoneCallTracker.1
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                MtkImsPhoneCallTracker.this.log("SubscriptionListener.onSubscriptionInfoChanged, subId=" + MtkImsPhoneCallTracker.this.mPhone.getSubId());
                int subId = MtkImsPhoneCallTracker.this.mPhone.getSubId();
                if (SubscriptionManager.isValidSubscriptionId(subId)) {
                    if (!MtkImsPhoneCallTracker.this.mRoamingVariablesInited) {
                        MtkImsPhoneCallTracker.this.mRoamingVariablesInited = true;
                        MtkImsPhoneCallTracker.this.initRoamingAndRoamingSetting(subId);
                    }
                    MtkImsPhoneCallTracker.this.registerSettingsObserver();
                }
            }
        };
        this.mIsDataRoaming = false;
        this.mIsDataRoamingSettingEnabled = false;
        this.mIgnoreDataRoaming = false;
        this.mIgnoreClirWhenEcc = true;
        this.mCarrierSwitchWfcModeRequired = false;
        this.mIncomingCallCheker = null;
        this.mCallsDisconnectedDuringSrvccRegistrants = new RegistrantList();
        this.mImsStateListener = new MtkImsConnectionStateListener() { // from class: com.mediatek.internal.telephony.imsphone.MtkImsPhoneCallTracker.2
            public void onImsEmergencyCapabilityChanged(boolean eccSupport) {
                MtkImsPhoneCallTracker.this.log("onImsEmergencyCapabilityChanged: " + eccSupport);
                MtkImsPhoneCallTracker.this.mPhone.onFeatureCapabilityChanged();
                MtkImsPhoneCallTracker.this.mIsImsEccSupported = eccSupport;
                ((MtkImsPhone) MtkImsPhoneCallTracker.this.mPhone).updateIsEmergencyOnly();
            }

            public void onWifiPdnOOSStateChanged(int oosState) {
                MtkImsPhoneCallTracker.this.log("onWifiPdnOOSStateChanged: " + oosState);
                MtkImsPhoneCallTracker.this.mWifiPdnOOSState = oosState;
            }

            public void onRegistrationErrorCodeInd(int errorCode) {
                MtkImsPhoneCallTracker.this.log("onRegistrationErrorCodeInd: " + errorCode);
                ImsReasonInfo imsReasonInfo = new ImsReasonInfo(MtkDisconnectCause.MTK_DISCONNECTED_CAUSE_BASE, errorCode, Integer.toString(errorCode));
                MtkImsPhoneCallTracker.this.mPhone.processDisconnectReason(imsReasonInfo);
                if (errorCode == MtkImsPhoneCallTracker.CODE_WFC_403_NOT_ALLOWED_FROM_THIS_REGION && MtkImsPhoneCallTracker.this.mImsManager != null) {
                    MtkImsPhoneCallTracker.this.mImsManager.setWfcSetting(false);
                }
            }

            public void onCapabilitiesStatusChanged(ImsFeature.Capabilities capabilities) {
                MtkImsPhoneCallTracker.this.log("onCapabilitiesStatusChanged: " + capabilities);
                SomeArgs args = SomeArgs.obtain();
                args.arg1 = capabilities;
                MtkImsPhoneCallTracker.this.removeMessages(26);
                MtkImsPhoneCallTracker.this.obtainMessage(26, args).sendToTarget();
            }
        };
        this.mMtkImsCallListener = new MtkImsCall.Listener() { // from class: com.mediatek.internal.telephony.imsphone.MtkImsPhoneCallTracker.3
            public void onCallProgressing(ImsCall imsCall) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallProgressing(imsCall);
                ImsPhoneConnection conn = MtkImsPhoneCallTracker.this.findConnection(imsCall);
                if (conn != null) {
                    conn.onConnectionEvent("mediatek.telecom.event.EVENT_CALL_ALERTING_NOTIFICATION", (Bundle) null);
                }
            }

            public void onCallStarted(ImsCall imsCall) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallStarted(imsCall);
            }

            public void onCallUpdated(ImsCall imsCall) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallUpdated(imsCall);
            }

            public void onCallStartFailed(ImsCall imsCall, ImsReasonInfo reasonInfo) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallStartFailed(imsCall, reasonInfo);
                if (MtkImsPhoneCallTracker.this.mBackgroundCall.getState() == Call.State.HOLDING) {
                    MtkImsPhoneCallTracker.this.log("auto resume holding call");
                    MtkImsPhoneCallTracker.this.sendEmptyMessage(104);
                }
            }

            public void onCallTerminated(ImsCall imsCall, ImsReasonInfo reasonInfo) {
                MtkImsPhoneCallTracker.this.removeMessages(105);
                MtkImsPhoneCallTracker.this.log("remove EVENT_RETRY_DATA_ENABLED_CHANGED for call termination");
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallTerminated(imsCall, reasonInfo);
            }

            public void onCallHeld(ImsCall imsCall) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallHeld(imsCall);
            }

            public void onCallHoldFailed(ImsCall imsCall, ImsReasonInfo reasonInfo) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallHoldFailed(imsCall, reasonInfo);
            }

            public void onCallResumed(ImsCall imsCall) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallResumed(imsCall);
            }

            public void onCallResumeFailed(ImsCall imsCall, ImsReasonInfo reasonInfo) {
                MtkImsPhoneCallTracker.this.log("onCallResumeFailed reasonCode=" + reasonInfo.getCode());
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallResumeFailed(imsCall, reasonInfo);
            }

            public void onCallResumeReceived(ImsCall imsCall) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallResumeReceived(imsCall);
            }

            public void onCallHoldReceived(ImsCall imsCall) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallHoldReceived(imsCall);
            }

            public void onCallSuppServiceReceived(ImsCall call, ImsSuppServiceNotification suppServiceInfo) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallSuppServiceReceived(call, suppServiceInfo);
            }

            public void onCallMerged(ImsCall call, ImsCall peerCall, boolean swapCalls) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallMerged(call, peerCall, swapCalls);
                ImsPhoneConnection hostConn = MtkImsPhoneCallTracker.this.findConnection(call);
                if (hostConn != null && (hostConn instanceof MtkImsPhoneConnection)) {
                    MtkImsPhoneConnection hostConnExt = (MtkImsPhoneConnection) hostConn;
                    FormattedLog formattedLog = new FormattedLog.Builder().setCategory("CC").setServiceName("ImsPhone").setOpType(FormattedLog.OpType.DUMP).setCallNumber(MtkImsPhoneCallTracker.sensitiveEncode(hostConn.getAddress())).setCallId(MtkImsPhoneCallTracker.this.getConnectionCallId(hostConnExt)).setStatusInfo("state", "disconnected").setStatusInfo("isConfCall", "No").setStatusInfo("isConfChildCall", "No").setStatusInfo("parent", hostConnExt.getParentCallName()).buildDumpInfo();
                    if (formattedLog != null) {
                        if (!MtkImsPhoneCallTracker.SENLOG || MtkImsPhoneCallTracker.TELDBG) {
                            MtkImsPhoneCallTracker.this.log(formattedLog.toString());
                        }
                    }
                }
            }

            public void onCallMergeFailed(ImsCall call, ImsReasonInfo reasonInfo) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallMergeFailed(call, reasonInfo);
            }

            public void onConferenceParticipantsStateChanged(ImsCall call, List<ConferenceParticipant> participants) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onConferenceParticipantsStateChanged(call, participants);
            }

            public void onCallSessionTtyModeReceived(ImsCall call, int mode) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallSessionTtyModeReceived(call, mode);
            }

            public void onCallHandover(ImsCall imsCall, int srcAccessTech, int targetAccessTech, ImsReasonInfo reasonInfo) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallHandover(imsCall, srcAccessTech, targetAccessTech, reasonInfo);
            }

            public void onCallHandoverFailed(ImsCall imsCall, int srcAccessTech, int targetAccessTech, ImsReasonInfo reasonInfo) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallHandoverFailed(imsCall, srcAccessTech, targetAccessTech, reasonInfo);
            }

            public void onRttModifyRequestReceived(ImsCall imsCall) {
                MtkImsPhoneCallTracker.this.log("onRttModifyRequestReceived");
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onRttModifyRequestReceived(imsCall);
            }

            public void onRttModifyResponseReceived(ImsCall imsCall, int status) {
                MtkImsPhoneCallTracker.this.log("onRttModifyResponseReceived");
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onRttModifyResponseReceived(imsCall, status);
            }

            public void onRttMessageReceived(ImsCall imsCall, String message) {
                MtkImsPhoneCallTracker.this.log("onRttMessageReceived");
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onRttMessageReceived(imsCall, message);
            }

            public void onRttAudioIndicatorChanged(ImsCall imsCall, ImsStreamMediaProfile profile) {
                MtkImsPhoneCallTracker.this.log("onRttAudioIndicatorChanged");
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onRttAudioIndicatorChanged(imsCall, profile);
            }

            public void onCallSessionTransferred(ImsCall imsCall) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallSessionTransferred(imsCall);
            }

            public void onCallSessionTransferFailed(ImsCall imsCall, ImsReasonInfo reasonInfo) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallSessionTransferFailed(imsCall, reasonInfo);
            }

            public void onMultipartyStateChanged(ImsCall imsCall, boolean isMultiParty) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onMultipartyStateChanged(imsCall, isMultiParty);
            }

            public void onCallInviteParticipantsRequestDelivered(ImsCall call) {
                MtkImsPhoneCallTracker.this.log("onCallInviteParticipantsRequestDelivered");
                ImsPhoneConnection conn = MtkImsPhoneCallTracker.this.findConnection(call);
                if (conn != null && (conn instanceof MtkImsPhoneConnection)) {
                    ((MtkImsPhoneConnection) conn).notifyConferenceParticipantsInvited(true);
                }
            }

            public void onCallInviteParticipantsRequestFailed(ImsCall call, ImsReasonInfo reasonInfo) {
                MtkImsPhoneCallTracker.this.log("onCallInviteParticipantsRequestFailed reasonCode=" + reasonInfo.getCode());
                ImsPhoneConnection conn = MtkImsPhoneCallTracker.this.findConnection(call);
                if (conn != null && (conn instanceof MtkImsPhoneConnection)) {
                    ((MtkImsPhoneConnection) conn).notifyConferenceParticipantsInvited(false);
                }
            }

            public void onCallSessionDtmfReceived(ImsCall imsCall, char digit) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallSessionDtmfReceived(imsCall, digit);
            }

            public void onCallQualityChanged(ImsCall imsCall, CallQuality callQuality) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallQualityChanged(imsCall, callQuality);
            }

            public void onCallSessionRtpHeaderExtensionsReceived(ImsCall imsCall, Set<RtpHeaderExtension> rtpHeaderExtensionData) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallSessionRtpHeaderExtensionsReceived(imsCall, rtpHeaderExtensionData);
            }

            public void onCallSessionSendAnbrQuery(ImsCall imsCall, int mediaType, int direction, int bitsPerSecond) {
                ((ImsPhoneCallTracker) MtkImsPhoneCallTracker.this).mImsCallListener.onCallSessionSendAnbrQuery(imsCall, mediaType, direction, bitsPerSecond);
            }

            public void onTextCapabilityChanged(ImsCall call, int localCapability, int remoteCapability, int localTextStatus, int realRemoteCapability) {
                ImsPhoneConnection conn = MtkImsPhoneCallTracker.this.findConnection(call);
                boolean rttStatusLocal = localTextStatus == 1;
                boolean rttStatusRemote = remoteCapability == 1;
                boolean rttSupportLocal = localCapability == 1;
                boolean rttSupportRemote = realRemoteCapability == 1;
                MtkImsPhoneCallTracker.this.log("onTextCapabilityChanged localCapability: " + localCapability + " remote status: " + remoteCapability + " localTextStatus" + localTextStatus + " RemoteCapability: " + realRemoteCapability);
                Bundle bundle = new Bundle();
                bundle.putBoolean("mediatek.telecom.extra.RTT_STATUS_LOCAL", rttStatusLocal);
                bundle.putBoolean("mediatek.telecom.extra.RTT_STATUS_REMOTE", rttStatusRemote);
                bundle.putBoolean("mediatek.telecom.extra.RTT_SUPPORT_LOCAL", rttSupportLocal);
                bundle.putBoolean("mediatek.telecom.extra.RTT_SUPPORT_REMOTE", rttSupportRemote);
                if (conn != null) {
                    conn.onConnectionEvent("mediatek.telecom.event.RTT_SUPPORT_CHANGED", bundle);
                    MtkImsPhoneCallTracker.this.log("onTextCapabilityChanged update to conn");
                }
                if (localCapability == 1 && remoteCapability == 1) {
                    AudioManager audioManager = (AudioManager) MtkImsPhoneCallTracker.this.mPhone.getContext().getSystemService("audio");
                    audioManager.setParameters("ims_rtt_capability=1");
                    MtkImsPhoneCallTracker.this.log("onTextCapabilityChanged: set audio RTT capability");
                }
            }

            public void onRttEventReceived(ImsCall call, int event) {
                ImsPhoneConnection conn = MtkImsPhoneCallTracker.this.findConnection(call);
                if (conn != null) {
                    conn.onConnectionEvent("mediatek.telecom.event.EVENT_RTT_EMERGENCY_REDIAL", (Bundle) null);
                }
            }

            public void onCallRedialEcc(ImsCall call, boolean isNeedUserConfirm) {
                MtkImsPhoneCallTracker.this.log("onCallRedialEcc");
                ImsPhoneConnection conn = MtkImsPhoneCallTracker.this.findConnection(call);
                if (conn != null && (conn instanceof MtkImsPhoneConnection)) {
                    ((MtkImsPhoneConnection) conn).notifyRedialEcc(isNeedUserConfirm);
                }
            }

            public void onVideoRingtoneEventReceived(ImsCall call, int eventType, String event) {
                MtkImsPhoneCallTracker.this.log("onVideoRingtoneEventReceived");
                ImsPhoneConnection conn = MtkImsPhoneCallTracker.this.findConnection(call);
                if (conn != null && (conn instanceof MtkImsPhoneConnection)) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", eventType);
                    bundle.putString("data", event);
                    conn.onConnectionEvent("mediatek.telecom.event.IMS_EVENT_VIDEO_RINGTONE", bundle);
                }
            }

            public void onNotificationRingtoneReceived(ImsCall call, int causeNum, String causeText) {
                MtkImsPhoneCallTracker.this.log("onNotificationRingtoneReceived");
                ImsPhoneConnection conn = MtkImsPhoneCallTracker.this.findConnection(call);
                if (conn != null && (conn instanceof MtkImsPhoneConnection)) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("cause", causeNum);
                    bundle.putString(MtkTelephony.WapPush.TEXT, causeText);
                    conn.onConnectionEvent("mediatek.telecom.event.IMS_EVENT_NOTIFICATION_RINGTONE", bundle);
                }
            }
        };
        this.mImsBaseReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.imsphone.MtkImsPhoneCallTracker.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("com.mediatek.ims.MTK_IMS_SERVICE_UP")) {
                    try {
                        MtkImsManager imsMgr = ImsManager.getInstance(MtkImsPhoneCallTracker.this.mPhone.getContext(), MtkImsPhoneCallTracker.this.mPhone.getPhoneId());
                        imsMgr.removeImsConnectionStateListener(MtkImsPhoneCallTracker.this.mImsStateListener);
                        imsMgr.addImsConnectionStateListener(MtkImsPhoneCallTracker.this.mImsStateListener);
                        MtkImsPhoneCallTracker.this.loge("ACTION_MTK_IMS_SERVICE_UP: register ims succeed, " + MtkImsPhoneCallTracker.this.mImsStateListener);
                    } catch (ImsException e) {
                        MtkImsPhoneCallTracker.this.loge("ACTION_MTK_IMS_SERVICE_UP: register ims fail!");
                    }
                }
            }
        };
        this.mMultiSimConfigChangedReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.imsphone.MtkImsPhoneCallTracker.5
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                int numOfActiveModems = intent.getIntExtra("android.telephony.extra.ACTIVE_SIM_SUPPORTED_COUNT", 0);
                MtkImsPhoneCallTracker.this.log("ACTION_MULTI_SIM_CONFIG_CHANGED, numOfActiveModems: " + numOfActiveModems);
            }
        };
        this.mIndicationReceiver = new IncomingCallEventRecevier();
        this.mIsDataEnabled = this.mPhone.getDefaultPhone().getDataNetworkController().getDataSettingsManager().isDataEnabled();
        registerIndicationReceiver();
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction("com.mediatek.ims.MTK_IMS_SERVICE_UP");
        this.mPhone.getContext().registerReceiver(this.mImsBaseReceiver, intentfilter);
        IntentFilter multiSimConfigChanged = new IntentFilter();
        multiSimConfigChanged.addAction("android.telephony.action.MULTI_SIM_CONFIG_CHANGED");
        this.mPhone.getContext().registerReceiver(this.mMultiSimConfigChangedReceiver, multiSimConfigChanged);
        this.mSettingsObserver = new SettingsObserver(this.mPhone.getContext(), this);
        registerSettingsObserver();
        this.mSubscriptionManager = SubscriptionManager.from(this.mPhone.getContext());
        this.mSubscriptionManager.addOnSubscriptionsChangedListener(this.mOnSubscriptionsChangedListener);
        this.mPhone.getDefaultPhone().getServiceStateTracker().registerForDataRoamingOn(this, 101, (Object) null);
        this.mPhone.getDefaultPhone().getServiceStateTracker().registerForDataRoamingOff(this, 102, (Object) null, true);
    }

    public synchronized Connection dial(String dialString, ImsPhone.ImsDialArgs dialArgs) throws CallStateException {
        if (this.mSrvccState == Call.SrvccState.STARTED || this.mSrvccState == Call.SrvccState.COMPLETED) {
            throw new CallStateException(3, "cannot dial call: SRVCC");
        }
        return super.dial(dialString, dialArgs);
    }

    protected void dialInternal(ImsPhoneConnection conn, int clirMode, int videoState, int retryCallFailCause, int retryCallFailNetworkType, Bundle intentExtras) {
        if (conn == null) {
            return;
        }
        if (!conn.isAdhocConference() && (conn.getAddress() == null || conn.getAddress().length() == 0 || conn.getAddress().indexOf(78) >= 0)) {
            conn.setDisconnectCause(7);
            sendEmptyMessageDelayed(18, 500L);
            return;
        }
        char c = 0;
        setMute(false);
        boolean isEmergencyCall = conn.isAdhocConference() ? false : isEmergencyNumber(conn.getAddress());
        int serviceType = isEmergencyCall ? 2 : 1;
        if (this.mDialAsECC) {
            serviceType = 2;
            log("Dial as ECC: conn.getAddress(): " + sensitiveEncode(conn.getAddress()));
            this.mDialAsECC = false;
        }
        int callType = ImsCallProfile.getCallTypeFromVideoState(videoState);
        conn.setVideoState(videoState);
        try {
            String[] callees = {conn.getAddress()};
            ImsCallProfile profile = this.mImsManager.createCallProfile(serviceType, callType);
            if (conn.isAdhocConference()) {
                profile.setCallExtraBoolean("android.telephony.ims.extra.CONFERENCE", true);
                profile.setCallExtraBoolean("conference", true);
            }
            try {
                profile.setCallExtraInt("oir", clirMode);
                try {
                    profile.setCallExtraInt("android.telephony.ims.extra.RETRY_CALL_FAIL_REASON", retryCallFailCause);
                    try {
                        profile.setCallExtraInt("android.telephony.ims.extra.RETRY_CALL_FAIL_NETWORKTYPE", retryCallFailNetworkType);
                        if (isEmergencyCall) {
                            setEmergencyCallInfo(profile, conn);
                        }
                        if (intentExtras != null) {
                            if (intentExtras.containsKey("android.telecom.extra.CALL_SUBJECT")) {
                                intentExtras.putString("DisplayText", cleanseInstantLetteringMessage(intentExtras.getString("android.telecom.extra.CALL_SUBJECT")));
                                profile.setCallExtra("android.telephony.ims.extra.CALL_SUBJECT", intentExtras.getString("android.telecom.extra.CALL_SUBJECT"));
                            }
                            if (intentExtras.containsKey("android.telecom.extra.PRIORITY")) {
                                profile.setCallExtraInt("android.telephony.ims.extra.PRIORITY", intentExtras.getInt("android.telecom.extra.PRIORITY"));
                            }
                            if (intentExtras.containsKey("android.telecom.extra.LOCATION")) {
                                profile.setCallExtraParcelable("android.telephony.ims.extra.LOCATION", intentExtras.getParcelable("android.telecom.extra.LOCATION"));
                            }
                            if (intentExtras.containsKey("android.telecom.extra.OUTGOING_PICTURE")) {
                                String url = TelephonyLocalConnection.getCallComposerServerUrlForHandle(this.mPhone.getSubId(), ((ParcelUuid) intentExtras.getParcelable("android.telecom.extra.OUTGOING_PICTURE")).getUuid());
                                profile.setCallExtra("android.telephony.ims.extra.PICTURE_URL", url);
                            }
                            if (conn.hasRttTextStream()) {
                                profile.mMediaProfile.mRttMode = 1;
                            }
                            if (intentExtras.containsKey("CallPull")) {
                                profile.mCallExtras.putBoolean("CallPull", intentExtras.getBoolean("CallPull"));
                                int dialogId = intentExtras.getInt("android.telephony.ImsExternalCallTracker.extra.EXTERNAL_CALL_ID");
                                conn.setIsPulledCall(true);
                                conn.setPulledDialogId(dialogId);
                            }
                            profile.mCallExtras.putBundle("android.telephony.ims.extra.OEM_EXTRAS", intentExtras);
                        } else {
                            c = 0;
                        }
                        if (callees.length == 1 && !profile.getCallExtraBoolean("android.telephony.ims.extra.CONFERENCE")) {
                            profile.setCallExtra("oi", callees[c]);
                        }
                        this.mPhone.getVoiceCallSessionStats().onImsDial(conn);
                        synchronized (this) {
                            try {
                                ImsCall imsCall = this.mImsManager.makeCall(profile, conn.isAdhocConference() ? conn.getParticipantsToDial() : callees, this.mMtkImsCallListener);
                                conn.setImsCall(imsCall);
                                setVideoCallProvider(conn, imsCall);
                                conn.setAllowAddCallDuringVideoCall(this.mAllowAddCallDuringVideoCall);
                                conn.setAllowHoldingVideoCall(this.mAllowHoldingVideoCall);
                                this.mImsCallInfoTracker.addImsCallStatus(conn);
                            } catch (Throwable th) {
                                throw th;
                            }
                        }
                    } catch (ImsException e) {
                        e = e;
                        loge("dialInternal : " + e);
                        this.mOperationLocalLog.log("dialInternal exception: " + e);
                        conn.setDisconnectCause(36);
                        sendEmptyMessageDelayed(18, 500L);
                    } catch (RemoteException e2) {
                    }
                } catch (ImsException e3) {
                    e = e3;
                    loge("dialInternal : " + e);
                    this.mOperationLocalLog.log("dialInternal exception: " + e);
                    conn.setDisconnectCause(36);
                    sendEmptyMessageDelayed(18, 500L);
                } catch (RemoteException e4) {
                }
            } catch (RemoteException e5) {
            } catch (ImsException e6) {
                e = e6;
                loge("dialInternal : " + e);
                this.mOperationLocalLog.log("dialInternal exception: " + e);
                conn.setDisconnectCause(36);
                sendEmptyMessageDelayed(18, 500L);
            }
        } catch (RemoteException e7) {
        } catch (ImsException e8) {
            e = e8;
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.CallStateException */
    public void acceptCall(int videoState) throws CallStateException {
        if (this.mSrvccState == Call.SrvccState.STARTED || this.mSrvccState == Call.SrvccState.COMPLETED) {
            throw new CallStateException(3, "cannot accept call: SRVCC");
        }
        int videoStateAfterCheckingData = videoState;
        if (!isDataAvailableForViLTE() && !this.mRingingCall.getImsCall().isWifiCall()) {
            videoStateAfterCheckingData = 0;
            log("Data is off, answer as voice call");
        }
        logDebugMessagesWithOpFormat("CC", "Answer", this.mRingingCall.getFirstConnection(), "");
        super.acceptCall(videoStateAfterCheckingData);
    }

    public void rejectCall() throws CallStateException {
        logDebugMessagesWithOpFormat("CC", "Reject", this.mRingingCall.getFirstConnection(), "");
        super.rejectCall();
    }

    public void conference() {
        logDebugMessagesWithOpFormat("CC", "Conference", this.mForegroundCall.getFirstConnection(), " merge with " + this.mBackgroundCall.getFirstConnection());
        if (this.mHoldSwitchingState == ImsPhoneCallTracker.HoldSwapState.SWAPPING_ACTIVE_AND_HELD) {
            log("Can't merge during swap call.");
        } else {
            super.conference();
        }
    }

    private boolean isRttCall(ImsCall call) {
        if (call != null) {
            return call.getCallProfile().mMediaProfile.isRttCall();
        }
        return false;
    }

    public void checkForDialIssues() throws CallStateException {
        if (this.mPhone != null && (this.mPhone.getDefaultPhone() instanceof MtkGsmCdmaPhone) && this.mPhone.getDefaultPhone().shouldProcessSelfActivation()) {
            log("IMS: checkForDialIssues(), bypass checkForDialIssues for self activation");
        } else {
            super.checkForDialIssues();
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.CallStateException */
    public void hangup(ImsPhoneCall call) throws CallStateException {
        if (this.mSrvccState == Call.SrvccState.STARTED || this.mSrvccState == Call.SrvccState.COMPLETED) {
            throw new CallStateException(3, "cannot hangup call: SRVCC");
        }
        super.hangup(call);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.CallStateException */
    public void hangup(ImsPhoneCall call, int reason) throws CallStateException {
        log("hangup call with reason: " + reason);
        if (this.mSrvccState == Call.SrvccState.STARTED || this.mSrvccState == Call.SrvccState.COMPLETED) {
            throw new CallStateException(3, "cannot hangup call: SRVCC");
        }
        super.hangup(call, reason);
    }

    protected synchronized void addConnection(ImsPhoneConnection conn) {
        super.addConnection(conn);
    }

    protected void processCallStateChange(ImsCall imsCall, Call.State state, int cause, boolean ignoreState) {
        super.processCallStateChange(imsCall, state, cause, ignoreState);
        ImsPhoneConnection conn = findConnection(imsCall);
        logDebugMessagesWithDumpFormat("CC", conn, "");
        if (!this.mIsDataEnabled && state == Call.State.ACTIVE) {
            log("ImsCall updated to video call but data off, retry onDataEnabledChanged");
            sendEmptyMessage(105);
        }
    }

    public int getDisconnectCauseFromReasonInfo(ImsReasonInfo reasonInfo, Call.State callState) {
        int code = maybeRemapReasonCode(reasonInfo);
        switch (code) {
            case 1600:
                return 81;
            case 9000:
                return MtkDisconnectCause.CAUSE_MOVED_PERMANENTLY;
            case 9001:
                return MtkDisconnectCause.CAUSE_BAD_REQUEST;
            case 9002:
                return MtkDisconnectCause.CAUSE_UNAUTHORIZED;
            case 9003:
                return MtkDisconnectCause.CAUSE_PAYMENT_REQUIRED;
            case 9004:
                return MtkDisconnectCause.CAUSE_FORBIDDEN;
            case 9005:
                return MtkDisconnectCause.CAUSE_NOT_FOUND;
            case 9006:
                return MtkDisconnectCause.CAUSE_METHOD_NOT_ALLOWED;
            case 9007:
                return MtkDisconnectCause.CAUSE_NOT_ACCEPTABLE;
            case 9008:
                return MtkDisconnectCause.CAUSE_PROXY_AUTHENTICATION_REQUIRED;
            case 9009:
                return MtkDisconnectCause.CAUSE_REQUEST_TIMEOUT;
            case 9010:
                return MtkDisconnectCause.CAUSE_CONFLICT;
            case 9011:
                return MtkDisconnectCause.CAUSE_GONE;
            case 9012:
                return MtkDisconnectCause.CAUSE_LENGTH_REQUIRED;
            case 9013:
                return MtkDisconnectCause.CAUSE_REQUEST_ENTRY_TOO_LONG;
            case 9014:
                return MtkDisconnectCause.CAUSE_REQUEST_URI_TOO_LONG;
            case 9015:
                return MtkDisconnectCause.CAUSE_UNSUPPORTED_MEDIA_TYPE;
            case 9016:
                return MtkDisconnectCause.CAUSE_UNSUPPORTED_URI_SCHEME;
            case 9017:
                return MtkDisconnectCause.CAUSE_BAD_EXTENSION;
            case 9018:
                return MtkDisconnectCause.CAUSE_EXTENSION_REQUIRED;
            case 9019:
                return MtkDisconnectCause.CAUSE_INTERVAL_TOO_BRIEF;
            case 9020:
                return MtkDisconnectCause.CAUSE_TEMPORARILY_UNAVAILABLE;
            case 9021:
                return MtkDisconnectCause.CAUSE_CALL_TRANSACTION_NOT_EXIST;
            case 9022:
                return MtkDisconnectCause.CAUSE_LOOP_DETECTED;
            case 9023:
                return MtkDisconnectCause.CAUSE_TOO_MANY_HOPS;
            case 9024:
                return MtkDisconnectCause.CAUSE_ADDRESS_INCOMPLETE;
            case 9025:
                return MtkDisconnectCause.CAUSE_AMBIGUOUS;
            case 9026:
                return MtkDisconnectCause.CAUSE_BUSY_HERE;
            case 9027:
                return MtkDisconnectCause.CAUSE_REQUEST_TERMINATED;
            case 9028:
                return MtkDisconnectCause.CAUSE_NOT_ACCEPTABLE_HERE;
            case 9029:
                return MtkDisconnectCause.CAUSE_SERVER_INTERNAL_ERROR;
            case 9030:
                return MtkDisconnectCause.CAUSE_NOT_IMPLEMENTED;
            case 9031:
                return MtkDisconnectCause.CAUSE_BAD_GATEWAY;
            case 9032:
                return MtkDisconnectCause.CAUSE_SERVICE_UNAVAILABLE;
            case 9033:
                return MtkDisconnectCause.CAUSE_GATEWAY_TIMEOUT;
            case 9034:
                return MtkDisconnectCause.CAUSE_VERSION_NOT_SUPPORTED;
            case 9035:
                return MtkDisconnectCause.CAUSE_MESSAGE_TOO_LONG;
            case 9036:
                return MtkDisconnectCause.CAUSE_BUSY_EVERYWHERE;
            case 9037:
                return MtkDisconnectCause.CAUSE_DECLINE;
            case 9038:
                return MtkDisconnectCause.CAUSE_DOES_NOT_EXIST_ANYWHERE;
            case 9039:
                return MtkDisconnectCause.CAUSE_SESSION_NOT_ACCEPTABLE;
            case 9040:
                return 1009;
            case 9041:
                return 1008;
            case 9042:
                return 1010;
            case 9043:
                return 1011;
            case 61441:
                return MtkDisconnectCause.IMS_EMERGENCY_REREG;
            case 61442:
                return 1005;
            case 61443:
                return 1006;
            case 61451:
                return MtkDisconnectCause.WFC_WIFI_SIGNAL_LOST;
            case 61452:
                return MtkDisconnectCause.WFC_ISP_PROBLEM;
            case 61453:
                return MtkDisconnectCause.WFC_HANDOVER_WIFI_FAIL;
            case 61454:
                return MtkDisconnectCause.WFC_HANDOVER_LTE_FAIL;
            default:
                return super.getDisconnectCauseFromReasonInfo(reasonInfo, callState);
        }
    }

    public void notifySrvccState(int state) {
        if (state == 1) {
            sendRttSrvccOrCsfbEvent(this.mForegroundCall);
            sendRttSrvccOrCsfbEvent(this.mBackgroundCall);
            sendRttSrvccOrCsfbEvent(this.mRingingCall);
        }
        super.notifySrvccState(state);
        if (this.mSrvccState == Call.SrvccState.COMPLETED) {
            this.mSrvccState = Call.SrvccState.NONE;
            if (this.mHoldSwitchingState != ImsPhoneCallTracker.HoldSwapState.INACTIVE) {
                this.mHoldSwitchingState = ImsPhoneCallTracker.HoldSwapState.INACTIVE;
                return;
            }
            return;
        }
        if (this.mSrvccState == Call.SrvccState.FAILED) {
            this.mSrvccState = Call.SrvccState.NONE;
        }
    }

    protected void releasePendingMOIfRequired() {
        if (this.mPendingMO == null) {
            return;
        }
        this.mPendingMO.setDisconnectCause(36);
        sendEmptyMessageDelayed(18, 500L);
    }

    public void handleMessage(Message msg) {
        log("handleMessage what=" + msg.what);
        switch (msg.what) {
            case 101:
                onDataRoamingOn();
                break;
            case 102:
                onDataRoamingOff();
                break;
            case 103:
                onRoamingSettingsChanged();
                break;
            case 104:
                try {
                    this.mPhone.unholdHeldCall();
                } catch (CallStateException e) {
                    loge("handleMessage EVENT_RESUME_BACKROUND_CALL exception=" + e);
                    return;
                }
                break;
            case 105:
                onDataEnabledChanged(this.mIsDataEnabled, this.mLastDataEnabledReason);
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }

    public class IncomingCallEventRecevier extends BroadcastReceiver implements MtkIncomingCallChecker.OnCheckCompleteListener {
        public IncomingCallEventRecevier() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.android.ims.IMS_INCOMING_CALL_INDICATION")) {
                MtkImsPhoneCallTracker.this.log("onReceive() indication call intent");
                if (MtkImsPhoneCallTracker.this.mImsManager == null) {
                    MtkImsPhoneCallTracker.this.log("onReceive() no ims manager");
                    return;
                }
                int phoneId = intent.getIntExtra("android:phoneId", -1);
                int subId = MtkImsPhoneCallTracker.this.mPhone.getSubId();
                String number = intent.getStringExtra("android:imsDialString");
                MtkImsPhoneCallTracker.this.log("onReceive() : subId = " + subId + ", number =" + MtkImsPhoneCallTracker.sensitiveEncode(number) + ", phoneId = " + phoneId);
                if (phoneId != MtkImsPhoneCallTracker.this.mPhone.getPhoneId()) {
                    return;
                }
                MtkImsPhoneCallTracker.this.mIncomingCallCheker = new MtkIncomingCallChecker("ims_call_pre_check", intent);
                boolean bCheckStart = MtkImsPhoneCallTracker.this.mIncomingCallCheker.startIncomingCallNumberCheck(MtkImsPhoneCallTracker.this.mPhone.getContext(), subId, number, this);
                MtkImsPhoneCallTracker mtkImsPhoneCallTracker = MtkImsPhoneCallTracker.this;
                if (bCheckStart) {
                    mtkImsPhoneCallTracker.log("onReceive() startIncomingCallNumberCheck true. start check ");
                    return;
                }
                mtkImsPhoneCallTracker.log("onReceive() startIncomingCallNumberCheck false, and flow continues");
                MtkImsPhoneCallTracker.this.log("setCallIndication : intent = " + intent + ", isAllow = true, cause = 1");
                try {
                    if (MtkImsPhoneCallTracker.this.mImsManager instanceof MtkImsManager) {
                        MtkImsPhoneCallTracker.this.mImsManager.setCallIndication(phoneId, intent, true, 1);
                    }
                } catch (ImsException e) {
                    MtkImsPhoneCallTracker.this.loge("setCallIndication ImsException " + e);
                }
            }
        }

        @Override // com.mediatek.internal.telephony.MtkIncomingCallChecker.OnCheckCompleteListener
        public void onCheckComplete(boolean result, Object obj) {
            int rejectCause = 1;
            boolean isAllow = true;
            Intent intent = (Intent) obj;
            int phoneId = -1;
            if (intent != null) {
                phoneId = intent.getIntExtra("android:phoneId", -1);
            }
            if (result) {
                rejectCause = 16;
                isAllow = false;
            }
            MtkImsPhoneCallTracker.this.log("onCheckComplete(): intent = " + intent + ", isAllow = " + isAllow + ", cause = " + rejectCause);
            try {
                if (MtkImsPhoneCallTracker.this.mImsManager instanceof MtkImsManager) {
                    MtkImsPhoneCallTracker.this.mImsManager.setCallIndication(phoneId, intent, isAllow, rejectCause);
                }
            } catch (ImsException e) {
                MtkImsPhoneCallTracker.this.loge("onCheckComplete() ImsException " + e);
            }
        }
    }

    private void registerIndicationReceiver() {
        log("registerIndicationReceiver");
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction("com.android.ims.IMS_INCOMING_CALL_INDICATION");
        this.mPhone.getContext().registerReceiver(this.mIndicationReceiver, intentfilter, 2);
    }

    private void unregisterIndicationReceiver() {
        log("unregisterIndicationReceiver");
        this.mPhone.getContext().unregisterReceiver(this.mIndicationReceiver);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.CallStateException */
    void hangupAll() throws CallStateException {
        log("hangupAll");
        if (this.mImsManager == null || !(this.mImsManager instanceof MtkImsManager)) {
            throw new CallStateException("No MtkImsManager Instance");
        }
        try {
            this.mImsManager.hangupAllCall(this.mPhone.getPhoneId());
            if (!this.mRingingCall.isIdle()) {
                setCallTerminationFlag(this.mRingingCall);
                this.mRingingCall.onHangupLocal();
            }
            if (!this.mForegroundCall.isIdle()) {
                setCallTerminationFlag(this.mForegroundCall);
                this.mForegroundCall.onHangupLocal();
            }
            if (!this.mBackgroundCall.isIdle()) {
                setCallTerminationFlag(this.mBackgroundCall);
                this.mBackgroundCall.onHangupLocal();
            }
        } catch (ImsException e) {
            throw new CallStateException(e.getMessage());
        }
    }

    private void setCallTerminationFlag(ImsPhoneCall imsPhoneCall) {
        log("setCallTerminationFlag");
        MtkImsCall imsCall = imsPhoneCall.getImsCall();
        if (imsCall == null) {
            log("setCallTerminationFlag " + imsPhoneCall + " no ims call");
        } else if (imsCall instanceof MtkImsCall) {
            imsCall.setTerminationRequestFlag(true);
        }
    }

    protected void logDebugMessagesWithOpFormat(String category, String action, ImsPhoneConnection conn, String msg) {
        if (category == null || action == null || conn == null) {
            return;
        }
        MtkImsPhoneConnection connExt = (MtkImsPhoneConnection) conn;
        FormattedLog formattedLog = new FormattedLog.Builder().setCategory(category).setServiceName("ImsPhone").setOpType(FormattedLog.OpType.OPERATION).setActionName(action).setCallNumber(sensitiveEncode(getCallNumber(conn))).setCallId(getConnectionCallId(connExt)).setExtraMessage(msg).buildDebugMsg();
        if (formattedLog != null) {
            if (!SENLOG || TELDBG) {
                log(formattedLog.toString());
            }
        }
    }

    protected void logDebugMessagesWithDumpFormat(String category, ImsPhoneConnection conn, String msg) {
        if (category == null || conn == null || !(conn instanceof MtkImsPhoneConnection)) {
            return;
        }
        MtkImsPhoneConnection connExt = (MtkImsPhoneConnection) conn;
        FormattedLog formattedLog = new FormattedLog.Builder().setCategory("CC").setServiceName("ImsPhone").setOpType(FormattedLog.OpType.DUMP).setCallNumber(sensitiveEncode(getCallNumber(conn))).setCallId(getConnectionCallId(connExt)).setExtraMessage(msg).setStatusInfo("state", conn.getState().toString()).setStatusInfo("isConfCall", conn.isMultiparty() ? "Yes" : "No").setStatusInfo("isConfChildCall", "No").setStatusInfo("parent", connExt.getParentCallName()).buildDumpInfo();
        if (formattedLog != null) {
            if (!SENLOG || TELDBG) {
                log(formattedLog.toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getConnectionCallId(MtkImsPhoneConnection conn) {
        if (conn == null) {
            return "";
        }
        int callId = conn.getCallId();
        if (callId == -1 && (callId = conn.getCallIdBeforeDisconnected()) == -1) {
            return "";
        }
        return String.valueOf(callId);
    }

    private String getCallNumber(ImsPhoneConnection conn) {
        if (conn == null) {
            return null;
        }
        if (conn.isMultiparty()) {
            return "conferenceCall";
        }
        return conn.getAddress();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.ims.ImsException */
    public ImsUtInterface getUtInterface() throws ImsException {
        if (this.mImsManager == null) {
            throw getImsManagerIsNullException();
        }
        ImsUtInterface ut = this.mImsManager.getSupplementaryServiceConfiguration();
        return ut;
    }

    protected ImsPhoneConnection makeImsPhoneConnectionForMO(String dialString, boolean isEmergencyNumber, boolean isWpsCall, ImsPhone.ImsDialArgs dialArgs) {
        return new MtkImsPhoneConnection(this.mPhone, dialString, this, this.mForegroundCall, isEmergencyNumber, isWpsCall, dialArgs);
    }

    protected ImsPhoneConnection makeImsPhoneConnectionForMT(ImsCall imsCall, boolean isUnknown) {
        return new MtkImsPhoneConnection((Phone) this.mPhone, imsCall, (ImsPhoneCallTracker) this, isUnknown ? this.mForegroundCall : this.mRingingCall, isUnknown);
    }

    protected ImsPhoneConnection makeImsPhoneConnectionForConference(String[] participantsToDial) {
        return new MtkImsPhoneConnection((Phone) this.mPhone, participantsToDial, (ImsPhoneCallTracker) this, this.mForegroundCall, false);
    }

    protected ImsCall takeCall(IImsCallSession c, Bundle extras) throws ImsException {
        return this.mImsManager.takeCall(c, extras, this.mMtkImsCallListener);
    }

    private boolean isEmergencyNumber(String dialString) {
        return MtkLocalPhoneNumberUtils.getIsEmergencyNumber();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.CallStateException */
    protected void checkforCsfb() throws CallStateException {
        if (this.mHandoverCall.mConnections.size() > 0) {
            log("SRVCC: there are connections during handover, trigger CSFB!");
            throw new CallStateException("cs_fallback");
        }
        if (this.mPhone != null && this.mPhone.getDefaultPhone() != null) {
            Phone defaultPhone = this.mPhone.getDefaultPhone();
            if (defaultPhone.getState() != PhoneConstants.State.IDLE && getState() == PhoneConstants.State.IDLE) {
                log("There are CS connections, trigger CSFB!");
                throw new CallStateException("cs_fallback");
            }
        }
    }

    protected boolean canDailOnCallTerminated() {
        return (this.mPendingMO == null || hasMessages(18)) ? false : true;
    }

    protected void setRedialAsEcc(int cause) {
        if (cause == 380) {
            this.mDialAsECC = true;
        }
    }

    protected int updateDisconnectCause(int cause, ImsPhoneConnection conn) {
        if (cause == 36 && conn != null && conn.getImsCall().isMerged()) {
            return 45;
        }
        return cause;
    }

    protected void setMultiPartyState(Connection c) {
        if (c instanceof MtkImsPhoneConnection) {
            ((MtkImsPhoneConnection) c).mWasMultiparty = c.isMultiparty();
            ((MtkImsPhoneConnection) c).mWasPreMultipartyHost = c.isConferenceHost();
            log("SRVCC: Connection isMultiparty is " + ((MtkImsPhoneConnection) c).mWasMultiparty + "and isConfHost is " + ((MtkImsPhoneConnection) c).mWasPreMultipartyHost + " before handover");
        }
    }

    protected void resetRingBackTone(ImsPhoneCall call) {
        if (call instanceof MtkImsPhoneCall) {
            ((MtkImsPhoneCall) call).resetRingbackTone();
        }
    }

    protected AsyncResult getCallStateChangeAsyncResult() {
        return new AsyncResult((Object) null, this.mSrvccState, (Throwable) null);
    }

    public void startListeningForCalls(int subId) throws ImsException {
        super.startListeningForCalls(subId);
        try {
            MtkImsManager imsMgr = ImsManager.getInstance(this.mPhone.getContext(), this.mPhone.getPhoneId());
            imsMgr.removeImsConnectionStateListener(this.mImsStateListener);
            imsMgr.addImsConnectionStateListener(this.mImsStateListener);
            log("startListeningForCalls() : register ims succeed, " + this.mImsStateListener);
        } catch (ImsException e) {
            log("startListeningForCalls() : register ims fail!");
        }
    }

    protected void modifyVideoCall(ImsCall imsCall, int newVideoState) {
        ImsPhoneConnection conn = findConnection(imsCall);
        int newVideoState2 = newVideoState | 32768;
        if (conn != null) {
            int oldVideoState = conn.getVideoState();
            if (conn.getVideoProvider() != null) {
                conn.getVideoProvider().onSendSessionModifyRequest(new VideoProfile(oldVideoState), new VideoProfile(newVideoState2));
            }
        }
    }

    protected void switchWfcModeIfRequired(ImsManager imsManager, boolean isWfcEnabled, boolean isEmergencyNumber) {
        if (imsManager == null || !isWfcEnabled || !isEmergencyNumber || !this.mCarrierSwitchWfcModeRequired) {
            log("Do not switch WFC mode, isWfcEnabled:" + isWfcEnabled + ", isEmergencyNumber:" + isEmergencyNumber + ", mCarrierSwitchWfcModeRequired:" + this.mCarrierSwitchWfcModeRequired);
        } else if (imsManager.getWfcMode() == 0) {
            imsManager.setWfcMode(1);
        }
    }

    public static String sensitiveEncode(String input) {
        if (!SENLOG || TELDBG) {
            return Rlog.pii(LOG_TAG, input);
        }
        return "[hidden]";
    }

    protected boolean isCarrierPauseAllowed(ImsCall imsCall) {
        if (imsCall != null && imsCall.getState() == 3) {
            return false;
        }
        return true;
    }

    protected boolean ignoreCarrierPauseSupport() {
        return true;
    }

    public void registerForCallsDisconnectedDuringSrvcc(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mCallsDisconnectedDuringSrvccRegistrants.add(r);
    }

    public void unregisterForCallsDisconnectedDuringSrvcc(Handler h) {
        this.mCallsDisconnectedDuringSrvccRegistrants.remove(h);
    }

    public void registerSettingsObserver() {
        this.mSettingsObserver.unobserve();
        String simSuffix = "";
        if (TelephonyManager.getDefault().getSimCount() > 1) {
            simSuffix = Integer.toString(this.mPhone.getDefaultPhone().getSubId());
        }
        this.mSettingsObserver.observe(Settings.Global.getUriFor("data_roaming" + simSuffix), 103);
    }

    public void initRoamingAndRoamingSetting(int subId) {
        this.mIsDataRoaming = this.mPhone.getDefaultPhone().getServiceState().getDataRoaming();
        String dataRoamingSetting = TelephonyManager.getDefault().getSimCount() > 1 ? "data_roaming" + subId : "data_roaming";
        log("dataRoamingSetting uri: " + dataRoamingSetting);
        int dataRoamingOn = Settings.Global.getInt(this.mPhone.getContext().getContentResolver(), dataRoamingSetting, 0);
        this.mIsDataRoamingSettingEnabled = dataRoamingOn == 1;
        MtkImsManager imsMgr = ImsManager.getInstance(this.mPhone.getContext(), this.mPhone.getPhoneId());
        imsMgr.setDataRoamingSettingsEnabled(this.mIsDataRoamingSettingEnabled);
        log("initRoamingAndRoamingSetting, subId = " + subId + ", mIsDataRoaming = " + this.mIsDataRoaming + ", mIsDataRoamingSettingEnabled = " + this.mIsDataRoamingSettingEnabled);
    }

    protected void onDataRoamingOn() {
        log("onDataRoamingOn");
        if (this.mIsDataRoaming) {
            log("onDataRoamingOn: device already in roaming. ignored the update.");
            return;
        }
        this.mIsDataRoaming = this.mPhone.getDefaultPhone().getServiceState().getDataRoaming();
        if (this.mIsDataRoamingSettingEnabled) {
            log("onDataRoamingOn: setup data on roaming");
            onDataRoamingEnabledChanged(true);
        } else {
            log("onDataRoamingOn: Tear down data connection on roaming.");
            onDataRoamingEnabledChanged(false);
        }
    }

    protected void onDataRoamingOff() {
        log("onDataRoamingOff");
        if (!this.mIsDataRoaming) {
            log("onDataRoamingOff: device already not roaming. ignored the update.");
            return;
        }
        this.mIsDataRoaming = this.mPhone.getDefaultPhone().getServiceState().getDataRoaming();
        if (!this.mIsDataRoamingSettingEnabled) {
            onDataRoamingEnabledChanged(true);
        }
    }

    protected void onRoamingSettingsChanged() {
        log("onRoamingSettingsChanged");
        this.mIsDataRoamingSettingEnabled = this.mPhone.getDefaultPhone().getDataRoamingEnabled();
        MtkImsManager imsMgr = ImsManager.getInstance(this.mPhone.getContext(), this.mPhone.getPhoneId());
        imsMgr.setDataRoamingSettingsEnabled(this.mIsDataRoamingSettingEnabled);
        log("onRoamingSettingsChanged: mIsDataRoaming = " + this.mIsDataRoaming + ", mIsDataRoamingSettingEnabled = " + this.mIsDataRoamingSettingEnabled);
        if (!this.mIsDataRoaming) {
            log("onRoamingSettingsChanged: device is not roaming. ignored the request.");
        } else if (this.mIsDataRoamingSettingEnabled) {
            log("onRoamingSettingsChanged: setup data on roaming");
            onDataRoamingEnabledChanged(true);
        } else {
            log("onRoamingSettingsChanged: Tear down data connection on roaming.");
            onDataRoamingEnabledChanged(false);
        }
    }

    private void onDataRoamingEnabledChanged(boolean enabled) {
        log("onDataRoamingEnabledChanged: enabled=" + enabled);
        if (!this.mIsViLteDataMetered) {
            log("onDataRoamingEnabledChanged: Ignore data " + (enabled ? "enabled" : "disabled") + " - carrier policy indicates that data is not metered for ViLTE calls.");
            return;
        }
        if (this.mIgnoreDataRoaming) {
            log("onDataRoaming: Ignore data " + (enabled ? "enabled" : "disabled") + " - carrier policy indicates that ignore data roaming");
            return;
        }
        if (enabled && !this.mIsDataEnabled) {
            log("onDataRoamingEnabledChanged: Ignore on when data off");
            return;
        }
        for (ImsPhoneConnection conn : this.mConnections) {
            ImsCall imsCall = conn.getImsCall();
            boolean isLocalVideoCapable = enabled || (imsCall != null && imsCall.isWifiCall());
            conn.setLocalVideoCapable(isLocalVideoCapable);
        }
        maybeNotifyDataDisabled(enabled, 1406);
        handleDataEnabledChange(enabled, 1406);
        if (!this.mShouldUpdateImsConfigOnDisconnect && 0 != -1 && this.mImsManager != null) {
            this.mImsManager.updateImsServiceConfig();
        }
    }

    protected boolean isRoamingOnAndRoamingSettingOff() {
        return (!this.mIsDataRoaming || this.mIsDataRoamingSettingEnabled || this.mIgnoreDataRoaming) ? false : true;
    }

    protected void onDataEnabledChanged(boolean enabled, int reason) {
        this.mLastDataEnabledReason = reason;
        super.onDataEnabledChanged(enabled, reason);
    }

    protected void updateCarrierConfiguration(int subId, PersistableBundle carrierConfig) {
        super.updateCarrierConfiguration(subId, carrierConfig);
        if (carrierConfig == null) {
            loge("updateCarrierConfiguration: Empty carrier config.");
            return;
        }
        this.mIgnoreDataRoaming = carrierConfig.getBoolean("mtk_ignore_data_roaming_for_video_calls");
        this.mCarrierSwitchWfcModeRequired = carrierConfig.getBoolean("mtk_carrier_switch_wfc_mode_required_bool");
        if (isTestSim(this.mPhone.getPhoneId())) {
            this.mIsViLteDataMetered = isVTDataMeteredByOpid(SystemProperties.get(DataSubConstants.PROPERTY_OPERATOR_OPTR, DataSubConstants.OPERATOR_OM));
            this.mIgnoreDataRoaming = !this.mIsViLteDataMetered;
            log("updateCarrierConfiguration: For test sim, mIsViLteDataMetered = " + this.mIsViLteDataMetered);
        }
        this.mIgnoreClirWhenEcc = carrierConfig.getBoolean("mtk_ignore_clir_when_ecc");
    }

    protected boolean isDataAvailableForViLTE() {
        return !this.mIsViLteDataMetered || (this.mIsDataEnabled && !isRoamingOnAndRoamingSettingOff());
    }

    private void sendRttSrvccOrCsfbEvent(ImsPhoneCall call) {
        log("sendRttSrvccOrCsfbEvent: " + call);
        if (call == null) {
            loge("sendRttSrvccOrCsfbEvent no call");
            return;
        }
        if (!isRttCall(call.getImsCall())) {
            log("sendRttSrvccOrCsfbEvent: not for RTT call");
            return;
        }
        if (call.hasConnections()) {
            ImsCall activeCall = call.getFirstConnection().getImsCall();
            ImsPhoneConnection conn = call.getFirstConnection();
            if (activeCall != null && conn != null) {
                if (call.getState() == Call.State.DIALING) {
                    conn.onConnectionEvent("mediatek.telecom.event.EVENT_CSFB", (Bundle) null);
                } else {
                    conn.onConnectionEvent("mediatek.telecom.event.EVENT_SRVCC", (Bundle) null);
                }
            }
        }
    }

    protected int getHangupReasionInfo(int disconnectCause, boolean rejectCall) {
        if (disconnectCause == 1009) {
            return 9040;
        }
        if (disconnectCause == 1010) {
            return 9042;
        }
        if (disconnectCause == 1008) {
            return 9041;
        }
        if (disconnectCause == 1011) {
            return 9043;
        }
        return 504;
    }

    protected boolean ignoreConference(ImsCall fgImsCall, ImsCall bgImsCall) {
        if (fgImsCall == null || fgImsCall.getState() == 8) {
            log("conference: skip; foreground call state terminated");
            return true;
        }
        if (bgImsCall == null || bgImsCall.getState() == 8) {
            log("conference: skip; background call state terminated");
            return true;
        }
        return false;
    }

    protected boolean isVTDataMeteredByOpid(String optr) {
        if ("OP08".equals(optr)) {
            return true;
        }
        return false;
    }

    protected void disconnectPendingMo() {
        if (this.mPendingMO != null) {
            log("disconnectPendingMo");
            this.mPendingMO.update((ImsCall) null, Call.State.DISCONNECTED);
            this.mPendingMO.onDisconnect();
            removeConnection(this.mPendingMO);
            this.mPendingMO = null;
            removeMessages(20);
        }
    }

    private boolean isTestSim(int phoneId) {
        boolean isTestSim = false;
        switch (phoneId) {
            case 0:
                isTestSim = RadioCapabilitySwitchUtil.IMSI_READY.equals(SystemProperties.get("vendor.gsm.sim.ril.testsim", "0"));
                break;
            case 1:
                isTestSim = RadioCapabilitySwitchUtil.IMSI_READY.equals(SystemProperties.get("vendor.gsm.sim.ril.testsim.2", "0"));
                break;
            case 2:
                isTestSim = RadioCapabilitySwitchUtil.IMSI_READY.equals(SystemProperties.get("vendor.gsm.sim.ril.testsim.3", "0"));
                break;
            case 3:
                isTestSim = RadioCapabilitySwitchUtil.IMSI_READY.equals(SystemProperties.get("vendor.gsm.sim.ril.testsim.4", "0"));
                break;
        }
        log("phoneId: " + phoneId + ", isTestSim: " + isTestSim);
        return isTestSim;
    }

    protected boolean ignoreClirWhenEcc() {
        return this.mIgnoreClirWhenEcc;
    }

    protected CommandException getImsUssdCommandException(ImsReasonInfo reasonInfo) {
        if (reasonInfo.getCode() == 241) {
            return new CommandException(CommandException.Error.FDN_CHECK_FAILURE);
        }
        return new CommandException(CommandException.Error.GENERIC_FAILURE);
    }

    public boolean ignoreSwitchCallToBackground() {
        return this.mHoldSwitchingState == ImsPhoneCallTracker.HoldSwapState.PENDING_RESUME_FOREGROUND_AFTER_HOLD;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void doUnregisterWhenDispose() {
        this.mPhone.getContext().unregisterReceiver(this.mImsBaseReceiver);
        this.mPhone.getContext().unregisterReceiver(this.mMultiSimConfigChangedReceiver);
        unregisterIndicationReceiver();
        this.mPhone.setServiceState(1);
        this.mPhone.setImsRegistered(false);
        resetImsCapabilities();
        this.mPhone.onFeatureCapabilityChanged();
        this.mPhone.getDefaultPhone().getServiceStateTracker().unregisterForDataRoamingOn(this);
        this.mPhone.getDefaultPhone().getServiceStateTracker().unregisterForDataRoamingOff(this);
        this.mSubscriptionManager.removeOnSubscriptionsChangedListener(this.mOnSubscriptionsChangedListener);
        this.mSettingsObserver.unobserve();
        try {
            MtkImsManager imsMgr = ImsManager.getInstance(this.mPhone.getContext(), this.mPhone.getPhoneId());
            imsMgr.removeImsConnectionStateListener(this.mImsStateListener);
        } catch (ImsException e) {
            loge("dispose() : removeRegistrationListener failed: " + e);
        }
    }

    protected boolean isEmergencyCall(ImsPhoneConnection conn) {
        if (conn.isAdhocConference()) {
            return false;
        }
        boolean isEmergencyCall = isEmergencyNumber(conn.getAddress());
        return isEmergencyCall;
    }

    protected int shouldRedialAsECCCall(int serviceType) {
        if (this.mDialAsECC) {
            log("Dial as ECC");
            this.mDialAsECC = false;
            return 2;
        }
        return serviceType;
    }

    protected void removeAllConnectionsByCall(ImsPhoneCall call) {
        for (Connection conn : call.mConnections) {
            log("removeAllConnectionsByCall =" + conn);
            removeConnection((ImsPhoneConnection) conn);
        }
    }

    protected void notifyPhoneStateInSomeCase() {
        if (this.mPhone != null && this.mPhone.mDefaultPhone != null && this.mPhone.mDefaultPhone.getState() == PhoneConstants.State.IDLE) {
            log("SRVCC: notify ImsPhone state as idle.");
            this.mPhone.notifyPhoneStateChanged();
            this.mCallsDisconnectedDuringSrvccRegistrants.notifyRegistrants(getCallStateChangeAsyncResult());
        }
    }

    protected void resetHoldToneWhenHandover(ImsPhoneCall call) {
        log("resetHoldToneWhenHandover mSrvccState:" + this.mSrvccState);
        if (this.mSrvccState == Call.SrvccState.COMPLETED && call.mConnections != null) {
            for (Connection conn : call.mConnections) {
                if (this.mOnHoldToneStarted && conn != null && this.mOnHoldToneId == System.identityHashCode(conn)) {
                    log("transferHandoverConnections reset the hold tone.");
                    this.mPhone.stopOnHoldTone(conn);
                    this.mOnHoldToneStarted = false;
                    this.mOnHoldToneId = -1;
                }
            }
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.CallStateException */
    protected void checkforPendingResume() throws CallStateException {
        if (this.mHoldSwitchingState == ImsPhoneCallTracker.HoldSwapState.PENDING_SINGLE_CALL_UNHOLD && this.mForegroundCall.getState() == Call.State.HOLDING) {
            log("cannot dial when there is pending resume");
            throw new CallStateException("cannot dial when there is pending resume");
        }
    }
}
