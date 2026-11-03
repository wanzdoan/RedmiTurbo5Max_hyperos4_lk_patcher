package com.xiaomi.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public abstract class EuiccOsuManagerBase {
    private static final String TAG = "EuiccOsuManagerBase";
    protected static volatile EuiccOsuManagerBase mInstance;
    protected OsuCompleteCallback mCallback;
    protected final Context mContext;
    protected boolean mIsNeedEnableNfc = false;
    private final BroadcastReceiver mReceiver;

    public interface OsuCompleteCallback {
        void onComplete(int i, String str);
    }

    public abstract void doWarmReset();

    protected abstract void executeGetFileTask();

    public abstract void executeUpdate();

    public abstract String getOsuFileName();

    public abstract boolean isOsuFileExisted();

    public abstract byte[] osuOpenChannel(byte[] bArr);

    public abstract byte[] osuSend(byte[] bArr);

    public abstract void setModemDirectMode(boolean z);

    public static EuiccOsuManagerBase getInstance() {
        return mInstance;
    }

    protected EuiccOsuManagerBase(Context context) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.xiaomi.telephony.EuiccOsuManagerBase.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if ("miui.intent.action.OSU_START".equals(intent.getAction())) {
                    EuiccOsuManagerBase.logD("OSU: ACTION_MI_OSU_START");
                    if (EuiccOsuManagerBase.this.isNfcEnable()) {
                        EuiccOsuManagerBase.this.executeGetFileTask();
                        return;
                    }
                    EuiccOsuManagerBase.logD("OSU: NFC is not enabled");
                    if (EuiccOsuManagerBase.this.enableNfc(true)) {
                        EuiccOsuManagerBase euiccOsuManagerBase = EuiccOsuManagerBase.this;
                        euiccOsuManagerBase.mIsNeedEnableNfc = true;
                        euiccOsuManagerBase.executeGetFileTask();
                        return;
                    }
                    EuiccOsuManagerBase.logD("OSU: enable NFC failed");
                }
            }
        };
        this.mReceiver = broadcastReceiver;
        this.mContext = context;
        IntentFilter intentFilter = new IntentFilter();
        if (Settings.Secure.getLong(context.getContentResolver(), "osu_interval_time", -1L) == -1) {
            Settings.Secure.putLong(context.getContentResolver(), "osu_interval_time", 432000L);
        }
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("miui.intent.action.OSU_START");
        context.registerReceiver(broadcastReceiver, intentFilter, 2);
    }

    public void setOsuCompleteCallback(OsuCompleteCallback osuCompleteCallback) {
        this.mCallback = osuCompleteCallback;
    }

    public boolean isNfcEnable() {
        NfcManager nfcManager = (NfcManager) this.mContext.getSystemService("nfc");
        if (nfcManager == null) {
            logD("isNfcEnable nfcManager is null");
            return false;
        }
        NfcAdapter defaultAdapter = nfcManager.getDefaultAdapter();
        if (defaultAdapter == null) {
            logD("isNfcEnable nfcAdapter is null");
            return false;
        }
        return defaultAdapter.isEnabled();
    }

    public boolean enableNfc(boolean z) {
        NfcManager nfcManager = (NfcManager) this.mContext.getSystemService("nfc");
        if (nfcManager == null) {
            logD("enableNfc nfcManager is null");
            return false;
        }
        NfcAdapter defaultAdapter = nfcManager.getDefaultAdapter();
        if (defaultAdapter == null) {
            logD("enableNfc nfcAdapter is null");
            return false;
        }
        if (z) {
            return defaultAdapter.enable();
        }
        return defaultAdapter.disable();
    }

    public boolean isEsimEnabledOnSlot(int i) {
        SubscriptionManager subscriptionManagerFrom = SubscriptionManager.from(this.mContext);
        if (subscriptionManagerFrom == null) {
            return false;
        }
        List<SubscriptionInfo> activeSubscriptionInfoList = subscriptionManagerFrom.getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList == null || activeSubscriptionInfoList.isEmpty()) {
            logD("isEsimEnabledOnSlot activeInfos is null or empty");
            return false;
        }
        for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
            if (subscriptionInfo.getSimSlotIndex() == i && subscriptionInfo.isEmbedded()) {
                return true;
            }
        }
        return false;
    }

    protected static void logD(String str) {
        Rlog.d(TAG, str);
    }

    protected static void logE(String str) {
        Rlog.e(TAG, str);
    }

    public boolean isNeedEnableNfc() {
        return this.mIsNeedEnableNfc;
    }

    public void setIsNeedEnableNfc(boolean z) {
        this.mIsNeedEnableNfc = z;
    }
}
