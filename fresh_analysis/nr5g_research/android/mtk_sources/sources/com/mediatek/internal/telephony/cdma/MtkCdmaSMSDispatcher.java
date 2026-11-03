package com.mediatek.internal.telephony.cdma;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Message;
import android.telephony.Rlog;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SmsDispatchersController;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsResponse;
import com.android.internal.telephony.cdma.CdmaSMSDispatcher;
import com.android.internal.telephony.cdma.SmsMessage;
import com.mediatek.internal.telephony.util.MtkSMSDispatcherUtil;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MtkCdmaSMSDispatcher extends CdmaSMSDispatcher {
    private static final boolean ENG = "eng".equals(Build.TYPE);
    private static final int EVENT_COPY_TEXT_MESSAGE_DONE = 106;
    private static final int RESULT_ERROR_RUIM_PLUG_OUT = 107;
    private static final int RESULT_ERROR_SUCCESS = 0;
    private static final String TAG = "MtkCdmaSMSDispatcher";
    private static final boolean VDBG = false;
    private static final int WAKE_LOCK_TIMEOUT = 500;
    private boolean mCopied;
    private ThreadLocal<Integer> mEncodingType;
    protected Object mLock;
    private ThreadLocal<Integer> mOriginalPort;
    protected boolean mSuccess;

    public MtkCdmaSMSDispatcher(Phone phone, SmsDispatchersController smsDispatchersController, FeatureFlags featureFlags) {
        super(phone, smsDispatchersController, featureFlags);
        this.mLock = new Object();
        this.mCopied = VDBG;
        this.mSuccess = true;
        this.mOriginalPort = new ThreadLocal<Integer>(this) { // from class: com.mediatek.internal.telephony.cdma.MtkCdmaSMSDispatcher.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public Integer initialValue() {
                return -1;
            }
        };
        this.mEncodingType = new ThreadLocal<Integer>(this) { // from class: com.mediatek.internal.telephony.cdma.MtkCdmaSMSDispatcher.2
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public Integer initialValue() {
                return 0;
            }
        };
        Rlog.d(TAG, "MtkCdmaSMSDispatcher created");
    }

    public void sendSms(SMSDispatcher.SmsTracker tracker) {
        int ss = this.mPhone.getServiceState().getState();
        if (!isIms() && ss != 0) {
            if (isSimAbsent()) {
                tracker.onFailed(this.mContext, 1, 0);
                return;
            } else {
                tracker.onFailed(this.mContext, getNotInServiceError(ss), 0);
                return;
            }
        }
        super.sendSms(tracker);
    }

    public void sendData(String callingPackage, int callingUser, String destAddr, String scAddr, int destPort, int originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        this.mOriginalPort.set(Integer.valueOf(originalPort));
        sendData(callingPackage, callingUser, destAddr, scAddr, destPort, data, sentIntent, deliveryIntent, VDBG, 0L, -1);
        this.mOriginalPort.remove();
    }

    protected SmsMessageBase.SubmitPduBase onSendData(String scAddr, String destAddr, int destPort, byte[] data, boolean statusReportRequested, int messageRef) {
        if (this.mOriginalPort.get().intValue() == -1) {
            return super.onSendData(scAddr, destAddr, destPort, data, statusReportRequested, messageRef);
        }
        return MtkSmsMessage.getSubmitPdu(scAddr, destAddr, destPort, this.mOriginalPort.get().intValue(), data, statusReportRequested);
    }

    public int copyTextMessageToIccCard(String str, String str2, List<String> list, int i, long j) {
        boolean z;
        SmsMessage.SubmitPdu submitPduCreateEfPdu;
        this.mSuccess = true;
        int size = list.size();
        Rlog.d(TAG, "copyTextMessageToIccCard status = " + i + ", msgCount = " + size);
        if (size < 1) {
            return 1;
        }
        if (i == 1 || i == 3) {
            Rlog.d(TAG, "copyTextMessageToIccCard to encode deliver pdu");
            z = true;
        } else if (i == 5 || i == 7) {
            Rlog.d(TAG, "copyTextMessageToIccCard to encode submit pdu");
            z = VDBG;
        } else {
            Rlog.d(TAG, "copyTextMessageToIccCard invalid status, default is deliver pdu");
            return 1;
        }
        if (!z && !MtkSMSDispatcherUtil.checkPhoneNumber(str2)) {
            Rlog.d(TAG, "copyTextMessageToIccCard invalid dest address");
            return 8;
        }
        int i2 = 0;
        while (true) {
            boolean z2 = this.mSuccess;
            if (i2 < size) {
                if (!z2 || (submitPduCreateEfPdu = MtkSmsMessage.createEfPdu(str2, list.get(i2), j)) == null) {
                    return 1;
                }
                this.mCi.writeSmsToRuim(i, submitPduCreateEfPdu.encodedMessage, obtainMessage(EVENT_COPY_TEXT_MESSAGE_DONE));
                synchronized (this.mLock) {
                    this.mCopied = VDBG;
                    while (!this.mCopied) {
                        try {
                            this.mLock.wait();
                        } catch (InterruptedException e) {
                            return 1;
                        }
                    }
                }
                i2++;
            } else {
                return 1 ^ (z2 ? 1 : 0);
            }
        }
    }

    public void sendTextWithEncodingType(String destAddr, String scAddr, String text, int encodingType, PendingIntent sentIntent, PendingIntent deliveryIntent, Uri messageUri, String callingPkg, int callingUser, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        Rlog.d(TAG, "sendTextWithEncodingType encoding = " + encodingType);
        this.mEncodingType.set(Integer.valueOf(encodingType));
        sendText(destAddr, scAddr, text, sentIntent, deliveryIntent, messageUri, callingPkg, callingUser, persistMessage, priority, expectMore, validityPeriod, VDBG, 0L, -1);
        this.mEncodingType.remove();
    }

    protected SmsMessageBase.SubmitPduBase onSendText(String scAddr, String destAddr, String text, boolean statusReportRequested, Uri messageUri, String callingPkg, boolean persistMessage, int priority, boolean expectMore, int validityPeriod, int messageRef) {
        int encodingType = this.mEncodingType.get().intValue();
        if (encodingType == 1 || encodingType == 2 || encodingType == 3) {
            return MtkSmsMessage.getSubmitPdu(scAddr, destAddr, text, statusReportRequested, null, encodingType, validityPeriod, priority, true);
        }
        return super.onSendText(scAddr, destAddr, text, statusReportRequested, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod, messageRef);
    }

    public void sendMultipartTextWithEncodingType(String destAddr, String scAddr, ArrayList<String> parts, int encodingType, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, Uri messageUri, String callingPkg, int callingUser, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        Rlog.d(TAG, "sendMultipartTextWithEncodingType encoding = " + encodingType);
        this.mEncodingType.set(Integer.valueOf(encodingType));
        sendMultipartText(destAddr, scAddr, parts, sentIntents, deliveryIntents, messageUri, callingPkg, callingUser, persistMessage, priority, expectMore, validityPeriod, 0L, 0L, -1);
        this.mEncodingType.remove();
    }

    protected int onSendMultipartText(String destAddr, String scAddr, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, Uri messageUri, String callingPkg, boolean persistMessage, int priority, boolean expectMore, int validityPeriod, GsmAlphabet.TextEncodingDetails[] encodingForParts) {
        int encodingType = this.mEncodingType.get().intValue();
        if (encodingType == 1 || encodingType == 2 || encodingType == 3) {
            int msgCount = parts.size();
            for (int i = 0; i < msgCount; i++) {
                GsmAlphabet.TextEncodingDetails details = MtkSmsMessage.calculateLength(parts.get(i), VDBG, encodingType);
                details.codeUnitSize = encodingType;
                encodingForParts[i] = details;
            }
            return encodingType;
        }
        return super.onSendMultipartText(destAddr, scAddr, parts, sentIntents, deliveryIntents, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod, encodingForParts);
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case EVENT_COPY_TEXT_MESSAGE_DONE /* 106 */:
                AsyncResult ar = (AsyncResult) msg.obj;
                synchronized (this.mLock) {
                    this.mSuccess = ar.exception == null ? true : VDBG;
                    this.mCopied = true;
                    this.mLock.notifyAll();
                    break;
                }
                return;
            default:
                super.handleMessage(msg);
                return;
        }
    }

    protected void handleSendComplete(AsyncResult ar) {
        SMSDispatcher.SmsTracker tracker = (SMSDispatcher.SmsTracker) ar.userObj;
        if (ar.exception != null && ar.result != null) {
            int errorCode = ((SmsResponse) ar.result).mErrorCode;
            if (errorCode == RESULT_ERROR_RUIM_PLUG_OUT) {
                Rlog.d(TAG, "RUIM card is plug out");
                tracker.onFailed(this.mContext, 1, errorCode);
                return;
            }
            int ss = this.mPhone.getServiceState().getState();
            if (!isIms() && ss != 0 && isSimAbsent()) {
                tracker.onFailed(this.mContext, 1, errorCode);
                return;
            }
        }
        super.handleSendComplete(ar);
    }

    private boolean isSimAbsent() {
        IccCardConstants.State state;
        IccCard card = PhoneFactory.getPhone(this.mPhone.getPhoneId()).getIccCard();
        if (card == null) {
            state = IccCardConstants.State.UNKNOWN;
        } else {
            state = card.getState();
        }
        boolean ret = (state == IccCardConstants.State.ABSENT || state == IccCardConstants.State.NOT_READY) ? true : VDBG;
        Rlog.d(TAG, "isSimAbsent state = " + state + " ret=" + ret);
        return ret;
    }
}
