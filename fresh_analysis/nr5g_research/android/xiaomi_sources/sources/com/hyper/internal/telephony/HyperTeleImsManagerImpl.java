package com.hyper.internal.telephony;

import com.android.internal.telephony.IHyperTeleImsManager;

/* JADX INFO: loaded from: classes.dex */
public class HyperTeleImsManagerImpl implements IHyperTeleImsManager {
    private static HyperTeleImsManagerImpl sInstance;

    private HyperTeleImsManagerImpl() {
    }

    public static HyperTeleImsManagerImpl getInstance() {
        HyperTeleImsManagerImpl hyperTeleImsManagerImpl;
        synchronized (HyperTeleImsManagerImpl.class) {
            if (sInstance == null) {
                sInstance = new HyperTeleImsManagerImpl();
            }
            hyperTeleImsManagerImpl = sInstance;
        }
        return hyperTeleImsManagerImpl;
    }

    public boolean isVowifiConnected(int phoneId) {
        return HyperTelephonyInternalManager.getInstance().getHyperTeleImsManager().isVowifiConnected(phoneId);
    }
}
