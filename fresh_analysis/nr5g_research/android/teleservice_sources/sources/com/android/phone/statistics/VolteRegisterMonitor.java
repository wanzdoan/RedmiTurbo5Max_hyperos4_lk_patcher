package com.android.phone.statistics;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.services.telephony.ims.ImsAdapterBase;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import miui.telephony.DefaultSimManager;

/* JADX INFO: loaded from: classes.dex */
public class VolteRegisterMonitor extends Handler {
    private static VolteRegisterMonitor sInstance;
    private Context mContext;
    private long[] mLastVolteCdma1xFailedTime;
    private int[] mLastVolteCdma1xRat;
    private boolean[] mLastVolteCdma1xStatus;
    private long[] mLastVolteRegistrationFailedTime;
    private int[] mLastVolteRegistrationRat;
    private boolean[] mLastVolteStatus;
    private TelephonyManager mTm;
    private boolean[] mUploadLog;
    private ConcurrentHashMap mVolteGlobalizationStats;

    private VolteRegisterMonitor(Context context) {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mLastVolteRegistrationFailedTime = new long[i];
        this.mUploadLog = new boolean[i];
        this.mLastVolteRegistrationRat = new int[i];
        this.mLastVolteCdma1xFailedTime = new long[i];
        this.mLastVolteCdma1xRat = new int[i];
        this.mLastVolteStatus = new boolean[i];
        this.mLastVolteCdma1xStatus = new boolean[i];
        this.mVolteGlobalizationStats = new ConcurrentHashMap();
        this.mContext = context;
        this.mTm = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        obtainMessage(3).sendToTarget();
    }

