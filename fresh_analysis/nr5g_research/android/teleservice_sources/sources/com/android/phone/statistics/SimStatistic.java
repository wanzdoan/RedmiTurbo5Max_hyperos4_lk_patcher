package com.android.phone.statistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.phone.FiveGCloudController;
import com.android.phone.FiveGManagerBase;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.ATCommandUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiThreadUtil;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import miui.telephony.DefaultSimManager;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManagerEx;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class SimStatistic {
    private static final boolean IS_NORIL;
    private static boolean[] mIsEsimStatus;
    private static final Object mLock;
    private static int[] mServiceState;
    private static int[] mSimApplicationState;
    private static int[] mSimState;
    private static SimStatistic sInstance;
    private final String HW_LEVEL;
    private final long INVAILD_TIME;
    private final boolean IS_CUST_SINGLE_SIM;
    private Context mContext;
    private String mCurrMultisimConfig;
    private final Handler mHandler;
    private boolean[] mHasServiceStatusDelay;
    private boolean[] mHasSimStatusDelay;
    private boolean mIsOTAUpdate;
    private String mLastMultisimConfig;
    protected int mMultiConfigChangeType;
    private long[] mNotPresentStatTime;
    private int[] mPortId;
    private long[] mPresentStatTime;
    final BroadcastReceiver mReceiver;
    private int[] mSlotId;
    private SharedPreferences mStorage;
    private TelephonyManager mTm;

    private int matchServiceStatusErrorCode(int i) {
        if (i > -1) {
            if (i == 1) {
                return 915400207;
            }
            if (i == 2) {
                return 915400208;
            }
            if (i == 3) {
                return 915400209;
            }
        }
        return -1;
    }

    static {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        mSimState = new int[i];
        mIsEsimStatus = new boolean[i];
        mServiceState = new int[i];
        mSimApplicationState = new int[i];
        mLock = new Object();
        IS_NORIL = SystemProperties.getBoolean("ro.radio.noril", false);
    }

    enum TimePeriod {
        INVALID("Invalid"),
        SHORT("Short"),
        MEDIUM("Medium"),
        LONG("Long"),
        EXTRA_LONG("Extra Long");

        private String value;

        TimePeriod(String str) {
            this.value = str;
        }

        public String getValue() {
            return this.value;
        }
    }

    public static SimStatistic getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SimStatistic(context);
        }
        return sInstance;
    }

    protected SimStatistic(Context context) {
        int i = MiuiPhoneUtils.PHONE_COUNT;
        this.mSlotId = new int[i];
        this.mPortId = new int[i];
        this.mLastMultisimConfig = "";
        this.mCurrMultisimConfig = "";
        this.IS_CUST_SINGLE_SIM = SystemProperties.getInt("ro.miui.singlesim", 0) == 1;
        this.HW_LEVEL = SystemProperties.get("ro.boot.hwlevel", "unknown");
        this.mIsOTAUpdate = false;
        this.INVAILD_TIME = 0L;
        this.mPresentStatTime = new long[i];
        this.mNotPresentStatTime = new long[i];
        this.mHasSimStatusDelay = new boolean[i];
        this.mHasServiceStatusDelay = new boolean[i];
        this.mReceiver = new BroadcastReceiver() { // from class: com.android.phone.statistics.SimStatistic.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                Log.d("SimStatistic", "mReceiver action=".concat(action));
                if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
                    SimStatistic.this.mHandler.sendEmptyMessage(1);
                    return;
                }
                if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    SimStatistic.this.mHandler.sendMessage(SimStatistic.this.mHandler.obtainMessage(3, intent));
                    return;
                }
                if ("android.telephony.action.SIM_APPLICATION_STATE_CHANGED".equals(action)) {
                    SimStatistic.this.mHandler.sendMessage(SimStatistic.this.mHandler.obtainMessage(4, intent));
                    return;
                }
                if ("android.intent.action.ACTION_SHUTDOWN".equals(action)) {
                    SimStatistic.this.saveResetTime("time_boot_conpleted", 0L);
                    SimStatistic.this.unRegisterSimNotRecognizedModemError();
                    SimStatistic.this.unregisterModemReset();
                } else {
                    if ("android.intent.action.SERVICE_STATE".equals(action)) {
                        SimStatistic.this.mHandler.sendMessage(SimStatistic.this.mHandler.obtainMessage(7, intent));
                        return;
                    }
                    if ("miui.intent.action.MODEMENHANCE_UIM_STATISTIC".equals(action)) {
                        SimStatistic.this.mHandler.sendMessage(SimStatistic.this.mHandler.obtainMessage(9, intent));
                    } else if (MEModuleSimAbnormalMonitor.ACTION_SIM_FAULT_DETECTED.equals(action)) {
                        SimStatistic.this.mHandler.sendMessage(SimStatistic.this.mHandler.obtainMessage(10, intent));
                    } else if ("miui.intent.action.QCOM_SIM_ABNORMAL_DETECTED".equals(action)) {
                        SimStatistic.this.mHandler.sendMessage(SimStatistic.this.mHandler.obtainMessage(11, intent));
                    }
                }
            }
        };
        this.mHandler = new Handler(MiuiThreadUtil.getWorkThreadLooper()) { // from class: com.android.phone.statistics.SimStatistic.2
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                SimStatistic.this.handleEvent(message);
            }
        };
        this.mContext = context;
        this.mStorage = PreferenceManager.getDefaultSharedPreferences(context);
        this.mTm = TelephonyManager.from(this.mContext);
        for (int i2 = 0; i2 < MiuiPhoneUtils.PHONE_COUNT; i2++) {
            this.mSlotId[i2] = -1;
            this.mPortId[i2] = -1;
            mSimState[i2] = 0;
            this.mPresentStatTime[i2] = 0;
            this.mNotPresentStatTime[i2] = 0;
            this.mHasSimStatusDelay[i2] = false;
            this.mHasServiceStatusDelay[i2] = false;
            mServiceState[i2] = -1;
            mSimApplicationState[i2] = 0;
            mIsEsimStatus[i2] = false;
        }
        initReceiver();
    }

    private void initReceiver() {
        if (IS_NORIL) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        intentFilter.addAction("android.intent.action.SERVICE_STATE");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.telephony.action.SIM_APPLICATION_STATE_CHANGED");
        if (ModemProvider.IS_QCOM) {
            intentFilter.addAction("miui.intent.action.MODEMENHANCE_UIM_STATISTIC");
            intentFilter.addAction("miui.intent.action.QCOM_SIM_ABNORMAL_DETECTED");
        }
        if (ModemProvider.IS_MTK) {
            intentFilter.addAction(MEModuleSimAbnormalMonitor.ACTION_SIM_FAULT_DETECTED);
        }
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
        if (getResetTime("time_boot_conpleted") != 0) {
            saveSimErrorHistory(915400101, 0, System.currentTimeMillis());
        }
    }

    public void dispose() {
        if (IS_NORIL) {
            return;
        }
        Log.d("SimStatistic", "dispose");
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEvent(Message message) {
        int i = message.what;
        if (i == 1) {
            processBootCompleted();
            return;
        }
        if (i == 7) {
            processServiceStateChanged((Intent) message.obj);
            return;
        }
        if (i == 9 || i == 1000) {
            processSimNotRecognizedModemError(message);
            return;
        }
        if (i == 3) {
            processSimCardStateChanged((Intent) message.obj);
            return;
        }
        if (i == 4) {
            processSimApplicationStateChanged((Intent) message.obj);
        } else if (i == 5) {
            processModemReset();
        } else {
            handleDelayedMessages(message);
        }
    }

    private void handleDelayedMessages(Message message) {
        int i = message.what;
        if (i == 6) {
            int iIntValue = ((Integer) message.obj).intValue();
            this.mHasSimStatusDelay[iIntValue] = true;
            processSimNotRecognizedSimStatus(iIntValue);
        } else if (i == 8) {
            int iIntValue2 = ((Integer) message.obj).intValue();
            this.mHasServiceStatusDelay[iIntValue2] = true;
            processSimNotRecognizedServiceStatus(iIntValue2);
        } else if (i == 10) {
            handleSimFaultDetected((Intent) message.obj);
        } else {
            if (i != 11) {
                return;
            }
            handleQcomSimAbnormal((Intent) message.obj);
        }
    }

    protected void processBootCompleted() {
        if (this.mStorage == null) {
            return;
        }
        this.mIsOTAUpdate = isOTAUpdate();
        this.mLastMultisimConfig = this.mStorage.getString("miui_multisim_config", "");
        this.mCurrMultisimConfig = SystemProperties.get("persist.radio.multisim.config");
        if (!TextUtils.equals(this.mLastMultisimConfig, "") && !TextUtils.equals(this.mLastMultisimConfig, this.mCurrMultisimConfig)) {
            this.mMultiConfigChangeType = calMultiConfigChangeType(this.mLastMultisimConfig, this.mCurrMultisimConfig);
            recordMultiSimConfigChange();
        }
        this.mStorage.edit().putString("miui_multisim_config", this.mCurrMultisimConfig).apply();
        processSimNotRecognizedMultiSimConfig();
        registerModemReset();
        saveResetTime("time_boot_conpleted", System.currentTimeMillis());
        registerSimNotRecognizedModemError();
    }

    private void processSimCardStateChanged(Intent intent) {
        if (intent == null) {
            return;
        }
        int intExtra = intent.getIntExtra("phone", -1);
        if (SubscriptionManager.isValidPhoneId(intExtra)) {
            UiccSlot uiccSlotForPhone = UiccController.getInstance().getUiccSlotForPhone(intExtra);
            int slotIdFromPhoneId = UiccController.getInstance().getSlotIdFromPhoneId(intExtra);
            int portIndexFromPhoneId = uiccSlotForPhone != null ? uiccSlotForPhone.getPortIndexFromPhoneId(intExtra) : -1;
            this.mSlotId[intExtra] = slotIdFromPhoneId;
            this.mPortId[intExtra] = portIndexFromPhoneId;
            mSimState[intExtra] = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
            recordSimCardInfo(intExtra);
            processSimNotRecognizedSimStatus(intExtra);
        }
    }

    private void processSimApplicationStateChanged(Intent intent) {
        if (intent == null) {
            return;
        }
        int intExtra = intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1);
        if (SubscriptionManager.isValidPhoneId(intExtra)) {
            mSimApplicationState[intExtra] = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
            processSimNotRecognizedSimStatus(intExtra);
        }
    }

    private void processServiceStateChanged(Intent intent) {
        if (intent == null) {
            return;
        }
        int intExtra = intent.getIntExtra("slot", 0);
        ServiceState serviceStateNewFromBundle = ServiceState.newFromBundle(intent.getExtras());
        if (!SubscriptionManager.isValidPhoneId(intExtra) || serviceStateNewFromBundle == null) {
            return;
        }
        mServiceState[intExtra] = serviceStateNewFromBundle.getState();
        if (mServiceState[intExtra] == 0 && serviceStateNewFromBundle.getDataRegistrationState() != 0) {
            mServiceState[intExtra] = serviceStateNewFromBundle.getDataRegistrationState();
        }
        processSimNotRecognizedServiceStatus(intExtra);
    }

    private void processSimNotRecognizedMultiSimConfig() {
        if (isMatchMultiSimConfig()) {
            return;
        }
        saveSimErrorHistory(915400100, 0, System.currentTimeMillis());
    }

    private boolean isMatchMultiSimConfig() {
        Phone defaultPhone;
        if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && (defaultPhone = PhoneFactory.getDefaultPhone()) != null) {
            Object[] objArr = new Object[1];
            try {
                if (ATCommandUtils.getInstance().atCmdRunSync(defaultPhone.getPhoneId(), new String[]{"AT+ESADM?", "+ESADM"}, objArr)) {
                    String[] strArr = (String[]) objArr[0];
                    if (strArr != null && strArr.length >= 1) {
                        String[] strArrSplit = strArr[0].split(":");
                        if (strArrSplit != null && strArrSplit.length >= 1) {
                            int activeModemCount = TelephonyManager.getDefault().getActiveModemCount();
                            int i = Integer.parseInt(strArrSplit[1].trim());
                            Log.d("SimStatistic", "isMatchMultiSimConfig vendorMultisimConfig = " + i + "; androidMultisimConfig = " + activeModemCount);
                            return (activeModemCount == 1 && i == 1) || (activeModemCount == 2 && i == 3) || (activeModemCount == 3 && i == 7);
                        }
                        Log.e("SimStatistic", "isMatchMultiSimConfig: queryValue is empty");
                        return false;
                    }
                    Log.e("SimStatistic", "isMatchMultiSimConfig: queryResult is empty");
                    return false;
                }
            } catch (Exception e) {
                Log.e("SimStatistic", "isMatchMultiSimConfig exception = " + e);
            }
        }
        return true;
    }

    private void processSimNotRecognizedSimStatus(int i) {
        Log.d("SimStatistic", "processSimNotRecognizedSimStatus phoneId = " + i + "; mSimState = " + mSimState[i] + "; mSimApplicationState = " + mSimApplicationState[i]);
        synchronized (mLock) {
            try {
                if (getSimState(i) != mSimState[i]) {
                    if (getSimState(i) == 11 && mSimState[i] != 11 && (isSimSlotInsertedStatus(i) || currentTimeHasModemCrash() || currentTimeHasPhoneCrash())) {
                        simLostStat(handleRealTimeCheckSimLostError(i), i);
                    }
                    saveSimState(i, mSimState[i]);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        int i2 = mSimState[i];
        if (i2 == 8 || i2 == 9 || ((i2 == 11 && mSimApplicationState[i] == 2) || ((i2 == 11 && mSimApplicationState[i] == 3) || ((i2 == 11 && mSimApplicationState[i] == 4) || ((i2 == 11 && mSimApplicationState[i] == 7) || (i2 == 11 && mSimApplicationState[i] == 6 && isDisabledSim(i))))))) {
            if (this.mHandler.hasMessages(6, Integer.valueOf(i))) {
                return;
            }
            if (this.mHasSimStatusDelay[i]) {
                saveSimErrorHistory(matchSimStatusErrorCode(mSimState[i], mSimApplicationState[i], i), i, System.currentTimeMillis() - 180000);
                this.mHasSimStatusDelay[i] = false;
                return;
            } else {
                Handler handler = this.mHandler;
                if (handler != null) {
                    handler.sendMessageDelayed(handler.obtainMessage(6, Integer.valueOf(i)), 180000L);
                    return;
                }
                return;
            }
        }
        int i3 = mSimState[i];
        if ((i3 == 1 || i3 == 0 || (i3 == 11 && mSimApplicationState[i] == 10)) && this.mHandler.hasMessages(6, Integer.valueOf(i))) {
            this.mHandler.removeMessages(6, Integer.valueOf(i));
        }
    }

    private boolean isSimSlotInsertedStatus(int i) {
        if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
            Phone defaultPhone = PhoneFactory.getDefaultPhone();
            if (defaultPhone != null) {
                Object[] objArr = new Object[1];
                try {
                    if (!ATCommandUtils.getInstance().atCmdRunSync(defaultPhone.getPhoneId(), new String[]{"AT+ESIMTRAY?", "+ESIMTRAY"}, objArr)) {
                        return false;
                    }
                    String[] strArr = (String[]) objArr[0];
                    if (strArr != null && strArr.length >= 1) {
                        String[] strArrSplit = strArr[0].split(":");
                        if (strArrSplit != null && strArrSplit.length >= 1) {
                            return Integer.parseInt(strArrSplit[1].trim()) == 1;
                        }
                        Log.e("SimStatistic", "isSimSlotInsertedStatus: queryValue is empty");
                        return false;
                    }
                    Log.e("SimStatistic", "isSimSlotInsertedStatus: queryResult is empty");
                    return false;
                } catch (Exception e) {
                    Log.e("SimStatistic", "isSimSlotInsertedStatus exception = " + e);
                    return false;
                }
            }
        } else if (ModemProvider.IS_QCOM && FiveGManagerBase.getInstance() != null) {
            return FiveGManagerBase.getInstance().isSimCardSlotInsertedStatus(i);
        }
        return false;
    }

    private void simLostStat(int i, int i2) {
        if (!isMatchRecordCondition(i)) {
            Log.d("SimStatistic", "Not Match Record Condition, errorCode = " + i);
            return;
        }
        HashMap map = new HashMap();
        map.put("SimLostErrorCode", Integer.valueOf(i));
        map.put("slotId", Integer.valueOf(i2));
        map.put("hwlevel", this.HW_LEVEL);
        Log.d("SimStatistic", "simLostStat params = " + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "sim_card_lost_error_info", map);
        if (String.valueOf(i).startsWith("9154001")) {
            return;
        }
        MiuiTelephonyStatistic.getInstance().recordDFSEventStatistic(915400002, map);
    }

    private boolean isDisabledSim(int i) {
        boolean zIsIccCardActivated = MiuiPhoneUtils.isIccCardActivated(i);
        Log.d("SimStatistic", "isDisabledSim phoneId = " + i + "; activated = " + zIsIccCardActivated);
        return !zIsIccCardActivated;
    }

    private int matchSimStatusErrorCode(int i, int i2, int i3) {
        if (i > -1 && i2 > -1) {
            if (i == 8) {
                return 915400205;
            }
            if (i == 9) {
                return 915400206;
            }
            if (i == 11) {
                if (i2 == 2) {
                    return 915400201;
                }
                if (i2 == 3) {
                    return 915400202;
                }
                if (i2 == 4) {
                    return 915400203;
                }
                if (i2 == 7) {
                    return 915400204;
                }
                if (i2 == 6 && isDisabledSim(i3)) {
                    return 915400200;
                }
            }
        }
        return -1;
    }

    private void processSimNotRecognizedServiceStatus(int i) {
        Log.d("SimStatistic", "processSimNotRecognizedServiceStatus phoneId = " + i + "; mServiceState = " + mServiceState[i]);
        if (mSimState[i] == 11 || mSimApplicationState[i] == 10) {
            int i2 = mServiceState[i];
            if (i2 == 1 || i2 == 2 || i2 == 3) {
                if (this.mHandler.hasMessages(8, Integer.valueOf(i))) {
                    return;
                }
                if (this.mHasServiceStatusDelay[i]) {
                    saveSimErrorHistory(matchServiceStatusErrorCode(mServiceState[i]), i, System.currentTimeMillis() - 180000);
                    this.mHasServiceStatusDelay[i] = false;
                    return;
                } else {
                    Handler handler = this.mHandler;
                    if (handler != null) {
                        handler.sendMessageDelayed(handler.obtainMessage(8, Integer.valueOf(i)), 180000L);
                        return;
                    }
                    return;
                }
            }
            if (i2 == 0 && this.mHandler.hasMessages(8, Integer.valueOf(i))) {
                this.mHandler.removeMessages(8, Integer.valueOf(i));
            }
        }
    }

    private void registerModemReset() {
        Phone defaultPhone = PhoneFactory.getDefaultPhone();
        if (defaultPhone == null) {
            return;
        }
        defaultPhone.mCi.registerForModemReset(this.mHandler, 5, (Object) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterModemReset() {
        Phone defaultPhone = PhoneFactory.getDefaultPhone();
        if (defaultPhone == null) {
            return;
        }
        defaultPhone.mCi.unregisterForModemReset(this.mHandler);
    }

    private void processModemReset() {
        saveSimErrorHistory(915400102, 0, System.currentTimeMillis());
    }

    private void registerSimNotRecognizedModemError() {
        if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && ATCommandUtils.getInstance() != null) {
            ATCommandUtils.getInstance().regUnRegisterOemAtUrcInd(true, this.mHandler, 1000);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unRegisterSimNotRecognizedModemError() {
        if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && ATCommandUtils.getInstance() != null) {
            ATCommandUtils.getInstance().regUnRegisterOemAtUrcInd(false, this.mHandler, 1000);
        }
    }

    private synchronized void processSimNotRecognizedModemError(Message message) {
        if (message == null) {
            return;
        }
        if (!ModemProvider.IS_MTK && !ModemProvider.IS_XRING) {
            if (ModemProvider.IS_QCOM) {
                try {
                    Intent intent = (Intent) message.obj;
                    if (intent == null) {
                        Log.e("SimStatistic", "processSimNotRecognizedModemError intent null");
                        return;
                    }
                    ByteBuffer byteBufferWrap = ByteBuffer.wrap(intent.getByteArrayExtra("data"));
                    byteBufferWrap.order(ByteOrder.nativeOrder());
                    int i = byteBufferWrap.getInt();
                    int i2 = byteBufferWrap.getInt();
                    Log.d("SimStatistic", "processSimNotRecognizedModemError moduleId = " + i + "; errorTypeId = " + i2 + "; dataLength = " + byteBufferWrap.getInt());
                    if (i == 9) {
                        int i3 = byteBufferWrap.getInt();
                        int iMatchModemErrorCode = matchModemErrorCode(Integer.toString(i2), -1);
                        Log.d("SimStatistic", "processSimNotRecognizedModemError slotId = " + i3 + "; modemErrorCode = " + iMatchModemErrorCode);
                        if (iMatchModemErrorCode == 915400502) {
                            Log.e("SimStatistic", "processSimNotRecognizedModemError invalid error code, ignore");
                            return;
                        } else {
                            if (currentTimeHasSameModemErrorCode(i3, iMatchModemErrorCode)) {
                                Log.e("SimStatistic", "processSimNotRecognizedModemError same error code, ignore");
                                return;
                            }
                            saveSimErrorHistory(iMatchModemErrorCode, i3, System.currentTimeMillis());
                        }
                    }
                } catch (Exception e) {
                    Log.e("SimStatistic", "processSimNotRecognizedModemError exception = " + e);
                }
            }
        }
        AsyncResult asyncResult = (AsyncResult) message.obj;
        if (asyncResult != null) {
            Phone phone = (Phone) asyncResult.userObj;
            int phoneId = phone != null ? phone.getPhoneId() : -1;
            String str = (String) asyncResult.result;
            Log.d("SimStatistic", "processSimNotRecognizedModemError phoneId = " + phoneId + "; urcAtString = " + str);
            try {
                if (str.startsWith("+EDMFAPP:")) {
                    String[] strArrSplit = str.split(",");
                    if (Integer.parseInt(strArrSplit[0].trim().split(":")[1].trim()) != 20120) {
                        Log.d("SimStatistic", "processSimNotRecognizedModemError appId not match");
                        return;
                    }
                    if (strArrSplit.length >= 5) {
                        int i4 = Integer.parseInt(strArrSplit[strArrSplit.length - 4].trim());
                        int i5 = Integer.parseInt(strArrSplit[strArrSplit.length - 2].trim());
                        int iMatchModemErrorCode2 = matchModemErrorCode(strArrSplit[strArrSplit.length - 1].trim(), i4);
                        Log.d("SimStatistic", "processSimNotRecognizedModemError slotId = " + i5 + "; modemSimErrorLevel = " + i4 + "; mSimState = " + mSimState[i5] + "; modemErrorCode = " + iMatchModemErrorCode2);
                        if (iMatchModemErrorCode2 != 915400300 && iMatchModemErrorCode2 != 915400402 && iMatchModemErrorCode2 != 915400403 && iMatchModemErrorCode2 != 915400405 && iMatchModemErrorCode2 != 915400406 && iMatchModemErrorCode2 != 915400400) {
                            if (currentTimeHasSameModemErrorCode(phoneId, iMatchModemErrorCode2)) {
                                Log.e("SimStatistic", "processSimNotRecognizedModemError same error code, ignore");
                                return;
                            }
                            saveSimErrorHistory(iMatchModemErrorCode2, phoneId, System.currentTimeMillis());
                        }
                        Log.e("SimStatistic", "processSimNotRecognizedModemError invalid error code, ignore");
                        return;
                    }
                    Log.e("SimStatistic", "processSimNotRecognizedModemError data length invalid");
                }
            } catch (Exception e2) {
                Log.e("SimStatistic", "processSimNotRecognizedModemError exception = " + e2);
            }
        }
    }

    private int matchModemErrorCode(String str, int i) {
        int i2;
        int i3;
        int i4;
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
            if (i == 1) {
                i3 = 915400300;
                i4 = Integer.parseInt(str);
            } else {
                if (i == 0) {
                    i3 = 915400400;
                    i4 = Integer.parseInt(str);
                } else {
                    i2 = Integer.parseInt(str);
                }
                Log.d("SimStatistic", "matchModemErrorCode = " + i2);
                return i2;
            }
            i2 = i4 + i3;
            Log.d("SimStatistic", "matchModemErrorCode = " + i2);
            return i2;
        }
        if (ModemProvider.IS_QCOM) {
            return Integer.parseInt(str) + 915400500;
        }
        return -1;
    }

    private synchronized void saveSimErrorHistory(int i, int i2, long j) {
        JSONArray jSONArray;
        JSONObject jSONObject;
        try {
            Log.d("SimStatistic", "saveSimErrorHistory errorCode = " + i + "; phoneId = " + i2 + "; time = " + j);
            if (i == -1 || !SubscriptionManager.isValidPhoneId(i2) || j == 0) {
                return;
            }
            try {
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance());
                String string = defaultSharedPreferences.getString("SimNotRecognizedErrorCode", "");
                if (TextUtils.isEmpty(string)) {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("error_code", i);
                    jSONObject2.put(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, i2);
                    jSONObject = new JSONObject();
                    jSONObject.put(String.valueOf(j), jSONObject2);
                    jSONArray = new JSONArray();
                    jSONArray.put(String.valueOf(j));
                } else {
                    JSONObject jSONObject3 = new JSONObject(string);
                    JSONArray jSONArray2 = jSONObject3.getJSONArray("order");
                    JSONObject jSONObject4 = jSONObject3.getJSONObject("data");
                    if (jSONArray2.length() == 20) {
                        String string2 = jSONArray2.getString(0);
                        jSONArray2.remove(0);
                        jSONObject4.remove(string2);
                        JSONObject jSONObject5 = new JSONObject();
                        jSONObject5.put("error_code", i);
                        jSONObject5.put(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, i2);
                        jSONObject4.put(String.valueOf(j), jSONObject5);
                        jSONArray2.put(String.valueOf(j));
                    } else {
                        JSONObject jSONObject6 = new JSONObject();
                        jSONObject6.put("error_code", i);
                        jSONObject6.put(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, i2);
                        jSONObject4.put(String.valueOf(j), jSONObject6);
                        jSONArray2.put(String.valueOf(j));
                    }
                    jSONArray = jSONArray2;
                    jSONObject = jSONObject4;
                }
                JSONObject jSONObject7 = new JSONObject();
                jSONObject7.put("order", jSONArray);
                jSONObject7.put("data", jSONObject);
                SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
                editorEdit.putString("SimNotRecognizedErrorCode", jSONObject7.toString());
                editorEdit.apply();
            } catch (Exception e) {
                Log.e("SimStatistic", "saveSimErrorHistory exception = " + e);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private long getResetTime(String str) {
        return PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).getLong(str, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveResetTime(String str, long j) {
        Log.d("SimStatistic", "saveResetTime type = " + str + "; time = " + j);
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).edit();
        editorEdit.putLong(str, j);
        editorEdit.apply();
    }

    private int getSimState(int i) {
        return PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).getInt("simState" + i, 0);
    }

    private void saveSimState(int i, int i2) {
        Log.d("SimStatistic", "saveSimState phoneId = " + i + "; state = " + i2);
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).edit();
        StringBuilder sb = new StringBuilder("simState");
        sb.append(i);
        editorEdit.putInt(sb.toString(), i2);
        editorEdit.apply();
    }

    private int handleRealTimeCheckSimLostError(int i) {
        int i2 = mSimState[i];
        if (i2 != 1 && i2 != 0) {
            return -1;
        }
        if (currentTimeHasPhoneCrash()) {
            return 915400101;
        }
        if (currentTimeHasModemCrash()) {
            return 915400102;
        }
        if (isMatchMultiSimConfig()) {
            return currentTimeModemSimError(i);
        }
        return 915400100;
    }

    private boolean currentTimeHasPhoneCrash() {
        try {
            List listCurrentTimeSimErrorList = currentTimeSimErrorList(System.currentTimeMillis(), 300000, 0);
            if (listCurrentTimeSimErrorList != null && !listCurrentTimeSimErrorList.isEmpty()) {
                for (int i = 0; i < listCurrentTimeSimErrorList.size(); i++) {
                    if (Integer.parseInt((String) listCurrentTimeSimErrorList.get(i)) == 915400101) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            Log.e("SimStatistic", "currentTimeHasPhoneCrash exception = " + e);
        }
    }

    private boolean currentTimeHasModemCrash() {
        try {
            List listCurrentTimeSimErrorList = currentTimeSimErrorList(System.currentTimeMillis(), 300000, 0);
            if (listCurrentTimeSimErrorList != null && !listCurrentTimeSimErrorList.isEmpty()) {
                for (int i = 0; i < listCurrentTimeSimErrorList.size(); i++) {
                    if (Integer.parseInt((String) listCurrentTimeSimErrorList.get(i)) == 915400102) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            Log.e("SimStatistic", "currentTimeHasModemCrash exception = " + e);
        }
    }

    private boolean currentTimeHasSameModemErrorCode(int i, int i2) {
        try {
            List listCurrentTimeSimErrorList = currentTimeSimErrorList(System.currentTimeMillis(), 60000, i);
            if (listCurrentTimeSimErrorList != null && !listCurrentTimeSimErrorList.isEmpty()) {
                for (int i3 = 0; i3 < listCurrentTimeSimErrorList.size(); i3++) {
                    if (Integer.parseInt((String) listCurrentTimeSimErrorList.get(i3)) == i2) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            Log.e("SimStatistic", "currentTimeHasSameModemErrorCode exception = " + e);
        }
    }

    private int currentTimeModemSimError(int i) {
        try {
            List listCurrentTimeSimErrorList = currentTimeSimErrorList(System.currentTimeMillis(), 300000, i);
            if (listCurrentTimeSimErrorList != null && !listCurrentTimeSimErrorList.isEmpty()) {
                return Integer.parseInt((String) listCurrentTimeSimErrorList.get(listCurrentTimeSimErrorList.size() - 1));
            }
            return -1;
        } catch (Exception e) {
            Log.e("SimStatistic", "currentTimeModemSimError exception = " + e);
            return -1;
        }
    }

    private List currentTimeSimErrorList(long j, int i, int i2) {
        JSONObject jSONObject;
        try {
            String string = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).getString("SimNotRecognizedErrorCode", "");
            if (string.isEmpty()) {
                return null;
            }
            JSONObject jSONObject2 = new JSONObject(string);
            JSONArray jSONArray = jSONObject2.getJSONArray("order");
            JSONObject jSONObject3 = jSONObject2.getJSONObject("data");
            ArrayList arrayList = new ArrayList();
            for (int i3 = 0; i3 < jSONArray.length(); i3++) {
                long j2 = Long.parseLong(jSONArray.getString(i3));
                long j3 = i;
                if (j2 >= j - j3 && j2 <= j3 + j && (jSONObject = jSONObject3.getJSONObject(jSONArray.getString(i3))) != null) {
                    String string2 = jSONObject.getString("error_code");
                    if (Integer.parseInt(jSONObject.getString(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID)) == i2) {
                        Log.d("SimStatistic", "currentTimeSimErrorList match error code = " + string2);
                        arrayList.add(string2);
                    } else {
                        continue;
                    }
                }
            }
            return arrayList;
        } catch (Exception e) {
            Log.e("SimStatistic", "currentTimeSimErrorList exception = " + e);
            return null;
        }
    }

    private synchronized void recordSimCardInfo(int i) {
        try {
            HashMap map = new HashMap();
            map.put("simState", TelephonyManager.simStateToString(this.mTm.getSimState(i)));
            map.put("portId", Integer.valueOf(this.mPortId[i]));
            map.put("phoneId", Integer.valueOf(i));
            map.put("slotId", Integer.valueOf(this.mSlotId[i]));
            map.put("numeric", this.mTm.getSimOperatorNumericForPhone(i));
            if (SubscriptionManager.isValidSlotId(this.mSlotId[i])) {
                mIsEsimStatus[i] = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getEidFromSlotId(this.mSlotId[i]) != null;
            }
            map.put("esimState", Boolean.valueOf(mIsEsimStatus[i]));
            map.put("mepMode", UiccController.getInstance().getSupportedMepMode(this.mSlotId[i]));
            Log.d("SimStatistic", "recordSimCardInfo params = " + map);
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "sim_card_info", map);
        } catch (Throwable th) {
            throw th;
        }
    }

    private void recordMultiSimConfigChange() {
        HashMap map = new HashMap();
        map.put("isOtaUpdate", Boolean.valueOf(this.mIsOTAUpdate));
        map.put("multiConfigChangeType", Integer.valueOf(this.mMultiConfigChangeType));
        Log.d("SimStatistic", "recordMultiSimConfigChange params= " + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_multi_sim_config_change", map);
    }

    protected boolean isOTAUpdate() {
        SharedPreferences sharedPreferences = this.mStorage;
        if (sharedPreferences == null) {
            return false;
        }
        String string = sharedPreferences.getString("miui_fingerprint", "");
        String str = Build.FINGERPRINT;
        boolean zEquals = TextUtils.equals(string, str);
        boolean z = !zEquals;
        if (!zEquals) {
            this.mStorage.edit().putString("miui_fingerprint", str).apply();
        }
        if (TextUtils.equals(string, "")) {
            Log.d("SimStatistic", "isOTAUpdate is false, because the device is first used");
            return false;
        }
        Log.d("SimStatistic", "isOTAUpdate = " + z + ", lastFingerprint: " + string + ", currFingerprint: " + str);
        return z;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private int calMultiConfigChangeType(String str, String str2) {
        if (str != null && str2 != null) {
            str.hashCode();
            switch (str) {
                case "ss":
                case "ssss":
                    if (TextUtils.equals(str2, "dsds")) {
                        return 2;
                    }
                    break;
                case "dsds":
                    if (TextUtils.equals(str2, "ssss") || TextUtils.equals(str2, "ss")) {
                        return 1;
                    }
                    break;
            }
        }
        return -1;
    }

    public static String calSimAbsentDurationType(boolean z, long j) {
        TimePeriod timePeriod;
        TimePeriod timePeriod2 = TimePeriod.INVALID;
        if (z) {
            if (j < 20000) {
                timePeriod = TimePeriod.SHORT;
            } else if (j < 40000) {
                timePeriod = TimePeriod.MEDIUM;
            } else if (j < RelayUtils.Relay.RETRY_DELAY_IN_MILLIS) {
                timePeriod = TimePeriod.LONG;
            } else {
                timePeriod = TimePeriod.EXTRA_LONG;
            }
        } else if (j < RelayUtils.Relay.RETRY_DELAY_IN_MILLIS) {
            timePeriod = TimePeriod.SHORT;
        } else if (j < 300000) {
            timePeriod = TimePeriod.MEDIUM;
        } else if (j < 600000) {
            timePeriod = TimePeriod.LONG;
        } else {
            timePeriod = TimePeriod.EXTRA_LONG;
        }
        return timePeriod.getValue();
    }

    public void recordMiSimData(ServiceState[] serviceStateArr) {
        int[] customSimTypesForStatistic = getCustomSimTypesForStatistic();
        if (hasMiSimOrVirtualSim(customSimTypesForStatistic)) {
            HashMap map = new HashMap();
            FiveGCloudController fiveGCloudController = FiveGCloudController.getInstance();
            map.put("c", fiveGCloudController != null ? fiveGCloudController.getCity() : "");
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (int i2 : customSimTypesForStatistic) {
                sb.append(i2);
            }
            map.put("simCouple", sb.toString());
            String str = "";
            while (i < MiuiPhoneUtils.PHONE_COUNT) {
                int i3 = customSimTypesForStatistic[i];
                if (i3 == 2 || i3 == 3) {
                    ServiceState serviceState = serviceStateArr[i];
                    String operatorNumeric = serviceState == null ? "" : serviceState.getOperatorNumeric();
                    String simOperatorForSlot = TelephonyManagerEx.getDefault().getSimOperatorForSlot(i);
                    map.put("slotId", Integer.valueOf(i));
                    map.put("simType", Integer.valueOf(customSimTypesForStatistic[i]));
                    map.put("vplmn", operatorNumeric);
                    map.put("hplmn", simOperatorForSlot);
                    map.put("isDds", i == getDefaultDataSlotId() ? "1" : "0");
                    if (!str.isEmpty()) {
                        map.put("otherSimInfo", str);
                    }
                    str = "";
                } else if (str.isEmpty()) {
                    ServiceState serviceState2 = serviceStateArr[i];
                    str = "[otherSimState:" + TelephonyManager.simStateToString(TelephonyManagerEx.getDefault().getSimStateForSlot(i)) + ",otherSimType:" + customSimTypesForStatistic[i] + ",slotId:" + i + ",vplmn:" + (serviceState2 == null ? "" : serviceState2.getOperatorNumeric()) + ",hplmn:" + TelephonyManagerEx.getDefault().getSimOperatorForSlot(i) + "]";
                }
                i++;
            }
            if (!str.isEmpty()) {
                map.put("otherSimInfo", str);
            }
            Log.d("SimStatistic", "recordSimData params= " + map);
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "mi_sim_data", map);
        }
    }

    private int getDefaultDataSlotId() {
        return DefaultSimManager.getDefaultDataSlotId();
    }

    private int[] getCustomSimTypesForStatistic() {
        int[] iArr = new int[MiuiPhoneUtils.PHONE_COUNT];
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            iArr[i] = Utils.getCustomSimTypeForStatistic(i);
        }
        return iArr;
    }

    private boolean hasMiSimOrVirtualSim(int[] iArr) {
        for (int i : iArr) {
            if (i == 2 || i == 3) {
                return true;
            }
        }
        return false;
    }

    private boolean isEsimEnable() {
        return TelephonyManagerEx.getDefault().getEsimGPIOState() == 0;
    }

    private boolean isMatchRecordCondition(int i) {
        if (!isEsimEnable()) {
            return (i == -1 || String.valueOf(i).startsWith("9154002")) ? false : true;
        }
        Log.d("SimStatistic", "EsimEnable not record NotRecognized stat.");
        return false;
    }

    private void handleSimFaultDetected(Intent intent) {
        if (isValidSimFaultIntent(intent)) {
            int intExtra = intent.getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, -1);
            int intExtra2 = intent.getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_EVENT_TYPE, -1);
            long longExtra = intent.getLongExtra(MEModuleSimAbnormalMonitor.EXTRA_TIMESTAMP, 0L);
            boolean booleanExtra = intent.getBooleanExtra(MEModuleSimAbnormalMonitor.EXTRA_IS_BOOT_PHASE, false);
            String eventNameFromType = getEventNameFromType(intExtra2);
            if (eventNameFromType == null) {
                return;
            }
            HashMap mapBuildSimFaultParams = buildSimFaultParams(intExtra, intExtra2, longExtra);
            Log.d("SimStatistic", "[SIMFault] slot=" + intExtra + ", eventType=" + intExtra2 + ", boot=" + booleanExtra + ", event=" + eventNameFromType);
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, eventNameFromType, mapBuildSimFaultParams);
            MiuiTelephonyStatistic.getInstance().recordDFSEventStatistic(915400003, mapBuildSimFaultParams);
        }
    }

    private void handleQcomSimAbnormal(Intent intent) {
        if (intent == null) {
            Log.e("SimStatistic", "[SIMFault] QCOM intent is null");
            return;
        }
        if (isEsimEnable()) {
            Log.d("SimStatistic", "[SIMFault] eSIM enabled, skip QCOM");
            return;
        }
        int intExtra = intent.getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, -1);
        int intExtra2 = intent.getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_EVENT_TYPE, -1);
        String stringExtra = intent.getStringExtra("abnormal_info");
        long longExtra = intent.getLongExtra(MEModuleSimAbnormalMonitor.EXTRA_TIMESTAMP, 0L);
        if (intExtra < 0 || intExtra >= MiuiPhoneUtils.PHONE_COUNT) {
            Log.e("SimStatistic", "[SIMFault] QCOM invalid slotId=" + intExtra);
            return;
        }
        String qcomSimAbnormalEventName = getQcomSimAbnormalEventName(intExtra2);
        if (qcomSimAbnormalEventName == null) {
            Log.e("SimStatistic", "[SIMFault] QCOM unknown eventType=" + intExtra2);
            return;
        }
        Log.d("SimStatistic", "[SIMFault] slot=" + intExtra + ", eventType=" + intExtra2 + ", abnormalInfo=" + stringExtra + ", event=" + qcomSimAbnormalEventName);
        HashMap map = new HashMap();
        map.put(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, Integer.valueOf(intExtra));
        map.put(MEModuleSimAbnormalMonitor.EXTRA_EVENT_TYPE, Integer.valueOf(intExtra2));
        if (stringExtra == null) {
            stringExtra = "";
        }
        map.put("modem_abnormal_info", stringExtra);
        map.put(MEModuleSimAbnormalMonitor.EXTRA_TIMESTAMP, Long.valueOf(longExtra));
        map.put("hw_level", this.HW_LEVEL);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, qcomSimAbnormalEventName, map);
        MiuiTelephonyStatistic.getInstance().recordDFSEventStatistic(915400003, map);
    }

    private String getQcomSimAbnormalEventName(int i) {
        if (i == 1) {
            return "EVENT_SIM_CARD_INSERT_ERROR_INFO1";
        }
        if (i == 2) {
            return "EVENT_SIM_CARD_INSERT_ERROR_INFO2";
        }
        if (i == 3) {
            return "EVENT_BOOT_SIM_CARD_ERROR_INFO1";
        }
        if (i == 4) {
            return "EVENT_BOOT_SIM_CARD_ERROR_INFO2";
        }
        if (i != 5) {
            return null;
        }
        return "EVENT_QCOM_NAS_REJECT";
    }

    private boolean isValidSimFaultIntent(Intent intent) {
        if (intent == null) {
            Log.e("SimStatistic", "[SIMFault] intent is null");
            return false;
        }
        if (isEsimEnable()) {
            Log.d("SimStatistic", "[SIMFault] eSIM enabled, skip");
            return false;
        }
        int intExtra = intent.getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, -1);
        if (intExtra >= 0 && intExtra < MiuiPhoneUtils.PHONE_COUNT) {
            return true;
        }
        Log.e("SimStatistic", "[SIMFault] invalid slotId=" + intExtra);
        return false;
    }

    private String getEventNameFromType(int i) {
        if (i == 1) {
            return "EVENT_SIM_CARD_INSERT_ERROR_INFO1";
        }
        if (i == 2) {
            return "EVENT_SIM_CARD_INSERT_ERROR_INFO2";
        }
        if (i == 3) {
            return "EVENT_BOOT_SIM_CARD_ERROR_INFO1";
        }
        if (i == 4) {
            return "EVENT_BOOT_SIM_CARD_ERROR_INFO2";
        }
        Log.e("SimStatistic", "[SIMFault] unknown eventType=" + i);
        return null;
    }

    private HashMap buildSimFaultParams(int i, int i2, long j) {
        HashMap map = new HashMap();
        map.put(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, Integer.valueOf(i));
        map.put(MEModuleSimAbnormalMonitor.EXTRA_EVENT_TYPE, Integer.valueOf(i2));
        map.put(MEModuleSimAbnormalMonitor.EXTRA_TIMESTAMP, Long.valueOf(j));
        map.put("hw_level", this.HW_LEVEL);
        HashMap modemSimAbnormalInfo = getModemSimAbnormalInfo(i);
        if (modemSimAbnormalInfo != null && !modemSimAbnormalInfo.isEmpty()) {
            map.put("modem_abnormal_info", modemSimAbnormalInfo.toString());
        }
        return map;
    }

    private HashMap getModemSimAbnormalInfo(int i) {
        if (ModemProvider.IS_QCOM || !isValidPhoneId(i)) {
            return null;
        }
        try {
            Object[] objArrExecuteAtCommand = executeAtCommand(i);
            if (objArrExecuteAtCommand == null) {
                return null;
            }
            return parseAtResponse(objArrExecuteAtCommand);
        } catch (Exception e) {
            Log.e("SimStatistic", "getModemSimAbnormalInfo exception: " + e.getMessage());
            return null;
        }
    }

    private boolean isValidPhoneId(int i) {
        if (i >= 0 && i < MiuiPhoneUtils.PHONE_COUNT) {
            return true;
        }
        Log.e("SimStatistic", "getModemSimAbnormalInfo invalid phoneId: " + i);
        return false;
    }

    private Object[] executeAtCommand(int i) {
        if (PhoneFactory.getPhone(i) == null) {
            Log.e("SimStatistic", "getModemSimAbnormalInfo phone is null for phoneId=" + i);
            return null;
        }
        ATCommandUtils aTCommandUtils = ATCommandUtils.getInstance();
        if (aTCommandUtils == null) {
            Log.e("SimStatistic", "getModemSimAbnormalInfo ATCommandUtils is null");
            return null;
        }
        Object[] objArr = new Object[1];
        if (aTCommandUtils.atCmdRunSync(i, new String[]{"AT+EDMFAPP=20100,487", "+EDMFAPP"}, objArr)) {
            return objArr;
        }
        Log.e("SimStatistic", "getModemSimAbnormalInfo AT command failed");
        return null;
    }

    private HashMap parseAtResponse(Object[] objArr) {
        if (objArr == null || objArr.length == 0) {
            Log.d("SimStatistic", "getModemSimAbnormalInfo atResult is null or empty");
            return null;
        }
        String[] strArr = (String[]) objArr[0];
        if (strArr != null) {
            if (strArr.length > 1) {
                for (int i = 1; i < strArr.length; i++) {
                    Log.d("SimStatistic", "getModemSimAbnormalInfo response[" + i + "]: " + strArr[i]);
                    HashMap singleLine = parseSingleLine(strArr[i]);
                    if (singleLine != null) {
                        return singleLine;
                    }
                }
                return null;
            }
        }
        Log.d("SimStatistic", "getModemSimAbnormalInfo empty response");
        return null;
    }

    private HashMap parseSingleLine(String str) {
        if (str == null) {
            return null;
        }
        String[] strArrSplit = str.split(",");
        if (strArrSplit.length < 6) {
            return null;
        }
        String str2 = strArrSplit[1];
        String str3 = strArrSplit[2];
        String str4 = strArrSplit[3];
        String str5 = strArrSplit[4];
        String str6 = strArrSplit[5];
        HashMap map = new HashMap();
        map.put("modem_version", str2 != null ? str2 : "");
        map.put("modem_slot_id", str3 != null ? str3 : "");
        map.put("modem_app_mode", str4 != null ? str4 : "");
        map.put("modem_timestamp", str5 != null ? str5 : "");
        map.put("modem_event_type", str6 != null ? str6 : "");
        Log.d("SimStatistic", "getModemSimAbnormalInfo parsed: version=" + str2 + ", slotId=" + str3 + ", appMode=" + str4 + ", timestamp=" + str5 + ", eventType=" + str6);
        return map;
    }

    public static void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("Sim not recognized dump start------");
        try {
            String string = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).getString("SimNotRecognizedErrorCode", "");
            if (!TextUtils.isEmpty(string)) {
                JSONObject jSONObject = new JSONObject(string);
                JSONArray jSONArray = jSONObject.getJSONArray("order");
                JSONObject jSONObject2 = jSONObject.getJSONObject("data");
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject3 = jSONObject2.getJSONObject(jSONArray.getString(i));
                    if (jSONObject3 != null) {
                        String string2 = jSONObject3.getString("error_code");
                        String string3 = jSONObject3.getString(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID);
                        printWriter.println("Time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(Long.parseLong(jSONArray.getString(i)))) + ";    Error code:" + string2 + ";    Slot id:" + string3 + ";");
                    }
                }
            }
        } catch (Exception e) {
            Log.e("SimStatistic", "dump error", e);
        }
        printWriter.println("Sim not recognized dump end------");
        printWriter.flush();
    }
}
