package com.android.internal.telephony;

import android.app.timezonedetector.TelephonyTimeZoneSuggestion;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.hardware.display.DisplayManager;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.WorkSource;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.CarrierConfigManager;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellInfo;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.MiStubParam;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PreciseDataConnectionState;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyRegistryManager;
import android.text.TextUtils;
import android.util.NtpTrustedTime;
import android.util.SparseArray;
import android.view.Display;
import com.android.ims.ImsCall;
import com.android.internal.telephony.cdnr.CarrierDisplayNameData;
import com.android.internal.telephony.data.DataNetwork;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.data.MiuiDataControllerImpl;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCallTracker;
import com.android.internal.telephony.imsphone.MiuiImsControllerImpl;
import com.android.internal.telephony.nitz.TimeZoneSuggesterImpl;
import com.android.internal.telephony.qcrilhook.GUTIController;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.MiuiUiccCommonUtils;
import com.android.internal.telephony.vsim.CountryMonitor;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.function.TriFunction;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.modem.ModemUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.CommenUtils;
import miui.telephony.DefaultSimManager;
import miui.telephony.FeatureConfiguration;
import miui.telephony.MiuiDeviceStateListener;
import miui.telephony.MiuiDeviceStateMonitor;
import miui.telephony.MiuiDisplayInfoController;
import miui.telephony.MiuiTelephony;
import miui.telephony.PhoneDebug;
import miui.telephony.RegInfoMonitor;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyOptCloudController;
import miui.telephony.TelephonyStatAdapter;
import miui.telephony.VirtualSimUtils;
import vendor.xiaomi.hardware.radio.RadioError;

