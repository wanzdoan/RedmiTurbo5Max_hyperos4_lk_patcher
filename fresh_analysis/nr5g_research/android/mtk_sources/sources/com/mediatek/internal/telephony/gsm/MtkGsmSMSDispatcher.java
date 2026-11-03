package com.mediatek.internal.telephony.gsm;

import android.R;
import android.app.PendingIntent;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SmsController;
import com.android.internal.telephony.SmsDispatchersController;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.gsm.GsmInboundSmsHandler;
import com.android.internal.telephony.gsm.GsmSMSDispatcher;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SmsMessage;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.util.SMSDispatcherUtil;
import com.mediatek.internal.telephony.MtkSmsDispatchersController;
import com.mediatek.internal.telephony.MtkSmsHeader;
import com.mediatek.internal.telephony.util.MtkSMSDispatcherUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class MtkGsmSMSDispatcher extends GsmSMSDispatcher {
    protected static final int EVENT_COPY_TEXT_MESSAGE_DONE = 106;
    private static final String TAG = "MtkGsmSMSDispatcher";
    private ThreadLocal<Integer> mEncodingType;
    protected Object mLock;
    private ThreadLocal<Integer> mOriginalPort;
    private boolean mStorageAvailable;
    private boolean mSuccess;
    protected int messageCountNeedCopy;
    private static final boolean ENG = "eng".equals(Build.TYPE);
    protected static String PDU_SIZE = "pdu_size";
    protected static String MSG_REF_NUM = "msg_ref_num";

    /* JADX WARN: Multi-variable type inference failed */
    public MtkGsmSMSDispatcher(Phone phone, SmsDispatchersController smsDispatchersController, GsmInboundSmsHandler gsmInboundSmsHandler, FeatureFlags featureFlags) {
        super(phone, smsDispatchersController, gsmInboundSmsHandler, featureFlags);
        this.mStorageAvailable = true;
        this.mSuccess = true;
        this.messageCountNeedCopy = 0;
        this.mLock = new Object();
        this.mOriginalPort = new ThreadLocal<Integer>(this) { // from class: com.mediatek.internal.telephony.gsm.MtkGsmSMSDispatcher.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public Integer initialValue() {
                return -1;
            }
        };
        this.mEncodingType = new ThreadLocal<Integer>(this) { // from class: com.mediatek.internal.telephony.gsm.MtkGsmSMSDispatcher.2
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public Integer initialValue() {
                return 0;
            }
        };
        this.mUiccController.unregisterForIccChanged(this);
        Integer phoneId = new Integer(this.mPhone.getPhoneId());
        this.mUiccController.registerForIccChanged(this, 15, phoneId);
        Rlog.d(TAG, "MtkGsmSMSDispatcher created");
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 15:
                Integer phoneId = getUiccControllerPhoneId(msg);
                if (phoneId.intValue() != this.mPhone.getPhoneId()) {
                    Rlog.d(TAG, "Wrong phone id event coming, PhoneId: " + phoneId);
                    return;
                } else {
                    Rlog.d(TAG, "EVENT_ICC_CHANGED, PhoneId: " + phoneId + " match exactly.");
                    onUpdateIccAvailability();
                    return;
                }
            case EVENT_COPY_TEXT_MESSAGE_DONE /* 106 */:
                AsyncResult ar = (AsyncResult) msg.obj;
                synchronized (this.mLock) {
                    this.mSuccess = ar.exception == null;
                    if (this.mSuccess) {
                        Rlog.d(TAG, "[copyText success to copy one");
                        this.messageCountNeedCopy--;
                    } else {
                        Rlog.d(TAG, "[copyText fail to copy one");
                        this.messageCountNeedCopy = 0;
                    }
                    this.mLock.notifyAll();
                    break;
                }
                return;
            default:
                super.handleMessage(msg);
                return;
        }
    }

    protected SMSDispatcher.SmsTracker getNewSubmitPduTracker(String callingPackage, int callingUser, String destinationAddress, String scAddress, String message, SmsHeader smsHeader, int encoding, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean lastPart, AtomicInteger unsentPartCount, AtomicBoolean anyPartFailed, Uri messageUri, String fullMessageText, int priority, boolean expectMore, int validityPeriod, long messageId, int messageRef, long uniqueMessageId, int uid) {
        if (ENG) {
            Rlog.d(TAG, "getNewSubmitPduTracker w/ validity");
        }
        SmsMessage.SubmitPdu pdu = MtkSmsMessage.getSubmitPdu(scAddress, destinationAddress, message, deliveryIntent != null, SmsHeader.toByteArray(smsHeader), encoding, smsHeader.languageTable, smsHeader.languageShiftTable, validityPeriod);
        if (pdu != null) {
            HashMap map = getSmsTrackerMap(destinationAddress, scAddress, message, pdu);
            return getSmsTracker(callingPackage, callingUser, map, sentIntent, deliveryIntent, getFormat(), unsentPartCount, anyPartFailed, messageUri, smsHeader, !lastPart || expectMore, fullMessageText, true, true, priority, validityPeriod, false, messageId, messageRef, false, uniqueMessageId, uid);
        }
        Rlog.e(TAG, "GsmSMSDispatcher.getNewSubmitPduTracker(): getSubmitPdu() returned null");
        return null;
    }

    private Integer getUiccControllerPhoneId(Message msg) {
        Integer phoneId = new Integer(-1);
        AsyncResult ar = (AsyncResult) msg.obj;
        if (ar != null && (ar.result instanceof Integer)) {
            return (Integer) ar.result;
        }
        return phoneId;
    }

    public void sendData(String callingPackage, int callingUser, String destAddr, String scAddr, int destPort, int originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        Rlog.d(TAG, "MtkGsmSmsDispatcher.sendData: enter");
        this.mOriginalPort.set(Integer.valueOf(originalPort));
        sendData(callingPackage, callingUser, destAddr, scAddr, destPort, data, sentIntent, deliveryIntent, false, 0L, -1);
        this.mOriginalPort.remove();
    }

    protected SmsMessageBase.SubmitPduBase onSendData(String scAddr, String destAddr, int destPort, byte[] data, boolean statusReportRequested, int messageRef) {
        if (this.mOriginalPort.get().intValue() == -1) {
            return super.onSendData(scAddr, destAddr, destPort, data, statusReportRequested, messageRef);
        }
        return MtkSmsMessage.getSubmitPdu(scAddr, destAddr, destPort, this.mOriginalPort.get().intValue(), data, statusReportRequested);
    }

    /* JADX WARN: Code duplicated, block: B:108:0x0195 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:111:0x01ee A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:121:0x020d A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:62:0x0135  */
    /* JADX WARN: Code duplicated, block: B:63:0x014f  */
    /* JADX WARN: Code duplicated, block: B:66:0x015e  */
    /* JADX WARN: Code duplicated, block: B:68:0x0176  */
    /* JADX WARN: Code duplicated, block: B:83:0x01b6  */
    /* JADX WARN: Code duplicated, block: B:85:0x01cf  */
    public int copyTextMessageToIccCard(String scAddress, String address, List<String> text, int status, long timestamp) {
        String scAddress2;
        boolean isDeliverPdu;
        int encoding;
        int i;
        String scAddress3;
        int encoding_method;
        int encoding_method2;
        byte[] smsHeader;
        int encoding2;
        SmsMessage.SubmitPdu pdu;
        MtkSmsMessage.DeliverPdu pdu2;
        Rlog.d(TAG, "GsmSMSDispatcher: copy text message to icc card");
        if (MtkSMSDispatcherUtil.checkPhoneNumber(scAddress)) {
            scAddress2 = scAddress;
        } else {
            Rlog.d(TAG, "[copyText invalid sc address");
            scAddress2 = null;
        }
        int i2 = 1;
        this.mSuccess = true;
        int msgCount = text.size();
        Rlog.d(TAG, "[copyText storage available");
        if (status == 1 || status == 3) {
            Rlog.d(TAG, "[copyText to encode deliver pdu");
            isDeliverPdu = true;
        } else if (status == 5 || status == 7) {
            Rlog.d(TAG, "[copyText to encode submit pdu");
            isDeliverPdu = false;
        } else {
            Rlog.d(TAG, "[copyText invalid status, default is deliver pdu");
            return 1;
        }
        if (!isDeliverPdu && !MtkSMSDispatcherUtil.checkPhoneNumber(address)) {
            Rlog.d(TAG, "[copyText invalid dest address");
            return 8;
        }
        Rlog.d(TAG, "[copyText msgCount " + msgCount);
        if (msgCount > 1) {
            Rlog.d(TAG, "[copyText multi-part message");
        } else {
            if (msgCount != 1) {
                Rlog.d(TAG, "[copyText invalid message count");
                return 1;
            }
            Rlog.d(TAG, "[copyText single-part message");
        }
        int refNumber = getNextConcatenatedRef() & 255;
        GsmAlphabet.TextEncodingDetails[] details = new GsmAlphabet.TextEncodingDetails[msgCount];
        int i3 = 0;
        int encoding3 = 0;
        while (true) {
            encoding = 0;
            if (i3 >= msgCount) {
                break;
            }
            details[i3] = MtkSmsMessage.calculateLength(text.get(i3), false);
            if (encoding3 != details[i3].codeUnitSize && (encoding3 == 0 || encoding3 == 1)) {
                encoding3 = details[i3].codeUnitSize;
            }
            i3++;
        }
        int i4 = 0;
        while (true) {
            boolean z = this.mSuccess;
            if (i4 < msgCount) {
                if (!z) {
                    Rlog.d(TAG, "[copyText Exception happened when copy message");
                    return i2;
                }
                int singleShiftId = -1;
                int language = details[i4].shiftLangId;
                int encoding_method3 = encoding3;
                if (encoding3 != i2) {
                    i = encoding;
                    scAddress3 = scAddress2;
                } else {
                    i = encoding;
                    scAddress3 = scAddress2;
                    Rlog.d(TAG, "Detail: " + i4 + " ted" + details[i4]);
                    if (details[i4].useLockingShift && details[i4].useSingleShift) {
                        singleShiftId = language;
                        encoding_method = 13;
                        encoding_method2 = language;
                    } else if (details[i4].useLockingShift) {
                        encoding_method = 12;
                        encoding_method2 = language;
                    } else if (details[i4].useSingleShift) {
                        singleShiftId = language;
                        encoding_method = 11;
                        encoding_method2 = -1;
                    }
                    if (msgCount > 1) {
                        Rlog.d(TAG, "[copyText get pdu header for multi-part message");
                        byte[] smsHeader2 = MtkSmsHeader.getSubmitPduHeaderWithLang(-1, refNumber, i4 + 1, msgCount, singleShiftId, encoding_method2);
                        smsHeader = smsHeader2;
                    } else {
                        smsHeader = null;
                    }
                    if (isDeliverPdu) {
                        int encoding4 = encoding3;
                        scAddress2 = scAddress3;
                        pdu2 = MtkSmsMessage.getDeliverPduWithLang(scAddress2, address, text.get(i4), smsHeader, timestamp, encoding4, language);
                        encoding2 = encoding4;
                        if (pdu2 != null) {
                            Rlog.d(TAG, "[copyText write deliver pdu into SIM");
                            this.mCi.writeSmsToSim(status, IccUtils.bytesToHexString(pdu2.encodedScAddress), IccUtils.bytesToHexString(pdu2.encodedMessage), obtainMessage(EVENT_COPY_TEXT_MESSAGE_DONE));
                            synchronized (this.mLock) {
                                try {
                                    try {
                                        Rlog.d(TAG, "[copyText wait until the message be wrote in SIM");
                                        this.mLock.wait();
                                    } catch (InterruptedException e) {
                                        Rlog.d(TAG, "Fail to copy text message into SIM");
                                        return 1;
                                    }
                                } catch (Throwable th) {
                                    throw th;
                                }
                            }
                        }
                    } else {
                        scAddress2 = scAddress3;
                        encoding2 = encoding3;
                        pdu = MtkSmsMessage.getSubmitPduWithLang(scAddress2, address, text.get(i4), false, smsHeader, encoding_method, language, -1);
                        if (pdu != null) {
                            Rlog.d(TAG, "[copyText write submit pdu into SIM");
                            this.mCi.writeSmsToSim(status, IccUtils.bytesToHexString(pdu.encodedScAddress), IccUtils.bytesToHexString(pdu.encodedMessage), obtainMessage(EVENT_COPY_TEXT_MESSAGE_DONE));
                            synchronized (this.mLock) {
                                try {
                                    try {
                                        Rlog.d(TAG, "[copyText wait until the message be wrote in SIM");
                                        this.mLock.wait();
                                    } catch (InterruptedException e2) {
                                        Rlog.d(TAG, "fail to copy text message into SIM");
                                        return 1;
                                    }
                                } catch (Throwable th2) {
                                    throw th2;
                                }
                            }
                        } else {
                            continue;
                        }
                    }
                    Rlog.d(TAG, "[copyText thread is waked up");
                    i4++;
                    encoding = i;
                    encoding3 = encoding2;
                    msgCount = msgCount;
                    refNumber = refNumber;
                    i2 = 1;
                }
                encoding_method = encoding_method3;
                encoding_method2 = -1;
                if (msgCount > 1) {
                    Rlog.d(TAG, "[copyText get pdu header for multi-part message");
                    byte[] smsHeader3 = MtkSmsHeader.getSubmitPduHeaderWithLang(-1, refNumber, i4 + 1, msgCount, singleShiftId, encoding_method2);
                    smsHeader = smsHeader3;
                } else {
                    smsHeader = null;
                }
                if (isDeliverPdu) {
                    int encoding5 = encoding3;
                    scAddress2 = scAddress3;
                    pdu2 = MtkSmsMessage.getDeliverPduWithLang(scAddress2, address, text.get(i4), smsHeader, timestamp, encoding5, language);
                    encoding2 = encoding5;
                    if (pdu2 != null) {
                        Rlog.d(TAG, "[copyText write deliver pdu into SIM");
                        this.mCi.writeSmsToSim(status, IccUtils.bytesToHexString(pdu2.encodedScAddress), IccUtils.bytesToHexString(pdu2.encodedMessage), obtainMessage(EVENT_COPY_TEXT_MESSAGE_DONE));
                        synchronized (this.mLock) {
                            Rlog.d(TAG, "[copyText wait until the message be wrote in SIM");
                            this.mLock.wait();
                        }
                    }
                } else {
                    scAddress2 = scAddress3;
                    encoding2 = encoding3;
                    pdu = MtkSmsMessage.getSubmitPduWithLang(scAddress2, address, text.get(i4), false, smsHeader, encoding_method, language, -1);
                    if (pdu != null) {
                        Rlog.d(TAG, "[copyText write submit pdu into SIM");
                        this.mCi.writeSmsToSim(status, IccUtils.bytesToHexString(pdu.encodedScAddress), IccUtils.bytesToHexString(pdu.encodedMessage), obtainMessage(EVENT_COPY_TEXT_MESSAGE_DONE));
                        synchronized (this.mLock) {
                            Rlog.d(TAG, "[copyText wait until the message be wrote in SIM");
                            this.mLock.wait();
                        }
                    } else {
                        continue;
                    }
                }
                Rlog.d(TAG, "[copyText thread is waked up");
                i4++;
                encoding = i;
                encoding3 = encoding2;
                msgCount = msgCount;
                refNumber = refNumber;
                i2 = 1;
            } else {
                int i5 = encoding;
                if (z) {
                    Rlog.d(TAG, "[copyText all messages have been copied into SIM");
                    return i5;
                }
                Rlog.d(TAG, "[copyText copy failed");
                return 1;
            }
        }
    }

    private boolean isValidSmsAddress(String address) {
        String encodedAddress = PhoneNumberUtils.extractNetworkPortion(address);
        return encodedAddress == null || encodedAddress.length() == address.length();
    }

    public void sendTextWithEncodingType(String destAddr, String scAddr, String text, int encodingType, PendingIntent sentIntent, PendingIntent deliveryIntent, Uri messageUri, String callingPkg, int callingUser, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        Rlog.d(TAG, "sendTextWithEncodingType encoding = " + encodingType);
        this.mEncodingType.set(Integer.valueOf(encodingType));
        sendText(destAddr, scAddr, text, sentIntent, deliveryIntent, messageUri, callingPkg, callingUser, persistMessage, priority, expectMore, validityPeriod, false, 0L, -1);
        this.mEncodingType.remove();
    }

    protected SmsMessageBase.SubmitPduBase onSendText(String scAddr, String destAddr, String text, boolean statusReportRequested, Uri messageUri, String callingPkg, boolean persistMessage, int priority, boolean expectMore, int validityPeriod, int messageRef) {
        int encodingType = this.mEncodingType.get().intValue();
        if (encodingType == 0) {
            return super.onSendText(scAddr, destAddr, text, statusReportRequested, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod, messageRef);
        }
        GsmAlphabet.TextEncodingDetails details = SMSDispatcherUtil.calculateLength(false, text, false);
        return SmsMessage.getSubmitPdu(scAddr, destAddr, text, statusReportRequested, (byte[]) null, encodingType, details.languageTable, details.languageShiftTable, validityPeriod, messageRef);
    }

    public void sendMultipartTextWithEncodingType(String destAddr, String scAddr, ArrayList<String> parts, int encodingType, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, Uri messageUri, String callingPkg, int callingUser, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        Rlog.d(TAG, "sendMultipartTextWithEncodingType encoding = " + encodingType);
        this.mEncodingType.set(Integer.valueOf(encodingType));
        sendMultipartText(destAddr, scAddr, parts, sentIntents, deliveryIntents, messageUri, callingPkg, callingUser, persistMessage, priority, expectMore, validityPeriod, 0L, 0L, -1);
        this.mEncodingType.remove();
    }

    protected int onSendMultipartText(String destAddr, String scAddr, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, Uri messageUri, String callingPkg, boolean persistMessage, int priority, boolean expectMore, int validityPeriod, GsmAlphabet.TextEncodingDetails[] encodingForParts) {
        int encodingType = this.mEncodingType.get().intValue();
        if (encodingType == 0) {
            int encodingType2 = super.onSendMultipartText(destAddr, scAddr, parts, sentIntents, deliveryIntents, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod, encodingForParts);
            Rlog.d(TAG, "onSendMultipartText encoding = " + encodingType2);
            return encodingType2;
        }
        int msgCount = parts.size();
        for (int i = 0; i < msgCount; i++) {
            GsmAlphabet.TextEncodingDetails details = SMSDispatcherUtil.calculateLength(false, parts.get(i), false);
            if (encodingType != details.codeUnitSize && (encodingType == 0 || encodingType == 1)) {
                Rlog.d(TAG, "[enc conflict between details[" + details.codeUnitSize + "] and encoding " + encodingType);
                details.codeUnitSize = encodingType;
            }
            encodingForParts[i] = details;
        }
        return encodingType;
    }

    public void handleIccFull() {
        this.mGsmInboundSmsHandler.mStorageMonitor.handleIccFull();
    }

    public void handleQueryCbActivation(AsyncResult ar) {
        Boolean result = null;
        if (ar.exception == null) {
            ArrayList<SmsBroadcastConfigInfo> list = (ArrayList) ar.result;
            if (list.size() == 0) {
                result = new Boolean(false);
            } else {
                SmsBroadcastConfigInfo cbConfig = list.get(0);
                Rlog.d(TAG, "cbConfig: " + cbConfig.toString());
                if (cbConfig.getFromCodeScheme() == -1 && cbConfig.getToCodeScheme() == -1 && cbConfig.getFromServiceId() == -1 && cbConfig.getToServiceId() == -1 && !cbConfig.isSelected()) {
                    result = new Boolean(false);
                } else {
                    result = new Boolean(true);
                }
            }
        }
        Rlog.d(TAG, "queryCbActivation: " + result);
        AsyncResult.forMessage((Message) ar.userObj, result, ar.exception);
        ((Message) ar.userObj).sendToTarget();
    }

    public void setSmsMemoryStatus(boolean status) {
        if (status != this.mStorageAvailable) {
            this.mStorageAvailable = status;
            this.mCi.reportSmsMemoryStatus(status, (Message) null);
        }
    }

    public boolean isSmsReady() {
        MtkSmsDispatchersController pSmsDispatcherctrl = (MtkSmsDispatchersController) this.mSmsDispatchersController;
        return pSmsDispatcherctrl.isSmsReady();
    }

    protected SMSDispatcher.SmsTracker getSmsTracker(String callingPackage, int callingUser, HashMap<String, Object> data, PendingIntent sentIntent, PendingIntent deliveryIntent, String format, AtomicInteger unsentPartCount, AtomicBoolean anyPartFailed, Uri messageUri, SmsHeader smsHeader, boolean isExpectMore, String fullMessageText, boolean isText, boolean persistMessage, boolean isForVvm, long messageId, int messageRef, long uniqueMessageId) {
        SMSDispatcher.SmsTracker tracker = super.getSmsTracker(callingPackage, callingUser, data, sentIntent, deliveryIntent, format, unsentPartCount, anyPartFailed, messageUri, smsHeader, isExpectMore, fullMessageText, isText, persistMessage, -1, -1, isForVvm, messageId, messageRef, false, uniqueMessageId, -1);
        return tracker;
    }

    public boolean isImsEx() {
        boolean isCheckSmsImsCap = SystemProperties.getInt("ro.ril.check_sms_ims_capability", 0) == 1;
        if (!isCheckSmsImsCap) {
            return isIms();
        }
        if (this.mSmsDispatchersController != null) {
            return ((MtkSmsDispatchersController) this.mSmsDispatchersController).isImsEx();
        }
        Rlog.e(TAG, "mSmsDispatchersController is null");
        return false;
    }

    private boolean isSmsInCSService() {
        ServiceState ss = this.mPhone.getServiceState();
        NetworkRegistrationInfo regState = ss.getNetworkRegistrationInfo(1, 1);
        if (regState != null) {
            return regState.isInService();
        }
        return false;
    }

    private boolean isSmsInService() {
        int ss = this.mPhone.getServiceState().getState();
        boolean isCheckSmsImsCap = SystemProperties.getInt("ro.ril.check_sms_ims_capability", 0) == 1;
        if (!isCheckSmsImsCap) {
            return ss == 0;
        }
        if (ss != 0) {
            return false;
        }
        if (isSmsInCSService()) {
            return true;
        }
        return isSmsCapable();
    }

    private boolean isSmsCapable() {
        if (this.mSmsDispatchersController != null) {
            return ((MtkSmsDispatchersController) this.mSmsDispatchersController).isSmsCapable();
        }
        Rlog.e(TAG, "mSmsDispatchersController is null");
        return false;
    }

    private boolean isAllowSMSWhenEPSOnly() {
        boolean isAllowSMS = SystemProperties.getInt("ro.ril.allow_sms_eps_only", 0) == 1;
        if (isAllowSMS && this.mPhone.getServiceState().getDataRegState() == 0) {
            String mccmnc = this.mTelephonyManager.getSimOperatorNumericForPhone(this.mPhone.getPhoneId());
            String plmn = this.mPhone.getServiceState().getOperatorNumeric();
            if (mccmnc.equals("302720") && (plmn.equals("302722") || plmn.equals("302723"))) {
                return true;
            }
        }
        return false;
    }

    protected void sendSms(SMSDispatcher.SmsTracker tracker) {
        int ss = this.mPhone.getServiceState().getState();
        Rlog.d(TAG, "sendSms:  isImsEx()=" + isImsEx() + " mRetryCount=" + tracker.mRetryCount + " mImsRetry=" + tracker.mImsRetry + " mMessageRef=" + tracker.mMessageRef + " mUsesImsServiceForIms=" + tracker.mUsesImsServiceForIms + " SS=" + ss + " " + SmsController.formatCrossStackMessageId(tracker.mMessageId));
        boolean allowCheckMessageInNotConnected = this.mContext.getResources().getBoolean(R.bool.config_supportProfilesOnNonMainUser);
        boolean mtPollingMessageThatsAllowedInOOS = tracker.isMtSmsPollingMessage(this.mContext) && allowCheckMessageInNotConnected;
        if (!isImsEx() && !isSmsInService() && !mtPollingMessageThatsAllowedInOOS && this.mPhone.getServiceState().getRilDataRadioTechnology() != 20 && !isAllowSMSWhenEPSOnly()) {
            tracker.onFailed(this.mContext, getNotInServiceError(ss), -1);
            notifySmsSentFailedToEmergencyStateTracker(tracker, false);
            return;
        }
        Message reply = obtainMessage(2, tracker);
        HashMap<String, Object> map = tracker.getData();
        byte[] pdu = (byte[]) map.get("pdu");
        byte[] smsc = (byte[]) map.get("smsc");
        if (tracker.mRetryCount > 0 && (pdu[0] & 1) == 1) {
            pdu[0] = (byte) (pdu[0] | 4);
            pdu[1] = (byte) tracker.mMessageRef;
        }
        if ((tracker.mImsRetry == 0 && !isImsEx()) || tracker.mUsesImsServiceForIms) {
            if (tracker.mRetryCount == 0 && tracker.mExpectMore) {
                this.mCi.sendSMSExpectMore(IccUtils.bytesToHexString(smsc), IccUtils.bytesToHexString(pdu), reply);
                return;
            } else {
                this.mCi.sendSMS(IccUtils.bytesToHexString(smsc), IccUtils.bytesToHexString(pdu), reply);
                return;
            }
        }
        this.mCi.sendImsGsmSms(IccUtils.bytesToHexString(smsc), IccUtils.bytesToHexString(pdu), tracker.mImsRetry, tracker.mMessageRef, reply);
        tracker.mImsRetry++;
    }
}
