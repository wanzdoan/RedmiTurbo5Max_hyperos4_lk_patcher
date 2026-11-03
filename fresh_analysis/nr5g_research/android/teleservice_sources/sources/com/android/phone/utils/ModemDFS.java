package com.android.phone.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.DisconnectCause;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;
import com.android.phone.FiveGServiceInfo;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.RilHook;
import com.android.phone.dfs.MiuiTelephonyDFS;
import com.android.phone.module.ITelephonyModule;
import com.miui.misight.MiEvent;
import com.miui.misight.MiSight;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.ModemServiceMoniter;
import com.xiaomi.mirilhook.MiRilHook;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class ModemDFS {
    private static long mLastNetRejTime = 0;
    private static int mModemStatus = 1;
    static Map netRejCause = Map.ofEntries(Map.entry(2, "simCardAge"), Map.entry(11, "simCardAge"), Map.entry(20, "simCardPermissionError"), Map.entry(21, "simCardPermissionError"), Map.entry(22, "simCardPermissionError"), Map.entry(23, "5GNetworkError"), Map.entry(27, "simCardNotSupport"), Map.entry(36, "simCardAge"), Map.entry(95, "mobileNetworkError"), Map.entry(96, "mobileNetworkError"), Map.entry(97, "mobileNetworkError"), Map.entry(99, "mobileNetworkError"), Map.entry(111, "mobileNetworkError"));
    private static ModemDFS sInstance;
    private final String DMF_CMD_HEADER;
    private int mAutoCaptureLogMask;
    private ModemDfsHandler mDfsHandler;
    private final SimpleDateFormat mDumpTimestampFormat;
    private final BroadcastReceiver mIntentReceiver;
    private volatile boolean mIsAutoLogPropVerified;
    private final ModemUtils mModemUtil;
    private boolean[] mPreImsState;
    private ServiceState[] mServiceState;
    private boolean[] mSimLoaded;
    private TelephonyCallBack mTelephonyCallBack;
    private final HandlerThread mWorkThread;
    private TelephonyManager mTelephonyManager = null;
    private Context mContext = PhoneGlobals.getInstance();
    private MiuiTelephonyDFS miuiTelephonyDFS = MiuiTelephonyDFS.getInstance();

    /* JADX INFO: Access modifiers changed from: private */
    public void startCircleStatistic() {
    }

    public static ModemDFS getInstance() {
        return sInstance;
    }

    private ModemDFS() {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mServiceState = new ServiceState[i];
        this.mSimLoaded = new boolean[i];
        this.mDumpTimestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        this.DMF_CMD_HEADER = "AT+EDMFAPP=20104,";
        this.mIsAutoLogPropVerified = false;
        this.mAutoCaptureLogMask = -1;
        this.mPreImsState = new boolean[i];
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.utils.ModemDFS.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                Log.d("ModemDFS", "mIntentReceiver action=".concat(action));
                if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                    Log.d("ModemDFS", "mIntentReceiver action=".concat(action));
                    int intExtra = intent.getIntExtra("phone", SubscriptionManager.INVALID_PHONE_ID);
                    boolean zEquals = "LOADED".equals(intent.getStringExtra("ss"));
                    ModemDFS modemDFS = ModemDFS.this;
                    if (zEquals) {
                        modemDFS.mSimLoaded[intExtra] = true;
                        return;
                    }
                    modemDFS.mSimLoaded[intExtra] = false;
                    Log.d("ModemDFS", "SIM Unloaded");
                    ModemDFS.this.removeMessageSlot(intExtra);
                    return;
                }
                if (action.equals("android.intent.action.ACTION_IMS_REGISTED")) {
                    int intExtra2 = intent.getIntExtra("phone", SubscriptionManager.INVALID_PHONE_ID);
                    if (intExtra2 < 0 || intExtra2 > MiuiPhoneUtils.PHONE_COUNT) {
                        Log.d("ModemDFS", "INVALID_PHONE_ID" + intExtra2);
                        return;
                    }
                    boolean booleanExtra = intent.getBooleanExtra(MEModuleSignalT1s.EXTRA_STATE, false);
                    Log.d("ModemDFS", "ims register states:" + booleanExtra + " phoneId:" + intExtra2);
                    if (!booleanExtra && ModemDFS.this.mPreImsState[intExtra2]) {
                        ModemDFS.this.CheckImsRegister(intExtra2, booleanExtra);
                    }
                    ModemDFS.this.mPreImsState[intExtra2] = booleanExtra;
                }
            }
        };
        int integer = (ModemProvider.IS_XRING || ModemProvider.IS_QCOM) ? this.mContext.getResources().getInteger(R.integer.config_hydra_init_delay) : 0;
        HandlerThread handlerThread = new HandlerThread("ModemDFS", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        ModemDfsHandler modemDfsHandler = new ModemDfsHandler(handlerThread.getLooper());
        this.mDfsHandler = modemDfsHandler;
        modemDfsHandler.sendEmptyMessageDelayed(1, integer);
        Log.d("ModemDFS", "ModemDFS created, delayTime:" + integer);
        this.mModemUtil = ModemUtils.init(this.mContext);
    }

    public static ModemDFS make() {
        if (sInstance == null) {
            sInstance = new ModemDFS();
        }
        Log.d("ModemDFS", "ModemDFS created");
        return sInstance;
    }

    public class ModemDfsHandler extends CountableHandler {
        public ModemDfsHandler(Looper looper) {
            super(looper);
        }

        @Override // com.android.phone.utils.CountableHandler, android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i == 1) {
                ModemDFS.this.init();
                return;
            }
            if (i == 2) {
                ModemDFS.this.startCircleStatistic();
                return;
            }
            if (i == 4) {
                ModemDFS.this.updateOOSstatus(4);
                return;
            }
            if (i == 5) {
                ModemDFS.this.updateOOSstatus(5);
                return;
            }
            if (i == 21) {
                ModemDFS.this.processCallDisconnectEvent((Connection) ((AsyncResult) message.obj).result);
                return;
            }
            if (i != 22) {
                switch (i) {
                    case 10:
                        ModemDFS.this.updateRfDeviceStatus((Map) message.obj);
                        break;
                    case 11:
                        ModemDFS.this.updateRfMainChipStatus((Map) message.obj);
                        break;
                    case 12:
                        ServiceState serviceState = (ServiceState) message.obj;
                        ModemDFS.this.monitorOosState(message.arg1, serviceState);
                        break;
                    case 13:
                        ModemDFS.this.updateRfMainChipStatusForMtk((Map) message.obj);
                        break;
                }
                return;
            }
            ModemDFS.this.updateRfTxInvalidPaForMtk((Map) message.obj);
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_IMS_REGISTED");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    public static void sendEvent(MiEvent miEvent) {
        MiSight.sendEvent(miEvent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mPreImsState[i] = false;
        }
        CallManager.getInstance(this.mContext).registerForDisconnect(this.mDfsHandler, 21, (Object) null);
        getInitState();
        registerTelephonyCallBack();
        startCircleStatistic();
        registerReceiver();
        if (ModemProvider.IS_QCOM) {
            doDecisionAtStart();
        } else if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
            DoDecisionAtStartForMtk();
            enableMtkModemDfs(true);
        }
    }

    public void recordDFSEventStatistic(int i, ConcurrentHashMap concurrentHashMap) {
        if (concurrentHashMap == null || concurrentHashMap.isEmpty()) {
            return;
        }
        Object[] objArr = new Object[concurrentHashMap.size() * 2];
        int i2 = 0;
        for (Map.Entry entry : concurrentHashMap.entrySet()) {
            int i3 = i2 + 1;
            objArr[i2] = entry.getKey();
            i2 += 2;
            objArr[i3] = entry.getValue();
        }
        try {
            sendEvent(MiSight.constructEvent(i, objArr));
        } catch (Exception e) {
            com.android.phone.statistics.utils.Utils.logD("ModemDFS", "recordDFSEventStatistic exception:" + e);
        }
    }

    private void enableMtkModemDfs(boolean z) {
        int i = z ? 100 : 101;
        if (MiuiPhoneUtils.isSupportedDFS()) {
            ATCommandUtils.getInstance().atCmdRunSync(0, new String[]{"AT+EDMFAPP=20104," + i, "+EDMFAPP"}, null);
            Log.d("ModemDFS", "enableMtkModemDfs enable = " + z);
        }
    }

    private TelephonyManager getTelephonyManager(int i) {
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (telephonyManager != null) {
            return telephonyManager;
        }
        TelephonyManager telephonyManager2 = (TelephonyManager) this.mContext.getSystemService("phone");
        this.mTelephonyManager = telephonyManager2;
        return telephonyManager2;
    }

    private void getInitState() {
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mServiceState[i] = new ServiceState();
        }
    }

    private void registerTelephonyCallBack() {
        if (this.mTelephonyCallBack == null) {
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void monitorOosState(int i, ServiceState serviceState) {
        if (this.mSimLoaded[i]) {
            Log.d("ModemDFS", "processServiceStateChangeEvent phoneId=" + i);
            if (serviceState == null || !android.telephony.SubscriptionManager.isValidSlotIndex(i) || serviceState.getIsManualSelection()) {
                return;
            }
            monitorOOSChange(i, serviceState);
        }
    }

    private void monitorOOSChange(int i, ServiceState serviceState) {
        if (this.mServiceState[i] == null || serviceState == null) {
            Log.d("ModemDFS", "monitorNwChange invalid params!");
            return;
        }
        int voiceRegState = serviceState.getVoiceRegState();
        int voiceRegState2 = this.mServiceState[i].getVoiceRegState();
        Log.d("ModemDFS", "newVoiceReg:" + voiceRegState + ",oldVoiceReg=" + voiceRegState2);
        boolean z = false;
        boolean z2 = voiceRegState2 != 0 && voiceRegState == 0;
        if (voiceRegState2 == 0 && voiceRegState != 0) {
            z = true;
        }
        if (z2) {
            Log.d("ModemDFS", " sub:" + i + " voiceRegistered");
            removeMessageSlot(i);
        }
        if (z && this.mSimLoaded[i]) {
            Log.d("ModemDFS", " sub:" + i + " Deregistered");
            sendMessageDelayedSlot(i);
            netReject(i);
            modemOffline(i);
        }
        this.mServiceState[i] = serviceState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeMessageSlot(int i) {
        if (i == 0) {
            if (this.mDfsHandler.hasMessages(4)) {
                this.mDfsHandler.removeMessageWithCount(4);
            }
        } else if (i == 1 && this.mDfsHandler.hasMessages(5)) {
            this.mDfsHandler.removeMessageWithCount(5);
        }
    }

    private void sendMessageDelayedSlot(int i) {
        if (i == 0) {
            if (this.mDfsHandler.hasMessages(4)) {
                return;
            }
            this.mDfsHandler.sendEmptyMessageDelayed(4, 600000L);
        } else {
            if (i != 1 || this.mDfsHandler.hasMessages(5)) {
                return;
            }
            this.mDfsHandler.sendEmptyMessageDelayed(5, 600000L);
        }
    }

    private void DoDecisionAtStartForMtk() {
        HashMap map = new HashMap();
        String[] strArr = {"AT+EDMFAPP=20100,326", "+EDMFAPP"};
        String[] strArr2 = {"AT+EDMFAPP=20100,486", "+EDMFAPP"};
        Object[] objArr = new Object[1];
        Object[] objArr2 = new Object[1];
        if (ATCommandUtils.getInstance() != null) {
            ATCommandUtils.getInstance().atCmdRunSync(0, strArr, objArr);
            ATCommandUtils.getInstance().atCmdRunSync(0, strArr2, objArr2);
        }
        String[] strArr3 = (String[]) objArr[0];
        String[] strArr4 = (String[]) objArr2[0];
        if (strArr3 != null && strArr3.length >= 1) {
            for (int i = 0; i < strArr3.length; i++) {
                Log.d("ModemDFS", "DMFAPP id:326resp = " + strArr3[i]);
                String[] strArrSplit = strArr3[i].split(",");
                int iIntValue = Integer.valueOf(strArrSplit[1]).intValue();
                Log.d("ModemDFS", "doDecisionAtStart mainChipErrNum: " + iIntValue);
                StringBuffer stringBuffer = new StringBuffer();
                if (iIntValue != 0) {
                    int i2 = 0;
                    int i3 = 2;
                    while (i2 < iIntValue) {
                        stringBuffer.append(strArrSplit[i3] + ", ");
                        i2++;
                        i3++;
                    }
                    map.put("rfMainChipErrNum", Integer.toString(iIntValue));
                    map.put("rfMainChipErrIns", stringBuffer.toString());
                    this.mDfsHandler.obtainMessage(13, map).sendToTarget();
                }
            }
        }
        if (strArr4 == null || strArr4.length < 1) {
            return;
        }
        for (int i4 = 0; i4 < strArr4.length; i4++) {
            Log.d("ModemDFS", "DMFAPP id:486resp = " + strArr4[i4]);
            String[] strArrSplit2 = strArr4[i4].split(",");
            Log.d("ModemDFS", "doDecisionAtStart TxInvalid version: " + Integer.valueOf(strArrSplit2[1]).intValue());
            StringBuffer stringBuffer2 = new StringBuffer();
            int i5 = 2;
            int i6 = 2;
            while (i5 < strArrSplit2.length) {
                stringBuffer2.append(strArrSplit2[i6] + ", ");
                i5++;
                i6++;
            }
            map.put("txInvalidIns", stringBuffer2.toString());
            this.mDfsHandler.obtainMessage(22, map).sendToTarget();
        }
    }

    /* JADX WARN: Code duplicated, block: B:24:0x0079  */
    private void doDecisionAtStart() {
        boolean z;
        boolean z2;
        HashMap map = new HashMap();
        RilHook rilHookInit = RilHook.init(this.mContext);
        boolean z3 = false;
        if (rilHookInit != null) {
            String oemNvItem = rilHookInit.readOemNvItem(6860);
            Log.d("ModemDFS", "value: " + oemNvItem);
            if (oemNvItem != null) {
                String[] strArrSplit = oemNvItem.split(";");
                if (strArrSplit.length <= 10 || "0".equals(strArrSplit[10])) {
                    z2 = false;
                } else {
                    map.put("rfDeviceStatus1", strArrSplit[10]);
                    z2 = true;
                }
                if (strArrSplit.length > 11 && !"0".equals(strArrSplit[11])) {
                    map.put("rfDeviceStatus2", strArrSplit[11]);
                    z2 = true;
                }
                if (strArrSplit.length <= 1 || "0".equals(strArrSplit[1])) {
                    z = false;
                } else {
                    map.put("rfMainStatus", strArrSplit[1]);
                    z = true;
                }
                z3 = z2;
            } else {
                z = false;
            }
        } else {
            z = false;
        }
        if (z3) {
            this.mDfsHandler.obtainMessage(10, map).sendToTarget();
        }
        if (z) {
            this.mDfsHandler.obtainMessage(11, map).sendToTarget();
        }
        if (!SystemProperties.get("persist.vendor.radio.stack_id_0", "0").equals(SystemProperties.get("persist.vendor.radio.stack_id_1", "1"))) {
            Log.d("ModemDFS", "normal sim stack id ");
        } else {
            sendEvent(MiSight.constructEvent(915400001, new Object[]{"simStackId", 1}));
            Log.d("ModemDFS", "sim stack id repeat: 1");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRfTxInvalidPaForMtk(Map map) {
        if (map == null) {
            return;
        }
        String str = (String) map.get("txInvalidIns");
        MiEvent miEventConstructEvent = MiSight.constructEvent(915600003, new Object[]{"txInvalidIns", str});
        Log.d("ModemDFS", "txInvalidIns:" + str);
        sendEvent(miEventConstructEvent);
    }

    public void getRfFatalDeviceStatusForQcom() {
        boolean z;
        RilHook rilHookInit = RilHook.init(this.mContext);
        if (rilHookInit != null) {
            String oemNvItem = rilHookInit.readOemNvItem(6860);
            Log.d("ModemDFS", "value: " + oemNvItem);
            if (oemNvItem != null) {
                HashMap map = new HashMap();
                String[] strArrSplit = oemNvItem.split(";");
                boolean z2 = false;
                if (strArrSplit.length <= 10 || "0".equals(strArrSplit[10])) {
                    z = false;
                } else {
                    map.put("rfDeviceStatus1", strArrSplit[10]);
                    z = true;
                }
                if (strArrSplit.length > 11 && !"0".equals(strArrSplit[11])) {
                    map.put("rfDeviceStatus2", strArrSplit[11]);
                }
                if (strArrSplit.length > 1 && !"0".equals(strArrSplit[1])) {
                    map.put("rfMainStatus", strArrSplit[1]);
                    z2 = true;
                }
                if (z) {
                    this.mDfsHandler.obtainMessage(10, map).sendToTarget();
                }
                if (z2) {
                    this.mDfsHandler.obtainMessage(11, map).sendToTarget();
                }
            }
        }
    }

    public void reportTxFbrxFault(Bundle bundle) {
        String string;
        if (bundle == null) {
            com.android.phone.statistics.utils.Utils.logE("ModemDFS", "reportTxFbrxFault: null bundle");
            return;
        }
        ArrayList<String> stringArrayList = bundle.getStringArrayList("key_name_list");
        if (stringArrayList == null || stringArrayList.isEmpty()) {
            com.android.phone.statistics.utils.Utils.logE("ModemDFS", "reportTxFbrxFault: null or empty key_name_list");
            return;
        }
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        for (int i = 0; i < stringArrayList.size(); i++) {
            String str = stringArrayList.get(i);
            if (str != null && (string = bundle.getString(str)) != null) {
                concurrentHashMap.put(str, string);
            }
        }
        if (concurrentHashMap.isEmpty()) {
            com.android.phone.statistics.utils.Utils.logE("ModemDFS", "reportTxFbrxFault: all field values are null, skip reporting");
            return;
        }
        recordDFSEventStatistic(915600003, concurrentHashMap);
        com.android.phone.statistics.utils.Utils.logD("ModemDFS", "reportTxFbrxFault reported with " + concurrentHashMap.size() + " fields");
    }

    public void reportRfDeviceStatusDbg() {
        HashMap map = new HashMap();
        map.put("rfDeviceStatus1", "1");
        map.put("rfDeviceStatus2", "2");
        updateRfDeviceStatus(map);
    }

    public void reportRfMainChipStatusDbg() {
        HashMap map = new HashMap();
        map.put("rfMainStatus", "1");
        updateRfMainChipStatus(map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRfDeviceStatus(Map map) {
        int i;
        int i2;
        if (map != null) {
            i = Integer.parseInt((String) map.getOrDefault("rfDeviceStatus1", "0"));
            i2 = Integer.parseInt((String) map.getOrDefault("rfDeviceStatus2", "0"));
        } else {
            i = -1;
            i2 = -1;
        }
        sendEvent(MiSight.constructEvent(915600001, new Object[]{"rfDeviceStatus1", Integer.valueOf(i), "rfDeviceStatus2", Integer.valueOf(i2)}));
        Log.d("ModemDFS", "rfDeviceStatus1:" + i + " rfDeviceStatus2:" + i2);
    }

    private synchronized void dfsEventForMtb(int i) {
        try {
            if (this.mContext == null) {
                return;
            }
            if (!this.mIsAutoLogPropVerified) {
                this.mAutoCaptureLogMask = SystemProperties.getInt(ModemServiceMoniter.PROPERTY_AUTO_CAPTURE_LOG_MASK, -1);
                this.mIsAutoLogPropVerified = true;
            }
            if (1 == this.mAutoCaptureLogMask) {
                Intent intent = new Intent("android.intent.action.DFS_MODEM_EVENT_FOR_MTB");
                intent.setFlags(16777216);
                intent.putExtra("eventId", i);
                intent.setPackage("com.xiaomi.mtb");
                this.mContext.sendBroadcast(intent);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRfMainChipStatus(Map map) {
        int i = map != null ? Integer.parseInt((String) map.getOrDefault("rfMainStatus", "0")) : -1;
        sendEvent(MiSight.constructEvent(915600002, new Object[]{"rfMainDeviceStatus", Integer.valueOf(i)}));
        dfsEventForMtb(915600002);
        Log.d("ModemDFS", "rfMainDeviceStatus:" + i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRfMainChipStatusForMtk(Map map) {
        if (map == null) {
            return;
        }
        int i = Integer.parseInt((String) map.get("rfMainChipErrNum"));
        String str = (String) map.get("rfMainChipErrIns");
        int i2 = 0;
        if (str != null) {
            String[] strArrSplit = str.split(",");
            if (strArrSplit.length > 0) {
                i2 = Integer.parseInt(strArrSplit[0].trim());
            }
        }
        MiEvent miEventConstructEvent = MiSight.constructEvent(915600002, new Object[]{"rfMainDeviceStatus", Integer.valueOf(i2)});
        Log.d("ModemDFS", "rfMainChipNum:" + i + " rfMainChipInsId: " + str + " rfMainDeviceStatus: " + i2);
        sendEvent(miEventConstructEvent);
        dfsEventForMtb(915600002);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateOOSstatus(int i) {
        int i2 = 0;
        if (i != 4 && i == 5) {
            i2 = 1;
        }
        sendEvent(MiSight.constructEvent(915300004, new Object[]{"oosSubId", Integer.valueOf(i2)}));
        Log.d("ModemDFS", "OOS subID " + i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void CheckImsRegister(int i, boolean z) {
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        if (defaultDataSlotId != i) {
            return;
        }
        TelephonyManager telephonyManager = getTelephonyManager(i);
        this.mTelephonyManager = telephonyManager;
        if (telephonyManager == null) {
            return;
        }
        int voiceNetworkType = telephonyManager.getVoiceNetworkType();
        String networkType = getNetworkType(defaultDataSlotId);
        Log.d("ModemDFS", "ddsPhoneId=" + defaultDataSlotId + "\trat:" + networkType);
        if (MiuiPhoneUtils.isAirPlaneMode()) {
            return;
        }
        if ((networkType.equals("4G") || networkType.equals("NSA") || networkType.equals("SA")) && !z) {
            sendEvent(MiSight.constructEvent(915100007, new Object[]{"subId", Integer.valueOf(i), "isRegistrationIms", Boolean.valueOf(z), "voiceNetworkType", TelephonyManager.getNetworkTypeName(voiceNetworkType), "ddsPhoneId", Integer.valueOf(defaultDataSlotId), "DataRegisterRAT", networkType}));
            Log.d("ModemDFS", "LTE/NR register to volte fail: ".concat("915100007: isRegistrationIms = " + z + ", DataRegisterRAT = " + networkType));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCallDisconnectEvent(Connection connection) {
        int phoneId;
        Phone phone = (connection == null || connection.getCall() == null) ? null : connection.getCall().getPhone();
        if (phone != null && phone.getPhoneType() == 5 && (phoneId = phone.getPhoneId()) >= 0 && phoneId < MiuiPhoneUtils.PHONE_COUNT) {
            int callDisconnectCause = getCallDisconnectCause(connection);
            boolean zIsIncoming = connection.isIncoming();
            StringBuilder sb = new StringBuilder("process call disconnect event");
            sb.append(zIsIncoming ? "MT" : "MO");
            sb.append("phoneId:");
            sb.append(phoneId);
            sb.append("cause:");
            sb.append(DisconnectCause.toString(callDisconnectCause));
            Log.d("ModemDFS", sb.toString());
            if (callDisconnectCause == 2 || callDisconnectCause == 3 || callDisconnectCause == 4 || callDisconnectCause == 37 || callDisconnectCause == 1 || callDisconnectCause == 18 || callDisconnectCause == 21 || callDisconnectCause == 17 || callDisconnectCause == 16 || callDisconnectCause == 7) {
                return;
            }
            ArrayList arrayList = new ArrayList();
            if (ModemProvider.IS_QCOM) {
                getImsSipMessageForQcom(arrayList, phoneId);
            } else if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
                enableMtkModemDfs(false);
                getImsSipMessageForMtk(arrayList, phoneId);
                enableMtkModemDfs(true);
            }
            networkTempIssueJudge(arrayList, phoneId);
            poorSignalCauseByeMessageSendFailJudge(arrayList, zIsIncoming, phoneId);
            MoNotReceived200OkMessageJudge(arrayList, zIsIncoming, phoneId);
        }
    }

    private void getImsSipMessageForQcom(List list, int i) {
        ModemUtils modemUtils = this.mModemUtil;
        if (modemUtils == null) {
            Log.w("ModemDFS", "mModemUtil is NULL");
            return;
        }
        try {
            ByteBuffer byteBuffer = (ByteBuffer) modemUtils.callQcrilHookGetObject("getLteImsSipMessageForDfsList", -1);
            if (byteBuffer != null && byteBuffer.capacity() != 0) {
                byteBuffer.position(0);
                byte b = byteBuffer.get();
                short s = byteBuffer.getShort();
                Log.d("ModemDFS", "ims sip message version:" + ((int) b) + "count:" + ((int) s));
                for (int i2 = 0; i2 < s; i2++) {
                    HashMap map = new HashMap();
                    String str = this.mDumpTimestampFormat.format(new Date(byteBuffer.getLong()));
                    int i3 = byteBuffer.getInt();
                    byte b2 = byteBuffer.get();
                    short s2 = byteBuffer.getShort();
                    short s3 = byteBuffer.getShort();
                    if (i3 == i) {
                        map.put("timestamp", str);
                        map.put("sub", String.valueOf(i3));
                        map.put("direction", String.valueOf((int) b2));
                        map.put("msg_id", String.valueOf((int) s2));
                        map.put("resp_code", String.valueOf((int) s3));
                        list.add(map);
                    }
                }
                return;
            }
            Log.d("ModemDFS", "IMS SIP PACKET IS NULL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getImsSipMessageForMtk(List list, int i) {
        String[] strArr = {"AT+EDMFAPP=20104,104", "+EDMFAPP"};
        Object[] objArr = new Object[1];
        if (ATCommandUtils.getInstance() != null) {
            boolean z = true;
            int i2 = 0;
            while (i2 < 5 && z && ATCommandUtils.getInstance().atCmdRunSync(i, strArr, objArr)) {
                Object obj = objArr[0];
                String[] strArr2 = (String[]) obj;
                boolean z2 = strArr2 != null && strArr2.length > 10;
                modemDfsInfoGetForMtk(MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT, obj, list, i);
                i2++;
                z = z2;
            }
        }
        Log.d("ModemDFS", "modemDfsInfoGetForMtk dmfId = 104 list = " + list);
    }

    private void networkTempIssueJudge(List list, int i) {
        boolean z = false;
        String str = "";
        boolean z2 = false;
        for (int i2 = 0; i2 < list.size(); i2++) {
            int i3 = Integer.parseInt((String) ((HashMap) list.get(i2)).get("sub"));
            if (i3 == i) {
                int i4 = Integer.parseInt((String) ((HashMap) list.get(i2)).get("direction"));
                int i5 = Integer.parseInt((String) ((HashMap) list.get(i2)).get("msg_id"));
                int i6 = Integer.parseInt((String) ((HashMap) list.get(i2)).get("resp_code"));
                String str2 = (String) ((HashMap) list.get(i2)).get("timestamp");
                if ((ModemProvider.IS_QCOM && i4 == 0 && i5 == 2) || ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && i4 == 1 && i5 == 1)) {
                    if (i6 == 480) {
                        str = str + "DL: datetime: " + str2 + " direction:0 msg_id:2 resp_code:" + i6 + " sub:" + i3 + "\n";
                        z2 = true;
                    } else if (i6 == 503) {
                        str = str + "DL: datetime: " + str2 + " direction:0 msg_id:2 resp_code:" + i6 + " sub:" + i3 + "\n";
                        z = true;
                    }
                }
                if (z2 || z) {
                    sendEvent(MiSight.constructEvent(915200007, new Object[]{"subId", Integer.valueOf(i), "signaling", str}));
                    Log.d("ModemDFS", str);
                    return;
                }
            }
        }
    }

    private void poorSignalCauseByeMessageSendFailJudge(List list, boolean z, int i) {
        int i2 = !z ? 1 : 0;
        int rsrp = this.miuiTelephonyDFS.getRsrp(i);
        int snr = this.miuiTelephonyDFS.getSnr(i);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            int i5 = Integer.parseInt((String) ((HashMap) list.get(i4)).get("msg_id"));
            int i6 = Integer.parseInt((String) ((HashMap) list.get(i4)).get("sub"));
            int i7 = Integer.parseInt((String) ((HashMap) list.get(i4)).get("direction"));
            if ((ModemProvider.IS_QCOM && i5 == 6 && i6 == i && i7 == i2) || ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && i5 == 7 && i6 == i && i7 == z)) {
                i3++;
                Log.d("ModemDFS", "direction: " + i7 + "send bye");
            }
        }
        if ((rsrp < -115 || snr < 0) && i3 > 4) {
            sendEvent(MiSight.constructEvent(915200008, new Object[]{"subId", Integer.valueOf(i), "isMt", Boolean.valueOf(z), "direction", Integer.valueOf(i2), "snedTimes", Integer.valueOf(i3), "rsrp", Integer.valueOf(rsrp), "snr", Integer.valueOf(snr)}));
            Log.d("ModemDFS", "SIP BYE SEND FAILURE".concat("915200008:direction:" + i2 + " snedTimes:" + i3 + " rsrp:" + rsrp + " snr:" + snr));
        }
    }

    private void MoNotReceived200OkMessageJudge(List list, boolean z, int i) {
        if (z) {
            int rsrp = this.miuiTelephonyDFS.getRsrp(i);
            int snr = this.miuiTelephonyDFS.getSnr(i);
            int i2 = 0;
            for (int i3 = 0; i3 < list.size(); i3++) {
                int i4 = Integer.parseInt((String) ((HashMap) list.get(i3)).get("msg_id"));
                int i5 = Integer.parseInt((String) ((HashMap) list.get(i3)).get("sub"));
                int i6 = Integer.parseInt((String) ((HashMap) list.get(i3)).get("direction"));
                int i7 = Integer.parseInt((String) ((HashMap) list.get(i3)).get("resp_code"));
                if ((ModemProvider.IS_QCOM && i4 == 2 && i5 == i && i6 == 1 && i7 == 200) || ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && i4 == 1 && i5 == i && i6 == 0 && i7 == 200)) {
                    i2++;
                    Log.d("ModemDFS", "direction: " + i6 + "send 200 OK");
                }
            }
            if ((rsrp < -115 || snr < 0) && i2 > 4) {
                sendEvent(MiSight.constructEvent(915200009, new Object[]{"subId", Integer.valueOf(i), "isMt", Boolean.valueOf(z), "snedTimes", Integer.valueOf(i2), "rsrp", Integer.valueOf(rsrp), "snr", Integer.valueOf(snr)}));
                Log.d("ModemDFS", "915200009:isMt:" + z + " snedTimes:" + i2 + " rsrp:" + rsrp + " snr:" + snr);
            }
        }
    }

    private int getCallDisconnectCause(Connection connection) {
        int preciseDisconnectCause = connection.getPreciseDisconnectCause();
        if (preciseDisconnectCause != 0) {
            return preciseDisconnectCause;
        }
        if (connection instanceof ImsPhoneConnection) {
            return connection.getDisconnectCause() + 10000;
        }
        return connection.getDisconnectCause() + 20000;
    }

    public void imsErrorCausedByMBN(String str, int i) {
        sendEvent(MiSight.constructEvent(915100010, new Object[]{"subId", Integer.valueOf(i), "isRegistrationIms", Boolean.FALSE, "errCodeType", str}));
        Log.d("ModemDFS", "ims error caused by MBN, nv_item:" + str + " send error code");
        sendEvent(MiSight.constructEvent(815100010, new Object[]{"sudid", "phoneId", "RecoveryResult", "ture", "ProblemCode", 915100010}));
    }

    private void netReject(int i) {
        List listNasRejectBufGetForMtk;
        List listNasRejectBufGetForMtk2;
        if (System.currentTimeMillis() - mLastNetRejTime < 7200000) {
            return;
        }
        mLastNetRejTime = System.currentTimeMillis();
        if (ModemProvider.IS_QCOM) {
            listNasRejectBufGetForMtk = lteNasRejectBufGet();
            listNasRejectBufGetForMtk2 = nrNasRejectBufGet();
        } else if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
            enableMtkModemDfs(false);
            listNasRejectBufGetForMtk = nasRejectBufGetForMtk(102, i);
            listNasRejectBufGetForMtk2 = nasRejectBufGetForMtk(103, i);
            enableMtkModemDfs(true);
        } else {
            listNasRejectBufGetForMtk = null;
            listNasRejectBufGetForMtk2 = null;
        }
        if (listNasRejectBufGetForMtk == null && listNasRejectBufGetForMtk2 == null) {
            Log.d("ModemDFS", "nr && lte nas buf is null");
            return;
        }
        if (listNasRejectBufGetForMtk2 != null && listNasRejectBufGetForMtk == null) {
            checkNetIssueCausedByLteSARejected(listNasRejectBufGetForMtk2, i);
            return;
        }
        if (listNasRejectBufGetForMtk2 == null && listNasRejectBufGetForMtk != null) {
            checkNetIssueCausedByLteSARejected(listNasRejectBufGetForMtk, i);
            return;
        }
        ArrayList arrayList = new ArrayList(listNasRejectBufGetForMtk);
        arrayList.addAll(listNasRejectBufGetForMtk2);
        checkNetIssueCausedByLteSARejected(arrayList, i);
    }

    private List lteNasRejectBufGet() {
        ModemUtils modemUtils = this.mModemUtil;
        if (modemUtils == null) {
            Log.d("ModemDFS", "mModemUtil is null");
            return null;
        }
        try {
            ByteBuffer byteBuffer = (ByteBuffer) modemUtils.callQcrilHookGetObject("getLteNasRejectForDfsList", -1);
            if (byteBuffer == null) {
                return null;
            }
            byteBuffer.position(0);
            byte b = byteBuffer.get();
            short s = byteBuffer.getShort();
            Log.d("ModemDFS", "lte nas message version: " + ((int) b) + " count: " + ((int) s));
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < s; i++) {
                HashMap map = new HashMap();
                String str = this.mDumpTimestampFormat.format(new Date(byteBuffer.getLong()));
                byte b2 = byteBuffer.get();
                int unsignedInt = Byte.toUnsignedInt(byteBuffer.get());
                map.put("timestamp", str);
                map.put("cause", String.valueOf((int) b2));
                map.put("msgType", String.valueOf(unsignedInt));
                arrayList.add(map);
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List nrNasRejectBufGet() {
        ModemUtils modemUtils = this.mModemUtil;
        if (modemUtils == null) {
            Log.d("ModemDFS", "mModemUtil is null");
            return null;
        }
        try {
            ByteBuffer byteBuffer = (ByteBuffer) modemUtils.callQcrilHookGetObject("getNr5gNasRejectForDfsList", -1);
            if (byteBuffer == null) {
                return null;
            }
            byteBuffer.position(0);
            byte b = byteBuffer.get();
            short s = byteBuffer.getShort();
            Log.d("ModemDFS", "nr nas message version:" + ((int) b) + "count:" + ((int) s));
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < s; i++) {
                HashMap map = new HashMap();
                String str = this.mDumpTimestampFormat.format(new Date(byteBuffer.getLong()));
                byte b2 = byteBuffer.get();
                if (b == 4) {
                    int unsignedInt = Byte.toUnsignedInt(byteBuffer.get());
                    map.put("timeStamp", str);
                    map.put("cause", String.valueOf((int) b2));
                    map.put("msgType", String.valueOf(unsignedInt));
                } else if (b == 2) {
                    map.put("timeStamp", str);
                    map.put("cause", String.valueOf((int) b2));
                }
                map.put("version", String.valueOf((int) b));
                arrayList.add(map);
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List nasRejectBufGetForMtk(int i, int i2) {
        ArrayList arrayList = new ArrayList();
        String[] strArr = {"AT+EDMFAPP=20104," + i, "+EDMFAPP"};
        Object[] objArr = new Object[1];
        if (ATCommandUtils.getInstance() != null) {
            boolean z = true;
            int i3 = 0;
            while (i3 < 5 && z && ATCommandUtils.getInstance().atCmdRunSync(i2, strArr, objArr)) {
                Object obj = objArr[0];
                String[] strArr2 = (String[]) obj;
                boolean z2 = strArr2 != null && strArr2.length > 10;
                modemDfsInfoGetForMtk(i, obj, arrayList, -1);
                i3++;
                z = z2;
            }
        }
        Log.d("ModemDFS", "dmfId = " + i + "NasMessages:" + arrayList);
        return arrayList;
    }

    private void modemDfsInfoGetForMtk(int i, Object obj, List list, int i2) {
        String[] strArr = (String[]) obj;
        if (strArr.length > 1) {
            for (int i3 = 1; i3 < strArr.length; i3++) {
                Log.d("ModemDFS", "DMFAPP id:" + i + "resp = " + strArr[i3]);
                String[] strArrSplit = strArr[i3].split(",");
                HashMap map = new HashMap();
                String str = strArrSplit[1];
                switch (i) {
                    case 102:
                    case 103:
                        map.put("timestamp", strArrSplit[2]);
                        map.put("cause", strArrSplit[4]);
                        map.put("msgType", strArrSplit[5]);
                        list.add(map);
                        break;
                    case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                        String str2 = strArrSplit[2];
                        String str3 = strArrSplit[3];
                        if (Integer.valueOf(str3).intValue() == i2) {
                            map.put("timestamp", str2);
                            map.put("sub", str3);
                            map.put("direction", strArrSplit[4]);
                            map.put("msg_id", strArrSplit[5]);
                            map.put("resp_code", strArrSplit[6]);
                            list.add(map);
                        }
                        break;
                }
            }
        }
    }

    private void checkNetIssueCausedByLteSARejected(List list, int i) {
        for (int i2 = 1; i2 < list.size(); i2++) {
            int i3 = Integer.parseInt((String) ((HashMap) list.get(i2)).get("cause"));
            int i4 = Integer.parseInt((String) ((HashMap) list.get(i2)).get("msgType"));
            String str = (String) netRejCause.get(Integer.valueOf(i3));
            sendEvent(MiSight.constructEvent(915100008, new Object[]{"SubId", Integer.valueOf(i), "Cause", Integer.valueOf(i3), "ReportType", str, "MsgType", Integer.valueOf(i4)}));
            Log.d("ModemDFS", "4G&5G rejected caused: " + i3 + " msgType: " + i4 + " reportType: " + str);
        }
    }

    private void modemOffline(int i) {
        if (SystemProperties.getInt("persist.radio.operating_mode", -1) != 3) {
            return;
        }
        mModemStatus = 0;
        int phoneCount = this.mTelephonyManager.getPhoneCount();
        int i2 = 0;
        for (int i3 = 0; i3 < phoneCount; i3++) {
            if (this.mTelephonyManager.getSimState(i3) != 1) {
                i2++;
            }
        }
        if (i2 > 1 && i > 0) {
            Log.d("ModemDFS", "two sim only report sub1");
            return;
        }
        sendEvent(MiSight.constructEvent(915100009, new Object[]{"SubId", Integer.valueOf(i), "Status", Integer.valueOf(mModemStatus), "ReportType", "Modem_Offline"}));
        Log.d("ModemDFS", "net rejected cause: modem offline");
        sendEvent(MiSight.constructEvent(815100009, new Object[]{"ProblemCode", 915100009, "RecoveryResult", mModemStatus == 0 ? "false" : "true"}));
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            ModemDFS.this.mDfsHandler.obtainMessage(12, i, 0, serviceState).sendToTarget();
        }
    }

    private static String getNetworkType(int i) {
        int nrConfigType;
        int allocated;
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            return "UNKNOW";
        }
        FiveGServiceInfo fiveGServiceInfo = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getFiveGServiceInfo(i);
        if (fiveGServiceInfo != null) {
            nrConfigType = fiveGServiceInfo.getNrConfigType();
            allocated = fiveGServiceInfo.getAllocated();
        } else {
            nrConfigType = -1;
            allocated = 0;
        }
        if (nrConfigType == 0 && allocated == 1) {
            return "NSA";
        }
        if (nrConfigType == 1) {
            return "SA";
        }
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || phone.getServiceState() == null) {
            return "UNKNOW";
        }
        return TelephonyManagerEx.getDefault().getNetworkClassName(phone.getServiceState().getDataNetworkType());
    }
}
