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
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import com.xiaomi.modem.ModemUtils;
import com.xiaomi.modem.OemHookAgent;
import java.util.ArrayList;
import java.util.Arrays;

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
    private static final int HOOK_HEAD_SIZE = 16;
    private static final String HOOK_IDENTIFIER = "QOEMHOOK";
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
    public static int mAndroidVersion = 0;
    public static int mBpChipId = 0;
    public static String mBuildProfile = "NULL";
    public static String mBuildRegion = "none";
    public static String mBuildTags = "NULL";
    public static String mBuildVersion = "";
    public static String mCertType = "none";
    public static String mDeviceHwInfo = "NULL";
    public static String mDeviceHwLevel = "NULL";
    public static String mDeviceModName = "NULL";
    public static String mDeviceName = "NULL";
    public static String mFactoryBuild = "NULL";
    public static int mHalChipId = 0;
    private static OemHookAgent mHookAgent = null;
    private static int mInitState = 0;
    public static int mManufacturerId = 0;
    private static boolean mMtbIsRunning = false;
    public static int mOemProjectFlag = 0;
    private static final boolean mPowerConnectMoniterDiag = true;
    private static int mPowerkeeperSleepState = 2;
    public static String mRootType = "NULL";
    private static final boolean mScreenOnOffMoniterNotifyHal = false;
    public static int mSdkVersion = 0;
    private static final boolean mSleepChangedMoniterNotifyModem = true;
    private static final boolean mUsbAttachedMoniterDiag = true;
    public static boolean mXringHalFlag = false;
    private static ModemServiceMoniter sIntance;
    private static MtbStartThread sIntanceMtb;
    private int[] mCallState;
    private Context mContext;
    private int mNumPhones;
    private Phone[] mPhones;
    private TelephonyManager mTelephonyMgr;
    private static ArrayList DEVICE_MTB_SUPPORT_LIST = new ArrayList(Arrays.asList("munch"));
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
    private final int HOOK_TIMER_LEN_MAX = 2000;
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
            if (ModemServiceMoniter.BC_ACTION_SHUTDOWN.equals(action)) {
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
            } else if (ModemServiceMoniter.ACTION_MIUI_POWERKEEPER_SLEEP_CHANGED.equals(action)) {
                ModemServiceMoniter.log("ACTION_MIUI_POWERKEEPER_SLEEP_CHANGED");
                ModemServiceMoniter.this.onMsgNotifyModemForSleepChanged(intent);
            } else {
                ModemServiceMoniter.log("UNKNOWN action");
            }
        }
    };
    PhoneStateListener mListener = new PhoneStateListener(this) { // from class: com.xiaomi.internal.telephony.ModemServiceMoniter.4
        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int i, String str) {
            super.onCallStateChanged(i, str);
            if (i == 0) {
                ModemServiceMoniter.log("TelephonyManager.CALL_STATE_IDLE");
                return;
            }
            if (i == 1) {
                ModemServiceMoniter.log("TelephonyManager.CALL_STATE_RINGING");
            } else if (i == 2) {
                ModemServiceMoniter.log("TelephonyManager.CALL_STATE_OFFHOOK");
            } else {
                ModemServiceMoniter.log("Unknown TelephonyManager STATE");
            }
        }
    };
    protected Handler mHandler = new Handler() { // from class: com.xiaomi.internal.telephony.ModemServiceMoniter.5
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            ModemServiceMoniter.log("handleMessage msg id: " + message.what);
            ModemServiceMoniter.this.mOptFlag = message.what;
            new Bundle();
            Bundle data = message.getData();
            switch (message.what) {
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
                    ModemServiceMoniter.log("invalid msg id: " + message.what);
                    break;
            }
        }
    };

    public static void onMtbServiceWakeUp(final Context context) {
        log("onMtbServiceWakeUp, ModemUtils.getDeviceHwLevel = " + ModemUtils.getDeviceHwLevel());
        if (ModemUtils.getDeviceHwLevel().equals("MP")) {
            log("onMtbServiceWakeUp, MP machine, return.");
            return;
        }
        int i = SystemProperties.getInt(PROPERTY_AUTO_CAPTURE_LOG_MASK, -1);
        log("autoCaptureLogMask = " + i);
        if (1 == i) {
            log("MtbLogManagerService will start after 5s");
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.xiaomi.internal.telephony.ModemServiceMoniter.1
                @Override // java.lang.Runnable
                public void run() {
                    ModemServiceMoniter.log("MtbLogManagerService will start now");
                    Intent intent = new Intent(ModemServiceMoniter.INTENT_START_AUTO_CAPTURE_LOG_SERVICE);
                    intent.setPackage("com.xiaomi.mtb");
                    context.sendBroadcast(intent);
                }
            }, MirrorDesktopHelper.TIMEOUT_MILLIS);
        }
    }

    public boolean onMsgUnPkScreenStatusNotify(Bundle bundle) {
        if (bundle == null) {
            log("onMsgUnPkScreenStatusNotify, data is null");
            return false;
        }
        int i = bundle.getInt(BUNDLE_SCREEN_STATUS);
        log("screenStatus: " + i);
        mHookAgent.onHookCommonMsg(EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX, i);
        return true;
    }

    public boolean onMsgPkScreenStatusNotify(int i) {
        log("onMsgPkScreenStatusNotify, status = " + i);
        if (i != 0 && 1 != i) {
            log("status is invalid");
            return false;
        }
        Message messageObtain = Message.obtain();
        messageObtain.what = 1;
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_SCREEN_STATUS, i);
        messageObtain.setData(bundle);
        log("Re-start the timer, " + this.mScreenOnOffIndIntervalS + " seconds later, will notify rild for ACTION_SCREEN_ON or ACTION_SCREEN_OFF");
        onRemoveMsg(1);
        onSendMsg(messageObtain, ((long) this.mScreenOnOffIndIntervalS) * 1000);
        return true;
    }

    public boolean onMsgUnPkCallStatusNotify(Bundle bundle) {
        if (bundle == null) {
            log("onMsgUnPkCallStatusNotify, data is null");
            return false;
        }
        int i = bundle.getInt(BUNDLE_CALL_STATUS);
        log("callStatus: " + i + ", phoneId = " + bundle.getInt(BUNDLE_PHONE_ID));
        mHookAgent.onHookCommonMsg(47, i);
        return true;
    }

    public boolean onMsgPkCallStatusNotify(int i, int i2) {
        log("onMsgPkCallStatusNotify, status = " + i + ", phoneId = " + i2);
        if (i != 0 && 1 != i) {
            log("status is invalid");
            return false;
        }
        if (i2 < 0 || i2 >= this.mNumPhones) {
            log("phoneId is invalid");
            return false;
        }
        int[] iArr = this.mCallState;
        if (iArr[i2] == i) {
            log("call state no change");
            return true;
        }
        iArr[i2] = i;
        Message messageObtain = Message.obtain();
        messageObtain.what = 2;
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_CALL_STATUS, i);
        bundle.putInt(BUNDLE_PHONE_ID, i2);
        messageObtain.setData(bundle);
        this.mHandler.sendMessage(messageObtain);
        return true;
    }

    public boolean onMsgUnPkDdsChangeNotify() {
        int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
        int phoneId = SubscriptionManager.getPhoneId(defaultDataSubscriptionId);
        log("onMsgUnPkDdsChangeNotify, newDefaultDataSub = " + defaultDataSubscriptionId + ", phoneId = " + phoneId);
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
        Message messageObtain = Message.obtain();
        messageObtain.what = 4;
        this.mHandler.sendMessage(messageObtain);
        return true;
    }

    public boolean onMsgUnPkUimStateNotify(Bundle bundle) {
        if (bundle == null) {
            log("onMsgUnPkUimStateNotify, data is null");
            return false;
        }
        int i = bundle.getInt(BUNDLE_UIM_STATE);
        log("onMsgUnPkUimStateNotify, state = " + i);
        mHookAgent.onHookCommonMsg(22, i);
        log("onMsgUnPkUimStateNotify, call onMsgCheckDiagDeadline");
        onMsgCheckDiagDeadline();
        return true;
    }

    public boolean onMsgPkUimStateNotify(Context context, Intent intent) {
        String str;
        log("onMsgPkUimStateNotify, context = " + context);
        int simState = ((TelephonyManager) context.getSystemService("phone")).getSimState();
        String stringExtra = intent.getStringExtra("ss");
        int i = 2;
        if (5 == simState) {
            str = "SIM_STATE_READY";
        } else if (simState == 0) {
            str = "SIM_STATE_UNKNOWN";
        } else if (1 == simState) {
            str = "SIM_STATE_ABSENT";
        } else if (2 == simState) {
            str = "SIM_STATE_PIN_REQUIRED";
        } else if (3 == simState) {
            str = "SIM_STATE_PUK_REQUIRED";
        } else if (4 == simState) {
            str = "SIM_STATE_NETWORK_LOCKED";
        } else {
            str = "SIM_STATE_INVALID";
        }
        if ("LOADED".equals(stringExtra) || "IMSI".equals(stringExtra) || "READY".equals(stringExtra)) {
            i = 0;
        } else if (!"ABSENT".equals(stringExtra) && !"UNKNOWN".equals(stringExtra) && !"CARD_IO_ERROR".equals(stringExtra)) {
            i = 1;
        }
        log("Re-start the timer, " + this.mUimStateIndIntervalS + " seconds later, will notify rild for ACTION_SIM_STATE_CHANGED-" + str + "(" + simState + "), stateExtra: " + stringExtra + ", simState: " + i);
        Message messageObtain = Message.obtain();
        messageObtain.what = 5;
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_UIM_STATE, i);
        messageObtain.setData(bundle);
        onRemoveMsg(5);
        onSendMsg(messageObtain, ((long) this.mUimStateIndIntervalS) * 1000);
        return true;
    }

    public boolean onMsgCheckDiagDeadline() {
        log("onMsgCheckDiagDeadline");
        Message messageObtain = Message.obtain();
        messageObtain.what = 6;
        log("Re-start the timer, " + this.mCheckDiagDeadlineIntervalS + " seconds later, will check diag");
        onRemoveMsg(6);
        onSendMsg(messageObtain, ((long) this.mCheckDiagDeadlineIntervalS) * 1000);
        return true;
    }

    public void onMsgCheckDiagDeadlineDo() {
        log("onMsgCheckDiagDeadlineDo");
        diagDeadlineCheck();
    }

    public void diagDeadlineCheck() {
        log("diagDeadlineCheck");
        long diagDeadlineFromProp = ModemUtils.getDiagDeadlineFromProp();
        log("diagDeadlineCheck, deadline = " + diagDeadlineFromProp + ", deadlineStr = " + ModemUtils.timeLongToString(diagDeadlineFromProp));
        if (diagDeadlineFromProp <= 0) {
            log("diagDeadlineCheck, deadline is not cfged, do nothing");
            return;
        }
        long currentTimeWithLong = ModemUtils.getCurrentTimeWithLong();
        log("diagDeadlineCheck, curDate = " + currentTimeWithLong + ", curDateStr = " + ModemUtils.timeLongToString(currentTimeWithLong));
        if (diagDeadlineFromProp >= currentTimeWithLong) {
            log("diagDeadlineCheck, not expired, do nothing");
            return;
        }
        boolean zOnModemDiagIsOpen = ModemUtils.onModemDiagIsOpen();
        log("diagDeadlineCheck, diag port state = " + zOnModemDiagIsOpen);
        if (zOnModemDiagIsOpen) {
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
        int intExtra = intent.getIntExtra("state", 2);
        log("onMsgNotifyModemForSleepChanged, newState = " + intExtra + ", oldState = " + mPowerkeeperSleepState);
        Message messageObtain = Message.obtain();
        messageObtain.what = 7;
        Bundle bundle = new Bundle();
        bundle.putInt("state", intExtra);
        messageObtain.setData(bundle);
        if (2 == intExtra) {
            onRemoveMsg(7);
            onSendMsg(messageObtain, ((long) this.mSleepOffMoniterNotifyModemDelayS) * 1000);
            log("Re-start the timer, " + this.mSleepOffMoniterNotifyModemDelayS + " seconds later, will notify rild for POWERKEEPER_SLEEP_OFF");
            return;
        }
        onSendMsg(messageObtain);
        log("Will notify rild for POWERKEEPER_SLEEP_ON");
    }

    public void onMsgUnPkNotifyModemForSleepChanged(Bundle bundle) {
        if (bundle == null) {
            log("onMsgUnPkNotifyModemForSleepChanged, data is null, oldState = " + mPowerkeeperSleepState + ", mModemSleepStateCheckNum = " + this.mModemSleepStateCheckNum);
            return;
        }
        if (mHookAgent == null) {
            log("onMsgUnPkNotifyModemForSleepChanged, mHookAgent is null, oldState = " + mPowerkeeperSleepState + ", mModemSleepStateCheckNum = " + this.mModemSleepStateCheckNum);
            return;
        }
        int i = bundle.getInt("state");
        boolean zIsMobileDataEnabled = ModemUtils.isMobileDataEnabled(this.mContext);
        int insertedSimCount = ModemUtils.getInsertedSimCount();
        log("onMsgUnPkNotifyModemForSleepChanged, sleepingState = " + i + ", isDataEnabled = " + zIsMobileDataEnabled + ", insertedSimCount = " + insertedSimCount);
        int i2 = 1;
        if (1 != i || (zIsMobileDataEnabled && insertedSimCount != 0)) {
            i2 = 2;
        }
        log("onMsgUnPkNotifyModemForSleepChanged, newState = " + i2 + ", oldState = " + mPowerkeeperSleepState + ", mModemSleepStateCheckNum = " + this.mModemSleepStateCheckNum);
        if (i2 == mPowerkeeperSleepState) {
            log("onMsgUnPkNotifyModemForSleepChanged, state is no changed");
            return;
        }
        mPowerkeeperSleepState = i2;
        mHookAgent.onHookNotifyPowerkeeperSleepStatus(i2);
        log("onMsgUnPkNotifyModemForSleepChanged, onHookNotifyPowerkeeperSleepStatus is sent");
        this.mModemSleepStateCheckNum = 0;
        log("Will start the timer for check sleep state of modem, mModemSleepStateCheckNum = " + this.mModemSleepStateCheckNum);
        onMsgCheckModemForSleepState();
    }

    public void onMsgCheckModemForSleepState() {
        int i;
        Message messageObtain = Message.obtain();
        messageObtain.what = 8;
        if (2 == mPowerkeeperSleepState) {
            i = this.mModemSleepOffCheckDelayS;
            log("onMsgCheckModemForSleepState, Re-start the timer, " + i + " seconds later, will check POWERKEEPER_SLEEP_OFF of modem");
        } else {
            i = this.mModemSleepOnCheckDelayS;
            log("onMsgCheckModemForSleepState, Re-start the timer, " + i + " seconds later, will check POWERKEEPER_SLEEP_ON of modem");
        }
        onRemoveMsg(8);
        onSendMsg(messageObtain, ((long) i) * 1000);
    }

    public void onMsgUnPkCheckModemForSleepState() {
        OemHookAgent oemHookAgent = mHookAgent;
        if (oemHookAgent == null) {
            log("onMsgUnPkCheckModemForSleepState, mHookAgent is null, mPowerkeeperSleepState = " + mPowerkeeperSleepState);
            return;
        }
        int modemSleepState = oemHookAgent.getModemSleepState();
        log("onMsgUnPkCheckModemForSleepState, modemSleepState = " + modemSleepState + ", mPowerkeeperSleepState = " + mPowerkeeperSleepState + ", mModemSleepStateCheckNum = " + this.mModemSleepStateCheckNum + ", MODEM_SLEEP_ON_CHECK_NUM_MAX = 3, MODEM_SLEEP_OFF_CHECK_NUM_MAX = 3");
        int i = mPowerkeeperSleepState;
        if (modemSleepState == i) {
            this.mModemSleepStateCheckNum = 0;
            log("onMsgUnPkCheckModemForSleepState, The sleep state of Modem and AP is consistent");
            return;
        }
        int i2 = this.mModemSleepStateCheckNum + 1;
        this.mModemSleepStateCheckNum = i2;
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
        StringBuilder sb = new StringBuilder("At the same will start the timer for check sleep state of modem, mModemSleepStateCheckNum = ");
        sb.append(this.mModemSleepStateCheckNum);
        log(sb.toString());
        onMsgCheckModemForSleepState();
    }

    public static String getBuildRegion() {
        return SystemProperties.get(PROP_MIUI_BUILD_REGION, PROP_MIUI_BUILD_REGION_DEF);
    }

    public static String getTempBuildRegion() {
        return SystemProperties.get(PROP_TEMP_BUILD_REGION_VENDOR, PROP_MIUI_BUILD_REGION_DEF);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public void notifyBuildRegion() {
        String buildRegion = getBuildRegion();
        String tempBuildRegion = getTempBuildRegion();
        log("notifyBuildRegion, strBuildRegion = " + buildRegion + ", strTempBuildRegion = " + tempBuildRegion);
        if (mHookAgent == null) {
            log("notifyBuildRegion, mHookAgent is null, do nothing");
            return;
        }
        if (buildRegion == null || PROP_MIUI_BUILD_REGION_DEF.equals(buildRegion)) {
            log("notifyBuildRegion, BuildRegion is null, do nothing");
            return;
        }
        if (buildRegion.equals(tempBuildRegion)) {
            log("notifyBuildRegion, TempBuildRegion is same with BuildRegion, do nothing");
            return;
        }
        if (!mHookAgent.onHookPropSet(PROP_TEMP_BUILD_REGION_VENDOR, buildRegion)) {
            log("notifyBuildRegion, TempBuildRegion update failed");
            return;
        }
        String tempBuildRegion2 = getTempBuildRegion();
        log("notifyBuildRegion, new strTempBuildRegion = " + tempBuildRegion2);
        if (buildRegion.equals(tempBuildRegion2)) {
            log("notifyBuildRegion, TempBuildRegion sync ok, will notify rild");
        } else {
            log("notifyBuildRegion, TempBuildRegion sync fail, do nothing");
        }
        if (mHookAgent.onHookCommonMsg(EVENT_OEMHOOK_NOTIFY_MODEM_SYNC_DONE)) {
            return;
        }
        log("notifyBuildRegion, Maybe not support this hook interface");
    }

    public void onSendMsg(int i) {
        this.mHandler.sendEmptyMessage(i);
    }

    public void onSendMsg(Message message) {
        this.mHandler.sendMessage(message);
    }

    public void onSendMsg(int i, long j) {
        this.mHandler.sendEmptyMessageDelayed(i, j);
    }

    public void onSendMsg(Message message, long j) {
        this.mHandler.sendMessageDelayed(message, j);
    }

    public void onRemoveMsg(int i) {
        this.mHandler.removeMessages(i);
    }

    public void onInit() {
        log("onInit");
        if (this.mContext == null) {
            log("mContext is null");
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        boolean z = SystemProperties.getBoolean(PROPERTY_SCREEN_ON_OFF_MONITER_CONTROL, false);
        log("PROPERTY_SCREEN_ON_OFF_MONITER_CONTROL: " + z);
        if (true == z) {
            log("Screen on-off moniter switch is opened, will listen its broadcast!");
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
        } else {
            log("Screen on-off moniter switch is closed, will not listen its broadcast!");
        }
        boolean z2 = SystemProperties.getBoolean(PROPERTY_CALL_ON_OFF_MONITER_CONTROL, false);
        log("PROPERTY_CALL_ON_OFF_MONITER_CONTROL: " + z2);
        if (true == z2) {
            log("Call on-off moniter switch is opened, will listen its event!");
            this.mCallNotifier = new OemCallStateNotifier(this.mPhones);
        } else {
            log("Call on-off moniter switch is closed, will not listen its event!");
        }
        boolean z3 = SystemProperties.getBoolean(PROPERTY_DDS_CHNAGE_MONITER_CONTROL, false);
        log("PROPERTY_DDS_CHNAGE_MONITER_CONTROL: " + z3);
        if (true == z3) {
            log("Dds change moniter switch is opened, will listen its broadcast!");
            intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        } else {
            log("Dds change moniter switch is closed, will not listen its broadcast!");
        }
        boolean z4 = SystemProperties.getBoolean(PROPERTY_SHUTDOWN_MONITER_CONTROL, false);
        log("PROPERTY_SHUTDOWN_MONITER_CONTROL: " + z4);
        if (true == z4) {
            log("Shutdown moniter switch is opened, will listen its broadcast!");
            intentFilter.addAction(BC_ACTION_SHUTDOWN);
        } else {
            log("Shutdown moniter switch is closed, will not listen its broadcast!");
        }
        boolean z5 = SystemProperties.getBoolean(PROPERTY_UIM_STATE_MONITER_CONTROL, true);
        this.mUimStateIndIntervalS = SystemProperties.getInt(PROPERTY_UIM_STATE_IND_INTERVAL_S, 30);
        log("PROPERTY_UIM_STATE_MONITER_CONTROL: " + z5 + ", mUimStateIndIntervalS: " + this.mUimStateIndIntervalS);
        if (true == z5) {
            log("Uim moniter switch is opened, will listen its broadcast!");
            intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        } else {
            log("Uim moniter switch is closed, will not listen its broadcast!");
        }
        boolean z6 = SystemProperties.getBoolean(PROPERTY_USB_MONITER_CONTROL, true);
        log("PROPERTY_USB_MONITER_CONTROL: " + z6);
        if (true == z6) {
            log("Usb ATTACHED-DETACHED moniter switch is opened, will listen its broadcast!");
            intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
            intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        } else {
            log("Usb ATTACHED-DETACHED moniter switch is closed, will not listen its broadcast!");
        }
        boolean z7 = SystemProperties.getBoolean(PROPERTY_POWER_CONNECT_MONITER_CONTROL, true);
        log("PROPERTY_POWER_CONNECT_MONITER_CONTROL: " + z7);
        if (true == z7) {
            log("POWER state moniter switch is opened, will listen its broadcast!");
            intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
            intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        } else {
            log("POWER state moniter switch is closed, will not listen its broadcast!");
        }
        boolean z8 = SystemProperties.getBoolean(PROPERTY_SLEEP_CHANGED_MONITER_CONTROL, true);
        log("PROPERTY_SLEEP_CHANGED_MONITER_CONTROL: " + z8);
        if (true == z8) {
            log("Miui powerkeeper_sleep_changed moniter switch is opened, will listen its broadcast!");
            intentFilter.addAction(ACTION_MIUI_POWERKEEPER_SLEEP_CHANGED);
        } else {
            log("Miui powerkeeper_sleep_changed moniter switch is closed, will not listen its broadcast!");
        }
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
    }

    public static boolean isMtbStated() {
        String deviceName = ModemUtils.getDeviceName();
        log("isMtbStated, strDeviceName = " + deviceName);
        int i = SystemProperties.getInt("ro.build.version.release", 0);
        log("isMtbStated, androidVersion = " + i);
        log("isMtbStated, DEVICE_MTB_SUPPORT_LIST = " + DEVICE_MTB_SUPPORT_LIST);
        String str = SystemProperties.get(PROP_MTB_POWER_ON_START_SWITCH, "0");
        log("isMtbStated, swStart = " + str);
        if ("1".equals(str)) {
            log("The Power on start switch is opened, will start MTB");
            return true;
        }
        if (ModemUtils.isFactoryBuild()) {
            log("The Factory_Build will not start MTB");
            return false;
        }
        if (i == 13 || DEVICE_MTB_SUPPORT_LIST.contains(deviceName)) {
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

    private static void startMtbDo(Context context, boolean z) {
        log("startMtbDo, forceStart = " + z + ", context = " + context);
        if (sIntanceMtb != null) {
            log("startMtbDo do nothing, sIntance is not null");
            return;
        }
        if (context == null) {
            log("startMtbDo do nothing, context is null");
            return;
        }
        if (!z && !isMtbStated()) {
            log("startMtbDo do nothing, isMtbStated is false");
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BC_ACTION_MTB_IS_RUNNING);
        context.registerReceiver(mBroadcastReceiverForMtbRuningState, intentFilter, 2);
        MtbStartThread mtbStartThread = new MtbStartThread(context, 5000, 25);
        sIntanceMtb = mtbStartThread;
        mtbStartThread.start();
        log("startMtbDo done, sIntanceMtb = " + sIntanceMtb);
    }

    public class MtbStartThread extends Thread {
        public int CYCLE_COUNT_MAX = 50;
        private Context dContext;
        public int dCycleCount;
        public int dIntervalMs;

        public MtbStartThread(Context context, int i, int i2) {
            this.dContext = null;
            this.dIntervalMs = 1000;
            this.dCycleCount = 0;
            ModemServiceMoniter.log("MtbStartThread Constructor, intervalMs = " + i + ", cycleCount = " + i2);
            this.dContext = context;
            this.dIntervalMs = i;
            this.dCycleCount = i2;
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
            int i = this.dCycleCount;
            if (i <= 0) {
                ModemServiceMoniter.log("The Mtb-start thread do nothing, dCycleCount <= 0");
                return;
            }
            if (i > this.CYCLE_COUNT_MAX) {
                ModemServiceMoniter.log("dCycleCount can not > " + this.CYCLE_COUNT_MAX + ", will force to set it to " + this.CYCLE_COUNT_MAX);
                this.dCycleCount = this.CYCLE_COUNT_MAX;
            }
            int i2 = 0;
            while (i2 < this.dCycleCount && !ModemServiceMoniter.mMtbIsRunning) {
                String str = SystemProperties.get(ModemServiceMoniter.PROP_MTB_IS_STARTED_SYS, "0");
                if ("1".equals(str)) {
                    ModemServiceMoniter.log("The Mtb is running, stop wake-up opt.");
                    break;
                }
                Intent intent = new Intent("xiaomi.intent.action.ONLY_FOR_WAKEUP_MTB");
                intent.setPackage("com.xiaomi.mtb");
                this.dContext.sendBroadcast(intent);
                i2++;
                ModemServiceMoniter.log("Waking, will sleep to wait, dIntervalMs: " + this.dIntervalMs + ", dCycleCount: " + this.dCycleCount + ", cycleCount: " + i2 + ", mtbState: " + str + ", mMtbIsRunning: " + ModemServiceMoniter.mMtbIsRunning);
                try {
                    Thread.sleep(this.dIntervalMs);
                    ModemServiceMoniter.log("Sleep done, dCycleCount: " + this.dCycleCount + ", cycleCount: " + i2 + ", mMtbIsRunning: " + ModemServiceMoniter.mMtbIsRunning);
                } catch (Exception e) {
                    ModemServiceMoniter.log("MtbStartThread, Exception: " + e);
                    e.printStackTrace();
                    return;
                }
            }
            ModemServiceMoniter.log("MtbStartThread quit, obj = " + this);
        }
    }

    private ModemServiceMoniter(Context context, Phone[] phoneArr) {
        this.mContext = null;
        this.mPhones = null;
        this.mTelephonyMgr = null;
        this.mCallState = null;
        mInitState = 0;
        this.mNumPhones = TelephonyManager.getDefault().getPhoneCount();
        log("Constructor, mNumPhones = " + this.mNumPhones);
        int i = this.mNumPhones;
        if (i <= 0 || i >= 3) {
            log("mNumPhones is invalid");
            return;
        }
        this.mContext = context;
        if (context == null) {
            log("Constructor, mContext is null");
            return;
        }
        this.mPhones = phoneArr;
        if (phoneArr == null) {
            log("Constructor, mPhones is null");
            return;
        }
        int length = phoneArr.length;
        log("numPhones = " + length);
        if (this.mNumPhones != length || length <= 0) {
            log("Constructor, Phone count mismatch, cnt_tm = " + this.mNumPhones + ", cnt_pf = " + length);
            return;
        }
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.mTelephonyMgr = telephonyManager;
        if (telephonyManager == null) {
            log("mTelephonyMgr is null");
            return;
        }
        this.mCallState = new int[this.mNumPhones];
        for (int i2 = 0; i2 < this.mNumPhones; i2++) {
            this.mCallState[i2] = 0;
        }
        onInit();
        mInitState = 1;
    }

    public void dispose() {
        log("dispose, mHookAgent = " + mHookAgent);
        OemHookAgent oemHookAgent = mHookAgent;
        if (oemHookAgent != null) {
            oemHookAgent.dispose();
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
            String str = SystemProperties.get(PROPERTY_PHONE_INIT_COUNT, "0");
            int intFromString = ModemUtils.getIntFromString(str, false);
            int i = intFromString + 1;
            log("onInitForPhoneInitCount, initCountStr = " + str + ", initCountOld = " + intFromString + ", initCountNew = " + i);
            StringBuilder sb = new StringBuilder("");
            sb.append(i);
            SystemProperties.set(PROPERTY_PHONE_INIT_COUNT, sb.toString());
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
        int i = mHalChipId;
        if (i > 100 && i < 1001) {
            log("isMsmSupported, MSM is supported by this odm project, mHalChipId = " + mHalChipId);
            return true;
        }
        log("isMsmSupported, MSM is not supported by this project, mDeviceName = " + mDeviceName);
        return false;
    }

    public static void make(Context context, OemHookAgent oemHookAgent) {
        onEarlyInit();
        if (!isMsmSupported()) {
            log("Not supported, will do nothing");
            return;
        }
        boolean z = SystemProperties.getBoolean(PROPERTY_MODEM_SERVICE_MAIN_MONITER_CONTROL, true);
        log("mainEnabled: " + z);
        if (!z) {
            log("Modem service main moniter switch is closed, will not start Modem Service Moniter thread!");
            return;
        }
        if (context == null) {
            log("context is null, will not start Modem Service Moniter thread!");
            return;
        }
        if (oemHookAgent == null) {
            log("hookAgent is null, will not start Modem Service Moniter thread!");
            return;
        }
        mHookAgent = oemHookAgent;
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

        private OemCallStateNotifier(Phone[] phoneArr) {
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
            this.mPhones = phoneArr;
            ModemServiceMoniter.log("OemCallStateNotifier, mNumPhones = " + this.mNumPhones);
            int i = this.mNumPhones;
            this.mImsPhones = new ImsPhone[i];
            this.mFgCsCalls = new GsmCdmaCall[i];
            this.mBgCsCalls = new GsmCdmaCall[i];
            this.mRiCsCalls = new GsmCdmaCall[i];
            this.mFgImsCalls = new ImsPhoneCall[i];
            this.mBgImsCalls = new ImsPhoneCall[i];
            this.mRiImsCalls = new ImsPhoneCall[i];
            this.mCallStateHandler = new CallStateHandler();
            for (int i2 = 0; i2 < this.mNumPhones; i2++) {
                Phone phone = this.mPhones[i2];
                if (phone != null) {
                    phone.registerForPreciseCallStateChanged(this.mCallStateHandler, 101, Integer.valueOf(i2));
                    this.mFgCsCalls[i2] = (GsmCdmaCall) this.mPhones[i2].getForegroundCall();
                    this.mBgCsCalls[i2] = (GsmCdmaCall) this.mPhones[i2].getBackgroundCall();
                    this.mRiCsCalls[i2] = (GsmCdmaCall) this.mPhones[i2].getRingingCall();
                }
                this.mImsPhones[i2] = (ImsPhone) this.mPhones[i2].getImsPhone();
                ImsPhone imsPhone = this.mImsPhones[i2];
                if (imsPhone != null) {
                    imsPhone.registerForPreciseCallStateChanged(this.mCallStateHandler, 102, Integer.valueOf(i2));
                    this.mFgImsCalls[i2] = this.mImsPhones[i2].getForegroundCall();
                    this.mBgImsCalls[i2] = this.mImsPhones[i2].getBackgroundCall();
                    this.mRiImsCalls[i2] = this.mImsPhones[i2].getRingingCall();
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
        public boolean isCallActive(int i) {
            GsmCdmaCall gsmCdmaCall = this.mFgCsCalls[i];
            if (gsmCdmaCall != null && gsmCdmaCall.getState() == Call.State.ACTIVE) {
                return true;
            }
            GsmCdmaCall gsmCdmaCall2 = this.mBgCsCalls[i];
            if (gsmCdmaCall2 != null && gsmCdmaCall2.getState() == Call.State.ACTIVE) {
                return true;
            }
            GsmCdmaCall gsmCdmaCall3 = this.mRiCsCalls[i];
            if (gsmCdmaCall3 != null && gsmCdmaCall3.getState() == Call.State.ACTIVE) {
                return true;
            }
            ImsPhoneCall imsPhoneCall = this.mFgImsCalls[i];
            if (imsPhoneCall != null && imsPhoneCall.getState() == Call.State.ACTIVE) {
                return true;
            }
            ImsPhoneCall imsPhoneCall2 = this.mBgImsCalls[i];
            if (imsPhoneCall2 != null && imsPhoneCall2.getState() == Call.State.ACTIVE) {
                return true;
            }
            ImsPhoneCall imsPhoneCall3 = this.mRiImsCalls[i];
            return imsPhoneCall3 != null && imsPhoneCall3.getState() == Call.State.ACTIVE;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isCallIdle(int i) {
            GsmCdmaCall gsmCdmaCall = this.mFgCsCalls[i];
            if (gsmCdmaCall != null && !gsmCdmaCall.isIdle()) {
                return false;
            }
            GsmCdmaCall gsmCdmaCall2 = this.mBgCsCalls[i];
            if (gsmCdmaCall2 != null && !gsmCdmaCall2.isIdle()) {
                return false;
            }
            GsmCdmaCall gsmCdmaCall3 = this.mRiCsCalls[i];
            if (gsmCdmaCall3 != null && !gsmCdmaCall3.isIdle()) {
                return false;
            }
            ImsPhoneCall imsPhoneCall = this.mFgImsCalls[i];
            if (imsPhoneCall != null && !imsPhoneCall.isIdle()) {
                return false;
            }
            ImsPhoneCall imsPhoneCall2 = this.mBgImsCalls[i];
            if (imsPhoneCall2 != null && !imsPhoneCall2.isIdle()) {
                return false;
            }
            ImsPhoneCall imsPhoneCall3 = this.mRiImsCalls[i];
            return imsPhoneCall3 == null || imsPhoneCall3.isIdle();
        }

        class CallStateHandler extends Handler {
            private CallStateHandler() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int iIntValue = ((Integer) ((AsyncResult) message.obj).userObj).intValue();
                int i = message.what;
                if (i == 101 || i == 102) {
                    if (!OemCallStateNotifier.this.mIsCallInActiveState && OemCallStateNotifier.this.isCallActive(iIntValue)) {
                        ModemServiceMoniter.log("processCallStateChanged: call active on phone" + iIntValue);
                        OemCallStateNotifier.this.mIsCallInActiveState = true;
                        ModemServiceMoniter.this.onMsgPkCallStatusNotify(1, iIntValue);
                        return;
                    }
                    if (OemCallStateNotifier.this.isCallIdle(iIntValue)) {
                        ModemServiceMoniter.log("processCallStateChanged: call disconnected on phone" + iIntValue);
                        OemCallStateNotifier.this.mIsCallInActiveState = false;
                        OemCallStateNotifier.this.mIsCallInProgress = false;
                        ModemServiceMoniter.this.onMsgPkCallStatusNotify(0, iIntValue);
                        return;
                    }
                    if (OemCallStateNotifier.this.mIsCallInProgress) {
                        return;
                    }
                    ModemServiceMoniter.log("processCallStateChanged: call started on phone" + iIntValue);
                    OemCallStateNotifier.this.mIsCallInProgress = true;
                    ModemServiceMoniter.this.onMsgPkCallStatusNotify(1, iIntValue);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void log(String str) {
        Log.d(LOG_TAG, str);
    }
}
