package com.android.phone;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.euicc.EuiccProfileInfo;
import android.service.notification.StatusBarNotification;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import android.telephony.UiccSlotInfo;
import android.telephony.UiccSlotMapping;
import android.telephony.euicc.EuiccManager;
import android.text.TextUtils;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.internal.telephony.uicc.euicc.EuiccPort;
import com.android.internal.telephony.uicc.euicc.async.AsyncResultCallback;
import com.android.phone.module.IUtilityModule;
import com.android.phone.utils.Utils;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyUtils;
import miuix.animation.internal.TransitionInfo;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiEsimManagerBase {
    private static String ESIM_SIM1_MODE = "1";
    private static String ESIM_SLOT1_ONLY_MODE = "3";
    private static String EUICC_RESPONSE_CODE = "euicc_response_code";
    private static String METHOD_EUICC_DISABLE_ALL_PROFILE = "method_euicc_disable_all_profile";
    private static String PARAMS_EUICC_REFRESH = "params_euicc_refresh";
    public static boolean isBootCompleted = false;
    public static boolean isEsimIsActiveChangeToNoActive = false;
    public static boolean isEsimStateChanging = false;
    public static boolean isNeedSwitchMapping = false;
    protected static Context mContext = null;
    public static Collection mCurSlotMappings = null;
    public static ESimHandler mHandler = null;
    public static boolean mIsEuiccPowerOff = false;
    public static boolean mIsResetMemory = false;
    public static boolean mIsSwitchingSlotMapping = false;
    public static Object mLock = null;
    public static boolean mOldEsimStateIsActive = false;
    public static TelephonyManager mTelMgr;
    protected static volatile MiuiEsimManagerBase sInstance;
    private static volatile ArrayList sSupportEsimCountryList;
    private EuiccManager mEuiccManager;
    private Object mSimLock;
    public int STATUS_ALREADY_BOUND = 1;
    private int mWaitingVsimSlot = -1;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.phone.MiuiEsimManagerBase.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String iccId;
            String action = intent.getAction();
            MiuiEsimManagerBase.logi("onReceive: " + action);
            if (action == null) {
                return;
            }
            int simState = ((TelephonyManager) context.getSystemService("phone")).getSimState(1);
            if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                MiuiEsimManagerBase.logi("Slot2 sim state:" + simState);
                if (simState != 0) {
                    if (simState == 1) {
                        MiuiEsimManagerBase.logi("ResetNeedShowNotificationValue to 0");
                        MiuiEsimManagerBase.setNeedShowNotificationValue(0);
                        return;
                    }
                    if (simState == 2 || simState == 3 || simState == 5) {
                        MiuiEsimManagerBase.logi("when PIN_REQUIRED or PUK_REQUIRED or READY dismissEsimIsActivatingTips...");
                        MiuiEsimManagerBase.this.dismissEsimIsActivatingTips(context);
                        if (MiuiEsimManagerBase.getNeedShowNotificationValue() != 1 && MiuiEsimManagerBase.this.isEsimActive() && (iccId = MiuiPhoneUtils.getIccId(1)) != null && ("894921".equals(iccId.substring(0, 6)) || "894922".equals(iccId.substring(0, 6)))) {
                            MiuiEsimManagerBase.logi("setNeedShowNotificationValue for o2 eSIM");
                            MiuiEsimManagerBase.setNeedShowNotificationValue(1);
                        }
                        if (MiuiPhoneUtils.isSupportEuiccBinding()) {
                            boolean zIsEsimActive = MiuiEsimManagerBase.this.isEsimActive();
                            MiuiEsimManagerBase miuiEsimManagerBase = MiuiEsimManagerBase.this;
                            if (zIsEsimActive) {
                                miuiEsimManagerBase.setSrvDomain(true);
                                return;
                            } else {
                                miuiEsimManagerBase.setSrvDomain(false);
                                return;
                            }
                        }
                        return;
                    }
                    if (simState != 6) {
                        return;
                    }
                }
                if (!MiuiEsimManagerBase.isBootCompleted) {
                    MiuiEsimManagerBase.logi("when NOT_READY or UNKNOWN showEsimIsActivatingTips...");
                    MiuiEsimManagerBase.this.showEsimIsActivatingTips(context);
                }
                MiuiEsimManagerBase.logi("ResetNeedShowNotificationValue to 0");
                MiuiEsimManagerBase.setNeedShowNotificationValue(0);
                return;
            }
            if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
                MiuiEsimManagerBase.isBootCompleted = true;
                if (simState == 0 || simState == 6) {
                    MiuiEsimManagerBase.logi("when BOOT_COMPLETED and NOT_READY or UNKNOWN showEsimIsActivatingTips...");
                    MiuiEsimManagerBase.this.showEsimIsActivatingTips(context);
                    return;
                }
                return;
            }
            if ("com.android.updater.action.COTA_CARRIER".equals(action)) {
                MiuiEsimManagerBase.logi("onReceive: COTA config");
                boolean booleanExtra = intent.getBooleanExtra("carrierName", false);
                String str = SystemProperties.get("persist.sys.cota.carrier", "");
                List stringArray = HyperOSCustFeatureResolve.getStringArray("cota_enable_esim_carrier_list");
                MiuiEsimManagerBase.logi("cotaCarrier=" + str + " carrierList=" + stringArray);
                if (booleanExtra && stringArray != null && stringArray.contains(str)) {
                    int esimGPIOState = MiuiEsimManagerBase.this.getEsimGPIOState();
                    MiuiEsimManagerBase.setEsimSecureValue(0);
                    if (esimGPIOState == 1 && MiuiEsimManagerBase.this.setEsimState(0) == 0) {
                        MiuiEsimManagerBase.logi("setEsimState: enable esim by cota");
                    }
                    MiuiEsimManagerBase.setEsimEnabledByCotaValue(1);
                }
                MiuiEsimManagerBase.logi("is_enable_esim_by_cota = " + MiuiEsimManagerBase.getEsimEnabledByCotaValue());
                return;
            }
            if ("android.telephony.action.SIM_SLOT_STATUS_CHANGED".equals(action)) {
                MiuiEsimManagerBase.logi("ACTION_SIM_SLOT_STATUS_CHANGED");
                if (MiuiEsimManagerBase.this.mWaitingVsimSlot >= 0) {
                    int i = MiuiEsimManagerBase.this.mWaitingVsimSlot != 0 ? 1 : 0;
                    if (MiuiPhoneUtils.isSimPresent(i)) {
                        MiuiEsimManagerBase.logd("Target SIM" + (i + 1) + " present, notifying...");
                        try {
                            synchronized (MiuiEsimManagerBase.this.mSimLock) {
                                MiuiEsimManagerBase.this.mSimLock.notifyAll();
                            }
                        } catch (Exception e) {
                            MiuiEsimManagerBase.logd("notifyAll() has error: " + e);
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

    public abstract int getEsimGPIOState();

    public abstract String getProductEid();

    public abstract int modifyEuiccSVN(int i);

    public abstract void resetPortAndStateForSlot(int i);

    public void restUimPowerState(int i) {
    }

    public abstract int setEsimState(int i);

    public void setSrvDomain(boolean z) {
    }

    public abstract boolean shouldResetPortAndState();

    public abstract void writeProductEid(byte[] bArr);

    public static ArrayList getSupportEsimCountryList() {
        if (sSupportEsimCountryList == null) {
            synchronized (MiuiEsimManagerBase.class) {
                try {
                    if (sSupportEsimCountryList == null) {
                        sSupportEsimCountryList = new ArrayList();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sSupportEsimCountryList;
    }

    public static MiuiEsimManagerBase getInstance() {
        return sInstance;
    }

    class ResultCaptor extends AsyncResultCallback {
        public Throwable exception;
        private CountDownLatch mLatch;
        public boolean mResetMemorySuccess;
        final long mWaitTime;
        public Object result;

        private ResultCaptor() {
            this.mWaitTime = MirrorDesktopHelper.TIMEOUT_MILLIS;
            this.mResetMemorySuccess = false;
            this.mLatch = new CountDownLatch(1);
        }

        public void onResult(Object obj) {
            MiuiEsimManagerBase.logi("onResult E ");
            this.result = obj;
            if (MiuiEsimManagerBase.mIsResetMemory) {
                this.mResetMemorySuccess = true;
                MiuiEsimManagerBase.mIsResetMemory = false;
            }
            this.mLatch.countDown();
        }

        public void onException(Throwable th) {
            MiuiEsimManagerBase.logi("onException : " + th);
            this.exception = th;
            if (MiuiEsimManagerBase.mIsResetMemory) {
                this.mResetMemorySuccess = false;
                MiuiEsimManagerBase.mIsResetMemory = false;
            }
            this.mLatch.countDown();
        }

        public Object retrieveResult() {
            try {
                if (this.mLatch.await(MirrorDesktopHelper.TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
                    MiuiEsimManagerBase.logi("mLatch.await end");
                }
                return this.result;
            } catch (Exception unused) {
                Thread.currentThread().interrupt();
                return this.result;
            }
        }

        public boolean retrieveResultForRestMemory() {
            try {
                if (this.mLatch.await(MirrorDesktopHelper.TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
                    MiuiEsimManagerBase.logi("mLatch.await end");
                }
                return this.mResetMemorySuccess;
            } catch (Exception unused) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
    }

    public class ESimHandler extends Handler {
        public ESimHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            MiuiEsimManagerBase.logi("eSimHandler handleMessage what=" + message.what);
            int i = message.what;
            if (i != 99) {
                switch (i) {
                    case 10:
                        if (MiuiEsimManagerBase.mIsSwitchingSlotMapping) {
                            MiuiEsimManagerBase.mIsSwitchingSlotMapping = false;
                            MiuiEsimManagerBase.isNeedSwitchMapping = false;
                            synchronized (MiuiEsimManagerBase.mLock) {
                                try {
                                    MiuiEsimManagerBase.mLock.wait(1000L);
                                    MiuiEsimManagerBase.mLock.notifyAll();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    e.printStackTrace();
                                }
                                break;
                            }
                            return;
                        }
                        return;
                    case 11:
                        if (!MiuiEsimManagerBase.isNeedDownGradeSVNRegion() || MiuiEsimManagerBase.getSVNDowngradeStatus() == 1) {
                            return;
                        }
                        MiuiEsimManagerBase.this.modifyEuiccSVN(2);
                        return;
                    case 12:
                        MiuiEsimManagerBase.this.recoveryForInit();
                        return;
                    default:
                        return;
                }
            }
            MiuiEsimManagerBase.logi("EVENT_SET_USAGE_SETTING_DONE");
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception != null) {
                MiuiEsimManagerBase.logi("Unexpected failure to set usage setting " + asyncResult.exception);
            }
        }
    }

    protected MiuiEsimManagerBase() {
        logi("MiuiEsimManagerBase init...");
        mContext = PhoneGlobals.getInstance();
        mLock = new Object();
        this.mSimLock = new Object();
        HandlerThread handlerThread = new HandlerThread("MiuiEsimManager");
        handlerThread.start();
        mHandler = new ESimHandler(handlerThread.getLooper());
        this.mEuiccManager = (EuiccManager) mContext.getSystemService("euicc");
        mTelMgr = (TelephonyManager) mContext.getSystemService(TelephonyManager.class);
        initList();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        intentFilter.addAction("com.android.updater.action.COTA_CARRIER");
        mContext.registerReceiver(this.mReceiver, intentFilter, 2);
        modifySmdsAdressIfNeed();
        logi("MiuiEsimManagerBase inited!");
    }

    private void modifySmdsAdressIfNeed() {
        if (HyperOSCustFeatureResolve.getBoolean("config_modify_sms_address_if_need", false)) {
            logi("change default SM-DS to kddi customized SM-DS.");
            Settings.Global.putString(PhoneGlobals.getInstance().getContentResolver(), "root_smds_address", "lpa.live.esimdiscovery.com");
        }
    }

    private void initList() {
        List listAsList;
        if (miui.telephony.TelephonyManager.isCustFeatureResolveEnabled()) {
            listAsList = HyperOSCustFeatureResolve.getStringArray("country_support_esim");
        } else {
            String[] stringArray = mContext.getResources().getStringArray(R.array.country_support_esim);
            listAsList = stringArray != null ? Arrays.asList(stringArray) : null;
        }
        if (listAsList == null) {
            return;
        }
        getSupportEsimCountryList().addAll(listAsList);
        if (ModuleDelegate.get(IUtilityModule.class) != null) {
            ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).updateCloudEsimCountryList("initlist");
        }
    }

    public boolean isEsimActive() {
        boolean z;
        if (mHandler == null) {
            return false;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            String eidFromSlotId = getEidFromSlotId(1);
            EuiccManager euiccManager = this.mEuiccManager;
            if (euiccManager == null || !euiccManager.isEnabled() || eidFromSlotId == null) {
                z = false;
            } else {
                UiccSlot[] uiccSlots = UiccController.getInstance().getUiccSlots();
                z = false;
                for (int i = 0; i < uiccSlots.length; i++) {
                    UiccSlot uiccSlot = uiccSlots[i];
                    String iccId = uiccSlot.getIccId(uiccSlot.getPortIndexFromPhoneId(i));
                    if (uiccSlot.isEuicc() && iccId != null && MiuiPhoneUtils.isValidIccId(iccId) && !iccId.isEmpty()) {
                        z = true;
                    }
                }
            }
            if (z) {
                mOldEsimStateIsActive = true;
                isEsimIsActiveChangeToNoActive = false;
            } else if (mOldEsimStateIsActive) {
                mOldEsimStateIsActive = false;
                isEsimIsActiveChangeToNoActive = true;
            }
            logd("isEsimActive =" + z + " , mOldEsimStateIsActive = " + mOldEsimStateIsActive + " , isEsimIsActiveChangeToNoActive = " + isEsimIsActiveChangeToNoActive);
            return z;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getEidFromSlotId(int i) {
        UiccSlotInfo uiccSlotInfo;
        String str = null;
        if (mHandler == null) {
            return null;
        }
        UiccSlotInfo[] uiccSlotsInfo = ((TelephonyManager) mContext.getSystemService("phone")).getUiccSlotsInfo();
        if (uiccSlotsInfo != null && i < uiccSlotsInfo.length && (uiccSlotInfo = uiccSlotsInfo[i]) != null) {
            String cardId = uiccSlotInfo.getCardId();
            if (uiccSlotsInfo[i].getIsEuicc() && cardId != null && !cardId.isEmpty() && cardId.length() == 32) {
                str = cardId;
            }
        }
        logd("getEidFromSlotId eid = " + TelephonyUtils.pii(str) + " , slotId = " + i);
        return str;
    }

    public boolean stopVsimIfNeeded(int i) {
        if (i == 1) {
            logd("no need stop vsim when disable esim");
            return true;
        }
        MiuiPhoneUtils.DualSimType dualSimTypeCheckCurrentDualSimType = MiuiPhoneUtils.checkCurrentDualSimType();
        int virtualSimSlot = MiuiPhoneUtils.getVirtualSimSlot();
        logd("vsimSlot: " + virtualSimSlot + ", dualSimType: " + dualSimTypeCheckCurrentDualSimType.getValue());
        if (dualSimTypeCheckCurrentDualSimType != MiuiPhoneUtils.DualSimType.VSIM_SLOT_1) {
            logd("No need to stop VSIM before enabling eSIM.");
            return true;
        }
        this.mWaitingVsimSlot = virtualSimSlot;
        if (MiuiPhoneUtils.stopVsimBeforeEnableEsim()) {
            logd("VSIM stopped successfully.");
            handlePostStopOperations(virtualSimSlot);
            this.mWaitingVsimSlot = -1;
            return true;
        }
        this.mWaitingVsimSlot = -1;
        return false;
    }

    private void handlePostStopOperations(int i) {
        if (!ModemProvider.IS_QCOM) {
            logd("not QCOM, no need to wait.");
            return;
        }
        if (i != 0 && i != 1) {
            logd("vsimSlot is not 0 or 1, no need to wait.");
            return;
        }
        String str = "SIM" + (i + 1);
        if (MiuiPhoneUtils.isSimPresent(i)) {
            logd(str.concat(" is already present"));
            if (i == 1) {
                logd("wait for 2s to wait for " + str + " to be ready...");
                try {
                    Thread.sleep(2000L);
                    return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            return;
        }
        synchronized (this.mSimLock) {
            try {
                try {
                    long jCurrentTimeMillis = System.currentTimeMillis() + MirrorDesktopHelper.TIMEOUT_MILLIS;
                    logd("Waiting for " + str + " to be ready...");
                    while (!MiuiPhoneUtils.isSimPresent(i)) {
                        long jCurrentTimeMillis2 = jCurrentTimeMillis - System.currentTimeMillis();
                        if (jCurrentTimeMillis2 <= 0) {
                            logd(str + " wait timeout");
                            break;
                        }
                        this.mSimLock.wait(jCurrentTimeMillis2);
                    }
                } catch (InterruptedException e2) {
                    loge("Error waiting for " + str);
                    e2.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        logd("handlePostStopOperations done");
    }

    public List getAllEsimProfiles() {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ArrayList arrayList = new ArrayList();
            EuiccPort euiccPort = getEuiccPort(getEidFromSlotId(1));
            if (euiccPort == null) {
                logi("euiccPort is null");
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return null;
            }
            ResultCaptor resultCaptor = new ResultCaptor();
            euiccPort.getAllProfiles(resultCaptor, mHandler);
            EuiccProfileInfo[] euiccProfileInfoArr = (EuiccProfileInfo[]) resultCaptor.retrieveResult();
            if (euiccProfileInfoArr != null) {
                for (EuiccProfileInfo euiccProfileInfo : euiccProfileInfoArr) {
                    arrayList.add(euiccProfileInfo);
                }
            }
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            return arrayList;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean resetEsimMemoryForJP() {
        EuiccPort euiccPort = getEuiccPort(getEidFromSlotId(1));
        if (euiccPort == null) {
            logd("euiccPort is null");
            return false;
        }
        mIsResetMemory = true;
        ResultCaptor resultCaptor = new ResultCaptor();
        euiccPort.resetMemory(1, resultCaptor, mHandler);
        return resultCaptor.retrieveResultForRestMemory();
    }

    private EuiccPort getEuiccPort(String str) {
        UiccSlot uiccSlot;
        EuiccPort uiccPort;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        UiccController uiccController = UiccController.getInstance();
        int uiccSlotForCardId = uiccController.getUiccSlotForCardId(str);
        if (uiccSlotForCardId != -1 && (uiccSlot = uiccController.getUiccSlot(uiccSlotForCardId)) != null && uiccSlot.isEuicc() && (uiccPort = uiccController.getUiccPort(uiccSlotForCardId)) != null && (uiccPort instanceof EuiccPort)) {
            return uiccPort;
        }
        logi("EuiccPort is null. CardId : " + TelephonyUtils.pii(str));
        return null;
    }

    public boolean isSupportEsimForCountry(String str) {
        if (getEsimSecureValue() != -1) {
            return true;
        }
        if ("global".equalsIgnoreCase(SystemProperties.get("ro.miui.build.region")) || "eea".equalsIgnoreCase(SystemProperties.get("ro.miui.build.region"))) {
            return getSupportEsimCountryList().contains(str);
        }
        return true;
    }

    public boolean isSupportEsim() {
        String eidFromSlotId = getEidFromSlotId(1);
        EuiccManager euiccManager = this.mEuiccManager;
        return (euiccManager == null || !euiccManager.isEnabled() || eidFromSlotId == null) ? false : true;
    }

    public boolean isEsimEnabled() {
        if (!MiuiPhoneUtils.isEsimCapable()) {
            logi("eSIM is not supported");
            return false;
        }
        if (isRemoveEsimSwitch()) {
            logi("no eSIM switch");
            return true;
        }
        if (isSupportForRegion()) {
            return true;
        }
        logi("eSIM is not supported in current region");
        return false;
    }

    public static boolean isRemoveEsimSwitch() {
        return HyperOSCustFeatureResolve.getBoolean("config_remove_esim_switch", false) || ESIM_SIM1_MODE.equals(getEsimMode()) || ESIM_SLOT1_ONLY_MODE.equals(getEsimMode()) || getEsimEnabledByCotaValue() == 1;
    }

    public static boolean isEsimSlot1OnlyMode() {
        return ESIM_SLOT1_ONLY_MODE.equals(getEsimMode());
    }

    public boolean isSupportForRegion() {
        return isSupportEsimForCountry(getRegion());
    }

    public void recoveryForInit() {
        final int i = 0;
        if (isEsimSlot1OnlyMode()) {
            setEsimSecureValue(0);
            return;
        }
        int esimGPIOState = getEsimGPIOState();
        if (isRemoveEsimSwitch()) {
            setEsimSecureValue(0);
            if (esimGPIOState == 1 && setEsimState(0) == 0) {
                logi("recoveryForInit turn on for reset");
                return;
            }
            return;
        }
        if (getEsimSecureValue() == -1 && esimGPIOState == 0) {
            ESimHandler eSimHandler = mHandler;
            if (eSimHandler == null) {
                logw("recoveryForInit: mHandler is null");
                return;
            } else {
                eSimHandler.postDelayed(new Runnable() { // from class: com.android.phone.MiuiEsimManagerBase.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (MiuiEsimManagerBase.this.setEsimState(1) == i) {
                            Settings.Secure.putInt(PhoneGlobals.getInstance().getContentResolver(), "is_enable_esim_for_user", -1);
                        }
                        MiuiEsimManagerBase.logi("recoveryForInit turn off for reset");
                    }
                }, MiuiPhoneUtils.isSupportMep() ? 2000L : 0L);
                return;
            }
        }
        if (getEsimSecureValue() == esimGPIOState) {
            i = getEsimSecureValue() == 0 ? 1 : 0;
            logi("recoveryForInit setEsimState finalState = " + i);
            setEsimState(i);
        }
    }

    private static String getRegion() {
        try {
            String str = SystemProperties.get("ro.miui.region", "");
            return "".equals(str) ? SystemProperties.get("ro.product.locale.region", "") : str;
        } catch (Exception e) {
            loge("getRegion Exception: " + e);
            return "";
        }
    }

    public static int getEsimSecureValue() {
        return Settings.Secure.getInt(PhoneGlobals.getInstance().getContentResolver(), "is_enable_esim_for_user", -1);
    }

    public static void setEsimSecureValue(int i) {
        ContentResolver contentResolver = PhoneGlobals.getInstance().getContentResolver();
        int i2 = -1;
        if (i != -1) {
            i2 = i == 0 ? 1 : 0;
        }
        Settings.Secure.putInt(contentResolver, "is_enable_esim_for_user", i2);
    }

    public static int getEsimEnabledByCotaValue() {
        return Settings.Secure.getInt(PhoneGlobals.getInstance().getContentResolver(), "is_enable_esim_by_cota", 0);
    }

    public static void setEsimEnabledByCotaValue(int i) {
        Settings.Secure.putInt(PhoneGlobals.getInstance().getContentResolver(), "is_enable_esim_by_cota", i);
    }

    public static void setNeedShowNotificationValue(int i) {
        Settings.Secure.putInt(PhoneGlobals.getInstance().getContentResolver(), "is_need_show_notification", i);
    }

    public static int getNeedShowNotificationValue() {
        return Settings.Secure.getInt(PhoneGlobals.getInstance().getContentResolver(), "is_need_show_notification", -1);
    }

    public static void setSVNDowngradeStatus(int i) {
        Settings.Secure.putInt(PhoneGlobals.getInstance().getContentResolver(), "svn_downgrade_state", i);
    }

    public static int getSVNDowngradeStatus() {
        return Settings.Secure.getInt(PhoneGlobals.getInstance().getContentResolver(), "svn_downgrade_state", -1);
    }

    public void showEsimIsActivatingTips(Context context) {
        if (getNeedShowNotificationValue() != 1 || getEsimSecureValue() != 1) {
            logi("is not showEsimIsActivatingTips, return...");
            return;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        for (StatusBarNotification statusBarNotification : notificationManager.getActiveNotifications()) {
            if ("eSimIsActivatingNotificationForO2".equals(statusBarNotification.getNotification().getChannelId())) {
                logi("showEsimIsActivatingTips already exists...");
                return;
            }
        }
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(context.getString(R.string.esim_inactive_notification_title_global)).setSmallIcon(R.drawable.ic_esim_global_notify).setAutoCancel(true).setShowWhen(false).setContentText(context.getString(R.string.esim_is_activating_notification_message_for_o2)).setChannel("eSimIsActivatingNotificationForO2");
        notificationManager.notify(4, builder.build());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissEsimIsActivatingTips(Context context) {
        if (getNeedShowNotificationValue() != 1 || getEsimSecureValue() != 1) {
            logi("is not dismissEsimIsActivatingTips, return...");
            return;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        for (StatusBarNotification statusBarNotification : notificationManager.getActiveNotifications()) {
            if ("eSimIsActivatingNotificationForO2".equals(statusBarNotification.getNotification().getChannelId())) {
                notificationManager.cancel(4);
                logi("dismissEsimIsActivatingTips done...");
                return;
            }
        }
        logi("dismissEsimIsActivatingTips is not exists...");
    }

    public static boolean isO2eSimCard(String str) {
        return "894921".equals(str) || "894922".equals(str);
    }

    public static String getEsimMode() {
        return SystemProperties.get("ro.vendor.miui.esim_mode", "");
    }

    public static void switchToRemovableSlot() {
        final ArrayList arrayList = new ArrayList(Arrays.asList(new UiccSlotMapping(0, 0, 0)));
        isNeedSwitchMapping = true;
        TelephonyManager telephonyManager = mTelMgr;
        if (telephonyManager == null) {
            logw("switchToRemovableSlot: mTelMgr is null");
            isNeedSwitchMapping = false;
            return;
        }
        try {
            mCurSlotMappings = telephonyManager.getSimSlotMapping();
        } catch (Exception e) {
            loge("switchToRemovableSlot getSimSlotMapping fail = " + e);
            arrayList.add(new UiccSlotMapping(0, 1, 1));
        }
        Collection collection = mCurSlotMappings;
        if (collection != null && collection.size() > 1 && arrayList.size() < 2) {
            for (UiccSlotMapping uiccSlotMapping : mCurSlotMappings) {
                if (uiccSlotMapping.getLogicalSlotIndex() == 1) {
                    if (!MiuiPhoneUtils.isSupportMepForQcom()) {
                        arrayList.add(uiccSlotMapping);
                    } else {
                        arrayList.add(new UiccSlotMapping(0, 1, 1));
                    }
                }
            }
        }
        if (arrayList.equals(mCurSlotMappings) || arrayList.size() < 2) {
            logd("not need switchToRemovableSlot");
            isNeedSwitchMapping = false;
        } else {
            new Thread(new Runnable() { // from class: com.android.phone.MiuiEsimManagerBase$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    MiuiEsimManagerBase.m1135$r8$lambda$_lQZ9NguUUy0gzOJGRw6PSWuk(arrayList);
                }
            }).start();
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$_lQZ9NguUUy0g-zOJGRw6-PSWuk, reason: not valid java name */
    public static /* synthetic */ void m1135$r8$lambda$_lQZ9NguUUy0gzOJGRw6PSWuk(final List list) {
        ESimHandler eSimHandler;
        try {
            mIsSwitchingSlotMapping = true;
            if (UiccController.getInstance() == null || (eSimHandler = mHandler) == null) {
                return;
            }
            eSimHandler.post(new Runnable() { // from class: com.android.phone.MiuiEsimManagerBase$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    UiccController.getInstance().switchSlots(list, MiuiEsimManagerBase.mHandler.obtainMessage(10));
                }
            });
        } catch (Exception e) {
            loge("switchToRemovableSlot fail = " + e);
        }
    }

    public static void switchSlotMappingForCloseEsimFail() {
        Collection collection = mCurSlotMappings;
        if (collection == null || collection.size() < 2) {
            return;
        }
        if (mTelMgr == null) {
            logw("switchSlotMappingForCloseEsimFail: mTelMgr is null");
        } else {
            new Thread(new Runnable() { // from class: com.android.phone.MiuiEsimManagerBase$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    MiuiEsimManagerBase.$r8$lambda$u13gjQvLp724YnNbqPCjRLV5ZUc();
                }
            }).start();
        }
    }

    public static /* synthetic */ void $r8$lambda$u13gjQvLp724YnNbqPCjRLV5ZUc() {
        try {
            mTelMgr.setSimSlotMapping(mCurSlotMappings);
        } catch (Exception e) {
            loge("switchSlotMappingForCloseEsimFail fail = " + e);
        }
    }

    public static boolean isNeedDownGradeSVNRegion() {
        return "BA".equals(getRegion()) || "SG".equals(getRegion()) || "UZ".equals(getRegion()) || "KZ".equals(getRegion());
    }

    public static boolean isNeedDownGradeSVNDevice() {
        return HyperOSCustFeatureResolve.getBoolean("support_downgrade_svn", false);
    }

    public boolean isPsim2EsimAvailable(int i) {
        if (getEsimSecureValue() == 1) {
            return getPsim2EsimAvailabilityStatus(i) == 0;
        }
        logd("return false: esim is not turned on");
        return false;
    }

    private int getPsim2EsimAvailabilityStatus(int i) {
        SubscriptionInfo subscriptionInfoForSubscription = SubscriptionManager.getDefault().getSubscriptionInfoForSubscription(i);
        if (subscriptionInfoForSubscription == null) {
            logd("return CONDITIONALLY_UNAVAILABLE: subInfo is null");
            return 1;
        }
        if (subscriptionInfoForSubscription.getSlotId() != 0) {
            logd("return CONDITIONALLY_UNAVAILABLE: subInfo.getSlotId() != 0");
            return 1;
        }
        if (!PhoneGlobals.getInstance().getResources().getBoolean(R.bool.config_psim_conversion_menu_enabled)) {
            logd("return CONDITIONALLY_UNAVAILABLE: psim conversion menu is disabled");
            return 1;
        }
        try {
            if (!isPsimConversionSupportedOrFinished(i)) {
                logd("return CONDITIONALLY_UNAVAILABLE:psim conversion is not supported or or finished");
                return 1;
            }
            if (MiuiPhoneUtils.findConversionSupportComponent()) {
                logd("subInfo.isActivated() = " + subscriptionInfoForSubscription.isActivated() + " , subInfo.isEmbedded() = " + subscriptionInfoForSubscription.isEmbedded() + " , isActiveSubscription(subId) = " + isActiveSubscription(i));
                return (subscriptionInfoForSubscription.isActivated() && !subscriptionInfoForSubscription.isEmbedded() && isActiveSubscription(i)) ? 0 : 1;
            }
            logd("isPsim2EsimAvailable return CONDITIONALLY_UNAVAILABLE: conversion component is not found");
            return 1;
        } catch (RuntimeException e) {
            loge("Fail to check pSIM conversion supported carrier: " + e.getMessage());
        }
    }

    private boolean isActiveSubscription(int i) {
        Iterator it = SubscriptionManager.getDefault().getActiveSubscriptionInfoList().iterator();
        while (it.hasNext()) {
            if (((SubscriptionInfo) it.next()).getSubscriptionId() == i) {
                return true;
            }
        }
        return false;
    }

    private boolean isPsimConversionSupportedOrFinished(int i) {
        android.telephony.SubscriptionInfo activeSubscriptionInfo = ((android.telephony.SubscriptionManager) PhoneGlobals.getInstance().getSystemService(android.telephony.SubscriptionManager.class)).getActiveSubscriptionInfo(i);
        if (activeSubscriptionInfo == null) {
            return false;
        }
        if (activeSubscriptionInfo.getTransferStatus() == 2) {
            logd("isPsimConversionSupportedOrFinished subInfo.getTransferStatus() == android.telephony.SubscriptionManager.TRANSFER_STATUS_CONVERTED");
            return false;
        }
        logd("isPsimConversionSupportedOrFinished subInfo.getSimCarrierId() = " + activeSubscriptionInfo.getCarrierId());
        EuiccManager euiccManager = this.mEuiccManager;
        return euiccManager != null && euiccManager.isPsimConversionSupported(activeSubscriptionInfo.getCarrierId());
    }

    class BerTlv {
        private final List children;
        private byte[] data;
        private int len;
        private int tag;
        private int valueOffset;

        public BerTlv(byte[] bArr) {
            this(bArr, 0, bArr.length);
        }

        public BerTlv(byte[] bArr, int i, int i2) {
            this.children = new ArrayList();
            this.data = bArr;
            int iDecodeTag = decodeTag(bArr, i);
            if (iDecodeTag < i + i2) {
                this.valueOffset = decodeLen(bArr, iDecodeTag);
                decodeChildren();
            }
        }

        public int getTag() {
            return this.tag;
        }

        public byte[] getValue() {
            byte[] bArr = new byte[this.len];
            copyValue(bArr, 0);
            return bArr;
        }

        public void copyValue(byte[] bArr, int i) {
            System.arraycopy(this.data, this.valueOffset, bArr, i, this.len);
        }

        public List getChildren() {
            return this.children;
        }

        public BerTlv find(int i) {
            return find(i, false);
        }

        public BerTlv find(int i, boolean z) {
            return z ? findBfs(i) : findDfs(i, this);
        }

        private BerTlv findBfs(int i) {
            LinkedList linkedList = new LinkedList();
            linkedList.add(this);
            while (linkedList.peek() != null) {
                BerTlv berTlv = (BerTlv) linkedList.poll();
                if (berTlv.getTag() == i) {
                    return berTlv;
                }
                linkedList.addAll(berTlv.getChildren());
            }
            return null;
        }

        public int getTlvLength() {
            return getIntSize(this.tag) + getIntSize(getEncodedLength()) + this.len;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            sb.append(Utils.intToHexString(this.tag));
            sb.append('|');
            sb.append(Utils.intToHexString(getEncodedLength()));
            sb.append('|');
            if (isConstructed()) {
                Iterator it = getChildren().iterator();
                while (it.hasNext()) {
                    sb.append(((BerTlv) it.next()).toString());
                }
            } else {
                sb.append(Utils.bytesToHexString(getValue()));
            }
            sb.append(']');
            return sb.toString();
        }

        private int decodeTag(byte[] bArr, int i) {
            byte b;
            int i2 = i + 1;
            byte b2 = bArr[i];
            this.tag = b2 & TransitionInfo.INIT;
            if ((b2 & 31) == 31) {
                do {
                    int i3 = this.tag << 8;
                    this.tag = i3;
                    b = bArr[i2];
                    this.tag = i3 + (b & TransitionInfo.INIT);
                    i2++;
                } while ((b & (-128)) == -128);
            }
            return i2;
        }

        private int decodeLen(byte[] bArr, int i) {
            int i2 = i + 1;
            byte b = bArr[i];
            this.len = b & TransitionInfo.INIT;
            if ((b & 128) == 128) {
                int i3 = b & 127;
                this.len = 0;
                while (i3 > 0) {
                    int i4 = this.len << 8;
                    this.len = i4;
                    this.len = i4 + (bArr[i2] & TransitionInfo.INIT);
                    i3--;
                    i2++;
                }
            }
            return i2;
        }

        private void decodeChildren() {
            if (!isConstructed()) {
                return;
            }
            int tlvLength = this.valueOffset;
            while (true) {
                int i = this.valueOffset;
                int i2 = this.len;
                if (tlvLength >= i + i2) {
                    return;
                }
                BerTlv berTlv = new BerTlv(this.data, tlvLength, (i + i2) - tlvLength);
                this.children.add(berTlv);
                tlvLength += berTlv.getTlvLength();
            }
        }

        private boolean isConstructed() {
            int iNumberOfLeadingZeros = 32 << ((3 - (Integer.numberOfLeadingZeros(this.tag) / 8)) * 8);
            return (this.tag & iNumberOfLeadingZeros) == iNumberOfLeadingZeros;
        }

        private BerTlv findDfs(int i, BerTlv berTlv) {
            if (berTlv.getTag() == i) {
                return berTlv;
            }
            Iterator it = berTlv.getChildren().iterator();
            while (it.hasNext()) {
                BerTlv berTlvFindDfs = findDfs(i, (BerTlv) it.next());
                if (berTlvFindDfs != null) {
                    return berTlvFindDfs;
                }
            }
            return null;
        }

        public int getEncodedLength() {
            int i;
            int i2 = this.len;
            if (i2 < 128) {
                return i2;
            }
            if (i2 < 256) {
                i = 33024;
            } else {
                i = i2 < 65536 ? 8519680 : -2097152000;
            }
            return i2 + i;
        }

        private int getIntSize(int i) {
            if (i == 0) {
                return 1;
            }
            return 4 - (Integer.numberOfLeadingZeros(i) / 8);
        }
    }

    public static void trackerEsimEvent(String str, Object obj) {
        logd("trackerEsimEvent action = " + str + " , result = " + obj);
        HashMap map = new HashMap();
        map.put("eSIMAction", str);
        map.put("eSIMActionResult", obj);
        ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "esim_event", map);
    }

    public static int disableProfile() {
        try {
            ApplicationInfo applicationInfo = mContext.getPackageManager().getApplicationInfo("com.miui.euicc", 0);
            if (applicationInfo == null) {
                return -1;
            }
            boolean z = (applicationInfo.flags & 129) > 0 || UserHandle.getAppId(applicationInfo.uid) < 10000;
            logd("disableProfile isSystemApp = " + z);
            if (!z) {
                return -1;
            }
            try {
                Uri uri = Uri.parse("content://com.miui.euicc.provider");
                Bundle bundle = new Bundle();
                bundle.putBoolean(PARAMS_EUICC_REFRESH, ModemProvider.IS_QCOM);
                Bundle bundleCall = mContext.getContentResolver().call(uri, METHOD_EUICC_DISABLE_ALL_PROFILE, (String) null, bundle);
                if (bundleCall != null) {
                    return bundleCall.getInt(EUICC_RESPONSE_CODE, -1);
                }
                return -1;
            } catch (Exception e) {
                loge(" disableProfile fail - " + e);
            }
        } catch (Exception unused) {
        }
        return -1;
    }

    public boolean isDualEsimActive() {
        List activeSubscriptionInfoList = SubscriptionManager.getDefault().getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList == null || activeSubscriptionInfoList.size() == 0 || activeSubscriptionInfoList.size() == 1) {
            logd("activeSubInfoList is empty");
            return false;
        }
        Iterator it = activeSubscriptionInfoList.iterator();
        while (it.hasNext()) {
            if (!((SubscriptionInfo) it.next()).isEmbedded()) {
                return false;
            }
        }
        return true;
    }

    public String[] openChannelEsim() {
        return new String[]{""};
    }

    public String[] closeChannelEsim(int i) {
        return new String[]{""};
    }

    public String[] sendApduEsim(int i, String str) {
        return new String[]{""};
    }

    class EuiccFailureDialog extends AlertDialog {
        public EuiccFailureDialog(Context context) {
            super(context, miuix.appcompat.R$style.AlertDialog_Theme_DayNight);
            getWindow().setType(2003);
            getWindow().setFlags(131072, 131072);
            setTitle(R.string.euicc_binding_error_alert_title);
            setMessage(MiuiEsimManagerBase.mContext.getString(R.string.euicc_binding_error_alert_message));
            setCancelable(false);
            setButton(-1, MiuiEsimManagerBase.mContext.getString(R.string.got_it), new DialogInterface.OnClickListener() { // from class: com.android.phone.MiuiEsimManagerBase$EuiccFailureDialog$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    this.f$0.lambda$new$1(dialogInterface, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(DialogInterface dialogInterface, int i) {
            dismiss();
            ESimHandler eSimHandler = MiuiEsimManagerBase.mHandler;
            if (eSimHandler != null) {
                eSimHandler.post(new Runnable() { // from class: com.android.phone.MiuiEsimManagerBase$EuiccFailureDialog$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$new$0();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            if (MiuiEsimManagerBase.this.setEsimState(1) == 0 && MiuiEsimManagerBase.this.shouldResetPortAndState()) {
                MiuiEsimManagerBase.this.resetPortAndStateForSlot(1);
            }
        }
    }

    protected void showEuiccFailureAlertDialog() {
        logd("showEuiccFailureAlertDialog start");
        if (mContext == null) {
            loge("showEuiccFailureAlertDialog: mContext is null");
        } else if (Looper.myLooper() == null || Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.android.phone.MiuiEsimManagerBase$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.showEuiccFailureDialogInternal();
                }
            });
        } else {
            showEuiccFailureDialogInternal();
            logd("showEuiccFailureAlertDialog end");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showEuiccFailureDialogInternal() {
        try {
            new EuiccFailureDialog(mContext).show();
        } catch (Exception e) {
            loge("showEuiccFailureAlertDialog: show dialog failed - " + e);
        }
    }

    public int getEuiccBindingResult() {
        return SystemProperties.getInt("persist.miui.euicc.binding_result", -2);
    }

    protected static void logi(String str) {
        Rlog.i("MiuiEsimManager", str);
    }

    protected static void logd(String str) {
        Rlog.d("MiuiEsimManager", str);
    }

    protected static void logw(String str) {
        Rlog.w("MiuiEsimManager", str);
    }

    protected static void loge(String str) {
        Rlog.e("MiuiEsimManager", str);
    }
}
