package com.hyper.internal.telephony;

import android.content.Context;
import android.content.res.Resources;
import android.os.Message;
import com.android.internal.telephony.IHyperSsManager;
import com.android.internal.telephony.uicc.AdnRecord;
import com.android.internal.telephony.uicc.AdnRecordCache;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class HyperSsManagerImpl implements IHyperSsManager {
    private static HyperSsManagerImpl sInstance;

    private HyperSsManagerImpl() {
    }

    public static HyperSsManagerImpl getInstance() {
        HyperSsManagerImpl hyperSsManagerImpl;
        synchronized (HyperSsManagerImpl.class) {
            if (sInstance == null) {
                sInstance = new HyperSsManagerImpl();
            }
            hyperSsManagerImpl = sInstance;
        }
        return hyperSsManagerImpl;
    }

    public int getCallForwardServiceClass(Context context, int subId, int serviceClass) {
        return HyperTelephonyInternalManager.getInstance().getHyperSsManager().getCallForwardServiceClass(context, subId, serviceClass);
    }

    public void setServiceClass(int serviceClass) {
        HyperTelephonyInternalManager.getInstance().getHyperSsManager().setServiceClass(serviceClass);
    }

    public Resources getResourcesForSubId(Context context, int subId) {
        return HyperTelephonyInternalManager.getInstance().getHyperSsManager().getResourcesForSubId(context, subId);
    }

    public void updateVideoCFNotification(Context context, boolean isVCFEnable, int subId) {
        HyperTelephonyInternalManager.getInstance().getHyperSsManager().updateVideoCFNotification(context, isVCFEnable, subId);
    }

    public CharSequence getCfType(Context context, int subId, String sc) {
        return HyperTelephonyInternalManager.getInstance().getHyperSsManager().getCfType(context, subId, sc);
    }

    public ArrayList<AdnRecord> getAllFdnList(AdnRecordCache adnRecordCache, int phoneId) {
        return HyperTelephonyInternalManager.getInstance().getHyperSsManager().getAllFdnList(adnRecordCache, phoneId);
    }

    public void unRegisterForCallBarringPwdRequiredChanged(int phoneId) {
        HyperTelephonyInternalManager.getInstance().getHyperSsManager().unRegisterForCallBarringPwdRequiredChanged(phoneId);
    }

    public void changeCallBarringPassword(int phoneId, String facility, String oldPwd, String newPwd, Message onComplete) {
        HyperTelephonyInternalManager.getInstance().getHyperSsManager().changeCallBarringPassword(phoneId, facility, oldPwd, newPwd, onComplete);
    }

    public boolean isSkipFdnCheckForUssd(int phoneId, String dialStr) {
        return HyperTelephonyInternalManager.getInstance().getHyperSsManager().isSkipFdnCheckForUssd(phoneId, dialStr);
    }
}
