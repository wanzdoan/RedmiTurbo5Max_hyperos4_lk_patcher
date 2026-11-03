package com.hyper.internal.telephony;

import android.content.Context;
import android.content.Intent;
import com.android.internal.telephony.IHyperTeleSmsManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SmsMessageBase;

/* JADX INFO: loaded from: classes.dex */
public class HyperTeleSmsManagerImpl implements IHyperTeleSmsManager {
    private static HyperTeleSmsManagerImpl sInstance;

    private HyperTeleSmsManagerImpl() {
    }

    public static HyperTeleSmsManagerImpl getInstance() {
        HyperTeleSmsManagerImpl hyperTeleSmsManagerImpl;
        synchronized (HyperTeleSmsManagerImpl.class) {
            if (sInstance == null) {
                sInstance = new HyperTeleSmsManagerImpl();
            }
            hyperTeleSmsManagerImpl = sInstance;
        }
        return hyperTeleSmsManagerImpl;
    }

    public boolean isFdnBlockDisabled(Context context, int subId) {
        return HyperTelephonyInternalManager.getInstance().getHyperTeleSmsManager().isFdnBlockDisabled(context, subId);
    }

    public int getMaxSendRetries(Context context, int subId) {
        return HyperTelephonyInternalManager.getInstance().getHyperTeleSmsManager().getMaxSendRetries(context, subId);
    }

    public int dispatchCarrierTrue(Context context, Phone phone, SmsMessageBase smsb) {
        return HyperTelephonyInternalManager.getInstance().getHyperTeleSmsManager().dispatchCarrierTrue(context, phone, smsb);
    }

    public void sendOmacpBroadcast(Context context, Intent intent) {
        HyperTelephonyInternalManager.getInstance().getHyperTeleSmsManager().sendOmacpBroadcast(context, intent);
    }

    public boolean isVoLTERegisteredForPhone(int phoneId) {
        return HyperTelephonyInternalManager.getInstance().getHyperTeleSmsManager().isVoLTERegisteredForPhone(phoneId);
    }

    public int carrierTureError(Context context, int error, Phone phone) {
        return HyperTelephonyInternalManager.getInstance().getHyperTeleSmsManager().carrierTureError(context, error, phone);
    }
}
