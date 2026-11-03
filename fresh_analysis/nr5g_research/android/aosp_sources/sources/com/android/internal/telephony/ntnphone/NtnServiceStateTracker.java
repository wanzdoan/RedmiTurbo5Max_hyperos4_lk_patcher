package com.android.internal.telephony.ntnphone;

import android.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.telephony.CarrierConfigManager;
import android.telephony.MiuiTelephonyManagerStub;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.EventLog;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.EventLogTags;
import com.android.internal.telephony.NtnRIL;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.utils.TelephonyCommonUtils;
import java.util.ArrayList;
import vendor.xiaomi.hardware.satellite.CellIdentity;
import vendor.xiaomi.hardware.satellite.CellIdentityNtn;
import vendor.xiaomi.hardware.satellite.VoiceRegStateResult;

/* JADX INFO: loaded from: classes.dex */
public class NtnServiceStateTracker extends Handler {
    protected static final int EVENT_GET_SIGNAL_STRENGTH = 3;
    protected static final int EVENT_NETWORK_STATE_CHANGED = 2;
    protected static final int EVENT_POLL_SIGNAL_STRENGTH = 5;
    protected static final int EVENT_POLL_STATE_REGISTRATION = 4;
    protected static final int EVENT_RADIO_STATE_CHANGED = 1;
    protected static final int EVENT_SIGNAL_STRENGTH_UPDATE = 6;
    public static final String EXTRA_IS_NTN_MODE = "miui.telephony.extra.IS_NTN_MODE";
    private static final int[] sRssiThresholdsForNtn = {-128, -125, -122, -119, -116};
    protected int mBeamId;
    protected CommandsInterface mCi;
    private int mNewRejectCode;
    protected ServiceState mNewSS;
    protected NtnPhone mPhone;
    protected int[] mPollingContext;
    private int mRejectCode;
    protected boolean mReportedGprsNoReg;
    public ServiceState mSS;
    protected SignalStrength mSignalStrength;
    protected boolean mStartedGprsRegCheck;
    private UiccController mUiccController;
    private String LOG_TAG = "NTNSST";
    private boolean mDontPollSignalStrength = false;
    protected int mMaxDataCalls = 1;
    protected int mNewMaxDataCalls = 1;
    protected int mReasonDataDenied = -1;
    protected int mNewReasonDataDenied = -1;
    protected boolean mGsmRoaming = false;
    protected boolean mEmergencyOnly = false;
    protected boolean mNitzUpdatedTime = false;
    private String mCurSpn = null;
    private String mCurDataSpn = null;
    private String mCurPlmn = null;
    private boolean mCurShowPlmn = false;
    private boolean mCurShowSpn = false;
    private int mSubId = -1;
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.ntnphone.NtnServiceStateTracker.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (NtnServiceStateTracker.this.mCi.getRadioState() == 2) {
                NtnServiceStateTracker.this.log("radio power unavailable, ignore");
                return;
            }
            if (action.equals("android.intent.action.LOCALE_CHANGED")) {
                NtnServiceStateTracker.this.log("ACTION_LOCALE_CHANGED");
                NtnServiceStateTracker.this.pollState();
                NtnServiceStateTracker.this.updateSpnDisplay();
            } else if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                if ((TelephonyCommonUtils.isNtnChipVendor_54() || NtnServiceStateTracker.this.mPhone.isHardSimSwapForTT()) && "READY".equals(intent.getStringExtra("ss")) && NtnServiceStateTracker.this.mPhone.isSatellitePowerOn()) {
                    NtnServiceStateTracker.this.log("sim ready, trigger radio power on");
                    NtnServiceStateTracker.this.mCi.setRadioPower(true, false, false, null);
                }
            }
        }
    };
    private SignalStrength mLastSignalStrength = null;

    private int getRegStateFromHalRegState(int i) {
        switch (i) {
            case 1:
            case 6:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            default:
                switch (i) {
                    case 12:
                        return 2;
                    case 13:
                        return 3;
                    case 14:
                        return 4;
                    default:
                        return 0;
                }
        }
    }

    private int regCodeToServiceState(int i) {
        return (i == 1 || i == 5) ? 0 : 1;
    }

    public NtnServiceStateTracker(NtnPhone ntnPhone, NtnRIL ntnRIL) {
        this.mUiccController = null;
        this.LOG_TAG += "[" + ntnPhone.getPhoneId() + "]";
        this.mPhone = ntnPhone;
        this.mCi = ntnRIL;
        this.mUiccController = UiccController.getInstance();
        this.mCi.setOnSignalStrengthUpdate(this, 6, null);
        this.mCi.registerForRadioStateChanged(this, 1, null);
        this.mCi.registerForNetworkStateChanged(this, 2, null);
        this.mSS = new ServiceState();
        this.mNewSS = new ServiceState();
        this.mSignalStrength = new SignalStrength();
        Context context = this.mPhone.getContext();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        context.registerReceiver(this.mIntentReceiver, intentFilter, 2);
    }

    public void dispose() {
        this.mCi.unSetOnSignalStrengthUpdate(this);
        this.mCi.unregisterForRadioStateChanged(this);
        this.mCi.unregisterForNetworkStateChanged(this);
        this.mPhone.getContext().unregisterReceiver(this.mIntentReceiver);
        removeCallbacksAndMessages(null);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        switch (i) {
            case 1:
                modemTriggeredPollState();
                break;
            case 2:
                modemTriggeredPollState();
                break;
            case 3:
                if (this.mCi.getRadioState() == 1) {
                    onSignalStrengthResult((AsyncResult) message.obj);
                    queueNextSignalStrengthPoll();
                    break;
                }
                break;
            case 4:
                handlePollStateResult(i, (AsyncResult) message.obj);
                break;
            case 5:
                this.mCi.getSignalStrength(obtainMessage(3));
                break;
            case 6:
                AsyncResult asyncResult = (AsyncResult) message.obj;
                this.mDontPollSignalStrength = true;
                onSignalStrengthResult(asyncResult);
                break;
            default:
                log("Unhandled message with number: " + message.what);
                break;
        }
    }

    protected void handlePollStateResult(int i, AsyncResult asyncResult) {
        if (asyncResult.userObj != this.mPollingContext) {
            return;
        }
        Throwable th = asyncResult.exception;
        if (th != null) {
            CommandException.Error commandError = th instanceof CommandException ? ((CommandException) th).getCommandError() : null;
            if (commandError == CommandException.Error.RADIO_NOT_AVAILABLE) {
                cancelPollState();
                if (this.mCi.getRadioState() != 1) {
                    int radioState = this.mCi.getRadioState();
                    ServiceState serviceState = this.mNewSS;
                    if (radioState == 2) {
                        serviceState.setStateOutOfService();
                    } else {
                        serviceState.setStateOff();
                    }
                    setSignalStrengthDefaultValues();
                    this.mNitzUpdatedTime = false;
                    pollStateDone();
                    loge("Mlog: pollStateDone to notify RADIO_NOT_AVAILABLE");
                    return;
                }
                return;
            }
            if (commandError != CommandException.Error.OP_NOT_ALLOWED_BEFORE_REG_NW) {
                loge("RIL implementation has returned an error where it must succeed" + asyncResult.exception);
            }
        } else {
            try {
                handlePollStateResultMessage(i, asyncResult);
            } catch (RuntimeException e) {
                loge("Exception while polling service state. Probably malformed RIL response." + e);
            }
        }
        int[] iArr = this.mPollingContext;
        int i2 = iArr[0] - 1;
        iArr[0] = i2;
        if (i2 == 0) {
            if (this.mPhone.isPhoneTypeGsm()) {
                this.mNewSS.setEmergencyOnly(this.mEmergencyOnly);
            }
            pollStateDone();
        }
    }

    protected void cancelPollState() {
        this.mPollingContext = new int[1];
    }

    /* JADX WARN: Code duplicated, block: B:8:0x0014  */
    private int getNtnArfcn(VoiceRegStateResult voiceRegStateResult) {
        int i;
        CellIdentity cellIdentity = voiceRegStateResult.cellIdentity;
        if (cellIdentity.cellInfoType != 6) {
            i = -1;
        } else {
            CellIdentityNtn[] cellIdentityNtnArr = cellIdentity.cellIdentityNtn;
            if (cellIdentityNtnArr.length == 1) {
                i = cellIdentityNtnArr[0].arfcn;
            } else {
                i = -1;
            }
        }
        log("getNtnArfcn arfcn = " + i);
        return i;
    }

    protected void handlePollStateResultMessage(int i, AsyncResult asyncResult) {
        if (i == 4) {
            VoiceRegStateResult voiceRegStateResult = (VoiceRegStateResult) asyncResult.result;
            int regStateFromHalRegState = getRegStateFromHalRegState(voiceRegStateResult.regState);
            this.mNewSS.setVoiceRegState(regCodeToServiceState(regStateFromHalRegState));
            this.mNewSS.setRilVoiceRadioTechnology(voiceRegStateResult.rat);
            this.mNewSS.setChannelNumber(getNtnArfcn(voiceRegStateResult));
            updateNtnBeamId(voiceRegStateResult);
            this.mNewSS.addNetworkRegistrationInfo(createRegistrationStateFromVoiceRegState(voiceRegStateResult));
            int i2 = voiceRegStateResult.reasonForDenial;
            if (this.mPhone.isPhoneTypeGsm()) {
                this.mGsmRoaming = false;
                this.mNewRejectCode = i2;
                this.mEmergencyOnly = false;
            }
            log("handlPollVoiceRegResultMessage: regState=" + regStateFromHalRegState + " radioTechnology=" + voiceRegStateResult.rat);
            return;
        }
        loge("handlePollStateResultMessage: Unexpected RIL response received: " + i);
    }

    private NetworkRegistrationInfo createRegistrationStateFromVoiceRegState(VoiceRegStateResult voiceRegStateResult) {
        if (voiceRegStateResult == null) {
            return null;
        }
        try {
            return new NetworkRegistrationInfo(1, 1, getRegStateFromHalRegState(voiceRegStateResult.regState), ServiceState.rilRadioTechnologyToNetworkType(voiceRegStateResult.rat), voiceRegStateResult.reasonForDenial, false, new ArrayList(), null, "", voiceRegStateResult.cssSupported, voiceRegStateResult.roamingIndicator, voiceRegStateResult.systemIsInPrl, voiceRegStateResult.defaultRoamingIndicator);
        } catch (Exception e) {
            loge("createRegistrationStateFromVoiceRegState error " + e);
            return null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:18:0x006c  */
    /* JADX WARN: Code duplicated, block: B:19:0x0071  */
    /* JADX WARN: Code duplicated, block: B:21:0x0074  */
    /* JADX WARN: Code duplicated, block: B:22:0x0079  */
    /* JADX WARN: Code duplicated, block: B:24:0x007d  */
    /* JADX WARN: Code duplicated, block: B:25:0x0090  */
    /* JADX WARN: Code duplicated, block: B:28:0x0097  */
    /* JADX WARN: Code duplicated, block: B:29:0x0099  */
    /* JADX WARN: Code duplicated, block: B:34:0x00a4  */
    /* JADX WARN: Code duplicated, block: B:42:0x00ba  */
    /* JADX WARN: Code duplicated, block: B:55:0x00e5  */
    protected void updateSpnDisplay() {
        String string;
        boolean z;
        String operatorAlpha;
        boolean z2;
        UiccCardApplication uiccCardApplication;
        IccRecords iccRecords;
        String serviceProviderName;
        String spn;
        String str;
        boolean z3;
        String str2;
        int subId;
        log("updateSpnDisplay+");
        int voiceRegState = this.mSS.getVoiceRegState();
        boolean z4 = false;
        if (voiceRegState == 1 || voiceRegState == 2) {
            if (this.mEmergencyOnly) {
                string = Resources.getSystem().getText(R.string.global_action_settings).toString();
                z = false;
            } else {
                string = Resources.getSystem().getText(R.string.network_partial_connectivity).toString();
                z = true;
            }
            log("updateSpnDisplay: radio is on but out of service, set plmn='" + string + "'");
            operatorAlpha = string;
        } else {
            if (voiceRegState == 0) {
                operatorAlpha = this.mSS.getOperatorAlpha();
                z2 = false;
                z = false;
            } else {
                log("updateSpnDisplay: radio is off w/ showPlmn=true plmn=null");
                operatorAlpha = null;
                z = false;
            }
            uiccCardApplication = getUiccCardApplication();
            if (uiccCardApplication != null) {
                iccRecords = uiccCardApplication.getIccRecords();
            } else {
                iccRecords = null;
            }
            if (iccRecords != null) {
                serviceProviderName = iccRecords.getServiceProviderName();
            } else {
                serviceProviderName = "";
            }
            if (iccRecords != null) {
                spn = MiuiTelephonyManagerStub.get().getSpn(iccRecords.getOperatorNumeric(), this.mPhone.getPhoneId(), serviceProviderName, true);
            } else {
                spn = null;
            }
            if (TextUtils.isEmpty(spn)) {
                str = serviceProviderName;
            } else {
                str = spn;
            }
            if (!z || TextUtils.isEmpty(str)) {
                z3 = false;
            } else {
                z3 = true;
            }
            if (this.mSS.getVoiceRegState() != 3 || (z2 && TextUtils.equals(str, operatorAlpha))) {
                str2 = null;
            } else {
                z4 = z3;
                str2 = str;
            }
            subId = this.mPhone.getSubId();
            if (this.mSubId == subId || z2 != this.mCurShowPlmn || z4 != this.mCurShowSpn || !TextUtils.equals(str2, this.mCurSpn) || !TextUtils.equals(str, this.mCurDataSpn) || !TextUtils.equals(operatorAlpha, this.mCurPlmn)) {
                log(String.format("updateSpnDisplay: changed sending intent ntn mode=true showPlmn='%b' plmn='%s' showSpn='%b' spn='%s' dataSpn='%s' subId='%d'", Boolean.valueOf(z2), operatorAlpha, Boolean.valueOf(z4), str2, str, Integer.valueOf(subId)));
                Intent intent = new Intent("android.telephony.action.SERVICE_PROVIDERS_UPDATED");
                intent.addFlags(268435456);
                intent.putExtra("android.telephony.extra.SHOW_SPN", z4);
                intent.putExtra("android.telephony.extra.SPN", str2);
                intent.putExtra("android.telephony.extra.DATA_SPN", str);
                intent.putExtra("android.telephony.extra.SHOW_PLMN", z2);
                intent.putExtra("android.telephony.extra.PLMN", operatorAlpha);
                intent.putExtra(EXTRA_IS_NTN_MODE, true);
                SubscriptionManager.putPhoneIdAndSubIdExtra(intent, this.mPhone.getPhoneId());
                this.mPhone.getContext().sendStickyBroadcastAsUser(intent, UserHandle.ALL);
            }
            this.mSubId = subId;
            this.mCurShowSpn = z4;
            this.mCurShowPlmn = z2;
            this.mCurSpn = str2;
            this.mCurDataSpn = str;
            this.mCurPlmn = operatorAlpha;
            log("updateSpnDisplay-");
        }
        z2 = true;
        uiccCardApplication = getUiccCardApplication();
        if (uiccCardApplication != null) {
            iccRecords = uiccCardApplication.getIccRecords();
        } else {
            iccRecords = null;
        }
        if (iccRecords != null) {
            serviceProviderName = iccRecords.getServiceProviderName();
        } else {
            serviceProviderName = "";
        }
        if (iccRecords != null) {
            spn = MiuiTelephonyManagerStub.get().getSpn(iccRecords.getOperatorNumeric(), this.mPhone.getPhoneId(), serviceProviderName, true);
        } else {
            spn = null;
        }
        if (TextUtils.isEmpty(spn)) {
            str = spn;
        } else {
            str = serviceProviderName;
        }
        if (z) {
            z3 = false;
        } else {
            z3 = false;
        }
        if (this.mSS.getVoiceRegState() != 3) {
            str2 = null;
        } else {
            str2 = null;
        }
        subId = this.mPhone.getSubId();
        if (this.mSubId == subId) {
            log(String.format("updateSpnDisplay: changed sending intent ntn mode=true showPlmn='%b' plmn='%s' showSpn='%b' spn='%s' dataSpn='%s' subId='%d'", Boolean.valueOf(z2), operatorAlpha, Boolean.valueOf(z4), str2, str, Integer.valueOf(subId)));
            Intent intent2 = new Intent("android.telephony.action.SERVICE_PROVIDERS_UPDATED");
            intent2.addFlags(268435456);
            intent2.putExtra("android.telephony.extra.SHOW_SPN", z4);
            intent2.putExtra("android.telephony.extra.SPN", str2);
            intent2.putExtra("android.telephony.extra.DATA_SPN", str);
            intent2.putExtra("android.telephony.extra.SHOW_PLMN", z2);
            intent2.putExtra("android.telephony.extra.PLMN", operatorAlpha);
            intent2.putExtra(EXTRA_IS_NTN_MODE, true);
            SubscriptionManager.putPhoneIdAndSubIdExtra(intent2, this.mPhone.getPhoneId());
            this.mPhone.getContext().sendStickyBroadcastAsUser(intent2, UserHandle.ALL);
        } else {
            log(String.format("updateSpnDisplay: changed sending intent ntn mode=true showPlmn='%b' plmn='%s' showSpn='%b' spn='%s' dataSpn='%s' subId='%d'", Boolean.valueOf(z2), operatorAlpha, Boolean.valueOf(z4), str2, str, Integer.valueOf(subId)));
            Intent intent3 = new Intent("android.telephony.action.SERVICE_PROVIDERS_UPDATED");
            intent3.addFlags(268435456);
            intent3.putExtra("android.telephony.extra.SHOW_SPN", z4);
            intent3.putExtra("android.telephony.extra.SPN", str2);
            intent3.putExtra("android.telephony.extra.DATA_SPN", str);
            intent3.putExtra("android.telephony.extra.SHOW_PLMN", z2);
            intent3.putExtra("android.telephony.extra.PLMN", operatorAlpha);
            intent3.putExtra(EXTRA_IS_NTN_MODE, true);
            SubscriptionManager.putPhoneIdAndSubIdExtra(intent3, this.mPhone.getPhoneId());
            this.mPhone.getContext().sendStickyBroadcastAsUser(intent3, UserHandle.ALL);
        }
        this.mSubId = subId;
        this.mCurShowSpn = z4;
        this.mCurShowPlmn = z2;
        this.mCurSpn = str2;
        this.mCurDataSpn = str;
        this.mCurPlmn = operatorAlpha;
        log("updateSpnDisplay-");
    }

    public void pollState() {
        pollState(false);
    }

    protected void modemTriggeredPollState() {
        pollState(true);
    }

    public void pollState(boolean z) {
        log("pollState: modemTriggered=" + z);
        int radioState = this.mCi.getRadioState();
        if (radioState == 0) {
            this.mNewSS.setStateOff();
            setSignalStrengthDefaultValues();
            pollStateDone();
        } else {
            if (radioState == 2) {
                this.mNewSS.setStateOutOfService();
                setSignalStrengthDefaultValues();
                return;
            }
            this.mPollingContext = new int[]{0};
            log("pollState: modemTriggered=" + z);
            int[] iArr = this.mPollingContext;
            iArr[0] = iArr[0] + 1;
            this.mCi.getVoiceRegistrationState(obtainMessage(4, iArr));
        }
    }

    protected void pollStateDone() {
        log("Poll ServiceState done:  oldSS=[" + this.mSS + "] newSS=[" + this.mNewSS + "] oldMaxDataCalls=" + this.mMaxDataCalls + " mNewMaxDataCalls=" + this.mNewMaxDataCalls + " oldReasonDataDenied=" + this.mReasonDataDenied + " mNewReasonDataDenied=" + this.mNewReasonDataDenied);
        boolean z = this.mSS.getVoiceRegState() != 0 && this.mNewSS.getVoiceRegState() == 0;
        boolean z2 = this.mSS.getVoiceRegState() == 0 && this.mNewSS.getVoiceRegState() != 0;
        boolean z3 = this.mSS.getDataRegState() != 0 && this.mNewSS.getDataRegState() == 0;
        boolean z4 = this.mSS.getDataRegState() == 0 && this.mNewSS.getDataRegState() != 0;
        boolean z5 = this.mSS.getDataRegState() != this.mNewSS.getDataRegState();
        boolean z6 = this.mSS.getVoiceRegState() != this.mNewSS.getVoiceRegState();
        boolean z7 = this.mSS.getRilVoiceRadioTechnology() != this.mNewSS.getRilVoiceRadioTechnology();
        boolean z8 = this.mSS.getRilDataRadioTechnology() != this.mNewSS.getRilDataRadioTechnology();
        boolean zEquals = this.mNewSS.equals(this.mSS);
        boolean z9 = z6;
        log("pollStateDone: hasRegistered=" + z + " hasDeregistered=" + z2 + " hasDataAttached=" + z3 + " hasDataDetached=" + z4 + " hasDataRegStateChanged=" + z5 + " hasRilVoiceRadioTechnologyChanged= " + z7 + " hasRilDataRadioTechnologyChanged=" + z8 + " hasChanged=" + (!zEquals) + " hasVoiceRoamingOn=" + (!this.mSS.getVoiceRoaming() && this.mNewSS.getVoiceRoaming()) + " hasVoiceRoamingOff=" + (this.mSS.getVoiceRoaming() && !this.mNewSS.getVoiceRoaming()) + " hasDataRoamingOn=" + (!this.mSS.getDataRoaming() && this.mNewSS.getDataRoaming()) + " hasDataRoamingOff=" + (this.mSS.getDataRoaming() && !this.mNewSS.getDataRoaming()) + " hasLocationChanged=false has4gHandoff = false hasMultiApnSupport=false hasLostMultiApnSupport=false");
        if (z9 || z5) {
            EventLog.writeEvent(this.mPhone.isPhoneTypeGsm() ? EventLogTags.GSM_SERVICE_STATE_CHANGE : EventLogTags.CDMA_SERVICE_STATE_CHANGE, Integer.valueOf(this.mSS.getVoiceRegState()), Integer.valueOf(this.mSS.getDataRegState()), Integer.valueOf(this.mNewSS.getVoiceRegState()), Integer.valueOf(this.mNewSS.getDataRegState()));
        }
        if (this.mPhone.isPhoneTypeGsm()) {
            if (z7) {
                EventLog.writeEvent(EventLogTags.GSM_RAT_SWITCHED_NEW, -1, Integer.valueOf(this.mSS.getRilVoiceRadioTechnology()), Integer.valueOf(this.mNewSS.getRilVoiceRadioTechnology()));
                log("RAT switched " + ServiceState.rilRadioTechnologyToString(this.mSS.getRilVoiceRadioTechnology()) + " -> " + ServiceState.rilRadioTechnologyToString(this.mNewSS.getRilVoiceRadioTechnology()) + " at cell -1");
            }
            this.mReasonDataDenied = this.mNewReasonDataDenied;
            this.mMaxDataCalls = this.mNewMaxDataCalls;
            this.mRejectCode = this.mNewRejectCode;
        }
        ServiceState serviceState = this.mSS;
        this.mSS = this.mNewSS;
        this.mNewSS = serviceState;
        serviceState.setStateOutOfService();
        if (z8 && 18 == this.mSS.getRilDataRadioTechnology()) {
            log("pollStateDone: IWLAN enabled");
        }
        if (z) {
            log("pollStateDone: registering current mNitzUpdatedTime=" + this.mNitzUpdatedTime + " changing to false");
            this.mNitzUpdatedTime = false;
        }
        if (!zEquals) {
            updateSpnDisplay();
            this.mSS.getOperatorNumeric();
            this.mSS.getOperatorNumeric();
            log("Broadcasting ServiceState : " + this.mSS);
            this.mPhone.notifyServiceStateChanged(this.mSS);
        }
        if (this.mPhone.isPhoneTypeGsm()) {
            this.mReportedGprsNoReg = false;
        }
    }

    private UiccCardApplication getUiccCardApplication() {
        if (this.mUiccController == null) {
            return null;
        }
        boolean zIsPhoneTypeGsm = this.mPhone.isPhoneTypeGsm();
        UiccController uiccController = this.mUiccController;
        if (zIsPhoneTypeGsm) {
            return uiccController.getUiccCardApplication(this.mPhone.getPhoneId(), 1);
        }
        return uiccController.getUiccCardApplication(this.mPhone.getPhoneId(), 2);
    }

    protected void queueNextSignalStrengthPoll() {
        if (this.mDontPollSignalStrength) {
            return;
        }
        Message messageObtainMessage = obtainMessage();
        messageObtainMessage.what = 5;
        sendMessageDelayed(messageObtainMessage, 20000L);
    }

    private boolean onSignalStrengthResult(AsyncResult asyncResult) {
        Object obj;
        if (asyncResult.exception == null && (obj = asyncResult.result) != null) {
            this.mSignalStrength = (SignalStrength) obj;
            this.mSignalStrength.updateLevel(getCarrierConfig(), this.mSS);
        } else {
            log("onSignalStrengthResult() Exception from RIL : " + asyncResult.exception);
            this.mSignalStrength = new SignalStrength();
        }
        return notifySignalStrength();
    }

    public boolean notifySignalStrength() {
        boolean z = false;
        if (!this.mSignalStrength.equals(this.mLastSignalStrength)) {
            try {
                if (TelephonyCommonUtilsStub.get().notifySignalStrength(this.mPhone)) {
                    z = true;
                    this.mLastSignalStrength = this.mSignalStrength;
                    return true;
                }
            } catch (NullPointerException e) {
                loge("updateSignalStrength() Phone already destroyed: " + e + "SignalStrength not notified");
            }
        }
        return z;
    }

    public SignalStrength getSignalStrength() {
        return this.mSignalStrength;
    }

    protected void setSignalStrengthDefaultValues() {
        this.mSignalStrength = new SignalStrength();
    }

    private PersistableBundle getCarrierConfig() {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService("carrier_config");
        return (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(this.mPhone.getSubId())) == null) ? CarrierConfigManager.getDefaultConfig() : configForSubId;
    }

    private void updateNtnBeamId(VoiceRegStateResult voiceRegStateResult) {
        this.mBeamId = -1;
        CellIdentity cellIdentity = voiceRegStateResult.cellIdentity;
        if (cellIdentity.cellInfoType == 6) {
            CellIdentityNtn[] cellIdentityNtnArr = cellIdentity.cellIdentityNtn;
            if (cellIdentityNtnArr.length == 1) {
                this.mBeamId = cellIdentityNtnArr[0].beamid;
            }
        }
        log("updateNtnBeamId beam id = " + this.mBeamId);
    }

    public int getNtnBeamId() {
        return this.mBeamId;
    }

    protected void log(String str) {
        Rlog.d(this.LOG_TAG, str);
    }

    protected void loge(String str) {
        Rlog.e(this.LOG_TAG, str);
    }
}
