package com.mediatek.internal.telephony;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.ims.ImsManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.PhoneNotifier;
import com.android.internal.telephony.RadioCapability;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.gsm.GsmMmiCode;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneMmiCode;
import com.android.internal.telephony.imsphone.MiuiImsControllerStub;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccPort;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.telephony.Rlog;
import com.mediatek.internal.telephony.data.MtkDataNetworkController;
import com.mediatek.internal.telephony.data.MtkDataProfileManager;
import com.mediatek.internal.telephony.gsm.MtkSuppServiceNotification;
import com.mediatek.internal.telephony.imsphone.MtkImsPhone;
import com.mediatek.internal.telephony.imsphone.MtkLocalPhoneNumberUtils;
import com.mediatek.internal.telephony.selfactivation.ISelfActivation;
import com.mediatek.internal.telephony.subscription.MtkSubscriptionManagerService;
import com.mediatek.internal.telephony.uicc.MtkSIMRecords;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import mediatek.telephony.MtkDisconnectCause;

/* JADX INFO: loaded from: classes.dex */
public class MtkGsmCdmaPhone extends GsmCdmaPhone {
    public static final String ACT_TYPE_GSM = "0";
    public static final String ACT_TYPE_LTE = "7";
    public static final String ACT_TYPE_NR = "11";
    public static final String ACT_TYPE_UTRAN = "2";
    private static final String CFU_TIME_SLOT = "persist.vendor.radio.cfu.timeslot.";
    private static final boolean DBG = true;
    protected static final int EVENT_CIPHER_INDICATION = 1000;
    protected static final int EVENT_CRSS_IND = 1003;
    private static final int EVENT_ESSENTIAL_SIM_RECORDS_LOADED = 3000;
    protected static final int EVENT_GET_APC_INFO = 1001;
    public static final int EVENT_GET_CALL_BARRING_COMPLETE = 2006;
    public static final int EVENT_GET_CALL_FORWARD_TIME_SLOT_DONE = 109;
    public static final int EVENT_GET_CLIR_COMPLETE = 2004;
    protected static final int EVENT_MTK_BASE = 1000;
    protected static final int EVENT_OEM_RAW_URC = 1005;
    public static final int EVENT_SET_CALL_BARRING_COMPLETE = 2005;
    public static final int EVENT_SET_CALL_FORWARD_TIME_SLOT_DONE = 110;
    protected static final int EVENT_SET_SS_PROPERTY = 1004;
    protected static final int EVENT_SSN_EX = 1002;
    public static final int EVENT_UNSOL_RADIO_CAPABILITY_CHANGED = 111;
    public static final String GSM_INDICATOR = "2G";
    public static final String LOG_TAG = "MtkGsmCdmaPhone";
    public static final String LTE_INDICATOR = "4G";
    public static final String NR_INDICATOR = "5G";
    public static final int NT_MODE_LTE_GSM = 101;
    public static final int NT_MODE_LTE_TDD_ONLY = 102;
    private static final int OPERATION_TIME_OUT_MILLIS = 3000;
    private static final String PROPERTY_DISABLE_AUTO_RETURN_RPLMN = "persist.vendor.radio.disable_auto_return_rplmn";
    private static final int PROPERTY_MODE_BOOL = 1;
    private static final int PROPERTY_MODE_INT = 0;
    private static final int PROPERTY_MODE_STRING = 2;
    private static final String[] PROPERTY_RIL_FULL_UICC_TYPE = {"vendor.gsm.ril.fulluicctype", "vendor.gsm.ril.fulluicctype.2", "vendor.gsm.ril.fulluicctype.3", "vendor.gsm.ril.fulluicctype.4"};
    public static final String SIM_STATE_ESSENTIAL_RECORDS_LOADED = "ESSENTIAL_LOADED";
    private static final String SS_SERVICE_CLASS_PROP = "vendor.gsm.radio.ss.sc";
    public static final String UTRAN_INDICATOR = "3G";
    private static final int VENDOR_SETTING_PREFERRED_PSIM = 18;
    private boolean enableFakeSS;
    private BroadcastReceiver mBroadcastReceiver;
    private AsyncResult mCachedCrssn;
    private AsyncResult mCachedSsn;
    RegistrantList mCallRelatedSuppSvcRegistrants;
    private CountDownLatch mCallbackLatch;
    protected final RegistrantList mCipherIndicationRegistrants;
    private ExecutorService mExecutorService;
    private final Object mLock;
    public MtkRIL mMtkCi;
    public MtkServiceStateTracker mMtkSST;
    protected final RegistrantList mOemIndRegistrants;
    private int mPrecisePhoneType;
    private ISelfActivation mSelfActInstance;

