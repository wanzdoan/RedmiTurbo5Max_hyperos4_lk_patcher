package com.android.internal.telephony;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.internal.telephony.sysprop.TelephonyProperties;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.ims.ImsEcbmStateListener;
import com.android.ims.ImsException;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCallTracker;
import java.util.Arrays;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class EcbmHandler extends Handler {
    public static final int CANCEL_ECM_TIMER = 1;
    protected static final int EVENT_EMERGENCY_CALLBACK_MODE_ENTER = 1;
    protected static final int EVENT_EXIT_EMERGENCY_CALLBACK_RESPONSE = 2;
    public static final int RESTART_ECM_TIMER = 0;
    private static EcbmHandler mInstance;
    private Context mContext;
    private Registrant mEcmExitRespRegistrant;
    protected boolean mIsPhoneInEcmState;
    private PowerManager.WakeLock mWakeLock;
    private ECBMTracker[] trackers;
    private int mNumPhones = TelephonyManager.getDefault().getPhoneCount();
    private boolean mEcmCanceledForEmergency = false;
    private final RegistrantList mEcmTimerResetRegistrants = new RegistrantList();
    private boolean mIsEcbmOnIms = false;
    private int mEcbmPhoneId = -1;
    private Runnable mExitEcmRunnable = new Runnable() { // from class: com.android.internal.telephony.EcbmHandler.1
        @Override // java.lang.Runnable
        public void run() {
            try {
                EcbmHandler.this.exitEmergencyCallbackMode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public class ECBMTracker {
        public CommandsInterface mCi;
        public ImsEcbmStateHandler mImsEcbmStateHandler;
        public ImsPhone mImsPhone;
        public GsmCdmaPhone mPhone;
        public int phoneId;

        public ECBMTracker(EcbmHandler ecbmHandler) {
        }
    }

    private EcbmHandler() {
        this.trackers = null;
        this.mIsPhoneInEcmState = false;
        this.trackers = new ECBMTracker[this.mNumPhones];
        for (int i = 0; i < this.mNumPhones; i++) {
            this.trackers[i] = new ECBMTracker(this);
        }
        this.mIsPhoneInEcmState = getInEcmMode();
    }

    public static EcbmHandler getInstance() {
        if (mInstance == null) {
            mInstance = new EcbmHandler();
        }
        return mInstance;
    }

    public EcbmHandler initialize(Context context, GsmCdmaPhone gsmCdmaPhone, CommandsInterface commandsInterface, int i) {
        int i2;
        if (this.mContext == null) {
            this.mContext = context;
            PowerManager.WakeLock wakeLockNewWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, "EcbmHandler");
            this.mWakeLock = wakeLockNewWakeLock;
            wakeLockNewWakeLock.setReferenceCounted(false);
            if (this.mIsPhoneInEcmState) {
                restoreCachedEcbmState();
                logd("initialize: ecbmPhoneId = " + this.mEcbmPhoneId + " isEcbmOnIms = " + this.mIsEcbmOnIms);
            }
        }
        int activeModemCount = TelephonyManager.getDefault().getActiveModemCount();
        this.mNumPhones = activeModemCount;
        ECBMTracker[] eCBMTrackerArr = this.trackers;
        int length = eCBMTrackerArr.length;
        this.trackers = (ECBMTracker[]) Arrays.copyOf(eCBMTrackerArr, activeModemCount);
        while (true) {
            i2 = this.mNumPhones;
            if (length >= i2) {
                break;
            }
            this.trackers[length] = new ECBMTracker(this);
            length++;
        }
        if (i >= 0 && i < i2) {
            ECBMTracker eCBMTracker = this.trackers[i];
            eCBMTracker.phoneId = i;
            eCBMTracker.mCi = commandsInterface;
            eCBMTracker.mPhone = gsmCdmaPhone;
            commandsInterface.setEmergencyCallbackMode(this, 1, Integer.valueOf(i));
            commandsInterface.registerForExitEmergencyCallbackMode(this, 2, Integer.valueOf(i));
        }
        return mInstance;
    }

    public void updateImsPhone(Phone phone, int i) {
        if (i < 0 || i >= this.mNumPhones) {
            return;
        }
        ECBMTracker[] eCBMTrackerArr = this.trackers;
        if (phone != null) {
            ECBMTracker eCBMTracker = eCBMTrackerArr[i];
            eCBMTracker.mImsPhone = (ImsPhone) phone;
            eCBMTracker.mImsEcbmStateHandler = new ImsEcbmStateHandler(i, phone.getContext().getMainExecutor());
        } else {
            ECBMTracker eCBMTracker2 = eCBMTrackerArr[i];
            eCBMTracker2.mImsPhone = null;
            eCBMTracker2.mImsEcbmStateHandler = null;
        }
    }

    public ImsEcbmStateListener getImsEcbmStateListener(int i) {
        return this.trackers[i].mImsEcbmStateHandler;
    }

    private void sendEmergencyCallbackModeChange() {
        Intent intent = new Intent("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED");
        intent.putExtra("android.telephony.extra.PHONE_IN_ECM_STATE", isInEcm());
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent, this.mEcbmPhoneId);
        ActivityManager.broadcastStickyIntent(intent, -1);
        logd("sendEmergencyCallbackModeChange");
    }

    public class ImsEcbmStateHandler extends ImsEcbmStateListener {
        int phoneId;

        public ImsEcbmStateHandler(int i, Executor executor) {
            super(executor);
            this.phoneId = i;
        }

        public void onECBMEntered(Executor executor) {
            EcbmHandler.this.logd("onECBMEntered: " + this.phoneId);
            EcbmHandler.this.mIsEcbmOnIms = true;
            EcbmHandler.this.mEcbmPhoneId = this.phoneId;
            EcbmHandler.this.handleEnterEmergencyCallbackMode(this.phoneId);
        }

        public void onECBMExited(Executor executor) {
            EcbmHandler.this.logd("onECBMExited: " + this.phoneId);
            EcbmHandler.this.handleExitEmergencyCallbackMode(this.phoneId);
            EcbmHandler.this.mIsEcbmOnIms = false;
            EcbmHandler.this.mEcbmPhoneId = -1;
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            this.mEcbmPhoneId = ((Integer) ((AsyncResult) message.obj).userObj).intValue();
            Rlog.d("EcbmHandler", " EVENT_EMERGENCY_CALLBACK_MODE_ENTER mEcbmPhoneId: " + this.mEcbmPhoneId);
            handleEnterEmergencyCallbackMode(this.mEcbmPhoneId);
            return;
        }
        if (i == 2) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            int iIntValue = ((Integer) asyncResult.userObj).intValue();
            Rlog.d("EcbmHandler", " EVENT_EXIT_EMERGENCY_CALLBACK_RESPONSE phoneId: " + iIntValue + "ar.exception: " + asyncResult.exception);
            if (asyncResult.exception == null) {
                handleExitEmergencyCallbackMode(iIntValue);
                this.mEcbmPhoneId = -1;
                return;
            }
            return;
        }
        Rlog.d("EcbmHandler", "Unnown event received: " + message.what);
    }

    public void exitEmergencyCallbackMode() throws Exception {
        ImsPhone imsPhone;
        if (this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }
        logd("exitEmergencyCallbackMode() mIsEcbmOnIms: " + this.mIsEcbmOnIms + " mEcbmPhoneId: " + this.mEcbmPhoneId);
        if (this.mIsEcbmOnIms && (imsPhone = this.trackers[this.mEcbmPhoneId].mImsPhone) != null) {
            ImsPhoneCallTracker imsPhoneCallTracker = (ImsPhoneCallTracker) imsPhone.getCallTracker();
            if (imsPhoneCallTracker != null) {
                try {
                    imsPhoneCallTracker.getEcbmInterface().exitEmergencyCallbackMode();
                    return;
                } catch (ImsException e) {
                    e.printStackTrace();
                    throw new Exception("exitEmergencyCallbackMode");
                }
            }
            return;
        }
        ECBMTracker[] eCBMTrackerArr = this.trackers;
        int i = this.mEcbmPhoneId;
        eCBMTrackerArr[i].mCi.exitEmergencyCallbackMode(obtainMessage(2, Integer.valueOf(i)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEnterEmergencyCallbackMode(int i) {
        Rlog.d("EcbmHandler", "handleEnterEmergencyCallbackMode, isInEcm()=" + isInEcm() + "phoneId: " + i);
        if (isInEcm()) {
            return;
        }
        setIsInEcm(true);
        cacheEcbmState();
        sendEmergencyCallbackModeChange();
        this.trackers[i].mPhone.notifyEmergencyCallRegistrants(true);
        postDelayed(this.mExitEcmRunnable, TelephonyProperties.ecm_exit_timer().orElse(300000L).longValue());
        this.mWakeLock.acquire();
    }

    public void handleExitEmergencyCallbackMode(int i) {
        Rlog.d("EcbmHandler", "handleExitEmergencyCallbackMode, isInEcm=" + isInEcm());
        removeCallbacks(this.mExitEcmRunnable);
        Registrant registrant = this.mEcmExitRespRegistrant;
        if (registrant != null) {
            registrant.notifyRegistrant();
        }
        setIsInEcm(false);
        removeEcbmCache();
        if (this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }
        sendEmergencyCallbackModeChange();
        this.trackers[i].mPhone.notifyEmergencyCallRegistrants(false);
    }

    public void handleTimerInEmergencyCallbackMode(int i) {
        if (i == 0) {
            postDelayed(this.mExitEcmRunnable, TelephonyProperties.ecm_exit_timer().orElse(300000L).longValue());
            this.mEcmTimerResetRegistrants.notifyResult(Boolean.FALSE);
            setEcmCanceledForEmergency(false);
            return;
        }
        if (i == 1) {
            removeCallbacks(this.mExitEcmRunnable);
            this.mEcmTimerResetRegistrants.notifyResult(Boolean.TRUE);
            setEcmCanceledForEmergency(true);
        } else {
            Rlog.e("EcbmHandler", "handleTimerInEmergencyCallbackMode, unsupported action " + i);
        }
    }

    public void setOnEcbModeExitResponse(Handler handler, int i, Object obj) {
        this.mEcmExitRespRegistrant = new Registrant(handler, i, obj);
    }

    public void unsetOnEcbModeExitResponse(Handler handler) {
        this.mEcmExitRespRegistrant.clear();
    }

    public void notifyEcbmTimerReset(Boolean bool) {
        this.mEcmTimerResetRegistrants.notifyResult(bool);
    }

    public void registerForEcmTimerReset(Handler handler, int i, Object obj) {
        this.mEcmTimerResetRegistrants.addUnique(handler, i, obj);
    }

    public void unregisterForEcmTimerReset(Handler handler) {
        this.mEcmTimerResetRegistrants.remove(handler);
    }

    public boolean isInEcm() {
        return this.mIsPhoneInEcmState;
    }

    public boolean isInImsEcm() {
        return this.mIsEcbmOnIms;
    }

    public void setIsInEcm(boolean z) {
        TelephonyProperties.in_ecm_mode(Boolean.valueOf(z));
        this.mIsPhoneInEcmState = z;
    }

    public static boolean getInEcmMode() {
        return TelephonyProperties.in_ecm_mode().orElse(Boolean.FALSE).booleanValue();
    }

    private void cacheEcbmState() {
        Context context = this.mContext;
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editorEdit.putInt("ecbm_phoneid", this.mEcbmPhoneId);
        editorEdit.putBoolean("is_ecbm_on_ims", this.mIsEcbmOnIms);
        editorEdit.apply();
    }

    private void restoreCachedEcbmState() {
        Context context = this.mContext;
        if (context == null) {
            return;
        }
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int i = defaultSharedPreferences.getInt("ecbm_phoneid", 0);
        this.mEcbmPhoneId = i;
        if (i < 0 || i >= TelephonyManager.getDefault().getActiveModemCount()) {
            this.mEcbmPhoneId = -1;
        }
        this.mIsEcbmOnIms = defaultSharedPreferences.getBoolean("is_ecbm_on_ims", false);
    }

    private void removeEcbmCache() {
        Context context = this.mContext;
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editorEdit.remove("ecbm_phoneid");
        editorEdit.remove("is_ecbm_on_ims");
        editorEdit.apply();
    }

    public boolean isEcmCanceledForEmergency() {
        return this.mEcmCanceledForEmergency;
    }

    public void setEcmCanceledForEmergency(boolean z) {
        this.mEcmCanceledForEmergency = z;
    }

    public boolean isInEcm(int i) {
        return isInEcm() && i == this.mEcbmPhoneId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Rlog.d("EcbmHandler", str);
    }
}
