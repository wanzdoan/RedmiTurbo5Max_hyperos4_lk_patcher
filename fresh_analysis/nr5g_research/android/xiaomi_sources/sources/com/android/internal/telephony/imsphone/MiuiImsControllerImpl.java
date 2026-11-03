package com.android.internal.telephony.imsphone;

import android.app.ActivityManagerNative;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsReasonInfo;
import com.android.ims.ImsCall;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.gsm.MiuiGsmMmiCodeUtils;
import com.android.internal.telephony.ims.ImsResolver;
import miui.os.Build;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.CommenUtils;
import miui.telephony.MiuiDisplayInfoController;
import miui.telephony.MiuiTelephony;
import vendor.xiaomi.hardware.radio.RadioError;

/* JADX INFO: loaded from: classes.dex */
public class MiuiImsControllerImpl implements MiuiImsControllerStub.IMiuiImsController {
    private static final int DELAY_HANGUP_VOWIFI_CALL_TIMER = 10000;
    private static final int DOMAIN_AUTOMATIC = 0;
    private static final int DOMAIN_CS = 1;
    private static final int DOMAIN_PS = 2;
    private static final int EVENT_ANSWER_WAITING_CALL = 30;
    private static final int EVENT_DELAY_IMS_STATE_UPDATE = 1;
    private static final int EVENT_HANGUP_VOWIFI_CALL = 33;
    private static final int EVENT_SET_COMPLETE = 0;
    private static final int EVENT_SUPP_SVC_QUERY_COMPLETE = 7;
    private static final String EXTRA_CALL_DOMAIN = "org.codeaurora.extra.CALL_DOMAIN";
    private static final int HANDLER_CONFIG_CHANGED = 2;
    private static String LOG_TAG = "MiuiImsControllerImpl";
    private static final String SC_CLIP = "30";
    private static final String SC_CLIR = "31";
    private static final String SC_COLP = "76";
    private static final String SC_COLR = "77";
    protected MiuiImsHandler mHandler;
    private Phone mPhone;
    private HandlerThread mWorkThread;
    private int mImsRegistedState = -1;
    private int mWfcRegistedState = -1;
    private boolean mDelayUpdateVolteState = false;
    private boolean mNeedDelayUpdateIMSRegistedState = false;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.imsphone.MiuiImsControllerImpl.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Handler handler;
            MiuiImsControllerImpl.this.log("onReceive : " + intent.getAction());
            ImsPhoneCallTracker tracker = MiuiImsControllerImpl.this.mPhone.getCallTracker();
            if ("android.net.wifi.RSSI_CHANGED".equals(intent.getAction())) {
                if (tracker.getState() != PhoneConstants.State.IDLE && tracker.isVowifiEnabled()) {
                    MiuiImsControllerImpl.this.shouldDisconnectVowifiCall(intent, MiuiImsControllerImpl.this.mPhone);
                    return;
                }
                return;
            }
            if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
                int slotId = intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1);
                MiuiImsControllerImpl miuiImsControllerImpl = MiuiImsControllerImpl.this;
                if (slotId == -1) {
                    miuiImsControllerImpl.log("Received simState for invalid slot id.");
                    return;
                }
                if (slotId != miuiImsControllerImpl.mPhone.getPhoneId()) {
                    return;
                }
                int subId = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
                String iccState = intent.getStringExtra("ss");
                if ("ABSENT".equals(iccState)) {
                    MiuiImsControllerImpl.this.log("Received sim state Changed for SlotId: " + slotId + ", SubId: " + subId + ", sim state: " + iccState);
                    ImsResolver resolver = ImsResolver.getInstance();
                    if (resolver == null || (handler = resolver.getHandler()) == null) {
                        return;
                    }
                    handler.obtainMessage(2, slotId, subId).sendToTarget();
                    return;
                }
                return;
            }
            if (CommenUtils.ACTIVE_RE_REGISTRATION_NETWORK.equals(intent.getAction())) {
                Rlog.d(MiuiImsControllerImpl.LOG_TAG, "onReceive : ACTIVE_RE_REGISTRATION_NETWORK need delay update IMS State");
                MiuiImsControllerImpl.this.mNeedDelayUpdateIMSRegistedState = true;
                String reasonString = intent.getStringExtra(CommenUtils.RE_REGISTRATION_NETWORK_REASON);
                int delayTimer = CommenUtils.getReRegistrationDelayTimer(CommenUtils.AVOID_OOS_FOR_IMS, reasonString);
                MiuiImsControllerImpl.this.mHandler.removeMessages(1);
                MiuiImsControllerImpl.this.mHandler.sendEmptyMessageDelayed(1, delayTimer);
            }
        }
    };
    private MiuiDisplayInfoController.CallStateListener mCallstateListener = new MiuiDisplayInfoController.CallStateListener() { // from class: com.android.internal.telephony.imsphone.MiuiImsControllerImpl.2
        @Override // miui.telephony.MiuiDisplayInfoController.CallStateListener
        public void onCallStateChange(int phoneId, boolean delay) {
            if (!SubscriptionManager.isValidPhoneId(phoneId) || MiuiImsControllerImpl.this.mPhone.getPhoneId() != phoneId) {
                MiuiImsControllerImpl.this.log("delayUpdateVoltestate: error phoneId: " + phoneId);
                return;
            }
            MiuiImsControllerImpl.this.log("delayUpdateVoltestate: phoneId" + phoneId + " delay: " + delay + " mDelayUpdateVolteState: " + MiuiImsControllerImpl.this.mDelayUpdateVolteState);
            if (MiuiImsControllerImpl.this.mDelayUpdateVolteState != delay) {
                MiuiImsControllerImpl.this.mDelayUpdateVolteState = delay;
                if (!delay) {
                    MiuiImsControllerImpl.this.sendIMSRegistedStateBroadcast(MiuiImsControllerImpl.this.mPhone.isVolteEnabled(), MiuiImsControllerImpl.this.mPhone);
                }
            }
        }
    };

    public void init(Phone phone, Object... arg) {
        this.mPhone = phone;
        this.mWorkThread = new HandlerThread("MiuiImsControllerImpl" + this.mPhone.getPhoneId());
        this.mWorkThread.start();
        this.mHandler = new MiuiImsHandler(this.mWorkThread.getLooper());
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction("android.net.wifi.RSSI_CHANGED");
        intentfilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentfilter.addAction(CommenUtils.ACTIVE_RE_REGISTRATION_NETWORK);
        this.mPhone.getContext().registerReceiver(this.mReceiver, intentfilter, 2);
        if (!Build.IS_INTERNATIONAL_BUILD) {
            MiuiDisplayInfoController.getInstance().addCallStateListener(this.mCallstateListener);
        }
        MiuiTelephony.getInstance().registerTelephonyPlugins(this, phone.getPhoneId());
    }

    public void dispose(Phone phone) {
        log("dispose");
        this.mPhone.getContext().unregisterReceiver(this.mReceiver);
        if (!Build.IS_INTERNATIONAL_BUILD) {
            MiuiDisplayInfoController.getInstance().removeCallStateListener(this.mCallstateListener);
        }
        MiuiTelephony.getInstance().unregisterTelephonyPlugins(this, phone.getPhoneId());
    }

    protected void log(String msg) {
        Rlog.d(LOG_TAG + "[" + this.mPhone.getPhoneId() + "]", msg);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v3 */
    public void sendIMSRegistedStateBroadcast(boolean z, Phone phone) {
        ?? IsWifiCallingEnabled = phone.isWifiCallingEnabled();
        if (!phone.isImsRegistered()) {
            IsWifiCallingEnabled = 0;
        }
        boolean z2 = this.mWfcRegistedState != IsWifiCallingEnabled;
        boolean z3 = z;
        if ((!MiuiImsPhoneUtils.isRatLTE(phone) && !MiuiImsPhoneUtils.isRatNr(phone)) || !phone.isImsRegistered()) {
            z3 = false;
        }
        boolean z4 = this.mImsRegistedState != (z3 ? 1 : 0);
        MiuiDisplayInfoController.getInstance().updateVolteStatus(z3, this.mImsRegistedState == 1, phone.getPhoneId());
        if (!z4 && !z2) {
            return;
        }
        log("sendIMSRegistedStateBroadcast: ims register state changed");
        if (z4 && z3) {
            this.mNeedDelayUpdateIMSRegistedState = false;
        }
        if (this.mNeedDelayUpdateIMSRegistedState || this.mDelayUpdateVolteState) {
            log("delay send IMS register state mNeedDelayUpdateIMSRegistedState: " + this.mNeedDelayUpdateIMSRegistedState);
            return;
        }
        this.mImsRegistedState = z3 ? 1 : 0;
        this.mWfcRegistedState = IsWifiCallingEnabled;
        Intent intent = new Intent("android.intent.action.ACTION_IMS_REGISTED");
        intent.putExtra("state", z3);
        intent.putExtra("wfc_state", (boolean) IsWifiCallingEnabled);
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent, phone.getPhoneId());
        log("sendIMSRegistedStateBroadcast: ACTION_IMS_REGISTED volte = " + z3 + ", wfc=" + ((boolean) IsWifiCallingEnabled) + " for phoneId=" + phone.getPhoneId());
        ActivityManagerNative.broadcastStickyIntent(intent, "android.permission.READ_PHONE_STATE", -1);
        Intent intent2 = new Intent(intent);
        intent2.setAction("android.intent.action.ACTION_IMS_REGISTED" + phone.getPhoneId());
        ActivityManagerNative.broadcastStickyIntent(intent2, "android.permission.READ_PHONE_STATE", -1);
    }

    public boolean isCarrierConfigedLocalTone(Phone phone) {
        return MiuiImsPhoneUtils.isCarrierConfigedLocalTone(phone);
    }

    public boolean isCarrierConfigedBusyTone(Phone phone) {
        return MiuiImsPhoneUtils.isCarrierConfigedBusyTone(phone);
    }

    public void broadcastCallAutoReject(Phone phone, ImsCallProfile callProfile, ImsReasonInfo reason) {
        MiuiImsPhoneUtils.broadcastCallAutoReject(phone, callProfile, reason);
    }

    public boolean isGwsdMode(Phone phone) {
        return MiuiImsPhoneUtils.isGwsdMode(phone);
    }

    public boolean shouldNotifyAutoRejectedCall(Phone phone, ImsReasonInfo imsReasonInfo) {
        return MiuiImsPhoneUtils.shouldNotifyAutoRejectedCall(phone, imsReasonInfo);
    }

    public String getEffectiveNumber(Phone phone, String formattedNumber) {
        return MiuiImsPhoneUtils.getEffectiveNumber(phone, formattedNumber);
    }

    public int getClirModeForEmergencyCall(int clirMode, Phone mPhone, boolean isEmergencyNumber, int defaultClirMode) {
        return MiuiImsPhoneUtils.getClirModeForEmergencyCall(clirMode, mPhone, isEmergencyNumber, defaultClirMode);
    }

    protected class MiuiImsHandler extends Handler {
        public MiuiImsHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            MiuiImsControllerImpl.this.log("ImsController handleMessage what=" + msg.what);
            switch (msg.what) {
                case 1:
                    MiuiImsControllerImpl.this.mNeedDelayUpdateIMSRegistedState = false;
                    MiuiImsControllerImpl.this.sendIMSRegistedStateBroadcast(MiuiImsControllerImpl.this.mPhone.isVolteEnabled(), MiuiImsControllerImpl.this.mPhone);
                    break;
                case MiuiImsControllerImpl.EVENT_HANGUP_VOWIFI_CALL /* 33 */:
                    ImsPhoneCallTracker tracker = MiuiImsControllerImpl.this.mPhone.getCallTracker();
                    if (!tracker.isVoiceOverCellularImsEnabled()) {
                        MiuiImsControllerImpl.this.log("wifi signal is poor or wifi lost, hangup all ims call");
                        ImsCall imsRingCall = tracker.mRingingCall.getImsCall();
                        if (imsRingCall != null && tracker.mRingingCall.getState().isAlive()) {
                            imsRingCall.terminate(RadioError.OEM_ERROR_1);
                        }
                        ImsCall imsBgCall = tracker.mBackgroundCall.getImsCall();
                        if (imsBgCall != null && tracker.mBackgroundCall.getState().isAlive()) {
                            imsBgCall.terminate(RadioError.OEM_ERROR_1);
                        }
                        ImsCall imsFgCall = tracker.mForegroundCall.getImsCall();
                        if (imsFgCall != null && tracker.mForegroundCall.getState().isAlive()) {
                            imsFgCall.terminate(RadioError.OEM_ERROR_1);
                            break;
                        }
                    }
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shouldDisconnectVowifiCall(Intent intent, Phone phone) {
        boolean shouldDisconnectCall = CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_wifi_signal_weak_disconnect_vowifi_call");
        int mRssi = intent.getIntExtra("newRssi", 0);
        log("shouldDisconnectVowifiCall : wifi rssi change mRssi: " + mRssi + ", should Disconnect Call: " + shouldDisconnectCall);
        if (shouldDisconnectCall && mRssi <= -75 && !this.mHandler.hasMessages(EVENT_HANGUP_VOWIFI_CALL)) {
            this.mHandler.sendEmptyMessageDelayed(EVENT_HANGUP_VOWIFI_CALL, 10000L);
        } else if (mRssi > -75 && this.mHandler.hasMessages(EVENT_HANGUP_VOWIFI_CALL)) {
            this.mHandler.removeMessages(EVENT_HANGUP_VOWIFI_CALL);
        }
    }

    public boolean processCode(Phone phone, final ImsPhoneMmiCode owner, Object... arg) {
        if (arg.length < 3) {
            return false;
        }
        String mSc = (String) arg[0];
        String mSib = (String) arg[1];
        String mSia = (String) arg[2];
        if (!owner.isShortCode()) {
            if (ImsPhoneMmiCode.isServiceCodeCallBarring(mSc)) {
                String facility = ImsPhoneMmiCode.scToBarringFacility(mSc);
                int serviceClass = MiuiImsPhoneUtils.siToServiceClass(mSib);
                if (owner.isInterrogate()) {
                    MiuiImsPhoneUtils.getCallBarring(7, owner, this.mPhone, this.mPhone.mDefaultPhone.mCi, facility, mSia, serviceClass, new IMiuiImsMmiCodeCallBack(this) { // from class: com.android.internal.telephony.imsphone.MiuiImsControllerImpl.3
                        @Override // com.android.internal.telephony.imsphone.IMiuiImsMmiCodeCallBack
                        public void onSuppSvcQueryComplete(AsyncResult ar) {
                            Message getcallBarringMsg = owner.obtainMessage(7, ar);
                            owner.sendMessage(getcallBarringMsg);
                        }

                        @Override // com.android.internal.telephony.imsphone.IMiuiImsMmiCodeCallBack
                        public void onSetComplete(Message msg, AsyncResult ar) {
                        }
                    });
                    return true;
                }
                if (owner.isActivate() || owner.isDeactivate()) {
                    MiuiImsPhoneUtils.setCallBarring(0, owner, this.mPhone, this.mPhone.mDefaultPhone.mCi, facility, owner.isActivate(), mSia, serviceClass, new IMiuiImsMmiCodeCallBack(this) { // from class: com.android.internal.telephony.imsphone.MiuiImsControllerImpl.4
                        @Override // com.android.internal.telephony.imsphone.IMiuiImsMmiCodeCallBack
                        public void onSuppSvcQueryComplete(AsyncResult ar) {
                        }

                        @Override // com.android.internal.telephony.imsphone.IMiuiImsMmiCodeCallBack
                        public void onSetComplete(Message msg, AsyncResult ar) {
                            Message setcallBarringMsg = owner.obtainMessage(0, ar);
                            owner.sendMessage(setcallBarringMsg);
                        }
                    });
                    return true;
                }
            } else if (mSc != null && mSc.equals("31") && owner.isActivate() && !this.mPhone.isClirActivationAndDeactivationPrevented() && MiuiGsmMmiCodeUtils.isCLIREnableForbidden(this.mPhone)) {
                log("processCode: CLIR is not allowed to enable");
                this.mPhone.onMMIDone(owner);
                return true;
            }
        }
        return false;
    }

    public boolean shallDialOnCircuitSwitch(int phoneId, Bundle extras) {
        return MiuiTelephony.IS_QCOM && extras != null && extras.getInt(EXTRA_CALL_DOMAIN, 0) == 1;
    }

    public boolean isPseudoDsdaCall(int phoneId) {
        TelephonyManager telephony = TelephonyManager.from(this.mPhone.getContext());
        if (telephony.getActiveModemCount() > 1) {
            for (Phone phone : PhoneFactory.getPhones()) {
                if (phone.getSubId() != this.mPhone.getSubId()) {
                    return phone.getState() == PhoneConstants.State.OFFHOOK;
                }
            }
        }
        return false;
    }

    public void setActiveCallDisconnectedOnAnswer(Connection conn, int phoneId) {
        conn.setActiveCallDisconnectedOnAnswer(isPseudoDsdaCall(phoneId));
    }

    public boolean isCarrierSupportCbOverIms(Phone phone) {
        return MiuiImsPhoneUtils.isCarrierSupportCbOverIms(phone);
    }

    public boolean isCarrierSupportCbOverIms(Phone phone, PersistableBundle pb) {
        return MiuiImsPhoneUtils.isCarrierSupportCbOverIms(phone);
    }

    public boolean isCustForKrAndKrSimInserted(Phone phone) {
        return MiuiImsPhoneUtils.isCustForKrAndKrSimInserted();
    }

    public void holdActiveCall(ImsPhoneCallTracker tracker, Object param) {
        if (tracker != null && tracker.mForegroundCall.getState() != Call.State.ACTIVE && tracker.mBackgroundCall.getState() == Call.State.HOLDING) {
            try {
                tracker.unholdHeldCall();
            } catch (CallStateException e) {
                log("unholdHeldCall... exception=" + e);
            }
        }
    }

    public void holdActiveCallForWaitingCall(ImsPhoneCallTracker tracker, Object param) {
        if (tracker == null) {
            return;
        }
        boolean switchingWithWaitingCall = !tracker.mBackgroundCall.getState().isAlive() && tracker.mRingingCall.getState() == Call.State.WAITING;
        if (!switchingWithWaitingCall) {
            if (tracker.mRingingCall.getState() == Call.State.INCOMING) {
                log("cannot be in the incoming state");
                return;
            }
            if (tracker.mRingingCall.getState() == Call.State.WAITING && tracker.mBackgroundCall.getState() == Call.State.HOLDING) {
                tracker.sendEmptyMessage(30);
            } else if (tracker.mBackgroundCall.getState() == Call.State.HOLDING) {
                try {
                    tracker.unholdHeldCall();
                } catch (CallStateException e) {
                    log("unholdHeldCall... exception=" + e);
                }
            }
        }
    }

    public boolean isVolteEnabled() {
        return this.mImsRegistedState == 1;
    }

    public boolean isVoWiFiEnabled() {
        return this.mWfcRegistedState == 1;
    }
}