    /* JADX WARN: Multi-variable type inference failed */
    public MtkGsmCdmaPhone(Context context, CommandsInterface ci, PhoneNotifier notifier, boolean unitTestMode, int phoneId, int precisePhoneType, TelephonyComponentFactory telephonyComponentFactory, FeatureFlags featureFlags) {
        super(context, ci, notifier, unitTestMode, phoneId, precisePhoneType, telephonyComponentFactory, featureFlags);
        this.mLock = new Object();
        this.mCipherIndicationRegistrants = new RegistrantList();
        this.mCallRelatedSuppSvcRegistrants = new RegistrantList();
        this.mCachedSsn = null;
        this.mCachedCrssn = null;
        this.mSelfActInstance = null;
        this.mExecutorService = Executors.newSingleThreadExecutor();
        this.enableFakeSS = false;
        this.mOemIndRegistrants = new RegistrantList();
        this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.MtkGsmCdmaPhone.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                intent.getAction();
                if (intent.getAction().equals("android.telephony.action.CARRIER_CONFIG_CHANGED") && intent.getExtras().getInt("phone") == MtkGsmCdmaPhone.this.mPhoneId) {
                    MtkGsmCdmaPhone.this.sendMessage(MtkGsmCdmaPhone.this.obtainMessage(43));
                }
            }
        };
        this.mPrecisePhoneType = precisePhoneType;
        Rlog.d(LOG_TAG, "constructor: sub = " + phoneId);
        this.mMtkCi = (MtkRIL) ci;
        this.mMtkSST = (MtkServiceStateTracker) this.mSST;
        this.mMtkCi.registerForCipherIndication(this, MtkDisconnectCause.MTK_DISCONNECTED_CAUSE_BASE, null);
        OpTelephonyCustomizationFactoryBase telephonyCustomizationFactory = OpTelephonyCustomizationUtils.getOpFactory(context);
        this.mSelfActInstance = telephonyCustomizationFactory.makeSelfActivationInstance(phoneId);
        this.mSelfActInstance.setContext(context).setCommandsInterface(ci).buildParams();
        this.mCi.getVoiceRadioTechnology(obtainMessage(40));
        this.mIsCarrierNrSupported = DBG;
    }

    public MtkGsmCdmaPhone(Context context, CommandsInterface ci, PhoneNotifier notifier, int phoneId, int precisePhoneType, TelephonyComponentFactory telephonyComponentFactory, FeatureFlags featureFlags) {
        this(context, ci, notifier, false, phoneId, precisePhoneType, telephonyComponentFactory, featureFlags);
    }

    public ISelfActivation getSelfActivationInstance() {
        return this.mSelfActInstance;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void initOnce(CommandsInterface ci) {
        super.initOnce(ci);
        this.mCi.registerForRadioCapabilityChanged(this, EVENT_UNSOL_RADIO_CAPABILITY_CHANGED, (Object) null);
        if (this.mMtkCi == null) {
            this.mMtkCi = (MtkRIL) ci;
        }
        this.mMtkCi.setOnSuppServiceNotificationEx(this, 1002, null);
        this.mMtkCi.setOnCallRelatedSuppSvc(this, 1003, null);
        this.mMtkCi.setOnUnsolOemHookRaw(this, 1005, null);
    }

    protected void onUpdateIccAvailability() {
        super.onUpdateIccAvailability();
        UiccCardApplication newUiccApplication = getUiccCardApplication();
        UiccCardApplication app = (UiccCardApplication) this.mUiccApplication.get();
        IccRecords newIccRecord = newUiccApplication != null ? newUiccApplication.getIccRecords() : null;
        if (app == newUiccApplication && this.mIccRecords.get() != newIccRecord) {
            if (app != null) {
                logd("Removing stale icc objects.");
                if (this.mIccRecords.get() != null) {
                    unregisterForIccRecordEvents();
                    this.mIccPhoneBookIntManager.updateIccRecords((IccRecords) null);
                }
                this.mIccRecords.set(null);
                this.mUiccApplication.set(null);
            }
            if (newUiccApplication != null) {
                logd("New Uicc application found. type = " + newUiccApplication.getType());
                this.mUiccApplication.set(newUiccApplication);
                this.mIccRecords.set(newUiccApplication.getIccRecords());
                registerForIccRecordEvents();
                this.mIccPhoneBookIntManager.updateIccRecords((IccRecords) this.mIccRecords.get());
            }
        }
    }

    private boolean isCdmaWithoutLteCard() {
        int iccFamily = MtkTelephonyManagerEx.getDefault().getIccAppFamily(getPhoneId());
        if (iccFamily == 2) {
            return DBG;
        }
        return false;
    }

    public void triggerModeSwitchByEcc(int mode, Message response) {
        this.mMtkCi.triggerModeSwitchByEcc(mode, response);
    }

    public String getLocatedPlmn() {
        return this.mMtkSST.getLocatedPlmn();
    }

    public void selectNetworkManually(OperatorInfo network, boolean persistSelection, Message response) {
        if (MtkRIL.showRat) {
            Rlog.d(LOG_TAG, "MTK GSMPhone selectNetworkManuallyWithAct:" + network);
            if (network.getOperatorAlphaLong() != null && network.getOperatorAlphaLong().endsWith(GSM_INDICATOR)) {
                network = new OperatorInfo(network.getOperatorAlphaLong(), network.getOperatorAlphaShort(), network.getOperatorNumeric(), 1);
            } else if (network.getOperatorAlphaLong() != null && network.getOperatorAlphaLong().endsWith(UTRAN_INDICATOR)) {
                network = new OperatorInfo(network.getOperatorAlphaLong(), network.getOperatorAlphaShort(), network.getOperatorNumeric(), 2);
            } else if (network.getOperatorAlphaLong() != null && network.getOperatorAlphaLong().endsWith(LTE_INDICATOR)) {
                network = new OperatorInfo(network.getOperatorAlphaLong(), network.getOperatorAlphaShort(), network.getOperatorNumeric(), 3);
            } else if (network.getOperatorAlphaLong() != null && network.getOperatorAlphaLong().endsWith(NR_INDICATOR)) {
                network = new OperatorInfo(network.getOperatorAlphaLong(), network.getOperatorAlphaShort(), network.getOperatorNumeric(), 6);
            }
        }
        super.selectNetworkManually(network, persistSelection, response);
        if (persistSelection) {
            Phone.NetworkSelectMessage nsm = new Phone.NetworkSelectMessage();
            nsm.message = response;
            nsm.operatorNumeric = network.getOperatorNumeric();
            nsm.operatorAlphaLong = network.getOperatorAlphaLong();
            nsm.operatorAlphaShort = network.getOperatorAlphaShort();
            updateManualNetworkSelection(nsm);
        }
    }

    public void setNetworkSelectionModeSemiAutomatic(OperatorInfo network, Message response) {
        Phone.NetworkSelectMessage nsm = new Phone.NetworkSelectMessage();
        nsm.message = response;
        nsm.operatorNumeric = "";
        nsm.operatorAlphaLong = "";
        nsm.operatorAlphaShort = "";
        Message msg = obtainMessage(17, nsm);
        Rlog.d(LOG_TAG, "MTK GSMPhone setNetworkSelectionModeSemiAutomatic:" + network);
        String actype = "0";
        if (network.getOperatorAlphaLong() != null && network.getOperatorAlphaLong().endsWith(UTRAN_INDICATOR)) {
            actype = ACT_TYPE_UTRAN;
        } else if (network.getOperatorAlphaLong() != null && network.getOperatorAlphaLong().endsWith(LTE_INDICATOR)) {
            actype = ACT_TYPE_LTE;
        }
        this.mMtkCi.setNetworkSelectionModeManualWithAct(network.getOperatorNumeric(), actype, 1, msg);
    }

    public void getAvailableNetworks(Message response) {
        if (!MtkRIL.showRat) {
            this.mCi.getAvailableNetworks(response);
        } else {
            this.mMtkCi.getAvailableNetworksWithAct(response);
        }
    }

    public synchronized void cancelAvailableNetworks(Message response) {
        Rlog.d(LOG_TAG, "cancelAvailableNetworks");
        this.mMtkCi.cancelAvailableNetworks(response);
    }

    public void getFemtoCellList(Message response) {
        Rlog.d(LOG_TAG, "getFemtoCellList()");
        this.mMtkCi.getFemtoCellList(response);
    }

    public void abortFemtoCellList(Message response) {
        Rlog.d(LOG_TAG, "abortFemtoCellList()");
        this.mMtkCi.abortFemtoCellList(response);
    }

    public void selectFemtoCell(FemtoCellInfo femtocell, Message response) {
        Rlog.d(LOG_TAG, "selectFemtoCell(): " + femtocell);
        this.mMtkCi.selectFemtoCell(femtocell, response);
    }

    public void queryFemtoCellSystemSelectionMode(Message response) {
        Rlog.d(LOG_TAG, "queryFemtoCellSystemSelectionMode()");
        this.mMtkCi.queryFemtoCellSystemSelectionMode(response);
    }

    public void setFemtoCellSystemSelectionMode(int mode, Message response) {
        Rlog.d(LOG_TAG, "setFemtoCellSystemSelectionMode(), mode=" + mode);
        this.mMtkCi.setFemtoCellSystemSelectionMode(mode, response);
    }

    public String getMvnoPattern(String type) {
        String pattern = "";
        synchronized (this.mLock) {
            if (this.mIccRecords.get() != null) {
                if (type.equals("spn")) {
                    pattern = ((MtkSIMRecords) this.mIccRecords.get()).getSpNameInEfSpn();
                } else if (type.equals("imsi")) {
                    pattern = ((MtkSIMRecords) this.mIccRecords.get()).isOperatorMvnoForImsi();
                } else if (type.equals("pnn")) {
                    pattern = ((MtkSIMRecords) this.mIccRecords.get()).isOperatorMvnoForEfPnn();
                } else if (type.equals(MtkPhoneConstants.MVNO_TYPE_GID)) {
                    pattern = ((IccRecords) this.mIccRecords.get()).getGid1();
                } else {
                    Rlog.d(LOG_TAG, "getMvnoPattern: Wrong type = " + type);
                }
            }
        }
        return pattern;
    }

    public String getMvnoMatchType() {
        String type = "";
        synchronized (this.mLock) {
            if (this.mIccRecords.get() != null) {
                type = ((MtkSIMRecords) this.mIccRecords.get()).getMvnoMatchType();
            }
            Rlog.d(LOG_TAG, "getMvnoMatchType: Type = " + type);
        }
        return type;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.CallStateException */
    public void hangupAll() throws CallStateException {
        if (!hasCalling()) {
            return;
        }
        ((MtkGsmCdmaCallTracker) this.mCT).hangupAll();
    }

    public Call getCSRingingCall() {
        if (!hasCalling()) {
            return null;
        }
        return this.mCT.mRingingCall;
    }

    boolean isInCSCall() {
        if (!hasCalling()) {
            return false;
        }
        Call.State foregroundCallState = getForegroundCall().getState();
        Call.State backgroundCallState = getBackgroundCall().getState();
        Call.State ringingCallState = getCSRingingCall().getState();
        if (foregroundCallState.isAlive() || backgroundCallState.isAlive() || ringingCallState.isAlive()) {
            return DBG;
        }
        return false;
    }

    public void registerForCipherIndication(Handler h, int what, Object obj) {
        this.mCipherIndicationRegistrants.addUnique(h, what, obj);
    }

    public void unregisterForCipherIndication(Handler h) {
        this.mCipherIndicationRegistrants.remove(h);
    }

    public void handleMessage(Message msg) {
        MtkSuppServHelper ssHelper;
        String errorMsg;
        MtkSuppServHelper ssHelper2;
        String errorMsg2;
        MtkSuppServHelper ssHelper3;
        String errorMsg3;
        MtkSuppServHelper ssHelper4;
        String errorMsg4;
        MtkSuppServHelper ssHelper5;
        String errorMsg5;
        MtkSuppServHelper ssHelper6;
        String errorMsg6;
        boolean exception = false;
        switch (msg.what) {
            case 12:
                AsyncResult ar = (AsyncResult) msg.obj;
                IccRecords r = (IccRecords) this.mIccRecords.get();
                GsmCdmaPhone.Cfu cfu = (GsmCdmaPhone.Cfu) ar.userObj;
                if (ar.exception == null && r != null) {
                    if ((cfu.mServiceClass & 1) != 0) {
                        exception = msg.arg1 == 1;
                        setVoiceCallForwardingFlag(1, exception, cfu.mSetCfNumber);
                    }
                } else if (ar.exception != null) {
                    CommandException cmdException = ar.exception;
                    CommandException.Error err = cmdException.getCommandError();
                    if (isUtErrorHasMsg(err) && cmdException.getMessage() != null && cmdException.getMessage().isEmpty() && (ssHelper = MtkSuppServManager.getSuppServHelper(getPhoneId())) != null && (errorMsg = ssHelper.getXCAPErrorMessageFromSysProp(err)) != null && !errorMsg.isEmpty()) {
                        Rlog.d(LOG_TAG, "Create OEM error = " + err + ", msg =" + errorMsg);
                        ar.exception = new CommandException(err, errorMsg);
                    }
                }
                if (cfu.mOnComplete != null) {
                    AsyncResult.forMessage(cfu.mOnComplete, ar.result, ar.exception);
                    cfu.mOnComplete.sendToTarget();
                    return;
                }
                return;
            case 13:
                Rlog.d(LOG_TAG, "mPhoneId= " + this.mPhoneId + "subId=" + getSubId());
                AsyncResult ar2 = (AsyncResult) msg.obj;
                if (ar2.exception == null) {
                    handleCfuQueryResult((CallForwardInfo[]) ar2.result);
                } else {
                    CommandException cmdException2 = ar2.exception;
                    CommandException.Error err2 = cmdException2.getCommandError();
                    if (isUtErrorHasMsg(err2) && cmdException2.getMessage() != null && cmdException2.getMessage().isEmpty() && (ssHelper2 = MtkSuppServManager.getSuppServHelper(getPhoneId())) != null && (errorMsg2 = ssHelper2.getXCAPErrorMessageFromSysProp(err2)) != null && !errorMsg2.isEmpty()) {
                        Rlog.d(LOG_TAG, "Create OEM error = " + err2 + ", msg =" + errorMsg2);
                        ar2.exception = new CommandException(err2, errorMsg2);
                    }
                }
                Message onComplete = (Message) ar2.userObj;
                if (onComplete != null) {
                    AsyncResult.forMessage(onComplete, ar2.result, ar2.exception);
                    onComplete.sendToTarget();
                    return;
                }
                return;
            case 16:
                super.handleMessage(msg);
                AsyncResult ar3 = (AsyncResult) msg.obj;
                boolean restoreSelection = this.mContext.getResources().getBoolean(17892131) ^ DBG;
                if (ar3 != null && ar3.exception != null) {
                    exception = true;
                }
                Rlog.d(LOG_TAG, "EVENT_SET_NETWORK_MANUAL_COMPLETE, restoreSelection=" + restoreSelection + ", disable_auto_return_rplmn=" + DBG + ", exception=" + exception);
                if (!restoreSelection && exception && 1 == 0) {
                    clearSavedNetworkSelection();
                    this.mCi.setNetworkSelectionModeAutomatic((Message) null);
                    return;
                }
                return;
            case 18:
                Rlog.d(LOG_TAG, "EVENT_SET_CLIR_COMPLETE");
                AsyncResult ar4 = (AsyncResult) msg.obj;
                if (ar4.exception == null) {
                    saveClirSetting(msg.arg1);
                }
                if (ar4.exception != null && (ar4.exception instanceof CommandException)) {
                    CommandException cmdException3 = ar4.exception;
                    CommandException.Error err3 = cmdException3.getCommandError();
                    Rlog.d(LOG_TAG, "EVENT_SET_CLIR_COMPLETE: cmdException error:" + err3);
                    if (cmdException3 != null) {
                        if ((isOp(MtkOperatorUtils.OPID.OP01) || isOp(MtkOperatorUtils.OPID.OP02)) && isUtError(cmdException3.getCommandError())) {
                            Rlog.d(LOG_TAG, "return REQUEST_NOT_SUPPORTED");
                            ar4.exception = new CommandException(CommandException.Error.REQUEST_NOT_SUPPORTED);
                        } else if (isUtErrorHasMsg(err3)) {
                            if (cmdException3.getMessage() != null && cmdException3.getMessage().isEmpty() && (ssHelper3 = MtkSuppServManager.getSuppServHelper(getPhoneId())) != null && (errorMsg3 = ssHelper3.getXCAPErrorMessageFromSysProp(err3)) != null && !errorMsg3.isEmpty()) {
                                Rlog.d(LOG_TAG, "Create OEM error = " + err3 + ", msg =" + errorMsg3);
                                ar4.exception = new CommandException(err3, errorMsg3);
                            }
                        } else {
                            Rlog.d(LOG_TAG, "return Original Error");
                        }
                    }
                }
                Message onComplete2 = (Message) ar4.userObj;
                if (onComplete2 != null) {
                    AsyncResult.forMessage(onComplete2, ar4.result, ar4.exception);
                    onComplete2.sendToTarget();
                    return;
                }
                return;
            case 30:
                AsyncResult ar5 = (AsyncResult) msg.obj;
                Rlog.d(LOG_TAG, "EVENT_ICC_CHANGED, phone id = " + getPhoneId() + ", result = " + ((Integer) ar5.result));
                if (ar5.userObj != null || (ar5.result != null && getPhoneId() == ((Integer) ar5.result).intValue())) {
                    super.handleMessage(msg);
                    return;
                }
                return;
            case 109:
                Rlog.d(LOG_TAG, "mPhoneId = " + this.mPhoneId + ", subId = " + getSubId());
                AsyncResult ar6 = (AsyncResult) msg.obj;
                Rlog.d(LOG_TAG, "[EVENT_GET_CALL_FORWARD_TIME_SLOT_DONE]ar.exception = " + ar6.exception);
                if (ar6.exception == null) {
                    handleCfuInTimeSlotQueryResult((MtkCallForwardInfo[]) ar6.result);
                }
                Rlog.d(LOG_TAG, "[EVENT_GET_CALL_FORWARD_TIME_SLOT_DONE]msg.arg1 = " + msg.arg1);
                if (ar6.exception != null && (ar6.exception instanceof CommandException)) {
                    CommandException cmdException4 = ar6.exception;
                    Rlog.d(LOG_TAG, "[EVENT_GET_CALL_FORWARD_TIME_SLOT_DONE] cmdException error:" + cmdException4.getCommandError());
                    if (msg.arg1 == 1 && cmdException4 != null && cmdException4.getCommandError() == CommandException.Error.REQUEST_NOT_SUPPORTED && this.mSST != null && this.mSST.mSS != null && this.mSST.mSS.getState() == 0) {
                        getCallForwardingOption(0, obtainMessage(13));
                    }
                    if (cmdException4 != null && cmdException4.getCommandError() == CommandException.Error.OEM_ERROR_2) {
                        Rlog.d(LOG_TAG, "return REQUEST_NOT_SUPPORTED");
                        ar6.exception = new CommandException(CommandException.Error.REQUEST_NOT_SUPPORTED);
                    }
                }
                Message onComplete3 = (Message) ar6.userObj;
                if (onComplete3 != null) {
                    AsyncResult.forMessage(onComplete3, ar6.result, ar6.exception);
                    onComplete3.sendToTarget();
                    return;
                }
                return;
            case 110:
                AsyncResult ar7 = (AsyncResult) msg.obj;
                IccRecords records = (IccRecords) this.mIccRecords.get();
                CfuEx cfuEx = (CfuEx) ar7.userObj;
                if (ar7.exception == null && records != null) {
                    exception = msg.arg1 == 1;
                    records.setVoiceCallForwardingFlag(1, exception, cfuEx.mSetCfNumber);
                    saveTimeSlot(cfuEx.mSetTimeSlot);
                }
                if (cfuEx.mOnComplete != null) {
                    AsyncResult.forMessage(cfuEx.mOnComplete, ar7.result, ar7.exception);
                    cfuEx.mOnComplete.sendToTarget();
                    return;
                }
                return;
            case EVENT_UNSOL_RADIO_CAPABILITY_CHANGED /* 111 */:
                AsyncResult ar8 = (AsyncResult) msg.obj;
                RadioCapability rc_unsol = (RadioCapability) ar8.result;
                if (ar8.exception != null) {
                    Rlog.d(LOG_TAG, "RIL_UNSOL_RADIO_CAPABILITY fail, don't change capability");
                } else {
                    radioCapabilityUpdated(rc_unsol, false);
                }
                Rlog.d(LOG_TAG, "EVENT_UNSOL_RADIO_CAPABILITY_CHANGED: rc: " + rc_unsol);
                return;
            case 1001:
                Rlog.d(LOG_TAG, "handle EVENT_GET_APC_INFO");
                AsyncResult ar9 = (AsyncResult) msg.obj;
                PseudoCellInfoResult result = (PseudoCellInfoResult) ar9.userObj;
                if (result == null) {
                    Rlog.e(LOG_TAG, "EVENT_GET_APC_INFO: result return null");
                    return;
                }
                synchronized (result.lockObj) {
                    if (ar9.exception != null) {
                        Rlog.d(LOG_TAG, "EVENT_GET_APC_INFO: error ret null, e=" + ar9.exception);
                        result.infos = null;
                    } else {
                        int[] msgs = (int[]) ar9.result;
                        result.infos = new PseudoCellInfo(msgs);
                    }
                    result.lockObj.notify();
                    break;
                }
                return;
            case 1002:
                logd("Event EVENT_SSN_EX Received");
                MtkSuppServiceNotification not = (MtkSuppServiceNotification) ((AsyncResult) msg.obj).result;
                if (not.notificationType == 1) {
                    if (not.code == 0 || not.code >= 11) {
                        AsyncResult ar10 = new AsyncResult((Object) null, not, (Throwable) null);
                        if (this.mSsnRegistrants.size() == 0) {
                            this.mCachedSsn = ar10;
                        }
                        this.mSsnRegistrants.notifyRegistrants(ar10);
                        return;
                    }
                    logd("Unexpected SSN_EX code:" + not.code);
                    return;
                }
                return;
            case 1003:
                Rlog.d(LOG_TAG, "EVENT_CRSS_IND do not update number");
                return;
            case 1004:
                if (this.mCallbackLatch != null) {
                    this.mCallbackLatch.countDown();
                }
                Rlog.d(LOG_TAG, "EVENT_SET_SS_PROPERTY done");
                return;
            case 1005:
                AsyncResult ar11 = (AsyncResult) msg.obj;
                if (ar11 != null) {
                    byte[] response = (byte[]) ar11.result;
                    if (this.mOemIndRegistrants != null && response != null) {
                        this.mOemIndRegistrants.notifyRegistrants(new AsyncResult((Object) null, new String(response), (Throwable) null));
                        return;
                    }
                    return;
                }
                return;
            case 2004:
                Rlog.d(LOG_TAG, "EVENT_GET_CLIR_COMPLETE");
                AsyncResult ar12 = (AsyncResult) msg.obj;
                if (ar12.exception != null && (ar12.exception instanceof CommandException)) {
                    CommandException cmdException5 = ar12.exception;
                    CommandException.Error err4 = cmdException5.getCommandError();
                    Rlog.d(LOG_TAG, "EVENT_GET_CLIR_COMPLETE: cmdException error:" + err4);
                    if (cmdException5 != null) {
                        if (isOp(MtkOperatorUtils.OPID.OP01) || isOp(MtkOperatorUtils.OPID.OP02)) {
                            if (isUtError(cmdException5.getCommandError())) {
                                Rlog.d(LOG_TAG, "return REQUEST_NOT_SUPPORTED");
                                ar12.exception = new CommandException(CommandException.Error.REQUEST_NOT_SUPPORTED);
                            } else {
                                Rlog.d(LOG_TAG, "return Original Error");
                            }
                        } else if (isUtErrorHasMsg(err4) && cmdException5.getMessage() != null && cmdException5.getMessage().isEmpty() && (ssHelper4 = MtkSuppServManager.getSuppServHelper(getPhoneId())) != null && (errorMsg4 = ssHelper4.getXCAPErrorMessageFromSysProp(err4)) != null && !errorMsg4.isEmpty()) {
                            Rlog.d(LOG_TAG, "Create OEM error = " + err4 + ", msg =" + errorMsg4);
                            ar12.exception = new CommandException(err4, errorMsg4);
                        }
                    }
                }
                Message onComplete4 = (Message) ar12.userObj;
                if (onComplete4 != null) {
                    AsyncResult.forMessage(onComplete4, ar12.result, ar12.exception);
                    onComplete4.sendToTarget();
                    return;
                }
                return;
            case 2005:
                Rlog.d(LOG_TAG, "EVENT_SET_CALL_BARRING_COMPLETE");
                AsyncResult ar13 = (AsyncResult) msg.obj;
                if (ar13.exception != null && (ar13.exception instanceof CommandException)) {
                    CommandException cmdException6 = ar13.exception;
                    CommandException.Error err5 = cmdException6.getCommandError();
                    Rlog.d(LOG_TAG, "EVENT_SET_CALL_BARRING_COMPLETE: cmdException error:" + err5);
                    if (cmdException6 != null) {
                        if (isOp(MtkOperatorUtils.OPID.OP01)) {
                            if (isUtError(cmdException6.getCommandError())) {
                                Rlog.d(LOG_TAG, "return REQUEST_NOT_SUPPORTED");
                                ar13.exception = new CommandException(CommandException.Error.REQUEST_NOT_SUPPORTED);
                            } else {
                                Rlog.d(LOG_TAG, "return Original Error");
                            }
                        } else if (isUtErrorHasMsg(err5) && cmdException6.getMessage() != null && cmdException6.getMessage().isEmpty() && (ssHelper5 = MtkSuppServManager.getSuppServHelper(getPhoneId())) != null && (errorMsg5 = ssHelper5.getXCAPErrorMessageFromSysProp(err5)) != null && !errorMsg5.isEmpty()) {
                            Rlog.d(LOG_TAG, "Create OEM error = " + err5 + ", msg =" + errorMsg5);
                            ar13.exception = new CommandException(err5, errorMsg5);
                        }
                    }
                }
                Message onComplete5 = (Message) ar13.userObj;
                if (onComplete5 != null) {
                    AsyncResult.forMessage(onComplete5, ar13.result, ar13.exception);
                    onComplete5.sendToTarget();
                    return;
                }
                return;
            case 2006:
                Rlog.d(LOG_TAG, "EVENT_GET_CALL_BARRING_COMPLETE");
                AsyncResult ar14 = (AsyncResult) msg.obj;
                if (ar14.exception != null && (ar14.exception instanceof CommandException)) {
                    CommandException cmdException7 = ar14.exception;
                    CommandException.Error err6 = cmdException7.getCommandError();
                    Rlog.d(LOG_TAG, "EVENT_GET_CALL_BARRING_COMPLETE: cmdException error:" + err6);
                    if (cmdException7 != null) {
                        if (isOp(MtkOperatorUtils.OPID.OP01) || isOp(MtkOperatorUtils.OPID.OP09)) {
                            if (isUtError(cmdException7.getCommandError())) {
                                Rlog.d(LOG_TAG, "return REQUEST_NOT_SUPPORTED");
                                ar14.exception = new CommandException(CommandException.Error.REQUEST_NOT_SUPPORTED);
                            } else {
                                Rlog.d(LOG_TAG, "return Original Error");
                            }
                        } else if (isUtErrorHasMsg(err6) && cmdException7.getMessage() != null && cmdException7.getMessage().isEmpty() && (ssHelper6 = MtkSuppServManager.getSuppServHelper(getPhoneId())) != null && (errorMsg6 = ssHelper6.getXCAPErrorMessageFromSysProp(err6)) != null && !errorMsg6.isEmpty()) {
                            Rlog.d(LOG_TAG, "Create OEM error = " + err6 + ", msg =" + errorMsg6);
                            ar14.exception = new CommandException(err6, errorMsg6);
                        }
                    }
                }
                Message onComplete6 = (Message) ar14.userObj;
                if (onComplete6 != null) {
                    AsyncResult.forMessage(onComplete6, ar14.result, ar14.exception);
                    onComplete6.sendToTarget();
                    return;
                }
                return;
            case 3000:
                logd("Event EVENT_ESSENTIAL_SIM_RECORDS_LOADED Received");
                handleEssentialRecordsLoaded();
                return;
            default:
                super.handleMessage(msg);
                return;
        }
    }

    public void setApcMode(int apcMode, boolean reportOn, int reportInterval) {
        this.mMtkCi.setApcMode(apcMode, reportOn, reportInterval, null);
    }

    private class PseudoCellInfoResult {
        PseudoCellInfo infos;
        Object lockObj;

        private PseudoCellInfoResult(MtkGsmCdmaPhone mtkGsmCdmaPhone) {
            this.infos = null;
            this.lockObj = new Object();
        }
    }

    public PseudoCellInfo getApcInfo() {
        PseudoCellInfoResult result = new PseudoCellInfoResult();
        synchronized (result.lockObj) {
            result.infos = null;
            this.mMtkCi.getApcInfo(obtainMessage(1001, result));
            try {
                result.lockObj.wait(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        synchronized (result.lockObj) {
            if (result.infos != null) {
                Rlog.d(LOG_TAG, "getApcInfo return: list.size = " + result.infos.toString());
                return result.infos;
            }
            Rlog.d(LOG_TAG, "getApcInfo return null");
            return null;
        }
    }

    public void registerForCrssSuppServiceNotification(Handler h, int what, Object obj) {
        this.mCallRelatedSuppSvcRegistrants.addUnique(h, what, obj);
        if (this.mCachedCrssn != null) {
            this.mCallRelatedSuppSvcRegistrants.notifyRegistrants(this.mCachedCrssn);
            this.mCachedCrssn = null;
        }
    }

    public void unregisterForCrssSuppServiceNotification(Handler h) {
        this.mCallRelatedSuppSvcRegistrants.remove(h);
        this.mCachedCrssn = null;
    }

    public void registerForSuppServiceNotification(Handler h, int what, Object obj) {
        super.registerForSuppServiceNotification(h, what, obj);
        if (this.mCachedSsn != null) {
            this.mSsnRegistrants.notifyRegistrants(this.mCachedSsn);
            this.mCachedSsn = null;
        }
    }

    public void unregisterForSuppServiceNotification(Handler h) {
        super.unregisterForSuppServiceNotification(h);
        this.mCachedSsn = null;
    }

    private boolean handleUdubIncallSupplementaryService(String dialString) {
        if (!hasCalling() || dialString.length() > 1) {
            return false;
        }
        if (getRingingCall().getState() != Call.State.IDLE || getBackgroundCall().getState() != Call.State.IDLE) {
            Rlog.d(LOG_TAG, "MmiCode 0: hangupWaitingOrBackground");
            this.mCT.hangupWaitingOrBackground();
        }
        return DBG;
    }

    public void queryPhbStorageInfo(int type, Message response) {
        this.mMtkCi.queryPhbStorageInfo(type, response);
    }

    public void registerForNetworkInfo(Handler h, int what, Object obj) {
        this.mMtkCi.registerForNetworkInfo(h, what, obj);
    }

    public void unregisterForNetworkInfo(Handler h) {
        this.mMtkCi.unregisterForNetworkInfo(h);
    }

    public void getPolCapability(Message onComplete) {
        this.mMtkCi.getPOLCapability(onComplete);
    }

    public void getPol(Message onComplete) {
        this.mMtkCi.getCurrentPOLList(onComplete);
    }

    public void setPolEntry(NetworkInfoWithAcT networkWithAct, Message onComplete) {
        this.mMtkCi.setPOLEntry(networkWithAct.getPriority(), networkWithAct.getOperatorNumeric(), networkWithAct.getAccessTechnology(), onComplete);
    }

    public List<? extends MmiCode> getPendingMmiCodes() {
        Rlog.d(LOG_TAG, "getPendingMmiCodes");
        dumpPendingMmi();
        ImsPhone imsPhone = this.mImsPhone;
        ArrayList<MmiCode> imsphonePendingMMIs = new ArrayList<>();
        if (imsPhone != null && imsPhone.getServiceState().getState() == 0) {
            List<ImsPhoneMmiCode> imsMMIs = imsPhone.getPendingMmiCodes();
            for (ImsPhoneMmiCode mmi : imsMMIs) {
                imsphonePendingMMIs.add(mmi);
            }
        }
        ArrayList<MmiCode> allPendingMMIs = new ArrayList<>(this.mPendingMMIs);
        allPendingMMIs.addAll(imsphonePendingMMIs);
        Rlog.d(LOG_TAG, "allPendingMMIs.size() = " + allPendingMMIs.size());
        int s = allPendingMMIs.size();
        for (int i = 0; i < s; i++) {
            Rlog.d(LOG_TAG, "dump allPendingMMIs: " + allPendingMMIs.get(i));
        }
        return allPendingMMIs;
    }

    @Deprecated
    private static class CfuEx {
        final Message mOnComplete;
        final String mSetCfNumber;
        final long[] mSetTimeSlot;

        CfuEx(String cfNumber, long[] cfTimeSlot, Message onComplete) {
            this.mSetCfNumber = cfNumber;
            this.mSetTimeSlot = cfTimeSlot;
            this.mOnComplete = onComplete;
        }
    }

    @Deprecated
    public void saveTimeSlot(long[] timeSlot) {
        String timeSlotKey = CFU_TIME_SLOT + this.mPhoneId;
        String timeSlotString = "";
        if (timeSlot != null && timeSlot.length == 2) {
            timeSlotString = Long.toString(timeSlot[0]) + "," + Long.toString(timeSlot[1]);
        }
        SystemProperties.set(timeSlotKey, timeSlotString);
        Rlog.d(LOG_TAG, "timeSlotString = " + timeSlotString);
    }

    @Deprecated
    public long[] getTimeSlot() {
        String timeSlotKey = CFU_TIME_SLOT + this.mPhoneId;
        String timeSlotString = SystemProperties.get(timeSlotKey, "");
        long[] timeSlot = null;
        if (timeSlotString != null && !timeSlotString.equals("")) {
            String[] timeArray = timeSlotString.split(",");
            if (timeArray.length == 2) {
                timeSlot = new long[2];
                for (int i = 0; i < 2; i++) {
                    timeSlot[i] = Long.parseLong(timeArray[i]);
                    Calendar calenar = Calendar.getInstance(TimeZone.getDefault());
                    calenar.setTimeInMillis(timeSlot[i]);
                    int hour = calenar.get(11);
                    int min = calenar.get(12);
                    Calendar calenar2 = Calendar.getInstance(TimeZone.getDefault());
                    calenar2.set(11, hour);
                    calenar2.set(12, min);
                    timeSlot[i] = calenar2.getTimeInMillis();
                }
            }
        }
        Rlog.d(LOG_TAG, "timeSlot = " + Arrays.toString(timeSlot));
        return timeSlot;
    }

    @Deprecated
    public void getCallForwardInTimeSlot(int commandInterfaceCFReason, Message onComplete) {
        ImsPhone imsPhone = this.mImsPhone;
        Rlog.d(LOG_TAG, "getCallForwardInTimeSlot enter, CFReason:" + commandInterfaceCFReason);
        if (imsPhone != null && (imsPhone.getServiceState().getState() == 0 || imsPhone.isUtEnabled())) {
            ((MtkImsPhone) imsPhone).getCallForwardInTimeSlot(commandInterfaceCFReason, onComplete);
        } else if (commandInterfaceCFReason == 0) {
            Rlog.d(LOG_TAG, "requesting call forwarding in time slot query.");
            Message resp = obtainMessage(109, onComplete);
            this.mMtkCi.queryCallForwardInTimeSlotStatus(commandInterfaceCFReason, 0, resp);
        }
    }

    @Deprecated
    public void setCallForwardInTimeSlot(int i, int i2, String str, int i3, long[] jArr, Message message) {
        ImsPhone imsPhone = this.mImsPhone;
        Rlog.d(LOG_TAG, "setCallForwardInTimeSlot enter, CFReason:" + i2 + ", CFAction:" + i + ", dialingNumber:" + pii(str) + ", timerSeconds:" + i3);
        if (imsPhone != null && (imsPhone.getServiceState().getState() == 0 || imsPhone.isUtEnabled())) {
            ((MtkImsPhone) imsPhone).setCallForwardInTimeSlot(i, i2, str, i3, jArr, message);
        } else if (isValidCommandInterfaceCFAction(i) && i2 == 0) {
            this.mMtkCi.setCallForwardInTimeSlot(i, i2, 1, str, i3, jArr, obtainMessage(110, isCfEnable(i) ? 1 : 0, 0, new CfuEx(str, jArr, message)));
        }
    }

    private PhoneAccountHandle subscriptionIdToPhoneAccountHandle(int subId) {
        TelecomManager telecomManager = (TelecomManager) this.mContext.getSystemService(TelecomManager.class);
        TelephonyManager telephonyManager = TelephonyManager.from(this.mContext);
        Iterator<PhoneAccountHandle> phoneAccounts = telecomManager.getCallCapablePhoneAccounts(DBG).listIterator();
        while (phoneAccounts.hasNext()) {
            PhoneAccountHandle phoneAccountHandle = phoneAccounts.next();
            PhoneAccount phoneAccount = telecomManager.getPhoneAccount(phoneAccountHandle);
            if (phoneAccount != null && subId == telephonyManager.getSubIdForPhoneAccount(phoneAccount)) {
                return phoneAccountHandle;
            }
        }
        return null;
    }

    @Deprecated
    public void disableAllCallForwardingOptions() {
        Rlog.i(LOG_TAG, "disableAllCallForwardingOptions is deprecated");
    }

    @Deprecated
    private void handleCfuInTimeSlotQueryResult(MtkCallForwardInfo[] infos) {
        IccRecords r = (IccRecords) this.mIccRecords.get();
        if (r != null) {
            if (infos == null || infos.length == 0) {
                setVoiceCallForwardingFlag(1, false, null);
                return;
            }
            int s = infos.length;
            for (int i = 0; i < s; i++) {
                if ((infos[i].serviceClass & 1) != 0) {
                    setVoiceCallForwardingFlag(1, infos[i].status == 1, infos[i].number);
                    saveTimeSlot(infos[i].timeSlot);
                    return;
                }
            }
        }
    }

    public void setOutgoingCallerIdDisplay(int commandInterfaceCLIRMode, Message onComplete) {
        Rlog.d(LOG_TAG, "setOutgoingCallerIdDisplay enter, CLIRmode:" + commandInterfaceCLIRMode);
        Message resp = obtainMessage(18, commandInterfaceCLIRMode, 0, onComplete);
        super.setOutgoingCallerIdDisplay(commandInterfaceCLIRMode, resp);
    }

    public void getCallBarring(String facility, String password, Message onComplete, int serviceClass) {
        Rlog.d(LOG_TAG, "getCallBarringInternal enter, facility:" + facility + ", serviceClass:" + serviceClass + ", password...");
        Message resp = obtainMessage(2006, onComplete);
        super.getCallBarring(facility, password, resp, serviceClass);
    }

    public void setCallBarring(String facility, boolean lockState, String password, Message onComplete, int serviceClass) {
        Rlog.d(LOG_TAG, "setCallBarring enter, facility:" + facility + ", serviceClass:" + serviceClass + ", password..., lockState:" + lockState);
        Message resp = obtainMessage(2005, onComplete);
        super.setCallBarring(facility, lockState, password, resp, serviceClass);
    }

    public void dumpPendingMmi() {
        int size = this.mPendingMMIs.size();
        if (size == 0) {
            Rlog.d(LOG_TAG, "dumpPendingMmi: none");
            return;
        }
        for (int i = 0; i < size; i++) {
            Rlog.d(LOG_TAG, "dumpPendingMmi: " + this.mPendingMMIs.get(i));
        }
    }

    public void setServiceClass(int serviceClass) {
        Rlog.d(LOG_TAG, "setServiceClass: " + serviceClass);
        SystemProperties.set("vendor.gsm.radio.ss.sc", String.valueOf(serviceClass));
    }

    private boolean isUsimCard() {
        if (!isOp(MtkOperatorUtils.OPID.OP09)) {
            boolean r = false;
            String iccCardType = PhoneFactory.getPhone(getPhoneId()).getIccCard().getIccCardType();
            if (iccCardType != null && iccCardType.equals("USIM")) {
                r = DBG;
            }
            Rlog.d(LOG_TAG, "isUsimCard: " + r + ", " + iccCardType);
            return r;
        }
        String[] values = null;
        int subId = MtkSubscriptionManager.getSubIdUsingPhoneId(getPhoneId());
        int slotId = SubscriptionManager.getSlotIndex(subId);
        if (slotId < 0 || slotId >= PROPERTY_RIL_FULL_UICC_TYPE.length) {
            return false;
        }
        String prop = SystemProperties.get(PROPERTY_RIL_FULL_UICC_TYPE[slotId], "");
        if (!prop.equals("") && prop.length() > 0) {
            values = prop.split(",");
        }
        Rlog.d(LOG_TAG, "isUsimCard PhoneId = " + getPhoneId() + " cardType = " + Arrays.toString(values));
        if (values == null) {
            return false;
        }
        for (String s : values) {
            if (s.equals("USIM")) {
                return DBG;
            }
        }
        return false;
    }

    private boolean isOp(MtkOperatorUtils.OPID id) {
        return MtkOperatorUtils.isOperator(getOperatorNumeric(), id);
    }

    private boolean isIccCardMncMccAvailable(int phoneId) {
        UiccController uiccCtl = UiccController.getInstance();
        IccRecords iccRecords = uiccCtl.getIccRecords(phoneId, 1);
        if (iccRecords != null) {
            String mccMnc = iccRecords.getOperatorNumeric();
            Rlog.d(LOG_TAG, "isIccCardMncMccAvailable(): mccMnc is " + mccMnc);
            if (mccMnc != null) {
                return DBG;
            }
            return false;
        }
        Rlog.d(LOG_TAG, "isIccCardMncMccAvailable(): false");
        return false;
    }

    private boolean isUtError(CommandException.Error error) {
        if (error == CommandException.Error.OEM_ERROR_25 || error == CommandException.Error.OEM_ERROR_3 || error == CommandException.Error.OEM_ERROR_4 || error == CommandException.Error.OEM_ERROR_6 || error == CommandException.Error.OEM_ERROR_24 || error == CommandException.Error.OEM_ERROR_23 || error == CommandException.Error.OEM_ERROR_22) {
            return DBG;
        }
        return false;
    }

    private boolean isUtErrorHasMsg(CommandException.Error error) {
        if (error == CommandException.Error.OEM_ERROR_25 || error == CommandException.Error.OEM_ERROR_6 || error == CommandException.Error.OEM_ERROR_24 || error == CommandException.Error.OEM_ERROR_23 || error == CommandException.Error.OEM_ERROR_22) {
            return DBG;
        }
        return false;
    }

    public boolean isSupportCFUTimeSlot() {
        boolean r = false;
        if (isOp(MtkOperatorUtils.OPID.OP01)) {
            r = DBG;
        }
        Rlog.d(LOG_TAG, "isSupportCFUTimeSlot: " + r);
        return r;
    }

    void sendErrorResponse(Message onComplete, CommandException.Error error) {
        Rlog.d(LOG_TAG, "sendErrorResponse" + error);
        if (onComplete != null) {
            AsyncResult.forMessage(onComplete, (Object) null, new CommandException(error));
            onComplete.sendToTarget();
        }
    }

    public boolean isIMSRegistered() {
        MtkSuppServHelper ssHelper = MtkSuppServManager.getSuppServHelper(getPhoneId());
        if (ssHelper != null) {
            return ssHelper.getIMSRegistered();
        }
        return false;
    }

    public int getCdmaSubscriptionActStatus() {
        return 0;
    }

    public void setRoamingEnable(int[] config, Message response) {
        Rlog.d(LOG_TAG, "set roaming enable");
        if (config == null || config.length != 6) {
            Rlog.d(LOG_TAG, "set roaming enable, invalid paramter");
            sendErrorResponse(response, CommandException.Error.INVALID_ARGUMENTS);
        } else {
            config[0] = this.mPhoneId;
            this.mMtkCi.setRoamingEnable(config, response);
        }
    }

    public void getRoamingEnable(Message response) {
        Rlog.d(LOG_TAG, "get roaming enable");
        this.mMtkCi.getRoamingEnable(this.mPhoneId, response);
    }

    @Deprecated
    public Message getCFCallbackMessage() {
        return obtainMessage(13);
    }

    @Deprecated
    public Message getCFTimeSlotCallbackMessage() {
        return obtainMessage(109);
    }

    public boolean isImsUseEnabled() {
        ImsManager imsManager = ImsManager.getInstance(this.mContext, this.mPhoneId);
        boolean isEnhanced4gLteModeSettingEnabledByUser = imsManager.isEnhanced4gLteModeSettingEnabledByUser();
        boolean isWfcEnabledByUser = DBG;
        boolean isNonTtyOrTtyOnVolteEnabled = DBG;
        if (!isEnhanced4gLteModeSettingEnabledByUser) {
            isWfcEnabledByUser = imsManager.isWfcEnabledByUser();
            isNonTtyOrTtyOnVolteEnabled = imsManager.isNonTtyOrTtyOnVolteEnabled();
        }
        boolean imsUseEnabled = (isEnhanced4gLteModeSettingEnabledByUser || (isWfcEnabledByUser && isNonTtyOrTtyOnVolteEnabled)) ? DBG : false;
        Rlog.d(LOG_TAG, "isImsUseEnabled() VolteEnableByUser: " + isEnhanced4gLteModeSettingEnabledByUser + ", WfcEnableByUser: " + isWfcEnabledByUser + ", isNonTtyOrTtyOnVolteEnabled: " + isNonTtyOrTtyOnVolteEnabled);
        return imsUseEnabled;
    }

    @Deprecated
    public void cleanCallForwardingIndicatorFromSharedPref() {
        setCallForwardingIndicatorInSharedPref(false);
    }

    public boolean shouldProcessSelfActivation() {
        int selfActivateState = getSelfActivationInstance().getSelfActivateState();
        Rlog.d(LOG_TAG, "shouldProcessSelfActivation() state: " + selfActivateState);
        if (selfActivateState == 2) {
            return DBG;
        }
        return false;
    }

    public boolean useImsForPCOChanged() {
        int pcoState = getSelfActivationInstance().getPCO520State();
        Rlog.d(LOG_TAG, "pcoState() state: " + pcoState);
        if (pcoState == 1) {
            return DBG;
        }
        return false;
    }

    protected boolean needResetPhbIntMgr() {
        return false;
    }

    public String getFullIccSerialNumber() {
        String iccId = super.getFullIccSerialNumber();
        if (iccId != null) {
            return iccId;
        }
        long identity = Binder.clearCallingIdentity();
        try {
            int subId = getSubId();
            SubscriptionInfo subInfo = SubscriptionManager.from(getContext()).getActiveSubscriptionInfo(subId);
            if (subInfo != null) {
                iccId = subInfo.getIccId();
            }
            return iccId;
        } finally {
            Binder.restoreCallingIdentity(identity);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void registerForIccRecordEvents() {
        IccRecords r = (IccRecords) this.mIccRecords.get();
        if (r != null) {
            logd("registerForEssentialRecordsLoaded");
            r.registerForEssentialRecordsLoaded(this, 3000, (Object) null);
        }
        super.registerForIccRecordEvents();
    }

    private void handleEssentialRecordsLoaded() {
        Rlog.d(LOG_TAG, "handleEssentialRecordsLoaded, mPhoneId: " + this.mPhoneId);
        resolveSubscriptionCarrierId("LOADED");
        CarrierConfigManager configManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        configManager.updateConfigForPhoneId(this.mPhoneId, SIM_STATE_ESSENTIAL_RECORDS_LOADED);
    }

    public void invokeOemRilRequestRaw(byte[] data, Message response) {
        this.mMtkCi.invokeOemRilRequestRaw(data, response);
    }

    public void setImsRegistrationState(boolean registered) {
        super.setImsRegistrationState(registered);
        if (this.mImsPhone != null && this.mImsPhone.isImsRegistered()) {
            NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService("notification");
            Rlog.d(LOG_TAG, "setImsRegistrationState: " + registered);
            notificationManager.cancel("wifi_calling", 1);
            MiuiImsControllerStub.get(this.mImsPhone.getPhoneId()).sendIMSRegistedStateBroadcast(this.mImsPhone.isVolteEnabled(), this);
        }
    }

    public void setDisable2G(boolean mode, Message result) {
        Rlog.d(LOG_TAG, "setDisable2G " + mode);
        this.mMtkCi.setDisable2G(mode, result);
    }

    public void getDisable2G(Message result) {
        Rlog.d(LOG_TAG, "getDisable2G");
        this.mMtkCi.getDisable2G(result);
    }

    public void invokeOemRilRequestStrings(String[] strings, Message response) {
        this.mMtkCi.invokeOemRilRequestStrings(strings, response);
    }

    public void setSuppServProperty(String name, String value) {
        Rlog.d(LOG_TAG, "setSuppServProperty, name = " + name + ", value = " + value);
        this.mCallbackLatch = new CountDownLatch(1);
        this.mMtkCi.setSuppServProperty(name, value, obtainMessage(1004));
        if (!isCallbackDone()) {
            Rlog.e(LOG_TAG, "waitForCallback: callback is not done!");
        }
    }

    private boolean isCallbackDone() {
        boolean isDone;
        try {
            isDone = this.mCallbackLatch.await(3000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            isDone = false;
        }
        Rlog.d(LOG_TAG, "waitForCallback: isDone=" + isDone);
        return isDone;
    }

    public PhoneConstants.State getState() {
        PhoneConstants.State state = super.getState();
        if (state == PhoneConstants.State.IDLE && this.mCT != null && ((MtkGsmCdmaCallTracker) this.mCT).getHandoverConnectionSize() > 0) {
            return PhoneConstants.State.OFFHOOK;
        }
        return state;
    }

    public String getLine1PhoneNumber() {
        IccRecords r = (IccRecords) this.mIccRecords.get();
        if (r != null) {
            return r.getMsisdnNumber();
        }
        return null;
    }

    public void notifyServiceStateChanged(ServiceState ss) {
        MtkServiceStateTracker mtkSST = (MtkServiceStateTracker) this.mSST;
        if (!this.enableFakeSS && mtkSST.once_pollState_done) {
            super.notifyServiceStateChanged(ss);
        } else {
            logd("notifyServiceStateChanged, skip");
        }
    }

    public void notifyMtkFakeServiceStateChanged(ServiceState ss) {
        if (ss == null) {
            this.enableFakeSS = false;
        } else {
            this.enableFakeSS = DBG;
            this.mNotifier.notifyMtkServiceState(this, ss);
        }
    }

    public void notifyMtkServiceStateChanged(ServiceState ss) {
        this.mNotifier.notifyMtkServiceState(this, ss);
        MtkDataNetworkController mtkDnc = (MtkDataNetworkController) getDataNetworkController();
        if (mtkDnc != null) {
            mtkDnc.notifyMtkServiceStateChanged(ss);
        }
    }

    public void notifyMtkSignalStrength(SignalStrength ss) {
        this.mNotifier.notifyMtkSignalStrength(this, ss);
    }

    public String getSubscriberId() {
        String subscriberId = null;
        DataNetworkController dnc = getDataNetworkController();
        if (dnc != null) {
            subscriberId = ((MtkDataProfileManager) dnc.getDataProfileManager()).getImsi();
        }
        if (TextUtils.isEmpty(subscriberId)) {
            String subscriberId2 = super.getSubscriberId();
            return subscriberId2;
        }
        return subscriberId;
    }

    public void iwlanSetRegisterCellularQualityReport(int qualityRegister, int type, int[] values, Message result) {
        this.mMtkCi.iwlanSetRegisterCellularQualityReport(qualityRegister, type, values, result);
    }

    public void getSuggestedPlmnList(int rat, int num, int timer, Message onCompleted) {
        this.mMtkCi.getSuggestedPlmnList(rat, num, timer, onCompleted);
    }

    protected void reapplyUiccAppsEnablementIfNeeded(int retries) {
        super.reapplyUiccAppsEnablementIfNeeded(retries);
        UiccSlot slot = this.mUiccController.getUiccSlotForPhone(this.mPhoneId);
        if (slot == null || slot.getCardState() != IccCardStatus.CardState.CARDSTATE_PRESENT || this.mUiccApplicationsEnabled == null) {
            if (slot == null) {
                loge("reapplyUiccAppsEnablementIfNeeded(), slot is null ,mUiccApplicationsEnabled = " + this.mUiccApplicationsEnabled);
                return;
            } else {
                loge("reapplyUiccAppsEnablementIfNeeded(), CardState=" + slot.getCardState() + " ,mUiccApplicationsEnabled = " + this.mUiccApplicationsEnabled);
                return;
            }
        }
        UiccCard uiccCard = slot.getUiccCard();
        if (uiccCard == null) {
            loge("reapplyUiccAppsEnablementIfNeeded(), uiccCard is null");
            return;
        }
        UiccPort uiccPort = uiccCard.getUiccPortForPhone(getPhoneId());
        if (uiccPort == null) {
            loge("reapplyUiccAppsEnablementIfNeeded(), uiccPort is null");
            return;
        }
        String iccId = uiccPort.getIccId();
        if (iccId == null) {
            loge("reapplyUiccAppsEnablementIfNeeded(), iccid is null");
            return;
        }
        SubscriptionInfo info = MtkSubscriptionManagerService.getInstance().getSubInfoForIccId(IccUtils.stripTrailingFs(iccId));
        boolean expectedValue = info == null ? DBG : info.areUiccApplicationsEnabled();
        logd("reapplyUiccAppsEnablementIfNeeded(), expectedValue =" + expectedValue + " ,mUiccApplicationsEnabled =" + this.mUiccApplicationsEnabled + " ,iccId=" + SubscriptionInfo.getPrintableId(iccId));
    }

    public void registerForOemIndication(Handler h, int what, Object obj) {
        this.mOemIndRegistrants.addUnique(h, what, obj);
    }

    public void unregisterForOemIndication(Handler h) {
        this.mOemIndRegistrants.remove(h);
    }

    protected void notifyImsPhoneHandoverStateChanged(Connection cn) {
        if (this.mImsPhone != null) {
            this.mImsPhone.notifyHandoverStateChanged(cn);
        }
    }

    public void getCallSubAddress(Message response) {
        this.mMtkCi.getCallSubAddress(response);
    }

    public void setCallSubAddress(boolean enable, Message response) {
        this.mMtkCi.setCallSubAddress(enable, response);
    }

    public void getEcholocateMetrics(int index, Message response) {
        Rlog.d(LOG_TAG, "getEcholocateMetrics()");
        this.mMtkCi.getEcholocateMetrics(index, response);
    }

    public void getIWlanRegistrationState(Message response) {
        this.mMtkCi.getIWlanRegistrationState(response);
    }

    protected boolean onCheckIfForceHandleInCallMmiByCS() {
        return isInCSCall();
    }

    protected boolean onHandleInCallMmi0(String dialString) {
        return handleUdubIncallSupplementaryService(dialString);
    }

    protected void onCheckEmergencyCall(boolean isEmergency) {
        MtkLocalPhoneNumberUtils.setIsEmergencyNumber(isEmergency);
    }

    protected boolean onSetUseImsForUt(boolean useImsForUt) {
        if (isInCSCall()) {
            return false;
        }
        return useImsForUt;
    }

    protected boolean onSetUseImsForCall(boolean useImsForCall) {
        if (isInCSCall()) {
            return false;
        }
        if (!useImsForCall) {
            CarrierConfigManager configManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
            boolean useImsFirstForCall = configManager.getConfigForSubId(getSubId()).getBoolean("mtk_use_ims_first_for_call_bool");
            logd("useImsFirstForCall = " + useImsFirstForCall);
            if (useImsFirstForCall && isImsUseEnabled()) {
                return DBG;
            }
            if (shouldProcessSelfActivation() || useImsForPCOChanged()) {
                logd("always use ImsPhone for self activation");
                return DBG;
            }
        }
        return useImsForCall;
    }

    protected boolean onCheckIfIgnoreWifiOnlyModeCheck() {
        int setting = SubscriptionManager.getIntegerSubscriptionProperty(getSubId(), "wfc_ims_mode", 2, this.mContext);
        if (setting == 0) {
            return false;
        }
        return DBG;
    }

    protected void onPendingMmiAdded(String dialString, GsmMmiCode mmi) {
        Rlog.d(LOG_TAG, "dialInternal: " + MtkSuppServHelper.encryptString(dialString) + ", mmi=" + mmi);
        dumpPendingMmi();
    }

    protected boolean onPreHandleSsnEvent(Object result) {
        SuppServiceNotification noti = (SuppServiceNotification) result;
        if (noti.notificationType == 1 && noti.code == 0) {
            logd("skip AOSP event for MT forwarded call notification");
            return DBG;
        }
        if (this.mSsnRegistrants.size() == 0) {
            this.mCachedSsn = new AsyncResult((Object) null, noti, (Throwable) null);
            return false;
        }
        return false;
    }

    protected boolean ssOverGsmPipeIfNeeded(Message onComplete) {
        if (isCsRetry(onComplete)) {
            return false;
        }
        return DBG;
    }

    protected boolean uiccProfileUpdateIfNeeded() {
        UiccCard uiccCard;
        UiccPort uiccPort;
        String iccId;
        Rlog.d(LOG_TAG, "uiccProfileUpdateIfNeeded(), enter");
        UiccSlot slot = this.mUiccController.getUiccSlotForPhone(this.mPhoneId);
        boolean expectedValue = DBG;
        if (slot == null || slot.getCardState() != IccCardStatus.CardState.CARDSTATE_PRESENT || this.mUiccApplicationsEnabled == null || (uiccCard = slot.getUiccCard()) == null || (uiccPort = uiccCard.getUiccPortForPhone(getPhoneId())) == null || (iccId = uiccPort.getIccId()) == null) {
            return DBG;
        }
        SubscriptionInfo info = MtkSubscriptionManagerService.getInstance().getSubInfoForIccId(IccUtils.stripTrailingFs(iccId));
        if (info != null) {
            expectedValue = info.areUiccApplicationsEnabled();
        }
        Rlog.d(LOG_TAG, "uiccProfileUpdateIfNeeded(), expectedValue is: " + expectedValue);
        return expectedValue;
    }

    public boolean isPhoneTypeGsm() {
        if (this.mPrecisePhoneType == 1) {
            return DBG;
        }
        return false;
    }

    public boolean isPhoneTypeCdma() {
        if (this.mPrecisePhoneType == 2) {
            return DBG;
        }
        return false;
    }

    public boolean isPhoneTypeCdmaLte() {
        if (this.mPrecisePhoneType == 6) {
            return DBG;
        }
        return false;
    }

    public void setPreferredPhysicalSim(int pSlotId, Message onComplete) {
        this.mMtkCi.setVendorSetting(18, Integer.toString(pSlotId), onComplete);
    }
}
