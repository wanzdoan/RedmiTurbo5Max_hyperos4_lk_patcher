package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class CdmaDataOptimizeManager {
    private static String LOG_TAG = "CdmaDataOptimizeManager";
    protected static Context mContext = null;
    protected static int mDefaultDataSlotId = -1;
    protected static boolean mInCdmaState;
    protected static boolean mIsDataConnected;
    protected static boolean mIsWifiConnected;
    protected static boolean mOnVoiceCall;
    protected static CdmaDataOptimizeManager sInstance;
    ContentObserver mDataEnabledObserver;
    protected CdmaDataOptimizeHandler mHandler;
    protected boolean mIsAirplaneEnabled;
    protected CdmaDataOptimizeStateMachine mStateMachine;
    private TelephonyManager mTelephonyManager;
    protected boolean mUserDataEnabled;
    protected HandlerThread mWorkThread;
    protected int mOldRat = -1;
    private TelephonyCallBack mTelephonyCallBack = null;
    PhoneStateListener mPhoneStateListener = new PhoneStateListener() { // from class: com.android.phone.CdmaDataOptimizeManager.1
        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int i, String str) {
            CdmaDataOptimizeManager.log("onCallStateChanged: state = " + i);
            boolean zIsPhoneStateIdle = CdmaDataOptimizeManager.this.isPhoneStateIdle();
            CdmaDataOptimizeManager cdmaDataOptimizeManager = CdmaDataOptimizeManager.this;
            if (zIsPhoneStateIdle) {
                CdmaDataOptimizeHandler cdmaDataOptimizeHandler = cdmaDataOptimizeManager.mHandler;
                cdmaDataOptimizeHandler.sendMessageDelayed(cdmaDataOptimizeHandler.obtainMessage(7), MirrorDesktopHelper.TIMEOUT_MILLIS);
            } else {
                CdmaDataOptimizeHandler cdmaDataOptimizeHandler2 = cdmaDataOptimizeManager.mHandler;
                cdmaDataOptimizeHandler2.sendMessage(cdmaDataOptimizeHandler2.obtainMessage(7));
            }
        }
    };
    public final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.CdmaDataOptimizeManager.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            CdmaDataOptimizeManager.log("IntentReceiver action = " + intent.getAction());
            if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                CdmaDataOptimizeManager.this.mHandler.obtainMessage(2, intent).sendToTarget();
                return;
            }
            if (action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED") || action.equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY")) {
                CdmaDataOptimizeManager.this.mHandler.obtainMessage(3).sendToTarget();
                return;
            }
            if (action.equals("android.intent.action.AIRPLANE_MODE")) {
                CdmaDataOptimizeManager.this.mHandler.obtainMessage(4, intent).sendToTarget();
                return;
            }
            if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                CdmaDataOptimizeManager.this.mHandler.obtainMessage(9, intent).sendToTarget();
            } else if (action.equals("com.android.phone.timeout.alarm")) {
                CdmaDataOptimizeStateMachine cdmaDataOptimizeStateMachine = CdmaDataOptimizeManager.this.mStateMachine;
                cdmaDataOptimizeStateMachine.sendMessage(cdmaDataOptimizeStateMachine.obtainMessage(2));
            }
        }
    };

    public boolean isCdma(int i) {
        return i == 4 || i == 5 || i == 6;
    }

    protected CdmaDataOptimizeManager() {
        this.mDataEnabledObserver = new ContentObserver(this.mHandler) { // from class: com.android.phone.CdmaDataOptimizeManager.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                CdmaDataOptimizeManager.this.mHandler.obtainMessage(6).sendToTarget();
            }
        };
        log("CdmaDataOptimizeManager init.");
        mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("CdmaDataOptimizeThread");
        this.mWorkThread = handlerThread;
        handlerThread.start();
        CdmaDataOptimizeHandler cdmaDataOptimizeHandler = new CdmaDataOptimizeHandler(this.mWorkThread.getLooper());
        this.mHandler = cdmaDataOptimizeHandler;
        cdmaDataOptimizeHandler.sendEmptyMessage(1);
        this.mStateMachine = CdmaDataOptimizeStateMachine.makeStateMachine(mContext, this.mHandler);
        log("CdmaDataOptimizeManager inited!");
    }

    public static void init() {
        synchronized (CdmaDataOptimizeManager.class) {
            try {
                if (sInstance == null) {
                    sInstance = new CdmaDataOptimizeManager();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static CdmaDataOptimizeManager getInstance() {
        if (sInstance == null) {
            log("getInstance null");
        }
        return sInstance;
    }

    protected void onInitCdmaData() {
        this.mTelephonyManager = (TelephonyManager) mContext.getSystemService("phone");
        registerDeviceState();
    }

    protected void registerDeviceState() {
        log("registerDeviceState");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        intentFilter.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_READY");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("com.android.phone.timeout.alarm");
        mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
        this.mTelephonyManager.listen(this.mPhoneStateListener, 32);
        mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor(getMobileDataSettingName()), false, this.mDataEnabledObserver);
        if (this.mTelephonyCallBack == null) {
            log("register TelephonyCallBack");
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    protected void unregisterDeviceState() {
        log("unregisterDeviceState");
        try {
            PhoneStateListener phoneStateListener = this.mPhoneStateListener;
            if (phoneStateListener != null) {
                this.mTelephonyManager.listen(phoneStateListener, 0);
                this.mPhoneStateListener = null;
            }
            mContext.unregisterReceiver(this.mIntentReceiver);
            mContext.getContentResolver().unregisterContentObserver(this.mDataEnabledObserver);
            TelephonyCallBack telephonyCallBack = this.mTelephonyCallBack;
            if (telephonyCallBack != null) {
                TelephonyStatAdapter.unregisterCallBack(telephonyCallBack);
                this.mTelephonyCallBack = null;
            }
        } catch (Exception e) {
            log("unregisterDeviceState: " + e.getMessage());
        }
    }

    private String getMobileDataSettingName() {
        return MiuiPhoneUtils.PHONE_COUNT > 1 ? "mobile_data0" : "mobile_data";
    }

    protected static boolean isChinaTelecomSim() {
        int i = mDefaultDataSlotId;
        if (i != -1) {
            return MiuiPhoneUtils.isChinaOrMacauTelecomSim(MiuiPhoneUtils.getPhone(i));
        }
        return false;
    }

    protected void cleanup() {
        log("cleanup()");
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "cdma_data_optimize_quit");
        this.mStateMachine.quitStateMachine();
        unregisterDeviceState();
    }

    protected static boolean isMobileDataEnabled() {
        return MiuiPhoneUtils.isMobileDataEnabled(mContext);
    }

    public class CdmaDataOptimizeHandler extends Handler {
        public CdmaDataOptimizeHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            boolean z;
            CdmaDataOptimizeManager.log("handleMessage what = " + message.what);
            switch (message.what) {
                case 1:
                    CdmaDataOptimizeManager.this.onInitCdmaData();
                    break;
                case 2:
                    CdmaDataOptimizeManager.this.onSimStateChanged((Intent) message.obj);
                    break;
                case 3:
                    CdmaDataOptimizeManager.this.updateDefaultDataSlotId();
                    CdmaDataOptimizeManager.this.mStateMachine.sendMessage(8);
                    break;
                case 4:
                    CdmaDataOptimizeManager.this.updataAirplaneModeState((Intent) message.obj);
                    CdmaDataOptimizeManager cdmaDataOptimizeManager = CdmaDataOptimizeManager.this;
                    if (cdmaDataOptimizeManager.mIsAirplaneEnabled) {
                        cdmaDataOptimizeManager.mStateMachine.sendMessage(9);
                    }
                    break;
                case 5:
                    int i = message.arg1;
                    z = message.arg2 == 0;
                    CdmaDataOptimizeManager.log("network state changed, networkType is " + i + ", new state: " + z);
                    if (i == 1) {
                        if (!z) {
                            CdmaDataOptimizeManager.this.mStateMachine.sendMessage(6);
                        } else if (z) {
                            CdmaDataOptimizeManager.this.mStateMachine.sendMessage(12);
                        }
                    }
                    break;
                case 6:
                    CdmaDataOptimizeManager.this.updataMobileDataEnabled();
                    CdmaDataOptimizeManager cdmaDataOptimizeManager2 = CdmaDataOptimizeManager.this;
                    if (cdmaDataOptimizeManager2.mUserDataEnabled) {
                        cdmaDataOptimizeManager2.mStateMachine.sendMessage(15);
                    } else {
                        cdmaDataOptimizeManager2.mStateMachine.sendMessage(14);
                    }
                    break;
                case 7:
                    CdmaDataOptimizeManager.this.updataVoiceCallState();
                    if (CdmaDataOptimizeManager.mOnVoiceCall) {
                        CdmaDataOptimizeManager.this.mStateMachine.sendMessage(7);
                    }
                    break;
                case 8:
                    CdmaDataOptimizeManager cdmaDataOptimizeManager3 = CdmaDataOptimizeManager.this;
                    int i2 = cdmaDataOptimizeManager3.mOldRat;
                    if (i2 == 1 && !CdmaDataOptimizeManager.mOnVoiceCall) {
                        CdmaDataOptimizeManager.mInCdmaState = true;
                        cdmaDataOptimizeManager3.mStateMachine.sendMessage(3);
                    } else if (i2 == 2) {
                        CdmaDataOptimizeManager.mInCdmaState = false;
                        cdmaDataOptimizeManager3.mStateMachine.sendMessage(13);
                    } else {
                        CdmaDataOptimizeManager.mInCdmaState = false;
                        cdmaDataOptimizeManager3.mStateMachine.sendMessage(11);
                    }
                    break;
                case 9:
                    NetworkInfo networkInfo = (NetworkInfo) ((Intent) message.obj).getParcelableExtra("networkInfo");
                    if (networkInfo != null) {
                        int type = networkInfo.getType();
                        CdmaDataOptimizeManager.log("networkType=" + type);
                        if (type == 1) {
                            CdmaDataOptimizeManager cdmaDataOptimizeManager4 = CdmaDataOptimizeManager.this;
                            NetworkInfo.State state = networkInfo.getState();
                            NetworkInfo.State state2 = NetworkInfo.State.CONNECTED;
                            cdmaDataOptimizeManager4.updateWifiStatus(state == state2);
                            if (networkInfo.getState() == state2) {
                                CdmaDataOptimizeManager.this.mHandler.obtainMessage(5, networkInfo.getType(), 0).sendToTarget();
                            } else if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                                CdmaDataOptimizeManager.this.mHandler.obtainMessage(5, networkInfo.getType(), 1).sendToTarget();
                            }
                        } else if (type == 0) {
                            z = networkInfo.getState() == NetworkInfo.State.CONNECTED;
                            CdmaDataOptimizeManager.this.updateDataConnectionStatus(z);
                            if (z) {
                                CdmaDataOptimizeManager.this.mStateMachine.sendMessage(5);
                            }
                        }
                        break;
                    }
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSimStateChanged(Intent intent) {
        int intExtra = intent.getIntExtra("phone", -1);
        if (SubscriptionManager.isValidPhoneId(intExtra)) {
            String stringExtra = intent.getStringExtra("ss");
            log("onSimStateChanged iccState=" + stringExtra + ", phoneId=" + intExtra);
            if ("ABSENT".equals(stringExtra)) {
                this.mStateMachine.sendMessage(10);
            } else if ("LOADED".equals(stringExtra) && isChinaTelecomSim()) {
                this.mStateMachine.sendMessage(4);
            }
        }
    }

    protected void updataMobileDataEnabled() {
        this.mUserDataEnabled = isMobileDataEnabled();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDefaultDataSlotId() {
        mDefaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDataConnectionStatus(boolean z) {
        mIsDataConnected = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWifiStatus(boolean z) {
        mIsWifiConnected = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updataVoiceCallState() {
        if (isPhoneStateIdle()) {
            mOnVoiceCall = false;
        } else {
            mOnVoiceCall = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updataAirplaneModeState(Intent intent) {
        this.mIsAirplaneEnabled = intent.getBooleanExtra(MEModuleSignalT1s.EXTRA_STATE, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRatChange(ServiceState serviceState) {
        int i;
        if (isInCdmaState(serviceState)) {
            i = 1;
        } else {
            i = isInLteOrNrState(serviceState) ? 2 : 0;
        }
        if (this.mOldRat == i) {
            return false;
        }
        this.mOldRat = i;
        return true;
    }

    private boolean isInCdmaState(ServiceState serviceState) {
        if (serviceState == null) {
            loge("isInCdmaState: null ServiceState!");
            return false;
        }
        if (!isCdma(serviceState.getRilDataRadioTechnology()) || !isCdma(serviceState.getRilVoiceRadioTechnology())) {
            return false;
        }
        log("isInCdmaState is true");
        return true;
    }

    private boolean isInLteOrNrState(ServiceState serviceState) {
        if (serviceState == null) {
            loge("isInLteOrNrState: null ServiceState!");
            return false;
        }
        if (!ServiceState.isPsOnlyTech(serviceState.getRilDataRadioTechnology())) {
            return false;
        }
        log("isInLteOrNrState is true");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPhoneStateIdle() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null && phone.getState() != PhoneConstants.State.IDLE) {
                log("isPhoneStateIdle false: Voice call active on phone" + i);
                return false;
            }
        }
        return true;
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            CdmaDataOptimizeManager.log("onServiceStateChanged phoneId = " + i + ", ServiceState" + serviceState);
            if (CdmaDataOptimizeManager.this.isValidatePhoneId(i) && i == CdmaDataOptimizeManager.mDefaultDataSlotId && CdmaDataOptimizeManager.this.isRatChange(serviceState)) {
                CdmaDataOptimizeManager.log("onServiceStateChanged isRatChange: delay report for 5000");
                if (CdmaDataOptimizeManager.this.mHandler.hasMessages(8)) {
                    CdmaDataOptimizeManager.this.mHandler.removeMessages(8);
                }
                CdmaDataOptimizeHandler cdmaDataOptimizeHandler = CdmaDataOptimizeManager.this.mHandler;
                cdmaDataOptimizeHandler.sendMessageDelayed(cdmaDataOptimizeHandler.obtainMessage(8), MirrorDesktopHelper.TIMEOUT_MILLIS);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isValidatePhoneId(int i) {
        return MiuiPhoneUtils.isValidatePhoneId(i);
    }

    public static void log(String str) {
        Log.d(LOG_TAG, str);
    }

    public static void loge(String str) {
        Log.e(LOG_TAG, str);
    }
}
