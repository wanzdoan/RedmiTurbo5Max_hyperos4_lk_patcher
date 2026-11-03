package com.android.phone.signalMap;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SettingsObserver;
import com.android.phone.CloudCellDbHelper;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemDataCollectorBase;
import com.android.phone.ModemProvider;
import com.android.phone.NetworkModeRecognition;
import com.android.phone.PhoneGlobals;
import com.android.phone.TelephonySensorManager;
import com.android.phone.network.ModemNetworkPerception;
import com.android.phone.network.ModemQoeMetrics;
import java.util.HashSet;
import miui.telephony.DefaultSimManager;
import miui.telephony.TelephonyStatAdapter;

/* JADX INFO: loaded from: classes.dex */
public class SignalMapSubwayController {
    private static SignalMapSubwayController mSignalMapSubwayController;
    private static final String signalMapSubwayCity = null;
    private Context mContext;
    private CloudCellDbHelper mDbHelper;
    private SignalMapSubwayHandler mHandler;
    private ModemDataCollectorBase mModemDataCollect;
    private ModemNetworkPerception mModemNetworkPerception;
    private NetworkModeRecognition mNetworkModeRecognition;
    private ContentResolver mResolver;
    private HandlerThread mWorkThread;
    private ModemQoeMetrics mModemQoeMetrics = null;
    private SensorStateCallBack mSensorStateCallBack = null;
    private ServiceState[] mServiceState = new ServiceState[MiuiPhoneUtils.PHONE_COUNT];
    private TeleCallBack mTeleCallBack = null;
    private HashSet mSubwayPlatformCellSet = new HashSet();
    private HashSet mSubwayAbnormalCellSet = new HashSet();
    private boolean DEBUG = false;
    private boolean mCurrentSubwayState = false;
    private boolean mIsSubwayAbnoramlCell = false;
    private boolean mDurPlatform = false;
    private int mCurCellType = -1;
    private int mLastCellType = -1;
    private long mCurCellID = 0;
    private long mLastCellID = 0;
    private long mLastNotifyTtPTime = 0;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.phone.signalMap.SignalMapSubwayController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            SignalMapSubwayController.this.log("receive action".concat(action));
            if (action.equals("com.android.phone.intent.action.SUBWAY_SIGNAL_MAP_ACTION_DEBUG_ALL")) {
                SignalMapSubwayController.this.DEBUG = intent.getBooleanExtra("DEBUG", false);
                SignalMapSubwayController.this.mCurrentSubwayState = intent.getBooleanExtra("mCurrentSubwayState", false);
                SignalMapSubwayController.this.log("DEBUG = " + SignalMapSubwayController.this.DEBUG + "mCurrentSubwayState = " + SignalMapSubwayController.this.mCurrentSubwayState);
                SignalMapSubwayController.this.mHandler.obtainMessage(100).sendToTarget();
                return;
            }
            if (action.equals("com.android.phone.intent.action.SUBWAY_SIGNAL_MAP_ACTION_DEBUG_DURING_PLATFORM")) {
                SignalMapSubwayController.this.mDurPlatform = intent.getBooleanExtra("mDurPlatform", false);
                SignalMapSubwayController.this.log("mDurPlatform = " + SignalMapSubwayController.this.mDurPlatform);
            }
        }
    };
    ModemNetworkPerceptionListener mListener = new ModemNetworkPerceptionListener();

    private boolean isNrNwType(int i) {
        return i == 20 || i == 30;
    }

    public SignalMapSubwayController() {
        logD("SignalMapSubwayController Instantiation");
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        this.mResolver = phoneGlobals.getContentResolver();
        this.mDbHelper = CloudCellDbHelper.getInstance(this.mContext);
        this.mNetworkModeRecognition = NetworkModeRecognition.getInstance();
        HandlerThread handlerThread = new HandlerThread("SignalMapSubwayController", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        SignalMapSubwayHandler signalMapSubwayHandler = new SignalMapSubwayHandler(this.mWorkThread.getLooper());
        this.mHandler = signalMapSubwayHandler;
        signalMapSubwayHandler.obtainMessage(1).sendToTarget();
    }

    public static synchronized SignalMapSubwayController getInstance() {
        try {
            if (mSignalMapSubwayController == null) {
                mSignalMapSubwayController = new SignalMapSubwayController();
            }
        } catch (Throwable th) {
            throw th;
        }
        return mSignalMapSubwayController;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() {
        logD("SignalMapSubwayController init");
        registerReceiver();
        regSensorStateCallBack();
        registerTelephonyInfoCallBack();
        getSubwayAbnormalCellFromDb(signalMapSubwayCity);
        if (MiuiPhoneUtils.isModemNetworkPerceptionSupported() && MiuiPhoneUtils.isSupportModemAiSignalSupported() && ModemProvider.IS_QCOM) {
            this.mModemNetworkPerception = ModemNetworkPerception.getInstance();
        }
        ModemNetworkPerception modemNetworkPerception = this.mModemNetworkPerception;
        if (modemNetworkPerception != null) {
            modemNetworkPerception.registerModemNetworkPerception(this.mListener);
        }
        registerSettingsCallbacks();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            this.mServiceState[i] = new ServiceState();
        }
    }

    public class SignalMapSubwayHandler extends Handler {
        SignalMapSubwayHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            SignalMapSubwayController.this.log("msg.what = " + message.what);
            int i = message.what;
            if (i != 100) {
                switch (i) {
                    case 1:
                        SignalMapSubwayController.this.init();
                        break;
                    case 2:
                        SignalMapSubwayController.this.processSubwayIn();
                        break;
                    case 3:
                        SignalMapSubwayController.this.processSubwayOut();
                        break;
                    case 4:
                        SignalMapSubwayController.this.processSubwayStateChangeEvent();
                        break;
                    case 5:
                        SignalMapSubwayController.this.processServiceStateChangeEvent(message.arg1, (ServiceState) message.obj);
                        break;
                    case 6:
                        SignalMapSubwayController.this.mDurPlatform = true;
                        SignalMapSubwayController.this.logD("processSubwayStateStopDelay");
                        break;
                    case 7:
                        int i2 = message.arg1;
                        if (SignalMapSubwayController.this.isNeedAbnormalCellJudge(i2)) {
                            SignalMapSubwayController.this.processAbnormalCellJudge(i2);
                        }
                        break;
                    case 8:
                        SignalMapSubwayController.this.processSaSubwayPlatformRecognize(message.arg1, message.arg2);
                        break;
                    case 9:
                        SignalMapSubwayController.this.processNotifyTunnelToPlatform(String.valueOf(((Long) message.obj).longValue()));
                        break;
                }
                return;
            }
            SignalMapSubwayController.this.processDebugAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSubwayStateChangeEvent() {
        int subwayState = getSubwayState();
        logD("processSubwayStateChangeEvent = " + subwayState);
        if (subwayState == 1) {
            this.mDurPlatform = false;
        } else if (subwayState == 2 && !this.mHandler.hasMessages(6)) {
            SignalMapSubwayHandler signalMapSubwayHandler = this.mHandler;
            signalMapSubwayHandler.sendMessageDelayed(signalMapSubwayHandler.obtainMessage(6), 10000L);
        }
    }

    public boolean getCurrentSubwayState() {
        return this.mCurrentSubwayState;
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.intent.action.SUBWAY_SIGNAL_MAP_ACTION_DEBUG_ALL");
        intentFilter.addAction("com.android.phone.intent.action.SUBWAY_SIGNAL_MAP_ACTION_DEBUG_DURING_PLATFORM");
        this.mContext.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    private void registerSettingsCallbacks() {
        new SettingsObserver(this.mContext, this.mHandler).observe(Settings.Global.getUriFor("network_mode_state"), 4);
    }

    class TeleCallBack implements TelephonyStatAdapter.ICallBack {
        TeleCallBack() {
        }

        public void onServiceStateChanged(int i, ServiceState serviceState) {
            SignalMapSubwayController.this.mHandler.obtainMessage(5, i, 0, serviceState).sendToTarget();
        }

        public void onCellLocationChanged(int i, CellLocation cellLocation) {
            SignalMapSubwayController.this.mHandler.obtainMessage(7, i, 0, cellLocation).sendToTarget();
        }
    }

    private void registerTelephonyInfoCallBack() {
        log("registerTelephonyInfoCallBack");
        if (this.mTeleCallBack == null) {
            log("new TeleCallBack");
            TeleCallBack teleCallBack = new TeleCallBack();
            this.mTeleCallBack = teleCallBack;
            TelephonyStatAdapter.registerCallBack(teleCallBack);
        }
    }

    class SensorStateCallBack implements TelephonySensorManager.SensorStateListener {
        @Override // com.android.phone.TelephonySensorManager.SensorStateListener
        public void onDriveStateChanged(boolean z) {
        }

        @Override // com.android.phone.TelephonySensorManager.SensorStateListener
        public void onHstStateChanged(boolean z) {
        }

        @Override // com.android.phone.TelephonySensorManager.SensorStateListener
        public void onScreenStateChanged(boolean z) {
        }

        SensorStateCallBack() {
        }

        @Override // com.android.phone.TelephonySensorManager.SensorStateListener
        public void onSubwayStateChanged(boolean z) {
            if (!SignalMapSubwayController.this.mCurrentSubwayState && z) {
                SignalMapSubwayController.this.mHandler.obtainMessage(2).sendToTarget();
            } else {
                if (!SignalMapSubwayController.this.mCurrentSubwayState || z) {
                    return;
                }
                SignalMapSubwayController.this.mHandler.obtainMessage(3).sendToTarget();
            }
        }
    }

    private void regSensorStateCallBack() {
        TelephonySensorManager telephonySensorManager = TelephonySensorManager.getInstance();
        if (this.mSensorStateCallBack != null || telephonySensorManager == null) {
            return;
        }
        SensorStateCallBack sensorStateCallBack = new SensorStateCallBack();
        this.mSensorStateCallBack = sensorStateCallBack;
        telephonySensorManager.registerSensorStateListener(sensorStateCallBack);
    }

    class ModemNetworkPerceptionListener implements ModemNetworkPerception.IModemNetworkPerception {
        ModemNetworkPerceptionListener() {
        }

        @Override // com.android.phone.network.ModemNetworkPerception.IModemNetworkPerception
        public void onSaSubwayPlatformRecognize(int i, int i2) {
            SignalMapSubwayController.this.logD("onSaSubwayPlatformRecognize " + i + "  " + i2);
            SignalMapSubwayController.this.mHandler.obtainMessage(8, i, i2).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processSubwayIn() {
        this.mCurrentSubwayState = true;
        log("subway in");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processSubwayOut() {
        this.mCurrentSubwayState = false;
        this.mIsSubwayAbnoramlCell = false;
        this.mSubwayPlatformCellSet.clear();
        log("subway out");
    }

    private int getSubwayState() {
        if (this.mNetworkModeRecognition == null) {
            return 0;
        }
        int i = Settings.Global.getInt(this.mResolver, "network_mode_state", 0);
        if (!this.mNetworkModeRecognition.isSubwayStartStopStateUpdate(i)) {
            return 0;
        }
        if (this.mNetworkModeRecognition.isSubwayStart(i)) {
            return 1;
        }
        return this.mNetworkModeRecognition.isSubwayStop(i) ? 2 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNeedAbnormalCellJudge(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i) && i == getDefaultDataSlotId() && this.mCurrentSubwayState) {
            return true;
        }
        logD("NoNeedAbnormalCellJudge noDDS");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processAbnormalCellJudge(int i) {
        long nci;
        try {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null && phone.getCurrentCellIdentity() != null) {
                CellIdentity currentCellIdentity = phone.getCurrentCellIdentity();
                if (currentCellIdentity instanceof CellIdentityLte) {
                    nci = ((CellIdentityLte) currentCellIdentity).getCi();
                    updateCellState(2, nci);
                } else if (currentCellIdentity instanceof CellIdentityNr) {
                    nci = ((CellIdentityNr) currentCellIdentity).getNci();
                    updateCellState(1, nci);
                } else {
                    nci = -1;
                }
                String strValueOf = String.valueOf(nci);
                HashSet hashSet = this.mSubwayAbnormalCellSet;
                if (hashSet == null || hashSet.isEmpty()) {
                    this.mIsSubwayAbnoramlCell = false;
                    logD("NoNeedAbnormalCellJudge not mCurrentSubwayState");
                } else {
                    this.mIsSubwayAbnoramlCell = this.mSubwayAbnormalCellSet.contains(strValueOf);
                    logD("processAbnormalCellJudge IsAbCell=" + this.mIsSubwayAbnoramlCell + "cidStr=" + strValueOf);
                }
                return;
            }
            logD("processAbnormalCellJudge phone=null");
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processSaSubwayPlatformRecognize(int i, int i2) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone != null && phone.getCurrentCellIdentity() != null && this.mServiceState[i] != null && MiuiPhoneUtils.isValidatePhoneId(i) && i == getDefaultDataSlotId() && i2 == 0) {
            int dataNetworkType = this.mServiceState[i].getDataNetworkType();
            logD("mLastCellType = " + this.mLastCellType + " mDurPlatform = " + this.mDurPlatform + " dataType = " + dataNetworkType);
            if (getCellLayerNum() == 4 && this.mDurPlatform) {
                this.mCurCellType = 0;
                logD("mCurCellType = " + this.mCurCellType + " ifUploaded = " + this.mSubwayPlatformCellSet.contains(Long.valueOf(this.mLastCellID)));
                if (this.mLastCellType == 1 && isNrNwType(dataNetworkType) && !this.mSubwayPlatformCellSet.contains(Long.valueOf(this.mLastCellID))) {
                    SignalMapSubwayHandler signalMapSubwayHandler = this.mHandler;
                    signalMapSubwayHandler.sendMessageDelayed(signalMapSubwayHandler.obtainMessage(9, Long.valueOf(this.mLastCellID)), 7000L);
                    this.mSubwayPlatformCellSet.add(Long.valueOf(this.mLastCellID));
                }
            }
            return;
        }
        logD("processSaSubwayPlatformRecognize invalid parameter");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNotifyTunnelToPlatform(String str) {
        Intent intent = new Intent("com.android.phone.intent.action.SUBWAY_SIGNAL_MAP_TUNNEL_TO_PLATFORM");
        intent.putExtra("cid", str);
        this.mContext.sendBroadcast(intent);
        logD("processNotifyTunnelToPlatform" + str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processServiceStateChangeEvent(int i, ServiceState serviceState) {
        try {
            log("processServiceStateChangeEvent phoneId=" + i);
            if (serviceState != null && MiuiPhoneUtils.isValidatePhoneId(i)) {
                this.mServiceState[i] = new ServiceState(serviceState);
            } else {
                log("processServiceStateChangeEvent invalid params!!!");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private byte getCellLayerNum() {
        this.mModemQoeMetrics = ModemQoeMetrics.getInstance();
        ModemDataCollectorBase modemDataCollectorBase = ModemDataCollectorBase.getInstance();
        this.mModemDataCollect = modemDataCollectorBase;
        if (this.mModemQoeMetrics == null || modemDataCollectorBase == null) {
            logD("getCellLayerNum is invalid");
            return (byte) -1;
        }
        modemDataCollectorBase.updateModemAiData();
        byte numLayers = this.mModemQoeMetrics.getNumLayers();
        logD("getCellLayerNum = " + ((int) numLayers));
        return numLayers;
    }

    private void updateCellState(int i, long j) {
        this.mLastCellType = this.mCurCellType;
        this.mCurCellType = i;
        this.mLastCellID = this.mCurCellID;
        this.mCurCellID = j;
        logD("updateCellState " + this.mLastCellType + this.mCurCellType + this.mLastCellID + this.mCurCellID);
    }

    private void getSubwayAbnormalCellFromDb(String str) {
        if (!this.mSubwayAbnormalCellSet.isEmpty()) {
            this.mSubwayAbnormalCellSet.clear();
        }
        Cursor signalMapSubwayTableData = null;
        try {
            try {
                this.mDbHelper.readAndLoadDefaultSubwaySignalMap();
                signalMapSubwayTableData = this.mDbHelper.getSignalMapSubwayTableData(str);
                if (signalMapSubwayTableData != null) {
                    while (signalMapSubwayTableData.moveToNext()) {
                        this.mSubwayAbnormalCellSet.add(getCursorString(signalMapSubwayTableData, "cid"));
                    }
                }
                if (signalMapSubwayTableData != null) {
                    signalMapSubwayTableData.close();
                }
            } catch (Exception e) {
                log("Error retrieving data from database: " + e.getMessage());
                if (signalMapSubwayTableData != null) {
                }
            }
            log("getSubwayAbnormalCellFromDb");
        } catch (Throwable th) {
            if (signalMapSubwayTableData != null) {
                signalMapSubwayTableData.close();
            }
            throw th;
        }
    }

    private String getCursorString(Cursor cursor, String str) {
        return cursor.getString(cursor.getColumnIndex(str));
    }

    private int getDefaultDataSlotId() {
        return DefaultSimManager.getDefaultDataSlotId();
    }

    public boolean isSubwayAbnormalCellMatch() {
        return this.mIsSubwayAbnoramlCell;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDebugAll() {
        if (this.DEBUG) {
            recordAbnormalCellSet();
            processNotifyTunnelToPlatform("1711071619");
            getCellLayerNum();
        }
    }

    private void recordAbnormalCellSet() {
        HashSet hashSet = this.mSubwayAbnormalCellSet;
        if (hashSet == null || hashSet.isEmpty()) {
            logD("mSubwayAbnormalCellSet is null or empty");
            return;
        }
        logD("recordAbnormalCellSet" + this.mSubwayAbnormalCellSet.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("SignalMapSubwayController", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logD(String str) {
        if (this.DEBUG) {
            Log.d("SignalMapSubwayController", str);
        }
    }
}
