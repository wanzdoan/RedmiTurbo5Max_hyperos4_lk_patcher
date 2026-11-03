package com.android.internal.telephony.data;

import android.R;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.hardware.display.DisplayManager;
import android.net.ConnectivityManager;
import android.net.InterfaceConfiguration;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.NetworkAgentConfig;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Telephony;
import android.sysprop.TelephonyProperties;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.DataFailCause;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PreciseDataConnectionState;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.data.ApnSetting;
import android.telephony.data.DataCallResponse;
import android.telephony.data.DataProfile;
import android.telephony.data.TrafficDescriptor;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import com.android.internal.telephony.InternalTelephonyUtils;
import com.android.internal.telephony.MiuiIccPhoneBookInterfaceManager;
import com.android.internal.telephony.MiuiIccProvider;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.datasub.Constants;
import com.android.internal.telephony.datasub.SmartDualSimSwitch;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCallTracker;
import com.android.internal.telephony.ntndata.NetworkUtils;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.MiuiAdnRecord;
import com.android.internal.telephony.uicc.MiuiUiccControllerImpl;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.utils.MiEventUtils;
import com.android.internal.util.ArrayUtils;
import com.miui.misight.MiEvent;
import com.miui.misight.MiSight;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.modem.ModemUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.CommenUtils;
import miui.telephony.DefaultSimManager;
import miui.telephony.MiuiTelephony;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.SubscriptionInfo;
import miui.telephony.TelephonyManagerEx;
import miui.telephony.TelephonyStatAdapter;
import miui.telephony.VirtualSimUtils;

