package com.android.internal.telephony;

import android.app.ActivityThread;
import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class CallManager {
    static final int EVENT_CALL_WAITING = 108;
    static final int EVENT_PRECISE_CALL_STATE_CHANGED = 101;
    static final int EVENT_RINGBACK_TONE = 105;
    private static CallManager INSTANCE;
    private final ArrayList<Call> mBackgroundCalls;
    private Phone mDefaultPhone;
    private final ArrayList<Call> mForegroundCalls;
    private final ArrayList<Phone> mPhones;
    private final ArrayList<Call> mRingingCalls;
    private TelephonyManager mTelephonyManager;
    private final HashMap mHandlerMap = new HashMap();
    private Object mRegistrantidentifier = new Object();
    protected final RegistrantList mPreciseCallStateRegistrants = new RegistrantList();
    protected final RegistrantList mNewRingingConnectionRegistrants = new RegistrantList();
    protected final RegistrantList mIncomingRingRegistrants = new RegistrantList();
    protected final RegistrantList mDisconnectRegistrants = new RegistrantList();
    protected final RegistrantList mUnknownConnectionRegistrants = new RegistrantList();
    protected final RegistrantList mRingbackToneRegistrants = new RegistrantList();
    protected final RegistrantList mOnHoldToneRegistrants = new RegistrantList();
    protected final RegistrantList mInCallVoicePrivacyOnRegistrants = new RegistrantList();
    protected final RegistrantList mInCallVoicePrivacyOffRegistrants = new RegistrantList();
    protected final RegistrantList mCallWaitingRegistrants = new RegistrantList();
    protected final RegistrantList mDisplayInfoRegistrants = new RegistrantList();
    protected final RegistrantList mSignalInfoRegistrants = new RegistrantList();
    protected final RegistrantList mCdmaOtaStatusChangeRegistrants = new RegistrantList();
    protected final RegistrantList mResendIncallMuteRegistrants = new RegistrantList();
    protected final RegistrantList mMmiInitiateRegistrants = new RegistrantList();
    protected final RegistrantList mMmiCompleteRegistrants = new RegistrantList();
    protected final RegistrantList mEcmTimerResetRegistrants = new RegistrantList();
    protected final RegistrantList mSubscriptionInfoReadyRegistrants = new RegistrantList();
    protected final RegistrantList mSuppServiceFailedRegistrants = new RegistrantList();
    protected final RegistrantList mServiceStateChangedRegistrants = new RegistrantList();
    protected final RegistrantList mPostDialCharacterRegistrants = new RegistrantList();
    protected final RegistrantList mTtyModeReceivedRegistrants = new RegistrantList();

    private CallManager(Context context) {
        if (context != null) {
            this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        }
        this.mPhones = new ArrayList<>();
        this.mRingingCalls = new ArrayList<>();
        this.mBackgroundCalls = new ArrayList<>();
        this.mForegroundCalls = new ArrayList<>();
        this.mDefaultPhone = null;
    }

    public static synchronized CallManager getInstance(Context context) {
        try {
            if (INSTANCE == null) {
                INSTANCE = new CallManager(context);
            }
        } catch (Throwable th) {
            throw th;
        }
        return INSTANCE;
    }

    public static synchronized CallManager getInstance() {
        try {
            if (INSTANCE == null) {
                INSTANCE = new CallManager(ActivityThread.currentApplication());
            }
        } catch (Throwable th) {
            throw th;
        }
        return INSTANCE;
    }

    public static synchronized void setInstanceForTesting(Context context) {
        INSTANCE = new CallManager(context);
    }

    public Phone getPhone(int i) {
        ArrayList<Phone> arrayList = this.mPhones;
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            Phone phone = arrayList.get(i2);
            i2++;
            Phone phone2 = phone;
            if (phone2.getSubId() == i && phone2.getPhoneType() != 5) {
                return phone2;
            }
        }
        return null;
    }

    public PhoneConstants.State getState() {
        PhoneConstants.State state = PhoneConstants.State.IDLE;
        ArrayList<Phone> arrayList = this.mPhones;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Phone phone = arrayList.get(i);
            i++;
            Phone phone2 = phone;
            PhoneConstants.State state2 = phone2.getState();
            PhoneConstants.State state3 = PhoneConstants.State.RINGING;
            if (state2 == state3) {
                state = state3;
            } else {
                PhoneConstants.State state4 = phone2.getState();
                PhoneConstants.State state5 = PhoneConstants.State.OFFHOOK;
                if (state4 == state5 && state == PhoneConstants.State.IDLE) {
                    state = state5;
                }
            }
        }
        return state;
    }

    public PhoneConstants.State getState(int i) {
        PhoneConstants.State state = PhoneConstants.State.IDLE;
        ArrayList<Phone> arrayList = this.mPhones;
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            Phone phone = arrayList.get(i2);
            i2++;
            Phone phone2 = phone;
            if (phone2.getSubId() == i) {
                PhoneConstants.State state2 = phone2.getState();
                PhoneConstants.State state3 = PhoneConstants.State.RINGING;
                if (state2 == state3) {
                    state = state3;
                } else {
                    PhoneConstants.State state4 = phone2.getState();
                    PhoneConstants.State state5 = PhoneConstants.State.OFFHOOK;
                    if (state4 == state5 && state == PhoneConstants.State.IDLE) {
                        state = state5;
                    }
                }
            }
        }
        return state;
    }

    public boolean registerPhone(Phone phone) {
        if (phone == null || this.mPhones.contains(phone)) {
            return false;
        }
        Rlog.d("CallManager", "registerPhone(" + phone.getPhoneName() + " " + phone + ")");
        if (this.mPhones.isEmpty()) {
            this.mDefaultPhone = phone;
        }
        this.mPhones.add(phone);
        this.mRingingCalls.add(phone.getRingingCall());
        this.mBackgroundCalls.add(phone.getBackgroundCall());
        this.mForegroundCalls.add(phone.getForegroundCall());
        registerForPhoneStates(phone);
        return true;
    }

    public void unregisterPhone(Phone phone) {
        if (phone == null || !this.mPhones.contains(phone)) {
            return;
        }
        Rlog.d("CallManager", "unregisterPhone(" + phone.getPhoneName() + " " + phone + ")");
        Phone imsPhone = phone.getImsPhone();
        if (imsPhone != null) {
            unregisterPhone(imsPhone);
        }
        this.mPhones.remove(phone);
        this.mRingingCalls.remove(phone.getRingingCall());
        this.mBackgroundCalls.remove(phone.getBackgroundCall());
        this.mForegroundCalls.remove(phone.getForegroundCall());
        unregisterForPhoneStates(phone);
        if (phone == this.mDefaultPhone) {
            if (this.mPhones.isEmpty()) {
                this.mDefaultPhone = null;
            } else {
                this.mDefaultPhone = this.mPhones.get(0);
            }
        }
    }

    public Phone getDefaultPhone() {
        return this.mDefaultPhone;
    }

    public Phone getFgPhone() {
        return getActiveFgCall().getPhone();
    }

    public Object getRegistrantIdentifier() {
        return this.mRegistrantidentifier;
    }

    private void registerForPhoneStates(Phone phone) {
        if (((CallManagerHandler) this.mHandlerMap.get(phone)) != null) {
            Rlog.d("CallManager", "This phone has already been registered.");
            return;
        }
        CallManagerHandler callManagerHandler = new CallManagerHandler();
        this.mHandlerMap.put(phone, callManagerHandler);
        phone.registerForPreciseCallStateChanged(callManagerHandler, 101, this.mRegistrantidentifier);
        phone.registerForDisconnect(callManagerHandler, 100, this.mRegistrantidentifier);
        phone.registerForNewRingingConnection(callManagerHandler, 102, this.mRegistrantidentifier);
        phone.registerForUnknownConnection(callManagerHandler, NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING, this.mRegistrantidentifier);
        phone.registerForIncomingRing(callManagerHandler, NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE, this.mRegistrantidentifier);
        phone.registerForRingbackTone(callManagerHandler, 105, this.mRegistrantidentifier);
        phone.registerForInCallVoicePrivacyOn(callManagerHandler, 106, this.mRegistrantidentifier);
        phone.registerForInCallVoicePrivacyOff(callManagerHandler, NtnRILConstants.MI_NTN_RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS, this.mRegistrantidentifier);
        phone.registerForDisplayInfo(callManagerHandler, 109, this.mRegistrantidentifier);
        phone.registerForSignalInfo(callManagerHandler, NtnRILConstants.MI_NTN_RIL_REQUEST_SET_UNSOL_CELL_INFO_LIST_RATE, this.mRegistrantidentifier);
        phone.registerForResendIncallMute(callManagerHandler, 112, this.mRegistrantidentifier);
        phone.registerForMmiInitiate(callManagerHandler, 113, this.mRegistrantidentifier);
        phone.registerForMmiComplete(callManagerHandler, 114, this.mRegistrantidentifier);
        phone.registerForSuppServiceFailed(callManagerHandler, 117, this.mRegistrantidentifier);
        phone.registerForServiceStateChanged(callManagerHandler, 118, this.mRegistrantidentifier);
        phone.setOnPostDialCharacter(callManagerHandler, 119, null);
        phone.registerForOnHoldTone(callManagerHandler, 120, null);
        phone.registerForSuppServiceFailed(callManagerHandler, 117, null);
        phone.registerForTtyModeReceived(callManagerHandler, 122, null);
    }

    private void unregisterForPhoneStates(Phone phone) {
        CallManagerHandler callManagerHandler = (CallManagerHandler) this.mHandlerMap.get(phone);
        if (callManagerHandler == null) {
            Rlog.e("CallManager", "Could not find Phone handler for unregistration");
            return;
        }
        this.mHandlerMap.remove(phone);
        phone.unregisterForPreciseCallStateChanged(callManagerHandler);
        phone.unregisterForDisconnect(callManagerHandler);
        phone.unregisterForNewRingingConnection(callManagerHandler);
        phone.unregisterForUnknownConnection(callManagerHandler);
        phone.unregisterForIncomingRing(callManagerHandler);
        phone.unregisterForRingbackTone(callManagerHandler);
        phone.unregisterForInCallVoicePrivacyOn(callManagerHandler);
        phone.unregisterForInCallVoicePrivacyOff(callManagerHandler);
        phone.unregisterForDisplayInfo(callManagerHandler);
        phone.unregisterForSignalInfo(callManagerHandler);
        phone.unregisterForResendIncallMute(callManagerHandler);
        phone.unregisterForMmiInitiate(callManagerHandler);
        phone.unregisterForMmiComplete(callManagerHandler);
        phone.unregisterForSuppServiceFailed(callManagerHandler);
        phone.unregisterForServiceStateChanged(callManagerHandler);
        phone.unregisterForTtyModeReceived(callManagerHandler);
        phone.setOnPostDialCharacter(null, 119, null);
        phone.unregisterForOnHoldTone(callManagerHandler);
        phone.unregisterForSuppServiceFailed(callManagerHandler);
    }

    public Connection dial(Phone phone, String str, PhoneInternalInterface.DialArgs dialArgs) throws CallStateException {
        int subId = phone.getSubId();
        if (!canDial(phone)) {
            if (phone.handleInCallMmiCommands(PhoneNumberUtils.stripSeparators(str))) {
                return null;
            }
            CallManager$$ExternalSyntheticBUOutline0.m("cannot dial in current state");
            return null;
        }
        if (hasActiveFgCall(subId)) {
            Phone phone2 = getActiveFgCall(subId).getPhone();
            boolean zIsIdle = phone2.getBackgroundCall().isIdle();
            StringBuilder sb = new StringBuilder("hasBgCall: ");
            sb.append(!zIsIdle);
            sb.append(" sameChannel:");
            sb.append(phone2 == phone);
            Rlog.d("CallManager", sb.toString());
            Phone imsPhone = phone.getImsPhone();
            if (phone2 != phone && (imsPhone == null || imsPhone != phone2)) {
                if (!zIsIdle) {
                    Rlog.d("CallManager", "Hangup");
                    getActiveFgCall(subId).hangup();
                } else {
                    Rlog.d("CallManager", "Switch");
                    phone2.switchHoldingAndActive();
                }
            }
        }
        return phone.dial(str, dialArgs);
    }

    private boolean canDial(Phone phone) {
        int state = phone.getServiceState().getState();
        int subId = phone.getSubId();
        boolean zHasActiveRingingCall = hasActiveRingingCall();
        Call.State activeFgCallState = getActiveFgCallState(subId);
        boolean z = (state == 3 || zHasActiveRingingCall || (activeFgCallState != Call.State.ACTIVE && activeFgCallState != Call.State.IDLE && activeFgCallState != Call.State.DISCONNECTED && activeFgCallState != Call.State.ALERTING)) ? false : true;
        if (!z) {
            Rlog.d("CallManager", "canDial serviceState=" + state + " hasRingingCall=" + zHasActiveRingingCall + " fgCallState=" + activeFgCallState);
        }
        return z;
    }

    public void registerForDisconnect(Handler handler, int i, Object obj) {
        this.mDisconnectRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForDisconnect(Handler handler) {
        this.mDisconnectRegistrants.remove(handler);
    }

    public void registerForPreciseCallStateChanged(Handler handler, int i, Object obj) {
        this.mPreciseCallStateRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForPreciseCallStateChanged(Handler handler) {
        this.mPreciseCallStateRegistrants.remove(handler);
    }

    public void registerForOnHoldTone(Handler handler, int i, Object obj) {
        this.mOnHoldToneRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForOnHoldTone(Handler handler) {
        this.mOnHoldToneRegistrants.remove(handler);
    }

    public void registerForMmiComplete(Handler handler, int i, Object obj) {
        Rlog.d("CallManager", "registerForMmiComplete");
        this.mMmiCompleteRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForMmiComplete(Handler handler) {
        this.mMmiCompleteRegistrants.remove(handler);
    }

    public void registerForSuppServiceFailed(Handler handler, int i, Object obj) {
        this.mSuppServiceFailedRegistrants.addUnique(handler, i, obj);
    }

    public void registerForInCallVoicePrivacyOn(Handler handler, int i, Object obj) {
        this.mInCallVoicePrivacyOnRegistrants.addUnique(handler, i, obj);
    }

    public void registerForInCallVoicePrivacyOff(Handler handler, int i, Object obj) {
        this.mInCallVoicePrivacyOffRegistrants.addUnique(handler, i, obj);
    }

    public void registerForSignalInfo(Handler handler, int i, Object obj) {
        this.mSignalInfoRegistrants.addUnique(handler, i, obj);
    }

    public void registerForDisplayInfo(Handler handler, int i, Object obj) {
        this.mDisplayInfoRegistrants.addUnique(handler, i, obj);
    }

    public void registerForTtyModeReceived(Handler handler, int i, Object obj) {
        this.mTtyModeReceivedRegistrants.addUnique(handler, i, obj);
    }

    public List<Call> getRingingCalls() {
        return Collections.unmodifiableList(this.mRingingCalls);
    }

    public List<Call> getForegroundCalls() {
        return Collections.unmodifiableList(this.mForegroundCalls);
    }

    public List<Call> getBackgroundCalls() {
        return Collections.unmodifiableList(this.mBackgroundCalls);
    }

    public boolean hasActiveFgCall() {
        return getFirstActiveCall(this.mForegroundCalls) != null;
    }

    public boolean hasActiveFgCall(int i) {
        return getFirstActiveCall(this.mForegroundCalls, i) != null;
    }

    public boolean hasActiveBgCall() {
        return getFirstActiveCall(this.mBackgroundCalls) != null;
    }

    public boolean hasActiveBgCall(int i) {
        return getFirstActiveCall(this.mBackgroundCalls, i) != null;
    }

    public boolean hasActiveRingingCall() {
        return getFirstActiveCall(this.mRingingCalls) != null;
    }

    public boolean hasActiveRingingCall(int i) {
        return getFirstActiveCall(this.mRingingCalls, i) != null;
    }

    public Call getActiveFgCall() {
        Call firstNonIdleCall = getFirstNonIdleCall(this.mForegroundCalls);
        if (firstNonIdleCall != null) {
            return firstNonIdleCall;
        }
        Phone phone = this.mDefaultPhone;
        if (phone == null) {
            return null;
        }
        return phone.getForegroundCall();
    }

    public Call getActiveFgCall(int i) {
        Call firstNonIdleCall = getFirstNonIdleCall(this.mForegroundCalls, i);
        if (firstNonIdleCall != null) {
            return firstNonIdleCall;
        }
        Phone phone = getPhone(i);
        if (phone == null) {
            return null;
        }
        return phone.getForegroundCall();
    }

    private Call getFirstNonIdleCall(List list) {
        Iterator it = list.iterator();
        Call call = null;
        while (it.hasNext()) {
            Call call2 = (Call) it.next();
            if (!call2.isIdle()) {
                return call2;
            }
            if (call2.getState() != Call.State.IDLE && call == null) {
                call = call2;
            }
        }
        return call;
    }

    private Call getFirstNonIdleCall(List list, int i) {
        Iterator it = list.iterator();
        Call call = null;
        while (it.hasNext()) {
            Call call2 = (Call) it.next();
            if (call2.getPhone().getSubId() == i) {
                if (!call2.isIdle()) {
                    return call2;
                }
                if (call2.getState() != Call.State.IDLE && call == null) {
                    call = call2;
                }
            }
        }
        return call;
    }

    public Call getFirstActiveRingingCall() {
        Call firstNonIdleCall = getFirstNonIdleCall(this.mRingingCalls);
        if (firstNonIdleCall != null) {
            return firstNonIdleCall;
        }
        Phone phone = this.mDefaultPhone;
        if (phone == null) {
            return null;
        }
        return phone.getRingingCall();
    }

    public Call.State getActiveFgCallState(int i) {
        Call activeFgCall = getActiveFgCall(i);
        if (activeFgCall != null) {
            return activeFgCall.getState();
        }
        return Call.State.IDLE;
    }

    private Call getFirstActiveCall(ArrayList arrayList) {
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            Call call = (Call) obj;
            if (!call.isIdle()) {
                return call;
            }
        }
        return null;
    }

    private Call getFirstActiveCall(ArrayList arrayList, int i) {
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList.get(i2);
            i2++;
            Call call = (Call) obj;
            if (!call.isIdle() && call.getPhone().getSubId() == i) {
                return call;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasMoreThanOneRingingCall() {
        ArrayList<Call> arrayList = this.mRingingCalls;
        int size = arrayList.size();
        int i = 0;
        int i2 = 0;
        while (i2 < size) {
            Call call = arrayList.get(i2);
            i2++;
            if (call.getState().isRinging() && (i = i + 1) > 1) {
                return true;
            }
        }
        return false;
    }

    private class CallManagerHandler extends Handler {
        private CallManagerHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 122) {
                switch (i) {
                    case 100:
                        CallManager.this.mDisconnectRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        break;
                    case 101:
                        CallManager.this.mPreciseCallStateRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        break;
                    case 102:
                        Connection connection = (Connection) ((AsyncResult) message.obj).result;
                        int subId = connection.getCall().getPhone().getSubId();
                        if (connection.getPhoneType() == 5) {
                            ((ImsPhoneConnection) connection).isIncomingCallAutoRejected();
                        }
                        if (CallManager.this.hasMoreThanOneRingingCall()) {
                            CallManager.this.isSimultaneousCallingEnabled();
                        }
                        if (!CallManager.this.getActiveFgCallState(subId).isDialing()) {
                            CallManager.this.hasMoreThanOneRingingCall();
                        }
                        CallManager.this.mNewRingingConnectionRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        break;
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING /* 103 */:
                        CallManager.this.mUnknownConnectionRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        break;
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                        if (!CallManager.this.hasActiveFgCall()) {
                            CallManager.this.mIncomingRingRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        }
                        break;
                    case 105:
                        CallManager.this.mRingbackToneRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        break;
                    case 106:
                        CallManager.this.mInCallVoicePrivacyOnRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        break;
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS /* 107 */:
                        CallManager.this.mInCallVoicePrivacyOffRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        break;
                    case 108:
                        CallManager.this.mCallWaitingRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        break;
                    case 109:
                        CallManager.this.mDisplayInfoRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        break;
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_UNSOL_CELL_INFO_LIST_RATE /* 110 */:
                        CallManager.this.mSignalInfoRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        break;
                    case 111:
                        CallManager.this.mCdmaOtaStatusChangeRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        break;
                    case 112:
                        CallManager.this.mResendIncallMuteRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        break;
                    case 113:
                        CallManager.this.mMmiInitiateRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        break;
                    case 114:
                        Rlog.d("CallManager", "CallManager: handleMessage (EVENT_MMI_COMPLETE)");
                        CallManager.this.mMmiCompleteRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        break;
                    case 115:
                        CallManager.this.mEcmTimerResetRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        break;
                    case 116:
                        CallManager.this.mSubscriptionInfoReadyRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        break;
                    case 117:
                        CallManager.this.mSuppServiceFailedRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        break;
                    case 118:
                        CallManager.this.mServiceStateChangedRegistrants.notifyRegistrants((AsyncResult) message.obj);
                        break;
                    case 119:
                        for (int i2 = 0; i2 < CallManager.this.mPostDialCharacterRegistrants.size(); i2++) {
                            Message messageMessageForRegistrant = ((Registrant) CallManager.this.mPostDialCharacterRegistrants.get(i2)).messageForRegistrant();
                            messageMessageForRegistrant.obj = message.obj;
                            messageMessageForRegistrant.arg1 = message.arg1;
                            messageMessageForRegistrant.sendToTarget();
                        }
                        break;
                }
                return;
            }
            CallManager.this.mTtyModeReceivedRegistrants.notifyRegistrants((AsyncResult) message.obj);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSimultaneousCallingEnabled() {
        try {
            if (this.mTelephonyManager == null) {
                this.mTelephonyManager = TelephonyManager.getDefault();
            }
            return this.mTelephonyManager.getMaxNumberOfSimultaneouslyActiveSims() > 1 || this.mTelephonyManager.getPhoneCapability().getMaxActiveVoiceSubscriptions() > 1;
        } catch (UnsupportedOperationException unused) {
            Rlog.d("CallManager", "Telephony not supported");
            return false;
        } catch (Exception e) {
            Rlog.d("CallManager", "exception in isSimultaneousCallingEnabled(): ", e);
            return false;
        }
    }
}
