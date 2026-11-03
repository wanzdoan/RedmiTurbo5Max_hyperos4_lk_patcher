package com.android.phone;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import androidx.preference.PreferenceManager;
import androidx.profileinstaller.ProfileInstallReceiver$$ExternalSyntheticLambda0;
import com.android.internal.os.BackgroundThread;
import com.android.phone.CloudController.TelephonyCloudController;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.android.phone.statistics.SimStatistic;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import miui.telephony.FeatureConfiguration;

/* JADX INFO: loaded from: classes.dex */
public class MiSimRepairManager {
    public static final boolean IS_CUST_SINGLE_SIM;
    private static MiSimRepairManager sInstance;
    private int mAbsentSlotId;
    private long[] mAbsentStartTime;
    private final AlarmManager.OnAlarmListener mAlarmListener;
    private Context mContext;
    private int mDetectSimAbnormalStage;
    private final BaseEvent.IEventListener mDisplayListener;
    private final Handler mHandler;
    private boolean mIsScreenOn;
    private boolean[] mIsSimSlotInsert;
    private String[] mLastSimState;
    private long mMaxSimAbsentDuration;
    private BroadcastReceiver mReceiver;
    private String[] mSimState;
    private SharedPreferences mStorage;
    private SubscriptionManager mSubMgr;
    private TelephonyManager mTm;
    private final long WAKE_UP_MAX_TIME = RelayUtils.Relay.RETRY_DELAY_IN_MILLIS;
    private final long SLEEP_MAX_TIME = 600000;
    private long mAlarmTime = -1;

    static {
        IS_CUST_SINGLE_SIM = SystemProperties.getInt("ro.miui.singlesim", 0) == 1;
    }

