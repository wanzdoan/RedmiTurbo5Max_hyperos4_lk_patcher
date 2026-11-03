package com.hyper.internal.telephony;

import android.content.Context;
import android.telephony.data.DataProfile;
import com.android.internal.telephony.IHyperFrameworkTelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class HyperFrameworkTelephonyManagerImpl implements IHyperFrameworkTelephonyManager {
    private static HyperFrameworkTelephonyManagerImpl sInstance;

    private HyperFrameworkTelephonyManagerImpl() {
    }

    public static HyperFrameworkTelephonyManagerImpl getInstance() {
        HyperFrameworkTelephonyManagerImpl hyperFrameworkTelephonyManagerImpl;
        synchronized (HyperFrameworkTelephonyManagerImpl.class) {
            if (sInstance == null) {
                sInstance = new HyperFrameworkTelephonyManagerImpl();
            }
            hyperFrameworkTelephonyManagerImpl = sInstance;
        }
        return hyperFrameworkTelephonyManagerImpl;
    }

    public boolean isPayStateSupported(Context context, int subId) {
        return HyperTelephonyInternalManager.getInstance().getHyperFrameworkTelephonyManager().isPayStateSupported(context, subId);
    }

    public void popupDataEnabledForSubsidy(int oldSubId, int newSubId) {
        HyperTelephonyInternalManager.getInstance().getHyperFrameworkTelephonyManager().popupDataEnabledForSubsidy(oldSubId, newSubId);
    }

    public boolean isSmsAvaliable(int phoneId) {
        return HyperTelephonyInternalManager.getInstance().getHyperFrameworkTelephonyManager().isSmsAvaliable(phoneId);
    }

    public String replaceIccid(String iccid) {
        return HyperTelephonyInternalManager.getInstance().getHyperFrameworkTelephonyManager().replaceIccid(iccid);
    }

    public boolean canFqdnBeSatisfiedBy(DataProfile dataProfile) {
        return HyperTelephonyInternalManager.getInstance().getHyperFrameworkTelephonyManager().canFqdnBeSatisfiedBy(dataProfile);
    }

    public boolean canIpDescriptorBeSatisfiedBy(DataProfile dataProfile) {
        return HyperTelephonyInternalManager.getInstance().getHyperFrameworkTelephonyManager().canIpDescriptorBeSatisfiedBy(dataProfile);
    }

    public boolean canBeSatisfiedBy(DataProfile dataProfile) {
        return HyperTelephonyInternalManager.getInstance().getHyperFrameworkTelephonyManager().canBeSatisfiedBy(dataProfile);
    }
}
