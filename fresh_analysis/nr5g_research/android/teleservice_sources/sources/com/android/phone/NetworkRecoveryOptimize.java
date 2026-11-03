package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.util.ArrayUtils;
import com.android.phone.CloudController.TelephonyCloudController;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.statistics.MiuiTelephonyStatistic;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiThreadUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;
import java.util.Map;
import miui.os.Build;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class NetworkRecoveryOptimize {
    private String mCurrentNetworkName;
    final Handler mHandler;
    boolean mIsReadyToRecoveryOnUnlock;
    Phone mPhone;
    int mPreferredNetworkMode;
    ContentObserver mPreferredNetworkModeObserver;
    private long mPreviousNetworkRecoveryTime;
    final BroadcastReceiver mReceiver;
    private long mRecoveredNetworkTime;
    private MiuiTelephonyStatistic.StatisticListener mStatisticListener;
    private TelephonyCallBack mTelephonyCallBack = null;
    private long mRecoveredNetworkStartTime = -1;
    private Map mRecoveredNetworkDuration = new HashMap();
    boolean mIsWifiConnected = false;

    boolean isPsOnlyTech(int i) {
        return i == 13 || i == 19 || i == 20;
    }

    public NetworkRecoveryOptimize(int i) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.NetworkRecoveryOptimize.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    if (NetworkRecoveryOptimize.this.mPhone.getPhoneId() == SubscriptionManager.getSlotIdExtra(intent, SubscriptionManager.INVALID_SLOT_ID)) {
                        String stringExtra = intent.getStringExtra("ss");
                        if ("ABSENT".equals(stringExtra)) {
                            NetworkRecoveryOptimize.this.unRegisterTelephony();
                            NetworkRecoveryOptimize.this.cancelRecoveryNetworkOnUnlock();
                            if (NetworkRecoveryOptimize.this.mHandler.hasMessages(5)) {
                                NetworkRecoveryOptimize.this.mHandler.removeMessages(5);
                                NetworkRecoveryOptimize.this.recordRecoveryNetworkEvent("SIM_OOS", "INVAILD");
                            }
                            if (NetworkRecoveryOptimize.this.mHandler.hasMessages(6)) {
                                NetworkRecoveryOptimize.this.mHandler.removeMessages(6);
                                NetworkRecoveryOptimize.this.recordRecoveryNetworkEvent("RECOVERY_SUCCESS", "SIM_OOS");
                            }
                            NetworkRecoveryOptimize networkRecoveryOptimize = NetworkRecoveryOptimize.this;
                            networkRecoveryOptimize.calculateNetworkRecoveryTime(networkRecoveryOptimize.mCurrentNetworkName);
                            return;
                        }
                        if ("NOT_READY".equals(stringExtra) || "READY".equals(stringExtra) || "LOADED".equals(stringExtra)) {
                            NetworkRecoveryOptimize.this.registerTelephony();
                            return;
                        }
                        return;
                    }
                    return;
                }
                if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                    NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                    if (networkInfo == null) {
                        return;
                    }
                    int type = networkInfo.getType();
                    NetworkRecoveryOptimize.this.log("CONNECTIVITY_ACTION, networkType = " + type + ", state = " + networkInfo.getState());
                    if (type == 1 && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        NetworkRecoveryOptimize networkRecoveryOptimize2 = NetworkRecoveryOptimize.this;
                        if (networkRecoveryOptimize2.mIsWifiConnected) {
                            return;
                        }
                        networkRecoveryOptimize2.mIsWifiConnected = true;
                        networkRecoveryOptimize2.cancelRecoveryNetworkOnUnlock();
                        return;
                    }
                    if (type == 1 && networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                        NetworkRecoveryOptimize networkRecoveryOptimize3 = NetworkRecoveryOptimize.this;
                        if (networkRecoveryOptimize3.mIsWifiConnected) {
                            networkRecoveryOptimize3.mIsWifiConnected = false;
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (action.equals("android.intent.action.USER_PRESENT")) {
                    NetworkRecoveryOptimize.this.mHandler.sendEmptyMessage(4);
                }
            }
        };
        this.mReceiver = broadcastReceiver;
        Handler handler = new Handler(MiuiThreadUtil.getWorkThreadLooper()) { // from class: com.android.phone.NetworkRecoveryOptimize.2
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        NetworkRecoveryOptimize.this.processServiceStateChanged((ServiceState) message.obj);
                        break;
                    case 2:
                        NetworkRecoveryOptimize.this.executeStatistics();
                        break;
                    case 3:
                        NetworkRecoveryOptimize.this.mIsReadyToRecoveryOnUnlock = true;
                        break;
                    case 4:
                        NetworkRecoveryOptimize.this.log("mIsReadyToRecoveryOnUnlock = " + NetworkRecoveryOptimize.this.mIsReadyToRecoveryOnUnlock + ", state = " + NetworkRecoveryOptimize.this.mPhone.getState());
                        NetworkRecoveryOptimize networkRecoveryOptimize = NetworkRecoveryOptimize.this;
                        if (networkRecoveryOptimize.mIsReadyToRecoveryOnUnlock && networkRecoveryOptimize.mPhone.getState() == PhoneConstants.State.IDLE) {
                            NetworkRecoveryOptimize networkRecoveryOptimize2 = NetworkRecoveryOptimize.this;
                            networkRecoveryOptimize2.mIsReadyToRecoveryOnUnlock = false;
                            networkRecoveryOptimize2.recoveryNetwork();
                            break;
                        }
                        break;
                    case 5:
                        NetworkRecoveryOptimize.this.recordRecoveryNetworkEvent("RECOVERY_FAIL", "INVAILD");
                        break;
                    case 6:
                        NetworkRecoveryOptimize.this.recordRecoveryNetworkEvent("RECOVERY_SUCCESS", "MAX_TIME");
                        break;
                }
            }
        };
        this.mHandler = handler;
        this.mPreferredNetworkModeObserver = new ContentObserver(handler) { // from class: com.android.phone.NetworkRecoveryOptimize.3
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                int preferredNetworkModeFromDb = ModemProvider.getInstance().getPreferredNetworkModeFromDb(NetworkRecoveryOptimize.this.mPhone.getPhoneId());
                NetworkRecoveryOptimize networkRecoveryOptimize = NetworkRecoveryOptimize.this;
                if (networkRecoveryOptimize.mPreferredNetworkMode != preferredNetworkModeFromDb) {
                    networkRecoveryOptimize.cancelRecoveryNetworkOnUnlock();
                    NetworkRecoveryOptimize networkRecoveryOptimize2 = NetworkRecoveryOptimize.this;
                    networkRecoveryOptimize2.mPreferredNetworkMode = preferredNetworkModeFromDb;
                    if (networkRecoveryOptimize2.mHandler.hasMessages(5)) {
                        NetworkRecoveryOptimize.this.mHandler.removeMessages(5);
                        NetworkRecoveryOptimize.this.recordRecoveryNetworkEvent("PREF_CHANGED", "INVAILD");
                    }
                    if (NetworkRecoveryOptimize.this.mHandler.hasMessages(6)) {
                        NetworkRecoveryOptimize.this.mHandler.removeMessages(6);
                        NetworkRecoveryOptimize.this.recordRecoveryNetworkEvent("RECOVERY_SUCCESS", "PREF_CHANGED");
                    }
                    NetworkRecoveryOptimize networkRecoveryOptimize3 = NetworkRecoveryOptimize.this;
                    networkRecoveryOptimize3.calculateNetworkRecoveryTime(networkRecoveryOptimize3.mCurrentNetworkName);
                }
            }
        };
        this.mStatisticListener = new MiuiTelephonyStatistic.StatisticListener() { // from class: com.android.phone.NetworkRecoveryOptimize.4
            @Override // com.android.phone.statistics.MiuiTelephonyStatistic.StatisticListener
            public HashMap getStatisticListener() {
                HashMap map = new HashMap();
                boolean z = NetworkRecoveryOptimize.this.mRecoveredNetworkStartTime != -1;
                NetworkRecoveryOptimize networkRecoveryOptimize = NetworkRecoveryOptimize.this;
                networkRecoveryOptimize.calculateNetworkRecoveryTime(networkRecoveryOptimize.mCurrentNetworkName);
                NetworkRecoveryOptimize.this.mRecoveredNetworkStartTime = z ? System.currentTimeMillis() : -1L;
                if (!NetworkRecoveryOptimize.this.mRecoveredNetworkDuration.isEmpty()) {
                    map.put("network_recovery_duration" + NetworkRecoveryOptimize.this.mPhone.getPhoneId(), NetworkRecoveryOptimize.this.mRecoveredNetworkDuration.toString());
                }
                NetworkRecoveryOptimize.this.mRecoveredNetworkDuration.clear();
                return map;
            }
        };
        this.mPhone = PhoneFactory.getPhone(i);
        this.mPreferredNetworkMode = ModemProvider.getInstance().getPreferredNetworkModeFromDb(i);
        this.mPhone.getContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor("preferred_network_mode"), false, this.mPreferredNetworkModeObserver);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        this.mPhone.getContext().registerReceiver(broadcastReceiver, intentFilter, 2);
        MiuiTelephonyStatistic miuiTelephonyStatistic = MiuiTelephonyStatistic.getInstance();
        if (miuiTelephonyStatistic != null) {
            miuiTelephonyStatistic.addStatisticListener(this.mStatisticListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerTelephony() {
        if (this.mTelephonyCallBack == null) {
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unRegisterTelephony() {
        TelephonyCallBack telephonyCallBack = this.mTelephonyCallBack;
        if (telephonyCallBack != null) {
            TelephonyStatAdapter.unregisterCallBack(telephonyCallBack);
            this.mTelephonyCallBack = null;
        }
    }

    void processServiceStateChanged(ServiceState serviceState) {
        if (this.mIsWifiConnected || !isLteOrNrPrefNetworkMode()) {
            cancelRecoveryNetworkOnUnlock();
            return;
        }
        NetworkRegistrationInfo networkRegistrationInfo = serviceState == null ? null : serviceState.getNetworkRegistrationInfo(2, 1);
        if (networkRegistrationInfo == null) {
            log("processServiceStateChanged networkInfo is null");
            cancelRecoveryNetworkOnUnlock();
            this.mCurrentNetworkName = "";
            return;
        }
        int accessNetworkTechnology = networkRegistrationInfo.getAccessNetworkTechnology();
        String str = this.mCurrentNetworkName;
        this.mCurrentNetworkName = getNetworkName(accessNetworkTechnology);
        if (!MiuiPhoneUtils.isIccCardActivated(this.mPhone.getPhoneId()) || accessNetworkTechnology == 0) {
            cancelRecoveryNetworkOnUnlock();
            if (this.mHandler.hasMessages(6)) {
                this.mHandler.removeMessages(6);
                recordRecoveryNetworkEvent("RECOVERY_SUCCESS", "SIM_OOS");
            }
            calculateNetworkRecoveryTime(str);
            return;
        }
        if (isPsOnlyTech(accessNetworkTechnology)) {
            cancelRecoveryNetworkOnUnlock();
            if (this.mHandler.hasMessages(5)) {
                this.mHandler.removeMessages(5);
                Handler handler = this.mHandler;
                handler.sendMessageDelayed(handler.obtainMessage(6), 300000L);
                this.mRecoveredNetworkTime = System.currentTimeMillis();
                this.mRecoveredNetworkStartTime = System.currentTimeMillis();
                return;
            }
            if (this.mRecoveredNetworkStartTime == -1 || str.equals(this.mCurrentNetworkName)) {
                return;
            }
            calculateNetworkRecoveryTime(str);
            this.mRecoveredNetworkStartTime = System.currentTimeMillis();
            return;
        }
        if (this.mHandler.hasMessages(6)) {
            this.mHandler.removeMessages(6);
            recordRecoveryNetworkEvent("RECOVERY_SUCCESS", "RECOVERY_SUCCESS", System.currentTimeMillis() - this.mRecoveredNetworkTime);
        }
        calculateNetworkRecoveryTime(str);
        prepareRecoveryNetworkOnUnlock();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void calculateNetworkRecoveryTime(String str) {
        if (TextUtils.isEmpty(str) || this.mRecoveredNetworkStartTime == -1) {
            this.mRecoveredNetworkStartTime = -1L;
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis() - this.mRecoveredNetworkStartTime;
        boolean zContainsKey = this.mRecoveredNetworkDuration.containsKey(str);
        Map map = this.mRecoveredNetworkDuration;
        if (!zContainsKey) {
            map.put(str, Long.valueOf(jCurrentTimeMillis));
        } else {
            map.put(str, Long.valueOf(((Long) map.get(str)).longValue() + jCurrentTimeMillis));
        }
        this.mRecoveredNetworkStartTime = -1L;
    }

    private String getNetworkName(int i) {
        return TelephonyManagerEx.getDefault().getNetworkClassName(i) + "_" + getPreferredNetworkModeName();
    }

    private boolean isMatchNetworkMode(int i) {
        return ArrayUtils.contains(((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportNetworkMode(i), this.mPreferredNetworkMode);
    }

    private String getPreferredNetworkModeName() {
        if (isMatchNetworkMode(6) || isMatchNetworkMode(5) || isMatchNetworkMode(8) || isMatchNetworkMode(4) || isMatchNetworkMode(7)) {
            return "LTE";
        }
        if (isMatchNetworkMode(9)) {
            return "NR";
        }
        return "UNKNOWN";
    }

    private void prepareRecoveryNetworkOnUnlock() {
        if (this.mHandler.hasMessages(3) || this.mIsReadyToRecoveryOnUnlock) {
            log("prepareRecoveryNetworkOnUnlock\u3000in progress");
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis() - this.mPreviousNetworkRecoveryTime;
        if (jCurrentTimeMillis > 10800000) {
            log("prepareRecoveryNetworkOnUnlock, duration = " + jCurrentTimeMillis);
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(3), 600000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelRecoveryNetworkOnUnlock() {
        this.mIsReadyToRecoveryOnUnlock = false;
        if (this.mHandler.hasMessages(3)) {
            this.mHandler.removeMessages(3);
        }
    }

    boolean isLteOrNrPrefNetworkMode() {
        return ArrayUtils.contains(((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportNetworkMode(7), this.mPreferredNetworkMode) || ArrayUtils.contains(((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getSupportNetworkMode(9), this.mPreferredNetworkMode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recoveryNetwork() {
        boolean zIsSupportNetworkRecoveryOptimization = MiuiPhoneUtils.isSupportNetworkRecoveryOptimization(this.mPhone.getContext());
        if (!zIsSupportNetworkRecoveryOptimization || TelephonyCloudController.CLOUD_CONTROLLER_NETWORK_RECOVERY_DISABLED) {
            log("recoveryNetwork not support, supportNetworkRecovery = " + zIsSupportNetworkRecoveryOptimization);
        } else {
            this.mPreviousNetworkRecoveryTime = System.currentTimeMillis();
            if (Build.IS_INTERNATIONAL_BUILD) {
                reRegisterNetwork();
            } else {
                forceLte();
            }
        }
    }

    private void reRegisterNetwork() {
        MiuiPhoneUtils.hideOOS("network_recovery", this.mPhone.getPhoneId());
        this.mPhone.getServiceStateTracker().registerForNetworkAttached(this.mHandler, 2, (Object) null);
    }

    private void forceLte() {
        if (NetworkRecoveryStrategyBase.getInstance() != null) {
            NetworkRecoveryStrategyBase.getInstance().forceLte(this.mPhone.getPhoneId());
        }
        executeStatistics();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void executeStatistics() {
        log("executeStatistics");
        if (this.mHandler.hasMessages(5)) {
            return;
        }
        this.mHandler.sendEmptyMessageDelayed(5, 30000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordRecoveryNetworkEvent(String str, String str2) {
        recordRecoveryNetworkEvent(str, str2, 0L);
    }

    private void recordRecoveryNetworkEvent(String str, String str2, long j) {
        if (j != 0) {
            str2 = String.valueOf(j);
        }
        HashMap map = new HashMap();
        map.put("is_recovery", str);
        map.put("duration_after_recovery", str2);
        log("recordLowRatForceLteEvent params = " + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "low_rat_recovery", map);
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            if (i == NetworkRecoveryOptimize.this.mPhone.getPhoneId()) {
                NetworkRecoveryOptimize.this.mHandler.obtainMessage(1, serviceState).sendToTarget();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("NetworkRecoveryOptimize[" + this.mPhone.getPhoneId() + "]", str);
    }
}
