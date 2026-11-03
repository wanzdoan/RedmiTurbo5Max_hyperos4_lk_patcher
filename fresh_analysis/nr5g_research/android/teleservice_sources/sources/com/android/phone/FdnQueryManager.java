package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telecom.Log;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.android.internal.telephony.MiuiIccPhoneBookInterfaceManager;
import com.android.internal.telephony.uicc.MiuiAdnRecord;
import com.android.internal.telephony.uicc.MiuiUiccControllerImpl;
import java.util.List;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.MiuiTelephony;
import miui.telephony.PhoneDebug;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miui.telephony.SubscriptionManagerEx;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class FdnQueryManager {
    public static final boolean IS_ENABLE;
    private static FdnQueryManager mInstance;
    private ArrayMap mFdnCache = new ArrayMap();
    private BroadcastReceiver mSimStateReceiver = new BroadcastReceiver() { // from class: com.android.phone.FdnQueryManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra("ss");
            int intExtra = intent.getIntExtra(SubscriptionManagerEx.PHONE_KEY, -1);
            if ("LOADED".equals(stringExtra)) {
                FdnQueryManager.getInstance().updateFdnCache();
                Log.i("FdnQueryManager", "onReceive: update fdn cache, slotId = " + intExtra, new Object[0]);
                return;
            }
            if ("ABSENT".equals(stringExtra)) {
                FdnQueryManager.this.mFdnCache.clear();
                Log.i("FdnQueryManager", "onReceive: clear fdn cache", new Object[0]);
            }
        }
    };
    private BroadcastReceiver mFdnChangeReceiver = new BroadcastReceiver() { // from class: com.android.phone.FdnQueryManager.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("action_fdn_change".equals(intent.getAction())) {
                FdnQueryManager.this.updateFdnCache();
            }
            Log.i("FdnQueryManager", "onReceive: fdn change", new Object[0]);
        }
    };

    static {
        IS_ENABLE = TelephonyManager.isGoogleCsp() && isSupportFdnQuery();
        mInstance = new FdnQueryManager();
    }

    private FdnQueryManager() {
    }

    public static synchronized FdnQueryManager getInstance() {
        return mInstance;
    }

    public void init(Context context) {
        if (IS_ENABLE) {
            context.registerReceiver(this.mSimStateReceiver, new IntentFilter("android.intent.action.SIM_STATE_CHANGED"), 2);
            context.registerReceiver(this.mFdnChangeReceiver, new IntentFilter("action_fdn_change"), 2);
            updateFdnCache();
            Log.i("FdnQueryManager", "init", new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFdnCache() {
        this.mFdnCache.clear();
        final List allSubscriptionInfoList = SubscriptionManager.getDefault().getAllSubscriptionInfoList();
        if (allSubscriptionInfoList == null || allSubscriptionInfoList.size() == 0) {
            return;
        }
        new Thread(new Runnable() { // from class: com.android.phone.FdnQueryManager.3
            @Override // java.lang.Runnable
            public void run() {
                for (SubscriptionInfo subscriptionInfo : allSubscriptionInfoList) {
                    FdnQueryManager.this.updateFdnCacheForSlotId(subscriptionInfo != null ? subscriptionInfo.getSlotId() : -1);
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFdnCacheForSlotId(int i) {
        if (i < 0) {
            return;
        }
        List<MiuiAdnRecord> adnRecordsInEf = null;
        try {
            MiuiIccPhoneBookInterfaceManager iccPhoneBookService = getIccPhoneBookService(i);
            if (iccPhoneBookService != null) {
                adnRecordsInEf = iccPhoneBookService.getAdnRecordsInEf(28475);
            }
        } catch (SecurityException e) {
            Log.i("FdnQueryManager", android.util.Log.getStackTraceString(e), new Object[0]);
        }
        if (adnRecordsInEf == null || adnRecordsInEf.size() <= 0) {
            return;
        }
        for (MiuiAdnRecord miuiAdnRecord : adnRecordsInEf) {
            if (!TextUtils.isEmpty(miuiAdnRecord.getNumber()) && !TextUtils.isEmpty(miuiAdnRecord.getAlphaTag())) {
                this.mFdnCache.put(miuiAdnRecord.getNumber(), miuiAdnRecord.getAlphaTag());
            }
        }
    }

    private MiuiIccPhoneBookInterfaceManager getIccPhoneBookService(int i) {
        MiuiUiccControllerImpl miuiUiccControllerImpl;
        if (SubscriptionManager.isRealSlotId(i) && (miuiUiccControllerImpl = (MiuiUiccControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiUiccControllerImpl.class, i)) != null) {
            return miuiUiccControllerImpl.getMiuiIccPhoneBookInterfaceManager();
        }
        return null;
    }

    public String queryFdn(String str) {
        if (!IS_ENABLE || TextUtils.isEmpty(str)) {
            return null;
        }
        String str2 = (String) this.mFdnCache.get(str);
        printLog("queryFdn: number = " + str + " result = " + str2);
        return str2;
    }

    public static boolean isSupportFdnQuery() {
        if (TelephonyManager.isCustFeatureResolveEnabled()) {
            return HyperOSCustFeatureResolve.getBoolean("support_fdn_query", false);
        }
        return TelephonyManager.isCustForMxTelcel();
    }

    private void printLog(String str) {
        if (PhoneDebug.VDBG) {
            Log.i("FdnQueryManager", str, new Object[0]);
        }
    }
}
