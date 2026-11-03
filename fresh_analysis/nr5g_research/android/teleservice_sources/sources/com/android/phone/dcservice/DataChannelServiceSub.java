package com.android.phone.dcservice;

import android.os.RemoteException;
import android.util.Log;
import vendor.qti.imsdatachannel.client.ImsDataChannelServiceAvailabilityCallback;
import vendor.qti.imsdatachannel.client.ImsDataChannelServiceManager;

/* JADX INFO: loaded from: classes.dex */
public class DataChannelServiceSub implements ImsDataChannelServiceAvailabilityCallback {
    final String TAG = "DataChannelServiceSub";
    private boolean mDataChannelServiceAvailable = false;
    private ImsDataChannelServiceManager mDcManager;
    private final int mSlotId;

    public DataChannelServiceSub(int i) {
        this.mDcManager = null;
        this.mDcManager = MiDcServiceManager.getInstance();
        this.mSlotId = i;
    }

    public void onServiceConnected() {
        logd("onServiceConnected, calling getAvailability");
        try {
            this.mDcManager.getAvailability(this.mSlotId, this);
        } catch (RemoteException unused) {
            logd("getAvailability RemoteException!!!");
        }
    }

    public void onServiceDisconnected() {
        logd("onServiceDisconnected");
        this.mDataChannelServiceAvailable = false;
    }

    public boolean isDataChannelServiceAvailable() {
        if (!this.mDataChannelServiceAvailable) {
            try {
                this.mDcManager.getAvailability(this.mSlotId, this);
                logd("retry to getAvailability");
            } catch (RemoteException unused) {
                logd("retry getAvailability RemoteException!!!");
            }
        }
        return this.mDataChannelServiceAvailable;
    }

    private void logd(String str) {
        Log.i("DataChannelServiceSub", "[" + this.mSlotId + "]" + str);
    }
}
