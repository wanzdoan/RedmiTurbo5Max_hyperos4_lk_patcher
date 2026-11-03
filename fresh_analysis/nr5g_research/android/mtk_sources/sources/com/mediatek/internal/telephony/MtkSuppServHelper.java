package com.mediatek.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.ims.RegistrationManager;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccController;
import com.mediatek.internal.telephony.uicc.MtkSIMRecords;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class MtkSuppServHelper extends Handler {
    private static final String ACTION_SYSTEM_UPDATE_SUCCESSFUL = "com.mediatek.systemupdate.UPDATE_SUCCESSFUL";
    private static final boolean CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT = false;
    private static final String CFU_SETTING_ALWAYS_NOT_QUERY = "1";
    private static final String CFU_SETTING_ALWAYS_QUERY = "2";
    private static final String CFU_SETTING_DEFAULT = "0";
    private static final String CFU_SETTING_QUERY_IF_EFCFIS_INVALID = "3";
    private static final int CONNECTOR_RETRY_DELAY_MS = 5000;
    private static final boolean DBG = true;
    private static final int EFCFIS_STATUS_INVALID = 3;
    private static final int EFCFIS_STATUS_NOT_READY = 0;
    private static final int EFCFIS_STATUS_VALID = 2;
    private static final int EVENT_CALL_FORWARDING_STATUS_FROM_MD = 6;
    private static final int EVENT_CARRIER_CONFIG_LOADED = 15;
    private static final int EVENT_CFU_STATUS_FROM_MD = 8;
    public static final int EVENT_CLEAN_CFU_STATUS = 16;
    private static final int EVENT_DATA_CONNECTION_ATTACHED = 2;
    private static final int EVENT_DATA_CONNECTION_DETACHED = 3;
    private static final int EVENT_GET_CALL_FORWARD_BY_GSM_DONE = 4;
    private static final int EVENT_GET_CALL_FORWARD_BY_IMS_DONE = 5;
    private static final int EVENT_GET_CALL_FORWARD_TIME_SLOT_BY_GSM_DONE = 10;
    private static final int EVENT_GET_CALL_FORWARD_TIME_SLOT_BY_IMS_DONE = 11;
    private static final int EVENT_ICCRECORDS_READY = 1;
    private static final int EVENT_ICC_CHANGED = 13;
    private static final int EVENT_QUERY_CFU_OVER_CS = 7;
    private static final int EVENT_QUERY_CFU_OVER_CS_AFTER_DATA_NOT_ATTACHED = 14;
    private static final int EVENT_REGISTERED_TO_NETWORK = 0;
    private static final int EVENT_SIM_RECORDS_LOADED = 12;
    private static final int EVENT_SS_RESET = 9;
    private static final String IMS_NOT_QUERY_YET = "1";
    private static final String IMS_NO_NEED_QUERY = "0";
    private static final String IMS_QUERY_DONE = "2";
    private static final String LOG_TAG = "MtkSuppServHelper";
    private static final int QUERY_OVER_GSM = 0;
    private static final int QUERY_OVER_GSM_OVER_UT = 1;
    private static final int QUERY_OVER_IMS = 2;
    private static final boolean SDBG;
    private static final String SIM_CHANGED = "1";
    private static final String SIM_NO_CHANGED = "0";
    private static final int TASK_CLEAN_CFU_STATUS = 4;
    private static final int TASK_QUERY_CFU = 0;
    private static final int TASK_QUERY_CFU_OVER_GSM = 1;
    private static final int TASK_QUERY_CFU_OVER_IMS = 2;
    private static final int TASK_SET_CW_STATUS = 5;
    private static final int TASK_TIME_SLOT_FAILED = 3;
    private static final int TIMER_FOR_RETRY_QUERY_CFU = 20000;
    private static final int TIMER_FOR_WAIT_DATA_ATTACHED = 20000;
    private static final boolean VDBG = SystemProperties.get("ro.build.type").equals("eng");
    private Context mContext;
    private HandlerExecutor mExecutor;
    private MtkGsmCdmaPhone mPhone;
    private SuppServTaskDriven mSuppServTaskDriven;
    private UiccController mUiccController = null;
    private AtomicBoolean mAttached = new AtomicBoolean(CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT);
    private MtkSuppServHelper mMtkSuppServHelper = null;
    private boolean mSimRecordsLoaded = CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
    private boolean mCarrierConfigLoaded = CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
    private ImsManager mImsManager = null;
    private int mNeeedSyncForOTA = -1;
    private Runnable mConnectorRunnable = new Runnable(this) { // from class: com.mediatek.internal.telephony.MtkSuppServHelper.1
        @Override // java.lang.Runnable
        public void run() {
        }
    };
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.MtkSuppServHelper.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.mediatek.intent.action.ACTION_SUBINFO_RECORD_UPDATED")) {
                MtkSuppServHelper.this.handleSubinfoUpdate();
                return;
            }
            if (!action.equals("android.intent.action.ACTION_SET_RADIO_CAPABILITY_DONE")) {
                if (action.equals("android.intent.action.AIRPLANE_MODE")) {
                    boolean bAirplaneModeOn = intent.getBooleanExtra("state", MtkSuppServHelper.CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT);
                    MtkSuppServHelper.this.logd("ACTION_AIRPLANE_MODE_CHANGED, bAirplaneModeOn = " + bAirplaneModeOn);
                    return;
                }
                if (action.equals("android.intent.action.ACTION_SUPPLEMENTARY_SERVICE_UT_TEST")) {
                    MtkSuppServHelper.this.logd("ACTION_SUPPLEMENTARY_SERVICE_UT_TEST");
                    if (!MtkSuppServHelper.this.isSupportSuppServUTTest()) {
                        return;
                    }
                    MtkSuppServUtTest ssUtTest = MtkSuppServHelper.this.makeMtkSuppServUtTest(intent);
                    ssUtTest.run();
                    return;
                }
                if (action.equals("android.telephony.action.SIM_APPLICATION_STATE_CHANGED")) {
                    int simStatus = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
                    int subId = intent.getIntExtra("subscription", -1);
                    MtkSuppServHelper.this.logd("ACTION_SIM_APPLICATION_STATE_CHANGED: " + simStatus + ", subId: " + subId);
                    if (10 != simStatus) {
                        return;
                    } else {
                        return;
                    }
                }
                if (action.equals(MtkSuppServHelper.ACTION_SYSTEM_UPDATE_SUCCESSFUL)) {
                    MtkSuppServHelper.this.logd("ACTION_SYSTEM_UPDATE_SUCCESSFUL");
                }
            }
        }
    };
    private final RegistrationManager.RegistrationCallback mImsRegistrationCallback = new RegistrationManager.RegistrationCallback() { // from class: com.mediatek.internal.telephony.MtkSuppServHelper.3
        @Override // android.telephony.ims.RegistrationManager.RegistrationCallback
        public void onRegistered(int imsRadioTech) {
            MtkSuppServHelper.this.logd("onImsRegistered imsRadioTech=" + imsRadioTech);
        }
    };

    static {
        SDBG = SystemProperties.get("ro.build.type").equals("user") ? CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT : DBG;
    }

    private class Task {
        private boolean mExtraBool;
        private int mExtraInt;
        private String mExtraMsg;
        private int mTaskId;

        public Task(MtkSuppServHelper mtkSuppServHelper, int taskId, boolean b, String extraMsg) {
            this.mTaskId = -1;
            this.mExtraBool = MtkSuppServHelper.CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
            this.mExtraInt = -1;
            this.mExtraMsg = "";
            this.mTaskId = taskId;
            this.mExtraBool = b;
            this.mExtraMsg = extraMsg;
        }

        public Task(MtkSuppServHelper mtkSuppServHelper, int taskId, String extraMsg) {
            this.mTaskId = -1;
            this.mExtraBool = MtkSuppServHelper.CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
            this.mExtraInt = -1;
            this.mExtraMsg = "";
            this.mTaskId = taskId;
            this.mExtraMsg = extraMsg;
        }

        public int getTaskId() {
            return this.mTaskId;
        }

        public int getExtraInt() {
            return this.mExtraInt;
        }

        public boolean getExtraBoolean() {
            return this.mExtraBool;
        }

        public String getExtraMsg() {
            return this.mExtraMsg;
        }

        public String toString() {
            return "Task ID: " + this.mTaskId + ", ExtraBool: " + this.mExtraBool + ", ExtraInt: " + this.mExtraInt + ", ExtraMsg: " + this.mExtraMsg;
        }
    }

    private class SuppServTaskDriven extends Handler {
        private static final int EVENT_DONE = 0;
        private static final int EVENT_EXEC_NEXT = 1;
        private static final int STATE_DOING = 1;
        private static final int STATE_DONE = 2;
        private static final int STATE_NO_PENDING = 0;
        private ArrayList<Task> mPendingTask;
        private int mState;
        private Object mStateLock;
        private Object mTaskLock;

        public SuppServTaskDriven() {
            this.mPendingTask = new ArrayList<>();
            this.mTaskLock = new Object();
            this.mStateLock = new Object();
            this.mState = 0;
        }

        public SuppServTaskDriven(Looper looper) {
            super(looper);
            this.mPendingTask = new ArrayList<>();
            this.mTaskLock = new Object();
            this.mStateLock = new Object();
            this.mState = 0;
        }

        public void appendTask(Task task) {
            synchronized (this.mTaskLock) {
                this.mPendingTask.add(task);
            }
            Message msg = obtainMessage(1);
            msg.sendToTarget();
        }

        private int getState() {
            int i;
            synchronized (this.mStateLock) {
                i = this.mState;
            }
            return i;
        }

        private void setState(int state) {
            synchronized (this.mStateLock) {
                this.mState = state;
            }
        }

        private Task getCurrentPendingTask() {
            synchronized (this.mTaskLock) {
                if (this.mPendingTask.size() == 0) {
                    return null;
                }
                return this.mPendingTask.get(0);
            }
        }

        private void removePendingTask(int index) {
            synchronized (this.mTaskLock) {
                if (this.mPendingTask.size() > 0) {
                    this.mPendingTask.remove(index);
                    MtkSuppServHelper.this.logd("removePendingTask remain mPendingTask: " + this.mPendingTask.size());
                }
            }
        }

        public void clearPendingTask() {
            synchronized (this.mTaskLock) {
                this.mPendingTask.clear();
            }
        }

        public void exec() {
            Task task = getCurrentPendingTask();
            if (task == null) {
                setState(0);
            }
            if (getState() == 1) {
                return;
            }
            setState(1);
            int taskId = task.getTaskId();
            MtkSuppServHelper.this.logd(task.toString());
            MtkSuppServHelper mtkSuppServHelper = MtkSuppServHelper.this;
            switch (taskId) {
                case 5:
                    mtkSuppServHelper.taskDone();
                    break;
                default:
                    mtkSuppServHelper.taskDone();
                    break;
            }
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    removePendingTask(0);
                    setState(2);
                    break;
                case 1:
                    break;
                default:
                    return;
            }
            exec();
        }

        private String stateToString(int state) {
            switch (state) {
                case 0:
                    return "STATE_NO_PENDING";
                case 1:
                    return "STATE_DOING";
                case 2:
                    return "STATE_DONE";
                default:
                    return "UNKNOWN_STATE";
            }
        }

        private String eventToString(int event) {
            switch (event) {
                case 0:
                    return "EVENT_DONE";
                case 1:
                    return "EVENT_EXEC_NEXT";
                default:
                    return "UNKNOWN_EVENT";
            }
        }
    }

    public MtkSuppServHelper(Context context, Phone phone, Looper looper) {
        this.mPhone = null;
        this.mSuppServTaskDriven = null;
        this.mContext = context;
        this.mPhone = (MtkGsmCdmaPhone) phone;
        this.mSuppServTaskDriven = new SuppServTaskDriven(looper);
        this.mExecutor = new HandlerExecutor(this.mSuppServTaskDriven);
        registerBroadcastReceiver();
        logd("MtkSuppServHelper init done.");
    }

    public void init(Looper looper) {
    }

    private boolean checkInitCriteria(StringBuilder criteriaFailReason) {
        if (!isSubInfoReady()) {
            criteriaFailReason.append("SubInfo not ready, ");
            return CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
        }
        if (!isIccCardMncMccAvailable(this.mPhone.getPhoneId())) {
            criteriaFailReason.append("MCC MNC not ready, ");
            return CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
        }
        if (!isIccRecordsAvailable()) {
            criteriaFailReason.append("Icc record available, ");
            return CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
        }
        if (!isVoiceInService()) {
            criteriaFailReason.append("Network is not registered, ");
            return CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
        }
        if (!getSimRecordsLoaded()) {
            criteriaFailReason.append("Sim not loaded, ");
            return CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
        }
        criteriaFailReason.append("All Criteria ready.");
        return DBG;
    }

    private IccRecords getUiccRecords(int appFamily) {
        return this.mUiccController.getIccRecords(this.mPhone.getPhoneId(), appFamily);
    }

    private boolean getSimRecordsLoaded() {
        logi("mSimRecordsLoaded: " + this.mSimRecordsLoaded);
        return this.mSimRecordsLoaded;
    }

    private void setSimRecordsLoaded(boolean value) {
        logi("Set mSimRecordsLoaded: " + value);
        this.mSimRecordsLoaded = value;
    }

    private boolean getCarrierConfigLoaded() {
        logi("mCarrierConfigLoaded: " + this.mCarrierConfigLoaded);
        return this.mCarrierConfigLoaded;
    }

    private void setCarrierConfigLoaded(boolean value) {
        logi("Set mCarrierConfigLoaded: " + value);
        this.mCarrierConfigLoaded = value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSubinfoUpdate() {
        if (!isSubInfoReady()) {
            return;
        }
        handleSuppServInit();
        if (!isIccRecordsAvailable()) {
        }
    }

    private boolean isNotMachineTest() {
        String isTestSim = "0";
        boolean isRRMEnv = CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
        if (this.mPhone.getPhoneId() == 0) {
            isTestSim = SystemProperties.get("vendor.gsm.sim.ril.testsim", "0");
        } else if (this.mPhone.getPhoneId() == 1) {
            isTestSim = SystemProperties.get("vendor.gsm.sim.ril.testsim.2", "0");
        }
        String operatorNumeric = this.mPhone.getServiceState().getOperatorNumeric();
        if (operatorNumeric != null && operatorNumeric.equals("46602")) {
            isRRMEnv = DBG;
        }
        logd("isTestSIM : " + isTestSim + " isRRMEnv : " + isRRMEnv);
        return (!isTestSim.equals("0") || isRRMEnv) ? CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT : DBG;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void taskDone() {
        Message ssmsg = this.mSuppServTaskDriven.obtainMessage(0);
        ssmsg.sendToTarget();
    }

    private boolean isIccCardMncMccAvailable(int phoneId) {
        UiccController uiccCtl = UiccController.getInstance();
        IccRecords iccRecords = uiccCtl.getIccRecords(phoneId, 1);
        if (iccRecords == null) {
            return CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
        }
        String mccMnc = iccRecords.getOperatorNumeric();
        if (mccMnc != null) {
            return DBG;
        }
        return CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
    }

    private boolean isIccRecordsAvailable() {
        IccRecords r = this.mPhone.getIccRecords();
        if (r != null) {
            return DBG;
        }
        return CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
    }

    private boolean isVoiceInService() {
        if (this.mPhone.mSST != null && this.mPhone.mSST.mSS != null && this.mPhone.mSST.mSS.getState() == 0) {
            return DBG;
        }
        return CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
    }

    private boolean isSubInfoReady() {
        SubscriptionManager subMgr = SubscriptionManager.from(this.mContext);
        SubscriptionInfo mySubInfo = null;
        if (subMgr != null) {
            mySubInfo = subMgr.getActiveSubscriptionInfo(this.mPhone.getSubId());
        }
        if (mySubInfo != null && mySubInfo.getIccId() != null) {
            return DBG;
        }
        return CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
    }

    private void handleSuppServInit() {
        String mySettingName = MtkTelephonyProperties.CFU_QUERY_ICCID_PROP + this.mPhone.getPhoneId();
        String oldIccId = SystemProperties.get(mySettingName, "");
        SubscriptionManager subMgr = SubscriptionManager.from(this.mContext);
        SubscriptionInfo mySubInfo = null;
        if (subMgr != null) {
            mySubInfo = subMgr.getActiveSubscriptionInfo(this.mPhone.getSubId());
        }
        if (mySubInfo == null) {
            this.mNeeedSyncForOTA = -1;
            return;
        }
        String newIccId = encryptString(mySubInfo.getIccId());
        if (oldIccId.equals(newIccId)) {
            return;
        }
        logw("mySubId " + this.mPhone.getSubId() + " mySettingName " + Rlog.pii(SDBG, mySettingName) + " old iccid : " + oldIccId + " new iccid : " + newIccId);
        SystemProperties.set(mySettingName, newIccId);
        String isChanged = MtkTelephonyProperties.CFU_QUERY_SIM_CHANGED_PROP + this.mPhone.getPhoneId();
        SystemProperties.set(isChanged, RadioCapabilitySwitchUtil.IMSI_READY);
        handleSuppServIfSimChanged();
    }

    private void handleSuppServIfSimChanged() {
        if (getSIMChangedRecordFromSystemProp()) {
            reset();
            this.mSuppServTaskDriven.appendTask(new Task(this, 5, CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT, "Sim Changed"));
            this.mPhone.saveTimeSlot(null);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.mContext);
            int clirSetting = sp.getInt("clir_sub_key" + this.mPhone.getPhoneId(), -1);
            if (clirSetting != -1) {
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("clir_sub_key" + this.mPhone.getPhoneId());
                if (!editor.commit()) {
                    loge("failed to commit the removal of CLIR preference");
                }
            }
            Task task = new Task(this, 0, CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT, "Sim Changed");
            this.mSuppServTaskDriven.appendTask(task);
        }
    }

    private boolean getSIMChangedRecordFromSystemProp() {
        String isChangedProp = MtkTelephonyProperties.CFU_QUERY_SIM_CHANGED_PROP + this.mPhone.getPhoneId();
        String isChanged = SystemProperties.get(isChangedProp, "0");
        logd("getSIMChangedRecordFromSystemProp: " + isChanged);
        if (isChanged.equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            return DBG;
        }
        return CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
    }

    private boolean isIMSRegistered() {
        Phone imsPhone = this.mPhone.getImsPhone();
        if (imsPhone != null && imsPhone.getServiceState().getState() == 0) {
            return DBG;
        }
        return CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
    }

    public boolean getIMSRegistered() {
        return isIMSRegistered();
    }

    private void setImsCallback() throws ImsException {
        try {
            if (this.mImsManager != null && this.mSuppServTaskDriven != null) {
                this.mImsManager.addRegistrationCallback(this.mImsRegistrationCallback, this.mExecutor);
                logd("ImsManager addRegistrationCallback");
            }
        } catch (ImsException ie) {
            logd("ImsManager addRegistrationCallback failed, " + ie.toString());
        }
    }

    private void unSetImsCallback() {
        logd("unSetImsCallback");
        if (this.mImsManager != null) {
            this.mImsManager.removeRegistrationListener(this.mImsRegistrationCallback);
        }
    }

    private void registerEvent() {
        this.mPhone.getServiceStateTracker().registerForDataConnectionAttached(1, this, 2, (Object) null);
        this.mPhone.getServiceStateTracker().registerForDataConnectionDetached(1, this, 3, (Object) null);
        this.mPhone.getServiceStateTracker().registerForNetworkAttached(this, 0, (Object) null);
        this.mPhone.registerForSimRecordsLoaded(this, 12, null);
        this.mUiccController = UiccController.getInstance();
        this.mUiccController.registerForIccChanged(this, 13, (Object) null);
        this.mPhone.mCi.registerForCallForwardingInfo(this, 8, null);
    }

    private void unRegisterEvent() {
        this.mPhone.getServiceStateTracker().unregisterForDataConnectionAttached(1, this);
        this.mPhone.getServiceStateTracker().unregisterForDataConnectionDetached(1, this);
        this.mPhone.getServiceStateTracker().unregisterForNetworkAttached(this);
        this.mPhone.unregisterForSimRecordsLoaded(this);
        this.mUiccController = UiccController.getInstance();
        this.mUiccController.unregisterForIccChanged(this);
        this.mPhone.mCi.unregisterForCallForwardingInfo(this);
    }

    private void registerBroadcastReceiver() {
        IntentFilter utTestFilter = new IntentFilter();
        utTestFilter.addAction("android.intent.action.ACTION_SUPPLEMENTARY_SERVICE_UT_TEST");
        this.mContext.registerReceiver(this.mBroadcastReceiver, utTestFilter, "com.mediatek.permission.SUPPLEMENTARY_SERVICE_UT_TEST", null, 2);
    }

    private void unRegisterBroadReceiver() {
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
    }

    public void dispose() {
        unRegisterBroadReceiver();
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        logd("handleMessage: " + toEventString(msg.what) + "(" + msg.what + ")");
        switch (msg.what) {
            case 3:
                this.mAttached.set(CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT);
                break;
            case 12:
                setSimRecordsLoaded(DBG);
                notifyCdmaCallForwardingIndicator();
                break;
            case 15:
                setCarrierConfigLoaded(DBG);
                break;
            default:
                logd("Bypass msg: " + msg.what);
                break;
        }
    }

    private int checkEfCfis() {
        MtkSIMRecords iccRecords = this.mPhone.getIccRecords();
        if (iccRecords != null && (iccRecords instanceof MtkSIMRecords)) {
            if (iccRecords.checkEfCfis()) {
                return 2;
            }
            return 3;
        }
        return 0;
    }

    private boolean syncSysPropToSIMforOTA() {
        if (this.mNeeedSyncForOTA == 0) {
            logd("syncSysPropToSIMforOTA: No need to sync (sim change): " + this.mNeeedSyncForOTA);
            return DBG;
        }
        if (this.mNeeedSyncForOTA == -1) {
            logd("syncSysPropToSIMforOTA: No need to sync (unknown): " + this.mNeeedSyncForOTA);
            return CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
        }
        if (!getSimRecordsLoaded()) {
            logd("syncSysPropToSIMforOTA: SIM not loaded.");
            return CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
        }
        int checkEfCfis = checkEfCfis();
        logd("syncSysPropToSIMforOTA: checkEfCfis = " + checkEfCfis);
        if (checkEfCfis == 0) {
            return CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
        }
        if (checkEfCfis != 2) {
            return checkEfCfis == 3 ? DBG : DBG;
        }
        boolean cfuStatus = this.mPhone.getCallForwardingIndicator();
        if (cfuStatus) {
            logd("syncSysPropToSIMforOTA: true from system preference.");
            this.mPhone.setVoiceCallForwardingFlag(1, cfuStatus, "");
        }
        return DBG;
    }

    public void setIccRecordsReady() {
        Message msg = obtainMessage(1);
        msg.sendToTarget();
    }

    private void reset() {
        this.mSuppServTaskDriven.clearPendingTask();
    }

    public void notifyCarrierConfigLoaded() {
        Message msg = obtainMessage(15);
        msg.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSupportSuppServUTTest() {
        return (SystemProperties.get("persist.vendor.ims_support").equals(RadioCapabilitySwitchUtil.IMSI_READY) && SystemProperties.get("persist.vendor.volte_support").equals(RadioCapabilitySwitchUtil.IMSI_READY) && this.mPhone.getPhoneId() == 0) ? DBG : CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
    }

    private boolean isMDSupportIMSSuppServ() {
        if (!SystemProperties.get("ro.vendor.md_auto_setup_ims").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            return CFU_QUERY_WHEN_IMS_REGISTERED_DEFAULT;
        }
        return DBG;
    }

    private boolean isSupportCFUTimeSlot() {
        return this.mPhone.isSupportCFUTimeSlot();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MtkSuppServUtTest makeMtkSuppServUtTest(Intent intent) {
        return new MtkSuppServUtTest(this.mContext, intent, this.mPhone);
    }

    private void notifyCdmaCallForwardingIndicator() {
        if (this.mPhone.getPhoneType() == 2) {
            this.mPhone.notifyCallForwardingIndicator();
        }
    }

    public String getXCAPErrorMessageFromSysProp(CommandException.Error error) {
        String errorCode;
        String propNamePrefix = "vendor.gsm.radio.ss.errormsg." + this.mPhone.getPhoneId();
        String fullErrorMsg = "";
        int idx = 0;
        String propName = propNamePrefix + ".0";
        String propValue = SystemProperties.get(propName, "");
        while (!propValue.equals("")) {
            fullErrorMsg = fullErrorMsg + propValue;
            idx++;
            String propName2 = propNamePrefix + "." + idx;
            propValue = SystemProperties.get(propName2, "");
        }
        logd("fullErrorMsg: " + fullErrorMsg);
        switch (AnonymousClass4.$SwitchMap$com$android$internal$telephony$CommandException$Error[error.ordinal()]) {
            case 1:
                errorCode = "409";
                break;
            case 2:
                errorCode = "412";
                break;
            case 3:
                errorCode = "415";
                break;
            case 4:
                errorCode = "500";
                break;
            case 5:
                errorCode = "503";
                break;
            default:
                return null;
        }
        if (!fullErrorMsg.startsWith(errorCode)) {
            return null;
        }
        String errorMsg = fullErrorMsg.substring(errorCode.length() + 1);
        logd("errorMsg: " + errorMsg);
        return errorMsg;
    }

    /* JADX INFO: renamed from: com.mediatek.internal.telephony.MtkSuppServHelper$4, reason: invalid class name */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$CommandException$Error = new int[CommandException.Error.values().length];

        static {
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_25.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_6.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_24.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_23.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_22.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    private String toReasonString(int event) {
        switch (event) {
            case 0:
                return "CS in service";
            case 1:
                return "ICCRecords ready";
            case 2:
                return "Data Attached";
            case 12:
                return "SIM records loaded";
            case 15:
                return "Carrier config loaded";
            case 16:
                return "Clean CFU status";
            default:
                return "Unknown reason, should not be here.";
        }
    }

    private String toEventString(int event) {
        switch (event) {
            case 0:
                return "EVENT_REGISTERED_TO_NETWORK";
            case 1:
                return "EVENT_ICCRECORDS_READY";
            case 2:
                return "EVENT_DATA_CONNECTION_ATTACHED";
            case 3:
                return "EVENT_DATA_CONNECTION_DETACHED";
            case 4:
                return "EVENT_GET_CALL_FORWARD_BY_GSM_DONE";
            case 5:
                return "EVENT_GET_CALL_FORWARD_BY_IMS_DONE";
            case 6:
                return "EVENT_CALL_FORWARDING_STATUS_FROM_MD";
            case 7:
                return "EVENT_QUERY_CFU_OVER_CS";
            case 8:
                return "EVENT_CFU_STATUS_FROM_MD";
            case 9:
                return "EVENT_SS_RESET";
            case 10:
                return "EVENT_GET_CALL_FORWARD_TIME_SLOT_BY_GSM_DONE";
            case 11:
                return "EVENT_GET_CALL_FORWARD_TIME_SLOT_BY_IMS_DONE";
            case 12:
                return "EVENT_SIM_RECORDS_LOADED";
            case 13:
                return "EVENT_ICC_CHANGED";
            case 14:
                return "EVENT_QUERY_CFU_OVER_CS_AFTER_DATA_NOT_ATTACHED";
            case 15:
                return "EVENT_CARRIER_CONFIG_LOADED";
            case 16:
                return "EVENT_CLEAN_CFU_STATUS";
            default:
                return "UNKNOWN_EVENT_ID";
        }
    }

    private void loge(String s) {
        Rlog.e(LOG_TAG, "[" + this.mPhone.getPhoneId() + "]" + s);
    }

    private void logw(String s) {
        Rlog.w(LOG_TAG, "[" + this.mPhone.getPhoneId() + "]" + s);
    }

    private void logi(String s) {
        Rlog.i(LOG_TAG, "[" + this.mPhone.getPhoneId() + "]" + s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String s) {
        Rlog.d(LOG_TAG, "[" + this.mPhone.getPhoneId() + "]" + s);
    }

    private void logv(String s) {
        Rlog.v(LOG_TAG, "[" + this.mPhone.getPhoneId() + "]" + s);
    }

    public static String encryptString(String message) {
        byte[] textByte;
        Base64.Encoder encoder = Base64.getEncoder();
        if (message == null) {
            return "null";
        }
        try {
            textByte = message.getBytes("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            textByte = null;
        }
        if (textByte == null) {
            return "";
        }
        String encryptedString = encoder.encodeToString(textByte);
        return encryptedString;
    }
}
