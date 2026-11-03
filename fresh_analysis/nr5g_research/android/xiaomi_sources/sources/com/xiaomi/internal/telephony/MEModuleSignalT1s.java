package com.xiaomi.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.UserHandle;
import com.xiaomi.modem.ModemUtils;
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
    private static final int status_low_rsrp_call = 2;
    private static final int status_off = 0;
    private static final int status_on = 1;
    private Context mContext;
    private static MEModuleSignalT1s mInstance = null;
    private static int mBtConnectNum = 0;
    private static boolean mSimCardInserted = false;
    public static int mcurrentCoexistMode = ModemUtils.MODEM_STATS_DATA_INVALID_3;
    public static long mlastCoexistModeChangeTime = 0;
    public static long mCoexistModeTotalDuration = 0;
    private Map<Integer, Boolean> mSimCardStateMap = new ConcurrentHashMap();
    private int mIsFeatureEnable = 1;
    private int mReportedWeakSignal = 0;
    private String last_state = "000";
    Map<String, Integer> inputParams = new HashMap();
    MEGestureModeConfig configT1 = null;
    private int mSleepState = 2;
    public ConcurrentHashMap<Integer, Long> coexistModeTimes = new ConcurrentHashMap<>();
    public ConcurrentHashMap<Integer, Long> coexistModeDurations = new ConcurrentHashMap<>();
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.xiaomi.internal.telephony.MEModuleSignalT1s.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            byte b;
            if (intent == null) {
                MELog.d(MEModuleSignalT1s.TAG, "mBroadcastReceiver, intent is null");
                return;
            }
            String action = intent.getAction();
            MELog.d(MEModuleSignalT1s.TAG, "BroadcastReceiver action: " + action);
            switch (action.hashCode()) {
                case -1530327060:
                    b = action.equals(MEModuleSignalT1s.ACTION_BT_STATE_CHANGED) ? (byte) 4 : (byte) -1;
                    break;
                case -1237599970:
                    b = action.equals(MEModuleSignalT1s.ACTION_RECEIVER_CALL_STATE) ? (byte) 2 : (byte) -1;
                    break;
                case -1172645946:
                    b = action.equals(MEModuleSignalT1s.ACTION_CONNECTIVITY_CHANGE) ? (byte) 0 : (byte) -1;
                    break;
                case -301431627:
                    b = action.equals(MEModuleSignalT1s.ACTION_BT_ACL_CONNECTED) ? (byte) 5 : (byte) -1;
                    break;
                case -229777127:
                    b = action.equals("android.intent.action.SIM_STATE_CHANGED") ? (byte) 1 : (byte) -1;
                    break;
                case 333968915:
                    b = action.equals("com.miui.powerkeeper_sleep_changed") ? (byte) 10 : (byte) -1;
                    break;
                case 1085724623:
                    b = action.equals(MEModuleSignalT1s.ACTION_OFFLINE_CALL_STATE) ? (byte) 8 : (byte) -1;
                    break;
                case 1232959958:
                    b = action.equals(MEModuleSignalT1s.ACTION_T1_STATE_CHANGED) ? (byte) 9 : (byte) -1;
                    break;
                case 1493787937:
                    b = action.equals(MEModuleSignalT1s.ACTION_WIFI_5G_BAND_INFO_CHANGED) ? (byte) 7 : (byte) -1;
                    break;
                case 1821585647:
                    b = action.equals(MEModuleSignalT1s.ACTION_BT_ACL_DISCONNECTED) ? (byte) 6 : (byte) -1;
                    break;
                case 1948052767:
                    b = action.equals(MEModuleSignalT1s.ACTION_GPS_STATE_CHANGE) ? (byte) 3 : (byte) -1;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    NetworkInfo activeNetworkInfo = ((ConnectivityManager) MEModuleSignalT1s.this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
                        MELog.d(MEModuleSignalT1s.TAG, "networkInfo.getTypeName(): " + activeNetworkInfo.getTypeName());
                        MEModuleSignalT1s.this.inputParams.put("wifi", Integer.valueOf(activeNetworkInfo.getType() == 1 ? 1 : 0));
                    } else {
                        MELog.d(MEModuleSignalT1s.TAG, "Network is not available!");
                        MEModuleSignalT1s.this.inputParams.put("wifi", 0);
                    }
                    break;
                case 1:
                    int intExtra = intent.getIntExtra("phone", -1);
                    String stringExtra = intent.getStringExtra("ss");
                    MELog.d(MEModuleSignalT1s.TAG, "phoneId = " + intExtra + " sim state = " + stringExtra);
                    if (intExtra != -1) {
                        if ("LOADED".equals(stringExtra)) {
                            MEModuleSignalT1s.this.mSimCardStateMap.put(Integer.valueOf(intExtra), true);
                        } else if ("ABSENT".equals(stringExtra)) {
                            MEModuleSignalT1s.this.mSimCardStateMap.put(Integer.valueOf(intExtra), false);
                        }
                        MEModuleSignalT1s.mSimCardInserted = MEModuleSignalT1s.this.mSimCardStateMap.values().stream().anyMatch(new Predicate() { // from class: com.xiaomi.internal.telephony.MEModuleSignalT1s$1$$ExternalSyntheticLambda0
                            @Override // java.util.function.Predicate
                            public final boolean test(Object obj) {
                                return ((Boolean) obj).booleanValue();
                            }
                        });
                        MEModuleSignalT1s.this.inputParams.put("sigweak", Integer.valueOf(MEModuleSignalT1s.this.getEffectiveSigWeak()));
                    }
                    break;
                case 2:
                    int intExtra2 = intent.getIntExtra("call_type", -1);
                    int intExtra3 = intent.getIntExtra("opt_type", -1);
                    int intExtra4 = intent.getIntExtra("opt_enabled", -1);
                    if (intExtra2 == 4 && intExtra3 == 1) {
                        MEModuleSignalT1s.this.mReportedWeakSignal = intExtra4 == 1 ? 1 : 0;
                    }
                    MEModuleSignalT1s.this.inputParams.put("sigweak", Integer.valueOf(MEModuleSignalT1s.this.getEffectiveSigWeak()));
                    break;
                case 3:
                    MEModuleSignalT1s.this.inputParams.put("gps", Integer.valueOf("true".equals(intent.getStringExtra(MEModuleSignalT1s.KEY_GPS_STATUS)) ? 1 : 0));
                    break;
                case 4:
                    int intExtra5 = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                    if (intExtra5 == 12) {
                        MELog.d(MEModuleSignalT1s.TAG, "BT on");
                    } else if (intExtra5 == 10) {
                        MEModuleSignalT1s.this.inputParams.put("bt", 0);
                        MEModuleSignalT1s.mBtConnectNum = 0;
                        MELog.d(MEModuleSignalT1s.TAG, "BT off");
                    }
                    break;
                case 5:
                    MEModuleSignalT1s.mBtConnectNum++;
                    MELog.d(MEModuleSignalT1s.TAG, "bt connect device num = " + MEModuleSignalT1s.mBtConnectNum);
                    if (MEModuleSignalT1s.mBtConnectNum > 0) {
                        MEModuleSignalT1s.this.inputParams.put("bt", 1);
                    }
                    break;
                case 6:
                    if (MEModuleSignalT1s.mBtConnectNum != 0) {
                        MEModuleSignalT1s.mBtConnectNum--;
                    }
                    MELog.d(MEModuleSignalT1s.TAG, "bt connect device num = " + MEModuleSignalT1s.mBtConnectNum);
                    if (MEModuleSignalT1s.mBtConnectNum == 0) {
                        MEModuleSignalT1s.this.inputParams.put("bt", 0);
                    }
                    break;
                case 7:
                    MEModuleSignalT1s.this.inputParams.put("wififrequency", Integer.valueOf(intent.getIntExtra(MEModuleSignalT1s.EXTRA_LEVEL, -1)));
                    break;
                case 8:
                    MEModuleSignalT1s.this.inputParams.put("offlinecall", Integer.valueOf(intent.getIntExtra("state", -1)));
                    break;
                case 9:
                    MEModuleSignalT1s.this.mIsFeatureEnable = intent.getIntExtra("state", -1) == 1 ? 1 : 0;
                    if (MEModuleSignalT1s.this.mIsFeatureEnable == 0) {
                        MELog.d(MEModuleSignalT1s.TAG, "mIsFeatureEnable = False, set default mode.");
                        MEModuleSignalT1s.this.setGPIO(MEModuleSignalT1s.this.configT1.getGpioSettings(-1));
                    } else if (MEModuleSignalT1s.this.mIsFeatureEnable == 1) {
                        MELog.d(MEModuleSignalT1s.TAG, "mIsFeatureEnable = True!");
                        MEModuleSignalT1s.this.updateBroadCastOutMode(MEModuleSignalT1s.this.configT1.getOutMode(MEModuleSignalT1s.this.inputParams).intValue());
                    }
                    break;
                case 10:
                    int intExtra6 = intent.getIntExtra("state", 2);
                    if (intExtra6 != MEModuleSignalT1s.this.mSleepState) {
                        MEModuleSignalT1s.this.mSleepState = intExtra6;
                        if (intExtra6 == 2) {
                            new Handler().postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.MEModuleSignalT1s.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    MEModuleSignalT1s.this.reportCoexistModeStatistic();
                                }
                            }, 60000L);
                        }
                    }
                    break;
            }
            if (MEModuleSignalT1s.this.mIsFeatureEnable == 1 && !"com.miui.powerkeeper_sleep_changed".equals(action)) {
                for (Map.Entry<String, Integer> entry : MEModuleSignalT1s.this.inputParams.entrySet()) {
                    MELog.i(MEModuleSignalT1s.TAG, "Key: " + entry.getKey() + ", Value: " + entry.getValue());
                }
                MEModuleSignalT1s.this.updateBroadCastOutMode(MEModuleSignalT1s.this.configT1.getOutMode(MEModuleSignalT1s.this.inputParams).intValue());
            }
        }
    };

    private MEModuleSignalT1s(Context context) {
        MELog.d(TAG, "MEModuleSignalT1s init...");
        this.mContext = context;
        if (initT1Config()) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_CONNECTIVITY_CHANGE);
            filter.addAction(ACTION_RECEIVER_CALL_STATE);
            filter.addAction(ACTION_GPS_STATE_CHANGE);
            filter.addAction(ACTION_T1_STATE_CHANGED);
            filter.addAction(ACTION_BT_STATE_CHANGED);
            filter.addAction(ACTION_BT_ACL_CONNECTED);
            filter.addAction(ACTION_BT_ACL_DISCONNECTED);
            filter.addAction("android.intent.action.SIM_STATE_CHANGED");
            filter.addAction("com.miui.powerkeeper_sleep_changed");
            filter.addAction(ACTION_WIFI_5G_BAND_INFO_CHANGED);
            filter.addAction(ACTION_OFFLINE_CALL_STATE);
            this.mContext.registerReceiver(this.mBroadcastReceiver, filter, 2);
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
        this.configT1 = new MEGestureModeConfig();
        if (MEGestureModeConfig.initialize("/odm/etc/me_config.xml", this.configT1)) {
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
    public void updateBroadCastOutMode(int outMode) {
        MELog.i(TAG, "updateBroadCastOutMode: " + outMode);
        setGPIO(this.configT1.getGpioSettings(outMode));
        calculateCoexistModeDurationAndTime(outMode);
    }

    private void calculateCoexistModeDurationAndTime(int newMode) {
        long currentTime = System.currentTimeMillis();
        if (mcurrentCoexistMode != 255 && mcurrentCoexistMode != newMode) {
            long duration = currentTime - mlastCoexistModeChangeTime;
            mCoexistModeTotalDuration += duration;
            if (mCoexistModeTotalDuration >= 86400000) {
                MELog.d(TAG, "Total coexist mode duration has exceeded 24 hours.");
                return;
            } else {
                this.coexistModeDurations.put(Integer.valueOf(mcurrentCoexistMode), Long.valueOf(this.coexistModeDurations.getOrDefault(Integer.valueOf(mcurrentCoexistMode), 0L).longValue() + duration));
                this.coexistModeTimes.put(Integer.valueOf(mcurrentCoexistMode), Long.valueOf(this.coexistModeTimes.getOrDefault(Integer.valueOf(mcurrentCoexistMode), 0L).longValue() + 1));
            }
        }
        mcurrentCoexistMode = newMode;
        mlastCoexistModeChangeTime = currentTime;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportCoexistModeStatistic() {
        if (this.coexistModeDurations.size() == 0 || this.coexistModeTimes.size() == 0) {
            return;
        }
        ArrayList<ConcurrentHashMap<String, Object>> mCoexistModeStatisticList = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : this.coexistModeDurations.entrySet()) {
            int mode = entry.getKey().intValue();
            if (this.coexistModeTimes.containsKey(Integer.valueOf(mode))) {
                ConcurrentHashMap<String, Object> coexistModeStatistic = new ConcurrentHashMap<>();
                long duration = entry.getValue().longValue();
                this.coexistModeTimes.get(Integer.valueOf(mode)).longValue();
                coexistModeStatistic.put(KEY_MODE, Integer.valueOf(mode));
                coexistModeStatistic.put(KEY_DURATION, Long.valueOf(duration));
                coexistModeStatistic.put(KEY_TIMES, this.coexistModeTimes.get(Integer.valueOf(mode)));
                mCoexistModeStatisticList.add(coexistModeStatistic);
            }
        }
        MELog.d(TAG, "mCoexistModeStatisticList" + mCoexistModeStatisticList);
        Intent intent = new Intent(ACTION_MODEMENHANCE_STATISTIC);
        intent.putExtra(EXTRA_CATEGORY_NAME, ME_STATISTIC_CATEGORY);
        intent.putExtra(EXTRA_EVENT_NAME, KEY_ME_COEXIST_MODE_STATISTIC);
        intent.putExtra(EXTRA_PARAM_NAME, mCoexistModeStatisticList);
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

    public void setGPIO(String gpioState) {
        if (gpioState == null) {
            MELog.d(TAG, "setGPIO skipped: gpioState is null");
            return;
        }
        if (this.last_state.equals(gpioState)) {
            MELog.d(TAG, "skip setGPIO as same GPIO state!");
            return;
        }
        File gpiof = new File(KERNEL_T1_GPIO_CTRL_FILE_PATH);
        if (!gpiof.exists()) {
            MELog.d(TAG, "setGPIO failed as proc node does not exist!");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(gpiof);
            try {
                fos.write(gpioState.getBytes());
                this.last_state = gpioState;
                fos.close();
            } catch (Throwable th) {
                try {
                    fos.close();
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
