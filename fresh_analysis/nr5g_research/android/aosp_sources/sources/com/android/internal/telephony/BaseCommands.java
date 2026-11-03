package com.android.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.telephony.BarringInfo;
import android.telephony.emergency.EmergencyNumber;
import com.android.internal.telephony.uicc.SimPhonebookRecord;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseCommands implements CommandsInterface {
    protected int mAllowedNetworkTypesBitmask;
    protected Registrant mCatCallSetUpRegistrant;
    protected Registrant mCatCcAlphaRegistrant;
    protected Registrant mCatEventRegistrant;
    protected Registrant mCatProCmdRegistrant;
    protected Registrant mCatSessionEndRegistrant;
    protected Registrant mCdmaSmsRegistrant;
    protected Context mContext;
    protected Registrant mEmergencyCallbackModeRegistrant;
    protected Registrant mGsmBroadcastSmsRegistrant;
    protected Registrant mGsmSmsRegistrant;
    protected Registrant mIccSmsFullRegistrant;
    protected Registrant mNITZTimeRegistrant;
    protected int mPhoneType;
    protected Registrant mRegistrationFailedRegistrant;
    protected Registrant mRestrictedStateRegistrant;
    protected Registrant mRingRegistrant;
    protected Registrant mSignalStrengthRegistrant;
    protected Registrant mSmsOnSimRegistrant;
    protected Registrant mSmsStatusRegistrant;
    protected Registrant mSsRegistrant;
    protected Registrant mSsnRegistrant;
    protected Registrant mUSSDRegistrant;
    protected int mState = 2;
    protected Object mStateMonitor = new Object();
    protected RegistrantList mRadioStateChangedRegistrants = new RegistrantList();
    protected RegistrantList mOnRegistrants = new RegistrantList();
    protected RegistrantList mAvailRegistrants = new RegistrantList();
    protected RegistrantList mOffOrNotAvailRegistrants = new RegistrantList();
    protected RegistrantList mNotAvailRegistrants = new RegistrantList();
    protected RegistrantList mCallStateRegistrants = new RegistrantList();
    protected RegistrantList mNetworkStateRegistrants = new RegistrantList();
    protected RegistrantList mDataCallListChangedRegistrants = new RegistrantList();
    protected RegistrantList mDataCallListUpdatedRegistrants = new RegistrantList();
    protected RegistrantList mApnUnthrottledRegistrants = new RegistrantList();
    protected RegistrantList mSlicingConfigChangedRegistrants = new RegistrantList();
    protected RegistrantList mVoiceRadioTechChangedRegistrants = new RegistrantList();
    protected RegistrantList mImsNetworkStateChangedRegistrants = new RegistrantList();
    protected RegistrantList mIccStatusChangedRegistrants = new RegistrantList();
    protected RegistrantList mVoicePrivacyOnRegistrants = new RegistrantList();
    protected RegistrantList mVoicePrivacyOffRegistrants = new RegistrantList();
    protected RegistrantList mDisplayInfoRegistrants = new RegistrantList();
    protected RegistrantList mSignalInfoRegistrants = new RegistrantList();
    protected RegistrantList mRingbackToneRegistrants = new RegistrantList();
    protected RegistrantList mResendIncallMuteRegistrants = new RegistrantList();
    protected RegistrantList mExitEmergencyCallbackModeRegistrants = new RegistrantList();
    protected RegistrantList mRilConnectedRegistrants = new RegistrantList();
    protected RegistrantList mIccRefreshRegistrants = new RegistrantList();
    protected RegistrantList mRilCellInfoListRegistrants = new RegistrantList();
    protected RegistrantList mSubscriptionStatusRegistrants = new RegistrantList();
    protected RegistrantList mSrvccStateRegistrants = new RegistrantList();
    protected RegistrantList mHardwareConfigChangeRegistrants = new RegistrantList();
    protected RegistrantList mPhoneRadioCapabilityChangedRegistrants = new RegistrantList();
    protected RegistrantList mPcoDataRegistrants = new RegistrantList();
    protected RegistrantList mCarrierInfoForImsiEncryptionRegistrants = new RegistrantList();
    public RegistrantList mRilNetworkScanResultRegistrants = new RegistrantList();
    protected RegistrantList mModemResetRegistrants = new RegistrantList();
    protected RegistrantList mNattKeepaliveStatusRegistrants = new RegistrantList();
    protected RegistrantList mPhysicalChannelConfigurationRegistrants = new RegistrantList();
    protected RegistrantList mLceInfoRegistrants = new RegistrantList();
    protected RegistrantList mEmergencyNumberListRegistrants = new RegistrantList();
    protected RegistrantList mUiccApplicationsEnablementRegistrants = new RegistrantList();
    protected RegistrantList mBarringInfoChangedRegistrants = new RegistrantList();
    protected RegistrantList mSimPhonebookChangedRegistrants = new RegistrantList();
    protected RegistrantList mSimPhonebookRecordsReceivedRegistrants = new RegistrantList();
    protected RegistrantList mEmergencyNetworkScanRegistrants = new RegistrantList();
    protected RegistrantList mConnectionSetupFailureRegistrants = new RegistrantList();
    protected RegistrantList mNotifyAnbrRegistrants = new RegistrantList();
    protected RegistrantList mTriggerImsDeregistrationRegistrants = new RegistrantList();
    protected RegistrantList mImeiInfoRegistrants = new RegistrantList();
    protected RegistrantList mCellularIdentifierDisclosedRegistrants = new RegistrantList();
    protected RegistrantList mSecurityAlgorithmUpdatedRegistrants = new RegistrantList();
    protected RegistrantList mDisplayNetworkTypeChangedRegistrants = new RegistrantList();
    protected RegistrantList mPrioritizedScanModeChangedRegistrants = new RegistrantList();
    protected RegistrantList mNetworkSecurityEventsRegistrants = new RegistrantList();
    private Object mLastEmergencyNumberListIndicationLock = new Object();
    private final List mLastEmergencyNumberListIndication = new ArrayList();
    protected BarringInfo mLastBarringInfo = new BarringInfo();
    protected int mRilVersion = -1;

    @Override // com.android.internal.telephony.CommandsInterface
    public void getRadioCapability(Message message) {
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void getSimPhonebookCapacity(Message message) {
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void getSimPhonebookRecords(Message message) {
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void requestShutdown(Message message) {
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setDataAllowed(boolean z, Message message) {
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setRadioCapability(RadioCapability radioCapability, Message message) {
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setUiccSubscription(int i, int i2, int i3, int i4, Message message) {
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void testingEmergencyCall() {
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void updateSimPhonebookRecord(SimPhonebookRecord simPhonebookRecord, Message message) {
    }

    public BaseCommands(Context context) {
        this.mContext = context;
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public int getRadioState() {
        return this.mState;
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForRadioStateChanged(Handler handler, int i, Object obj) {
        synchronized (this.mStateMonitor) {
            this.mRadioStateChangedRegistrants.addUnique(handler, i, obj);
            Message.obtain(handler, i, new AsyncResult(obj, (Object) null, (Throwable) null)).sendToTarget();
        }
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForRadioStateChanged(Handler handler) {
        synchronized (this.mStateMonitor) {
            this.mRadioStateChangedRegistrants.remove(handler);
        }
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForImsNetworkStateChanged(Handler handler, int i, Object obj) {
        this.mImsNetworkStateChangedRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForImsNetworkStateChanged(Handler handler) {
        this.mImsNetworkStateChangedRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForOn(Handler handler, int i, Object obj) {
        synchronized (this.mStateMonitor) {
            try {
                this.mOnRegistrants.addUnique(handler, i, obj);
                if (this.mState == 1) {
                    Message.obtain(handler, i, new AsyncResult(obj, (Object) null, (Throwable) null)).sendToTarget();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForOn(Handler handler) {
        synchronized (this.mStateMonitor) {
            this.mOnRegistrants.remove(handler);
        }
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForAvailable(Handler handler, int i, Object obj) {
        synchronized (this.mStateMonitor) {
            try {
                this.mAvailRegistrants.addUnique(handler, i, obj);
                if (this.mState != 2) {
                    Message.obtain(handler, i, new AsyncResult(obj, (Object) null, (Throwable) null)).sendToTarget();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForAvailable(Handler handler) {
        synchronized (this.mStateMonitor) {
            this.mAvailRegistrants.remove(handler);
        }
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForNotAvailable(Handler handler, int i, Object obj) {
        synchronized (this.mStateMonitor) {
            try {
                this.mNotAvailRegistrants.addUnique(handler, i, obj);
                if (this.mState == 2) {
                    Message.obtain(handler, i, new AsyncResult(obj, (Object) null, (Throwable) null)).sendToTarget();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForNotAvailable(Handler handler) {
        synchronized (this.mStateMonitor) {
            this.mNotAvailRegistrants.remove(handler);
        }
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForOffOrNotAvailable(Handler handler, int i, Object obj) {
        synchronized (this.mStateMonitor) {
            this.mOffOrNotAvailRegistrants.addUnique(handler, i, obj);
            int i2 = this.mState;
            if (i2 == 0 || i2 == 2) {
                Message.obtain(handler, i, new AsyncResult(obj, (Object) null, (Throwable) null)).sendToTarget();
            }
        }
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForOffOrNotAvailable(Handler handler) {
        synchronized (this.mStateMonitor) {
            this.mOffOrNotAvailRegistrants.remove(handler);
        }
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForCallStateChanged(Handler handler, int i, Object obj) {
        this.mCallStateRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForNetworkStateChanged(Handler handler, int i, Object obj) {
        this.mNetworkStateRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForNetworkStateChanged(Handler handler) {
        this.mNetworkStateRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForDataCallListChanged(Handler handler, int i, Object obj) {
        this.mDataCallListChangedRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForDataCallListChanged(Handler handler) {
        this.mDataCallListChangedRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForDataCallListUpdated(Handler handler, int i, Object obj) {
        this.mDataCallListUpdatedRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForDataCallListUpdated(Handler handler) {
        this.mDataCallListUpdatedRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForApnUnthrottled(Handler handler, int i, Object obj) {
        this.mApnUnthrottledRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForSlicingConfigChanged(Handler handler, int i, Object obj) {
        this.mSlicingConfigChangedRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForVoiceRadioTechChanged(Handler handler, int i, Object obj) {
        this.mVoiceRadioTechChangedRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForIccStatusChanged(Handler handler, int i, Object obj) {
        this.mIccStatusChangedRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnNewGsmSms(Handler handler, int i, Object obj) {
        this.mGsmSmsRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unSetOnNewGsmSms(Handler handler) {
        Registrant registrant = this.mGsmSmsRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mGsmSmsRegistrant.clear();
        this.mGsmSmsRegistrant = null;
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnNewCdmaSms(Handler handler, int i, Object obj) {
        this.mCdmaSmsRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unSetOnNewCdmaSms(Handler handler) {
        Registrant registrant = this.mCdmaSmsRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mCdmaSmsRegistrant.clear();
        this.mCdmaSmsRegistrant = null;
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnNewGsmBroadcastSms(Handler handler, int i, Object obj) {
        this.mGsmBroadcastSmsRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unSetOnNewGsmBroadcastSms(Handler handler) {
        Registrant registrant = this.mGsmBroadcastSmsRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mGsmBroadcastSmsRegistrant.clear();
        this.mGsmBroadcastSmsRegistrant = null;
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnSmsOnSim(Handler handler, int i, Object obj) {
        this.mSmsOnSimRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unSetOnSmsOnSim(Handler handler) {
        Registrant registrant = this.mSmsOnSimRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mSmsOnSimRegistrant.clear();
        this.mSmsOnSimRegistrant = null;
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnSmsStatus(Handler handler, int i, Object obj) {
        this.mSmsStatusRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unSetOnSmsStatus(Handler handler) {
        Registrant registrant = this.mSmsStatusRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mSmsStatusRegistrant.clear();
        this.mSmsStatusRegistrant = null;
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnSignalStrengthUpdate(Handler handler, int i, Object obj) {
        this.mSignalStrengthRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unSetOnSignalStrengthUpdate(Handler handler) {
        Registrant registrant = this.mSignalStrengthRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mSignalStrengthRegistrant.clear();
        this.mSignalStrengthRegistrant = null;
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnNITZTime(Handler handler, int i, Object obj) {
        this.mNITZTimeRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unSetOnNITZTime(Handler handler) {
        Registrant registrant = this.mNITZTimeRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mNITZTimeRegistrant.clear();
        this.mNITZTimeRegistrant = null;
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnUSSD(Handler handler, int i, Object obj) {
        this.mUSSDRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnSuppServiceNotification(Handler handler, int i, Object obj) {
        this.mSsnRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnCatSessionEnd(Handler handler, int i, Object obj) {
        this.mCatSessionEndRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unSetOnCatSessionEnd(Handler handler) {
        Registrant registrant = this.mCatSessionEndRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mCatSessionEndRegistrant.clear();
        this.mCatSessionEndRegistrant = null;
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnCatProactiveCmd(Handler handler, int i, Object obj) {
        this.mCatProCmdRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unSetOnCatProactiveCmd(Handler handler) {
        Registrant registrant = this.mCatProCmdRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mCatProCmdRegistrant.clear();
        this.mCatProCmdRegistrant = null;
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnCatEvent(Handler handler, int i, Object obj) {
        this.mCatEventRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unSetOnCatEvent(Handler handler) {
        Registrant registrant = this.mCatEventRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mCatEventRegistrant.clear();
        this.mCatEventRegistrant = null;
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnCatCallSetUp(Handler handler, int i, Object obj) {
        this.mCatCallSetUpRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unSetOnCatCallSetUp(Handler handler) {
        Registrant registrant = this.mCatCallSetUpRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mCatCallSetUpRegistrant.clear();
        this.mCatCallSetUpRegistrant = null;
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnIccSmsFull(Handler handler, int i, Object obj) {
        this.mIccSmsFullRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unSetOnIccSmsFull(Handler handler) {
        Registrant registrant = this.mIccSmsFullRegistrant;
        if (registrant == null || registrant.getHandler() != handler) {
            return;
        }
        this.mIccSmsFullRegistrant.clear();
        this.mIccSmsFullRegistrant = null;
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForIccRefresh(Handler handler, int i, Object obj) {
        this.mIccRefreshRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setEmergencyCallbackMode(Handler handler, int i, Object obj) {
        this.mEmergencyCallbackModeRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForIccRefresh(Handler handler) {
        this.mIccRefreshRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnCallRing(Handler handler, int i, Object obj) {
        this.mRingRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnSs(Handler handler, int i, Object obj) {
        this.mSsRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnCatCcAlphaNotify(Handler handler, int i, Object obj) {
        this.mCatCcAlphaRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unSetOnCatCcAlphaNotify(Handler handler) {
        this.mCatCcAlphaRegistrant.clear();
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnRegistrationFailed(Handler handler, int i, Object obj) {
        this.mRegistrationFailedRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForInCallVoicePrivacyOn(Handler handler, int i, Object obj) {
        this.mVoicePrivacyOnRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForInCallVoicePrivacyOn(Handler handler) {
        this.mVoicePrivacyOnRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForInCallVoicePrivacyOff(Handler handler, int i, Object obj) {
        this.mVoicePrivacyOffRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForInCallVoicePrivacyOff(Handler handler) {
        this.mVoicePrivacyOffRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void setOnRestrictedStateChanged(Handler handler, int i, Object obj) {
        this.mRestrictedStateRegistrant = new Registrant(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForDisplayInfo(Handler handler, int i, Object obj) {
        this.mDisplayInfoRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForDisplayInfo(Handler handler) {
        this.mDisplayInfoRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForSignalInfo(Handler handler, int i, Object obj) {
        this.mSignalInfoRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForSignalInfo(Handler handler) {
        this.mSignalInfoRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForRingbackTone(Handler handler, int i, Object obj) {
        this.mRingbackToneRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForRingbackTone(Handler handler) {
        this.mRingbackToneRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForResendIncallMute(Handler handler, int i, Object obj) {
        this.mResendIncallMuteRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForResendIncallMute(Handler handler) {
        this.mResendIncallMuteRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForExitEmergencyCallbackMode(Handler handler, int i, Object obj) {
        this.mExitEmergencyCallbackModeRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForHardwareConfigChanged(Handler handler, int i, Object obj) {
        this.mHardwareConfigChangeRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForHardwareConfigChanged(Handler handler) {
        this.mHardwareConfigChangeRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForNetworkScanResult(Handler handler, int i, Object obj) {
        this.mRilNetworkScanResultRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForNetworkScanResult(Handler handler) {
        this.mRilNetworkScanResultRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForRilConnected(Handler handler, int i, Object obj) {
        this.mRilConnectedRegistrants.addUnique(handler, i, obj);
        if (this.mRilVersion != -1) {
            Message.obtain(handler, i, new AsyncResult(obj, new Integer(this.mRilVersion), (Throwable) null)).sendToTarget();
        }
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForRilConnected(Handler handler) {
        this.mRilConnectedRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForEmergencyNumberList(Handler handler, int i, Object obj) {
        this.mEmergencyNumberListRegistrants.addUnique(handler, i, obj);
        List lastEmergencyNumberListIndication = getLastEmergencyNumberListIndication();
        if (lastEmergencyNumberListIndication != null) {
            this.mEmergencyNumberListRegistrants.notifyRegistrants(new AsyncResult((Object) null, lastEmergencyNumberListIndication, (Throwable) null));
        }
    }

    protected void setRadioState(int i, boolean z) {
        synchronized (this.mStateMonitor) {
            try {
                int i2 = this.mState;
                this.mState = i;
                if (i2 != i || z) {
                    this.mRadioStateChangedRegistrants.notifyRegistrants();
                    if (this.mState != 2 && i2 == 2) {
                        this.mAvailRegistrants.notifyRegistrants();
                    }
                    if (this.mState == 2 && i2 != 2) {
                        this.mNotAvailRegistrants.notifyRegistrants();
                    }
                    if (this.mState == 1 && i2 != 1) {
                        this.mOnRegistrants.notifyRegistrants();
                    }
                    int i3 = this.mState;
                    if ((i3 == 0 || i3 == 2) && i2 == 1) {
                        this.mOffOrNotAvailRegistrants.notifyRegistrants();
                        this.mLastBarringInfo = new BarringInfo();
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    protected void cacheEmergencyNumberListIndication(List<EmergencyNumber> list) {
        synchronized (this.mLastEmergencyNumberListIndicationLock) {
            this.mLastEmergencyNumberListIndication.clear();
            this.mLastEmergencyNumberListIndication.addAll(list);
        }
    }

    private List getLastEmergencyNumberListIndication() {
        ArrayList arrayList;
        synchronized (this.mLastEmergencyNumberListIndicationLock) {
            arrayList = new ArrayList(this.mLastEmergencyNumberListIndication);
        }
        return arrayList;
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public BarringInfo getLastBarringInfo() {
        return this.mLastBarringInfo;
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForCellInfoList(Handler handler, int i, Object obj) {
        this.mRilCellInfoListRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForCellInfoList(Handler handler) {
        this.mRilCellInfoListRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForPhysicalChannelConfiguration(Handler handler, int i, Object obj) {
        this.mPhysicalChannelConfigurationRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForPhysicalChannelConfiguration(Handler handler) {
        this.mPhysicalChannelConfigurationRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForSrvccStateChanged(Handler handler, int i, Object obj) {
        this.mSrvccStateRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForDisplayNetworkTypeChanged(Handler handler) {
        this.mDisplayNetworkTypeChangedRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForDisplayNetworkTypeChanged(Handler handler, int i, Object obj) {
        this.mDisplayNetworkTypeChangedRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForPrioritizedScanModeChanged(Handler handler) {
        this.mPrioritizedScanModeChangedRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForPrioritizedScanModeChanged(Handler handler, int i, Object obj) {
        this.mPrioritizedScanModeChangedRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public int getRilVersion() {
        return this.mRilVersion;
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForRadioCapabilityChanged(Handler handler, int i, Object obj) {
        this.mPhoneRadioCapabilityChangedRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForRadioCapabilityChanged(Handler handler) {
        this.mPhoneRadioCapabilityChangedRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForLceInfo(Handler handler, int i, Object obj) {
        synchronized (this.mStateMonitor) {
            this.mLceInfoRegistrants.addUnique(handler, i, obj);
        }
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForLceInfo(Handler handler) {
        synchronized (this.mStateMonitor) {
            this.mLceInfoRegistrants.remove(handler);
        }
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForModemReset(Handler handler, int i, Object obj) {
        this.mModemResetRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForModemReset(Handler handler) {
        this.mModemResetRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForPcoData(Handler handler, int i, Object obj) {
        this.mPcoDataRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForPcoData(Handler handler) {
        this.mPcoDataRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForCarrierInfoForImsiEncryption(Handler handler, int i, Object obj) {
        this.mCarrierInfoForImsiEncryptionRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForNattKeepaliveStatus(Handler handler, int i, Object obj) {
        synchronized (this.mStateMonitor) {
            this.mNattKeepaliveStatusRegistrants.addUnique(handler, i, obj);
        }
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForNattKeepaliveStatus(Handler handler) {
        synchronized (this.mStateMonitor) {
            this.mNattKeepaliveStatusRegistrants.remove(handler);
        }
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerUiccApplicationEnablementChanged(Handler handler, int i, Object obj) {
        this.mUiccApplicationsEnablementRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForBarringInfoChanged(Handler handler, int i, Object obj) {
        this.mBarringInfoChangedRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForBarringInfoChanged(Handler handler) {
        this.mBarringInfoChangedRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForSimPhonebookChanged(Handler handler, int i, Object obj) {
        this.mSimPhonebookChangedRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForSimPhonebookChanged(Handler handler) {
        this.mSimPhonebookChangedRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForSimPhonebookRecordsReceived(Handler handler, int i, Object obj) {
        this.mSimPhonebookRecordsReceivedRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForSimPhonebookRecordsReceived(Handler handler) {
        this.mSimPhonebookRecordsReceivedRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForEmergencyNetworkScan(Handler handler, int i, Object obj) {
        this.mEmergencyNetworkScanRegistrants.add(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForEmergencyNetworkScan(Handler handler) {
        this.mEmergencyNetworkScanRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForConnectionSetupFailure(Handler handler, int i, Object obj) {
        this.mConnectionSetupFailureRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void unregisterForConnectionSetupFailure(Handler handler) {
        this.mConnectionSetupFailureRegistrants.remove(handler);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForNotifyAnbr(Handler handler, int i, Object obj) {
        this.mNotifyAnbrRegistrants.addUnique(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForTriggerImsDeregistration(Handler handler, int i, Object obj) {
        this.mTriggerImsDeregistrationRegistrants.add(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForImeiMappingChanged(Handler handler, int i, Object obj) {
        this.mImeiInfoRegistrants.add(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForCellularIdentifierDisclosures(Handler handler, int i, Object obj) {
        this.mCellularIdentifierDisclosedRegistrants.add(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForSecurityAlgorithmUpdates(Handler handler, int i, Object obj) {
        this.mSecurityAlgorithmUpdatedRegistrants.add(handler, i, obj);
    }

    @Override // com.android.internal.telephony.CommandsInterface
    public void registerForNetworkSecurityEvents(Handler handler, int i, Object obj) {
        this.mNetworkSecurityEventsRegistrants.add(handler, i, obj);
    }
}
