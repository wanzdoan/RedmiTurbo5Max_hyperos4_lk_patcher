package com.android.services.telephony.domainselection;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SubscriptionManager;
import android.telephony.ims.ImsException;
import android.telephony.ims.ImsManager;
import android.telephony.ims.ImsMmTelManager;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsRegistrationAttributes;
import android.telephony.ims.ImsStateCallback;
import android.telephony.ims.RegistrationManager;
import android.util.Log;
import androidx.emoji2.text.ConcurrencyHelpers$$ExternalSyntheticLambda0;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class ImsEmergencyRegistrationStateHelper {
    private static final String TAG = "ImsEmergencyRegistrationStateHelper";
    private final Context mContext;
    private final Handler mHandler;
    private boolean mImsEmergencyRegistered;
    private ImsStateCallback mImsStateCallback;
    private ImsMmTelManager mMmTelManager;
    private RegistrationManager.RegistrationCallback mRegistrationCallback;
    private final int mSlotId;
    private final int mSubId;

    public ImsEmergencyRegistrationStateHelper(Context context, int i, int i2, Looper looper) {
        this.mContext = context;
        this.mSlotId = i;
        this.mSubId = i2;
        this.mHandler = new Handler(looper);
    }

    public void destroy() {
        stopListeningForImsEmergencyRegistrationState();
        this.mHandler.removeCallbacksAndMessages(null);
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public boolean isImsEmergencyRegistered() {
        return this.mImsEmergencyRegistered;
    }

    public void start() {
        startListeningForImsEmergencyRegistrationState();
    }

    private void startListeningForImsEmergencyRegistrationState() {
        if (SubscriptionManager.isValidSubscriptionId(this.mSubId)) {
            this.mMmTelManager = ((ImsManager) this.mContext.getSystemService(ImsManager.class)).getImsMmTelManager(this.mSubId);
            this.mImsEmergencyRegistered = false;
            registerImsStateCallback();
        }
    }

    private void stopListeningForImsEmergencyRegistrationState() {
        if (this.mMmTelManager != null) {
            unregisterImsEmergencyRegistrationCallback();
            unregisterImsStateCallback();
            this.mMmTelManager = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerImsStateCallback() {
        if (this.mImsStateCallback != null) {
            loge("ImsStateCallback is already registered for sub-" + this.mSubId);
            return;
        }
        this.mImsStateCallback = new AnonymousClass1();
        try {
            ImsMmTelManager imsMmTelManager = this.mMmTelManager;
            Handler handler = this.mHandler;
            Objects.requireNonNull(handler);
            imsMmTelManager.registerImsStateCallback(new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), this.mImsStateCallback);
        } catch (ImsException e) {
            loge("Exception when registering ImsStateCallback: " + e);
            this.mImsStateCallback = null;
        }
    }

    /* JADX INFO: renamed from: com.android.services.telephony.domainselection.ImsEmergencyRegistrationStateHelper$1, reason: invalid class name */
    class AnonymousClass1 extends ImsStateCallback {
        AnonymousClass1() {
        }

        @Override // android.telephony.ims.ImsStateCallback
        public void onUnavailable(int i) {
            ImsEmergencyRegistrationStateHelper.this.unregisterImsEmergencyRegistrationCallback();
        }

        @Override // android.telephony.ims.ImsStateCallback
        public void onAvailable() {
            ImsEmergencyRegistrationStateHelper.this.registerImsEmergencyRegistrationCallback();
        }

        @Override // android.telephony.ims.ImsStateCallback
        public void onError() {
            ImsEmergencyRegistrationStateHelper.this.mImsStateCallback = null;
            Handler handler = ImsEmergencyRegistrationStateHelper.this.mHandler;
            final ImsEmergencyRegistrationStateHelper imsEmergencyRegistrationStateHelper = ImsEmergencyRegistrationStateHelper.this;
            handler.postDelayed(new Runnable() { // from class: com.android.services.telephony.domainselection.ImsEmergencyRegistrationStateHelper$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    imsEmergencyRegistrationStateHelper.registerImsStateCallback();
                }
            }, 2000L);
        }
    }

    private void unregisterImsStateCallback() {
        ImsStateCallback imsStateCallback = this.mImsStateCallback;
        if (imsStateCallback != null) {
            try {
                this.mMmTelManager.unregisterImsStateCallback(imsStateCallback);
            } catch (Exception e) {
                logd("Exception when unregistering ImsStateCallback: " + e);
            }
            this.mImsStateCallback = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerImsEmergencyRegistrationCallback() {
        if (this.mRegistrationCallback != null) {
            logd("RegistrationCallback is already registered for sub-" + this.mSubId);
            return;
        }
        this.mRegistrationCallback = new RegistrationManager.RegistrationCallback() { // from class: com.android.services.telephony.domainselection.ImsEmergencyRegistrationStateHelper.2
            @Override // android.telephony.ims.RegistrationManager.RegistrationCallback
            public void onRegistered(ImsRegistrationAttributes imsRegistrationAttributes) {
                ImsEmergencyRegistrationStateHelper.this.mImsEmergencyRegistered = true;
            }

            @Override // android.telephony.ims.RegistrationManager.RegistrationCallback
            public void onUnregistered(ImsReasonInfo imsReasonInfo) {
                ImsEmergencyRegistrationStateHelper.this.mImsEmergencyRegistered = false;
            }
        };
        try {
            ImsMmTelManager imsMmTelManager = this.mMmTelManager;
            Handler handler = this.mHandler;
            Objects.requireNonNull(handler);
            imsMmTelManager.registerImsEmergencyRegistrationCallback(new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler), this.mRegistrationCallback);
        } catch (ImsException e) {
            loge("Exception when registering RegistrationCallback: " + e);
            this.mRegistrationCallback = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterImsEmergencyRegistrationCallback() {
        RegistrationManager.RegistrationCallback registrationCallback = this.mRegistrationCallback;
        if (registrationCallback != null) {
            try {
                this.mMmTelManager.unregisterImsEmergencyRegistrationCallback(registrationCallback);
            } catch (Exception e) {
                logd("Exception when unregistering RegistrationCallback: " + e);
            }
            this.mRegistrationCallback = null;
        }
    }

    private void logd(String str) {
        Log.d(TAG, "[" + this.mSlotId + "|" + this.mSubId + "] " + str);
    }

    private void loge(String str) {
        Log.e(TAG, "[" + this.mSlotId + "|" + this.mSubId + "] " + str);
    }
}