/* JADX INFO: loaded from: classes.dex */
public class MiuiNetworkControllerImpl implements MiuiNetworkControllerStub.IMiuiNetworkController, TelephonyOptCloudController.CloudInfoListener {
    private static final String ACTION_SS_OPTIMIZATION_SCENE_STATISTIC = "miui.intent.action.SS_OPTIMIZATION_SCENE_STATISTIC";
    private static final int ALL_REGISTERED_CELLID = 30;
    private static final int DEACTIVATE_CHECK_DELAY_TIME = 10000;
    private static final int DELAY_DISPLAY_ICON_MILLIS = 5000;
    private static final int DELAY_MODEM_RESET_UPDATE_TIMER = 6000;
    private static final int DELAY_OOS_SMOOTH_SIGNAL_UPDATE_TIMER = 60000;
    private static final int DELAY_SA_CALL_SIGNAL_UPDATE_TIMER = 3000;
    private static final int DELAY_SCREEN_ON_SIGNAL_UPDATE_TIME = 30000;
    private static final int DELAY_SIGNAL_UPDATE_TIMER = 10000;
    private static final int DIFF_TIME_ZONE = -2;
    private static final String DUAL_NR_ENABLED_SETTING = "dual_nr_enabled";
    private static final int ERROR_NITZ_INFO = -1;
    private static final int EVENT_CARRIER_CONFIG_CHANGED = 209;
    private static final int EVENT_DEACTIVATE_CHECK_DELAY = 1;
    private static final int EVENT_DELAY_DISPLAY_ICON = 3;
    private static final int EVENT_DELAY_NTNPHONE_SIGNAL_UPDATE = 210;
    private static final int EVENT_DELAY_SIGNAL_UPDATE = 203;
    private static final int EVENT_DEVICE_STATE_CHANGED = 2;
    private static final int EVENT_MIDDLE_SIGNAL_DISPLAY_TIMEOUT = 205;
    private static final int EVENT_MODEM_RESET = 202;
    private static final int EVENT_MODEM_RESET_DONE = 300;
    private static final int EVENT_NITZ_TIME_TEST = 201;
    private static final int EVENT_PRECISE_CALL_STATE_CHANGED = 7;
    private static final int EVENT_RADIO_RESET_DONE = 400;
    private static final int EVENT_REQUEST_CELL_INFO = 206;
    private static final int EVENT_RESET_RILD = 6;
    private static final int EVENT_RESPONSE_CELL_INFO = 207;
    private static final int EVENT_RE_REGISTRATION_NETWORK_DONE = 200;
    private static final int EVENT_RIL_MESSAGE_TIME_OUT = 4;
    private static final int EVENT_SCREEN_ON_DELAY_SIGNAL_TIMEOUT = 204;
    private static final int EVENT_SERVICE_STATE_DELAY = 2000;
    private static final int EVENT_SMOOTH_SIGNAL_UPDATE = 208;
    private static final int EVENT_UPDATE_FIVEG_ICON_GROUP = 5;
    private static final int EXCEPTION_TIME_VALUE = -3;
    private static final String FIVEG_ICON_GROUP_SETTING_PREFIX = "5g_icon_group_mode";
    private static final int FIVEG_SS_DELAY_PERIOD_MILLIS = 14000;
    private static final String KEY_EXTRA_STATE = "extra_state";
    private static final String NETWORK_DISPLAY_CONFIG_KEY = "network_display_config";
    private static final int NORMAL_NITZ = 0;
    private static final int NOT_DELAY_SIGNAL_UPDATE_TIMER = 0;
    private static final int PLMN_CONDITION_HOME = 2;
    private static final int PLMN_CONDITION_ROAMING = 1;
    private static final int PLMN_CONDITION_WIFICALLING = 3;
    private static final int PLMN_NO_CONDITION = 0;
    private static final int PLMN_RULE_CUSTOMIZED = 4;
    private static final int PLMN_RULE_OPERATOR_ALPHA_LONG = 2;
    private static final int PLMN_RULE_OPERATOR_ALPHA_SHORT = 3;
    private static final int PLMN_RULE_PLMN_CUSTOMIZED = 5;
    private static final int PLMN_RULE_SPN = 1;
    private static final String REGION_CHINA_MCC = "460";
    private static final int RELATIVE_SAME_NITZ_INFO = 1;
    private static final int RESET_RILD_DELAY_TIME = 300000;
    private static final int RESET_RILD_INTERVA_DELAY_TIME = 21600000;
    private static final String SPLIT_COMMA = ",";
    private static final String SS_OPTIMIZATION_EXTRA = "ss_optimization_extra";
    private static final String STATE_OPTIMIZATION = "optimization";
    private static final String STATE_TIME_CHACHE = "time_cache";
    private static final String TAG = "MiuiNetworkControllerImpl";
    private static GUTIController mGUTIController;
    private NitzData mAnchorNitzSignal;
    private long mBroadcastRilWakeLockRequestTime;
    private int mCurrentReportLevel;
    private MiuiDeviceStateMonitor mDeviceStateMonitor;
    private boolean mIsScreenOn;
    private boolean mIsShow5GIcon;
    private TelephonyTimeZoneSuggestion mLatestTimeZone;
    private ServiceStateTracker mOwner;
    private Phone mPhone;
    private int mPreferredNetworkMode;
    private long mResetRildTimeByRilMessageTimeOut;
    private TelephonyRegistryManager mTelephonyRegistryMgr;
    private MiuiTimeZoneOptimization mTimeZoneOptimization;
    private static HashMap<String, String> mSignalStrengthOptimizationCountMap = new HashMap<>();
    private static long NITZ_UPDATE_DIFF_TIME = 120000;
    private static long OFFSET_TIME = 60000;
    private static String CHINA_TIME_ZONE = "Asia/Shanghai";
    private boolean mIsModemDataRestoring = false;
    private boolean mIsModemResetting = false;
    private boolean mIsRadioResetting = false;
    private boolean mIsNotifySignalStrengthImmediately = false;
    private byte[] mNextDumpState = null;
    private boolean mSupportScreenSignal = false;
    private long mLastScreenOnTime = -1;
    private boolean mSupportNeighboringSignal = false;
    private List<CellInfoOptimized> mRegisteredCellInfo = new ArrayList();
    private int mLastRegisteredTac = -1;
    private int mUnitTimeDelay = 0;
    private int mPreMiuiLevel = 0;
    private int mTargetLevel = -1;
    private volatile boolean mIsNetworkModeChanging = false;
    private boolean mCarrierConfigLoaded = false;
    private int mPreviousDataRAT = 0;
    private int mPreviousVoiceRAT = 0;
    private long mCommonSignalStrengthOptimizationCount = 0;
    private long mLockedScreenSignalStrengthOptimizationCount = 0;
    private long mNeighborSignalStrengthOptimizationCount = 0;
    private int mCurrentDisplayNetworkType = 0;
    private int mNewDisplayNetworkType = 0;
    private long mDisplayIconNoRecoveryCount = 0;
    private long mDisplayIconRecoveryCount = 0;
    private boolean mIsRecordedOos = false;
    private int mLastServiceState = -1;
    private long mRilMessageTimeOutCount = 0;
    private long mOosRilMessageTimeOutCount = 0;
    private long mResetRildInVaildCount = 0;
    private long mResetRildCount = 0;
    private boolean mSupportUpdateSignalPrimaryInNsa = false;
    private boolean mSupportOosResetRild = false;
    private boolean mSupportNitzTimeZonePreferred = false;
    private boolean mIsCloudNitzTimeZonePreferredDisableFeature = false;
    private Handler mHandler = new Handler() { // from class: com.android.internal.telephony.MiuiNetworkControllerImpl.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            ServiceStateTracker ssT;
            SignalStrength realSignalStrength;
            DataNetwork dataNetwork;
            switch (msg.what) {
                case 1:
                    Processor.checkDeactiveForCustSingleSim();
                    break;
                case 2:
                    Processor.processDeviceStateChange(MiuiNetworkControllerImpl.this.mPhone, MiuiNetworkControllerImpl.this.mDeviceStateMonitor);
                    break;
                case 3:
                    MiuiNetworkControllerImpl.this.log("EVENT_DELAY_DISPLAY_ICON, phoneId = " + MiuiNetworkControllerImpl.this.mPhone.getPhoneId());
                    MiuiNetworkControllerImpl.this.mDisplayIconNoRecoveryCount++;
                    MiuiNetworkControllerImpl.this.mCurrentDisplayNetworkType = MiuiNetworkControllerImpl.this.mNewDisplayNetworkType;
                    break;
                case 4:
                    MiuiNetworkControllerImpl.this.log("EVENT_RIL_MESSAGE_TIME_OUT, phoneId = " + MiuiNetworkControllerImpl.this.mPhone.getPhoneId());
                    MiuiNetworkControllerImpl.this.doRilMessageTimeOut(msg);
                    break;
                case 5:
                    MiuiNetworkControllerImpl.this.mIsShow5GIcon = Settings.Global.getInt(MiuiNetworkControllerImpl.this.mPhone.getContext().getContentResolver(), new StringBuilder().append(MiuiNetworkControllerImpl.FIVEG_ICON_GROUP_SETTING_PREFIX).append(MiuiNetworkControllerImpl.this.mPhone.getPhoneId()).toString(), 0) != 0;
                    break;
                case 6:
                    MiuiNetworkControllerImpl.this.log("EVENT_RESET_RILD");
                    MiuiNetworkControllerImpl.this.mResetRildInVaildCount++;
                    break;
                case 7:
                    MiuiNetworkControllerImpl.this.log("EVENT_PRECISE_CALL_STATE_CHANGED");
                    if (MiuiNetworkControllerImpl.this.mPhone.getState() != PhoneConstants.State.IDLE) {
                        if (MiuiNetworkControllerImpl.this.mHandler.hasMessages(MiuiNetworkControllerImpl.EVENT_SCREEN_ON_DELAY_SIGNAL_TIMEOUT) || MiuiNetworkControllerImpl.this.mHandler.hasMessages(MiuiNetworkControllerImpl.EVENT_MIDDLE_SIGNAL_DISPLAY_TIMEOUT)) {
                            MiuiNetworkControllerImpl.this.log("Incoming call, update delay signal");
                            MiuiNetworkControllerImpl.this.mHandler.removeMessages(MiuiNetworkControllerImpl.EVENT_SCREEN_ON_DELAY_SIGNAL_TIMEOUT);
                            MiuiNetworkControllerImpl.this.mHandler.removeMessages(MiuiNetworkControllerImpl.EVENT_MIDDLE_SIGNAL_DISPLAY_TIMEOUT);
                            MiuiNetworkControllerImpl.this.setOptimizedLevel(0);
                        }
                    }
                    break;
                case 200:
                    MiuiNetworkControllerImpl.this.log("EVENT_RE_REGISTRATION_NETWORK_DONE");
                    MiuiNetworkControllerImpl.this.onModemResetComplete(MiuiNetworkControllerImpl.this.mPhone);
                    break;
                case MiuiNetworkControllerImpl.EVENT_NITZ_TIME_TEST /* 201 */:
                    String nitzTest = (String) msg.obj;
                    int nitzPhoneId = msg.arg1;
                    int deltaNitzReceiveTest = msg.arg2;
                    long startTime = SystemClock.elapsedRealtime();
                    long nitzReceiveTest = ((long) deltaNitzReceiveTest) + startTime;
                    MiuiNetworkControllerImpl.this.log("EVENT_NITZ_TIME_TEST,  nitzTest:" + nitzTest + ",nitzReceiveTest:" + nitzReceiveTest);
                    if (nitzPhoneId == MiuiNetworkControllerImpl.this.mPhone.getPhoneId() && (ssT = MiuiNetworkControllerImpl.this.mPhone.getServiceStateTracker()) != null) {
                        ssT.setTimeFromNITZString(nitzTest, nitzReceiveTest, deltaNitzReceiveTest);
                        break;
                    }
                    break;
                case MiuiNetworkControllerImpl.EVENT_MODEM_RESET /* 202 */:
                    MiuiNetworkControllerImpl.this.log("Modem reset handle: restart event");
                    MiuiNetworkControllerImpl.this.updateModemResetFlag(true);
                    removeMessages(300);
                    int delayTimer = MiuiNetworkControllerImpl.this.mPhone.getContext().getResources().getInteger(285933642);
                    if (MiuiNetworkControllerImpl.this.mNextDumpState != null) {
                        if (MiuiNetworkControllerImpl.this.mNextDumpState[1] == 0) {
                            delayTimer = 120000;
                        } else if (MiuiNetworkControllerImpl.this.mNextDumpState[1] == 3) {
                            delayTimer = 65000;
                        }
                    }
                    sendEmptyMessageDelayed(300, delayTimer);
                    break;
                case MiuiNetworkControllerImpl.EVENT_DELAY_SIGNAL_UPDATE /* 203 */:
                    MiuiNetworkControllerImpl.this.handleOptimizedLevel();
                    if (MiuiNetworkControllerImpl.this.mIsScreenOn) {
                        MiuiNetworkControllerImpl.this.mCommonSignalStrengthOptimizationCount++;
                    }
                    break;
                case MiuiNetworkControllerImpl.EVENT_SCREEN_ON_DELAY_SIGNAL_TIMEOUT /* 204 */:
                    if (MiuiNetworkControllerImpl.this.mPhone != null && MiuiNetworkControllerImpl.this.mCurrentShowSignalStrength != null && MiuiNetworkControllerImpl.this.mPhone.getSignalStrengthController() != null && (realSignalStrength = MiuiNetworkControllerImpl.this.mPhone.getSignalStrengthController().getSignalStrength()) != null) {
                        int currentShowMiuiLevel = MiuiNetworkControllerImpl.this.mCurrentShowSignalStrength.getMiuiLevel();
                        int realMiuiLevel = realSignalStrength.getMiuiLevel();
                        MiuiNetworkControllerImpl.this.log("EVENT_SCREEN_ON_DELAY_SIGNAL_TIMEOUT: currentShowMiuiLevel=" + currentShowMiuiLevel + ", realMiuiLevel=" + realMiuiLevel);
                        MiuiNetworkControllerImpl.this.trackOptimizationSs(MiuiNetworkControllerImpl.ACTION_SS_OPTIMIZATION_SCENE_STATISTIC, "timeout");
                        if (currentShowMiuiLevel - realMiuiLevel <= 2) {
                            MiuiNetworkControllerImpl.this.setOptimizedLevel(0);
                        } else {
                            int middleLevel = (int) Math.floor((currentShowMiuiLevel + realMiuiLevel) / 2);
                            MiuiNetworkControllerImpl.this.setOptimizedLevel(middleLevel);
                            Random random = new Random();
                            int num = random.nextInt(10);
                            MiuiNetworkControllerImpl.this.log("EVENT_SCREEN_ON_DELAY_SIGNAL_TIMEOUT: middleLevel=" + middleLevel + ", num=" + num);
                            MiuiNetworkControllerImpl.this.mHandler.sendEmptyMessageDelayed(MiuiNetworkControllerImpl.EVENT_MIDDLE_SIGNAL_DISPLAY_TIMEOUT, num * 1000);
                        }
                        break;
                    }
                    break;
                case MiuiNetworkControllerImpl.EVENT_MIDDLE_SIGNAL_DISPLAY_TIMEOUT /* 205 */:
                    MiuiNetworkControllerImpl.this.log("EVENT_MIDDLE_SIGNAL_DISPLAY_TIMEOUT");
                    MiuiNetworkControllerImpl.this.setOptimizedLevel(0);
                    break;
                case MiuiNetworkControllerImpl.EVENT_REQUEST_CELL_INFO /* 206 */:
                    MiuiNetworkControllerImpl.this.log("EVENT_REQUEST_CELL_INFO");
                    MiuiNetworkControllerImpl.this.mPhone.mCi.getCellInfoList(MiuiNetworkControllerImpl.this.mHandler.obtainMessage(MiuiNetworkControllerImpl.EVENT_RESPONSE_CELL_INFO), (WorkSource) null);
                    break;
                case MiuiNetworkControllerImpl.EVENT_RESPONSE_CELL_INFO /* 207 */:
                    MiuiNetworkControllerImpl.this.log("EVENT_RESPONSE_CELL_INFO");
                    MiuiNetworkControllerImpl.this.processCellInfo((AsyncResult) msg.obj);
                    break;
                case MiuiNetworkControllerImpl.EVENT_SMOOTH_SIGNAL_UPDATE /* 208 */:
                    if (MiuiNetworkControllerImpl.this.mPhone != null && MiuiNetworkControllerImpl.this.mCurrentShowSignalStrength != null) {
                        int currentShowLevel = MiuiNetworkControllerImpl.this.mCurrentShowSignalStrength.getOptimizedLevel();
                        if (currentShowLevel > 0) {
                            Rlog.d(MiuiNetworkControllerImpl.TAG, "notifySignalStrength currentShowLevel= " + currentShowLevel);
                            MiuiNetworkControllerImpl.this.mCurrentShowSignalStrength.setMiuiLevel(0);
                            MiuiNetworkControllerImpl.this.mCurrentShowSignalStrength.setOptimizedLevel(currentShowLevel - 1);
                            MiuiNetworkControllerImpl.this.mTelephonyRegistryMgr.notifySignalStrengthChanged(MiuiNetworkControllerImpl.this.mPhone.getPhoneId(), MiuiNetworkControllerImpl.this.mPhone.getSubId(), MiuiNetworkControllerImpl.this.mCurrentShowSignalStrength);
                        }
                        MiuiNetworkControllerImpl.this.handleOOSSmooth();
                        break;
                    }
                    break;
                case MiuiNetworkControllerImpl.EVENT_DELAY_NTNPHONE_SIGNAL_UPDATE /* 210 */:
                    Phone phone = (Phone) msg.obj;
                    MiuiNetworkControllerImpl.this.log("EVENT_DELAY_NTNPHONE_SIGNAL_UPDATE, update miuilevel ");
                    MiuiNetworkControllerImpl.this.setNtnPhoneOptimizedLevel(phone);
                    break;
                case 300:
                    MiuiNetworkControllerImpl.this.log("EVENT_MODEM_RESET_DONE");
                    if (MiuiNetworkControllerImpl.this.mIsModemResetting) {
                        MiuiNetworkControllerImpl.this.onModemResetComplete(MiuiNetworkControllerImpl.this.mPhone);
                    }
                    break;
                case 400:
                    MiuiNetworkControllerImpl.this.log("EVENT_RADIO_RESET_DONE");
                    if (MiuiNetworkControllerImpl.this.mIsRadioResetting) {
                        MiuiNetworkControllerImpl.this.onModemResetComplete(MiuiNetworkControllerImpl.this.mPhone);
                    }
                    break;
                case 2000:
                    MiuiNetworkControllerImpl.this.mIsNetworkModeChanging = false;
                    MiuiNetworkControllerImpl.this.log("SS display optimization, trigger to pollState");
                    MiuiNetworkControllerImpl.this.mPhone.notifyServiceStateChangedP(MiuiNetworkControllerImpl.this.mPhone.getServiceState());
                    MiuiNetworkControllerImpl.this.mPhone.getServiceStateTracker().pollState();
                    if (MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING) {
                        MiuiNetworkControllerImpl.this.mPhone.getServiceStateTracker().updateCarrierDisplayName();
                    }
                    MiuiDataControllerImpl miuiDataController = (MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, MiuiNetworkControllerImpl.this.mPhone.getPhoneId());
                    if (miuiDataController != null && (dataNetwork = miuiDataController.findActiveInternetDataNetwork()) != null && dataNetwork.getDataProfile() != null && MiuiNetworkControllerImpl.this.mPhone.getServiceState() != null) {
                        MiuiNetworkControllerImpl.this.log("SS display optimization, trigger to notifyDataConnection");
                        MiuiNetworkControllerImpl.this.mPhone.notifyDataConnection(new PreciseDataConnectionState.Builder().setTransportType(dataNetwork.getTransport()).setState(2).setApnSetting(dataNetwork.getDataProfile().getApnSetting()).setNetworkType(MiuiNetworkControllerImpl.this.mPhone.getServiceState().getDataNetworkType()).setLinkProperties(dataNetwork.getLinkProperties()).build());
                        break;
                    }
                    break;
            }
        }
    };
    private MiuiDeviceStateListener mDeviceStateListener = new MiuiDeviceStateListener() { // from class: com.android.internal.telephony.MiuiNetworkControllerImpl.2
        @Override // miui.telephony.MiuiDeviceStateListener
        public void onDeviceStateChange(boolean screenOn, boolean charging, boolean powerSaveOn) {
            if (MiuiNetworkControllerImpl.this.mDeviceStateMonitor != null && !MiuiNetworkControllerImpl.this.mDeviceStateMonitor.shouldTurnOffServiceStateFrequentUpdate()) {
                MiuiNetworkControllerImpl.this.mHandler.removeMessages(2);
                Message msg = MiuiNetworkControllerImpl.this.mHandler.obtainMessage(2);
                MiuiNetworkControllerImpl.this.mHandler.sendMessage(msg);
            }
        }
    };
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.MiuiNetworkControllerImpl.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(CommenUtils.ACTIVE_RE_REGISTRATION_NETWORK)) {
                int slotId = intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1);
                MiuiNetworkControllerImpl.this.log("BroadcastReceiver ACTIVE_RE_REGISTRATION_NETWORK, slotId = " + slotId);
                if (slotId != -1 && MiuiNetworkControllerImpl.this.mPhone.getPhoneId() != slotId) {
                    return;
                }
                MiuiNetworkControllerImpl.this.mIsModemDataRestoring = true;
                if (MiuiNetworkControllerImpl.this.mHandler.hasMessages(200)) {
                    MiuiNetworkControllerImpl.this.mHandler.removeMessages(200);
                }
                String reason = intent.getStringExtra(CommenUtils.RE_REGISTRATION_NETWORK_REASON);
                Processor.setDelayUpdateReason(MiuiNetworkControllerImpl.this.mPhone.getPhoneId(), reason);
                int delayTimer = CommenUtils.getReRegistrationDelayTimer(CommenUtils.AVOID_OOS_FOR_SST, reason);
                MiuiNetworkControllerImpl.this.mHandler.sendEmptyMessageDelayed(200, delayTimer);
                return;
            }
            if (intent.getAction().equals(CommenUtils.ACTION_RESET_RADIO)) {
                MiuiNetworkControllerImpl.this.log("BroadcastReceiver Radio reset ");
                MiuiNetworkControllerImpl.this.mIsRadioResetting = true;
                MiuiNetworkControllerImpl.this.mHandler.removeMessages(400);
                MiuiNetworkControllerImpl.this.mHandler.sendEmptyMessageDelayed(400, 6000L);
                return;
            }
            if (intent.getAction().equals("miui.intent.action.NITZ_TEST")) {
                String nitzString = intent.getStringExtra("nitz_time");
                int nitzReceiveTime = intent.getIntExtra("nitz_receive", -1);
                int nitzPhoneId = intent.getIntExtra("nitz_phone_id", -1);
                if (nitzPhoneId == MiuiNetworkControllerImpl.this.mPhone.getPhoneId()) {
                    MiuiNetworkControllerImpl.this.log("NITZ_TEST:" + nitzString + "," + nitzReceiveTime + "," + nitzPhoneId);
                    MiuiNetworkControllerImpl.this.log("Build.TAGS:" + Build.TAGS);
                    if (!ModemUtils.PROP_BUILD_TAGS_RELE.equals(Build.TAGS) && !TextUtils.isEmpty(nitzString) && nitzReceiveTime != -1 && nitzPhoneId != -1) {
                        MiuiNetworkControllerImpl.this.mHandler.sendMessage(MiuiNetworkControllerImpl.this.mHandler.obtainMessage(MiuiNetworkControllerImpl.EVENT_NITZ_TIME_TEST, nitzPhoneId, nitzReceiveTime, nitzString));
                        return;
                    }
                    return;
                }
                return;
            }
            if (intent.getAction().equals("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED")) {
                if (MiuiTelephony.IS_MTK) {
                    int oldSlotId = intent.getIntExtra("old_data_slot", -1);
                    int cardCount = TelephonyManager.getDefault().getIccCardCount();
                    MiuiNetworkControllerImpl.this.log("oldSlotId =" + oldSlotId + ",cardCount =" + cardCount);
                    if (oldSlotId != -1 && cardCount == 2) {
                        String region = SystemProperties.get("ro.miui.region", "");
                        if ("".equals(region)) {
                            region = SystemProperties.get("ro.product.locale.region", "");
                        }
                        TelephonyManagerEx manager = TelephonyManagerEx.getDefault();
                        int ddsSlot = intent.getIntExtra(SubscriptionManager.SLOT_KEY, -1);
                        if (ddsSlot == 0 && !"".equals(region) && manager != null && manager.isSupportEsimForCountry(region) && !manager.isEsimActive()) {
                            MiuiNetworkControllerImpl.this.log("ignore esim not active case");
                            return;
                        } else {
                            MiuiNetworkControllerImpl.this.optimizateServiceStateDisplay("data slot changed");
                            return;
                        }
                    }
                    return;
                }
                return;
            }
            if ("android.intent.action.SCREEN_ON".equals(intent.getAction())) {
                MiuiNetworkControllerImpl.this.log("BroadcastReceiver ACTION_SCREEN_ON");
                if (TelephonyOptCloudController.CLOUD_CONTROLLER_NETWORK_LOCKED_DISABLED) {
                    return;
                }
                MiuiNetworkControllerImpl.this.mIsScreenOn = true;
                long screenOnTime = SystemClock.elapsedRealtime();
                long j = screenOnTime - MiuiNetworkControllerImpl.this.mLastScreenOnTime;
                MiuiNetworkControllerImpl miuiNetworkControllerImpl = MiuiNetworkControllerImpl.this;
                if (j < 300000) {
                    miuiNetworkControllerImpl.mLastScreenOnTime = screenOnTime;
                    if (MiuiNetworkControllerImpl.this.mPhone != null && MiuiNetworkControllerImpl.this.mPhone.getSignalStrengthController() != null) {
                        MiuiNetworkControllerImpl.this.mPhone.getSignalStrengthController().notifySignalStrength();
                    }
                    MiuiNetworkControllerImpl.this.log("screens on is less than five minutes");
                    return;
                }
                if (miuiNetworkControllerImpl.isCardAbsentOrNoService() || MiuiNetworkControllerImpl.this.mPhone.getState() != PhoneConstants.State.IDLE) {
                    MiuiNetworkControllerImpl.this.log("screens on is no card or no service or call");
                    MiuiNetworkControllerImpl.this.mLastScreenOnTime = screenOnTime;
                    if (MiuiNetworkControllerImpl.this.mPhone != null && MiuiNetworkControllerImpl.this.mPhone.getSignalStrengthController() != null) {
                        MiuiNetworkControllerImpl.this.mPhone.getSignalStrengthController().notifySignalStrength();
                        return;
                    }
                    return;
                }
                MiuiNetworkControllerImpl.this.mLastScreenOnTime = screenOnTime;
                MiuiNetworkControllerImpl.this.mHandler.sendEmptyMessageDelayed(MiuiNetworkControllerImpl.EVENT_SCREEN_ON_DELAY_SIGNAL_TIMEOUT, 30000L);
                return;
            }
            if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                MiuiNetworkControllerImpl.this.log("BroadcastReceiver ACTION_SCREEN_OFF");
                if (TelephonyOptCloudController.CLOUD_CONTROLLER_NETWORK_LOCKED_DISABLED) {
                    MiuiNetworkControllerImpl.this.mSupportScreenSignal = false;
                    MiuiNetworkControllerImpl.this.unregisterForPreciseCallStateChanged();
                }
                if (MiuiNetworkControllerImpl.this.mHandler.hasMessages(MiuiNetworkControllerImpl.EVENT_SCREEN_ON_DELAY_SIGNAL_TIMEOUT) && MiuiNetworkControllerImpl.this.mPhone.getSignalStrengthController() != null) {
                    SignalStrength realSignalStrength = MiuiNetworkControllerImpl.this.mPhone.getSignalStrengthController().getSignalStrength();
                    if (MiuiNetworkControllerImpl.this.mCurrentShowSignalStrength.getMiuiLevel() > realSignalStrength.getMiuiLevel()) {
                        MiuiNetworkControllerImpl.this.mLockedScreenSignalStrengthOptimizationCount++;
                    }
                    MiuiNetworkControllerImpl.this.trackOptimizationSs(MiuiNetworkControllerImpl.ACTION_SS_OPTIMIZATION_SCENE_STATISTIC, "screen off");
                    MiuiNetworkControllerImpl.this.mHandler.removeMessages(MiuiNetworkControllerImpl.EVENT_SCREEN_ON_DELAY_SIGNAL_TIMEOUT);
                }
                MiuiNetworkControllerImpl.this.mIsScreenOn = false;
                MiuiNetworkControllerImpl.this.mIsScreenOffSignalStrength = MiuiNetworkControllerImpl.this.mCurrentShowSignalStrength;
                return;
            }
            if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(intent.getAction())) {
                int phoneId = intent.getExtras().getInt("android.telephony.extra.SLOT_INDEX");
                if (phoneId == MiuiNetworkControllerImpl.this.mPhone.getPhoneId()) {
                    MiuiNetworkControllerImpl.this.mHandler.sendEmptyMessage(MiuiNetworkControllerImpl.EVENT_CARRIER_CONFIG_CHANGED);
                    return;
                }
                return;
            }
            if ("android.intent.action.SERVICE_STATE".equals(intent.getAction())) {
                int serviceState = MiuiNetworkControllerImpl.this.mPhone.getServiceStateTracker().getCombinedRegState(ServiceState.newFromBundle(intent.getExtras()));
                if (MiuiNetworkControllerImpl.this.mPhone.getPhoneId() == intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1)) {
                    if (serviceState == 0 && MiuiNetworkControllerImpl.this.mLastServiceState != 0) {
                        MiuiNetworkControllerImpl.this.mIsRecordedOos = false;
                        MiuiNetworkControllerImpl.this.mHandler.removeMessages(6);
                    }
                    MiuiNetworkControllerImpl.this.mLastServiceState = serviceState;
                    MiuiNetworkControllerImpl.this.log("mLastServiceState = " + MiuiNetworkControllerImpl.this.mLastServiceState);
                    return;
                }
                return;
            }
            if (SarDeviceStateListener.BC_INTENT_ACTION_SATELLITE_STATE_CHANGE.equals(intent.getAction())) {
                boolean isNtnEnabled = intent.getBooleanExtra(SarDeviceStateListener.BC_INTENT_EXTRA_SATELLITE_STATE_IS_ENABLE, false);
                if (!isNtnEnabled && MiuiNetworkControllerImpl.this.mHandler.hasMessages(MiuiNetworkControllerImpl.EVENT_DELAY_NTNPHONE_SIGNAL_UPDATE)) {
                    MiuiNetworkControllerImpl.this.mHandler.removeMessages(MiuiNetworkControllerImpl.EVENT_DELAY_NTNPHONE_SIGNAL_UPDATE);
                }
                MiuiNetworkControllerImpl.this.log("NtnPhone changed, enabled = " + isNtnEnabled);
                return;
            }
            if ("thinmd.dumpLevel".equals(intent.getAction())) {
                MiuiNetworkControllerImpl.this.mNextDumpState = intent.getByteArrayExtra("dumpLevel");
                MiuiNetworkControllerImpl.this.log("mNextDumpState " + ((int) MiuiNetworkControllerImpl.this.mNextDumpState[1]));
            }
        }
    };
    private SignalStrength mCurrentShowSignalStrength = new SignalStrength();
    private SignalStrength mIsScreenOffSignalStrength = null;
    private ServiceState mCurrentShowServiceState = null;
    private boolean mSameTimeZone = false;
    private boolean mIsChinaTimeZone = false;
    private boolean mIsUserFiveGEnabled = true;
    private ContentObserver mSsDisplayOptimization = new ContentObserver(this.mHandler) { // from class: com.android.internal.telephony.MiuiNetworkControllerImpl.4
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            MiuiNetworkControllerImpl.this.updateUserFiveGEnabledValue();
        }
    };
    ContentObserver mPreferredNetworkModeObserver = new ContentObserver(this.mHandler) { // from class: com.android.internal.telephony.MiuiNetworkControllerImpl.5
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            int newPreferredNetworkMode = DefaultSimManager.getPreferredNetworkModeFromDb(MiuiNetworkControllerImpl.this.mPhone.getContext(), MiuiNetworkControllerImpl.this.mPhone.getPhoneId());
            Rlog.d(MiuiNetworkControllerImpl.TAG, "onChange, newPreferredNetworkMode = " + newPreferredNetworkMode + ", mPreferredNetworkMode = " + MiuiNetworkControllerImpl.this.mPreferredNetworkMode + ", mPhone.getPhoneId() = " + MiuiNetworkControllerImpl.this.mPhone.getPhoneId());
            if (MiuiNetworkControllerImpl.this.mPreferredNetworkMode != newPreferredNetworkMode && MiuiNetworkControllerImpl.this.mHandler.hasMessages(3)) {
                MiuiNetworkControllerImpl.this.mHandler.removeMessages(3);
                MiuiNetworkControllerImpl.this.mCurrentDisplayNetworkType = MiuiNetworkControllerImpl.this.mNewDisplayNetworkType;
                MiuiNetworkControllerImpl.this.mDisplayIconRecoveryCount++;
            }
            MiuiNetworkControllerImpl.this.mPreferredNetworkMode = newPreferredNetworkMode;
        }
    };
    ContentObserver m5GNetworkTypeObserver = new ContentObserver(this.mHandler) { // from class: com.android.internal.telephony.MiuiNetworkControllerImpl.6
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            MiuiNetworkControllerImpl.this.mHandler.obtainMessage(5).sendToTarget();
        }
    };
    ContentObserver mNitzTimeZonePreferredObserver = new ContentObserver(this.mHandler) { // from class: com.android.internal.telephony.MiuiNetworkControllerImpl.7
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            boolean isEnable = Settings.Global.getInt(MiuiNetworkControllerImpl.this.mPhone.getContext().getContentResolver(), "enable_nitz_timezone_preferred", 1) == 1;
            MiuiNetworkControllerImpl.this.log("Nitz TimeZone enable change, isEnable = " + isEnable + ", mSupportNitzTimeZonePreferred = " + MiuiNetworkControllerImpl.this.mSupportNitzTimeZonePreferred);
            MiuiNetworkControllerImpl miuiNetworkControllerImpl = MiuiNetworkControllerImpl.this;
            if (!isEnable) {
                miuiNetworkControllerImpl.mSupportNitzTimeZonePreferred = false;
                if (MiuiNetworkControllerImpl.this.mTimeZoneOptimization != null) {
                    MiuiNetworkControllerImpl.this.mTimeZoneOptimization.stopNitzTimeZonePreferred();
                    return;
                }
                return;
            }
            miuiNetworkControllerImpl.mSupportNitzTimeZonePreferred = MiuiNetworkControllerImpl.this.isSupportNitzTimeZonePreferred();
        }
    };

    public void init(Phone phone, Object... arg) {
        this.mPhone = phone;
        if (arg.length > 0 && (arg[0] instanceof ServiceStateTracker)) {
            this.mOwner = (ServiceStateTracker) arg[0];
        }
        MiuiPhoneImpl miuiPhone = (MiuiPhoneImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiPhoneImpl.class, this.mPhone.getPhoneId());
        if (miuiPhone != null) {
            this.mDeviceStateMonitor = miuiPhone.getDeviceStateMonitor();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(CommenUtils.ACTION_RESET_RADIO);
        this.mPhone.getContext().registerReceiver(this.mIntentReceiver, filter, "miui.permission.RESET_RADIO", null, 2);
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(CommenUtils.ACTIVE_RE_REGISTRATION_NETWORK);
        filter2.addAction("miui.intent.action.NITZ_TEST");
        filter2.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
        this.mTelephonyRegistryMgr = (TelephonyRegistryManager) this.mPhone.getContext().getSystemService("telephony_registry");
        this.mSupportScreenSignal = !miui.os.Build.IS_INTERNATIONAL_BUILD && this.mPhone.getContext().getResources().getBoolean(285540420);
        this.mSupportUpdateSignalPrimaryInNsa = FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_NSA_SIGNAL_OPTIMIZATION);
        if (this.mSupportScreenSignal) {
            this.mIsScreenOn = isScreenOn();
            filter2.addAction("android.intent.action.SCREEN_OFF");
            filter2.addAction("android.intent.action.SCREEN_ON");
            CallManager.getInstance(this.mPhone.getContext()).registerForPreciseCallStateChanged(this.mHandler, 7, (Object) null);
        }
        this.mSupportNeighboringSignal = !miui.os.Build.IS_INTERNATIONAL_BUILD && this.mPhone.getContext().getResources().getBoolean(285540427);
        this.mIsShow5GIcon = Settings.Global.getInt(this.mPhone.getContext().getContentResolver(), new StringBuilder().append(FIVEG_ICON_GROUP_SETTING_PREFIX).append(this.mPhone.getPhoneId()).toString(), 0) != 0;
        filter2.addAction("miui.intent.action.ACTION_DEFAULT_DATA_SLOT_CHANGED");
        filter2.addAction("android.intent.action.SERVICE_STATE");
        filter2.addAction("thinmd.dumpLevel");
        if (TelephonyManagerEx.getDefault().isSupportSatelliteByDevice()) {
            filter2.addAction(SarDeviceStateListener.BC_INTENT_ACTION_SATELLITE_STATE_CHANGE);
        }
        this.mPhone.getContext().registerReceiver(this.mIntentReceiver, filter2, 2);
        if (this.mDeviceStateMonitor != null) {
            this.mDeviceStateMonitor.registerDeviceStateListener(this.mDeviceStateListener);
        }
        this.mPhone.mCi.registerForModemReset(this.mHandler, EVENT_MODEM_RESET, (Object) null);
        MiuiTelephony.getInstance().registerTelephonyPlugins(this, phone.getPhoneId());
        if (MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING) {
            registerSsDisplayOptimizationEvent();
        }
        this.mSupportOosResetRild = FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_OOS_RILD_SELF_RECOVERY);
        this.mPreferredNetworkMode = DefaultSimManager.getPreferredNetworkModeFromDb(this.mPhone.getContext(), this.mPhone.getPhoneId());
        this.mPhone.getContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor("preferred_network_mode"), false, this.mPreferredNetworkModeObserver);
        register5GNetworkTypeObserver();
        boolean isSupportDeviceNitzTimeZonePreferred = FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_NITZ_TIMEZONE_PREFERRED);
        this.mSupportNitzTimeZonePreferred = isSupportNitzTimeZonePreferred();
        this.mIsCloudNitzTimeZonePreferredDisableFeature = TelephonyOptCloudController.CLOUD_CONTROLLER_NITZ_TIMEZONE_PREFERRED_DISABLE;
        if (isSupportDeviceNitzTimeZonePreferred) {
            registerEnableNitzTimeZonePreferredObserver();
            TelephonyOptCloudController.getInstance().addCloudInfoListener(this);
            this.mTimeZoneOptimization = new MiuiTimeZoneOptimization(phone);
        }
        SystemProperties.set("radio.modem.modemresetting", String.valueOf(false));
    }

    public void dispose(Phone phone) {
        this.mPhone.getContext().unregisterReceiver(this.mIntentReceiver);
        this.mPhone.mCi.unregisterForModemReset(this.mHandler);
        if (MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING) {
            this.mPhone.getContext().getContentResolver().unregisterContentObserver(this.mSsDisplayOptimization);
        }
        MiuiTelephony.getInstance().unregisterTelephonyPlugins(this, phone.getPhoneId());
        this.mPhone.getContext().getContentResolver().unregisterContentObserver(this.mPreferredNetworkModeObserver);
        unRegister5GNetworkTypeObserver();
        unregisterEnableNitzTimeZonePreferredObserver();
        if (this.mSupportScreenSignal) {
            unregisterForPreciseCallStateChanged();
        }
        this.mPhone = null;
    }

    public boolean isOperatorConsideredNonRoaming(Phone phone, String newOperatorNumeric) {
        boolean isNonRoamingOperator = HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "operator_considered_non_roaming", false);
        if (phone.getOperatorNumeric() != null && (phone.getOperatorNumeric().equals(newOperatorNumeric) || (isNonRoamingOperator && TextUtils.isEmpty(newOperatorNumeric)))) {
            log("isOperatorConsideredNonRoaming getOperatorNumeric = " + this.mPhone.getOperatorNumeric());
            return true;
        }
        return Processor.isOperatorConsideredNonRoaming(phone, newOperatorNumeric);
    }

    public String getDataOperatorNumeric(Phone phone, String operatorNumeric) {
        return Processor.getOperatorNumericFromPs(phone.getServiceStateTracker(), operatorNumeric);
    }

    public int convertNetworkType(Phone phone, int type) {
        return Processor.convertNetworkType(phone.getServiceStateTracker(), type);
    }

    public void dump(Phone phone, FileDescriptor fd, PrintWriter pw, String[] args) {
        Processor.dump(phone, fd, pw, args);
    }

    public void handleMccMncChange(Phone phone, ServiceState newSS, ServiceState ss) {
        Processor.handleMccMncChange(phone.getContext(), newSS, ss);
    }

    public String getWfcDisplaySpn(Phone phone, String wfcVoiceSpnFormat, String originalSpn) {
        return Processor.getWfcDisplaySpn(phone.getServiceStateTracker(), wfcVoiceSpnFormat, originalSpn);
    }

    public String getWfcDisplayPlmn(Phone phone, String wfcVoiceSpnFormat, String originalPlmn) {
        return Processor.getWfcDisplayPlmn(phone.getServiceStateTracker(), wfcVoiceSpnFormat, originalPlmn);
    }

    public String getSpn(ServiceStateTracker sst) {
        return Processor.getSpn(sst);
    }

    public String getPlmn(ServiceStateTracker sst) {
        return Processor.getPlmn(sst);
    }

    public boolean isDelayUpdateServiceState(Phone phone) {
        return this.mIsModemResetting || this.mIsModemDataRestoring || this.mIsNetworkModeChanging || MiuiDisplayInfoController.getInstance().shouldDelayForSpecialScenario(phone);
    }

    public void handleMessage(Phone phone, Message msg) {
        log("received event " + msg.what);
        switch (msg.what) {
            case 1:
                if (phone.mCi.getRadioState() == 1) {
                    phone.getServiceStateTracker().pollStateDone();
                    log("manually pollStateDone ");
                }
                break;
            case 4:
                if (phone.getPhoneType() == 1) {
                    RegInfoMonitor.notifyRegInfoUpdate(phone, (NetworkRegistrationInfo) ((AsyncResult) msg.obj).result);
                    break;
                }
            case 5:
                Processor.tryResetDataRat(phone.getServiceStateTracker(), this.mDeviceStateMonitor);
            case 6:
                NetworkRegistrationInfo networkRegState = (NetworkRegistrationInfo) ((AsyncResult) msg.obj).result;
                if (networkRegState != null) {
                    if ((this.mIsRadioResetting || this.mIsModemDataRestoring) && networkRegState.isInService()) {
                        onModemResetComplete(phone);
                    }
                    int curRat = networkRegState.getAccessNetworkTechnology();
                    if (networkRegState.getDomain() == 1) {
                        if (curRat != this.mPreviousVoiceRAT) {
                            this.mPreviousVoiceRAT = curRat;
                            MiuiImsControllerImpl imsImpl = (MiuiImsControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiImsControllerImpl.class, this.mPhone.getPhoneId());
                            if (imsImpl != null) {
                                imsImpl.sendIMSRegistedStateBroadcast(this.mPhone.isVolteEnabled(), this.mPhone);
                            }
                        }
                    } else if (networkRegState.getDomain() == 2 && curRat != this.mPreviousDataRAT) {
                        DataNetworkController dnc = this.mPhone.getDataNetworkController();
                        if (this.mPreviousDataRAT == 0 && curRat != 0 && networkRegState.isInService()) {
                            dnc.sendEmptyMessage(MiuiDataControllerImpl.EVENT_DATA_NETWORK_ATTACHED);
                        } else if (this.mPreviousDataRAT != 0 && curRat == 0) {
                            dnc.sendEmptyMessage(MiuiDataControllerImpl.EVENT_DATA_NETWORK_DETACHED);
                        }
                        this.mPreviousDataRAT = curRat;
                        MiuiImsControllerImpl imsImpl2 = (MiuiImsControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiImsControllerImpl.class, this.mPhone.getPhoneId());
                        if (imsImpl2 != null) {
                            imsImpl2.sendIMSRegistedStateBroadcast(this.mPhone.isVolteEnabled(), this.mPhone);
                        }
                    }
                    break;
                }
            case 7:
            case 14:
                AsyncResult ar = (AsyncResult) msg.obj;
                if (ar != null && ar.userObj == phone.getServiceStateTracker().mPollingContext) {
                    updateRoamingState(phone);
                }
                break;
            case EVENT_CARRIER_CONFIG_CHANGED /* 209 */:
                this.mCarrierConfigLoaded = true;
                break;
        }
    }

    public boolean isValidNitz(int slotIndex, NitzSignal nitzSignal) {
        if (nitzSignal == null) {
            this.mAnchorNitzSignal = null;
            log("isValidNitz:nitzSignal is null");
            return true;
        }
        if (!InternalTelephonyUtils.isSpecialBuild() && this.mIsChinaTimeZone && InternalTelephonyUtils.isChinaSim(slotIndex)) {
            if (this.mAnchorNitzSignal != null) {
                return processNitz(nitzSignal.getNitzData());
            }
            if (isSameWithNtpCachedTime(nitzSignal)) {
                this.mAnchorNitzSignal = nitzSignal.getNitzData();
                log("isValidNitz:set anchor nitz by ntp");
                return true;
            }
            return true;
        }
        return true;
    }

    public void storeAndCheckTimeZone(int slotIndex, TelephonyTimeZoneSuggestion timeZone, NitzSignal nitzSignal) {
        if (nitzSignal == null) {
            this.mSameTimeZone = false;
            this.mIsChinaTimeZone = false;
            this.mLatestTimeZone = null;
        } else {
            this.mIsChinaTimeZone = CHINA_TIME_ZONE.equals(timeZone.getZoneId());
            if (this.mLatestTimeZone != null && this.mLatestTimeZone.getZoneId() == timeZone.getZoneId() && this.mIsChinaTimeZone) {
                this.mSameTimeZone = true;
            }
            this.mLatestTimeZone = timeZone;
        }
        log("storeAndCheckTimeZone mSameTimeZone=" + this.mSameTimeZone + ",mIsChinaTimeZone=" + this.mIsChinaTimeZone);
    }

    private boolean offsetInfoIsTheSame(NitzData one, NitzData two) {
        return Objects.equals(two.getDstAdjustmentMillis(), one.getDstAdjustmentMillis()) && Objects.equals(two.getEmulatorHostTimeZone(), one.getEmulatorHostTimeZone()) && two.getLocalOffsetMillis() == one.getLocalOffsetMillis();
    }

    private int checkNitzWithAnchor(NitzData newNitzData, NitzData previousNitzData) {
        if (newNitzData == null || previousNitzData == null) {
            log("checkNitzWithAnchor Signal is null!");
            return -1;
        }
        if (!offsetInfoIsTheSame(previousNitzData, newNitzData)) {
            log("checkNitzWithAnchor offsetInfoIsTheSame is false");
            return -2;
        }
        long elapsedRealtimeSinceLastSaved = newNitzData.getLocalOffsetMillis() - previousNitzData.getLocalOffsetMillis();
        long utcTimeDifferenceMillis = newNitzData.getCurrentTimeInMillis() - previousNitzData.getCurrentTimeInMillis();
        long millisGainedOrLost = Math.abs(utcTimeDifferenceMillis - elapsedRealtimeSinceLastSaved);
        log("checkNitzWithAnchor: utcTimeDifferenceMillis=" + utcTimeDifferenceMillis + " previousNitzData=" + previousNitzData + ", newNitzData=" + newNitzData + ", elapsedRealtimeSinceLastSaved=" + elapsedRealtimeSinceLastSaved + ", millisGainedOrLost=" + millisGainedOrLost);
        if (millisGainedOrLost > NITZ_UPDATE_DIFF_TIME) {
            return EXCEPTION_TIME_VALUE;
        }
        return 1;
    }

    private boolean processNitz(NitzData nitzSignal) {
        int rst = checkNitzWithAnchor(nitzSignal, this.mAnchorNitzSignal);
        switch (rst) {
            case EXCEPTION_TIME_VALUE /* -3 */:
                log("ignore current exception nitz!");
                break;
            case -2:
                this.mAnchorNitzSignal = null;
                break;
            case -1:
            case 0:
            default:
                log("processNitz:unknown!");
                break;
            case 1:
                this.mAnchorNitzSignal = nitzSignal;
                break;
        }
        return rst != EXCEPTION_TIME_VALUE;
    }

    private boolean isSameWithNtpCachedTime(NitzSignal nitzSignal) {
        NitzData newNitzTimeValue = nitzSignal.getNitzData();
        long elapsedRealtimeMillis = 0;
        long timeMillis = 0;
        NtpTrustedTime time = NtpTrustedTime.getInstance(this.mPhone.getContext());
        if (time.hasCache()) {
            elapsedRealtimeMillis = time.getCachedNtpTimeReference();
            timeMillis = time.getCachedNtpTime();
        } else {
            String propVal = SystemProperties.get("net.ntp.time");
            log("propVal=" + propVal);
            if (!TextUtils.isEmpty(propVal)) {
                String[] values = propVal.split(",");
                if (values.length == 2) {
                    elapsedRealtimeMillis = Long.parseLong(values[0]);
                    timeMillis = Long.parseLong(values[1]);
                }
            }
        }
        log("isSameWithNtpCachedTime reftime=" + elapsedRealtimeMillis + ",time=" + timeMillis);
        if (elapsedRealtimeMillis != 0 && timeMillis != 0) {
            long curElapsedRealtimeMillis = SystemClock.elapsedRealtime();
            long newNtpTime = (curElapsedRealtimeMillis - elapsedRealtimeMillis) + timeMillis;
            long newNitzTime = newNitzTimeValue.getCurrentTimeInMillis();
            long diffTime = Math.abs(newNtpTime - newNitzTime);
            log("isSameWithNtpCachedTime diffTime=" + diffTime + ",ntp=" + newNtpTime + ",nitz=" + newNitzTime);
            return diffTime < OFFSET_TIME;
        }
        return false;
    }

    public String getSelectNetworkManuallyOperatorNumeric(Phone phone, OperatorInfo network) {
        if (TextUtils.isEmpty(network.getRadioTech())) {
            return network.getOperatorNumeric();
        }
        return network.getOperatorNumeric() + "+" + network.getRadioTech();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onModemResetComplete(Phone phone) {
        DataNetwork dataNetwork;
        this.mIsRadioResetting = false;
        updateModemResetFlag(false);
        this.mIsModemDataRestoring = false;
        Processor.setDelayUpdateReason(phone.getPhoneId(), null);
        if (phone.getServiceStateTracker().getCombinedRegState(phone.getServiceState()) == 0) {
            phone.getServiceStateTracker().updateCarrierDisplayName();
            if (phone.getSignalStrengthController() != null) {
                phone.getSignalStrengthController().notifySignalStrength();
            }
            phone.notifyServiceStateChangedP(phone.getServiceState());
            if (phone.getDisplayInfoController() != null) {
                phone.getDisplayInfoController().updateTelephonyDisplayInfo();
            }
            MiuiDataControllerImpl miuiDataController = (MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, phone.getPhoneId());
            if (miuiDataController != null && (dataNetwork = miuiDataController.findActiveInternetDataNetwork()) != null && phone.getServiceState() != null) {
                phone.notifyDataConnection(new PreciseDataConnectionState.Builder().setTransportType(dataNetwork.getTransport()).setState(-1).setApnSetting(dataNetwork.getDataProfile().getApnSetting()).setNetworkType(phone.getServiceState().getDataNetworkType()).setLinkProperties(dataNetwork.getLinkProperties()).build());
            }
        }
    }

    private void registerSsDisplayOptimizationEvent() {
        updateUserFiveGEnabledValue();
        this.mPhone.getContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor("fiveg_user_enable"), false, this.mSsDisplayOptimization);
        this.mPhone.getContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor(DUAL_NR_ENABLED_SETTING), false, this.mSsDisplayOptimization);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUserFiveGEnabledValue() {
        boolean z = true;
        if (Settings.Global.getInt(this.mPhone.getContext().getContentResolver(), "fiveg_user_enable", 1) != 1 && Settings.Global.getInt(this.mPhone.getContext().getContentResolver(), DUAL_NR_ENABLED_SETTING, 1) != 1) {
            z = false;
        }
        this.mIsUserFiveGEnabled = z;
    }

    public void optimizateServiceStateDisplay(String reason) {
        if (hideNrForServiceState(this.mPhone.getServiceState())) {
            this.mPhone.notifyServiceStateChangedP(this.mPhone.getServiceState());
        }
        ServiceStateTracker sst = this.mPhone.getServiceStateTracker();
        ServiceState ss = sst.getServiceState();
        if (ss != null && sst.getCombinedRegState(ss) == 0) {
            log("No need do SS display optimization due to in service");
            return;
        }
        this.mIsNetworkModeChanging = true;
        log("SS display optimization, " + reason + ", 5G switch: " + this.mIsUserFiveGEnabled + ", data subId: " + android.telephony.SubscriptionManager.getDefaultDataSubscriptionId());
        this.mHandler.removeMessages(2000);
        this.mHandler.sendEmptyMessageDelayed(2000, 14000L);
    }

    public void resetServiceStateDisplay(Phone phone) {
        if (phone == null) {
            return;
        }
        log("resetServiceStateDisplay mIsNetworkModeChanging = " + this.mIsNetworkModeChanging);
        if (this.mIsNetworkModeChanging) {
            if (this.mHandler.hasMessages(2000)) {
                this.mIsNetworkModeChanging = false;
                this.mHandler.removeMessages(2000);
            }
            if (this.mPhone != null && this.mPhone.getServiceStateTracker() != null) {
                this.mPhone.notifyServiceStateChangedP(this.mPhone.getServiceState());
                this.mPhone.getServiceStateTracker().pollState();
                if (MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING) {
                    this.mPhone.getServiceStateTracker().updateCarrierDisplayName();
                }
            }
        }
    }

    private boolean hideNrForServiceState(ServiceState ss) {
        NetworkRegistrationInfo regInfo;
        if ((this.mIsUserFiveGEnabled && (this.mPhone.getSubId() == android.telephony.SubscriptionManager.getDefaultDataSubscriptionId() || TelephonyManager.getDefault().isDualNrSupported())) || (regInfo = ss.getNetworkRegistrationInfo(2, 1)) == null || regInfo.getNrState() == 0) {
            return false;
        }
        log("SS display optimization, hide nr for ServiceState, NrState is " + regInfo.getNrState());
        regInfo.setNrState(0);
        ss.addNetworkRegistrationInfo(regInfo);
        log("SS display optimization, hide nr for ServiceState: " + ss);
        return true;
    }

    private void updateRoamingState(Phone phone) {
        if (phone.getServiceStateTracker().mPollingContext[0] == 0 && this.mPhone.getPhoneType() == 1 && "26006".equals(this.mPhone.getOperatorNumeric()) && !phone.getServiceState().getRoaming()) {
            if ((phone.getServiceState().getVoiceRoaming() || phone.getServiceState().getDataRoaming()) && !this.mCarrierConfigLoaded) {
                log("updateRoamingState carrier config loaded " + this.mCarrierConfigLoaded);
                phone.getServiceState().setRoaming(true);
            }
        }
    }

    public CarrierDisplayNameData convertCarrierDisplayName(Phone phone, CarrierDisplayNameData data) {
        boolean needConvert = false;
        String spn = data.getSpn();
        String dataSpn = data.getDataSpn();
        String plmn = data.getPlmn();
        boolean showSpn = data.shouldShowSpn();
        boolean showPlmn = data.shouldShowPlmn();
        boolean miuiShowSpn = showSpn;
        boolean miuiShowPlmn = showPlmn;
        String miuiPlmn = plmn;
        if (Processor.isOnlyShowSpn(phone.getServiceStateTracker()) && !TextUtils.isEmpty(spn)) {
            miuiShowPlmn = false;
            miuiShowSpn = true;
        } else if (Processor.isOnlyShowPlmn(phone.getServiceStateTracker()) && !TextUtils.isEmpty(plmn)) {
            miuiShowPlmn = true;
            miuiShowSpn = false;
        } else if (showPlmn && showSpn && Processor.isShowSpnConfigWithoutSimSpn(phone.getServiceStateTracker())) {
            miuiShowPlmn = false;
            miuiShowSpn = true;
        }
        if (Processor.isShowPlmnLimitedService(this.mPhone.getServiceStateTracker())) {
            miuiShowPlmn = true;
            miuiShowSpn = false;
            miuiPlmn = Processor.getLimitedServicePlmn();
            log("convertCarrierDisplayName, custom display limited service");
        }
        if (Processor.isBothPlmnAndSpnShow(phone.getServiceStateTracker())) {
            miuiShowPlmn = true;
            miuiShowSpn = true;
            log("convertCarrierDisplayName, custom display plmn and spn");
        }
        if (Processor.isCnSharedPlmn(phone.getServiceState().getOperatorNumeric())) {
            String cnSharePlmn = Processor.getCnSharePlmn(phone.getServiceStateTracker());
            if (!TextUtils.isEmpty(cnSharePlmn)) {
                miuiPlmn = cnSharePlmn;
                miuiShowPlmn = true;
                miuiShowSpn = false;
            }
        }
        if (!TextUtils.isEmpty(miuiPlmn) && !miuiPlmn.equals(plmn)) {
            plmn = miuiPlmn;
            needConvert = true;
        }
        if (showSpn != miuiShowSpn) {
            showSpn = miuiShowSpn;
            needConvert = true;
        }
        if (showPlmn != miuiShowPlmn) {
            showPlmn = miuiShowPlmn;
            needConvert = true;
        }
        if (this.mPhone.getServiceState().getVoiceRegState() != 0 && this.mPhone.getServiceState().getRilDataRadioTechnology() == 18 && !this.mPhone.isWifiCallingEnabled()) {
            spn = null;
            showSpn = false;
            needConvert = true;
        }
        if (!needConvert) {
            return data;
        }
        log("spn = " + spn + " plmn = " + plmn + " dataSpn = " + dataSpn + " showSpn = " + showSpn + " showPlmn = " + showPlmn);
        return new CarrierDisplayNameData.Builder().setSpn(spn).setDataSpn(dataSpn).setShowSpn(showSpn).setPlmn(plmn).setShowPlmn(showPlmn).build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String s) {
        Rlog.d("MiuiNetworkControllerImpl[" + this.mPhone.getPhoneId() + "]", s);
    }

    private void loge(String s) {
        Rlog.e("MiuiNetworkControllerImpl[" + this.mPhone.getPhoneId() + "]", s);
    }

    public boolean isDelayNotifySignalStrength(Phone phone) {
        boolean notified;
        if (InternalTelephonyUtils.isChinaTestBuild() || this.mIsNotifySignalStrengthImmediately) {
            notified = true;
        } else if (isNtnPhone(phone)) {
            checkDelayMiuiLevelIfNeeded(phone);
            notified = true;
        } else {
            if (this.mHandler.hasMessages(EVENT_DELAY_NTNPHONE_SIGNAL_UPDATE)) {
                this.mHandler.removeMessages(EVENT_DELAY_NTNPHONE_SIGNAL_UPDATE);
            }
            if (this.mPhone != null && shouldDelayForPoorSignalStrength(this.mPhone.getServiceStateTracker())) {
                int currentShowLevel = this.mCurrentShowSignalStrength.getMiuiLevel();
                this.mCurrentShowSignalStrength = new SignalStrength(this.mPhone.getSignalStrengthController().getSignalStrength());
                this.mTargetLevel = this.mCurrentShowSignalStrength.getMiuiLevel();
                log("set targetLevels = " + this.mTargetLevel);
                this.mCurrentShowSignalStrength.setOptimizedLevel(currentShowLevel);
                if (!this.mHandler.hasMessages(EVENT_DELAY_SIGNAL_UPDATE) && this.mHandler.sendEmptyMessageDelayed(EVENT_DELAY_SIGNAL_UPDATE, 10000L)) {
                    log("send EVENT_DELAY_SIGNAL_UPDATE msg success");
                }
                notified = false;
            } else {
                if (this.mHandler.hasMessages(EVENT_DELAY_SIGNAL_UPDATE)) {
                    this.mHandler.removeMessages(EVENT_DELAY_SIGNAL_UPDATE);
                    log("removed EVENT_DELAY_SIGNAL_UPDATE msg success");
                }
                notified = true;
                this.mTargetLevel = -1;
            }
            if (!this.mIsScreenOn && this.mIsScreenOffSignalStrength != null && this.mPhone.getSignalStrengthController() != null && this.mPhone.getSignalStrengthController().getSignalStrength() != null && this.mIsScreenOffSignalStrength.getMiuiLevel() > this.mPhone.getSignalStrengthController().getSignalStrength().getMiuiLevel()) {
                this.mHandler.removeMessages(EVENT_DELAY_SIGNAL_UPDATE);
                log("When screen off, signal becomes worse not update");
                notified = false;
            }
            if (this.mHandler.hasMessages(EVENT_SCREEN_ON_DELAY_SIGNAL_TIMEOUT) || this.mHandler.hasMessages(EVENT_MIDDLE_SIGNAL_DISPLAY_TIMEOUT)) {
                this.mHandler.removeMessages(EVENT_DELAY_SIGNAL_UPDATE);
                log("When screen on,delay update the signal");
                notified = false;
            }
            if (this.mPhone != null && Processor.getShouldDelayForSmoothOOS(this.mPhone.getPhoneId())) {
                notified = false;
            }
        }
        if (notified) {
            this.mIsNotifySignalStrengthImmediately = false;
            if (isNtnPhone(phone) && !this.mHandler.hasMessages(EVENT_DELAY_NTNPHONE_SIGNAL_UPDATE)) {
                this.mCurrentShowSignalStrength = phone.getSignalStrength();
            }
            if (!isNtnPhone(phone)) {
                this.mCurrentShowSignalStrength = this.mPhone.getSignalStrengthController().getSignalStrength();
            }
            this.mCurrentShowServiceState = new ServiceState(this.mPhone.getServiceState());
            if (!isNtnPhone(phone) && MiuiCallControllerImpl.Processor.supportVtQualityProperty() && !MiuiCallControllerImpl.Processor.disabledSetVtQualityPropByCloud()) {
                MiuiCallControllerImpl.Processor.setVtQualityProperty(this.mPhone);
            }
        }
        log("isDelayNotifySignalStrength: " + (!notified));
        return !notified;
    }

    public boolean handleOOSSmooth() {
        if (this.mPhone == null) {
            return false;
        }
        boolean airplaneModeOn = Settings.System.getInt(this.mPhone.getContext().getContentResolver(), "airplane_mode_on", 0) == 1;
        if (!MiuiDisplayInfoController.isSupportOOSSmoothFeature() || airplaneModeOn) {
            return false;
        }
        boolean zSmoothSignalStrengthForOOSIfNeed = smoothSignalStrengthForOOSIfNeed();
        Handler handler = this.mHandler;
        if (zSmoothSignalStrengthForOOSIfNeed) {
            if (handler.hasMessages(EVENT_DELAY_SIGNAL_UPDATE)) {
                this.mHandler.removeMessages(EVENT_DELAY_SIGNAL_UPDATE);
                log("already oos now, Smooth signal will work insdead of  poor signal optimization...");
            }
            if (this.mUnitTimeDelay == 0) {
                int currentShowLevel = this.mCurrentShowSignalStrength.getOptimizedLevel();
                this.mPreMiuiLevel = this.mCurrentShowSignalStrength.getMiuiLevel();
                this.mUnitTimeDelay = DELAY_OOS_SMOOTH_SIGNAL_UPDATE_TIMER / Math.max(currentShowLevel, 1);
                log("no service fix signal..., mUnitTimeDelay = " + this.mUnitTimeDelay + ",mPreMiuiLevel=" + this.mPreMiuiLevel);
                TelephonyStatAdapter.notifyServiceState(this.mPhone.getPhoneId(), this.mPhone.getServiceState());
            }
            int currentShowLevel2 = this.mUnitTimeDelay;
            if (currentShowLevel2 > 0 && !this.mHandler.hasMessages(EVENT_SMOOTH_SIGNAL_UPDATE)) {
                this.mHandler.sendEmptyMessageDelayed(EVENT_SMOOTH_SIGNAL_UPDATE, this.mUnitTimeDelay);
                log("no service fix signal..., phoneId = " + this.mPhone.getPhoneId());
            }
            return true;
        }
        if (handler.hasMessages(EVENT_SMOOTH_SIGNAL_UPDATE)) {
            this.mHandler.removeMessages(EVENT_SMOOTH_SIGNAL_UPDATE);
            log("removed EVENT_SMOOTH_SIGNAL_UPDATE due to no need fix signal, phoneId = " + this.mPhone.getPhoneId());
        }
        this.mUnitTimeDelay = 0;
        this.mPreMiuiLevel = 0;
        log("no Need fix signal..., phoneId = " + this.mPhone.getPhoneId());
        return false;
    }

    public void cancelOOSSmooth() {
        if (this.mPhone == null) {
            return;
        }
        Processor.setDelayForSmoothOOS(this.mPhone.getPhoneId(), false);
        this.mHandler.removeMessages(EVENT_SMOOTH_SIGNAL_UPDATE);
        this.mUnitTimeDelay = 0;
        this.mPreMiuiLevel = 0;
        log("cancelOOSSmooth: full cleanup, phoneId=" + this.mPhone.getPhoneId());
    }

    private boolean smoothSignalStrengthForOOSIfNeed() {
        if (this.mPhone == null) {
            return false;
        }
        int simState = android.telephony.TelephonyManager.getSimStateForSlotIndex(this.mPhone.getPhoneId());
        if (!MiuiDisplayInfoController.isAllPhonesInIdle() || simState != 10) {
            Processor.setDelayForSmoothOOS(this.mPhone.getPhoneId(), false);
            log("Exist call or Sim doesnt loaded yet, no need fix OOS. simState=" + simState);
            return false;
        }
        if (this.mPhone.getServiceStateTracker() != null && this.mPhone.getServiceStateTracker().getCombinedRegState(this.mPhone.getServiceState()) == 1 && this.mLastServiceState == 0 && this.mCurrentShowSignalStrength != null) {
            int miuiLevel = this.mCurrentShowSignalStrength.getMiuiLevel();
            SignalStrength signalStrength = this.mCurrentShowSignalStrength;
            int currentShowLevel = miuiLevel > 0 ? signalStrength.getMiuiLevel() : signalStrength.getOptimizedLevel();
            this.mCurrentShowSignalStrength.setOptimizedLevel(currentShowLevel);
            log("currentShowLevel = " + currentShowLevel);
            if (currentShowLevel == 0 && Processor.getShouldDelayForSmoothOOS(this.mPhone.getPhoneId())) {
                this.mTelephonyRegistryMgr.notifyServiceStateChanged(this.mPhone.getPhoneId(), this.mPhone.getSubId(), this.mPhone.getServiceState());
                TelephonyStatAdapter.notifyServiceState(this.mPhone.getPhoneId(), this.mPhone.getServiceState());
                this.mCurrentShowServiceState = new ServiceState(this.mPhone.getServiceState());
                this.mPhone.getServiceStateTracker().updateCarrierDisplayName();
                Processor.setDelayForSmoothOOS(this.mPhone.getPhoneId(), false);
                this.mCurrentShowSignalStrength.setMiuiLevel(this.mPreMiuiLevel);
                this.mCurrentShowSignalStrength.setOptimizedLevel(0);
                this.mTelephonyRegistryMgr.notifySignalStrengthChanged(this.mPhone.getPhoneId(), this.mPhone.getSubId(), this.mCurrentShowSignalStrength);
                return false;
            }
            Processor.setDelayForSmoothOOS(this.mPhone.getPhoneId(), true);
            return true;
        }
        if (Processor.getShouldDelayForSmoothOOS(this.mPhone.getPhoneId())) {
            log("Stop OOS smooth due to condition changed, phoneId = " + this.mPhone.getPhoneId());
            Processor.setDelayForSmoothOOS(this.mPhone.getPhoneId(), false);
            if (this.mCurrentShowSignalStrength != null) {
                this.mCurrentShowSignalStrength.setMiuiLevel(this.mPreMiuiLevel);
                this.mCurrentShowSignalStrength.setOptimizedLevel(0);
                this.mTelephonyRegistryMgr.notifySignalStrengthChanged(this.mPhone.getPhoneId(), this.mPhone.getSubId(), this.mCurrentShowSignalStrength);
            }
        }
        return false;
    }

    private boolean needDelayByLessLevel(SignalStrength newSignal, SignalStrength currentShowSignal) {
        if (newSignal.getMiuiLevel() < currentShowSignal.getMiuiLevel()) {
            log("needDelayByLessLevel: miuiLevel is worse, start delay");
            return true;
        }
        return false;
    }

    private boolean needDelayByCsPs(SignalStrength newSignal, SignalStrength currentShowSignal, ServiceState nowServiceState) {
        boolean needDelay = false;
        int currentShowPsLevel = getPSLevel(currentShowSignal, nowServiceState);
        int newPsLevel = getPSLevel(newSignal, nowServiceState);
        int currentShowCsLevel = getCSLevel(currentShowSignal, nowServiceState);
        int newCsLevel = getCSLevel(newSignal, nowServiceState);
        if ((newPsLevel < currentShowPsLevel && newCsLevel <= currentShowCsLevel) || (newCsLevel < currentShowCsLevel && newPsLevel <= currentShowPsLevel)) {
            needDelay = true;
            log("SignalStrength is worse,start delay");
        }
        if (!needDelay && newSignal.getMiuiLevel() < currentShowSignal.getMiuiLevel()) {
            log("SignalStrength miuiLevel is worse,also start delay");
            return true;
        }
        return needDelay;
    }

    private int getPSLevel(SignalStrength signal, ServiceState state) {
        return getNetworkTypeSignalLevel(signal, state.getDataNetworkType());
    }

    private int getCSLevel(SignalStrength signal, ServiceState state) {
        return getNetworkTypeSignalLevel(signal, state.getVoiceNetworkType());
    }

    private int getNetworkTypeSignalLevel(SignalStrength signal, int networkType) {
        switch (networkType) {
            case 0:
            case 18:
            default:
                return -1;
            case 1:
            case 2:
            case 11:
            case 16:
                return signal.getGsmLevel();
            case 3:
            case 8:
            case 9:
            case 10:
            case 15:
            case 17:
                int level = signal.getTdScdmaLevel();
                if (level <= 0) {
                    int level2 = signal.getWcdmaLevel();
                    if (level2 <= 0) {
                        return signal.getGsmLevel();
                    }
                    return level2;
                }
                return level;
            case 4:
            case 7:
                return signal.getCdmaLevel();
            case 5:
            case 6:
            case 12:
            case 14:
                return signal.getEvdoLevel();
            case 13:
            case 19:
                return signal.getLteLevel();
            case 20:
                return signal.getLevel();
        }
    }

    public boolean shouldDelayForPoorSignalStrength(ServiceStateTracker sst) {
        boolean isSignalPoor = false;
        if (sst == null || this.mPhone == null || this.mPhone.getSignalStrengthController() == null) {
            return false;
        }
        removeScreenSignalStrengthTimer(sst);
        removeNeighborSignalStrengthTimer(sst);
        int simState = android.telephony.TelephonyManager.getSimStateForSlotIndex(this.mPhone.getPhoneId());
        if (!MiuiDisplayInfoController.isAllPhonesInIdle() || simState != 10) {
            log("Exist call or Sim doesnt loaded yet, do not need fix signal, simState=" + simState);
            return false;
        }
        ServiceState newSS = new ServiceState(this.mPhone.getServiceState());
        SignalStrength newSignalStrength = this.mPhone.getSignalStrengthController().getSignalStrength();
        boolean supportDelay = HyperOSCustFeatureResolve.getBoolean("support_smooth_poor_signal_strength", true);
        boolean inService = sst.getCombinedRegState(newSS) == 0;
        boolean serviceChanged = sst.getCombinedRegState(newSS) != this.mLastServiceState;
        int level = newSignalStrength.getMiuiLevel();
        boolean needDelayByCallState = this.mPhone.getCallTracker().getState() == PhoneConstants.State.IDLE;
        if (this.mCurrentShowSignalStrength != null && supportDelay && inService && !serviceChanged && needDelayByCallState && needDelayByLessLevel(newSignalStrength, this.mCurrentShowSignalStrength)) {
            isSignalPoor = true;
        }
        log("isSignalPoor: " + isSignalPoor + ", supportDelay: " + supportDelay + ", inService: " + inService + ", serviceChanged: " + serviceChanged + ", level: " + level + ", needDelayByCallState: " + needDelayByCallState);
        return isSignalPoor;
    }

    public void removeNeighborSignalStrengthTimer(ServiceStateTracker sst) {
        if (!this.mSupportNeighboringSignal || !this.mHandler.hasMessages(EVENT_REQUEST_CELL_INFO)) {
            return;
        }
        boolean isCardAbsent = isCardAbsentOrNoService();
        if (isCardAbsent || isSignalStrengthBecomeBetter()) {
            if (!isCardAbsent) {
                trackOptimizationSs(ACTION_SS_OPTIMIZATION_SCENE_STATISTIC, "Neighbor normal");
            }
            this.mHandler.removeMessages(EVENT_REQUEST_CELL_INFO);
        }
    }

    public void removeScreenSignalStrengthTimer(ServiceStateTracker sst) {
        if (this.mSupportScreenSignal) {
            if (!this.mHandler.hasMessages(EVENT_SCREEN_ON_DELAY_SIGNAL_TIMEOUT) && !this.mHandler.hasMessages(EVENT_MIDDLE_SIGNAL_DISPLAY_TIMEOUT)) {
                return;
            }
            boolean isCardAbsent = isCardAbsentOrNoService();
            if (isCardAbsent || (this.mIsScreenOn && isSignalStrengthBecomeBetter())) {
                if (!isCardAbsent) {
                    trackOptimizationSs(ACTION_SS_OPTIMIZATION_SCENE_STATISTIC, "screen normal");
                    this.mLockedScreenSignalStrengthOptimizationCount++;
                }
                this.mHandler.removeMessages(EVENT_SCREEN_ON_DELAY_SIGNAL_TIMEOUT);
                this.mHandler.removeMessages(EVENT_MIDDLE_SIGNAL_DISPLAY_TIMEOUT);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCardAbsentOrNoService() {
        return (android.telephony.SubscriptionManager.isValidSubscriptionId(this.mPhone.getSubId()) && this.mPhone.getServiceStateTracker().getCombinedRegState(this.mPhone.getServiceState()) == 0) ? false : true;
    }

    private boolean isSignalStrengthBecomeBetter() {
        if (this.mCurrentShowSignalStrength != null && this.mPhone.getSignalStrengthController() != null && this.mPhone.getSignalStrengthController().getSignalStrength() != null && this.mCurrentShowSignalStrength.getMiuiLevel() >= 0 && this.mCurrentShowSignalStrength.getMiuiLevel() <= this.mPhone.getSignalStrengthController().getSignalStrength().getMiuiLevel()) {
            return true;
        }
        return false;
    }

    public boolean isScreenOn() {
        DisplayManager dm = (DisplayManager) this.mPhone.getContext().getSystemService("display");
        Display[] displays = dm.getDisplays();
        if (displays != null) {
            for (Display display : displays) {
                if (display.getState() == 2) {
                    if (PhoneDebug.VDBG) {
                        log("Screen on for display=" + display);
                        return true;
                    }
                    return true;
                }
            }
            log("Screens all off");
            return false;
        }
        log("No displays found");
        return false;
    }

    public void notifyLocationChanged(Phone phone, CellIdentity cellIdentity) {
        if (!TelephonyOptCloudController.CLOUD_CONTROLLER_NETWORK_NEIGHBOR_DISABLED && this.mSupportNeighboringSignal) {
            if (getTacFromCellIdentity(cellIdentity) != -1) {
                this.mLastRegisteredTac = getTacFromCellIdentity(cellIdentity);
            }
            int pci = getPciFromCellIdentity(cellIdentity);
            int arfcn = getArfcnFromCellIdentity(cellIdentity);
            if (pci == -1 || arfcn == -1) {
                return;
            }
            CellInfoOptimized cellInfo = new CellInfoOptimized(pci, arfcn);
            if (this.mRegisteredCellInfo != null || this.mRegisteredCellInfo.size() > 0) {
                for (int i = 0; i < this.mRegisteredCellInfo.size(); i++) {
                    CellInfoOptimized cellInfoIndex = this.mRegisteredCellInfo.get(i);
                    if (cellInfoIndex.equals(cellInfo)) {
                        return;
                    }
                }
            }
            if (this.mRegisteredCellInfo.size() == 30) {
                this.mRegisteredCellInfo.remove(0);
            }
            this.mRegisteredCellInfo.add(cellInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processCellInfo(AsyncResult ar) {
        int registeredMiuiLevel = 0;
        int unRegisteredMiuiLevel = 0;
        int registeredTac = -1;
        CellInfoOptimized unRegisteredCellInfo = null;
        if (ar == null || ar.exception != null) {
            setOptimizedLevel(0);
            return;
        }
        List<CellInfo> cellInfoList = (List) ar.result;
        if (this.mRegisteredCellInfo == null || this.mRegisteredCellInfo.size() == 0 || cellInfoList == null || cellInfoList.size() < 2) {
            log("no Neighbor information");
            setOptimizedLevel(0);
            return;
        }
        for (CellInfo ci : cellInfoList) {
            int pci = getPciFromCellIdentity(ci.getCellIdentity());
            int arfcn = getArfcnFromCellIdentity(ci.getCellIdentity());
            if (pci != -1 && arfcn != -1) {
                ci.getCellSignalStrength().updateLevel(getCarrierConfig(), this.mPhone.getServiceStateTracker().mSS);
                int cellLevel = ci.getCellSignalStrength().getMiuiLevel();
                if (ci.isRegistered()) {
                    registeredMiuiLevel = cellLevel;
                    registeredTac = getTacFromCellIdentity(ci.getCellIdentity());
                } else if (registeredTac == this.mLastRegisteredTac && unRegisteredMiuiLevel <= cellLevel) {
                    unRegisteredMiuiLevel = cellLevel;
                    unRegisteredCellInfo = new CellInfoOptimized(pci, arfcn);
                }
            }
        }
        if (unRegisteredMiuiLevel > registeredMiuiLevel) {
            trackOptimizationSs(ACTION_SS_OPTIMIZATION_SCENE_STATISTIC, "neighbor exist");
            for (int i = 0; i < this.mRegisteredCellInfo.size(); i++) {
                CellInfoOptimized cellInfoIndex = this.mRegisteredCellInfo.get(i);
                if (cellInfoIndex.equals(unRegisteredCellInfo)) {
                    SignalStrength realSignalStrength = this.mPhone.getSignalStrengthController().getSignalStrength();
                    if (unRegisteredMiuiLevel > realSignalStrength.getMiuiLevel() && this.mIsScreenOn) {
                        this.mNeighborSignalStrengthOptimizationCount++;
                    }
                    trackOptimizationSs(ACTION_SS_OPTIMIZATION_SCENE_STATISTIC, "neighbor display");
                    setOptimizedLevel(unRegisteredMiuiLevel);
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(EVENT_REQUEST_CELL_INFO), 10000L);
                    return;
                }
            }
        }
        setOptimizedLevel(0);
    }

    private PersistableBundle getCarrierConfig() {
        PersistableBundle config;
        CarrierConfigManager configManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService("carrier_config");
        if (configManager != null && (config = configManager.getConfigForSubId(this.mPhone.getSubId())) != null) {
            return config;
        }
        return CarrierConfigManager.getDefaultConfig();
    }

    private int getTacFromCellIdentity(CellIdentity id) {
        if (id == null) {
            return -1;
        }
        int tac = -1;
        switch (id.getType()) {
            case 3:
                tac = ((CellIdentityLte) id).getTac();
                break;
            case 6:
                tac = ((CellIdentityNr) id).getTac();
                break;
        }
        if (tac == Integer.MAX_VALUE) {
            return -1;
        }
        return tac;
    }

    private int getPciFromCellIdentity(CellIdentity id) {
        if (id == null) {
            return -1;
        }
        int pci = -1;
        switch (id.getType()) {
            case 3:
                pci = ((CellIdentityLte) id).getPci();
                break;
            case 6:
                pci = ((CellIdentityNr) id).getPci();
                break;
        }
        if (pci == Integer.MAX_VALUE) {
            return -1;
        }
        return pci;
    }

    private int getArfcnFromCellIdentity(CellIdentity id) {
        if (id == null) {
            return -1;
        }
        int arfcn = -1;
        switch (id.getType()) {
            case 3:
                arfcn = ((CellIdentityLte) id).getEarfcn();
                break;
            case 6:
                arfcn = ((CellIdentityNr) id).getNrarfcn();
                break;
        }
        if (arfcn == Integer.MAX_VALUE) {
            return -1;
        }
        return arfcn;
    }

    private static class CellInfoOptimized {
        private final int mArfcn;
        private final int mPci;

        public CellInfoOptimized(int pci, int arfcn) {
            this.mPci = pci;
            this.mArfcn = arfcn;
        }

        public boolean equals(CellInfoOptimized cellInfoOptimized) {
            return this.mPci == cellInfoOptimized.mPci && this.mArfcn == cellInfoOptimized.mArfcn;
        }

        public String toString() {
            return "CellInfoOptimized,Pci: " + this.mPci + ", mArfcn: " + this.mArfcn;
        }
    }

    public void setOptimizedLevel(int optimizedLevel) {
        SignalStrength realSignalStrength;
        log("setOptimizedLevel optimizedLevel:" + optimizedLevel);
        if (this.mPhone != null && (realSignalStrength = this.mPhone.getSignalStrengthController().getSignalStrength()) != null) {
            realSignalStrength.setOptimizedLevel(optimizedLevel);
            this.mIsNotifySignalStrengthImmediately = true;
            this.mPhone.getSignalStrengthController().notifySignalStrength();
            this.mIsNotifySignalStrengthImmediately = false;
        }
    }

    public void setNtnPhoneOptimizedLevel(Phone phone) {
        SignalStrength realSignalStrength = phone.getSignalStrength();
        if (realSignalStrength != null) {
            realSignalStrength.setMiuiLevel(this.mCurrentReportLevel);
            log("setNtnPhoneOptimizedLevel realSignalStrength : " + realSignalStrength);
            this.mCurrentShowSignalStrength = realSignalStrength;
            phone.notifySignalStrength();
        }
    }

    private void checkDelayMiuiLevelIfNeeded(Phone phone) {
        this.mCurrentReportLevel = phone.getSignalStrength().getMiuiLevel();
        int currentShowLevel = this.mCurrentShowSignalStrength == null ? 0 : this.mCurrentShowSignalStrength.getMiuiLevel();
        log("check if need update signal level delay, currentReportlevel = " + this.mCurrentReportLevel + ", currentShowLevel = " + currentShowLevel);
        int i = this.mCurrentReportLevel;
        Handler handler = this.mHandler;
        if (i < currentShowLevel) {
            if (!handler.hasMessages(EVENT_DELAY_NTNPHONE_SIGNAL_UPDATE)) {
                Message message = this.mHandler.obtainMessage(EVENT_DELAY_NTNPHONE_SIGNAL_UPDATE, phone);
                if (this.mHandler.sendMessageDelayed(message, 3000L)) {
                    log("send EVENT_DELAY_NTNPHONE_SIGNAL_UPDATE msg success, timer = 3000");
                    return;
                }
                return;
            }
            log("already have EVENT_DELAY_NTNPHONE_SIGNAL_UPDATE msg, ignore");
            return;
        }
        if (handler.hasMessages(EVENT_DELAY_NTNPHONE_SIGNAL_UPDATE)) {
            this.mHandler.removeMessages(EVENT_DELAY_NTNPHONE_SIGNAL_UPDATE);
        }
    }

    public boolean isNtnPhone(Phone phone) {
        if (PhoneDebug.VDBG) {
            log("phoneType = " + phone.getPhoneType());
        }
        return phone.getPhoneType() == 7;
    }

    public void handleOptimizedLevel() {
        if (this.mPhone != null && this.mCurrentShowSignalStrength != null) {
            int currentShowLevel = this.mCurrentShowSignalStrength.getOptimizedLevel();
            if (currentShowLevel > 0 && currentShowLevel > this.mTargetLevel && this.mTargetLevel != -1) {
                log("handleOptimizedLevel currentShowLevel = " + currentShowLevel + ", targetLevel = " + this.mTargetLevel);
                this.mCurrentShowSignalStrength.setOptimizedLevel(currentShowLevel - 1);
                log("handleOptimizedLevel notifySignalStrength = " + this.mCurrentShowSignalStrength);
                this.mTelephonyRegistryMgr.notifySignalStrengthChanged(this.mPhone.getPhoneId(), this.mPhone.getSubId(), this.mCurrentShowSignalStrength);
                TelephonyStatAdapter.notifySignalStrength(this.mPhone.getPhoneId(), new SignalStrength(this.mCurrentShowSignalStrength));
            }
            isDelayNotifySignalStrength(this.mPhone);
            return;
        }
        if (TelephonyOptCloudController.CLOUD_CONTROLLER_NETWORK_NEIGHBOR_DISABLED) {
            this.mSupportNeighboringSignal = false;
        }
        if (this.mSupportNeighboringSignal && this.mCurrentShowSignalStrength != null && this.mPhone.getSignalStrengthController().getSignalStrength() != null) {
            int lastLevel = this.mCurrentShowSignalStrength.getMiuiLevel();
            int currentLevel = this.mPhone.getSignalStrengthController().getSignalStrength().getMiuiLevel();
            if (lastLevel - currentLevel > 1) {
                this.mHandler.removeMessages(EVENT_REQUEST_CELL_INFO);
                this.mHandler.sendMessage(this.mHandler.obtainMessage(EVENT_REQUEST_CELL_INFO));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void trackOptimizationSs(String action, String scene) {
        Intent intent = new Intent(action);
        intent.putExtra(SS_OPTIMIZATION_EXTRA, scene);
        this.mPhone.getContext().sendBroadcast(intent);
        log("trackOptimizationSs: " + scene);
    }

    public boolean needCacheCellInfoList(Phone phone, CellIdentity cellIdentity, List<CellInfo> cellInfoList) {
        log("request cell info list without optimization: cts");
        return false;
    }

    private static /* synthetic */ boolean lambda$needCacheCellInfoList$0(CellInfo cellInfo) {
        return cellInfo.getCellIdentity() != null;
    }

    public boolean processCacheCellInfoList(Phone phone, Message rspMsg, long lastCellInfoReqTime, int cellInfoMinIntervalMs, MiStubParam param) {
        CellIdentity cellIdentity = this.mOwner.getCellIdentity();
        List<CellInfo> lastCellInfoList = this.mOwner.getAllCellInfo();
        boolean needOptimize = needCacheCellInfoList(phone, cellIdentity, lastCellInfoList);
        long curTime = SystemClock.elapsedRealtime();
        if (curTime - lastCellInfoReqTime < cellInfoMinIntervalMs || needOptimize) {
            if (rspMsg != null) {
                if (rspMsg.getData() != null) {
                    Bundle bundle = rspMsg.getData();
                    String state = needOptimize ? STATE_OPTIMIZATION : STATE_TIME_CHACHE;
                    bundle.putString(KEY_EXTRA_STATE, state);
                    rspMsg.setData(bundle);
                }
                log("SST.requestAllCellInfo(): return last, back to back calls");
                AsyncResult.forMessage(rspMsg, lastCellInfoList, (Throwable) null);
                rspMsg.sendToTarget();
                return true;
            }
            return true;
        }
        log("processCacheCellInfoList is empty function,return false");
        return false;
    }

    private void updateSignalStrengthOptimizationCountMap() {
        int phoneId = this.mPhone.getPhoneId();
        mSignalStrengthOptimizationCountMap.put(CommenUtils.KEY_COMMON_SIGNAL_OPTIMIZATION_COUNT + phoneId, String.valueOf(this.mCommonSignalStrengthOptimizationCount));
        mSignalStrengthOptimizationCountMap.put(CommenUtils.KEY_SCREEN_SIGNAL_OPTIMIZATION_COUNT + phoneId, String.valueOf(this.mLockedScreenSignalStrengthOptimizationCount));
        mSignalStrengthOptimizationCountMap.put(CommenUtils.KEY_NEIGHBOR_SIGNAL_OPTIMIZATION_COUNT + phoneId, String.valueOf(this.mNeighborSignalStrengthOptimizationCount));
        mSignalStrengthOptimizationCountMap.put(CommenUtils.KEY_DISPLAY_ICON_RECOVERY_COUNT + phoneId, String.valueOf(this.mDisplayIconRecoveryCount));
        mSignalStrengthOptimizationCountMap.put(CommenUtils.KEY_DISPLAY_ICON_NO_RECOVERY_COUNT + phoneId, String.valueOf(this.mDisplayIconNoRecoveryCount));
        mSignalStrengthOptimizationCountMap.put(CommenUtils.KEY_RIL_MESSAGE_TIME_OUT + phoneId, getRilMessageTimeOut());
    }

    public HashMap<String, String> getSignalStrengthOptimizationCountMap() {
        updateSignalStrengthOptimizationCountMap();
        this.mCommonSignalStrengthOptimizationCount = 0L;
        this.mLockedScreenSignalStrengthOptimizationCount = 0L;
        this.mNeighborSignalStrengthOptimizationCount = 0L;
        this.mDisplayIconRecoveryCount = 0L;
        this.mDisplayIconNoRecoveryCount = 0L;
        return mSignalStrengthOptimizationCountMap;
    }

    public boolean needGUTIWork(Context context, ServiceStateTracker sst) {
        boolean needGUIIWork = false;
        if (GUTIController.needWork(context, this.mPhone.getPhoneId())) {
            needGUIIWork = true;
            if (mGUTIController == null) {
                mGUTIController = GUTIController.getInstance(context);
            }
            mGUTIController.initQcGUTIHandler(this.mPhone.getPhoneId(), this.mPhone.mCi);
        }
        Rlog.d(TAG, "needGUTIWork =" + needGUIIWork);
        return needGUIIWork;
    }

    public void startGUTITask(ServiceStateTracker sst) {
        if (mGUTIController != null) {
            mGUTIController.start(sst.getPhoneId());
        }
    }

    public boolean isDelayUpdateTelephonyDisplayInfo(DisplayInfoController displayInfoController, int overrideNetworkType) {
        int dataNetworkType;
        NetworkRegistrationInfo nri = this.mPhone.getServiceState().getNetworkRegistrationInfo(2, 1);
        if (nri == null) {
            dataNetworkType = 0;
        } else {
            dataNetworkType = nri.getAccessNetworkTechnology();
        }
        TelephonyDisplayInfo newDisplayInfo = new TelephonyDisplayInfo(dataNetworkType, overrideNetworkType);
        this.mNewDisplayNetworkType = CommenUtils.getExpectedDisplayIcon(newDisplayInfo, this.mPhone);
        Rlog.d(TAG, "isDelayUpdateTelephonyDisplayInfo, mNewDisplayNetworkType = " + this.mNewDisplayNetworkType + ", mCurrentDisplayNetworkType = " + this.mCurrentDisplayNetworkType + ", phoneId = " + this.mPhone.getPhoneId());
        if (dataNetworkType != 13 && dataNetworkType != 20) {
            if (this.mHandler.hasMessages(3)) {
                this.mHandler.removeMessages(3);
                this.mDisplayIconRecoveryCount++;
            }
            this.mCurrentDisplayNetworkType = this.mNewDisplayNetworkType;
            Rlog.d(TAG, "isDelayUpdateTelephonyDisplayInfo, No longer registered on PS, phoneId = " + this.mPhone.getPhoneId());
            return false;
        }
        if (!this.mHandler.hasMessages(3) && !CommenUtils.isDisplayIconMoreStable(this.mNewDisplayNetworkType, this.mCurrentDisplayNetworkType)) {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(3), 5000L);
            return false;
        }
        boolean zHasMessages = this.mHandler.hasMessages(3);
        int i = this.mNewDisplayNetworkType;
        if (zHasMessages) {
            if (!CommenUtils.isDisplayIconMoreStable(i, this.mCurrentDisplayNetworkType)) {
                return false;
            }
            this.mHandler.removeMessages(3);
            this.mCurrentDisplayNetworkType = this.mNewDisplayNetworkType;
            this.mDisplayIconRecoveryCount++;
            return false;
        }
        this.mCurrentDisplayNetworkType = i;
        return false;
    }

    public void doRilMessageTimeOut(SparseArray<RILRequest> requestList) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(4, requestList));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doRilMessageTimeOut(Message msg) {
        if ((miui.os.Build.IS_STABLE_VERSION && !this.mSupportOosResetRild) || TelephonyOptCloudController.CLOUD_CONTROLLER_OOS_RESET_RILD_DISABLED) {
            Rlog.d(TAG, "doRilMessageTimeOut not support, mSupportOosResetRild = " + this.mSupportOosResetRild);
            return;
        }
        this.mRilMessageTimeOutCount++;
        SparseArray<RILRequest> requestList = (SparseArray) msg.obj;
        if (this.mPhone.getServiceStateTracker().getCombinedRegState(this.mPhone.getServiceState()) == 1) {
            if (!this.mIsRecordedOos) {
                this.mOosRilMessageTimeOutCount++;
                this.mIsRecordedOos = true;
            }
            if (!isDuringCall() && isNwRegReqBlocking(requestList)) {
                resetRildOos();
            } else {
                Rlog.d(TAG, "No network registration requests are blocking");
                return;
            }
        }
        if (System.currentTimeMillis() - this.mBroadcastRilWakeLockRequestTime < 21600000) {
            Rlog.d(TAG, "It has been since the last broadcast: " + (System.currentTimeMillis() - this.mBroadcastRilWakeLockRequestTime));
            return;
        }
        int requestCode = -1;
        if (requestList.size() > 0) {
            requestCode = requestList.valueAt(0).mRequest;
        }
        broadcastRilWakeLockRequest(requestCode);
    }

    public void resetRildOos() {
        Long currentTime = Long.valueOf(System.currentTimeMillis());
        if (currentTime.longValue() - this.mResetRildTimeByRilMessageTimeOut > 21600000) {
            Rlog.d(TAG, "resetRild");
            restartRild();
            this.mResetRildTimeByRilMessageTimeOut = currentTime.longValue();
            this.mHandler.sendEmptyMessageDelayed(6, 300000L);
        }
    }

    private boolean isNwRegReqBlocking(SparseArray<RILRequest> requestList) {
        for (int i = 0; i < requestList.size(); i++) {
            RILRequest rr = requestList.valueAt(i);
            if (21 == rr.mRequest || 20 == rr.mRequest) {
                return true;
            }
        }
        return false;
    }

    private void restartRild() {
        TelephonyManager.getDefault().restartRILD(0, 0);
        this.mResetRildCount++;
    }

    public String getRilMessageTimeOut() {
        String result = "{rilMessageTimeOutCount : " + String.valueOf(this.mRilMessageTimeOutCount) + ", " + CommenUtils.KEY_OOS_RIL_MESSAGE_TIME_OUT_COUNT + " : " + String.valueOf(this.mOosRilMessageTimeOutCount) + ", " + CommenUtils.KEY_RESET_RILD_COUNT + " : " + String.valueOf(this.mResetRildCount) + ", " + CommenUtils.KEY_RESET_RILD_INVAILD_COUNT + " : " + String.valueOf(this.mResetRildInVaildCount) + "}";
        this.mOosRilMessageTimeOutCount = 0L;
        this.mRilMessageTimeOutCount = 0L;
        this.mResetRildCount = 0L;
        this.mResetRildInVaildCount = 0L;
        return result;
    }

    private void broadcastRilWakeLockRequest(int requestCode) {
        this.mBroadcastRilWakeLockRequestTime = System.currentTimeMillis();
        Intent intent = new Intent("com.android.phone.intent.action.RIL_WAKE_LOCK_REQUEST");
        intent.putExtra("operator", TelephonyManager.getDefault().getSimOperatorForSlot(this.mPhone.getPhoneId()));
        intent.putExtra("requestCode", requestCode);
        this.mPhone.getContext().sendBroadcast(intent);
    }

    public static class Processor {
        public static final String ACTION_VIRTUALSIM_DATA_DENIED = "miui.intent.action.ACTION_VIRTUALSIM_DATA_DENIED";
        private static final String BUTTON_AUTO_SELECT_KEY = "button_auto_select_key";
        private static final int CN_OP_ID_CBN = 4;
        private static final int CN_OP_ID_CMCC = 1;
        private static final int CN_OP_ID_CT = 3;
        private static final int CN_OP_ID_CU = 2;
        private static final int CN_OP_ID_UNKNOWN = 0;
        public static final String DATA_DEREGISTERED = "Data Deregistered";
        public static final String REASON_DATA_DENIED_KEY = "reasonDataDenied";
        private static final String TAG = "GSSTInjector";
        public static final String VOICE_DEREGISTERED = "Voice Deregistered";
        private static final int sPhoneCount = android.telephony.TelephonyManager.getDefault().getPhoneCount();
        private static boolean[] sReceiveNitz = new boolean[sPhoneCount];
        private static String[] sNitzOperator = new String[sPhoneCount];
        private static int[] sNewIgnoreRat = new int[sPhoneCount];
        private static boolean mIsLowPowerReboot = false;
        private static String[] mDelayUpdateReason = new String[sPhoneCount];
        private static boolean[] mShouldDlayForSmoothOOS = new boolean[sPhoneCount];
        private static String[][] sTimeZoneIdByMcc = {new String[]{"334", "America/Mexico_City"}, new String[]{MiuiNetworkControllerImpl.REGION_CHINA_MCC, "Asia/Shanghai"}, new String[]{"404", "Asia/Calcutta"}, new String[]{"405", "Asia/Calcutta"}, new String[]{"406", "Asia/Calcutta"}, new String[]{"454", "Asia/Hong_Kong"}};
        private static String[][] sTimeZoneIdOfCapitalCity = {new String[]{"au", "Australia/Sydney"}, new String[]{ModemUtils.PROP_MIUI_BUILD_REGION_CN, "Asia/Shanghai"}, new String[]{"br", "America/Sao_Paulo"}, new String[]{"ca", "America/Toronto"}, new String[]{"cl", "America/Santiago"}, new String[]{"es", "Europe/Madrid"}, new String[]{"fm", "Pacific/Ponape"}, new String[]{"gl", "America/Godthab"}, new String[]{"id", "Asia/Jakarta"}, new String[]{"kz", "Asia/Almaty"}, new String[]{"mn", "Asia/Ulaanbaatar"}, new String[]{"mx", "America/Mexico_City"}, new String[]{"pf", "Pacific/Tahiti"}, new String[]{"pt", "Europe/Lisbon"}, new String[]{"ru", "Europe/Moscow"}, new String[]{"ua", "Europe/Kiev"}, new String[]{"us", "America/New_York"}};

        private Processor() {
        }

        public static void sendIssueIntent(ServiceStateTracker sst, String tag) {
            if (sst.mPhone.mCi.getRadioState() == 1) {
                Intent intent = new Intent("com.xiaomi.issuecollection.TelephonyIssueReceiver.RECEIVE");
                intent.putExtra(MiuiIccProvider.STR_TAG, tag);
                intent.putExtra("UTCtime", System.currentTimeMillis());
                android.telephony.SubscriptionManager.putPhoneIdAndSubIdExtra(intent, sst.mPhone.getPhoneId());
                sst.mPhone.getContext().sendBroadcastAsUser(intent, UserHandle.ALL);
            }
        }

        public static String getSpnUpdateActionName(ServiceStateTracker sst) {
            int slotId = sst.mPhone.getPhoneId();
            return slotId == 0 ? "android.telephony.action.SERVICE_PROVIDERS_UPDATED" : "android.telephony.action.SERVICE_PROVIDERS_UPDATED" + slotId;
        }

        public static String getSpn(ServiceStateTracker sst) {
            if (sst.mIccRecords != null) {
                if ((sst.getServiceState().isEmergencyOnly() && !sst.getPhone().isWifiCallingEnabled()) || ServiceProviderUtils.isAPTSim(sst.mPhone.getPhoneId()) || sst.getCombinedRegState(sst.mSS) != 0) {
                    return "";
                }
                String spn = sst.mIccRecords.getServiceProviderName();
                if (TextUtils.isEmpty(spn)) {
                    spn = sst.getServiceProviderName();
                }
                String spnOverride = ServiceProviderUtils.get(sst.mIccRecords.getOperatorNumeric(), sst.getPhoneId(), spn);
                if (!TextUtils.isEmpty(spnOverride)) {
                    spn = spnOverride;
                }
                if (sst.mSS != null && !sst.mSS.getRoaming() && alwaysShowPlmn(sst.mIccRecords.getOperatorNumeric(), sst.mPhone.getPhoneId())) {
                    return sst.mSS.getOperatorAlphaLong();
                }
                return spn;
            }
            return "";
        }

        public static String getPlmn(ServiceStateTracker sst) {
            String condition;
            Rlog.i(TAG, "getPlmn() is called.");
            String spn = "";
            if (sst.mIccRecords != null) {
                spn = sst.mIccRecords.getServiceProviderName();
                boolean isMvno = ServiceProviderUtils.isVirtualServiceProvider(sst.mIccRecords.getOperatorNumeric(), sst.getPhoneId(), spn);
                boolean isRoaming = sst.mSS.getRoaming();
                Rlog.d(TAG, "isRoaming=" + isRoaming + " isMvno=" + isMvno + " spn= " + spn);
                if (!isRoaming && isMvno) {
                    String spnOverride = ServiceProviderUtils.get(sst.mIccRecords.getOperatorNumeric(), sst.getPhoneId(), spn);
                    if (!TextUtils.isEmpty(spnOverride)) {
                        return spnOverride;
                    }
                }
            }
            if (supportNonRoamingShowNonNullNitz(sst)) {
                String nitzPlmn = sst.mSS.getOperatorAlphaLong();
                List<String> needModifyPlmns = HyperOSCustFeatureResolve.getStringArray("config_need_modify_plmns");
                if (!TextUtils.isEmpty(nitzPlmn) && ArrayUtils.contains(needModifyPlmns, nitzPlmn)) {
                    return ServiceProviderUtils.get(sst.mSS.getOperatorNumeric(), sst.getPhoneId(), sst.mSS.getOperatorAlphaLong());
                }
                return nitzPlmn;
            }
            String plmn = ServiceProviderUtils.get(sst.mSS.getOperatorNumeric(), sst.getPhoneId(), sst.mSS.getOperatorAlphaLong());
            Map<String, String> data = HyperOSCustFeatureResolve.getMapForPhoneId(sst.mPhone.getPhoneId(), "plmn_network_display_rule");
            if (!ArrayUtils.isEmpty(data)) {
                for (String key : data.keySet()) {
                    if (isNumeric(key) && !TextUtils.equals(key, sst.mSS.getOperatorNumeric())) {
                        Rlog.d(TAG, "key is not matched");
                    } else {
                        String plmnRuleFromXml = data.get(key);
                        Rlog.d(TAG, "getPlmn() key = " + key + ", plmnRuleFromXml = " + plmnRuleFromXml);
                        if (TextUtils.isEmpty(plmnRuleFromXml)) {
                            continue;
                        } else {
                            String[] values = plmnRuleFromXml.split(",");
                            int plmnRule = Integer.parseInt(values[0]);
                            Rlog.d(TAG, "getPlmn() plmnRule = " + plmnRule);
                            if (values.length == 1) {
                                condition = Integer.toString(0);
                            } else {
                                condition = values[1];
                            }
                            switch (plmnRule) {
                                case 1:
                                    if (matchCondition(condition, sst)) {
                                        return spn;
                                    }
                                    continue;
                                    break;
                                case 2:
                                    if (matchCondition(condition, sst)) {
                                        return sst.mSS.getOperatorAlphaLong();
                                    }
                                    continue;
                                    break;
                                case 3:
                                    if (matchCondition(condition, sst)) {
                                        return sst.mSS.getOperatorAlphaShort();
                                    }
                                    break;
                                case 4:
                                    break;
                                case 5:
                                    if (values.length != 3) {
                                        continue;
                                    } else {
                                        if (TextUtils.equals(values[1], plmn)) {
                                            return values[2];
                                        }
                                    }
                                    break;
                                default:
                                    Rlog.d(TAG, "plmnRule is invalid.");
                                    continue;
                            }
                            if (values.length == 3 && matchCondition(condition, sst)) {
                                return values[2];
                            }
                        }
                    }
                }
            }
            Rlog.i(TAG, "getPlmn() isCustFeatureResolveEnabled plmn = " + plmn);
            return plmn;
        }

        private static boolean matchCondition(String condition, ServiceStateTracker sst) {
            try {
                int conditionInt = Integer.parseInt(condition);
                Rlog.i(TAG, "matchCondition() conditionInt = " + conditionInt);
                boolean isMatched = false;
                switch (conditionInt) {
                    case 0:
                        isMatched = true;
                        break;
                    case 1:
                        isMatched = sst.mSS.getRoaming();
                        break;
                    case 2:
                        isMatched = !sst.mSS.getRoaming();
                        break;
                    case 3:
                        isMatched = sst.getPhone().isWifiCallingEnabled();
                        break;
                }
                Rlog.i(TAG, "matchCondition() isMatched = " + isMatched);
                return isMatched;
            } catch (NumberFormatException e) {
                Rlog.e(TAG, "invalid numberc=" + condition);
                return false;
            }
        }

        private static boolean isNumeric(String str) {
            if (str == null || str.length() == 0) {
                return false;
            }
            return str.matches("[0-9]+");
        }

        public static boolean isOnlyShowSpn(ServiceStateTracker sst) {
            if (sst.mIccRecords != null && sst.mSS != null) {
                String operatorNumeric = sst.mSS.getOperatorNumeric();
                String[] numericArray = null;
                CarrierConfigManager configLoader = (CarrierConfigManager) sst.mPhone.getContext().getSystemService("carrier_config");
                if (configLoader != null) {
                    try {
                        PersistableBundle b = configLoader.getConfigForSubId(sst.mPhone.getSubId());
                        if (b != null) {
                            numericArray = b.getStringArray("config_only_show_spn_on_roaming");
                        }
                    } catch (Exception e) {
                        Rlog.e(TAG, "isOnlyShowSpn: carrier config error: " + e.toString());
                    }
                }
                if (ArrayUtils.isEmpty(numericArray) || operatorNumeric == null) {
                    return false;
                }
                for (String numeric : numericArray) {
                    if (!TextUtils.isEmpty(numeric) && operatorNumeric.startsWith(numeric)) {
                        Rlog.d(TAG, "updateSpnDisplay isOnlyShowSpn");
                        return true;
                    }
                }
            }
            return false;
        }

        public static boolean isOnlyShowPlmn(ServiceStateTracker sst) {
            if (sst.mIccRecords != null && sst.mPhone != null) {
                boolean isSfrRoamingOnMeo = "20810".equals(sst.mIccRecords.getOperatorNumeric()) && "26806".equals(sst.mSS.getOperatorNumeric()) && sst.mSS.getRoaming();
                boolean isOnlyShowPlmn = HyperOSCustFeatureResolve.getBooleanForPhoneId(sst.mPhone.getPhoneId(), "is_only_show_plmn", false);
                boolean isNonRoamingShowNonNullNitz = supportNonRoamingShowNonNullNitz(sst);
                Rlog.d(TAG, "updateSpnDisplay isOnlyShowPlmn = " + isOnlyShowPlmn);
                if (isOnlyShowPlmn || isSfrRoamingOnMeo || isNonRoamingShowNonNullNitz) {
                    return true;
                }
            }
            return false;
        }

        private static boolean supportNonRoamingShowNonNullNitz(ServiceStateTracker sst) {
            if (sst.mPhone == null || sst.mSS == null) {
                return false;
            }
            boolean operatorSupport = HyperOSCustFeatureResolve.getBooleanForPhoneId(sst.mPhone.getPhoneId(), "is_non_roaming_show_non_null_nitz", false);
            String operatorName = sst.mSS.getOperatorAlphaLong();
            Rlog.d(TAG, "supportNonRoamingShowNonNullNitz operatorSupport = " + operatorSupport + ", operatorName = " + operatorName);
            return (TextUtils.isEmpty(operatorName) || !operatorSupport || isRoamingState(sst)) ? false : true;
        }

        private static boolean isRoamingState(ServiceStateTracker sst) {
            boolean isRoaming;
            if (sst.mIccRecords == null || sst.mPhone == null || sst.mSS == null) {
                return false;
            }
            CarrierConfigManager configLoader = (CarrierConfigManager) sst.mPhone.getContext().getSystemService("carrier_config");
            boolean useRoamingFromServiceState = false;
            if (configLoader != null) {
                try {
                    PersistableBundle b = configLoader.getConfigForSubId(sst.mPhone.getSubId());
                    if (b != null) {
                        useRoamingFromServiceState = b.getBoolean("spn_display_rule_use_roaming_from_service_state_bool");
                    }
                } catch (Exception e) {
                    Rlog.e(TAG, "isRoamingState: carrier config error: " + e.toString());
                }
            }
            if (useRoamingFromServiceState) {
                isRoaming = sst.mSS.getRoaming();
            } else {
                String[] hplmns = sst.mIccRecords.getHomePlmns();
                isRoaming = !ArrayUtils.contains(hplmns, sst.mSS.getOperatorNumeric());
                if (TextUtils.isEmpty(sst.mSS.getOperatorNumeric())) {
                    isRoaming = false;
                }
            }
            Rlog.d(TAG, "isRoamingState = " + isRoaming);
            return isRoaming;
        }

        public static boolean isShowPlmnLimitedService(ServiceStateTracker sst) {
            if (sst.mIccRecords != null && sst.mSS != null) {
                int rejCode = sst.mSS.getNetworkRegistrationInfo(1, 1).getRejectCause();
                int dataNetworkType = sst.mSS.getDataNetworkType();
                if (TelephonyManager.isForEEA() && isShowLimitedServiceSim(sst) && sst.mSS.getState() != 0 && sst.mSS.getDataRegistrationState() == 0 && sst.mSS.getRilDataRadioTechnology() != 18) {
                    Rlog.d(TAG, "rejCode= " + rejCode + "dataNetworkType= " + dataNetworkType);
                    return ((rejCode == 18 || rejCode == 0) && isLTEorNR(dataNetworkType)) ? false : true;
                }
            }
            return false;
        }

        private static boolean isLTEorNR(int dataNetworkType) {
            if (dataNetworkType == 13 || dataNetworkType == 20) {
                return true;
            }
            return false;
        }

        private static boolean isShowLimitedServiceSim(ServiceStateTracker sst) {
            boolean config = CarrierConfigManagerEx.getBooleanConfigForSubId(sst.mPhone.getContext(), sst.mPhone.getSubId(), "config_show_plmn_limited_service");
            return config;
        }

        public static boolean isShowSpnConfigWithoutSimSpn(ServiceStateTracker sst) {
            boolean showSpnConfigWithoutSimSpn = HyperOSCustFeatureResolve.getBooleanForPhoneId(sst.mPhone.getPhoneId(), "show_spn_config_without_sim_spn", false);
            return showSpnConfigWithoutSimSpn;
        }

        public static String getLimitedServicePlmn() {
            return Resources.getSystem().getText(286196342).toString();
        }

        private static boolean alwaysShowPlmn(String operatorNumeric, int phoneId) {
            boolean alwaysShowPlmn = HyperOSCustFeatureResolve.getBooleanForPhoneId(phoneId, "always_show_plmn", false);
            Rlog.d(TAG, "alwaysShowPlmn() alwaysShowPlmn = " + alwaysShowPlmn + ", phoneId = " + phoneId);
            return alwaysShowPlmn;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isBothPlmnAndSpnShow(ServiceStateTracker sst) {
            if (sst.mIccRecords == null) {
                return false;
            }
            int phoneId = sst.getPhone().getPhoneId();
            boolean isBothPlmnAndSpnShow = HyperOSCustFeatureResolve.getBooleanForPhoneId(phoneId, "config_both_show_plmn_and_spn", false);
            return isBothPlmnAndSpnShow && !TextUtils.isEmpty(sst.mSS.getOperatorAlphaLong());
        }

        public static boolean isOperatorConsideredNonRoaming(Phone phone, String newOperatorNumeric) {
            ServiceStateTracker sst = phone.getServiceStateTracker();
            if (sst.mIccRecords == null || newOperatorNumeric == null || newOperatorNumeric.length() < 3) {
                return false;
            }
            String networkMcc = newOperatorNumeric.substring(0, 3);
            String mcclist = HyperOSCustFeatureResolve.getStringForPhoneId(phone.getPhoneId(), "operator_considered_non_roaming_mcclist", "");
            if (!mcclist.contains(networkMcc)) {
                return false;
            }
            Rlog.d(TAG, "Consider " + newOperatorNumeric + " as non roaming.");
            return true;
        }

        public static String getOperatorNumericFromPs(ServiceStateTracker sst, String operatorNumeric) {
            NetworkRegistrationInfo wwanCsRegState;
            if (TextUtils.isEmpty(operatorNumeric)) {
                String newOperatorNumeric = null;
                if (sst.mNewSS.getDataRegistrationState() == 0) {
                    NetworkRegistrationInfo wwanPsRegState = sst.mNewSS.getNetworkRegistrationInfo(2, 1);
                    if (wwanPsRegState != null) {
                        newOperatorNumeric = wwanPsRegState.getRegisteredPlmn();
                    }
                } else if (sst.mNewSS.getVoiceRegState() == 0 && (wwanCsRegState = sst.mNewSS.getNetworkRegistrationInfo(1, 1)) != null) {
                    newOperatorNumeric = wwanCsRegState.getRegisteredPlmn();
                }
                if (!TextUtils.isEmpty(newOperatorNumeric)) {
                    return newOperatorNumeric;
                }
            }
            return operatorNumeric;
        }

        public static boolean getPsRegistrationState(ServiceState ss) {
            boolean isRegisteredOnWwan = false;
            for (NetworkRegistrationInfo nri : ss.getNetworkRegistrationInfoListForTransportType(1)) {
                isRegisteredOnWwan |= nri.isRegistered();
            }
            return isRegisteredOnWwan;
        }

        public static int convertNetworkType(ServiceStateTracker sst, int type) {
            String operator = sst.mSS.getOperatorNumeric();
            if (20 == type && (TextUtils.isEmpty(operator) || !ArrayUtils.contains(sst.mPhone.mContext.getResources().getStringArray(285409290), operator))) {
                return 15;
            }
            return type;
        }

        public static void setReceivedNitz(int phoneId, boolean received) {
            sReceiveNitz[phoneId] = received;
            Rlog.d(TAG, "setReceivedNitz phoneId=" + phoneId + " received=" + received);
        }

        public static void setReceivedNitzOperator(int phoneId, String nitzOperator) {
            sNitzOperator[phoneId] = nitzOperator == null ? "" : nitzOperator;
            Rlog.d(TAG, "setReceivedNitzOperator phoneId=" + phoneId + " nitzOperator=" + nitzOperator);
        }

        public static void setReceivedNitzAndOperator(int phoneId, boolean received, String nitzOperator) {
            sReceiveNitz[phoneId] = received;
            sNitzOperator[phoneId] = nitzOperator == null ? "" : nitzOperator;
            Rlog.d(TAG, "setReceivedNitz phoneId=" + phoneId + " received=" + received + " nitzOperator=" + nitzOperator);
        }

        public static void setDelayForSmoothOOS(int phoneId, boolean delay) {
            mShouldDlayForSmoothOOS[phoneId] = delay;
            Rlog.d(TAG, "setDelayForSmoothOOS phoneId=" + phoneId + " reason=" + delay);
        }

        public static boolean getShouldDelayForSmoothOOS(int phoneId) {
            Rlog.d(TAG, "getShouldDelayForSmoothOOS phoneId=" + phoneId + " delay=" + mShouldDlayForSmoothOOS[phoneId]);
            return mShouldDlayForSmoothOOS[phoneId];
        }

        public static void setDelayUpdateReason(int phoneId, String reason) {
            mDelayUpdateReason[phoneId] = reason;
            Rlog.d(TAG, "mDelayUpdateReason phoneId=" + phoneId + " reason=" + reason);
        }

        public static String getDelayUpdateReason(int phoneId) {
            return mDelayUpdateReason[phoneId];
        }

        public static boolean isSkipDataConnectionNotifier(int phoneId) {
            return CommenUtils.AVOID_OOS_FOR_REFRESH_MBN.equals(mDelayUpdateReason[phoneId]) || mShouldDlayForSmoothOOS[phoneId];
        }

        public static boolean getReceivedNitz(int phoneId) {
            return sReceiveNitz[phoneId];
        }

        public static TimeZone getTimeZoneByMcc(String mcc, TimeZone originalTimeZone) {
            if (TextUtils.isEmpty(mcc) || getReceivedNitz()) {
                return originalTimeZone;
            }
            TimeZone zone = getTimeZoneByMcc(mcc);
            return zone == null ? originalTimeZone : zone;
        }

        public static TimeZone getTimeZoneByMcc(String mcc) {
            if (TextUtils.isEmpty(mcc)) {
                return null;
            }
            TimeZone ret = null;
            String mccTz = null;
            for (int i = 0; i < sTimeZoneIdByMcc.length; i++) {
                if (mcc.equals(sTimeZoneIdByMcc[i][0])) {
                    mccTz = sTimeZoneIdByMcc[i][1];
                    break;
                }
            }
            if (mccTz != null) {
                ret = TimeZone.getTimeZone(mccTz);
            }
            Rlog.d(TAG, "getTimeZoneByMcc mcc:" + mcc + " zone.getID=" + (ret == null ? "" : ret.getID()));
            return ret;
        }

        public static boolean updateNitzInfo(int slot, int zoneOffset, String currentOperator) {
            String currentMcc;
            TimeZone mccZone;
            String strSubstring;
            if (sReceiveNitz[slot] && sNitzOperator[slot].isEmpty()) {
                Rlog.d(TAG, "updateNitzInfo from  slot=" + slot + " NitzOperator old=" + sNitzOperator[slot] + " new=" + currentOperator);
                sNitzOperator[slot] = currentOperator;
            }
            if (zoneOffset == 0) {
                return false;
            }
            if (currentOperator != null && currentOperator.length() >= 3) {
                currentMcc = currentOperator.substring(0, 3);
            } else {
                currentMcc = "";
            }
            if (!MiuiNetworkControllerImpl.REGION_CHINA_MCC.equals(currentMcc) || currentOperator.equals(sNitzOperator[slot]) || (mccZone = getTimeZoneByMcc(currentMcc)) == null) {
                return false;
            }
            int mccZoneOffset = mccZone.getRawOffset();
            Rlog.d(TAG, "needResetTimezone slot=" + slot + " mccZoneOffset=" + mccZoneOffset + " zoneOffset=" + zoneOffset + " nitzOperator" + sNitzOperator[slot]);
            if (zoneOffset == mccZoneOffset) {
                if (sNitzOperator[slot].length() >= 3) {
                    strSubstring = sNitzOperator[slot].substring(0, 3);
                } else {
                    strSubstring = "";
                }
                if (currentMcc.equals(strSubstring)) {
                    return false;
                }
            }
            sReceiveNitz[slot] = false;
            sNitzOperator[slot] = "";
            return true;
        }

        public static TimeZone getTimeZoneWithCapitalCity(String iso, boolean canGet) {
            if (!canGet || TextUtils.isEmpty(iso)) {
                return null;
            }
            ArrayList<TimeZone> zones = MiuiTimeUtils.getTimeZones(iso);
            String currentZoneID = TimeZone.getDefault().getID();
            for (TimeZone zone : zones) {
                if (currentZoneID.equals(zone.getID())) {
                    Rlog.d(TAG, "getTimeZonesWithCapitalCity iso " + iso + " , current zone ID " + currentZoneID);
                    return null;
                }
            }
            TimeZone ret = null;
            for (int i = 0; i < sTimeZoneIdOfCapitalCity.length; i++) {
                if (iso.equals(sTimeZoneIdOfCapitalCity[i][0])) {
                    ret = TimeZone.getTimeZone(sTimeZoneIdOfCapitalCity[i][1]);
                    break;
                }
            }
            Rlog.d(TAG, "getTimeZonesWithCapitalCity iso=" + iso + " zone.getID=" + (ret == null ? "" : ret.getID()));
            return ret;
        }

        public static boolean getReceivedNitz() {
            for (int i = 0; i < sReceiveNitz.length; i++) {
                if (sReceiveNitz[i]) {
                    return true;
                }
            }
            return false;
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.android.internal.telephony.MiuiNetworkControllerImpl$Processor$1] */
        public static void processForCustSingleSimDevice() {
            new Thread() { // from class: com.android.internal.telephony.MiuiNetworkControllerImpl.Processor.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    Rlog.d(Processor.TAG, "isCustSingleSimDevice setIccCardActivate false on slot 1");
                    IccCardActivateHelper.setIccCardActivate(1, false);
                }
            }.start();
        }

        /* JADX WARN: Type inference failed for: r1v3, types: [com.android.internal.telephony.MiuiNetworkControllerImpl$Processor$2] */
        protected static void checkDeactiveForCustSingleSim() {
            Phone phone = PhoneFactory.getPhone(1);
            if (phone != null && phone.getServiceState() != null && phone.getServiceState().getState() == 0) {
                new Thread() { // from class: com.android.internal.telephony.MiuiNetworkControllerImpl.Processor.2
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        Rlog.d(Processor.TAG, "isCustSingleSimDevice setIccCardActivate false on slot 1 retry");
                        IccCardActivateHelper.setIccCardActivate(1, false);
                    }
                }.start();
            }
        }

        public static void dump(Phone phone, FileDescriptor fd, PrintWriter pw, String[] args) {
            StringBuilder sb = new StringBuilder(RadioError.OEM_ERROR_12).append("Processor: ");
            sb.append(" sReceiveNitz=").append(sReceiveNitz[phone.getPhoneId()]).append(" sNitzOperator=" + sNitzOperator[phone.getPhoneId()]);
            pw.println(sb.toString());
            pw.flush();
        }

        protected static void processDeviceStateChange(Phone phone, MiuiDeviceStateMonitor monitor) {
            if (phone == null || monitor == null) {
                return;
            }
            int newRat = sNewIgnoreRat[phone.getPhoneId()];
            int oldRat = phone.getServiceStateTracker().mSS.getRilDataRadioTechnology();
            if (newRat != 0 && newRat != oldRat && (isHspaDataRat(oldRat) || is4gDataRat(oldRat))) {
                Rlog.d(TAG, "deviceState[" + phone.getPhoneId() + "] (" + monitor.stateToString() + "), update service state from old rat " + oldRat + " to new rat" + newRat);
                ServiceStateTracker ssT = phone.getServiceStateTracker();
                if (ssT != null) {
                    if (ssT.mPollingContext[0] == 0) {
                        ssT.mNewSS = new ServiceState(ssT.mSS);
                        ssT.mNewSS.setRilDataRadioTechnology(newRat);
                        ssT.pollStateDone();
                    } else {
                        ssT.mNewSS.setRilDataRadioTechnology(newRat);
                    }
                }
            }
            sNewIgnoreRat[phone.getPhoneId()] = 0;
        }

        public static void tryResetDataRat(ServiceStateTracker sst, MiuiDeviceStateMonitor monitor) {
            if (monitor != null && monitor.shouldTurnOffServiceStateFrequentUpdate()) {
                int oldRat = sst.mSS.getRilDataRadioTechnology();
                int newRat = sst.mNewSS.isUsingCarrierAggregation() ? 19 : sst.mNewSS.getRilDataRadioTechnology();
                if ((isHspaDataRat(oldRat) && isHspaDataRat(newRat)) || (is4gDataRat(oldRat) && is4gDataRat(newRat))) {
                    sNewIgnoreRat[sst.mPhone.getPhoneId()] = newRat;
                    sst.mNewSS.setRilDataRadioTechnology(oldRat);
                    updatePsNetworkRegistrationStateTechnology(sst.mNewSS, oldRat);
                    Rlog.d(TAG, "deviceState[" + sst.mPhone.getPhoneId() + "] (" + monitor.stateToString() + "), reset new rat " + newRat + " to old rat" + oldRat + ", sNewIgnoreRat: " + sNewIgnoreRat[sst.mPhone.getPhoneId()]);
                    return;
                }
                sNewIgnoreRat[sst.mPhone.getPhoneId()] = 0;
            }
        }

        public static void updatePsNetworkRegistrationStateTechnology(ServiceState ss, int rat) {
            int accessNetworkType = ServiceState.rilRadioTechnologyToNetworkType(rat);
            NetworkRegistrationInfo nri = ss.getNetworkRegistrationInfo(1, 2);
            if (nri != null) {
                nri.setAccessNetworkTechnology(accessNetworkType);
            }
        }

        public static void updateServiceStateDataRat(ServiceState ss, int newDataRat) {
            if (ss == null) {
                return;
            }
            ss.setRilDataRadioTechnology(newDataRat);
            updatePsNetworkRegistrationStateTechnology(ss, newDataRat);
        }

        private static boolean isHspaDataRat(int dataRat) {
            if (dataRat == 9 || dataRat == 10 || dataRat == 11 || dataRat == 15) {
                return true;
            }
            return false;
        }

        private static boolean is4gDataRat(int dataRat) {
            if (dataRat == 14 || dataRat == 19) {
                return true;
            }
            return false;
        }

        public static void handleMccMncChange(Context context, ServiceState newSS, ServiceState ss) {
            CountryMonitor.getInstance().handleMccMncChange(context, newSS, ss);
        }

        public static void handleDataDenied(Phone phone, int oldReasonDataDenied, int newReasonDataDenied) {
            if (phone != null && !isDataDenied(oldReasonDataDenied) && isDataDenied(newReasonDataDenied) && VirtualSimUtils.getInstance().getVirtualSimSlotId() == phone.getPhoneId()) {
                Intent intent = new Intent(ACTION_VIRTUALSIM_DATA_DENIED);
                intent.putExtra(REASON_DATA_DENIED_KEY, newReasonDataDenied);
                android.telephony.SubscriptionManager.putPhoneIdAndSubIdExtra(intent, phone.getPhoneId());
                phone.getContext().sendBroadcastAsUser(intent, UserHandle.ALL);
            }
        }

        private static boolean isDataDenied(int reasondataDenied) {
            return (reasondataDenied == -1 || reasondataDenied == 0) ? false : true;
        }

        public static String getWfcDisplaySpn(ServiceStateTracker sst, String wfcVoiceSpnFormat, String originalSpn) {
            String displaySpn;
            boolean showSpnForVowifi = true;
            CarrierConfigManager configLoader = (CarrierConfigManager) sst.mPhone.getContext().getSystemService("carrier_config");
            if (configLoader != null) {
                try {
                    PersistableBundle b = configLoader.getConfigForSubId(sst.mPhone.getSubId());
                    if (b != null) {
                        showSpnForVowifi = b.getBoolean("carrier_show_spn_for_vowifi", true);
                    }
                } catch (Exception e) {
                    Rlog.e(TAG, "getDisplaySpn updateSpnDisplay: carrier config error: " + e.toString());
                }
            }
            if (!showSpnForVowifi) {
                displaySpn = String.format(wfcVoiceSpnFormat, "").trim();
            } else {
                displaySpn = String.format(wfcVoiceSpnFormat, originalSpn);
            }
            String displaySpn2 = HyperOSCustFeatureResolve.getStringForPhoneId(sst.mPhone.getPhoneId(), "config_wfc_display_spn_name", displaySpn);
            Rlog.d(TAG, "getDisplaySpn updateSpnDisplay: showSpnForVowifi = " + showSpnForVowifi + "; originalSpn = " + originalSpn + "; displaySpn = " + displaySpn2);
            return displaySpn2;
        }

        public static String getWfcDisplayPlmn(ServiceStateTracker sst, String wfcVoiceSpnFormat, String originalPlmn) {
            String displayPlmn;
            boolean showPlmnForVowifi = true;
            CarrierConfigManager configLoader = (CarrierConfigManager) sst.mPhone.getContext().getSystemService("carrier_config");
            if (configLoader != null) {
                try {
                    PersistableBundle b = configLoader.getConfigForSubId(sst.mPhone.getSubId());
                    if (b != null) {
                        showPlmnForVowifi = b.getBoolean("carrier_show_plmn_for_vowifi", true);
                    }
                } catch (Exception e) {
                    Rlog.e(TAG, "getDisplayPlmn updateSpnDisplay: carrier config error: " + e.toString());
                }
            }
            if (!showPlmnForVowifi) {
                displayPlmn = String.format(wfcVoiceSpnFormat, "").trim();
            } else {
                displayPlmn = String.format(wfcVoiceSpnFormat, originalPlmn);
            }
            Rlog.d(TAG, "getDisplayPlmn updateSpnDisplay: showPlmnForVowifi = " + showPlmnForVowifi + "; originalPlmn = " + originalPlmn + "; displayPlmn = " + displayPlmn);
            return displayPlmn;
        }

        public static void hangupImsCallIfNeeded(Phone phone, boolean isDeviceShuttingDown) {
            if (!TelephonyManager.isCustForKrKt()) {
                return;
            }
            Rlog.d(TAG, "hangupImsCallIfNeeded isDeviceShuttingDown = " + isDeviceShuttingDown + "; mIsLowPowerReboot = " + mIsLowPowerReboot);
            if (!isDeviceShuttingDown || !mIsLowPowerReboot) {
                return;
            }
            ImsPhone imsPhone = phone.getImsPhone();
            if (imsPhone != null) {
                ImsPhoneCallTracker tracker = imsPhone.getCallTracker();
                ImsCall imsRingCall = tracker.mRingingCall.getImsCall();
                if (imsRingCall != null && tracker.mRingingCall.getState().isAlive()) {
                    imsRingCall.terminate(RadioError.OEM_ERROR_5);
                }
                ImsCall imsBgCall = tracker.mBackgroundCall.getImsCall();
                if (imsBgCall != null && tracker.mBackgroundCall.getState().isAlive()) {
                    imsBgCall.terminate(RadioError.OEM_ERROR_5);
                }
                ImsCall imsFgCall = tracker.mForegroundCall.getImsCall();
                if (imsFgCall != null && tracker.mForegroundCall.getState().isAlive()) {
                    imsFgCall.terminate(RadioError.OEM_ERROR_5);
                }
            }
            mIsLowPowerReboot = false;
        }

        public static void notifyServiceStateChanged(Phone phone) {
            phone.notifyServiceStateChangedP(phone.getServiceState());
        }

        public static boolean shouldForceDisplayNoServiceForBuild() {
            boolean isVoiceCapable = android.telephony.TelephonyManager.getDefault().isVoiceCapable();
            Rlog.d(TAG, "shouldForceDisplayNoServiceForBuild isVoiceCapable = " + isVoiceCapable);
            return !isVoiceCapable && HyperOSCustFeatureResolve.getBoolean("config_force_display_no_service", false);
        }

        private static String getCnOpName(int id, int phoneId) {
            switch (id) {
                case 1:
                    return ServiceProviderUtils.get("46000", phoneId);
                case 2:
                    return ServiceProviderUtils.get("46001", phoneId);
                case 3:
                    return ServiceProviderUtils.get("46003", phoneId);
                case 4:
                    return ServiceProviderUtils.get("46015", phoneId);
                default:
                    return "";
            }
        }

        public static final String getCnSharePlmn(ServiceStateTracker sst) {
            if (sst == null || sst.mIccRecords == null || !sst.mSS.getOperatorNumeric().startsWith(MiuiNetworkControllerImpl.REGION_CHINA_MCC)) {
                return null;
            }
            String plmn = sst.mSS.getOperatorNumeric();
            String numeric = sst.mIccRecords.getOperatorNumeric();
            int phoneId = sst.getPhoneId();
            Rlog.d(TAG, "getCnSharePlmn: plmn = " + plmn + ", phoneId = " + phoneId + ", numeric = " + numeric);
            if (ServiceProviderUtils.isChinaMobile(numeric)) {
                if (plmn.equals("46050")) {
                    return getCnOpName(1, phoneId) + "-" + getCnOpName(3, phoneId);
                }
                if (plmn.equals("46031")) {
                    return getCnOpName(1, phoneId) + "-" + getCnOpName(2, phoneId);
                }
            }
            if (ServiceProviderUtils.isChinaUnicom(numeric)) {
                if (plmn.equals("46022")) {
                    return getCnOpName(2, phoneId) + "-" + getCnOpName(1, phoneId);
                }
                if (plmn.equals("46061")) {
                    return getCnOpName(2, phoneId) + "-" + getCnOpName(4, phoneId);
                }
            }
            if (ServiceProviderUtils.isChinaTelecom(numeric)) {
                if (plmn.equals("46021")) {
                    return getCnOpName(3, phoneId) + "-" + getCnOpName(1, phoneId);
                }
                if (plmn.equals("46060")) {
                    return getCnOpName(3, phoneId) + "-" + getCnOpName(4, phoneId);
                }
            }
            if (isCbnSim(numeric)) {
                if (plmn.equals("46051")) {
                    return getCnOpName(4, phoneId) + "-" + getCnOpName(3, phoneId);
                }
                if (plmn.equals("46032")) {
                    return getCnOpName(4, phoneId) + "-" + getCnOpName(2, phoneId);
                }
            }
            return null;
        }

        public static final boolean isCnSharedPlmn(String numeric) {
            if (TextUtils.isEmpty(numeric) || !numeric.startsWith(MiuiNetworkControllerImpl.REGION_CHINA_MCC)) {
                return false;
            }
            List<String> cnSharePlmns = HyperOSCustFeatureResolve.getStringArray("config_cn_share_plmns");
            return ArrayUtils.contains(cnSharePlmns, numeric);
        }

        private static boolean isCbnSim(String numeric) {
            return "46015".equals(numeric);
        }

        public static boolean isAutoNetSelInUI(Phone phone) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(phone.getContext());
            if (sp.contains(BUTTON_AUTO_SELECT_KEY)) {
                boolean oldSetting = sp.getBoolean(BUTTON_AUTO_SELECT_KEY, true);
                sp.edit().remove(BUTTON_AUTO_SELECT_KEY).apply();
                if (oldSetting) {
                    return true;
                }
            }
            return sp.getBoolean(BUTTON_AUTO_SELECT_KEY + phone.getPhoneId(), true);
        }

        public static void restoreSettings(Phone phone) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(phone.getContext());
            sp.edit().remove(BUTTON_AUTO_SELECT_KEY + phone.getPhoneId()).apply();
        }
    }

    private void register5GNetworkTypeObserver() {
        if (this.mPhone == null || !android.telephony.SubscriptionManager.isValidPhoneId(this.mPhone.getPhoneId())) {
            return;
        }
        this.mPhone.getContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor(FIVEG_ICON_GROUP_SETTING_PREFIX + this.mPhone.getPhoneId()), false, this.m5GNetworkTypeObserver);
    }

    private void unRegister5GNetworkTypeObserver() {
        if (this.mPhone == null) {
            return;
        }
        this.mPhone.getContext().getContentResolver().unregisterContentObserver(this.m5GNetworkTypeObserver);
    }

    private void registerEnableNitzTimeZonePreferredObserver() {
        if (this.mPhone == null) {
            return;
        }
        this.mPhone.getContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor("enable_nitz_timezone_preferred"), false, this.mNitzTimeZonePreferredObserver);
    }

    private void unregisterEnableNitzTimeZonePreferredObserver() {
        if (this.mPhone == null) {
            return;
        }
        this.mPhone.getContext().getContentResolver().unregisterContentObserver(this.mNitzTimeZonePreferredObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterForPreciseCallStateChanged() {
        CallManager.getInstance(this.mPhone.getContext()).unregisterForPreciseCallStateChanged(this.mHandler);
    }

    private boolean isVaildLteRsrp(int rsrp) {
        return rsrp >= -140 && rsrp <= -43;
    }

    private boolean isVaildNrRsrp(int rsrp) {
        return rsrp >= -156 && rsrp <= -31;
    }

    public void updateSignalPrimaryInNsa(SignalStrength signalStrength) {
        if ((miui.os.Build.IS_STABLE_VERSION && !this.mSupportUpdateSignalPrimaryInNsa) || TelephonyOptCloudController.CLOUD_CONTROLLER_UPDATE_SIGNAL_PRIMARY_NSA_DISABLED) {
            Rlog.d(TAG, "updateSignalPrimaryInNsa not support, mSupportUpdateSignalPrimaryInNsa = " + this.mSupportUpdateSignalPrimaryInNsa);
            return;
        }
        if (signalStrength == null) {
            Rlog.d(TAG, "updateSignalPrimaryInNsa signalStrength is null");
            return;
        }
        int lteRsrp = Integer.MAX_VALUE;
        int nrSsRsrp = Integer.MAX_VALUE;
        for (CellSignalStrength cellSignal : signalStrength.getCellSignalStrengths()) {
            if (cellSignal instanceof CellSignalStrengthLte) {
                lteRsrp = ((CellSignalStrengthLte) cellSignal).getRsrp();
            } else if (cellSignal instanceof CellSignalStrengthNr) {
                nrSsRsrp = ((CellSignalStrengthNr) cellSignal).getSsRsrp();
            }
        }
        if (isVaildLteRsrp(lteRsrp) && isVaildNrRsrp(nrSsRsrp) && nrSsRsrp > lteRsrp && this.mIsShow5GIcon) {
            signalStrength.setSignalPrimaryInNrNsa(true);
        } else {
            signalStrength.setSignalPrimaryInNrNsa(false);
        }
    }

    public String lookupOperatorNameEons(String spnFromEons, int phoneId, int subId, String numeric, boolean desireLongName, TriFunction<Integer, String, Boolean, String> nitzfunc) {
        SubscriptionInfo subInfo;
        try {
            Phone phone = PhoneFactory.getPhone(phoneId);
            if (phone == null) {
                return null;
            }
            ServiceStateTracker sst = phone.getServiceStateTracker();
            if (subId == phone.getSubId() && "Turk Telekom".equals(spnFromEons) && sst != null && sst.mSS != null && !sst.mSS.getRoaming() && sst.mIccRecords != null && "28601".equals(sst.mIccRecords.getOperatorNumeric()) && sst.mIccRecords.getHomePlmns() != null && ArrayUtils.contains(sst.mIccRecords.getHomePlmns(), "28603") && "Turkcell".equals(nitzfunc.apply(Integer.valueOf(subId), numeric, Boolean.valueOf(desireLongName))) && (subInfo = SubscriptionManager.getDefault().getSubscriptionInfoForSlot(phoneId)) != null && subInfo.isEmbedded()) {
                return "Turkcell";
            }
        } catch (IllegalStateException e) {
        } catch (Exception e2) {
        }
        return null;
    }

    public String handleCarrierNameOverride(String spnFromEons, int phoneId, IccRecords r) {
        SubscriptionInfo subInfo;
        String spnFromEons2 = HyperOSCustFeatureResolve.getStringForPhoneId(phoneId, "config_carrier_name_override", spnFromEons);
        if (r != null && "Turk Telekom".equals(spnFromEons2) && "28601".equals(r.getOperatorNumeric()) && r.getHomePlmns() != null && ArrayUtils.contains(r.getHomePlmns(), "28603") && (subInfo = SubscriptionManager.getDefault().getSubscriptionInfoForSlot(phoneId)) != null && subInfo.isEmbedded()) {
            spnFromEons2 = "Turkcell";
        }
        MiuiUiccCommonUtils.setSpnFromEons(spnFromEons2, r);
        return spnFromEons2;
    }

    private static boolean isDuringCall() {
        Phone[] phones = PhoneFactory.getPhones();
        for (Phone phone : phones) {
            if (phone.getState() != PhoneConstants.State.IDLE) {
                return true;
            }
        }
        return false;
    }

    public int getShowMiuiLevel(int miuiLevel) {
        if (this.mHandler.hasMessages(EVENT_DELAY_NTNPHONE_SIGNAL_UPDATE)) {
            int currentShowLevel = this.mCurrentShowSignalStrength.getMiuiLevel();
            log("need delay update , show currentShowLevel = " + currentShowLevel);
            return currentShowLevel;
        }
        return miuiLevel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSupportNitzTimeZonePreferred() {
        boolean isDeviceSupport = FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_NITZ_TIMEZONE_PREFERRED);
        boolean isDisableByCloudControler = TelephonyOptCloudController.CLOUD_CONTROLLER_NITZ_TIMEZONE_PREFERRED_DISABLE;
        boolean isEnabelFeatureInDevelopment = Settings.Global.getInt(this.mPhone.getContext().getContentResolver(), "enable_nitz_timezone_preferred", 1) == 1;
        log("isSupportNitzTimeZonePreferred, isDeviceSupport = " + isDeviceSupport + ", isDisableByCloudControler = " + isDisableByCloudControler + ", isEnabelFeatureInDevelopment = " + isEnabelFeatureInDevelopment);
        return isDeviceSupport && !isDisableByCloudControler && isEnabelFeatureInDevelopment;
    }

    @Override // miui.telephony.TelephonyOptCloudController.CloudInfoListener
    public void onCloudInfoChange() {
        boolean change = this.mIsCloudNitzTimeZonePreferredDisableFeature != TelephonyOptCloudController.CLOUD_CONTROLLER_NITZ_TIMEZONE_PREFERRED_DISABLE;
        log("cloud info change, nitz optimization cloud info change:" + change + ", mIsCloudNitzTimeZonePreferredDisableFeature = " + this.mIsCloudNitzTimeZonePreferredDisableFeature + ", mSupportNitzTimeZonePreferred = " + this.mSupportNitzTimeZonePreferred);
        if (change) {
            this.mIsCloudNitzTimeZonePreferredDisableFeature = TelephonyOptCloudController.CLOUD_CONTROLLER_NITZ_TIMEZONE_PREFERRED_DISABLE;
            if (this.mIsCloudNitzTimeZonePreferredDisableFeature) {
                this.mSupportNitzTimeZonePreferred = false;
                if (this.mTimeZoneOptimization != null) {
                    this.mTimeZoneOptimization.stopNitzTimeZonePreferred();
                    return;
                }
                return;
            }
            this.mSupportNitzTimeZonePreferred = isSupportNitzTimeZonePreferred();
        }
    }

    public TelephonyTimeZoneSuggestion updateTimeZoneSuggesionFromNetworkCountryCode(TelephonyTimeZoneSuggestion suggestion, String countryIso) {
        if (this.mSupportNitzTimeZonePreferred && this.mTimeZoneOptimization != null) {
            return this.mTimeZoneOptimization.updateTimeZoneSuggesionFromNetworkCountryCode(suggestion, countryIso);
        }
        return suggestion;
    }

    public TelephonyTimeZoneSuggestion updateTimeZoneSuggesionFromCountryAndNitz(TimeZoneSuggesterImpl timeZoneSuggesterImpl, TelephonyTimeZoneSuggestion suggestion, NitzSignal nitzSignal, String countryIso) {
        if (this.mSupportNitzTimeZonePreferred && this.mTimeZoneOptimization != null) {
            return this.mTimeZoneOptimization.updateTimeZoneSuggesionFromCountryAndNitz(timeZoneSuggesterImpl, suggestion, nitzSignal, countryIso);
        }
        return suggestion;
    }

    public void notifyNitzTimeChange(NitzData nitzData) {
        if (this.mSupportNitzTimeZonePreferred && this.mTimeZoneOptimization != null) {
            this.mTimeZoneOptimization.notifyNitzTimeChange(nitzData);
        }
    }

    public void updateModemResetFlag(boolean isModemResetting) {
        this.mIsModemResetting = isModemResetting;
        SystemProperties.set("radio.modem.modemresetting", String.valueOf(isModemResetting));
    }

    public boolean isAlwaysSetNitzName(String operatorNumeric) {
        List<String> supportList = HyperOSCustFeatureResolve.getStringArray("is_always_set_nitz_name_mtk");
        return (supportList == null || TextUtils.isEmpty(operatorNumeric) || !supportList.contains(operatorNumeric)) ? false : true;
    }

    public boolean isAllowGeolocationTimeZoneDetection(List<String> zoneIds, long effectiveFromElapsedMillis) {
        if (this.mSupportNitzTimeZonePreferred && this.mTimeZoneOptimization != null && isSupportLtzp()) {
            return this.mTimeZoneOptimization.isAllowGeolocationTimeZoneDetection(zoneIds, effectiveFromElapsedMillis);
        }
        return false;
    }

    public String findBestTZInMultiZones(String defaultZoneId, List<String> zoneIds, long effectiveFromElapsedMillis) {
        if (this.mSupportNitzTimeZonePreferred && this.mTimeZoneOptimization != null && isSupportLtzp()) {
            return this.mTimeZoneOptimization.findBestTZInMultiZones(defaultZoneId, zoneIds, effectiveFromElapsedMillis);
        }
        return defaultZoneId;
    }

    private boolean isSupportLtzp() {
        return SystemProperties.getBoolean("ro.telephony.phone_support_ltzp", false);
    }
}
