package com.android.phone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.OutcomeReceiver;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.MiuiSettings;
import android.telephony.ModemActivityInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.profileinstaller.ProfileInstallReceiver$$ExternalSyntheticLambda0;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import miui.os.HyperOSCustFeatureResolve;

/* JADX INFO: loaded from: classes.dex */
public class FiveGModemStatusTracker {
    private static String SECRET_CODE_ACTION = "android.provider.Telephony.SECRET_CODE";
    private static FiveGModemStatusTracker mInstance;
    private AlarmManager mAlarmManager;
    private Context mContext;
    private MSTHandler mMSTHandler;
    private PackageManager mPackageManager;
    private PendingIntent mPendingIntent;
    private float mRatio;
    protected final BroadcastReceiver mReceiver;
    private TelephonyManager mTelephonyManager;
    private boolean mTrackerStatus;
    private int POLL_MODEM_STATUS_TIME = 600000;
    private int CATCH_MODEM_LOG_TIME = 1800000;
    private int WAIT_MODEM_STATUS_TIME = 60000;
    private int RADIO_POWER_OFF_TIME = 1200000;
    private float RATIO_OF_DYNAMIC = 0.8f;
    private boolean mCanCatch = true;
    private boolean mIsPolling = false;
    private boolean mIsNightMode = false;
    private boolean mIsDynamic = false;
    private boolean mIsSetLogSize = false;
    private final String mNewLogSize = "set_log_size_10000";
    private ArrayList mModemActivityInfoList = new ArrayList();
    private int mCount = 0;
    private ArrayList mModemSleepRatios = new ArrayList();
    private ArrayList mModemSleepTime = new ArrayList();

    public static FiveGModemStatusTracker init() {
        if (mInstance == null && isModemStatusTrackerSupported()) {
            mInstance = new FiveGModemStatusTracker();
        }
        return mInstance;
    }

