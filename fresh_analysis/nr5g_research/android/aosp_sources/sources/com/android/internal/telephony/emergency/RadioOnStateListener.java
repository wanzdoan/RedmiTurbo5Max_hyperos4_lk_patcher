package com.android.internal.telephony.emergency;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.ServiceState;
import android.telephony.satellite.ISatelliteModemStateCallback;
import com.android.internal.telephony.IIntegerConsumer;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SomeArgs;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.telephony.Rlog;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class RadioOnStateListener {
    private static int MAX_NUM_RETRIES = 5;
    public static final int MSG_IMS_CAPABILITY_CHANGED = 6;
    public static final int MSG_RADIO_OFF_OR_NOT_AVAILABLE = 5;
    public static final int MSG_RADIO_ON = 4;
    public static final int MSG_SATELLITE_ENABLED_CHANGED = 8;
    public static final int MSG_SERVICE_STATE_CHANGED = 2;
    public static final int MSG_TIMEOUT_ONTIMEOUT_CALLBACK = 7;
    private static long TIME_BETWEEN_RETRIES_MILLIS = 5000;
    private Callback mCallback;
    private boolean mForEmergencyCall;
    private int mNumRetriesSoFar;
    private int mOnTimeoutCallbackInterval;
    private Phone mPhone;
    private boolean mRadioStateFine;
    private SatelliteController mSatelliteController;
    private boolean mSelectedPhoneForEmergencyCall;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: com.android.internal.telephony.emergency.RadioOnStateListener.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    SomeArgs someArgs = (SomeArgs) message.obj;
                    try {
                        RadioOnStateListener.this.startSequenceInternal((Phone) someArgs.arg1, (Callback) someArgs.arg2, ((Boolean) someArgs.arg3).booleanValue(), ((Boolean) someArgs.arg4).booleanValue(), someArgs.argi1);
                        return;
                    } finally {
                        someArgs.recycle();
                    }
                case 2:
                    RadioOnStateListener.this.onServiceStateChanged((ServiceState) ((AsyncResult) message.obj).result);
                    return;
                case 3:
                    RadioOnStateListener.this.onRetryTimeout();
                    return;
                case 4:
                    RadioOnStateListener.this.onRadioOn();
                    return;
                case 5:
                    RadioOnStateListener.this.registerForRadioOn();
                    if (RadioOnStateListener.this.mWaitForRadioOffFirst) {
                        Rlog.d("RadioOnStateListener", "MSG_RADIO_OFF_OR_NOT_AVAILABLE");
                        RadioOnStateListener.this.mWaitForRadioOffFirst = false;
                        RadioOnStateListener.this.mNumRetriesSoFar = 0;
                        return;
                    }
                    return;
                case 6:
                    RadioOnStateListener.this.onImsCapabilityChanged();
                    return;
                case 7:
                    RadioOnStateListener.this.onTimeoutCallbackTimeout();
                    return;
                case 8:
                    RadioOnStateListener.this.onSatelliteEnabledChanged();
                    return;
                default:
                    Rlog.w("RadioOnStateListener", String.format(Locale.getDefault(), "handleMessage: unexpected message: %d.", Integer.valueOf(message.what)));
                    return;
            }
        }
    };
    private final ISatelliteModemStateCallback mSatelliteCallback = new ISatelliteModemStateCallback.Stub() { // from class: com.android.internal.telephony.emergency.RadioOnStateListener.2
        public void onSatelliteModemStateChanged(int i) {
            RadioOnStateListener.this.mHandler.obtainMessage(8).sendToTarget();
        }

        public void onEmergencyModeChanged(boolean z) {
            Rlog.d("RadioOnStateListener", "onEmergencyModeChanged: ignored " + z);
        }

        public void onRegistrationFailure(int i) {
            Rlog.d("RadioOnStateListener", "onRegistrationFailure: causeCode " + i);
        }

        public void onTerrestrialNetworkAvailableChanged(boolean z) {
            Rlog.d("RadioOnStateListener", "onTerrestrialNetworkAvailableChanged: isAvailable " + z);
        }
    };
    private boolean mWaitForRadioOffFirst = false;

    public interface Callback {
        boolean isOkToCall(Phone phone, int i, boolean z);

        void onComplete(RadioOnStateListener radioOnStateListener, boolean z);

        default void onEnterEmergencyMode(Phone phone) {
        }

        boolean onTimeout(Phone phone, int i, boolean z);
    }

    public void waitForRadioOn(Phone phone, Callback callback, boolean z, boolean z2, int i) {
        Rlog.d("RadioOnStateListener", "waitForRadioOn: Phone " + phone.getPhoneId());
        if (this.mPhone != null) {
            return;
        }
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = phone;
        someArgsObtain.arg2 = callback;
        someArgsObtain.arg3 = Boolean.valueOf(z);
        someArgsObtain.arg4 = Boolean.valueOf(z2);
        someArgsObtain.argi1 = i;
        this.mHandler.obtainMessage(1, someArgsObtain).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startSequenceInternal(Phone phone, Callback callback, boolean z, boolean z2, int i) {
        Rlog.d("RadioOnStateListener", "startSequenceInternal: Phone " + phone.getPhoneId());
        this.mSatelliteController = SatelliteController.getInstance();
        cleanup();
        this.mPhone = phone;
        this.mCallback = callback;
        this.mForEmergencyCall = z;
        this.mSelectedPhoneForEmergencyCall = z2;
        this.mOnTimeoutCallbackInterval = i;
        registerForServiceStateChanged();
        registerForRadioOff();
        if (this.mSatelliteController.isSatelliteEnabledOrBeingEnabled()) {
            registerForSatelliteEnabledChanged();
        }
        startRetryTimer();
        registerForImsCapabilityChanged();
        startOnTimeoutCallbackTimer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onImsCapabilityChanged() {
        Phone phone = this.mPhone;
        if (phone == null) {
            return;
        }
        boolean zIsVoiceOverCellularImsEnabled = phone.isVoiceOverCellularImsEnabled();
        Rlog.d("RadioOnStateListener", String.format("onImsCapabilityChanged, capable = %s, Phone = %s", Boolean.valueOf(zIsVoiceOverCellularImsEnabled), Integer.valueOf(this.mPhone.getPhoneId())));
        if (isOkToCall(this.mPhone.getServiceState().getState(), zIsVoiceOverCellularImsEnabled)) {
            Rlog.d("RadioOnStateListener", "onImsCapabilityChanged: ok to call!");
            onComplete(true);
            cleanup();
            return;
        }
        Rlog.d("RadioOnStateListener", "onImsCapabilityChanged: not ready to call yet, keep waiting.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTimeoutCallbackTimeout() {
        Phone phone = this.mPhone;
        if (phone == null) {
            return;
        }
        if (onTimeout(phone.getServiceState().getState(), this.mPhone.isVoiceOverCellularImsEnabled())) {
            Rlog.d("RadioOnStateListener", "onTimeout: ok to call!");
            onComplete(true);
            cleanup();
        } else if (this.mNumRetriesSoFar > MAX_NUM_RETRIES) {
            Rlog.w("RadioOnStateListener", "onTimeout: Hit MAX_NUM_RETRIES; giving up.");
            cleanup();
        } else {
            Rlog.d("RadioOnStateListener", "onTimeout: not ready to call yet, keep waiting.");
            startOnTimeoutCallbackTimer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onServiceStateChanged(ServiceState serviceState) {
        Phone phone = this.mPhone;
        if (phone == null) {
            return;
        }
        Rlog.d("RadioOnStateListener", String.format("onServiceStateChanged(), new state = %s, Phone = %s", serviceState, Integer.valueOf(phone.getPhoneId())));
        if (this.mWaitForRadioOffFirst) {
            Rlog.i("RadioOnStateListener", "onServiceStateChanged: need to wait for radio off first.");
        } else {
            if (isOkToCall(serviceState.getState(), this.mPhone.isVoiceOverCellularImsEnabled())) {
                Rlog.d("RadioOnStateListener", "onServiceStateChanged: ok to call!");
                onComplete(true);
                cleanup();
                return;
            }
            Rlog.d("RadioOnStateListener", "onServiceStateChanged: not ready to call yet, keep waiting.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRadioOn() {
        Phone phone = this.mPhone;
        if (phone == null) {
            return;
        }
        this.mRadioStateFine = true;
        ServiceState serviceState = phone.getServiceState();
        Rlog.d("RadioOnStateListener", String.format("onRadioOn, state = %s, Phone = %s", serviceState, Integer.valueOf(this.mPhone.getPhoneId())));
        if (isOkToCall(serviceState.getState(), this.mPhone.isVoiceOverCellularImsEnabled())) {
            onComplete(true);
            cleanup();
        } else {
            Rlog.d("RadioOnStateListener", "onRadioOn: not ready to call yet, keep waiting.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSatelliteEnabledChanged() {
        Phone phone = this.mPhone;
        if (phone == null) {
            return;
        }
        if (isOkToCall(phone.getServiceState().getState(), this.mPhone.isVoiceOverCellularImsEnabled())) {
            onComplete(true);
            cleanup();
        } else {
            Rlog.d("RadioOnStateListener", "onSatelliteEnabledChanged: not ready to call yet, keep waiting.");
        }
    }

    private boolean isOkToCall(int i, boolean z) {
        if (this.mWaitForRadioOffFirst) {
            Rlog.i("RadioOnStateListener", "isOkToCall: need to wait for radio off first.");
            return false;
        }
        Callback callback = this.mCallback;
        if (callback == null) {
            return false;
        }
        return callback.isOkToCall(this.mPhone, i, z);
    }

    private boolean onTimeout(int i, boolean z) {
        Callback callback = this.mCallback;
        if (callback == null) {
            return false;
        }
        return callback.onTimeout(this.mPhone, i, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRetryTimeout() {
        Phone phone = this.mPhone;
        if (phone == null) {
            return;
        }
        int state = phone.getServiceState().getState();
        Rlog.d("RadioOnStateListener", String.format(Locale.getDefault(), "onRetryTimeout():  phone state = %s, service state = %d, retries = %d.", this.mPhone.getState(), Integer.valueOf(state), Integer.valueOf(this.mNumRetriesSoFar)));
        if (this.mWaitForRadioOffFirst) {
            Rlog.i("RadioOnStateListener", "onServiceStateChanged: need to wait for radio off first.");
            int i = this.mNumRetriesSoFar + 1;
            this.mNumRetriesSoFar = i;
            if (i > MAX_NUM_RETRIES) {
                Rlog.w("RadioOnStateListener", "Hit MAX_NUM_RETRIES in waiting for radio off first.");
                this.mWaitForRadioOffFirst = false;
                this.mNumRetriesSoFar = 0;
            } else {
                startRetryTimer();
                return;
            }
        }
        if (isOkToCall(state, this.mPhone.isVoiceOverCellularImsEnabled())) {
            Rlog.d("RadioOnStateListener", "onRetryTimeout: Radio is on. Cleaning up.");
            onComplete(true);
            cleanup();
            return;
        }
        this.mNumRetriesSoFar++;
        Rlog.d("RadioOnStateListener", "mNumRetriesSoFar is now " + this.mNumRetriesSoFar);
        if (this.mNumRetriesSoFar <= MAX_NUM_RETRIES) {
            Rlog.d("RadioOnStateListener", "Trying (again) to turn the radio on and satellite modem off.");
            this.mPhone.setRadioPower(true, this.mForEmergencyCall, this.mSelectedPhoneForEmergencyCall, false);
            if (this.mSatelliteController.isSatelliteEnabledOrBeingEnabled()) {
                this.mSatelliteController.requestSatelliteEnabled(false, false, false, new IIntegerConsumer.Stub() { // from class: com.android.internal.telephony.emergency.RadioOnStateListener.3
                    public void accept(int i2) {
                        RadioOnStateListener.this.mHandler.obtainMessage(8).sendToTarget();
                    }
                });
            }
            startRetryTimer();
            return;
        }
        if (this.mHandler.hasMessages(7)) {
            Rlog.w("RadioOnStateListener", "Hit MAX_NUM_RETRIES; waiting onTimeout callback");
        } else {
            Rlog.w("RadioOnStateListener", "Hit MAX_NUM_RETRIES; giving up.");
            cleanup();
        }
    }

    public void cleanup() {
        Rlog.d("RadioOnStateListener", "cleanup()");
        this.mRadioStateFine = false;
        onComplete(false);
        unregisterForServiceStateChanged();
        unregisterForRadioOff();
        unregisterForRadioOn();
        unregisterForSatelliteEnabledChanged();
        cancelRetryTimer();
        unregisterForImsCapabilityChanged();
        this.mHandler.removeMessages(1);
        this.mPhone = null;
        this.mNumRetriesSoFar = 0;
        this.mOnTimeoutCallbackInterval = 0;
    }

    private void startRetryTimer() {
        cancelRetryTimer();
        this.mHandler.sendEmptyMessageDelayed(3, TIME_BETWEEN_RETRIES_MILLIS);
    }

    private void cancelRetryTimer() {
        this.mHandler.removeMessages(3);
    }

    private void registerForServiceStateChanged() {
        unregisterForServiceStateChanged();
        this.mPhone.registerForServiceStateChanged(this.mHandler, 2, null);
    }

    private void unregisterForServiceStateChanged() {
        Phone phone = this.mPhone;
        if (phone != null) {
            phone.unregisterForServiceStateChanged(this.mHandler);
        }
        this.mHandler.removeMessages(2);
    }

    private void registerForRadioOff() {
        this.mPhone.mCi.registerForOffOrNotAvailable(this.mHandler, 5, null);
    }

    private void unregisterForRadioOff() {
        Phone phone = this.mPhone;
        if (phone != null) {
            phone.mCi.unregisterForOffOrNotAvailable(this.mHandler);
        }
        this.mHandler.removeMessages(5);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerForRadioOn() {
        unregisterForRadioOff();
        this.mPhone.mCi.registerForOn(this.mHandler, 4, null);
    }

    private void unregisterForRadioOn() {
        Phone phone = this.mPhone;
        if (phone != null) {
            phone.mCi.unregisterForOn(this.mHandler);
        }
        this.mHandler.removeMessages(4);
    }

    private void registerForSatelliteEnabledChanged() {
        this.mSatelliteController.registerForSatelliteModemStateChanged(this.mSatelliteCallback);
    }

    private void unregisterForSatelliteEnabledChanged() {
        this.mSatelliteController.unregisterForModemStateChanged(this.mSatelliteCallback);
        this.mHandler.removeMessages(8);
    }

    private void registerForImsCapabilityChanged() {
        unregisterForImsCapabilityChanged();
        this.mPhone.getServiceStateTracker().registerForImsCapabilityChanged(this.mHandler, 6, null);
    }

    private void unregisterForImsCapabilityChanged() {
        Phone phone = this.mPhone;
        if (phone != null) {
            phone.getServiceStateTracker().unregisterForImsCapabilityChanged(this.mHandler);
        }
        this.mHandler.removeMessages(6);
    }

    private void startOnTimeoutCallbackTimer() {
        Rlog.d("RadioOnStateListener", "startOnTimeoutCallbackTimer: mOnTimeoutCallbackInterval=" + this.mOnTimeoutCallbackInterval);
        this.mHandler.removeMessages(7);
        int i = this.mOnTimeoutCallbackInterval;
        if (i > 0) {
            this.mHandler.sendEmptyMessageDelayed(7, i);
        }
    }

    private void onComplete(boolean z) {
        Callback callback = this.mCallback;
        if (callback != null) {
            this.mCallback = null;
            callback.onComplete(this, z);
        }
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public void setMaxNumRetries(int i) {
        MAX_NUM_RETRIES = i;
    }

    public void setTimeBetweenRetriesMillis(long j) {
        TIME_BETWEEN_RETRIES_MILLIS = j;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass().equals(obj.getClass())) {
            RadioOnStateListener radioOnStateListener = (RadioOnStateListener) obj;
            if (this.mNumRetriesSoFar != radioOnStateListener.mNumRetriesSoFar) {
                return false;
            }
            Callback callback = this.mCallback;
            Callback callback2 = radioOnStateListener.mCallback;
            if (callback == null ? callback2 != null : !callback.equals(callback2)) {
                return false;
            }
            Phone phone = this.mPhone;
            Phone phone2 = radioOnStateListener.mPhone;
            if (phone != null) {
                return phone.equals(phone2);
            }
            if (phone2 == null) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int i = (NtnRILConstants.MI_NTN_RIL_REQUEST_START_HANDOVER + this.mNumRetriesSoFar) * 31;
        Callback callback = this.mCallback;
        int iHashCode = (i + (callback == null ? 0 : callback.hashCode())) * 31;
        Phone phone = this.mPhone;
        return iHashCode + (phone != null ? phone.hashCode() : 0);
    }

    void setWaitForRadioOffFirst(boolean z) {
        this.mWaitForRadioOffFirst = z;
    }
}
