package com.android.services.telephony;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import com.android.internal.telephony.Phone;

/* JADX INFO: loaded from: classes.dex */
final class PstnPhoneCapabilitiesNotifier {
    private final Handler mHandler;
    private final Listener mListener;
    private final Phone mPhone;

    public interface Listener {
        void onVideoCapabilitiesChanged(boolean z);
    }

    PstnPhoneCapabilitiesNotifier(Phone phone, Listener listener) {
        phone.getClass();
        this.mPhone = phone;
        this.mHandler = new Handler(phone.getLooper()) { // from class: com.android.services.telephony.PstnPhoneCapabilitiesNotifier.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != 1) {
                    return;
                }
                PstnPhoneCapabilitiesNotifier.this.handleVideoCapabilitesChanged((AsyncResult) message.obj);
            }
        };
        this.mListener = listener;
        registerForNotifications();
    }

    void teardown() {
        unregisterForNotifications();
    }

    private void registerForNotifications() {
        if (this.mPhone != null) {
            Log.d(this, "Registering: " + this.mPhone, new Object[0]);
            this.mPhone.registerForVideoCapabilityChanged(this.mHandler, 1, (Object) null);
        }
    }

    private void unregisterForNotifications() {
        if (this.mPhone != null) {
            Log.d(this, "Unregistering: " + this.mPhone, new Object[0]);
            this.mPhone.unregisterForVideoCapabilityChanged(this.mHandler);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleVideoCapabilitesChanged(AsyncResult asyncResult) {
        try {
            boolean zBooleanValue = ((Boolean) asyncResult.result).booleanValue();
            Log.i(this, "handleVideoCapabilitesChanged. Video capability - " + zBooleanValue, new Object[0]);
            this.mListener.onVideoCapabilitiesChanged(zBooleanValue);
        } catch (Exception e) {
            Log.w(this, "handleVideoCapabilitesChanged. Exception=" + e, new Object[0]);
        }
    }
}