    private FiveGModemStatusTracker() {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.FiveGModemStatusTracker.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                if (action.equals("com.miui.modem.UPDATE")) {
                    boolean booleanExtra = intent.getBooleanExtra("tracker", true);
                    FiveGModemStatusTracker.this.updateModemTrackerStatus(booleanExtra);
                    FiveGModemStatusTracker.this.mCanCatch = intent.getBooleanExtra("mdlog", true);
                    FiveGModemStatusTracker.log("update modem tracker: " + booleanExtra + ", catch mdlog: " + FiveGModemStatusTracker.this.mCanCatch);
                    if (FiveGModemStatusTracker.this.mTrackerStatus) {
                        FiveGModemStatusTracker.this.RATIO_OF_DYNAMIC = intent.getFloatExtra("ratio", 0.8f);
                        FiveGModemStatusTracker.this.POLL_MODEM_STATUS_TIME = intent.getIntExtra("poll", 10) * 60000;
                        FiveGModemStatusTracker.this.CATCH_MODEM_LOG_TIME = intent.getIntExtra("catch", 30) * 60000;
                        FiveGModemStatusTracker.this.WAIT_MODEM_STATUS_TIME = intent.getIntExtra("wait", 1) * 60000;
                        FiveGModemStatusTracker.this.RADIO_POWER_OFF_TIME = intent.getIntExtra("power", 20) * 60000;
                        FiveGModemStatusTracker.this.mIsDynamic = true;
                        FiveGModemStatusTracker.log("update ratio=" + FiveGModemStatusTracker.this.RATIO_OF_DYNAMIC + ", poll=" + FiveGModemStatusTracker.this.POLL_MODEM_STATUS_TIME + ", catch=" + FiveGModemStatusTracker.this.CATCH_MODEM_LOG_TIME + ", wait=" + FiveGModemStatusTracker.this.WAIT_MODEM_STATUS_TIME + ", power=" + FiveGModemStatusTracker.this.RADIO_POWER_OFF_TIME);
                        return;
                    }
                    return;
                }
                if (action.equals("com.miui.powerkeeper_sleep_changed")) {
                    FiveGModemStatusTracker.this.mIsNightMode = 1 == intent.getIntExtra(MEModuleSignalT1s.EXTRA_STATE, 2);
                    FiveGModemStatusTracker.log("night mode change to " + FiveGModemStatusTracker.this.mIsNightMode);
                    boolean z = FiveGModemStatusTracker.this.mIsNightMode;
                    FiveGModemStatusTracker fiveGModemStatusTracker = FiveGModemStatusTracker.this;
                    if (z) {
                        fiveGModemStatusTracker.execPollModemStatus();
                        return;
                    } else {
                        fiveGModemStatusTracker.stopPollModemStatus();
                        return;
                    }
                }
                if (action.equals("com.miui.modem.alarm_poll_modem_status")) {
                    FiveGModemStatusTracker.log("receive alarm for poll");
                    FiveGModemStatusTracker.this.pollModemStatus();
                }
            }
        };
        this.mReceiver = broadcastReceiver;
        log("FiveGModemStatusTracker init");
        this.mMSTHandler = new MSTHandler();
        this.mContext = PhoneGlobals.getInstance();
        this.mTrackerStatus = true;
        EventHubManager.getInstance().registerListener("telephony", "cloud_update", new BaseEvent.IEventListener() { // from class: com.android.phone.FiveGModemStatusTracker$$ExternalSyntheticLambda0
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$new$0(baseEvent);
            }
        });
        this.mPackageManager = this.mContext.getPackageManager();
        this.mAlarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.miui.modem.UPDATE");
        intentFilter.addAction("com.miui.powerkeeper_sleep_changed");
        this.mContext.registerReceiver(broadcastReceiver, intentFilter, "android.permission.SET_DEBUG_APP", null, 2);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("com.miui.modem.alarm_poll_modem_status");
        this.mContext.registerReceiver(broadcastReceiver, intentFilter2, 4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(BaseEvent baseEvent) {
        if (baseEvent instanceof TelephonyEvent.CloudEvent) {
            MSTHandler mSTHandler = this.mMSTHandler;
            if (mSTHandler == null) {
                log("handler is null");
            } else {
                mSTHandler.sendEmptyMessage(10);
            }
        }
    }

    private static boolean isModemStatusTrackerSupported() {
        return !MiuiPhoneUtils.isMPDevice() && (MiuiPhoneUtils.isMtkModemStatusTrackSupported() || HyperOSCustFeatureResolve.getBoolean("support_modem_status_tracker_bool", false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateModemTrackerByCloud() {
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(this.mContext.getContentResolver(), "MtkModemStatus");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    log("updateModemTrackerByCloud data =" + cloudData.toString());
                    String string = cloudData.getString("Model", "NA");
                    String string2 = cloudData.getString("DeviceVersionType", "NA");
                    if (FiveGCloudUtils.modelMatched(string) && FiveGCloudUtils.versionTypeMatched(string2)) {
                        boolean z = cloudData.getBoolean("Status", true);
                        log("updateModemTrackerByCloud mTrackerStatus = " + this.mTrackerStatus + ", status = " + z);
                        updateModemTrackerStatus(z);
                        String string3 = cloudData.getString("Ratio", "0.80");
                        if (string3.startsWith("0.")) {
                            this.RATIO_OF_DYNAMIC = Float.valueOf(string3).floatValue();
                            this.mIsDynamic = true;
                            log("updateModemTrackerByCloud update mRatio = " + this.RATIO_OF_DYNAMIC);
                        }
                        this.POLL_MODEM_STATUS_TIME = cloudData.getInt("Poll", 10) * 60000;
                        log("updateModemTrackerByCloud update POLL_MODEM_STATUS_TIME = " + this.POLL_MODEM_STATUS_TIME);
                        this.mCanCatch = cloudData.getBoolean("Catch", false);
                        log("updateModemTrackerByCloud update mCanCatch = " + this.mCanCatch);
                    }
                }
                return;
            }
            log("updateModemTrackerByCloud cloudList is empty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateModemTrackerStatus(boolean z) {
        if (this.mTrackerStatus != z) {
            this.mTrackerStatus = z;
        }
        if (z) {
            return;
        }
        stopPollModemStatus();
    }

    class MSTHandler extends Handler {
        private MSTHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            FiveGModemStatusTracker.log("handle " + message);
            int i = message.what;
            if (i == 0) {
                FiveGModemStatusTracker.this.startPollModemStatusAlarm();
                return;
            }
            if (i == 1) {
                FiveGModemStatusTracker.this.catchMDLog("start");
                return;
            }
            if (i == 2) {
                FiveGModemStatusTracker.this.catchMDLog("stop");
                FiveGModemStatusTracker.this.initModemStatus();
            } else if (i == 3) {
                FiveGModemStatusTracker.this.setRadioPowerOff();
            } else if (i == 4) {
                FiveGModemStatusTracker.this.onModemActivityInfoResult((ModemActivityInfo) message.obj);
            } else {
                if (i != 10) {
                    return;
                }
                FiveGModemStatusTracker.this.updateModemTrackerByCloud();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startPollModemStatusAlarm() {
        log("startPollModemStatusAlarm");
        if (this.mAlarmManager == null) {
            return;
        }
        this.mPendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.miui.modem.alarm_poll_modem_status"), 335544320);
        this.mAlarmManager.set(2, SystemClock.elapsedRealtime() + ((long) this.POLL_MODEM_STATUS_TIME), this.mPendingIntent);
    }

    private void cancelPollModemStatusAlarm() {
        if (this.mAlarmManager == null || this.mPendingIntent == null) {
            return;
        }
        log("cancelPollModemStatusAlarm");
        this.mAlarmManager.cancel(this.mPendingIntent);
        this.mPendingIntent = null;
    }

    private void clearMtkModemStatus() {
        this.mCount = 0;
        this.mModemActivityInfoList.clear();
        this.mModemSleepRatios.clear();
        this.mModemSleepTime.clear();
    }

    private void updateSleepRatioThreshold() {
        if (this.mIsDynamic) {
            this.mRatio = this.RATIO_OF_DYNAMIC;
        } else {
            this.mRatio = 0.8f;
        }
        float f = this.mRatio;
        float f2 = f - (this.mCount * 0.05f);
        if (f2 > 0.0f) {
            f = f2;
        }
        this.mRatio = f;
    }

    private void recordMtkModemStatus() {
        log("recordMtkModemStatus count = " + this.mModemSleepRatios.size());
        if (this.mModemSleepRatios.isEmpty() || this.mCount == 0) {
            log("no data to record!");
            return;
        }
        HashMap map = new HashMap();
        map.put("mtk_modem_status", this.mModemSleepRatios);
        map.put("mtk_modem_time", this.mModemSleepTime);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_modem_sleep_status", map);
    }

    private void setModemLogSize(String str) {
        log("setModemLogSize cmd=" + str);
        Intent intent = new Intent("com.debug.loggerui.ADB_CMD");
        intent.addFlags(268435456);
        intent.putExtra("cmd_name", str);
        intent.putExtra("cmd_target", 2);
        intent.setComponent(new ComponentName("com.debug.loggerui", "com.debug.loggerui.framework.LogReceiver"));
        this.mContext.sendBroadcast(intent, "android.permission.SET_DEBUG_APP");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void catchMDLog(String str) {
        log("sendMDLog for " + str);
        Intent intent = new Intent("com.debug.loggerui.ADB_CMD");
        intent.addFlags(268435456);
        intent.putExtra("cmd_name", str);
        intent.putExtra("cmd_target", 7);
        intent.setComponent(new ComponentName("com.debug.loggerui", "com.debug.loggerui.framework.LogReceiver"));
        this.mContext.sendBroadcast(intent, "android.permission.SET_DEBUG_APP");
    }

    private void sendBugreport() {
        log("sendBugreport");
        Intent intent = new Intent(SECRET_CODE_ACTION, Uri.parse("android_secret_code://284"));
        intent.setPackage("com.miui.bugreport");
        this.mContext.sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRadioPowerOff() {
        log("setRadioPowerOff");
        for (Phone phone : PhoneFactory.getPhones()) {
            phone.getServiceStateTracker().powerOffRadioSafely();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pollModemStatus() {
        log("query modem status");
        updateSleepRatioThreshold();
        requestModemActivityInfo();
    }

    private void evaluateAndScheduleNextPoll() {
        boolean zTriggerMDLog = triggerMDLog();
        boolean zEquals = "1".equals(SystemProperties.get("vendor.mdlogger.Running", "0"));
        log("md log running is " + zEquals + " and trigger is " + zTriggerMDLog);
        if (zTriggerMDLog && !zEquals && !this.mMSTHandler.hasMessages(2)) {
            if (!this.mIsSetLogSize) {
                setModemLogSize("set_log_size_10000");
                this.mIsSetLogSize = true;
            }
            this.mMSTHandler.obtainMessage(1).sendToTarget();
            this.mMSTHandler.sendEmptyMessageDelayed(2, this.CATCH_MODEM_LOG_TIME);
            if (this.mIsNightMode) {
                this.mMSTHandler.sendEmptyMessageDelayed(3, this.RADIO_POWER_OFF_TIME);
            }
        }
        if (this.mMSTHandler.hasMessages(2) || zEquals) {
            this.mMSTHandler.sendEmptyMessageDelayed(0, this.CATCH_MODEM_LOG_TIME);
        } else {
            this.mMSTHandler.sendEmptyMessage(0);
        }
    }

    /* JADX WARN: Code duplicated, block: B:23:0x00ff  */
    private boolean triggerMDLog() {
        boolean z;
        int i;
        int size = this.mModemActivityInfoList.size();
        if (size > 1) {
            ModemActivityInfo delta = ((ModemActivityInfo) this.mModemActivityInfoList.get(size - 2)).getDelta((ModemActivityInfo) this.mModemActivityInfoList.get(size - 1));
            long jCalculateTotalTime = calculateTotalTime(delta) / 1000;
            long sleepTimeMillis = delta.getSleepTimeMillis() / 1000;
            if (jCalculateTotalTime > 0 && sleepTimeMillis >= 0) {
                float f = sleepTimeMillis / jCalculateTotalTime;
                this.mModemSleepTime.add(new SimpleDateFormat("HH:mm:ss").format(Long.valueOf(System.currentTimeMillis())));
                DecimalFormat decimalFormat = new DecimalFormat("0.000");
                DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
                decimalFormatSymbols.setDecimalSeparator('.');
                decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
                this.mModemSleepRatios.add(decimalFormat.format(f));
            }
        }
        if (size >= 7) {
            ModemActivityInfo delta2 = ((ModemActivityInfo) this.mModemActivityInfoList.get(0)).getDelta((ModemActivityInfo) this.mModemActivityInfoList.get(size - 1));
            long jCalculateTotalTime2 = calculateTotalTime(delta2) / 1000;
            long sleepTimeMillis2 = delta2.getSleepTimeMillis() / 1000;
            if (jCalculateTotalTime2 <= 0 || sleepTimeMillis2 < 0) {
                z = false;
            } else {
                float f2 = sleepTimeMillis2 / jCalculateTotalTime2;
                z = f2 < this.mRatio;
                if (z) {
                    localLog("triggerMDLog", "isAwake=true ratio=" + f2 + " total=" + jCalculateTotalTime2 + " sleep=" + sleepTimeMillis2 + " count=" + this.mCount);
                } else {
                    log("triggerMDLog isAwake=false ratio=" + f2 + " total=" + jCalculateTotalTime2 + " sleep=" + sleepTimeMillis2 + " count=" + this.mCount);
                }
            }
        } else {
            z = false;
        }
        if (!z || !this.mCanCatch || (i = this.mCount) >= 3) {
            return false;
        }
        this.mCount = i + 1;
        return true;
    }

    private void requestModemActivityInfo() {
        try {
            this.mTelephonyManager.requestModemActivityInfo(new ProfileInstallReceiver$$ExternalSyntheticLambda0(), new OutcomeReceiver() { // from class: com.android.phone.FiveGModemStatusTracker.2
                @Override // android.os.OutcomeReceiver
                public void onResult(ModemActivityInfo modemActivityInfo) {
                    FiveGModemStatusTracker.this.mMSTHandler.obtainMessage(4, modemActivityInfo).sendToTarget();
                }

                @Override // android.os.OutcomeReceiver
                public void onError(TelephonyManager.ModemActivityInfoException modemActivityInfoException) {
                    FiveGModemStatusTracker.log("requestModemActivityInfo error: " + modemActivityInfoException);
                    FiveGModemStatusTracker.this.mMSTHandler.obtainMessage(4, null).sendToTarget();
                }
            });
        } catch (Exception e) {
            log("requestModemActivityInfo exception: " + e);
            this.mMSTHandler.obtainMessage(4, null).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onModemActivityInfoResult(ModemActivityInfo modemActivityInfo) {
        if (modemActivityInfo == null || !modemActivityInfo.isValid()) {
            log("invalid ModemActivityInfo");
            if (!this.mIsPolling) {
                log("can't get modem activity info for init");
                return;
            } else {
                this.mMSTHandler.sendEmptyMessageDelayed(0, this.WAIT_MODEM_STATUS_TIME);
                return;
            }
        }
        logModemActivityInfo(modemActivityInfo);
        this.mModemActivityInfoList.add(modemActivityInfo);
        while (this.mModemActivityInfoList.size() > 7) {
            this.mModemActivityInfoList.remove(0);
        }
        if (!this.mIsPolling) {
            this.mMSTHandler.sendEmptyMessageDelayed(0, this.WAIT_MODEM_STATUS_TIME);
            this.mIsPolling = true;
            log("Start Monitor");
            return;
        }
        evaluateAndScheduleNextPoll();
    }

    private void logModemActivityInfo(ModemActivityInfo modemActivityInfo) {
        long sleepTimeMillis = modemActivityInfo.getSleepTimeMillis();
        long idleTimeMillis = modemActivityInfo.getIdleTimeMillis();
        long receiveTimeMillis = modemActivityInfo.getReceiveTimeMillis();
        long transmitDurationMillisAtPowerLevel = 0;
        for (int i = 0; i < ModemActivityInfo.getNumTxPowerLevels(); i++) {
            transmitDurationMillisAtPowerLevel += modemActivityInfo.getTransmitDurationMillisAtPowerLevel(i);
        }
        log("Sleep time: " + sleepTimeMillis + ", Idle time: " + idleTimeMillis + ", RxTime: " + receiveTimeMillis + ", TxTime: " + transmitDurationMillisAtPowerLevel + ", Total: " + (sleepTimeMillis + idleTimeMillis + receiveTimeMillis + transmitDurationMillisAtPowerLevel));
    }

    private long calculateTotalTime(ModemActivityInfo modemActivityInfo) {
        long sleepTimeMillis = modemActivityInfo.getSleepTimeMillis() + modemActivityInfo.getIdleTimeMillis() + modemActivityInfo.getReceiveTimeMillis();
        for (int i = 0; i < ModemActivityInfo.getNumTxPowerLevels(); i++) {
            sleepTimeMillis += modemActivityInfo.getTransmitDurationMillisAtPowerLevel(i);
        }
        return sleepTimeMillis;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void execPollModemStatus() {
        if (!this.mTrackerStatus || !isUserProbation()) {
            log("don't allow poll");
            return;
        }
        if (this.mMSTHandler == null) {
            log("handler is null");
        } else if (this.mIsPolling) {
            log("already exec");
        } else {
            initModemStatus();
            requestModemActivityInfo();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initModemStatus() {
        log("init Modem Status");
        this.mModemActivityInfoList.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopPollModemStatus() {
        if (this.mMSTHandler.hasMessages(2)) {
            this.mMSTHandler.removeMessages(2);
            this.mMSTHandler.removeMessages(0);
            this.mMSTHandler.sendEmptyMessage(2);
            log("stop catch md log due to stop monitoring");
        }
        if (this.mMSTHandler.hasMessages(3)) {
            this.mMSTHandler.removeMessages(3);
        }
        if (this.mIsPolling) {
            this.mMSTHandler.removeMessages(0);
            this.mIsPolling = false;
            if (this.mCount != 0) {
                sendBugreport();
            }
            recordMtkModemStatus();
            clearMtkModemStatus();
            log("Stop Monitor");
        }
        cancelPollModemStatusAlarm();
    }

    private boolean isUserProbation() {
        try {
            boolean z = (this.mPackageManager.getApplicationInfo("com.jtom.userprobation", 0).flags & 8388608) != 0;
            log("user probation app found, installed=" + z);
            return z;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    protected static void log(String str) {
        Log.d("FiveGModemStatusTracker", str);
    }

    protected static void localLog(String str, String str2) {
        FiveGManagerBase.getInstance();
        FiveGManagerBase.localLog("FiveGModemStatusTracker", str, str2);
    }
}
