package com.android.internal.telephony.cdma;

import android.os.Message;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.MiuiSmsControllerStub;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SmsController;
import com.android.internal.telephony.SmsDispatchersController;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.util.SMSDispatcherUtil;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class CdmaSMSDispatcher extends SMSDispatcher {
    public CdmaSMSDispatcher(Phone phone, SmsDispatchersController smsDispatchersController, FeatureFlags featureFlags) {
        super(phone, smsDispatchersController, featureFlags);
        Rlog.d("CdmaSMSDispatcher", "CdmaSMSDispatcher created");
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    public String getFormat() {
        return "3gpp2";
    }

    public void sendStatusReportMessage(SmsMessage smsMessage) {
        sendMessage(obtainMessage(10, smsMessage));
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected void handleStatusReport(Object obj) {
        if (obj instanceof SmsMessage) {
            this.mSmsDispatchersController.handleSmsStatusReport("3gpp2", ((SmsMessage) obj).getPdu());
        } else {
            Rlog.e("CdmaSMSDispatcher", "handleStatusReport() called for object type ".concat(obj.getClass().getName()));
        }
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected boolean shouldBlockSmsForEcbm() {
        return this.mPhone.isInEcm() && isCdmaMo() && !isIms();
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected SmsMessageBase.SubmitPduBase getSubmitPdu(String str, String str2, String str3, boolean z, SmsHeader smsHeader, int i, int i2) {
        return SMSDispatcherUtil.getSubmitPduCdma(str, str2, str3, z, smsHeader, i);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected SmsMessageBase.SubmitPduBase getSubmitPdu(String str, String str2, int i, byte[] bArr, boolean z) {
        return SMSDispatcherUtil.getSubmitPduCdma(str, str2, i, bArr, z);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected SmsMessageBase.SubmitPduBase getSubmitPdu(String str, String str2, String str3, boolean z, SmsHeader smsHeader, int i, int i2, int i3) {
        return SMSDispatcherUtil.getSubmitPduCdma(str, str2, str3, z, smsHeader, i);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected SmsMessageBase.SubmitPduBase getSubmitPdu(String str, String str2, int i, byte[] bArr, boolean z, int i2) {
        return SMSDispatcherUtil.getSubmitPduCdma(str, str2, i, bArr, z);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected GsmAlphabet.TextEncodingDetails calculateLength(CharSequence charSequence, boolean z) {
        return SMSDispatcherUtil.calculateLengthCdma(charSequence, z);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    public void sendSms(SMSDispatcher.SmsTracker smsTracker) {
        int state = this.mPhone.getServiceState().getState();
        Rlog.d("CdmaSMSDispatcher", "sendSms:  isIms()=" + isIms() + " mRetryCount=" + smsTracker.mRetryCount + " mImsRetry=" + smsTracker.mImsRetry + " mMessageRef=" + smsTracker.mMessageRef + " mUsesImsServiceForIms=" + smsTracker.mUsesImsServiceForIms + " SS=" + state + " " + SmsController.formatCrossStackMessageId(smsTracker.mMessageId));
        if (!MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).isNeedSendCsSMS(this.mPhone, smsTracker, getFormat())) {
            smsTracker.onFailed(this.mContext, SMSDispatcher.getNotInServiceError(state), -1);
            MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).sendSmsDotBroadcast(this.mPhone, smsTracker, null, -1, false);
            return;
        }
        boolean z = false;
        if (!isIms() && state != 0) {
            smsTracker.onFailed(this.mContext, SMSDispatcher.getNotInServiceError(state), -1);
            notifySmsSentFailedToEmergencyStateTracker(smsTracker, false);
            MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).sendSmsDotBroadcast(this.mPhone, smsTracker, null, -1, false);
            return;
        }
        Message messageObtainMessage = obtainMessage(2, smsTracker);
        byte[] bArr = (byte[]) smsTracker.getData().get("pdu");
        int dataNetworkType = this.mPhone.getServiceState().getDataNetworkType();
        if ((dataNetworkType == 14 || ((dataNetworkType == 13 || dataNetworkType == 19 || dataNetworkType == 20) && !this.mPhone.getServiceStateTracker().isConcurrentVoiceAndDataAllowed())) && this.mPhone.getServiceState().getVoiceNetworkType() == 7 && ((GsmCdmaPhone) this.mPhone).mCT.mState != PhoneConstants.State.IDLE) {
            z = true;
        }
        if ((smsTracker.mImsRetry == 0 && !isIms()) || z || smsTracker.mUsesImsServiceForIms) {
            if (smsTracker.mRetryCount == 0 && smsTracker.mExpectMore) {
                this.mCi.sendCdmaSMSExpectMore(bArr, messageObtainMessage);
                return;
            } else {
                this.mCi.sendCdmaSms(bArr, messageObtainMessage);
                return;
            }
        }
        this.mCi.sendImsCdmaSms(bArr, smsTracker.mImsRetry, smsTracker.mMessageRef, messageObtainMessage);
        smsTracker.mImsRetry++;
    }
}
