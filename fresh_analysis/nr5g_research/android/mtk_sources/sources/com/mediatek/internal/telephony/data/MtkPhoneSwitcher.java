package com.mediatek.internal.telephony.data;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseUtilsStub;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.data.DataConfigManager;
import com.android.internal.telephony.data.PhoneSwitcher;
import com.mediatek.internal.telephony.datasub.SmartDataSwitchAssistant;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class MtkPhoneSwitcher extends PhoneSwitcher {
    private static final String EVALUATION_REASON_RADIO_ON = "EVENT_RADIO_ON";
    private static final int EVENT_CALL_EVALUATE = 1001;
    private static final String LOG_TAG = "MtkPhoneSwitcher";
    private static final boolean VDBG = true;
    private boolean mIsInCall;
    private boolean mTempDataSwitching;

    public static MtkPhoneSwitcher mtkMake(int maxDataAttachModemCount, Context context, Looper looper, FeatureFlags flags) {
        if (sPhoneSwitcher == null) {
            sPhoneSwitcher = new MtkPhoneSwitcher(maxDataAttachModemCount, context, looper, flags);
        }
        return (MtkPhoneSwitcher) sPhoneSwitcher;
    }

    public MtkPhoneSwitcher(int maxActivePhones, Context context, Looper looper, FeatureFlags flags) {
        super(maxActivePhones, context, looper, flags);
        this.mIsInCall = false;
        this.mTempDataSwitching = false;
    }

    public static MtkPhoneSwitcher getInstance() {
        return (MtkPhoneSwitcher) sPhoneSwitcher;
    }

    protected boolean updateHalCommandToUse() {
        this.mHalCommandToUse = 2;
        return VDBG;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void onGetInVoiceCallPhoneId() {
        ArrayList<Integer> phoneIdInVoiceCallList = new ArrayList<>();
        for (Phone phone : PhoneFactory.getPhones()) {
            if (isPhoneInVoiceCall(phone) || isPhoneInVoiceCall(phone.getImsPhone())) {
                phoneIdInVoiceCallList.add(Integer.valueOf(phone.getPhoneId()));
            }
        }
        int nonRadioCallId = MtkDataHelper.getInstance().getNonRadioCallingPhone(phoneIdInVoiceCallList);
        if (nonRadioCallId != -1) {
            phoneIdInVoiceCallList.remove(new Integer(nonRadioCallId));
        }
        if (phoneIdInVoiceCallList.size() > 1) {
            if (this.mPreferredDataPhoneId != -1) {
                this.mPhoneIdInVoiceCall = this.mPreferredDataPhoneId;
            } else {
                log("seldom case, 2 calling phone but no preferred data phoneId");
                this.mPhoneIdInVoiceCall = phoneIdInVoiceCallList.get(0).intValue();
            }
        } else if (phoneIdInVoiceCallList.size() == 1) {
            this.mPhoneIdInVoiceCall = phoneIdInVoiceCallList.get(0).intValue();
        }
        SmartDataSwitchAssistant smartDataSwitchAssistant = SmartDataSwitchAssistant.getInstance();
        if (this.mPhoneIdInVoiceCall != -1) {
            if (!this.mIsInCall) {
                smartDataSwitchAssistant.registerReEvaluateEvent(this, 1001, null, this.mPhoneIdInVoiceCall);
                this.mIsInCall = VDBG;
                return;
            } else {
                smartDataSwitchAssistant.setInCallPhoneId(this.mPhoneIdInVoiceCall);
                return;
            }
        }
        this.mIsInCall = false;
        this.mTempDataSwitching = false;
        smartDataSwitchAssistant.unregisterReEvaluateEvent(this);
    }

    protected boolean shouldSwitchDataDueToInCall() {
        boolean ret = super.shouldSwitchDataDueToInCall();
        SmartDataSwitchAssistant smartDataSwitchAssistant = SmartDataSwitchAssistant.getInstance();
        Phone defaultDataPhone = getPhoneBySubId(this.mPrimaryDataSubId);
        if (defaultDataPhone != null) {
            ret = ret && smartDataSwitchAssistant.checkIsSwitchAvailable(this.mPhoneIdInVoiceCall, defaultDataPhone.getPhoneId(), this.mPreferredDataPhoneId);
        }
        if (ret) {
            this.mTempDataSwitching = VDBG;
        }
        return ret;
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 108:
                log(EVALUATION_REASON_RADIO_ON);
                this.mAutoDataSwitchController.readDeviceResourceConfig();
                super.handleMessage(msg);
                break;
            case 109:
            case 120:
                log("message " + msg.what + " received");
                MtkDataHelper.getInstance().notifyPreciseCallStateChanged();
                super.handleMessage(msg);
                break;
            case 1001:
                log("EVENT_CALL_EVALUATE");
                onEvaluate(false, "CALL_EVALUATE");
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }

    private void updateDataEnabledState(int primaryDataSubId, boolean isUpdatePrimary) {
        Phone phone;
        Phone primaryDataPhone = getPhoneBySubId(primaryDataSubId);
        int primaryPhoneId = primaryDataPhone != null ? primaryDataPhone.getPhoneId() : -1;
        for (int phoneId = 0; phoneId < this.mActiveModemCount; phoneId++) {
            int sub = SubscriptionManager.getSubscriptionId(phoneId);
            if (SubscriptionManager.isValidSubscriptionId(sub) && ((isUpdatePrimary || phoneId != primaryPhoneId) && (phone = findPhoneById(phoneId)) != null && phone.getDataSettingsManager() != null)) {
                phone.getDataSettingsManager().updateDataEnabledAndNotify(4);
            }
        }
    }

    protected void handlePrimaryDataSubChanged(int primaryDataSubId) {
        this.mAutoSelectedDataSubId = Integer.MAX_VALUE;
        this.mAutoDataSwitchController.evaluateAutoDataSwitch(5);
        int validSubCount = 0;
        for (int i = 0; i < this.mActiveModemCount; i++) {
            int sub = SubscriptionManager.getSubscriptionId(i);
            if (SubscriptionManager.isValidSubscriptionId(sub)) {
                validSubCount++;
            }
        }
        boolean z = VDBG;
        if (validSubCount != 1) {
            z = false;
        }
        updateDataEnabledState(primaryDataSubId, z);
    }

    private boolean needHandleDialing(int phoneId) {
        Phone phone = findPhoneById(phoneId);
        if (phone != null && phone.getDataNetworkController() != null) {
            DataConfigManager dcm = phone.getDataNetworkController().getDataConfigManager();
            return ((MtkDataConfigManager) dcm).getHandleDialingDuringCall();
        }
        return VDBG;
    }

    protected boolean isPhoneInVoiceCall(Phone phone) {
        if (phone == null) {
            return false;
        }
        if (TelephonyBaseUtilsStub.isMiuiRom() && Settings.Global.getInt(phone.getContext().getContentResolver(), "data_service_enabled", 0) == 0) {
            log("isPhoneInVoiceCall() settings is off, not passed");
            return false;
        }
        Call bgCall = phone.getBackgroundCall();
        Call fgCall = phone.getForegroundCall();
        Call ringCall = phone.getRingingCall();
        if (bgCall == null || fgCall == null || ringCall == null) {
            return false;
        }
        boolean isVolteCall = MtkDataHelper.getInstance().isVoLteCalling(phone.getPhoneId());
        boolean isDsdalikeAvailable = MtkDataHelper.getInstance().isDsdalikeAvailable(phone.getPhoneId());
        boolean isHandleDialing = needHandleDialing(phone.getPhoneId());
        log("ForegroundCall:" + fgCall.getState() + ", RingingCall:" + ringCall.getState() + ", BackgroundCall: " + bgCall.getState() + ", getPhoneId(): " + phone.getPhoneId() + ", isVolteCall: " + isVolteCall + ", isDsdalikeAvailable: " + isDsdalikeAvailable + ", isHandleDialing: " + isHandleDialing);
        if (isDsdalikeAvailable && ringCall.getState().isRinging() && isVolteCall) {
            log("dsdalikable and volte ring call exist.");
            if (fgCall.getState() != Call.State.ACTIVE && fgCall.getState() != Call.State.ALERTING && ((!isHandleDialing || fgCall.getState() != Call.State.DIALING) && fgCall.getState() != Call.State.HOLDING && bgCall.getState() != Call.State.HOLDING)) {
                return false;
            }
            return VDBG;
        }
        if (fgCall.getState() != Call.State.ACTIVE && fgCall.getState() != Call.State.ALERTING && ((!isHandleDialing || fgCall.getState() != Call.State.DIALING) && fgCall.getState() != Call.State.HOLDING && bgCall.getState() != Call.State.HOLDING && !ringCall.getState().isRinging())) {
            return false;
        }
        return VDBG;
    }

    public boolean getTempDataSwitchState() {
        int subId = this.mSubscriptionManagerService.getDefaultDataSubId();
        if (!this.mTempDataSwitching || this.mSubscriptionManagerService.getPhoneId(subId) == this.mPhoneIdInVoiceCall) {
            return false;
        }
        return VDBG;
    }

    public int getPhoneIdInVoiceCall() {
        return this.mPhoneIdInVoiceCall;
    }
}
