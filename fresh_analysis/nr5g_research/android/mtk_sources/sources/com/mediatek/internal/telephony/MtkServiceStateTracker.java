package com.mediatek.internal.telephony;

import android.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.radio.V1_5.RegStateResult;
import android.hardware.radio.network.AccessTechnologySpecificInfo;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.telephony.AccessNetworkUtils;
import android.telephony.CarrierConfigManager;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityTdscdma;
import android.telephony.CellIdentityWcdma;
import android.telephony.LteVopsSupportInfo;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PhysicalChannelConfig;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.SparseArray;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.MiuiNetworkControllerStub;
import com.android.internal.telephony.NetworkRegistrationManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.android.internal.telephony.cdnr.CarrierDisplayNameData;
import com.android.internal.telephony.metrics.RadioPowerStateStats;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.telephony.Rlog;
import com.mediatek.internal.telephony.data.MtkDataNetworkController;
import com.mediatek.internal.telephony.uicc.MtkSpnOverride;
import com.mediatek.provider.MtkContactsContract;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MtkServiceStateTracker extends ServiceStateTracker {
    private static final int CN_OP_ID_CBN = 4;
    private static final int CN_OP_ID_CMCC = 1;
    private static final int CN_OP_ID_CT = 3;
    private static final int CN_OP_ID_CU = 2;
    private static final int CN_OP_ID_UNKNOWN = 0;
    private static final boolean DBG = true;
    protected static final int EVENT_FEMTO_CELL_INFO = 102;
    protected static final int EVENT_GET_SIGNAL_STRENGTH = 124;
    protected static final int EVENT_IWLAN_PREFERRED_CHANGED = 125;
    protected static final int EVENT_MODULATION_INFO = 105;
    protected static final int EVENT_NETWORK_EVENT = 104;
    protected static final int EVENT_PS_NETWORK_STATE_CHANGED = 103;
    protected static final int EVENT_RECHECK_NRM_STATUS = 123;
    protected static final int EVENT_RESTART_TURBO = 122;
    protected static final int EVENT_RIL_READY = 120;
    protected static final int EVENT_SIM_OPL_LOADED = 119;
    protected static final int EVENT_UPDATE_PLMN = 121;
    private static final String LOG_TAG = "MTKSST";
    protected static final String PROP_IWLAN_STATE = "persist.vendor.radio.wfc_state";
    private static final boolean VDBG = true;
    private static final long mCheckNrmPeriod = 1000;
    private static String[][] sSpnOverride = {new String[]{"45406", "SmarTone HK"}, new String[]{"46605", "GT 4G"}, new String[]{"52505", "StarHub"}};
    private boolean hasPendingPollState;
    private SparseArray<int[]> mCnShareNwConfig;
    private String mCsgId;
    private RegistrantList mDataRoamingTypeChangedRegistrants;
    private boolean mEnableERI;
    private String mFemtoAct;
    private String mFemtoPlmn;
    private int mFemtocellDomain;
    private String mHhbName;
    private int mIsFemtocell;
    private boolean mIsTurboSSAlive;
    private String mLocatedPlmn;
    private BroadcastReceiver mMtkIntentReceiver;
    private ServiceState mOldSS;
    private IServiceStateTrackerExt mServiceStateTrackerExt;
    private OpTelephonyCustomizationFactoryBase mTelephonyCustomizationFactory;
    private Handler mtkHandler;
    private HandlerThread mtkHandlerThread;
    protected boolean once_pollState_done;
    private ServiceState turboSS;
    private SignalStrength turboSig;

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTurboPLMN() {
        updatePLMN(false, null, false, null);
    }

    private void updatePLMN(boolean showPlmn, String plmn, boolean showSpn, String spn) {
        CarrierDisplayNameData mtkCdnd = new CarrierDisplayNameData.Builder().setSpn(spn).setDataSpn(spn).setShowSpn(showSpn).setPlmn(plmn).setShowPlmn(showPlmn).build();
        this.mtkHandler.sendMessage(obtainMessage(EVENT_UPDATE_PLMN, mtkCdnd));
    }

    private class MtkHandler extends Handler {
        private boolean pending;
        private boolean polling;
        private boolean stop;

        public MtkHandler(Looper looper) {
            super(looper);
            this.stop = false;
            this.polling = false;
            this.pending = false;
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int regState;
            int reasonForDenial;
            int networkType;
            int reasonForDenial2;
            boolean isEndcAvailable;
            boolean isNrAvailable;
            boolean isDcNrRestricted;
            int regState2;
            int networkType2;
            switch (msg.what) {
                case 2:
                case MtkServiceStateTracker.EVENT_RESTART_TURBO /* 122 */:
                    break;
                case 4:
                    AsyncResult ar = (AsyncResult) msg.obj;
                    Object result = ar.result;
                    if (ar.exception != null || result == null || (!(result instanceof RegStateResult) && !(result instanceof android.hardware.radio.V1_6.RegStateResult) && !(result instanceof android.hardware.radio.network.RegStateResult))) {
                        MtkServiceStateTracker.this.log("Turbo stop due to wrong object");
                        this.polling = false;
                        return;
                    }
                    int networkType3 = 0;
                    int regState3 = 1;
                    int reasonForDenial3 = 0;
                    if (result instanceof RegStateResult) {
                        RegStateResult voiceRegState = (RegStateResult) result;
                        networkType3 = ServiceState.rilRadioTechnologyToNetworkType(voiceRegState.rat);
                        int regState4 = voiceRegState.regState;
                        int reasonForDenial4 = voiceRegState.reasonForDenial;
                        regState = regState4;
                        reasonForDenial = reasonForDenial4;
                    } else {
                        if (result instanceof android.hardware.radio.V1_6.RegStateResult) {
                            android.hardware.radio.V1_6.RegStateResult voiceRegState2 = (android.hardware.radio.V1_6.RegStateResult) result;
                            networkType3 = ServiceState.rilRadioTechnologyToNetworkType(voiceRegState2.rat);
                            regState3 = voiceRegState2.regState;
                            reasonForDenial3 = voiceRegState2.reasonForDenial;
                        } else if (result instanceof android.hardware.radio.network.RegStateResult) {
                            android.hardware.radio.network.RegStateResult voiceRegState3 = (android.hardware.radio.network.RegStateResult) result;
                            networkType3 = ServiceState.rilRadioTechnologyToNetworkType(voiceRegState3.rat);
                            regState = voiceRegState3.regState >= 10 ? voiceRegState3.regState - 10 : voiceRegState3.regState;
                            int reasonForDenial5 = voiceRegState3.reasonForDenial;
                            reasonForDenial = reasonForDenial5;
                        }
                        regState = regState3;
                        reasonForDenial = reasonForDenial3;
                    }
                    if (networkType3 != 19) {
                        networkType = networkType3;
                    } else {
                        networkType = 13;
                    }
                    NetworkRegistrationInfo networkRegState = new NetworkRegistrationInfo(1, 1, regState, networkType, reasonForDenial, false, null, null, "", false, 0, 0, 0);
                    int registrationState = networkRegState.getNetworkRegistrationState();
                    MtkServiceStateTracker.this.turboSS.setVoiceRegState(MtkServiceStateTracker.this.regCodeToServiceState(registrationState));
                    MtkServiceStateTracker.this.turboSS.addNetworkRegistrationInfo(networkRegState);
                    if ((MtkServiceStateTracker.this.turboSS.getVoiceRegState() == 0 && !MtkServiceStateTracker.this.turboSS.getVoiceRoaming()) || (MtkServiceStateTracker.this.turboSS.getDataRegState() == 0 && !MtkServiceStateTracker.this.turboSS.getDataRoaming())) {
                        if (SubscriptionManager.isValidSubscriptionId(MtkServiceStateTracker.this.mPhone.getSubId())) {
                            MtkServiceStateTracker.this.updateTurboPLMN();
                        }
                        MtkServiceStateTracker.this.log("MtkHandler: stop MTK turbo.");
                        this.stop = true;
                        MtkServiceStateTracker.this.mCi.unregisterForNetworkStateChanged(MtkServiceStateTracker.this.mtkHandler);
                        if (MtkServiceStateTracker.this.turboSS.getDataRegState() == 0) {
                            this.pending = false;
                        }
                    }
                    this.polling = false;
                    if (this.pending) {
                        this.pending = false;
                        MtkServiceStateTracker.this.mtkHandler.sendEmptyMessage(MtkServiceStateTracker.EVENT_RESTART_TURBO);
                        return;
                    }
                    return;
                case 5:
                    AsyncResult ar2 = (AsyncResult) msg.obj;
                    Object result2 = ar2.result;
                    if (ar2.exception != null || result2 == null || (!(result2 instanceof RegStateResult) && !(result2 instanceof android.hardware.radio.V1_6.RegStateResult) && !(result2 instanceof android.hardware.radio.network.RegStateResult))) {
                        MtkServiceStateTracker.this.log("Turbo stop due to wrong object");
                        return;
                    }
                    int networkType4 = 0;
                    int regState5 = 1;
                    int reasonForDenial6 = 0;
                    boolean isEndcAvailable2 = false;
                    boolean isNrAvailable2 = false;
                    boolean isDcNrRestricted2 = false;
                    if (result2 instanceof RegStateResult) {
                        RegStateResult dataRegState = (RegStateResult) result2;
                        networkType4 = ServiceState.rilRadioTechnologyToNetworkType(dataRegState.rat);
                        int regState6 = dataRegState.regState;
                        int reasonForDenial7 = dataRegState.reasonForDenial;
                        if (dataRegState.accessTechnologySpecificInfo.getDiscriminator() == 2) {
                            RegStateResult.AccessTechnologySpecificInfo.EutranRegistrationInfo eutranInfo = dataRegState.accessTechnologySpecificInfo.eutranInfo();
                            isDcNrRestricted2 = eutranInfo.nrIndicators.isDcNrRestricted;
                            isNrAvailable2 = eutranInfo.nrIndicators.isNrAvailable;
                            isEndcAvailable2 = eutranInfo.nrIndicators.isEndcAvailable;
                        }
                        reasonForDenial2 = reasonForDenial7;
                        isEndcAvailable = isEndcAvailable2;
                        isNrAvailable = isNrAvailable2;
                        isDcNrRestricted = isDcNrRestricted2;
                        regState2 = regState6;
                    } else {
                        if (result2 instanceof android.hardware.radio.V1_6.RegStateResult) {
                            android.hardware.radio.V1_6.RegStateResult dataRegState2 = (android.hardware.radio.V1_6.RegStateResult) result2;
                            networkType4 = ServiceState.rilRadioTechnologyToNetworkType(dataRegState2.rat);
                            regState5 = dataRegState2.regState;
                            reasonForDenial6 = dataRegState2.reasonForDenial;
                            if (dataRegState2.accessTechnologySpecificInfo.getDiscriminator() == 2) {
                                RegStateResult.AccessTechnologySpecificInfo.EutranRegistrationInfo eutranInfo2 = dataRegState2.accessTechnologySpecificInfo.eutranInfo();
                                isDcNrRestricted2 = eutranInfo2.nrIndicators.isDcNrRestricted;
                                isNrAvailable2 = eutranInfo2.nrIndicators.isNrAvailable;
                                isEndcAvailable2 = eutranInfo2.nrIndicators.isEndcAvailable;
                            }
                        } else if (result2 instanceof android.hardware.radio.network.RegStateResult) {
                            android.hardware.radio.network.RegStateResult dataRegState3 = (android.hardware.radio.network.RegStateResult) result2;
                            networkType4 = ServiceState.rilRadioTechnologyToNetworkType(dataRegState3.rat);
                            int regState7 = dataRegState3.regState >= 10 ? dataRegState3.regState - 10 : dataRegState3.regState;
                            int reasonForDenial8 = dataRegState3.reasonForDenial;
                            AccessTechnologySpecificInfo info = dataRegState3.accessTechnologySpecificInfo;
                            if (info.getTag() != 2) {
                                reasonForDenial2 = reasonForDenial8;
                                isEndcAvailable = false;
                                isNrAvailable = false;
                                isDcNrRestricted = false;
                                regState2 = regState7;
                            } else {
                                boolean isDcNrRestricted3 = info.getEutranInfo().nrIndicators.isDcNrRestricted;
                                boolean isNrAvailable3 = info.getEutranInfo().nrIndicators.isNrAvailable;
                                reasonForDenial2 = reasonForDenial8;
                                isEndcAvailable = info.getEutranInfo().nrIndicators.isEndcAvailable;
                                isNrAvailable = isNrAvailable3;
                                isDcNrRestricted = isDcNrRestricted3;
                                regState2 = regState7;
                            }
                        }
                        reasonForDenial2 = reasonForDenial6;
                        isEndcAvailable = isEndcAvailable2;
                        isNrAvailable = isNrAvailable2;
                        isDcNrRestricted = isDcNrRestricted2;
                        regState2 = regState5;
                    }
                    if (networkType4 != 19) {
                        networkType2 = networkType4;
                    } else {
                        networkType2 = 13;
                    }
                    List<Integer> availableServices = new ArrayList<>();
                    if (regState2 == 1) {
                        availableServices.add(2);
                    }
                    NetworkRegistrationInfo networkRegState2 = new NetworkRegistrationInfo(2, 1, regState2, networkType2, reasonForDenial2, false, availableServices, null, "", 16, isDcNrRestricted, isNrAvailable, isEndcAvailable, new LteVopsSupportInfo(1, 1));
                    int registrationState2 = networkRegState2.getNetworkRegistrationState();
                    int serviceState = MtkServiceStateTracker.this.regCodeToServiceState(registrationState2);
                    MtkServiceStateTracker.this.turboSS.setDataRegState(serviceState);
                    MtkServiceStateTracker.this.turboSS.addNetworkRegistrationInfo(networkRegState2);
                    return;
                case 7:
                    AsyncResult ar3 = (AsyncResult) msg.obj;
                    if (!(ar3.result instanceof String[])) {
                        MtkServiceStateTracker.this.log("Turbo EVENT OPERATOR wrong object");
                        return;
                    }
                    String[] opNames = (String[]) ar3.result;
                    if (opNames != null && opNames.length >= 3) {
                        MtkServiceStateTracker.this.turboSS.setOperatorName(opNames[0], opNames[1], opNames[2]);
                        return;
                    }
                    return;
                case MtkServiceStateTracker.EVENT_RIL_READY /* 120 */:
                    MtkServiceStateTracker.this.mCi.registerForNetworkStateChanged(MtkServiceStateTracker.this.mtkHandler, 2, (Object) null);
                    break;
                case MtkServiceStateTracker.EVENT_UPDATE_PLMN /* 121 */:
                    CarrierDisplayNameData mtkCdnd = (CarrierDisplayNameData) msg.obj;
                    int subId = MtkServiceStateTracker.this.mPhone.getSubId();
                    boolean connected = ((NetworkRegistrationManager) MtkServiceStateTracker.this.mRegStateManagers.get(1)).isServiceConnected();
                    if (connected) {
                        MtkServiceStateTracker mtkServiceStateTracker = MtkServiceStateTracker.this;
                        if (mtkCdnd == null) {
                            mtkServiceStateTracker.log("EVENT_UPDATE_PLMN should not be happened here");
                            return;
                        }
                        if (!mtkServiceStateTracker.isTurboSSAlive() || mtkCdnd.shouldShowPlmn() || mtkCdnd.shouldShowSpn()) {
                            MtkServiceStateTracker.this.log("MtkHandler: EVENT_UPDATE_PLMN " + mtkCdnd);
                            if (SubscriptionManager.isValidSubscriptionId(subId)) {
                                MtkServiceStateTracker.this.mSubscriptionManagerService.setCarrierName(subId, TextUtils.emptyIfNull(MtkServiceStateTracker.this.getCarrierName(mtkCdnd)));
                                return;
                            }
                            return;
                        }
                        MtkServiceStateTracker.this.log("updateTurboPLMN comes after connected, skip");
                        return;
                    }
                    if (SubscriptionManager.isValidSubscriptionId(subId)) {
                        if ((MtkServiceStateTracker.this.turboSS.getVoiceRegState() == 0 && !MtkServiceStateTracker.this.turboSS.getVoiceRoaming()) || (MtkServiceStateTracker.this.turboSS.getDataRegState() == 0 && !MtkServiceStateTracker.this.turboSS.getDataRoaming())) {
                            int display_type = MtkServiceStateTracker.this.turboSS.getDataNetworkType();
                            int displayNetworkType = 0;
                            if ((display_type == 13 || display_type == 19) && MtkServiceStateTracker.this.turboSS.getNrState() != 0 && MtkServiceStateTracker.this.turboSS.getNrState() != 1) {
                                displayNetworkType = 3;
                            }
                            TelephonyDisplayInfo displayInfo = new TelephonyDisplayInfo(display_type, displayNetworkType);
                            MtkServiceStateTracker.this.log("updateTurboPLMN turboSS=" + MtkServiceStateTracker.this.turboSS + " displayInfo=" + displayInfo);
                            MtkServiceStateTracker.this.mSubscriptionManagerService.setCarrierName(subId, TextUtils.emptyIfNull(MtkServiceStateTracker.this.turboSS.getOperatorAlpha()));
                            ((MtkGsmCdmaPhone) MtkServiceStateTracker.this.mPhone).notifyMtkServiceStateChanged(MtkServiceStateTracker.this.turboSS);
                            ((MtkGsmCdmaPhone) MtkServiceStateTracker.this.mPhone).notifyDisplayInfoChanged(displayInfo);
                            return;
                        }
                        if (mtkCdnd.getPlmn() != null || mtkCdnd.getSpn() != null) {
                            MtkServiceStateTracker.this.log("update aosp PLMN mtkCdnd=" + mtkCdnd);
                            MtkServiceStateTracker.this.mSubscriptionManagerService.setCarrierName(subId, TextUtils.emptyIfNull(MtkServiceStateTracker.this.getCarrierName(mtkCdnd)));
                            return;
                        } else {
                            MtkServiceStateTracker.this.logv("it's updateTurboPLMN when turboSS is OOS, skip");
                            return;
                        }
                    }
                    return;
                case MtkServiceStateTracker.EVENT_RECHECK_NRM_STATUS /* 123 */:
                    boolean connected2 = ((NetworkRegistrationManager) MtkServiceStateTracker.this.mRegStateManagers.get(1)).isServiceConnected();
                    MtkServiceStateTracker.this.log("EVENT_RECHECK_NRM_STATUS connected=" + connected2);
                    MtkServiceStateTracker mtkServiceStateTracker2 = MtkServiceStateTracker.this;
                    if (connected2) {
                        ((MtkGsmCdmaPhone) mtkServiceStateTracker2.mPhone).getServiceStateTracker().sendEmptyMessage(MtkServiceStateTracker.EVENT_RECHECK_NRM_STATUS);
                        return;
                    } else {
                        mtkServiceStateTracker2.mtkHandler.sendEmptyMessageDelayed(MtkServiceStateTracker.EVENT_RECHECK_NRM_STATUS, MtkServiceStateTracker.mCheckNrmPeriod);
                        return;
                    }
                case MtkServiceStateTracker.EVENT_GET_SIGNAL_STRENGTH /* 124 */:
                    AsyncResult ar4 = (AsyncResult) msg.obj;
                    if (ar4.exception != null || ar4.result == null) {
                        MtkServiceStateTracker.this.log("onSignalStrengthResult() Exception from RIL : " + ar4.exception);
                        return;
                    }
                    MtkServiceStateTracker.this.turboSig = (SignalStrength) ar4.result;
                    MtkServiceStateTracker.this.turboSig.updateLevel(MtkServiceStateTracker.this.mCarrierConfig, MtkServiceStateTracker.this.mSS);
                    MtkServiceStateTracker.this.log("notifySignalStrength level:" + MtkServiceStateTracker.this.turboSig.getLevel() + " raw:" + MtkServiceStateTracker.this.turboSig);
                    ((MtkGsmCdmaPhone) MtkServiceStateTracker.this.mPhone).notifyMtkSignalStrength(MtkServiceStateTracker.this.turboSig);
                    return;
                default:
                    MtkServiceStateTracker.this.loge("Should not be here msg.what=" + msg.what);
                    return;
            }
            boolean connected3 = ((NetworkRegistrationManager) MtkServiceStateTracker.this.mRegStateManagers.get(1)).isServiceConnected();
            if (connected3 || this.stop) {
                MtkServiceStateTracker.this.log("MtkHandler: stop MTK turbo");
                if (!this.stop) {
                    this.stop = true;
                }
                MtkServiceStateTracker.this.mCi.unregisterForNetworkStateChanged(MtkServiceStateTracker.this.mtkHandler);
                return;
            }
            if (msg.what == MtkServiceStateTracker.EVENT_RIL_READY) {
                MtkServiceStateTracker.this.log("MtkHandler: EVENT_RIL_READY");
            } else {
                int i = msg.what;
                MtkServiceStateTracker mtkServiceStateTracker3 = MtkServiceStateTracker.this;
                if (i == MtkServiceStateTracker.EVENT_RESTART_TURBO) {
                    mtkServiceStateTracker3.log("MtkHandler: EVENT_RESTART_TURBO");
                } else {
                    mtkServiceStateTracker3.log("MtkHandler: EVENT_NETWORK_STATE_CHANGED");
                    if (this.polling) {
                        this.pending = true;
                        return;
                    }
                }
            }
            if (MtkServiceStateTracker.this.mCi.getRadioState() == 1) {
                this.polling = true;
                MtkServiceStateTracker.this.mCi.getOperator(MtkServiceStateTracker.this.mtkHandler.obtainMessage(7, MtkServiceStateTracker.this.mPollingContext));
                MtkServiceStateTracker.this.mCi.getDataRegistrationState(MtkServiceStateTracker.this.mtkHandler.obtainMessage(5, MtkServiceStateTracker.this.mPollingContext));
                MtkServiceStateTracker.this.mCi.getVoiceRegistrationState(MtkServiceStateTracker.this.mtkHandler.obtainMessage(4, MtkServiceStateTracker.this.mPollingContext));
                MtkServiceStateTracker.this.mCi.getSignalStrength(MtkServiceStateTracker.this.mtkHandler.obtainMessage(MtkServiceStateTracker.EVENT_GET_SIGNAL_STRENGTH));
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public MtkServiceStateTracker(GsmCdmaPhone phone, CommandsInterface ci, FeatureFlags featureFlags) {
        super(phone, ci, featureFlags);
        this.mEnableERI = false;
        this.mTelephonyCustomizationFactory = null;
        this.mServiceStateTrackerExt = null;
        this.mDataRoamingTypeChangedRegistrants = new RegistrantList();
        this.mLocatedPlmn = null;
        this.mHhbName = null;
        this.mCsgId = null;
        this.mFemtocellDomain = 0;
        this.mIsFemtocell = 0;
        this.mFemtoPlmn = null;
        this.mFemtoAct = null;
        this.hasPendingPollState = false;
        this.turboSS = new ServiceState();
        this.mIsTurboSSAlive = true;
        this.turboSig = null;
        this.once_pollState_done = false;
        this.mCnShareNwConfig = new SparseArray<>(4);
        this.mMtkIntentReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.MtkServiceStateTracker.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("com.mediatek.common.carrierexpress.operator_config_changed")) {
                    try {
                        MtkServiceStateTracker.this.mTelephonyCustomizationFactory = OpTelephonyCustomizationUtils.getOpFactory(MtkServiceStateTracker.this.mPhone.getContext());
                        MtkServiceStateTracker.this.mServiceStateTrackerExt = MtkServiceStateTracker.this.mTelephonyCustomizationFactory.makeServiceStateTrackerExt(MtkServiceStateTracker.this.mPhone.getContext());
                        MtkServiceStateTracker.this.log("mServiceStateTrackerExt reload success");
                    } catch (Exception e) {
                        MtkServiceStateTracker.this.log("mServiceStateTrackerExt init fail");
                        e.printStackTrace();
                    }
                }
            }
        };
        this.mtkHandlerThread = new HandlerThread("MtkHandlerThread");
        this.mtkHandlerThread.start();
        this.mtkHandler = new MtkHandler(this.mtkHandlerThread.getLooper());
        Context context = this.mPhone.getContext();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.mediatek.common.carrierexpress.operator_config_changed");
        context.registerReceiver(this.mMtkIntentReceiver, filter);
        try {
            this.mTelephonyCustomizationFactory = OpTelephonyCustomizationUtils.getOpFactory(this.mPhone.getContext());
            this.mServiceStateTrackerExt = this.mTelephonyCustomizationFactory.makeServiceStateTrackerExt(this.mPhone.getContext());
        } catch (Exception e) {
            log("mServiceStateTrackerExt init fail");
            e.printStackTrace();
        }
        this.mCi.registerForPsNetworkStateChanged(this, 103, null);
        this.mCi.registerForRilConnected(this.mtkHandler, EVENT_RIL_READY, (Object) null);
        if (SystemProperties.get("ro.vendor.mtk_femto_cell_support").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            this.mCi.registerForFemtoCellInfo(this, 102, null);
        }
        this.mCi.registerForNetworkEvent(this, 104, null);
        this.mCi.registerForModulation(this, 105, null);
    }

    public boolean isTurboSSAlive() {
        return this.mIsTurboSSAlive;
    }

    public void registerForDataRoamingTypeChange(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mDataRoamingTypeChangedRegistrants.add(r);
    }

    public void unregisterForDataRoamingTypeChange(Handler h) {
        this.mDataRoamingTypeChangedRegistrants.remove(h);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void dispose() {
        super.dispose();
        this.mPhone.getContext().unregisterReceiver(this.mMtkIntentReceiver);
        this.mCi.unregisterForPsNetworkStateChanged(this);
        this.mtkHandlerThread.quit();
        this.mCi.unregisterForNetworkEvent(this);
        this.mCi.unregisterForModulation(this);
        if (SystemProperties.get("ro.vendor.mtk_femto_cell_support").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            this.mCi.unregisterForFemtoCellInfo(this);
        }
    }

    public void handleMessage(Message msg) {
        logv("received event " + msg.what);
        switch (msg.what) {
            case 1:
                RadioPowerStateStats.onRadioStateChanged(this.mCi.getRadioState());
                log("handle EVENT_RADIO_STATE_CHANGED");
                RadioManager.getInstance().setRadioPower(this.mDesiredPowerState, this.mPhone.getPhoneId());
                pollStateInternal(true);
                return;
            case 16:
                if (refreshSpn(this.mSS, false)) {
                    pollState();
                }
                super.handleMessage(msg);
                return;
            case 43:
            case 44:
                if (msg.obj != null) {
                    AsyncResult ar = (AsyncResult) msg.obj;
                    if (ar.exception != null || ar.result == null) {
                        this.mLastCellInfoList = null;
                    }
                }
                super.handleMessage(msg);
                return;
            case 45:
                log("EVENT_CHANGE_IMS_STATE: no action");
                return;
            case 49:
                log("EVENT_ALL_DATA_DISCONNECTED");
                synchronized (this) {
                    if (this.mFeatureFlags.keepWfcOnApm()) {
                        if (this.mPendingRadioPowerOffReason == 0) {
                            return;
                        }
                    } else if (!this.mPendingRadioPowerOffAfterDataOff) {
                        return;
                    }
                    boolean areAllDataDisconnectedOnAllPhones = true;
                    for (Phone phone : PhoneFactory.getPhones()) {
                        MtkDataNetworkController mtkDnc = (MtkDataNetworkController) this.mPhone.getDataNetworkController();
                        if (mtkDnc == null) {
                            return;
                        }
                        if (mtkDnc.areAllDataDisconnectedExceptIms()) {
                            mtkDnc.unregisterDataNetworkControllerCallback(this.mDataDisconnectedCallback);
                        } else {
                            log("Still waiting for all data disconnected on phone: " + phone.getPhoneId());
                            areAllDataDisconnectedOnAllPhones = false;
                        }
                    }
                    if (areAllDataDisconnectedOnAllPhones) {
                        if (this.mFeatureFlags.keepWfcOnApm()) {
                            this.mPendingRadioPowerOffReason = 0;
                        } else {
                            this.mPendingRadioPowerOffAfterDataOff = false;
                        }
                        removeMessages(38);
                        log("Data disconnected for all phones, turn radio off now.");
                        hangupAndPowerOff();
                    }
                    return;
                }
            case 50:
                pollStateInternal(true);
                return;
            case 55:
                AsyncResult ar2 = (AsyncResult) msg.obj;
                if (isTurboSSAlive()) {
                    log("Skip PHYSICAL_CHANNEL_CONFIG Because Turbo SS Alive");
                    if (ar2.exception == null) {
                        List<PhysicalChannelConfig> list = (List) ar2.result;
                        this.mLastPhysicalChannelConfigList = list;
                        this.mPhone.notifyPhysicalChannelConfig(list);
                        return;
                    }
                    return;
                }
                super.handleMessage(msg);
                return;
            case 102:
                onFemtoCellInfoResult((AsyncResult) msg.obj);
                return;
            case 103:
                onPsNetworkStateChangeResult((AsyncResult) msg.obj);
                return;
            case 104:
                onNetworkEventReceived((AsyncResult) msg.obj);
                return;
            case 105:
                onModulationInfoReceived((AsyncResult) msg.obj);
                return;
            case EVENT_SIM_OPL_LOADED /* 119 */:
                AsyncResult ar3 = (AsyncResult) msg.obj;
                if (ar3 != null && ar3.result != null) {
                    Integer id = (Integer) ar3.result;
                    if (id.intValue() == 103) {
                        log("EVENT_SIM_OPL_LOADED: EVENT_OPL5G");
                        if (refreshSpn(this.mSS, false)) {
                            pollState();
                            return;
                        }
                        return;
                    }
                    return;
                }
                loge("EVENT_SIM_OPL_LOADED obj is null");
                return;
            case EVENT_RECHECK_NRM_STATUS /* 123 */:
                log("EVENT_RECHECK_NRM_STATUS trigger pollState");
                pollStateInternal(true);
                return;
            case EVENT_IWLAN_PREFERRED_CHANGED /* 125 */:
                boolean isIwlanPreferred = this.mAccessNetworksManager.isAnyApnOnIwlan();
                log("EVENT_IWLAN_PREFERRED_CHANGED: isIwlanPreferred = " + isIwlanPreferred);
                if (this.mSS != null && isIwlanPreferred != this.mSS.isIwlanPreferred() && this.mPollingContext[0] == 0) {
                    pollStateInternal(false);
                    return;
                }
                return;
            default:
                super.handleMessage(msg);
                return;
        }
    }

    protected void handlePollStateResultMessage(int what, AsyncResult ar) {
        super.handlePollStateResultMessage(what, ar);
        switch (what) {
            case 5:
                NetworkRegistrationInfo networkRegState = (NetworkRegistrationInfo) ar.result;
                if (this.mServiceStateTrackerExt.modifyRsrpThresholdsForRsrpBoost(this.mCarrierConfig, networkRegState.getCellIdentity().getType(), this.mNewSS.getArfcnRsrpBoost())) {
                    log("need update signal strength's level after the rsrpBoost changed: " + this.mNewSS.getArfcnRsrpBoost());
                    SignalStrength ss = this.mPhone.getSignalStrengthController().getSignalStrength();
                    log("old:{level:" + ss.getLevel() + ", raw:" + ss.toString() + "}");
                    ss.updateLevel(this.mCarrierConfig, this.mNewSS);
                    log("new:{level: " + ss.getLevel() + "}");
                    this.mPhone.getSignalStrengthController().notifySignalStrength();
                }
                break;
        }
    }

    protected void updateRoamingState() {
        refreshSpn(this.mNewSS, true);
        super.updateRoamingState();
    }

    protected void onPollStateResultError(CommandException.Error error) {
        if (this.hasPendingPollState) {
            this.hasPendingPollState = false;
            pollState();
            loge("handlePollStateResult trigger pending pollState()");
        }
    }

    protected boolean needDiscardCurrentPollStateResult(ServiceState currentServiceState) {
        boolean in_service = currentServiceState.getVoiceRegState() == 0 || currentServiceState.getDataRegState() == 0;
        NetworkRegistrationInfo wlanPsNri = currentServiceState.getNetworkRegistrationInfo(2, 2);
        boolean radioOffwithIwlan = this.mCi.getRadioState() == 0 && (currentServiceState.getRilDataRadioTechnology() == 18 || (wlanPsNri != null && wlanPsNri.isInService()));
        String oper = currentServiceState.getOperatorNumeric();
        if (((in_service || TextUtils.isEmpty(oper)) && !(in_service && TextUtils.isEmpty(oper))) || radioOffwithIwlan || !this.hasPendingPollState) {
            return false;
        }
        loge("Temporary service state, need restart PollState");
        this.hasPendingPollState = false;
        cancelPollState();
        pollState();
        return true;
    }

    public void updateCarrierDisplayName() {
        if (isTurboSSAlive() && this.mPrevSubId == -1 && SubscriptionManager.isValidSubscriptionId(this.mPhone.getSubId()) && ((this.turboSS.getVoiceRegState() == 0 && !this.turboSS.getVoiceRoaming()) || (this.turboSS.getDataRegState() == 0 && !this.turboSS.getDataRoaming()))) {
            updateTurboPLMN();
        } else {
            super.updateCarrierDisplayName();
        }
    }

    protected CarrierDisplayNameData getCarrierDisplayNameLegacy() {
        CarrierDisplayNameData cdnd = super.getCarrierDisplayNameLegacy();
        return extendSpnDisplayNameData(cdnd);
    }

    private void onPsNetworkStateChangeResult(AsyncResult ar) {
        if (ar.exception != null || ar.result == null) {
            loge("onPsNetworkStateChangeResult exception");
            return;
        }
        int[] info = (int[]) ar.result;
        if (info.length < 6) {
            String operator_plmn = String.valueOf(info[1]);
            if (operator_plmn != null && operator_plmn.length() >= 5) {
                updateLocatedPlmn(operator_plmn);
                return;
            } else {
                updateLocatedPlmn(null);
                return;
            }
        }
        if (info.length != 7) {
            loge("onPsNetworkStateChangeResult wrong size");
            return;
        }
        if (info[0] == 4) {
            ((MtkGsmCdmaPhone) this.mPhone).notifyMtkFakeServiceStateChanged(null);
            return;
        }
        if (info[0] != 1) {
            ((MtkGsmCdmaPhone) this.mPhone).notifyMtkFakeServiceStateChanged(null);
            this.mPhone.notifyServiceStateChanged(this.mPhone.getServiceState());
            return;
        }
        boolean isCs = false;
        ServiceState fakeSS = new ServiceState();
        int networkType = ServiceState.rilRadioTechnologyToNetworkType(info[1]);
        if (info[1] == 3 || info[1] == 16) {
            isCs = true;
        }
        if (networkType == 19) {
            networkType = 13;
        }
        NetworkRegistrationInfo networkVoiceRegState = new NetworkRegistrationInfo(1, 1, isCs ? info[0] : 0, isCs ? networkType : 0, 0, false, null, null, "", false, 0, 0, 0);
        int voiceRegistrationState = networkVoiceRegState.getNetworkRegistrationState();
        fakeSS.setVoiceRegState(regCodeToServiceState(voiceRegistrationState));
        fakeSS.addNetworkRegistrationInfo(networkVoiceRegState);
        NetworkRegistrationInfo networkDataRegState = new NetworkRegistrationInfo(2, 1, !isCs ? info[0] : 0, !isCs ? networkType : 0, 0, false, null, null, "", 0, info[4] == 1, info[5] == 2, info[6] == 1, new LteVopsSupportInfo(1, 1));
        int dataRegistrationState = networkDataRegState.getNetworkRegistrationState();
        int serviceState = regCodeToServiceState(dataRegistrationState);
        fakeSS.setDataRegState(serviceState);
        fakeSS.addNetworkRegistrationInfo(networkDataRegState);
        log("broadcast fakeSS:" + fakeSS);
        ((MtkGsmCdmaPhone) this.mPhone).notifyMtkFakeServiceStateChanged(fakeSS);
        int display_type = fakeSS.getDataNetworkType();
        if (display_type == 0) {
            display_type = fakeSS.getVoiceNetworkType();
        }
        TelephonyDisplayInfo displayInfo = new TelephonyDisplayInfo(display_type, 0);
        log("broadcast fakeSS: " + displayInfo);
        ((MtkGsmCdmaPhone) this.mPhone).notifyDisplayInfoChanged(displayInfo);
    }

    protected void pollStateInternal(boolean modemTriggered) {
        boolean connected = ((NetworkRegistrationManager) this.mRegStateManagers.get(1)).isServiceConnected();
        int support_ap_iwlan = this.mRegStateManagers.get(2) != null ? 1 : 0;
        log("pollState: modemTriggered=" + modemTriggered + ", mPollingContext=" + (this.mPollingContext != null ? this.mPollingContext[0] : -1) + ", RadioState=" + this.mCi.getRadioState() + ", connected=" + connected + ", support_ap_iwlan=" + support_ap_iwlan);
        if (this.mPollingContext != null && this.mCi.getRadioState() == 1) {
            if (this.mPollingContext[0] == support_ap_iwlan + 4) {
                this.hasPendingPollState = true;
                return;
            }
        } else if (this.mPollingContext != null && this.mPollingContext[0] > 0 && !modemTriggered && this.mCi.getRadioState() == 0) {
            log("pollState: skip pollState when radio off && pollState is ongoing");
            return;
        }
        if (this.mCi.getRadioState() == 1) {
            this.mPollingContext = new int[1];
            this.mtkHandler.removeMessages(EVENT_RECHECK_NRM_STATUS);
            if (!connected) {
                log("Skip pollState due to disconnection of service");
                this.mtkHandler.sendEmptyMessageDelayed(EVENT_RECHECK_NRM_STATUS, mCheckNrmPeriod);
                return;
            }
        }
        super.pollStateInternal(modemTriggered);
    }

    protected void pollStateDone() {
        this.mOldSS = this.mSS;
        this.once_pollState_done = true;
        super.pollStateDone();
        if (this.mIsTurboSSAlive) {
            this.mIsTurboSSAlive = false;
            this.turboSS = new ServiceState();
        }
        boolean isMtkSSInService = false;
        for (NetworkRegistrationInfo nri : this.mSS.getNetworkRegistrationInfoListForTransportType(1)) {
            isMtkSSInService |= nri.isRegistered();
        }
        if (isMtkSSInService && (this.mLocatedPlmn == null || !this.mLocatedPlmn.equals(this.mSS.getOperatorNumeric()))) {
            updateLocatedPlmn(this.mSS.getOperatorNumeric());
        }
        if (this.hasPendingPollState) {
            this.hasPendingPollState = false;
            pollState();
        }
        this.mPhone.getSignalStrengthController().notifySignalStrength();
        if (this.mOldSS.getNrState() == 0 && this.mSS.getNrState() == 2 && this.mLastPhysicalChannelConfigList != null && this.mLastPhysicalChannelConfigList.size() > 0) {
            this.mPhone.notifyPhysicalChannelConfig(this.mLastPhysicalChannelConfigList);
        }
    }

    protected boolean hasSetRoamingType(ServiceState currentServiceState) {
        setRoamingType(currentServiceState);
        return true;
    }

    protected void onServiceStateChangeNotified(ServiceState newSS, boolean hasVoiceRegStateChanged, boolean anyDataRegChanged) {
        boolean hasVoiceRoamingTypeChanged = this.mOldSS.getVoiceRoamingType() != newSS.getVoiceRoamingType();
        boolean hasDataRoamingTypeChanged = this.mOldSS.getDataRoamingType() != newSS.getDataRoamingType();
        if (hasVoiceRegStateChanged || anyDataRegChanged || hasVoiceRoamingTypeChanged || hasDataRoamingTypeChanged) {
            this.mCi.setServiceStateToModem(this.mSS.getVoiceRegState(), this.mSS.getDataRegState(), this.mSS.getVoiceRoamingType(), this.mSS.getDataRoamingType(), this.mSS.getNetworkRegistrationInfo(1, 1).getNetworkRegistrationState(), this.mSS.getNetworkRegistrationInfo(2, 1).getNetworkRegistrationState(), null);
        }
    }

    private final boolean isConcurrentVoiceAndDataAllowedForIwlan() {
        if (this.mSS.getDataRegState() == 0 && this.mSS.getRilDataRadioTechnology() == 18 && getImsServiceState() == 0) {
            return true;
        }
        return false;
    }

    public boolean isConcurrentVoiceAndDataAllowed() {
        boolean allowed = super.isConcurrentVoiceAndDataAllowed();
        if (allowed || !(isConcurrentVoiceAndDataAllowedForVolte() || isConcurrentVoiceAndDataAllowedForIwlan() || isConcurrentVoiceAndDataAllowedForVolte())) {
            return allowed;
        }
        return true;
    }

    public String getLocatedPlmn() {
        return this.mLocatedPlmn;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastLocatedPlmnChanged(String old_Plmn, String plmn) {
        log("updateLocatedPlmn(),previous plmn= " + old_Plmn + " ,update to: " + plmn);
        Intent intent = new Intent("com.mediatek.intent.action.LOCATED_PLMN_CHANGED");
        if (TelephonyManager.getDefault().getPhoneCount() == 1) {
            intent.addFlags(536870912);
        }
        intent.putExtra("android.telephony.extra.PLMN", plmn);
        if (plmn != null) {
            try {
                intent.putExtra("iso", MccTable.countryCodeForMcc(plmn.substring(0, 3)));
            } catch (StringIndexOutOfBoundsException ex) {
                loge("updateLocatedPlmn: countryCodeForMcc error" + ex);
                intent.putExtra("iso", "");
            }
        } else {
            intent.putExtra("iso", "");
        }
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent, this.mPhone.getPhoneId());
        this.mPhone.getContext().sendStickyBroadcastAsUser(intent, UserHandle.ALL);
    }

    private void updateLocatedPlmn(final String plmn) {
        Thread subT = null;
        if ((this.mLocatedPlmn == null && plmn != null) || ((this.mLocatedPlmn != null && plmn == null) || (this.mLocatedPlmn != null && plmn != null && !this.mLocatedPlmn.equals(plmn)))) {
            final String old_plmn = this.mLocatedPlmn;
            subT = new Thread(new Runnable() { // from class: com.mediatek.internal.telephony.MtkServiceStateTracker.2
                @Override // java.lang.Runnable
                public void run() {
                    MtkServiceStateTracker.this.broadcastLocatedPlmnChanged(old_plmn, plmn);
                }
            });
        }
        this.mLocatedPlmn = plmn;
        if (subT != null) {
            subT.start();
        }
    }

    private void updateSsOperatorName(ServiceState ss, String strOperatorLong, String strOperatorShort, String operatorNumeric) {
        ss.setOperatorName(strOperatorLong, strOperatorShort, ss.getOperatorNumeric());
        NetworkRegistrationInfo nri_ps = ss.getNetworkRegistrationInfo(2, 1);
        if (nri_ps != null && nri_ps.getCellIdentity() != null) {
            CellIdentity ps_cell = nri_ps.getCellIdentity();
            ps_cell.setOperatorAlphaLong(strOperatorLong);
            ps_cell.setOperatorAlphaShort(strOperatorShort);
            NetworkRegistrationInfo new_nri_ps = new NetworkRegistrationInfo(nri_ps);
            ss.addNetworkRegistrationInfo(new_nri_ps);
        }
        NetworkRegistrationInfo nri_cs = ss.getNetworkRegistrationInfo(1, 1);
        if (nri_cs != null && nri_cs.getCellIdentity() != null) {
            CellIdentity cs_cell = nri_cs.getCellIdentity();
            cs_cell.setOperatorAlphaLong(strOperatorLong);
            cs_cell.setOperatorAlphaShort(strOperatorShort);
            NetworkRegistrationInfo new_nri_cs = new NetworkRegistrationInfo(nri_cs);
            ss.addNetworkRegistrationInfo(new_nri_cs);
        }
    }

    protected boolean refreshSpn(ServiceState ss, boolean fromPollState) {
        ServiceState ss2;
        String strOperatorLong;
        String strOperatorShort;
        boolean needPollState = false;
        String brandOverride = this.mUiccController.getUiccPort(getPhoneId()) != null ? this.mUiccController.getUiccPort(getPhoneId()).getOperatorBrandOverride() : null;
        if (brandOverride != null) {
            log("refreshSpn: use brandOverride" + brandOverride);
            strOperatorLong = brandOverride;
            strOperatorShort = brandOverride;
            ss2 = ss;
        } else {
            int lac = getLacFromServiceState(ss);
            ss2 = ss;
            strOperatorLong = this.mCi.lookupOperatorName(this.mPhone.getSubId(), ss.getOperatorNumeric(), true, lac, ss2);
            strOperatorShort = this.mCi.lookupOperatorName(this.mPhone.getSubId(), ss2.getOperatorNumeric(), false, lac, ss2);
        }
        if (!TextUtils.equals(strOperatorLong, ss2.getOperatorAlphaLong()) || !TextUtils.equals(strOperatorShort, ss2.getOperatorAlphaShort())) {
            needPollState = true;
            if (fromPollState) {
                updateSsOperatorName(ss2, strOperatorLong, strOperatorShort, ss2.getOperatorNumeric());
            }
        }
        log("refreshSpn: " + strOperatorLong + ", " + strOperatorShort + ", fromPollState=" + fromPollState + ", needPollState=" + needPollState);
        return needPollState;
    }

    protected void setPowerStateToDesired(boolean forEmergencyCall, boolean isSelectedPhoneForEmergencyCall, boolean forceApply) {
        String tmpLog = "setPowerStateToDesired: mDeviceShuttingDown=" + this.mDeviceShuttingDown + ", mDesiredPowerState=" + this.mDesiredPowerState + ", getRadioState=" + this.mCi.getRadioState() + ", mRadioPowerOffReasons=" + this.mRadioPowerOffReasons + ", IMS reg state=" + this.mImsRegistrationOnOff + ", pending radio off=" + hasMessages(62);
        log(tmpLog);
        this.mRadioPowerLog.log(tmpLog);
        if (this.mDesiredPowerState && this.mDeviceShuttingDown) {
            log("setPowerStateToDesired powering on of radio failed because the device is shutting down");
            return;
        }
        if (this.mDesiredPowerState && this.mRadioPowerOffReasons.isEmpty() && (forceApply || this.mCi.getRadioState() == 0 || this.mCi.getRadioState() == 2)) {
            RadioManager.getInstance();
            RadioManager.sendRequestBeforeSetRadioPower(true, this.mPhone.getPhoneId());
            this.mCi.setRadioPower(true, forEmergencyCall, isSelectedPhoneForEmergencyCall, (Message) null);
        } else if ((!this.mDesiredPowerState || !this.mRadioPowerOffReasons.isEmpty()) && this.mCi.getRadioState() == 1) {
            log("setPowerStateToDesired: powerOffRadioSafely()");
            powerOffRadioSafely();
        } else if (this.mDeviceShuttingDown && this.mCi.getRadioState() != 2) {
            this.mCi.requestShutdown((Message) null);
        }
        cancelDelayRadioOffWaitingForImsDeregTimeout();
    }

    protected void hangupAndPowerOff() {
        if (this.mCi.getRadioState() == 0) {
            return;
        }
        if (this.mPhone.isInCall()) {
            this.mPhone.mCT.mRingingCall.hangupIfAlive();
            this.mPhone.mCT.mBackgroundCall.hangupIfAlive();
            this.mPhone.mCT.mForegroundCall.hangupIfAlive();
        }
        mtkHangupAllImsCall();
        RadioManager.getInstance();
        RadioManager.sendRequestBeforeSetRadioPower(false, this.mPhone.getPhoneId());
        this.mCi.setRadioPower(false, obtainMessage(54));
    }

    public String getFemtoCsgId() {
        return this.mCsgId;
    }

    public String getFemtoPlmn() {
        return this.mFemtoPlmn;
    }

    public String getFemtoAct() {
        return this.mFemtoAct;
    }

    private void onFemtoCellInfoResult(AsyncResult ar) {
        int isCsgCell = 0;
        if (ar.exception != null || ar.result == null) {
            loge("onFemtoCellInfo exception");
            return;
        }
        String[] info = (String[]) ar.result;
        if (info.length > 0) {
            if (info[0] != null && info[0].length() > 0) {
                this.mFemtocellDomain = Integer.parseInt(info[0]);
                log("onFemtoCellInfo: mFemtocellDomain set to " + this.mFemtocellDomain);
            }
            if (info[3] != null && info[3].length() > 0) {
                this.mFemtoPlmn = info[3];
                log("onFemtoCellInfo: mFemtoPlmn set to " + this.mFemtoPlmn);
            }
            if (info[4] != null && info[4].length() > 0) {
                this.mFemtoAct = info[4];
                log("onFemtoCellInfo: mFemtoAct set to " + this.mFemtoAct);
            }
            if (info[5] != null && info[5].length() > 0) {
                isCsgCell = Integer.parseInt(info[5]);
            }
            this.mIsFemtocell = isCsgCell;
            log("onFemtoCellInfo: domain= " + this.mFemtocellDomain + ",isCsgCell= " + isCsgCell);
            if (isCsgCell == 1) {
                if (info[6] != null && info[6].length() > 0) {
                    this.mCsgId = info[6];
                    log("onFemtoCellInfo: mCsgId set to " + this.mCsgId);
                }
                if (info[8] != null && info[8].length() > 0) {
                    this.mHhbName = new String(IccUtils.hexStringToBytes(info[8]));
                    log("onFemtoCellInfo: mHhbName set from " + info[8] + " to " + this.mHhbName);
                } else {
                    this.mHhbName = null;
                    log("onFemtoCellInfo: mHhbName is not available ,set to null");
                }
            } else {
                this.mCsgId = null;
                this.mHhbName = null;
                log("onFemtoCellInfo: csgId and hnbName are cleared");
            }
            Intent intent = new Intent("android.telephony.action.SERVICE_PROVIDERS_UPDATED");
            SubscriptionManager.putPhoneIdAndSubIdExtra(intent, this.mPhone.getPhoneId());
            if (TelephonyManager.getDefault().getPhoneCount() == 1) {
                intent.addFlags(536870912);
            }
            intent.putExtra("android.telephony.extra.SHOW_SPN", this.mCarrierDisplayNameData.shouldShowSpn());
            intent.putExtra("android.telephony.extra.SPN", this.mCarrierDisplayNameData.getSpn());
            intent.putExtra("android.telephony.extra.SHOW_PLMN", this.mCarrierDisplayNameData.shouldShowPlmn());
            intent.putExtra("android.telephony.extra.PLMN", this.mCarrierDisplayNameData.getPlmn());
            intent.putExtra("hnbName", this.mHhbName);
            intent.putExtra("csgId", this.mCsgId);
            intent.putExtra("domain", this.mFemtocellDomain);
            intent.putExtra("femtocell", this.mIsFemtocell);
            this.mPhone.getContext().sendStickyBroadcastAsUser(intent, UserHandle.ALL);
            this.mPhone.getPhoneId();
            String plmn = this.mCarrierDisplayNameData.getPlmn();
            if (this.mHhbName == null && this.mCsgId != null) {
                String str = (plmn + " - ") + this.mCsgId;
            } else if (this.mHhbName != null) {
                String str2 = (plmn + " - ") + this.mHhbName;
            }
            int subId = this.mPhone.getSubId();
            if (SubscriptionManager.isValidSubscriptionId(subId)) {
                this.mSubscriptionManagerService.setCarrierName(subId, TextUtils.emptyIfNull(getCarrierName(this.mCarrierDisplayNameData)));
            }
        }
    }

    private void onNetworkEventReceived(AsyncResult ar) {
        if (ar.exception != null || ar.result == null) {
            loge("onNetworkEventReceived exception");
            return;
        }
        int nwEventType = ((int[]) ar.result)[1];
        log("[onNetworkEventReceived] event_type:" + nwEventType);
        Intent intent = new Intent("com.mediatek.intent.action.ACTION_NETWORK_EVENT");
        intent.addFlags(536870912);
        intent.putExtra("eventType", nwEventType + 1);
        this.mPhone.getContext().sendStickyBroadcastAsUser(intent, UserHandle.ALL);
    }

    private void onModulationInfoReceived(AsyncResult ar) {
        if (ar.exception != null || ar.result == null) {
            loge("onModulationInfoReceived exception");
            return;
        }
        int[] info = (int[]) ar.result;
        int modulation = info[0];
        log("[onModulationInfoReceived] modulation:" + modulation);
        Intent intent = new Intent("com.mediatek.intent.action.ACTION_NOTIFY_MODULATION_INFO");
        intent.addFlags(536870912);
        intent.putExtra("modulation_info", modulation);
        this.mPhone.getContext().sendStickyBroadcastAsUser(intent, UserHandle.ALL);
    }

    protected final boolean IsInternationalRoamingException(String operatorNumeric) {
        CarrierConfigManager configManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService("carrier_config");
        if (configManager == null) {
            Rlog.e(LOG_TAG, "Carrier config service is not available");
            return false;
        }
        PersistableBundle b = configManager.getConfigForSubId(this.mPhone.getSubId());
        if (b == null) {
            Rlog.e(LOG_TAG, "Can't get the config. subId = " + this.mPhone.getSubId());
            return false;
        }
        String[] operatorRoamingException = b.getStringArray("mtk_carrier_international_roaming_exception_list_strings");
        if (operatorRoamingException == null) {
            Rlog.e(LOG_TAG, "mtk_carrier_international_roaming_exception_list_strings is not available. subId = " + this.mPhone.getSubId());
            return false;
        }
        HashSet<String> internationalRoamingSet = new HashSet<>(Arrays.asList(operatorRoamingException));
        Rlog.d(LOG_TAG, "For subId = " + this.mPhone.getSubId() + ", international roaming exceptions are " + Arrays.toString(internationalRoamingSet.toArray()) + ", operatorNumeric = " + operatorNumeric);
        if (internationalRoamingSet.contains(operatorNumeric)) {
            Rlog.d(LOG_TAG, operatorNumeric + " in list.");
            return true;
        }
        Rlog.d(LOG_TAG, operatorNumeric + " is not in list.");
        return false;
    }

    protected void setRoamingType(ServiceState currentServiceState) {
        super.setRoamingType(currentServiceState);
        boolean isVoiceInService = currentServiceState.getState() == 0;
        if (isVoiceInService && currentServiceState.getVoiceRoaming()) {
            boolean isInternationalRoaming = IsInternationalRoamingException(currentServiceState.getVoiceOperatorNumeric());
            if (isInternationalRoaming) {
                log(currentServiceState.getVoiceOperatorNumeric() + " is in operator defined international roaming list");
                currentServiceState.setVoiceRoamingType(3);
            }
        }
        boolean isDataInService = currentServiceState.getDataRegistrationState() == 0;
        if (isDataInService && currentServiceState.getDataRoaming()) {
            int dataRegType = getRilDataRadioTechnologyForWwan(currentServiceState);
            if (ServiceState.isGsm(dataRegType)) {
                if (isVoiceInService) {
                    currentServiceState.setDataRoamingType(currentServiceState.getVoiceRoamingType());
                    return;
                }
                if (inSameCountry(currentServiceState.getVoiceOperatorNumeric())) {
                    currentServiceState.setDataRoamingType(2);
                } else {
                    currentServiceState.setDataRoamingType(3);
                }
                boolean isInternationalRoaming2 = IsInternationalRoamingException(currentServiceState.getVoiceOperatorNumeric());
                if (isInternationalRoaming2) {
                    log(currentServiceState.getVoiceOperatorNumeric() + " is in operator defined international roaming list");
                    currentServiceState.setDataRoamingType(3);
                }
            }
        }
    }

    private final boolean isConcurrentVoiceAndDataAllowedForVolte() {
        if (this.mSS.getDataRegState() == 0 && ServiceState.isPsOnlyTech(this.mSS.getRilDataRadioTechnology()) && getImsServiceState() == 0) {
            return true;
        }
        return false;
    }

    private final int getImsServiceState() {
        Phone imsPhone = this.mPhone.getImsPhone();
        if (imsPhone != null) {
            return imsPhone.getServiceState().getState();
        }
        return 1;
    }

    protected boolean isOperatorConsideredNonRoaming(ServiceState s) {
        boolean result = super.isOperatorConsideredNonRoaming(s);
        if (result) {
            log("isOperatorConsideredNonRoaming true");
        }
        return result;
    }

    protected boolean isOperatorConsideredRoaming(ServiceState s) throws RemoteException {
        boolean result;
        String[] opeartors = MtkTelephonyManagerEx.getDefault().getSimOperatorNumericForPhoneEx(this.mPhone.getPhoneId());
        String simNumeric = opeartors != null ? opeartors[0] : "";
        log("isOperatorConsideredRoaming simNumeric  = " + simNumeric);
        String operatorNumeric = s.getOperatorNumeric();
        if (MiuiNetworkControllerStub.get(this.mPhone.getPhoneId()).isOperatorConsideredNonRoaming(this.mPhone, operatorNumeric)) {
            return false;
        }
        if (!TextUtils.isEmpty(simNumeric) && ((simNumeric.substring(0, 3).equals("404") || simNumeric.substring(0, 3).equals("405")) && !TextUtils.isEmpty(operatorNumeric) && (operatorNumeric.substring(0, 3).equals("404") || operatorNumeric.substring(0, 3).equals("405")))) {
            result = true;
        } else {
            result = super.isOperatorConsideredRoaming(s);
        }
        if (result) {
            log("isOperatorConsideredRoaming true");
        }
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void onUpdateIccAvailability() {
        UiccCardApplication oldUiccApplication = this.mUiccApplication;
        IccRecords oldRecords = this.mIccRecords;
        super.onUpdateIccAvailability();
        TelephonyBaseUtilsStub.isMiuiRom();
        UiccCardApplication newUiccApplication = this.mUiccApplication;
        IccRecords newRecords = this.mIccRecords;
        if (oldUiccApplication != newUiccApplication) {
            if (oldRecords != null) {
                oldRecords.unregisterForRecordsEvents(this);
            }
            if (newRecords != null) {
                newRecords.registerForRecordsEvents(this, EVENT_SIM_OPL_LOADED, (Object) null);
            }
        }
    }

    protected boolean mtkAreAllDataDisconnected(Phone phone) {
        boolean ret = true;
        MtkDataNetworkController mtkDnc = (MtkDataNetworkController) phone.getDataNetworkController();
        if (mtkDnc != null) {
            ret = mtkDnc.areAllDataDisconnectedExceptIms();
        }
        log("mtkAreAllDataDisconnected: " + ret);
        return ret;
    }

    public void setRadioPowerForReason(boolean power, boolean forEmergencyCall, boolean isSelectedPhoneForEmergencyCall, boolean forceApply, int reason) {
        int radioState = this.mCi.getRadioState();
        log("setRadioPowerForReason power:" + power + ", mDesiredPowerState:" + this.mDesiredPowerState + ", forEmergencyCall:" + forEmergencyCall + ", isSelectedPhoneForEmergencyCall:" + isSelectedPhoneForEmergencyCall + ", forceApply:" + forceApply + ", radioState:" + radioState + ", reason:" + reason);
        if (TelephonyCommonUtilsStub.get().isDemoBuild()) {
            forceApply = true;
        }
        if (power) {
            if (forEmergencyCall) {
                clearAllRadioOffReasons();
            } else {
                this.mRadioPowerOffReasons.remove(Integer.valueOf(reason));
            }
        } else {
            this.mRadioPowerOffReasons.add(Integer.valueOf(reason));
        }
        if (((power && radioState == 1) || (!power && radioState == 0 && !this.mDeviceShuttingDown)) && !forceApply) {
            this.mDesiredPowerState = power;
            log("setRadioPowerForReason mDesiredPowerState is already " + power + " Do nothing.");
            return;
        }
        if (power && !this.mRadioPowerOffReasons.isEmpty()) {
            log("setRadioPowerForReason power: " + power + " forEmergencyCall= " + forEmergencyCall + " isSelectedPhoneForEmergencyCall: " + isSelectedPhoneForEmergencyCall + " forceApply " + forceApply + "reason:" + reason + " will not power on the radio as it is powered off for the following reasons: " + this.mRadioPowerOffReasons + ".");
            return;
        }
        if (power && power == this.mDesiredPowerState && !forEmergencyCall && MtkTelephonyManagerEx.getDefault().isInEmergencyCall()) {
            log("adjust forEmergencyCall = true");
            forEmergencyCall = true;
        }
        if (power && power == this.mDesiredPowerState && !forEmergencyCall && MtkTelephonyManagerEx.getDefault().isInEmergencyCall()) {
            log("adjust forEmergencyCall = true");
            forEmergencyCall = true;
        }
        if (TelephonyCommonUtilsStub.get().isDemoBuild()) {
            this.mDesiredPowerState = false;
        }
        this.mDesiredPowerState = power;
        setPowerStateToDesired(forEmergencyCall, isSelectedPhoneForEmergencyCall, forceApply);
        if (this.mDesiredPowerState) {
            SatelliteController.getInstance().onSetCellularRadioPowerStateRequested(true);
        }
        if (forEmergencyCall) {
            RadioManager.getInstance().forceRefreshSimState(power, this.mPhone.getPhoneId());
        }
    }

    public boolean hasPendingRadioPowerOff() {
        return this.mPendingRadioPowerOffAfterDataOff;
    }

    protected static final String lookupOperatorName(Context context, int subId, String numeric, boolean desireLongName) {
        String operName = null;
        boolean isChinaTelecomMvno = false;
        Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(subId));
        if (phone != null) {
            operName = MtkSpnOverride.getInstance().getSpnByPattern(subId, numeric);
            isChinaTelecomMvno = isChinaTelecomMvno(context, subId, numeric, operName);
        } else {
            Rlog.e(LOG_TAG, "lookupOperatorName getPhone null subid=" + subId);
        }
        if (operName == null) {
            operName = lookupCnSharingOperatorName(context, numeric, desireLongName);
        }
        if (operName == null || isChinaTelecomMvno) {
            operName = MtkSpnOverride.getInstance().getSpnByNumeric(numeric, desireLongName, context);
        }
        if (TelephonyBaseUtilsStub.isMiuiRom() && TextUtils.isEmpty(operName)) {
            for (String[] numeric_spn : sSpnOverride) {
                if (numeric_spn[0].equals(numeric)) {
                    return numeric_spn[1];
                }
            }
        }
        return operName == null ? numeric : operName;
    }

    private static final boolean isChinaTelecomMvno(Context context, int subId, String numeric, String mvnoOperName) {
        String simCarrierName = TelephonyManager.from(context).getSimOperatorName(subId);
        if ("".equals(mvnoOperName)) {
            return true;
        }
        if ((!"20404".equals(numeric) && !"45403".equals(numeric)) || !"".equals(simCarrierName)) {
            return false;
        }
        return true;
    }

    protected void mtkHangupAllImsCall() {
        Phone imsPhone = this.mPhone.getImsPhone();
        if (imsPhone != null) {
            if (!imsPhone.isWifiCallingEnabled() || this.mDeviceShuttingDown) {
                boolean sendNetworkCoverage = this.mCarrierConfig.getBoolean("mtk_send_network_coverage_lost");
                log("hangupAndPowerOff: sendNetworkCoverage = " + sendNetworkCoverage);
                if (sendNetworkCoverage) {
                    imsPhone.getForegroundCall().hangupIfAlive(1009);
                    imsPhone.getBackgroundCall().hangupIfAlive(1009);
                    imsPhone.getRingingCall().hangupIfAlive(1009);
                    log("hangupAndPowerOff: RJIO: hangup VoLTE call.");
                    return;
                }
                imsPhone.getForegroundCall().hangupIfAlive();
                imsPhone.getBackgroundCall().hangupIfAlive();
                imsPhone.getRingingCall().hangupIfAlive();
                log("hangupAndPowerOff: hangup VoLTE call.");
            }
        }
    }

    public int getLacFromServiceState(ServiceState ss) {
        List<CellIdentity> prioritizedCids = getPrioritizedCellIdentities(ss);
        CellIdentity id = prioritizedCids.isEmpty() ? null : prioritizedCids.get(0);
        if (id == null) {
            return -1;
        }
        int lac = -1;
        switch (id.getType()) {
            case 1:
                lac = ((CellIdentityGsm) id).getLac();
                break;
            case 3:
                lac = ((CellIdentityLte) id).getTac();
                break;
            case 4:
                lac = ((CellIdentityWcdma) id).getLac();
                break;
            case 5:
                lac = ((CellIdentityTdscdma) id).getLac();
                break;
            case 6:
                lac = ((CellIdentityNr) id).getTac();
                break;
        }
        if (lac == Integer.MAX_VALUE) {
            return -1;
        }
        return lac;
    }

    public int getLac() {
        return getLacFromServiceState(this.mSS);
    }

    protected CarrierDisplayNameData extendSpnDisplayNameData(CarrierDisplayNameData origin) throws RemoteException {
        String spn = origin.getSpn();
        String dataSpn = origin.getDataSpn();
        boolean showSpn = origin.shouldShowSpn();
        String plmn = origin.getPlmn();
        boolean showPlmn = origin.shouldShowPlmn();
        try {
            plmn = this.mServiceStateTrackerExt.onUpdateSpnDisplay(plmn, this.mSS, this.mPhone.getPhoneId());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (this.mSS.getVoiceRegState() != 0 && this.mSS.getDataRegState() != 0) {
            spn = null;
            showSpn = false;
        } else if (!this.mSS.getRoaming()) {
            String[] opeartors = MtkTelephonyManagerEx.getDefault().getSimOperatorNumericForPhoneEx(this.mPhone.getPhoneId());
            String simNumeric = opeartors != null ? opeartors[0] : "";
            if (((!TextUtils.isEmpty(simNumeric) && simNumeric.substring(0, 3).equals(RadioCapabilitySwitchUtil.CN_MCC)) || this.mCarrierConfig.getBoolean("mtk_show_plmn_only_for_home_bool", false)) && !TextUtils.isEmpty(plmn)) {
                showSpn = false;
                showPlmn = true;
                log("extendSpnDisplayNameData: show plmn only due to cn sim or CC");
            }
        }
        if (!showPlmn && !showSpn && !TextUtils.isEmpty(plmn)) {
            showPlmn = true;
        }
        return new CarrierDisplayNameData.Builder().setSpn(spn).setDataSpn(dataSpn).setShowSpn(showSpn).setPlmn(plmn).setShowPlmn(showPlmn).build();
    }

    protected void extendSpnDisplayIntent(Intent intent) {
        intent.putExtra("hnbName", this.mHhbName);
        intent.putExtra("csgId", this.mCsgId);
        intent.putExtra("domain", this.mFemtocellDomain);
        intent.putExtra("femtocell", this.mIsFemtocell);
        if (TelephonyManager.getDefault().getPhoneCount() == 1) {
            intent.addFlags(536870912);
        }
    }

    protected boolean setCarrierNameAsync(CarrierDisplayNameData data) {
        updatePLMN(data.shouldShowPlmn(), data.getPlmn(), data.shouldShowSpn(), data.getSpn());
        return true;
    }

    protected boolean updateNrFrequencyRangeFromPhysicalChannelConfigs(List<PhysicalChannelConfig> physicalChannelConfigs, ServiceState ss) {
        if ((physicalChannelConfigs == null || physicalChannelConfigs.isEmpty()) && ss.getDataNetworkType() == 20) {
            int newFrequencyRange = 0;
            NetworkRegistrationInfo nri = ss.getNetworkRegistrationInfo(2, 1);
            if (nri != null && nri.getCellIdentity() != null) {
                CellIdentity cellIdentity = nri.getCellIdentity();
                if (cellIdentity.getType() == 6) {
                    int[] bands = ((CellIdentityNr) cellIdentity).getBands();
                    for (int band : bands) {
                        newFrequencyRange = Math.max(newFrequencyRange, AccessNetworkUtils.getFrequencyRangeGroupFromNrBand(band));
                        log("NR IDLE state, use ps band[" + band + "] update FrequencyRange to " + newFrequencyRange);
                    }
                }
            }
            boolean hasChanged = newFrequencyRange != ss.getNrFrequencyRange();
            if (hasChanged) {
                log(String.format("NR frequency range changed from %s to %s.", ServiceState.frequencyRangeToString(ss.getNrFrequencyRange()), ServiceState.frequencyRangeToString(newFrequencyRange)));
            }
            ss.setNrFrequencyRange(newFrequencyRange);
            return hasChanged;
        }
        return super.updateNrFrequencyRangeFromPhysicalChannelConfigs(physicalChannelConfigs, ss);
    }

    private static String getCnOpName(Context context, int id, boolean desireLongName) {
        switch (id) {
            case 1:
                if (desireLongName) {
                    return context.getText(R.string.permlab_callCompanionApp).toString();
                }
                return context.getText(R.string.permlab_changeWimaxState).toString();
            case 2:
                if (desireLongName) {
                    return context.getText(R.string.permlab_callPhone).toString();
                }
                return context.getText(R.string.permlab_companionProfileWatch).toString();
            case 3:
                if (desireLongName) {
                    return context.getText(R.string.permlab_camera).toString();
                }
                return context.getText(R.string.permlab_connection_manager).toString();
            case 4:
                if (desireLongName) {
                    return context.getText(R.string.permlab_cameraHeadlessSystemUser).toString();
                }
                return context.getText(R.string.permlab_control_incall_experience).toString();
            default:
                return "";
        }
    }

    protected static final String lookupCnSharingOperatorName(Context context, String plmn, boolean desireLongName) {
        if (TextUtils.isEmpty(plmn) || !plmn.startsWith(RadioCapabilitySwitchUtil.CN_MCC)) {
            return null;
        }
        if (plmn.equals("46050")) {
            return getCnOpName(context, 1, desireLongName) + MtkContactsContract.Aas.ENCODE_SYMBOL + getCnOpName(context, 3, desireLongName);
        }
        if (plmn.equals("46051")) {
            return getCnOpName(context, 4, desireLongName) + MtkContactsContract.Aas.ENCODE_SYMBOL + getCnOpName(context, 3, desireLongName);
        }
        if (plmn.equals("46021")) {
            return getCnOpName(context, 3, desireLongName) + MtkContactsContract.Aas.ENCODE_SYMBOL + getCnOpName(context, 1, desireLongName);
        }
        if (plmn.equals("46022")) {
            return getCnOpName(context, 2, desireLongName) + MtkContactsContract.Aas.ENCODE_SYMBOL + getCnOpName(context, 1, desireLongName);
        }
        if (plmn.equals("46031")) {
            return getCnOpName(context, 1, desireLongName) + MtkContactsContract.Aas.ENCODE_SYMBOL + getCnOpName(context, 2, desireLongName);
        }
        if (plmn.equals("46032")) {
            return getCnOpName(context, 4, desireLongName) + MtkContactsContract.Aas.ENCODE_SYMBOL + getCnOpName(context, 2, desireLongName);
        }
        if (plmn.equals("46060")) {
            return getCnOpName(context, 3, desireLongName) + MtkContactsContract.Aas.ENCODE_SYMBOL + getCnOpName(context, 4, desireLongName);
        }
        if (plmn.equals("46061")) {
            return getCnOpName(context, 2, desireLongName) + MtkContactsContract.Aas.ENCODE_SYMBOL + getCnOpName(context, 4, desireLongName);
        }
        return null;
    }

    private static final boolean isCnSharedPlmn(String numeric) {
        if (TextUtils.isEmpty(numeric) || !numeric.startsWith(RadioCapabilitySwitchUtil.CN_MCC)) {
            return false;
        }
        String[] cnSharePlmns = {"46021", "46022", "46031", "46032", "46050", "46051", "46060", "46061"};
        for (String item : cnSharePlmns) {
            if (item.equals(numeric)) {
                return true;
            }
        }
        return false;
    }
}
