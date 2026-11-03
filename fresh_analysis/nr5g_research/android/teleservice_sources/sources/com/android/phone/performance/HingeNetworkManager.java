package com.android.phone.performance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.LocalLog;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.PhoneGlobals;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import miui.telephony.DefaultSimManager;

/* JADX INFO: loaded from: classes.dex */
public class HingeNetworkManager {
    private static boolean DBG = true;
    private static String LOG_TAG = "HingeNetworkManager";
    private static boolean VDBG = false;
    private static HingeNetworkManager sInstance;
    private Context mContext;
    ContentObserver mDataEnabledObserver;
    private HingeNetworkHandler mHandler;
    private WifiToDataSelfStudyDBHelper mWifiToDataSelfStudyDBHelper;
    private HandlerThread mWorkThread;
    private static final HashMap sLocalLogs = new HashMap();
    private static long DELAY_NETWORK_MILLISECOND = 10000;
    private static long PENALTY_TIMER = 259200000;
    private boolean mIsMobileConnected = false;
    private boolean mIsWifiConnected = false;
    private boolean mIsAirPlaneMode = false;
    private boolean mUserDataEnabled = false;
    private boolean mIsTriggerWifiErrorRecovery = false;
    private String mLastWifiSsid = "";
    private long mWifiDisconnectedTime = 0;
    private boolean mEnabledDualLink = false;
    private long mLastTime = 0;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.performance.HingeNetworkManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                HingeNetworkManager.this.mHandler.obtainMessage(4, intent).sendToTarget();
                return;
            }
            if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                HingeNetworkManager.this.mHandler.obtainMessage(5, intent).sendToTarget();
                return;
            }
            if (action.equals("com.android.phone.intent.action.CHANGE_LEVEL")) {
                HingeNetworkManager.this.mHandler.obtainMessage(1, intent).sendToTarget();
            } else if (action.equals("com.android.phone.intent.action.ARREARS_SIM_NOTIFICATION")) {
                HingeNetworkManager.this.mHandler.obtainMessage(3, intent).sendToTarget();
            } else if (action.equals("HingeNetworkManager.intent.debugall")) {
                HingeNetworkManager.VDBG = true;
            }
        }
    };

    enum Scene {
        DUMMY(0),
        GAME(1),
        ROAMING(2),
        WIFI_ERROR_SSID(3);

        private int value;

        Scene(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static synchronized HingeNetworkManager getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new HingeNetworkManager();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private HingeNetworkManager() {
        this.mDataEnabledObserver = new ContentObserver(this.mHandler) { // from class: com.android.phone.performance.HingeNetworkManager.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                boolean zIsMobileDataSettingOn = HingeNetworkManager.this.isMobileDataSettingOn();
                if (HingeNetworkManager.this.mUserDataEnabled != zIsMobileDataSettingOn) {
                    HingeNetworkManager.this.mUserDataEnabled = zIsMobileDataSettingOn;
                }
            }
        };
        if (DBG) {
            log("HingeNetworkManager: init");
        }
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread(LOG_TAG, 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        HingeNetworkHandler hingeNetworkHandler = new HingeNetworkHandler(this.mWorkThread.getLooper());
        this.mHandler = hingeNetworkHandler;
        hingeNetworkHandler.obtainMessage(0).sendToTarget();
    }

    class HingeNetworkHandler extends Handler {
        public HingeNetworkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    HingeNetworkManager.this.initAction();
                    break;
                case 1:
                    if (HingeNetworkManager.DBG) {
                        HingeNetworkManager.log("EVENT_SET_LEVEL");
                    }
                    Intent intent = (Intent) message.obj;
                    long longExtra = intent.getLongExtra("Level_UL", 1L);
                    long longExtra2 = intent.getLongExtra("Level_DL", 1L);
                    if (longExtra != 1 && longExtra2 != 1) {
                        HingeNetworkManager hingeNetworkManager = HingeNetworkManager.this;
                        hingeNetworkManager.enableDualLink(hingeNetworkManager.getDefaultDataSlotId(), Scene.GAME);
                        break;
                    } else if (longExtra == 1 && longExtra2 == 1 && !HingeNetworkManager.this.mIsTriggerWifiErrorRecovery) {
                        HingeNetworkManager.this.disableDualLink(Scene.GAME);
                        break;
                    }
                    break;
                case 2:
                    if (HingeNetworkManager.DBG) {
                        HingeNetworkManager.log("EVENT_DATA_CONNECTION_ROAM_ON");
                    }
                    for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
                        if (HingeNetworkManager.this.isRoamingStatus(i)) {
                            HingeNetworkManager.this.disableDualLink(Scene.ROAMING);
                        }
                        break;
                    }
                    break;
                case 3:
                    if (HingeNetworkManager.DBG) {
                        HingeNetworkManager.log("EVENT_ARREARS_SIM_NOTIFICATION");
                    }
                    if (!HingeNetworkManager.this.isLingerTime()) {
                        MiuiPhoneUtils.showArrearsSimDialog(((Intent) message.obj).getIntExtra("slotId", 0));
                    }
                    break;
                case 4:
                    HingeNetworkManager.this.processNetworkStateChanged((Intent) message.obj);
                    break;
                case 5:
                    HingeNetworkManager hingeNetworkManager2 = HingeNetworkManager.this;
                    hingeNetworkManager2.mIsAirPlaneMode = hingeNetworkManager2.isAirPlaneModeOn();
                    break;
                case 6:
                    if (!HingeNetworkManager.this.mIsWifiConnected) {
                        HingeNetworkManager.this.processDetectMobileData();
                    }
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            MiuiPhoneUtils.getPhone(i).getServiceStateTracker().registerForDataRoamingOn(this.mHandler, 2, (Object) null);
        }
        this.mIsAirPlaneMode = isAirPlaneModeOn();
        this.mUserDataEnabled = isMobileDataSettingOn();
        this.mWifiToDataSelfStudyDBHelper = WifiToDataSelfStudyDBHelper.getInstance(this.mContext);
        disableDualLink(Scene.DUMMY);
        registerAllEvents();
    }

    private void registerAllEvents() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("com.android.phone.intent.action.CHANGE_LEVEL");
        intentFilter.addAction("com.android.phone.intent.action.ARREARS_SIM_NOTIFICATION");
        intentFilter.addAction("HingeNetworkManager.intent.debugall");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor(getMobileDataSettingName()), false, this.mDataEnabledObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean enableDualLink(int i, Scene scene) {
        if (DBG) {
            log("enableDualLink alwaysOn=" + isMobileDataAlwaysOn() + ",isRoaming=" + isRoamingStatus(i) + ",isDualLinkEnabled=" + isDualLinkEnabled() + "Scene=" + scene.getValue());
        }
        if (!isValidatePhoneId(i) || isDualLinkEnabled() || isMobileDataAlwaysOn() || isRoamingStatus(i) || !this.mUserDataEnabled) {
            return false;
        }
        log("enableDualLink enter");
        setMobileDataAlwaysOn(true, scene);
        setDualLinkFlag(1);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean disableDualLink(Scene scene) {
        try {
            if (DBG) {
                log("disableDualLink alwaysOn=" + isMobileDataAlwaysOn() + ",isDualLinkEnabled=" + isDualLinkEnabled() + "Scene=" + scene.getValue());
            }
            if (isDualLinkEnabled()) {
                setDualLinkFlag(0);
                if (isMobileDataAlwaysOn() && !isLinkTurboSetting()) {
                    log("disableDualLink enter");
                    setMobileDataAlwaysOn(false, scene);
                }
            }
        } catch (Throwable th) {
            throw th;
        }
        return true;
    }

    private boolean isMobileDataAlwaysOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "mobile_data_always_on", 0) == 1;
    }

    private void setMobileDataAlwaysOn(boolean z, Scene scene) {
        localLog("setMobileDataAlwaysOn", ", isEnabled= " + z + ", sceneType = " + scene.getValue());
        recordMobileDataAlwaysOn(z, scene);
        Settings.Global.putInt(this.mContext.getContentResolver(), "mobile_data_always_on", z ? 1 : 0);
    }

    private void recordMobileDataAlwaysOn(boolean z, Scene scene) {
        HashMap map = new HashMap();
        map.put("UpdateDataAlwaysEnable", String.valueOf(z));
        map.put("UpdateDataAlwayScene", Integer.valueOf(scene.getValue()));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_mobile_data_always_on", map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDefaultDataSlotId() {
        return DefaultSimManager.getDefaultDataSlotId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRoamingStatus(int i) {
        if (isValidatePhoneId(i)) {
            return MiuiPhoneUtils.getPhone(i).getServiceState().getDataRoaming();
        }
        return false;
    }

    private boolean isValidatePhoneId(int i) {
        return MiuiPhoneUtils.isValidatePhoneId(i);
    }

    private boolean isDualLinkEnabled() {
        return PreferenceUtils.getInstance().getLastMobileDataAlwaysOn() == 1;
    }

    private void setDualLinkFlag(int i) {
        PreferenceUtils.getInstance().setLastMobileDataAlwaysOn(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isLingerTime() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long j = this.mLastTime;
        if (j != 0 && jCurrentTimeMillis < j + PENALTY_TIMER) {
            log("do NOT popup arrears sim during lingering time");
            return true;
        }
        this.mLastTime = jCurrentTimeMillis;
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNetworkStateChanged(Intent intent) {
        NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        if (networkInfo == null) {
            return;
        }
        NetworkInfo.State state = networkInfo.getState();
        NetworkInfo.State state2 = NetworkInfo.State.CONNECTED;
        if (state == state2 || state == NetworkInfo.State.DISCONNECTED) {
            int type = networkInfo.getType();
            boolean z = state == state2;
            log("networkType=" + type + ",isConnected=" + z);
            if (type == 0) {
                if (this.mIsMobileConnected == z) {
                    return;
                }
                this.mIsMobileConnected = z;
                if (!z || this.mWifiDisconnectedTime == 0) {
                    return;
                }
                long jElapsedRealtime = SystemClock.elapsedRealtime() - this.mWifiDisconnectedTime;
                if (jElapsedRealtime > 0) {
                    if (this.mWifiToDataSelfStudyDBHelper.queryRecord(this.mLastWifiSsid)) {
                        this.mEnabledDualLink = true;
                    }
                    recordWifiDisconnectToDataConnect(jElapsedRealtime);
                    return;
                }
                return;
            }
            if (type != 1 || this.mIsWifiConnected == z) {
                return;
            }
            this.mIsWifiConnected = z;
            if (z) {
                String currentWifiSsid = getCurrentWifiSsid(this.mContext);
                if (TextUtils.isEmpty(currentWifiSsid)) {
                    return;
                }
                this.mLastWifiSsid = currentWifiSsid;
                if (this.mWifiToDataSelfStudyDBHelper.queryRecord(currentWifiSsid)) {
                    this.mIsTriggerWifiErrorRecovery = true;
                    enableDualLink(getDefaultDataSlotId(), Scene.WIFI_ERROR_SSID);
                    return;
                }
                return;
            }
            if (isNecessaryCase() && ModemProvider.IS_QCOM) {
                this.mWifiDisconnectedTime = SystemClock.elapsedRealtime();
                this.mHandler.removeMessages(6);
                HingeNetworkHandler hingeNetworkHandler = this.mHandler;
                hingeNetworkHandler.sendMessageDelayed(hingeNetworkHandler.obtainMessage(6), DELAY_NETWORK_MILLISECOND);
            }
        }
    }

    private void recordWifiDisconnectToDataConnect(long j) {
        HashMap map = new HashMap();
        map.put("duration", String.valueOf(j));
        map.put("enabledDualLink", String.valueOf(this.mEnabledDualLink));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_wifi_disconnect_to_data_connect", map);
        clearWifiDisconnectData();
    }

    private void clearWifiDisconnectData() {
        this.mEnabledDualLink = false;
        this.mWifiDisconnectedTime = 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDetectMobileData() {
        if (isNecessaryCase() && ModemProvider.IS_QCOM) {
            if (!this.mIsMobileConnected) {
                this.mWifiToDataSelfStudyDBHelper.insertOrUpdateRecord(this.mLastWifiSsid, System.currentTimeMillis());
            }
            if (this.mIsTriggerWifiErrorRecovery) {
                this.mIsTriggerWifiErrorRecovery = false;
                disableDualLink(Scene.WIFI_ERROR_SSID);
            }
        }
    }

    public String getCurrentWifiSsid(Context context) {
        String ssid = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getSSID();
        log("getCurrentWifiSsid initial=" + ssid);
        if (!TextUtils.isEmpty(ssid) && ssid.startsWith("\"") && ssid.endsWith("\"")) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        log("getCurrentWifiSsid final=" + ssid);
        return !TextUtils.isEmpty(ssid) ? ssid : "";
    }

    private boolean isNecessaryCase() {
        if (VDBG) {
            return true;
        }
        return this.mUserDataEnabled && !this.mIsAirPlaneMode && !isDuringCall() && isDataRegisteredForDDS();
    }

    private String getMobileDataSettingName() {
        return MiuiPhoneUtils.PHONE_COUNT > 1 ? "mobile_data0" : "mobile_data";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMobileDataSettingOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), getMobileDataSettingName(), 0) != 0;
    }

    private boolean isDuringCall() {
        return MiuiPhoneUtils.isDuringCall();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAirPlaneModeOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
    }

    private boolean isDataRegisteredForDDS() {
        int defaultDataSlotId = getDefaultDataSlotId();
        if (isValidatePhoneId(defaultDataSlotId)) {
            return MiuiPhoneUtils.isDataRegistered(defaultDataSlotId);
        }
        return false;
    }

    private boolean isLinkTurboSetting() {
        return Settings.System.getInt(this.mContext.getContentResolver(), "linkturbo_is_enable", 0) == 1;
    }

    private static void localLog(String str, String str2) {
        localLog(LOG_TAG, str, str2);
    }

    private static void localLog(String str, String str2, String str3) {
        String str4 = str + "." + str2;
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                if (!map.containsKey(str4)) {
                    map.put(str4, new LocalLog(50));
                }
                ((LocalLog) map.get(str4)).log(str3);
            } catch (Throwable th) {
                throw th;
            }
        }
        Log.d(str, str2 + " " + str3);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("HingeNetworkManager dump start------");
        HashMap map = sLocalLogs;
        synchronized (map) {
            try {
                for (Map.Entry entry : map.entrySet()) {
                    printWriter.println((String) entry.getKey());
                    ((LocalLog) entry.getValue()).dump(null, printWriter, null);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        printWriter.println("HingeNetworkManager dump end------");
        printWriter.flush();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d(LOG_TAG, str);
    }
}
