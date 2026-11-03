package com.android.internal.telephony.imsphone;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.ResultReceiver;
import android.os.UserHandle;
import android.preference.PreferenceManager;
import android.telephony.AccessNetworkConstants;
import android.telephony.CallQuality;
import android.telephony.CarrierConfigManager;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.NetworkScanRequest;
import android.telephony.ParsedPhoneNumber;
import android.telephony.PhoneNumberManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyManager;
import android.telephony.UssdResponse;
import android.telephony.ims.ImsCallForwardInfo;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsRegistrationAttributes;
import android.telephony.ims.ImsSsInfo;
import android.telephony.ims.MediaQualityStatus;
import android.telephony.ims.RegistrationManager;
import android.text.TextUtils;
import com.android.ims.ImsEcbmStateListener;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallFailCause;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CallManager$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CallTracker;
import com.android.internal.telephony.CarrierPrivilegesTracker;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.HyperTelephonyFactory;
import com.android.internal.telephony.IHyperSsManager;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.MiuiPhoneStub;
import com.android.internal.telephony.MiuiSubscriptionControllerStub;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.NetworkTypeController$ModemDisplayNetworkType$$ExternalSyntheticRecord0;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneInternalInterface;
import com.android.internal.telephony.PhoneNotifier;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.data.TelephonyMultiInternetStub;
import com.android.internal.telephony.domainselection.DomainSelectionResolver;
import com.android.internal.telephony.emergency.EmergencyNumberTracker;
import com.android.internal.telephony.emergency.EmergencyStateTracker;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.metrics.ImsStats;
import com.android.internal.telephony.metrics.VoiceCallSessionStats;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.util.NotificationChannelController;
import com.android.internal.telephony.util.QtiImsUtils;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class ImsPhone extends ImsPhoneBase {
    public static final int CANCEL_ECM_TIMER = 1;
    protected static final boolean DBG = true;
    protected static final long DEFAULT_ECM_EXIT_TIMER_VALUE = 300000;
    protected static final int EVENT_GET_CALL_BARRING_DONE = 79;
    protected static final int EVENT_GET_CALL_WAITING_DONE = 81;
    protected static final int EVENT_GET_CLIR_DONE = 83;
    public static final int EVENT_SERVICE_STATE_CHANGED = 85;
    protected static final int EVENT_SET_CALL_BARRING_DONE = 78;
    protected static final int EVENT_SET_CALL_WAITING_DONE = 80;
    protected static final int EVENT_SET_CLIR_DONE = 82;
    protected static final int EVENT_VOICE_CALL_ENDED = 86;
    public static final int RESTART_ECM_TIMER = 0;
    private String LOG_TAG;
    public ImsPhoneCallTracker mCT;
    private Uri[] mCurrentSubscriberUris;
    public Phone mDefaultPhone;
    protected Registrant mEcmExitRespRegistrant;
    protected Runnable mExitEcmRunnable;
    ImsExternalCallTracker mExternalCallTracker;
    private int mImsDeregistrationTech;
    private ImsEcbmStateListener mImsEcbmStateListener;
    private final ImsManagerFactory mImsManagerFactory;
    private ImsRegistrationCallbackHelper mImsMmTelEmergencyRegistrationHelper;
    private ImsRegistrationCallbackHelper mImsMmTelRegistrationHelper;
    ImsNrSaModeHandler mImsNrSaModeHandler;
    private SharedPreferences mImsPhoneSharedPreferences;
    private int mImsRegistrationCapabilities;
    private int mImsRegistrationState;
    private int mImsRegistrationSuggestedAction;
    private int mImsRegistrationTech;
    private final RegistrantList mImsRegistrationUpdateRegistrants;
    private ImsStats mImsStats;
    private boolean mIsInImsEcm;
    private boolean mIsOutgoingImsVoiceAllowed;
    protected String mLastDialString;
    private boolean mLastKnownIsUsingNtnState;
    protected boolean mLastKnownRoamingState;
    private ImsRegistrationCallbackHelper.ImsRegistrationUpdate mMmTelEmergencyRegistrationUpdate;
    private ImsRegistrationCallbackHelper.ImsRegistrationUpdate mMmTelRegistrationUpdate;
    private boolean mNotifiedRegisteredState;
    protected ArrayList<ImsPhoneMmiCode> mPendingMMIs;
    private PhoneNumberManager mPhoneNumberManager;
    private final LocalLog mRegLocalLog;
    protected BroadcastReceiver mResultReceiver;
    protected ServiceState mSS;
    private final RegistrantList mSilentRedialRegistrants;
    private RegistrantList mSsnRegistrants;
    private int mTransportType;
    protected PowerManager.WakeLock mWakeLock;

    public interface ImsManagerFactory {
        ImsManager create(Context context, int i);
    }

    protected boolean IsWfcModeHomeForDomRoaming(ServiceState serviceState, ImsManager imsManager) {
        return false;
    }

    protected int getActionFromCFAction(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return 1;
        }
        if (i != 3) {
            return i != 4 ? -1 : 4;
        }
        return 3;
    }

    protected int getCFReasonFromCondition(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return 1;
        }
        if (i == 2) {
            return 2;
        }
        if (i == 4) {
            return 4;
        }
        if (i != 5) {
            return i != 6 ? 3 : 6;
        }
        return 5;
    }

    protected int getConditionFromCFReason(int i) {
        switch (i) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            default:
                return -1;
        }
    }

    protected boolean isCfEnable(int i) {
        return i == 1 || i == 3;
    }

    protected boolean isValidCommandInterfaceCFAction(int i) {
        return i == 0 || i == 1 || i == 3 || i == 4;
    }

    protected boolean isValidCommandInterfaceCFReason(int i) {
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return true;
            default:
                return false;
        }
    }

    protected void updateLastKnownRoamingState(boolean z) {
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void activateCellBroadcastSms(int i, Message message) {
        super.activateCellBroadcastSms(i, message);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase
    public /* bridge */ /* synthetic */ boolean disableDataConnectivity() {
        return super.disableDataConnectivity();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void disableLocationUpdates() {
        super.disableLocationUpdates();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase
    public /* bridge */ /* synthetic */ boolean enableDataConnectivity() {
        return super.enableDataConnectivity();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void enableLocationUpdates() {
        super.enableLocationUpdates();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void getAvailableNetworks(Message message) {
        super.getAvailableNetworks(message);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void getCellBroadcastSmsConfig(Message message) {
        super.getCellBroadcastSmsConfig(message);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ int getDataActivityState() {
        return super.getDataActivityState();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ boolean getDataRoamingEnabled() {
        return super.getDataRoamingEnabled();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ String getDeviceId() {
        return super.getDeviceId();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ String getDeviceSvn() {
        return super.getDeviceSvn();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ String getGroupIdLevel1() {
        return super.getGroupIdLevel1();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ String getGroupIdLevel2() {
        return super.getGroupIdLevel2();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ IccCard getIccCard() {
        return super.getIccCard();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ IccFileHandler getIccFileHandler() {
        return super.getIccFileHandler();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ IccPhoneBookInterfaceManager getIccPhoneBookInterfaceManager() {
        return super.getIccPhoneBookInterfaceManager();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ boolean getIccRecordsLoaded() {
        return super.getIccRecordsLoaded();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ String getIccSerialNumber() {
        return super.getIccSerialNumber();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ String getImei() {
        return super.getImei();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ int getImeiType() {
        return super.getImeiType();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ String getLine1AlphaTag() {
        return super.getLine1AlphaTag();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ boolean getMessageWaitingIndicator() {
        return super.getMessageWaitingIndicator();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ int getPhoneType() {
        return super.getPhoneType();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ SignalStrength getSignalStrength() {
        return super.getSignalStrength();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ String getSubscriberId() {
        return super.getSubscriberId();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ int getTerminalBasedCallWaitingState(boolean z) {
        return super.getTerminalBasedCallWaitingState(z);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ String getVoiceMailAlphaTag() {
        return super.getVoiceMailAlphaTag();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ String getVoiceMailNumber() {
        return super.getVoiceMailNumber();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ boolean handlePinMmi(String str) {
        return super.handlePinMmi(str);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ boolean isDataAllowed() {
        return super.isDataAllowed();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ boolean isUserDataEnabled() {
        return super.isUserDataEnabled();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void migrateFrom(Phone phone) {
        super.migrateFrom(phone);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ boolean needsOtaServiceProvisioning() {
        return super.needsOtaServiceProvisioning();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase
    public /* bridge */ /* synthetic */ void notifyDisconnect(Connection connection) {
        super.notifyDisconnect(connection);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase
    public /* bridge */ /* synthetic */ void notifyImsReason(ImsReasonInfo imsReasonInfo) {
        super.notifyImsReason(imsReasonInfo);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase
    public /* bridge */ /* synthetic */ void notifyPhoneStateChanged() {
        super.notifyPhoneStateChanged();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase
    public /* bridge */ /* synthetic */ void notifyPreciseCallStateChanged() {
        super.notifyPreciseCallStateChanged();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase
    public /* bridge */ /* synthetic */ void notifyPreciseCallStateToNotifier() {
        super.notifyPreciseCallStateToNotifier();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase
    public /* bridge */ /* synthetic */ void notifySuppServiceFailed(PhoneInternalInterface.SuppService suppService) {
        super.notifySuppServiceFailed(suppService);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase
    public /* bridge */ /* synthetic */ void onCallQualityChanged(CallQuality callQuality, int i) {
        super.onCallQualityChanged(callQuality, i);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase
    public /* bridge */ /* synthetic */ void onMediaQualityStatusChanged(MediaQualityStatus mediaQualityStatus) {
        super.onMediaQualityStatusChanged(mediaQualityStatus);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase
    public /* bridge */ /* synthetic */ void onTtyModeReceived(int i) {
        super.onTtyModeReceived(i);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void registerForOnHoldTone(Handler handler, int i, Object obj) {
        super.registerForOnHoldTone(handler, i, obj);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void registerForRingbackTone(Handler handler, int i, Object obj) {
        super.registerForRingbackTone(handler, i, obj);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void registerForTtyModeReceived(Handler handler, int i, Object obj) {
        super.registerForTtyModeReceived(handler, i, obj);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void selectNetworkManually(OperatorInfo operatorInfo, boolean z, Message message) {
        super.selectNetworkManually(operatorInfo, z, message);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void setCellBroadcastSmsConfig(int[] iArr, Message message) {
        super.setCellBroadcastSmsConfig(iArr, message);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void setDataRoamingEnabled(boolean z) {
        super.setDataRoamingEnabled(z);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ boolean setLine1Number(String str, String str2, Message message) {
        return super.setLine1Number(str, str2, message);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void setNetworkSelectionModeAutomatic(Message message) {
        super.setNetworkSelectionModeAutomatic(message);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void setRadioPower(boolean z) {
        super.setRadioPower(z);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void setTerminalBasedCallWaitingSupported(boolean z) {
        super.setTerminalBasedCallWaitingSupported(z);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void setVoiceMailNumber(String str, String str2, Message message) {
        super.setVoiceMailNumber(str, str2, message);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void startNetworkScan(NetworkScanRequest networkScanRequest, Message message) {
        super.startNetworkScan(networkScanRequest, message);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase
    public /* bridge */ /* synthetic */ void startOnHoldTone(Connection connection) {
        super.startOnHoldTone(connection);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void startRingbackTone() {
        super.startRingbackTone();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void stopNetworkScan(Message message) {
        super.stopNetworkScan(message);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase
    public /* bridge */ /* synthetic */ void stopOnHoldTone(Connection connection) {
        super.stopOnHoldTone(connection);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void stopRingbackTone() {
        super.stopRingbackTone();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void unregisterForOnHoldTone(Handler handler) {
        super.unregisterForOnHoldTone(handler);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void unregisterForRingbackTone(Handler handler) {
        super.unregisterForRingbackTone(handler);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public /* bridge */ /* synthetic */ void unregisterForTtyModeReceived(Handler handler) {
        super.unregisterForTtyModeReceived(handler);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public /* bridge */ /* synthetic */ void updateServiceLocation() {
        super.updateServiceLocation();
    }

    public static class ImsDialArgs extends PhoneInternalInterface.DialArgs {
        public final boolean isWpsCall;
        public final int retryCallFailCause;
        public final int retryCallFailNetworkType;
        public final android.telecom.Connection.RttTextStream rttTextStream;

        public static class Builder extends PhoneInternalInterface.DialArgs.Builder<Builder> {
            private android.telecom.Connection.RttTextStream mRttTextStream;
            private int mRetryCallFailCause = 0;
            private int mRetryCallFailNetworkType = 0;
            private boolean mIsWpsCall = false;

            public static Builder from(PhoneInternalInterface.DialArgs dialArgs) {
                if (dialArgs instanceof ImsDialArgs) {
                    ImsDialArgs imsDialArgs = (ImsDialArgs) dialArgs;
                    return new Builder().setUusInfo(dialArgs.uusInfo).setIsEmergency(dialArgs.isEmergency).setEccCategory(dialArgs.eccCategory).setVideoState(dialArgs.videoState).setIntentExtras(dialArgs.intentExtras).setRttTextStream(imsDialArgs.rttTextStream).setClirMode(dialArgs.clirMode).setRetryCallFailCause(imsDialArgs.retryCallFailCause).setRetryCallFailNetworkType(imsDialArgs.retryCallFailNetworkType).setIsWpsCall(imsDialArgs.isWpsCall);
                }
                return new Builder().setUusInfo(dialArgs.uusInfo).setIsEmergency(dialArgs.isEmergency).setVideoState(dialArgs.videoState).setClirMode(dialArgs.clirMode).setIntentExtras(dialArgs.intentExtras);
            }

            public Builder setRttTextStream(android.telecom.Connection.RttTextStream rttTextStream) {
                this.mRttTextStream = rttTextStream;
                return this;
            }

            public Builder setRetryCallFailCause(int i) {
                this.mRetryCallFailCause = i;
                return this;
            }

            public Builder setRetryCallFailNetworkType(int i) {
                this.mRetryCallFailNetworkType = i;
                return this;
            }

            public Builder setIsWpsCall(boolean z) {
                this.mIsWpsCall = z;
                return this;
            }

            @Override // com.android.internal.telephony.PhoneInternalInterface.DialArgs.Builder
            public ImsDialArgs build() {
                return new ImsDialArgs(this);
            }
        }

        private ImsDialArgs(Builder builder) {
            super(builder);
            this.rttTextStream = builder.mRttTextStream;
            this.retryCallFailCause = builder.mRetryCallFailCause;
            this.retryCallFailNetworkType = builder.mRetryCallFailNetworkType;
            this.isWpsCall = builder.mIsWpsCall;
        }
    }

    public static final class ImsRegistrationRadioTechInfo extends Record {
        private final int imsRegistrationState;
        private final int imsRegistrationTech;
        private final int phoneId;

        private /* synthetic */ boolean $record$equals(Object obj) {
            if (!(obj instanceof ImsRegistrationRadioTechInfo)) {
                return false;
            }
            ImsRegistrationRadioTechInfo imsRegistrationRadioTechInfo = (ImsRegistrationRadioTechInfo) obj;
            return this.phoneId == imsRegistrationRadioTechInfo.phoneId && this.imsRegistrationTech == imsRegistrationRadioTechInfo.imsRegistrationTech && this.imsRegistrationState == imsRegistrationRadioTechInfo.imsRegistrationState;
        }

        private /* synthetic */ Object[] $record$getFieldsAsObjects() {
            return new Object[]{Integer.valueOf(this.phoneId), Integer.valueOf(this.imsRegistrationTech), Integer.valueOf(this.imsRegistrationState)};
        }

        public ImsRegistrationRadioTechInfo(int i, int i2, int i3) {
            this.phoneId = i;
            this.imsRegistrationTech = i2;
            this.imsRegistrationState = i3;
        }

        @Override // java.lang.Record
        public final boolean equals(Object obj) {
            return $record$equals(obj);
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return ImsPhone$ImsRegistrationRadioTechInfo$$ExternalSyntheticRecord0.m(this.phoneId, this.imsRegistrationTech, this.imsRegistrationState);
        }

        public int imsRegistrationState() {
            return this.imsRegistrationState;
        }

        public int imsRegistrationTech() {
            return this.imsRegistrationTech;
        }

        public int phoneId() {
            return this.phoneId;
        }

        @Override // java.lang.Record
        public final String toString() {
            return NetworkTypeController$ModemDisplayNetworkType$$ExternalSyntheticRecord0.m($record$getFieldsAsObjects(), ImsRegistrationRadioTechInfo.class, "phoneId;imsRegistrationTech;imsRegistrationState");
        }
    }

    public void setCurrentSubscriberUris(Uri[] uriArr) {
        this.mCurrentSubscriberUris = uriArr;
    }

    @Override // com.android.internal.telephony.Phone
    public Uri[] getCurrentSubscriberUris() {
        return this.mCurrentSubscriberUris;
    }

    public void setCallComposerStatus(int i) {
        this.mImsPhoneSharedPreferences.edit().putInt("userset_callcomposer_prefix" + getSubId(), i).commit();
    }

    public int getCallComposerStatus() {
        return this.mImsPhoneSharedPreferences.getInt("userset_callcomposer_prefix" + getSubId(), 0);
    }

    @Override // com.android.internal.telephony.Phone
    public int getEmergencyNumberDbVersion() {
        return getEmergencyNumberTracker().getEmergencyNumberDbVersion();
    }

    @Override // com.android.internal.telephony.Phone
    public EmergencyNumberTracker getEmergencyNumberTracker() {
        return this.mDefaultPhone.getEmergencyNumberTracker();
    }

    @Override // com.android.internal.telephony.Phone
    public ServiceStateTracker getServiceStateTracker() {
        return this.mDefaultPhone.getServiceStateTracker();
    }

    protected static class Cf {
        public final boolean mIsCfu;
        public final Message mOnComplete;
        final int mServiceClass;
        public final String mSetCfNumber;

        Cf(String str, boolean z, Message message, int i) {
            this.mSetCfNumber = str;
            this.mIsCfu = z;
            this.mOnComplete = message;
            this.mServiceClass = i;
        }

        public Cf(String str, boolean z, Message message) {
            this.mSetCfNumber = str;
            this.mIsCfu = z;
            this.mOnComplete = message;
            this.mServiceClass = 0;
        }
    }

    public static class SS {
        public int mCfAction;
        public int mCfReason;
        int mClirMode;
        public String mDialingNumber;
        boolean mEnable;
        String mFacility;
        boolean mLockState;
        public Message mOnComplete;
        String mPassword;
        public int mServiceClass;
        int mTimerSeconds;

        SS(Message message) {
            this.mOnComplete = message;
        }

        SS(int i, Message message) {
            this.mClirMode = i;
            this.mOnComplete = message;
        }

        SS(boolean z, int i, Message message) {
            this.mEnable = z;
            this.mServiceClass = i;
            this.mOnComplete = message;
        }

        SS(int i, int i2, Message message) {
            this.mCfReason = i;
            this.mServiceClass = i2;
            this.mOnComplete = message;
        }

        SS(int i, int i2, String str, int i3, int i4, Message message) {
            this.mCfAction = i;
            this.mCfReason = i2;
            this.mDialingNumber = str;
            this.mServiceClass = i3;
            this.mTimerSeconds = i4;
            this.mOnComplete = message;
        }

        SS(String str, String str2, int i, Message message) {
            this.mFacility = str;
            this.mPassword = str2;
            this.mServiceClass = i;
            this.mOnComplete = message;
        }

        SS(String str, boolean z, String str2, int i, Message message) {
            this.mFacility = str;
            this.mLockState = z;
            this.mPassword = str2;
            this.mServiceClass = i;
            this.mOnComplete = message;
        }
    }

    public ImsPhone(Context context, PhoneNotifier phoneNotifier, Phone phone, FeatureFlags featureFlags) {
        this(context, phoneNotifier, phone, new ImsManagerFactory() { // from class: com.android.internal.telephony.imsphone.ImsPhone$$ExternalSyntheticLambda1
            @Override // com.android.internal.telephony.imsphone.ImsPhone.ImsManagerFactory
            public final ImsManager create(Context context2, int i) {
                return ImsManager.getInstance(context2, i);
            }
        }, false, featureFlags);
    }

    public ImsPhone(Context context, PhoneNotifier phoneNotifier, Phone phone, ImsManagerFactory imsManagerFactory, boolean z, FeatureFlags featureFlags) {
        super("ImsPhone", context, phoneNotifier, z, featureFlags);
        this.LOG_TAG = "ImsPhone";
        this.mPendingMMIs = new ArrayList<>();
        this.mSS = new ServiceState();
        this.mSilentRedialRegistrants = new RegistrantList();
        this.mImsRegistrationUpdateRegistrants = new RegistrantList();
        this.mRegLocalLog = new LocalLog(64);
        this.mLastKnownRoamingState = false;
        this.mLastKnownIsUsingNtnState = false;
        this.mIsInImsEcm = false;
        this.mIsOutgoingImsVoiceAllowed = false;
        this.mSsnRegistrants = new RegistrantList();
        this.mImsRegistrationTech = -1;
        this.mImsDeregistrationTech = -1;
        this.mTransportType = -1;
        this.mPhoneNumberManager = null;
        this.mExitEcmRunnable = new Runnable() { // from class: com.android.internal.telephony.imsphone.ImsPhone.1
            @Override // java.lang.Runnable
            public void run() {
                ImsPhone.this.exitEmergencyCallbackMode();
            }
        };
        this.mImsEcbmStateListener = new AnonymousClass2(this.mContext.getMainExecutor());
        this.mResultReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.imsphone.ImsPhone.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (getResultCode() == -1) {
                    CharSequence charSequenceExtra = intent.getCharSequenceExtra(Phone.EXTRA_KEY_ALERT_TITLE);
                    CharSequence charSequenceExtra2 = intent.getCharSequenceExtra(Phone.EXTRA_KEY_ALERT_MESSAGE);
                    CharSequence charSequenceExtra3 = intent.getCharSequenceExtra(Phone.EXTRA_KEY_NOTIFICATION_MESSAGE);
                    Intent intent2 = new Intent("android.intent.action.MAIN");
                    intent2.setClassName("com.android.settings", "com.android.settings.Settings$WifiCallingSettingsActivity");
                    intent2.putExtra(Phone.EXTRA_KEY_ALERT_SHOW, true);
                    intent2.putExtra(Phone.EXTRA_KEY_ALERT_TITLE, charSequenceExtra);
                    intent2.putExtra(Phone.EXTRA_KEY_ALERT_MESSAGE, charSequenceExtra2);
                    ((NotificationManager) ((Phone) ImsPhone.this).mContext.getSystemService("notification")).notify("wifi_calling", 1, new Notification.Builder(((Phone) ImsPhone.this).mContext).setSmallIcon(R.drawable.stat_sys_warning).setContentTitle(charSequenceExtra).setContentText(charSequenceExtra3).setAutoCancel(true).setContentIntent(PendingIntent.getActivity(((Phone) ImsPhone.this).mContext, 0, intent2, 201326592)).setStyle(new Notification.BigTextStyle().bigText(charSequenceExtra3)).setChannelId(NotificationChannelController.CHANNEL_ID_WFC).build());
                }
            }
        };
        this.mMmTelRegistrationUpdate = new ImsRegistrationCallbackHelper.ImsRegistrationUpdate() { // from class: com.android.internal.telephony.imsphone.ImsPhone.4
            @Override // com.android.internal.telephony.imsphone.ImsRegistrationCallbackHelper.ImsRegistrationUpdate
            public void handleImsRegistered(ImsRegistrationAttributes imsRegistrationAttributes) {
                int transportType = imsRegistrationAttributes.getTransportType();
                ImsPhone.this.logd("handleImsRegistered: onImsMmTelConnected imsTransportType=" + AccessNetworkConstants.transportTypeToString(transportType));
                ImsPhone.this.mRegLocalLog.log("handleImsRegistered: onImsMmTelConnected imsTransportType=" + AccessNetworkConstants.transportTypeToString(transportType));
                ImsPhone.this.setServiceState(0);
                ImsPhone.this.getDefaultPhone().setImsRegistrationState(true);
                ImsPhone.this.mImsStats.onImsRegistered(imsRegistrationAttributes);
                ImsPhone.this.mImsNrSaModeHandler.onImsRegistered(imsRegistrationAttributes.getRegistrationTechnology());
                ImsPhone.this.updateImsRegistrationInfo(2, imsRegistrationAttributes.getRegistrationTechnology(), 0, transportType);
                ImsPhone.this.mImsRegistrationUpdateRegistrants.notifyRegistrants(new AsyncResult((Object) null, new ImsRegistrationRadioTechInfo(((Phone) ImsPhone.this).mPhoneId, imsRegistrationAttributes.getRegistrationTechnology(), 2), (Throwable) null));
            }

            @Override // com.android.internal.telephony.imsphone.ImsRegistrationCallbackHelper.ImsRegistrationUpdate
            public void handleImsRegistering(int i) {
                ImsPhone.this.logd("handleImsRegistering: onImsMmTelProgressing imsRadioTech=" + AccessNetworkConstants.transportTypeToString(i));
                ImsPhone.this.mRegLocalLog.log("handleImsRegistering: onImsMmTelProgressing imsRadioTech=" + AccessNetworkConstants.transportTypeToString(i));
                ImsPhone.this.setServiceState(1);
                ImsPhone.this.getDefaultPhone().setImsRegistrationState(false);
                ImsPhone.this.mImsStats.onImsRegistering(i);
                ImsPhone.this.mImsRegistrationUpdateRegistrants.notifyRegistrants(new AsyncResult((Object) null, new ImsRegistrationRadioTechInfo(((Phone) ImsPhone.this).mPhoneId, i, 1), (Throwable) null));
            }

            @Override // com.android.internal.telephony.imsphone.ImsRegistrationCallbackHelper.ImsRegistrationUpdate
            public void handleImsUnregistered(ImsReasonInfo imsReasonInfo, int i, int i2) {
                FeatureFlags unused = ((Phone) ImsPhone.this).mFeatureFlags;
                ImsPhone.this.processImsUnregistered(imsReasonInfo, i, i2, 0);
            }

            @Override // com.android.internal.telephony.imsphone.ImsRegistrationCallbackHelper.ImsRegistrationUpdate
            public void handleImsUnregistered(ImsReasonInfo imsReasonInfo, int i, int i2, int i3) {
                ImsPhone.this.processImsUnregistered(imsReasonInfo, i, i2, i3);
            }

            @Override // com.android.internal.telephony.imsphone.ImsRegistrationCallbackHelper.ImsRegistrationUpdate
            public void handleImsSubscriberAssociatedUriChanged(Uri[] uriArr) {
                ImsPhone.this.logd("handleImsSubscriberAssociatedUriChanged" + uriArr);
                ImsPhone.this.setCurrentSubscriberUris(uriArr);
                MiuiSubscriptionControllerStub.get().addPhoneNumberToSubInfo(((Phone) ImsPhone.this).mContext, ((Phone) ImsPhone.this).mPhoneId);
                if (TelephonyBaseUtilsStub.isAutoDevice()) {
                    TelephonyMultiInternetStub.get().setPhoneNumber(ImsPhone.this.getSubscriberUriNumber(), ImsPhone.this.mDefaultPhone.getPhoneId());
                }
                ImsPhone.this.setPhoneNumberForSourceIms(uriArr);
            }
        };
        this.mMmTelEmergencyRegistrationUpdate = new ImsRegistrationCallbackHelper.ImsRegistrationUpdate() { // from class: com.android.internal.telephony.imsphone.ImsPhone.5
            @Override // com.android.internal.telephony.imsphone.ImsRegistrationCallbackHelper.ImsRegistrationUpdate
            public void handleImsRegistering(int i) {
            }

            @Override // com.android.internal.telephony.imsphone.ImsRegistrationCallbackHelper.ImsRegistrationUpdate
            public void handleImsSubscriberAssociatedUriChanged(Uri[] uriArr) {
            }

            @Override // com.android.internal.telephony.imsphone.ImsRegistrationCallbackHelper.ImsRegistrationUpdate
            public void handleImsRegistered(ImsRegistrationAttributes imsRegistrationAttributes) {
                ImsPhone.this.mImsNrSaModeHandler.onImsEmergencyRegistered(imsRegistrationAttributes.getRegistrationTechnology());
            }

            @Override // com.android.internal.telephony.imsphone.ImsRegistrationCallbackHelper.ImsRegistrationUpdate
            public void handleImsUnregistered(ImsReasonInfo imsReasonInfo, int i, int i2) {
                ImsPhone.this.mImsNrSaModeHandler.onImsEmergencyUnregistered(i2);
            }

            @Override // com.android.internal.telephony.imsphone.ImsRegistrationCallbackHelper.ImsRegistrationUpdate
            public void handleImsUnregistered(ImsReasonInfo imsReasonInfo, int i, int i2, int i3) {
                ImsPhone.this.mImsNrSaModeHandler.onImsEmergencyUnregistered(i2);
            }
        };
        this.LOG_TAG += "[" + phone.getPhoneId() + "]";
        this.mDefaultPhone = phone;
        this.mImsManagerFactory = imsManagerFactory;
        this.mImsPhoneSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.mImsStats = new ImsStats(this);
        this.mExternalCallTracker = TelephonyComponentFactory.getInstance().inject(ImsExternalCallTracker.class.getName()).makeImsExternalCallTracker(this);
        this.mImsNrSaModeHandler = TelephonyComponentFactory.getInstance().inject(ImsNrSaModeHandler.class.getName()).makeImsNrSaModeHandler(this);
        ImsPhoneCallTracker imsPhoneCallTrackerMakeImsPhoneCallTracker = TelephonyComponentFactory.getInstance().inject(ImsPhoneCallTracker.class.getName()).makeImsPhoneCallTracker(this, featureFlags);
        this.mCT = imsPhoneCallTrackerMakeImsPhoneCallTracker;
        imsPhoneCallTrackerMakeImsPhoneCallTracker.registerPhoneStateListener(this.mExternalCallTracker);
        this.mExternalCallTracker.setCallPuller(this.mCT);
        this.mSS.setOutOfService(false, false);
        this.mPhoneId = this.mDefaultPhone.getPhoneId();
        this.mImsMmTelRegistrationHelper = new ImsRegistrationCallbackHelper(this.mMmTelRegistrationUpdate, context.getMainExecutor());
        this.mImsMmTelEmergencyRegistrationHelper = new ImsRegistrationCallbackHelper(this.mMmTelEmergencyRegistrationUpdate, context.getMainExecutor());
        PowerManager.WakeLock wakeLockNewWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, this.LOG_TAG);
        this.mWakeLock = wakeLockNewWakeLock;
        wakeLockNewWakeLock.setReferenceCounted(false);
        if (this.mDefaultPhone.getServiceStateTracker() != null && this.mDefaultPhone.getAccessNetworksManager() != null) {
            for (int i : this.mDefaultPhone.getAccessNetworksManager().getAvailableTransports()) {
                this.mDefaultPhone.getServiceStateTracker().registerForDataRegStateOrRatChanged(i, this, 84, null);
            }
        }
        setServiceState(1);
        this.mDefaultPhone.registerForServiceStateChanged(this, 85, null);
        this.mDefaultPhone.registerForVolteSilentRedial(this, 87, null);
    }

    @Override // com.android.internal.telephony.Phone
    public void dispose() {
        logd("dispose");
        this.mPendingMMIs.clear();
        this.mExternalCallTracker.tearDown();
        this.mImsNrSaModeHandler.tearDown();
        this.mCT.unregisterPhoneStateListener(this.mExternalCallTracker);
        this.mCT.unregisterForVoiceCallEnded(this);
        this.mCT.dispose();
        Phone phone = this.mDefaultPhone;
        if (phone != null && phone.getServiceStateTracker() != null) {
            for (int i : this.mDefaultPhone.getAccessNetworksManager().getAvailableTransports()) {
                this.mDefaultPhone.getServiceStateTracker().unregisterForDataRegStateOrRatChanged(i, this);
            }
            this.mDefaultPhone.unregisterForServiceStateChanged(this);
        }
        Phone phone2 = this.mDefaultPhone;
        if (phone2 != null) {
            phone2.unregisterForVolteSilentRedial(this);
        }
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public ServiceState getServiceState() {
        return new ServiceState(this.mSS);
    }

    public void setServiceState(int i) {
        boolean z;
        synchronized (this) {
            z = this.mSS.getState() != i;
            this.mSS.setVoiceRegState(i);
        }
        updateDataServiceState();
        if (!z || this.mDefaultPhone.getServiceStateTracker() == null) {
            return;
        }
        this.mDefaultPhone.getServiceStateTracker().onImsServiceStateChanged();
    }

    @Override // com.android.internal.telephony.Phone
    public CallTracker getCallTracker() {
        return this.mCT;
    }

    @Override // com.android.internal.telephony.Phone
    public void setVoiceCallForwardingFlag(int i, boolean z, String str) {
        IccRecords iccRecords = this.mDefaultPhone.getIccRecords();
        if (iccRecords != null) {
            setVoiceCallForwardingFlag(iccRecords, i, z, str);
        }
    }

    public ImsExternalCallTracker getExternalCallTracker() {
        return this.mExternalCallTracker;
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public List<? extends ImsPhoneMmiCode> getPendingMmiCodes() {
        return this.mPendingMMIs;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void acceptCall(int i) throws CallStateException {
        this.mCT.acceptCall(i);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void rejectCall() throws CallStateException {
        this.mCT.rejectCall();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void switchHoldingAndActive() throws CallStateException {
        throw new UnsupportedOperationException("Use hold() and unhold() instead.");
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public boolean canConference() {
        return this.mCT.canConference();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase
    public boolean canDial() {
        try {
            this.mCT.checkForDialIssues();
            return true;
        } catch (CallStateException unused) {
            return false;
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void conference() {
        this.mCT.conference();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void clearDisconnected() {
        this.mCT.clearDisconnected();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public boolean canTransfer() {
        return this.mCT.canTransfer();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void explicitCallTransfer() throws CallStateException {
        this.mCT.explicitCallTransfer();
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public ImsPhoneCall getForegroundCall() {
        return this.mCT.mForegroundCall;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public ImsPhoneCall getBackgroundCall() {
        return this.mCT.mBackgroundCall;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public ImsPhoneCall getRingingCall() {
        return this.mCT.mRingingCall;
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isImsAvailable() {
        return this.mCT.isImsServiceReady();
    }

    @Override // com.android.internal.telephony.Phone
    public CarrierPrivilegesTracker getCarrierPrivilegesTracker() {
        return this.mDefaultPhone.getCarrierPrivilegesTracker();
    }

    public void holdActiveCall() throws CallStateException {
        this.mCT.holdActiveCall();
    }

    public void unholdHeldCall() throws CallStateException {
        this.mCT.unholdHeldCall();
    }

    private boolean handleCallDeflectionIncallSupplementaryService(String str) {
        if (str.length() > 1) {
            return false;
        }
        Call.State state = getRingingCall().getState();
        Call.State state2 = Call.State.IDLE;
        if (state != state2) {
            logd("MmiCode 0: rejectCall");
            try {
                this.mCT.rejectCall();
            } catch (CallStateException e) {
                Rlog.d(this.LOG_TAG, "reject failed", e);
                notifySuppServiceFailed(PhoneInternalInterface.SuppService.REJECT);
            }
        } else if (getBackgroundCall().getState() != state2) {
            logd("MmiCode 0: hangupWaitingOrBackground");
            try {
                this.mCT.hangup(getBackgroundCall());
            } catch (CallStateException e2) {
                Rlog.d(this.LOG_TAG, "hangup failed", e2);
            }
        }
        return true;
    }

    protected void sendUssdResponse(String str, CharSequence charSequence, int i, ResultReceiver resultReceiver) {
        UssdResponse ussdResponse = new UssdResponse(str, charSequence);
        Bundle bundle = new Bundle();
        bundle.putParcelable("USSD_RESPONSE", ussdResponse);
        resultReceiver.send(i, bundle);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public boolean handleUssdRequest(String str, ResultReceiver resultReceiver) throws CallStateException {
        if (this.mPendingMMIs.size() > 0) {
            logi("handleUssdRequest: queue full: " + Rlog.pii(this.LOG_TAG, str));
            sendUssdResponse(str, null, -1, resultReceiver);
            return true;
        }
        try {
            dialInternal(str, new ImsDialArgs.Builder().build(), resultReceiver);
        } catch (CallStateException e) {
            if (Phone.CS_FALLBACK.equals(e.getMessage())) {
                throw e;
            }
            Rlog.w(this.LOG_TAG, "Could not execute USSD " + e);
            sendUssdResponse(str, null, -1, resultReceiver);
        } catch (Exception e2) {
            Rlog.w(this.LOG_TAG, "Could not execute USSD " + e2);
            sendUssdResponse(str, null, -1, resultReceiver);
            return false;
        }
        return true;
    }

    private boolean handleCallWaitingIncallSupplementaryService(String str) {
        int length = str.length();
        if (length > 2) {
            return false;
        }
        ImsPhoneCall foregroundCall = getForegroundCall();
        try {
            if (length > 1) {
                logd("not support 1X SEND");
                notifySuppServiceFailed(PhoneInternalInterface.SuppService.HANGUP);
            } else if (foregroundCall.getState() != Call.State.IDLE) {
                logd("MmiCode 1: hangup foreground");
                this.mCT.hangup(foregroundCall);
            } else {
                logd("MmiCode 1: holdActiveCallForWaitingCall");
                this.mCT.holdActiveCallForWaitingCall();
            }
        } catch (CallStateException e) {
            Rlog.d(this.LOG_TAG, "hangup failed", e);
            notifySuppServiceFailed(PhoneInternalInterface.SuppService.HANGUP);
        }
        return true;
    }

    private boolean handleCallHoldIncallSupplementaryService(String str) {
        int length = str.length();
        if (length > 2) {
            return false;
        }
        if (length > 1) {
            logd("separate not supported");
            notifySuppServiceFailed(PhoneInternalInterface.SuppService.SEPARATE);
        } else {
            try {
                Call.State state = getRingingCall().getState();
                Call.State state2 = Call.State.IDLE;
                if (state != state2) {
                    logd("MmiCode 2: accept ringing call");
                    this.mCT.acceptCall(0);
                } else if (getBackgroundCall().getState() == Call.State.HOLDING) {
                    if (getForegroundCall().getState() != state2) {
                        logd("MmiCode 2: switch holding and active");
                        this.mCT.holdActiveCall();
                    } else {
                        logd("MmiCode 2: unhold held call");
                        this.mCT.unholdHeldCall();
                    }
                } else if (getForegroundCall().getState() != state2) {
                    logd("MmiCode 2: hold active call");
                    this.mCT.holdActiveCall();
                }
            } catch (CallStateException e) {
                Rlog.d(this.LOG_TAG, "switch failed", e);
                notifySuppServiceFailed(PhoneInternalInterface.SuppService.SWITCH);
            }
        }
        return true;
    }

    private boolean handleMultipartyIncallSupplementaryService(String str) {
        if (str.length() > 1) {
            return false;
        }
        logd("MmiCode 3: merge calls");
        conference();
        return true;
    }

    protected boolean handleEctIncallSupplementaryService(String str) {
        if (str.length() != 1) {
            return false;
        }
        logd("MmiCode 4: explicit call transfer");
        try {
            explicitCallTransfer();
        } catch (CallStateException e) {
            Rlog.d(this.LOG_TAG, "explicit call transfer failed", e);
            notifySuppServiceFailed(PhoneInternalInterface.SuppService.TRANSFER);
        }
        return true;
    }

    private boolean handleCcbsIncallSupplementaryService(String str) {
        if (str.length() > 1) {
            return false;
        }
        logi("MmiCode 5: CCBS not supported!");
        notifySuppServiceFailed(PhoneInternalInterface.SuppService.UNKNOWN);
        return true;
    }

    public void notifySuppSvcNotification(SuppServiceNotification suppServiceNotification) {
        logd("notifySuppSvcNotification: suppSvc = " + suppServiceNotification);
        this.mSsnRegistrants.notifyRegistrants(new AsyncResult((Object) null, suppServiceNotification, (Throwable) null));
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public boolean handleInCallMmiCommands(String str) {
        if (!isInCall() || TextUtils.isEmpty(str)) {
            return false;
        }
        switch (str.charAt(0)) {
            case '0':
                return handleCallDeflectionIncallSupplementaryService(str);
            case '1':
                return handleCallWaitingIncallSupplementaryService(str);
            case '2':
                return handleCallHoldIncallSupplementaryService(str);
            case '3':
                return handleMultipartyIncallSupplementaryService(str);
            case '4':
                return handleEctIncallSupplementaryService(str);
            case '5':
                return handleCcbsIncallSupplementaryService(str);
            default:
                return false;
        }
    }

    public boolean isInCall() {
        return getForegroundCall().getState().isAlive() || getBackgroundCall().getState().isAlive() || getRingingCall().getState().isAlive();
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isInImsEcm() {
        if (DomainSelectionResolver.getInstance().isDomainSelectionSupported()) {
            return EmergencyStateTracker.getInstance().isInImsEcm();
        }
        return this.mIsInImsEcm;
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isInEcm() {
        return this.mDefaultPhone.isInEcm();
    }

    @Override // com.android.internal.telephony.Phone
    public void setIsInEcm(boolean z) {
        this.mIsInImsEcm = z;
        this.mDefaultPhone.setIsInEcm(z);
    }

    public void notifyNewRingingConnection(Connection connection) {
        this.mDefaultPhone.notifyNewRingingConnectionP(connection);
    }

    public void notifyUnknownConnection(Connection connection) {
        this.mDefaultPhone.notifyUnknownConnectionP(connection);
    }

    @Override // com.android.internal.telephony.Phone
    public void notifyForVideoCapabilityChanged(boolean z) {
        this.mIsVideoCapable = z;
        this.mDefaultPhone.notifyForVideoCapabilityChanged(z);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void setRadioPower(boolean z, boolean z2, boolean z3, boolean z4) {
        this.mDefaultPhone.setRadioPower(z, z2, z3, z4);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public Connection startConference(String[] strArr, PhoneInternalInterface.DialArgs dialArgs) throws CallStateException {
        return this.mCT.startConference(strArr, ImsDialArgs.Builder.from(dialArgs).build());
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public Connection dial(String str, PhoneInternalInterface.DialArgs dialArgs, Consumer<Phone> consumer) throws CallStateException {
        consumer.accept(this);
        return dialInternal(str, dialArgs, null);
    }

    protected Connection dialInternal(String str, PhoneInternalInterface.DialArgs dialArgs, ResultReceiver resultReceiver) throws CallStateException {
        this.mLastDialString = str;
        boolean zIsUriNumber = PhoneNumberUtils.isUriNumber(str);
        String strStripSeparators = !zIsUriNumber ? PhoneNumberUtils.stripSeparators(str) : str;
        if (!dialArgs.isEmergency && resultReceiver == null && handleInCallMmiCommands(strStripSeparators)) {
            return null;
        }
        ImsDialArgs.Builder builderFrom = ImsDialArgs.Builder.from(dialArgs);
        builderFrom.setClirMode(this.mCT.getClirMode());
        if (!builderFrom.build().isEmergency && handleInCallMmiCommands(strStripSeparators)) {
            return null;
        }
        if (dialArgs.isEmergency) {
            logd("dialInternal: emergency number, skip to check mmi code");
            return this.mCT.dial(str, builderFrom.build());
        }
        ImsPhoneMmiCode imsPhoneMmiCodeNewFromDialString = !zIsUriNumber ? ImsPhoneMmiCode.newFromDialString(PhoneNumberUtils.extractNetworkPortionAlt(strStripSeparators), this, resultReceiver, this.mFeatureFlags) : null;
        logd("dialInternal: dialing w/ mmi '" + imsPhoneMmiCodeNewFromDialString + "'...");
        if (imsPhoneMmiCodeNewFromDialString == null) {
            return this.mCT.dial(str, builderFrom.build());
        }
        if (imsPhoneMmiCodeNewFromDialString.isTemporaryModeCLIR()) {
            builderFrom.setClirMode(imsPhoneMmiCodeNewFromDialString.getCLIRMode());
            return this.mCT.dial(imsPhoneMmiCodeNewFromDialString.getDialingNumber(), builderFrom.build());
        }
        if (!imsPhoneMmiCodeNewFromDialString.isSupportedOverImsPhone()) {
            logi("dialInternal: USSD not supported by IMS; fallback to CS.");
            CallManager$$ExternalSyntheticBUOutline0.m(Phone.CS_FALLBACK);
            return null;
        }
        this.mPendingMMIs.add(imsPhoneMmiCodeNewFromDialString);
        this.mMmiRegistrants.notifyRegistrants(new AsyncResult((Object) null, imsPhoneMmiCodeNewFromDialString, (Throwable) null));
        try {
            logd("MMI processCode");
            imsPhoneMmiCodeNewFromDialString.processCode();
        } catch (CallStateException e) {
            if (Phone.CS_FALLBACK.equals(e.getMessage())) {
                logi("dialInternal: fallback to GSM required.");
                this.mPendingMMIs.remove(imsPhoneMmiCodeNewFromDialString);
                throw e;
            }
        }
        return null;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void sendDtmf(char c) {
        if (!PhoneNumberUtils.is12Key(c)) {
            loge("sendDtmf called with invalid character '" + c + "'");
            return;
        }
        if (this.mCT.getState() == PhoneConstants.State.OFFHOOK) {
            this.mCT.sendDtmf(c, null);
        }
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void startDtmf(char c) {
        if (!PhoneNumberUtils.is12Key(c) && (c < 'A' || c > 'D')) {
            loge("startDtmf called with invalid character '" + c + "'");
            return;
        }
        this.mCT.startDtmf(c);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void stopDtmf() {
        this.mCT.stopDtmf();
    }

    public void notifyIncomingRing() {
        logd("notifyIncomingRing");
        sendMessage(obtainMessage(14, new AsyncResult((Object) null, (Object) null, (Throwable) null)));
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public void setMute(boolean z) {
        this.mCT.setMute(z);
    }

    @Override // com.android.internal.telephony.Phone
    public void setTTYMode(int i, Message message) {
        this.mCT.setTtyMode(i);
    }

    @Override // com.android.internal.telephony.Phone
    public void setUiTTYMode(int i, Message message) {
        this.mCT.setUiTTYMode(i, message);
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public boolean getMute() {
        return this.mCT.getMute();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public PhoneConstants.State getState() {
        return this.mCT.getState();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public void getOutgoingCallerIdDisplay(Message message) {
        logd("getCLIR");
        try {
            this.mCT.getUtInterface().queryCLIR(obtainMessage(83, new SS(message)));
        } catch (ImsException e) {
            sendErrorResponse(message, e);
        }
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public void setOutgoingCallerIdDisplay(int i, Message message) {
        logd("setCLIR action= " + i);
        try {
            this.mCT.getUtInterface().updateCLIR(i, obtainMessage(82, new SS(i, message)));
        } catch (ImsException e) {
            sendErrorResponse(message, e);
        }
    }

    @Override // com.android.internal.telephony.Phone
    public void queryCLIP(Message message) {
        Message messageObtainMessage = obtainMessage(88, new SS(message));
        try {
            Rlog.d(this.LOG_TAG, "ut.queryCLIP");
            this.mCT.getUtInterface().queryCLIP(messageObtainMessage);
        } catch (ImsException e) {
            sendErrorResponse(message, e);
        }
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public void getCallForwardingOption(int i, Message message) {
        getCallForwardingOption(i, 1, message);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone, com.android.internal.telephony.PhoneInternalInterface
    public void getCallForwardingOption(int i, int i2, Message message) {
        Rlog.d(this.LOG_TAG, "getCallForwardingOption reason=" + i + "serviceclass =" + i2);
        if (!isValidCommandInterfaceCFReason(i)) {
            if (message != null) {
                sendErrorResponse(message);
            }
        } else {
            Rlog.d(this.LOG_TAG, "requesting call forwarding query.");
            try {
                this.mCT.getUtInterface().queryCallForward(getConditionFromCFReason(i), (String) null, obtainMessage(13, new SS(i, i2, message)));
            } catch (ImsException e) {
                sendErrorResponse(message, e);
            }
        }
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public void setCallForwardingOption(int i, int i2, String str, int i3, Message message) {
        setCallForwardingOption(i, i2, str, 1, i3, message);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone, com.android.internal.telephony.PhoneInternalInterface
    public void setCallForwardingOption(int i, int i2, String str, int i3, int i4, Message message) {
        logd("setCallForwardingOption action=" + i + ", reason=" + i2 + " serviceClass=" + i3);
        if (!isValidCommandInterfaceCFAction(i) || !isValidCommandInterfaceCFReason(i2)) {
            if (message != null) {
                sendErrorResponse(message);
            }
        } else {
            try {
                this.mCT.getUtInterface().updateCallForward(getActionFromCFAction(i), getConditionFromCFReason(i2), str, i3, i4, obtainMessage(12, new SS(i, i2, str, i3, i4, message)));
            } catch (ImsException e) {
                sendErrorResponse(message, e);
            }
        }
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public void getCallWaiting(Message message) {
        logd("getCallWaiting");
        try {
            this.mCT.getUtInterface().queryCallWaiting(obtainMessage(81, new SS(message)));
        } catch (ImsException e) {
            sendErrorResponse(message, e);
        }
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public void setCallWaiting(boolean z, Message message) {
        PersistableBundle configForSubId = ((CarrierConfigManager) getContext().getSystemService("carrier_config")).getConfigForSubId(getSubId());
        setCallWaiting(z, configForSubId != null ? configForSubId.getInt("call_waiting_service_class_int", 1) : 1, message);
    }

    @Override // com.android.internal.telephony.Phone
    public void setCallWaiting(boolean z, int i, Message message) {
        logd("setCallWaiting enable=" + z);
        try {
            this.mCT.getUtInterface().updateCallWaiting(z, i, obtainMessage(80, new SS(z, i, message)));
        } catch (ImsException e) {
            sendErrorResponse(message, e);
        }
    }

    protected int getCBTypeFromFacility(String str) {
        if (CommandsInterface.CB_FACILITY_BAOC.equals(str)) {
            return 2;
        }
        if (CommandsInterface.CB_FACILITY_BAOIC.equals(str)) {
            return 3;
        }
        if (CommandsInterface.CB_FACILITY_BAOICxH.equals(str)) {
            return 4;
        }
        if (CommandsInterface.CB_FACILITY_BAIC.equals(str)) {
            return 1;
        }
        if (CommandsInterface.CB_FACILITY_BAICr.equals(str)) {
            return 5;
        }
        if (CommandsInterface.CB_FACILITY_BA_ALL.equals(str)) {
            return 7;
        }
        if (CommandsInterface.CB_FACILITY_BA_MO.equals(str)) {
            return 8;
        }
        if (CommandsInterface.CB_FACILITY_BA_MT.equals(str)) {
            return 9;
        }
        return CommandsInterface.CB_FACILITY_BIC_ACR.equals(str) ? 6 : 0;
    }

    public void getCallBarring(String str, Message message) {
        getCallBarring(str, message, 0);
    }

    public void getCallBarring(String str, Message message, int i) {
        getCallBarring(str, "", message, i);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public void getCallBarring(String str, String str2, Message message, int i) {
        logd("getCallBarring facility=" + str + ", serviceClass = " + i);
        try {
            this.mCT.getUtInterface().queryCallBarring(getCBTypeFromFacility(str), obtainMessage(79, new SS(str, str2, i, message)), i);
        } catch (ImsException e) {
            sendErrorResponse(message, e);
        }
    }

    public void setCallBarring(String str, boolean z, String str2, Message message) {
        setCallBarring(str, z, str2, message, 0);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public void setCallBarring(String str, boolean z, String str2, Message message, int i) {
        logd("setCallBarring facility=" + str + ", lockState=" + z + ", serviceClass = " + i);
        try {
            this.mCT.getUtInterface().updateCallBarring(getCBTypeFromFacility(str), z ? 1 : 0, obtainMessage(78, new SS(str, z, str2, i, message)), (String[]) null, i, str2);
        } catch (ImsException e) {
            sendErrorResponse(message, e);
        }
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public void sendUssdResponse(String str) {
        logd("sendUssdResponse");
        ImsPhoneMmiCode imsPhoneMmiCodeNewFromUssdUserInput = ImsPhoneMmiCode.newFromUssdUserInput(str, this, this.mFeatureFlags);
        this.mPendingMMIs.add(imsPhoneMmiCodeNewFromUssdUserInput);
        this.mMmiRegistrants.notifyRegistrants(new AsyncResult((Object) null, imsPhoneMmiCodeNewFromUssdUserInput, (Throwable) null));
        imsPhoneMmiCodeNewFromUssdUserInput.sendUssd(str);
    }

    public void sendUSSD(String str, Message message) {
        Rlog.d(this.LOG_TAG, "sendUssd ussdString = " + str);
        this.mLastDialString = str;
        this.mCT.sendUSSD(str, message);
    }

    @Override // com.android.internal.telephony.Phone
    public void cancelUSSD(Message message) {
        this.mCT.cancelUSSD(message);
    }

    public void sendErrorResponse(Message message) {
        logd("sendErrorResponse");
        if (message != null) {
            AsyncResult.forMessage(message, (Object) null, new CommandException(CommandException.Error.GENERIC_FAILURE));
            message.sendToTarget();
        }
    }

    public void sendErrorResponse(Message message, Throwable th) {
        logd("sendErrorResponse");
        if (message != null) {
            AsyncResult.forMessage(message, (Object) null, getCommandException(th));
            message.sendToTarget();
        }
    }

    protected CommandException getCommandException(int i, String str) {
        logd("getCommandException code= " + i + ", errorString= " + str);
        CommandException.Error error = CommandException.Error.GENERIC_FAILURE;
        if (i != 241) {
            switch (i) {
                case 801:
                case 803:
                    error = CommandException.Error.REQUEST_NOT_SUPPORTED;
                    break;
                case 802:
                    error = CommandException.Error.RADIO_NOT_AVAILABLE;
                    break;
                default:
                    switch (i) {
                        case 821:
                            error = CommandException.Error.PASSWORD_INCORRECT;
                            break;
                        case 822:
                            error = CommandException.Error.SS_MODIFIED_TO_DIAL;
                            break;
                        case 823:
                            error = CommandException.Error.SS_MODIFIED_TO_USSD;
                            break;
                        case 824:
                            error = CommandException.Error.SS_MODIFIED_TO_SS;
                            break;
                        case 825:
                            error = CommandException.Error.SS_MODIFIED_TO_DIAL_VIDEO;
                            break;
                    }
                    break;
            }
        } else {
            error = CommandException.Error.FDN_CHECK_FAILURE;
        }
        return new CommandException(error, str);
    }

    protected CommandException getCommandException(Throwable th) {
        if (th instanceof ImsException) {
            return getCommandException(((ImsException) th).getCode(), th.getMessage());
        }
        logd("getCommandException generic failure");
        return new CommandException(CommandException.Error.GENERIC_FAILURE);
    }

    protected void onNetworkInitiatedUssd(ImsPhoneMmiCode imsPhoneMmiCode) {
        logd("onNetworkInitiatedUssd");
        this.mMmiCompleteRegistrants.notifyRegistrants(new AsyncResult((Object) null, imsPhoneMmiCode, (Throwable) null));
    }

    protected void onIncomingUSSD(int i, String str) {
        ImsPhoneMmiCode imsPhoneMmiCode;
        logd("onIncomingUSSD ussdMode=" + i);
        int i2 = 0;
        boolean z = i == 1;
        boolean z2 = (i == 0 || i == 1) ? false : true;
        int size = this.mPendingMMIs.size();
        while (true) {
            if (i2 >= size) {
                imsPhoneMmiCode = null;
                break;
            } else {
                if (this.mPendingMMIs.get(i2).isPendingUSSD()) {
                    imsPhoneMmiCode = this.mPendingMMIs.get(i2);
                    break;
                }
                i2++;
            }
        }
        if (imsPhoneMmiCode != null) {
            if (z2) {
                imsPhoneMmiCode.onUssdFinishedError();
                return;
            } else {
                imsPhoneMmiCode.onUssdFinished(str, z);
                return;
            }
        }
        if (!z2 && !TextUtils.isEmpty(str)) {
            onNetworkInitiatedUssd(ImsPhoneMmiCode.newNetworkInitiatedUssd(str, z, this, this.mFeatureFlags));
        } else if (z2) {
            ImsPhoneMmiCode.newNetworkInitiatedUssd(str, true, this, this.mFeatureFlags).onUssdFinishedError();
        }
    }

    public void onMMIDone(ImsPhoneMmiCode imsPhoneMmiCode) {
        logd("onMMIDone: mmi=" + imsPhoneMmiCode);
        if (this.mPendingMMIs.remove(imsPhoneMmiCode) || imsPhoneMmiCode.isUssdRequest() || imsPhoneMmiCode.isSsInfo()) {
            ResultReceiver ussdCallbackReceiver = imsPhoneMmiCode.getUssdCallbackReceiver();
            if (ussdCallbackReceiver != null) {
                sendUssdResponse(imsPhoneMmiCode.getDialString(), imsPhoneMmiCode.getMessage(), imsPhoneMmiCode.getState() == MmiCode.State.COMPLETE ? 100 : -1, ussdCallbackReceiver);
            } else {
                logv("onMMIDone: notifyRegistrants");
                this.mMmiCompleteRegistrants.notifyRegistrants(new AsyncResult((Object) null, imsPhoneMmiCode, (Throwable) null));
            }
        }
    }

    @Override // com.android.internal.telephony.Phone
    public ArrayList<Connection> getHandoverConnection() {
        ArrayList<Connection> arrayList = new ArrayList<>();
        arrayList.addAll(getForegroundCall().getConnections());
        arrayList.addAll(getBackgroundCall().getConnections());
        arrayList.addAll(getRingingCall().getConnections());
        if (arrayList.size() > 0) {
            return arrayList;
        }
        return null;
    }

    @Override // com.android.internal.telephony.Phone
    public void notifySrvccState(int i) {
        this.mCT.notifySrvccState(i);
    }

    public void initiateSilentRedial() {
        initiateSilentRedial(false, 0);
    }

    void initiateSilentRedial(final boolean z, final int i) {
        final AsyncResult asyncResult = new AsyncResult((Object) null, new Phone.SilentRedialParam(this.mLastDialString, CallFailCause.LOCAL_CALL_CS_RETRY_REQUIRED, new PhoneInternalInterface.DialArgs.Builder().setIsEmergency(z).setEccCategory(i).build()), (Throwable) null);
        this.mContext.getMainExecutor().execute(new Runnable() { // from class: com.android.internal.telephony.imsphone.ImsPhone$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$initiateSilentRedial$0(z, i, asyncResult);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initiateSilentRedial$0(boolean z, int i, AsyncResult asyncResult) {
        logd("initiateSilentRedial: notifying registrants, isEmergency=" + z + ", eccCategory=" + i);
        this.mSilentRedialRegistrants.notifyRegistrants(asyncResult);
    }

    public void registerForImsRegistrationChanges(Handler handler, int i, Object obj) {
        this.mImsRegistrationUpdateRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForImsRegistrationChanges(Handler handler) {
        this.mImsRegistrationUpdateRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.Phone
    public void registerForSilentRedial(Handler handler, int i, Object obj) {
        this.mSilentRedialRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.Phone
    public void unregisterForSilentRedial(Handler handler) {
        this.mSilentRedialRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public void registerForSuppServiceNotification(Handler handler, int i, Object obj) {
        this.mSsnRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.PhoneInternalInterface
    public void unregisterForSuppServiceNotification(Handler handler) {
        this.mSsnRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.Phone
    public int getSubId() {
        return this.mDefaultPhone.getSubId();
    }

    @Override // com.android.internal.telephony.Phone
    public int getPhoneId() {
        return this.mDefaultPhone.getPhoneId();
    }

    protected CallForwardInfo getCallForwardInfo(ImsCallForwardInfo imsCallForwardInfo) {
        CallForwardInfo callForwardInfo = new CallForwardInfo();
        callForwardInfo.status = imsCallForwardInfo.getStatus();
        callForwardInfo.reason = getCFReasonFromCondition(imsCallForwardInfo.getCondition());
        callForwardInfo.serviceClass = ((IHyperSsManager) HyperTelephonyFactory.getInstance().initModule(IHyperSsManager.DEFAULT, new Object[0])).getCallForwardServiceClass(this.mContext, getSubId(), imsCallForwardInfo.getServiceClass());
        callForwardInfo.toa = imsCallForwardInfo.getToA();
        callForwardInfo.number = imsCallForwardInfo.getNumber();
        callForwardInfo.timeSeconds = imsCallForwardInfo.getTimeSeconds();
        return callForwardInfo;
    }

    @Override // com.android.internal.telephony.PhoneInternalInterface
    public String getLine1Number() {
        return this.mDefaultPhone.getLine1Number();
    }

    @Override // com.android.internal.telephony.Phone
    public String getSubscriberUriNumber() {
        return QtiImsUtils.getSubscriberUriNumber(this.mCurrentSubscriberUris);
    }

    public CallForwardInfo[] handleCfQueryResult(ImsCallForwardInfo[] imsCallForwardInfoArr) {
        CallForwardInfo[] callForwardInfoArr = (imsCallForwardInfoArr == null || imsCallForwardInfoArr.length == 0) ? null : new CallForwardInfo[imsCallForwardInfoArr.length];
        if (imsCallForwardInfoArr == null || imsCallForwardInfoArr.length == 0) {
            setVoiceCallForwardingFlag(getIccRecords(), 1, false, null);
            return callForwardInfoArr;
        }
        int length = imsCallForwardInfoArr.length;
        for (int i = 0; i < length; i++) {
            if (imsCallForwardInfoArr[i].getCondition() == 0) {
                setVoiceCallForwardingFlag(getIccRecords(), 1, imsCallForwardInfoArr[i].getStatus() == 1, imsCallForwardInfoArr[i].getNumber());
            }
            callForwardInfoArr[i] = getCallForwardInfo(imsCallForwardInfoArr[i]);
        }
        return callForwardInfoArr;
    }

    protected int[] handleCbQueryResult(ImsSsInfo[] imsSsInfoArr) {
        int[] iArr = {0};
        if (imsSsInfoArr[0].getStatus() == 1) {
            iArr[0] = 1;
        }
        return iArr;
    }

    protected int[] handleCwQueryResult(ImsSsInfo[] imsSsInfoArr) {
        int[] iArr = {0, 0};
        if (imsSsInfoArr[0].getStatus() == 1) {
            iArr[0] = 1;
            iArr[1] = 1;
        }
        return iArr;
    }

    protected void sendResponse(Message message, Object obj, Throwable th) {
        if (message != null) {
            AsyncResult.forMessage(message, obj, th != null ? getCommandException(th) : null);
            message.sendToTarget();
        }
    }

    protected void updateDataServiceState() {
        if (this.mSS == null || this.mDefaultPhone.getServiceStateTracker() == null || this.mDefaultPhone.getServiceStateTracker().mSS == null) {
            return;
        }
        ServiceState serviceState = this.mDefaultPhone.getServiceStateTracker().mSS;
        this.mSS.setDataRegState(serviceState.getDataRegistrationState());
        Iterator it = serviceState.getNetworkRegistrationInfoListForDomain(2).iterator();
        while (it.hasNext()) {
            this.mSS.addNetworkRegistrationInfo((NetworkRegistrationInfo) it.next());
        }
        this.mSS.setIwlanPreferred(serviceState.isIwlanPreferred());
        logd("updateDataServiceState: defSs = " + serviceState + " imsSs = " + this.mSS);
    }

    boolean isCsRetryException(Throwable th) {
        return th != null && (th instanceof ImsException) && ((ImsException) th).getCode() == 146;
    }

    private Bundle setCsfbBundle(boolean z) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Phone.CS_FALLBACK_SS, z);
        return bundle;
    }

    protected void sendResponseOrRetryOnCsfbSs(SS ss, int i, Throwable th, Object obj) {
        if (!isCsRetryException(th)) {
            sendResponse(ss.mOnComplete, obj, th);
            return;
        }
        Rlog.d(this.LOG_TAG, "Try CSFB: " + i);
        ss.mOnComplete.setData(setCsfbBundle(true));
        if (i == 12) {
            this.mDefaultPhone.setCallForwardingOption(ss.mCfAction, ss.mCfReason, ss.mDialingNumber, ss.mServiceClass, ss.mTimerSeconds, ss.mOnComplete);
            return;
        }
        if (i == 13) {
            this.mDefaultPhone.getCallForwardingOption(ss.mCfReason, ss.mServiceClass, ss.mOnComplete);
            return;
        }
        if (i != 88) {
            switch (i) {
                case 78:
                    this.mDefaultPhone.setCallBarring(ss.mFacility, ss.mLockState, ss.mPassword, ss.mOnComplete, ss.mServiceClass);
                    break;
                case 79:
                    this.mDefaultPhone.getCallBarring(ss.mFacility, ss.mPassword, ss.mOnComplete, ss.mServiceClass);
                    break;
                case 80:
                    this.mDefaultPhone.setCallWaiting(ss.mEnable, ss.mServiceClass, ss.mOnComplete);
                    break;
                case 81:
                    this.mDefaultPhone.getCallWaiting(ss.mOnComplete);
                    break;
                case 82:
                    this.mDefaultPhone.setOutgoingCallerIdDisplay(ss.mClirMode, ss.mOnComplete);
                    break;
                case 83:
                    this.mDefaultPhone.getOutgoingCallerIdDisplay(ss.mOnComplete);
                    break;
            }
            return;
        }
        this.mDefaultPhone.queryCLIP(ss.mOnComplete);
    }

    @Override // com.android.internal.telephony.Phone, android.os.Handler
    public void handleMessage(Message message) {
        Object obj;
        AsyncResult asyncResult = (AsyncResult) message.obj;
        if (asyncResult == null) {
            return;
        }
        Object obj2 = asyncResult.userObj;
        Object objHandleCfQueryResult = null;
        SS ss = obj2 instanceof SS ? (SS) obj2 : null;
        logd("handleMessage what=" + message.what);
        int i = message.what;
        if (i == 12) {
            if (asyncResult.exception == null && ss != null && ss.mCfReason == 0) {
                setVoiceCallForwardingFlag(getIccRecords(), 1, isCfEnable(ss.mCfAction), ss.mDialingNumber);
            }
            if (asyncResult.exception == null && ss != null) {
                MiuiPhoneStub.get(this.mPhoneId).notifyHandoverProvider(this.mContext, ss.mCfAction, ss.mCfReason);
            }
            if (ss != null) {
                sendResponseOrRetryOnCsfbSs(ss, message.what, asyncResult.exception, null);
                return;
            }
            return;
        }
        if (i == 13) {
            objHandleCfQueryResult = asyncResult.exception == null ? handleCfQueryResult((ImsCallForwardInfo[]) asyncResult.result) : null;
            if (ss != null) {
                sendResponseOrRetryOnCsfbSs(ss, message.what, asyncResult.exception, objHandleCfQueryResult);
                return;
            }
            return;
        }
        switch (i) {
            case 78:
            case 80:
                break;
            case 79:
            case 81:
                if (asyncResult.exception == null) {
                    if (i == 79) {
                        objHandleCfQueryResult = handleCbQueryResult((ImsSsInfo[]) asyncResult.result);
                    } else if (i == 81) {
                        objHandleCfQueryResult = handleCwQueryResult((ImsSsInfo[]) asyncResult.result);
                    }
                }
                if (ss != null) {
                    sendResponseOrRetryOnCsfbSs(ss, message.what, asyncResult.exception, objHandleCfQueryResult);
                    return;
                }
                return;
            case 82:
                if (asyncResult.exception == null && ss != null) {
                    saveClirSetting(ss.mClirMode);
                }
                break;
            case 83:
                ImsSsInfo imsSsInfo = (ImsSsInfo) asyncResult.result;
                objHandleCfQueryResult = imsSsInfo != null ? imsSsInfo.getCompatArray(8) : null;
                if (ss != null) {
                    sendResponseOrRetryOnCsfbSs(ss, message.what, asyncResult.exception, objHandleCfQueryResult);
                    return;
                }
                return;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_FLASH /* 84 */:
                logd("EVENT_DEFAULT_PHONE_DATA_STATE_CHANGED");
                updateDataServiceState();
                return;
            case 85:
                updateRoamingState((ServiceState) ((AsyncResult) message.obj).result);
                return;
            case 86:
                logd("Voice call ended. Handle pending updateRoamingState.");
                this.mCT.unregisterForVoiceCallEnded(this);
                ServiceStateTracker serviceStateTracker = getDefaultPhone().getServiceStateTracker();
                if (serviceStateTracker != null) {
                    updateRoamingState(serviceStateTracker.mSS);
                    return;
                }
                return;
            case 87:
                AsyncResult asyncResult2 = (AsyncResult) message.obj;
                if (asyncResult2.exception != null || (obj = asyncResult2.result) == null) {
                    return;
                }
                Phone.SilentRedialParam silentRedialParam = (Phone.SilentRedialParam) obj;
                try {
                    Connection connectionDial = dial(silentRedialParam.dialString, updateDialArgsForVolteSilentRedial(silentRedialParam.dialArgs, silentRedialParam.causeCode));
                    Rlog.d(this.LOG_TAG, "Notify volte redial connection changed cn: " + connectionDial);
                    Phone phone = this.mDefaultPhone;
                    if (phone != null) {
                        phone.notifyRedialConnectionChanged(connectionDial);
                        return;
                    }
                    return;
                } catch (CallStateException e) {
                    Rlog.e(this.LOG_TAG, "volte silent redial failed: " + e);
                    Phone phone2 = this.mDefaultPhone;
                    if (phone2 != null) {
                        phone2.notifyRedialConnectionChanged(null);
                        return;
                    }
                    return;
                }
            case 88:
                Throwable th = asyncResult.exception;
                if (th == null) {
                    Object obj3 = asyncResult.result;
                    if (obj3 instanceof ImsSsInfo) {
                        objHandleCfQueryResult = (ImsSsInfo) obj3;
                    }
                }
                if (ss != null) {
                    sendResponseOrRetryOnCsfbSs(ss, i, th, objHandleCfQueryResult);
                    return;
                }
                return;
            default:
                super.handleMessage(message);
                return;
        }
        if (ss != null) {
            sendResponseOrRetryOnCsfbSs(ss, message.what, asyncResult.exception, null);
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.imsphone.ImsPhone$2, reason: invalid class name */
    class AnonymousClass2 extends ImsEcbmStateListener {
        AnonymousClass2(Executor executor) {
            super(executor);
        }

        public void onECBMEntered(Executor executor) {
            ImsPhone.this.logd("onECBMEntered");
            TelephonyUtils.runWithCleanCallingIdentity(new Runnable() { // from class: com.android.internal.telephony.imsphone.ImsPhone$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onECBMEntered$0();
                }
            }, executor);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onECBMEntered$0() {
            ImsPhone.this.handleEnterEmergencyCallbackMode();
        }

        public void onECBMExited(Executor executor) {
            ImsPhone.this.logd("onECBMExited");
            TelephonyUtils.runWithCleanCallingIdentity(new Runnable() { // from class: com.android.internal.telephony.imsphone.ImsPhone$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onECBMExited$1();
                }
            }, executor);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onECBMExited$1() {
            ImsPhone.this.handleExitEmergencyCallbackMode();
        }
    }

    public ImsEcbmStateListener getImsEcbmStateListener() {
        return this.mImsEcbmStateListener;
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isInEmergencyCall() {
        return this.mCT.isInEmergencyCall();
    }

    protected void sendEmergencyCallbackModeChange() {
        Intent intent = new Intent("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED");
        intent.putExtra("android.telephony.extra.PHONE_IN_ECM_STATE", isInEcm());
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent, getPhoneId());
        this.mContext.sendStickyBroadcastAsUser(intent, UserHandle.ALL);
        logd("sendEmergencyCallbackModeChange: isInEcm=" + isInEcm());
    }

    @Override // com.android.internal.telephony.Phone
    public void exitEmergencyCallbackMode() {
        if (this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }
        logd("exitEmergencyCallbackMode()");
        try {
            this.mCT.getEcbmInterface().exitEmergencyCallbackMode();
        } catch (ImsException e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEnterEmergencyCallbackMode() {
        if (DomainSelectionResolver.getInstance().isDomainSelectionSupported()) {
            logd("DomainSelection enabled: ignore ECBM enter event.");
            return;
        }
        logd("handleEnterEmergencyCallbackMode,mIsPhoneInEcmState= " + isInEcm());
        if (isInImsEcm()) {
            return;
        }
        setIsInEcm(true);
        sendEmergencyCallbackModeChange();
        ((GsmCdmaPhone) this.mDefaultPhone).notifyEmergencyCallRegistrants(true);
        postDelayed(this.mExitEcmRunnable, TelephonyProperties.ecm_exit_timer().orElse(Long.valueOf(DEFAULT_ECM_EXIT_TIMER_VALUE)).longValue());
        this.mWakeLock.acquire();
    }

    @Override // com.android.internal.telephony.Phone
    protected void handleExitEmergencyCallbackMode() {
        if (DomainSelectionResolver.getInstance().isDomainSelectionSupported()) {
            logd("DomainSelection enabled: ignore ECBM exit event.");
            return;
        }
        logd("handleExitEmergencyCallbackMode: mIsPhoneInEcmState = " + isInEcm() + ",isImsEcb= " + isInImsEcm());
        if (isInImsEcm()) {
            setIsInEcm(false);
        }
        removeCallbacks(this.mExitEcmRunnable);
        setEcmCanceledForEmergency(false);
        Registrant registrant = this.mEcmExitRespRegistrant;
        if (registrant != null) {
            registrant.notifyResult(Boolean.TRUE);
        }
        if (this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }
        sendEmergencyCallbackModeChange();
        ((GsmCdmaPhone) this.mDefaultPhone).notifyEmergencyCallRegistrants(false);
    }

    protected void handleTimerInEmergencyCallbackMode(int i) {
        if (DomainSelectionResolver.getInstance().isDomainSelectionSupported()) {
            return;
        }
        if (i == 0) {
            postDelayed(this.mExitEcmRunnable, TelephonyProperties.ecm_exit_timer().orElse(Long.valueOf(DEFAULT_ECM_EXIT_TIMER_VALUE)).longValue());
            ((GsmCdmaPhone) this.mDefaultPhone).notifyEcbmTimerReset(Boolean.FALSE);
            setEcmCanceledForEmergency(false);
            return;
        }
        if (i == 1) {
            removeCallbacks(this.mExitEcmRunnable);
            ((GsmCdmaPhone) this.mDefaultPhone).notifyEcbmTimerReset(Boolean.TRUE);
            setEcmCanceledForEmergency(true);
        } else {
            loge("handleTimerInEmergencyCallbackMode, unsupported action " + i);
        }
    }

    @Override // com.android.internal.telephony.Phone
    public void setOnEcbModeExitResponse(Handler handler, int i, Object obj) {
        this.mEcmExitRespRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.Phone
    public void unsetOnEcbModeExitResponse(Handler handler) {
        this.mEcmExitRespRegistrant.clear();
    }

    public void onFeatureCapabilityChanged() {
        this.mDefaultPhone.getServiceStateTracker().onImsCapabilityChanged();
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isImsCapabilityAvailable(int i, int i2) throws ImsException {
        return this.mCT.isImsCapabilityAvailable(i, i2);
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isVolteEnabled() {
        return isVoiceOverCellularImsEnabled();
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isVoiceOverCellularImsEnabled() {
        return this.mCT.isVoiceOverCellularImsEnabled();
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isWifiCallingEnabled() {
        return this.mCT.isVowifiEnabled();
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isVideoEnabled() {
        return this.mCT.isVideoCallEnabled();
    }

    @Override // com.android.internal.telephony.Phone
    public int getImsRegistrationTech() {
        return this.mCT.getImsRegistrationTech();
    }

    @Override // com.android.internal.telephony.Phone
    public void getImsRegistrationTech(Consumer<Integer> consumer) {
        this.mCT.getImsRegistrationTech(consumer);
    }

    @Override // com.android.internal.telephony.Phone
    public void getImsRegistrationState(Consumer<Integer> consumer) {
        consumer.accept(Integer.valueOf(this.mImsMmTelRegistrationHelper.getImsRegistrationState()));
    }

    @Override // com.android.internal.telephony.Phone
    public Phone getDefaultPhone() {
        return this.mDefaultPhone;
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isImsRegistered() {
        return this.mImsMmTelRegistrationHelper.isImsRegistered();
    }

    public void setImsRegistered(boolean z) {
        this.mImsMmTelRegistrationHelper.updateRegistrationState(z ? 2 : 0);
        MiuiImsControllerStub.get(this.mPhoneId).sendIMSRegistedStateBroadcast(isVolteEnabled(), this.mDefaultPhone);
    }

    @Override // com.android.internal.telephony.Phone
    public void callEndCleanupHandOverCallIfAny() {
        this.mCT.callEndCleanupHandOverCallIfAny();
    }

    public void processDisconnectReason(ImsReasonInfo imsReasonInfo) {
        if (imsReasonInfo.mCode == 1000 && imsReasonInfo.mExtraMessage != null && this.mImsManagerFactory.create(this.mContext, this.mPhoneId).isWfcEnabledByUser()) {
            processWfcDisconnectForNotification(imsReasonInfo);
        }
    }

    private void processWfcDisconnectForNotification(ImsReasonInfo imsReasonInfo) {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        if (carrierConfigManager == null) {
            loge("processDisconnectReason: CarrierConfigManager is not ready");
            return;
        }
        PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(getSubId());
        if (configForSubId == null) {
            loge("processDisconnectReason: no config for subId " + getSubId());
            return;
        }
        String[] stringArray = configForSubId.getStringArray("wfc_operator_error_codes_string_array");
        if (stringArray == null) {
            return;
        }
        String[] stringArray2 = this.mContext.getResources().getStringArray(17236325);
        String[] stringArray3 = this.mContext.getResources().getStringArray(17236326);
        for (int i = 0; i < stringArray.length; i++) {
            String[] strArrSplit = stringArray[i].split("\\|");
            if (strArrSplit.length != 2) {
                loge("Invalid carrier config: " + stringArray[i]);
            } else if (imsReasonInfo.mExtraMessage.startsWith(strArrSplit[0])) {
                int length = strArrSplit[0].length();
                if (!Character.isLetterOrDigit(strArrSplit[0].charAt(length - 1)) || imsReasonInfo.mExtraMessage.length() <= length || !Character.isLetterOrDigit(imsReasonInfo.mExtraMessage.charAt(length))) {
                    CharSequence text = this.mContext.getText(17042515);
                    int i2 = Integer.parseInt(strArrSplit[1]);
                    if (i2 >= 0 && i2 < stringArray2.length && i2 < stringArray3.length) {
                        String str = imsReasonInfo.mExtraMessage;
                        String str2 = !stringArray2[i2].isEmpty() ? String.format(stringArray2[i2], imsReasonInfo.mExtraMessage) : str;
                        if (!stringArray3[i2].isEmpty()) {
                            str = String.format(stringArray3[i2], imsReasonInfo.mExtraMessage);
                        }
                        Intent intent = new Intent("android.telephony.ims.action.WFC_IMS_REGISTRATION_ERROR");
                        intent.putExtra(Phone.EXTRA_KEY_ALERT_TITLE, text);
                        intent.putExtra(Phone.EXTRA_KEY_ALERT_MESSAGE, str2);
                        intent.putExtra(Phone.EXTRA_KEY_NOTIFICATION_MESSAGE, str);
                        intent.putExtra("android:phone_id", getPhoneId());
                        this.mContext.sendOrderedBroadcast(intent, null, this.mResultReceiver, null, -1, null, null);
                        return;
                    }
                    loge("Invalid index: " + stringArray[i]);
                }
            } else {
                continue;
            }
        }
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isOutgoingImsVoiceAllowed() {
        return this.mIsOutgoingImsVoiceAllowed;
    }

    @Override // com.android.internal.telephony.Phone
    public boolean isUtEnabled() {
        return this.mCT.isUtEnabled();
    }

    @Override // com.android.internal.telephony.imsphone.ImsPhoneBase, com.android.internal.telephony.Phone
    public void notifyCallForwardingIndicator() {
        this.mDefaultPhone.notifyCallForwardingIndicator();
    }

    public PowerManager.WakeLock getWakeLock() {
        return this.mWakeLock;
    }

    protected int getSimState(int i) {
        if (((TelephonyManager) getContext().getSystemService(TelephonyManager.class)) == null) {
            loge("getSimState: can't access TelephonyManager");
            return 0;
        }
        return TelephonyManager.getSimStateForSlotIndex(i);
    }

    protected void updateRoamingState(ServiceState serviceState) {
        if (serviceState == null) {
            loge("updateRoamingState: null ServiceState!");
            return;
        }
        boolean roaming = serviceState.getRoaming();
        boolean zIsUsingNonTerrestrialNetwork = serviceState.isUsingNonTerrestrialNetwork();
        if (this.mLastKnownRoamingState == roaming && this.mLastKnownIsUsingNtnState == zIsUsingNonTerrestrialNetwork) {
            return;
        }
        updateLastKnownRoamingState(roaming);
        if ((serviceState.getState() != 0 && serviceState.getDataRegistrationState() != 0) || !this.mDefaultPhone.isRadioOn() || getSimState(this.mPhoneId) != 10) {
            logi("updateRoamingState: we are not IN_SERVICE, ignoring roaming change.");
            return;
        }
        if (this.mCT.getState() == PhoneConstants.State.IDLE) {
            logd("updateRoamingState now: " + roaming);
            CarrierConfigManager carrierConfigManager = (CarrierConfigManager) getContext().getSystemService("carrier_config");
            if (carrierConfigManager == null || !CarrierConfigManager.isConfigForIdentifiedCarrier(carrierConfigManager.getConfigForSubId(getSubId()))) {
                return;
            }
            ImsManager imsManagerCreate = this.mImsManagerFactory.create(this.mContext, this.mPhoneId);
            boolean zShouldOverrideWfcRoamingModeWhileUsingNTN = imsManagerCreate.shouldOverrideWfcRoamingModeWhileUsingNTN();
            logd("updateRoamingState overrideWfcRoamingModeWhileUsingNtn= " + zShouldOverrideWfcRoamingModeWhileUsingNTN);
            if (!zShouldOverrideWfcRoamingModeWhileUsingNTN && !IsWfcModeHomeForDomRoaming(serviceState, imsManagerCreate)) {
                imsManagerCreate.setWfcMode(imsManagerCreate.getWfcMode(roaming), roaming);
            }
            this.mLastKnownRoamingState = roaming;
            this.mLastKnownIsUsingNtnState = zIsUsingNonTerrestrialNetwork;
            return;
        }
        logd("updateRoamingState postponed: " + roaming);
        this.mCT.registerForVoiceCallEnded(this, 86, null);
    }

    public RegistrationManager.RegistrationCallback getImsMmTelRegistrationCallback() {
        return this.mImsMmTelRegistrationHelper.getCallback();
    }

    public RegistrationManager.RegistrationCallback getImsMmTelEmergencyRegistrationCallback() {
        return this.mImsMmTelEmergencyRegistrationHelper.getCallback();
    }

    public void resetImsRegistrationState() {
        logd("resetImsRegistrationState");
        this.mImsMmTelRegistrationHelper.reset();
        this.mImsMmTelEmergencyRegistrationHelper.reset();
        if (SubscriptionManager.isValidSubscriptionId(getSubId())) {
            updateImsRegistrationInfo(0, -1, 0, -1);
        }
    }

    public void processImsUnregistered(ImsReasonInfo imsReasonInfo, int i, int i2, int i3) {
        logd("handleImsUnregistered: onImsMmTelDisconnected imsReasonInfo=" + imsReasonInfo + ", suggestedAction=" + i + ", disconnectedRadioTech=" + i2 + ", throttlingTimeSec=" + i3);
        LocalLog localLog = this.mRegLocalLog;
        StringBuilder sb = new StringBuilder("handleImsUnregistered: onImsMmTelDisconnected imsRadioTech=");
        sb.append(imsReasonInfo);
        localLog.log(sb.toString());
        if (setIsOutgoingImsVoiceAllowed(imsReasonInfo)) {
            return;
        }
        setServiceState(1);
        processDisconnectReason(imsReasonInfo);
        getDefaultPhone().setImsRegistrationState(false);
        this.mImsStats.onImsUnregistered(imsReasonInfo);
        this.mImsNrSaModeHandler.onImsUnregistered(i2);
        this.mSubscriptionManagerService.clearImsNumberUpdateStatus(getSubId());
        this.mCurrentSubscriberUris = null;
        this.mImsRegistrationTech = -1;
        updateImsRegistrationInfo(0, i2, (imsReasonInfo.getCode() == 1000 && (i == 1 || i == 2 || i == 3 || i == 4)) ? i : 0, -1, i3);
        this.mImsRegistrationUpdateRegistrants.notifyRegistrants(new AsyncResult((Object) null, new ImsRegistrationRadioTechInfo(this.mPhoneId, -1, 0), (Throwable) null));
    }

    private boolean setIsOutgoingImsVoiceAllowed(ImsReasonInfo imsReasonInfo) {
        int extraCode = imsReasonInfo.getExtraCode();
        if (extraCode != 4001 && extraCode != 4002) {
            return false;
        }
        this.mIsOutgoingImsVoiceAllowed = extraCode == 4001;
        logi("mIsOutgoingImsVoiceAllowed= " + this.mIsOutgoingImsVoiceAllowed);
        return true;
    }

    public void clearPhoneNumberForSourceIms() {
        int subId = getSubId();
        if (SubscriptionManager.isValidSubscriptionId(subId)) {
            logd("clearPhoneNumberForSourceIms");
            this.mSubscriptionManagerService.setNumberFromIms(subId, new String(""));
        }
    }

    public void setPhoneNumberForSourceIms(Uri[] uriArr) {
        int subId = getSubId();
        if (SubscriptionManager.isValidSubscriptionId(subId)) {
            this.mSubscriptionManagerService.clearImsNumberUpdateStatus(getSubId());
            SubscriptionInfoInternal subscriptionInfoInternal = this.mSubscriptionManagerService.getSubscriptionInfoInternal(subId);
            if (subscriptionInfoInternal == null) {
                loge("trigger setPhoneNumberForSourceIms, but subInfo is null");
                return;
            }
            String countryIso = subscriptionInfoInternal.getCountryIso();
            PhoneNumberManager phoneNumberManager = getPhoneNumberManager();
            if (uriArr == null) {
                loge("setPhoneNumberForSourceIms: input is null");
                return;
            }
            if (phoneNumberManager != null) {
                try {
                    ParsedPhoneNumber phoneNumber = phoneNumberManager.parsePhoneNumber(Arrays.asList(uriArr), countryIso);
                    if (phoneNumber == null) {
                        loge("setPhoneNumberForSourceIms: PhoneNumberManager returned a null");
                        return;
                    }
                    if (phoneNumber.isValidPhoneNumber()) {
                        this.mSubscriptionManagerService.setNumberFromIms(subId, phoneNumber.getParsedPhoneNumber());
                        this.mSubscriptionManagerService.setImsNumberUpdateStatus(subId, true);
                        logd("setPhoneNumberForSourceIms: update IMS phone number");
                        return;
                    } else {
                        loge("setPhoneNumberForSourceIms: PhoneNumberManager return error " + phoneNumber.getErrorCode());
                        return;
                    }
                } catch (IllegalArgumentException e) {
                    loge("setPhoneNumberForSourceIms: failed to parse phone number, " + e);
                    return;
                }
            }
            logi("setPhoneNumberForSourceIms: can't access PhoneNumberManager");
        }
    }

    private PhoneNumberManager getPhoneNumberManager() {
        if (this.mPhoneNumberManager == null) {
            try {
                this.mPhoneNumberManager = (PhoneNumberManager) this.mContext.getSystemService(PhoneNumberManager.class);
            } catch (UnsupportedOperationException unused) {
                logd("getPhoneNumberManager: can't access PhoneNumberManager");
                this.mPhoneNumberManager = null;
            }
        }
        return this.mPhoneNumberManager;
    }

    public void setPhoneNumberManager(PhoneNumberManager phoneNumberManager) {
        this.mPhoneNumberManager = phoneNumberManager;
    }

    @Override // com.android.internal.telephony.Phone
    public IccRecords getIccRecords() {
        return this.mDefaultPhone.getIccRecords();
    }

    public PhoneInternalInterface.DialArgs updateDialArgsForVolteSilentRedial(PhoneInternalInterface.DialArgs dialArgs, int i) {
        if (dialArgs != null) {
            ImsDialArgs.Builder builderFrom = ImsDialArgs.Builder.from(dialArgs);
            Bundle bundle = new Bundle(dialArgs.intentExtras);
            if (i == 3002) {
                bundle.putString("CallRadioTech", String.valueOf(18));
                logd("trigger VoWifi emergency call");
                builderFrom.setIntentExtras(bundle);
            } else if (i == 3001) {
                logd("trigger VoLte emergency call");
            }
            return builderFrom.build();
        }
        return new PhoneInternalInterface.DialArgs.Builder().build();
    }

    @Override // com.android.internal.telephony.Phone
    public VoiceCallSessionStats getVoiceCallSessionStats() {
        return this.mDefaultPhone.getVoiceCallSessionStats();
    }

    public ImsStats getImsStats() {
        return this.mImsStats;
    }

    public int getTransportType() {
        return this.mTransportType;
    }

    public void setImsStats(ImsStats imsStats) {
        this.mImsStats = imsStats;
    }

    public boolean hasAliveCall() {
        Call.State state = getForegroundCall().getState();
        Call.State state2 = Call.State.IDLE;
        return (state == state2 && getBackgroundCall().getState() == state2) ? false : true;
    }

    public boolean getLastKnownRoamingState() {
        return this.mLastKnownRoamingState;
    }

    public void updateImsRegistrationInfo(int i) {
        int i2 = this.mImsRegistrationState;
        if (i2 == 2) {
            if (this.mNotifiedRegisteredState && i == this.mImsRegistrationCapabilities) {
                return;
            }
            this.mImsRegistrationCapabilities = i;
            if (i == 0) {
                return;
            }
            this.mDefaultPhone.mCi.updateImsRegistrationInfo(i2, this.mImsRegistrationTech, 0, i, 0, null);
            this.mNotifiedRegisteredState = true;
            this.mImsNrSaModeHandler.updateImsCapability(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateImsRegistrationInfo(int i, int i2, int i3, int i4) {
        updateImsRegistrationInfo(i, i2, i3, i4, 0);
    }

    private void updateImsRegistrationInfo(int i, int i2, int i3, int i4, int i5) {
        int i6;
        int i7 = this.mImsRegistrationState;
        if (i == i7) {
            if (i == 2 && i2 == this.mImsRegistrationTech) {
                return;
            }
            if (i == 0 && i3 == 0 && this.mImsRegistrationSuggestedAction == 0 && i2 == this.mImsDeregistrationTech) {
                return;
            }
        }
        if (i == 0) {
            this.mDefaultPhone.mCi.updateImsRegistrationInfo(i, i2, i3, 0, i5, null);
        } else if (i7 == 2 && (i6 = this.mImsRegistrationCapabilities) > 0) {
            this.mDefaultPhone.mCi.updateImsRegistrationInfo(i, i2, 0, i6, i5, null);
            this.mImsRegistrationTech = i2;
            this.mTransportType = i4;
            this.mNotifiedRegisteredState = true;
            return;
        }
        this.mImsRegistrationState = i;
        this.mImsRegistrationTech = i2;
        this.mTransportType = i4;
        this.mImsRegistrationSuggestedAction = i3;
        if (i == 0) {
            this.mImsDeregistrationTech = i2;
        } else {
            this.mImsDeregistrationTech = -1;
        }
        this.mImsRegistrationCapabilities = 0;
        this.mNotifiedRegisteredState = false;
    }

    @Override // com.android.internal.telephony.Phone
    public void setTerminalBasedCallWaitingStatus(int i) {
        this.mCT.setTerminalBasedCallWaitingStatus(i);
    }

    @Override // com.android.internal.telephony.Phone
    public void triggerEpsFallback(int i, Message message) {
        this.mDefaultPhone.triggerEpsFallback(i, message);
    }

    @Override // com.android.internal.telephony.Phone
    public void startImsTraffic(int i, int i2, int i3, int i4, Message message) {
        this.mDefaultPhone.startImsTraffic(i, i2, i3, i4, message);
    }

    @Override // com.android.internal.telephony.Phone
    public void stopImsTraffic(int i, Message message) {
        this.mDefaultPhone.stopImsTraffic(i, message);
    }

    @Override // com.android.internal.telephony.Phone
    public void registerForConnectionSetupFailure(Handler handler, int i, Object obj) {
        this.mDefaultPhone.registerForConnectionSetupFailure(handler, i, obj);
    }

    @Override // com.android.internal.telephony.Phone
    public void unregisterForConnectionSetupFailure(Handler handler) {
        this.mDefaultPhone.unregisterForConnectionSetupFailure(handler);
    }

    @Override // com.android.internal.telephony.Phone
    public void triggerImsDeregistration(int i) {
        this.mCT.triggerImsDeregistration(i);
    }

    @Override // com.android.internal.telephony.Phone
    public void updateImsCallStatus(List<ImsCallInfo> list, Message message) {
        this.mDefaultPhone.updateImsCallStatus(list, message);
    }

    @Override // com.android.internal.telephony.Phone
    public void triggerNotifyAnbr(int i, int i2, int i3) {
        this.mCT.triggerNotifyAnbr(i, i2, i3);
    }

    public boolean canMakeWifiCall() {
        return isImsRegistered() && getImsRegistrationTech() == 1;
    }

    public void setAllowedImsServices(int i, boolean z, boolean z2) {
        Phone phone = this.mDefaultPhone;
        if (z2) {
            phone.setAllowedImsServicesForHomeOnly(i, z);
        } else {
            phone.setAllowedImsServicesForAny(i, z);
        }
    }

    @Override // com.android.internal.telephony.Phone
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("ImsPhone extends:");
        super.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.flush();
        indentingPrintWriter.println("ImsPhone:");
        indentingPrintWriter.println("  mDefaultPhone = " + this.mDefaultPhone);
        indentingPrintWriter.println("  mPendingMMIs = " + this.mPendingMMIs);
        indentingPrintWriter.println("  mPostDialHandler = " + this.mPostDialHandler);
        indentingPrintWriter.println("  mSS = " + this.mSS);
        indentingPrintWriter.println("  mWakeLock = " + this.mWakeLock);
        indentingPrintWriter.println("  mIsPhoneInEcmState = " + isInEcm());
        indentingPrintWriter.println("  mEcmExitRespRegistrant = " + this.mEcmExitRespRegistrant);
        indentingPrintWriter.println("  mSilentRedialRegistrants = " + this.mSilentRedialRegistrants);
        indentingPrintWriter.println("  mImsMmTelRegistrationState = " + this.mImsMmTelRegistrationHelper.getImsRegistrationState());
        indentingPrintWriter.println("  mImsMmTelEmergencyRegistrationState = " + this.mImsMmTelEmergencyRegistrationHelper.getImsRegistrationState());
        indentingPrintWriter.println("  mLastKnownRoamingState = " + this.mLastKnownRoamingState);
        indentingPrintWriter.println(" mLastKnownIsUsingNtn = " + this.mLastKnownIsUsingNtnState);
        indentingPrintWriter.println("  mSsnRegistrants = " + this.mSsnRegistrants);
        indentingPrintWriter.println(" Registration Log:");
        indentingPrintWriter.increaseIndent();
        this.mRegLocalLog.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.flush();
    }

    protected void logi(String str) {
        Rlog.i(this.LOG_TAG, "[" + this.mPhoneId + "] " + str);
    }

    protected void logv(String str) {
        Rlog.v(this.LOG_TAG, "[" + this.mPhoneId + "] " + str);
    }

    protected void logd(String str) {
        Rlog.d(this.LOG_TAG, "[" + this.mPhoneId + "] " + str);
    }

    protected void loge(String str) {
        Rlog.e(this.LOG_TAG, "[" + this.mPhoneId + "] " + str);
    }
}
