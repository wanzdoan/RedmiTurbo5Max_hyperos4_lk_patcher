package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.utils.MiuiThreadUtil;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public class Enable2GManager {
    private static Enable2GManager sInstance;
    private Context mContext;
    private final Handler mHandler = new Handler(MiuiThreadUtil.getWorkThreadLooper()) { // from class: com.android.phone.Enable2GManager.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            Intent intent = (Intent) message.obj;
            int intExtra = intent.getIntExtra("phone", -1);
            int intExtra2 = intent.getIntExtra(SubscriptionManager.SUBSCRIPTION_KEY, -1);
            Phone phone = PhoneFactory.getPhone(intExtra);
            int simStateForSlotIndex = TelephonyManager.getSimStateForSlotIndex(intExtra);
            Enable2GManager.log("ACTION_CARRIER_CONFIG_CHANGED phoneId = " + intExtra + ", subId = " + intExtra2 + ", isDisable2G = " + (true ^ Enable2GManager.this.getEnable2GState(phone)) + ", simState = " + simStateForSlotIndex);
            if (android.telephony.SubscriptionManager.isValidPhoneId(intExtra) && android.telephony.SubscriptionManager.isValidSubscriptionId(intExtra2) && simStateForSlotIndex == 10) {
                Enable2GManager enable2GManager = Enable2GManager.this;
                enable2GManager.setEnable2G(intExtra, enable2GManager.getEnable2GState(phone), null);
            }
        }
    };
    final BroadcastReceiver mReceiver;

    public static Enable2GManager getInstance() {
        return sInstance;
    }

    private Enable2GManager(Context context) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.Enable2GManager.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                Enable2GManager.log("action = " + action);
                if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(action)) {
                    Enable2GManager.this.mHandler.obtainMessage(1, intent).sendToTarget();
                }
            }
        };
        this.mReceiver = broadcastReceiver;
        this.mContext = context;
        this.mContext.registerReceiver(broadcastReceiver, new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED"), 2);
        log("new Enable2GManager");
    }

    public static void init(Context context) {
        log("init");
        if (sInstance == null) {
            sInstance = new Enable2GManager(context);
        }
    }

    public void setEnable2G(int i, boolean z, Message message) {
        Phone phone = PhoneFactory.getPhone(i);
        log("setEnable2G enabled = " + z + ", phoneId = " + i);
        if (phone == null || !android.telephony.SubscriptionManager.isValidSubscriptionId(phone.getSubId())) {
            log("setEnable2G, subId is invaild");
        } else if (!MiuiPhoneUtils.isSupportEnable2GSwitch(phone)) {
            log("setEnable2G, not support feature");
        } else {
            setUserEnable2G(phone, z, message);
            setUserEnable2GState(phone, z);
        }
    }

    private static void setUserEnable2G(Phone phone, boolean z, Message message) {
        long allNetworkTypesBitmask = TelephonyManager.getAllNetworkTypesBitmask();
        phone.getSubId();
        log("setUserEnable2G, AllAllowedNetworkTypes = " + TelephonyManager.getAllNetworkTypesBitmask());
        if (!z) {
            allNetworkTypesBitmask &= -32844;
            log("Disabling 2g. Allowed network types: " + allNetworkTypesBitmask + ", phoneId = " + phone.getPhoneId());
        }
        phone.setAllowedNetworkTypes(3, allNetworkTypesBitmask, message);
    }

    public boolean getEnable2GState(Phone phone) {
        if (phone == null || !android.telephony.SubscriptionManager.isValidSubscriptionId(phone.getSubId())) {
            log("getEnable2GStatus, subId is invaild");
            return true;
        }
        int subId = phone.getSubId();
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "button_2g_enable" + subId, -1);
        boolean z = i == 1;
        if (i == -1) {
            z = !CarrierConfigManagerEx.getBooleanConfigForSubId(this.mContext, subId, "config_disable_2G_switch_bool", false);
        }
        log("getEnable2GStatus, subId = " + subId + ", phoneId = " + phone.getPhoneId() + ", enable2g = " + z + ", enable2gByDatabase = " + i);
        return z;
    }

    private void setUserEnable2GState(Phone phone, boolean z) {
        if (phone == null || !android.telephony.SubscriptionManager.isValidSubscriptionId(phone.getSubId())) {
            log("setEnable2GStatus, subId is invaild");
            return;
        }
        int subId = phone.getSubId();
        Settings.Global.putInt(this.mContext.getContentResolver(), "button_2g_enable" + subId, z ? 1 : 0);
        log("setEnable2GStatus, subId = " + subId + ", phoneId = " + phone.getPhoneId() + ", enabled = " + z);
    }

    public void reset() {
        log("reset");
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null && android.telephony.SubscriptionManager.isValidSubscriptionId(phone.getSubId())) {
                boolean z = !CarrierConfigManagerEx.getBooleanConfigForSubId(this.mContext, phone.getSubId(), "config_disable_2G_switch_bool", false);
                if (getEnable2GState(phone) != z) {
                    setEnable2G(i, z, null);
                }
            } else {
                log("slotId " + i + " reset fail");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("Enable2GManager", str);
    }
}
