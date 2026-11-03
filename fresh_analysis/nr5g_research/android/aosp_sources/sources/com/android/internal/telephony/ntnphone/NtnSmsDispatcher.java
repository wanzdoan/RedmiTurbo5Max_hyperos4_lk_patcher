package com.android.internal.telephony.ntnphone;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Message;
import android.telephony.MiuiTelephonyManagerStub;
import android.telephony.ims.aidl.IImsSmsListener;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlagsImpl;
import com.android.internal.telephony.CallFailCause;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.NtnRIL;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SmsDispatchersController;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsResponse;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.util.SMSDispatcherUtil;
import com.android.internal.telephony.utils.NtnBoxOptimization;
import com.android.internal.telephony.utils.TelephonyCommonUtils;
import com.android.telephony.Rlog;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class NtnSmsDispatcher extends SMSDispatcher {
    public static final String ACTION_SEND_SMS_RESULT_EVENT = "com.anrdoid.internal.action.SEND_SMS_RESULT_EVENT";
    protected static final int EVENT_NTN_NEW_SMS_SEND_RESULT = 102;
    protected static final int EVENT_NTN_NEW_SMS_STATUS_REPORT = 101;
    protected static final int STATUS_SMS_SEND_RESULT_FAIL = 0;
    protected static final int STATUS_SMS_SEND_RESULT_SUCCESS = 1;
    private static NtnSmsDispatcher sInstance;
    private HashMap mDeliveryPendingMapForNtn;
    private final Object mLock;
    public AtomicInteger mNextToken;
    private int mNtnMessageRef;
    private NtnPhone mNtnPhone;
    private NtnRIL mNtnRil;
    private HashMap mSendResultPendingMapForNtn;
    private HashMap mSendSmsDurationMap;
    public Map<Integer, SMSDispatcher.SmsTracker> mTrackers;

    public void fallbackToPstn(SMSDispatcher.SmsTracker smsTracker) {
    }

    public IImsSmsListener getSmsListener() {
        return null;
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected boolean isCdmaMo() {
        return false;
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected boolean shouldBlockSmsForEcbm() {
        return false;
    }

    public NtnSmsDispatcher(Phone phone, SmsDispatchersController smsDispatchersController) {
        super(phone, smsDispatchersController, new FeatureFlagsImpl());
        this.mTrackers = new ConcurrentHashMap();
        this.mNextToken = new AtomicInteger();
        this.mLock = new Object();
        this.mDeliveryPendingMapForNtn = new HashMap();
        this.mSendResultPendingMapForNtn = new HashMap();
        this.mSendSmsDurationMap = new HashMap();
        this.mNtnMessageRef = -1;
        sInstance = this;
    }

    public void initNtnPhone(Phone phone) {
        if (phone instanceof NtnPhone) {
            NtnPhone ntnPhone = (NtnPhone) phone;
            this.mNtnPhone = ntnPhone;
            NtnRIL ntnRIL = ntnPhone.mNtnRil;
            this.mNtnRil = ntnRIL;
            ntnRIL.setOnNewNtnSmsStatusReport(this, 101, null);
            this.mNtnRil.setOnNewNtnSmsSendResult(this, 102, null);
        }
    }

    public static NtnSmsDispatcher getInstance() {
        return sInstance;
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected String getFormat() {
        return "3gpp";
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
        return SMSDispatcherUtil.getSubmitPdu(isCdmaMo(), str, str2, str3, z, smsHeader, i, i2, i3);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected SmsMessageBase.SubmitPduBase getSubmitPdu(String str, String str2, int i, byte[] bArr, boolean z, int i2) {
        return SMSDispatcherUtil.getSubmitPdu(isCdmaMo(), str, str2, i, bArr, z, i2);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    protected GsmAlphabet.TextEncodingDetails calculateLength(CharSequence charSequence, boolean z) {
        return SMSDispatcherUtil.calculateLengthGsm(charSequence, z);
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    public void sendSms(SMSDispatcher.SmsTracker smsTracker) {
        logd("NtnPhone: send sms start");
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
        } else {
            logi("sendSms tracker.mMessageRef = " + smsTracker.mMessageRef);
        }
        Phone defaultPhone = NtnPhoneFactory.getDefaultPhone();
        initNtnPhone(defaultPhone);
        defaultPhone.mCi.sendSMS(IccUtils.bytesToHexString(bArr2), IccUtils.bytesToHexString(bArr), messageObtainMessage);
        this.mSendSmsDurationMap.put(Integer.valueOf(smsTracker.mMessageRef), Long.valueOf(System.currentTimeMillis()));
    }

    protected void handleSendToStack(AsyncResult asyncResult) {
        if (asyncResult.exception == null) {
            SMSDispatcher.SmsTracker smsTracker = (SMSDispatcher.SmsTracker) asyncResult.userObj;
            logi("handleSendToStack tracker.mMessageRef = " + smsTracker.mMessageRef);
            putSendResultPendingTrackerForNtn(smsTracker.mMessageRef, asyncResult);
            return;
        }
        sendSmsResultException(asyncResult);
    }

    public void putSendResultPendingTrackerForNtn(int i, AsyncResult asyncResult) {
        logi("putSendResultPendingTrackerForNtn mMessageRef = " + asyncResult.result);
        this.mSendResultPendingMapForNtn.put(Integer.valueOf(i), asyncResult);
    }

    public void putDeliveryPendingTrackerForNtn(SMSDispatcher.SmsTracker smsTracker) {
        logi("putDeliveryPendingTrackerForNtn mMessageRef = " + smsTracker.mMessageRef);
        this.mDeliveryPendingMapForNtn.put(Integer.valueOf(smsTracker.mMessageRef), smsTracker);
    }

    @Override // com.android.internal.telephony.SMSDispatcher, android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 2) {
            boolean zIsNtnChipVendor_54 = TelephonyCommonUtils.isNtnChipVendor_54();
            Object obj = message.obj;
            if (zIsNtnChipVendor_54) {
                handleSendCompleteFor54Ntn((AsyncResult) obj);
                return;
            } else {
                handleSendToStack((AsyncResult) obj);
                return;
            }
        }
        if (i == 3) {
            logi("SMS retry..");
            sendRetrySms((SMSDispatcher.SmsTracker) message.obj);
            return;
        }
        if (i == 101) {
            logd("Event EVENT_NTN_NEW_SMS_STATUS_REPORT Received");
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception == null) {
                logd("EVENT_NTN_NEW_SMS_STATUS_REPORT ar.result =" + asyncResult.result);
                updateSmsStatusReporter((NtnSmsStatusReport) asyncResult.result);
                return;
            }
            return;
        }
        if (i == 102) {
            logd("Event EVENT_NTN_NEW_SMS_SEND_RESULT Received");
            AsyncResult asyncResult2 = (AsyncResult) message.obj;
            if (asyncResult2.exception == null) {
                logd("EVENT_NTN_NEW_SMS_SEND_RESULT ar.result =" + asyncResult2.result);
                updateSmsSendResultStatus((NtnSmsSendResult) asyncResult2.result);
                return;
            }
            return;
        }
        super.handleMessage(message);
    }

    private void updateSmsSendResultStatus(NtnSmsSendResult ntnSmsSendResult) {
        HashMap map = this.mSendResultPendingMapForNtn;
        if (map == null || map.isEmpty()) {
            loge("updateSmsSendResultStatus is called, but delivery pending is empty");
            return;
        }
        AsyncResult asyncResult = (AsyncResult) this.mSendResultPendingMapForNtn.get(Integer.valueOf(ntnSmsSendResult.messageRef));
        if (asyncResult == null || asyncResult.exception != null) {
            loge("updateSmsSendResultStatus: message ref is wrong, messageRef = " + ntnSmsSendResult.messageRef);
            return;
        }
        handleSendCompleteForNtn(ntnSmsSendResult, asyncResult);
    }

    public void handleSendCompleteForNtn(NtnSmsSendResult ntnSmsSendResult, AsyncResult asyncResult) {
        SMSDispatcher.SmsTracker smsTracker = (SMSDispatcher.SmsTracker) asyncResult.userObj;
        SmsResponse smsResponse = (SmsResponse) asyncResult.result;
        logi("handleSendCompleteForNtn tracker.mMessageRef = " + smsTracker.mMessageRef);
        if (ntnSmsSendResult != null) {
            if (ntnSmsSendResult.status != 1) {
                asyncResult.exception = new CommandException(CommandException.Error.SMS_FAIL_RETRY);
            }
            logi("handleSendCompleteForNtn ar.exception = " + asyncResult.exception);
            smsResponse.mErrorCode = ntnSmsSendResult.error;
            logi("handleSendCompleteForNtn smsResponse.mErrorCode = " + smsResponse.mErrorCode);
            asyncResult.result = smsResponse;
        }
        logi("handleSendCompleteForNtn after replace exception, ar.exception = " + asyncResult.exception);
        if (asyncResult.exception == null) {
            logi("handleSendCompleteForNtn send message successfully!");
            if (smsTracker.mDeliveryIntent != null) {
                putDeliveryPendingTrackerForNtn(smsTracker);
            }
            sendNtnSmsDot(smsTracker, smsResponse.mErrorCode, true);
            smsTracker.onSent(this.mContext);
            this.mPhone.notifySmsSent(smsTracker.mDestAddress);
            return;
        }
        sendSmsResultException(asyncResult);
    }

    public void handleSendCompleteFor54Ntn(AsyncResult asyncResult) {
        SMSDispatcher.SmsTracker smsTracker = (SMSDispatcher.SmsTracker) asyncResult.userObj;
        SmsResponse smsResponse = (SmsResponse) asyncResult.result;
        logi("handleSendCompleteFor54Ntn tracker.mMessageRef = " + smsTracker.mMessageRef);
        if (smsResponse != null) {
            smsTracker.mMessageRef = smsResponse.mMessageRef;
            logi("handleSendCompleteFor54Ntn smsResponse.mMessageRef = " + smsResponse.mMessageRef);
        } else {
            logi("SmsResponse was null");
        }
        logi("handleSendCompleteFor54Ntn ar.exception = " + asyncResult.exception);
        if (asyncResult.exception == null) {
            logi("handleSendCompleteFor54Ntn send message successfully!");
            if (smsTracker.mDeliveryIntent != null) {
                putDeliveryPendingTrackerForNtn(smsTracker);
            }
            sendNtnSmsDot(smsTracker, smsResponse.mErrorCode, true);
            smsTracker.onSent(this.mContext);
            this.mPhone.notifySmsSent(smsTracker.mDestAddress);
            return;
        }
        sendSmsResultException(asyncResult);
    }

    /* JADX WARN: Code duplicated, block: B:14:0x005c  */
    private void sendNtnSmsDot(SMSDispatcher.SmsTracker smsTracker, int i, boolean z) {
        long jCurrentTimeMillis;
        HashMap map = this.mSendSmsDurationMap;
        if (map == null || map.isEmpty()) {
            jCurrentTimeMillis = -1;
        } else {
            int iIntValue = smsTracker.mMessageRef;
            if (iIntValue == 0) {
                Iterator it = this.mSendSmsDurationMap.entrySet().iterator();
                if (it.hasNext()) {
                    iIntValue = ((Integer) ((Map.Entry) it.next()).getKey()).intValue();
                }
            }
            if (this.mSendSmsDurationMap.get(Integer.valueOf(iIntValue)) != null) {
                jCurrentTimeMillis = (System.currentTimeMillis() - ((Long) this.mSendSmsDurationMap.get(Integer.valueOf(iIntValue))).longValue()) / 1000;
                this.mSendSmsDurationMap.remove(Integer.valueOf(iIntValue));
            } else {
                jCurrentTimeMillis = -1;
            }
        }
        Intent intent = new Intent(ACTION_SEND_SMS_RESULT_EVENT);
        intent.addFlags(268435456);
        intent.putExtra("is_satellite_sms", true);
        intent.putExtra("sms_packagename", smsTracker.getAppPackageName());
        intent.putExtra("is_Sent_SMS_Success", z);
        intent.putExtra("error_Code", i);
        intent.putExtra("is_mult_sms", smsTracker.mSmsHeader != null);
        intent.putExtra("sms_send_duration", String.valueOf(jCurrentTimeMillis));
        logd("sendNtnSmsDot packageName = " + smsTracker.getAppPackageName());
        this.mPhone.getContext().sendBroadcast(intent);
    }

    private void sendSmsResultException(AsyncResult asyncResult) {
        Throwable th;
        if (asyncResult == null || (th = asyncResult.exception) == null) {
            return;
        }
        SMSDispatcher.SmsTracker smsTracker = (SMSDispatcher.SmsTracker) asyncResult.userObj;
        SmsResponse smsResponse = (SmsResponse) asyncResult.result;
        int iRilErrorToSmsManagerResult = rilErrorToSmsManagerResult(((CommandException) th).getCommandError());
        int i = smsResponse != null ? smsResponse.mErrorCode : -1;
        if (iRilErrorToSmsManagerResult == 101) {
            logi("send restry sms.");
            retrySendSmsForFail(smsTracker, iRilErrorToSmsManagerResult, i);
        } else {
            smsTracker.onFailed(this.mContext, iRilErrorToSmsManagerResult, i);
            sendNtnSmsDot(smsTracker, i, false);
        }
    }

    private int rilErrorToSmsManagerResult(CommandException.Error error) {
        this.mSmsOutgoingErrorCodes.log("rilError: " + error);
        switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$CommandException$Error[error.ordinal()]) {
            case 1:
                return 100;
            case 2:
                return 101;
            case 3:
                return 102;
            case 4:
                return NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING;
            case 5:
                return NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE;
            case 6:
                return NtnRILConstants.MI_NTN_RIL_REQUEST_ISIM_AUTHENTICATION;
            case 7:
                return 106;
            case 8:
                return NtnRILConstants.MI_NTN_RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS;
            case 9:
                return NtnRILConstants.MI_NTN_RIL_REQUEST_VOICE_RADIO_TECH;
            case 10:
                return 109;
            case 11:
                return 111;
            case 12:
                return 112;
            case 13:
                return 113;
            case 14:
                return 114;
            case 15:
                return 115;
            case 16:
                return 116;
            case 17:
                return 117;
            case 18:
                return 118;
            case 19:
                return 119;
            case 20:
                return 120;
            case 21:
                return 6;
            case 22:
                return 121;
            case 23:
                return 122;
            case 24:
                return 123;
            case 25:
                return 19;
            case 26:
                return NtnRILConstants.MI_NTN_RIL_REQUEST_SIM_AUTHENTICATION;
            case 27:
                return NtnRILConstants.MI_NTN_RIL_REQUEST_GET_DC_RT_INFO;
            case 28:
                return 127;
            case 29:
                return 128;
            case 30:
                return 129;
            case 31:
                return NtnRILConstants.MI_NTN_RIL_REQUEST_GET_RADIO_CAPABILITY;
            case 32:
                return NtnRILConstants.MI_NTN_RIL_REQUEST_SET_RADIO_CAPABILITY;
            case 33:
                return 132;
            case 34:
                return 133;
            case 35:
                return 134;
            case 36:
                return 135;
            case 37:
                return 136;
            case 38:
                return NtnRILConstants.MI_NTN_RIL_REQUEST_GET_ALLOWED_CARRIERS;
            default:
                Rlog.d("NtnSmsDispatcher", "rilErrorToSmsManagerResult: " + error);
                return 124;
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.ntnphone.NtnSmsDispatcher$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$CommandException$Error;

        static {
            int[] iArr = new int[CommandException.Error.values().length];
            $SwitchMap$com$android$internal$telephony$CommandException$Error = iArr;
            try {
                iArr[CommandException.Error.RADIO_NOT_AVAILABLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.SMS_FAIL_RETRY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.NETWORK_REJECT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.INVALID_STATE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.INVALID_ARGUMENTS.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.NO_MEMORY.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.REQUEST_RATE_LIMITED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.INVALID_SMS_FORMAT.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.SYSTEM_ERR.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.ENCODING_ERR.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.MODEM_ERR.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.NETWORK_ERR.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.INTERNAL_ERR.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.REQUEST_NOT_SUPPORTED.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.INVALID_MODEM_STATE.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.NETWORK_NOT_READY.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OPERATION_NOT_ALLOWED.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.NO_RESOURCES.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.REQUEST_CANCELLED.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.SIM_ABSENT.ordinal()] = 20;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.FDN_CHECK_FAILURE.ordinal()] = 21;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.SIMULTANEOUS_SMS_AND_CALL_NOT_ALLOWED.ordinal()] = 22;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.ACCESS_BARRED.ordinal()] = 23;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.BLOCKED_DUE_TO_CALL.ordinal()] = 24;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.INVALID_SMSC_ADDRESS.ordinal()] = 25;
            } catch (NoSuchFieldError unused25) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.INVALID_RESPONSE.ordinal()] = 26;
            } catch (NoSuchFieldError unused26) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.SIM_PIN2.ordinal()] = 27;
            } catch (NoSuchFieldError unused27) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.SIM_PUK2.ordinal()] = 28;
            } catch (NoSuchFieldError unused28) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.SUBSCRIPTION_NOT_AVAILABLE.ordinal()] = 29;
            } catch (NoSuchFieldError unused29) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.SIM_ERR.ordinal()] = 30;
            } catch (NoSuchFieldError unused30) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.INVALID_SIM_STATE.ordinal()] = 31;
            } catch (NoSuchFieldError unused31) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.NO_SMS_TO_ACK.ordinal()] = 32;
            } catch (NoSuchFieldError unused32) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.SIM_BUSY.ordinal()] = 33;
            } catch (NoSuchFieldError unused33) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.SIM_FULL.ordinal()] = 34;
            } catch (NoSuchFieldError unused34) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.NO_SUBSCRIPTION.ordinal()] = 35;
            } catch (NoSuchFieldError unused35) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.NO_NETWORK_FOUND.ordinal()] = 36;
            } catch (NoSuchFieldError unused36) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.DEVICE_IN_USE.ordinal()] = 37;
            } catch (NoSuchFieldError unused37) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.ABORTED.ordinal()] = 38;
            } catch (NoSuchFieldError unused38) {
            }
        }
    }

    private void retrySendSmsForFail(SMSDispatcher.SmsTracker smsTracker, int i, int i2) {
        logi("retrySendSmsForFail, tracker.mRetryCount = " + smsTracker.mRetryCount);
        if (smsTracker.mRetryCount < getMaxSmsRetryCount()) {
            smsTracker.mRetryCount++;
            sendMessageDelayed(obtainMessage(3, smsTracker), getSmsRetryDelayValue());
        } else {
            logi("retrySendSmsForFail, send sms fail!");
            smsTracker.onFailed(this.mContext, i, i2);
            sendNtnSmsDot(smsTracker, i2, false);
        }
    }

    private void updateSmsStatusReporter(NtnSmsStatusReport ntnSmsStatusReport) {
        updateSmsStatusReporter(ntnSmsStatusReport.messageRef, ntnSmsStatusReport.status, ntnSmsStatusReport.tpStatus);
    }

    /* JADX WARN: Code duplicated, block: B:11:0x002f  */
    private void updateSmsStatusReporter(int i, int i2, int i3) {
        int iIntValue;
        HashMap map = this.mDeliveryPendingMapForNtn;
        if (map == null || map.isEmpty()) {
            loge("updateSmsStatusReporter is called, but delivery pending is empty");
            return;
        }
        if (i == 0) {
            Iterator it = this.mDeliveryPendingMapForNtn.entrySet().iterator();
            if (it.hasNext()) {
                iIntValue = ((Integer) ((Map.Entry) it.next()).getKey()).intValue();
            } else {
                iIntValue = i;
            }
        } else {
            iIntValue = i;
        }
        SMSDispatcher.SmsTracker smsTracker = (SMSDispatcher.SmsTracker) this.mDeliveryPendingMapForNtn.get(Integer.valueOf(iIntValue));
        if (smsTracker == null) {
            loge("updateSmsStatusReporter: message ref is wrong, messageRef = " + i);
            return;
        }
        logi("tpStatus = " + i3);
        if (i3 >= 64 || i3 < 32) {
            logi("start tp status");
            smsTracker.updateSentMessageStatus(this.mContext, i3);
            this.mDeliveryPendingMapForNtn.remove(Integer.valueOf(iIntValue));
        }
        if (!triggerDeliveryIntent(smsTracker, "3gpp", null, i3)) {
            Rlog.e("NtnSmsDispatcher", "handleSmsStatusReport: can not handle the status report!");
        }
        if (!MiuiTelephonyManagerStub.isSatelliteBox() || NtnBoxOptimization.getInstance() == null) {
            return;
        }
        NtnBoxOptimization.getInstance().onSendSmsStatusReport(this.mDeliveryPendingMapForNtn);
    }

    private boolean triggerDeliveryIntent(SMSDispatcher.SmsTracker smsTracker, String str, byte[] bArr, int i) {
        logi("start triggerDeliveryIntent");
        PendingIntent pendingIntent = smsTracker.mDeliveryIntent;
        Intent intent = new Intent();
        intent.putExtra("pdu", bArr);
        intent.putExtra("format", str);
        intent.putExtra("Status", i);
        intent.putExtra("isStatusReport", true);
        try {
            logi("send intent");
            pendingIntent.send(this.mContext, -1, intent);
            logi("send intent successfully");
            return true;
        } catch (PendingIntent.CanceledException unused) {
            return false;
        }
    }

    @Override // com.android.internal.telephony.SMSDispatcher
    public int nextMessageRef() {
        this.mNtnMessageRef = (this.mNtnMessageRef + 1) % CallFailCause.RADIO_UPLINK_FAILURE;
        logi("nextMessageRef = " + this.mNtnMessageRef);
        return this.mNtnMessageRef;
    }

    public void destroy() {
        clearNtnSmsHashMap();
    }

    public void clearNtnSmsHashMap() {
        logd("clearNtnSmsHashMap.");
        this.mDeliveryPendingMapForNtn.clear();
        this.mSendResultPendingMapForNtn.clear();
        this.mSendSmsDurationMap.clear();
    }

    private void logd(String str) {
        Rlog.d("NtnSmsDispatcher [Ntnphone]", str);
    }

    private void logi(String str) {
        Rlog.i("NtnSmsDispatcher [Ntnphone]", str);
    }

    private void loge(String str) {
        Rlog.e("NtnSmsDispatcher [Ntnphone]", str);
    }
}
