package com.xiaomi.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.UserHandle;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class MEModuleSignalT1s {
    private static final String ACTION_BT_ACL_CONNECTED = "android.bluetooth.device.action.ACL_CONNECTED";
    private static final String ACTION_BT_ACL_DISCONNECTED = "android.bluetooth.device.action.ACL_DISCONNECTED";
    private static final String ACTION_BT_STATE_CHANGED = "android.bluetooth.adapter.action.STATE_CHANGED";
    private static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final String ACTION_GPS_STATE_CHANGE = "com.android.phone.intent.action.gps_state_changed";
    private static final String ACTION_MODEMENHANCE_STATISTIC = "miui.intent.action.MODEMENHANCE_STATISTIC";
    private static final String ACTION_OFFLINE_CALL_STATE = "android.telephony.offlinecall.state";
    private static final String ACTION_RECEIVER_CALL_STATE = "com.android.phone.intent.action.receive_call_state";
    private static final String ACTION_SLEEP_CHANGED = "com.miui.powerkeeper_sleep_changed";
    private static final String ACTION_T1_STATE_CHANGED = "com.android.phone.intent.action.t1_state_changed";
    private static final String ACTION_WIFI_5G_BAND_INFO_CHANGED = "android.net.wifi.WIFI_5G_BAND_INFO_CHANGED";
    private static final long COEXIST_MODE_MAX_DURATION_MS = 86400000;
    private static final int DEFAULT_MODE = -1;
    private static final String EXTRA_CATEGORY_NAME = "category_name";
    private static final String EXTRA_EVENT_NAME = "event_name";
    private static final String EXTRA_LEVEL = "LEVEL";
    private static final String EXTRA_PARAM_NAME = "param_name";
    public static final String EXTRA_STATE = "state";
    private static final String KERNEL_T1_GPIO_CTRL_FILE_PATH = "/proc/modem_t1_gpio_ctrl";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_GPS_STATUS = "KEY_GPS_STATUS";
    public static final String KEY_ME_COEXIST_MODE_STATISTIC = "modemenhance_statistic_coexist_mode";
    private static final String KEY_MODE = "mode";
    private static final String KEY_TIMES = "times";
    private static final int MAX_COEXIST_MODE = 9;
    public static final String ME_STATISTIC_CATEGORY = "telephony_statistic";
    private static final int OPT_ENABLED = 1;
    private static final String PROPERTY_MODEM_ENHANCE_LOG = "persist.radio.me.modem_enhance_log";
    public static final int STATE_EXIT_SLEEP = 2;
    private static final int T1_OPT = 1;
    private static final String TAG = "MEModuleSignalT1s";
    private static final int VOLTE_CALL = 0;
    private static final int VONR_CALL = 1;
    private static final int WEAK_SIGNAL = 4;
    private static int mBtConnectNum = 0;
    public static long mCoexistModeTotalDuration = 0;
    private static MEModuleSignalT1s mInstance = null;
    private static boolean mSimCardInserted = false;
    public static int mcurrentCoexistMode = 255;
    public static long mlastCoexistModeChangeTime = 0;
    private static final int status_low_rsrp_call = 2;
    private static final int status_off = 0;
    private static final int status_on = 1;
    private Context mContext;
    private Map mSimCardStateMap = new ConcurrentHashMap();
    private int mIsFeatureEnable = 1;
    private int mReportedWeakSignal = 0;
    private String last_state = "000";
    Map inputParams = new HashMap();
    MEGestureModeConfig configT1 = null;
    private int mSleepState = 2;
    public ConcurrentHashMap coexistModeTimes = new ConcurrentHashMap();
    public ConcurrentHashMap coexistModeDurations = new ConcurrentHashMap();
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.xiaomi.internal.telephony.MEModuleSignalT1s.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                MELog.d(MEModuleSignalT1s.TAG, "mBroadcastReceiver, intent is null");
                return;
            }
            String action = intent.getAction();
            MELog.d(MEModuleSignalT1s.TAG, "BroadcastReceiver action: " + action);
            action.hashCode();
            switch (action) {
                case "android.bluetooth.adapter.action.STATE_CHANGED":
                    int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                    if (intExtra != 12) {
                        if (intExtra == 10) {
                            MEModuleSignalT1s.this.inputParams.put("bt", 0);
                            MEModuleSignalT1s.mBtConnectNum = 0;
                            MELog.d(MEModuleSignalT1s.TAG, "BT off");
                        }
                        break;
                    } else {
                        MELog.d(MEModuleSignalT1s.TAG, "BT on");
                        break;
                    }
                    break;
                case "com.android.phone.intent.action.receive_call_state":
                    int intExtra2 = intent.getIntExtra("call_type", -1);
                    int intExtra3 = intent.getIntExtra("opt_type", -1);
                    int intExtra4 = intent.getIntExtra("opt_enabled", -1);
                    if (intExtra2 == 4 && intExtra3 == 1) {
                        MEModuleSignalT1s.this.mReportedWeakSignal = intExtra4 == 1 ? 1 : 0;
                    }
                    MEModuleSignalT1s mEModuleSignalT1s = MEModuleSignalT1s.this;
                    mEModuleSignalT1s.inputParams.put("sigweak", Integer.valueOf(mEModuleSignalT1s.getEffectiveSigWeak()));
                    break;
                case "android.net.conn.CONNECTIVITY_CHANGE":
                    NetworkInfo activeNetworkInfo = ((ConnectivityManager) MEModuleSignalT1s.this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
                    if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
                        MELog.d(MEModuleSignalT1s.TAG, "Network is not available!");
                        MEModuleSignalT1s.this.inputParams.put("wifi", 0);
                        break;
                    } else {
                        MELog.d(MEModuleSignalT1s.TAG, "networkInfo.getTypeName(): " + activeNetworkInfo.getTypeName());
                        MEModuleSignalT1s.this.inputParams.put("wifi", Integer.valueOf(activeNetworkInfo.getType() == 1 ? 1 : 0));
                        break;
                    }
                    break;
                case "android.bluetooth.device.action.ACL_CONNECTED":
                    MEModuleSignalT1s.mBtConnectNum++;
                    MELog.d(MEModuleSignalT1s.TAG, "bt connect device num = " + MEModuleSignalT1s.mBtConnectNum);
                    if (MEModuleSignalT1s.mBtConnectNum > 0) {
                        MEModuleSignalT1s.this.inputParams.put("bt", 1);
                        break;
                    }
                    break;
                case "android.intent.action.SIM_STATE_CHANGED":
                    int intExtra5 = intent.getIntExtra("phone", -1);
                    String stringExtra = intent.getStringExtra("ss");
                    MELog.d(MEModuleSignalT1s.TAG, "phoneId = " + intExtra5 + " sim state = " + stringExtra);
                    if (intExtra5 != -1) {
                        if ("LOADED".equals(stringExtra)) {
                            MEModuleSignalT1s.this.mSimCardStateMap.put(Integer.valueOf(intExtra5), Boolean.TRUE);
                        } else if ("ABSENT".equals(stringExtra)) {
                            MEModuleSignalT1s.this.mSimCardStateMap.put(Integer.valueOf(intExtra5), Boolean.FALSE);
                        }
                        MEModuleSignalT1s.mSimCardInserted = MEModuleSignalT1s.this.mSimCardStateMap.values().stream().anyMatch(new Predicate() { // from class: com.xiaomi.internal.telephony.MEModuleSignalT1s$1$$ExternalSyntheticLambda0
                            @Override // java.util.function.Predicate
                            public final boolean test(Object obj) {
                                return ((Boolean) obj).booleanValue();
                            }
                        });
                        MEModuleSignalT1s mEModuleSignalT1s2 = MEModuleSignalT1s.this;
                        mEModuleSignalT1s2.inputParams.put("sigweak", Integer.valueOf(mEModuleSignalT1s2.getEffectiveSigWeak()));
                        break;
                    }
                    break;
                case "com.miui.powerkeeper_sleep_changed":
                    int intExtra6 = intent.getIntExtra(MEModuleSignalT1s.EXTRA_STATE, 2);
                    if (intExtra6 != MEModuleSignalT1s.this.mSleepState) {
                        MEModuleSignalT1s.this.mSleepState = intExtra6;
                        if (intExtra6 == 2) {
                            new Handler().postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSignalT1s.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    MEModuleSignalT1s.this.reportCoexistModeStatistic();
                                }
                            }, RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
                        }
                        break;
                    }
                    break;
                case "android.telephony.offlinecall.state":
                    MEModuleSignalT1s.this.inputParams.put("offlinecall", Integer.valueOf(intent.getIntExtra(MEModuleSignalT1s.EXTRA_STATE, -1)));
                    break;
                case "com.android.phone.intent.action.t1_state_changed":
                    MEModuleSignalT1s.this.mIsFeatureEnable = intent.getIntExtra(MEModuleSignalT1s.EXTRA_STATE, -1) == 1 ? 1 : 0;
                    if (MEModuleSignalT1s.this.mIsFeatureEnable == 0) {
                        MELog.d(MEModuleSignalT1s.TAG, "mIsFeatureEnable = False, set default mode.");
                        MEModuleSignalT1s mEModuleSignalT1s3 = MEModuleSignalT1s.this;
                        mEModuleSignalT1s3.setGPIO(mEModuleSignalT1s3.configT1.getGpioSettings(-1));
                        break;
                    } else {
                        if (MEModuleSignalT1s.this.mIsFeatureEnable == 1) {
                            MELog.d(MEModuleSignalT1s.TAG, "mIsFeatureEnable = True!");
                            MEModuleSignalT1s mEModuleSignalT1s4 = MEModuleSignalT1s.this;
                            mEModuleSignalT1s4.updateBroadCastOutMode(mEModuleSignalT1s4.configT1.getOutMode(mEModuleSignalT1s4.inputParams).intValue());
                        }
                        break;
                    }
                    break;
                case "android.net.wifi.WIFI_5G_BAND_INFO_CHANGED":
                    MEModuleSignalT1s.this.inputParams.put("wififrequency", Integer.valueOf(intent.getIntExtra(MEModuleSignalT1s.EXTRA_LEVEL, -1)));
                    break;
                case "android.bluetooth.device.action.ACL_DISCONNECTED":
                    if (MEModuleSignalT1s.mBtConnectNum != 0) {
                        MEModuleSignalT1s.mBtConnectNum--;
                    }
                    MELog.d(MEModuleSignalT1s.TAG, "bt connect device num = " + MEModuleSignalT1s.mBtConnectNum);
                    if (MEModuleSignalT1s.mBtConnectNum == 0) {
                        MEModuleSignalT1s.this.inputParams.put("bt", 0);
                        break;
                    }
                    break;
                case "com.android.phone.intent.action.gps_state_changed":
                    MEModuleSignalT1s.this.inputParams.put("gps", Integer.valueOf("true".equals(intent.getStringExtra(MEModuleSignalT1s.KEY_GPS_STATUS)) ? 1 : 0));
                    break;
            }
            if (MEModuleSignalT1s.this.mIsFeatureEnable != 1 || MEModuleSignalT1s.ACTION_SLEEP_CHANGED.equals(action)) {
                return;
            }
            for (Map.Entry entry : MEModuleSignalT1s.this.inputParams.entrySet()) {
                MELog.i(MEModuleSignalT1s.TAG, "Key: " + ((String) entry.getKey()) + ", Value: " + entry.getValue());
            }
            MEModuleSignalT1s mEModuleSignalT1s5 = MEModuleSignalT1s.this;
            mEModuleSignalT1s5.updateBroadCastOutMode(mEModuleSignalT1s5.configT1.getOutMode(mEModuleSignalT1s5.inputParams).intValue());
        }
    };

    private MEModuleSignalT1s(Context context) {
        MELog.d(TAG, "MEModuleSignalT1s init...");
        this.mContext = context;
        if (initT1Config()) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_CONNECTIVITY_CHANGE);
            intentFilter.addAction(ACTION_RECEIVER_CALL_STATE);
            intentFilter.addAction(ACTION_GPS_STATE_CHANGE);
            intentFilter.addAction(ACTION_T1_STATE_CHANGED);
            intentFilter.addAction(ACTION_BT_STATE_CHANGED);
            intentFilter.addAction(ACTION_BT_ACL_CONNECTED);
            intentFilter.addAction(ACTION_BT_ACL_DISCONNECTED);
            intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
            intentFilter.addAction(ACTION_SLEEP_CHANGED);
            intentFilter.addAction(ACTION_WIFI_5G_BAND_INFO_CHANGED);
            intentFilter.addAction(ACTION_OFFLINE_CALL_STATE);
            this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
            MELog.d(TAG, "MEModuleSignalT1s init done");
        }
    }

    public static MEModuleSignalT1s getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MEModuleSignalT1s(context);
        }
        return mInstance;
    }

    private boolean initT1Config() {
        MEGestureModeConfig mEGestureModeConfig = new MEGestureModeConfig();
        this.configT1 = mEGestureModeConfig;
        if (MEGestureModeConfig.initialize("/odm/etc/me_config.xml", mEGestureModeConfig)) {
            this.inputParams.put("bt", 0);
            this.inputParams.put("wifi", 0);
            this.inputParams.put("sigweak", 0);
            this.inputParams.put("gps", 0);
            this.inputParams.put("wififrequency", -1);
            this.inputParams.put("offlinecall", -1);
            return true;
        }
        MELog.d(TAG, "initT1Config failed");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBroadCastOutMode(int i) {
        MELog.i(TAG, "updateBroadCastOutMode: " + i);
        setGPIO(this.configT1.getGpioSettings(i));
        calculateCoexistModeDurationAndTime(i);
    }

    private void calculateCoexistModeDurationAndTime(int i) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        int i2 = mcurrentCoexistMode;
        if (i2 != 255 && i2 != i) {
            long j = jCurrentTimeMillis - mlastCoexistModeChangeTime;
            long j2 = mCoexistModeTotalDuration + j;
            mCoexistModeTotalDuration = j2;
            if (j2 >= 86400000) {
                MELog.d(TAG, "Total coexist mode duration has exceeded 24 hours.");
                return;
            } else {
                this.coexistModeDurations.put(Integer.valueOf(i2), Long.valueOf(((Long) this.coexistModeDurations.getOrDefault(Integer.valueOf(mcurrentCoexistMode), 0L)).longValue() + j));
                this.coexistModeTimes.put(Integer.valueOf(mcurrentCoexistMode), Long.valueOf(((Long) this.coexistModeTimes.getOrDefault(Integer.valueOf(mcurrentCoexistMode), 0L)).longValue() + 1));
            }
        }
        mcurrentCoexistMode = i;
        mlastCoexistModeChangeTime = jCurrentTimeMillis;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportCoexistModeStatistic() {
        if (this.coexistModeDurations.size() == 0 || this.coexistModeTimes.size() == 0) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (Map.Entry entry : this.coexistModeDurations.entrySet()) {
            Integer num = (Integer) entry.getKey();
            num.intValue();
            if (this.coexistModeTimes.containsKey(num)) {
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                Long l = (Long) entry.getValue();
                l.longValue();
                ((Long) this.coexistModeTimes.get(num)).getClass();
                concurrentHashMap.put(KEY_MODE, num);
                concurrentHashMap.put("duration", l);
                concurrentHashMap.put(KEY_TIMES, this.coexistModeTimes.get(num));
                arrayList.add(concurrentHashMap);
            }
        }
        MELog.d(TAG, "mCoexistModeStatisticList" + arrayList);
        Intent intent = new Intent(ACTION_MODEMENHANCE_STATISTIC);
        intent.putExtra(EXTRA_CATEGORY_NAME, ME_STATISTIC_CATEGORY);
        intent.putExtra("event_name", KEY_ME_COEXIST_MODE_STATISTIC);
        intent.putExtra(EXTRA_PARAM_NAME, arrayList);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.CURRENT);
        mCoexistModeTotalDuration = 0L;
        this.coexistModeDurations.clear();
        this.coexistModeTimes.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getEffectiveSigWeak() {
        if (mSimCardInserted) {
            return this.mReportedWeakSignal;
        }
        return 0;
    }

    public void setGPIO(String str) {
        if (str == null) {
            MELog.d(TAG, "setGPIO skipped: gpioState is null");
            return;
        }
        if (this.last_state.equals(str)) {
            MELog.d(TAG, "skip setGPIO as same GPIO state!");
            return;
        }
        File file = new File(KERNEL_T1_GPIO_CTRL_FILE_PATH);
        if (!file.exists()) {
            MELog.d(TAG, "setGPIO failed as proc node does not exist!");
            return;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            try {
                fileOutputStream.write(str.getBytes());
                this.last_state = str;
                fileOutputStream.close();
            } catch (Throwable th) {
                try {
                    fileOutputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MELog.e(TAG, "setGPIO errors");
        }
        MELog.d(TAG, "SetGPIO gpio string = " + this.last_state);
    }
}
