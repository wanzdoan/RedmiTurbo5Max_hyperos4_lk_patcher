package com.android.phone.signalMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.NetworkMetricsController;
import com.android.phone.PhoneGlobals;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class SignalMapController {
    private static boolean DEBUG = false;
    private static SignalMapController mSignalMapController;
    private SignalMapHandler mHandler;
    private MiuiCellDetectionStrategy mMiuiCellDetectionStrategy;
    private NetworkMetricsController mNetworkMetricsController;
    private SignalMapCloudController mSMCtrller;
    private TelephonyCallBack mTelephonyCallBack;
    private HandlerThread mWorkThread;
    private SignalStrength[] mSignalStrength = new SignalStrength[MiuiPhoneUtils.PHONE_COUNT];
    private String mCellInfo = "";
    private Map mStats = new HashMap();
    private boolean mIsHighSpeedTrain = false;
    private long mLastRequestTime = 0;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.signalMap.SignalMapController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            SignalMapController.this.log("receive action".concat(action));
            if ("signal.map.intent.debug".equals(action)) {
                MiuiCellDetectionStrategy.DEBUG = true;
                SignalMapController.DEBUG = true;
                SignalMapCloudController.DEBUG = true;
                SignalMapSnsControllerBase.DEBUG = true;
                SignalMapHSTController.DEBUG = true;
                SignalMapHSTCloudController.DEBUG = true;
                return;
            }
            if ("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED".equals(action)) {
                SignalMapController.this.mHandler.obtainMessage(3, intent).sendToTarget();
            }
        }
    };
    private Context mContext = PhoneGlobals.getInstance();

    private SignalMapController() {
        HandlerThread handlerThread = new HandlerThread("SignalMapController", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mHandler = new SignalMapHandler(this.mWorkThread.getLooper());
        this.mMiuiCellDetectionStrategy = MiuiCellDetectionStrategy.getInstance();
        if (MiuiPhoneUtils.isSpecialNetorkOptimizationSupported()) {
            this.mNetworkMetricsController = NetworkMetricsController.getInstance();
        }
        this.mSMCtrller = SignalMapCloudController.getInstance();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mSignalStrength[i] = new SignalStrength();
        }
        registerTelephonyCallBack();
        registerBroadcastReceiver();
    }

    public static SignalMapController getInstance() {
        if (mSignalMapController == null) {
            mSignalMapController = new SignalMapController();
        }
        return mSignalMapController;
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.HST_MODE_STATUS_CHANGED");
        intentFilter.addAction("signal.map.intent.debug");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    class SignalMapHandler extends Handler {
        SignalMapHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            SignalMapController.this.log("msg.what = " + message.what);
            int i = message.what;
            if (i == 1) {
                SignalMapController.this.processCellLocationChanged(message.arg1);
            } else if (i == 2) {
                SignalMapController.this.cellDetectionStrategy(((Integer) message.obj).intValue());
            } else {
                if (i != 3) {
                    return;
                }
                SignalMapController.this.processHstStateChangeEvent((Intent) message.obj);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processHstStateChangeEvent(Intent intent) {
        this.mIsHighSpeedTrain = intent.getBooleanExtra("mIsHstMode", false);
        log("processHstStateChangeEvent, isHighSpeedTrain:" + this.mIsHighSpeedTrain);
    }

    private boolean shouldDetectionCell() {
        return (MiuiPhoneUtils.isWifiConnected() || !MiuiPhoneUtils.isMobileDataEnabled(this.mContext) || MiuiPhoneUtils.isDuringCall() || !isScreenOn() || MiuiPhoneUtils.getTopAppName().equals("com.miui.home")) ? false : true;
    }

    private boolean isSMapBadCell() {
        HashMap commonBlackCellMap = this.mSMCtrller.getCommonBlackCellMap();
        if (this.mNetworkMetricsController == null || commonBlackCellMap == null || commonBlackCellMap.isEmpty()) {
            return false;
        }
        String ddsLteNrCid = this.mNetworkMetricsController.getDdsLteNrCid();
        if (TextUtils.isEmpty(ddsLteNrCid)) {
            log("cid is empty ");
            return false;
        }
        return commonBlackCellMap.containsKey(ddsLteNrCid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCellLocationChanged(int i) {
        if (isSMapBadCell()) {
            recordCount("cellHit");
        }
        if (!shouldDetectionCell()) {
            log("call or wifi, return");
            return;
        }
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            logD("processCellLocationChanged, phoneId = " + i);
            if (this.mHandler.hasMessages(2)) {
                this.mHandler.removeMessages(2);
            }
            SignalMapHandler signalMapHandler = this.mHandler;
            signalMapHandler.sendMessageDelayed(signalMapHandler.obtainMessage(2, Integer.valueOf(i)), MirrorDesktopHelper.TIMEOUT_MILLIS);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cellDetectionStrategy(int i) {
        if (!MiuiPhoneUtils.isValidatePhoneId(i)) {
            log("cellDetectionStrategy invalidate phoneId, return");
            return;
        }
        int dbm = getDbm(this.mSignalStrength[i]);
        if (dbm > -110 || !shouldDetectionCell()) {
            logD("return, good signal quality or shouldDetectionCell is not met, rsrp = " + dbm);
            return;
        }
        String cellInfo = this.mMiuiCellDetectionStrategy.getCellInfo(i);
        if ("exceptionCell".equals(cellInfo)) {
            return;
        }
        boolean zIsBadCell = this.mMiuiCellDetectionStrategy.isBadCell(cellInfo);
        this.mCellInfo = cellInfo;
        if (zIsBadCell) {
            signalOptimizationStrategy(i);
        }
    }

    private void recordCount(String str) {
        Integer num = (Integer) this.mStats.get(str);
        logD("recordCount key=" + str + ", value=" + num);
        Map map = this.mStats;
        if (num == null) {
            map.put(str, 1);
        } else {
            map.put(str, Integer.valueOf(num.intValue() + 1));
        }
    }

    public void reportStatistics() {
        Map map;
        Set signalMapCellBlackList = this.mSMCtrller.getSignalMapCellBlackList(this.mSMCtrller.getCity());
        if (signalMapCellBlackList != null && signalMapCellBlackList.size() != 0 && (map = this.mStats) != null) {
            map.put("memoryCellCnt", Integer.valueOf(signalMapCellBlackList.size()));
        }
        Map map2 = this.mStats;
        if (map2 == null || map2.isEmpty()) {
            return;
        }
        HashMap map3 = new HashMap(this.mStats);
        log("reportStatistics mStats=" + this.mStats);
        this.mStats.clear();
        this.mSMCtrller.clearDistrict();
        MiStatInterfaceUtil.recordObjectEvent("signalMap", "event_singalmap_common", map3);
    }

    private void signalOptimizationStrategy(int i) {
        if (i != getDefaultDataSlotId()) {
            log("phoneId is not dds, return");
            return;
        }
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null) {
            return;
        }
        if (isDualSim(i)) {
            dualSimSwitch(i);
        } else if (isFiveG(phone.getServiceState())) {
            nr2Lte(i);
        }
    }

    private void nr2Lte(int i) {
        if (this.mNetworkMetricsController == null) {
            return;
        }
        StringBuilder sb = new StringBuilder("s[");
        NetworkMetricsController.Scene scene = NetworkMetricsController.Scene.SIGNAL_MAP;
        sb.append(scene.getValue());
        sb.append("]");
        NetworkMetricsController.localLog("DSA", sb.toString());
        recordCount("nr2lte");
        this.mNetworkMetricsController.doRecoveryIfNeed(53, i, false, scene);
    }

    private boolean isDualSim(int i) {
        if (MiuiPhoneUtils.getInsertedSimCount() < 2) {
            log("inserted sim cnt < 2, return");
            return false;
        }
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "smart_dual_sim", 0) == 1 && !isSameServiceProvider(i, getAnotherPhoneId(i))) {
            return true;
        }
        log("smartDualSim off or sameServiceProvider, return");
        return false;
    }

    private void dualSimSwitch(int i) {
        notifySimSwitch(i);
        recordCount("simSwitch");
        log("signalOptimizationStrategy sim switch, phoneId = " + i);
    }

    private static int getAnotherPhoneId(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            return i == 0 ? 1 : 0;
        }
        return -1;
    }

    private boolean isSameServiceProvider(int i, int i2) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && MiuiPhoneUtils.isValidatePhoneId(i2)) {
            Phone phone = PhoneFactory.getPhone(i);
            Phone phone2 = PhoneFactory.getPhone(i2);
            if (phone != null && phone2 != null) {
                String operatorNumeric = phone.getOperatorNumeric();
                String operatorNumeric2 = phone2.getOperatorNumeric();
                log("isSameServicePrvider:" + operatorNumeric + "," + operatorNumeric2);
                if (!TextUtils.isEmpty(operatorNumeric) && !TextUtils.isEmpty(operatorNumeric2)) {
                    return operatorNumeric.equals(operatorNumeric2);
                }
            }
        }
        return true;
    }

    private int getDbm(SignalStrength signalStrength) {
        if (signalStrength != null) {
            return signalStrength.getDbm();
        }
        return Integer.MAX_VALUE;
    }

    private boolean isFiveG(ServiceState serviceState) {
        if (serviceState == null) {
            return false;
        }
        int dataNetworkType = serviceState.getDataNetworkType();
        log("fiveG check current rat = " + dataNetworkType);
        return Utils.isNr(dataNetworkType);
    }

    private void notifySimSwitch(int i) {
        Intent intent = new Intent();
        intent.setAction("com.android.phone.intent.action.SIM_SWITCH");
        intent.putExtra("phoneId", i);
        intent.putExtra("switchScene", "SIGNAL_MAP");
        this.mContext.sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDefaultDataSlotId() {
        return DefaultSimManager.getDefaultDataSlotId();
    }

    private void registerTelephonyCallBack() {
        if (this.mTelephonyCallBack == null) {
            log("registerTelephonyCallBack");
            TelephonyCallBack telephonyCallBack = new TelephonyCallBack();
            this.mTelephonyCallBack = telephonyCallBack;
            TelephonyStatAdapter.registerCallBack(telephonyCallBack);
        }
    }

    private boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) this.mContext.getSystemService("power");
        return powerManager != null && powerManager.isScreenOn();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMoreThanFourHour() {
        if (this.mLastRequestTime == 0) {
            return true;
        }
        if ((this.mLastRequestTime / 1000) + 14400 >= System.currentTimeMillis() / 1000) {
            return false;
        }
        log("isMoreThanFourHour");
        return true;
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        public void onSignalStrengthChanged(int i, SignalStrength signalStrength) {
            if (!MiuiPhoneUtils.isValidatePhoneId(i) || signalStrength == null) {
                return;
            }
            SignalMapController.this.mSignalStrength[i] = new SignalStrength(signalStrength);
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            if (i != SignalMapController.this.getDefaultDataSlotId()) {
                return;
            }
            SignalMapController.this.mHandler.obtainMessage(1, i, 0, cellLocation).sendToTarget();
            if (TextUtils.isEmpty(SignalMapController.this.mSMCtrller.getDistrict()) && SignalMapController.this.isMoreThanFourHour() && SystemClock.elapsedRealtime() >= 1200000) {
                SignalMapController.this.mLastRequestTime = System.currentTimeMillis();
                Location lastKnownLocation = SignalMapController.this.mSMCtrller.getLastKnownLocation();
                if (lastKnownLocation != null) {
                    SignalMapController.this.mSMCtrller.getAddressFromLocation(lastKnownLocation);
                    if (MiuiPhoneUtils.isSignalMapNOMLSupported()) {
                        SignalMapNOMLController.getInstance().mHandler.sendMessageDelayed(SignalMapNOMLController.getInstance().mHandler.obtainMessage(2, Integer.valueOf(i)), 20000L);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("SignalMapController", str);
    }

    private void logD(String str) {
        if (DEBUG) {
            Log.d("SignalMapController", str);
        }
    }
}
