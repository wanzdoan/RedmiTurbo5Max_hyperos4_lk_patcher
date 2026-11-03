package com.mediatek.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.radio.V1_0.HardwareConfigModem;
import android.hardware.radio.V1_0.HardwareConfigSim;
import android.hardware.radio.V1_0.IRadio;
import android.hardware.radio.V1_0.RadioResponseInfo;
import android.hardware.radio.data.IRadioData;
import android.hardware.radio.ims.IRadioIms;
import android.hardware.radio.messaging.IRadioMessaging;
import android.hardware.radio.modem.IRadioModem;
import android.hardware.radio.network.IRadioNetwork;
import android.hardware.radio.sim.IRadioSim;
import android.hardware.radio.voice.IRadioVoice;
import android.net.LinkProperties;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.IBinder;
import android.os.IHwBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyManager;
import android.telephony.data.DataProfile;
import android.telephony.data.NetworkSliceInfo;
import android.text.TextUtils;
import android.util.SparseArray;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.HalVersion;
import com.android.internal.telephony.HardwareConfig;
import com.android.internal.telephony.MiuiNetworkControllerStub;
import com.android.internal.telephony.ModemIndication;
import com.android.internal.telephony.NetworkIndication;
import com.android.internal.telephony.NetworkResponse;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConfigurationManager;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.RILRequest;
import com.android.internal.telephony.RILUtils;
import com.android.internal.telephony.RadioIndication;
import com.android.internal.telephony.RadioResponse;
import com.android.internal.telephony.RadioServiceProxy;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.internal.util.function.TriFunction;
import com.mediatek.internal.telephony.data.PlmnMvnoData;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import com.mediatek.internal.telephony.phb.MtkAdnRecordCache;
import com.mediatek.internal.telephony.phb.PBEntry;
import com.mediatek.internal.telephony.phb.PhbEntry;
import com.mediatek.internal.telephony.rsu.RsuRequestData;
import com.mediatek.internal.telephony.uicc.MtkSIMRecords;
import com.mediatek.internal.telephony.uicc.SimTypeInfoExt;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import mediatek.telephony.MtkSmsParameters;
import vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioEx;
import vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData;
import vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging;
import vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem;
import vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork;
import vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim;
import vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice;

/* JADX INFO: loaded from: classes.dex */
public class MtkRIL extends RIL {
    public static final String CB_FACILITY_BA_ACR = "ACR";
    public static final int CF_REASON_NOT_REGISTERED = 6;
    public static final int DISPLAY_EONS = 1;
    public static final int DISPLAY_NITZ = 2;
    public static final int DISPLAY_SPN = 8;
    public static final int DISPLAY_TS25 = 4;
    protected static final int EVENT_MTKRADIOEX_AIDL_PROXY_DEAD = 1007;
    protected static final int EVENT_MTKRADIOEX_CHECK_RADIO_STATE = 1009;
    protected static final int EVENT_MTKRADIOEX_MULTI_SIM_CONFIG_CHANGED = 1010;
    protected static final int EVENT_MTKRADIOEX_PROXY_DEAD = 1006;
    protected static final int EVENT_MTKRADIO_RETRY_GET_SERVICE = 1008;
    protected static final int IMTKRADIOEX_GET_SERVICE_DELAY_MILLIS = 1000;
    protected static final int MTKRADIOEX_CHECK_RADIO_STATE_DELAY_MILLIS = 120000;
    public static final int MTK_DATA_SERVICE = 1;
    public static final int MTK_MAX_SERVICE_IDX = 6;
    public static final int MTK_MESSAGING_SERVICE = 2;
    public static final int MTK_MIN_SERVICE_IDX = 0;
    public static final int MTK_MODEM_SERVICE = 3;
    public static final int MTK_NETWORK_SERVICE = 4;
    public static final int MTK_RADIO_SERVICE = 0;
    static final boolean MTK_RILJ_LOGD = true;
    static final boolean MTK_RILJ_LOGV = false;
    public static final int MTK_SIM_SERVICE = 5;
    public static final int MTK_VOICE_SERVICE = 6;
    static final String RILJ_LOG_TAG = "MtkRILJ";
    public static final int SERVICE_CLASS_LINE2 = 256;
    public static final int SERVICE_CLASS_MTK_MAX = 512;
    public static final int SERVICE_CLASS_VIDEO = 512;
    private ArrayList<String> hide_plmns;
    protected RegistrantList m5gUWInfoRegistrants;
    private boolean mAllRadioServiceReady;
    protected RegistrantList mBipProCmdRegistrant;
    protected RegistrantList mCallAdditionalInfoRegistrants;
    protected RegistrantList mCallForwardingInfoRegistrants;
    protected Registrant mCallRelatedSuppSvcRegistrant;
    protected RegistrantList mCardDetectedIndRegistrant;
    Object mCfuReturnValue;
    protected RegistrantList mCipherIndicationRegistrants;
    protected RegistrantList mCsNetworkStateRegistrants;
    protected RegistrantList mDsbpStateRegistrant;
    protected RegistrantList mDsdaStateRegistrant;
    protected RegistrantList mEconfSrvccRegistrants;
    protected RegistrantList mFemtoCellInfoRegistrants;
    protected RegistrantList mIWlanStateRegistrants;
    private String mIccid;
    protected RegistrantList mIccidRegistrants;
    protected RegistrantList mImeiLockRegistrant;
    protected RegistrantList mImsiRefreshDoneRegistrant;
    protected Registrant mIncomingCallIndicationRegistrant;
    public Integer mInstanceId;
    BroadcastReceiver mIntentReceiver;
    protected RegistrantList mInvalidSimInfoRegistrant;
    public boolean mIsCardDetected;
    private boolean mIsFirstInitialized;
    public boolean mIsSmsReady;
    protected SparseArray<int[]> mLatestQualifiedNetworkTypes;
    protected RegistrantList mMccMncRegistrants;
    protected RegistrantList mMdDataRetryCountResetRegistrants;
    protected Registrant mMeSmsFullRegistrant;
    protected RegistrantList mMobileDataUsageRegistrants;
    protected RegistrantList mModulationRegistrants;
    protected Context mMtkContext;
    private final SparseArray<MtkBinderServiceDeathRecipient> mMtkDeathRecipients;
    private MtkRadioExDataIndication mMtkRadioExDataIndication;
    private MtkRadioExDataResponse mMtkRadioExDataResponse;
    MtkRadioExIndication mMtkRadioExIndication;
    private MtkRadioExMessagingIndication mMtkRadioExMessagingIndication;
    private MtkRadioExMessagingResponse mMtkRadioExMessagingResponse;
    private MtkRadioExModemIndication mMtkRadioExModemIndication;
    private MtkRadioExModemResponse mMtkRadioExModemResponse;
    private MtkRadioExNetworkIndication mMtkRadioExNetworkIndication;
    private MtkRadioExNetworkResponse mMtkRadioExNetworkResponse;
    protected final MtkRadioExProxyDeathRecipient mMtkRadioExProxyDeathRecipient;
    MtkRadioExResponse mMtkRadioExResponse;
    private MtkRadioExSimIndication mMtkRadioExSimIndication;
    private MtkRadioExSimResponse mMtkRadioExSimResponse;
    private MtkRadioExVoiceIndication mMtkRadioExVoiceIndication;
    private MtkRadioExVoiceResponse mMtkRadioExVoiceResponse;
    MtkRadioIndication mMtkRadioIndication;
    MtkRadioResponse mMtkRadioResponse;
    private boolean mMtkRilJIntiDone;
    private IMtkRilOp mMtkRilOp;
    private final SparseArray<AtomicLong> mMtkServiceCookies;
    protected RegistrantList mNetworkEventRegistrants;
    protected RegistrantList mNetworkInfoRegistrant;
    protected RegistrantList mNetworkRejectRegistrants;
    protected RegistrantList mNrSysInfoRegistrants;
    protected RegistrantList mNwCfgInfoRegistrants;
    protected RegistrantList mNwLimitRegistrants;
    public RegistrantList mPhbReadyRegistrants;
    protected RegistrantList mPlmnDataRegistrants;
    private PlmnMvnoData mPlmnMvnoData;
    protected RegistrantList mPsNetworkStateRegistrants;
    protected RegistrantList mPseudoCellInfoRegistrants;
    protected RegistrantList mQualifiedNetworkTypesRegistrant;
    volatile IMtkRadioEx mRadioProxyMtk;
    protected Map<Integer, HalVersion> mRadioVersionMtk;
    protected RegistrantList mRsuSimlockRegistrants;
    private SparseArray<MtkRadioExServiceProxy> mServiceProxiesMtk;
    protected RegistrantList mSignalStrengthWithWcdmaEcioRegistrants;
    protected RegistrantList mSimMissing;
    protected RegistrantList mSimPlugIn;
    protected RegistrantList mSimPlugOut;
    protected RegistrantList mSimPowerChanged;
    Object mSimPowerInfo;
    protected RegistrantList mSimRecovery;
    private SimTypeInfoExt mSimTypeInfo;
    protected RegistrantList mSimTypeRegistrants;
    Object mSmlSlotLockInfo;
    protected RegistrantList mSmlSlotLockInfoChanged;
    protected RegistrantList mSmsInfoExtRegistrants;
    protected RegistrantList mSmsReadyRegistrants;
    protected Registrant mSsnExRegistrant;
    protected RegistrantList mStkSetupMenuResetRegistrant;
    protected RegistrantList mTxPowerRegistrant;
    protected RegistrantList mTxPowerStatusRegistrant;
    protected Registrant mUnsolOemHookRegistrant;
    protected RegistrantList mVirtualSimOff;
    protected RegistrantList mVirtualSimOn;
    protected RegistrantList mVsimIndicationRegistrants;
    static final String[] HIDL_SERVICE_NAME_MTK = {"mtkSlot1", "mtkSlot2", "mtkSlot3"};
    static final String[] AIDL_SERVICE_NAME_MTK = {"slot1", "slot2", "slot3"};
    public static final HalVersion RADIO_HAL_VERSION_MTK_UNKNOWN = HalVersion.UNKNOWN;
    public static final HalVersion RADIO_HAL_VERSION_MTK_3_0 = new HalVersion(3, 0);
    public static final HalVersion RADIO_HAL_VERSION_MTK_4_0 = new HalVersion(4, 0);
    public static final boolean showRat = TelephonyBaseUtilsStub.isMiuiRom();
    private static final String[] IGNORE_SET_EONS_NAME_NUMERIC_ARRAY = {"50212"};

    private static boolean isIgnoreSetEonsName(String operatorNumeric) {
        for (String numeric : IGNORE_SET_EONS_NAME_NUMERIC_ARRAY) {
            if (numeric.equals(operatorNumeric)) {
                return MTK_RILJ_LOGD;
            }
        }
        return MTK_RILJ_LOGV;
    }

    public class MtkRilHandler extends RIL.RilHandler {
        public MtkRilHandler() {
            super(MtkRIL.this);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 7:
                    int aidlService = msg.arg1;
                    MtkRIL.this.mAllRadioServiceReady = MtkRIL.MTK_RILJ_LOGV;
                    if (((HalVersion) MtkRIL.this.mHalVersion.get(Integer.valueOf(aidlService))).less(RIL.RADIO_HAL_VERSION_2_0) && !((HalVersion) MtkRIL.this.mHalVersion.get(Integer.valueOf(aidlService))).equals(RIL.RADIO_HAL_VERSION_UNKNOWN)) {
                        MtkRIL.this.mtkRiljLogi("Ignore EVENT_AIDL_PROXY_DEAD for HIDL version.");
                    } else {
                        super.handleMessage(msg);
                    }
                    break;
                case 1006:
                    MtkRIL.this.mtkRiljLogi("handleMessage: EVENT_MTKRADIOEX_PROXY_DEAD cookie = " + msg.obj + " mMtkServiceCookies = " + MtkRIL.this.mMtkServiceCookies.get(0));
                    if (((Long) msg.obj).longValue() == ((AtomicLong) MtkRIL.this.mMtkServiceCookies.get(0)).get()) {
                        IRadio iRadio = MtkRIL.this.mRadioProxy;
                        MtkRIL mtkRIL = MtkRIL.this;
                        if (iRadio == null) {
                            mtkRIL.mtkRiljLogi("handleMessage: wait for getting mRadioProxy");
                            MtkRIL.this.mRilHandler.removeMessages(1006);
                            MtkRIL.this.mRilHandler.sendMessageDelayed(MtkRIL.this.mRilHandler.obtainMessage(1006, Long.valueOf(((AtomicLong) MtkRIL.this.mMtkServiceCookies.get(0)).get())), 1000L);
                        } else {
                            mtkRIL.resetMtkProxyAndRequestList(0);
                        }
                    }
                    break;
                case 1007:
                    int mtkAidlService = msg.arg1;
                    AtomicLong obj = (AtomicLong) msg.obj;
                    MtkRIL.this.mtkRiljLogi("handleMessage: EVENT_MTKRADIOEX_AIDL_PROXY_DEAD cookie = " + msg.obj + ", service = " + MtkRIL.mtkServiceToString(mtkAidlService) + ", cookie = " + MtkRIL.this.mMtkServiceCookies.get(mtkAidlService));
                    if (obj.get() == ((AtomicLong) MtkRIL.this.mMtkServiceCookies.get(mtkAidlService)).get()) {
                        MtkRIL.this.resetMtkProxyAndRequestList(mtkAidlService);
                    }
                    break;
                case 1008:
                case 1010:
                    MtkRIL.this.mtkRiljLogi("handleMessage: retry get service, msg.what = " + msg.what);
                    MtkRIL.this.mRilHandler.removeMessages(1008);
                    if (SubscriptionManager.isValidPhoneId(MtkRIL.this.mPhoneId.intValue())) {
                        boolean zIsRildReady = MtkRIL.this.isRildReady();
                        MtkRIL mtkRIL2 = MtkRIL.this;
                        if (zIsRildReady) {
                            mtkRIL2.getAllRadioServices();
                        } else {
                            mtkRIL2.mRilHandler.sendMessageDelayed(MtkRIL.this.mRilHandler.obtainMessage(1008), 1000L);
                        }
                    }
                    break;
                case 1009:
                    if (!TelephonyUtils.IS_USER) {
                        MtkRIL.this.mRilHandler.removeMessages(1009);
                        int radioState = MtkRIL.this.getRadioState();
                        if (radioState == 2) {
                            if (((HalVersion) MtkRIL.this.mHalVersion.get(0)).greaterOrEqual(RIL.RADIO_HAL_VERSION_2_0) && MtkRIL.this.isRildReady()) {
                                MtkRIL.this.mtkRiljLoge("Radio unavailable 2 mins after reset. reset proxy.");
                                for (int service = 0; service <= 7; service++) {
                                    MtkRIL.this.resetProxyAndRequestList(service);
                                }
                                for (int service2 = 0; service2 <= 6; service2++) {
                                    MtkRIL.this.resetMtkProxyAndRequestList(service2);
                                }
                            } else {
                                MtkRIL.this.mtkRiljLoge("isRildReady() == false or HIDL version: " + MtkRIL.this.mHalVersion.get(0));
                            }
                        }
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    final class MtkRadioExProxyDeathRecipient implements IHwBinder.DeathRecipient {
        MtkRadioExProxyDeathRecipient() {
        }

        public void serviceDied(long cookie) {
            MtkRIL.this.riljLog("IMtkRadioEx serviceDied");
            MtkRIL.this.mRilHandler.removeMessages(1006);
            MtkRIL.this.mRilHandler.sendMessageDelayed(MtkRIL.this.mRilHandler.obtainMessage(1006, Long.valueOf(((AtomicLong) MtkRIL.this.mMtkServiceCookies.get(0)).get())), 1000L);
        }
    }

    private final class MtkBinderServiceDeathRecipient implements IBinder.DeathRecipient {
        private IBinder mBinder;
        private final int mService;

        MtkBinderServiceDeathRecipient(int service) {
            this.mService = service;
        }

        public void linkToDeath(IBinder service) throws RemoteException {
            if (service != null) {
                this.mBinder = service;
                this.mBinder.linkToDeath(this, (int) ((AtomicLong) MtkRIL.this.mMtkServiceCookies.get(this.mService)).incrementAndGet());
            }
        }

        public synchronized void unlinkToDeath() {
            if (this.mBinder != null) {
                this.mBinder.unlinkToDeath(this, 0);
                this.mBinder = null;
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            MtkRIL.this.mtkRiljLogi("Service " + MtkRIL.mtkServiceToString(this.mService) + " has died.");
            MtkRIL.this.mRilHandler.sendMessage(MtkRIL.this.mRilHandler.obtainMessage(1007, this.mService, 0, MtkRIL.this.mMtkServiceCookies.get(this.mService)));
            unlinkToDeath();
        }
    }

    public MtkRIL() {
        this.mRadioProxyMtk = null;
        this.mServiceProxiesMtk = new SparseArray<>();
        this.mMtkRilJIntiDone = MTK_RILJ_LOGV;
        this.mCallAdditionalInfoRegistrants = new RegistrantList();
        this.mCipherIndicationRegistrants = new RegistrantList();
        this.mFemtoCellInfoRegistrants = new RegistrantList();
        this.mPhbReadyRegistrants = new RegistrantList();
        this.mCallForwardingInfoRegistrants = new RegistrantList();
        this.mTxPowerRegistrant = new RegistrantList();
        this.mTxPowerStatusRegistrant = new RegistrantList();
        this.mCfuReturnValue = null;
        this.mIccidRegistrants = new RegistrantList();
        this.mSimTypeRegistrants = new RegistrantList();
        this.mMtkRilOp = null;
        this.mRadioVersionMtk = new HashMap();
        this.mAllRadioServiceReady = MTK_RILJ_LOGV;
        this.mIsFirstInitialized = MTK_RILJ_LOGD;
        this.mMtkDeathRecipients = new SparseArray<>();
        this.mMtkServiceCookies = new SparseArray<>();
        this.mIccid = null;
        this.mSimTypeInfo = null;
        this.mMdDataRetryCountResetRegistrants = new RegistrantList();
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.MtkRIL.1
            private static final int MODE_CDMA_ASSERT = 31;
            private static final int MODE_CDMA_RESET = 32;
            private static final int MODE_CDMA_RILD_NE = 103;
            private static final int MODE_GSM_RILD_NE = 101;
            private static final int MODE_PHONE_PROCESS_JE = 100;

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("com.mtk.TEST_TRM")) {
                    int mode = intent.getIntExtra("mode", 2);
                    Rlog.d(MtkRIL.RILJ_LOG_TAG, "RIL received com.mtk.TEST_TRM, mode = " + mode + ", mInstanceIds = " + MtkRIL.this.mInstanceId);
                    if (mode == 100) {
                        throw new RuntimeException("UserTriggerPhoneJE");
                    }
                    MtkRIL.this.setTrm(mode, null);
                    return;
                }
                Rlog.w(MtkRIL.RILJ_LOG_TAG, "RIL received unexpected Intent: " + intent.getAction());
            }
        };
        this.hide_plmns = new ArrayList<>();
        this.mCsNetworkStateRegistrants = new RegistrantList();
        this.mSignalStrengthWithWcdmaEcioRegistrants = new RegistrantList();
        this.mVirtualSimOn = new RegistrantList();
        this.mVirtualSimOff = new RegistrantList();
        this.mImeiLockRegistrant = new RegistrantList();
        this.mImsiRefreshDoneRegistrant = new RegistrantList();
        this.mCardDetectedIndRegistrant = new RegistrantList();
        this.mRsuSimlockRegistrants = new RegistrantList();
        this.mInvalidSimInfoRegistrant = new RegistrantList();
        this.mNetworkEventRegistrants = new RegistrantList();
        this.mNetworkRejectRegistrants = new RegistrantList();
        this.mModulationRegistrants = new RegistrantList();
        this.mIsSmsReady = MTK_RILJ_LOGV;
        this.mSmsReadyRegistrants = new RegistrantList();
        this.mSmsInfoExtRegistrants = new RegistrantList();
        this.mIsCardDetected = MTK_RILJ_LOGV;
        this.mPsNetworkStateRegistrants = new RegistrantList();
        this.mNetworkInfoRegistrant = new RegistrantList();
        this.mPseudoCellInfoRegistrants = new RegistrantList();
        this.mBipProCmdRegistrant = new RegistrantList();
        this.mStkSetupMenuResetRegistrant = new RegistrantList();
        this.mSimPlugIn = new RegistrantList();
        this.mSimPlugOut = new RegistrantList();
        this.mSimMissing = new RegistrantList();
        this.mSimRecovery = new RegistrantList();
        this.mSimPowerChanged = new RegistrantList();
        this.mSimPowerInfo = null;
        this.mSmlSlotLockInfoChanged = new RegistrantList();
        this.mSmlSlotLockInfo = null;
        this.mEconfSrvccRegistrants = new RegistrantList();
        this.mMccMncRegistrants = new RegistrantList();
        this.mVsimIndicationRegistrants = new RegistrantList();
        this.mNwCfgInfoRegistrants = new RegistrantList();
        this.mDsbpStateRegistrant = new RegistrantList();
        this.mDsdaStateRegistrant = new RegistrantList();
        this.mQualifiedNetworkTypesRegistrant = new RegistrantList();
        this.mLatestQualifiedNetworkTypes = new SparseArray<>();
        this.mMobileDataUsageRegistrants = new RegistrantList();
        this.mNwLimitRegistrants = new RegistrantList();
        this.mPlmnDataRegistrants = new RegistrantList();
        this.mPlmnMvnoData = null;
        this.mIWlanStateRegistrants = new RegistrantList();
        this.m5gUWInfoRegistrants = new RegistrantList();
        this.mNrSysInfoRegistrants = new RegistrantList();
        this.mMtkRadioExProxyDeathRecipient = null;
    }

    public MtkRIL(Context context, int allowedNetworkTypes, Integer instanceId, FeatureFlags flags) {
        this(context, allowedNetworkTypes, instanceId, null, flags);
    }