    public static MiSimRepairManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MiSimRepairManager(context);
        }
        return sInstance;
    }

    public MiSimRepairManager(Context context) {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mIsSimSlotInsert = new boolean[i];
        this.mAbsentStartTime = new long[i];
        this.mSimState = new String[i];
        this.mLastSimState = new String[i];
        this.mDetectSimAbnormalStage = 0;
        this.mReceiver = new BroadcastReceiver() { // from class: com.android.phone.MiSimRepairManager.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                Rlog.d("MiSimRepairManager", "receive action : " + action);
                if ("miui.intent.action.ACTION_OTA_UPDATE".equals(action)) {
                    MiSimRepairManager.this.mHandler.obtainMessage(1).sendToTarget();
                    return;
                }
                if ("android.intent.action.ACTION_SHUTDOWN".equals(action)) {
                    MiSimRepairManager.this.mHandler.obtainMessage(2).sendToTarget();
                } else if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
                    MiSimRepairManager.this.mHandler.obtainMessage(6).sendToTarget();
                } else if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    MiSimRepairManager.this.mHandler.obtainMessage(3, intent).sendToTarget();
                }
            }
        };
        this.mHandler = new Handler(BackgroundThread.get().getLooper()) { // from class: com.android.phone.MiSimRepairManager.3
            @Override // android.os.Handler
            public void handleMessage(Message message) throws Throwable {
                boolean z;
                switch (message.what) {
                    case 1:
                        MiSimRepairManager.this.checkMultiSimConfig();
                        break;
                    case 2:
                        MiSimRepairManager.this.restoreMultiSimConfig();
                        int i2 = MiSimRepairManager.this.mDetectSimAbnormalStage;
                        MiSimRepairManager miSimRepairManager = MiSimRepairManager.this;
                        if (i2 == 1) {
                            miSimRepairManager.recordSimAbnormal(miSimRepairManager.mAbsentSlotId, "sim_absent_detect_scene", 2, System.currentTimeMillis() - MiSimRepairManager.this.mAbsentStartTime[MiSimRepairManager.this.mAbsentSlotId]);
                        } else if (miSimRepairManager.mDetectSimAbnormalStage == 2) {
                            MiSimRepairManager miSimRepairManager2 = MiSimRepairManager.this;
                            miSimRepairManager2.recordSimAbnormal(miSimRepairManager2.mAbsentSlotId, "sim_absent_restore_scene", 2, MiSimRepairManager.this.mMaxSimAbsentDuration);
                        }
                        break;
                    case 3:
                        MiSimRepairManager.this.processSimStateChanged((Intent) message.obj);
                        break;
                    case 4:
                        MiSimRepairManager.this.processSimPowerChanged(message.arg1, message.arg2);
                        break;
                    case 5:
                        z = MiSimRepairManager.this.mTm.getSimCardState(MiSimRepairManager.this.mAbsentSlotId) == 11;
                        MiSimRepairManager miSimRepairManager3 = MiSimRepairManager.this;
                        miSimRepairManager3.recordSimAbnormal(miSimRepairManager3.mAbsentSlotId, "sim_absent_restore_scene", z ? 13 : 12, MiSimRepairManager.this.mMaxSimAbsentDuration);
                        break;
                    case 6:
                        String str = SystemProperties.get("persist.radio.multisim.config");
                        z = TextUtils.equals(str, "dsds") || TextUtils.equals(str, "dsda");
                        if (!TextUtils.isEmpty(MiSimRepairManager.this.mStorage.getString("key_multisim_config_self_recovery_reboot", ""))) {
                            MiSimRepairManager miSimRepairManager4 = MiSimRepairManager.this;
                            miSimRepairManager4.recordMultiSimConfigRestore(miSimRepairManager4.mStorage.getString("key_multisim_config_restore_scene", ""), z ? 15 : 14);
                            MiSimRepairManager.this.mStorage.edit().remove("key_multisim_config_self_recovery_reboot").apply();
                        }
                        break;
                    case 7:
                        MiSimRepairManager.this.mIsScreenOn = ((DeviceStateEvent.DisplayEvent) message.obj).isScreenOn();
                        break;
                }
            }
        };
        this.mDisplayListener = new BaseEvent.IEventListener() { // from class: com.android.phone.MiSimRepairManager.4
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public void onEvent(BaseEvent baseEvent) {
                if (baseEvent instanceof DeviceStateEvent.DisplayEvent) {
                    DeviceStateEvent.DisplayEvent displayEvent = (DeviceStateEvent.DisplayEvent) baseEvent;
                    if (displayEvent.isDefaultDisplay()) {
                        MiSimRepairManager.this.mHandler.obtainMessage(7, displayEvent).sendToTarget();
                    }
                }
            }
        };
        this.mAlarmListener = new AlarmManager.OnAlarmListener() { // from class: com.android.phone.MiSimRepairManager.5
            @Override // android.app.AlarmManager.OnAlarmListener
            public void onAlarm() {
                if (SubscriptionManager.isValidSlotIndex(MiSimRepairManager.this.mAbsentSlotId)) {
                    if (!MiSimRepairManager.isSimAbnormalSelfRecoveryDisabledByCloud() && "ABSENT".equals(MiSimRepairManager.this.mSimState[MiSimRepairManager.this.mAbsentSlotId]) && MiSimRepairManager.this.mIsSimSlotInsert[MiSimRepairManager.this.mAbsentSlotId]) {
                        MiSimRepairManager.this.mDetectSimAbnormalStage = 2;
                        MiSimRepairManager miSimRepairManager = MiSimRepairManager.this;
                        miSimRepairManager.tryRestoreSimState(miSimRepairManager.mAbsentSlotId, 0);
                    } else {
                        Rlog.d("MiSimRepairManager", "not allow restoreSimAbnormalState");
                        MiSimRepairManager miSimRepairManager2 = MiSimRepairManager.this;
                        miSimRepairManager2.recordSimAbnormal(miSimRepairManager2.mAbsentSlotId, "sim_absent_detect_scene", 1, MiSimRepairManager.this.mMaxSimAbsentDuration);
                    }
                }
            }
        };
        this.mContext = context;
        this.mStorage = PreferenceManager.getDefaultSharedPreferences(context);
        this.mTm = (TelephonyManager) this.mContext.getSystemService("phone");
        this.mSubMgr = (SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service");
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            this.mIsSimSlotInsert[i2] = false;
            this.mAbsentStartTime[i2] = 0;
            this.mSimState[i2] = "UNKNOWN";
            this.mLastSimState[i2] = "UNKNOWN";
        }
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "display_state_changed", this.mDisplayListener);
        initReceiver();
        registerDeviceProvisionedChangeObserver();
    }

    private void initReceiver() {
        boolean zIsSupportMultiSimConfigSelfRecovery = isSupportMultiSimConfigSelfRecovery();
        boolean zIsSupportSimAbsentSelfRecovery = isSupportSimAbsentSelfRecovery();
        IntentFilter intentFilter = new IntentFilter();
        if (zIsSupportMultiSimConfigSelfRecovery) {
            intentFilter.addAction("miui.intent.action.ACTION_OTA_UPDATE");
            intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        }
        if (zIsSupportMultiSimConfigSelfRecovery || zIsSupportSimAbsentSelfRecovery) {
            intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        }
        if (zIsSupportSimAbsentSelfRecovery) {
            intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        }
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
    }

    public static boolean isSupportMultiSimConfigSelfRecovery() {
        return !IS_CUST_SINGLE_SIM && FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_MULTISIM_CONFIG_SELF_RECOVERY);
    }

    public static boolean isSupportSimAbsentSelfRecovery() {
        return !IS_CUST_SINGLE_SIM && ModemProvider.IS_QCOM && FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_SIM_ABSENT_SELF_RECOVERY);
    }

    private static boolean isMultiSimCfgSelfRecoveryDisabledByCloud() {
        return TelephonyCloudController.CLOUD_SUPPORT_MULTISIM_CONFIG_SELF_RECOVERY_DISABLED;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSimAbnormalSelfRecoveryDisabledByCloud() {
        return TelephonyCloudController.CLOUD_SUPPORT_SIM_ABNORMAL_SELF_RECOVERY_DISABLED;
    }

    private void registerDeviceProvisionedChangeObserver() {
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("device_provisioned"), true, new ContentObserver(this.mHandler) { // from class: com.android.phone.MiSimRepairManager.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                if (MiuiPhoneUtils.isDevicesProvisioned(MiSimRepairManager.this.mContext)) {
                    MiSimRepairManager.this.mStorage.edit().putString("key_multisim_config_original", SystemProperties.get("persist.radio.multisim.config")).apply();
                    Rlog.d("MiSimRepairManager", "DeviceProvisioned change, save the initial value of dual card attributes");
                }
            }
        });
    }

    protected void checkMultiSimConfig() {
        if (TextUtils.equals(this.mStorage.getString("key_multisim_config_restore_scene", ""), "config_force_restore_scene")) {
            return;
        }
        String string = this.mStorage.getString("key_multisim_config_original", "");
        String str = SystemProperties.get("persist.radio.multisim.config");
        if (TextUtils.equals(string, "dsds") || TextUtils.equals(string, "dsda")) {
            if (TextUtils.equals(str, "ssss") || TextUtils.equals(str, "ss")) {
                this.mStorage.edit().putString("key_multisim_config_restore_scene", "config_ota_restore_scene").putString("key_multisim_config_restore", string).putString("key_multisim_config_last", str).apply();
                recordMultiSimConfigRestore("config_detect_scene", 0);
                Rlog.d("MiSimRepairManager", "After the OTA upgrade, dual card attribute abnormal");
            }
        }
    }

    protected void restoreMultiSimConfig() {
        String string = this.mStorage.getString("key_multisim_config_restore_scene", "");
        if (isMultiSimCfgSelfRecoveryDisabledByCloud() && !TextUtils.equals(string, "config_force_restore_scene")) {
            Rlog.d("MiSimRepairManager", "not allow restoreMultiSimConfig");
            return;
        }
        String string2 = this.mStorage.getString("key_multisim_config_restore", "");
        if (TextUtils.isEmpty(string2)) {
            return;
        }
        SystemProperties.set("persist.radio.multisim.config", string2);
        this.mStorage.edit().remove("key_multisim_config_restore").putString("key_multisim_config_self_recovery_reboot", "Reboot").apply();
        Rlog.d("MiSimRepairManager", "Restoration of abnormal dual card attributes");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processSimStateChanged(Intent intent) {
        String str;
        try {
            int intExtra = intent.getIntExtra("phone", -1);
            if (SubscriptionManager.isValidSlotIndex(intExtra) && !isVirtualSim(intExtra) && !isEuicc(intExtra)) {
                this.mSimState[intExtra] = intent.getStringExtra("ss");
                if (!"UNKNOWN".equals(this.mSimState[intExtra]) && !"NOT_READY".equals(this.mSimState[intExtra]) && ((str = this.mSimState[intExtra]) == null || !str.equals(this.mLastSimState[intExtra]))) {
                    this.mIsSimSlotInsert[intExtra] = isSimCardSlotInsertedStatus(intExtra);
                    detectSimAbnormalState(intExtra);
                    this.mLastSimState[intExtra] = this.mSimState[intExtra];
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void detectSimAbnormalState(int i) {
        if (this.mDetectSimAbnormalStage == 2) {
            Rlog.d("MiSimRepairManager", "Skip detection on SIM card recovery");
            return;
        }
        if (isSimAbnormalAndNotAbsent(i)) {
            recordSimAbnormal(i, "sim_abnormal_detect_scene", 0, 0L);
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (isAllCardsAbsent()) {
            for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
                long j = this.mAbsentStartTime[i2];
                if (j != 0) {
                    boolean[] zArr = this.mIsSimSlotInsert;
                    if (zArr[i2] && !zArr[i]) {
                        recordSimAbnormal(i2, "sim_absent_detect_scene", 3, jCurrentTimeMillis - j);
                        return;
                    }
                }
            }
            clearStatisticCache();
            return;
        }
        boolean zIsSimAbnormalChangeToAbsent = isSimAbnormalChangeToAbsent(i);
        long[] jArr = this.mAbsentStartTime;
        if (!zIsSimAbnormalChangeToAbsent) {
            long j2 = jArr[i];
            if (j2 != 0) {
                recordSimAbnormal(i, "sim_absent_detect_scene", 4, jCurrentTimeMillis - j2);
                return;
            }
            return;
        }
        if (jArr[i] == 0) {
            jArr[i] = jCurrentTimeMillis;
            this.mAbsentSlotId = i;
            this.mDetectSimAbnormalStage = 1;
            startTimeOut(jCurrentTimeMillis);
        }
    }

    private boolean isSimAbnormalAndNotAbsent(int i) {
        return "CARD_IO_ERROR".equals(this.mSimState[i]) || "CARD_RESTRICTED".equals(this.mSimState[i]) || "PERM_DISABLED".equals(this.mSimState[i]);
    }

    private boolean isSimAbnormalChangeToAbsent(int i) {
        return this.mIsSimSlotInsert[i] && !"UNKNOWN".equals(this.mLastSimState[i]) && !"ABSENT".equals(this.mLastSimState[i]) && "ABSENT".equals(this.mSimState[i]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processSimPowerChanged(int i, int i2) throws Throwable {
        MiSimRepairManager miSimRepairManager;
        MiSimRepairManager miSimRepairManager2;
        boolean z = i == 0;
        try {
            if (i2 == 0) {
                miSimRepairManager2 = this;
                if (z) {
                    miSimRepairManager2.tryRestoreSimState(miSimRepairManager2.mAbsentSlotId, 1);
                } else {
                    Message message = new Message();
                    message.what = 5;
                    miSimRepairManager2.mHandler.sendMessageDelayed(message, MirrorDesktopHelper.TIMEOUT_MILLIS);
                }
            } else if (i2 == 1) {
                miSimRepairManager2 = this;
                miSimRepairManager2.recordSimAbnormal(miSimRepairManager2.mAbsentSlotId, "sim_absent_restore_scene", z ? 8 : 11, miSimRepairManager2.mMaxSimAbsentDuration);
            } else if (i2 == 2) {
                miSimRepairManager2 = this;
                miSimRepairManager2.recordSimAbnormal(miSimRepairManager2.mAbsentSlotId, "sim_absent_restore_scene", z ? 6 : 9, miSimRepairManager2.mMaxSimAbsentDuration);
            } else if (i2 == 3) {
                miSimRepairManager2 = this;
                miSimRepairManager2.recordSimAbnormal(miSimRepairManager2.mAbsentSlotId, "sim_absent_restore_scene", z ? 7 : 10, miSimRepairManager2.mMaxSimAbsentDuration);
            } else if (i2 != 4) {
                miSimRepairManager2 = this;
            } else {
                try {
                    try {
                        recordSimAbnormal(this.mAbsentSlotId, "sim_absent_restore_scene", 5, this.mMaxSimAbsentDuration);
                        miSimRepairManager2 = this;
                    } catch (Throwable th) {
                        th = th;
                        miSimRepairManager = this;
                        Throwable th2 = th;
                        throw th2;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    miSimRepairManager = this;
                }
            }
        } catch (Throwable th4) {
            th = th4;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordSimAbnormal(int i, String str, int i2, long j) {
        HashMap map = new HashMap();
        map.put("abnormalSimState", "sim_abnormal_detect_scene".equals(str) ? this.mSimState[i] : "ABSENT");
        map.put("abnormalSimScene", str);
        map.put("abnormalSimResult", Integer.valueOf(i2));
        map.put("simAbsentDuration", SimStatistic.calSimAbsentDurationType(this.mIsScreenOn, j));
        map.put("isScreenOn", Boolean.valueOf(this.mIsScreenOn));
        clearStatisticCache();
        Rlog.d("MiSimRepairManager", "recordSimAbnormal params= " + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_sim_abnormal_restore", map);
    }

    public void recordMultiSimConfigRestore(String str, int i) {
        HashMap map = new HashMap();
        map.put("multisimConfigSelfRecoveryScene", str);
        map.put("multisimConfigSelfRecoveryResult", Integer.valueOf(i));
        Rlog.d("MiSimRepairManager", "recordMultiSimConfigRestore params= " + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_multi_sim_config_restore", map);
    }

    private void clearStatisticCache() {
        Arrays.fill(this.mAbsentStartTime, 0L);
        this.mDetectSimAbnormalStage = 0;
        cancelTimeOut();
    }

    private void startTimeOut(long j) {
        if (this.mAlarmTime != -1) {
            return;
        }
        AlarmManager alarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
        boolean z = this.mIsScreenOn;
        if (z) {
            this.mAlarmTime = j + RelayUtils.Relay.RETRY_DELAY_IN_MILLIS;
            this.mMaxSimAbsentDuration = RelayUtils.Relay.RETRY_DELAY_IN_MILLIS;
        } else {
            this.mAlarmTime = j + 600000;
            this.mMaxSimAbsentDuration = 600000L;
        }
        alarmManager.setExact(!z ? 1 : 0, this.mAlarmTime, "MiSimRepairManager", this.mAlarmListener, null);
    }

    private void cancelTimeOut() {
        if (this.mAlarmTime != -1) {
            ((AlarmManager) this.mContext.getSystemService("alarm")).cancel(this.mAlarmListener);
            this.mAlarmTime = -1L;
        }
    }

    private boolean isVirtualSim(int i) {
        return MiuiPhoneUtils.isVirtualSim(i) || MiuiPhoneUtils.isVirtualSeedSimBySlot(this.mContext, i);
    }

    private boolean isEuicc(int i) {
        List<SubscriptionInfo> availableSubscriptionInfoList = this.mSubMgr.getAvailableSubscriptionInfoList();
        if (availableSubscriptionInfoList == null) {
            return false;
        }
        for (SubscriptionInfo subscriptionInfo : availableSubscriptionInfoList) {
            if (subscriptionInfo != null && subscriptionInfo.getSimSlotIndex() == i) {
                return subscriptionInfo.isEmbedded();
            }
        }
        return false;
    }

    private boolean isSimCardSlotInsertedStatus(int i) {
        if (FiveGManagerBase.getInstance() != null) {
            return FiveGManagerBase.getInstance().isSimCardSlotInsertedStatus(i);
        }
        return false;
    }

    private boolean isAllCardsAbsent() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (!"ABSENT".equals(this.mSimState[i]) && !"UNKNOWN".equals(this.mSimState[i])) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryRestoreSimState(int i, final int i2) {
        Rlog.d("MiSimRepairManager", "Setting SIM " + i + " power state to " + i2);
        try {
            this.mTm.setSimPowerStateForSlot(i, i2, new ProfileInstallReceiver$$ExternalSyntheticLambda0(), new Consumer() { // from class: com.android.phone.MiSimRepairManager$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$tryRestoreSimState$0(i2, (Integer) obj);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$tryRestoreSimState$0(int i, Integer num) {
        this.mHandler.obtainMessage(4, i, num.intValue()).sendToTarget();
    }
}
