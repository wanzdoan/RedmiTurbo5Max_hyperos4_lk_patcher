package com.hyper.internal.telephony;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.text.TextUtils;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccPort;
import com.hyper.internal.telephony.ext.IHyperUiccManagerExt;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class HyperUiccManagerImplExt implements IHyperUiccManagerExt {
    private static final boolean DBG = true;
    private static final String LOG_TAG = "HyperUiccManagerImplExt";
    private static HyperUiccManagerImplExt sInstance = null;
    protected final UiccManagerHandler mBaseHandler;
    private HandlerThread mHandlerThread;

    protected class UiccManagerHandler extends Handler {
        public UiccManagerHandler(HyperUiccManagerImplExt this$0, Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int i = msg.what;
            HyperUiccManagerImplExt.logd("wrong message: " + msg.what);
        }
    }

    private HyperUiccManagerImplExt() {
        HandlerThread handlerThread = new HandlerThread("UiccManagerHandler");
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        this.mBaseHandler = new UiccManagerHandler(this, this.mHandlerThread.getLooper());
    }

    public static HyperUiccManagerImplExt getInstance() {
        HyperUiccManagerImplExt hyperUiccManagerImplExt;
        synchronized (HyperUiccManagerImplExt.class) {
            if (sInstance == null) {
                sInstance = new HyperUiccManagerImplExt();
            }
            hyperUiccManagerImplExt = sInstance;
        }
        return hyperUiccManagerImplExt;
    }

    @Override // com.hyper.internal.telephony.ext.IHyperUiccManagerExt
    public void activateUiccCard(int phoneId) {
        int subId = getSubIdForSlot(phoneId);
        SubscriptionManagerService.getInstance().setUiccApplicationsEnabled(DBG, subId);
    }

    @Override // com.hyper.internal.telephony.ext.IHyperUiccManagerExt
    public void deactivateUiccCard(int phoneId) {
        int subId = getSubIdForSlot(phoneId);
        SubscriptionManagerService.getInstance().setUiccApplicationsEnabled(false, subId);
    }

    private int getSubIdForSlot(int slot) {
        Rlog.d(IHyperUiccManagerExt.TAG, "getSubIdForSlot for slot " + slot);
        UiccPort port = UiccController.getInstance().getUiccPort(slot);
        String iccidFromSlotInfo = port == null ? null : port.getIccId();
        if (!TextUtils.isEmpty(iccidFromSlotInfo)) {
            List<SubscriptionInfo> subscriptionInfoList = SubscriptionManagerService.getInstance().getAvailableSubscriptionInfoList((String) null, (String) null);
            if (subscriptionInfoList != null) {
                for (SubscriptionInfo subinfo : subscriptionInfoList) {
                    if (TextUtils.equals(subinfo.getIccId(), iccidFromSlotInfo)) {
                        Rlog.d(IHyperUiccManagerExt.TAG, "find sub id for slot");
                        return subinfo.getSubscriptionId();
                    }
                }
            }
            Rlog.d(IHyperUiccManagerExt.TAG, "getSubIdForSlot return INVALID_SUBSCRIPTION_ID");
            return -1;
        }
        Rlog.d(IHyperUiccManagerExt.TAG, "iccid is empty");
        return -1;
    }

    public static void logd(String string) {
        Rlog.d(LOG_TAG, string);
    }
}
