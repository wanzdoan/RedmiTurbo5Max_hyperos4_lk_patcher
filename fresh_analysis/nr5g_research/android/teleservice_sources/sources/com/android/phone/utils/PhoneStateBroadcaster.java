package com.android.phone.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.AccessNetworkUtils;
import android.telephony.CellLocation;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.Phone;
import com.android.phone.FiveGServiceInfo;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.module.ITelephonyModule;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class PhoneStateBroadcaster extends Handler implements TelephonyStatAdapter.ICallBack {
    private static boolean isSupportV2;
    private static PhoneStateBroadcaster sInstance;
    private final int[] lastBands;
    private final Context mContext;

    PhoneStateBroadcaster() {
        super(MiuiThreadUtil.getWorkThreadLooper());
        this.lastBands = new int[MiuiPhoneUtils.PHONE_COUNT];
        this.mContext = PhoneGlobals.getInstance();
        TelephonyStatAdapter.registerCallBack(this);
    }

    public static PhoneStateBroadcaster init() {
        if (sInstance == null && (isPhoneStateBroadcasterSupportV1() || isPhoneStateBroadcasterSupportV2())) {
            sInstance = new PhoneStateBroadcaster();
        }
        return sInstance;
    }

    private static boolean isPhoneStateBroadcasterSupportV1() {
        return HyperOSCustFeatureResolve.getBoolean("config_phone_state_broadcaster_support", false);
    }

    private static boolean isPhoneStateBroadcasterSupportV2() {
        boolean zEquals = SarDeviceStateListener.PROP_AUDIO_RECEIVER_STATE_ON.equals(Settings.System.getStringForUser(PhoneGlobals.getInstance().getContentResolver(), "cloud_wifi_modem_coex_report_enabled", -2));
        isSupportV2 = zEquals;
        return zEquals;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 100001:
            case 100002:
                int i = message.arg1;
                Phone phone = MiuiPhoneUtils.getPhone(i);
                if (phone != null) {
                    ServiceState serviceState = phone.getServiceState();
                    int currentBandFromSS = getCurrentBandFromSS(i, serviceState);
                    if (isSupportV2 && this.lastBands[i] != currentBandFromSS) {
                        sendBroasdcast(i, currentBandFromSS, serviceState.getChannelNumber());
                    } else {
                        int i2 = this.lastBands[i];
                        if (i2 != currentBandFromSS && (currentBandFromSS == 40 || i2 == 40)) {
                            sendBroasdcast(i, currentBandFromSS, -1);
                        }
                    }
                    this.lastBands[i] = currentBandFromSS;
                    break;
                }
                break;
        }
    }

    public void onServiceStateChanged(int i, ServiceState serviceState) {
        obtainMessage(100001, i, 0, serviceState).sendToTarget();
    }

    public void onCellLocationChanged(int i, CellLocation cellLocation) {
        obtainMessage(100002, i, 0).sendToTarget();
    }

    private void sendBroasdcast(int i, int i2, int i3) {
        Intent intent = new Intent();
        intent.setAction("com.android.phone.intent.action.MODEM_STATE_CHANGE");
        intent.setPackage("android");
        intent.putExtra("phone", i);
        intent.putExtra("band", i2);
        if (i3 != -1) {
            intent.putExtra("freq", i3);
        }
        this.mContext.sendBroadcast(intent);
        Rlog.d("PhoneStateBroadcaster", "send MODEM_STATE_CHANGE band:" + i2 + " on phone: " + i);
    }

    private int getCurrentBandFromSS(int i, ServiceState serviceState) {
        if (!SubscriptionManager.isValidPhoneId(i) || serviceState == null) {
            return -1;
        }
        FiveGServiceInfo fiveGServiceInfo = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(i);
        boolean zIs5GConnected = fiveGServiceInfo != null ? fiveGServiceInfo.is5GConnected() : false;
        if (!zIs5GConnected) {
            zIs5GConnected = serviceState.getRilDataRadioTechnology() == 20 || serviceState.getRilDataRadioTechnology() == 30;
        }
        int channelNumber = serviceState.getChannelNumber();
        return zIs5GConnected ? com.android.phone.statistics.utils.Utils.getOperationBandForNrarfcn(channelNumber) : AccessNetworkUtils.getOperatingBandForEarfcn(channelNumber);
    }
}
