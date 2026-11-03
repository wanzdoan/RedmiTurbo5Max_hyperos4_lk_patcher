package com.android.internal.telephony.emergency;

import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.CallWaitingController;
import com.android.internal.telephony.IIntegerConsumer;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class RadioOnHelper implements RadioOnStateListener.Callback {
    private RadioOnStateListener.Callback mCallback;
    private final Context mContext;
    private List mInProgressListeners = new ArrayList(2);
    private boolean mIsRadioReady;
    private List mListeners;

    protected boolean shouldSkipListenerForPhone(Phone phone, boolean z) {
        return false;
    }

    public RadioOnHelper(Context context) {
        this.mContext = context;
    }

    private void setupListeners() {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList(2);
        }
        int activeModemCount = TelephonyManager.from(this.mContext).getActiveModemCount();
        while (this.mListeners.size() < activeModemCount) {
            this.mListeners.add(new RadioOnStateListener());
        }
        while (this.mListeners.size() > activeModemCount) {
            List list = this.mListeners;
            ((RadioOnStateListener) list.get(list.size() - 1)).cleanup();
            List list2 = this.mListeners;
            list2.remove(list2.size() - 1);
        }
    }

    public void triggerRadioOnAndListen(RadioOnStateListener.Callback callback, boolean z, Phone phone, boolean z2, int i) {
        RadioOnHelper radioOnHelper;
        boolean z3;
        Rlog.d("RadioOnStateListener", "triggerRadioOnAndListen, forEmergencyCall: " + z);
        setupListeners();
        this.mCallback = callback;
        this.mInProgressListeners.clear();
        this.mIsRadioReady = false;
        int i2 = 0;
        while (i2 < TelephonyManager.from(this.mContext).getActiveModemCount()) {
            Phone phone2 = PhoneFactory.getPhone(i2);
            if (phone2 == null) {
                radioOnHelper = this;
                z3 = z;
            } else if (this.shouldSkipListenerForPhone(phone2, z)) {
                Rlog.d("RadioOnStateListener", "Skip listener for phone " + phone2.getPhoneId());
                radioOnHelper = this;
                z3 = z;
            } else {
                int i3 = phone2 == phone ? i : 0;
                this.mInProgressListeners.add((RadioOnStateListener) this.mListeners.get(i2));
                boolean z4 = Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) > 0 && phone2.isRadioOn();
                ServiceStateTracker serviceStateTracker = phone2.getServiceStateTracker();
                if (serviceStateTracker != null && serviceStateTracker.hasPendingRadioPowerOff()) {
                    z4 = true;
                }
                ((RadioOnStateListener) this.mListeners.get(i2)).setWaitForRadioOffFirst(z4);
                radioOnHelper = this;
                z3 = z;
                ((RadioOnStateListener) this.mListeners.get(i2)).waitForRadioOn(phone2, radioOnHelper, z3, z && phone2 == phone, i3);
            }
            i2++;
            this = radioOnHelper;
            z = z3;
        }
        RadioOnHelper radioOnHelper2 = this;
        radioOnHelper2.powerOnRadio(z, phone, z2);
        if (SatelliteController.getInstance().isSatelliteEnabledOrBeingEnabled()) {
            radioOnHelper2.powerOffSatellite();
        }
    }

    private void powerOnRadio(boolean z, Phone phone, boolean z2) {
        Phone[] phones = PhoneFactory.getPhones();
        int length = phones.length;
        for (int i = 0; i < length; i++) {
            Phone phone2 = phones[i];
            Rlog.d("RadioOnStateListener", "powerOnRadio, enabling Radio");
            if (z2) {
                phone2.setRadioPowerOnForTestEmergencyCall(phone2 == phone);
            } else {
                phone2.setRadioPower(true, z, phone2 == phone, true);
            }
        }
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) > 0) {
            Rlog.d("RadioOnStateListener", "==> Turning off airplane mode for emergency call.");
            Settings.Global.putInt(this.mContext.getContentResolver(), "airplane_mode_on", 0);
            Intent intent = new Intent("android.intent.action.AIRPLANE_MODE");
            intent.putExtra(CallWaitingController.KEY_STATE, false);
            this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
        }
    }

    private void powerOffSatellite() {
        Rlog.d("RadioOnStateListener", "powerOffSatellite: disabling Satellite");
        SatelliteController.getInstance().requestSatelliteEnabled(false, false, false, new IIntegerConsumer.Stub(this) { // from class: com.android.internal.telephony.emergency.RadioOnHelper.1
            public void accept(int i) {
                Rlog.d("RadioOnStateListener", "powerOffSatellite: result = " + i);
            }
        });
    }

    @Override // com.android.internal.telephony.emergency.RadioOnStateListener.Callback
    public void onComplete(RadioOnStateListener radioOnStateListener, boolean z) {
        this.mIsRadioReady = z | this.mIsRadioReady;
        this.mInProgressListeners.remove(radioOnStateListener);
        if (this.mCallback == null || !this.mInProgressListeners.isEmpty()) {
            return;
        }
        this.mCallback.onComplete(null, this.mIsRadioReady);
    }

    @Override // com.android.internal.telephony.emergency.RadioOnStateListener.Callback
    public boolean isOkToCall(Phone phone, int i, boolean z) {
        RadioOnStateListener.Callback callback = this.mCallback;
        if (callback == null) {
            return false;
        }
        return callback.isOkToCall(phone, i, z);
    }

    @Override // com.android.internal.telephony.emergency.RadioOnStateListener.Callback
    public boolean onTimeout(Phone phone, int i, boolean z) {
        RadioOnStateListener.Callback callback = this.mCallback;
        if (callback == null) {
            return false;
        }
        return callback.onTimeout(phone, i, z);
    }

    public void cleanup() {
        Rlog.d("RadioOnStateListener", "cleanup()");
        ArrayList arrayList = new ArrayList(this.mInProgressListeners);
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            ((RadioOnStateListener) obj).cleanup();
        }
    }
}
