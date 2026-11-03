package com.android.phone;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.Log;
import androidx.fragment.app.BackStackRecord$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.PhoneInternalInterface;
import com.android.internal.telephony.cdma.CdmaInformationRecords;
import com.android.internal.telephony.cdma.SignalToneUtil;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class CallNotifier extends Handler {
    private static final boolean DBG;
    private static CallNotifier sInstance;
    private PhoneGlobals mApplication;
    private AudioManager mAudioManager;
    private BluetoothHeadset mBluetoothHeadset;
    private CallManager mCM;
    private ToneGenerator mSignalInfoToneGenerator;
    private SubscriptionManager mSubscriptionManager;
    private TelephonyManager mTelephonyManager;
    private Map mTelephonyCallback = new ArrayMap();
    private Map mCFIStatus = new ArrayMap();
    private Map mMWIStatus = new ArrayMap();
    private boolean mVoicePrivacyState = false;
    private BluetoothProfile.ServiceListener mBluetoothProfileServiceListener = new BluetoothProfile.ServiceListener() { // from class: com.android.phone.CallNotifier.3
        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            CallNotifier.this.mBluetoothHeadset = (BluetoothHeadset) bluetoothProfile;
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            CallNotifier.this.mBluetoothHeadset = null;
        }
    };

    static {
        DBG = SystemProperties.getInt("ro.debuggable", 0) == 1;
    }

    static CallNotifier init(PhoneGlobals phoneGlobals) {
        CallNotifier callNotifier;
        synchronized (CallNotifier.class) {
            try {
                if (sInstance != null) {
                    Log.wtf("CallNotifier", "init() called multiple times!  sInstance = " + sInstance);
                } else {
                    sInstance = new CallNotifier(phoneGlobals);
                }
                callNotifier = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return callNotifier;
    }

    private CallNotifier(PhoneGlobals phoneGlobals) {
        this.mApplication = phoneGlobals;
        this.mCM = phoneGlobals.mCM;
        this.mAudioManager = (AudioManager) phoneGlobals.getSystemService("audio");
        this.mTelephonyManager = (TelephonyManager) this.mApplication.getSystemService("phone");
        this.mSubscriptionManager = (SubscriptionManager) this.mApplication.getSystemService("telephony_subscription_service");
        registerForNotifications();
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null) {
            defaultAdapter.getProfileProxy(this.mApplication.getApplicationContext(), this.mBluetoothProfileServiceListener, 1);
        }
        this.mSubscriptionManager.addOnSubscriptionsChangedListener(new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.phone.CallNotifier.1
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                CallNotifier.this.log("onSubscriptionsChanged");
                CallNotifier.this.mApplication.onSubInfoUpdate();
                CallNotifier.this.updatePhoneStateListeners(true);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createSignalInfoToneGenerator() {
        if (this.mSignalInfoToneGenerator == null) {
            try {
                this.mSignalInfoToneGenerator = new ToneGenerator(0, 80);
                Log.d("CallNotifier", "CallNotifier: mSignalInfoToneGenerator created when toneplay");
                return;
            } catch (RuntimeException e) {
                Log.w("CallNotifier", "CallNotifier: Exception caught while creating mSignalInfoToneGenerator: " + e);
                this.mSignalInfoToneGenerator = null;
                return;
            }
        }
        Log.d("CallNotifier", "mSignalInfoToneGenerator created already, hence skipping");
    }

    private void registerForNotifications() {
        this.mCM.registerForSuppServiceFailed(this, 14, (Object) null);
        this.mCM.registerForTtyModeReceived(this, 15, (Object) null);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        boolean z = DBG;
        if (z) {
            Log.d("CallNotifier", "handleMessage(" + message.what + ")");
        }
        int i = message.what;
        if (i == 3) {
            if (z) {
                log("DISCONNECT");
            }
            stopSignalInfoTone();
            return;
        }
        if (i == 22) {
            if (z) {
                log("Received Display Info notification done event ...");
            }
            PhoneDisplayMessage.dismissMessage();
            return;
        }
        if (i == 6) {
            if (z) {
                log("Received PHONE_STATE_DISPLAYINFO event");
            }
            onDisplayInfo((AsyncResult) message.obj);
            return;
        }
        if (i == 7) {
            if (z) {
                log("Received PHONE_STATE_SIGNALINFO event");
            }
            onSignalInfo((AsyncResult) message.obj);
            return;
        }
        if (i == 9) {
            if (z) {
                log("PHONE_ENHANCED_VP_ON...");
            }
            if (this.mVoicePrivacyState) {
                return;
            }
            new InCallTonePlayer(5).start();
            this.mVoicePrivacyState = true;
            return;
        }
        if (i == 10) {
            if (z) {
                log("PHONE_ENHANCED_VP_OFF...");
            }
            if (this.mVoicePrivacyState) {
                new InCallTonePlayer(5).start();
                this.mVoicePrivacyState = false;
                return;
            }
            return;
        }
        if (i == 14) {
            if (z) {
                log("PHONE_SUPP_SERVICE_FAILED...");
            }
            onSuppServiceFailed((AsyncResult) message.obj);
        } else {
            if (i != 15) {
                return;
            }
            if (z) {
                log("Received PHONE_TTY_MODE_RECEIVED event");
            }
            onTtyModeReceived((AsyncResult) message.obj);
        }
    }

    void updateCallNotifierRegistrationsAfterRadioTechnologyChange() {
        if (DBG) {
            Log.d("CallNotifier", "updateCallNotifierRegistrationsAfterRadioTechnologyChange...");
        }
        createSignalInfoToneGenerator();
    }

    class InCallTonePlayer extends Thread {
        private int mState = 0;
        private int mToneId;

        InCallTonePlayer(int i) {
            this.mToneId = i;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            ToneGenerator toneGenerator;
            CallNotifier.this.log("InCallTonePlayer.run(toneId = " + this.mToneId + ")...");
            CallNotifier.this.mCM.getFgPhone().getPhoneType();
            if (this.mToneId != 5) {
                BackStackRecord$$ExternalSyntheticBUOutline0.m("Bad toneId: ", this.mToneId);
                return;
            }
            try {
                toneGenerator = new ToneGenerator((CallNotifier.this.mBluetoothHeadset == null || !CallNotifier.this.isBluetoothAudioOn()) ? 0 : 6, 80);
            } catch (RuntimeException e) {
                Log.w("CallNotifier", "InCallTonePlayer: Exception caught while creating ToneGenerator: " + e);
                toneGenerator = null;
            }
            if (toneGenerator != null) {
                CallNotifier.this.mAudioManager.getRingerMode();
                synchronized (this) {
                    if (this.mState != 2) {
                        this.mState = 1;
                        toneGenerator.startTone(86);
                        try {
                            wait(5020L);
                        } catch (InterruptedException e2) {
                            Log.w("CallNotifier", "InCallTonePlayer stopped: " + e2);
                        }
                        toneGenerator.stopTone();
                        toneGenerator.release();
                        this.mState = 0;
                    } else {
                        toneGenerator.release();
                        this.mState = 0;
                    }
                    throw th;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isBluetoothAudioOn() {
        return this.mBluetoothHeadset.getConnectedDevices().size() > 0;
    }

    private void onDisplayInfo(AsyncResult asyncResult) {
        CdmaInformationRecords.CdmaDisplayInfoRec cdmaDisplayInfoRec = (CdmaInformationRecords.CdmaDisplayInfoRec) asyncResult.result;
        if (cdmaDisplayInfoRec != null) {
            String str = cdmaDisplayInfoRec.alpha;
            if (DBG) {
                log("onDisplayInfo: displayInfo=" + str);
            }
            PhoneDisplayMessage.displayNetworkMessage(this.mApplication, str);
            sendEmptyMessageDelayed(22, 3000L);
        }
    }

    private void onSuppServiceFailed(AsyncResult asyncResult) {
        String string;
        Object obj;
        if (TelephonyBaseUtilsStub.isGoogleCspRom() || !((obj = asyncResult.result) == PhoneInternalInterface.SuppService.CONFERENCE || obj == PhoneInternalInterface.SuppService.RESUME || obj == PhoneInternalInterface.SuppService.HOLD || obj == PhoneInternalInterface.SuppService.TRANSFER)) {
            Object obj2 = asyncResult.result;
            if (obj2 == PhoneInternalInterface.SuppService.CONFERENCE) {
                if (DBG) {
                    log("onSuppServiceFailed: displaying merge failure message");
                }
                string = this.mApplication.getResources().getString(R.string.incall_error_supp_service_conference);
            } else if (obj2 == PhoneInternalInterface.SuppService.RESUME) {
                if (!this.mCM.hasActiveFgCall() && !this.mCM.hasActiveBgCall()) {
                    if (DBG) {
                        log("onSuppServiceFailed: skip resume failure - all calls disconnected");
                        return;
                    }
                    return;
                } else {
                    if (DBG) {
                        log("onSuppServiceFailed: displaying resume failure message");
                    }
                    string = this.mApplication.getResources().getString(R.string.incall_error_supp_service_resume);
                }
            } else if (obj2 == PhoneInternalInterface.SuppService.HOLD) {
                if (DBG) {
                    log("onSuppServiceFailed: displaying hold failure message");
                }
                string = this.mApplication.getResources().getString(R.string.incall_error_supp_service_hold);
            } else if (obj2 == PhoneInternalInterface.SuppService.TRANSFER) {
                if (DBG) {
                    log("onSuppServiceFailed: displaying transfer failure message");
                }
                string = this.mApplication.getResources().getString(R.string.incall_error_supp_service_transfer);
            } else if (obj2 == PhoneInternalInterface.SuppService.SEPARATE) {
                if (DBG) {
                    log("onSuppServiceFailed: displaying separate failure message");
                }
                string = this.mApplication.getResources().getString(R.string.incall_error_supp_service_separate);
            } else if (obj2 == PhoneInternalInterface.SuppService.SWITCH) {
                if (DBG) {
                    log("onSuppServiceFailed: displaying switch failure message");
                }
                string = this.mApplication.getResources().getString(R.string.incall_error_supp_service_switch);
            } else if (obj2 == PhoneInternalInterface.SuppService.REJECT) {
                if (DBG) {
                    log("onSuppServiceFailed: displaying reject failure message");
                }
                string = this.mApplication.getResources().getString(R.string.incall_error_supp_service_reject);
            } else if (obj2 == PhoneInternalInterface.SuppService.HANGUP) {
                if (DBG) {
                    log("onSuppServiceFailed: displaying hangup failure message");
                }
                string = this.mApplication.getResources().getString(R.string.incall_error_supp_service_hangup);
            } else if (obj2 == PhoneInternalInterface.SuppService.UNKNOWN) {
                if (DBG) {
                    log("onSuppServiceFailed: displaying unknown failure message");
                }
                string = this.mApplication.getResources().getString(R.string.incall_error_supp_service_unknown);
            } else {
                if (DBG) {
                    log("onSuppServiceFailed: unknown failure");
                    return;
                }
                return;
            }
            PhoneDisplayMessage.displayErrorMessage(this.mApplication, string);
            sendEmptyMessageDelayed(22, 3000L);
        }
    }

    public void updatePhoneStateListeners(boolean z) {
        updatePhoneStateListeners(z, 2, -1);
    }

    public void updatePhoneStateListeners(boolean z, int i, int i2) {
        int i3;
        List activeSubscriptionInfoList = SubscriptionManagerService.getInstance().getActiveSubscriptionInfoList(this.mApplication.getOpPackageName(), this.mApplication.getAttributionTag(), true);
        ArrayList arrayList = new ArrayList(this.mTelephonyCallback.keySet());
        Collections.sort(arrayList, new Comparator(this) { // from class: com.android.phone.CallNotifier.2
            @Override // java.util.Comparator
            public int compare(Integer num, Integer num2) {
                return SubscriptionManager.getSlotIndex(num.intValue()) > SubscriptionManager.getSlotIndex(num2.intValue()) ? 0 : -1;
            }
        });
        int size = arrayList.size() - 1;
        while (true) {
            if (size < 0) {
                break;
            }
            Integer num = (Integer) arrayList.get(size);
            int iIntValue = num.intValue();
            if (activeSubscriptionInfoList == null || !containsSubId(activeSubscriptionInfoList, iIntValue)) {
                Log.i("CallNotifier", "updatePhoneStateListeners: Hide the outstanding notifications.");
                this.mApplication.notificationMgr.updateMwi(iIntValue, false);
                this.mApplication.notificationMgr.updateCfi(iIntValue, false);
                this.mTelephonyManager.unregisterTelephonyCallback((TelephonyCallback) this.mTelephonyCallback.get(num));
                this.mTelephonyCallback.remove(num);
                this.mCFIStatus.remove(num);
                this.mMWIStatus.remove(num);
            } else {
                Log.i("CallNotifier", "updatePhoneStateListeners: update CF/MWI for subId=" + iIntValue);
                if (this.mCFIStatus.containsKey(num)) {
                    if (i == 1 && iIntValue == i2) {
                        this.mApplication.notificationMgr.updateCfi(iIntValue, ((Boolean) this.mCFIStatus.get(num)).booleanValue(), z);
                    } else {
                        this.mApplication.notificationMgr.updateCfi(iIntValue, ((Boolean) this.mCFIStatus.get(num)).booleanValue(), true);
                    }
                }
                if (this.mMWIStatus.containsKey(num)) {
                    if (i == 0 && iIntValue == i2) {
                        this.mApplication.notificationMgr.updateMwi(iIntValue, ((Boolean) this.mMWIStatus.get(num)).booleanValue(), z);
                    } else {
                        this.mApplication.notificationMgr.updateMwi(iIntValue, ((Boolean) this.mMWIStatus.get(num)).booleanValue(), true);
                    }
                }
            }
            size--;
        }
        if (activeSubscriptionInfoList == null) {
            log("updatePhoneStateListeners: subInfos is null");
            return;
        }
        for (i3 = 0; i3 < activeSubscriptionInfoList.size(); i3++) {
            int subscriptionId = ((SubscriptionInfo) activeSubscriptionInfoList.get(i3)).getSubscriptionId();
            if (!this.mTelephonyCallback.containsKey(Integer.valueOf(subscriptionId))) {
                CallNotifierTelephonyCallback callNotifierTelephonyCallback = new CallNotifierTelephonyCallback(subscriptionId);
                this.mTelephonyManager.createForSubscriptionId(subscriptionId).registerTelephonyCallback(new HandlerExecutor(this), callNotifierTelephonyCallback);
                this.mTelephonyCallback.put(Integer.valueOf(subscriptionId), callNotifierTelephonyCallback);
                log("updatePhoneStateListeners: add listener for subId=" + subscriptionId);
            }
        }
    }

    private boolean containsSubId(List list, int i) {
        if (list == null) {
            return false;
        }
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (((SubscriptionInfo) list.get(i2)).getSubscriptionId() == i) {
                return true;
            }
        }
        return false;
    }

    private void onTtyModeReceived(AsyncResult asyncResult) {
        int i;
        if (DBG) {
            log("TtyModeReceived: displaying notification message");
        }
        int iIntValue = ((Integer) asyncResult.result).intValue();
        if (iIntValue == 0) {
            i = android.R.string.permlab_install_shortcut;
        } else if (iIntValue == 1) {
            i = android.R.string.permlab_highSamplingRateSensors;
        } else if (iIntValue == 2) {
            i = android.R.string.permlab_imagesWrite;
        } else if (iIntValue != 3) {
            Log.e("CallNotifier", "Unsupported TTY mode: " + asyncResult.result);
            i = 0;
        } else {
            i = android.R.string.permlab_invokeCarrierSetup;
        }
        if (i != 0) {
            PhoneGlobals phoneGlobals = this.mApplication;
            PhoneDisplayMessage.displayNetworkMessage(phoneGlobals, phoneGlobals.getResources().getString(i));
            sendEmptyMessageDelayed(22, 3000L);
        }
    }

    class SignalInfoTonePlayer extends Thread {
        private int mToneId;

        SignalInfoTonePlayer(int i) {
            this.mToneId = i;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            CallNotifier.this.log("SignalInfoTonePlayer.run(toneId = " + this.mToneId + ")...");
            CallNotifier.this.createSignalInfoToneGenerator();
            if (CallNotifier.this.mSignalInfoToneGenerator != null) {
                CallNotifier.this.mSignalInfoToneGenerator.stopTone();
                CallNotifier.this.mSignalInfoToneGenerator.startTone(this.mToneId);
            }
        }
    }

    private void onSignalInfo(AsyncResult asyncResult) {
        if (!PhoneGlobals.sVoiceCapable) {
            Log.w("CallNotifier", "Got onSignalInfo() on non-voice-capable device! Ignoring...");
            return;
        }
        if (PhoneUtils.isRealIncomingCall(this.mCM.getFirstActiveRingingCall().getState())) {
            stopSignalInfoTone();
            return;
        }
        CdmaInformationRecords.CdmaSignalInfoRec cdmaSignalInfoRec = (CdmaInformationRecords.CdmaSignalInfoRec) asyncResult.result;
        if (cdmaSignalInfoRec != null) {
            boolean z = cdmaSignalInfoRec.isPresent;
            boolean z2 = DBG;
            if (z2) {
                log("onSignalInfo: isPresent=" + z);
            }
            if (z) {
                int i = cdmaSignalInfoRec.signalType;
                int i2 = cdmaSignalInfoRec.alertPitch;
                int i3 = cdmaSignalInfoRec.signal;
                if (z2) {
                    log("onSignalInfo: uSignalType=" + i + ", uAlertPitch=" + i2 + ", uSignal=" + i3);
                }
                new SignalInfoTonePlayer(SignalToneUtil.getAudioToneFromSignalInfo(i, i2, i3)).start();
            }
        }
    }

    void stopSignalInfoTone() {
        if (DBG) {
            log("stopSignalInfoTone: Stopping SignalInfo tone player");
        }
        new SignalInfoTonePlayer(98).start();
    }

    class CallNotifierTelephonyCallback extends TelephonyCallback implements TelephonyCallback.MessageWaitingIndicatorListener, TelephonyCallback.CallForwardingIndicatorListener {
        private final int mSubId;

        CallNotifierTelephonyCallback(int i) {
            this.mSubId = i;
        }

        @Override // android.telephony.TelephonyCallback.MessageWaitingIndicatorListener
        public void onMessageWaitingIndicatorChanged(boolean z) {
            CallNotifier callNotifier = CallNotifier.this;
            StringBuilder sb = new StringBuilder("onMessageWaitingIndicatorChanged(): subId＝");
            sb.append(this.mSubId);
            sb.append(", visible=");
            sb.append(z ? "Y" : "N");
            callNotifier.log(sb.toString());
            CallNotifier.this.mMWIStatus.put(Integer.valueOf(this.mSubId), Boolean.valueOf(z));
            CallNotifier.this.updatePhoneStateListeners(false, 0, this.mSubId);
        }

        @Override // android.telephony.TelephonyCallback.CallForwardingIndicatorListener
        public void onCallForwardingIndicatorChanged(boolean z) {
            StringBuilder sb = new StringBuilder("onCallForwardingIndicatorChanged(): subId=");
            sb.append(this.mSubId);
            sb.append(", visible=");
            sb.append(z ? "Y" : "N");
            Log.i("CallNotifier", sb.toString());
            CallNotifier.this.mCFIStatus.put(Integer.valueOf(this.mSubId), Boolean.valueOf(z));
            CallNotifier.this.updatePhoneStateListeners(false, 1, this.mSubId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("CallNotifier", str);
    }
}