/* JADX INFO: loaded from: classes.dex */
public class MiuiDataControllerImpl implements MiuiDataControllerStub.IMiuiDataController {
    private static final String ACTION_FDN_CHANGE = "action_fdn_change";
    private static final int CLARO_DATA_STALL_ALARM_AGGRESSIVE_DELAY_IN_MS_DEFAULT = 2100000;
    private static final int CLARO_DATA_STALL_ALARM_NON_AGGRESSIVE_DELAY_IN_MS_DEFAULT = 2100000;
    private static final int DEFAULT_MTU = 1400;
    private static final int EVENT_APN_DATABASE_CHANGED = 10001;
    private static final int EVENT_CHECK_FDN_LIST = 3000;
    public static final int EVENT_DATA_NETWORK_ATTACHED = 10002;
    public static final int EVENT_DATA_NETWORK_DETACHED = 10003;
    private static final int EVENT_DO_RECOVERY = 2;
    public static final int EVENT_FAKE_CONNECTION_DELAY = 10004;
    private static final int EVENT_INTERNET_CONNECTED = 10000;
    private static final int EVENT_REEVALUATE_EXISTING_DATA_NETWORKS = 16;
    public static final int EVENT_REEVALUATE_UNSATISFIED_NETWORK_REQUESTS = 5;
    private static final int EVENT_REGISTER_ALL_EVENTS = 19;
    private static final int EVENT_SERVICE_STATE_CHANGED = 10005;
    private static final int EVENT_SETUP_DATA_NO_RESPONSE_DELAY = 10006;
    private static final int EVENT_SIGNAL_STRENGTH_CHANGED = 10007;
    private static final int EVENT_SIM_STATE_CHANGED = 9;
    private static final int EVENT_VOICE_CALL_ENDED = 18;
    private static final int EVENT_WAIT_PHONE_BOOK_READY = 3001;
    private static final int FAKE_CONNECTION_DELAY_INTEVAL = 20000;
    private static final long FAKE_CONNECTION_VALIDATION_GRACE_PERIOD_MS = 20000;
    private static final String FDN_FOR_ALLOW_DATA = "*99#";
    private static final String KDDI_MCC_MNC = "44051,44054";
    private static final String LOG_TAG = "MiuiDataControllerImpl";
    private static final String LTE_FLAG = "lte";
    private static final int LTE_RSRP_WEAK_THRESHOLD = -120;
    private static final int LTE_SNR_WEAK_THRESHOLD = 0;
    private static final int MAX_COUNT_FOR_ROAMING_SETUP_FAILED = 2;
    private static final int MAX_FAKE_CONNECTION_COUNT = 2;
    private static final int MAX_WAITING_TIMES = 6;
    private static final String NR_FLAG = "nr";
    private static final int NR_RSRP_WEAK_THRESHOLD = -115;
    private static final int NR_SNR_WEAK_THRESHOLD = 0;
    private static final String SATELLITE_DATA_ENABLED = "satellite_data_enabled";
    private static final int SATELLITE_DATA_OFF = 0;
    private static final int SATELLITE_DATA_ON = 1;
    private static final String VDF_ES_NUMERIC = "21401";
    private boolean isSatelliteModeTearDown;
    private boolean isSatelliteNetworkMode;
    private List<DataProfile> mAllDataProfiles;
    private List<DataNetwork> mDataNetworkList;
    private DataProfileManager mDataProfileManager;
    private DataStallRecoveryManager mDataStallRecoveryManager;
    private String mInterfaceName;
    private boolean mIsMmsRequestActive;
    private Phone mPhone;
    private ServiceState mServiceState;
    private TelephonyManager mTelephonyManager;
    private Handler mWorkerHandler;
    public static final boolean IS_TELEFONICA = miui.telephony.TelephonyManager.isCustForEsTelefonica();
    private static final String CUSTOMIZED_REGION = SystemProperties.get("ro.miui.customized.region", "");
    protected static AlertDialog mDataRejectDialog = null;
    private static boolean mUnsolDataCallError = false;
    private static boolean mSetupDataCallNoResponse = false;
    private final String PROPERTY_TEMP_DDSSWITCH = "persist.vendor.radio.enable_temp_dds";
    private ApnSetting mRecoveryApn = null;
    private ArrayList<ApnSetting> mValidationFailedApnList = new ArrayList<>();
    private int mValidFailCount = 0;
    private boolean mHasApnListRestored = false;
    private boolean mConfigReady = false;
    private int mSimState = 0;
    private DataProfile mPreferredDataProfile = null;
    private boolean mIsNeedRecoveryApn = true;
    private DataProfile mDefaultProfileExist = null;
    private HashMap<Integer, Long> mSetupDataNetworkStartTime = new HashMap<>();
    private volatile boolean mSkipNextPsCheck = false;
    private int mRoamingSetupFailedCount = 0;
    private boolean mRoamingFailedOptimizationNeeded = false;
    private boolean isNtnServiceStateInService = false;
    private TelephonyCallBack mTelephonyCallBack = null;
    private boolean isConfigCarrierSpecific = false;
    private boolean mIsScreenOn = false;
    private boolean mIsWifiConnected = false;
    private volatile long mLastInternetDataConnectedTime = 0;
    private volatile long mLastWifiStateChangedTime = 0;
    private boolean mIsFakeConnectionTest = false;
    private boolean mIsSetupFailTest = false;
    private int mLteRsrp = Integer.MAX_VALUE;
    private int mLteSnr = Integer.MAX_VALUE;
    private int mNrRsrp = Integer.MAX_VALUE;
    private int mNrSnr = Integer.MAX_VALUE;
    private boolean mIsSignalPoor = false;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (MiuiDataControllerImpl.this.mPhone == null) {
                return;
            }
            String action = intent.getAction();
            boolean z = true;
            if (action.equals(Processor.ACTION_FAKE_CONNECTION_TEST)) {
                int subId = MiuiDataControllerImpl.this.mPhone.getSubId();
                boolean isValid = intent.getBooleanExtra("isValid", false);
                MiuiDataControllerImpl.this.mIsFakeConnectionTest = intent.getBooleanExtra("mIsFakeConnectionTest", true);
                MiuiDataControllerImpl.this.log("updateFakeConnectionInfo test subId=" + subId + "isValid=" + isValid + "activeSubId=" + SubscriptionManager.getActiveDataSubscriptionId());
                if (SubscriptionManager.isValidSubscriptionId(subId) && subId == SubscriptionManager.getActiveDataSubscriptionId()) {
                    Processor.updateFakeConnectionInfo(MiuiDataControllerImpl.this.mPhone, isValid);
                    return;
                }
                return;
            }
            if (action.equals(Processor.ACTION_NOTIFY_FAKE_CONNECTION_TEST)) {
                int subId2 = MiuiDataControllerImpl.this.mPhone.getSubId();
                boolean isValid2 = intent.getBooleanExtra("isValid", false);
                MiuiDataControllerImpl.this.log("updateFakeConnectionInfo test subId=" + subId2 + "isValid=" + isValid2 + "activeSubId=" + SubscriptionManager.getActiveDataSubscriptionId());
                if (SubscriptionManager.isValidSubscriptionId(subId2) && subId2 == SubscriptionManager.getActiveDataSubscriptionId()) {
                    MiuiDataControllerImpl miuiDataControllerImpl = MiuiDataControllerImpl.this;
                    if (!isValid2) {
                        miuiDataControllerImpl.log("onInternetValidationStatusChanged -> notifyDataStatistic");
                        TelephonyStatAdapter.notifyDataStatistic(TelephonyStatAdapter.DataStatType.FAKE_CONNECTION_CN, MiuiDataControllerImpl.this.mPhone.getPhoneId(), null);
                        return;
                    } else {
                        miuiDataControllerImpl.log("onInternetValidationStatusChanged: DATA_CONNECTION_OK_CN");
                        TelephonyStatAdapter.notifyDataStatistic(TelephonyStatAdapter.DataStatType.DATA_CONNECTION_OK_CN, MiuiDataControllerImpl.this.mPhone.getPhoneId(), null);
                        return;
                    }
                }
                return;
            }
            if (action.equals(Processor.ACTION_SETUP_DATA_FAIL_TEST)) {
                int subId3 = MiuiDataControllerImpl.this.mPhone.getSubId();
                int cause = intent.getIntExtra("cause", 0);
                boolean isSetupSuccess = intent.getBooleanExtra("isSetupSuccess", false);
                MiuiDataControllerImpl.this.mIsSetupFailTest = intent.getBooleanExtra("mIsSetupFailTest", true);
                MiuiDataControllerImpl.mUnsolDataCallError = intent.getBooleanExtra("mUnsolDataCallError", false);
                MiuiDataControllerImpl.mSetupDataCallNoResponse = intent.getBooleanExtra("mSetupDataCallNoResponse", false);
                MiuiDataControllerImpl.this.log("updateSetupFailedInfo test subId=" + subId3 + "isSetupSuccess=" + isSetupSuccess + "cause=" + cause + "activeSubId=" + SubscriptionManager.getActiveDataSubscriptionId());
                if (SubscriptionManager.isValidSubscriptionId(subId3) && subId3 == SubscriptionManager.getActiveDataSubscriptionId()) {
                    DataNetwork dataNetwork = MiuiDataControllerImpl.this.findActiveInternetDataNetwork();
                    Processor.updateSetupFailedInfo(MiuiDataControllerImpl.this.mPhone, dataNetwork, isSetupSuccess, cause);
                    return;
                }
                return;
            }
            if (action.equals(Processor.ACTION_WECHAT_VOIP_SCENE) || action.equals(Processor.ACTION_MEETING_VOIP_SCENE)) {
                boolean isWechatVoip = intent.getBooleanExtra(Processor.WECHAT_VOIP_ENABLED, false);
                boolean isMeetingVoip = intent.getBooleanExtra(Processor.MEETING_VOIP_ENABLED, false);
                if (!isWechatVoip && !isMeetingVoip) {
                    z = false;
                }
                Processor.mIsVoipForgroud = z;
                return;
            }
            if (action.equals(Processor.ACTION_GAME_CHANGE_LEVEL)) {
                long level_ul = intent.getLongExtra("Level_UL", 1L);
                long level_dl = intent.getLongExtra("Level_DL", 1L);
                if (level_ul == 2 && level_dl == 2) {
                    Processor.mIsGameForgroud = true;
                    return;
                } else {
                    Processor.mIsGameForgroud = false;
                    return;
                }
            }
            if (action.equals("com.android.phone.intent.action.NETWORK_SPEED_DATA_STALL")) {
                boolean zShouldIgnoreDataStallForDsdsViceCall = Processor.shouldIgnoreDataStallForDsdsViceCall();
                MiuiDataControllerImpl miuiDataControllerImpl2 = MiuiDataControllerImpl.this;
                if (zShouldIgnoreDataStallForDsdsViceCall) {
                    miuiDataControllerImpl2.log("ignore network speed data stall for DSDS vice call");
                    return;
                }
                miuiDataControllerImpl2.log("updateFakeConnectionInfo isDataStall");
                Processor.mLastDataStallTime = System.currentTimeMillis();
                Processor.mIsDataStall = true;
                return;
            }
            if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                MiuiDataControllerImpl.this.updateWifiStateInfo(intent);
                return;
            }
            if (action.equals(MiuiDataControllerImpl.ACTION_FDN_CHANGE)) {
                int slotId = intent.getIntExtra(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, -1);
                MiuiDataControllerImpl.this.log("onFdnChanged called by action_fdn_change CB for slotId=" + slotId);
                MiuiDataControllerImpl.this.onFdnChanged();
            } else if (action.equals("android.telephony.action.NETWORK_COUNTRY_CHANGED")) {
                MiuiDataControllerImpl.this.resetRoamingSetupInfo();
            }
        }
    };
    private final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl.3
        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int displayId) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int displayId) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int displayId) {
            boolean isScreenOn;
            if (displayId != 0 || (isScreenOn = MiuiDataControllerImpl.this.isScreenOn()) == MiuiDataControllerImpl.this.mIsScreenOn) {
                return;
            }
            MiuiDataControllerImpl.this.log("onDisplayChanged isScreenOn=" + isScreenOn + ",mIsScreenOn=" + MiuiDataControllerImpl.this.mIsScreenOn);
            MiuiDataControllerImpl.this.mIsScreenOn = isScreenOn;
            if (!MiuiDataControllerImpl.this.mIsScreenOn && MiuiTelephony.IS_XRING && MiuiDataControllerImpl.this.isMeetModemResetForFakeConnection() && MiuiDataControllerImpl.this.isMeetCommonOptForFakeConnection()) {
                Processor.doRecoveryForFakeConnection(MiuiDataControllerImpl.this.mPhone, MiuiDataControllerImpl.this.mDataNetworkController);
            }
        }
    };
    private DataNetworkController mDataNetworkController;
    ContentObserver mDomesticRoamingSettingObserver = new ContentObserver(this.mDataNetworkController) { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl.4
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            for (Phone phone : PhoneFactory.getPhones()) {
                if (phone.getServiceState().getDataRoamingType() == 2 && phone.getSubId() == SubscriptionManager.getDefaultDataSubscriptionId()) {
                    boolean isDomesticRoamingEnable = miui.telephony.TelephonyManager.isDomesticRoamingEnable(phone.getContext());
                    MiuiDataControllerImpl.this.log("isDomesticRoamingEnable = " + isDomesticRoamingEnable);
                    MiuiDataControllerImpl.this.onDataRoamingEnabledChanged(isDomesticRoamingEnable);
                }
            }
        }
    };
    private boolean mIsFdnChecked = false;
    private boolean mIsMatchFdnForAllowData = false;
    private int mWaitingCount = 0;

    public void init(Phone phone, List<DataNetwork> dataNwList, Object... arg) {
        this.mPhone = phone;
        this.mDataNetworkList = dataNwList;
        this.mServiceState = new ServiceState();
        if (arg.length > 1) {
            if (arg[0] instanceof DataNetworkController) {
                this.mDataNetworkController = (DataNetworkController) arg[0];
            }
            if (arg[1] instanceof DataStallRecoveryManager) {
                this.mDataStallRecoveryManager = (DataStallRecoveryManager) arg[1];
            }
        }
        if (this.mDataNetworkController != null) {
            this.mDataProfileManager = this.mDataNetworkController.getDataProfileManager();
        }
        try {
            this.mAllDataProfiles = getAllDataProfiles();
        } catch (Exception e) {
            loge("Can not get mAllDataProfiles");
        }
        registerAllEvents();
        String prop = SystemProperties.get("radio.dataroaming.enable.suffix.subid");
        if (TextUtils.isEmpty(prop)) {
            try {
                SystemProperties.set("radio.dataroaming.enable.suffix.subid", "true");
            } catch (RuntimeException e2) {
                Processor.log(phone, "Failed to set radio.dataroaming.enable.suffix.subid to true");
            }
        }
        this.mTelephonyManager = TelephonyManager.from(phone.getContext());
        createWorkerHandler();
        MiuiTelephony.getInstance().registerTelephonyPlugins(this, phone.getPhoneId());
        if (this.mDataProfileManager != null) {
            DataProfileManager dataProfileManager = this.mDataProfileManager;
            final DataNetworkController dataNetworkController = this.mDataNetworkController;
            Objects.requireNonNull(dataNetworkController);
            dataProfileManager.registerCallback(new AnonymousClass2(new Executor() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$$ExternalSyntheticLambda10
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    dataNetworkController.post(runnable);
                }
            }));
        }
        if (MiuiTelephony.IS_XRING) {
            this.mIsScreenOn = isScreenOn();
        }
        this.mIsWifiConnected = isWifiConnect(this.mPhone);
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.data.MiuiDataControllerImpl$2, reason: invalid class name */
    class AnonymousClass2 extends DataProfileManager.DataProfileManagerCallback {
        AnonymousClass2(Executor arg0) {
            super(arg0);
        }

        public void onDataProfilesChanged() {
            MiuiDataControllerImpl.this.isConfigCarrierSpecific = MiuiDataControllerImpl.this.mDataNetworkController.getDataConfigManager().isConfigCarrierSpecific();
            MiuiDataControllerImpl.this.log("isConfigCarrierSpecific = " + MiuiDataControllerImpl.this.isConfigCarrierSpecific);
            if (MiuiDataControllerImpl.this.mAllDataProfiles != null) {
                DataProfile defaultProfileExist = (DataProfile) MiuiDataControllerImpl.this.mAllDataProfiles.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$2$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return ((DataProfile) obj).canSatisfy(DataUtils.apnTypeToNetworkCapability(17));
                    }
                }).findFirst().orElse(null);
                MiuiDataControllerImpl.this.log("onDataProfilesChanged defaultProfileExist = " + defaultProfileExist + "， mDefaultProfileExist = " + MiuiDataControllerImpl.this.mDefaultProfileExist);
                MiuiDataControllerImpl miuiDataControllerImpl = MiuiDataControllerImpl.this;
                if (defaultProfileExist == null) {
                    if (miuiDataControllerImpl.mDefaultProfileExist == null && MiuiDataControllerImpl.this.isConfigCarrierSpecific) {
                        MiuiDataControllerImpl.this.mIsNeedRecoveryApn = true;
                        return;
                    }
                    return;
                }
                miuiDataControllerImpl.mDefaultProfileExist = defaultProfileExist;
                MiuiDataControllerImpl.this.mIsNeedRecoveryApn = false;
            }
        }
    }

    public void dispose(Phone phone) {
        unregisterAllEvents();
        this.mIsFdnChecked = false;
        this.mWaitingCount = 0;
        this.mIsMatchFdnForAllowData = false;
        if (this.mWorkerHandler != null) {
            Looper looper = this.mWorkerHandler.getLooper();
            looper.quit();
        }
        MiuiTelephony.getInstance().unregisterTelephonyPlugins(this, phone.getPhoneId());
        this.mPhone = null;
    }

    private void registerTelephonyCallBack() {
        if (this.mTelephonyCallBack == null) {
            log("new  TelephonyCallBack");
            this.mTelephonyCallBack = new TelephonyCallBack();
            TelephonyStatAdapter.registerCallBack(this.mTelephonyCallBack);
        }
    }

    private void unregisterTelephonyCallBack() {
        if (this.mTelephonyCallBack != null) {
            TelephonyStatAdapter.unregisterCallBack(this.mTelephonyCallBack);
            this.mTelephonyCallBack = null;
        }
    }

    class TelephonyCallBack implements TelephonyStatAdapter.ICallBack {
        TelephonyCallBack() {
        }

        @Override // miui.telephony.TelephonyStatAdapter.ICallBack
        public void onDataStatisticChanged(TelephonyStatAdapter.DataStatType dataStatType, int phoneId, Map<String, String> params) {
        }

        @Override // miui.telephony.TelephonyStatAdapter.ICallBack
        public void onDataConnectionChanged(int phoneId, int dataState) {
        }

        @Override // miui.telephony.TelephonyStatAdapter.ICallBack
        public void onSignalStrengthChanged(int phoneId, SignalStrength signal) {
            MiuiDataControllerImpl.this.mWorkerHandler.obtainMessage(MiuiDataControllerImpl.EVENT_SIGNAL_STRENGTH_CHANGED, phoneId, 0, signal).sendToTarget();
        }

        @Override // miui.telephony.TelephonyStatAdapter.ICallBack
        public void onServiceStateChanged(int phoneId, ServiceState ss) {
            MiuiDataControllerImpl.this.mWorkerHandler.obtainMessage(MiuiDataControllerImpl.EVENT_SERVICE_STATE_CHANGED, phoneId, 0, ss).sendToTarget();
        }

        @Override // miui.telephony.TelephonyStatAdapter.ICallBack
        public void onCellLocationChanged(int phoneId, CellLocation cellLocation) {
        }

        @Override // miui.telephony.TelephonyStatAdapter.ICallBack
        public void onPreciseCallStateChanged(int phoneId, int foregroundCallState) {
        }
    }

    private void registerAllEvents() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_FDN_CHANGE);
        filter.addAction(Processor.ACTION_FAKE_CONNECTION_TEST);
        filter.addAction(Processor.ACTION_NOTIFY_FAKE_CONNECTION_TEST);
        filter.addAction(Processor.ACTION_SETUP_DATA_FAIL_TEST);
        filter.addAction(Processor.ACTION_WECHAT_VOIP_SCENE);
        filter.addAction(Processor.ACTION_MEETING_VOIP_SCENE);
        filter.addAction(Processor.ACTION_GAME_CHANGE_LEVEL);
        filter.addAction("com.android.phone.intent.action.NETWORK_SPEED_DATA_STALL");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.telephony.action.NETWORK_COUNTRY_CHANGED");
        this.mPhone.getContext().registerReceiver(this.mIntentReceiver, filter, null, this.mPhone, 2);
        this.mPhone.getContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor("data_domestic_roaming"), false, this.mDomesticRoamingSettingObserver);
        if (MiuiTelephony.IS_XRING) {
            DisplayManager dm = (DisplayManager) this.mPhone.getContext().getSystemService("display");
            dm.registerDisplayListener(this.mDisplayListener, null);
        }
        registerTelephonyCallBack();
    }

    private void unregisterAllEvents() {
        this.mPhone.getContext().unregisterReceiver(this.mIntentReceiver);
        this.mPhone.getContext().getContentResolver().unregisterContentObserver(this.mDomesticRoamingSettingObserver);
        this.mDomesticRoamingSettingObserver = null;
        if (MiuiTelephony.IS_XRING) {
            DisplayManager dm = (DisplayManager) this.mPhone.getContext().getSystemService("display");
            dm.unregisterDisplayListener(this.mDisplayListener);
        }
        unregisterTelephonyCallBack();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isScreenOn() {
        DisplayManager dm = (DisplayManager) this.mPhone.getContext().getSystemService("display");
        Display[] displays = dm.getDisplays();
        if (displays != null) {
            for (Display display : displays) {
                if (display.getState() == 2) {
                    return true;
                }
            }
            return false;
        }
        log("No displays found");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMeetModemResetForFakeConnection() {
        if (!this.mIsScreenOn && !Processor.mIsNetworkValid && Processor.mIsDataStall && Processor.FakeDcOptScene.LPM_RECOVERY == Processor.mFakeDcOptScene) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMeetCommonOptForFakeConnection() {
        return (Processor.mIsNetworkValid || this.mIsWifiConnected || Processor.isOnCall() || PhoneConstants.State.IDLE != this.mPhone.getState() || Processor.isLingerTimeForModemRestart() || this.mDataNetworkController == null || !this.mDataNetworkController.isInternetDataAllowed(false) || SystemProperties.getInt("ril.multisim.dualdata_status", 0) == 1 || !SubscriptionManager.isValidSubscriptionId(this.mPhone.getSubId()) || SubscriptionManager.getActiveDataSubscriptionId() != this.mPhone.getSubId() || !isLteNrRat(this.mPhone) || this.mDataNetworkList == null || !this.mDataNetworkList.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return MiuiDataControllerImpl.lambda$isMeetCommonOptForFakeConnection$0((DataNetwork) obj);
            }
        })) ? false : true;
    }

    static /* synthetic */ boolean lambda$isMeetCommonOptForFakeConnection$0(DataNetwork dataNetwork) {
        return dataNetwork.isInternetSupported() && dataNetwork.isConnected();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDataRoamingEnabledChanged(boolean enabled) {
        int i;
        if (this.mDataNetworkController != null) {
            DataNetworkController dataNetworkController = this.mDataNetworkController;
            DataNetworkController dataNetworkController2 = this.mDataNetworkController;
            if (enabled) {
                i = 5;
            } else {
                i = 16;
            }
            dataNetworkController.sendMessage(dataNetworkController2.obtainMessage(i, DataEvaluation.DataEvaluationReason.ROAMING_ENABLED_CHANGED));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String s) {
        Rlog.d("MiuiDataControllerImpl[" + this.mPhone.getPhoneId() + "]", s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String s) {
        Rlog.e("MiuiDataControllerImpl[" + this.mPhone.getPhoneId() + "]", s);
    }

    private void markInternetDataConnected() {
        this.mLastInternetDataConnectedTime = System.currentTimeMillis();
    }

    private void markWifiStateChanged() {
        this.mLastWifiStateChangedTime = System.currentTimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getFakeConnectionValidationGraceRemainingTime() {
        long curTime = System.currentTimeMillis();
        long remainingTime = this.mLastInternetDataConnectedTime != 0 ? Math.max(0L, FAKE_CONNECTION_VALIDATION_GRACE_PERIOD_MS - (curTime - this.mLastInternetDataConnectedTime)) : 0L;
        if (this.mLastWifiStateChangedTime != 0) {
            remainingTime = Math.max(remainingTime, FAKE_CONNECTION_VALIDATION_GRACE_PERIOD_MS - (curTime - this.mLastWifiStateChangedTime));
        }
        return Math.max(0L, remainingTime);
    }

    private List<DataProfile> getAllDataProfiles() throws Exception {
        Field field = DataProfileManager.class.getDeclaredField("mAllDataProfiles");
        field.setAccessible(true);
        return (List) field.get(this.mDataProfileManager);
    }

    public boolean fillDnsByDefault(Phone phone, LinkProperties linkProperties) {
        String dns;
        String dnsv6;
        if (phone == null) {
            return false;
        }
        boolean isInCnRegion = InternalTelephonyUtils.isInCnRegion();
        if (isInCnRegion) {
            dns = "114.114.114.114";
        } else {
            dns = "8.8.8.8";
        }
        if (isInCnRegion) {
            dnsv6 = "240c::6666";
        } else {
            dnsv6 = "2001:4860:4860::8888";
        }
        try {
            InetAddress inetAddress = InetAddress.parseNumericAddress(dnsv6);
            if (!inetAddress.isAnyLocalAddress()) {
                linkProperties.addDnsServer(inetAddress);
            }
            InetAddress inetAddress2 = InetAddress.parseNumericAddress(dns);
            if (!inetAddress2.isAnyLocalAddress()) {
                linkProperties.addDnsServer(inetAddress2);
                return true;
            }
            return true;
        } catch (IllegalArgumentException e) {
            Processor.log(phone, "fillDnsByDefault, Error setting dns using " + dns + " or " + dnsv6);
            return true;
        }
    }

    public boolean isAllowedOnRoaming(Phone phone, boolean defVal) {
        return Processor.isDomesticRoamingEnable(phone) || Processor.isVirtualSeedSim(phone) || Processor.mmsAllowedOnRoaming(phone, this.mIsMmsRequestActive) || Processor.dataAllowedOnRoaming(phone) || defVal;
    }

    public void onSetupResponse(DataNetwork dataNetwork, int resultCode, DataCallResponse response) {
        log("onSetupResponse isSatelliteNetworkMode = " + this.isSatelliteNetworkMode + ", isSatelliteDataEnabled = " + isSatelliteDataEnabled());
        if (!this.isSatelliteNetworkMode || !isSatelliteDataEnabled()) {
            return;
        }
        try {
            this.mInterfaceName = response.getInterfaceName();
            log("onSetupResponse mInterfaceName=" + this.mInterfaceName);
            InterfaceConfiguration ifcg = NetworkUtils.getInterfaceConfig(this.mInterfaceName);
            if (ifcg != null && response.getAddresses().size() > 0) {
                for (LinkAddress la : response.getAddresses()) {
                    if (!la.getAddress().isAnyLocalAddress()) {
                        log("addr/pl=" + la.getAddress() + "/" + la.getPrefixLength());
                        ifcg.setLinkAddress(la);
                    }
                }
                ifcg.setInterfaceUp();
                NetworkUtils.setInterfaceConfig(this.mInterfaceName, ifcg);
                return;
            }
            loge("cfg is null or no address for ifname=" + this.mInterfaceName);
        } catch (Exception e) {
            loge("Unable to set interface: " + e);
        }
    }

    public void onTearDown(DataNetwork dataNetwork, int cause) {
        if (dataNetwork != null && dataNetwork.isInternetSupported() && cause == 4) {
            log("onTearDown: reset fake connection info for data disabled");
            Processor.resetFakeConnectionInfo(this.mDataNetworkController);
        }
        if (this.mInterfaceName != null && this.mInterfaceName.length() > 0) {
            NetworkUtils.setInterfaceDown(this.mInterfaceName);
            NetworkUtils.clearInterfaceAddresses(this.mInterfaceName);
            this.mInterfaceName = null;
            this.isSatelliteNetworkMode = false;
            this.isSatelliteModeTearDown = true;
        }
    }

    public DataEvaluation evaluateNetworkRequest(Phone phone, TelephonyNetworkRequest networkRequest, DataEvaluation evaluation) {
        if (isFdnEnabled()) {
            evaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_DISABLED);
        }
        if (this.mDataNetworkController != null) {
            if (networkRequest.hasCapability(0)) {
                this.mIsMmsRequestActive = true;
            } else {
                this.mIsMmsRequestActive = false;
            }
            evaluation = evaluateDataNetwork(phone, evaluation, networkRequest);
            boolean dataEabled = this.mDataNetworkController.getDataSettingsManager().isDataEnabled();
            if (networkRequest.hasCapability(0) && !dataEabled && !isAlwaysEnableMMS(phone, networkRequest) && !isGoogleMessage(phone)) {
                evaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_DISABLED);
            }
            if (isMmsDisabledOnWifiWithoutVoWifi(phone, networkRequest)) {
                evaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_DISABLED);
            }
            if (networkRequest.hasCapability(12) && TelephonyManagerEx.getDefault().getVoiceAndDataAllowed() && evaluation.contains(DataEvaluation.DataDisallowedReason.CONCURRENT_VOICE_DATA_NOT_ALLOWED)) {
                log("removeDataDisallowedReason CONCURRENT_VOICE_DATA_NOT_ALLOWED");
                evaluation.removeDataDisallowedReason(DataEvaluation.DataDisallowedReason.CONCURRENT_VOICE_DATA_NOT_ALLOWED);
            }
            if (evaluation.contains(DataEvaluation.DataDisallowedReason.NOT_IN_SERVICE) && this.mSkipNextPsCheck) {
                log("removeDataDisallowedReason NOT_IN_SERVICE");
                evaluation.removeDataDisallowedReason(DataEvaluation.DataDisallowedReason.NOT_IN_SERVICE);
                this.mSkipNextPsCheck = false;
            }
        }
        boolean dataEabled2 = isSatelliteNetworkRequest(networkRequest);
        if (dataEabled2) {
            log("evaluateNetworkRequest: evaluation = " + evaluation + ", isNtnServiceStateInService = " + this.isNtnServiceStateInService);
            if (this.isNtnServiceStateInService && evaluation.containsDisallowedReasons()) {
                evaluation.addDataAllowedReason(DataEvaluation.DataAllowedReason.NORMAL);
            }
            this.isSatelliteNetworkMode = true;
            this.isSatelliteModeTearDown = false;
        }
        return evaluation;
    }

    public boolean isSatelliteNetworkRequest(TelephonyNetworkRequest networkRequest) {
        return networkRequest.hasCapability(12) && networkRequest.hasTransport(10) && !networkRequest.hasTransport(0) && isSatelliteDataEnabled();
    }

    public void onRemoveNetworkRequest(TelephonyNetworkRequest networkRequest) {
        boolean isSatelliteNetworkRequest = isSatelliteNetworkRequest(networkRequest);
        int networkRequestState = networkRequest.getState();
        log("onRemoveNetworkRequest isSatelliteNetworkMode = " + this.isSatelliteNetworkMode + ", networkRequestState = " + networkRequestState);
        if (isSatelliteNetworkRequest && networkRequestState == 0) {
            this.isSatelliteNetworkMode = false;
        }
    }

    public boolean attachNetworkRequests(DataNetworkController.NetworkRequestList requestList) {
        TelephonyNetworkRequest networkRequest = (TelephonyNetworkRequest) requestList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return MiuiDataControllerImpl.lambda$attachNetworkRequests$1((TelephonyNetworkRequest) obj);
            }
        }).filter(new Predicate() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((TelephonyNetworkRequest) obj).hasTransport(10);
            }
        }).findAny().orElse(null);
        if (this.isSatelliteNetworkMode && networkRequest == null) {
            log("attachNetworkRequests return true");
            return true;
        }
        return false;
    }

    static /* synthetic */ boolean lambda$attachNetworkRequests$1(TelephonyNetworkRequest request) {
        return request.getState() == 0;
    }

    public boolean findSatisfiableNetworkRequests(DataNetworkController.NetworkRequestList requestList) {
        TelephonyNetworkRequest networkRequest = (TelephonyNetworkRequest) requestList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return MiuiDataControllerImpl.lambda$findSatisfiableNetworkRequests$3((TelephonyNetworkRequest) obj);
            }
        }).filter(new Predicate() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((TelephonyNetworkRequest) obj).hasTransport(10);
            }
        }).findAny().orElse(null);
        if (this.isSatelliteNetworkMode && networkRequest != null) {
            log("findSatisfiableNetworkRequests return true");
            return true;
        }
        return false;
    }

    static /* synthetic */ boolean lambda$findSatisfiableNetworkRequests$3(TelephonyNetworkRequest request) {
        return request.getState() == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onServiceStateChanged(ServiceState ss) {
        if (!this.mServiceState.equals(ss)) {
            log("onServiceStateChanged: changed to " + ss);
            int voiceRegState = ss.getVoiceRegState();
            int rilVoiceRadioTechnology = ss.getRilVoiceRadioTechnology();
            if (voiceRegState == 0 && 101 == rilVoiceRadioTechnology) {
                this.mDataNetworkController.sendMessage(this.mDataNetworkController.obtainMessage(5, DataEvaluation.DataEvaluationReason.DATA_SERVICE_STATE_CHANGED));
                this.isNtnServiceStateInService = true;
            } else {
                this.mDataNetworkController.sendMessage(this.mDataNetworkController.obtainMessage(16, DataEvaluation.DataEvaluationReason.DATA_SERVICE_STATE_CHANGED));
                this.isNtnServiceStateInService = false;
            }
            this.mServiceState = ss;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSignalStrengthChanged(int phoneId, SignalStrength signal) {
        if (Processor.isValidatePhoneId(phoneId) && signal != null) {
            updateSignalInfo(phoneId, signal);
        } else {
            log("onSignalStrengthChanged-invalid phone id!");
        }
    }

    private void updateSignalInfo(int phoneId, SignalStrength signal) {
        if (phoneId != this.mPhone.getPhoneId()) {
            log("updateSignalInfo not allowed, ignore");
            return;
        }
        List<CellSignalStrength> cellSignalStrengths = signal.getCellSignalStrengths();
        String flag = "";
        for (CellSignalStrength cellSignal : cellSignalStrengths) {
            if (cellSignal instanceof CellSignalStrengthLte) {
                CellSignalStrengthLte cellLteSignal = (CellSignalStrengthLte) cellSignal;
                this.mLteRsrp = inRangeOrUnavailable(cellLteSignal.getRsrp(), -140, -43);
                this.mLteSnr = inRangeOrUnavailable(cellLteSignal.getRssnr(), -23, 40);
                log("updateSignalInfo lteRsrp=" + this.mLteRsrp + ",lteSnr=" + this.mLteSnr);
                flag = LTE_FLAG;
            } else if (cellSignal instanceof CellSignalStrengthNr) {
                CellSignalStrengthNr cellNrSignal = (CellSignalStrengthNr) cellSignal;
                this.mNrRsrp = inRangeOrUnavailable(cellNrSignal.getSsRsrp(), -140, -44);
                this.mNrSnr = inRangeOrUnavailable(cellNrSignal.getSsSinr(), -23, 40);
                log("updateSignalInfo nrRsrp= " + this.mNrRsrp + ",nrSnr= " + this.mNrSnr);
                flag = NR_FLAG;
            }
            if ((flag.equals(LTE_FLAG) && isLteRat(phoneId)) || (flag.equals(NR_FLAG) && isNrRat(phoneId))) {
                if ((this.mLteRsrp < LTE_RSRP_WEAK_THRESHOLD && this.mLteSnr < 0) || (this.mNrRsrp < NR_RSRP_WEAK_THRESHOLD && this.mNrSnr < 0)) {
                    this.mIsSignalPoor = true;
                } else {
                    this.mIsSignalPoor = false;
                }
            } else {
                this.mIsSignalPoor = false;
            }
        }
    }

    private int inRangeOrUnavailable(int value, int rangeMin, int rangeMax) {
        if (value < rangeMin || value > rangeMax) {
            return Integer.MAX_VALUE;
        }
        return value;
    }

    private boolean isMmsDisabledOnWifiWithoutVoWifi(Phone phone, TelephonyNetworkRequest networkRequest) {
        ImsPhone imsPhone;
        if (networkRequest.hasCapability(0) && (imsPhone = phone.getImsPhone()) != null) {
            ImsPhoneCallTracker tracker = imsPhone.getCallTracker();
            boolean isVowifiEnabled = tracker.isVowifiEnabled();
            boolean airplaneModeOn = Settings.System.getInt(phone.getContext().getContentResolver(), "airplane_mode_on", 0) == 1;
            boolean isDisabledMms = HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "config_disable_mms_without_vowifi", false);
            log("isMmsDisabledOnWifiWithoutVoWifi: isWifiConnect = " + this.mIsWifiConnected + ", isVowifiEnabled = " + isVowifiEnabled + ", airplaneModeOn = " + airplaneModeOn);
            if (isDisabledMms && airplaneModeOn && this.mIsWifiConnected && !isVowifiEnabled) {
                return true;
            }
        }
        return false;
    }

    private static boolean isWifiConnect(Phone phone) {
        Context context = phone.getContext();
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo info = mConnectivityManager.getNetworkInfo(1);
        if (info != null && info.isConnected()) {
            Rlog.d(LOG_TAG, "wifi is connected.");
            return true;
        }
        return false;
    }

    public DataEvaluation evaluateDataNetwork(Phone phone, DataEvaluation evaluation) {
        return evaluateDataNetwork(phone, evaluation, null);
    }

    private DataEvaluation evaluateDataNetwork(Phone phone, DataEvaluation evaluation, TelephonyNetworkRequest networkRequest) {
        if (isFdnEnabled()) {
            evaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_DISABLED);
        }
        if (this.mDataNetworkController != null) {
            evaluation.removeDataDisallowedReason(DataEvaluation.DataDisallowedReason.ROAMING_DISABLED);
            boolean isImsRequest = networkRequest != null && networkRequest.hasCapability(4);
            if (!isImsRequest) {
                if (phone.getServiceState().getDataRoaming() && Processor.disAllowedOnRoaming(phone, this.mIsMmsRequestActive)) {
                    evaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.ROAMING_DISABLED);
                }
            } else {
                log("IMS request: exempt ROAMING_DISABLED and DATA_DISABLED");
                evaluation.removeDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_DISABLED);
            }
        }
        boolean isImsRequest2 = this.isSatelliteNetworkMode;
        if (isImsRequest2 && this.isSatelliteModeTearDown) {
            log("evaluateDataNetwork: evaluation = " + evaluation + ", isNtnServiceStateInService = " + this.isNtnServiceStateInService + ", isSatelliteDataEnabled = " + isSatelliteDataEnabled());
            if (this.isNtnServiceStateInService && evaluation.containsDisallowedReasons()) {
                evaluation.addDataAllowedReason(DataEvaluation.DataAllowedReason.NORMAL);
                if (!isSatelliteDataEnabled()) {
                    evaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_DISABLED);
                } else if (!this.isNtnServiceStateInService) {
                    evaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.NOT_IN_SERVICE);
                }
            }
        }
        return evaluation;
    }

    private boolean isSatelliteDataEnabled() {
        return TelephonyManagerEx.getDefault().isSatelliteDataEnabled();
    }

    public void showDataUsageDialogCustomized(Phone phone, DataProfile dataProfile) {
        Processor.showDataUsageDialogCustomized(phone, dataProfile);
    }

    public ApnSetting getDataProfileForNetworkRequest(Phone phone, List<DataProfile> dataProfiles, ApnSetting apnSetting, TelephonyNetworkRequest networkRequest) {
        if (apnSetting != null && !this.mHasApnListRestored && networkRequest.hasCapability(12)) {
            this.mHasApnListRestored = true;
            log("can load apn, ignore restore apn opt");
        }
        if (this.mIsNeedRecoveryApn && this.mConfigReady && this.isConfigCarrierSpecific && apnSetting == null && !this.mHasApnListRestored && networkRequest.hasCapability(12)) {
            Processor.restoreDefaultApnsIfNeeded(phone, apnSetting, networkRequest);
            this.mHasApnListRestored = true;
            this.mIsNeedRecoveryApn = false;
        }
        if (isSatelliteNetworkRequest(networkRequest)) {
            log("isSatelliteNetworkRequest: true");
            new ArrayList();
            for (DataProfile dataProfile : dataProfiles) {
                if (dataProfile.getApnSetting().isForInfrastructure(2) && !dataProfile.getApnSetting().isForInfrastructure(1)) {
                    apnSetting = dataProfile.getApnSetting();
                    log("apnSetting = " + apnSetting);
                }
            }
        }
        return apnSetting;
    }

    public void removeWapApn(List<DataProfile> dataProfiles, DataProfile preferredDataProfile, TelephonyNetworkRequest networkRequest) {
    }

    public void filterSpecialDunApn(Phone phone, List<DataProfile> dataProfiles, DataProfile preferredDataProfile, TelephonyNetworkRequest networkRequest) {
        Processor.filterSpecialDunApn(phone, dataProfiles, preferredDataProfile, networkRequest);
    }

    public void addIAApnSetting(Phone phone, String operator, List<DataProfile> dataProfiles) {
        Processor.addIAApnSetting(operator, dataProfiles);
    }

    public boolean setInitialAttachApn(Phone phone, DataServiceManager dsMgr, DataProfile dataProfile) {
        return Processor.maybeSkipOrSetEmptyInitialAttachApn(phone, dsMgr, dataProfile);
    }

    public boolean ignoreTetheringDataProfile(Phone phone) {
        return Processor.ignoreTetheringDataProfile(phone);
    }

    public boolean ignoreDataSetupRetry(Phone phone, DataProfile dataProfile, int cause) {
        return Processor.ignoreDataSetupRetry(phone, dataProfile, cause);
    }

    public void updateDataConfig(Phone phone) {
        if (phone == null) {
            return;
        }
        if (this.mSimState == 10) {
            this.mConfigReady = true;
        } else if (this.mSimState == 1) {
            this.mDefaultProfileExist = null;
            this.mConfigReady = false;
            this.mRecoveryApn = null;
            this.mPreferredDataProfile = null;
        }
        if (this.mConfigReady) {
            this.mRecoveryApn = null;
            String operator = phone.getOperatorNumeric();
            if (!TextUtils.isEmpty(operator) && Processor.shouldInitRecoveryApn(phone)) {
                try {
                    this.mRecoveryApn = Processor.getPreferenceApnSetting(phone, operator);
                } catch (Exception e) {
                    Processor.log(phone, "initRecoveryApn Exception:" + e);
                }
            }
        }
    }

    public DataProfile setupDataNetwork(Phone phone, DataProfile dataProfile) {
        if (dataProfile != null && dataProfile.getApnSetting() != null) {
            int apnTypeBitmask = dataProfile.getApnSetting().getApnTypeBitmask();
            this.mSetupDataNetworkStartTime.put(Integer.valueOf(apnTypeBitmask), Long.valueOf(System.currentTimeMillis()));
            if ((apnTypeBitmask & 1) == 1) {
                this.mDataNetworkController.sendMessageDelayed(this.mDataNetworkController.obtainMessage(EVENT_SETUP_DATA_NO_RESPONSE_DELAY, Integer.valueOf(apnTypeBitmask)), 30000L);
                TelephonyStatAdapter.notifyDataStatistic(TelephonyStatAdapter.DataStatType.DATA_SETUP_REQUEST_CN, phone.getPhoneId(), null);
            }
        }
        if (Processor.isRoamingSetupFailedOptimization(phone)) {
            return updateRoamingDataProfileIfNeed(phone, dataProfile);
        }
        return dataProfile;
    }

    public DataProfile updateRoamingDataProfileIfNeed(Phone phone, DataProfile dataProfile) {
        if (dataProfile == null || dataProfile.getApnSetting() == null || !dataProfile.equals(Processor.mOptimizationDataProfile) || this.mRoamingSetupFailedCount >= 2) {
            return dataProfile;
        }
        int currentRoamingProtocolType = dataProfile.getRoamingProtocolType();
        if (Processor.mOptimizationRoamingProtocol != -1 && currentRoamingProtocolType != Processor.mOptimizationRoamingProtocol) {
            DataProfile dataProfile2 = buildDataProfile(dataProfile, Processor.mOptimizationRoamingProtocol);
            this.mRoamingFailedOptimizationNeeded = true;
            this.mRoamingSetupFailedCount++;
            return dataProfile2;
        }
        return dataProfile;
    }

    private DataProfile buildDataProfile(DataProfile dataProfile, int optimizationRoamingProtocol) {
        ApnSetting apn = dataProfile.getApnSetting();
        ApnSetting apnSetting = new ApnSetting.Builder().setId(apn.getId()).setOperatorNumeric(apn.getOperatorNumeric()).setEntryName(apn.getEntryName()).setApnName(apn.getApnName()).setProxyAddress(apn.getProxyAddressAsString()).setProxyPort(apn.getProxyPort()).setMmsc(apn.getMmsc()).setMmsProxyAddress(apn.getMmsProxyAddressAsString()).setMmsProxyPort(apn.getMmsProxyPort()).setUser(apn.getUser()).setPassword(apn.getPassword()).setAuthType(apn.getAuthType()).setApnTypeBitmask(apn.getApnTypeBitmask()).setProtocol(apn.getProtocol()).setRoamingProtocol(optimizationRoamingProtocol).setCarrierEnabled(apn.isEnabled()).setNetworkTypeBitmask(apn.getNetworkTypeBitmask()).setLingeringNetworkTypeBitmask(apn.getLingeringNetworkTypeBitmask()).setProfileId(apn.getProfileId()).setModemCognitive(apn.isPersistent()).setMaxConns(apn.getMaxConns()).setWaitTime(apn.getWaitTime()).setMaxConnsTime(apn.getMaxConnsTime()).setMtuV4(apn.getMtuV4()).setMtuV6(apn.getMtuV6()).setMvnoType(apn.getMvnoType()).setMvnoMatchData(apn.getMvnoMatchData()).setApnSetId(apn.getApnSetId()).setCarrierId(apn.getCarrierId()).setSkip464Xlat(apn.getSkip464Xlat()).setAlwaysOn(apn.isAlwaysOn()).buildWithoutCheck();
        return new DataProfile.Builder().setApnSetting(apnSetting).setTrafficDescriptor(dataProfile.getTrafficDescriptor()).setPreferred(dataProfile.isPreferred()).build();
    }

    public long onDataNetworkSetupFailed(Phone phone, DataNetwork dataNetwork, DataNetworkController.NetworkRequestList requestList, int cause, long retryDelayMillis) {
        long retryDelayMillis2;
        long duration;
        log("onDataNetworkSetupFailed: " + dataNetwork);
        boolean isSupportDataClearCode = HyperOSCustFeatureResolve.getBoolean("support_data_clear_code", false);
        if (isSupportDataClearCode && (cause == 29 || cause == 33)) {
            log("ClearCodeProcess cause=" + cause);
            displayPopup(cause, phone);
        }
        if (dataNetwork != null && dataNetwork.isInternetSupported() && !this.mIsSetupFailTest) {
            this.mValidFailCount = 0;
            Processor.updateSetupFailedInfo(phone, dataNetwork, false, cause);
            retryDelayMillis2 = Processor.getRetryTimerByDisconnectedCause(cause, retryDelayMillis);
            if (Processor.isRoamingSetupFailedOptimization(phone)) {
                Processor.updateSetupFailedRoamingInfo(phone, dataNetwork, cause);
            }
        } else {
            retryDelayMillis2 = retryDelayMillis;
        }
        if (dataNetwork != null && dataNetwork.getDataProfile() != null && dataNetwork.getDataProfile().getApnSetting() != null) {
            int bitmask = dataNetwork.getDataProfile().getApnSetting().getApnTypeBitmask();
            if (this.mSetupDataNetworkStartTime.containsKey(Integer.valueOf(bitmask))) {
                long duration2 = System.currentTimeMillis() - this.mSetupDataNetworkStartTime.get(Integer.valueOf(bitmask)).longValue();
                this.mSetupDataNetworkStartTime.remove(Integer.valueOf(bitmask));
                if (this.mDataNetworkController != null && this.mDataNetworkController.hasMessages(EVENT_SETUP_DATA_NO_RESPONSE_DELAY)) {
                    this.mDataNetworkController.removeMessages(EVENT_SETUP_DATA_NO_RESPONSE_DELAY);
                }
                duration = duration2;
            } else {
                duration = -1;
            }
            TelephonyStatAdapter.notifyDataSetupAction(dataNetwork.getDataProfile().getApnSetting(), true, DataFailCause.toString(cause), phone.getPhoneId(), duration);
        }
        Intent intent = new Intent("android.telephony.action.CARRIER_SIGNAL_REQUEST_NETWORK_FAILED");
        intent.putExtra("android.telephony.extra.DATA_FAIL_CAUSE", cause);
        if (dataNetwork != null && dataNetwork.getDataProfile() != null) {
            intent.putExtra("android.telephony.extra.APN_TYPE", dataNetwork.getDataProfile().getSupportedApnTypesBitmask());
        }
        phone.getCarrierSignalAgent().notifyCarrierSignalReceivers(intent);
        log("send to the specific carrier signaling receivers for setup failed..");
        return retryDelayMillis2;
    }

    public long onDataEvaluationFailed(Phone phone, TelephonyNetworkRequest networkRequest, DataEvaluation evaluation, DataEvaluation.DataEvaluationReason reason) {
        if (Build.IS_INTERNATIONAL_BUILD) {
            log("onDataEvaluationFailed return as INTERNATIONA BUILD");
            return -1L;
        }
        if (networkRequest.hasCapability(12) && evaluation.containsDisallowedReasons()) {
            TelephonyStatAdapter.notifyDataEvaluationFailed(networkRequest, evaluation, phone.getPhoneId(), reason);
        }
        return -1L;
    }

    public void onDataNetworkConnected(Phone phone, DataNetwork dataNetwork) {
        long duration;
        DataProfile dp;
        log("onDataNetworkConnected: " + dataNetwork);
        if (dataNetwork != null && dataNetwork.isInternetSupported() && !this.mIsSetupFailTest) {
            markInternetDataConnected();
            this.mValidFailCount = 0;
            Processor.updateSetupFailedInfo(phone, dataNetwork, true, 0);
            if (((this.mPreferredDataProfile == null && getPreferredDataProfileSetId() == 0) || !this.mPreferredDataProfile.canSatisfy(DataUtils.apnTypeToNetworkCapability(17))) && (dp = dataNetwork.getDataProfile()) != null) {
                setPreferredDataProfile(dp);
                this.mPreferredDataProfile = dp;
            }
            setTelefonicaPreferredApn(dataNetwork.getDataProfile());
            this.mDataNetworkController.sendMessage(this.mDataNetworkController.obtainMessage(EVENT_INTERNET_CONNECTED, dataNetwork));
            if (this.mRoamingFailedOptimizationNeeded && this.mRoamingSetupFailedCount <= 2) {
                recordRoamingRecoveryEvents(phone);
            }
            resetRoamingSetupInfo();
        }
        if (dataNetwork != null && dataNetwork.getDataProfile() != null && dataNetwork.getDataProfile().getApnSetting() != null) {
            int bitmask = dataNetwork.getDataProfile().getApnSetting().getApnTypeBitmask();
            if (!this.mSetupDataNetworkStartTime.containsKey(Integer.valueOf(bitmask))) {
                duration = -1;
            } else {
                long duration2 = System.currentTimeMillis() - this.mSetupDataNetworkStartTime.get(Integer.valueOf(bitmask)).longValue();
                this.mSetupDataNetworkStartTime.remove(Integer.valueOf(bitmask));
                if (this.mDataNetworkController != null && this.mDataNetworkController.hasMessages(EVENT_SETUP_DATA_NO_RESPONSE_DELAY)) {
                    this.mDataNetworkController.removeMessages(EVENT_SETUP_DATA_NO_RESPONSE_DELAY);
                }
                duration = duration2;
            }
            TelephonyStatAdapter.notifyDataSetupAction(dataNetwork.getDataProfile().getApnSetting(), false, DataFailCause.toString(0), phone.getPhoneId(), duration);
        }
    }

    public static void recordRoamingRecoveryEvents(Phone phone) {
        String operatorNumeric = phone.getOperatorNumeric();
        String networkOperatorNumeric = getOperatorNumericFromPs(phone);
        if (Processor.mOptimizationRoamingProtocol != -1 || !TextUtils.isEmpty(operatorNumeric) || !TextUtils.isEmpty(networkOperatorNumeric)) {
            Map<String, String> parmers = new HashMap<>();
            parmers.put("operatorNumeric", operatorNumeric);
            parmers.put("networkOperatorNumeric", networkOperatorNumeric);
            parmers.put("optimizationRoamingProtocol", ApnSetting.getProtocolStringFromInt(Processor.mOptimizationRoamingProtocol));
            TelephonyStatAdapter.notifyDataStatistic(TelephonyStatAdapter.DataStatType.DATA_APN_RECOVERY_CN, phone.getPhoneId(), parmers);
        }
    }

    public static String getOperatorNumericFromPs(Phone phone) {
        NetworkRegistrationInfo wwanPsRegState;
        ServiceStateTracker sst = phone.getServiceStateTracker();
        if (sst.getServiceState().getDataRegistrationState() != 0 || (wwanPsRegState = sst.getServiceState().getNetworkRegistrationInfo(2, 1)) == null) {
            return null;
        }
        String networkOperatorNumeric = wwanPsRegState.getRegisteredPlmn();
        return networkOperatorNumeric;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetRoamingSetupInfo() {
        this.mRoamingFailedOptimizationNeeded = false;
        this.mRoamingSetupFailedCount = 0;
        Processor.mOptimizationRoamingProtocol = -1;
    }

    public void setTelefonicaPreferredApn(DataProfile dataProfile) {
        if (IS_TELEFONICA && dataProfile.getApnSetting() != null) {
            int pos = dataProfile.getApnSetting().getId();
            String spn = TelephonyManagerEx.getDefault().getSimOperatorNameForSlot(this.mPhone.getPhoneId());
            String operator = this.mPhone.getOperatorNumeric();
            log("setTelefonicaPreferredApn: spn = " + spn + ", operator = " + operator + ", pos = " + pos);
            if ("21407".equals(operator)) {
                if ("Particular".equals(spn) || "Empresa".equals(spn)) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.mPhone.getContext());
                    sp.edit().putInt(spn, pos).commit();
                }
            }
        }
    }

    public void recoveryTelefonicaPreferredApn() {
        if (!IS_TELEFONICA) {
            return;
        }
        String spn = TelephonyManagerEx.getDefault().getSimOperatorNameForSlot(this.mPhone.getPhoneId());
        String operator = this.mPhone.getOperatorNumeric();
        log("recoveryTelefonicaPreferredApn: spn = " + spn + " , operator = " + operator);
        if ("21407".equals(operator)) {
            if ("Particular".equals(spn) || "Empresa".equals(spn)) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.mPhone.getContext());
                int preApnSelected = sp.getInt(spn, -1);
                log("recoveryTelefonicaPreferredApn: preApnSelected=" + preApnSelected);
                if (preApnSelected > 0) {
                    setPreferredDataProfile(preApnSelected);
                }
            }
        }
    }

    public void onDataNetworkDisconnected(Phone phone, DataNetwork dataNetwork, int cause) {
        log("onDataNetworkDisconnected: " + dataNetwork);
        if (dataNetwork != null && dataNetwork.isInternetSupported() && !this.mIsSetupFailTest) {
            log("no DataConnection for defaut apncontext exists");
            if (cause == 2112) {
                mUnsolDataCallError = true;
                Processor.updateSetupFailedInfo(phone, dataNetwork, false, cause);
            }
        }
        recordDataInterruptionCause(phone, dataNetwork, cause);
    }

    public long getRetryDelayMillis(long delayMillis, int tearDownReason) {
        if (tearDownReason != 12) {
            return delayMillis;
        }
        return 100L;
    }

    public void recordDataInterruptionCause(Phone phone, DataNetwork dataNetwork, int cause) {
        if (dataNetwork != null && dataNetwork.getDataProfile() != null && dataNetwork.getDataProfile().getApnSetting() != null) {
            ApnSetting apn = dataNetwork.getDataProfile().getApnSetting();
            Map<String, String> params = new HashMap<>();
            params.put(TelephonyStatAdapter.APN_NAME_KEY, apn != null ? apn.getApnName() : ModemUtils.SIM_UNKNOWN);
            if (apn != null) {
                params.put("mccmnc", apn.getOperatorNumeric());
                params.put(TelephonyStatAdapter.APN_TYPE_KEY, ApnSetting.getApnTypesStringFromBitmask(apn.getApnTypeBitmask()));
                params.put("protocol", ApnSetting.getProtocolStringFromInt(apn.getProtocol()));
                params.put("roamingProtocol", ApnSetting.getProtocolStringFromInt(apn.getRoamingProtocol()));
            }
            params.put("dataInterruptionCause", DataFailCause.toString(cause));
            TelephonyStatAdapter.notifyDataStatistic(TelephonyStatAdapter.DataStatType.DATA_INTERRUPTION_CAUSE, phone.getPhoneId(), params);
        }
    }

    private void displayPopup(int pdpRejectCause, Phone phone) {
        log("displayPopup pdpRejectCause=" + pdpRejectCause);
        String message = null;
        if (pdpRejectCause == 29) {
            message = phone.getContext().getResources().getString(286196699);
        } else if (pdpRejectCause == 33) {
            message = phone.getContext().getResources().getString(286196553);
        }
        if (mDataRejectDialog == null || !mDataRejectDialog.isShowing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(phone.getContext());
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.ok, (DialogInterface.OnClickListener) null);
            mDataRejectDialog = builder.create();
        }
        mDataRejectDialog.setMessage(message);
        mDataRejectDialog.setCanceledOnTouchOutside(false);
        mDataRejectDialog.getWindow().setType(2003);
        mDataRejectDialog.show();
    }

    public void onInternetValidationStatusChanged(Phone phone, DataStallRecoveryManager dsrMgr, int status) {
        boolean isValid = status == 1;
        if (this.mIsFakeConnectionTest) {
            return;
        }
        Processor.updateFakeConnectionInfo(phone, isValid);
        if (!isValid) {
            log("onInternetValidationStatusChanged -> notifyDataStatistic");
            TelephonyStatAdapter.notifyDataStatistic(TelephonyStatAdapter.DataStatType.FAKE_CONNECTION_CN, this.mPhone.getPhoneId(), null);
        } else {
            TelephonyStatAdapter.notifyDataStatistic(TelephonyStatAdapter.DataStatType.DATA_CONNECTION_OK_CN, this.mPhone.getPhoneId(), null);
            log("onInternetValidationStatusChanged: DATA_CONNECTION_OK_CN");
        }
    }

    public void onDataNetworkValidationStatusChanged(Phone phone, DataNetwork dataNetwork, int status, Uri redirectUri) {
        if (!TextUtils.isEmpty(redirectUri.toString()) && status != 1) {
            Processor.notifyArrearsSimIfNeed(phone, redirectUri.toString());
        }
    }

    public void handleMessage(Phone phone, Message msg) {
        switch (msg.what) {
            case 9:
                this.mSimState = msg.arg1;
                if (this.mSimState == 10) {
                    updateDataConfig(this.mPhone);
                    recoveryTelefonicaPreferredApn();
                } else if (this.mSimState == 1) {
                    updateDataConfig(this.mPhone);
                    resetRoamingSetupInfo();
                }
                break;
            case 18:
            case 270343:
                onVoiceCallChange(phone);
                break;
            case 19:
                onRegisterAllEvents();
                break;
            case EVENT_INTERNET_CONNECTED /* 10000 */:
                DataNetwork network = (DataNetwork) msg.obj;
                if (network != null) {
                    PreciseDataConnectionState pds = new PreciseDataConnectionState.Builder().setTransportType(network.getTransport()).setState(2).setApnSetting(network.getDataProfile().getApnSetting()).setLinkProperties(network.getLinkProperties()).setNetworkType(getDataNetworkType(network.getTransport())).setFailCause(0).build();
                    phone.notifyDataConnection(pds);
                    break;
                }
                break;
            case EVENT_APN_DATABASE_CHANGED /* 10001 */:
                updatePreferredDataProfile();
                break;
            case EVENT_DATA_NETWORK_ATTACHED /* 10002 */:
                log("EVENT_DATA_NETWORK_ATTACHED ");
                this.mSkipNextPsCheck = true;
                this.mDataNetworkController.sendMessage(this.mDataNetworkController.obtainMessage(5, DataEvaluation.DataEvaluationReason.DATA_SERVICE_STATE_CHANGED));
                break;
            case EVENT_DATA_NETWORK_DETACHED /* 10003 */:
                log("EVENT_DATA_NETWORK_DETACHED ");
                this.mSkipNextPsCheck = false;
                break;
            case EVENT_FAKE_CONNECTION_DELAY /* 10004 */:
                if (Processor.FakeDcOptScene.LPM_RECOVERY != Processor.mFakeDcOptScene && isMeetCommonOptForFakeConnection()) {
                    log("EVENT_FAKE_CONNECTION_DELAY ");
                    Processor.doRecoveryForFakeConnection(phone, this.mDataNetworkController);
                    this.mDataNetworkController.sendMessageDelayed(this.mDataNetworkController.obtainMessage(EVENT_FAKE_CONNECTION_DELAY), FAKE_CONNECTION_VALIDATION_GRACE_PERIOD_MS);
                    break;
                }
                break;
            case EVENT_SETUP_DATA_NO_RESPONSE_DELAY /* 10006 */:
                int apnTypeBitmask = ((Integer) msg.obj).intValue();
                if (this.mSetupDataNetworkStartTime.containsKey(Integer.valueOf(apnTypeBitmask)) && !this.mIsSetupFailTest) {
                    log("EVENT_SETUP_DATA_NO_RESPONSE_DELAY ");
                    mSetupDataCallNoResponse = true;
                    DataNetwork dataNetwork = findConnectingInternetDataNetwork();
                    Processor.updateSetupFailedInfo(phone, dataNetwork, false, -1);
                    break;
                }
                break;
        }
    }

    public void updatePreferredDataProfile() {
        DataProfile dataProfile;
        DataProfile preferredDataProfile = getPreferredDataProfileFromDb();
        if (preferredDataProfile == null) {
            DataNetwork curInternetNetwork = findActiveInternetDataNetwork();
            DataProfile dataProfile2 = getInternetDataProfile(curInternetNetwork);
            if (dataProfile2 != null) {
                setPreferredDataProfile(dataProfile2);
            }
            this.mDataNetworkController.sendMessage(this.mDataNetworkController.obtainMessage(EVENT_INTERNET_CONNECTED, curInternetNetwork));
        } else if (preferredDataProfile != null && !preferredDataProfile.canSatisfy(12) && this.mDataNetworkController != null && this.mDataNetworkController.getInternetDataNetworkState() == 2 && (dataProfile = getInternetDataProfile(findActiveInternetDataNetwork())) != null) {
            log("updatePreferredDataProfile: preferredDB is Non-Internet DP, setting preferred to be the connected internet DP");
            setPreferredDataProfile(dataProfile);
            if (this.mPhone != null) {
                Intent intent = new Intent("miui.intent.action.ACTION_PREFERRED_APN_CHANGE");
                this.mPhone.getContext().sendBroadcast(intent);
            }
        }
        this.mPreferredDataProfile = preferredDataProfile;
    }

    private DataProfile getInternetDataProfile(final DataNetwork internetNetwork) {
        if (internetNetwork == null || !this.mDataProfileManager.isDataProfileCompatible(internetNetwork.getDataProfile())) {
            return null;
        }
        DataProfile dataProfile = this.mAllDataProfiles.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$getInternetDataProfile$5(internetNetwork, (DataProfile) obj);
            }
        }).findFirst().orElse(null);
        return dataProfile;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getInternetDataProfile$5(DataNetwork internetNetwork, DataProfile dp) {
        return dp.getApnSetting() != null && dp.getApnSetting().equals(internetNetwork.getDataProfile().getApnSetting(), this.mPhone.getServiceState().getDataRoamingFromRegistration());
    }

    private void onRegisterAllEvents() {
        if (this.mDataNetworkController != null) {
            if (!MiuiTelephony.IS_MTK) {
                this.mPhone.getCallTracker().registerForVoiceCallStarted(this.mDataNetworkController, 270343, (Object) null);
                if (this.mPhone.getImsPhone() != null) {
                    this.mPhone.getImsPhone().getCallTracker().registerForVoiceCallStarted(this.mDataNetworkController, 270343, (Object) null);
                }
            }
            this.mPhone.getContext().getContentResolver().registerContentObserver(Telephony.Carriers.CONTENT_URI, true, new ContentObserver(this.mDataNetworkController) { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl.5
                @Override // android.database.ContentObserver
                public void onChange(boolean selfChange) {
                    super.onChange(selfChange);
                    int tfTime = MiuiDataControllerImpl.IS_TELEFONICA ? 50 : 0;
                    MiuiDataControllerImpl.this.mDataNetworkController.sendEmptyMessageDelayed(MiuiDataControllerImpl.EVENT_APN_DATABASE_CHANGED, tfTime);
                }
            });
        }
    }

    private int getMtuForJpKddi(Phone phone, InetAddress gateway) {
        int ret = -1;
        boolean isJpKddi = "jp_kd".equals(SystemProperties.get("ro.miui.customized.region"));
        if (isJpKddi && phone != null && TextUtils.equals(phone.getOperatorNumeric(), "44054")) {
            ret = gateway instanceof Inet4Address ? 1420 : 1440;
        }
        log("getMtuForJpKddi ret=" + ret);
        return ret;
    }

    private boolean isJpSoftBank(Phone phone) {
        String mccmnc;
        return (!"jp_sb".equals(SystemProperties.get("ro.miui.customized.region", "")) || this.mTelephonyManager == null || phone == null || (mccmnc = this.mTelephonyManager.getSimOperatorNumericForPhone(phone.getPhoneId())) == null || !"44020".equals(mccmnc)) ? false : true;
    }

    public int updatePreferredMtu(Phone phone, InetAddress gateWay, int rilRat, int defVal) {
        if (phone == null) {
            return defVal;
        }
        if (this.isSatelliteNetworkMode) {
            return MiRilHook.HOOK_ID_START_URC_BY_AT;
        }
        int kddiMtu = getMtuForJpKddi(phone, gateWay);
        if (kddiMtu > 0) {
            return kddiMtu;
        }
        if (isJpSoftBank(phone)) {
            return 1358;
        }
        return (Build.IS_INTERNATIONAL_BUILD || !(gateWay instanceof Inet4Address) || this.mDataNetworkController == null || this.mDataNetworkController.getInternetDataNetworkState() != 2 || isLteNrRat(phone)) ? defVal : Math.min(defVal, DEFAULT_MTU);
    }

    public void updateLinkProperties(Phone phone, LinkProperties linkProperties) {
        if (isJpSoftBank(phone) && linkProperties != null) {
            log("updateLinkProperties mtu");
            linkProperties.setMtu(1358);
        }
    }

    public void updateNetworkCapabilities(Phone phone, NetworkCapabilities.Builder nwCapbuilder, DataNetwork dataNetwork) {
        DataNetworkController.NetworkRequestList networkRequestList = dataNetwork.getAttachedNetworkRequestList();
        boolean suplRequest = networkRequestList.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((TelephonyNetworkRequest) obj).hasCapability(1);
            }
        });
        boolean internetRequest = networkRequestList.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((TelephonyNetworkRequest) obj).hasCapability(12);
            }
        });
        if (!Build.IS_INTERNATIONAL_BUILD && suplRequest && !internetRequest && this.mDataNetworkController != null && this.mDataNetworkController.getInternetDataNetworkState() == 2 && nwCapbuilder.build().hasCapability(12) && (!phone.getDataSettingsManager().isDataEnabled() || phone.getSubId() != SubscriptionManager.getActiveDataSubscriptionId())) {
            log("updateNetworkCapabilities removeCapability INTERNET");
            nwCapbuilder.removeCapability(12);
        }
        if (!Build.IS_INTERNATIONAL_BUILD && isViceSlotRoaming(phone)) {
            boolean mmsRequest = networkRequestList.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$$ExternalSyntheticLambda7
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ((TelephonyNetworkRequest) obj).hasCapability(0);
                }
            });
            if (!mmsRequest && nwCapbuilder.build().hasCapability(12)) {
                log("Roaming updateNetworkCapabilities removeCapability INTERNET");
                nwCapbuilder.removeCapability(12);
            }
        }
        boolean isSatelliteRequest = isSatelliteRequest(dataNetwork.getAttachedNetworkRequestList());
        if (isSatelliteRequest) {
            nwCapbuilder.addTransportType(10);
            nwCapbuilder.removeTransportType(0);
        }
    }

    private boolean isSatelliteRequest(DataNetworkController.NetworkRequestList networkRequestList) {
        if (!isSatelliteDataEnabled()) {
            log("isSatelliteRequest: satellite data disabled");
            return false;
        }
        boolean satelliteRequest = networkRequestList.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return MiuiDataControllerImpl.lambda$isSatelliteRequest$9((TelephonyNetworkRequest) obj);
            }
        });
        log("isSatelliteRequest satelliteRequest = " + satelliteRequest);
        return satelliteRequest;
    }

    static /* synthetic */ boolean lambda$isSatelliteRequest$9(TelephonyNetworkRequest request) {
        return request.hasTransport(10) && request.hasCapability(12);
    }

    private boolean isViceSlotRoaming(Phone phone) {
        if (phone.getSubId() == SubscriptionManager.getDefaultDataSubscriptionId()) {
            return false;
        }
        boolean isRoaming = phone.getServiceState().getRoaming();
        return isRoaming;
    }

    public void createNetworkAgent(DataNetwork dataNetwork, NetworkAgentConfig.Builder configBuilder) {
        boolean isSatelliteRequest = isSatelliteRequest(dataNetwork.getAttachedNetworkRequestList());
        if (isSatelliteRequest) {
            log("createNetworkAgent: satellite request, set satellite config");
            configBuilder.setLegacyType(21);
            configBuilder.setLegacyTypeName("SATELLITE");
        }
    }

    public void onDetachNetworkRequest(DataNetwork dataNetwork) {
        if (this.isSatelliteNetworkMode && dataNetwork.isConnected()) {
            log("onDetachNetworkRequest: Safe to tear down " + dataNetwork);
            dataNetwork.tearDown(1);
        }
    }

    public boolean onDeactivateResponse(DataNetwork dataNetwork) {
        log("onDeactivateResponse isSatelliteModeTearDown = " + this.isSatelliteModeTearDown + ", dataNetwork = " + dataNetwork);
        if (this.isSatelliteModeTearDown && dataNetwork.isDisconnecting()) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWifiStateInfo(Intent intent) {
        boolean connected;
        NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        if (networkInfo == null) {
            return;
        }
        NetworkInfo.State state = networkInfo.getState();
        if (state != NetworkInfo.State.CONNECTED && state != NetworkInfo.State.DISCONNECTED) {
            return;
        }
        int networkType = networkInfo.getType();
        if (networkType != 1 || (connected = isWifiConnect(this.mPhone)) == this.mIsWifiConnected) {
            return;
        }
        log("updateWifiStateInfo: connected=" + connected + ",mIsWifiConnected=" + this.mIsWifiConnected);
        markWifiStateChanged();
        this.mIsWifiConnected = connected;
        if (Processor.mLastFakeDcOptTime != 0 && this.mIsWifiConnected && !Processor.mIsNetworkValid) {
            int fakeDcOptSceneValue = Processor.mFakeDcOptScene.getValue();
            long curActionTimeForFakeConnection = Processor.getCurActionTimeForFakeConnection();
            log("updateWifiStateInfo:" + fakeDcOptSceneValue + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + curActionTimeForFakeConnection);
            TelephonyStatAdapter.notifyFakeConnectionRecoveryTime(fakeDcOptSceneValue, curActionTimeForFakeConnection);
            Processor.resetFakeConnectionInfo();
        }
    }

    private boolean isFdnEnabled() {
        boolean bFdnEnabled = false;
        if (isFdnEnableSupport()) {
            if (Processor.isPhoneBookReady(this.mPhone)) {
                bFdnEnabled = this.mPhone.getIccCard().getIccFdnEnabled();
            } else {
                log("isFdnEnabled phoneBook is not ready yet...,should not setup date call.");
                bFdnEnabled = true;
            }
            if (bFdnEnabled) {
                if (this.mIsFdnChecked) {
                    log("isFdnEnabled(), match FDN for allow data = " + this.mIsMatchFdnForAllowData);
                    return !this.mIsMatchFdnForAllowData;
                }
                this.mWorkerHandler.sendEmptyMessage(EVENT_CHECK_FDN_LIST);
            }
        }
        log("isFdnEnabled = " + bFdnEnabled);
        return bFdnEnabled;
    }

    private boolean isFdnEnableSupport() {
        boolean isEnabled = HyperOSCustFeatureResolve.getBooleanForPhoneId(this.mPhone.getPhoneId(), "support_fdn_data_enable", false);
        log("isFdnEnableSupport isEnabled " + isEnabled);
        return isEnabled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onFdnChanged() {
        if (isFdnEnableSupport()) {
            log("onFdnChanged()");
            boolean bFdnEnabled = false;
            log("onFdnChanged isPhoneBookReady=" + Processor.isPhoneBookReady(this.mPhone));
            if (Processor.isPhoneBookReady(this.mPhone)) {
                if (this.mWorkerHandler.hasMessages(EVENT_WAIT_PHONE_BOOK_READY)) {
                    this.mWorkerHandler.removeMessages(EVENT_WAIT_PHONE_BOOK_READY);
                }
                log("onFdnChanged bFdnEnabled=" + this.mPhone.getIccCard().getIccFdnEnabled());
                bFdnEnabled = this.mPhone.getIccCard().getIccFdnEnabled();
                this.mWaitingCount = 0;
            } else if (this.mWaitingCount < 6) {
                log("onFdnChanged phoneBook is not ready yet...waiting for ready");
                if (this.mWorkerHandler.hasMessages(EVENT_WAIT_PHONE_BOOK_READY)) {
                    this.mWorkerHandler.removeMessages(EVENT_WAIT_PHONE_BOOK_READY);
                }
                StringBuilder sbAppend = new StringBuilder().append("onFdnChanged waiting for phoneBook ready mWaitingCount =");
                int i = this.mWaitingCount + 1;
                this.mWaitingCount = i;
                log(sbAppend.append(i).toString());
                this.mWorkerHandler.sendEmptyMessageDelayed(EVENT_WAIT_PHONE_BOOK_READY, 500L);
                return;
            }
            if (bFdnEnabled) {
                log("fdn enabled, check fdn list");
                this.mWorkerHandler.sendEmptyMessage(EVENT_CHECK_FDN_LIST);
                return;
            } else {
                log("fdn disabled, call setupDataOnAllConnectableApns()");
                this.mDataNetworkController.sendMessage(this.mDataNetworkController.obtainMessage(5, DataEvaluation.DataEvaluationReason.DATA_ENABLED_CHANGED));
                return;
            }
        }
        log("not support fdn enabled, skip onFdnChanged");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanOrSetupDataConnByCheckFdn() {
        log("cleanOrSetupDataConnByCheckFdn()");
        this.mIsMatchFdnForAllowData = false;
        if (Processor.hasSpecialFDN(this.mPhone, FDN_FOR_ALLOW_DATA)) {
            this.mIsMatchFdnForAllowData = true;
            this.mIsFdnChecked = true;
        }
        if (this.mPhone.getSubId() != SubscriptionManager.getDefaultDataSubscriptionId()) {
            log("No need to check dataconnection for nDDS.... ");
        } else if (this.mIsMatchFdnForAllowData) {
            log("match FDN for allow data ...");
            this.mDataNetworkController.sendMessage(this.mDataNetworkController.obtainMessage(5, DataEvaluation.DataEvaluationReason.DATA_ENABLED_CHANGED));
        } else {
            log("not match FDN for allow data ...");
            this.mDataNetworkController.sendMessage(this.mDataNetworkController.obtainMessage(16, DataEvaluation.DataEvaluationReason.DATA_ENABLED_CHANGED));
        }
    }

    private void createWorkerHandler() {
        if (this.mWorkerHandler == null) {
            Thread thread = new Thread() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl.6
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    Looper.prepare();
                    MiuiDataControllerImpl.this.mWorkerHandler = new WorkerHandler();
                    Looper.loop();
                }
            };
            thread.start();
        }
    }

    private class WorkerHandler extends Handler {
        private WorkerHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MiuiDataControllerImpl.EVENT_CHECK_FDN_LIST /* 3000 */:
                    MiuiDataControllerImpl.this.cleanOrSetupDataConnByCheckFdn();
                    break;
                case MiuiDataControllerImpl.EVENT_WAIT_PHONE_BOOK_READY /* 3001 */:
                    MiuiDataControllerImpl.this.onFdnChanged();
                    break;
                case MiuiDataControllerImpl.EVENT_SERVICE_STATE_CHANGED /* 10005 */:
                    MiuiDataControllerImpl.this.onServiceStateChanged((ServiceState) msg.obj);
                    break;
                case MiuiDataControllerImpl.EVENT_SIGNAL_STRENGTH_CHANGED /* 10007 */:
                    MiuiDataControllerImpl.this.onSignalStrengthChanged(msg.arg1, (SignalStrength) msg.obj);
                    break;
                default:
                    MiuiDataControllerImpl.this.loge("Unexpected event " + msg.what);
                    break;
            }
        }
    }

    private boolean isAlwaysEnableMMS(Phone phone, TelephonyNetworkRequest networkRequest) {
        return (phone == null || networkRequest == null || Settings.System.getInt(phone.getContext().getContentResolver(), Processor.ALWAYS_ENABLE_MMS, Processor.getDefaultAlwaysEnableMms(phone.getContext())) != 1) ? false : true;
    }

    private boolean isGoogleMessage(Phone phone) {
        return "com.google.android.apps.messaging".equals(Telephony.Sms.getDefaultSmsPackage(phone.getContext()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isLteNrRat(Phone phone) {
        if (phone == null || phone.getServiceState() == null) {
            return false;
        }
        int rilRat = phone.getServiceState().getDataNetworkType();
        return rilRat == 13 || rilRat == 19 || rilRat == 20 || rilRat == 30;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isLteRat(int phoneId) {
        Phone phone = PhoneFactory.getPhone(phoneId);
        if (phone == null || phone.getServiceState() == null) {
            return false;
        }
        int rilRat = phone.getServiceState().getDataNetworkType();
        return rilRat == 13 || rilRat == 19;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNrRat(int phoneId) {
        Phone phone = PhoneFactory.getPhone(phoneId);
        if (phone == null || phone.getServiceState() == null) {
            return false;
        }
        int rilRat = phone.getServiceState().getDataNetworkType();
        return rilRat == 20 || rilRat == 30;
    }

    public DataNetwork findActiveInternetDataNetwork() {
        if (this.mDataNetworkList.isEmpty()) {
            log("findInternetDataNetwork: No existing data networks to find.");
            return null;
        }
        for (DataNetwork dataNetwork : this.mDataNetworkList) {
            if (dataNetwork.isInternetSupported() && (dataNetwork.isConnected() || dataNetwork.isHandoverInProgress() || dataNetwork.isDisconnecting())) {
                return dataNetwork;
            }
        }
        return null;
    }

    public DataNetwork findConnectingInternetDataNetwork() {
        if (this.mDataNetworkList.isEmpty()) {
            log("findInternetDataNetwork: No existing data networks to find.");
            return null;
        }
        for (DataNetwork dataNetwork : this.mDataNetworkList) {
            if (dataNetwork.isInternetSupported() && dataNetwork.isConnecting()) {
                return dataNetwork;
            }
        }
        return null;
    }

    public DataNetwork getActiveDataNetworkByCapability(int nwCapability) {
        if (this.mDataNetworkList.isEmpty()) {
            log("getDataNetworkByCapability: No existing data networks to find.");
            return null;
        }
        for (DataNetwork dataNetwork : this.mDataNetworkList) {
            NetworkCapabilities netCapbilities = dataNetwork.getNetworkCapabilities();
            if (netCapbilities != null && netCapbilities.hasCapability(nwCapability) && (dataNetwork.isConnecting() || dataNetwork.isConnected() || dataNetwork.isHandoverInProgress() || dataNetwork.isDisconnecting())) {
                return dataNetwork;
            }
        }
        return null;
    }

    public boolean isRecoveryNotNeeded(Phone phone, boolean isNeedToCheckTimer, Object obj) {
        boolean isNotRecovery = false;
        if (isNeedToCheckTimer) {
            isNotRecovery = isSkipRecoveryApn(phone);
        }
        return isNotRecovery || !isCallStateIdle();
    }

    public boolean isRebootModem() {
        boolean isNotSupportRebootModem = isNotSupportRebootModem();
        log("isRebootModem: isNotSupportRebootModem = " + isNotSupportRebootModem);
        return !isNotSupportRebootModem;
    }

    private static boolean isNotSupportRebootModem() {
        return HyperOSCustFeatureResolve.getBoolean("config_not_support_reboot_modem", false);
    }

    private boolean isCallStateIdle() {
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone != null && phone.getState() != PhoneConstants.State.IDLE) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSkipRecoveryApn(Phone phone) {
        ApnSetting apnSetting;
        DataNetwork dataNw = findActiveInternetDataNetwork();
        if (dataNw == null || (apnSetting = dataNw.getDataProfile().getApnSetting()) == null) {
            return false;
        }
        List<String> skipRecoveryApns = HyperOSCustFeatureResolve.getStringArrayForPhoneId(phone.getPhoneId(), "skip_data_recovery_apns");
        boolean isMatched = ArrayUtils.contains(skipRecoveryApns, apnSetting.getApnName());
        boolean isSkipRecovery = false;
        if (isMatched && phone.getDataSettingsManager() != null && phone.getDataSettingsManager().isDataEnabled(17)) {
            isSkipRecovery = true;
        }
        log("isSkipRecovery=" + isSkipRecovery);
        return isSkipRecovery;
    }

    private void onVoiceCallChange(Phone voicePhone) {
        for (Phone phone : PhoneFactory.getPhones()) {
            if (phone != null && phone.getPhoneId() != voicePhone.getPhoneId()) {
                log("onVoiceCallChange,getPhoneId = " + phone.getPhoneId());
                phone.getDataNetworkController();
                MiuiDataControllerImpl miuiDataController = (MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, phone.getPhoneId());
                DataNetwork dataNetwork = miuiDataController.findActiveInternetDataNetwork();
                if (dataNetwork != null) {
                    phone.notifyDataConnection(new PreciseDataConnectionState.Builder().setTransportType(dataNetwork.getTransport()).setState(0).setApnSetting(dataNetwork.getDataProfile().getApnSetting()).setNetworkType(miuiDataController.getDataNetworkType(dataNetwork.getTransport())).setLinkProperties(dataNetwork.getLinkProperties()).build());
                }
            }
        }
    }

    private int getPreferredDataProfileSetId() {
        int setId;
        Cursor cursor = this.mPhone.getContext().getContentResolver().query(Uri.withAppendedPath(Telephony.Carriers.PREFERRED_APN_SET_URI, String.valueOf(this.mPhone.getSubId())), new String[]{"apn_set_id"}, null, null, null);
        if (cursor == null) {
            log("getPreferredDataProfileSetId: cursor is null");
            return 0;
        }
        if (cursor.getCount() < 1) {
            loge("getPreferredDataProfileSetId: no APNs found");
            setId = 0;
        } else {
            cursor.moveToFirst();
            setId = cursor.getInt(cursor.getColumnIndexOrThrow("apn_set_id"));
        }
        cursor.close();
        return setId;
    }

    private void setPreferredDataProfile(DataProfile dataProfile) {
        log("setPreferredDataProfile: " + dataProfile);
        String subId = Long.toString(this.mPhone.getSubId());
        Uri uri = Uri.withAppendedPath(Telephony.Carriers.PREFERRED_APN_URI, subId);
        ContentResolver resolver = this.mPhone.getContext().getContentResolver();
        resolver.delete(uri, null, null);
        if (dataProfile != null && dataProfile.getApnSetting() != null) {
            ContentValues values = new ContentValues();
            values.put("apn_id", Integer.valueOf(dataProfile.getApnSetting().getId()));
            resolver.insert(uri, values);
        }
    }

    private void setPreferredDataProfile(int pos) {
        log("setPreferredDataProfile pos: " + pos);
        String subId = Long.toString(this.mPhone.getSubId());
        Uri uri = Uri.withAppendedPath(Telephony.Carriers.PREFERRED_APN_URI, subId);
        ContentResolver resolver = this.mPhone.getContext().getContentResolver();
        resolver.delete(uri, null, null);
        ContentValues values = new ContentValues();
        values.put("apn_id", Integer.valueOf(pos));
        resolver.insert(uri, values);
    }

    private DataProfile getPreferredDataProfileFromDb() {
        try {
            this.mAllDataProfiles = getAllDataProfiles();
        } catch (Exception ex) {
            Rlog.e(LOG_TAG, "getPreferredDataProfileFromDb: refresh mAllDataProfiles failed: " + ex);
        }
        if (this.mAllDataProfiles == null || this.mAllDataProfiles.isEmpty()) {
            return null;
        }
        Cursor cursor = this.mPhone.getContext().getContentResolver().query(Uri.withAppendedPath(Telephony.Carriers.PREFERRED_APN_URI, String.valueOf(this.mPhone.getSubId())), null, null, null, "name ASC");
        DataProfile dataProfile = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                final int apnId = cursor.getInt(cursor.getColumnIndexOrThrow(MiuiIccProvider.STR_ID));
                dataProfile = this.mAllDataProfiles.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return MiuiDataControllerImpl.lambda$getPreferredDataProfileFromDb$10(apnId, (DataProfile) obj);
                    }
                }).findFirst().orElse(null);
            }
            cursor.close();
        }
        log("getPreferredDataProfileFromDb: " + dataProfile);
        return dataProfile;
    }

    static /* synthetic */ boolean lambda$getPreferredDataProfileFromDb$10(int apnId, DataProfile dp) {
        return dp.getApnSetting() != null && dp.getApnSetting().getId() == apnId;
    }

    public boolean isPreferDataApnUserEdited() {
        DataProfile dataProfile = getPreferredDataProfileFromDb();
        if (dataProfile == null) {
            return false;
        }
        int apnEditedStatus = dataProfile.getApnSetting().getEditedStatus();
        log("apnEditedStatus=" + apnEditedStatus);
        if (apnEditedStatus != 1) {
            return false;
        }
        return true;
    }

    public int getDataNetworkType(int transport) {
        NetworkRegistrationInfo nri;
        ServiceState ss = this.mPhone.getServiceState();
        if (ss == null || (nri = ss.getNetworkRegistrationInfo(2, transport)) == null) {
            return 0;
        }
        return nri.getAccessNetworkTechnology();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void trimCurrentTimeMillis(LinkedList<Long> list, int seconds) {
        Iterator<Long> iter = list.iterator();
        long now = System.currentTimeMillis();
        while (iter.hasNext()) {
            Long restartTimeMillis = iter.next();
            if (now - restartTimeMillis.longValue() > TimeUnit.SECONDS.toMillis(seconds)) {
                iter.remove();
            } else {
                return;
            }
        }
    }

    public static class Processor {
        private static final String ACTION_ARREARS_SIM_NOTIFICATION = "com.android.phone.intent.action.ARREARS_SIM_NOTIFICATION";
        public static final String ACTION_FAKE_CONNECTION_TEST = "com.android.phone.data.action.FAKE_CONNECTION_TEST";
        public static final String ACTION_GAME_CHANGE_LEVEL = "com.android.phone.intent.action.CHANGE_LEVEL";
        public static final String ACTION_MEETING_VOIP_SCENE = "com.android.phone.intent.action.MEETING_VOIP_SCENE";
        private static final String ACTION_NETWORK_SPEED_DATA_STALL = "com.android.phone.intent.action.NETWORK_SPEED_DATA_STALL";
        public static final String ACTION_NOTIFY_FAKE_CONNECTION_TEST = "com.android.phone.data.action.NOTIFY.FAKE_CONNECTION_TEST";
        public static final String ACTION_SETUP_DATA_FAIL_TEST = "com.android.phone.data.action.SETUP_DATA_FAIL_TEST";
        public static final String ACTION_WECHAT_VOIP_SCENE = "com.android.phone.intent.action.WECHAT_VOIP_SCENE";
        public static final String ALWAYS_ENABLE_MMS = "always_enable_mms";
        private static final String CHINA_TELECOM_OPERATOR_NUMERIC = "46003";
        private static final int CLEAR_DATA_NETWORK_FAIL = 3000008;
        private static final String CMNET_RECHARGE_PAGE = "10086.cn";
        private static final String CTNET_RECHARGE_PAGE = "189.cn";
        private static final int DATA_CONNECTIVITY_DIAG_INDEX_DNS = 3;
        private static final int DATA_CONNECTIVITY_DIAG_INDEX_LOOKBACK = 1;
        private static final int DATA_CONNECTIVITY_DIAG_INDEX_ORGIN = 0;
        private static final int DATA_CONNECTIVITY_DIAG_INDEX_PING = 2;
        private static final int DATA_CONNECTIVITY_DIAG_MIN_LENGTH = 4;
        private static final char DATA_CONNECTIVITY_DIAG_RESULT_OK = '1';
        private static final String DEVICE_FIRST_USING_DATA = "device_first_using_data";
        private static final int DSDA_STATUS_DIVISOR = 100;
        private static final int EVENT_DATA_CONFIG_UPDATED = 1;
        private static final String EXTRA_CMCC_WAP_APN_NAME = "cmwap";
        private static final String EXTRA_CT_APN_NAME = "ctnet";
        private static final String EXTRA_CU_WAP_APN_NAME = "3gwap";
        private static final String EXTRA_PROBE_ACK = "PROBE_ACK";
        private static final String FAKE_CONNECTION_TRIGGER_SCENE = "FAKE_CONNECTION";
        private static final long FAST_RETRY_AFTER_DISCONNECT_DELAY = 1000;
        private static final int FAST_SETUP_FAILED_COUNT_THRESHOLD = 1;
        private static final String FIELD_SLOT_ID = "slotId";
        private static final long GPRS_REGISTRATION_FAIL_RETRY_IN_MS_DEFAULT = 3000;
        private static final String KDDI_5G_NET_DEFAULT_APN_NAME = "uad5gn.au-net.ne.jp";
        private static final String KDDI_5G_NET_DUN_APN_NAME = "uad5gntrg.au-net.ne.jp";
        private static final String KDDI_5G_NET_FOR_DATA_DEFAULT_APN_NAME = "au5g.au-net.ne.jp";
        private static final String KDDI_5G_NET_FOR_DATA_DUN_APN_NAME = "au5gtrg.au-net.ne.jp";
        private static final String KEY_DATA_NETWORK_FAIL_CAUSE = "dataNetworkFailCause";
        private static final String KEY_IS_DELAY = "isDelay";
        private static final String KEY_RESULT = "result";
        private static final String KEY_TRIGGER_SCENE = "triggerScene";
        private static final String LATENCY_KEY_LEVEL_DL = "Level_DL";
        private static final String LATENCY_KEY_LEVEL_UL = "Level_UL";
        private static final long LEVEL1 = 1;
        private static final long LEVEL2 = 2;
        private static final int MAX_COUNT_FOR_ACCELERATE_MODE = 10;
        private static final long MAX_INTERVAL_TIME_FOR_ACCELERATE_MODE = 86400000;
        public static final int MAX_VALIDATION_COUNT = 5;
        public static final String MEETING_VOIP_ENABLED = "meetingVoipEnabled";
        private static final String NETWORK_INFO_REASON = "connected";
        private static final int NORMAL_SETUP_FAILED_COUNT_THRESHOLD = 3;
        private static final String NOTIFACATION_RECEIVER_PACKAGE = "com.android.phone";
        public static final String PREFERENCE_KEY_VERSION = "version";
        private static final String PREFERENCE_LAST_VALID_APN = "last_valid_apn";
        public static final int PROTOCOL_APN_ABNORMAL_EVENT_CODE = 915000003;
        public static final int PROTOCOL_APN_ABNORMAL_RECOVERY_EVENT_CODE = 815000003;
        public static final int PROTOCOL_SETUPDATACALL_ERROR_RECOVERY_EVENT_CODE = 815000002;
        public static final int PROTOCOL_SETUPDATACALL_NULL_RECOVERY_EVENT_CODE = 815000001;
        public static final int PROTOCOL_SETUPDATACALL_RETURN_ERROR_EVENT_CODE = 915000002;
        public static final int PROTOCOL_SETUPDATACALL_RETURN_NULL_EVENT_CODE = 915000001;
        public static final int RECOVERY_MODE_DELAY_IN_MS = 30000;
        public static final int RECOVERY_MODE_NUMBER_SENT_PACKETS_OF_HANG = 6;
        private static final String RECOVERY_RESULT = "false";
        private static final String REGION_CHINA_MCC = "460";
        private static final int REPAIR_DATA_NETWORK_FAIL = 3000007;
        public static final int SETUP_DATA_CALL_NO_RESPONSE = -1;
        private static final String SETUP_FAIL_TRIGGER_SCENE = "SETUP_DATA_FAIL";
        private static final String SMART_DUAL_SIM = "smart_dual_sim";
        public static final int STATE_DSDA = 3;
        private static final int STATE_DSDA_NOT_POSSIBLE = 4;
        private static final int STATE_DSDA_ONGOING = 2;
        private static final int STATE_DSDA_POSSIBLE = 3;
        private static final long TOP_RETRY_AFTER_DISCONNECT_DELAY = 2500;
        private static final int UNSET_MTU = 0;
        private static final int UNSPECIFIED_INT = -1;
        private static final String UNSPECIFIED_STRING = "";
        private static final String V2_FORMAT_REGEX = "^\\[ApnSettingV2\\]\\s*";
        private static final String V3_FORMAT_REGEX = "^\\[ApnSettingV3\\]\\s*";
        private static final String V4_FORMAT_REGEX = "^\\[ApnSettingV4\\]\\s*";
        private static final String V5_FORMAT_REGEX = "^\\[ApnSettingV5\\]\\s*";
        private static final String V6_FORMAT_REGEX = "^\\[ApnSettingV6\\]\\s*";
        private static final String V7_FORMAT_REGEX = "^\\[ApnSettingV7\\]\\s*";
        private static final String VODAFONE_OPERATOR_NUMERIC = "20404";
        public static final String WECHAT_VOIP_ENABLED = "wechatVoipEnabled";
        private static SmartDualSimSwitch mSmartDualSimSwitch;
        private static final String EXTRA_CU_APN_NAME = "3gnet";
        private static final String EXTRA_CMCC_APN_NAME = "cmnet";
        private static final List<String> GPRS_REGISTRATION_FAIL_RETRY_APN_LIST = Arrays.asList(EXTRA_CU_APN_NAME, EXTRA_CMCC_APN_NAME);
        public static boolean sIsInAccelerationMode = false;
        private static int sAccelerateModeCounter = 0;
        private static long sAccelerateModeInitialTime = 0;
        private static long sAccelerateModeTime = 0;
        private static int mCurrentSetupFailedCause = 0;
        private static int mSetupFailedCount = 0;
        public static boolean mIsSetupSuccess = false;
        private static int mLastSubId = -1;
        public static long mLastLPMTime = 0;
        private static long mLastSetupFailedOptTime = 0;
        private static long PENALTY_TIMER = 21600000;
        private static boolean mIsSpecialNetworkOptForSetupFailed = false;
        private static boolean mIsClearPermanentFailForSetupFailed = false;
        private static final LinkedList<Long> mDisconnectReasonList = new LinkedList<>();
        private static SetupFailedOptScene mSetupFailedOptScene = SetupFailedOptScene.NO_RECOVERY;
        public static boolean mIsNetworkValid = false;
        public static long mLastModemRestartTime = 0;
        public static long mLastFakeDcOptTime = 0;
        private static boolean mIsSpecialNetworkOptForFakeDc = false;
        public static boolean mIsVoipForgroud = false;
        public static boolean mIsGameForgroud = false;
        public static boolean mIsDataStall = false;
        public static long mLastDataStallTime = 0;
        private static final LinkedList<Long> mFakeConnectionTimeList = new LinkedList<>();
        public static FakeDcOptScene mFakeDcOptScene = FakeDcOptScene.NO_RECOVERY;
        private static boolean[] mInSetNetworkSelectionManual = {false, false, false};
        public static int mOptimizationRoamingProtocol = -1;
        public static DataProfile mOptimizationDataProfile = null;
        private static final Set<Integer> mDataFailCauseIpv4Disallowed = new HashSet();
        private static final Set<Integer> mDataFailCauseIpv6Disallowed = new HashSet();

        static {
            mDataFailCauseIpv4Disallowed.add(2033);
            mDataFailCauseIpv6Disallowed.add(2035);
        }

        public enum SetupFailedOptScene {
            NO_RECOVERY(10),
            LPM_RECOVERY(1),
            APN_CHANGE_RECOVERY(2),
            SPECIAL_NETWORK_OPT_RECOVERY(3),
            CLEAR_PERMANENT_FAILURE(4),
            DUAL_SIM_SWITCH_RECOVERY(5),
            RETRY_TIME_RECOVERY(6);

            private int value;

            SetupFailedOptScene(int value) {
                this.value = value;
            }

            public int getValue() {
                return this.value;
            }
        }

        public enum FakeDcOptScene {
            NO_RECOVERY(10),
            MODEM_RESTART(0),
            LPM_RECOVERY(1),
            SPECIAL_NETWORK_OPT_RECOVERY(2),
            CLEAN_UP_RECOVERY(3),
            DUAL_SIM_SWITCH_RECOVERY(4);

            private int value;

            FakeDcOptScene(int value) {
                this.value = value;
            }

            public int getValue() {
                return this.value;
            }
        }

        public static void setManualNetworkSelected(Phone phone, boolean val) {
            if (phone == null) {
                return;
            }
            int phoneId = phone.getPhoneId();
            if (phoneId >= 0 && phoneId < mInSetNetworkSelectionManual.length) {
                mInSetNetworkSelectionManual[phoneId] = val;
            }
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "setManualNetworkSelected, phoneId:" + phoneId + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + val);
        }

        public static boolean getManualNetworkSelected(Phone phone) {
            int phoneId;
            if (phone == null || (phoneId = phone.getPhoneId()) < 0 || phoneId >= mInSetNetworkSelectionManual.length) {
                return false;
            }
            return mInSetNetworkSelectionManual[phoneId];
        }

        private static boolean isSupportSmartNetworkAutonomy() {
            boolean isPersistConfigStatus = SystemProperties.getBoolean("persist.radio.support.datastall", true);
            boolean isSupport = HyperOSCustFeatureResolve.getBoolean("config_support_smart_network_autonomy", false);
            return isPersistConfigStatus && (!Build.IS_INTERNATIONAL_BUILD || isSupport);
        }

        public static boolean isRoamingSetupFailedOptimization(Phone phone) {
            if (Build.IS_INTERNATIONAL_BUILD || !InternalTelephonyUtils.isSupportRoamingOptimization()) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "isRoamingSetupFailedOptimization, device is not support");
                return false;
            }
            String operatorNumeric = phone.getOperatorNumeric();
            if (!ServiceProviderUtils.isChineseServiceProvider(operatorNumeric)) {
                return false;
            }
            int subId = phone.getSubId();
            if (!SubscriptionManager.isValidSubscriptionId(subId) || subId != SubscriptionManager.getActiveDataSubscriptionId()) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "isRoamingSetupFailedOptimization - invalid subId!");
                return false;
            }
            boolean isDataRoaming = phone.getServiceState().getDataRoaming();
            return isDataRoaming;
        }

        public static void updateSetupFailedRoamingInfo(Phone phone, DataNetwork dataNetwork, int cause) {
            mOptimizationDataProfile = dataNetwork.getDataProfile();
            if (mDataFailCauseIpv4Disallowed.contains(Integer.valueOf(cause))) {
                mOptimizationRoamingProtocol = 1;
            } else if (mDataFailCauseIpv6Disallowed.contains(Integer.valueOf(cause))) {
                mOptimizationRoamingProtocol = 0;
            } else {
                mOptimizationRoamingProtocol = -1;
                mOptimizationDataProfile = null;
            }
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateSetupFailedRoamingInfo, roamingProtocol = " + mOptimizationRoamingProtocol + ", optimizationDataProfile = " + mOptimizationDataProfile);
        }

        public static void updateFakeConnectionInfo(Phone phone, boolean isValid) {
            if (!isSupportSmartNetworkAutonomy()) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateFakeConnectionInfo:skip as device not support!");
                return;
            }
            if (PhoneConstants.State.IDLE != phone.getState() || isOnCall()) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateFakeConnectionInfo:skip as there is an active call!");
                return;
            }
            DataNetworkController dataNetworkController = phone.getDataNetworkController();
            if (dataNetworkController != null) {
                if (dataNetworkController.isInternetDataAllowed(false)) {
                    boolean isDualDataConnected = SystemProperties.getInt("ril.multisim.dualdata_status", 0) == 1;
                    if (isDualDataConnected) {
                        Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateFakeConnectionInfo:skip as dual data connected!");
                        return;
                    }
                    int subId = phone.getSubId();
                    if (!SubscriptionManager.isValidSubscriptionId(subId) || subId != SubscriptionManager.getActiveDataSubscriptionId()) {
                        Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateFakeConnectionInfo:skip as invalid subId!");
                        return;
                    }
                    MiuiDataControllerImpl miuiDataController = (MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, phone.getPhoneId());
                    if (miuiDataController == null || !miuiDataController.isLteNrRat(phone)) {
                        Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateFakeConnectionInfo:skip as not lte/nr!");
                        return;
                    }
                    if (miuiDataController == null || miuiDataController.mDataNetworkList == null || !miuiDataController.mDataNetworkList.stream().anyMatch(new Predicate() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$Processor$$ExternalSyntheticLambda0
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            return MiuiDataControllerImpl.Processor.lambda$updateFakeConnectionInfo$0((DataNetwork) obj);
                        }
                    })) {
                        Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateFakeConnectionInfo:skip as not connected state!");
                        return;
                    }
                    if (miuiDataController == null || miuiDataController.mIsWifiConnected) {
                        Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateFakeConnectionInfo:skip as wifi connected state!");
                        return;
                    }
                    if (dataNetworkController.hasMessages(MiuiDataControllerImpl.EVENT_FAKE_CONNECTION_DELAY)) {
                        dataNetworkController.removeMessages(MiuiDataControllerImpl.EVENT_FAKE_CONNECTION_DELAY);
                    }
                    long validationGraceRemainingTime = miuiDataController.getFakeConnectionValidationGraceRemainingTime();
                    if (!isValid && validationGraceRemainingTime > 0) {
                        Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateFakeConnectionInfo:skip as validation grace period!");
                        mIsNetworkValid = isValid;
                        dataNetworkController.sendMessageDelayed(dataNetworkController.obtainMessage(MiuiDataControllerImpl.EVENT_FAKE_CONNECTION_DELAY), validationGraceRemainingTime);
                        return;
                    }
                    if (!isValid) {
                        if (isLingerTimeForModemRestart()) {
                            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateFakeConnectionInfo:skip as is linger time for modem restart!");
                            mIsNetworkValid = isValid;
                            return;
                        }
                        MiuiDataControllerImpl.trimCurrentTimeMillis(mFakeConnectionTimeList, 120);
                        mFakeConnectionTimeList.add(Long.valueOf(System.currentTimeMillis()));
                        dataNetworkController.sendMessageDelayed(dataNetworkController.obtainMessage(MiuiDataControllerImpl.EVENT_FAKE_CONNECTION_DELAY), MiuiDataControllerImpl.FAKE_CONNECTION_VALIDATION_GRACE_PERIOD_MS);
                        Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doRecoveryForFakeConnection:fakeConnectionCount=" + mFakeConnectionTimeList.size());
                        if (mFakeConnectionTimeList.size() >= 2 || System.currentTimeMillis() - mLastDataStallTime <= MiuiDataControllerImpl.FAKE_CONNECTION_VALIDATION_GRACE_PERIOD_MS) {
                            doRecoveryForFakeConnection(phone, dataNetworkController);
                        }
                    } else if (mIsNetworkValid != isValid) {
                        if (mLastFakeDcOptTime != 0) {
                            int fakeDcOptSceneValue = mFakeDcOptScene.getValue();
                            long curActionTimeForFakeConnection = getCurActionTimeForFakeConnection();
                            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateFakeConnectionInfo:" + fakeDcOptSceneValue + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + curActionTimeForFakeConnection);
                            TelephonyStatAdapter.notifyFakeConnectionRecoveryTime(fakeDcOptSceneValue, curActionTimeForFakeConnection);
                        }
                        resetFakeConnectionInfo();
                    }
                    mIsNetworkValid = isValid;
                    return;
                }
            }
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateFakeConnectionInfo:skip as data not allowed!");
        }

        static /* synthetic */ boolean lambda$updateFakeConnectionInfo$0(DataNetwork dataNetwork) {
            return dataNetwork.isInternetSupported() && dataNetwork.isConnected();
        }

        public static void doRecoveryForFakeConnection(Phone phone, DataNetworkController dataNetworkController) {
            MiuiDataControllerImpl miuiDataController = (MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, phone.getPhoneId());
            if (miuiDataController.isSkipRecoveryApn(phone)) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doRecoveryForFakeConnection:skip data recovery for special apns!");
                return;
            }
            if (isLingerTimeForModemRestart()) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doRecoveryForFakeConnection:skip as is linger time for modem restart!");
                return;
            }
            if (shouldAvoidFakeDcOptByPlatform()) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doRecoveryForFakeConnection:skip as platform avoid scene!");
                return;
            }
            int ddsPhoneId = DefaultSimManager.getDefaultDataSlotId();
            mSmartDualSimSwitch = SmartDualSimSwitch.getInstance();
            if (FakeDcOptScene.DUAL_SIM_SWITCH_RECOVERY.getValue() < mFakeDcOptScene.getValue() && mSmartDualSimSwitch != null && isDualSimSwitchSupport(phone) && mSmartDualSimSwitch.isGoodSignalForFakeConnection(getAnotherPhoneId(phone.getPhoneId()))) {
                TelephonyStatAdapter.notifyFakeConnectionRecovery(FakeDcOptScene.DUAL_SIM_SWITCH_RECOVERY.getValue());
                mLastFakeDcOptTime = System.currentTimeMillis();
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doRecoveryForFakeConnection:Try recover by dual sim switch recovery");
                mSmartDualSimSwitch.processDataNetworkFail(ddsPhoneId == phone.getPhoneId(), SmartDualSimSwitch.ActiveScene.FAKE_CONNECTION);
                mFakeDcOptScene = FakeDcOptScene.DUAL_SIM_SWITCH_RECOVERY;
                return;
            }
            doCommonOptForFakeConnection(phone, dataNetworkController);
        }

        private static void doCommonOptForFakeConnection(final Phone phone, DataNetworkController dataNetworkController) {
            MiuiDataControllerImpl miuiDataController = (MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, phone.getPhoneId());
            if (FakeDcOptScene.CLEAN_UP_RECOVERY.getValue() < mFakeDcOptScene.getValue() && miuiDataController != null && !miuiDataController.mIsSignalPoor && !miuiDataController.isLteRat(phone.getPhoneId())) {
                TelephonyStatAdapter.notifyFakeConnectionRecovery(FakeDcOptScene.CLEAN_UP_RECOVERY.getValue());
                mLastFakeDcOptTime = System.currentTimeMillis();
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doCommonOptForFakeConnection:Try recover by clean up data recovery");
                List<DataNetwork> dataNetworkList = miuiDataController != null ? miuiDataController.mDataNetworkList : null;
                if (dataNetworkList != null) {
                    dataNetworkList.stream().filter(new Predicate() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$Processor$$ExternalSyntheticLambda2
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            return ((DataNetwork) obj).isInternetSupported();
                        }
                    }).forEach(new Consumer() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$Processor$$ExternalSyntheticLambda3
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            ((DataNetwork) obj).tearDown(12);
                        }
                    });
                }
                mFakeDcOptScene = FakeDcOptScene.CLEAN_UP_RECOVERY;
                return;
            }
            if (FakeDcOptScene.SPECIAL_NETWORK_OPT_RECOVERY.getValue() < mFakeDcOptScene.getValue() && InternalTelephonyUtils.isSpecialNetorkOptimizationSupported() && InternalTelephonyUtils.isSpecialNetworkModeEnabled(phone.getContext()) && phone.getPhoneId() == DefaultSimManager.getDefaultDataSlotId()) {
                TelephonyStatAdapter.notifyFakeConnectionRecovery(FakeDcOptScene.SPECIAL_NETWORK_OPT_RECOVERY.getValue());
                mIsSpecialNetworkOptForFakeDc = true;
                mLastFakeDcOptTime = System.currentTimeMillis();
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doCommonOptForFakeConnection:Try recover by special network optimization recovery");
                recoveryDataBySpecialNetworkOpt(phone, REPAIR_DATA_NETWORK_FAIL, FAKE_CONNECTION_TRIGGER_SCENE);
                mFakeDcOptScene = FakeDcOptScene.SPECIAL_NETWORK_OPT_RECOVERY;
                return;
            }
            if (FakeDcOptScene.LPM_RECOVERY.getValue() < mFakeDcOptScene.getValue()) {
                TelephonyStatAdapter.notifyFakeConnectionRecovery(FakeDcOptScene.LPM_RECOVERY.getValue());
                mLastFakeDcOptTime = System.currentTimeMillis();
                if (mIsSpecialNetworkOptForFakeDc) {
                    recoveryDataBySpecialNetworkOpt(phone, CLEAR_DATA_NETWORK_FAIL, FAKE_CONNECTION_TRIGGER_SCENE);
                    mIsSpecialNetworkOptForFakeDc = false;
                }
                if (!Build.IS_INTERNATIONAL_BUILD) {
                    Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doCommonOptForFakeConnection:Try recover by LPM recovery");
                    new Thread(new Runnable() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$Processor$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            MiuiDataControllerImpl.Processor.recoveryDataByLpm(phone);
                        }
                    }).start();
                } else {
                    Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doCommonOptForFakeConnection:Skip as international build!");
                }
                mFakeDcOptScene = FakeDcOptScene.LPM_RECOVERY;
                return;
            }
            if (FakeDcOptScene.MODEM_RESTART.getValue() < mFakeDcOptScene.getValue()) {
                if (miuiDataController != null && miuiDataController.isMeetModemResetForFakeConnection()) {
                    TelephonyStatAdapter.notifyFakeConnectionRecovery(FakeDcOptScene.MODEM_RESTART.getValue());
                    mLastFakeDcOptTime = System.currentTimeMillis();
                    mLastModemRestartTime = mLastFakeDcOptTime;
                    if (MiuiTelephony.IS_XRING) {
                        Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doCommonOptForFakeConnection:Try recover by modem restart recovery");
                        recoveryDataByModemRestart(phone);
                    }
                    mFakeDcOptScene = FakeDcOptScene.MODEM_RESTART;
                    return;
                }
                return;
            }
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doCommonOptForFakeConnection:trigger recover invalid.");
        }

        private static synchronized boolean shouldAvoidFakeDcOptByPlatform() {
            boolean isShouldAvoidFakeDcOptByPlatform;
            isShouldAvoidFakeDcOptByPlatform = false;
            if (MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING) {
                if (System.currentTimeMillis() - mLastDataStallTime > MiuiDataControllerImpl.FAKE_CONNECTION_VALIDATION_GRACE_PERIOD_MS) {
                    isShouldAvoidFakeDcOptByPlatform = true;
                    Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doRecoveryForFakeConnection:skip as mtk/xring scenes");
                }
            } else if ((mIsVoipForgroud || mIsGameForgroud) && System.currentTimeMillis() - mLastDataStallTime > MiuiDataControllerImpl.FAKE_CONNECTION_VALIDATION_GRACE_PERIOD_MS) {
                isShouldAvoidFakeDcOptByPlatform = true;
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doRecoveryForFakeConnection:skip as qc scenes," + mIsVoipForgroud + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + mIsGameForgroud);
            }
            return isShouldAvoidFakeDcOptByPlatform;
        }

        public static void updateSetupFailedInfo(Phone phone, DataNetwork dataNetwork, boolean isSetupSuccess, int cause) {
            if (!isSupportSmartNetworkAutonomy()) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateSetupFailedInfo:skip as device not support!");
                return;
            }
            if (dataNetwork == null || !dataNetwork.isInternetSupported()) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateSetupFailedInfo:skip as only for default apnContext!");
                return;
            }
            boolean isDualDataConnected = SystemProperties.getInt("ril.multisim.dualdata_status", 0) == 1;
            if (isDualDataConnected) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateSetupFailedInfo:skip as dual data connected!");
                return;
            }
            int subId = phone.getSubId();
            if (!SubscriptionManager.isValidSubscriptionId(subId) || subId != SubscriptionManager.getActiveDataSubscriptionId()) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateSetupFailedInfo:skip as invalid subId!");
                return;
            }
            MiuiDataControllerImpl miuiDataController = (MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, phone.getPhoneId());
            if ((miuiDataController == null || !miuiDataController.isLteNrRat(phone)) && !isPermanentFailure(cause)) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateSetupFailedInfo:skip as not permanentFailure and not lte/nr!");
                return;
            }
            if (getManualNetworkSelected(phone) && cause == 65535) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateSetupFailedInfo:skip as ignore manual plmn case");
                return;
            }
            if (!isSetupSuccess) {
                if (isLingerTimeForLPM()) {
                    Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateSetupFailedInfo:skip as is linger time for LPM!");
                    mIsSetupSuccess = isSetupSuccess;
                    return;
                } else {
                    if (mLastSubId != subId) {
                        Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateSetupFailedInfo:reset_fail mLastSubId:" + mLastSubId + "subId:" + subId + "mLastSetupFailedOptTime" + mLastSetupFailedOptTime);
                        resetSetupInfo();
                        mLastSubId = subId;
                    }
                    doRecoveryForSetupFailed(phone, cause);
                }
            } else if (mIsSetupSuccess != isSetupSuccess) {
                if (mLastSetupFailedOptTime != 0) {
                    int setupFailOptSceneValue = mSetupFailedOptScene.getValue();
                    long curActionTimeForSetupFail = getCurActionTimeForSetupFail();
                    Rlog.d(MiuiDataControllerImpl.LOG_TAG, "updateSetupFailedInfo:" + setupFailOptSceneValue + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + curActionTimeForSetupFail);
                    TelephonyStatAdapter.notifySetupFailRecoveryTime(mCurrentSetupFailedCause, setupFailOptSceneValue, curActionTimeForSetupFail);
                }
                resetSetupInfo();
            }
            mIsSetupSuccess = isSetupSuccess;
        }

        private static void sendEvent(MiEvent event) {
            if (TelephonyManagerEx.getDefault().isMievnetSupported()) {
                MiSight.sendEvent(event);
            }
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
        private static void doRecoveryForSetupFailed(final Phone phone, final int i) {
            if (isLingerTimeForLPM()) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doRecoveryForSetupFailed:skip as is linger time for LPM!");
                return;
            }
            int i2 = 1;
            i2 = 1;
            i2 = 1;
            i2 = 1;
            mSetupFailedCount++;
            mCurrentSetupFailedCause = i;
            boolean zIsPermanentFailure = isPermanentFailure(i);
            boolean zIsDataThrottledScene = isDataThrottledScene(phone.getPhoneId(), i);
            int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
            mSmartDualSimSwitch = SmartDualSimSwitch.getInstance();
            if (zIsPermanentFailure && mSmartDualSimSwitch != null && isDualSimSwitchSupport(phone)) {
                TelephonyStatAdapter.notifySetupDataFailRecovery(i, SetupFailedOptScene.DUAL_SIM_SWITCH_RECOVERY.getValue());
                mLastSetupFailedOptTime = System.currentTimeMillis();
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doRecoveryForSetupFailed:Try recover by dual sim switch recovery");
                mSmartDualSimSwitch.processDataNetworkFail(defaultDataSlotId == phone.getPhoneId(), SmartDualSimSwitch.ActiveScene.SETUP_FAILURE);
                mSetupFailedOptScene = SetupFailedOptScene.DUAL_SIM_SWITCH_RECOVERY;
                return;
            }
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doRecoveryForSetupFailed:mCurrentSetupFailedCause=" + mCurrentSetupFailedCause + "mSetupFailedCount=" + mSetupFailedCount + ",mPhoneState=" + phone.getState() + "isPermanentFailure(cause)=" + zIsPermanentFailure);
            if (!zIsPermanentFailure && !MiuiDataControllerImpl.mUnsolDataCallError && !MiuiDataControllerImpl.mSetupDataCallNoResponse && !zIsDataThrottledScene) {
                i2 = 3;
            }
            MiuiDataControllerImpl miuiDataControllerImpl = (MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, phone.getPhoneId());
            if (mSetupFailedCount >= i2 && PhoneConstants.State.IDLE == phone.getState() && !isOnCall()) {
                switch (i) {
                    case -1:
                    case 2034:
                    case 2112:
                    case 2190:
                    case ModemUtils.MODEM_STATS_DATA_INVALID /* 65535 */:
                        if (!mIsSpecialNetworkOptForSetupFailed && InternalTelephonyUtils.isSpecialNetorkOptimizationSupported() && InternalTelephonyUtils.isSpecialNetworkModeEnabled(phone.getContext()) && phone.getPhoneId() == defaultDataSlotId) {
                            doSpecialNetworkOptForSetupFailed(phone, i);
                        } else if (!MiuiDataControllerImpl.mUnsolDataCallError) {
                            doCommonOptForSetupFailed(phone, i);
                        }
                        break;
                    default:
                        if (MiuiTelephony.IS_QCOM && miuiDataControllerImpl != null && isPermanentFailure(i) && !mIsClearPermanentFailForSetupFailed) {
                            mSetupFailedOptScene = SetupFailedOptScene.NO_RECOVERY;
                            miuiDataControllerImpl.mWorkerHandler.postDelayed(new Runnable() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$Processor$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    MiuiDataControllerImpl.Processor.doClearPermanentFailForSetupFailed(phone, i);
                                }
                            }, 200L);
                        } else {
                            doCommonOptForSetupFailed(phone, i);
                        }
                        break;
                }
                recordDfsForSetupFail(i);
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, ",mSetupFailedOptScene =" + mSetupFailedOptScene + ",mLastSetupFailedOptTime=" + mLastSetupFailedOptTime);
            }
        }

        private static void recordDfsForSetupFail(int cause) {
            if (cause == 65538) {
                MiEvent ApnNullEvent = MiSight.constructEvent(PROTOCOL_SETUPDATACALL_RETURN_NULL_EVENT_CODE, new Object[]{"ifname", "", "dnses", "", "gateways", ""});
                sendEvent(ApnNullEvent);
                MiEvent ApnNullRecoveryEvent = MiSight.constructEvent(PROTOCOL_SETUPDATACALL_NULL_RECOVERY_EVENT_CODE, new Object[]{MiEventUtils.RECOVERY_RESULT, "false", MiEventUtils.PROBLEM_CODE, Integer.valueOf(PROTOCOL_SETUPDATACALL_RETURN_NULL_EVENT_CODE)});
                sendEvent(ApnNullRecoveryEvent);
                return;
            }
            MiEvent PDNErrorEvent = MiSight.constructEvent(PROTOCOL_SETUPDATACALL_RETURN_ERROR_EVENT_CODE, new Object[]{"cause", Integer.valueOf(cause)});
            sendEvent(PDNErrorEvent);
            MiEvent PDNErrorRecoveryEvent = MiSight.constructEvent(PROTOCOL_SETUPDATACALL_ERROR_RECOVERY_EVENT_CODE, new Object[]{MiEventUtils.RECOVERY_RESULT, "false", MiEventUtils.PROBLEM_CODE, Integer.valueOf(PROTOCOL_SETUPDATACALL_RETURN_ERROR_EVENT_CODE)});
            sendEvent(PDNErrorRecoveryEvent);
        }

        private static void doSpecialNetworkOptForSetupFailed(Phone phone, int cause) {
            if (SetupFailedOptScene.SPECIAL_NETWORK_OPT_RECOVERY.getValue() < mSetupFailedOptScene.getValue()) {
                TelephonyStatAdapter.notifySetupDataFailRecovery(cause, SetupFailedOptScene.SPECIAL_NETWORK_OPT_RECOVERY.getValue());
                mIsSpecialNetworkOptForSetupFailed = true;
                mLastSetupFailedOptTime = System.currentTimeMillis();
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doSpecialNetworkOptForSetupFailed:Try recover by special network optimization recovery");
                recoveryDataBySpecialNetworkOpt(phone, REPAIR_DATA_NETWORK_FAIL, SETUP_FAIL_TRIGGER_SCENE);
                mSetupFailedOptScene = SetupFailedOptScene.SPECIAL_NETWORK_OPT_RECOVERY;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void doClearPermanentFailForSetupFailed(Phone phone, int cause) {
            if (SetupFailedOptScene.CLEAR_PERMANENT_FAILURE.getValue() < mSetupFailedOptScene.getValue()) {
                TelephonyStatAdapter.notifySetupDataFailRecovery(cause, SetupFailedOptScene.CLEAR_PERMANENT_FAILURE.getValue());
                mIsClearPermanentFailForSetupFailed = true;
                mLastSetupFailedOptTime = System.currentTimeMillis();
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doClearPermanentFailForSetupFailed：Try recover by clear permanent failure recovery");
                recoveryDataByClearPermanentFailures(phone);
                mSetupFailedOptScene = SetupFailedOptScene.CLEAR_PERMANENT_FAILURE;
            }
        }

        private static void doCommonOptForSetupFailed(final Phone phone, int cause) {
            MiuiDataControllerImpl miuiDataController = (MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, phone.getPhoneId());
            if (SetupFailedOptScene.APN_CHANGE_RECOVERY.getValue() < mSetupFailedOptScene.getValue() && !isPermanentFailure(cause)) {
                TelephonyStatAdapter.notifySetupDataFailRecovery(cause, SetupFailedOptScene.APN_CHANGE_RECOVERY.getValue());
                mLastSetupFailedOptTime = System.currentTimeMillis();
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doCommonOptForSetupFailed：Try recover by APN change recovery");
                try {
                    recoveryDataByUpdateDataProfiles(phone, false);
                } catch (Exception e) {
                    Rlog.e(MiuiDataControllerImpl.LOG_TAG, "doCommonOptForSetupFailed：Can not updateDataProfiles");
                }
                mSetupFailedOptScene = SetupFailedOptScene.APN_CHANGE_RECOVERY;
                return;
            }
            if (SetupFailedOptScene.LPM_RECOVERY.getValue() < mSetupFailedOptScene.getValue()) {
                TelephonyStatAdapter.notifySetupDataFailRecovery(cause, SetupFailedOptScene.LPM_RECOVERY.getValue());
                mLastSetupFailedOptTime = System.currentTimeMillis();
                mLastLPMTime = mLastSetupFailedOptTime;
                if (mIsSpecialNetworkOptForSetupFailed) {
                    recoveryDataBySpecialNetworkOpt(phone, CLEAR_DATA_NETWORK_FAIL, SETUP_FAIL_TRIGGER_SCENE);
                    mIsSpecialNetworkOptForSetupFailed = false;
                }
                if (!Build.IS_INTERNATIONAL_BUILD && miuiDataController != null && !miuiDataController.mIsWifiConnected) {
                    Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doCommonOptForSetupFailed：Try recover by LPM recovery");
                    new Thread(new Runnable() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl$Processor$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            MiuiDataControllerImpl.Processor.recoveryDataByLpm(phone);
                        }
                    }).start();
                } else {
                    Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doCommonOptForSetupFailed:Skip as international build!");
                }
                mSetupFailedOptScene = SetupFailedOptScene.LPM_RECOVERY;
                return;
            }
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "doCommonOptForSetupFailed：trigger recover invalid.");
        }

        private static void recoveryDataBySpecialNetworkOpt(Phone phone, int cause, String triggerScene) {
            Intent intent = new Intent("com.android.phone.intent.action.DATA_NETWORK_FAIL");
            intent.setPackage(NOTIFACATION_RECEIVER_PACKAGE);
            intent.putExtra(KEY_DATA_NETWORK_FAIL_CAUSE, cause);
            intent.putExtra(KEY_TRIGGER_SCENE, triggerScene);
            intent.putExtra(KEY_IS_DELAY, MiuiDataControllerImpl.mUnsolDataCallError);
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "recoveryDataBySpecialNetworkOpt cause " + cause + " triggerScene " + triggerScene);
            phone.getContext().sendBroadcast(intent);
        }

        private static void recoveryDataByClearPermanentFailures(Phone phone) {
            DataProfileManager dataProfileManager = phone.getDataNetworkController().getDataProfileManager();
            if (dataProfileManager != null) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "recoveryDataByClearPermanentFailures");
                dataProfileManager.clearAllDataProfilePermanentFailures();
            }
        }

        private static void recoveryDataByUpdateDataProfiles(Phone phone, boolean forceUpdateIa) throws Exception {
            Method updateDataProfilesMethod = DataProfileManager.class.getDeclaredMethod("updateDataProfiles", Boolean.TYPE);
            updateDataProfilesMethod.setAccessible(true);
            DataProfileManager dataProfileManager = phone.getDataNetworkController().getDataProfileManager();
            if (dataProfileManager != null) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "recoveryDataByUpdateDataProfiles");
                updateDataProfilesMethod.invoke(dataProfileManager, Boolean.valueOf(forceUpdateIa));
            }
        }

        public static void recoveryDataByLpm(Phone phone) {
            Intent intent = new Intent(CommenUtils.ACTIVE_RE_REGISTRATION_NETWORK);
            phone.getContext().sendBroadcast(intent);
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "recoveryDataByLpm");
            for (Phone phoneProxy : PhoneFactory.getPhones()) {
                phoneProxy.getServiceStateTracker().powerOffRadioSafely();
            }
        }

        private static void recoveryDataByModemRestart(Phone phone) {
            Intent intent = new Intent(CommenUtils.ACTIVE_RE_REGISTRATION_NETWORK);
            phone.getContext().sendBroadcast(intent);
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "recoveryDataByModemRestart");
            phone.rebootModem((Message) null);
        }

        private static void resetSetupInfo() {
            mSetupFailedCount = 0;
            mLastSetupFailedOptTime = 0L;
            mSetupFailedOptScene = SetupFailedOptScene.NO_RECOVERY;
            mCurrentSetupFailedCause = 0;
            MiuiDataControllerImpl.mUnsolDataCallError = false;
            MiuiDataControllerImpl.mSetupDataCallNoResponse = false;
            mIsSpecialNetworkOptForSetupFailed = false;
            mIsClearPermanentFailForSetupFailed = false;
        }

        private static boolean isLingerTimeForLPM() {
            long curTime = System.currentTimeMillis();
            if (mLastLPMTime != 0 && curTime < mLastLPMTime + PENALTY_TIMER) {
                return true;
            }
            return false;
        }

        public static boolean isLingerTimeForModemRestart() {
            long curTime = System.currentTimeMillis();
            if (mLastModemRestartTime != 0 && curTime < mLastModemRestartTime + PENALTY_TIMER) {
                return true;
            }
            return false;
        }

        public static void resetFakeConnectionInfo(DataNetworkController dataNetworkController) {
            if (dataNetworkController != null && dataNetworkController.hasMessages(MiuiDataControllerImpl.EVENT_FAKE_CONNECTION_DELAY)) {
                dataNetworkController.removeMessages(MiuiDataControllerImpl.EVENT_FAKE_CONNECTION_DELAY);
            }
            resetFakeConnectionInfo();
        }

        public static void resetFakeConnectionInfo() {
            mLastFakeDcOptTime = 0L;
            mIsDataStall = false;
            mLastDataStallTime = 0L;
            mIsSpecialNetworkOptForFakeDc = false;
            mIsNetworkValid = false;
            mFakeDcOptScene = FakeDcOptScene.NO_RECOVERY;
            mFakeConnectionTimeList.clear();
        }

        public static long getCurActionTimeForFakeConnection() {
            return System.currentTimeMillis() - mLastFakeDcOptTime;
        }

        private static long getCurActionTimeForSetupFail() {
            return System.currentTimeMillis() - mLastSetupFailedOptTime;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isOnCall() {
            for (int index = 0; index < MiuiTelephony.PHONE_COUNT; index++) {
                if (TelephonyManagerEx.getDefault().getCallStateForSlot(index) != 0) {
                    Rlog.d(MiuiDataControllerImpl.LOG_TAG, "On call!!");
                    return true;
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean shouldIgnoreDataStallForDsdsViceCall() {
            if (MiuiTelephony.PHONE_COUNT <= 1) {
                return false;
            }
            int ddsPhoneId = DefaultSimManager.getDefaultDataSlotId();
            if (!SubscriptionManager.isValidPhoneId(ddsPhoneId) || ddsPhoneId >= MiuiTelephony.PHONE_COUNT) {
                return false;
            }
            boolean isDualDataConnected = SystemProperties.getInt("ril.multisim.dualdata_status", 0) == 1;
            boolean isVoiceDataConcurrencyState = isCurrentDSDAState() || TelephonyManagerEx.getDefault().isDsdaFullOrTxsConcurrencyState() || isAnyViceSlotVoiceLinkRegistered(ddsPhoneId);
            if (isVoiceDataConcurrencyState || isDualDataConnected) {
                return false;
            }
            int ddsCallState = TelephonyManagerEx.getDefault().getCallStateForSlot(ddsPhoneId);
            if (ddsCallState != 0) {
                return false;
            }
            int viceCallPhoneId = -1;
            for (int phoneId = 0; phoneId < MiuiTelephony.PHONE_COUNT; phoneId++) {
                if (phoneId != ddsPhoneId) {
                    int callState = TelephonyManagerEx.getDefault().getCallStateForSlot(phoneId);
                    if (callState != 0) {
                        viceCallPhoneId = phoneId;
                        break;
                    }
                }
            }
            if (viceCallPhoneId == -1) {
                return false;
            }
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "shouldIgnoreDataStallForDsdsViceCall: true, ddsPhoneId=" + ddsPhoneId + ", viceCallPhoneId=" + viceCallPhoneId + ", isDualDataConnected=" + isDualDataConnected + ", isVoiceDataConcurrencyState=" + isVoiceDataConcurrencyState);
            return true;
        }

        private static boolean isAnyViceSlotVoiceLinkRegistered(int ddsPhoneId) {
            for (int phoneId = 0; phoneId < MiuiTelephony.PHONE_COUNT; phoneId++) {
                if (phoneId != ddsPhoneId && TelephonyManagerEx.getDefault().isVoiceLinkRegistered(phoneId)) {
                    return true;
                }
            }
            return false;
        }

        public static PhoneConstants.DataState adjustDataState(PhoneConstants.DataState dataState, String apnType, int phoneId) {
            if (!ModemUtils.PROP_NTN_LOG_LEVEL_DEFAULT.equals(apnType) || dataState != PhoneConstants.DataState.CONNECTED) {
                return dataState;
            }
            for (Phone phoneProxy : PhoneFactory.getPhones()) {
                if (phoneProxy.getPhoneId() != phoneId && phoneProxy.getState() != PhoneConstants.State.IDLE) {
                    return PhoneConstants.DataState.SUSPENDED;
                }
            }
            return dataState;
        }

        public static boolean isPlatformDE20() {
            return TelephonyManagerEx.getDefault().isPlatformX450() || TelephonyManagerEx.getDefault().isPlatformX475() || TelephonyManagerEx.getDefault().isPlatform7450();
        }

        public static boolean isPlatformDE30() {
            return TelephonyManagerEx.getDefault().isPlatform8550();
        }

        public static boolean isPlatformDE50() {
            return TelephonyManagerEx.getDefault().isPlatform8650() || TelephonyManagerEx.getDefault().isPlatform8635();
        }

        public static boolean isPlatformDE70() {
            return TelephonyManagerEx.getDefault().isPlatform8750() || TelephonyManagerEx.getDefault().isPlatform8735();
        }

        public static boolean isPlatformDE90() {
            return TelephonyManagerEx.getDefault().isPlatform8850();
        }

        public static boolean isCurrentDSDAState() {
            boolean ret = false;
            boolean z = true;
            if (MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING) {
                int dsdaState = SystemProperties.getInt("vendor.radio.dsda.state", 4);
                if (dsdaState > 100) {
                    dsdaState /= 100;
                }
                if (dsdaState != 2 && dsdaState != 3) {
                    z = false;
                }
                ret = z;
            } else if (MiuiTelephony.IS_QCOM) {
                ret = 3 == SystemProperties.getInt("ril.multisim.voice_capability", 0);
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "Qcom Dsda State = " + ret);
            }
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "isCurrentDSDAState:" + ret);
            return ret;
        }

        public static void updateAPNSimOperatorNumeric(Phone phone, String operatorNumeric) {
            List<String> newList = ServiceProviderUtils.updateTelephonyProperty(TelephonyProperties.gsm_apn_sim_operator_numeric(), phone.getPhoneId(), operatorNumeric);
            TelephonyProperties.gsm_apn_sim_operator_numeric(newList);
        }

        public static void resetApnSimOperatoryNumeric(Phone phone) {
            UiccController controller = UiccController.getInstance();
            if (controller == null) {
                return;
            }
            UiccCard card = controller.getUiccCard(phone.getPhoneId());
            if (card == null || card.getCardState() == IccCardStatus.CardState.CARDSTATE_ABSENT) {
                updateAPNSimOperatorNumeric(phone, "");
            }
        }

        static String switchOperatorNumericForCT(Phone phone, String numeric, IccRecords iccRecords) {
            if (isNeedSwitchOperatorNumericToCT(phone, numeric)) {
                String ctNumeric = iccRecords != null ? iccRecords.getOperatorNumeric() : null;
                log(phone, "switchOperatorNumericForCT  ctNumeric： " + ctNumeric + ", iccRecords: " + iccRecords);
                if (TextUtils.isEmpty(ctNumeric)) {
                    ctNumeric = CHINA_TELECOM_OPERATOR_NUMERIC;
                }
                updateAPNSimOperatorNumeric(phone, ctNumeric);
                return ctNumeric;
            }
            log(phone, "switchOperatorNumericForCT numeric: " + numeric);
            return numeric;
        }

        static boolean isNeedSwitchOperatorNumericToCT(Phone phone, String numeric) {
            if (TextUtils.isEmpty(numeric) || ServiceProviderUtils.isChinaTelecom(numeric)) {
                return false;
            }
            boolean dataRoaming = phone.getServiceState().getDataRoaming();
            boolean hasCdmaApp = UiccController.getInstance().getUiccCardApplication(phone.getPhoneId(), 2) != null;
            log(phone, "isNeedSwitchOperatorNumericToCT numeric: " + numeric + ", dataRoaming: " + dataRoaming + ", hasCdmaApp: " + hasCdmaApp);
            return VODAFONE_OPERATOR_NUMERIC.equals(numeric) && dataRoaming && hasCdmaApp;
        }

        static boolean isNeedRestartDataConnection(Phone phone) {
            if (isNeedSwitchOperatorNumericToCT(phone, phone.getOperatorNumeric()) && miui.telephony.SubscriptionManager.getDefault().getDefaultDataSlotId() == phone.getPhoneId()) {
                return true;
            }
            return false;
        }

        public static IccRecords getIccRecords(Phone phone) {
            IccRecords newIccRecords = getUiccRecords(phone, 2);
            if (newIccRecords == null || (!TextUtils.isEmpty(newIccRecords.getOperatorNumeric()) && !isChinaTelecomNumeric(newIccRecords.getOperatorNumeric()))) {
                return getUiccRecords(phone, 1);
            }
            return newIccRecords;
        }

        private static boolean isChinaTelecomNumeric(String numeric) {
            return CHINA_TELECOM_OPERATOR_NUMERIC.equals(numeric) || "45502".equals(numeric);
        }

        private static IccRecords getUiccRecords(Phone phone, int appFamily) {
            UiccController controller = UiccController.getInstance();
            if (controller == null) {
                return null;
            }
            return controller.getIccRecords(phone.getPhoneId(), appFamily);
        }

        static String getOperatorNumeric(IccRecords records, boolean isNvSubscription) {
            if (isNvSubscription) {
                return "";
            }
            String result = records != null ? records.getOperatorNumeric() : "";
            return result;
        }

        public static boolean isSupportDomesticRoamingWithoutSwitch(Phone phone) {
            String networkOperator = phone.getServiceState().getOperatorNumeric();
            String simOperator = phone.getOperatorNumeric();
            return "22299".equals(simOperator) && "22288".equals(networkOperator);
        }

        public static boolean isSupportDomesticRoaming(Phone phone) {
            return HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "support_domestic_roaming", false);
        }

        public static boolean onRoamingOnIfDomestic(Phone phone) {
            if (isSupportDomesticRoaming(phone) && phone.getServiceState().getDataRoamingType() == 2) {
                return miui.telephony.TelephonyManager.isDomesticRoamingEnable(phone.getContext());
            }
            return phone.getDataRoamingEnabled();
        }

        public static boolean isDomesticRoamingDisable(Phone phone) {
            return isSupportDomesticRoaming(phone) && !miui.telephony.TelephonyManager.isDomesticRoamingEnable(phone.getContext());
        }

        public static boolean isDomesticRoamingEnable(Phone phone) {
            return isSupportDomesticRoaming(phone) && phone.getServiceState().getDataRoamingType() == 2 && miui.telephony.TelephonyManager.isDomesticRoamingEnable(phone.getContext());
        }

        public static boolean shouldNotifyOffApnOfDomesticAvailability(Phone phone) {
            return isSupportDomesticRoaming(phone) && !miui.telephony.TelephonyManager.isDomesticRoamingEnable(phone.getContext()) && phone.getServiceState().getDataRoamingType() == 2;
        }

        static void log(Phone phone, String s) {
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "[" + phone.getPhoneId() + "]" + s);
        }

        public static boolean maybeSkipOrSetEmptyInitialAttachApn(Phone phone, DataServiceManager dsMgr, DataProfile dataProfile) {
            if (dataProfile == null) {
                return true;
            }
            ApnSetting apnSetting = dataProfile.getApnSetting();
            if (apnSetting.canHandleType(2) && !apnSetting.canHandleType(17)) {
                log(phone, "skip only can handle mms type apn");
                return true;
            }
            String numeric = apnSetting.getOperatorNumeric();
            boolean isEmptyIA = HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "empty_initial_attach_apn", false);
            log(phone, " isEmptyIA " + isEmptyIA);
            if (!isEmptyIA) {
                return false;
            }
            if ("20801".equals(numeric) && isExceptionalCondition(phone)) {
                return false;
            }
            int protocol = apnSetting.getProtocol();
            int roamingProtocol = apnSetting.getRoamingProtocol();
            if ("45005".equals(numeric) || ((MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING) && "22210".equals(numeric))) {
                protocol = 2;
                roamingProtocol = 2;
            }
            log(phone, "set empty initial attach apn for " + numeric);
            ApnSetting initialAttachApnSetting = new ApnSetting.Builder().setId(-1).setOperatorNumeric(numeric).setApnTypeBitmask(DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER).setProtocol(protocol).setRoamingProtocol(roamingProtocol).setCarrierEnabled(true).buildWithoutCheck();
            DataProfile iaDataProfile = new DataProfile.Builder().setApnSetting(initialAttachApnSetting).setTrafficDescriptor(new TrafficDescriptor(initialAttachApnSetting.getApnName(), null)).setPreferred(false).build();
            dsMgr.setInitialAttachApn(iaDataProfile, phone.getServiceState().getDataRoamingFromRegistration(), (Message) null);
            return true;
        }

        private static boolean isExceptionalCondition(Phone phone) {
            if (phone != null) {
                String iccid = phone.getIccSerialNumber();
                if ((iccid != null && iccid.startsWith("8981090")) || (phone.getIccCard() != null && "Rakuten".equals(phone.getIccCard().getServiceProviderName()))) {
                    log(phone, "skip for Rakuten SIM");
                    return true;
                }
                return false;
            }
            return false;
        }

        public static class MobileTxRxSum {
            public long rxPkts;
            public long txPkts;

            public MobileTxRxSum() {
                reset();
            }

            public MobileTxRxSum(long TxPkts, long RxPkts) {
                this.txPkts = TxPkts;
                this.rxPkts = RxPkts;
            }

            public MobileTxRxSum(MobileTxRxSum sum) {
                this.txPkts = sum.txPkts;
                this.rxPkts = sum.rxPkts;
            }

            public void reset() {
                this.txPkts = -1L;
                this.rxPkts = -1L;
            }

            public String toString() {
                return "{MobileTxRxSum TxPkts=" + this.txPkts + " RxPkts=" + this.rxPkts + "}";
            }

            public void updateTxRxSum() {
                this.txPkts = TrafficStats.getMobileTxPackets();
                this.rxPkts = TrafficStats.getMobileRxPackets();
            }

            public void updateDataStallTxRxSum() {
                this.txPkts = TrafficStats.getMobileTxPackets();
                this.rxPkts = TrafficStats.getMobileRxPackets();
            }

            public void updateDataActivityTxRxSum() {
                this.txPkts = TrafficStats.getMobileTxPackets();
                this.rxPkts = TrafficStats.getMobileRxPackets();
            }
        }

        private static boolean isMaxCountInAccelerateMode(long currentTime) {
            if (sAccelerateModeInitialTime == 0) {
                sAccelerateModeInitialTime = currentTime;
            } else {
                sAccelerateModeTime = currentTime;
            }
            sAccelerateModeCounter++;
            long intervalTime = sAccelerateModeTime - sAccelerateModeInitialTime;
            if (intervalTime >= 86400000) {
                sAccelerateModeInitialTime = currentTime;
                sAccelerateModeCounter = 1;
                intervalTime = 0;
            }
            return sAccelerateModeCounter > 10 && intervalTime < 86400000;
        }

        public static boolean isDdsForSubscription(int subId) {
            return subId == SubscriptionManager.getDefaultDataSubscriptionId();
        }

        public static int getDefaultAlwaysEnableMms(Context context) {
            String[] configArray = context.getResources().getStringArray(285409309);
            for (int i = 0; i < MiuiTelephony.PHONE_COUNT; i++) {
                TelephonyManager.getDefault();
                if (ArrayUtils.contains(configArray, TelephonyManager.getSimCountryIsoForPhone(i).toLowerCase())) {
                    return 0;
                }
            }
            return 1;
        }

        public static boolean isVirtualSeedSim(Phone phone) {
            String virtualsimIccid = Settings.Global.getString(phone.getContext().getContentResolver(), "virtual_seed_sim_iccid");
            if (TextUtils.isEmpty(virtualsimIccid)) {
                return false;
            }
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "virtual_seed_sim_iccid: " + virtualsimIccid);
            SubscriptionInfo subInfo = miui.telephony.SubscriptionManager.getDefault().getSubscriptionInfoForSubscription(phone.getSubId());
            if (subInfo == null) {
                return false;
            }
            return virtualsimIccid.equals(subInfo.getIccId());
        }

        public static void showDataUsageDialogCustomized(Phone phone, DataProfile dataProfile) {
            Context context = phone.getContext();
            boolean isShown = Settings.Global.getInt(context.getContentResolver(), DEVICE_FIRST_USING_DATA, 0) == 1;
            if (dataProfile.canSatisfy(12) && !isShown) {
                boolean showDataUsageDialog = HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "show_data_usage_dialog", false);
                if (showDataUsageDialog) {
                    Rlog.d(MiuiDataControllerImpl.LOG_TAG, "popupDialog: show dialog data usage for first time");
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(false);
                    builder.setMessage(286196135);
                    builder.setPositiveButton(R.string.ok, (DialogInterface.OnClickListener) null);
                    AlertDialog dialog = builder.create();
                    dialog.getWindow().setType(2008);
                    dialog.show();
                    Settings.Global.putInt(context.getContentResolver(), DEVICE_FIRST_USING_DATA, 1);
                }
            }
        }

        protected static void filterSpecialDunApn(Phone phone, List<DataProfile> dataProfiles, DataProfile preferredDataProfile, TelephonyNetworkRequest networkRequest) {
            if (!networkRequest.hasCapability(2)) {
                log(phone, "filterSpecialDunApn networkRequest not dun type");
                return;
            }
            log(phone, "filterSpecialDunApn preferred apn name is " + preferredDataProfile);
            filterSpecialDunApnForKDDI(phone, dataProfiles, preferredDataProfile);
            filterSpecialDunApnForDocomo(phone, dataProfiles);
            log(phone, "filterSpecialDunApn dataProfiles size: " + dataProfiles.size());
            for (DataProfile dp : dataProfiles) {
                log(phone, "filterSpecialDunApn dataProfiles dp: " + dp);
            }
        }

        private static void filterSpecialDunApnForKDDI(Phone phone, List<DataProfile> dataProfiles, DataProfile preferredDataProfile) {
            if (miui.telephony.TelephonyManager.isCustForJpKd() && phone != null && preferredDataProfile != null) {
                if (preferredDataProfile.getApnSetting() != null && preferredDataProfile.getApnSetting().hasMvnoParams()) {
                    return;
                }
                int dataNetworkType = phone.getServiceState().getDataNetworkType();
                boolean isGteLteNetworkType = isGteLteNetworkType(dataNetworkType);
                boolean isInternationalRoaming = phone.getServiceState().getDataRoamingType() == 3;
                log(phone, "filterSpecialDunApn dataNetworkType: " + dataNetworkType + ", isGteLteNetworkType: " + isGteLteNetworkType + ", isInternationalRoaming: " + isInternationalRoaming);
                if (isInternationalRoaming) {
                    dataProfiles.clear();
                    return;
                }
                if (KDDI_5G_NET_DEFAULT_APN_NAME.equals(preferredDataProfile.getApnSetting().getApnName())) {
                    List<DataProfile> dunDataProfile = getKddiDunDataProfile(KDDI_5G_NET_DUN_APN_NAME, dataProfiles);
                    dataProfiles.clear();
                    dataProfiles.addAll(dunDataProfile);
                } else {
                    if (KDDI_5G_NET_FOR_DATA_DEFAULT_APN_NAME.equals(preferredDataProfile.getApnSetting().getApnName())) {
                        List<DataProfile> dunDataProfile2 = getKddiDunDataProfile(KDDI_5G_NET_FOR_DATA_DUN_APN_NAME, dataProfiles);
                        dataProfiles.clear();
                        dataProfiles.addAll(dunDataProfile2);
                        return;
                    }
                    dataProfiles.clear();
                }
            }
        }

        private static void filterSpecialDunApnForDocomo(Phone phone, List<DataProfile> dataProfiles) {
            if (!"jp_rk".equals(SystemProperties.get("ro.miui.customized.region", "")) || phone == null || !"44010".equals(phone.getOperatorNumeric())) {
                return;
            }
            dataProfiles.clear();
        }

        private static List<DataProfile> getKddiDunDataProfile(String dunApnName, List<DataProfile> dataProfiles) {
            List<DataProfile> retDunDataProfiles = new ArrayList<>();
            if (!ArrayUtils.isEmpty(dataProfiles)) {
                for (DataProfile dp : dataProfiles) {
                    if (dp.getApnSetting() != null && dp.getApnSetting().canHandleType(8) && dunApnName.equals(dp.getApnSetting().getApnName())) {
                        retDunDataProfiles.add(dp);
                        break;
                    }
                }
            }
            return retDunDataProfiles;
        }

        protected static boolean isGteLteNetworkType(int dataNetworkType) {
            if (dataNetworkType == 20 || dataNetworkType == 19 || dataNetworkType == 13) {
                return true;
            }
            return false;
        }

        private static boolean isChinaOperatorNetApn(String apn) {
            return EXTRA_CMCC_APN_NAME.equals(apn) || EXTRA_CU_APN_NAME.equals(apn);
        }

        public static boolean isChinaOperatorWapApn(String apn) {
            return EXTRA_CMCC_WAP_APN_NAME.equals(apn) || EXTRA_CU_WAP_APN_NAME.equals(apn);
        }

        private static boolean isChinaDefaultApn(String numeric, TelephonyNetworkRequest networkRequest) {
            return (ServiceProviderUtils.isChinaMobile(numeric) || ServiceProviderUtils.isChinaUnicom(numeric)) && networkRequest.hasCapability(12);
        }

        public static void removeWapApn(List<DataProfile> dataProfiles, DataProfile preferredDataProfile, TelephonyNetworkRequest networkRequest) {
            DataProfile wapDataProfile = null;
            boolean hasNetApn = false;
            for (DataProfile dataProfile : dataProfiles) {
                ApnSetting apn = dataProfile.getApnSetting();
                if (apn != null && isChinaOperatorNetApn(apn.getApnName())) {
                    hasNetApn = true;
                }
                if (apn != null && isChinaOperatorWapApn(apn.getApnName())) {
                    wapDataProfile = dataProfile;
                }
            }
            if (wapDataProfile == null || preferredDataProfile != null || !hasNetApn || !isChinaDefaultApn(wapDataProfile.getApnSetting().getOperatorNumeric(), networkRequest)) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "no wap apn");
            } else {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "remove wap dataprofile");
                dataProfiles.remove(wapDataProfile);
            }
        }

        public static long getRetryTimerByDisconnectedCause(int cause, long delay) {
            String reason = DataFailCause.toString(cause);
            MiuiDataControllerImpl.trimCurrentTimeMillis(mDisconnectReasonList, 3600);
            long delayTime = delay;
            if (mDisconnectReasonList.size() > 3) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, ",getRetryTimerByDisconnectedCause  delayTime=" + delayTime);
                return delayTime;
            }
            if (isFastRetryReason(reason)) {
                if (delayTime != -1 && delayTime < 1000) {
                    return delayTime;
                }
                delayTime = 1000;
                mDisconnectReasonList.add(Long.valueOf(System.currentTimeMillis()));
                TelephonyStatAdapter.notifySetupDataFailRecovery(cause, SetupFailedOptScene.RETRY_TIME_RECOVERY.getValue());
            } else if (isTopDisconnectReason(reason)) {
                if (delayTime != -1 && delayTime < TOP_RETRY_AFTER_DISCONNECT_DELAY) {
                    return delayTime;
                }
                delayTime = TOP_RETRY_AFTER_DISCONNECT_DELAY;
                mDisconnectReasonList.add(Long.valueOf(System.currentTimeMillis()));
                TelephonyStatAdapter.notifySetupDataFailRecovery(cause, SetupFailedOptScene.RETRY_TIME_RECOVERY.getValue());
            }
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, ",getRetryTimerByDisconnectedCause  delayTime=" + delayTime);
            return delayTime;
        }

        private static boolean isFastRetryReason(String reason) {
            return DataFailCause.toString(65540).equals(reason) || "apnChanged".equals(reason) || isUnknownReason(reason);
        }

        private static boolean isUnknownReason(String reason) {
            return isSupportSmartNetworkAutonomy() && DataFailCause.toString(65536).equals(reason);
        }

        private static boolean isTopDisconnectReason(String reason) {
            return isSupportSmartNetworkAutonomy() && !isPermanentFailure(reason) && (DataFailCause.toString(ModemUtils.MODEM_STATS_DATA_INVALID).equals(reason) || DataFailCause.toString(65547).equals(reason) || DataFailCause.toString(2190).equals(reason) || DataFailCause.toString(2099).equals(reason) || DataFailCause.toString(2079).equals(reason) || DataFailCause.toString(2164).equals(reason) || DataFailCause.toString(33).equals(reason) || DataFailCause.toString(2218).equals(reason) || DataFailCause.toString(2217).equals(reason));
        }

        private static boolean isPermanentFailure(int cause) {
            String causeStr = DataFailCause.toString(cause);
            for (String failure : Constants.sFailure) {
                if (causeStr.startsWith(failure)) {
                    return true;
                }
            }
            return false;
        }

        private static boolean isDataThrottledScene(int phoneId, int cause) {
            MiuiDataControllerImpl miuiDataController;
            if (!isValidatePhoneId(phoneId) || cause != 2034 || (miuiDataController = (MiuiDataControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiDataControllerImpl.class, phoneId)) == null || !miuiDataController.isNrRat(phoneId)) {
                return false;
            }
            return true;
        }

        private static boolean isPermanentFailure(String causeStr) {
            for (String failure : Constants.sFailure) {
                if (causeStr.startsWith(failure)) {
                    return true;
                }
            }
            return false;
        }

        private static boolean isDualSimSwitchSupport(Phone phone) {
            boolean rst = false;
            if (phone == null) {
                return false;
            }
            int ddsPhoneId = DefaultSimManager.getDefaultDataSlotId();
            if (!SubscriptionManager.isValidPhoneId(ddsPhoneId) || !SubscriptionManager.isValidPhoneId(phone.getPhoneId())) {
                return false;
            }
            int smartDualSimSettings = Settings.Global.getInt(phone.getContext().getContentResolver(), SMART_DUAL_SIM, 0);
            if (getInsertedSimCount() == 2 && smartDualSimSettings == 1 && !isSameServicePrvider(phone.getPhoneId(), getAnotherPhoneId(phone.getPhoneId()))) {
                rst = true;
            }
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "isDualSimSwitchSupport:" + rst);
            return rst;
        }

        public static boolean isValidatePhoneId(int phoneId) {
            return phoneId >= 0 && phoneId < MiuiTelephony.PHONE_COUNT;
        }

        private static int getInsertedSimCount() {
            int insertedSimCount = 0;
            Phone[] phones = PhoneFactory.getPhones();
            if (phones != null) {
                for (Phone phone : phones) {
                    if (phone.getIccCard().hasIccCard()) {
                        insertedSimCount++;
                    }
                }
            }
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "getInsertedSimCount:" + insertedSimCount);
            return insertedSimCount;
        }

        private static int getAnotherPhoneId(int phoneId) {
            if (isValidatePhoneId(phoneId)) {
                return phoneId == 0 ? 1 : 0;
            }
            return -1;
        }

        private static boolean isSameServicePrvider(int ddsPhoneId, int nonDdsPhoneId) {
            if (!isValidatePhoneId(ddsPhoneId) || !isValidatePhoneId(nonDdsPhoneId)) {
                return true;
            }
            Phone ddsPhone = PhoneFactory.getPhone(ddsPhoneId);
            Phone nddsPhone = PhoneFactory.getPhone(nonDdsPhoneId);
            if (ddsPhone == null || nddsPhone == null) {
                return true;
            }
            String ddsOperatorNum = ddsPhone.getOperatorNumeric();
            String nonDdsOperatorNum = nddsPhone.getOperatorNumeric();
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "isSameServicePrvider:" + ddsOperatorNum + SarDeviceStateListener.SIM_MCC_DEFAULT_STATE + nonDdsOperatorNum);
            if (TextUtils.isEmpty(ddsOperatorNum) || TextUtils.isEmpty(nonDdsOperatorNum) || ddsOperatorNum.equals(nonDdsOperatorNum)) {
                return true;
            }
            return false;
        }

        public static boolean isValidMobileNetworkMonitor() {
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "isValidMobileNetworkMonitor Enter");
            if (Build.IS_INTERNATIONAL_BUILD || Build.IS_CM_CUSTOMIZATION_TEST || Build.IS_CU_CUSTOMIZATION_TEST || Build.IS_CT_CUSTOMIZATION_TEST) {
                return false;
            }
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "isValidMobileNetworkMonitor true");
            return true;
        }

        public static boolean isChinaApnName(String apnName, String reason) {
            if ((EXTRA_CU_APN_NAME.equals(apnName) || EXTRA_CT_APN_NAME.equals(apnName) || EXTRA_CMCC_APN_NAME.equals(apnName)) && NETWORK_INFO_REASON.equals(reason)) {
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "isChinaApnName true");
                return true;
            }
            return false;
        }

        public static boolean isIPv6OnlyDnsServer(Collection<InetAddress> dnses) {
            if (dnses != null && dnses.size() >= 1) {
                boolean hasIpV6 = false;
                boolean hasIpV4 = false;
                for (InetAddress ia : new ArrayList<>(dnses)) {
                    if (ia instanceof Inet4Address) {
                        hasIpV4 = true;
                    } else if (ia instanceof Inet6Address) {
                        hasIpV6 = true;
                    }
                }
                if (hasIpV6 && !hasIpV4) {
                    Rlog.d(MiuiDataControllerImpl.LOG_TAG, "isIPv6OnlyDnsServer true");
                    return true;
                }
                return false;
            }
            return false;
        }

        public static boolean dataAllowedOnRoaming(Phone phone) {
            boolean isDataAllowOnRoaming = false;
            String simOperator = phone.getOperatorNumeric();
            String regPlmn = phone.getServiceState().getOperatorNumeric();
            if (miui.telephony.TelephonyManager.isCustForHkH3g() && "22299".equals(simOperator) && "22288".equals(regPlmn) && phone.getServiceState().getDataRoaming()) {
                isDataAllowOnRoaming = true;
            }
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "isDataAllowOnRoaming=" + isDataAllowOnRoaming + ",simOperator =" + simOperator + ",regPlmn=" + regPlmn);
            return isDataAllowOnRoaming;
        }

        public static boolean mmsAllowedOnRoaming(Phone phone, boolean isMmsRequestActive) {
            if (phone == null || !isMmsRequestActive) {
                return false;
            }
            boolean config = CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_mms_allowed_on_roaming");
            return config;
        }

        public static boolean disAllowedOnRoaming(Phone phone, boolean isMmsRequestActive) {
            if (onRoamingOnIfDomestic(phone)) {
                return false;
            }
            return !mmsAllowedOnRoaming(phone, isMmsRequestActive);
        }

        private static boolean canHandleDefault(ApnSetting apnSetting) {
            if (apnSetting == null) {
                return false;
            }
            return apnSetting.canHandleType(17);
        }

        public static void restoreDefaultApnsIfNeeded(final Phone phone, ApnSetting apnSetting, TelephonyNetworkRequest networkRequest) {
            if (networkRequest.hasCapability(12) && !TextUtils.isEmpty(phone.getOperatorNumeric())) {
                boolean isCanHandleDefault = canHandleDefault(apnSetting);
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, "canHandleDefault(apnSetting)=" + isCanHandleDefault);
                if (!isCanHandleDefault) {
                    Thread restoreThread = new Thread() { // from class: com.android.internal.telephony.data.MiuiDataControllerImpl.Processor.1
                        @Override // java.lang.Thread, java.lang.Runnable
                        public void run() {
                            Context context = phone.getContext();
                            Uri DEFAULTAPN_URI = Uri.parse("content://telephony/carriers/restore");
                            ContentResolver resolver = context.getContentResolver();
                            int subId = phone.getSubId();
                            if (SubscriptionManager.isValidSubscriptionId(subId)) {
                                Processor.log(phone, "restore apns since apn list is empty");
                                Uri uri = Uri.withAppendedPath(DEFAULTAPN_URI, "subId/" + String.valueOf(subId));
                                resolver.delete(uri, null, null);
                                Map<String, String> params = new HashMap<>();
                                params.put("ApnEmpty", phone.getOperatorNumeric());
                                params.put("LastVersion", Processor.getPreferenceString(phone, Processor.PREFERENCE_KEY_VERSION));
                                TelephonyStatAdapter.notifyDataStatistic(TelephonyStatAdapter.DataStatType.DATA_APN_RECOVERY_CN, phone.getPhoneId(), params);
                            }
                        }
                    };
                    restoreThread.start();
                    MiEvent event = MiSight.constructEvent(915000003, new Object[]{"apnSetting", "", "Mcc", "", "Mnc", "", "Apn", "", "Type", "", "Protocol", "", "Roaming_protocol", ""});
                    sendEvent(event);
                    MiEvent Recovery_Event = MiSight.constructEvent(PROTOCOL_APN_ABNORMAL_RECOVERY_EVENT_CODE, new Object[]{MiEventUtils.RECOVERY_RESULT, "false", MiEventUtils.PROBLEM_CODE, 915000003});
                    sendEvent(Recovery_Event);
                }
            }
        }

        public static boolean shouldInitRecoveryApn(Phone phone) {
            return isRecoveryApnSupported() && isVersionChanged(phone) && !VirtualSimUtils.isVirtualSim(phone.getContext(), phone.getPhoneId());
        }

        public static void saveValidApn(Phone phone, ApnSetting apn, ApnSetting recoveryApn) {
            if (isVersionChanged(phone)) {
                updatePreferenceString(phone, PREFERENCE_KEY_VERSION, android.os.Build.VERSION.INCREMENTAL);
            }
            updateValidApn(phone, apn, false);
        }

        public static void recordApnRecoveryEvents(Phone phone, ApnSetting apnSetting, ApnSetting apnSetting2) {
            if (apnSetting2 != null && apnSetting != null) {
                HashMap map = new HashMap();
                map.put("RecoveryApnFailed", String.valueOf(apnSetting.equals(apnSetting2, false) ? 1 : 0));
                map.put("Network", apnSetting2.getOperatorNumeric());
                map.put("Apn", apnSetting2.getApnName());
                map.put("LastVersion", getPreferenceString(phone, PREFERENCE_KEY_VERSION));
                log(phone, "report apn recovery events");
                TelephonyStatAdapter.notifyDataStatistic(TelephonyStatAdapter.DataStatType.DATA_APN_RECOVERY_CN, phone.getPhoneId(), map);
            }
        }

        public static String getPreferenceString(Phone phone, String key) {
            if (TextUtils.isEmpty(key)) {
                return null;
            }
            SharedPreferences sp = getValidApnPreferences(phone);
            return sp.getString(key, "");
        }

        private static int portFromString(String strPort) {
            if (TextUtils.isEmpty(strPort)) {
                return -1;
            }
            try {
                int port = Integer.parseInt(strPort);
                return port;
            } catch (NumberFormatException e) {
                Rlog.e(MiuiDataControllerImpl.LOG_TAG, "Can't parse port from String");
                return -1;
            }
        }

        private static Uri UriFromString(String uri) {
            if (TextUtils.isEmpty(uri)) {
                return null;
            }
            return Uri.parse(uri);
        }

        protected static ApnSetting getPreferenceApnSetting(Phone phone, String key) {
            int version;
            int authType;
            char c;
            String protocol;
            char c2;
            String[] typeArray;
            int mtu;
            String protocol2;
            int carrierId;
            String roamingProtocol;
            int mtu2;
            int apnSetId;
            boolean carrierEnabled;
            int skip464xlat;
            if (TextUtils.isEmpty(key)) {
                return null;
            }
            SharedPreferences sp = getValidApnPreferences(phone);
            String data = sp.getString(key, "");
            if (data == null) {
                return null;
            }
            if (data.matches("^\\[ApnSettingV7\\]\\s*.*")) {
                version = 7;
                data = data.replaceFirst(V7_FORMAT_REGEX, "");
            } else if (data.matches("^\\[ApnSettingV6\\]\\s*.*")) {
                version = 6;
                data = data.replaceFirst(V6_FORMAT_REGEX, "");
            } else if (data.matches("^\\[ApnSettingV5\\]\\s*.*")) {
                version = 5;
                data = data.replaceFirst(V5_FORMAT_REGEX, "");
            } else if (data.matches("^\\[ApnSettingV4\\]\\s*.*")) {
                version = 4;
                data = data.replaceFirst(V4_FORMAT_REGEX, "");
            } else if (data.matches("^\\[ApnSettingV3\\]\\s*.*")) {
                version = 3;
                data = data.replaceFirst(V3_FORMAT_REGEX, "");
            } else if (data.matches("^\\[ApnSettingV2\\]\\s*.*")) {
                version = 2;
                data = data.replaceFirst(V2_FORMAT_REGEX, "");
            } else {
                version = 1;
            }
            String[] a = data.split("\\s*,\\s*", -1);
            if (a.length < 14) {
                return null;
            }
            try {
                int authType2 = Integer.parseInt(a[12]);
                authType = authType2;
            } catch (NumberFormatException e) {
                authType = 0;
            }
            int networkTypeBitmask = 0;
            int profileId = 0;
            boolean modemCognitive = false;
            int maxConns = 0;
            int waitTime = 0;
            int maxConnsTime = 0;
            int mtu3 = 0;
            String mvnoType = "";
            String mvnoMatchData = "";
            int apnSetId2 = 0;
            int carrierId2 = -1;
            if (version == 1) {
                c = 1;
                typeArray = new String[a.length - 13];
                System.arraycopy(a, 13, typeArray, 0, a.length - 13);
                roamingProtocol = "IP";
                c2 = 0;
                mtu = 0;
                carrierId = -1;
                mtu2 = 0;
                carrierEnabled = true;
                protocol = "IP";
                protocol2 = "";
                apnSetId = 0;
                skip464xlat = -1;
            } else {
                c = 1;
                if (a.length < 18) {
                    return null;
                }
                String[] typeArray2 = a[13].split("\\s*\\|\\s*");
                protocol = a[14];
                String roamingProtocol2 = a[15];
                boolean carrierEnabled2 = Boolean.parseBoolean(a[16]);
                int bearerBitmask = ServiceState.getBitmaskFromString(a[17]);
                c2 = 0;
                if (a.length > 22) {
                    modemCognitive = Boolean.parseBoolean(a[19]);
                    try {
                        profileId = Integer.parseInt(a[18]);
                        maxConns = Integer.parseInt(a[20]);
                        waitTime = Integer.parseInt(a[21]);
                        maxConnsTime = Integer.parseInt(a[22]);
                    } catch (NumberFormatException e2) {
                    }
                }
                if (a.length > 23) {
                    try {
                        mtu3 = Integer.parseInt(a[23]);
                    } catch (NumberFormatException e3) {
                    }
                }
                if (a.length > 25) {
                    mvnoType = a[24];
                    mvnoMatchData = a[25];
                }
                if (a.length > 26) {
                    networkTypeBitmask = ServiceState.getBitmaskFromString(a[26]);
                }
                if (a.length > 27) {
                    apnSetId2 = Integer.parseInt(a[27]);
                }
                if (a.length > 28) {
                    carrierId2 = Integer.parseInt(a[28]);
                }
                if (a.length > 29) {
                    try {
                        int skip464xlat2 = Integer.parseInt(a[29]);
                        typeArray = typeArray2;
                        mtu = mtu3;
                        protocol2 = mvnoMatchData;
                        carrierId = carrierId2;
                        roamingProtocol = roamingProtocol2;
                        mtu2 = bearerBitmask;
                        apnSetId = apnSetId2;
                        carrierEnabled = carrierEnabled2;
                        skip464xlat = skip464xlat2;
                    } catch (NumberFormatException e4) {
                        typeArray = typeArray2;
                        mtu = mtu3;
                        protocol2 = mvnoMatchData;
                        carrierId = carrierId2;
                        roamingProtocol = roamingProtocol2;
                        mtu2 = bearerBitmask;
                        apnSetId = apnSetId2;
                        carrierEnabled = carrierEnabled2;
                        skip464xlat = -1;
                    }
                } else {
                    typeArray = typeArray2;
                    mtu = mtu3;
                    protocol2 = mvnoMatchData;
                    carrierId = carrierId2;
                    roamingProtocol = roamingProtocol2;
                    mtu2 = bearerBitmask;
                    apnSetId = apnSetId2;
                    carrierEnabled = carrierEnabled2;
                    skip464xlat = -1;
                }
            }
            if (networkTypeBitmask == 0) {
                networkTypeBitmask = ServiceState.convertBearerBitmaskToNetworkTypeBitmask(mtu2);
            }
            return new ApnSetting.Builder().setId(-1).setOperatorNumeric(a[10] + a[11]).setEntryName(a[c2]).setApnName(a[c]).setProxyAddress(a[2]).setProxyPort(portFromString(a[3])).setMmsc(UriFromString(a[7])).setMmsProxyAddress(a[8]).setMmsProxyPort(portFromString(a[9])).setUser(a[4]).setPassword(a[5]).setAuthType(authType).setApnTypeBitmask(ApnSetting.getApnTypesBitmaskFromString(TextUtils.join(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE, typeArray))).setProtocol(ApnSetting.getProtocolIntFromString(protocol)).setRoamingProtocol(ApnSetting.getProtocolIntFromString(roamingProtocol)).setCarrierEnabled(carrierEnabled).setNetworkTypeBitmask(networkTypeBitmask).setProfileId(profileId).setModemCognitive(modemCognitive).setMaxConns(maxConns).setWaitTime(waitTime).setMaxConnsTime(maxConnsTime).setMtuV4(mtu).setMtuV6(mtu).setMvnoType(ApnSetting.getMvnoTypeIntFromString(mvnoType)).setMvnoMatchData(protocol2).setApnSetId(apnSetId).setCarrierId(carrierId).setSkip464Xlat(skip464xlat).build();
        }

        public static void updatePreferenceString(Phone phone, String key, String value) {
            if (TextUtils.isEmpty(key)) {
                return;
            }
            SharedPreferences sp = getValidApnPreferences(phone);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, value);
            editor.commit();
        }

        private static String formatApnSettingToString(ApnSetting apn) {
            if (apn == null || !apn.toString().contains("ApnSettingV7")) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("[ApnSettingV7] ").append(apn.getEntryName()).append(", ").append(apn.getApnName()).append(", ").append(nullToEmpty(apn.getProxyAddressAsString())).append(", ").append(apn.getProxyPort()).append(", ").append(apn.getUser()).append(", ").append(apn.getPassword()).append(", ").append("").append(", ").append(UriToString(apn.getMmsc())).append(", ").append(nullToEmpty(apn.getMmsProxyAddressAsString())).append(", ").append(apn.getMmsProxyPort()).append(", ").append(apn.getOperatorNumeric()).append(", ").append("").append(", ").append(apn.getAuthType());
            String[] types = ApnSetting.getApnTypesStringFromBitmask(apn.getApnTypeBitmask()).split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
            sb.append(", ").append(TextUtils.join(" | ", types)).append(", ").append(ApnSetting.getProtocolStringFromInt(apn.getProtocol())).append(", ").append(ApnSetting.getProtocolStringFromInt(apn.getRoamingProtocol())).append(", ").append(apn.isEnabled()).append(", ").append("").append(", ").append(apn.getProfileId()).append(", ").append(apn.isPersistent()).append(", ").append(apn.getMaxConns()).append(", ").append(apn.getWaitTime()).append(", ").append(apn.getMaxConnsTime()).append(", ").append(apn.getMtuV4()).append(", ").append(apn.getMtuV6()).append(", ").append(ApnSetting.getMvnoTypeStringFromInt(apn.getMvnoType())).append(", ").append(apn.getMvnoMatchData()).append(", ").append(convertNetworkTypeBitMaskToString(apn.getNetworkTypeBitmask())).append(", ").append(apn.getApnSetId()).append(", ").append(apn.getCarrierId()).append(", ").append(apn.getSkip464Xlat());
            return sb.toString();
        }

        private static String convertNetworkTypeBitMaskToString(int networkBitMask) {
            StringBuilder networks = new StringBuilder();
            int bitmisk = networkBitMask;
            int i = 1;
            while (bitmisk != 0) {
                if ((bitmisk & 1) == 1) {
                    networks.append(i).append("|");
                }
                bitmisk >>= 1;
                i++;
            }
            int size = networks.length();
            if (size != 0) {
                if (networks.lastIndexOf("|") == size - 1) {
                    networks.deleteCharAt(size - 1);
                }
                Rlog.d(MiuiDataControllerImpl.LOG_TAG, networks.toString());
            }
            return networks.toString();
        }

        private static SharedPreferences getValidApnPreferences(Phone phone) {
            return phone.getContext().getSharedPreferences(PREFERENCE_LAST_VALID_APN, 0);
        }

        private static String UriToString(Uri uri) {
            return uri == null ? "" : uri.toString();
        }

        private static String nullToEmpty(String value) {
            return TextUtils.isEmpty(value) ? "" : value;
        }

        private static boolean isVersionChanged(Phone phone) {
            String currentVersion = android.os.Build.VERSION.INCREMENTAL;
            String LastVersion = getPreferenceString(phone, PREFERENCE_KEY_VERSION);
            log(phone, "currentVersion =" + currentVersion + ", LastVersion = " + LastVersion);
            return (TextUtils.isEmpty(currentVersion) || currentVersion.equals(LastVersion)) ? false : true;
        }

        private static void updateValidApn(Phone phone, ApnSetting apn, boolean isRemove) {
            String operator = phone.getOperatorNumeric();
            log(phone, "updateValidApn: " + isRemove + ", operator: " + operator + ", apn is null:" + (apn == null));
            if (TextUtils.isEmpty(operator) || apn == null) {
                return;
            }
            try {
                String apnString = getPreferenceString(phone, operator);
                if (isRemove) {
                    if (!TextUtils.isEmpty(apnString)) {
                        ApnSetting validApn = getPreferenceApnSetting(phone, operator);
                        if (apn.equals(validApn, false)) {
                            updatePreferenceString(phone, operator, null);
                        }
                        return;
                    }
                    return;
                }
                if (TextUtils.isEmpty(apnString)) {
                    updatePreferenceString(phone, operator, formatApnSettingToString(apn));
                    return;
                }
                ApnSetting recoveryApn = getPreferenceApnSetting(phone, operator);
                boolean isSameApn = apn.equals(recoveryApn, false);
                log(phone, "same apn = " + isSameApn);
                if (!isSameApn) {
                    updatePreferenceString(phone, operator, formatApnSettingToString(apn));
                }
            } catch (Exception e) {
                log(phone, "updateValidApn Exception:" + e);
                updatePreferenceString(phone, operator, null);
            }
        }

        private static boolean isRecoveryApnSupported() {
            return true;
        }

        public static boolean isObsoletedNetwork(Phone phone) {
            ServiceState ss;
            if (Build.IS_INTERNATIONAL_BUILD || (ss = phone.getServiceState()) == null) {
                return false;
            }
            int[] oldRat = {1, 2, 4, 5, 6, 7, 8, 12, 16};
            return ArrayUtils.contains(oldRat, ss.getRilDataRadioTechnology());
        }

        public static void addIAApnSetting(String operator, List<DataProfile> dataProfiles) {
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "addIAApnSetting operator =" + operator + " dataProfiles=" + dataProfiles);
            if (operator != null && "20801".equals(operator) && dataProfiles != null) {
                ApnSetting iaApnSetting = new ApnSetting.Builder().setEntryName("ia").setId(-1).setOperatorNumeric(operator).setApnTypeBitmask(DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER).setProtocol(1).setRoamingProtocol(0).setCarrierEnabled(true).buildWithoutCheck();
                DataProfile iaDataProfile = new DataProfile.Builder().setApnSetting(iaApnSetting).setTrafficDescriptor(new TrafficDescriptor("", null)).setPreferred(false).build();
                dataProfiles.add(iaDataProfile);
            }
        }

        public static void notifyArrearsSimIfNeed(Phone phone, String redirectUrl) {
            ConnectivityManager cm;
            NetworkInfo ni;
            if (phone != null && isValidMobileNetworkMonitor() && (cm = (ConnectivityManager) phone.getContext().getSystemService("connectivity")) != null && (ni = cm.getActiveNetworkInfo()) != null && ni.isConnected() && ni.getType() == 0 && !TextUtils.isEmpty(redirectUrl)) {
                String extraInfo = ni.getExtraInfo();
                if ((EXTRA_CT_APN_NAME.equals(extraInfo) && redirectUrl.contains(CTNET_RECHARGE_PAGE)) || (EXTRA_CMCC_APN_NAME.equals(extraInfo) && redirectUrl.contains(CMNET_RECHARGE_PAGE))) {
                    log(phone, "notifyArrearsSimIfNeed slotId=" + phone.getPhoneId());
                    Intent intent = new Intent();
                    intent.setAction(ACTION_ARREARS_SIM_NOTIFICATION);
                    intent.setPackage(NOTIFACATION_RECEIVER_PACKAGE);
                    intent.putExtra(FIELD_SLOT_ID, phone.getPhoneId());
                    phone.getContext().sendBroadcast(intent);
                }
            }
        }

        private static MiuiIccPhoneBookInterfaceManager getIccPhoneBookService(int slotId) {
            MiuiUiccControllerImpl uiccImpl = (MiuiUiccControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiUiccControllerImpl.class, slotId);
            return uiccImpl.getMiuiIccPhoneBookInterfaceManager();
        }

        public static boolean isPhoneBookReady(Phone phone) {
            MiuiIccPhoneBookInterfaceManager phoneBook = getIccPhoneBookService(phone.getPhoneId());
            if (phoneBook != null) {
                return phoneBook.isPhoneBookReady();
            }
            return false;
        }

        public static boolean hasSpecialFDN(Phone phone, String fdnStr) {
            List<MiuiAdnRecord> adnRecords = null;
            boolean bMatched = false;
            try {
                MiuiIccPhoneBookInterfaceManager phoneBook = getIccPhoneBookService(phone.getPhoneId());
                if (phoneBook != null) {
                    adnRecords = phoneBook.getAdnRecordsInEf(28475);
                } else {
                    log(phone, "phoneBook is null...");
                }
            } catch (SecurityException e) {
                log(phone, Log.getStackTraceString(e));
            }
            if (adnRecords != null && adnRecords.size() > 0) {
                for (MiuiAdnRecord adnRecord : adnRecords) {
                    if (!TextUtils.isEmpty(adnRecord.getNumber())) {
                        log(phone, "hasSpecialFDN strFdnNumber = " + adnRecord.getNumber());
                        if (adnRecord.getNumber().equals(fdnStr)) {
                            bMatched = true;
                            break;
                        }
                    }
                }
            }
            log(phone, "hasSpecialFDN bMatched = " + bMatched);
            return bMatched;
        }

        public static boolean mvnoMatchesCustomized(IccRecords r, String mvnoType, String mvnoMatchData) {
            boolean matched = false;
            if (r == null) {
                return false;
            }
            String gid1 = r.getGid1();
            int mvno_match_data_length = mvnoMatchData.length();
            if (gid1 != null && gid1.length() >= mvno_match_data_length && (("20826".equals(r.getOperatorNumeric()) || "20601".equals(r.getOperatorNumeric()) || "20801".equals(r.getOperatorNumeric())) && "4E524A31".equalsIgnoreCase(gid1) && gid1.equalsIgnoreCase(mvnoMatchData))) {
                matched = true;
            }
            Rlog.d(MiuiDataControllerImpl.LOG_TAG, "matched=" + matched);
            return matched;
        }

        public static boolean mvnoMatchesCustomized(IccRecords r, int mvnoType, String mvnoMatchData) {
            if (r != null && mvnoType == 0 && miui.telephony.TelephonyManager.isCustForKrOps()) {
                TelephonyManager tm = TelephonyManager.getDefault();
                int phoneCount = tm.getPhoneCount();
                for (int phId = 0; phId < phoneCount; phId++) {
                    Phone phone = PhoneFactory.getPhone(phId);
                    IccRecords iccRecords = UiccController.getInstance().getIccRecords(SubscriptionManager.getPhoneId(phone.getSubId()), 1);
                    String operatorNumeric = r.getOperatorNumeric();
                    if (operatorNumeric == null || operatorNumeric.length() < 3) {
                        Rlog.d(MiuiDataControllerImpl.LOG_TAG, "invalid kr SIM numeric");
                        return false;
                    }
                    if ("450".equals(operatorNumeric.substring(0, 3)) && iccRecords == r && TelephonyManager.getDefault().isNetworkRoaming(phone.getSubId()) && "roaming".equals(mvnoMatchData)) {
                        Rlog.d(MiuiDataControllerImpl.LOG_TAG, "Korean apn matched");
                        return true;
                    }
                }
            }
            return false;
        }

        public static boolean ignoreTetheringDataProfile(Phone phone) {
            if (!miui.telephony.TelephonyManager.isCustForJpKd() && phone != null && MiuiDataControllerImpl.KDDI_MCC_MNC.contains(phone.getOperatorNumeric())) {
                return true;
            }
            return false;
        }

        public static boolean ignoreDataSetupRetry(Phone phone, DataProfile dp, int cause) {
            boolean ignore = HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "config_ignore_data_setup_retry", false);
            return ignore && dp != null && dp.getApnSetting() != null && dp.getApnSetting().canHandleType(8) && (cause == 33 || cause == 4097 || cause == 29 || cause == 2034);
        }
    }

    public boolean isDataRoamingEnabled(Phone phone) {
        boolean alwaysEnableDataRoaming = HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "always_enable_data_roaming", false);
        if (!alwaysEnableDataRoaming || phone.getServiceState().getDataRoamingType() != 2) {
            return false;
        }
        log("Roaming is always enabled for turkey's carriers in domestic roaming no matter the roaming switch is on or off");
        return true;
    }

    public void tearDownDunDataNetworkIfNeeded(Phone phone, int reason, Object obj) {
        boolean isTearDownDunDataNetwork = HyperOSCustFeatureResolve.getBooleanForPhoneId(phone.getPhoneId(), "config_tear_down_dun_data_network", false);
        if (!isTearDownDunDataNetwork) {
            log("No need tear down dun datanetwork due to is not KDDI verison or KDDI sim");
            return;
        }
        if (reason != 26) {
            log("No need tear down dun dataNetwork due to not preferred profile change");
            return;
        }
        DataNetwork dunDataNetwork = getActiveDataNetworkByCapability(2);
        if (dunDataNetwork != null) {
            dunDataNetwork.tearDown(reason);
            log("Tear down dun dataNetwork due to: " + reason);
        } else {
            log("No need tear down dun dataNetwork due to is already inactive");
        }
    }

    public boolean isNotNeedPreferredApn(Phone phone, TelephonyNetworkRequest networkRequest) {
        boolean notNeedPreferred = false;
        try {
            Resources res = SubscriptionManager.getResourcesForSubId(phone.getContext(), phone.getSubId());
            if (res != null) {
                notNeedPreferred = res.getBoolean(R.bool.config_dreamsEnabledByDefault);
            }
        } catch (Resources.NotFoundException e) {
            log("notNeedPreferred NotFoundException set to false");
        }
        return notNeedPreferred && networkRequest.hasCapability(0);
    }

    public void recordDataInterruptionRecoveryMeasures(Phone phone, boolean isValid, String recoveryAction, int timeDuration, int timeDurationOfCurrentAction) {
        Map<String, String> params = new HashMap<>();
        params.put("isValid", String.valueOf(isValid));
        params.put("recoveryAction", recoveryAction);
        params.put("timeDuration", String.valueOf(timeDuration));
        params.put("timeDurationOfCurrentAction", String.valueOf(timeDurationOfCurrentAction));
        TelephonyStatAdapter.notifyDataStatistic(TelephonyStatAdapter.DataStatType.DATA_INTERRUPTION_RECOVERY_ACTION, phone.getPhoneId(), params);
    }

    public void onDataCallListChanged(Phone phone, int cause, String dnn) {
        TelephonyStatAdapter.notifyDataCallList(phone.getPhoneId(), cause, dnn);
    }
}
