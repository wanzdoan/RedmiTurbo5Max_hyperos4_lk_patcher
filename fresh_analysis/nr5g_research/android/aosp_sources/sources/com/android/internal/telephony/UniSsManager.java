package com.android.internal.telephony;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.os.Message;
import com.android.internal.telephony.uicc.AdnRecord;
import com.android.internal.telephony.uicc.AdnRecordCache;
import com.android.internal.telephony.uicc.IccConstants;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class UniSsManager {
    private static UniSsManager sInstance;

    public void changeCallBarringPassword(int i, String str, String str2, String str3, Message message) {
    }

    public int getCallForwardServiceClass(Context context, int i, int i2) {
        return 1;
    }

    public void getCallForwardingOption(int i, int i2, int i3, String str, Message message) {
    }

    public boolean isCallBarringRequiredPwd(int i) {
        return true;
    }

    public boolean isSkipFdnCheckForUssd(int i, String str) {
        return false;
    }

    public void onImsConnectionReady(int i) {
    }

    public void registerForCallBarringPwdRequiredChanged(int i) {
    }

    public void setCallForwardingOption(int i, int i2, int i3, int i4, String str, int i5, String str2, Message message) {
    }

    public void setServiceClass(int i) {
    }

    public void setSuppServiceFlag(Context context, int i, int i2) {
    }

    public void unRegisterForCallBarringPwdRequiredChanged(int i) {
    }

    public void updateVideoCFNotification(Context context, boolean z, int i) {
    }

    public static UniSsManager getInstance() {
        if (sInstance == null) {
            sInstance = UniTelephonyInjectionFactory.getInstance().getUniSsManager();
        }
        return sInstance;
    }

    public Resources getResourcesForSubId(Context context, int i) {
        return context.getResources();
    }

    public CharSequence getCfType(Context context, int i, String str) {
        return context.getText(R.string.BaMmi);
    }

    public ArrayList<AdnRecord> getAllFdnList(AdnRecordCache adnRecordCache, int i) {
        if (adnRecordCache == null) {
            return null;
        }
        return adnRecordCache.getRecordsIfLoaded(IccConstants.EF_FDN);
    }
}
