package com.android.internal.telephony.gsm;

import android.R;
import android.content.Context;
import android.os.AsyncResult;
import android.os.Message;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.MiuiSmsControllerStub;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SmsController;
import com.android.internal.telephony.SmsDispatchersController;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.util.SMSDispatcherUtil;
import com.android.telephony.Rlog;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes.dex */
public class GsmSMSDispatcher extends SMSDispatcher {
    protected GsmInboundSmsHandler mGsmInboundSmsHandler;
    protected AtomicReference<IccRecords> mIccRecords;
    protected AtomicReference<UiccCardApplication> mUiccApplication;
    protected UiccController mUiccController;

    @Override // com.android.internal.telephony.SMSDispatcher
    protected boolean shouldBlockSmsForEcbm() {
        return false;
    }

    public GsmSMSDispatcher(Phone phone, SmsDispatchersController smsDispatchersController, GsmInboundSmsHandler gsmInboundSmsHandler, FeatureFlags featureFlags) {
        super(phone, smsDispatchersController, featureFlags);
        this.mUiccController = null;
        this.mIccRecords = new AtomicReference<>();
        this.mUiccApplication = new AtomicReference<>();
        this.mCi.setOnSmsStatus(this, 10, null);
        this.mGsmInboundSmsHandler = gsmInboundSmsHandler;
        UiccController uiccController = UiccController.getInstance();
        this.mUiccController = uiccController;
        uiccController.registerForIccChanged(this, 15, null);
        Rlog.d("GsmSMSDispatcher", "GsmSMSDispatcher created");
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    public void dispose() {
        super.dispose();
        this.mCi.unSetOnSmsStatus(this);
        this.mUiccController.unregisterForIccChanged(this);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    public String getFormat() {
        return "3gpp";
    }

    @Override // com.android.internal.telephony.SMSDispatcher, android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 14) {
            this.mGsmInboundSmsHandler.sendMessage(1, message.obj);
        } else if (i == 15) {
            onUpdateIccAvailability();
        } else {
            super.handleMessage(message);
        }
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected SmsMessageBase.SubmitPduBase getSubmitPdu(String str, String str2, String str3, boolean z, SmsHeader smsHeader, int i, int i2) {
        return SMSDispatcherUtil.getSubmitPduGsm(str, str2, str3, z, i2);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected SmsMessageBase.SubmitPduBase getSubmitPdu(String str, String str2, int i, byte[] bArr, boolean z) {
        return SMSDispatcherUtil.getSubmitPduGsm(str, str2, i, bArr, z);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected SmsMessageBase.SubmitPduBase getSubmitPdu(String str, String str2, String str3, boolean z, SmsHeader smsHeader, int i, int i2, int i3) {
        return SMSDispatcherUtil.getSubmitPduGsm(str, str2, str3, z, i2, i3);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected SmsMessageBase.SubmitPduBase getSubmitPdu(String str, String str2, int i, byte[] bArr, boolean z, int i2) {
        return SMSDispatcherUtil.getSubmitPduGsm(str, str2, i, bArr, z, i2);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected GsmAlphabet.TextEncodingDetails calculateLength(CharSequence charSequence, boolean z) {
        return SMSDispatcherUtil.calculateLengthGsm(charSequence, z);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected void handleStatusReport(Object obj) {
        if (obj instanceof AsyncResult) {
            this.mSmsDispatchersController.handleSmsStatusReport("3gpp", (byte[]) ((AsyncResult) obj).result);
            this.mCi.acknowledgeLastIncomingGsmSms(true, 0, null);
            return;
        }
        Rlog.e("GsmSMSDispatcher", "handleStatusReport() called for object type ".concat(obj.getClass().getName()));
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected void sendSms(SMSDispatcher.SmsTracker smsTracker) {
        int state = this.mPhone.getServiceState().getState();
        Rlog.d("GsmSMSDispatcher", "sendSms:  isIms()=" + isIms() + " mRetryCount=" + smsTracker.mRetryCount + " mImsRetry=" + smsTracker.mImsRetry + " mMessageRef=" + smsTracker.mMessageRef + " mUsesImsServiceForIms=" + smsTracker.mUsesImsServiceForIms + " SS=" + state + " " + SmsController.formatCrossStackMessageId(smsTracker.mMessageId));
        boolean zIsNeedSendCsSMS = MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).isNeedSendCsSMS(this.mPhone, smsTracker, getFormat());
        Context context = this.mContext;
        if (!zIsNeedSendCsSMS) {
            smsTracker.onFailed(context, SMSDispatcher.getNotInServiceError(state), -1);
            MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).sendSmsDotBroadcast(this.mPhone, smsTracker, null, -1, false);
            return;
        }
        boolean z = smsTracker.isMtSmsPollingMessage(this.mContext) && context.getResources().getBoolean(R.bool.config_supportProfilesOnNonMainUser);
        if (!isIms() && state != 0 && !z && this.mPhone.getServiceState().getRilDataRadioTechnology() != 20) {
            smsTracker.onFailed(this.mContext, SMSDispatcher.getNotInServiceError(state), -1);
            notifySmsSentFailedToEmergencyStateTracker(smsTracker, false);
            MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).sendSmsDotBroadcast(this.mPhone, smsTracker, null, -1, false);
            return;
        }
        Message messageObtainMessage = obtainMessage(2, smsTracker);
        HashMap<String, Object> data = smsTracker.getData();
        byte[] bArr = (byte[]) data.get("pdu");
        byte[] bArr2 = (byte[]) data.get("smsc");
        if (smsTracker.mRetryCount > 0) {
            byte b = bArr[0];
            if ((b & 1) == 1) {
                bArr[0] = (byte) (b | 4);
                bArr[1] = (byte) smsTracker.mMessageRef;
            }
        }
        if ((smsTracker.mImsRetry == 0 && !isIms()) || smsTracker.mUsesImsServiceForIms) {
            if (smsTracker.mRetryCount == 0 && smsTracker.mExpectMore) {
                this.mCi.sendSMSExpectMore(IccUtils.bytesToHexString(bArr2), IccUtils.bytesToHexString(bArr), messageObtainMessage);
                return;
            } else {
                this.mCi.sendSMS(IccUtils.bytesToHexString(bArr2), IccUtils.bytesToHexString(bArr), messageObtainMessage);
                return;
            }
        }
        this.mCi.sendImsGsmSms(IccUtils.bytesToHexString(bArr2), IccUtils.bytesToHexString(bArr), smsTracker.mImsRetry, smsTracker.mMessageRef, messageObtainMessage);
        smsTracker.mImsRetry++;
    }

    protected UiccCardApplication getUiccCardApplication() {
        Rlog.d("GsmSMSDispatcher", "GsmSMSDispatcher: subId = " + this.mPhone.getSubId() + " slotId = " + this.mPhone.getPhoneId());
        return this.mUiccController.getUiccCardApplication(this.mPhone.getPhoneId(), 1);
    }

    protected void onUpdateIccAvailability() {
        UiccCardApplication uiccCardApplication;
        UiccCardApplication uiccCardApplication2;
        if (this.mUiccController == null || (uiccCardApplication2 = this.mUiccApplication.get()) == (uiccCardApplication = getUiccCardApplication())) {
            return;
        }
        if (uiccCardApplication2 != null) {
            Rlog.d("GsmSMSDispatcher", "Removing stale icc objects.");
            if (this.mIccRecords.get() != null) {
                this.mIccRecords.get().unregisterForNewSms(this);
            }
            this.mIccRecords.set(null);
            this.mUiccApplication.set(null);
        }
        if (uiccCardApplication != null) {
            Rlog.d("GsmSMSDispatcher", "New Uicc application found");
            this.mUiccApplication.set(uiccCardApplication);
            this.mIccRecords.set(uiccCardApplication.getIccRecords());
            if (this.mIccRecords.get() != null) {
                this.mIccRecords.get().registerForNewSms(this, 14, null);
            }
        }
    }
}
