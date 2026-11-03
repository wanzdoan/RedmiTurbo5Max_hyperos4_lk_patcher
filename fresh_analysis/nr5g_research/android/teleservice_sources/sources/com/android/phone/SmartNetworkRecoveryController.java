package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.CellIdentity;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import miui.telephony.DefaultSimManager;

/* JADX INFO: loaded from: classes.dex */
public class SmartNetworkRecoveryController {
    private static SmartNetworkRecoveryController mInstance;
    private Context mContext;
    public Handler mHandler;
    private boolean mIsSmartNetwokCloudOn;
    private EventHubManager mModemNetworkPerception;
    private NetworkMetricsController mNetworkMetricsController;
    private SmartNetworkRecoveryPolicyController mRecoveryPolicy;
    private TelephonyManager mTelephonyManager;
    private HandlerThread mWorkThread;
    private ModemCauseMap mModemCauseInfoMap = new ModemCauseMap();
    private CauseInfo mApCauseInfo = new CauseInfo(this);
    private long mLastCellChangeTime = 0;
    private long mLastDataStallTime = 0;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.SmartNetworkRecoveryController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            SmartNetworkRecoveryController.log("receive action".concat(action));
            if (action.equals("com.android.phone.intent.action.LINK_RELIABILITY")) {
                SmartNetworkRecoveryController.log("onReceive ACTION_LINK_RELIABILITY ");
                SmartNetworkRecoveryController.this.mHandler.removeMessages(1);
                int intExtra = intent.getIntExtra("netId", -1);
                int intExtra2 = intent.getIntExtra("metricsCause", -1);
                Handler handler = SmartNetworkRecoveryController.this.mHandler;
                handler.sendMessageDelayed(handler.obtainMessage(1, intExtra, intExtra2), 100L);
                return;
            }
            if ("com.android.phone.intent.action.CLOUD_SMART_NETWORK_SWITCH".equals(action)) {
                SmartNetworkRecoveryController.this.mHandler.sendEmptyMessage(4);
            }
        }
    };
    BaseEvent.IEventListener mListener = new BaseEvent.IEventListener() { // from class: com.android.phone.SmartNetworkRecoveryController$$ExternalSyntheticLambda0
        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public final void onEvent(BaseEvent baseEvent) {
            this.f$0.lambda$new$0(baseEvent);
        }
    };

    public static synchronized SmartNetworkRecoveryController getInstance() {
        try {
            if (mInstance == null) {
                mInstance = new SmartNetworkRecoveryController();
            }
        } catch (Throwable th) {
            throw th;
        }
        return mInstance;
    }

    private SmartNetworkRecoveryController() {
        log("SmartNetworkRecoveryController init");
        this.mContext = PhoneGlobals.getInstance();
        HandlerThread handlerThread = new HandlerThread("SmartNetworkRecoveryController", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new RecoveryHandler(this.mWorkThread.getLooper());
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.mRecoveryPolicy = new SmartNetworkRecoveryPolicyController();
        this.mIsSmartNetwokCloudOn = getSmartNetworkSwitch();
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            this.mNetworkMetricsController = NetworkMetricsController.getInstance();
        }
        if (MiuiPhoneUtils.isModemNetworkPerceptionSupported()) {
            this.mModemNetworkPerception = EventHubManager.getInstance();
        }
        registerAllEvents();
    }

    private void registerAllEvents() {
        EventHubManager eventHubManager = this.mModemNetworkPerception;
        if (eventHubManager != null) {
            eventHubManager.registerListener("telephony", new String[]{"dl_high_bler", "ul_high_bler", "dl_congestion", "ul_congestion"}, this.mListener, this.mHandler);
        }
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.LINK_RELIABILITY");
        intentFilter.addAction("com.android.phone.intent.action.CLOUD_SMART_NETWORK_SWITCH");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(BaseEvent baseEvent) {
        Bundle bundle;
        if (!(baseEvent instanceof TelephonyEvent.NetworkEvent) || (bundle = ((TelephonyEvent.NetworkEvent) baseEvent).getBundle()) == null) {
            return;
        }
        int i = bundle.getInt("sub_id", -1);
        int i2 = bundle.getInt("pci", -1);
        if (baseEvent.getSubType().equals("ul_congestion")) {
            processModemPeceptionChange(i, i2, 1);
            return;
        }
        if (baseEvent.getSubType().equals("dl_congestion")) {
            processModemPeceptionChange(i, i2, 2);
        } else if (baseEvent.getSubType().equals("ul_high_bler")) {
            processModemPeceptionChange(i, i2, 4);
        } else if (baseEvent.getSubType().equals("dl_high_bler")) {
            processModemPeceptionChange(i, i2, 8);
        }
    }

    class RecoveryHandler extends Handler {
        RecoveryHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            SmartNetworkRecoveryController.log("handleMessage event:" + message.what);
            int i = message.what;
            if (i == 1) {
                SmartNetworkRecoveryController.this.processLinkReliability(message.arg1, message.arg2);
                return;
            }
            if (i == 2) {
                SmartNetworkRecoveryController.this.processCellLocationChange(message.arg1, (CellIdentity) message.obj);
                return;
            }
            if (i == 3) {
                SmartNetworkRecoveryController.this.mRecoveryPolicy.reset(true);
            } else {
                if (i != 4) {
                    return;
                }
                SmartNetworkRecoveryController smartNetworkRecoveryController = SmartNetworkRecoveryController.this;
                smartNetworkRecoveryController.mIsSmartNetwokCloudOn = smartNetworkRecoveryController.getSmartNetworkSwitch();
            }
        }
    }

    private synchronized void processModemPeceptionChange(int i, int i2, int i3) {
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        log("processModemPeceptionChange subId:" + i + "  pci:" + i2 + " type:" + i3 + " ddsPhoneId: " + defaultDataSlotId);
        if (i == defaultDataSlotId) {
            this.mModemCauseInfoMap.addOrUpdateCause(i3, System.currentTimeMillis());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCellLocationChange(int i, CellIdentity cellIdentity) {
        log("processCellLocationChange phoneId: cellIdentity:" + cellIdentity.toString());
        this.mLastCellChangeTime = System.currentTimeMillis();
        this.mApCauseInfo.reset();
        this.mModemCauseInfoMap.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processLinkReliability(int i, int i2) {
        NetworkMetricsController networkMetricsController;
        if (!this.mIsSmartNetwokCloudOn) {
            log("processLinkReliability:cloud swtich is off");
            return;
        }
        if (i < 100 || !((networkMetricsController = this.mNetworkMetricsController) == null || networkMetricsController.isNecessaryCaseForSmartNetwork())) {
            log("processLinkReliability ignore recovery netId:" + i);
            return;
        }
        if (this.mLastCellChangeTime != 0 && System.currentTimeMillis() - this.mLastCellChangeTime < MirrorDesktopHelper.TIMEOUT_MILLIS) {
            log("processLinkReliability skip : cell changed");
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        this.mApCauseInfo.updateInfo(i2, jCurrentTimeMillis);
        log("processLinkReliability mApCauseInfo:" + this.mApCauseInfo.toString() + " mModemCauseInfoMap:" + this.mModemCauseInfoMap.toString() + " mLastCellChangeTime:" + this.mLastCellChangeTime);
        ModemCauseMap modemCauseMap = this.mModemCauseInfoMap;
        int totalCausesSum = modemCauseMap != null ? modemCauseMap.getTotalCausesSum() : -1;
        long j = this.mLastDataStallTime;
        if (j != 0 && jCurrentTimeMillis - j < 90000) {
            log("processLinkReliability skip : exist penalty time and do nothing!");
            return;
        }
        int networkRecoveryPolicyByCauseCode = this.mRecoveryPolicy.getNetworkRecoveryPolicyByCauseCode(i2, -1, totalCausesSum, -1);
        if (doRecovery(networkRecoveryPolicyByCauseCode)) {
            recordSmartNetworkScheduleStatistic(i2, totalCausesSum, networkRecoveryPolicyByCauseCode);
        }
        this.mLastDataStallTime = jCurrentTimeMillis;
        if (this.mHandler.hasMessages(3)) {
            this.mHandler.removeMessages(3);
        }
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(3), 240000L);
    }

    private boolean doRecovery(int i) {
        if (this.mNetworkMetricsController == null || this.mTelephonyManager == null) {
            return false;
        }
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        ServiceState serviceState = this.mTelephonyManager.getServiceState();
        if (serviceState == null) {
            return false;
        }
        int dataNetworkType = serviceState.getDataNetworkType();
        log("doRecovery policy:" + i + " ddsPhoneId:" + defaultDataSlotId + " dataType:" + dataNetworkType);
        NetworkMetricsController.Scene scene = NetworkMetricsController.Scene.SMART_NETWORK_PECEPTION;
        if (i == 1) {
            this.mNetworkMetricsController.setDataStallOptimization(1, false, scene);
            return true;
        }
        if (i == 2) {
            if (!this.mNetworkMetricsController.isNrNwType(dataNetworkType)) {
                return false;
            }
            this.mNetworkMetricsController.doRecoveryIfNeed(53, defaultDataSlotId, false, scene);
            return true;
        }
        if (i == 3) {
            if (!this.mNetworkMetricsController.isLteNwType(dataNetworkType) || !this.mNetworkMetricsController.isAllowedToRestoreSa(3000010, scene)) {
                return false;
            }
            this.mNetworkMetricsController.doRecoveryIfNeed(MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT, defaultDataSlotId, false, scene);
            return true;
        }
        if (i == 4) {
            this.mNetworkMetricsController.doRecoveryIfNeed(3, defaultDataSlotId, false, scene);
            return true;
        }
        log("undefined recovery policy:" + i);
        return false;
    }

    private void recordSmartNetworkScheduleStatistic(int i, int i2, int i3) {
        if (this.mNetworkMetricsController == null) {
            return;
        }
        HashMap map = new HashMap();
        map.put("CellIdentity", this.mNetworkMetricsController.getDdsLteNrCid());
        map.put("Pci", Integer.toString(this.mNetworkMetricsController.getPci()));
        map.put("SmartNetworkScheduleApCause", Integer.toString(i));
        map.put("SmartNetworkScheduleModemCause", Integer.toString(i2));
        map.put("SmartNetworkSchedulePolicy", Integer.toString(i3));
        map.put("appPackageName", this.mNetworkMetricsController.getCurrentForegroundPackageName());
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_smart_network_schedule_statistic", map);
    }

    public class CauseInfo {
        int cause;
        long lastUpdateTime;

        public CauseInfo(SmartNetworkRecoveryController smartNetworkRecoveryController, int i, long j) {
            this.cause = i;
            this.lastUpdateTime = j;
        }

        public CauseInfo(SmartNetworkRecoveryController smartNetworkRecoveryController) {
            this.cause = 0;
            this.lastUpdateTime = 0L;
        }

        public long getLastUpdateTime() {
            return this.lastUpdateTime;
        }

        public void updateInfo(int i, long j) {
            this.cause = i;
            this.lastUpdateTime = j;
        }

        public void reset() {
            this.cause = 0;
            this.lastUpdateTime = 0L;
        }

        public String toString() {
            return "CauseInfo{cause=" + this.cause + ", lastUpdateTime=" + this.lastUpdateTime + '}';
        }
    }

    public class ModemCauseMap {
        private final Map causeMap = new HashMap();

        public ModemCauseMap() {
        }

        public void addOrUpdateCause(int i, long j) {
            this.causeMap.put(Integer.valueOf(i), new CauseInfo(SmartNetworkRecoveryController.this, i, j));
            SmartNetworkRecoveryController.log("addOrUpdateCause: " + this.causeMap.toString());
        }

        public int getTotalCausesSum() {
            long jCurrentTimeMillis = System.currentTimeMillis();
            Iterator it = this.causeMap.entrySet().iterator();
            while (it.hasNext()) {
                if (jCurrentTimeMillis - ((CauseInfo) ((Map.Entry) it.next()).getValue()).getLastUpdateTime() > 10000) {
                    it.remove();
                }
            }
            SmartNetworkRecoveryController.log("getTotalCausesSum: " + this.causeMap.toString());
            if (this.causeMap.isEmpty()) {
                return -1;
            }
            return this.causeMap.keySet().stream().mapToInt(new CarrierConfigLoader$$ExternalSyntheticLambda0()).sum();
        }

        public void clear() {
            this.causeMap.clear();
        }

        public String toString() {
            return this.causeMap.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getSmartNetworkSwitch() {
        boolean smartNetworkSwitchByCloud = FiveGManagerBase.getSmartNetworkSwitchByCloud(this.mContext);
        log("getSmartNetworkSwitch rst = " + smartNetworkSwitchByCloud);
        return smartNetworkSwitchByCloud;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Log.d("SmartNetworkRecoveryController", str);
    }
}
