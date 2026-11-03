package com.android.phone.smarttravel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.telephony.SettingsObserver;
import com.android.phone.PhoneGlobals;
import com.android.phone.statistics.MiuiTelephonyStatistic;
import com.android.phone.statistics.utils.DefaultPreferenceHelper;
import com.android.phone.statistics.utils.ObjectSerializer;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import miui.telephony.DefaultSimManager;

/* JADX INFO: loaded from: classes.dex */
public class DriveModeController {
    private static Context mContext;
    private static DriveModeController sInstance;
    public DriveModeStats mDriveModeStats;
    private DriveModeHandler mHandler;
    private SettingsObserver mSettingsObserver;
    private HandlerThread mWorkThread;
    private boolean VDBG = false;
    private int mDriveState = 0;
    private boolean mIsDriving = false;
    private long mLastDriveModeEnterTime = -1;
    private boolean mIsWifiConnected = false;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.smarttravel.DriveModeController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                DriveModeController.this.mHandler.obtainMessage(3, intent).sendToTarget();
            } else if ("DriveModeController.intent.debugall".equals(action)) {
                DriveModeController.this.mHandler.obtainMessage(100).sendToTarget();
            }
        }
    };

    public static synchronized DriveModeController getInstance() {
        try {
            if (sInstance == null) {
                sInstance = new DriveModeController();
            }
        } catch (Throwable th) {
            throw th;
        }
        return sInstance;
    }

    private DriveModeController() {
        this.mWorkThread = null;
        this.mHandler = null;
        log("DriveModeController init");
        mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("DriveModeController", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new DriveModeHandler(this.mWorkThread.getLooper());
        this.mDriveModeStats = new DriveModeStats();
        this.mSettingsObserver = new SettingsObserver(mContext, this.mHandler);
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    class DriveModeHandler extends Handler {
        DriveModeHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            DriveModeController.this.log("msg.what = " + message.what);
            int i = message.what;
            if (i == 1) {
                DriveModeController.this.initAction();
                return;
            }
            if (i == 2) {
                DriveModeController.this.processDriveStateChangeEvent();
                return;
            }
            if (i == 3) {
                DriveModeController.this.processNetworkStateChanged((Intent) message.obj);
            } else {
                if (i != 100) {
                    return;
                }
                DriveModeController.this.VDBG = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAction() {
        registerReceiver();
        registerSettingsCallbacks();
    }

    private void registerSettingsCallbacks() {
        this.mSettingsObserver.observe(Settings.Global.getUriFor("sensor_drive_state"), 2);
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("DriveModeController.intent.debugall");
        mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
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
            if (networkInfo.getType() == 1) {
                boolean z = state == state2;
                if (this.mIsWifiConnected == z) {
                    return;
                }
                this.mIsWifiConnected = z;
                log("wifi connected=" + this.mIsWifiConnected);
                if (this.mIsWifiConnected && this.mIsDriving) {
                    this.mIsDriving = false;
                    recordDurationAndUpdateNwBand();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDriveStateChangeEvent() {
        int driveState;
        if (isDriveNetworkCharacter() && this.mDriveState != (driveState = getDriveState())) {
            this.mDriveState = driveState;
            log("processDriveStateChangeEvent mDriveState=" + this.mDriveState + ", mIsDriving=" + this.mIsDriving);
            int i = this.mDriveState;
            if (i == 10 && !this.mIsDriving) {
                this.mIsDriving = true;
                this.mLastDriveModeEnterTime = SystemClock.elapsedRealtime();
                recordCountDriveModeStats("driveModeCount");
                if (MiuiTelephonyStatistic.getInstance() == null || MiuiTelephonyStatistic.getInstance().mDriveModemStats == null) {
                    return;
                }
                MiuiTelephonyStatistic.getInstance().mDriveModemStats.updateDriveState(true);
                return;
            }
            if (i == 10 || !this.mIsDriving) {
                return;
            }
            this.mIsDriving = false;
            recordDurationAndUpdateNwBand();
        }
    }

    private void recordDurationAndUpdateNwBand() {
        recordListDriveModeStats("driveModeDuration", Long.valueOf((SystemClock.elapsedRealtime() - this.mLastDriveModeEnterTime) / 1000));
        if (MiuiTelephonyStatistic.getInstance() == null || MiuiTelephonyStatistic.getInstance().mDriveModemStats == null) {
            return;
        }
        MiuiTelephonyStatistic.getInstance().updateNwBandInfo(DefaultSimManager.getDefaultDataSlotId(), null, true);
        MiuiTelephonyStatistic.getInstance().mDriveModemStats.updateDriveState(false);
    }

    private boolean isDriveNetworkCharacter() {
        return !this.mIsWifiConnected;
    }

    private int getDriveState() {
        return Settings.Global.getInt(mContext.getContentResolver(), "sensor_drive_state", 0);
    }

    private void recordCountDriveModeStats(String str) {
        DriveModeStats driveModeStats = this.mDriveModeStats;
        if (driveModeStats != null) {
            driveModeStats.recordCount(str);
        }
    }

    private void recordListDriveModeStats(String str, Object obj) {
        DriveModeStats driveModeStats = this.mDriveModeStats;
        if (driveModeStats != null) {
            driveModeStats.recordList(str, obj);
        }
    }

    public void reportStatistics() {
        DriveModeStats driveModeStats = this.mDriveModeStats;
        if (driveModeStats != null) {
            driveModeStats.reportStatistic();
            this.mDriveModeStats.clear();
        }
    }

    public void reloadDriveModeStatsFromSP() {
        DriveModeStats driveModeStats = this.mDriveModeStats;
        if (driveModeStats != null) {
            driveModeStats.reloadStatsFromSP();
        }
    }

    class DriveModeStats {
        private ConcurrentHashMap mStats;

        private DriveModeStats() {
            this.mStats = new ConcurrentHashMap();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recordCount(String str) {
            ConcurrentHashMap concurrentHashMap = this.mStats;
            if (concurrentHashMap == null) {
                return;
            }
            concurrentHashMap.put(str, Integer.valueOf(((Integer) concurrentHashMap.getOrDefault(str, 0)).intValue() + 1));
            try {
                if (DriveModeController.this.VDBG) {
                    DriveModeController.this.log("recordCount key=" + str + ", mStats=" + this.mStats);
                }
                DefaultPreferenceHelper.getInstance().setDriveModeStats(ObjectSerializer.serialize(this.mStats));
            } catch (Exception e) {
                DriveModeController.this.log("record exception:" + e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void recordList(String str, Object obj) {
            if (this.mStats == null) {
                return;
            }
            if (DriveModeController.this.VDBG) {
                DriveModeController.this.log("record key=" + str + ", value=" + obj);
            }
            if (obj instanceof Long) {
                ArrayList arrayListObjToList = DriveModeController.objToList(this.mStats.getOrDefault(str, null), Long.class);
                arrayListObjToList.add((Long) obj);
                this.mStats.put(str, arrayListObjToList);
                try {
                    if (DriveModeController.this.VDBG) {
                        DriveModeController.this.log("recordList mStats=" + this.mStats);
                    }
                    DefaultPreferenceHelper.getInstance().setDriveModeStats(ObjectSerializer.serialize(this.mStats));
                    return;
                } catch (Exception e) {
                    DriveModeController.this.log("record exception:" + e);
                    return;
                }
            }
            if (DriveModeController.this.VDBG) {
                DriveModeController.this.log("record key=" + str + ", value type not support");
            }
        }

        public void reportStatistic() {
            ConcurrentHashMap concurrentHashMap = this.mStats;
            if (concurrentHashMap == null || concurrentHashMap.isEmpty()) {
                return;
            }
            HashMap map = new HashMap(this.mStats);
            DriveModeController.this.log("reportStatistic mStats=" + this.mStats);
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_drive_mode_statistic", map);
        }

        public void reloadStatsFromSP() {
            try {
                ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) ObjectSerializer.deserialize(DefaultPreferenceHelper.getInstance().getDriveModeStats());
                if (concurrentHashMap != null) {
                    this.mStats = concurrentHashMap;
                }
                if (DriveModeController.this.VDBG) {
                    DriveModeController.this.log("reloadStatsFromSP mStats=" + this.mStats);
                }
            } catch (Exception e) {
                DriveModeController.this.log("reloadStatsFromSP exception:" + e);
            }
        }

        public void clear() {
            ConcurrentHashMap concurrentHashMap = this.mStats;
            if (concurrentHashMap == null) {
                return;
            }
            concurrentHashMap.clear();
            try {
                DefaultPreferenceHelper.getInstance().setDriveModeStats(ObjectSerializer.serialize(this.mStats));
            } catch (Exception e) {
                DriveModeController.this.log("clear exception:" + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ArrayList objToList(Object obj, Class cls) {
        ArrayList arrayList = new ArrayList();
        try {
            if (obj instanceof ArrayList) {
                ArrayList arrayList2 = (ArrayList) obj;
                int size = arrayList2.size();
                int i = 0;
                while (i < size) {
                    Object obj2 = arrayList2.get(i);
                    i++;
                    arrayList.add(cls.cast(obj2));
                }
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return arrayList;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("DriveModeController", str);
    }
}
