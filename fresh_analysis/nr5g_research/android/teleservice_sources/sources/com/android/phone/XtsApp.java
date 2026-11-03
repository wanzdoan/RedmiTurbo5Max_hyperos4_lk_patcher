package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.android.internal.telephony.RegistrantList;
import com.xiaomi.internal.telephony.ModemDumpServiceConfig;
import com.xiaomi.internal.telephony.ModemServiceMoniter;
import com.xiaomi.internal.telephony.XiaomiTelephonyComponentFactory;
import com.xiaomi.internal.telephony.XmPhoneServiceFactory;
import com.xiaomi.modem.ModemUtils;
import com.xiaomi.modem.OemHookAgent;

/* JADX INFO: loaded from: classes.dex */
public class XtsApp {
    public static int mAndroidVersion = 0;
    public static int mBpChipId = 0;
    public static String mBuildProfile = "NULL";
    public static String mBuildRegion = "none";
    public static String mBuildTags = "NULL";
    public static String mBuildVersion = "";
    public static String mCertType = "none";
    public static Context mContext = null;
    public static String mDeviceHwInfo = "NULL";
    public static String mDeviceHwLevel = "NULL";
    public static String mDeviceModName = "NULL";
    public static String mDeviceName = "NULL";
    public static String mFactoryBuild = "NULL";
    public static int mHalChipId = 0;
    private static OemHookAgent mHookAgent = null;
    private static RegistrantList mHookReadyRegistrants = new RegistrantList();
    public static boolean mIsHalDecoupled = false;
    public static int mManufacturerId = 0;
    public static int mOemProjectFlag = 0;
    public static String mRootType = "NULL";
    public static int mSdkVersion = 0;
    public static boolean mXringHalFlag = false;
    private static XtsApp mXtsApp;
    protected Handler mHandler = new Handler() { // from class: com.android.phone.XtsApp.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            XtsApp.log("handleMessage msg id: " + message.what);
            new Bundle();
            message.getData();
            if (message.what == 0) {
                XtsApp.log("EVENT_MIRIL_HOOK_READY");
                XtsApp.this.onHookDone();
            } else {
                XtsApp.log("invalid msg id: " + message.what);
            }
        }
    };
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(this) { // from class: com.android.phone.XtsApp.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            XtsApp.log("Received: " + action);
            if ("android.intent.action.SCREEN_ON".equals(action)) {
                XtsApp.log("ACTION_SCREEN_ON");
            } else if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                XtsApp.log("ACTION_SIM_STATE_CHANGED");
            } else {
                XtsApp.log("XtsApp mBroadcastReceiver do nothing");
            }
        }
    };

    public static void make(Context context) {
        log("make, cnt = " + context + ", mXtsApp = " + mXtsApp);
        if (mXtsApp != null) {
            log("make done, do nothing");
        } else {
            if (context == null) {
                log("cnt is null, do nothing");
                return;
            }
            mContext = context;
            mXtsApp = new XtsApp();
            XmPhoneServiceFactory.init(context);
        }
    }

    private XtsApp() {
        log("Constructor");
        onInit();
        XiaomiTelephonyComponentFactoryMake();
        OemHookAgent.registerHookReadyEvent(mContext, this.mHandler, 0, null);
    }

    private void XiaomiTelephonyComponentFactoryMake() {
        int hookType = OemHookAgent.getHookType();
        log("XiaomiTelephonyComponentFactoryMake, Hook Type = " + hookType);
        if (hookType == 2 || hookType == 1 || hookType == 3 || hookType == 4 || hookType == 0) {
            log("XTS will do XiaomiTelephonyComponentFactory.make");
            XiaomiTelephonyComponentFactory.make(mContext);
        } else {
            log("XTS will do nothing for XiaomiTelephonyComponentFactory");
        }
    }

    private void onInit() {
        log("onInit");
        ModemUtils.onInitContext(mContext);
        onInitVerInfo();
        log("onInit, " + ModemUtils.getVerInfoWithName());
    }

    private void onInitVerInfo() {
        log("onInitVerInfo");
        mDeviceName = ModemUtils.getDeviceName();
        mDeviceHwInfo = ModemUtils.getDeviceHwInfo();
        mDeviceModName = ModemUtils.getDeviceSwInfo();
        mDeviceHwLevel = ModemUtils.getDeviceHwLevel();
        mBuildRegion = ModemUtils.getBuildRegion();
        mCertType = ModemUtils.getCertType();
        mFactoryBuild = ModemUtils.getFactoryBuild();
        mRootType = ModemUtils.getRootType();
        mBuildVersion = ModemUtils.getBuildVersion();
        mBuildTags = ModemUtils.getBuildTags();
        mSdkVersion = ModemUtils.getSdkVersion();
        mAndroidVersion = ModemUtils.getAndroidVersion();
        mManufacturerId = ModemUtils.getManufacturerId();
        mHalChipId = ModemUtils.getHalChipId();
        mBpChipId = ModemUtils.getBpChipId();
        mBuildProfile = ModemUtils.getBuildProfile();
        mXringHalFlag = ModemUtils.isXringHal();
    }

    private void onInitVerInfo(OemHookAgent oemHookAgent) {
        log("onInitVerInfo, hook = " + oemHookAgent);
        if (oemHookAgent == null) {
            log("onInitVerInfo, hook is null");
            return;
        }
        mHalChipId = ModemUtils.getModemHalChipId(oemHookAgent);
        mBpChipId = ModemUtils.getModemChipId(oemHookAgent);
        mBuildProfile = ModemUtils.getBuildProfile(oemHookAgent);
        mManufacturerId = ModemUtils.getManufacturerId(oemHookAgent);
        mOemProjectFlag = ModemUtils.getOemProductFlag(oemHookAgent);
        mIsHalDecoupled = ModemUtils.isHalDecoupled(oemHookAgent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onHookDone() {
        log("onHookDone");
        OemHookAgent.unregisterHookReadyEvent(this.mHandler);
        OemHookAgent hook = OemHookAgent.getHook();
        mHookAgent = hook;
        ModemUtils.onInitHook(hook);
        synchronized (mHookReadyRegistrants) {
            mHookReadyRegistrants.notifyRegistrants();
        }
        onInitVerInfo(mHookAgent);
        log("onHookDone, VerInfo = " + ModemUtils.getVerInfoWithoutName());
        if (!isXtsSupported()) {
            log("Xts is not supported, will do nothing");
        } else {
            onInitByHook();
        }
    }

    public static boolean isXtsSupported() {
        log("isXtsSupported");
        if (true == mIsHalDecoupled) {
            log("isXtsSupported, XTS is supported by this HalDecoupled project, mManufacturerId = " + mManufacturerId);
            return true;
        }
        if (1 == mManufacturerId) {
            log("isXtsSupported, XTS is supported by this xiaomi project, mManufacturerId = " + mManufacturerId);
            return true;
        }
        int i = mHalChipId;
        if (101 == i || 102 == i || 103 == i) {
            log("isXtsSupported, XTS is supported by this odm project, mHalChipId = " + mHalChipId);
            return true;
        }
        if (1 == mOemProjectFlag) {
            log("isXtsSupported, XTS is not supported by this odm project, mBpChipId = " + mBpChipId);
            return false;
        }
        log("isXtsSupported, XTS is supported by this xiaomi old project, mBpChipId = " + mBpChipId);
        return true;
    }

    private void onInitByHook() {
        log("onInitByHook");
        try {
            startXtsService();
            onRegisterReceiver();
        } catch (Exception e) {
            log("Exception e: " + e);
            e.printStackTrace();
        } finally {
            log("onInitByHook done");
        }
    }

    private void startXtsService() {
        log("startXtsService, Hook Type = " + OemHookAgent.getHookType() + ", mContext = " + mContext);
        mContext.startService(new Intent(mContext, (Class<?>) XtsService.class));
        if (OemHookAgent.getHookType() == 2) {
            log("Qcom XTS will start Service");
            ModemDumpServiceConfig.startService(mContext);
        } else if (OemHookAgent.getHookType() == 1) {
            log("MTK XTS will start Service");
            ModemDumpServiceConfig.startService(mContext);
            ModemServiceMoniter.onMtbServiceWakeUp(mContext);
        } else if (OemHookAgent.getHookType() == 3) {
            log("XRING XTS will start Service");
            ModemDumpServiceConfig.startService(mContext);
        }
    }

    private void onRegisterReceiver() {
        log("onRegisterReceiver");
        if (mHookAgent == null) {
            log("mHookAgent is null");
        } else {
            mContext.registerReceiver(this.mBroadcastReceiver, new IntentFilter(), 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        ModemUtils.logd("XtsApp", str);
    }
}