    public MtkRIL(Context context, int allowedNetworkTypes, Integer instanceId, SparseArray<MtkRadioExServiceProxy> proxies, FeatureFlags flags) {
        super(context, allowedNetworkTypes, instanceId, flags);
        this.mRadioProxyMtk = null;
        this.mServiceProxiesMtk = new SparseArray<>();
        this.mMtkRilJIntiDone = MTK_RILJ_LOGV;
        this.mCallAdditionalInfoRegistrants = new RegistrantList();
        this.mCipherIndicationRegistrants = new RegistrantList();
        this.mFemtoCellInfoRegistrants = new RegistrantList();
        this.mPhbReadyRegistrants = new RegistrantList();
        this.mCallForwardingInfoRegistrants = new RegistrantList();
        this.mTxPowerRegistrant = new RegistrantList();
        this.mTxPowerStatusRegistrant = new RegistrantList();
        this.mCfuReturnValue = null;
        this.mIccidRegistrants = new RegistrantList();
        this.mSimTypeRegistrants = new RegistrantList();
        this.mMtkRilOp = null;
        this.mRadioVersionMtk = new HashMap();
        this.mAllRadioServiceReady = MTK_RILJ_LOGV;
        this.mIsFirstInitialized = MTK_RILJ_LOGD;
        this.mMtkDeathRecipients = new SparseArray<>();
        this.mMtkServiceCookies = new SparseArray<>();
        this.mIccid = null;
        this.mSimTypeInfo = null;
        this.mMdDataRetryCountResetRegistrants = new RegistrantList();
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.MtkRIL.1
            private static final int MODE_CDMA_ASSERT = 31;
            private static final int MODE_CDMA_RESET = 32;
            private static final int MODE_CDMA_RILD_NE = 103;
            private static final int MODE_GSM_RILD_NE = 101;
            private static final int MODE_PHONE_PROCESS_JE = 100;

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent.getAction().equals("com.mtk.TEST_TRM")) {
                    int mode = intent.getIntExtra("mode", 2);
                    Rlog.d(MtkRIL.RILJ_LOG_TAG, "RIL received com.mtk.TEST_TRM, mode = " + mode + ", mInstanceIds = " + MtkRIL.this.mInstanceId);
                    if (mode == 100) {
                        throw new RuntimeException("UserTriggerPhoneJE");
                    }
                    MtkRIL.this.setTrm(mode, null);
                    return;
                }
                Rlog.w(MtkRIL.RILJ_LOG_TAG, "RIL received unexpected Intent: " + intent.getAction());
            }
        };
        this.hide_plmns = new ArrayList<>();
        this.mCsNetworkStateRegistrants = new RegistrantList();
        this.mSignalStrengthWithWcdmaEcioRegistrants = new RegistrantList();
        this.mVirtualSimOn = new RegistrantList();
        this.mVirtualSimOff = new RegistrantList();
        this.mImeiLockRegistrant = new RegistrantList();
        this.mImsiRefreshDoneRegistrant = new RegistrantList();
        this.mCardDetectedIndRegistrant = new RegistrantList();
        this.mRsuSimlockRegistrants = new RegistrantList();
        this.mInvalidSimInfoRegistrant = new RegistrantList();
        this.mNetworkEventRegistrants = new RegistrantList();
        this.mNetworkRejectRegistrants = new RegistrantList();
        this.mModulationRegistrants = new RegistrantList();
        this.mIsSmsReady = MTK_RILJ_LOGV;
        this.mSmsReadyRegistrants = new RegistrantList();
        this.mSmsInfoExtRegistrants = new RegistrantList();
        this.mIsCardDetected = MTK_RILJ_LOGV;
        this.mPsNetworkStateRegistrants = new RegistrantList();
        this.mNetworkInfoRegistrant = new RegistrantList();
        this.mPseudoCellInfoRegistrants = new RegistrantList();
        this.mBipProCmdRegistrant = new RegistrantList();
        this.mStkSetupMenuResetRegistrant = new RegistrantList();
        this.mSimPlugIn = new RegistrantList();
        this.mSimPlugOut = new RegistrantList();
        this.mSimMissing = new RegistrantList();
        this.mSimRecovery = new RegistrantList();
        this.mSimPowerChanged = new RegistrantList();
        this.mSimPowerInfo = null;
        this.mSmlSlotLockInfoChanged = new RegistrantList();
        this.mSmlSlotLockInfo = null;
        this.mEconfSrvccRegistrants = new RegistrantList();
        this.mMccMncRegistrants = new RegistrantList();
        this.mVsimIndicationRegistrants = new RegistrantList();
        this.mNwCfgInfoRegistrants = new RegistrantList();
        this.mDsbpStateRegistrant = new RegistrantList();
        this.mDsdaStateRegistrant = new RegistrantList();
        this.mQualifiedNetworkTypesRegistrant = new RegistrantList();
        this.mLatestQualifiedNetworkTypes = new SparseArray<>();
        this.mMobileDataUsageRegistrants = new RegistrantList();
        this.mNwLimitRegistrants = new RegistrantList();
        this.mPlmnDataRegistrants = new RegistrantList();
        this.mPlmnMvnoData = null;
        this.mIWlanStateRegistrants = new RegistrantList();
        this.m5gUWInfoRegistrants = new RegistrantList();
        this.mNrSysInfoRegistrants = new RegistrantList();
        Rlog.d(RILJ_LOG_TAG, "constructor: sub = " + instanceId);
        boolean zGreaterOrEqual = ((HalVersion) this.mHalVersion.get(0)).greaterOrEqual(RADIO_HAL_VERSION_2_0);
        Map<Integer, HalVersion> map = this.mRadioVersionMtk;
        if (zGreaterOrEqual) {
            map.put(0, RADIO_HAL_VERSION_MTK_4_0);
        } else {
            map.put(0, RADIO_HAL_VERSION_MTK_UNKNOWN);
        }
        this.mAllowedNetworkTypesBitmask = allowedNetworkTypes;
        this.mRilHandler = new MtkRilHandler();
        this.mMtkRadioExProxyDeathRecipient = new MtkRadioExProxyDeathRecipient();
        this.mMtkContext = context;
        this.mInstanceId = instanceId;
        this.mMtkRadioExResponse = new MtkRadioExResponse(this);
        this.mMtkRadioExIndication = new MtkRadioExIndication(this);
        this.mMtkRadioExDataResponse = new MtkRadioExDataResponse(this);
        this.mMtkRadioExDataIndication = new MtkRadioExDataIndication(this);
        this.mMtkRadioExMessagingResponse = new MtkRadioExMessagingResponse(this);
        this.mMtkRadioExMessagingIndication = new MtkRadioExMessagingIndication(this);
        this.mMtkRadioExModemResponse = new MtkRadioExModemResponse(this);
        this.mMtkRadioExModemIndication = new MtkRadioExModemIndication(this);
        this.mMtkRadioExNetworkResponse = new MtkRadioExNetworkResponse(this);
        this.mMtkRadioExNetworkIndication = new MtkRadioExNetworkIndication(this);
        this.mMtkRadioExSimResponse = new MtkRadioExSimResponse(this);
        this.mMtkRadioExSimIndication = new MtkRadioExSimIndication(this);
        this.mMtkRadioExVoiceResponse = new MtkRadioExVoiceResponse(this);
        this.mMtkRadioExVoiceIndication = new MtkRadioExVoiceIndication(this);
        for (int service = 0; service <= 6; service++) {
            if (service != 0) {
                this.mRadioVersionMtk.put(Integer.valueOf(service), RADIO_HAL_VERSION_MTK_UNKNOWN);
                this.mMtkDeathRecipients.put(service, new MtkBinderServiceDeathRecipient(service));
            }
            this.mMtkServiceCookies.put(service, new AtomicLong(0L));
        }
        if (proxies == null) {
            this.mServiceProxiesMtk.put(1, new MtkRadioExDataProxy());
            this.mServiceProxiesMtk.put(2, new MtkRadioExMessagingProxy());
            this.mServiceProxiesMtk.put(3, new MtkRadioExModemProxy());
            this.mServiceProxiesMtk.put(4, new MtkRadioExNetworkProxy());
            this.mServiceProxiesMtk.put(5, new MtkRadioExSimProxy());
            this.mServiceProxiesMtk.put(6, new MtkRadioExVoiceProxy());
        } else {
            this.mServiceProxiesMtk = proxies;
        }
        for (int service2 = 0; service2 <= 7; service2++) {
            if (service2 == 0) {
                getRadioProxy();
            } else if (proxies == null) {
                getRadioServiceProxy(service2);
            }
        }
        if (this.mIsCellularSupported && getHalVersion(0).equals(RADIO_HAL_VERSION_UNKNOWN)) {
            this.mHalVersion.put(0, RADIO_HAL_VERSION_1_6);
        }
        if (instanceId.intValue() == 0 && !TelephonyUtils.IS_USER) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.mtk.TEST_TRM");
            context.registerReceiver(this.mIntentReceiver, filter, 2);
        }
        this.mMtkRilJIntiDone = MTK_RILJ_LOGD;
        for (int service3 = 0; service3 <= 6; service3++) {
            if (service3 == 0) {
                getMtkRadioExProxy(null);
            } else if (proxies == null) {
                getMtkRadioExServiceProxy(service3, (Message) null);
            }
        }
        getRilOp(flags);
        PhoneConfigurationManager.registerForMultiSimConfigChange(this.mRilHandler, 1010, (Object) null);
    }

    private void handleProxyNotExist(Message result) {
        if (result != null) {
            AsyncResult.forMessage(result, (Object) null, CommandException.fromRilErrno(1));
            result.sendToTarget();
        }
    }

    protected boolean canMakeRequest(String request, RadioServiceProxy proxy, Message result, HalVersion version) {
        if (this.mInstanceId == null) {
            mtkRiljLogi("canMakeRequest wait for MtkRIL");
            handleProxyNotExist(result);
            return MTK_RILJ_LOGV;
        }
        return super.canMakeRequest(request, proxy, result, version);
    }

    public synchronized IRadio getRadioProxy() {
        if (this.mInstanceId == null) {
            Rlog.i(RILJ_LOG_TAG, "getRadioProxy wait for MtkRIL");
            return null;
        }
        return super.getRadioProxy();
    }

    public synchronized RadioServiceProxy getRadioServiceProxy(int service) {
        if (this.mMockModem != null) {
            return super.getRadioServiceProxy(service);
        }
        RadioServiceProxy serviceProxy = (RadioServiceProxy) this.mServiceProxies.get(service);
        if (this.mIsCellularSupported && !SystemProperties.getBoolean("ro.radio.noril", MTK_RILJ_LOGV)) {
            if (this.mInstanceId == null) {
                Rlog.i(RILJ_LOG_TAG, "getRadioServiceProxy wait for MtkRIL");
                return serviceProxy;
            }
            if (!SubscriptionManager.isValidPhoneId(this.mPhoneId.intValue())) {
                mtkRiljLogi("getRadioServiceProxy invalid phone id.");
                return serviceProxy;
            }
            if (serviceProxy != null && serviceProxy.isEmpty() && !isRildReady()) {
                mtkRiljLogi("getRadioServiceProxy " + service + " not ready.");
                this.mRilHandler.removeMessages(1008);
                this.mRilHandler.sendMessageDelayed(this.mRilHandler.obtainMessage(1008), 1000L);
                return serviceProxy;
            }
            RadioServiceProxy serviceProxy2 = super.getRadioServiceProxy(service);
            if (serviceProxy2 != null && serviceProxy2.isEmpty()) {
                mtkRiljLogi("getRadioServiceProxy " + service + " is empty even though rild is ready.");
                this.mRilHandler.sendMessageDelayed(this.mRilHandler.obtainMessage(1008), 1000L);
            }
            return serviceProxy2;
        }
        return serviceProxy;
    }

    public IMtkRadioEx getMtkRadioExProxy(Message result) {
        if (this.mRadioVersionMtk.containsKey(0) && this.mRadioVersionMtk.get(0).greaterOrEqual(RADIO_HAL_VERSION_MTK_4_0)) {
            Rlog.i(RILJ_LOG_TAG, "getMtkRadioExProxy returns null for mRadioVersionMtk greaterOrEqual 4.0.");
            return null;
        }
        if (this.mRilHandler.hasMessages(1006) && result != null && this.mRadioProxy == null) {
            Rlog.i(RILJ_LOG_TAG, "getMtkRadioExProxy service died, we try again later");
            handleProxyNotExist(result);
            return null;
        }
        if (!this.mMtkRilJIntiDone) {
            Rlog.i(RILJ_LOG_TAG, "!mMtkRilJIntiDone");
            handleProxyNotExist(result);
            return null;
        }
        if (!SubscriptionManager.isValidPhoneId(this.mPhoneId.intValue())) {
            Rlog.i(RILJ_LOG_TAG, "phone" + this.mPhoneId + " is not valid or is disabled");
            handleProxyNotExist(result);
            return null;
        }
        if (!this.mIsCellularSupported) {
            if (result != null) {
                AsyncResult.forMessage(result, (Object) null, CommandException.fromRilErrno(1));
                result.sendToTarget();
            }
            return null;
        }
        if (this.mRadioProxyMtk != null) {
            return this.mRadioProxyMtk;
        }
        try {
            this.mRadioProxyMtk = IMtkRadioEx.getService(HIDL_SERVICE_NAME_MTK[this.mPhoneId == null ? 0 : this.mPhoneId.intValue()], MTK_RILJ_LOGV);
        } catch (RemoteException | RuntimeException e) {
            this.mRadioProxyMtk = null;
            Rlog.e(RILJ_LOG_TAG, "MtkRadioExProxy getServiceV3_0: " + e);
        }
        if (this.mRadioProxyMtk != null) {
            try {
                this.mRadioVersionMtk.put(0, RADIO_HAL_VERSION_MTK_3_0);
                this.mRadioProxyMtk.linkToDeath(this.mMtkRadioExProxyDeathRecipient, this.mMtkServiceCookies.get(0).incrementAndGet());
                this.mRadioProxyMtk.setResponseFunctionsMtk(this.mMtkRadioExResponse, this.mMtkRadioExIndication);
            } catch (RemoteException | RuntimeException e2) {
                this.mRadioProxyMtk = null;
                Rlog.e(RILJ_LOG_TAG, "MtkRadioExProxy setResponseFunctionsMtkV3_0: " + e2);
            }
        } else {
            mtkRiljLoge("IMtkRadioEx < 3.0 is no longer supported.");
        }
        if (this.mRadioProxyMtk == null) {
            if (result != null) {
                AsyncResult.forMessage(result, (Object) null, CommandException.fromRilErrno(1));
                result.sendToTarget();
            }
            this.mRilHandler.removeMessages(1006);
            this.mRilHandler.sendMessageDelayed(this.mRilHandler.obtainMessage(1006, Long.valueOf(this.mMtkServiceCookies.get(0).get())), 1000L);
            riljLog("MtkRadioExProxy sendMessageDelayed");
        }
        return this.mRadioProxyMtk;
    }

    public <T extends MtkRadioExServiceProxy> T getMtkRadioExServiceProxy(Class<T> cls, Message message) {
        if (cls == MtkRadioExDataProxy.class) {
            return (T) getMtkRadioExServiceProxy(1, message);
        }
        if (cls == MtkRadioExMessagingProxy.class) {
            return (T) getMtkRadioExServiceProxy(2, message);
        }
        if (cls == MtkRadioExModemProxy.class) {
            return (T) getMtkRadioExServiceProxy(3, message);
        }
        if (cls == MtkRadioExNetworkProxy.class) {
            return (T) getMtkRadioExServiceProxy(4, message);
        }
        if (cls == MtkRadioExSimProxy.class) {
            return (T) getMtkRadioExServiceProxy(5, message);
        }
        if (cls == MtkRadioExVoiceProxy.class) {
            return (T) getMtkRadioExServiceProxy(6, message);
        }
        return null;
    }

    public synchronized MtkRadioExServiceProxy getMtkRadioExServiceProxy(int service, Message result) {
        if (!SubscriptionManager.isValidPhoneId(this.mPhoneId.intValue())) {
            mtkRiljLogi("getMtkRadioExServiceProxy mPhoneId invalid, return empty RadioServiceProxy,, service: " + service);
            return this.mServiceProxiesMtk.get(service);
        }
        if (!this.mIsCellularSupported) {
            if (result != null) {
                AsyncResult.forMessage(result, (Object) null, CommandException.fromRilErrno(1));
                result.sendToTarget();
            }
            return this.mServiceProxiesMtk.get(service);
        }
        MtkRadioExServiceProxy mtkServiceProxy = this.mServiceProxiesMtk.get(service);
        if (!mtkServiceProxy.isEmpty()) {
            return mtkServiceProxy;
        }
        if (!isRildReady()) {
            mtkRiljLog("getMtkRadioExServiceProxy " + mtkServiceToString(service) + " not ready.");
            handleProxyNotExist(result);
            this.mRilHandler.removeMessages(1008);
            this.mRilHandler.sendMessageDelayed(this.mRilHandler.obtainMessage(1008), 1000L);
            return mtkServiceProxy;
        }
        try {
            switch (service) {
                case 1:
                    IBinder binder = ServiceManager.waitForDeclaredService(IMtkRadioExData.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]);
                    if (binder != null) {
                        this.mRadioVersionMtk.put(Integer.valueOf(service), ((MtkRadioExDataProxy) mtkServiceProxy).setAidl(this.mRadioVersionMtk.get(Integer.valueOf(service)), IMtkRadioExData.Stub.asInterface(binder)));
                    }
                    break;
                case 2:
                    IBinder binder2 = ServiceManager.waitForDeclaredService(IMtkRadioExMessaging.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]);
                    if (binder2 != null) {
                        this.mRadioVersionMtk.put(Integer.valueOf(service), ((MtkRadioExMessagingProxy) mtkServiceProxy).setAidl(this.mRadioVersionMtk.get(Integer.valueOf(service)), IMtkRadioExMessaging.Stub.asInterface(binder2)));
                    }
                    break;
                case 3:
                    IBinder binder3 = ServiceManager.waitForDeclaredService(IMtkRadioExModem.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]);
                    if (binder3 != null) {
                        this.mRadioVersionMtk.put(Integer.valueOf(service), ((MtkRadioExModemProxy) mtkServiceProxy).setAidl(this.mRadioVersionMtk.get(Integer.valueOf(service)), IMtkRadioExModem.Stub.asInterface(binder3)));
                    }
                    break;
                case 4:
                    IBinder binder4 = ServiceManager.waitForDeclaredService(IMtkRadioExNetwork.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]);
                    if (binder4 != null) {
                        this.mRadioVersionMtk.put(Integer.valueOf(service), ((MtkRadioExNetworkProxy) mtkServiceProxy).setAidl(this.mRadioVersionMtk.get(Integer.valueOf(service)), IMtkRadioExNetwork.Stub.asInterface(binder4)));
                    }
                    break;
                case 5:
                    IBinder binder5 = ServiceManager.waitForDeclaredService(IMtkRadioExSim.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]);
                    if (binder5 != null) {
                        this.mRadioVersionMtk.put(Integer.valueOf(service), ((MtkRadioExSimProxy) mtkServiceProxy).setAidl(this.mRadioVersionMtk.get(Integer.valueOf(service)), IMtkRadioExSim.Stub.asInterface(binder5)));
                    }
                    break;
                case 6:
                    IBinder binder6 = ServiceManager.waitForDeclaredService(IMtkRadioExVoice.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]);
                    if (binder6 != null) {
                        this.mRadioVersionMtk.put(Integer.valueOf(service), ((MtkRadioExVoiceProxy) mtkServiceProxy).setAidl(this.mRadioVersionMtk.get(Integer.valueOf(service)), IMtkRadioExVoice.Stub.asInterface(binder6)));
                    }
                    break;
            }
            if (!mtkServiceProxy.isEmpty() && mtkServiceProxy.isAidl()) {
                this.mRilHandler.removeMessages(1006);
                switch (service) {
                    case 1:
                        this.mMtkDeathRecipients.get(service).linkToDeath(((MtkRadioExDataProxy) mtkServiceProxy).getAidl().asBinder());
                        ((MtkRadioExDataProxy) mtkServiceProxy).getAidl().setResponseFunctionsMtk(this.mMtkRadioExDataResponse, this.mMtkRadioExDataIndication);
                        break;
                    case 2:
                        this.mMtkDeathRecipients.get(service).linkToDeath(((MtkRadioExMessagingProxy) mtkServiceProxy).getAidl().asBinder());
                        ((MtkRadioExMessagingProxy) mtkServiceProxy).getAidl().setResponseFunctionsMtk(this.mMtkRadioExMessagingResponse, this.mMtkRadioExMessagingIndication);
                        break;
                    case 3:
                        this.mMtkDeathRecipients.get(service).linkToDeath(((MtkRadioExModemProxy) mtkServiceProxy).getAidl().asBinder());
                        ((MtkRadioExModemProxy) mtkServiceProxy).getAidl().setResponseFunctionsMtk(this.mMtkRadioExModemResponse, this.mMtkRadioExModemIndication);
                        break;
                    case 4:
                        this.mMtkDeathRecipients.get(service).linkToDeath(((MtkRadioExNetworkProxy) mtkServiceProxy).getAidl().asBinder());
                        ((MtkRadioExNetworkProxy) mtkServiceProxy).getAidl().setResponseFunctionsMtk(this.mMtkRadioExNetworkResponse, this.mMtkRadioExNetworkIndication);
                        break;
                    case 5:
                        this.mMtkDeathRecipients.get(service).linkToDeath(((MtkRadioExSimProxy) mtkServiceProxy).getAidl().asBinder());
                        ((MtkRadioExSimProxy) mtkServiceProxy).getAidl().setResponseFunctionsMtk(this.mMtkRadioExSimResponse, this.mMtkRadioExSimIndication);
                        break;
                    case 6:
                        this.mMtkDeathRecipients.get(service).linkToDeath(((MtkRadioExVoiceProxy) mtkServiceProxy).getAidl().asBinder());
                        ((MtkRadioExVoiceProxy) mtkServiceProxy).getAidl().setResponseFunctionsMtk(this.mMtkRadioExVoiceResponse, this.mMtkRadioExVoiceIndication);
                        break;
                }
            } else if (mtkServiceProxy.isEmpty() && this.mRadioVersionMtk.get(Integer.valueOf(service)).less(RADIO_HAL_VERSION_MTK_4_0)) {
                mtkRiljLogi("getMtkRadioExServiceProxy No AIDL, get HIDL instead.");
                if (this.mRadioProxyMtk == null) {
                    getMtkRadioExProxy(result);
                }
                if (this.mRadioProxyMtk != null) {
                    this.mRadioVersionMtk.put(Integer.valueOf(service), this.mRadioVersionMtk.get(0));
                    mtkServiceProxy.setHidl(this.mRadioVersionMtk.get(Integer.valueOf(service)), this.mRadioProxyMtk);
                }
                return mtkServiceProxy;
            }
        } catch (RemoteException e) {
            mtkServiceProxy.clear();
            Rlog.e(RILJ_LOG_TAG, "MtkServiceProxy getService/setResponseFunctionsMtk: " + e);
        }
        if (mtkServiceProxy.isEmpty()) {
            Rlog.e(RILJ_LOG_TAG, "getMtkRadioExServiceProxy: mtkServiceProxy is empty [PHONE" + this.mPhoneId + "]");
            this.mRilHandler.sendMessageDelayed(this.mRilHandler.obtainMessage(1008), 1000L);
            if (result != null) {
                AsyncResult.forMessage(result, (Object) null, CommandException.fromRilErrno(1));
                result.sendToTarget();
            }
        }
        return mtkServiceProxy;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRildReady() {
        if (this.mMockModem != null || ((HalVersion) this.mHalVersion.get(0)).less(RADIO_HAL_VERSION_2_0) || this.mAllRadioServiceReady) {
            return MTK_RILJ_LOGD;
        }
        if (this.mIsFirstInitialized) {
            if (ServiceManager.checkService(IRadioModem.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]) == null) {
                return MTK_RILJ_LOGV;
            }
            this.mAllRadioServiceReady = MTK_RILJ_LOGD;
            this.mIsFirstInitialized = MTK_RILJ_LOGV;
            return MTK_RILJ_LOGD;
        }
        if (ServiceManager.checkService(IRadioData.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]) == null || ServiceManager.checkService(IRadioMessaging.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]) == null || ServiceManager.checkService(IRadioModem.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]) == null || ServiceManager.checkService(IRadioNetwork.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]) == null || ServiceManager.checkService(IRadioSim.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]) == null || ServiceManager.checkService(IRadioVoice.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]) == null || ServiceManager.checkService(IMtkRadioExData.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]) == null || ServiceManager.checkService(IMtkRadioExMessaging.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]) == null || ServiceManager.checkService(IMtkRadioExModem.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]) == null || ServiceManager.checkService(IMtkRadioExNetwork.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]) == null || ServiceManager.checkService(IMtkRadioExSim.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]) == null || ServiceManager.checkService(IMtkRadioExVoice.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]) == null) {
            return MTK_RILJ_LOGV;
        }
        if (ServiceManager.isDeclared(IRadioIms.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]) && ServiceManager.checkService(IRadioIms.DESCRIPTOR + "/" + AIDL_SERVICE_NAME_MTK[this.mPhoneId.intValue()]) == null) {
            return MTK_RILJ_LOGV;
        }
        this.mAllRadioServiceReady = MTK_RILJ_LOGD;
        return MTK_RILJ_LOGD;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getAllRadioServices() {
        for (int service = 0; service <= 7; service++) {
            if (service == 0) {
                getRadioProxy();
            } else {
                getRadioServiceProxy(service);
            }
        }
        for (int service2 = 0; service2 <= 6; service2++) {
            if (service2 == 0) {
                getMtkRadioExProxy(null);
            } else {
                getMtkRadioExServiceProxy(service2, (Message) null);
            }
        }
    }

    public void onSlotActiveStatusChange(boolean active) {
        super.onSlotActiveStatusChange(active);
        for (int service = 0; service <= 6; service++) {
            if (!active) {
                resetMtkProxyAndRequestList(service);
            } else if (service == 0) {
                getMtkRadioExProxy(null);
            } else {
                getMtkRadioExServiceProxy(service, (Message) null);
            }
        }
    }

    protected void handleMtkRadioProxyExceptionForRR(int service, RILRequest rr, String caller, Exception e) {
        Rlog.e(RILJ_LOG_TAG, caller + ": " + e);
        clearRequestWithError(rr, 1);
        resetMtkProxyAndRequestList(service);
    }

    public IMtkRilOp getRilOp(FeatureFlags flags) {
        Rlog.d(RILJ_LOG_TAG, "getRilOp");
        if (this.mRadioVersionMtk.containsKey(0) && this.mRadioVersionMtk.get(0).greaterOrEqual(RADIO_HAL_VERSION_MTK_4_0)) {
            mtkRiljLog("getRilOp return null for there is no HIDL, mRadioVersionMtk: " + this.mRadioVersionMtk.get(0));
            return null;
        }
        if (this.mMtkRilOp != null) {
            return this.mMtkRilOp;
        }
        String optr = SystemProperties.get(DataSubConstants.PROPERTY_OPERATOR_OPTR, "0");
        if ("0".equals(optr)) {
            Rlog.d(RILJ_LOG_TAG, "mMtkRilOp init fail, because OM load");
            return null;
        }
        try {
            Class<?> clazz = Class.forName("com.mediatek.opcommon.telephony.MtkRilOp");
            Rlog.d(RILJ_LOG_TAG, "class = " + clazz);
            Constructor<?> constructor = clazz.getConstructor(Context.class, Integer.TYPE, Integer.class, FeatureFlags.class);
            Rlog.d(RILJ_LOG_TAG, "constructor function = " + constructor);
            this.mMtkRilOp = (IMtkRilOp) constructor.newInstance(this.mContext, Integer.valueOf(this.mAllowedNetworkTypesBitmask), this.mPhoneId, flags);
            return this.mMtkRilOp;
        } catch (Exception e) {
            Rlog.d(RILJ_LOG_TAG, "mMtkRilOp init fail");
            e.printStackTrace();
            return null;
        }
    }

    protected RadioResponse makeRadioResponseFunction(RIL ril) {
        this.mMtkRadioResponse = new MtkRadioResponse((MtkRIL) ril);
        return this.mMtkRadioResponse;
    }

    protected RadioIndication makeRadioIndicationFunction(RIL ril) {
        this.mMtkRadioIndication = new MtkRadioIndication((MtkRIL) ril);
        return this.mMtkRadioIndication;
    }

    protected ModemIndication makeModemIndicationFunction(RIL ril) {
        return new MtkModemIndication((MtkRIL) ril);
    }

    protected NetworkResponse makeNetworkResponseFunction(RIL ril) {
        return new MtkNetworkResponse((MtkRIL) ril);
    }

    protected NetworkIndication makeNetworkIndicationFunction(RIL ril) {
        return new MtkNetworkIndication((MtkRIL) ril);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String mtkServiceToString(int service) {
        switch (service) {
            case 0:
                return "MTK_RADIO";
            case 1:
                return "MTK_DATA";
            case 2:
                return "MTK_MESSAGING";
            case 3:
                return "MTK_MODEM";
            case 4:
                return "MTK_NETWORK";
            case 5:
                return "MTK_SIM";
            case 6:
                return "MTK_VOICE";
            default:
                return "UNKNOWN:" + service;
        }
    }

    protected boolean isGetHidlServiceSync() {
        return MTK_RILJ_LOGV;
    }

    public MtkRadioResponse getMtkRadioResponse() {
        return this.mMtkRadioResponse;
    }

    public MtkRadioExResponse getMtkRadioExResponse() {
        return this.mMtkRadioExResponse;
    }

    public MtkRadioIndication getMtkRadioIndication() {
        return this.mMtkRadioIndication;
    }

    public MtkRadioExIndication getMtkRadioExIndication() {
        return this.mMtkRadioExIndication;
    }

    public void mtkRiljLog(String msg) {
        Rlog.d(RILJ_LOG_TAG, msg + (this.mPhoneId != null ? " [PHONE" + this.mPhoneId + "]" : ""));
    }

    public void mtkRiljLogi(String msg) {
        Rlog.i(RILJ_LOG_TAG, msg + (this.mPhoneId != null ? " [PHONE" + this.mPhoneId + "]" : ""));
    }

    public void mtkRiljLoge(String msg) {
        Rlog.e(RILJ_LOG_TAG, msg + (this.mPhoneId != null ? " [PHONE" + this.mPhoneId + "]" : ""));
    }

    private void clearRequestWithError(RILRequest rr, int error) {
        SparseArray<RILRequest> requestList = getRilRequestList();
        Rlog.e(RILJ_LOG_TAG, "clearRequestWithError target=" + rr.mRequest);
        synchronized (requestList) {
            if (requestList.contains(rr.mSerial)) {
                requestList.remove(rr.mSerial);
                RadioResponseInfo responseInfo = new RadioResponseInfo();
                responseInfo.type = 0;
                responseInfo.serial = rr.mSerial;
                responseInfo.error = error;
                processResponseDone(rr, responseInfo, null);
                return;
            }
            Rlog.e(RILJ_LOG_TAG, "clearRequestWithError: [" + rr.mSerial + "] was already removed.");
        }
    }

    protected synchronized void resetProxyAndRequestList(int service) {
        mtkRiljLoge("resetProxyAndRequestList, service: " + service + ", mHalVersion: " + this.mHalVersion.get(Integer.valueOf(service)));
        if (((HalVersion) this.mHalVersion.get(Integer.valueOf(service))).less(RADIO_HAL_VERSION_2_0)) {
            for (int i = 0; i <= 7; i++) {
                if (i == 0) {
                    this.mRadioProxy = null;
                } else {
                    ((RadioServiceProxy) this.mServiceProxies.get(i)).clear();
                }
            }
        }
        if (!TelephonyUtils.IS_USER) {
            this.mRilHandler.removeMessages(1009);
            this.mRilHandler.sendMessageDelayed(this.mRilHandler.obtainMessage(1009), 120000L);
        }
        super.resetProxyAndRequestList(service);
    }

    protected void setRadioState(int newState, boolean forceNotifyRegistrants) {
        if (!TelephonyUtils.IS_USER && newState != 2) {
            this.mRilHandler.removeMessages(1009);
        }
        super.setRadioState(newState, forceNotifyRegistrants);
    }

    public void setAllowedNetworkTypesBitmap(int networkTypeBitmask, Message result) {
        if (((HalVersion) this.mHalVersion.get(4)).less(RADIO_HAL_VERSION_2_1) && (networkTypeBitmask & 72) == 0 && (networkTypeBitmask & 10288) != 0 && ((~10288) & networkTypeBitmask) != 0) {
            networkTypeBitmask &= ~10288;
            mtkRiljLog("setAllowedNetworkTypesBitmap, after removing EVDO due to no CDMA, rat = " + networkTypeBitmask);
        }
        super.setAllowedNetworkTypesBitmap(networkTypeBitmask, result);
    }

    protected synchronized void resetMtkProxyAndRequestList(int service) {
        mtkRiljLoge("resetMtkProxyAndRequestList, service: " + service + ", mRadioVersionMtk: " + this.mRadioVersionMtk.get(0));
        if (this.mRadioVersionMtk.get(0).less(RADIO_HAL_VERSION_MTK_4_0)) {
            for (int i = 0; i <= 6; i++) {
                if (i == 0) {
                    this.mRadioProxyMtk = null;
                } else {
                    this.mServiceProxiesMtk.get(i).clear();
                }
            }
        } else if (service == 0) {
            this.mRadioProxyMtk = null;
        } else {
            this.mServiceProxiesMtk.get(service).clear();
        }
        this.mMtkServiceCookies.get(service).incrementAndGet();
        if (service == 0) {
            getMtkRadioExProxy(null);
        } else {
            getMtkRadioExServiceProxy(service, (Message) null);
        }
    }

    public static String requestToStringEx(Integer request) {
        String msg;
        switch (request.intValue()) {
            case 59:
                msg = "OEM_HOOK_RAW";
                break;
            case MtkAdnRecordCache.MAX_PHB_NAME_LENGTH /* 60 */:
                msg = "OEM_HOOK_STRINGS";
                break;
            case 2000:
                msg = "RIL_REQUEST_RESUME_REGISTRATION";
                break;
            case 2002:
                msg = "RIL_REQUEST_SET_SIM_POWER";
                break;
            case MtkRILConstants.RIL_REQUEST_MODEM_POWERON /* 2003 */:
                msg = "RIL_REQUEST_MODEM_POWERON";
                break;
            case 2004:
                msg = "RIL_REQUEST_MODEM_POWEROFF";
                break;
            case 2005:
                msg = "SET_NETWORK_SELECTION_MANUAL_WITH_ACT";
                break;
            case 2006:
                msg = "QUERY_AVAILABLE_NETWORKS_WITH_ACT";
                break;
            case MtkRILConstants.RIL_REQUEST_ABORT_QUERY_AVAILABLE_NETWORKS /* 2007 */:
                msg = "ABORT_QUERY_AVAILABLE_NETWORKS";
                break;
            case MtkRILConstants.RIL_REQUEST_GSM_SET_BROADCAST_LANGUAGE /* 2009 */:
                msg = "RIL_REQUEST_GSM_SET_BROADCAST_LANGUAGE";
                break;
            case MtkRILConstants.RIL_REQUEST_GSM_GET_BROADCAST_LANGUAGE /* 2010 */:
                msg = "RIL_REQUEST_GSM_GET_BROADCAST_LANGUAGE";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_SMS_SIM_MEM_STATUS /* 2011 */:
                msg = "RIL_REQUEST_GET_SMS_SIM_MEM_STATUS";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_SMS_PARAMS /* 2012 */:
                msg = "RIL_REQUEST_GET_SMS_PARAMS";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_SMS_PARAMS /* 2013 */:
                msg = "RIL_REQUEST_SET_SMS_PARAMS";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_ETWS /* 2014 */:
                msg = "RIL_REQUEST_SET_ETWS";
                break;
            case MtkRILConstants.RIL_REQUEST_REMOVE_CB_MESSAGE /* 2015 */:
                msg = "RIL_REQUEST_REMOVE_CB_MESSAGE";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_CALL_INDICATION /* 2016 */:
                msg = "SET_CALL_INDICATION";
                break;
            case MtkRILConstants.RIL_REQUEST_HANGUP_ALL /* 2019 */:
                msg = "HANGUP_ALL";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_PSEUDO_CELL_MODE /* 2021 */:
                msg = "RIL_REQUEST_SET_PSEUDO_CELL_MODE";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_PSEUDO_CELL_INFO /* 2022 */:
                msg = "RIL_REQUEST_GET_PSEUDO_CELL_INFO";
                break;
            case MtkRILConstants.RIL_REQUEST_SWITCH_MODE_FOR_ECC /* 2023 */:
                msg = "RIL_REQUEST_SWITCH_MODE_FOR_ECC";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_SMS_RUIM_MEM_STATUS /* 2024 */:
                msg = "RIL_REQUEST_GET_SMS_RUIM_MEM_STATUS";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_TRM /* 2028 */:
                msg = "RIL_REQUEST_SET_TRM";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_ECC_LIST /* 2030 */:
                msg = "RIL_REQUEST_SET_ECC_LIST";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_ECC_MODE /* 2035 */:
                msg = "SET_ECC_MODE";
                break;
            case MtkRILConstants.RIL_REQUEST_QUERY_PHB_STORAGE_INFO /* 2036 */:
                msg = "RIL_REQUEST_QUERY_PHB_STORAGE_INFO";
                break;
            case MtkRILConstants.RIL_REQUEST_WRITE_PHB_ENTRY /* 2037 */:
                msg = "RIL_REQUEST_WRITE_PHB_ENTRY";
                break;
            case MtkRILConstants.RIL_REQUEST_READ_PHB_ENTRY /* 2038 */:
                msg = "RIL_REQUEST_READ_PHB_ENTRY";
                break;
            case MtkRILConstants.RIL_REQUEST_QUERY_UPB_CAPABILITY /* 2039 */:
                msg = "RIL_REQUEST_QUERY_UPB_CAPABILITY";
                break;
            case MtkRILConstants.RIL_REQUEST_EDIT_UPB_ENTRY /* 2040 */:
                msg = "RIL_REQUEST_EDIT_UPB_ENTRY";
                break;
            case MtkRILConstants.RIL_REQUEST_DELETE_UPB_ENTRY /* 2041 */:
                msg = "RIL_REQUEST_DELETE_UPB_ENTRY";
                break;
            case MtkRILConstants.RIL_REQUEST_READ_UPB_GAS_LIST /* 2042 */:
                msg = "RIL_REQUEST_READ_UPB_GAS_LIST";
                break;
            case MtkRILConstants.RIL_REQUEST_READ_UPB_GRP /* 2043 */:
                msg = "RIL_REQUEST_READ_UPB_GRP";
                break;
            case MtkRILConstants.RIL_REQUEST_WRITE_UPB_GRP /* 2044 */:
                msg = "RIL_REQUEST_WRITE_UPB_GRP";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_PHB_STRING_LENGTH /* 2045 */:
                msg = "RIL_REQUEST_GET_PHB_STRING_LENGTH";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_PHB_MEM_STORAGE /* 2046 */:
                msg = "RIL_REQUEST_GET_PHB_MEM_STORAGE";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_PHB_MEM_STORAGE /* 2047 */:
                msg = "RIL_REQUEST_SET_PHB_MEM_STORAGE";
                break;
            case 2048:
                msg = "RIL_REQUEST_READ_PHB_ENTRY_EXT";
                break;
            case MtkRILConstants.RIL_REQUEST_WRITE_PHB_ENTRY_EXT /* 2049 */:
                msg = "RIL_REQUEST_WRITE_PHB_ENTRY_EXT";
                break;
            case MtkRILConstants.RIL_REQUEST_QUERY_UPB_AVAILABLE /* 2050 */:
                msg = "RIL_REQUEST_QUERY_UPB_AVAILABLE";
                break;
            case MtkRILConstants.RIL_REQUEST_READ_EMAIL_ENTRY /* 2051 */:
                msg = "RIL_REQUEST_READ_EMAIL_ENTRY";
                break;
            case MtkRILConstants.RIL_REQUEST_READ_SNE_ENTRY /* 2052 */:
                msg = "RIL_REQUEST_READ_SNE_ENTRY";
                break;
            case MtkRILConstants.RIL_REQUEST_READ_ANR_ENTRY /* 2053 */:
                msg = "RIL_REQUEST_READ_ANR_ENTRY";
                break;
            case MtkRILConstants.RIL_REQUEST_READ_UPB_AAS_LIST /* 2054 */:
                msg = "RIL_REQUEST_READ_UPB_AAS_LIST";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_FEMTOCELL_LIST /* 2055 */:
                msg = "REQUEST_GET_FEMTOCELL_LIST";
                break;
            case MtkRILConstants.RIL_REQUEST_ABORT_FEMTOCELL_LIST /* 2056 */:
                msg = "REQUEST_ABORT_FEMTOCELL_LIST";
                break;
            case MtkRILConstants.RIL_REQUEST_SELECT_FEMTOCELL /* 2057 */:
                msg = "REQUEST_SELECT_FEMTOCELL";
                break;
            case MtkRILConstants.RIL_REQUEST_QUERY_FEMTOCELL_SYSTEM_SELECTION_MODE /* 2058 */:
                msg = "REQUEST_QUERY_FEMTOCELL_SYSTEM_SELECTION_MODE";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_FEMTOCELL_SYSTEM_SELECTION_MODE /* 2059 */:
                msg = "REQUEST_SET_FEMTOCELL_SYSTEM_SELECTION_MODE";
                break;
            case MtkRILConstants.RIL_REQUEST_RESET_MD_DATA_RETRY_COUNT /* 2063 */:
                msg = "RIL_REQUEST_RESET_MD_DATA_RETRY_COUNT";
                break;
            case MtkRILConstants.RIL_REQUEST_QUERY_SIM_NETWORK_LOCK /* 2067 */:
                msg = "RIL_REQUEST_QUERY_SIM_NETWORK_LOCK";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_SIM_NETWORK_LOCK /* 2068 */:
                msg = "RIL_REQUEST_SET_SIM_NETWORK_LOCK";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_CLIP /* 2103 */:
                msg = "RIL_REQUEST_SET_CLIP";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_COLP /* 2104 */:
                msg = "RIL_REQUEST_GET_COLP";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_COLR /* 2105 */:
                msg = "RIL_REQUEST_GET_COLR";
                break;
            case MtkRILConstants.RIL_REQUEST_SEND_CNAP /* 2106 */:
                msg = "RIL_REQUEST_SEND_CNAP";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_POL_CAPABILITY /* 2107 */:
                msg = "RIL_REQUEST_GET_POL_CAPABILITY";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_POL_LIST /* 2108 */:
                msg = "RIL_REQUEST_GET_POL_LIST";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_POL_ENTRY /* 2109 */:
                msg = "RIL_REQUEST_SET_POL_ENTRY";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_ROAMING_ENABLE /* 2111 */:
                msg = "SET_ROAMING_ENABLE";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_ROAMING_ENABLE /* 2112 */:
                msg = "GET_ROAMING_ENABLE";
                break;
            case MtkRILConstants.RIL_REQUEST_VSIM_NOTIFICATION /* 2113 */:
                msg = "RIL_REQUEST_VSIM_NOTIFICATION";
                break;
            case MtkRILConstants.RIL_REQUEST_VSIM_OPERATION /* 2114 */:
                msg = "RIL_REQUEST_VSIM_OPERATION";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_GSM_SMS_BROADCAST_ACTIVATION /* 2115 */:
                msg = "RIL_REQUEST_GET_GSM_SMS_BROADCAST_ACTIVATION";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_WIFI_ENABLED /* 2116 */:
                msg = "RIL_REQUEST_SET_WIFI_ENABLED";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_WIFI_ASSOCIATED /* 2117 */:
                msg = "RIL_REQUEST_SET_WIFI_ASSOCIATED";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_WIFI_SIGNAL_LEVEL /* 2118 */:
                msg = "RIL_REQUEST_SET_WIFI_SIGNAL_LEVEL";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_WIFI_IP_ADDRESS /* 2119 */:
                msg = "RIL_REQUEST_SET_WIFI_IP_ADDRESS";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_GEO_LOCATION /* 2120 */:
                msg = "RIL_REQUEST_SET_GEO_LOCATION";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_EMERGENCY_ADDRESS_ID /* 2121 */:
                msg = "RIL_REQUEST_SET_EMERGENCY_ADDRESS_ID";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_COLP /* 2123 */:
                msg = "RIL_REQUEST_SET_COLP";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_COLR /* 2124 */:
                msg = "RIL_REQUEST_SET_COLR";
                break;
            case MtkRILConstants.RIL_REQUEST_QUERY_CALL_FORWARD_IN_TIME_SLOT /* 2125 */:
                msg = "RIL_REQUEST_QUERY_CALL_FORWARD_IN_TIME_SLOT";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_CALL_FORWARD_IN_TIME_SLOT /* 2126 */:
                msg = "RIL_REQUEST_SET_CALL_FORWARD_IN_TIME_SLOT";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_SERVICE_STATE /* 2130 */:
                msg = "RIL_REQUEST_SET_SERVICE_STATE";
                break;
            case MtkRILConstants.RIL_REQUEST_IMS_SEND_SMS_EX /* 2133 */:
                msg = "RIL_REQUEST_IMS_SEND_SMS_EX";
                break;
            case MtkRILConstants.RIL_REQUEST_DATA_CONNECTION_ATTACH /* 2144 */:
                msg = "RIL_REQUEST_DATA_CONNECTION_ATTACH";
                break;
            case MtkRILConstants.RIL_REQUEST_DATA_CONNECTION_DETACH /* 2145 */:
                msg = "RIL_REQUEST_DATA_CONNECTION_DETACH";
                break;
            case MtkRILConstants.RIL_REQUEST_RESET_ALL_CONNECTIONS /* 2146 */:
                msg = "RIL_REQUEST_RESET_ALL_CONNECTIONS";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_ECC_NUM /* 2148 */:
                msg = "RIL_REQUEST_SET_ECC_NUM";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_ECC_NUM /* 2149 */:
                msg = "RIL_REQUEST_GET_ECC_NUM";
                break;
            case MtkRILConstants.RIL_REQUEST_RESTART_RILD /* 2150 */:
                msg = "RIL_REQUEST_RESTART_RILD";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_LTE_RELEASE_VERSION /* 2151 */:
                msg = "RIL_REQUEST_SET_LTE_RELEASE_VERSION";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_LTE_RELEASE_VERSION /* 2152 */:
                msg = "RIL_REQUEST_GET_LTE_RELEASE_VERSION";
                break;
            case MtkRILConstants.RIL_REQUEST_SIGNAL_STRENGTH_WITH_WCDMA_ECIO /* 2153 */:
                msg = "RIL_REQUEST_SIGNAL_STRENGTH_WITH_WCDMA_ECIO";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_PHONEBOOK_READY /* 2157 */:
                msg = "RIL_REQUEST_SET_PHONEBOOK_READY";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_TX_POWER_STATUS /* 2158 */:
                msg = "RIL_REQUEST_SET_TX_POWER_STATUS";
                break;
            case MtkRILConstants.RIL_REQUEST_SETPROP_IMS_HANDOVER /* 2159 */:
                msg = "RIL_REQUEST_SETPROP_IMS_HANDOVER";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_SS_PROPERTY /* 2168 */:
                msg = "RIL_REQUEST_SET_SS_PROPERTY";
                break;
            case MtkRILConstants.RIL_REQUEST_ENTER_DEVICE_NETWORK_DEPERSONALIZATION /* 2171 */:
                msg = "RIL_REQUEST_ENTER_DEVICE_NETWORK_DEPERSONALIZATION";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_VENDOR_SETTING /* 2173 */:
                msg = "RIL_REQUEST_SET_VENDOR_SETTING";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_GWSD_MODE /* 2180 */:
                msg = "RIL_REQUEST_SET_GWSD_MODE";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_GWSD_CALL_VALID /* 2181 */:
                msg = "RIL_REQUEST_SET_GWSD_CALL_VALID";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_GWSD_IGNORE_CALL_INTERVAL /* 2182 */:
                msg = "RIL_REQUEST_SET_GWSD_IGNORE_CALL_INTERVAL";
                break;
            case MtkRILConstants.RIL_REQUEST_HANGUP_WITH_REASON /* 2183 */:
                msg = "HANGUP_WITH_REASON";
                break;
            case MtkRILConstants.RIL_REQUEST_MODIFY_MODEM_TYPE /* 2184 */:
                msg = "RIL_REQUEST_MODIFY_MODEM_TYPE";
                break;
            case MtkRILConstants.RIL_REQUEST_ENABLE_DSDA_INDICATION /* 2185 */:
                msg = "RIL_REQUEST_ENABLE_DSDA_INDICATION";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_DSDA_STATUS /* 2186 */:
                msg = "RIL_REQUEST_GET_DSDA_STATUS";
                break;
            case MtkRILConstants.RIL_REQUEST_IWLAN_REGISTER_CELLULAR_QUALITY_REPORT /* 2188 */:
                msg = "RIL_REQUEST_IWLAN_REGISTER_CELLULAR_QUALITY_REPORT";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_SUGGESTED_PLMN_LIST /* 2189 */:
                msg = "RIL_REQUEST_GET_SUGGESTED_PLMN_LIST";
                break;
            case MtkRILConstants.RIL_REQUEST_CONFIG_A2_OFFSET /* 2190 */:
                msg = "RIL_REQUEST_CONFIG_A2_OFFSET";
                break;
            case MtkRILConstants.RIL_REQUEST_CONFIG_B1_OFFSET /* 2191 */:
                msg = "RIL_REQUEST_CONFIG_B1_OFFSET";
                break;
            case MtkRILConstants.RIL_REQUEST_ENABLE_SCG_FAILURE /* 2192 */:
                msg = "RIL_REQUEST_ENABLE_SCG_FAILURE";
                break;
            case MtkRILConstants.RIL_REQUEST_DISABLE_NR /* 2193 */:
                msg = "RIL_REQUEST_DISABLE_NR";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_TX_POWER /* 2194 */:
                msg = "RIL_REQUEST_SET_TX_POWER";
                break;
            case MtkRILConstants.RIL_REQUEST_SEARCH_STORED_FREQUENCY_INFO /* 2195 */:
                msg = "RIL_REQUEST_SEARCH_STORED_FREQUENCY_INFO";
                break;
            case MtkRILConstants.RIL_REQUEST_SEARCH_RAT /* 2196 */:
                msg = "RIL_REQUEST_SEARCH_RAT";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_BACKGROUND_SEARCH_TIMER /* 2197 */:
                msg = "RIL_REQUEST_SET_BACKGROUND_SEARCH_TIMER";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_GWSD_KEEP_ALIVE_PDCP /* 2199 */:
                msg = "RIL_REQUEST_SET_GWSD_KEEP_ALIVE_PDCP";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_GWSD_KEEP_ALIVE_IPDATA /* 2200 */:
                msg = "RIL_REQUEST_SET_GWSD_KEEP_ALIVE_IPDATA";
                break;
            case MtkRILConstants.RIL_REQUEST_SEND_SAR_INDICATOR /* 2201 */:
                msg = "RIL_REQUEST_SEND_SAR_INDICATOR";
                break;
            case MtkRILConstants.RIL_REQUEST_DEACTIVATE_NR_SCG_COMMUNICATION /* 2202 */:
                msg = "RIL_REQUEST_DEACTIVATE_NR_SCG_COMMUNICATION";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_DEACTIVATE_NR_SCG_COMMUNICATION /* 2203 */:
                msg = "RIL_REQUEST_GET_DEACTIVATE_NR_SCG_COMMUNICATION";
                break;
            case MtkRILConstants.RIL_REQUEST_SML_RSU_REQUEST /* 2204 */:
                msg = "RIL_REQUEST_SML_RSU_REQUEST";
                break;
            case MtkRILConstants.RIL_REQUEST_SEND_WIFI_ENABLED /* 2205 */:
                msg = "RIL_REQUEST_SEND_WIFI_ENABLED";
                break;
            case MtkRILConstants.RIL_REQUEST_SEND_WIFI_ASSOCIATED /* 2206 */:
                msg = "RIL_REQUEST_SEND_WIFI_ASSOCIATED";
                break;
            case MtkRILConstants.RIL_REQUEST_SEND_WIFI_IP_ADDRESS /* 2207 */:
                msg = "RIL_REQUEST_SEND_WIFI_IP_ADDRESS";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_CALL_SUB_ADDRESS /* 2208 */:
                msg = "RIL_REQUEST_SET_CALL_SUB_ADDRESS";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_CALL_SUB_ADDRESS /* 2209 */:
                msg = "RIL_REQUEST_GET_CALL_SUB_ADDRESS";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_NR_OPTION /* 2210 */:
                msg = "RIL_REQUEST_SET_NR_OPTION";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_TOE_INFO /* 2211 */:
                msg = "RIL_REQUEST_GET_TOE_INFO";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_CARRIER_AGGREGATION_MODE /* 2212 */:
                msg = "RIL_REQUEST_SET_CARRIER_AGGREGATION_MODE";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_CA_LINK_ENABLE_STATUS /* 2213 */:
                msg = "RIL_REQUEST_GET_CA_LINK_ENABLE_STATUS";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_CA_LINK_ENABLE_STATUS /* 2214 */:
                msg = "RIL_REQUEST_SET_CA_LINK_ENABLE_STATUS";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_CA_LINK_CAPABILITY_LIST /* 2215 */:
                msg = "RIL_REQUEST_GET_CA_LINK_CAPABILITY_LIST";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_LTE_DATA /* 2216 */:
                msg = "RIL_REQUEST_GET_LTE_DATA";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_LTE_RRC_STATE /* 2217 */:
                msg = "RIL_REQUEST_GET_LTE_RRC_STATE";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_LTE_BAND_ENABLE_STATUS /* 2218 */:
                msg = "RIL_REQUEST_SET_LTE_BAND_ENABLE_STATUS";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_BAND_PRIORITY_LIST /* 2219 */:
                msg = "RIL_REQUEST_GET_BAND_PRIORITY_LIST";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_BAND_PRIORITY_LIST /* 2220 */:
                msg = "RIL_REQUEST_SET_BAND_PRIORITY_LIST";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_4X4MIMO_ENABLED /* 2221 */:
                msg = "RIL_REQUEST_SET_4X4MIMO_ENABLED";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_4X4MIMO_ENABLED /* 2222 */:
                msg = "RIL_REQUEST_GET_4X4MIMO_ENABLED";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_LTE_BSR_TIMER /* 2223 */:
                msg = "RIL_REQUEST_GET_LTE_BSR_TIMER";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_LTE_BSR_TIMER /* 2224 */:
                msg = "RIL_REQUEST_SET_LTE_BSR_TIMER";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_LTE_1XRTT_CELL_LIST /* 2225 */:
                msg = "RIL_REQUEST_GET_LTE_1XRTT_CELL_LIST";
                break;
            case MtkRILConstants.RIL_REQUEST_CLEAR_LTE_AVAILABLE_FILE /* 2226 */:
                msg = "RIL_REQUEST_CLEAR_LTE_AVAILABLE_FILE";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_BAND_MODE /* 2227 */:
                msg = "RIL_REQUEST_GET_BAND_MODE";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_CA_BAND_MODE /* 2228 */:
                msg = "RIL_REQUEST_GET_CA_BAND_MODE";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_CAMPED_FEMTO_CELL_INFO /* 2229 */:
                msg = "RIL_REQUEST_GET_CAMPED_FEMTO_CELL_INFO";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_QAM_ENABLED /* 2230 */:
                msg = "RIL_REQUEST_SET_QAM_ENABLED";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_QAM_ENABLED /* 2231 */:
                msg = "RIL_REQUEST_GET_QAM_ENABLED";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_TM9_ENABLED /* 2232 */:
                msg = "RIL_REQUEST_SET_TM9_ENABLED";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_TM9_ENABLED /* 2233 */:
                msg = "RIL_REQUEST_GET_TM9_ENABLED";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_LTE_SCAN_DURATION /* 2234 */:
                msg = "RIL_REQUEST_SET_LTE_SCAN_DURATION";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_LTE_SCAN_DURATION /* 2235 */:
                msg = "RIL_REQUEST_GET_LTE_SCAN_DURATION";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_DISABLE_2G /* 2236 */:
                msg = "RIL_REQUEST_SET_DISABLE_2G";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_DISABLE_2G /* 2237 */:
                msg = "RIL_REQUEST_GET_DISABLE_2G";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_ECHOLOCATE_METRICS /* 2238 */:
                msg = "RIL_REQUEST_GET_ECHOLOCATE_METRICS";
                break;
            case MtkRILConstants.RIL_REQUEST_IWLAN_REGISTRATION_STATE /* 2239 */:
                msg = "RIL_REQUEST_IWLAN_REGISTRATION_STATE";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_ALL_BAND_MODE /* 2240 */:
                msg = "RIL_REQUEST_GET_ALL_BAND_MODE";
                break;
            case MtkRILConstants.RIL_REQUEST_SET_NR_BAND_MODE /* 2241 */:
                msg = "RIL_REQUEST_SET_NR_BAND_MODE";
                break;
            case MtkRILConstants.RIL_REQUEST_SWITCH_SIM_TYPE /* 2242 */:
                msg = "RIL_REQUEST_SWITCH_SIM_TYPE";
                break;
            case MtkRILConstants.RIL_REQUEST_GET_SIM_TYPE /* 2243 */:
                msg = "RIL_REQUEST_GET_SIM_TYPE";
                break;
            default:
                msg = "<unknown request> " + request;
                break;
        }
        return "MTK: " + msg;
    }

    public static String responseToStringEx(Integer request) {
        String msg;
        switch (request.intValue()) {
            case 1028:
                msg = "UNSOL_OEM_HOOK_RAW";
                break;
            case 3000:
                msg = "RIL_UNSOL_RESPONSE_PLMN_CHANGED";
                break;
            case MtkRILConstants.RIL_UNSOL_RESPONSE_REGISTRATION_SUSPENDED /* 3001 */:
                msg = "RIL_UNSOL_RESPONSE_REGISTRATION_SUSPENDED";
                break;
            case MtkRILConstants.RIL_UNSOL_GMSS_RAT_CHANGED /* 3003 */:
                msg = "RIL_UNSOL_GMSS_RAT_CHANGED";
                break;
            case MtkRILConstants.RIL_UNSOL_SMS_READY_NOTIFICATION /* 3012 */:
                msg = "RIL_UNSOL_SMS_READY_NOTIFICATION";
                break;
            case MtkRILConstants.RIL_UNSOL_INCOMING_CALL_INDICATION /* 3015 */:
                msg = "UNSOL_INCOMING_CALL_INDICATION";
                break;
            case MtkRILConstants.RIL_UNSOL_INVALID_SIM /* 3016 */:
                msg = "RIL_UNSOL_INVALID_SIM";
                break;
            case MtkRILConstants.RIL_UNSOL_PSEUDO_CELL_INFO /* 3017 */:
                msg = "RIL_UNSOL_PSEUDO_CELL_INFO";
                break;
            case MtkRILConstants.RIL_UNSOL_NETWORK_EVENT /* 3018 */:
                msg = "RIL_UNSOL_NETWORK_EVENT";
                break;
            case MtkRILConstants.RIL_UNSOL_MODULATION_INFO /* 3019 */:
                msg = "RIL_UNSOL_MODULATION_INFO";
                break;
            case MtkRILConstants.RIL_UNSOL_RESET_ATTACH_APN /* 3020 */:
                msg = "RIL_UNSOL_RESET_ATTACH_APN";
                break;
            case MtkRILConstants.RIL_UNSOL_DATA_ATTACH_APN_CHANGED /* 3021 */:
                msg = "RIL_UNSOL_DATA_ATTACH_APN_CHANGED";
                break;
            case MtkRILConstants.RIL_UNSOL_CDMA_CARD_INITIAL_ESN_OR_MEID /* 3023 */:
                msg = "RIL_UNSOL_CDMA_CARD_INITIAL_ESN_OR_MEID";
                break;
            case MtkRILConstants.RIL_UNSOL_CIPHER_INDICATION /* 3024 */:
                msg = "UNSOL_CIPHER_INDICATION";
                break;
            case MtkRILConstants.RIL_UNSOL_CRSS_NOTIFICATION /* 3025 */:
                msg = "UNSOL_CRSS_NOTIFICATION";
                break;
            case MtkRILConstants.RIL_UNSOL_SUPP_SVC_NOTIFICATION_EX /* 3026 */:
                msg = "UNSOL_SUPP_SVC_NOTIFICATION_EX";
                break;
            case MtkRILConstants.RIL_UNSOL_PHB_READY_NOTIFICATION /* 3028 */:
                msg = "UNSOL_PHB_READY_NOTIFICATION";
                break;
            case MtkRILConstants.RIL_UNSOL_FEMTOCELL_INFO /* 3029 */:
                msg = "UNSOL_FEMTOCELL_INFO";
                break;
            case MtkRILConstants.RIL_UNSOL_NETWORK_INFO /* 3030 */:
                msg = "UNSOL_NETWORK_INFO";
                break;
            case MtkRILConstants.RIL_UNSOL_PCO_DATA_AFTER_ATTACHED /* 3053 */:
                msg = "RIL_UNSOL_PCO_DATA_AFTER_ATTACHED";
                break;
            case MtkRILConstants.RIL_UNSOL_MD_DATA_RETRY_COUNT_RESET /* 3059 */:
                msg = "RIL_UNSOL_MD_DATA_RETRY_COUNT_RESET";
                break;
            case MtkRILConstants.RIL_UNSOL_REMOVE_RESTRICT_EUTRAN /* 3060 */:
                msg = "RIL_UNSOL_REMOVE_RESTRICT_EUTRAN";
                break;
            case MtkRILConstants.RIL_UNSOL_CALL_FORWARDING /* 3070 */:
                msg = "UNSOL_CALL_FORWARDING";
                break;
            case MtkRILConstants.RIL_UNSOL_ECONF_SRVCC_INDICATION /* 3072 */:
                msg = "RIL_UNSOL_ECONF_SRVCC_INDICATION";
                break;
            case MtkRILConstants.RIL_UNSOL_VSIM_OPERATION_INDICATION /* 3074 */:
                msg = "RIL_UNSOL_VSIM_OPERATION_INDICATION";
                break;
            case MtkRILConstants.RIL_UNSOL_MOBILE_WIFI_ROVEOUT /* 3075 */:
                msg = "RIL_UNSOL_MOBILE_WIFI_ROVEOUT";
                break;
            case MtkRILConstants.RIL_UNSOL_MOBILE_WIFI_HANDOVER /* 3076 */:
                msg = "RIL_UNSOL_MOBILE_WIFI_HANDOVER";
                break;
            case MtkRILConstants.RIL_UNSOL_ACTIVE_WIFI_PDN_COUNT /* 3077 */:
                msg = "RIL_UNSOL_ACTIVE_WIFI_PDN_COUNT";
                break;
            case MtkRILConstants.RIL_UNSOL_WIFI_RSSI_MONITORING_CONFIG /* 3078 */:
                msg = "RIL_UNSOL_WIFI_RSSI_MONITORING_CONFIG";
                break;
            case MtkRILConstants.RIL_UNSOL_WIFI_PDN_ERROR /* 3079 */:
                msg = "RIL_UNSOL_WIFI_PDN_ERROR";
                break;
            case MtkRILConstants.RIL_UNSOL_REQUEST_GEO_LOCATION /* 3080 */:
                msg = "RIL_UNSOL_REQUEST_GEO_LOCATION";
                break;
            case MtkRILConstants.RIL_UNSOL_WFC_PDN_STATE /* 3081 */:
                msg = "RIL_UNSOL_WFC_PDN_STATE";
                break;
            case MtkRILConstants.RIL_UNSOL_NATT_KEEP_ALIVE_CHANGED /* 3086 */:
                msg = "RIL_UNSOL_NATT_KEEP_ALIVE_CHANGED";
                break;
            case MtkRILConstants.RIL_UNSOL_WIFI_PDN_OOS /* 3088 */:
                msg = "RIL_UNSOL_WIFI_PDN_OOS";
                break;
            case MtkRILConstants.RIL_UNSOL_ECC_NUM /* 3095 */:
                msg = "RIL_UNSOL_ECC_NUM";
                break;
            case MtkRILConstants.RIL_UNSOL_MCCMNC_CHANGED /* 3096 */:
                msg = "RIL_UNSOL_MCCMNC_CHANGED";
                break;
            case MtkRILConstants.RIL_UNSOL_SIGNAL_STRENGTH_WITH_WCDMA_ECIO /* 3097 */:
                msg = "UNSOL_SIGNAL_STRENGTH_WITH_WCDMA_ECIO";
                break;
            case MtkRILConstants.RIL_UNSOL_NETWORK_REJECT_CAUSE /* 3109 */:
                msg = "RIL_UNSOL_NETWORK_REJECT_CAUSE";
                break;
            case MtkRILConstants.RIL_UNSOL_DSBP_STATE_CHANGED /* 3114 */:
                msg = "RIL_UNSOL_DSBP_STATE_CHANGED";
                break;
            case MtkRILConstants.RIL_UNSOL_SIM_SLOT_LOCK_POLICY_NOTIFY /* 3115 */:
                msg = "RIL_UNSOL_SIM_SLOT_LOCK_POLICY_NOTIFY";
                break;
            case MtkRILConstants.RIL_UNSOL_SIM_POWER_CHANGED /* 3124 */:
                msg = "RIL_UNSOL_SIM_POWER_CHANGED";
                break;
            case MtkRILConstants.RIL_UNSOL_CARD_DETECTED_IND /* 3125 */:
                msg = "RIL_UNSOL_CARD_DETECTED_IND";
                break;
            case MtkRILConstants.RIL_UNSOL_CALL_ADDITIONAL_INFO /* 3126 */:
                msg = "UNSOL_CALL_ADDITIONAL_INFO";
                break;
            case MtkRILConstants.RIL_UNSOL_QUALIFIED_NETWORK_TYPES_CHANGED /* 3130 */:
                msg = "RIL_UNSOL_QUALIFIED_NETWORK_TYPES_CHANGED";
                break;
            case MtkRILConstants.RIL_UNSOL_ON_DSDA_CHANGED /* 3131 */:
                msg = "RIL_UNSOL_ON_DSDA_CHANGED";
                break;
            case MtkRILConstants.RIL_UNSOL_IWLAN_CELLULAR_QUALITY_CHANGED_IND /* 3132 */:
                msg = "RIL_UNSOL_IWLAN_CELLULAR_QUALITY_CHANGED_IND";
                break;
            case MtkRILConstants.RIL_UNSOL_MOBILE_DATA_USAGE /* 3133 */:
                msg = "RIL_UNSOL_MOBILE_DATA_USAGE";
                break;
            case MtkRILConstants.RIL_UNSOL_NW_LIMIT /* 3134 */:
                msg = "RIL_UNSOL_NW_LIMIT";
                break;
            case MtkRILConstants.RIL_UNSOL_ICCID_CHANGED /* 3135 */:
                msg = "RIL_UNSOL_ICCID_CHANGED";
                break;
            case MtkRILConstants.RIL_UNSOL_PLMN_DATA /* 3136 */:
                msg = "RIL_UNSOL_PLMN_DATA";
                break;
            case MtkRILConstants.RIL_UNSOL_RESPONSE_SMS_EINFO_EXTENSIONS /* 3137 */:
                msg = "RIL_UNSOL_RESPONSE_SMS_EINFO_EXTENSIONS";
                break;
            case MtkRILConstants.RIL_UNSOL_TOE_INFO_IND /* 3138 */:
                msg = "RIL_UNSOL_TOE_INFO_IND";
                break;
            case MtkRILConstants.RIL_UNSOL_NETWORK_BAND_INFO /* 3139 */:
                msg = "RIL_UNSOL_NETWORK_BAND_INFO";
                break;
            case MtkRILConstants.RIL_UNSOL_SIB16_TIME_INFO_IND /* 3140 */:
                msg = "RIL_UNSOL_SIB16_TIME_INFO_IND";
                break;
            case MtkRILConstants.RIL_UNSOL_IWLAN_REGISTRATION_STATE_IND /* 3141 */:
                msg = "RIL_UNSOL_IWLAN_REGISTRATION_STATE_IND";
                break;
            case MtkRILConstants.RIL_UNSOL_NR_CA_BAND_IND /* 3142 */:
                msg = "RIL_UNSOL_NR_CA_BAND_IND";
                break;
            case MtkRILConstants.RIL_UNSOL_5GUW_INFO_IND /* 3143 */:
                msg = "RIL_UNSOL_5GUW_INFO_IND";
                break;
            case MtkRILConstants.RIL_UNSOL_NR_SYS_INFO /* 3144 */:
                msg = "RIL_UNSOL_NR_SYS_INFO";
                break;
            case MtkRILConstants.RIL_UNSOL_SIM_TYPE_INFO_IND /* 3147 */:
                msg = "RIL_UNSOL_SIM_TYPE_INFO_IND";
                break;
            default:
                msg = "<unknown response>";
                break;
        }
        return "MTK: " + msg;
    }

    public void registerForCsNetworkStateChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mCsNetworkStateRegistrants.add(r);
    }

    public void unregisterForCsNetworkStateChanged(Handler h) {
        this.mCsNetworkStateRegistrants.remove(h);
    }

    public void registerForSignalStrengthWithWcdmaEcioChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mSignalStrengthWithWcdmaEcioRegistrants.add(r);
    }

    public void unregisterForignalStrengthWithWcdmaEcioChanged(Handler h) {
        this.mSignalStrengthWithWcdmaEcioRegistrants.remove(h);
    }

    protected static String retToString(int req, Object ret) {
        return RIL.retToString(req, ret);
    }

    public void setTrm(int mode, Message result) {
        MtkRadioExModemProxy modemProxy = (MtkRadioExModemProxy) getMtkRadioExServiceProxy(MtkRadioExModemProxy.class, result);
        if (!modemProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_TRM, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                modemProxy.setTrm(rr.mSerial, mode);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(3, rr, "setTrm", e);
            }
        }
    }

    public void getATR(Message result) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, result);
        if (!simProxy.isEmpty()) {
            RILRequest rr = obtainRequest(2001, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                simProxy.getATR(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(5, rr, "getATR", e);
            }
        }
    }

    public void getSimTypeInfo(Message result) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, result);
        if (!simProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_SIM_TYPE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                simProxy.getSimTypeInfo(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(5, rr, "getSimTypeInfo", e);
            }
        }
    }

    public void switchSimType(int mode, Message result) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, result);
        if (!simProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SWITCH_SIM_TYPE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                simProxy.switchSimType(rr.mSerial, mode);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(5, rr, "switchSimType", e);
            }
        }
    }

    public void getIccid(Message result) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, result);
        if (!simProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SIM_GET_ICCID, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                simProxy.getIccid(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(5, rr, "getIccid", e);
            }
        }
    }

    public void setSimPower(int mode, Message result) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, result);
        if (!simProxy.isEmpty()) {
            RILRequest rr = obtainRequest(2002, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                simProxy.setSimPower(rr.mSerial, mode);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(5, rr, "setSimPower", e);
            }
        }
    }

    public void registerForVirtualSimOn(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mVirtualSimOn.add(r);
    }

    public void unregisterForVirtualSimOn(Handler h) {
        this.mVirtualSimOn.remove(h);
    }

    public void registerForVirtualSimOff(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mVirtualSimOff.add(r);
    }

    public void unregisterForVirtualSimOff(Handler h) {
        this.mVirtualSimOff.remove(h);
    }

    public void registerForIMEILock(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mImeiLockRegistrant.add(r);
    }

    public void unregisterForIMEILock(Handler h) {
        this.mImeiLockRegistrant.remove(h);
    }

    public void registerForImsiRefreshDone(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mImsiRefreshDoneRegistrant.add(r);
    }

    public void unregisterForImsiRefreshDone(Handler h) {
        this.mImsiRefreshDoneRegistrant.remove(h);
    }

    public void registerForRsuSimLockChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mRsuSimlockRegistrants.add(r);
    }

    public void unregisterForRsuSimLockChanged(Handler h) {
        this.mRsuSimlockRegistrants.remove(h);
    }

    public void registerForCardDetectedInd(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mCardDetectedIndRegistrant.add(r);
        if (this.mIsCardDetected) {
            r.notifyRegistrant();
        }
    }

    public void unregisterForCardDetectedInd(Handler h) {
        this.mCardDetectedIndRegistrant.remove(h);
    }

    public String lookupOperatorNameEons(int subId, String numeric, boolean desireLongName, int nLac) {
        String eonsIfExist;
        UiccController uiccController = UiccController.getInstance();
        MtkSIMRecords simRecord = uiccController.getIccRecords(this.mInstanceId.intValue(), 1);
        String sEons = null;
        StringBuilder lac_sb = new StringBuilder(Integer.toHexString(nLac));
        if (lac_sb.length() == 1 || lac_sb.length() == 2) {
            lac_sb.setCharAt(0, '*');
        } else {
            for (int i = 0; i < lac_sb.length() / 2; i++) {
                lac_sb.setCharAt(i, '*');
            }
        }
        if (this.mPhoneType == 1) {
            if (nLac == 65534 || nLac == -1) {
                nLac = 0;
            }
            if (simRecord != null) {
                try {
                    eonsIfExist = simRecord.getEonsIfExist(numeric, nLac, desireLongName);
                } catch (RuntimeException ex) {
                    Rlog.e(RILJ_LOG_TAG, "Exception while getEonsIfExist. " + ex);
                }
            } else {
                eonsIfExist = null;
            }
            sEons = eonsIfExist;
            if (sEons != null && !sEons.equals("")) {
                Rlog.v(RILJ_LOG_TAG, "lookupOperatorNameEons subId=" + subId + " numeric=" + numeric + " desireLongName=" + desireLongName + " nLac=" + lac_sb.toString());
                Rlog.d(RILJ_LOG_TAG, "lookupOperatorNameEons plmn name update to Eons: " + sEons);
                return sEons;
            }
            String mSimOperatorNumeric = simRecord != null ? simRecord.getOperatorNumeric() : null;
            if (mSimOperatorNumeric != null && mSimOperatorNumeric.equals(numeric)) {
                String sCphsOns = simRecord != null ? simRecord.getSIMCPHSOns() : null;
                if (!TextUtils.isEmpty(sCphsOns)) {
                    Rlog.d(RILJ_LOG_TAG, "lookupOperatorNameEons plmn name update to CPHS Ons: " + sCphsOns);
                    return sCphsOns;
                }
            }
        }
        return null;
    }

    public String lookupOperatorName5GEons(int subId, String numeric, boolean desireLongName, int nLac) {
        String str;
        UiccController uiccController = UiccController.getInstance();
        MtkSIMRecords simRecord = uiccController.getIccRecords(this.mInstanceId.intValue(), 1);
        String sEons = null;
        StringBuilder lac_sb = new StringBuilder(Integer.toHexString(nLac));
        if (lac_sb.length() == 1 || lac_sb.length() == 2) {
            lac_sb.setCharAt(0, '*');
        } else {
            for (int i = 0; i < lac_sb.length() / 2; i++) {
                lac_sb.setCharAt(i, '*');
            }
        }
        if (this.mPhoneType == 1) {
            if (nLac == 16777214 || nLac == -1) {
                nLac = 0;
                Rlog.d(RILJ_LOG_TAG, "invalid lac, set 0");
            }
            if (simRecord != null) {
                try {
                    str = simRecord.get5GEonsIfExist(numeric, nLac, desireLongName);
                } catch (RuntimeException ex) {
                    Rlog.e(RILJ_LOG_TAG, "Exception while get5GEonsIfExist. " + ex);
                }
            } else {
                str = null;
            }
            sEons = str;
            if (sEons != null && !sEons.equals("")) {
                Rlog.v(RILJ_LOG_TAG, "lookupOperatorName5GEons subId=" + subId + " numeric=" + numeric + " desireLongName=" + desireLongName + " nLac=" + lac_sb.toString());
                Rlog.d(RILJ_LOG_TAG, "lookupOperatorName5GEons plmn name update to 5GEons: " + sEons);
                return sEons;
            }
        }
        return null;
    }

    public String lookupOperatorNameNitz(int subId, String numeric, boolean desireLongName) {
        String telephonyProperty;
        String nitzOperatorName;
        String telephonyProperty2;
        int phoneId = SubscriptionManager.getPhoneId(subId);
        boolean isSeperatedPropertyDesign = MTK_RILJ_LOGD;
        String nitzOperatorNumeric = SystemProperties.get(MtkTelephonyProperties.PROPERTY_NITZ_OPER_CODE + "_" + phoneId);
        if (TextUtils.isEmpty(nitzOperatorNumeric)) {
            isSeperatedPropertyDesign = MTK_RILJ_LOGV;
            nitzOperatorNumeric = TelephonyManager.getTelephonyProperty(phoneId, MtkTelephonyProperties.PROPERTY_NITZ_OPER_CODE, "");
        }
        if (numeric != null && numeric.equals(nitzOperatorNumeric)) {
            if (desireLongName) {
                if (isSeperatedPropertyDesign) {
                    telephonyProperty2 = SystemProperties.get(MtkTelephonyProperties.PROPERTY_NITZ_OPER_LNAME + "_" + phoneId);
                } else {
                    telephonyProperty2 = TelephonyManager.getTelephonyProperty(phoneId, MtkTelephonyProperties.PROPERTY_NITZ_OPER_LNAME, "");
                }
                nitzOperatorName = telephonyProperty2;
            } else {
                if (isSeperatedPropertyDesign) {
                    telephonyProperty = SystemProperties.get(MtkTelephonyProperties.PROPERTY_NITZ_OPER_SNAME + "_" + phoneId);
                } else {
                    telephonyProperty = TelephonyManager.getTelephonyProperty(phoneId, MtkTelephonyProperties.PROPERTY_NITZ_OPER_SNAME, "");
                }
                nitzOperatorName = telephonyProperty;
            }
            Rlog.d(RILJ_LOG_TAG, "lookupOperatorNameNitz plmn name update to Nitz: " + nitzOperatorName);
            if (!TextUtils.isEmpty(nitzOperatorName)) {
                return nitzOperatorName;
            }
            return null;
        }
        return null;
    }

    public String lookupOperatorNameMVNO(int subId, String numeric, boolean desireLongName) {
        if (numeric == null) {
            return null;
        }
        String operatorName = MtkServiceStateTracker.lookupOperatorName(this.mMtkContext, subId, numeric, desireLongName);
        Rlog.d(RILJ_LOG_TAG, "lookupOperatorNameMVNO plmn name update to TS.25/MVNO: " + operatorName);
        return operatorName;
    }

    public String lookupOperator5GName(int subId, String numeric, boolean desireLongName, int nLac) {
        String operatorName = lookupOperatorName5GEons(subId, numeric, desireLongName, nLac);
        if (TextUtils.isEmpty(operatorName)) {
            return null;
        }
        return operatorName;
    }

    public String lookupOperatorSPN(String numeric) {
        String operatorName = null;
        Phone phone = PhoneFactory.getPhone(this.mInstanceId.intValue());
        if (phone != null) {
            ServiceStateTracker sst = phone.getServiceStateTracker();
            String plmn = sst.getServiceState().getOperatorNumeric();
            if (!TextUtils.isEmpty(plmn) && !TextUtils.isEmpty(numeric) && plmn.equals(numeric)) {
                operatorName = sst.getServiceProviderName();
                Rlog.d(RILJ_LOG_TAG, "lookupOperatorSPN " + operatorName);
            }
        }
        if (TextUtils.isEmpty(operatorName)) {
            return null;
        }
        return operatorName;
    }

    private int getPlmnListDisplayRule(String rplmn, String plmn) {
        String optr = SystemProperties.get(DataSubConstants.PROPERTY_OPERATOR_OPTR);
        return ((TextUtils.isEmpty(optr) || !optr.equals("OP07")) && !TextUtils.isEmpty(rplmn) && !TextUtils.isEmpty(plmn) && rplmn.equals(plmn)) ? 7 : 7;
    }

    public String lookupOperatorNameForPlmnList(int subId, String numeric, boolean desireLongName, int nLac) {
        String mPlmn = null;
        Phone phone = PhoneFactory.getPhone(this.mInstanceId.intValue());
        if (phone != null) {
            ServiceStateTracker sst = phone.getServiceStateTracker();
            ServiceState ss = sst.mSS;
            mPlmn = ss.getOperatorNumeric();
        }
        int rule = getPlmnListDisplayRule(mPlmn, numeric);
        return lookupOperatorName(subId, numeric, desireLongName, nLac, rule, null);
    }

    public String lookupOperatorName(int subId, String numeric, boolean desireLongName, int nLac) {
        return lookupOperatorName(subId, numeric, desireLongName, nLac, 7, null);
    }

    public String lookupOperatorName(int subId, String numeric, boolean desireLongName, int nLac, ServiceState ss) {
        return lookupOperatorName(subId, numeric, desireLongName, nLac, 7, ss);
    }

    public String lookupOperatorName(int subId, String numeric, boolean desireLongName, int nLac, int display_rule, ServiceState ss) {
        if (TelephonyBaseUtilsStub.isMiuiRom() && (display_rule & 2) == 2 && MiuiNetworkControllerStub.get(this.mPhoneId.intValue()).isAlwaysSetNitzName(numeric)) {
            String operatorName = lookupOperatorNameNitz(subId, numeric, desireLongName);
            Rlog.d(RILJ_LOG_TAG, "isShowNitz numeric = " + numeric + ", operatorName = " + operatorName);
            if (!TextUtils.isEmpty(operatorName)) {
                return operatorName;
            }
        }
        if ((display_rule & 2) == 2 && TextUtils.equals(numeric, "71610")) {
            String operatorName2 = lookupOperatorNameNitz(subId, numeric, desireLongName);
            if (!TextUtils.isEmpty(operatorName2)) {
                return operatorName2;
            }
        }
        if ((display_rule & 8) == 8) {
            String operatorName3 = lookupOperatorSPN(numeric);
            if (!TextUtils.isEmpty(operatorName3)) {
                return operatorName3;
            }
        }
        if ((display_rule & 1) == 1 && !isIgnoreSetEonsName(numeric)) {
            if (isNRAttached(ss)) {
                String operatorName4 = lookupOperator5GName(subId, numeric, desireLongName, nLac);
                if (!TextUtils.isEmpty(operatorName4)) {
                    return operatorName4;
                }
            }
            String operatorName5 = lookupOperatorNameEons(subId, numeric, desireLongName, nLac);
            if (desireLongName && (display_rule & 2) == 2) {
                String res = MiuiNetworkControllerStub.get(this.mPhoneId.intValue()).lookupOperatorNameEons(operatorName5, this.mInstanceId.intValue(), subId, numeric, desireLongName, new TriFunction() { // from class: com.mediatek.internal.telephony.MtkRIL$$ExternalSyntheticLambda0
                    public final Object apply(Object obj, Object obj2, Object obj3) {
                        return this.f$0.lambda$lookupOperatorName$0((Integer) obj, (String) obj2, (Boolean) obj3);
                    }
                });
                if (!TextUtils.isEmpty(res)) {
                    return res;
                }
            }
            if (!TextUtils.isEmpty(operatorName5)) {
                return operatorName5;
            }
        }
        if ((display_rule & 2) == 2) {
            String operatorName6 = lookupOperatorNameNitz(subId, numeric, desireLongName);
            if (!TextUtils.isEmpty(operatorName6)) {
                return operatorName6;
            }
        }
        if ((display_rule & 4) == 4) {
            String operatorName7 = lookupOperatorNameMVNO(subId, numeric, desireLongName);
            if (TextUtils.isEmpty(operatorName7)) {
                return null;
            }
            return operatorName7;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$lookupOperatorName$0(Integer a, String b, Boolean c) {
        return lookupOperatorNameNitz(a.intValue(), b, c.booleanValue());
    }

    public boolean isNRAttached(ServiceState ss) {
        Phone phone;
        if (ss != null && ss.getDataRegistrationState() == 0 && ss.getDataNetworkType() == 20) {
            return MTK_RILJ_LOGD;
        }
        if (ss == null && (phone = PhoneFactory.getPhone(this.mInstanceId.intValue())) != null) {
            ServiceState state = phone.getServiceState();
            if (state.getDataRegistrationState() == 0 && state.getDataNetworkType() == 20) {
                return MTK_RILJ_LOGD;
            }
            return MTK_RILJ_LOGV;
        }
        return MTK_RILJ_LOGV;
    }

    public void setNetworkSelectionModeManualWithAct(String operatorNumeric, String act, int mode, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(2005, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " operatorNumeric = " + operatorNumeric);
            try {
                networkProxy.setNetworkSelectionModeManualWithAct(rr.mSerial, RILUtils.convertNullToEmptyString(operatorNumeric), RILUtils.convertNullToEmptyString(act), Integer.toString(mode));
            } catch (RemoteException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "setNetworkSelectionModeManualWithAct", e);
            }
        }
    }

    public boolean hidePLMN(String mccmnc) {
        for (String plmn : this.hide_plmns) {
            if (plmn.equals(mccmnc)) {
                return MTK_RILJ_LOGD;
            }
        }
        return MTK_RILJ_LOGV;
    }

    public void getAvailableNetworksWithAct(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(2006, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.getAvailableNetworksWithAct(rr.mSerial);
            } catch (RemoteException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getAvailableNetworksWithAct", e);
            }
        }
    }

    public void cancelAvailableNetworks(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_ABORT_QUERY_AVAILABLE_NETWORKS, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.cancelAvailableNetworks(rr.mSerial);
            } catch (RemoteException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getAvailableNetworks", e);
            }
        }
    }

    public void getFemtoCellList(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_FEMTOCELL_LIST, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.getFemtocellList(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getFemtoCellList", e);
            }
        }
    }

    public void abortFemtoCellList(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_ABORT_FEMTOCELL_LIST, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.abortFemtocellList(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "abortFemtoCellList", e);
            }
        }
    }

    public void selectFemtoCell(FemtoCellInfo femtocell, Message result) {
        int act;
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SELECT_FEMTOCELL, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            int act2 = femtocell.getCsgRat();
            if (act2 == 14) {
                act = 7;
            } else if (act2 == 3) {
                act = 2;
            } else {
                act = 0;
            }
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " csgId=" + femtocell.getCsgId() + " plmn=" + femtocell.getOperatorNumeric() + " rat=" + femtocell.getCsgRat() + " act=" + act);
            try {
                networkProxy.selectFemtocell(rr.mSerial, RILUtils.convertNullToEmptyString(femtocell.getOperatorNumeric()), RILUtils.convertNullToEmptyString(Integer.toString(act)), RILUtils.convertNullToEmptyString(Integer.toString(femtocell.getCsgId())));
            } catch (Exception e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "selectFemtoCell", e);
            }
        }
    }

    public void queryFemtoCellSystemSelectionMode(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_QUERY_FEMTOCELL_SYSTEM_SELECTION_MODE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.queryFemtoCellSystemSelectionMode(rr.mSerial);
            } catch (Exception e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "queryFemtoCellSystemSelectionMode", e);
            }
        }
    }

    public void setFemtoCellSystemSelectionMode(int mode, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_FEMTOCELL_SYSTEM_SELECTION_MODE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " mode=" + mode);
            try {
                networkProxy.setFemtoCellSystemSelectionMode(rr.mSerial, mode);
            } catch (Exception e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "setFemtoCellSystemSelectionMode", e);
            }
        }
    }

    public void configA2Offset(int offset, int threshBound, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_CONFIG_A2_OFFSET, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " offset=" + offset + " threshBound=" + threshBound);
            try {
                networkProxy.cfgA2offset(rr.mSerial, offset, threshBound);
            } catch (Exception e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "configA2Offset", e);
            }
        }
    }

    public void configB1Offset(int offset, int threshBound, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_CONFIG_B1_OFFSET, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " offset=" + offset + " threshBound=" + threshBound);
            try {
                networkProxy.cfgB1offset(rr.mSerial, offset, threshBound);
            } catch (Exception e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "configB1Offset", e);
            }
        }
    }

    public void enableScgFailure(boolean enable, int T1, int P1, int T2, Message result) {
        Exception e;
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_ENABLE_SCG_FAILURE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " enable=" + enable + " T1=" + T1 + " P1=" + P1 + " T2=" + T2);
            try {
                try {
                    networkProxy.enableSCGfailure(rr.mSerial, enable, T1, P1, T2);
                } catch (Exception e2) {
                    e = e2;
                    handleMtkRadioProxyExceptionForRR(4, rr, "enableScgFailure", e);
                }
            } catch (Exception e3) {
                e = e3;
            }
        }
    }

    public void disableNR(boolean enable, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_DISABLE_NR, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " enable=" + enable);
            try {
                networkProxy.disableNR(rr.mSerial, enable);
            } catch (Exception e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "disableNR", e);
            }
        }
    }

    public void setNROption(int option, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_NR_OPTION, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " option=" + option);
            try {
                networkProxy.setNROption(rr.mSerial, option);
            } catch (Exception e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "setNROption", e);
            }
        }
    }

    public void setTxPower(int power, Message result) {
        MtkRadioExModemProxy modemProxy = (MtkRadioExModemProxy) getMtkRadioExServiceProxy(MtkRadioExModemProxy.class, result);
        if (!modemProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_TX_POWER, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " power=" + power);
            try {
                modemProxy.setTxPower(rr.mSerial, power);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(3, rr, "setTxPower", e);
            }
        }
    }

    public void searchStoredFrequencyInfo(int operation, int plmnId, int rat, int[] freq, Message result) {
        Exception e;
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SEARCH_STORED_FREQUENCY_INFO, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " operation=" + operation + " plmnId=" + plmnId + " rat=" + rat + " length=" + freq.length);
            try {
                try {
                    networkProxy.setSearchStoredFreqInfo(rr.mSerial, operation, plmnId, rat, freq);
                } catch (Exception e2) {
                    e = e2;
                    handleMtkRadioProxyExceptionForRR(4, rr, "searchStoredFrequencyInfo", e);
                }
            } catch (Exception e3) {
                e = e3;
            }
        }
    }

    public void searchRat(int[] rat, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SEARCH_RAT, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " length=" + rat.length);
            try {
                networkProxy.setSearchRat(rr.mSerial, rat);
            } catch (Exception e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "searchRat", e);
            }
        }
    }

    public void setBackgroundSearchTimer(int duration, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_BACKGROUND_SEARCH_TIMER, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " duration=" + duration);
            try {
                networkProxy.setBgsrchDeltaSleepTimer(rr.mSerial, duration);
            } catch (Exception e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "setBackgroundSearchTimer", e);
            }
        }
    }

    public void deactivateNrScgCommunication(boolean deactivate, boolean allowSCGAdd, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_DEACTIVATE_NR_SCG_COMMUNICATION, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " deactivate=" + deactivate + " allowSCGAdd=" + allowSCGAdd);
            try {
                networkProxy.deactivateNrScgCommunication(rr.mSerial, deactivate, allowSCGAdd);
            } catch (Exception e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "deactivateNrScgCommunication", e);
            }
        }
    }

    public void getDeactivateNrScgCommunication(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_DEACTIVATE_NR_SCG_COMMUNICATION, result, this.mRILDefaultWorkSource);
            riljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.getDeactivateNrScgCommunication(rr.mSerial);
            } catch (Exception e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getDeactivateNrScgCommunication", e);
            }
        }
    }

    public void getSignalStrengthWithWcdmaEcio(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SIGNAL_STRENGTH_WITH_WCDMA_ECIO, result, this.mRILDefaultWorkSource);
            riljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.getSignalStrengthWithWcdmaEcio(rr.mSerial);
            } catch (Exception e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getSignalStrengthWithWcdmaEcio", e);
            }
        }
    }

    public void setModemPower(boolean isOn, Message result) {
        RILRequest rr;
        mtkRiljLog("Set Modem power as: " + isOn);
        MtkRadioExModemProxy modemProxy = (MtkRadioExModemProxy) getMtkRadioExServiceProxy(MtkRadioExModemProxy.class, result);
        if (!modemProxy.isEmpty()) {
            if (isOn) {
                rr = obtainRequest(MtkRILConstants.RIL_REQUEST_MODEM_POWERON, result, this.mRILDefaultWorkSource);
            } else {
                rr = obtainRequest(2004, result, this.mRILDefaultWorkSource);
            }
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " " + isOn);
            try {
                modemProxy.setModemPower(rr.mSerial, isOn);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(3, rr, "setModemPower", e);
            }
        }
    }

    public void setInvalidSimInfo(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mInvalidSimInfoRegistrant.add(r);
    }

    public void unSetInvalidSimInfo(Handler h) {
        this.mInvalidSimInfoRegistrant.remove(h);
    }

    public void registerForNetworkEvent(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mNetworkEventRegistrants.add(r);
    }

    public void unregisterForNetworkEvent(Handler h) {
        this.mNetworkEventRegistrants.remove(h);
    }

    public void registerForNetworkReject(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mNetworkRejectRegistrants.add(r);
    }

    public void unregisterForNetworkReject(Handler h) {
        this.mNetworkRejectRegistrants.remove(h);
    }

    public void registerForModulation(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mModulationRegistrants.add(r);
    }

    public void unregisterForModulation(Handler h) {
        this.mModulationRegistrants.remove(h);
    }

    public void registerForFemtoCellInfo(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mFemtoCellInfoRegistrants.add(r);
    }

    public void unregisterForFemtoCellInfo(Handler h) {
        this.mFemtoCellInfoRegistrants.remove(h);
    }

    public void registerForSmsReady(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mSmsReadyRegistrants.add(r);
        if (this.mIsSmsReady) {
            r.notifyRegistrant();
        }
    }

    public void unregisterForSmsReady(Handler h) {
        this.mSmsReadyRegistrants.remove(h);
    }

    public void registerForSmsInfoExt(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mSmsInfoExtRegistrants.add(r);
    }

    public void unregisterForSmsInfoExt(Handler h) {
        this.mSmsInfoExtRegistrants.remove(h);
    }

    public void setOnMeSmsFull(Handler h, int what, Object obj) {
        this.mMeSmsFullRegistrant = new Registrant(h, what, obj);
    }

    public void unSetOnMeSmsFull(Handler h) {
        this.mMeSmsFullRegistrant.clear();
    }

    public void getSmsParameters(Message result) {
        MtkRadioExMessagingProxy messagingProxy = (MtkRadioExMessagingProxy) getMtkRadioExServiceProxy(MtkRadioExMessagingProxy.class, result);
        if (!messagingProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_SMS_PARAMS, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                messagingProxy.getSmsParameters(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(2, rr, "getSmsParameters", e);
            }
        }
    }

    public void setSmsParameters(MtkSmsParameters params, Message result) {
        MtkRadioExMessagingProxy messagingProxy = (MtkRadioExMessagingProxy) getMtkRadioExServiceProxy(MtkRadioExMessagingProxy.class, result);
        if (!messagingProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_SMS_PARAMS, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                messagingProxy.setSmsParameters(rr.mSerial, params);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(2, rr, "setSmsParameters", e);
            }
        }
    }

    public void setEtws(int mode, Message result) {
        MtkRadioExMessagingProxy messagingProxy = (MtkRadioExMessagingProxy) getMtkRadioExServiceProxy(MtkRadioExMessagingProxy.class, result);
        if (!messagingProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_ETWS, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                messagingProxy.setEtws(rr.mSerial, mode);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(2, rr, "setEtws", e);
            }
        }
    }

    public void removeCellBroadcastMsg(int channelId, int serialId, Message result) {
        MtkRadioExMessagingProxy messagingProxy = (MtkRadioExMessagingProxy) getMtkRadioExServiceProxy(MtkRadioExMessagingProxy.class, result);
        if (!messagingProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_REMOVE_CB_MESSAGE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                messagingProxy.removeCbMsg(rr.mSerial, channelId, serialId);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(2, rr, "removeCellBroadcastMsg", e);
            }
        }
    }

    public void getSmsSimMemoryStatus(Message result) {
        MtkRadioExMessagingProxy messagingProxy = (MtkRadioExMessagingProxy) getMtkRadioExServiceProxy(MtkRadioExMessagingProxy.class, result);
        if (!messagingProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_SMS_SIM_MEM_STATUS, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                messagingProxy.getSmsMemStatus(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(2, rr, "getSmsSimMemoryStatus", e);
            }
        }
    }

    public void setGsmBroadcastLangs(String lang, Message result) {
        MtkRadioExMessagingProxy messagingProxy = (MtkRadioExMessagingProxy) getMtkRadioExServiceProxy(MtkRadioExMessagingProxy.class, result);
        if (!messagingProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GSM_SET_BROADCAST_LANGUAGE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                messagingProxy.setGsmBroadcastLangs(rr.mSerial, lang);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(2, rr, "setGsmBroadcastLangs", e);
            }
        }
    }

    public void getGsmBroadcastLangs(Message result) {
        MtkRadioExMessagingProxy messagingProxy = (MtkRadioExMessagingProxy) getMtkRadioExServiceProxy(MtkRadioExMessagingProxy.class, result);
        if (!messagingProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GSM_GET_BROADCAST_LANGUAGE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                messagingProxy.getGsmBroadcastLangs(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(2, rr, "getGsmBroadcastLangs", e);
            }
        }
    }

    public void getGsmBroadcastActivation(Message result) {
        MtkRadioExMessagingProxy messagingProxy = (MtkRadioExMessagingProxy) getMtkRadioExServiceProxy(MtkRadioExMessagingProxy.class, result);
        if (!messagingProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_GSM_SMS_BROADCAST_ACTIVATION, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                messagingProxy.getGsmBroadcastActivation(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(2, rr, "getGsmBroadcastActivation", e);
            }
        }
    }

    public void registerForPsNetworkStateChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mPsNetworkStateRegistrants.add(r);
    }

    public void unregisterForPsNetworkStateChanged(Handler h) {
        this.mPsNetworkStateRegistrants.remove(h);
    }

    public void registerForNetworkInfo(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mNetworkInfoRegistrant.add(r);
    }

    public void unregisterForNetworkInfo(Handler h) {
        this.mNetworkInfoRegistrant.remove(h);
    }

    public void changeBarringPassword(String facility, String oldPwd, String newPwd, String newCfm, Message result) {
        Exception e;
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (!voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(44, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + "facility = " + facility);
            try {
                try {
                    voiceProxy.setBarringPasswordCheckedByNW(rr.mSerial, facility, oldPwd, newPwd, newCfm);
                } catch (RemoteException | RuntimeException e2) {
                    e = e2;
                    handleMtkRadioProxyExceptionForRR(6, rr, "changeBarringPasswordCheckedByNW", e);
                }
            } catch (RemoteException | RuntimeException e3) {
                e = e3;
            }
        }
    }

    public void setCLIP(int clipEnable, Message result) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (!voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_CLIP, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " clipEnable = " + clipEnable);
            try {
                voiceProxy.setClip(rr.mSerial, clipEnable);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "setCLIP", e);
            }
        }
    }

    public void getCOLP(Message result) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (!voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_COLP, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                voiceProxy.getColp(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "getCOLP", e);
            }
        }
    }

    public void getCOLR(Message result) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (!voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_COLR, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                voiceProxy.getColr(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "getCOLR", e);
            }
        }
    }

    public void sendCNAP(String cnapssMessage, Message result) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (!voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SEND_CNAP, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + "CNAP string = " + cnapssMessage);
            try {
                voiceProxy.sendCnap(rr.mSerial, cnapssMessage);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "setKeepAliveByIpData", e);
            }
        }
    }

    public void setCOLR(int colrEnable, Message result) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (!voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_COLR, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " colrEnable = " + colrEnable);
            try {
                voiceProxy.setColr(rr.mSerial, colrEnable);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "setCOLR", e);
            }
        }
    }

    public void setCOLP(int colpEnable, Message result) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (!voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_COLP, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " colpEnable = " + colpEnable);
            try {
                voiceProxy.setColp(rr.mSerial, colpEnable);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "setCOLP", e);
            }
        }
    }

    public void queryCallForwardInTimeSlotStatus(int cfReason, int serviceClass, Message result) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (!voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_QUERY_CALL_FORWARD_IN_TIME_SLOT, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " cfreason = " + cfReason + " serviceClass = " + serviceClass);
            try {
                voiceProxy.queryCallForwardInTimeSlotStatus(rr.mSerial, cfReason, serviceClass);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "queryCallForwardInTimeSlotStatus", e);
            }
        }
    }

    public void setCallForwardInTimeSlot(int action, int cfReason, int serviceClass, String number, int timeSeconds, long[] timeSlot, Message result) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (!voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_CALL_FORWARD_IN_TIME_SLOT, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " action = " + action + " cfReason = " + cfReason + " serviceClass = " + serviceClass + " timeSeconds = " + timeSeconds);
            try {
                voiceProxy.setCallForwardInTimeSlot(rr.mSerial, action, cfReason, serviceClass, number, timeSeconds, timeSlot);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "setCallForwardInTimeSlot", e);
            }
        }
    }

    public void runGbaAuthentication(String nafFqdn, String nafSecureProtocolId, boolean forceRun, int netId, int phoneId, Message result) {
        Exception e;
        IMtkRadioEx radioProxy = getMtkRadioExProxy(result);
        if (radioProxy != null) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_RUN_GBA, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + ">  " + RILUtils.requestToString(rr.mRequest) + " nafFqdn = " + nafFqdn + " nafSecureProtocolId = " + nafSecureProtocolId + " forceRun = " + forceRun + " netId = " + netId);
            try {
                try {
                    radioProxy.runGbaAuthentication(rr.mSerial, nafFqdn, nafSecureProtocolId, forceRun, netId);
                } catch (RemoteException | RuntimeException e2) {
                    e = e2;
                    handleMtkRadioProxyExceptionForRR(0, rr, "runGbaAuthentication", e);
                }
            } catch (RemoteException | RuntimeException e3) {
                e = e3;
            }
        }
    }

    public void registerForCallForwardingInfo(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        Rlog.d(RILJ_LOG_TAG, "call registerForCallForwardingInfo, Handler : " + h);
        this.mCallForwardingInfoRegistrants.add(r);
        if (this.mCfuReturnValue != null) {
            r.notifyRegistrant(new AsyncResult((Object) null, this.mCfuReturnValue, (Throwable) null));
        }
    }

    public void unregisterForCallForwardingInfo(Handler h) {
        this.mCallForwardingInfoRegistrants.remove(h);
    }

    public void setOnIncomingCallIndication(Handler h, int what, Object obj) {
        this.mIncomingCallIndicationRegistrant = new Registrant(h, what, obj);
    }

    public void unsetOnIncomingCallIndication(Handler h) {
        this.mIncomingCallIndicationRegistrant.clear();
    }

    public void registerForCallAdditionalInfo(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mCallAdditionalInfoRegistrants.add(r);
    }

    public void unregisterForCallAdditionalInfo(Handler h) {
        this.mCallAdditionalInfoRegistrants.remove(h);
    }

    public void setOnSuppServiceNotificationEx(Handler h, int what, Object obj) {
        this.mSsnExRegistrant = new Registrant(h, what, obj);
    }

    public void unSetOnSuppServiceNotificationEx(Handler h) {
        if (this.mSsnExRegistrant != null && this.mSsnExRegistrant.getHandler() == h) {
            this.mSsnExRegistrant.clear();
            this.mSsnExRegistrant = null;
        }
    }

    public void setOnCallRelatedSuppSvc(Handler h, int what, Object obj) {
        this.mCallRelatedSuppSvcRegistrant = new Registrant(h, what, obj);
    }

    public void unSetOnCallRelatedSuppSvc(Handler h) {
        this.mCallRelatedSuppSvcRegistrant.clear();
    }

    public void registerForCipherIndication(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mCipherIndicationRegistrants.add(r);
    }

    public void unregisterForCipherIndication(Handler h) {
        this.mCipherIndicationRegistrants.remove(h);
    }

    public void hangupAll(Message result) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (!voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_HANGUP_ALL, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                voiceProxy.hangupAll(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "hangupAll", e);
            }
        }
    }

    public void setCallIndication(int mode, int callId, int seqNumber, int cause, Message result) {
        Exception e;
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (!voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_CALL_INDICATION, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " " + mode + ", " + callId + ", " + seqNumber + ", " + cause);
            try {
                try {
                    voiceProxy.setCallIndication(rr.mSerial, mode, callId, seqNumber, cause);
                } catch (RemoteException | RuntimeException e2) {
                    e = e2;
                    handleMtkRadioProxyExceptionForRR(6, rr, "setCallIndication", e);
                }
            } catch (RemoteException | RuntimeException e3) {
                e = e3;
            }
        }
    }

    public void hangupConnectionWithCause(int gsmIndex, int cause, Message result) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (!voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_HANGUP_WITH_REASON, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " gsmIndex = " + gsmIndex);
            try {
                voiceProxy.hangupWithReason(rr.mSerial, gsmIndex, cause);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "hangupConnectionWithCause", e);
            }
        }
    }

    public void setEccMode(String number, int enable, int airplaneMode, int imsReg, Message result) {
        Exception e;
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (!voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_ECC_MODE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " number=" + number + " enable=" + enable + " airplaneMode=" + airplaneMode + " imsReg=" + imsReg);
            try {
                try {
                    voiceProxy.setEccMode(rr.mSerial, number, enable, airplaneMode, imsReg);
                } catch (RemoteException | RuntimeException e2) {
                    e = e2;
                    handleMtkRadioProxyExceptionForRR(6, rr, "setEccMode", e);
                }
            } catch (RemoteException | RuntimeException e3) {
                e = e3;
            }
        }
    }

    public void setCallSubAddress(boolean enable, Message result) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (!voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_CALL_SUB_ADDRESS, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)) + " enable=" + enable);
            try {
                voiceProxy.setCallSubAddress(rr.mSerial, enable);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "setCallSubAddress error", e);
            }
        }
    }

    public void getCallSubAddress(Message result) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (!voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_CALL_SUB_ADDRESS, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                voiceProxy.getCallSubAddress(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "getCallSubAddress", e);
            }
        }
    }

    public void setQamEnabled(boolean ulOrDl, boolean enabled, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_QAM_ENABLED, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)) + " enabled=" + enabled);
            try {
                networkProxy.setQamEnabled(rr.mSerial, ulOrDl, enabled);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "setQamEnabled error", e);
            }
        }
    }

    public void getQamEnabled(boolean ulOrDl, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_QAM_ENABLED, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)) + " ulOrDl=" + ulOrDl);
            try {
                networkProxy.getQamEnabled(rr.mSerial, ulOrDl);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getQamEnabled error", e);
            }
        }
    }

    public void setTm9Enabled(boolean fddOrTdd, boolean enabled, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_TM9_ENABLED, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)) + " enabled=" + enabled);
            try {
                networkProxy.setTm9Enabled(rr.mSerial, fddOrTdd, enabled);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "setTm9Enabled error", e);
            }
        }
    }

    public void getTm9Enabled(boolean fddOrTdd, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_TM9_ENABLED, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)) + " fddOrTdd=" + fddOrTdd);
            try {
                networkProxy.getTm9Enabled(rr.mSerial, fddOrTdd);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getTm9Enabled error", e);
            }
        }
    }

    public void setLteScanDuration(int duration, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_LTE_SCAN_DURATION, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)) + " duration=" + duration);
            try {
                networkProxy.setLteScanDuration(rr.mSerial, duration);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "setLteScanDuration error", e);
            }
        }
    }

    public void getLteScanDuration(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_LTE_SCAN_DURATION, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)));
            try {
                networkProxy.getLteScanDuration(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getLteScanDuration error", e);
            }
        }
    }

    public void setEccNum(String eccListWithCard, String eccListNoCard) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, (Message) null);
        if (!voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_ECC_NUM, null, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " eccListWithCard: " + eccListWithCard + ", eccListNoCard: " + eccListNoCard);
            try {
                voiceProxy.setEccNum(rr.mSerial, eccListWithCard, eccListNoCard);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "setEccNum", e);
            }
        }
    }

    public void getEccNum() {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, (Message) null);
        if (!voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_ECC_NUM, null, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                voiceProxy.getEccNum(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "getEccNum", e);
            }
        }
    }

    public void registerForPseudoCellInfo(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mPseudoCellInfoRegistrants.add(r);
    }

    public void unregisterForPseudoCellInfo(Handler h) {
        this.mPseudoCellInfoRegistrants.remove(h);
    }

    public void setApcMode(int apcMode, boolean reportOn, int reportInterval, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_PSEUDO_CELL_MODE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " " + apcMode + ", " + reportOn + ", " + reportInterval);
            int reportMode = !reportOn ? 0 : 1;
            try {
                networkProxy.setApcMode(rr.mSerial, apcMode, reportMode, reportInterval);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "setApcMode", e);
            }
        }
    }

    public void getApcInfo(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_PSEUDO_CELL_INFO, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.getApcInfo(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getApcInfo", e);
            }
        }
    }

    public void triggerModeSwitchByEcc(int mode, Message result) {
        MtkRadioExModemProxy modemProxy = (MtkRadioExModemProxy) getMtkRadioExServiceProxy(MtkRadioExModemProxy.class, result);
        if (!modemProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SWITCH_MODE_FOR_ECC, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                modemProxy.triggerModeSwitchByEcc(rr.mSerial, mode);
                Message msg = this.mRilHandler.obtainMessage(5, Integer.valueOf(rr.mSerial));
                this.mRilHandler.sendMessageDelayed(msg, 2000L);
            } catch (RemoteException e) {
                handleMtkRadioProxyExceptionForRR(3, rr, "triggerModeSwitchByEcc", e);
            }
        }
    }

    public void getSmsRuimMemoryStatus(Message result) {
        MtkRadioExMessagingProxy messagingProxy = (MtkRadioExMessagingProxy) getMtkRadioExServiceProxy(MtkRadioExMessagingProxy.class, result);
        if (!messagingProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_SMS_RUIM_MEM_STATUS, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                messagingProxy.getSmsRuimMemoryStatus(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(2, rr, "getSmsSimMemoryStatus", e);
            }
        }
    }

    protected ArrayList<HardwareConfig> convertHalHwConfigList(ArrayList<android.hardware.radio.V1_0.HardwareConfig> hwListRil, RIL ril) {
        HardwareConfig hw;
        int num = hwListRil.size();
        ArrayList<HardwareConfig> response = new ArrayList<>(num);
        for (android.hardware.radio.V1_0.HardwareConfig hwRil : hwListRil) {
            int type = hwRil.type;
            switch (type) {
                case 0:
                    HardwareConfig hw2 = new MtkHardwareConfig(type);
                    HardwareConfigModem hwModem = (HardwareConfigModem) hwRil.modem.get(0);
                    hw2.assignModem(hwRil.uuid, hwRil.state, hwModem.rilModel, hwModem.rat, hwModem.maxVoice, hwModem.maxData, hwModem.maxStandby);
                    hw = hw2;
                    break;
                case 1:
                    hw = new MtkHardwareConfig(type);
                    hw.assignSim(hwRil.uuid, hwRil.state, ((HardwareConfigSim) hwRil.sim.get(0)).modemUuid);
                    break;
                default:
                    throw new RuntimeException("RIL_REQUEST_GET_HARDWARE_CONFIG invalid hardward type:" + type);
            }
            response.add(hw);
        }
        return response;
    }

    public void setResumeRegistration(int sessionId, Message result) {
        IMtkRadioEx radioProxy = getMtkRadioExProxy(result);
        if (radioProxy != null) {
            RILRequest rr = obtainRequest(2000, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " sessionId = " + sessionId);
            try {
                radioProxy.setResumeRegistration(rr.mSerial, sessionId);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(0, rr, "setResumeRegistration", e);
            }
        }
    }

    public void handleStkCallSetupRequestFromSimWithResCode(boolean z, int i, Message message) {
        MtkRadioExSimProxy mtkRadioExSimProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, message);
        if (mtkRadioExSimProxy.isEmpty()) {
            return;
        }
        RILRequest rILRequestObtainRequest = obtainRequest(MtkRILConstants.RIL_REQUEST_STK_HANDLE_CALL_SETUP_REQUESTED_FROM_SIM_WITH_RESULT_CODE, message, this.mRILDefaultWorkSource);
        mtkRiljLog(rILRequestObtainRequest.serialString() + "> " + RILUtils.requestToString(rILRequestObtainRequest.mRequest));
        int[] iArr = new int[1];
        if (i == 33 || i == 32 || i == 50) {
            iArr[0] = i;
        } else {
            iArr[0] = z ? 1 : 0;
        }
        try {
            mtkRadioExSimProxy.handleStkCallSetupRequestFromSimWithResCode(rILRequestObtainRequest.mSerial, iArr[0]);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rILRequestObtainRequest, "handleStkCallSetupRequestFromSimWithResCode", e);
        }
    }

    public void setVendorSetting(int setting, String value, Message result) {
        MtkRadioExModemProxy modemProxy = (MtkRadioExModemProxy) getMtkRadioExServiceProxy(MtkRadioExModemProxy.class, result);
        if (!modemProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_VENDOR_SETTING, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                modemProxy.setVendorSetting(rr.mSerial, setting, value);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(3, rr, "setVendorSetting", e);
            }
        }
    }

    public void restartRILD(Message result) {
        MtkRadioExModemProxy modemProxy = (MtkRadioExModemProxy) getMtkRadioExServiceProxy(MtkRadioExModemProxy.class, result);
        if (!modemProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_RESTART_RILD, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                modemProxy.restartRILD(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(3, rr, "restartRILD", e);
            }
        }
    }

    public void setOnBipProactiveCmd(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mBipProCmdRegistrant.add(r);
    }

    public void unSetOnBipProactiveCmd(Handler h) {
        this.mBipProCmdRegistrant.remove(h);
    }

    public void setOnStkSetupMenuReset(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mStkSetupMenuResetRegistrant.add(r);
    }

    public void unSetOnStkSetupMenuReset(Handler h) {
        this.mStkSetupMenuResetRegistrant.remove(h);
    }

    public void queryNetworkLock(int category, Message result) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, result);
        if (!simProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_QUERY_SIM_NETWORK_LOCK, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                simProxy.queryNetworkLock(rr.mSerial, category);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(0, rr, "queryNetworkLock", e);
            }
        }
    }

    public void setNetworkLock(int category, int lockop, String password, String data_imsi, String gid1, String gid2, Message result) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, result);
        if (!simProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_SIM_NETWORK_LOCK, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                simProxy.setNetworkLock(rr.mSerial, category, lockop, password, data_imsi, gid1, gid2);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(0, rr, "setNetworkLock", e);
            }
        }
    }

    public void supplyDepersonalization(String netpin, int type, Message result) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, result);
        if (!simProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_ENTER_DEPERSONALIZATION, result, this.mRILDefaultWorkSource);
            riljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " netpin = " + netpin + " type = " + type);
            try {
                simProxy.supplyDepersonalization(rr.mSerial, RILUtils.convertNullToEmptyString(netpin), type);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(0, rr, "supplyNetworkDepersonalization", e);
            }
        }
    }

    public void registerForSimPlugIn(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mSimPlugIn.add(r);
    }

    public void unregisterForSimPlugIn(Handler h) {
        this.mSimPlugIn.remove(h);
    }

    public void registerForSimPlugOut(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mSimPlugOut.add(r);
    }

    public void unregisterForSimPlugOut(Handler h) {
        this.mSimPlugOut.remove(h);
    }

    public void registerForSimMissing(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mSimMissing.add(r);
    }

    public void unregisterForSimMissing(Handler h) {
        this.mSimMissing.remove(h);
    }

    public void registerForSimRecovery(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mSimRecovery.add(r);
    }

    public void unregisterForSimRecovery(Handler h) {
        this.mSimRecovery.remove(h);
    }

    public void registerForSimPower(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mSimPowerChanged.add(r);
        if (this.mSimPowerInfo != null) {
            r.notifyRegistrant(new AsyncResult((Object) null, this.mSimPowerInfo, (Throwable) null));
        }
    }

    public void unregisterForSimPower(Handler h) {
        this.mSimPowerChanged.remove(h);
    }

    public void registerForSmlSlotLockInfoChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mSmlSlotLockInfoChanged.add(r);
        if (this.mSmlSlotLockInfo != null) {
            r.notifyRegistrant(new AsyncResult((Object) null, this.mSmlSlotLockInfo, (Throwable) null));
        }
    }

    public void unregisterForSmlSlotLockInfoChanged(Handler h) {
        this.mSmlSlotLockInfoChanged.remove(h);
    }

    public void supplyDeviceNetworkDepersonalization(String pwd, Message result) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, result);
        if (!simProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_ENTER_DEVICE_NETWORK_DEPERSONALIZATION, result, this.mRILDefaultWorkSource);
            riljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                simProxy.supplyDeviceNetworkDepersonalization(rr.mSerial, RILUtils.convertNullToEmptyString(pwd));
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(0, rr, "supplyDeviceNetworkDepersonalization", e);
            }
        }
    }

    public void registerForPhbReady(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        Rlog.d(RILJ_LOG_TAG, "call registerForPhbReady Handler : " + h);
        this.mPhbReadyRegistrants.add(r);
    }

    public void unregisterForPhbReady(Handler h) {
        this.mPhbReadyRegistrants.remove(h);
    }

    public void queryPhbStorageInfo(int type, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_QUERY_PHB_STORAGE_INFO, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + ": " + type);
        try {
            simProxy.queryPhbStorageInfo(rr.mSerial, type);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "queryPhbStorageInfo", e);
        }
    }

    public void writePhbEntry(PhbEntry entry, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_WRITE_PHB_ENTRY, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + ": " + entry);
        try {
            simProxy.writePhbEntry(rr.mSerial, entry);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "writePhbEntry", e);
        }
    }

    public void readPhbEntry(int type, int bIndex, int eIndex, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_READ_PHB_ENTRY, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + ": " + type + " begin: " + bIndex + " end: " + eIndex);
        try {
            simProxy.readPhbEntry(rr.mSerial, type, bIndex, eIndex);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "readPhbEntry", e);
        }
    }

    public void queryUPBCapability(Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_QUERY_UPB_CAPABILITY, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
        try {
            simProxy.queryUPBCapability(rr.mSerial);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "queryUPBCapability", e);
        }
    }

    public void editUPBEntry(int entryType, int adnIndex, int entryIndex, String strVal, String tonForNum, String aasAnrIndex, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_EDIT_UPB_ENTRY, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
        try {
            simProxy.editUPBEntry(rr.mSerial, entryType, adnIndex, entryIndex, strVal, tonForNum, aasAnrIndex);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "editUPBEntry", e);
        }
    }

    public void editUPBEntry(int entryType, int adnIndex, int entryIndex, String strVal, String tonForNum, Message result) {
        editUPBEntry(entryType, adnIndex, entryIndex, strVal, tonForNum, null, result);
    }

    public void deleteUPBEntry(int entryType, int adnIndex, int entryIndex, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_DELETE_UPB_ENTRY, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + ": " + entryType + " adnIndex: " + adnIndex + " entryIndex: " + entryIndex);
        try {
            simProxy.deleteUPBEntry(rr.mSerial, entryType, adnIndex, entryIndex);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "deleteUPBEntry", e);
        }
    }

    public void readUPBGasList(int startIndex, int endIndex, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_READ_UPB_GAS_LIST, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + ":  startIndex: " + startIndex + " endIndex: " + endIndex);
        try {
            simProxy.readUPBGasList(rr.mSerial, startIndex, endIndex);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "readUPBGasList", e);
        }
    }

    public void readUPBGrpEntry(int adnIndex, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_READ_UPB_GRP, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + ":  adnIndex: " + adnIndex);
        try {
            simProxy.readUPBGrpEntry(rr.mSerial, adnIndex);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "readUPBGrpEntry", e);
        }
    }

    public void writeUPBGrpEntry(int adnIndex, int[] grpIds, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_WRITE_UPB_GRP, response, this.mRILDefaultWorkSource);
        int nLen = grpIds.length;
        mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + ":  adnIndex: " + adnIndex + " nLen: " + nLen);
        try {
            simProxy.writeUPBGrpEntry(rr.mSerial, adnIndex, grpIds);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "writeUPBGrpEntry", e);
        }
    }

    public void getPhoneBookStringsLength(Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_PHB_STRING_LENGTH, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> :::" + RILUtils.requestToString(rr.mRequest));
        try {
            simProxy.getPhoneBookStringsLength(rr.mSerial);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "getPhoneBookStringsLength", e);
        }
    }

    public void getPhoneBookMemStorage(Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_PHB_MEM_STORAGE, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> :::" + RILUtils.requestToString(rr.mRequest));
        try {
            simProxy.getPhoneBookMemStorage(rr.mSerial);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "getPhoneBookMemStorage", e);
        }
    }

    public void setPhoneBookMemStorage(String storage, String password, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_PHB_MEM_STORAGE, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> :::" + RILUtils.requestToString(rr.mRequest));
        try {
            simProxy.setPhoneBookMemStorage(rr.mSerial, storage, password);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "setPhoneBookMemStorage", e);
        }
    }

    public void readPhoneBookEntryExt(int index1, int index2, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(2048, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> :::" + RILUtils.requestToString(rr.mRequest));
        try {
            simProxy.readPhoneBookEntryExt(rr.mSerial, index1, index2);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "readPhoneBookEntryExt", e);
        }
    }

    public void writePhoneBookEntryExt(PBEntry entry, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_WRITE_PHB_ENTRY_EXT, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> :::" + RILUtils.requestToString(rr.mRequest));
        try {
            simProxy.writePhoneBookEntryExt(rr.mSerial, entry);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "deleteUPBEntry", e);
        }
    }

    public void queryUPBAvailable(int eftype, int fileIndex, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_QUERY_UPB_AVAILABLE, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " eftype: " + eftype + " fileIndex: " + fileIndex);
        try {
            simProxy.queryUPBAvailable(rr.mSerial, eftype, fileIndex);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "queryUPBAvailable", e);
        }
    }

    public void readUPBEmailEntry(int adnIndex, int fileIndex, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_READ_EMAIL_ENTRY, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " adnIndex: " + adnIndex + " fileIndex: " + fileIndex);
        try {
            simProxy.readUPBEmailEntry(rr.mSerial, adnIndex, fileIndex);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "readUPBEmailEntry", e);
        }
    }

    public void readUPBSneEntry(int adnIndex, int fileIndex, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_READ_SNE_ENTRY, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " adnIndex: " + adnIndex + " fileIndex: " + fileIndex);
        try {
            simProxy.readUPBSneEntry(rr.mSerial, adnIndex, fileIndex);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "readUPBSneEntry", e);
        }
    }

    public void readUPBAnrEntry(int adnIndex, int fileIndex, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_READ_ANR_ENTRY, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " adnIndex: " + adnIndex + " fileIndex: " + fileIndex);
        try {
            simProxy.readUPBAnrEntry(rr.mSerial, adnIndex, fileIndex);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "readUPBAnrEntry", e);
        }
    }

    public void readUPBAasList(int startIndex, int endIndex, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_READ_UPB_AAS_LIST, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " startIndex: " + startIndex + " endIndex: " + endIndex);
        try {
            simProxy.readUPBAasList(rr.mSerial, startIndex, endIndex);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "readUPBAasList", e);
        }
    }

    public void setPhonebookReady(int ready, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_PHONEBOOK_READY, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " ready = " + ready);
        try {
            simProxy.setPhonebookReady(rr.mSerial, ready);
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "setPhonebookReady", e);
        }
    }

    public void getPOLCapability(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_POL_CAPABILITY, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.getPOLCapability(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getPOLCapability", e);
            }
        }
    }

    public void getCurrentPOLList(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_POL_LIST, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.getCurrentPOLList(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getCurrentPOLList", e);
            }
        }
    }

    public void setPOLEntry(int index, String numeric, int nAct, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_POL_ENTRY, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.setPOLEntry(rr.mSerial, index, numeric, nAct);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "setPOLEntry", e);
            }
        }
    }

    public void resetMdDataRetryCount(String apnName, Message result) {
        MtkRadioExDataProxy dataProxy = (MtkRadioExDataProxy) getMtkRadioExServiceProxy(MtkRadioExDataProxy.class, result);
        if (!dataProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_RESET_MD_DATA_RETRY_COUNT, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                dataProxy.resetMdDataRetryCount(rr.mSerial, apnName);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(1, rr, "resetMdDataRetryCount", e);
            }
        }
    }

    public void registerForMdDataRetryCountReset(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mMdDataRetryCountResetRegistrants.add(r);
    }

    public void unregisterForMdDataRetryCountReset(Handler h) {
        this.mMdDataRetryCountResetRegistrants.remove(h);
    }

    public void registerForEconfSrvcc(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mEconfSrvccRegistrants.add(r);
    }

    public void unregisterForEconfSrvcc(Handler h) {
        this.mEconfSrvccRegistrants.remove(h);
    }

    public void setRoamingEnable(int[] config, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_ROAMING_ENABLE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.setRoamingEnable(rr.mSerial, config);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "setRoamingEnable", e);
            }
        }
    }

    public void getRoamingEnable(int phoneId, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_ROAMING_ENABLE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.getRoamingEnable(rr.mSerial, phoneId);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getRoamingEnable", e);
            }
        }
    }

    public void getTOEInfo(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_TOE_INFO, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.getTOEInfo(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getTOEInfo", e);
            }
        }
    }

    public void disableAllCALinks(int linkType, Message result) {
        setCarrierAggregationMode(0, 0, 0, result);
    }

    public void setCarrierAggregationMode(int mode, int option, int linkType, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_CARRIER_AGGREGATION_MODE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.setCarrierAggregationMode(rr.mSerial, mode, option, linkType);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "setCarrierAggregationMode", e);
            }
        }
    }

    public void getCALinkEnableStatus(String bandsCombo, int linkType, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_CA_LINK_ENABLE_STATUS, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.getCALinkEnableStatus(rr.mSerial, bandsCombo, linkType);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getCALinkEnableStatus", e);
            }
        }
    }

    public void setCALinkEnableStatus(boolean status, String bandsCombo, int linkType, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_CA_LINK_ENABLE_STATUS, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.setCALinkEnableStatus(rr.mSerial, status, bandsCombo, linkType);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "setCALinkEnableStatus", e);
            }
        }
    }

    public void getCALinkCapabilityList(int linkType, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_CA_LINK_CAPABILITY_LIST, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.getCALinkCapabilityList(rr.mSerial, linkType);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getCALinkCapabilityList", e);
            }
        }
    }

    public void getLteData(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_LTE_DATA, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.getLteData(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getLteData", e);
            }
        }
    }

    public void getLteRRCState(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_LTE_RRC_STATE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.getLteRRCState(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getLteRRCState", e);
            }
        }
    }

    public void setLteBandEnableStatus(int bandId, boolean status, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_LTE_BAND_ENABLE_STATUS, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.setLteBandEnableStatus(rr.mSerial, bandId, status);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "setLteBandEnableStatus", e);
            }
        }
    }

    public void setBandPriorityList(int[] bandPriList, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_BAND_PRIORITY_LIST, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.setBandPriorityList(rr.mSerial, bandPriList);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "setBandPriorityList", e);
            }
        }
    }

    public void getBandPriorityList(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_BAND_PRIORITY_LIST, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.getBandPriorityList(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getBandPriorityList", e);
            }
        }
    }

    public void setLteReleaseVersion(int mode, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_LTE_RELEASE_VERSION, result, this.mRILDefaultWorkSource);
            riljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " mode = " + mode);
            try {
                networkProxy.setLteReleaseVersion(rr.mSerial, mode);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "setLteReleaseVersion", e);
            }
        }
    }

    public void getLteReleaseVersion(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_LTE_RELEASE_VERSION, result, this.mRILDefaultWorkSource);
            riljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.getLteReleaseVersion(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getLteReleaseVersion", e);
            }
        }
    }

    public void set4x4MimoEnabled(int enabled_bitmask, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_4X4MIMO_ENABLED, result, this.mRILDefaultWorkSource);
            riljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " enabled_bitmask = " + enabled_bitmask);
            try {
                networkProxy.set4x4MimoEnabled(rr.mSerial, enabled_bitmask);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "set4x4MimoEnabled", e);
            }
        }
    }

    public void get4x4MimoEnabled(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_4X4MIMO_ENABLED, result, this.mRILDefaultWorkSource);
            riljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.get4x4MimoEnabled(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "get4x4MimoEnabled", e);
            }
        }
    }

    public void registerForMccMncChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mMccMncRegistrants.add(r);
    }

    public void unregisterForMccMncChanged(Handler h) {
        this.mMccMncRegistrants.remove(h);
    }

    public void registerForVsimIndication(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        mtkRiljLog("registerForVsimIndication called...");
        this.mVsimIndicationRegistrants.add(r);
    }

    public void unregisterForVsimIndication(Handler h) {
        mtkRiljLog("unregisterForVsimIndication called...");
        this.mVsimIndicationRegistrants.remove(h);
    }

    public void registerForNwCfgInfoChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mNwCfgInfoRegistrants.add(r);
        riljLog("registerForNwCfgInfoChanged.");
    }

    public void unregisterForNwCfgInfoChanged(Handler h) {
        this.mNwCfgInfoRegistrants.remove(h);
    }

    public boolean sendVsimNotification(int transactionId, int eventId, int simType, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return MTK_RILJ_LOGV;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_VSIM_NOTIFICATION, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + ", eventId: " + eventId + ", simTpye: " + simType);
        try {
            simProxy.sendVsimNotification(rr.mSerial, transactionId, eventId, simType);
            return MTK_RILJ_LOGD;
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "sendVsimNotification", e);
            return MTK_RILJ_LOGV;
        }
    }

    public boolean sendVsimOperation(int transactionId, int eventId, int message, int dataLength, byte[] data, Message response) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, response);
        if (simProxy.isEmpty()) {
            return MTK_RILJ_LOGV;
        }
        RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_VSIM_OPERATION, response, this.mRILDefaultWorkSource);
        mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
        try {
            simProxy.sendVsimOperation(rr.mSerial, transactionId, eventId, message, dataLength, data);
            return MTK_RILJ_LOGD;
        } catch (RemoteException | RuntimeException e) {
            handleMtkRadioProxyExceptionForRR(5, rr, "sendVsimOperation", e);
            return MTK_RILJ_LOGV;
        }
    }

    public void setServiceStateToModem(int voiceRegState, int dataRegState, int voiceRoamingType, int dataRoamingType, int rilVoiceRegState, int rilDataRegState, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_SERVICE_STATE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " voiceRegState: " + voiceRegState + " dataRegState: " + dataRegState + " voiceRoamingType: " + voiceRoamingType + " dataRoamingType: " + dataRoamingType + " rilVoiceRegState: " + rilVoiceRegState + " rilDataRegState:" + rilDataRegState);
            try {
                networkProxy.setServiceStateToModem(rr.mSerial, voiceRegState, dataRegState, voiceRoamingType, dataRoamingType, rilVoiceRegState, rilDataRegState);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "setServiceStateToModem", e);
            }
        }
    }

    public void dataConnectionAttach(int type, Message result) {
        MtkRadioExDataProxy dataProxy = (MtkRadioExDataProxy) getMtkRadioExServiceProxy(MtkRadioExDataProxy.class, result);
        if (!dataProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_DATA_CONNECTION_ATTACH, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                dataProxy.dataConnectionAttach(rr.mSerial, type);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(1, rr, "dataConnectionAttach", e);
            }
        }
    }

    public void dataConnectionDetach(int type, Message result) {
        MtkRadioExDataProxy dataProxy = (MtkRadioExDataProxy) getMtkRadioExServiceProxy(MtkRadioExDataProxy.class, result);
        if (!dataProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_DATA_CONNECTION_DETACH, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                dataProxy.dataConnectionDetach(rr.mSerial, type);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(1, rr, "dataConnectionDetach", e);
            }
        }
    }

    public void resetAllConnections(Message result) {
        MtkRadioExDataProxy dataProxy = (MtkRadioExDataProxy) getMtkRadioExServiceProxy(MtkRadioExDataProxy.class, result);
        if (!dataProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_RESET_ALL_CONNECTIONS, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                dataProxy.resetAllConnections(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(1, rr, "resetAllConnections", e);
            }
        }
    }

    public void setupDataCallSlice(int accessNetworkType, DataProfile dataProfile, boolean isRoaming, boolean allowRoaming, int reason, LinkProperties linkProperties, int pduSessionId, NetworkSliceInfo sliceInfo, boolean matchAllRuleAllowed, TrafficDescriptor trafficDescriptor, Message result) {
        MtkRadioExDataProxy dataProxy = (MtkRadioExDataProxy) getMtkRadioExServiceProxy(MtkRadioExDataProxy.class, result);
        if (!dataProxy.isEmpty()) {
            RILRequest rr = obtainRequest(27, result, this.mRILDefaultWorkSource);
            riljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + ",reason=" + RILUtils.setupDataReasonToString(reason) + ",accessNetworkType=" + accessNetworkType + ",dataProfile=" + dataProfile + ",isRoaming=" + isRoaming + ",allowRoaming=" + allowRoaming + ",linkProperties=" + linkProperties + ",pduSessionId=" + pduSessionId + ",sliceInfo=" + sliceInfo + ",trafficDescriptor=" + trafficDescriptor + ",matchAllRuleAllowed=" + matchAllRuleAllowed);
            try {
                try {
                    dataProxy.setupDataCallSlice(rr.mSerial, accessNetworkType, dataProfile, isRoaming, allowRoaming, reason, linkProperties, pduSessionId, sliceInfo, matchAllRuleAllowed, trafficDescriptor, 0);
                } catch (RemoteException | RuntimeException e) {
                    e = e;
                    handleMtkRadioProxyExceptionForRR(1, rr, "setupDataCallSlice", e);
                }
            } catch (RemoteException | RuntimeException e2) {
                e = e2;
            }
        }
    }

    public void setOnUnsolOemHookRaw(Handler h, int what, Object obj) {
        this.mUnsolOemHookRegistrant = new Registrant(h, what, obj);
    }

    public void unSetOnUnsolOemHookRaw(Handler h) {
        if (this.mUnsolOemHookRegistrant != null && this.mUnsolOemHookRegistrant.getHandler() == h) {
            this.mUnsolOemHookRegistrant.clear();
            this.mUnsolOemHookRegistrant = null;
        }
    }

    public void invokeOemRilRequestRaw(byte[] data, Message result) {
        MtkRadioExModemProxy modemProxy = (MtkRadioExModemProxy) getMtkRadioExServiceProxy(MtkRadioExModemProxy.class, result);
        if (!modemProxy.isEmpty()) {
            RILRequest rr = obtainRequest(59, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + "[" + IccUtils.bytesToHexString(data) + "]");
            try {
                modemProxy.sendRequestRaw(rr.mSerial, data);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(3, rr, "OemRilRequestRaw", e);
            }
        }
    }

    public void invokeOemRilRequestStrings(String[] strings, Message result) {
        MtkRadioExModemProxy modemProxy = (MtkRadioExModemProxy) getMtkRadioExServiceProxy(MtkRadioExModemProxy.class, result);
        if (!modemProxy.isEmpty()) {
            RILRequest rr = obtainRequest(60, result, this.mRILDefaultWorkSource);
            String logStr = "";
            for (String str : strings) {
                logStr = logStr + str + " ";
            }
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " strings = " + logStr);
            try {
                modemProxy.sendRequestStrings(rr.mSerial, strings);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(3, rr, "OemRilRequestString", e);
            }
        }
    }

    public void setDisable2G(boolean mode, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_DISABLE_2G, result, this.mRILDefaultWorkSource);
            riljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)));
            try {
                networkProxy.setDisable2G(rr.mSerial, mode);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "setDisable2G", e);
            }
        }
    }

    public void getDisable2G(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_DISABLE_2G, result, this.mRILDefaultWorkSource);
            riljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)));
            try {
                networkProxy.getDisable2G(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getDisable2G", e);
            }
        }
    }

    public void registerForTxPower(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mTxPowerRegistrant.add(r);
    }

    public void unregisterForTxPower(Handler h) {
        this.mTxPowerRegistrant.remove(h);
    }

    public void registerForTxPowerStatus(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mTxPowerStatusRegistrant.add(r);
    }

    public void unregisterForTxPowerStatus(Handler h) {
        this.mTxPowerStatusRegistrant.remove(h);
    }

    public void setTxPowerStatus(int enable, Message result) {
        MtkRadioExModemProxy modemProxy = (MtkRadioExModemProxy) getMtkRadioExServiceProxy(MtkRadioExModemProxy.class, result);
        if (!modemProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_TX_POWER_STATUS, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                modemProxy.setTxPowerStatus(rr.mSerial, enable);
                Message msg = this.mRilHandler.obtainMessage(5, Integer.valueOf(rr.mSerial));
                this.mRilHandler.sendMessageDelayed(msg, 2000L);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(3, rr, "setTxPowerStatus", e);
            }
        }
    }

    public void setSuppServProperty(String name, String value, Message result) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (voiceProxy != null && !voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_SS_PROPERTY, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)) + " name=" + name + ", value=" + value + ", result=" + result);
            try {
                voiceProxy.setSuppServProperty(rr.mSerial, name, value);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "setSuppServProperty", e);
            }
        }
    }

    public void registerForDsbpStateChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mDsbpStateRegistrant.add(r);
    }

    public void unregisterForDsbpStateChanged(Handler h) {
        this.mDsbpStateRegistrant.remove(h);
    }

    public void setGwsdMode(int mode, String kaMode, String kaCycle, Message result) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (voiceProxy != null && !voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_GWSD_MODE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            ArrayList<String> arrList = new ArrayList<>();
            arrList.add(Integer.toString(mode));
            arrList.add(kaMode);
            arrList.add(kaCycle);
            try {
                voiceProxy.setGwsdMode(rr.mSerial, arrList);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "setGwsdMode", e);
            }
        }
    }

    public void setCallValidTimer(int timer, Message result) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (voiceProxy != null && !voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_GWSD_CALL_VALID, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " timer=" + timer);
            try {
                voiceProxy.setCallValidTimer(rr.mSerial, timer);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "setCallValidTimer", e);
            }
        }
    }

    public void setIgnoreSameNumberInterval(int interval, Message result) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (voiceProxy != null && !voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_GWSD_IGNORE_CALL_INTERVAL, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " interval=" + interval);
            try {
                voiceProxy.setIgnoreSameNumberInterval(rr.mSerial, interval);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "setIgnoreSameNumberInterval", e);
            }
        }
    }

    public void setKeepAliveByPDCPCtrlPDU(String data, Message result) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (voiceProxy != null && !voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_GWSD_KEEP_ALIVE_PDCP, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " data=" + data);
            try {
                voiceProxy.setKeepAliveByPDCPCtrlPDU(rr.mSerial, data);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "setKeepAliveByPDCPCtrlPDU", e);
            }
        }
    }

    public void setKeepAliveByIpData(String data, Message result) {
        MtkRadioExVoiceProxy voiceProxy = (MtkRadioExVoiceProxy) getMtkRadioExServiceProxy(MtkRadioExVoiceProxy.class, result);
        if (voiceProxy != null && !voiceProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_GWSD_KEEP_ALIVE_IPDATA, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " data=" + data);
            try {
                voiceProxy.setKeepAliveByIpData(rr.mSerial, data);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(6, rr, "setKeepAliveByIpData", e);
            }
        }
    }

    public void enableDsdaIndication(boolean enable, Message result) {
        MtkRadioExDataProxy dataProxy = (MtkRadioExDataProxy) getMtkRadioExServiceProxy(MtkRadioExDataProxy.class, result);
        if (!dataProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_ENABLE_DSDA_INDICATION, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                dataProxy.enableDsdaIndication(rr.mSerial, enable);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(1, rr, "enableDsdaIndication", e);
            }
        }
    }

    public void getDsdaStatus(Message result) {
        MtkRadioExDataProxy dataProxy = (MtkRadioExDataProxy) getMtkRadioExServiceProxy(MtkRadioExDataProxy.class, result);
        if (!dataProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_DSDA_STATUS, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                dataProxy.getDsdaStatus(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(1, rr, "getDsdaStatus", e);
            }
        }
    }

    public void registerForDsdaStateChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mDsdaStateRegistrant.add(r);
    }

    public void unregisterForDsdaStateChanged(Handler h) {
        this.mDsdaStateRegistrant.remove(h);
    }

    public void registerForQualifiedNetworkTypesChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        synchronized (this.mLatestQualifiedNetworkTypes) {
            this.mQualifiedNetworkTypesRegistrant.add(r);
            for (int i = 0; i < this.mLatestQualifiedNetworkTypes.size(); i++) {
                r.notifyRegistrant(new AsyncResult((Object) null, this.mLatestQualifiedNetworkTypes.valueAt(i), (Throwable) null));
            }
        }
    }

    public void unregisterForQualifiedNetworkTypesChanged(Handler h) {
        synchronized (this.mLatestQualifiedNetworkTypes) {
            this.mQualifiedNetworkTypesRegistrant.remove(h);
        }
    }

    public void iwlanSetRegisterCellularQualityReport(int qualityRegister, int type, int[] values, Message result) {
        int modemSignalType;
        MtkRadioExModemProxy modemProxy = (MtkRadioExModemProxy) getMtkRadioExServiceProxy(MtkRadioExModemProxy.class, result);
        if (!modemProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_IWLAN_REGISTER_CELLULAR_QUALITY_REPORT, result, this.mRILDefaultWorkSource);
            riljLog(rr.serialString() + ">  " + RILUtils.requestToString(rr.mRequest) + " enable = " + qualityRegister + " type = " + type);
            String str1 = qualityRegister == 1 ? RadioCapabilitySwitchUtil.IMSI_READY : "0";
            switch (type) {
                case 0:
                    modemSignalType = 0;
                    break;
                case 1:
                case 3:
                case 4:
                default:
                    riljLoge("iwlanSetRegisterCellularQualityReport(): type not support");
                    modemSignalType = -1;
                    break;
                case 2:
                    modemSignalType = 2;
                    break;
                case 5:
                    modemSignalType = 1;
                    break;
                case 6:
                    modemSignalType = 3;
                    break;
                case 7:
                    modemSignalType = 4;
                    break;
            }
            if (modemSignalType == -1) {
                riljLog(rr.serialString() + "< MTK : RIL_REQUEST_IWLAN_REGISTER_CELLULAR_QUALITY_REPORT Fail - Type is not supported");
                if (result != null) {
                    AsyncResult.forMessage(result, (Object) null, CommandException.fromRilErrno(6));
                    result.sendToTarget();
                    return;
                }
                return;
            }
            String str2 = String.valueOf(modemSignalType);
            int[] arraySorting = Arrays.copyOf(values, values.length);
            Arrays.sort(arraySorting);
            StringBuilder sb = new StringBuilder();
            for (int i = values.length - 1; i >= 0; i--) {
                sb.append(String.valueOf(arraySorting[i]));
                if (i > 0) {
                    sb.append(",");
                }
            }
            try {
                int i2 = rr.mSerial;
                modemProxy.registerCellQltyReport(i2, str1, str2, sb.toString(), "500");
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(3, rr, "registerCellQltyReport", e);
            }
        }
    }

    public void getSuggestedPlmnList(int rat, int num, int timer, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_SUGGESTED_PLMN_LIST, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + ", rat=" + rat + ", num=" + num + ", timer=" + timer);
            try {
                networkProxy.getSuggestedPlmnList(rr.mSerial, rat, num, timer);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getSuggestedPlmnList", e);
            }
        }
    }

    public void sendWifiEnabled(String ifName, int isWifiEnabled, Message result) {
        MtkRadioExModemProxy modemProxy = (MtkRadioExModemProxy) getMtkRadioExServiceProxy(MtkRadioExModemProxy.class, result);
        if (!modemProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SEND_WIFI_ENABLED, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " ifName:" + ifName + " isWifiEnabled:" + isWifiEnabled);
            try {
                modemProxy.sendWifiEnabled(rr.mSerial, ifName, isWifiEnabled);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(3, rr, "sendWifiEnabled", e);
            }
        }
    }

    public void sendWifiAssociated(String ifName, boolean associated, String ssid, String apMac, int mtuSize, String ueMac, Message result) {
        MtkRadioExModemProxy modemProxy = (MtkRadioExModemProxy) getMtkRadioExServiceProxy(MtkRadioExModemProxy.class, result);
        if (!modemProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SEND_WIFI_ASSOCIATED, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " ifName:" + ifName + " associated:" + associated + ", mtu: " + mtuSize);
            try {
                modemProxy.sendWifiAssociated(rr.mSerial, ifName, associated, ssid, apMac, mtuSize, ueMac);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(3, rr, "sendWifiAssociated", e);
            }
        }
    }

    public void sendWifiIpAddress(String ifName, String ipv4Addr, String ipv6Addr, int ipv4PrefixLen, int ipv6PrefixLen, String ipv4Gateway, String ipv6Gateway, int dnsCount, String dnsAddresses, Message result) {
        MtkRadioExModemProxy modemProxy = (MtkRadioExModemProxy) getMtkRadioExServiceProxy(MtkRadioExModemProxy.class, result);
        if (!modemProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SEND_WIFI_IP_ADDRESS, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest) + " ifName:" + ifName + " ipv4PrefixLen: " + ipv4PrefixLen + " ipv6PrefixLen: " + ipv6PrefixLen + " dnsCount: " + dnsCount);
            try {
                modemProxy.sendWifiIpAddress(rr.mSerial, ifName, ipv4Addr, ipv6Addr, ipv4PrefixLen, ipv6PrefixLen, ipv4Gateway, ipv6Gateway, dnsCount, dnsAddresses);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(3, rr, "sendWifiIpAddress", e);
            }
        }
    }

    public void registerForMobileDataUsage(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mMobileDataUsageRegistrants.add(r);
    }

    public void unregisterForMobileDataUsage(Handler h) {
        this.mMobileDataUsageRegistrants.remove(h);
    }

    public void registerForNwLimitState(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mNwLimitRegistrants.add(r);
    }

    public void unregisterForNwLimitState(Handler h) {
        this.mNwLimitRegistrants.remove(h);
    }

    public void sendSarIndicator(int sarCmdType, String sarParameter, Message result) {
        MtkRadioExModemProxy modemProxy = (MtkRadioExModemProxy) getMtkRadioExServiceProxy(MtkRadioExModemProxy.class, result);
        if (!modemProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SEND_SAR_INDICATOR, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)) + " sarCmdType=" + sarCmdType + " sarParameter=" + sarParameter);
            try {
                modemProxy.sendSarIndicator(rr.mSerial, sarCmdType, sarParameter);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(3, rr, "sendSarIndicator", e);
            }
        }
    }

    public void cacheSimTypeInfo(SimTypeInfoExt simTypeInfoExt) {
        this.mSimTypeInfo = simTypeInfoExt;
    }

    public void registerForSimTypeChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mSimTypeRegistrants.add(r);
        if (this.mSimTypeInfo != null) {
            mtkRiljLog("registerForSimTypeChanged, notify mSimTypeInfo");
            r.notifyRegistrant(new AsyncResult((Object) null, this.mSimTypeInfo, (Throwable) null));
        }
    }

    public void unregisterForSimTypeChanged(Handler h) {
        this.mSimTypeRegistrants.remove(h);
    }

    public void cacheIccid(String iccid) {
        this.mIccid = iccid;
    }

    public void registerForIccidChanged(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mIccidRegistrants.add(r);
        if (this.mIccid != null) {
            r.notifyRegistrant(new AsyncResult((Object) null, this.mIccid, (Throwable) null));
        }
    }

    public void unregisterForIccidChanged(Handler h) {
        this.mIccidRegistrants.remove(h);
    }

    public void registerForPlmnData(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mPlmnDataRegistrants.add(r);
        if (this.mPlmnMvnoData != null) {
            r.notifyRegistrant(new AsyncResult((Object) null, this.mPlmnMvnoData, (Throwable) null));
        }
    }

    public void unregisterForPlmnData(Handler h) {
        this.mPlmnDataRegistrants.remove(h);
    }

    public void notifyPlmnMvnoData(PlmnMvnoData response) {
        this.mPlmnMvnoData = response;
        if (this.mPlmnDataRegistrants != null) {
            this.mPlmnDataRegistrants.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    public void sendRsuRequest(RsuRequestData rri, Message result) {
        MtkRadioExSimProxy simProxy = (MtkRadioExSimProxy) getMtkRadioExServiceProxy(MtkRadioExSimProxy.class, result);
        if (!simProxy.isEmpty() && rri != null) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SML_RSU_REQUEST, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> RIL_REQUEST_SML_RSU_REQUEST opId = " + rri.opId + " requestId = " + rri.requestId);
            try {
                simProxy.sendRsuRequest(rr.mSerial, rri);
                return;
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(0, rr, "sendRsuRequest", e);
                return;
            }
        }
        mtkRiljLog("sendRsuRequest: Invalid RsuRequestData!");
    }

    public void setLteBsrTimer(int timer, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_LTE_BSR_TIMER, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)) + ", timer = " + timer);
            try {
                networkProxy.setLteBsrTimer(rr.mSerial, timer);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "setLteBsrTimer", e);
            }
        }
    }

    public void getLteBsrTimer(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_LTE_BSR_TIMER, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)));
            try {
                networkProxy.getLteBsrTimer(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getLteBsrTimer", e);
            }
        }
    }

    public void getLte1xRttCellList(boolean available, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_LTE_1XRTT_CELL_LIST, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)));
            try {
                networkProxy.getLte1xRttCellList(rr.mSerial, available);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getLte1xRttCellList", e);
            }
        }
    }

    public void clearLteAvailableFile(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_CLEAR_LTE_AVAILABLE_FILE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)));
            try {
                networkProxy.clearLteAvailableFile(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "clearLteAvailableFile", e);
            }
        }
    }

    public void getBandMode(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_BAND_MODE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)));
            try {
                networkProxy.getBandMode(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getBandMode", e);
            }
        }
    }

    public void getCaBandMode(int primaryBandId, Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_CA_BAND_MODE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)));
            try {
                networkProxy.getCaBandMode(rr.mSerial, primaryBandId);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getCaBandMode", e);
            }
        }
    }

    public void getCampedFemtoCellInfo(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_CAMPED_FEMTO_CELL_INFO, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)));
            try {
                networkProxy.getCampedFemtoCellInfo(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getCampedFemtoCellInfo", e);
            }
        }
    }

    public void getEcholocateMetrics(int index, Message result) {
        MtkRadioExModemProxy modemProxy = (MtkRadioExModemProxy) getMtkRadioExServiceProxy(MtkRadioExModemProxy.class, result);
        if (!modemProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_ECHOLOCATE_METRICS, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> RIL_REQUEST_GET_ECHOLOCATE_METRICS index: " + index);
            try {
                modemProxy.getEngineeringModeInfo(rr.mSerial, index);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(3, rr, "getEcholocateMetrics", e);
            }
        }
    }

    public void getIWlanRegistrationState(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_IWLAN_REGISTRATION_STATE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + RILUtils.requestToString(rr.mRequest));
            try {
                networkProxy.getIWlanRegistrationState(rr.mSerial);
            } catch (Exception e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getIWlanRegistrationState", e);
            }
        }
    }

    public void registerForIWlanRegistrationStateChanged(Handler h, int what, Object obj) {
        this.mIWlanStateRegistrants.addUnique(h, what, obj);
    }

    public void unregisterForIWlanRegistrationStateChanged(Handler h) {
        this.mIWlanStateRegistrants.remove(h);
    }

    public void getAllBandMode(Message result) {
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_GET_ALL_BAND_MODE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)));
            try {
                networkProxy.getAllBandMode(rr.mSerial);
            } catch (RemoteException | RuntimeException e) {
                handleMtkRadioProxyExceptionForRR(4, rr, "getAllBandMode", e);
            }
        }
    }

    public void setNrBandMode(ArrayList<Integer> saEnableBands, ArrayList<Integer> saDisableBands, ArrayList<Integer> nsaEnableBands, ArrayList<Integer> nsaDisableBands, Message result) {
        Exception e;
        MtkRadioExNetworkProxy networkProxy = (MtkRadioExNetworkProxy) getMtkRadioExServiceProxy(MtkRadioExNetworkProxy.class, result);
        if (!networkProxy.isEmpty()) {
            RILRequest rr = obtainRequest(MtkRILConstants.RIL_REQUEST_SET_NR_BAND_MODE, result, this.mRILDefaultWorkSource);
            mtkRiljLog(rr.serialString() + "> " + requestToStringEx(Integer.valueOf(rr.mRequest)));
            try {
                try {
                    networkProxy.setNrBandMode(rr.mSerial, saEnableBands, saDisableBands, nsaEnableBands, nsaDisableBands);
                } catch (Exception e2) {
                    e = e2;
                    handleMtkRadioProxyExceptionForRR(4, rr, "setNrBandMode", e);
                }
            } catch (Exception e3) {
                e = e3;
            }
        }
    }

    public RegistrantList getDisplayNetworkTypeChangedRegistrants() {
        return this.mDisplayNetworkTypeChangedRegistrants;
    }

    public void registerFor5gUWInfo(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.m5gUWInfoRegistrants.add(r);
    }

    public void unregisterFor5gUWInfo(Handler h) {
        this.m5gUWInfoRegistrants.remove(h);
    }

    public void registerForNrSysInfoChanged(Handler h, int what, Object obj) {
        this.mNrSysInfoRegistrants.addUnique(h, what, obj);
    }

    public void unregisterForNrSysInfoChanged(Handler h) {
        this.mNrSysInfoRegistrants.remove(h);
    }

    public void processIndicationMtk(int service, int indicationType) {
        processIndication(service, indicationType);
    }
}
