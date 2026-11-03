package com.android.phone.ims;

import android.content.Context;
import android.os.Handler;
import android.telephony.Rlog;
import android.telephony.ims.ImsMmTelManager;
import com.android.ims.FeatureConnector;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiImsRegistrationMonitor implements FeatureConnector.Listener {
    private final String TAG;
    private final FeatureConnector mConnector;
    private Context mContext;
    private ImsManager mImsManager;
    private boolean mIsRegisteredForImsRadioTechChange;
    private final ImsMmTelManager.RegistrationCallback mRegistrationCallback;
    private final int mSlotId;

    public MiuiImsRegistrationMonitor(Context context, int i, ImsMmTelManager.RegistrationCallback registrationCallback) {
        String simpleName = MiuiImsRegistrationMonitor.class.getSimpleName();
        this.TAG = simpleName;
        this.mContext = null;
        this.mIsRegisteredForImsRadioTechChange = false;
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mSlotId = i;
        this.mRegistrationCallback = registrationCallback;
        this.mConnector = ImsManager.getConnector(applicationContext, i, simpleName, this, applicationContext.getMainExecutor());
        connect();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connect() {
        FeatureConnector featureConnector = this.mConnector;
        if (featureConnector != null) {
            featureConnector.connect();
        }
    }

    public void connectionReady(ImsManager imsManager, int i) {
        log(this.TAG + ", connectionReady... i = " + i);
        this.mImsManager = imsManager;
        registerListener();
    }

    public void connectionUnavailable(int i) {
        log(this.TAG + ", connectionUnavailable...");
        if (i == 3) {
            new Handler().postDelayed(new Runnable() { // from class: com.android.phone.ims.MiuiImsRegistrationMonitor.1
                @Override // java.lang.Runnable
                public void run() {
                    MiuiImsRegistrationMonitor.this.connect();
                }
            }, 3000L);
        }
        unregisterListener();
    }

    private void registerListener() {
        ImsManager imsManager = this.mImsManager;
        if (imsManager == null) {
            log(this.TAG + ", registerListener: mImsManager is null");
            return;
        }
        try {
            if (this.mIsRegisteredForImsRadioTechChange) {
                return;
            }
            imsManager.addRegistrationCallback(this.mRegistrationCallback, this.mContext.getMainExecutor());
            this.mIsRegisteredForImsRadioTechChange = true;
            log(this.TAG + ", registerListener: add callback for mSlotId = " + this.mSlotId + " mImsManager = " + this.mImsManager);
        } catch (ImsException e) {
            this.mIsRegisteredForImsRadioTechChange = false;
            log(this.TAG + ", registerListener: " + e);
        }
    }

    private void unregisterListener() {
        ImsManager imsManager = this.mImsManager;
        if (imsManager == null) {
            log(this.TAG + ", unregisterListener: mImsManager is null");
            return;
        }
        imsManager.removeRegistrationListener(this.mRegistrationCallback);
        this.mIsRegisteredForImsRadioTechChange = false;
        log(this.TAG + ", unregisterListener: remove ims registration callback for mSlotId = " + this.mSlotId + " mImsManager = " + this.mImsManager);
    }

    protected void log(String str) {
        Rlog.d(this.TAG + "[" + this.mSlotId + "]", str);
    }
}