    public static VolteRegisterMonitor getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new VolteRegisterMonitor(context);
        }
        return sInstance;
    }

    private void init() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mLastVolteRegistrationFailedTime[i] = 0;
            this.mUploadLog[i] = true;
            this.mLastVolteCdma1xFailedTime[i] = 0;
            this.mLastVolteRegistrationRat[i] = -1;
            this.mLastVolteCdma1xRat[i] = -1;
            this.mLastVolteStatus[i] = false;
            this.mLastVolteCdma1xStatus[i] = false;
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        super.handleMessage(message);
        int i = message.what;
        if (i == 1) {
            calcVolteRegistrationFailedDuration(message.arg1, message.arg2);
        } else if (i == 2) {
            calcVolteCdma1xFailedDuration(message.arg1, message.arg2);
        } else {
            if (i != 3) {
                return;
            }
            init();
        }
    }

    private String getAvailableOperatorNumeric(ServiceState serviceState, int i) {
        String operatorNumeric = serviceState != null ? serviceState.getOperatorNumeric() : null;
        if (TextUtils.isEmpty(operatorNumeric)) {
            operatorNumeric = miui.telephony.TelephonyManager.getDefault().getSimOperatorForSlot(i);
        }
        return TextUtils.isEmpty(operatorNumeric) ? "" : operatorNumeric;
    }

    private void recordVolteRegistrationFailed(int i, ServiceState serviceState, long j, boolean z) {
        HashMap map = new HashMap();
        map.put("Network", getAvailableOperatorNumeric(serviceState, i));
        map.put("Value", Long.valueOf(j / 1000));
        map.put("isDds", i == DefaultSimManager.getDefaultDataSlotId() ? "1" : "0");
        if (MiuiTelephonyStatistic.getInstance() != null) {
            map.put("sceneType", MiuiTelephonyStatistic.getInstance().getSceneType());
        }
        if (z) {
            map.put("networkType", Integer.valueOf(this.mLastVolteCdma1xRat[i]));
            map.put("volteEnable", Boolean.valueOf(ImsAdapterBase.getInstance().getEnhanced4gLteModeSetting(this.mContext, i)));
            Utils.logD("VolteRegisterMonitor", "recordVolteAnd1xRegistrationFailed params= " + map);
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_volte_cdma1x_failed", map);
            return;
        }
        map.put("networkType", Integer.valueOf(this.mLastVolteRegistrationRat[i]));
        Utils.logD("VolteRegisterMonitor", "recordVolteRegistrationFailed params= " + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_registered_volteFailed", map);
        if (MiuiTelephonyStatistic.getInstance() != null) {
            map.put("UploadLog", String.valueOf(this.mUploadLog[i]));
            this.mUploadLog[i] = true;
            MiuiTelephonyStatistic.getInstance().recordDFSEventStatistic(915100015, map);
        }
    }

    private void calcVolteCdma1xFailedDuration(int i, int i2) {
        this.mLastVolteCdma1xFailedTime[i] = SystemClock.elapsedRealtime();
        this.mLastVolteCdma1xRat[i] = i2;
    }

    private void calcVolteRegistrationFailedDuration(int i, int i2) {
        this.mLastVolteRegistrationFailedTime[i] = SystemClock.elapsedRealtime();
        this.mLastVolteRegistrationRat[i] = i2;
    }

    private void processEventRecord(int i, int i2, ServiceState serviceState) {
        if (i == 1) {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            long[] jArr = this.mLastVolteCdma1xFailedTime;
            long j = jElapsedRealtime - jArr[i2];
            jArr[i2] = 0;
            recordVolteRegistrationFailed(i2, serviceState, j, true);
            return;
        }
        if (i != 2) {
            return;
        }
        long jElapsedRealtime2 = SystemClock.elapsedRealtime();
        long[] jArr2 = this.mLastVolteRegistrationFailedTime;
        long j2 = jElapsedRealtime2 - jArr2[i2];
        jArr2[i2] = 0;
        recordVolteRegistrationFailed(i2, serviceState, j2, false);
    }

    /* JADX WARN: Code duplicated, block: B:28:0x0051 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:29:0x0053 A[Catch: all -> 0x001d, TryCatch #0 {all -> 0x001d, blocks: (B:6:0x0005, B:12:0x0014, B:21:0x0025, B:23:0x002b, B:25:0x0033, B:27:0x003b, B:29:0x0053, B:31:0x0062), top: B:36:0x0005 }] */
    /* JADX WARN: Code duplicated, block: B:31:0x0062 A[Catch: all -> 0x001d, TRY_LEAVE, TryCatch #0 {all -> 0x001d, blocks: (B:6:0x0005, B:12:0x0014, B:21:0x0025, B:23:0x002b, B:25:0x0033, B:27:0x003b, B:29:0x0053, B:31:0x0062), top: B:36:0x0005 }] */
    public synchronized void processCheckVolteCdma1xState(int i, ServiceState serviceState, boolean z) {
        if (serviceState == null) {
            return;
        }
        try {
            int voiceNetworkType = serviceState.getVoiceNetworkType();
            boolean z2 = voiceNetworkType == 4 || voiceNetworkType == 7 || z || serviceState.getDataRegState() == 1;
            if (!z2) {
                boolean[] zArr = this.mLastVolteCdma1xStatus;
                if (zArr[i]) {
                    zArr[i] = false;
                    if (!hasMessages(2) && this.mLastVolteCdma1xFailedTime[i] == 0) {
                        Message message = new Message();
                        message.what = 2;
                        message.arg1 = i;
                        message.arg2 = serviceState.getDataNetworkType();
                        sendMessageDelayed(message, RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
                    }
                } else if (z2) {
                    this.mLastVolteCdma1xStatus[i] = true;
                    removeMessages(2);
                    if (this.mLastVolteCdma1xFailedTime[i] != 0) {
                        processEventRecord(1, i, serviceState);
                    }
                }
            } else if (z2) {
                this.mLastVolteCdma1xStatus[i] = true;
                removeMessages(2);
                if (this.mLastVolteCdma1xFailedTime[i] != 0) {
                    processEventRecord(1, i, serviceState);
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX WARN: Code duplicated, block: B:26:0x0058 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:27:0x005a A[Catch: all -> 0x0019, TryCatch #0 {all -> 0x0019, blocks: (B:6:0x0005, B:8:0x0012, B:27:0x005a, B:29:0x0069, B:14:0x001d, B:16:0x0023, B:18:0x002c, B:20:0x0034, B:22:0x0047, B:25:0x0051, B:24:0x004d), top: B:34:0x0005 }] */
    /* JADX WARN: Code duplicated, block: B:29:0x0069 A[Catch: all -> 0x0019, TRY_LEAVE, TryCatch #0 {all -> 0x0019, blocks: (B:6:0x0005, B:8:0x0012, B:27:0x005a, B:29:0x0069, B:14:0x001d, B:16:0x0023, B:18:0x002c, B:20:0x0034, B:22:0x0047, B:25:0x0051, B:24:0x004d), top: B:34:0x0005 }] */
    public synchronized void processCheckVolteState(int i, ServiceState serviceState, boolean z) {
        if (serviceState == null) {
            return;
        }
        try {
            int dataNetworkType = serviceState.getDataNetworkType();
            if (Utils.isLte(dataNetworkType) || Utils.isNr(dataNetworkType)) {
                if (!z) {
                    boolean[] zArr = this.mLastVolteStatus;
                    if (zArr[i]) {
                        zArr[i] = false;
                        if (!hasMessages(1) && this.mLastVolteRegistrationFailedTime[i] == 0) {
                            Message message = new Message();
                            message.what = 1;
                            message.arg1 = i;
                            message.arg2 = dataNetworkType;
                            if (!MiuiPhoneUtils.isScreenOn(this.mContext) || serviceState.getDataRegState() == 1) {
                                this.mUploadLog[i] = false;
                            }
                            sendMessageDelayed(message, RelayUtils.Relay.RETRY_DELAY_IN_MILLIS);
                        }
                    } else if (z) {
                        this.mLastVolteStatus[i] = true;
                        removeMessages(1);
                        if (this.mLastVolteRegistrationFailedTime[i] != 0) {
                            processEventRecord(2, i, serviceState);
                        }
                    }
                } else if (z) {
                    this.mLastVolteStatus[i] = true;
                    removeMessages(1);
                    if (this.mLastVolteRegistrationFailedTime[i] != 0) {
                        processEventRecord(2, i, serviceState);
                    }
                }
            } else if (z) {
                this.mLastVolteStatus[i] = true;
                removeMessages(1);
                if (this.mLastVolteRegistrationFailedTime[i] != 0) {
                    processEventRecord(2, i, serviceState);
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public void clearAndRecordVolteFailedEvent(int i, ServiceState serviceState) {
        this.mLastVolteStatus[i] = false;
        removeMessages(1);
        if (this.mLastVolteRegistrationFailedTime[i] != 0) {
            processEventRecord(2, i, serviceState);
        }
    }

    public void clearAndRecordCdma1xFailedEvent(int i, ServiceState serviceState) {
        this.mLastVolteCdma1xStatus[i] = false;
        removeMessages(2);
        if (this.mLastVolteCdma1xFailedTime[i] != 0) {
            processEventRecord(1, i, serviceState);
        }
    }

    public void updateVolteGlobalizationStats(int i, String str, boolean z) {
        Utils.logD("VolteRegisterMonitor", "updateVolteGlobalizationStats  phoneId = " + i + ", plmn = " + str + ", register = " + z);
        if ("NoSim".equals(str)) {
            return;
        }
        ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mVolteGlobalizationStats.get(Integer.valueOf(i));
        if (concurrentHashMap == null) {
            concurrentHashMap = new ConcurrentHashMap();
        }
        concurrentHashMap.put(str, Boolean.valueOf(z));
        this.mVolteGlobalizationStats.put(Integer.valueOf(i), concurrentHashMap);
    }

    public void recordVolteGlobalizationStats() {
        for (Integer num : this.mVolteGlobalizationStats.keySet()) {
            int iIntValue = num.intValue();
            if (ImsAdapterBase.getInstance().getEnhanced4gLteModeSetting(this.mContext, iIntValue) && this.mTm.hasIccCard(iIntValue)) {
                ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.mVolteGlobalizationStats.get(num);
                HashMap map = new HashMap();
                for (Map.Entry entry : concurrentHashMap.entrySet()) {
                    map.put("Network", (String) entry.getKey());
                    map.put("volte_status", ((Boolean) entry.getValue()).booleanValue() ? "1" : "0");
                }
                if (ModemProvider.IS_QCOM) {
                    map.put("mbn_sw", SystemProperties.get("persist.radio.mbn_sw_sub" + iIntValue));
                }
                map.put("numeric", miui.telephony.TelephonyManager.getDefault().getSimOperatorForSlot(iIntValue));
                Utils.logD("VolteRegisterMonitor", "recordVolteGlobalizationStats data=" + map);
                MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "volte_globalization", map);
            }
        }
        this.mVolteGlobalizationStats.clear();
    }

    public void recordVolteGlobalizationVolteSwitch(int i, Phone phone, String str) {
        boolean zIsEnhanced4gLteModeSettingEnabledByUser = MiuiPhoneUtils.isEnhanced4gLteModeSettingEnabledByUser(phone);
        if (((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getGlobalizationCarrierConfigMap().containsKey(str)) {
            HashMap map = new HashMap();
            map.put("volte_switch" + i, zIsEnhanced4gLteModeSettingEnabledByUser ? "1" : "0");
            map.put("volte_status_switch_on" + i, phone.isImsRegistered() ? "1" : "0");
            map.put("numeric_volte_switch_on" + i, str);
            Utils.logD("VolteRegisterMonitor", "recordVolteGlobalizationVolteSwitch data=" + map);
            MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "volte_globalization_switch", map);
        }
    }
}
