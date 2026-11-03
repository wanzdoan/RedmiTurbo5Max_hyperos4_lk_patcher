package com.xiaomi.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.PhoneStateListener;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.GsmCdmaCall;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCall;
import com.xiaomi.modem.ModemUtils;
import com.xiaomi.modem.OemHookAgent;
import java.util.ArrayList;
import java.util.Arrays;
import miui.telephony.CommenUtils;

/* JADX INFO: loaded from: classes.dex */
public class ModemServiceMoniter {
    private static final String ACTION_MIUI_POWERKEEPER_SLEEP_CHANGED = "com.miui.powerkeeper_sleep_changed";
    private static final String BC_ACTION_MTB_IS_RUNNING = "xiaomi.intent.action.MTB_IS_RUNNING";
    private static final String BC_ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";
    private static final String BUNDLE_CALL_STATUS = "CALL_STATUS";
    private static final String BUNDLE_PHONE_ID = "PHONE_ID";
    private static final String BUNDLE_SCREEN_STATUS = "SCREEN_STATUS";
    private static final String BUNDLE_UIM_STATE = "UIM_STATE";
    private static final int BYTE_SIZE = 1;
    private static final int CALL_OFF = 0;
    private static final int CALL_ON = 1;
    public static final int EVENT_OEMHOOK_CALL_STATUS_NOTIFY = 47;
    public static final int EVENT_OEMHOOK_DDS_CHANGE_NOTIFY = 48;
    public static final int EVENT_OEMHOOK_NOTIFY_MODEM_SYNC_DONE = 113;
    public static final int EVENT_OEMHOOK_NOTIFY_SHUTDOWN = 14;
    public static final int EVENT_OEMHOOK_NOTIFY_UIM_STATE = 22;
    public static final int EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY = 27;
    public static final int EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX = 114;
    private static final int INIT_STATE_IDLE = 0;
    private static final int INIT_STATE_PRE_DONE = 1;
    public static final String INTENT_START_AUTO_CAPTURE_LOG_SERVICE = "xiaomi.intent.action.ACTION_START_AUTO_CAPTURE_LOG_SERVICE";
    private static final int INT_SIZE = 4;
    private static final String LOG_TAG = "ModemServiceMoniter";
    private static final int MODEM_SLEEP_OFF_CHECK_NUM_MAX = 3;
    private static final int MODEM_SLEEP_ON_CHECK_NUM_MAX = 3;
    private static final int POWERKEEPER_SLEEP_OFF = 2;
    private static final int POWERKEEPER_SLEEP_ON = 1;
    private static final String POWERKEEPER_SLEEP_STATE = "state";
    public static final String PROPERTY_AUTO_CAPTURE_LOG_MASK = "persist.radio.auto_capture_log_mask";
    public static final int PROPERTY_AUTO_CAPTURE_LOG_MASK_ALL = 1;
    public static final int PROPERTY_AUTO_CAPTURE_LOG_MASK_DEFAULT = -1;
    private static final String PROPERTY_CALL_ON_OFF_MONITER_CONTROL = "persist.vendor.radio.moniter_call";
    private static final boolean PROPERTY_CALL_ON_OFF_MONITER_CONTROL_DEF = false;
    private static final String PROPERTY_DDS_CHNAGE_MONITER_CONTROL = "persist.vendor.radio.moniter_dds";
    private static final boolean PROPERTY_DDS_CHNAGE_MONITER_CONTROL_DEF = false;
    private static final String PROPERTY_MODEM_SERVICE_MAIN_MONITER_CONTROL = "persist.vendor.radio.moniter_main";
    private static final boolean PROPERTY_MODEM_SERVICE_MAIN_MONITER_CONTROL_DEF = true;
    private static final String PROPERTY_PHONE_INIT_COUNT = "debug.modem.phone.init_count";
    private static final String PROPERTY_POWER_CONNECT_MONITER_CONTROL = "persist.vendor.radio.moniter_power";
    private static final boolean PROPERTY_POWER_CONNECT_MONITER_CONTROL_DEF = true;
    private static final int PROPERTY_SCREEN_ON_OFF_IND_INTERVAL_S_DEF = 5;
    private static final String PROPERTY_SCREEN_ON_OFF_MONITER_CONTROL = "persist.vendor.radio.moniter_screen";
    private static final boolean PROPERTY_SCREEN_ON_OFF_MONITER_CONTROL_DEF = false;
    private static final String PROPERTY_SHUTDOWN_MONITER_CONTROL = "persist.vendor.radio.moniter_shutdown";
    private static final boolean PROPERTY_SHUTDOWN_MONITER_CONTROL_DEF = false;
    private static final String PROPERTY_SLEEP_CHANGED_MONITER_CONTROL = "persist.vendor.radio.moniter_sleep";
    private static final boolean PROPERTY_SLEEP_CHANGED_MONITER_CONTROL_DEF = true;
    private static final String PROPERTY_UIM_STATE_IND_INTERVAL_S = "persist.radio.uim_state_ind_interval_s";
    private static final int PROPERTY_UIM_STATE_IND_INTERVAL_S_DEF = 30;
    private static final String PROPERTY_UIM_STATE_MONITER_CONTROL = "persist.vendor.radio.moniter_uim_state";
    private static final boolean PROPERTY_UIM_STATE_MONITER_CONTROL_DEF = true;
    private static final String PROPERTY_USB_MONITER_CONTROL = "persist.vendor.radio.moniter_usb";
    private static final boolean PROPERTY_USB_MONITER_CONTROL_DEF = true;
    private static final String PROP_MIUI_BUILD_REGION = "ro.miui.build.region";
    private static final String PROP_MIUI_BUILD_REGION_CN = "cn";
    private static final String PROP_MIUI_BUILD_REGION_DEF = "none";
    private static final String PROP_MIUI_BUILD_REGION_VENDOR = "ro.vendor.miui.build.region";
    private static final String PROP_MTB_IS_STARTED_NO = "0";
    private static final String PROP_MTB_IS_STARTED_SYS = "debug.mtb.running";
    private static final String PROP_MTB_IS_STARTED_YES = "1";
    private static final String PROP_MTB_POWER_ON_START_OFF = "0";
    private static final String PROP_MTB_POWER_ON_START_ON = "1";
    private static final String PROP_MTB_POWER_ON_START_SWITCH = "persist.radio.mtb_power_on_start";
    private static final String PROP_RADIO_BUILD_REGION_VENDOR = "ro.vendor.radio.build_region";
    private static final String PROP_TEMP_BUILD_REGION_VENDOR = "persist.vendor.radio.build_region";
    private static final int QCRIL_REQ_HOOK_OEM_COMMON = 524970;
    private static final int SCREEN_OFF = 0;
    private static final int SCREEN_ON = 1;
    private static final int SIM_STATE_ERROR = 2;
    private static final int SIM_STATE_NOT_READY = 1;
    private static final int SIM_STATE_READY = 0;
    private static final int STATE_CALL_STATUS_NOTIFY = 2;
    private static final int STATE_CHECK_DIAG_DEADLINE = 6;
    private static final int STATE_DDS_CHANGE_NOTIFY = 3;
    private static final int STATE_IDLE = 0;
    private static final int STATE_MODEM_SLEEP_STATE_CHECK = 8;
    private static final int STATE_POWERKEEPER_SLEEP_CHANGED = 7;
    private static final int STATE_SCREEN_STATUS_NOTIFY = 1;
    private static final int STATE_SHUTDOWN_NOTIFY = 4;
    private static final int STATE_UIM_STATE_NOTIFY = 5;
    private static final boolean mPowerConnectMoniterDiag = true;
    private static final boolean mScreenOnOffMoniterNotifyHal = false;
    private static final boolean mSleepChangedMoniterNotifyModem = true;
    private static final boolean mUsbAttachedMoniterDiag = true;
    private static ModemServiceMoniter sIntance;
    private int[] mCallState;
    private Context mContext;
    private int mNumPhones;
    private Phone[] mPhones;
    private TelephonyManager mTelephonyMgr;
    public static String mDeviceName = "NULL";
    public static String mDeviceHwInfo = "NULL";
    public static String mDeviceModName = "NULL";
    public static String mDeviceHwLevel = "NULL";
    public static String mBuildRegion = "none";
    public static String mCertType = "none";
    public static String mFactoryBuild = "NULL";
    public static String mRootType = "NULL";
    public static String mBuildVersion = "";
    public static String mBuildTags = "NULL";
    public static int mSdkVersion = 0;
    public static int mAndroidVersion = 0;
    public static int mOemProjectFlag = 0;
    public static int mHalChipId = 0;
    public static int mBpChipId = 0;
    public static int mManufacturerId = 0;
    public static String mBuildProfile = "NULL";
    public static boolean mXringHalFlag = false;
    private static OemHookAgent mHookAgent = null;
    private static final String HOOK_IDENTIFIER = "QOEMHOOK";
    private static final int HOOK_HEAD_SIZE = HOOK_IDENTIFIER.length() + 8;
    private static int mInitState = 0;
    private static MtbStartThread sIntanceMtb = null;
    private static boolean mMtbIsRunning = false;
    private static int mPowerkeeperSleepState = 2;
    private static ArrayList<String> DEVICE_MTB_SUPPORT_LIST = new ArrayList<>(Arrays.asList(ModemUtils.PROP_DEVICE_NAME_L11R));
    private static BroadcastReceiver mBroadcastReceiverForMtbRuningState = new BroadcastReceiver() { // from class: com.xiaomi.internal.telephony.ModemServiceMoniter.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            ModemServiceMoniter.log("===================== mBroadcastReceiverForMtbRuningState action: " + action);
            if (ModemServiceMoniter.BC_ACTION_MTB_IS_RUNNING.equals(action)) {
                ModemServiceMoniter.log("BC_ACTION_MTB_IS_RUNNING");
                ModemServiceMoniter.mMtbIsRunning = true;
            } else {
                ModemServiceMoniter.log("UNKNOWN action");
            }
        }
    };
    private boolean mQcRilHookReady = false;
    private OemCallStateNotifier mCallNotifier = null;
    private final int HOOK_TIMER_LEN_MAX = ModemUtils.XIAOMI_MODEM_HAL_CHIP_ID_MAX;
    private final int SUB_0 = 0;
    private final int SUB_1 = 1;
    private int mScreenOnOffIndIntervalS = 5;
    private int mCheckDiagDeadlineIntervalS = 2;
    private int mUimStateIndIntervalS = 30;
    private int mUsbAttachedMoniterDiagIntervalS = 20;
    private int mSleepOffMoniterNotifyModemDelayS = 1;
    private int mModemSleepOffCheckDelayS = 5;
    private int mModemSleepOnCheckDelayS = 2;
    private int mModemSleepStateCheckNum = 0;
    private int mOptFlag = 0;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.xiaomi.internal.telephony.ModemServiceMoniter.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            ModemServiceMoniter.log("===================== mBroadcastReceiver action: " + action);
            if ("android.intent.action.SCREEN_ON".equals(action)) {
                ModemServiceMoniter.log("Intent.ACTION_SCREEN_ON");
                ModemServiceMoniter.log("mScreenOnOffMoniterNotifyHal is closed for ACTION_SCREEN_ON");
                return;
            }
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
                ModemServiceMoniter.log("Intent.ACTION_SCREEN_OFF");
                ModemServiceMoniter.log("mScreenOnOffMoniterNotifyHal is closed for ACTION_SCREEN_OFF");
                return;
            }
            if ("android.intent.action.PHONE_STATE".equals(action)) {
                ModemServiceMoniter.log("TelephonyManager.ACTION_PHONE_STATE_CHANGED");
                return;
            }
            if ("android.intent.action.NEW_OUTGOING_CALL".equals(action)) {
                ModemServiceMoniter.log("Intent.ACTION_NEW_OUTGOING_CALL");
                return;
            }
            if ("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED".equals(action)) {
                ModemServiceMoniter.log("TelephonyIntents.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
                ModemServiceMoniter.this.onMsgPkDdsChangeNotify();
                return;
            }
            if ("android.intent.action.ACTION_SHUTDOWN".equals(action)) {
                ModemServiceMoniter.log("BC_ACTION_SHUTDOWN");
                ModemServiceMoniter.this.onMsgPkShutdownNotify();
                return;
            }
            if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                ModemServiceMoniter.log("ACTION_SIM_STATE_CHANGED");
                ModemServiceMoniter.this.onMsgPkUimStateNotify(context, intent);
                return;
            }
            if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action)) {
                ModemServiceMoniter.log("UsbManager.ACTION_USB_DEVICE_ATTACHED");
                ModemServiceMoniter.this.onMsgCheckDiagDeadline();
                return;
            }
            if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                ModemServiceMoniter.log("UsbManager.ACTION_USB_DEVICE_DETACHED");
                ModemServiceMoniter.this.onMsgCheckDiagDeadline();
                return;
            }
            if ("android.intent.action.ACTION_POWER_CONNECTED".equals(action)) {
                ModemServiceMoniter.log("Intent.ACTION_POWER_CONNECTED");
                ModemServiceMoniter.this.onMsgCheckDiagDeadline();
            } else if ("android.intent.action.ACTION_POWER_DISCONNECTED".equals(action)) {
                ModemServiceMoniter.log("Intent.ACTION_POWER_DISCONNECTED");
                ModemServiceMoniter.this.onMsgCheckDiagDeadline();
            } else if ("com.miui.powerkeeper_sleep_changed".equals(action)) {
                ModemServiceMoniter.log("ACTION_MIUI_POWERKEEPER_SLEEP_CHANGED");
                ModemServiceMoniter.this.onMsgNotifyModemForSleepChanged(intent);
            } else {
                ModemServiceMoniter.log("UNKNOWN action");
            }
        }
    };
    PhoneStateListener mListener = new PhoneStateListener(this) { // from class: com.xiaomi.internal.telephony.ModemServiceMoniter.4
        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case 0:
                    ModemServiceMoniter.log("TelephonyManager.CALL_STATE_IDLE");
                    break;
                case 1:
                    ModemServiceMoniter.log("TelephonyManager.CALL_STATE_RINGING");
                    break;
                case 2:
                    ModemServiceMoniter.log("TelephonyManager.CALL_STATE_OFFHOOK");
                    break;
                default:
                    ModemServiceMoniter.log("Unknown TelephonyManager STATE");
                    break;
            }
        }
    };
    protected Handler mHandler = new Handler() { // from class: com.xiaomi.internal.telephony.ModemServiceMoniter.5
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            ModemServiceMoniter.log("handleMessage msg id: " + msg.what);
            ModemServiceMoniter.this.mOptFlag = msg.what;
            new Bundle();
            Bundle data = msg.getData();
            switch (msg.what) {
                case 1:
                    ModemServiceMoniter.this.onMsgUnPkScreenStatusNotify(data);
                    break;
                case 2:
                    ModemServiceMoniter.this.onMsgUnPkCallStatusNotify(data);
                    break;
                case 3:
                    ModemServiceMoniter.this.onMsgUnPkDdsChangeNotify();
                    break;
                case 4:
                    ModemServiceMoniter.this.onMsgUnPkShutdownNotify();
                    break;
                case 5:
                    ModemServiceMoniter.this.onMsgUnPkUimStateNotify(data);
                    break;
                case 6:
                    ModemServiceMoniter.this.onMsgCheckDiagDeadlineDo();
                    break;
                case 7:
                    ModemServiceMoniter.this.onMsgUnPkNotifyModemForSleepChanged(data);
                    break;
                case 8:
                    ModemServiceMoniter.this.onMsgUnPkCheckModemForSleepState();
                    break;
                default:
                    ModemServiceMoniter.log("invalid msg id: " + msg.what);
                    break;
            }
        }
    };

    public static void onMtbServiceWakeUp(final Context context) {
        log("onMtbServiceWakeUp, ModemUtils.getDeviceHwLevel = " + ModemUtils.getDeviceHwLevel());
        if (ModemUtils.getDeviceHwLevel().equals(ModemUtils.PROPERTY_DEVICE_INFO_HW_LEVEL_MP)) {
            log("onMtbServiceWakeUp, MP machine, return.");
            return;
        }
        int autoCaptureLogMask = SystemProperties.getInt(PROPERTY_AUTO_CAPTURE_LOG_MASK, -1);
        log("autoCaptureLogMask = " + autoCaptureLogMask);
        if (1 == autoCaptureLogMask) {
            log("MtbLogManagerService will start after 5s");
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.ModemServiceMoniter.1
                @Override // java.lang.Runnable
                public void run() {
                    ModemServiceMoniter.log("MtbLogManagerService will start now");
                    Intent intent = new Intent(ModemServiceMoniter.INTENT_START_AUTO_CAPTURE_LOG_SERVICE);
                    intent.setPackage("com.xiaomi.mtb");
                    context.sendBroadcast(intent);
                }
            }, 5000L);
        }
    }

    public boolean onMsgUnPkScreenStatusNotify(Bundle data) {
        if (data == null) {
            log("onMsgUnPkScreenStatusNotify, data is null");
            return false;
        }
        int screenStatus = data.getInt(BUNDLE_SCREEN_STATUS);
        log("screenStatus: " + screenStatus);
        mHookAgent.onHookCommonMsg(EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX, screenStatus);
        return true;
    }

    public boolean onMsgPkScreenStatusNotify(int status) {
        log("onMsgPkScreenStatusNotify, status = " + status);
        if (status != 0 && 1 != status) {
            log("status is invalid");
            return false;
        }
        Message msg = Message.obtain();
        msg.what = 1;
        Bundle data = new Bundle();
        data.putInt(BUNDLE_SCREEN_STATUS, status);
        msg.setData(data);
        log("Re-start the timer, " + this.mScreenOnOffIndIntervalS + " seconds later, will notify rild for ACTION_SCREEN_ON or ACTION_SCREEN_OFF");
        onRemoveMsg(1);
        onSendMsg(msg, ((long) this.mScreenOnOffIndIntervalS) * 1000);
        return true;
    }

    public boolean onMsgUnPkCallStatusNotify(Bundle data) {
        if (data == null) {
            log("onMsgUnPkCallStatusNotify, data is null");
            return false;
        }
        int callStatus = data.getInt(BUNDLE_CALL_STATUS);
        int phoneId = data.getInt(BUNDLE_PHONE_ID);
        log("callStatus: " + callStatus + ", phoneId = " + phoneId);
        mHookAgent.onHookCommonMsg(47, callStatus);
        return true;
    }

    public boolean onMsgPkCallStatusNotify(int status, int phoneId) {
        log("onMsgPkCallStatusNotify, status = " + status + ", phoneId = " + phoneId);
        if (status != 0 && 1 != status) {
            log("status is invalid");
            return false;
        }
        if (phoneId < 0 || phoneId >= this.mNumPhones) {
            log("phoneId is invalid");
            return false;
        }
        if (this.mCallState[phoneId] == status) {
            log("call state no change");
            return true;
        }
        this.mCallState[phoneId] = status;
        Message msg = Message.obtain();
        msg.what = 2;
        Bundle data = new Bundle();
        data.putInt(BUNDLE_CALL_STATUS, status);
        data.putInt(BUNDLE_PHONE_ID, phoneId);
        msg.setData(data);
        this.mHandler.sendMessage(msg);
        return true;
    }

    public boolean onMsgUnPkDdsChangeNotify() {
        int newDefaultDataSub = SubscriptionManager.getDefaultDataSubscriptionId();
        int phoneId = SubscriptionManager.getPhoneId(newDefaultDataSub);
        log("onMsgUnPkDdsChangeNotify, newDefaultDataSub = " + newDefaultDataSub + ", phoneId = " + phoneId);
        mHookAgent.onHookCommonMsgBySub(48, phoneId);
        return true;
    }

    public boolean onMsgPkDdsChangeNotify() {
        log("onMsgPkDdsChangeNotify");
        this.mHandler.sendEmptyMessage(3);
        return true;
    }

    public boolean onMsgUnPkShutdownNotify() {
        log("onMsgUnPkShutdownNotify");
        mHookAgent.onHookCommonMsg(14);
        return true;
    }

    public boolean onMsgPkShutdownNotify() {
        log("onMsgPkShutdownNotify");
        Message msg = Message.obtain();
        msg.what = 4;
        this.mHandler.sendMessage(msg);
        return true;
    }

    public boolean onMsgUnPkUimStateNotify(Bundle data) {
        if (data == null) {
            log("onMsgUnPkUimStateNotify, data is null");
            return false;
        }
        int state = data.getInt(BUNDLE_UIM_STATE);
        log("onMsgUnPkUimStateNotify, state = " + state);
        mHookAgent.onHookCommonMsg(22, state);
        log("onMsgUnPkUimStateNotify, call onMsgCheckDiagDeadline");
        onMsgCheckDiagDeadline();
        return true;
    }

    public boolean onMsgPkUimStateNotify(Context context, Intent intent) {
        String stateName;
        int simState;
        log("onMsgPkUimStateNotify, context = " + context);
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        int state = tm.getSimState();
        String stateExtra = intent.getStringExtra("ss");
        if (5 == state) {
            stateName = "SIM_STATE_READY";
        } else if (state == 0) {
            stateName = "SIM_STATE_UNKNOWN";
        } else if (1 == state) {
            stateName = "SIM_STATE_ABSENT";
        } else if (2 == state) {
            stateName = "SIM_STATE_PIN_REQUIRED";
        } else if (3 == state) {
            stateName = "SIM_STATE_PUK_REQUIRED";
        } else if (4 == state) {
            stateName = "SIM_STATE_NETWORK_LOCKED";
        } else {
            stateName = "SIM_STATE_INVALID";
        }
        if ("LOADED".equals(stateExtra) || "IMSI".equals(stateExtra) || "READY".equals(stateExtra)) {
            simState = 0;
        } else if ("ABSENT".equals(stateExtra) || "UNKNOWN".equals(stateExtra) || "CARD_IO_ERROR".equals(stateExtra)) {
            simState = 2;
        } else {
            simState = 1;
        }
        log("Re-start the timer, " + this.mUimStateIndIntervalS + " seconds later, will notify rild for ACTION_SIM_STATE_CHANGED-" + stateName + "(" + state + "), stateExtra: " + stateExtra + ", simState: " + simState);
        Message msg = Message.obtain();
        msg.what = 5;
        Bundle data = new Bundle();
        data.putInt(BUNDLE_UIM_STATE, simState);
        msg.setData(data);
        onRemoveMsg(5);
        onSendMsg(msg, ((long) this.mUimStateIndIntervalS) * 1000);
        return true;
    }

    public boolean onMsgCheckDiagDeadline() {
        log("onMsgCheckDiagDeadline");
        Message msg = Message.obtain();
        msg.what = 6;
        log("Re-start the timer, " + this.mCheckDiagDeadlineIntervalS + " seconds later, will check diag");
        onRemoveMsg(6);
        onSendMsg(msg, ((long) this.mCheckDiagDeadlineIntervalS) * 1000);
        return true;
    }

    public void onMsgCheckDiagDeadlineDo() {
        log("onMsgCheckDiagDeadlineDo");
        diagDeadlineCheck();
    }

    public void diagDeadlineCheck() {
        log("diagDeadlineCheck");
        long deadline = ModemUtils.getDiagDeadlineFromProp();
        String deadlineStr = ModemUtils.timeLongToString(deadline);
        log("diagDeadlineCheck, deadline = " + deadline + ", deadlineStr = " + deadlineStr);
        if (deadline <= 0) {
            log("diagDeadlineCheck, deadline is not cfged, do nothing");
            return;
        }
        long curDate = ModemUtils.getCurrentTimeWithLong();
        String curDateStr = ModemUtils.timeLongToString(curDate);
        log("diagDeadlineCheck, curDate = " + curDate + ", curDateStr = " + curDateStr);
        if (deadline >= curDate) {
            log("diagDeadlineCheck, not expired, do nothing");
            return;
        }
        boolean state = ModemUtils.onModemDiagIsOpen();
        log("diagDeadlineCheck, diag port state = " + state);
        if (state) {
            log("diagDeadlineCheck, will disable the diag USB port");
            onNotifyMtbCloseDiagPort();
        } else {
            log("diagDeadlineCheck, the diag USB port is closed, will do nothing");
        }
    }

    public void onNotifyMtbCloseDiagPort() {
        log("onNotifyMtbCloseDiagPort");
        if (this.mContext == null) {
            log("onNotifyMtbCloseDiagPort, mContext is null");
            return;
        }
        Intent intent = new Intent("xiaomi.intent.action.NOTIFY_MTB_CLOSE_DIAG_PORT");
        intent.setPackage("com.xiaomi.mtb");
        this.mContext.sendBroadcast(intent);
    }

    public void onMsgNotifyModemForSleepChanged(Intent intent) {
        if (intent == null) {
            log("onMsgNotifyModemForSleepChanged, intent is null, oldState = " + mPowerkeeperSleepState);
            return;
        }
        int newState = intent.getIntExtra("state", 2);
        log("onMsgNotifyModemForSleepChanged, newState = " + newState + ", oldState = " + mPowerkeeperSleepState);
        Message msg = Message.obtain();
        msg.what = 7;
        Bundle data = new Bundle();
        data.putInt("state", newState);
        msg.setData(data);
        if (2 == newState) {
            onRemoveMsg(7);
            onSendMsg(msg, ((long) this.mSleepOffMoniterNotifyModemDelayS) * 1000);
            log("Re-start the timer, " + this.mSleepOffMoniterNotifyModemDelayS + " seconds later, will notify rild for POWERKEEPER_SLEEP_OFF");
        } else {
            onSendMsg(msg);
            log("Will notify rild for POWERKEEPER_SLEEP_ON");
        }
    }

    public void onMsgUnPkNotifyModemForSleepChanged(Bundle data) {
        int newState;
        if (data == null) {
            log("onMsgUnPkNotifyModemForSleepChanged, data is null, oldState = " + mPowerkeeperSleepState + ", mModemSleepStateCheckNum = " + this.mModemSleepStateCheckNum);
            return;
        }
        if (mHookAgent == null) {
            log("onMsgUnPkNotifyModemForSleepChanged, mHookAgent is null, oldState = " + mPowerkeeperSleepState + ", mModemSleepStateCheckNum = " + this.mModemSleepStateCheckNum);
            return;
        }
        int sleepingState = data.getInt("state");
        boolean isDataEnabled = ModemUtils.isMobileDataEnabled(this.mContext);
        int insertedSimCount = ModemUtils.getInsertedSimCount();
        log("onMsgUnPkNotifyModemForSleepChanged, sleepingState = " + sleepingState + ", isDataEnabled = " + isDataEnabled + ", insertedSimCount = " + insertedSimCount);
        if (1 == sleepingState && (!isDataEnabled || insertedSimCount == 0)) {
            newState = 1;
        } else {
            newState = 2;
        }
        log("onMsgUnPkNotifyModemForSleepChanged, newState = " + newState + ", oldState = " + mPowerkeeperSleepState + ", mModemSleepStateCheckNum = " + this.mModemSleepStateCheckNum);
        if (newState == mPowerkeeperSleepState) {
            log("onMsgUnPkNotifyModemForSleepChanged, state is no changed");
            return;
        }
        mPowerkeeperSleepState = newState;
        mHookAgent.onHookNotifyPowerkeeperSleepStatus(mPowerkeeperSleepState);
        log("onMsgUnPkNotifyModemForSleepChanged, onHookNotifyPowerkeeperSleepStatus is sent");
        this.mModemSleepStateCheckNum = 0;
        log("Will start the timer for check sleep state of modem, mModemSleepStateCheckNum = " + this.mModemSleepStateCheckNum);
        onMsgCheckModemForSleepState();
    }

    public void onMsgCheckModemForSleepState() {
        int checkDelayS;
        Message msg = Message.obtain();
        msg.what = 8;
        if (2 == mPowerkeeperSleepState) {
            checkDelayS = this.mModemSleepOffCheckDelayS;
            log("onMsgCheckModemForSleepState, Re-start the timer, " + checkDelayS + " seconds later, will check POWERKEEPER_SLEEP_OFF of modem");
        } else {
            checkDelayS = this.mModemSleepOnCheckDelayS;
            log("onMsgCheckModemForSleepState, Re-start the timer, " + checkDelayS + " seconds later, will check POWERKEEPER_SLEEP_ON of modem");
        }
        onRemoveMsg(8);
        onSendMsg(msg, ((long) checkDelayS) * 1000);
    }

    public void onMsgUnPkCheckModemForSleepState() {
        if (mHookAgent == null) {
            log("onMsgUnPkCheckModemForSleepState, mHookAgent is null, mPowerkeeperSleepState = " + mPowerkeeperSleepState);
            return;
        }
        int modemSleepState = mHookAgent.getModemSleepState();
        log("onMsgUnPkCheckModemForSleepState, modemSleepState = " + modemSleepState + ", mPowerkeeperSleepState = " + mPowerkeeperSleepState + ", mModemSleepStateCheckNum = " + this.mModemSleepStateCheckNum + ", MODEM_SLEEP_ON_CHECK_NUM_MAX = 3, MODEM_SLEEP_OFF_CHECK_NUM_MAX = 3");
        if (modemSleepState == mPowerkeeperSleepState) {
            this.mModemSleepStateCheckNum = 0;
            log("onMsgUnPkCheckModemForSleepState, The sleep state of Modem and AP is consistent");
            return;
        }
        this.mModemSleepStateCheckNum++;
        int i = mPowerkeeperSleepState;
        int i2 = this.mModemSleepStateCheckNum;
        if (2 == i) {
            if (i2 >= 3) {
                log("onMsgUnPkCheckModemForSleepState, The sleep state of Modem and AP is not consistent, The check num for POWERKEEPER_SLEEP_OFF is out of range, will abort it.");
                return;
            }
        } else if (i2 >= 3) {
            log("onMsgUnPkCheckModemForSleepState, The sleep state of Modem and AP is not consistent, The check num for POWERKEEPER_SLEEP_ON is out of range, will abort it.");
            return;
        }
        log("onMsgUnPkCheckModemForSleepState, The sleep state of Modem and AP is not consistent, will retry to notify modem, mPowerkeeperSleepState = " + mPowerkeeperSleepState + ", mModemSleepStateCheckNum = " + this.mModemSleepStateCheckNum);
        mHookAgent.onHookNotifyPowerkeeperSleepStatus(mPowerkeeperSleepState);
        log("At the same will start the timer for check sleep state of modem, mModemSleepStateCheckNum = " + this.mModemSleepStateCheckNum);
        onMsgCheckModemForSleepState();
    }

    public static String getBuildRegion() {
        return SystemProperties.get("ro.miui.build.region", "none");
    }

    public static String getTempBuildRegion() {
        return SystemProperties.get(PROP_TEMP_BUILD_REGION_VENDOR, "none");
    }

    public void notifyBuildRegion() {
        String strBuildRegion = getBuildRegion();
        String strTempBuildRegion = getTempBuildRegion();
        log("notifyBuildRegion, strBuildRegion = " + strBuildRegion + ", strTempBuildRegion = " + strTempBuildRegion);
        if (mHookAgent == null) {
            log("notifyBuildRegion, mHookAgent is null, do nothing");
            return;
        }
        if (strBuildRegion == null || "none".equals(strBuildRegion)) {
            log("notifyBuildRegion, BuildRegion is null, do nothing");
            return;
        }
        if (strBuildRegion.equals(strTempBuildRegion)) {
            log("notifyBuildRegion, TempBuildRegion is same with BuildRegion, do nothing");
            return;
        }
        boolean ret = mHookAgent.onHookPropSet(PROP_TEMP_BUILD_REGION_VENDOR, strBuildRegion);
        if (!ret) {
            log("notifyBuildRegion, TempBuildRegion update failed");
            return;
        }
        String strTempBuildRegion2 = getTempBuildRegion();
        log("notifyBuildRegion, new strTempBuildRegion = " + strTempBuildRegion2);
        if (strBuildRegion.equals(strTempBuildRegion2)) {
            log("notifyBuildRegion, TempBuildRegion sync ok, will notify rild");
        } else {
            log("notifyBuildRegion, TempBuildRegion sync fail, do nothing");
        }
        boolean ret2 = mHookAgent.onHookCommonMsg(EVENT_OEMHOOK_NOTIFY_MODEM_SYNC_DONE);
        if (!ret2) {
            log("notifyBuildRegion, Maybe not support this hook interface");
        }
    }

    public void onSendMsg(int msg_id) {
        this.mHandler.sendEmptyMessage(msg_id);
    }

    public void onSendMsg(Message msg) {
        this.mHandler.sendMessage(msg);
    }

    public void onSendMsg(int msg_id, long delayMillis) {
        this.mHandler.sendEmptyMessageDelayed(msg_id, delayMillis);
    }

    public void onSendMsg(Message msg, long delayMillis) {
        this.mHandler.sendMessageDelayed(msg, delayMillis);
    }

    public void onRemoveMsg(int msg_id) {
        this.mHandler.removeMessages(msg_id);
    }

    public void onInit() {
        log("onInit");
        if (this.mContext == null) {
            log("mContext is null");
            return;
        }
        IntentFilter filter = new IntentFilter();
        boolean enabled = SystemProperties.getBoolean(PROPERTY_SCREEN_ON_OFF_MONITER_CONTROL, false);
        log("PROPERTY_SCREEN_ON_OFF_MONITER_CONTROL: " + enabled);
        if (true == enabled) {
            log("Screen on-off moniter switch is opened, will listen its broadcast!");
            filter.addAction("android.intent.action.SCREEN_ON");
            filter.addAction("android.intent.action.SCREEN_OFF");
        } else {
            log("Screen on-off moniter switch is closed, will not listen its broadcast!");
        }
        boolean enabled2 = SystemProperties.getBoolean(PROPERTY_CALL_ON_OFF_MONITER_CONTROL, false);
        log("PROPERTY_CALL_ON_OFF_MONITER_CONTROL: " + enabled2);
        if (true == enabled2) {
            log("Call on-off moniter switch is opened, will listen its event!");
            this.mCallNotifier = new OemCallStateNotifier(this.mPhones);
        } else {
            log("Call on-off moniter switch is closed, will not listen its event!");
        }
        boolean enabled3 = SystemProperties.getBoolean(PROPERTY_DDS_CHNAGE_MONITER_CONTROL, false);
        log("PROPERTY_DDS_CHNAGE_MONITER_CONTROL: " + enabled3);
        if (true == enabled3) {
            log("Dds change moniter switch is opened, will listen its broadcast!");
            filter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        } else {
            log("Dds change moniter switch is closed, will not listen its broadcast!");
        }
        boolean enabled4 = SystemProperties.getBoolean(PROPERTY_SHUTDOWN_MONITER_CONTROL, false);
        log("PROPERTY_SHUTDOWN_MONITER_CONTROL: " + enabled4);
        if (true == enabled4) {
            log("Shutdown moniter switch is opened, will listen its broadcast!");
            filter.addAction("android.intent.action.ACTION_SHUTDOWN");
        } else {
            log("Shutdown moniter switch is closed, will not listen its broadcast!");
        }
        boolean enabled5 = SystemProperties.getBoolean(PROPERTY_UIM_STATE_MONITER_CONTROL, true);
        this.mUimStateIndIntervalS = SystemProperties.getInt(PROPERTY_UIM_STATE_IND_INTERVAL_S, 30);
        log("PROPERTY_UIM_STATE_MONITER_CONTROL: " + enabled5 + ", mUimStateIndIntervalS: " + this.mUimStateIndIntervalS);
        if (true == enabled5) {
            log("Uim moniter switch is opened, will listen its broadcast!");
            filter.addAction("android.intent.action.SIM_STATE_CHANGED");
        } else {
            log("Uim moniter switch is closed, will not listen its broadcast!");
        }
        boolean enabled6 = SystemProperties.getBoolean(PROPERTY_USB_MONITER_CONTROL, true);
        log("PROPERTY_USB_MONITER_CONTROL: " + enabled6);
        if (true == enabled6) {
            log("Usb ATTACHED-DETACHED moniter switch is opened, will listen its broadcast!");
            filter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
            filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        } else {
            log("Usb ATTACHED-DETACHED moniter switch is closed, will not listen its broadcast!");
        }
        boolean enabled7 = SystemProperties.getBoolean(PROPERTY_POWER_CONNECT_MONITER_CONTROL, true);
        log("PROPERTY_POWER_CONNECT_MONITER_CONTROL: " + enabled7);
        if (true == enabled7) {
            log("POWER state moniter switch is opened, will listen its broadcast!");
            filter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
            filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        } else {
            log("POWER state moniter switch is closed, will not listen its broadcast!");
        }
        boolean enabled8 = SystemProperties.getBoolean(PROPERTY_SLEEP_CHANGED_MONITER_CONTROL, true);
        log("PROPERTY_SLEEP_CHANGED_MONITER_CONTROL: " + enabled8);
        if (true == enabled8) {
            log("Miui powerkeeper_sleep_changed moniter switch is opened, will listen its broadcast!");
            filter.addAction("com.miui.powerkeeper_sleep_changed");
        } else {
            log("Miui powerkeeper_sleep_changed moniter switch is closed, will not listen its broadcast!");
        }
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter, 2);
    }

    public static boolean isMtbStated() {
        String strDeviceName = ModemUtils.getDeviceName();
        log("isMtbStated, strDeviceName = " + strDeviceName);
        int androidVersion = SystemProperties.getInt("ro.build.version.release", 0);
        log("isMtbStated, androidVersion = " + androidVersion);
        log("isMtbStated, DEVICE_MTB_SUPPORT_LIST = " + DEVICE_MTB_SUPPORT_LIST);
        String swStart = SystemProperties.get(PROP_MTB_POWER_ON_START_SWITCH, "0");
        log("isMtbStated, swStart = " + swStart);
        if ("1".equals(swStart)) {
            log("The Power on start switch is opened, will start MTB");
            return true;
        }
        if (ModemUtils.isFactoryBuild()) {
            log("The Factory_Build will not start MTB");
            return false;
        }
        if (androidVersion == 13 || DEVICE_MTB_SUPPORT_LIST.contains(strDeviceName)) {
            return true;
        }
        log("Neither android 13 nor DEVICE_MTB_SUPPORT_LIST, will not start MTB");
        return false;
    }

    public static void startMtb(Context context) {
        startMtbDo(context, true);
    }

    public static void startMtbIfNeed(Context context) {
        startMtbDo(context, false);
    }

    private static void startMtbDo(Context context, boolean forceStart) {
        log("startMtbDo, forceStart = " + forceStart + ", context = " + context);
        if (sIntanceMtb != null) {
            log("startMtbDo do nothing, sIntance is not null");
            return;
        }
        if (context == null) {
            log("startMtbDo do nothing, context is null");
            return;
        }
        if (!forceStart && !isMtbStated()) {
            log("startMtbDo do nothing, isMtbStated is false");
            return;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(BC_ACTION_MTB_IS_RUNNING);
        context.registerReceiver(mBroadcastReceiverForMtbRuningState, filter, 2);
        sIntanceMtb = new MtbStartThread(context, CommenUtils.DELAY_IMS_STATE_UPDATE_TIMER, 25);
        sIntanceMtb.start();
        log("startMtbDo done, sIntanceMtb = " + sIntanceMtb);
    }

    public static class MtbStartThread extends Thread {
        public int CYCLE_COUNT_MAX = 50;
        private Context dContext;
        public int dCycleCount;
        public int dIntervalMs;

        public MtbStartThread(Context cnt, int intervalMs, int cycleCount) {
            this.dContext = null;
            this.dIntervalMs = 1000;
            this.dCycleCount = 0;
            ModemServiceMoniter.log("MtbStartThread Constructor, intervalMs = " + intervalMs + ", cycleCount = " + cycleCount);
            this.dContext = cnt;
            this.dIntervalMs = intervalMs;
            this.dCycleCount = cycleCount;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            ModemServiceMoniter.log("MtbStartThread run, dContext = " + this.dContext + ", dIntervalMs = " + this.dIntervalMs + ", dCycleCount = " + this.dCycleCount + ", mMtbIsRunning = " + ModemServiceMoniter.mMtbIsRunning);
            if (this.dContext == null) {
                ModemServiceMoniter.log("The Mtb-start thread do nothing, dContext is null");
                return;
            }
            if (this.dIntervalMs <= 0) {
                ModemServiceMoniter.log("The Mtb-start thread do nothing, dIntervalMs <= 0");
                return;
            }
            if (this.dCycleCount <= 0) {
                ModemServiceMoniter.log("The Mtb-start thread do nothing, dCycleCount <= 0");
                return;
            }
            if (this.dCycleCount > this.CYCLE_COUNT_MAX) {
                ModemServiceMoniter.log("dCycleCount can not > " + this.CYCLE_COUNT_MAX + ", will force to set it to " + this.CYCLE_COUNT_MAX);
                this.dCycleCount = this.CYCLE_COUNT_MAX;
            }
            int cycleCount = 0;
            while (cycleCount < this.dCycleCount && !ModemServiceMoniter.mMtbIsRunning) {
                String mtbState = SystemProperties.get(ModemServiceMoniter.PROP_MTB_IS_STARTED_SYS, "0");
                if ("1".equals(mtbState)) {
                    ModemServiceMoniter.log("The Mtb is running, stop wake-up opt.");
                    break;
                }
                Intent intent = new Intent("xiaomi.intent.action.ONLY_FOR_WAKEUP_MTB");
                intent.setPackage("com.xiaomi.mtb");
                this.dContext.sendBroadcast(intent);
                cycleCount++;
                ModemServiceMoniter.log("Waking, will sleep to wait, dIntervalMs: " + this.dIntervalMs + ", dCycleCount: " + this.dCycleCount + ", cycleCount: " + cycleCount + ", mtbState: " + mtbState + ", mMtbIsRunning: " + ModemServiceMoniter.mMtbIsRunning);
                try {
                    sleep(this.dIntervalMs);
                    ModemServiceMoniter.log("Sleep done, dCycleCount: " + this.dCycleCount + ", cycleCount: " + cycleCount + ", mMtbIsRunning: " + ModemServiceMoniter.mMtbIsRunning);
                } catch (Exception e) {
                    ModemServiceMoniter.log("MtbStartThread, Exception: " + e);
                    e.printStackTrace();
                    return;
                }
            }
            ModemServiceMoniter.log("MtbStartThread quit, obj = " + this);
        }
    }

    private ModemServiceMoniter(Context context, Phone[] phones) {
        this.mContext = null;
        this.mPhones = null;
        this.mTelephonyMgr = null;
        this.mCallState = null;
        mInitState = 0;
        this.mNumPhones = TelephonyManager.getDefault().getPhoneCount();
        log("Constructor, mNumPhones = " + this.mNumPhones);
        if (this.mNumPhones <= 0 || this.mNumPhones >= 3) {
            log("mNumPhones is invalid");
            return;
        }
        this.mContext = context;
        if (this.mContext == null) {
            log("Constructor, mContext is null");
            return;
        }
        this.mPhones = phones;
        if (this.mPhones == null) {
            log("Constructor, mPhones is null");
            return;
        }
        int numPhones = this.mPhones.length;
        log("numPhones = " + numPhones);
        if (this.mNumPhones != numPhones || numPhones <= 0) {
            log("Constructor, Phone count mismatch, cnt_tm = " + this.mNumPhones + ", cnt_pf = " + numPhones);
            return;
        }
        this.mTelephonyMgr = (TelephonyManager) this.mContext.getSystemService("phone");
        if (this.mTelephonyMgr == null) {
            log("mTelephonyMgr is null");
            return;
        }
        this.mCallState = new int[this.mNumPhones];
        for (int i = 0; i < this.mNumPhones; i++) {
            this.mCallState[i] = 0;
        }
        onInit();
        mInitState = 1;
    }

    public void dispose() {
        log("dispose, mHookAgent = " + mHookAgent);
        if (mHookAgent != null) {
            mHookAgent.dispose();
            mHookAgent = null;
        }
    }

    public static void onInitVerInfo() {
        log("onInitVerInfo");
        mDeviceName = ModemUtils.getDeviceName();
        mDeviceHwInfo = ModemUtils.getDeviceHwInfo();
        mDeviceModName = ModemUtils.getDeviceSwInfo();
        mDeviceHwLevel = ModemUtils.getDeviceHwLevel();
        mBuildRegion = ModemUtils.getBuildRegion();
        mCertType = ModemUtils.getCertType();
        mFactoryBuild = ModemUtils.getFactoryBuild();
        mRootType = ModemUtils.getRootType();
        mBuildVersion = ModemUtils.getBuildVersion();
        mBuildTags = ModemUtils.getBuildTags();
        mSdkVersion = ModemUtils.getSdkVersion();
        mAndroidVersion = ModemUtils.getAndroidVersion();
        mManufacturerId = ModemUtils.getManufacturerId();
        mHalChipId = ModemUtils.getHalChipId();
        mBpChipId = ModemUtils.getBpChipId();
        mBuildProfile = ModemUtils.getBuildProfile();
        mXringHalFlag = ModemUtils.isXringHal();
    }

    private static void onInitForPhoneInitCount() {
        try {
            String initCountStr = SystemProperties.get(PROPERTY_PHONE_INIT_COUNT, "0");
            int initCountOld = ModemUtils.getIntFromString(initCountStr, false);
            int initCountNew = initCountOld + 1;
            log("onInitForPhoneInitCount, initCountStr = " + initCountStr + ", initCountOld = " + initCountOld + ", initCountNew = " + initCountNew);
            SystemProperties.set(PROPERTY_PHONE_INIT_COUNT, "" + initCountNew);
        } catch (Exception e) {
            log("onInitForPhoneInitCount, Exception info: " + e);
            e.printStackTrace();
        }
    }

    private static void onEarlyInit() {
        log("onEarlyInit");
        onInitVerInfo();
        onInitForPhoneInitCount();
        log("onEarlyInit, " + ModemUtils.getVerInfoWithName());
    }

    public static boolean isMsmSupported() {
        log("isMsmSupported");
        if (ModemUtils.isFactoryBuild()) {
            log("The Factory_Build will not start Msm");
            return false;
        }
        if (1 == mManufacturerId) {
            log("isMsmSupported, MSM is supported by this xiaomi project, mManufacturerId = " + mManufacturerId);
            return true;
        }
        if (mHalChipId > 100 && mHalChipId < 1001) {
            log("isMsmSupported, MSM is supported by this odm project, mHalChipId = " + mHalChipId);
            return true;
        }
        log("isMsmSupported, MSM is not supported by this project, mDeviceName = " + mDeviceName);
        return false;
    }

    public static void make(Context context, OemHookAgent hookAgent) {
        onEarlyInit();
        if (!isMsmSupported()) {
            log("Not supported, will do nothing");
            return;
        }
        boolean mainEnabled = SystemProperties.getBoolean(PROPERTY_MODEM_SERVICE_MAIN_MONITER_CONTROL, true);
        log("mainEnabled: " + mainEnabled);
        if (!mainEnabled) {
            log("Modem service main moniter switch is closed, will not start Modem Service Moniter thread!");
            return;
        }
        if (context == null) {
            log("context is null, will not start Modem Service Moniter thread!");
            return;
        }
        if (hookAgent == null) {
            log("hookAgent is null, will not start Modem Service Moniter thread!");
            return;
        }
        mHookAgent = hookAgent;
        Phone[] phones = PhoneFactory.getPhones();
        if (phones == null) {
            log("phones is null, will not start Modem Service Moniter thread!");
        } else if (sIntance == null) {
            sIntance = new ModemServiceMoniter(context, phones);
        } else {
            log("sIntance is not null, do nothing");
        }
    }

    public class OemCallStateNotifier {
        private final int EVENT_PRECISE_CS_CALL_STATE_CHANGED;
        private final int EVENT_PRECISE_IMS_CALL_STATE_CHANGED;
        private GsmCdmaCall[] mBgCsCalls;
        private ImsPhoneCall[] mBgImsCalls;
        private CallStateHandler mCallStateHandler;
        private GsmCdmaCall[] mFgCsCalls;
        private ImsPhoneCall[] mFgImsCalls;
        private ImsPhone[] mImsPhones;
        private boolean mIsCallInActiveState;
        private boolean mIsCallInProgress;
        private int mNumPhones;
        private Phone[] mPhones;
        private GsmCdmaCall[] mRiCsCalls;
        private ImsPhoneCall[] mRiImsCalls;

        private OemCallStateNotifier(Phone[] phones) {
            this.mNumPhones = 0;
            this.mPhones = null;
            this.mFgCsCalls = null;
            this.mBgCsCalls = null;
            this.mRiCsCalls = null;
            this.mImsPhones = null;
            this.mFgImsCalls = null;
            this.mBgImsCalls = null;
            this.mRiImsCalls = null;
            this.mIsCallInActiveState = false;
            this.mIsCallInProgress = false;
            this.EVENT_PRECISE_CS_CALL_STATE_CHANGED = 101;
            this.EVENT_PRECISE_IMS_CALL_STATE_CHANGED = 102;
            this.mCallStateHandler = null;
            this.mNumPhones = TelephonyManager.getDefault().getPhoneCount();
            this.mPhones = phones;
            ModemServiceMoniter.log("OemCallStateNotifier, mNumPhones = " + this.mNumPhones);
            this.mImsPhones = new ImsPhone[this.mNumPhones];
            this.mFgCsCalls = new GsmCdmaCall[this.mNumPhones];
            this.mBgCsCalls = new GsmCdmaCall[this.mNumPhones];
            this.mRiCsCalls = new GsmCdmaCall[this.mNumPhones];
            this.mFgImsCalls = new ImsPhoneCall[this.mNumPhones];
            this.mBgImsCalls = new ImsPhoneCall[this.mNumPhones];
            this.mRiImsCalls = new ImsPhoneCall[this.mNumPhones];
            this.mCallStateHandler = new CallStateHandler();
            for (int i = 0; i < this.mNumPhones; i++) {
                if (this.mPhones[i] != null) {
                    this.mPhones[i].registerForPreciseCallStateChanged(this.mCallStateHandler, 101, Integer.valueOf(i));
                    this.mFgCsCalls[i] = (GsmCdmaCall) this.mPhones[i].getForegroundCall();
                    this.mBgCsCalls[i] = (GsmCdmaCall) this.mPhones[i].getBackgroundCall();
                    this.mRiCsCalls[i] = (GsmCdmaCall) this.mPhones[i].getRingingCall();
                }
                this.mImsPhones[i] = (ImsPhone) this.mPhones[i].getImsPhone();
                if (this.mImsPhones[i] != null) {
                    this.mImsPhones[i].registerForPreciseCallStateChanged(this.mCallStateHandler, 102, Integer.valueOf(i));
                    this.mFgImsCalls[i] = this.mImsPhones[i].getForegroundCall();
                    this.mBgImsCalls[i] = this.mImsPhones[i].getBackgroundCall();
                    this.mRiImsCalls[i] = this.mImsPhones[i].getRingingCall();
                }
            }
        }

        public boolean isCallActive() {
            return this.mIsCallInActiveState;
        }

        public boolean isCallInProgress() {
            return this.mIsCallInProgress;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isCallActive(int phoneId) {
            return (this.mFgCsCalls[phoneId] != null && this.mFgCsCalls[phoneId].getState() == Call.State.ACTIVE) || (this.mBgCsCalls[phoneId] != null && this.mBgCsCalls[phoneId].getState() == Call.State.ACTIVE) || ((this.mRiCsCalls[phoneId] != null && this.mRiCsCalls[phoneId].getState() == Call.State.ACTIVE) || ((this.mFgImsCalls[phoneId] != null && this.mFgImsCalls[phoneId].getState() == Call.State.ACTIVE) || ((this.mBgImsCalls[phoneId] != null && this.mBgImsCalls[phoneId].getState() == Call.State.ACTIVE) || (this.mRiImsCalls[phoneId] != null && this.mRiImsCalls[phoneId].getState() == Call.State.ACTIVE))));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isCallIdle(int phoneId) {
            return (this.mFgCsCalls[phoneId] == null || this.mFgCsCalls[phoneId].isIdle()) && (this.mBgCsCalls[phoneId] == null || this.mBgCsCalls[phoneId].isIdle()) && ((this.mRiCsCalls[phoneId] == null || this.mRiCsCalls[phoneId].isIdle()) && ((this.mFgImsCalls[phoneId] == null || this.mFgImsCalls[phoneId].isIdle()) && ((this.mBgImsCalls[phoneId] == null || this.mBgImsCalls[phoneId].isIdle()) && (this.mRiImsCalls[phoneId] == null || this.mRiImsCalls[phoneId].isIdle()))));
        }

        private class CallStateHandler extends Handler {
            private CallStateHandler() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                AsyncResult ar = (AsyncResult) msg.obj;
                int phoneId = ((Integer) ar.userObj).intValue();
                switch (msg.what) {
                    case 101:
                    case 102:
                        if (!OemCallStateNotifier.this.mIsCallInActiveState && OemCallStateNotifier.this.isCallActive(phoneId)) {
                            ModemServiceMoniter.log("processCallStateChanged: call active on phone" + phoneId);
                            OemCallStateNotifier.this.mIsCallInActiveState = true;
                            ModemServiceMoniter.this.onMsgPkCallStatusNotify(1, phoneId);
                        } else if (OemCallStateNotifier.this.isCallIdle(phoneId)) {
                            ModemServiceMoniter.log("processCallStateChanged: call disconnected on phone" + phoneId);
                            OemCallStateNotifier.this.mIsCallInActiveState = false;
                            OemCallStateNotifier.this.mIsCallInProgress = false;
                            ModemServiceMoniter.this.onMsgPkCallStatusNotify(0, phoneId);
                        } else if (!OemCallStateNotifier.this.mIsCallInProgress) {
                            ModemServiceMoniter.log("processCallStateChanged: call started on phone" + phoneId);
                            OemCallStateNotifier.this.mIsCallInProgress = true;
                            ModemServiceMoniter.this.onMsgPkCallStatusNotify(1, phoneId);
                        }
                        break;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void log(String s) {
        Log.d(LOG_TAG, s);
    }
}
