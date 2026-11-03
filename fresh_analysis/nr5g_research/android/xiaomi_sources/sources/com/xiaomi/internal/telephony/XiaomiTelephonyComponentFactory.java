package com.xiaomi.internal.telephony;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import com.android.internal.telephony.CommandsInterface;
import com.xiaomi.modem.ModemUtils;
import com.xiaomi.modem.OemHookAgent;

/* JADX INFO: loaded from: classes.dex */
public class XiaomiTelephonyComponentFactory {
    private static final int EVENT_OEM_HOOK_DISCONNECTED = 1;
    private static final int EVENT_OEM_HOOK_MAKE_DELAY = 2;
    private static final int EVENT_OEM_HOOK_READY = 0;
    private static final String LOG_TAG = "XiaomiTelephonyComponentFactory";
    private static final int OEM_HOOK_MAKE_DELAY_S = 0;
    private static final String PROPERTY_DEVICE_NAME = "ro.product.device";
    private static final String PROP_RADIO_SELF_DEV_FLAG = "ro.vendor.radio.self_dev_flag";
    private static final String PROP_RADIO_SELF_DEV_OFF = "0";
    private static final String PROP_RADIO_SELF_DEV_ON = "1";
    private static final int SELF_DEV_PROJECT_FLAG_INIT = 0;
    private static final int SELF_DEV_PROJECT_FLAG_NO = 2;
    private static final int SELF_DEV_PROJECT_FLAG_YES = 1;
    protected Handler mHandler = new Handler() { // from class: com.xiaomi.internal.telephony.XiaomiTelephonyComponentFactory.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            XiaomiTelephonyComponentFactory.log("handleMessage msg id: " + msg.what);
            new Bundle();
            msg.getData();
            switch (msg.what) {
                case 0:
                    XiaomiTelephonyComponentFactory.log("EVENT_OEM_HOOK_READY");
                    XiaomiTelephonyComponentFactory.this.onHookReady();
                    break;
                case 1:
                    XiaomiTelephonyComponentFactory.log("EVENT_OEM_HOOK_DISCONNECTED");
                    XiaomiTelephonyComponentFactory.this.onHookDisconnected();
                    break;
                case 2:
                    XiaomiTelephonyComponentFactory.log("EVENT_OEM_HOOK_MAKE_DELAY");
                    XiaomiTelephonyComponentFactory.this.makeHook();
                    break;
                default:
                    XiaomiTelephonyComponentFactory.log("invalid msg id: " + msg.what);
                    break;
            }
        }
    };
    private static XiaomiTelephonyComponentFactory sIntance = null;
    private static int mMaxDataAttachModemCount = -1;
    private static Context mContext = null;
    private static Looper mLooper = null;
    private static int mSelfDevProject = 0;
    private static OemHookAgent mHookAgent = null;
    private static int mHookType = 0;
    private static String mDeviceName = null;

    private void onInitEarlyMake() {
        log("onInitEarlyMake start");
        ModemServiceMoniter.startMtbIfNeed(mContext);
        log("onInitEarlyMake end");
    }

    private void onInitForHookReady() {
        log("onInitForHookReady start");
        if (2 == mHookType) {
            log("ModemTelephonyLegacy.make for HOOK_TYPE_QCOM start");
            ModemTelephonyLegacy.make(mContext, null);
        }
        log("ModemServiceMoniter.make start");
        ModemServiceMoniter.make(mContext, mHookAgent);
        log("ModemSarPreInit.make start");
        ModemSarPreInit.make(mContext, mHookAgent);
        log("ModemEnhanceMain.make start");
        ModemEnhanceMain.MEStart(mContext);
        log("onInitForHookReady end");
    }

    private void onDisposeForHookDisconnected() {
        log("onDisposeForHookDisconnected start");
        log("ModemEnhanceMain.onHookDisconnected start");
        ModemEnhanceMain.onHookDisconnected();
        log("onDisposeForHookDisconnected end");
    }

    public static boolean isSelfDevProduct() {
        if (mSelfDevProject == 0) {
            String strSelfDev = SystemProperties.get(PROP_RADIO_SELF_DEV_FLAG, "0");
            if ("1".equals(strSelfDev)) {
                mSelfDevProject = 1;
            } else {
                mSelfDevProject = 2;
            }
        }
        log("isSelfDevProduct, mSelfDevProject = " + mSelfDevProject);
        return 1 == mSelfDevProject;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onHookReady() {
        log("onHookReady");
        OemHookAgent.unregisterHookReadyEvent(this.mHandler);
        mHookAgent = OemHookAgent.getHook();
        mHookType = OemHookAgent.getHookType();
        ModemUtils.onInitHook(mHookAgent);
        log("onHookReady, mHookAgent = " + mHookAgent + ", mHookType = " + mHookType);
        if (mHookAgent == null) {
            log("onHookReady, mHookAgent is null, do nothing");
            return;
        }
        if (1 != mHookType && 2 != mHookType && mHookType != 0 && 3 != mHookType && 4 != mHookType) {
            log("onHookReady, mHookType is invalid, do nothing");
            return;
        }
        try {
            onInitForHookReady();
        } catch (Exception e) {
            log("onHookReady onInitForHookReady failed, Exception:" + e.toString());
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onHookDisconnected() {
        log("onHookDisconnected");
        OemHookAgent.unregisterHookDisconnectedEvent(this.mHandler);
        onDisposeForHookDisconnected();
    }

    private XiaomiTelephonyComponentFactory() {
        log("Constructor, mDeviceName = " + mDeviceName + ", mMaxDataAttachModemCount = " + mMaxDataAttachModemCount + ", mContext = " + mContext + ", mLooper = " + mLooper);
        try {
            onInitEarlyMake();
        } catch (Exception e) {
            log("Constructor onInitEarlyMake failed, Exception:" + e.toString());
            e.printStackTrace();
        }
        log("Constructor makeHook, OEM_HOOK_MAKE_DELAY_S = 0");
        ModemUtils.onSendMsgEx(this.mHandler, 2, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void makeHook() {
        log("makeHook");
        OemHookAgent.registerHookDisconnectedEvent(mContext, this.mHandler, 1, null);
        OemHookAgent.registerHookReadyEvent(mContext, this.mHandler, 0, null);
    }

    public static void makeMtkLegacy(Context context, int numPhones, CommandsInterface[] sCommandsInterfaces) {
        log("makeMtkLegacy, context = " + context + ", numPhones = " + numPhones + ", sCommandsInterfaces = " + sCommandsInterfaces + ", mContext = " + mContext);
        ModemUtils.onInitContext(context);
        if (mContext == null) {
            mContext = context;
        }
        log("ModemTelephonyLegacy.make for makeMtkLegacy start");
        ModemTelephonyLegacy.make(context, sCommandsInterfaces);
        log("ModemTelephonyLegacy.make for makeMtkLegacy end");
    }

    public static OemHookAgent getHookAgent() {
        return mHookAgent;
    }

    public static void make(Context context) {
        make(0, context, null);
    }

    public static void make(int maxDataAttachModemCount, Context context, Looper looper) {
        if (sIntance != null) {
            log("make, sIntance = " + sIntance);
            return;
        }
        log("make start");
        mDeviceName = SystemProperties.get("ro.product.device");
        mMaxDataAttachModemCount = maxDataAttachModemCount;
        mContext = context;
        mLooper = looper;
        ModemUtils.onInitContext(mContext);
        if (mContext == null) {
            log("make, mContext is null");
        } else {
            sIntance = new XiaomiTelephonyComponentFactory();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String msg) {
        ModemUtils.logd(LOG_TAG, msg);
    }
}
