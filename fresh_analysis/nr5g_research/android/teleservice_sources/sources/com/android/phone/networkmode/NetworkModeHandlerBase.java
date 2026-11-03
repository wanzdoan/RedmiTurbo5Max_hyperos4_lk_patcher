package com.android.phone.networkmode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.provider.MiuiSettings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.NetworkModeManager;
import com.android.phone.PhoneGlobals;
import com.android.phone.module.ITelephonyModule;
import java.util.List;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miui.telephony.VirtualSimUtils;

/* JADX INFO: loaded from: classes.dex */
public class NetworkModeHandlerBase extends Handler implements DefaultSimManager.DataSlotListener {
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.networkmode.NetworkModeHandlerBase.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            NetworkModeManager.log("action=" + action);
            if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(action)) {
                int intExtra = intent.getIntExtra("phone", SubscriptionManager.INVALID_PHONE_ID);
                int intExtra2 = intent.getIntExtra(SubscriptionManager.SUBSCRIPTION_KEY, SubscriptionManager.INVALID_SUBSCRIPTION_ID);
                int simStateForSlotIndex = TelephonyManager.getSimStateForSlotIndex(intExtra);
                NetworkModeManager.log("ACTION_CARRIER_CONFIG_CHANGED phoneId:" + intExtra + ", subId=" + intExtra2 + ", simState=" + simStateForSlotIndex + ", mDefaultDataSlotId=" + NetworkModeHandlerBase.this.mDefaultDataSlotId);
                if (SubscriptionManager.isValidPhoneId(intExtra) && SubscriptionManager.isValidSubscriptionId(intExtra2) && simStateForSlotIndex == 10) {
                    NetworkModeManager.log("simloaded updateNetworkTypeIfNeeded()");
                    NetworkModeHandlerBase.this.updateNetworkTypeIfNeeded();
                }
            }
        }
    };
    private TelephonyManager mTm = (TelephonyManager) PhoneGlobals.getInstance().getSystemService(TelephonyManager.class);
    protected int mDefaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
    protected String mVirtualSimIccId = getVirtualSimIccId();

    protected void handleSetNetworkTypeDone(Message message) {
    }

    protected void handleSetNetworkTypeRetry(Message message) {
    }

    protected void updateNetworkTypeIfNeeded() {
    }

    public NetworkModeHandlerBase() {
        DefaultSimManager.getInstance().addDataSlotListener(this);
        PhoneGlobals.getInstance().registerReceiver(this.mReceiver, new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED"), 2);
    }

    public void onDataSlotReady(boolean z) {
        removeMessages(0);
        sendEmptyMessageDelayed(0, 100L);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 0) {
            updateNetworkTypeIfNeeded();
        } else if (i == 1) {
            handleSetNetworkTypeDone(message);
        } else {
            if (i != 2) {
                return;
            }
            handleSetNetworkTypeRetry(message);
        }
    }

    protected boolean isReadyToUpdate() {
        List availableSubscriptionInfoList = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList();
        if (availableSubscriptionInfoList == null || availableSubscriptionInfoList.isEmpty()) {
            NetworkModeManager.log("isReadyToUpdate return for empty subinfolist");
            return false;
        }
        Phone[] phones = MiuiPhoneUtils.getPhones();
        boolean z = false;
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (this.mTm.hasIccCard(i)) {
                if (SubscriptionManager.getDefault().getSubscriptionInfoForSlot(i) == null || NetworkModeManager.getPhoneType(i) == 0) {
                    z = true;
                }
                if (TextUtils.isEmpty(MiuiPhoneUtils.getOperatorNumeric(phones[i]))) {
                    z = true;
                }
                if (isDisableSim(i)) {
                    z = false;
                }
                boolean z2 = MiuiPhoneUtils.isSupportGoogleLPAEsim() && !((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isEsimActive();
                NetworkModeManager.log("isGoogleEsimEnableNoActive=" + z2);
                if (z2 && DefaultSimManager.getInstance().isAllSubscriptionInfoCreated(availableSubscriptionInfoList)) {
                    z = false;
                }
                if (z && i == 1 && MiuiPhoneUtils.isEsimCapable() && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isSupportForRegion() && !((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isEsimActive() && DefaultSimManager.getInstance().isAllSubscriptionInfoCreated(availableSubscriptionInfoList)) {
                    NetworkModeManager.log("esim not active, ignore");
                    z = false;
                }
            } else if (SubscriptionManager.getDefault().getSubscriptionInfoForSlot(i) != null) {
                z = true;
            }
            if (z) {
                NetworkModeManager.log("isReadyToUpdate return false");
                removeMessages(0);
                sendEmptyMessageDelayed(0, 2000L);
                return false;
            }
        }
        return true;
    }

    protected void getPreferredNetworkTypes() {
        for (Phone phone : MiuiPhoneUtils.getPhones()) {
            phone.getAllowedNetworkTypesBitmask((Message) null);
        }
    }

    protected String getVirtualSimIccId() {
        if (VirtualSimUtils.getInstance().isVirtualSimEnabled()) {
            return MiuiSettings.VirtualSim.getVirtualSimIccId(PhoneGlobals.getInstance());
        }
        return "";
    }

    protected boolean isDisableSim(int i) {
        List<SubscriptionInfo> availableSubscriptionInfoList = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList();
        if (availableSubscriptionInfoList == null) {
            return false;
        }
        for (SubscriptionInfo subscriptionInfo : availableSubscriptionInfoList) {
            if (subscriptionInfo != null && subscriptionInfo.getSlotId() == i && !subscriptionInfo.isActivated()) {
                return true;
            }
        }
        return false;
    }
}
