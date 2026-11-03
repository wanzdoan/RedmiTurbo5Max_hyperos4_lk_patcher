package com.android.phone;

import android.provider.MiuiSettings;
import android.provider.Settings;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.phone.module.ISatelliteModule;
import com.android.phone.networkmode.NetworkMode;
import com.ot.pubsub.c.d$$ExternalSyntheticBUOutline0;
import java.util.List;
import miui.os.Build;
import miui.telephony.DefaultSimManager;
import miui.telephony.DefaultSlotSelector;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miui.telephony.VirtualSimUtils;

/* JADX INFO: loaded from: classes.dex */
public class DefaultSlotSelectorImpl implements DefaultSlotSelector {
    private static DefaultSlotSelectorImpl sInstance;
    private CardLostProcesser mCardLostProcesser = null;

    public static DefaultSlotSelectorImpl make() {
        if (sInstance == null) {
            DefaultSlotSelectorImpl defaultSlotSelectorImpl = new DefaultSlotSelectorImpl();
            sInstance = defaultSlotSelectorImpl;
            return defaultSlotSelectorImpl;
        }
        d$$ExternalSyntheticBUOutline0.m("DefaultSlotSelectorImpl.make() should only be called once");
        return null;
    }

    private DefaultSlotSelectorImpl() {
        DefaultSimManager.getInstance().setDefaultSlotSelector(this);
    }

    public int getDefaultDataSlot(int[] iArr, int i) {
        int dataSlotForCmcc;
        return ((isDdsForceSetForCmcc() || isAnyCardStateChanged(iArr)) && SubscriptionManager.INVALID_SUBSCRIPTION_ID != (dataSlotForCmcc = getDataSlotForCmcc())) ? dataSlotForCmcc : restoreForDdsCardLost(i);
    }

    private boolean isAnyCardStateChanged(int[] iArr) {
        if (iArr == null) {
            return false;
        }
        for (int i : iArr) {
            if (i == 2 || i == 4) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDdsForceSetForCmcc() {
        return NetworkMode.sLockNetworkType == 1;
    }

    public static int getDataSlotForCmcc() {
        if (Build.IS_CM_CUSTOMIZATION && !MiuiSettings.VirtualSim.isVirtualSimEnabled(PhoneGlobals.getInstance())) {
            List<SubscriptionInfo> subscriptionInfoList = SubscriptionManager.getDefault().getSubscriptionInfoList();
            if (subscriptionInfoList == null) {
                return SubscriptionManager.INVALID_SUBSCRIPTION_ID;
            }
            int i = SubscriptionManager.INVALID_SUBSCRIPTION_ID;
            int i2 = 0;
            for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
                int slotId = subscriptionInfo.getSlotId();
                if (ServiceProviderUtils.isChinaMobile(subscriptionInfo.getMcc(), subscriptionInfo.getMnc()) || ServiceProviderUtils.isChinaMobile(ModemProvider.getInstance().getSimImsi(slotId))) {
                    i2++;
                    i = slotId;
                }
            }
            if (i2 == 1) {
                return i;
            }
        }
        return SubscriptionManager.INVALID_SUBSCRIPTION_ID;
    }

    private void processForDdsCardLost(int i, int i2, String[] strArr) {
        Rlog.d("DefaultSlotSelectorImpl", "processForCardLost start");
        if (Settings.Global.getInt(PhoneGlobals.getInstance().getContentResolver(), "device_provisioned", 0) == 0) {
            Rlog.d("DefaultSlotSelectorImpl", "not provisioned, return");
            return;
        }
        for (int i3 = 0; i3 < MiuiPhoneUtils.PHONE_COUNT; i3++) {
            if (TextUtils.isEmpty(strArr[i3])) {
                return;
            }
        }
        CardLostProcesser cardLostProcesser = this.mCardLostProcesser;
        if (cardLostProcesser != null) {
            cardLostProcesser.dissmissAutoSwitchAlertDialog();
        }
        CardLostProcesser cardLostProcesser2 = new CardLostProcesser(i, i2, strArr);
        this.mCardLostProcesser = cardLostProcesser2;
        cardLostProcesser2.processForCardLost();
    }

    private int restoreForDdsCardLost(int i) {
        CardLostProcesser cardLostProcesser = this.mCardLostProcesser;
        if (cardLostProcesser == null || !cardLostProcesser.shouldRestoreForCardLost()) {
            return i;
        }
        int iRestoreForCardLost = this.mCardLostProcesser.restoreForCardLost();
        this.mCardLostProcesser = null;
        Rlog.d("DefaultSlotSelectorImpl", "restoreForCardLost to ds " + iRestoreForCardLost);
        return iRestoreForCardLost;
    }

    public void onSimRemoved(int i, String[] strArr) {
        List subscriptionInfoList = SubscriptionManager.getDefault().getSubscriptionInfoList();
        if (subscriptionInfoList == null || subscriptionInfoList.isEmpty() || VirtualSimUtils.getInstance().isEnablingVirtualSim() || VirtualSimUtils.getInstance().isDisablingVirtualSim() || ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isSatelliteEnabled() || i != DefaultSimManager.getDefaultDataSlotId()) {
            return;
        }
        processForDdsCardLost(i, ((SubscriptionInfo) subscriptionInfoList.get(0)).getSlotId(), strArr);
    }

    public boolean isEsimSwitching() {
        return MiuiEsimManagerBase.isEsimIsActiveChangeToNoActive;
    }
}
