package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.telephony.RadioAccessFamily;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.ims.ImsManager;
import com.android.internal.telephony.Phone;
import com.android.phone.networkmode.NetworkMode;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.SubscriptionManager;
import miui.telephony.SubscriptionManagerEx;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class OtaLteUpdateManager {
    private static final String LOG_TAG = "OtaLteUpdateManager";
    private static final String[] MCCMNC_CODES = {"73003", "71610", "732101", "74001", "37002", "70401", "70601", "708001", "71021", "71203", "71403", "74810", "722310", "330110", "74402"};
    private static OtaLteUpdateManager sInstance;
    private Context mContext;
    private Handler mHandler = new Handler() { // from class: com.android.phone.OtaLteUpdateManager.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                OtaLteUpdateManager.this.forceRecoveryHighRat(message.arg1);
                return;
            }
            if (i == 2) {
                OtaLteUpdateManager.this.forceEnableVolteSwitch(message.arg1);
            } else {
                if (i != 3) {
                    return;
                }
                OtaLteUpdateManager.this.onCarrierConfigChanged(message.arg1, message.arg2);
            }
        }
    };
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.OtaLteUpdateManager.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Rlog.d(OtaLteUpdateManager.LOG_TAG, "receive action : " + action);
            if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                String stringExtra = intent.getStringExtra("ss");
                int intExtra = intent.getIntExtra(SubscriptionManagerEx.PHONE_KEY, -1);
                Rlog.d(OtaLteUpdateManager.LOG_TAG, "receive slotId=" + intExtra + ",iccState= " + stringExtra);
                if ("LOADED".equals(stringExtra)) {
                    MiuiPhoneUtils.getPhone(intExtra);
                    Rlog.d(OtaLteUpdateManager.LOG_TAG, "receive simload slotId=" + intExtra);
                    return;
                }
                if ("ABSENT".equals(stringExtra)) {
                    Rlog.d(OtaLteUpdateManager.LOG_TAG, "receive ABSENT slotId=" + intExtra);
                    OtaLteUpdateManager.this.handleClaroRat(intExtra);
                    return;
                }
                return;
            }
            if (action.equals("android.telephony.action.CARRIER_CONFIG_CHANGED")) {
                int intExtra2 = intent.getIntExtra(SubscriptionManagerEx.PHONE_KEY, -1);
                int intExtra3 = intent.getIntExtra(SubscriptionManager.SUBSCRIPTION_KEY, SubscriptionManager.INVALID_SUBSCRIPTION_ID);
                Rlog.d(OtaLteUpdateManager.LOG_TAG, "receive ACTION_CARRIER_CONFIG_CHANGED phoneId=" + intExtra2 + ",subIdForCarrier =" + intExtra3);
                if (SubscriptionManager.isValidSlotId(intExtra2)) {
                    Message messageObtainMessage = OtaLteUpdateManager.this.mHandler.obtainMessage(3);
                    messageObtainMessage.arg1 = intExtra2;
                    messageObtainMessage.arg2 = intExtra3;
                    OtaLteUpdateManager.this.mHandler.sendMessage(messageObtainMessage);
                }
            }
        }
    };

    public OtaLteUpdateManager(Context context) {
        this.mContext = context;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
        context.registerReceiver(this.mReceiver, intentFilter, 2);
    }

    public static OtaLteUpdateManager getInstance() {
        return sInstance;
    }

    public static void init(Context context) {
        if (sInstance == null) {
            sInstance = new OtaLteUpdateManager(context);
        }
    }

    public void handleClaroRat(int i) {
        if (SubscriptionManager.isValidSlotId(i)) {
            String str = LOG_TAG;
            Rlog.d(str, "handleClaroRat phoneId: " + i);
            if (shouldForceUpdateRat(i)) {
                Handler handler = this.mHandler;
                handler.sendMessageDelayed(handler.obtainMessage(1, i, i), 0L);
                Handler handler2 = this.mHandler;
                handler2.sendMessageDelayed(handler2.obtainMessage(2, i, i), 1000L);
                return;
            }
            Rlog.d(str, "do not need to force update rat phoneId: " + i);
        }
    }

    public void forceRecoveryHighRat(int i) {
        int lteNetworkModeValue;
        if (SubscriptionManager.isValidSlotId(i)) {
            String str = LOG_TAG;
            Rlog.d(str, "forceRecoveryHighRat phoneId: " + i);
            Phone phone = MiuiPhoneUtils.getPhone(i);
            int preferredNetworkModeFromDb = ModemProvider.getInstance().getPreferredNetworkModeFromDb(i);
            if (FiveGManagerBase.isFiveGCapable() && FiveGManagerBase.getInstance() != null) {
                if (!FiveGManagerBase.getInstance().isUserFiveGEnabled()) {
                    FiveGManagerBase.getInstance().setUserFiveGEnabled(true);
                }
                lteNetworkModeValue = NetworkMode.getInstance().getNrNetworkModeValue(i);
            } else {
                lteNetworkModeValue = NetworkMode.getInstance().getLteNetworkModeValue(i);
            }
            Rlog.d(str, "dbMode: " + preferredNetworkModeFromDb + ",newDbMode=" + lteNetworkModeValue);
            if (lteNetworkModeValue != preferredNetworkModeFromDb) {
                ModemProvider.getInstance().setNetworkModeInDb(i, lteNetworkModeValue);
                phone.setAllowedNetworkTypes(0, RadioAccessFamily.getRafFromNetworkType(lteNetworkModeValue), (Message) null);
            }
        }
    }

    public void forceEnableVolteSwitch(int i) {
        ImsManager imsManager;
        boolean zIsVolteEnabledByPlatformForSlot = MiuiPhoneInterfaceManager.getInstance().isVolteEnabledByPlatformForSlot(i);
        boolean zIsVolteEnabledByUserForSlot = MiuiPhoneInterfaceManager.getInstance().isVolteEnabledByUserForSlot(i);
        Rlog.d(LOG_TAG, "slot: " + i + ", isVolteEnabledByPlatform: " + zIsVolteEnabledByPlatformForSlot + ", isVolteEnabledByUser: " + zIsVolteEnabledByUserForSlot);
        if (!zIsVolteEnabledByPlatformForSlot || zIsVolteEnabledByUserForSlot || (imsManager = ImsManager.getInstance(this.mContext, i)) == null) {
            return;
        }
        imsManager.setEnhanced4gLteModeSetting(true);
    }

    private boolean shouldForceUpdateRat(int i) {
        boolean booleanForPhoneId = false;
        if (TelephonyManager.isCustFeatureResolveEnabled()) {
            booleanForPhoneId = HyperOSCustFeatureResolve.getBooleanForPhoneId(i, "should_force_update_rat", false);
        } else {
            String operatorNumeric = MiuiPhoneUtils.getPhone(i).getOperatorNumeric();
            for (String str : MCCMNC_CODES) {
                if (TextUtils.equals(operatorNumeric, str)) {
                    booleanForPhoneId = true;
                    break;
                }
            }
        }
        Rlog.d(LOG_TAG, "forceUpdateRat : " + booleanForPhoneId);
        return booleanForPhoneId;
    }

    public void onCarrierConfigChanged(int i, int i2) {
        if (SubscriptionManager.isValidSlotId(i) && shouldForceUpdateRat(i) && android.telephony.TelephonyManager.getSimStateForSlotIndex(i) == 10) {
            forceEnableVolteSwitch(i);
        }
    }
}
