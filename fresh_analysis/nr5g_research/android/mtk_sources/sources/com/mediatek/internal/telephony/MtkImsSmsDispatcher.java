package com.mediatek.internal.telephony;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.telephony.Rlog;
import com.android.ims.FeatureConnector;
import com.android.ims.ImsManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.ImsSmsDispatcher;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SmsDispatchersController;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.util.SMSDispatcherUtil;
import com.mediatek.internal.telephony.util.MtkSMSDispatcherUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class MtkImsSmsDispatcher extends ImsSmsDispatcher {
    private static final boolean ENG = "eng".equals(Build.TYPE);
    private static final String TAG = "MtkImsSmsDispacher";
    private ThreadLocal<Integer> mEncodingType;
    private BroadcastReceiver mImsSmsIntentReceiver;

    protected SmsMessageBase.SubmitPduBase getSubmitPdu(String scAddr, String destAddr, int destinationPort, int originalPort, byte[] data, boolean statusReportRequested) {
        return MtkSMSDispatcherUtil.getSubmitPdu(isCdmaMo(), scAddr, destAddr, destinationPort, originalPort, data, statusReportRequested);
    }

    protected SmsMessageBase.SubmitPduBase getSubmitPdu(String scAddr, String destAddr, byte[] data, byte[] smsHeader, boolean statusReportRequested) {
        return MtkSMSDispatcherUtil.getSubmitPdu(isCdmaMo(), scAddr, destAddr, data, smsHeader, statusReportRequested);
    }

    protected SmsMessageBase.SubmitPduBase getSubmitPdu(boolean isCdma, String scAddress, String destinationAddress, String message, boolean statusReportRequested, byte[] header, int encoding, int languageTable, int languageShiftTable, int validityPeriod, int messageRef) {
        return MtkSMSDispatcherUtil.getSubmitPdu(isCdmaMo(), scAddress, destinationAddress, message, statusReportRequested, header, encoding, languageTable, languageShiftTable, validityPeriod, messageRef);
    }

    public MtkImsSmsDispatcher(Phone phone, SmsDispatchersController smsDispatchersController, FeatureFlags featureFlags) {
        super(phone, smsDispatchersController, new ImsSmsDispatcher.FeatureConnectorFactory() { // from class: com.mediatek.internal.telephony.MtkImsSmsDispatcher$$ExternalSyntheticLambda0
            public final FeatureConnector create(Context context, int i, String str, FeatureConnector.Listener listener, Executor executor) {
                return ImsManager.getConnector(context, i, str, listener, executor);
            }
        }, featureFlags);
        this.mEncodingType = new ThreadLocal<Integer>(this) { // from class: com.mediatek.internal.telephony.MtkImsSmsDispatcher.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public Integer initialValue() {
                return 0;
            }
        };
        this.mImsSmsIntentReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.MtkImsSmsDispatcher.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                Rlog.d(MtkImsSmsDispatcher.TAG, "mImsSmsIntentReceiver: action " + intent.getAction());
                if (intent.getAction().equals("com.android.ims.IMS_SERVICE_DOWN")) {
                    for (Map.Entry<Integer, SMSDispatcher.SmsTracker> entry : MtkImsSmsDispatcher.this.mTrackers.entrySet()) {
                        SMSDispatcher.SmsTracker tracker = entry.getValue();
                        tracker.onFailed(MtkImsSmsDispatcher.this.mContext, 4, -1);
                    }
                    MtkImsSmsDispatcher.this.mTrackers.clear();
                }
            }
        };
        Rlog.d(TAG, "Created!");
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.ims.IMS_SERVICE_DOWN");
        this.mContext.registerReceiver(this.mImsSmsIntentReceiver, filter);
    }

    protected void sendData(String callingPackage, int callingUser, String destAddr, String scAddr, int destPort, int originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        if (!isCdmaMo()) {
            sendDataGsm(callingPackage, callingUser, destAddr, scAddr, destPort, originalPort, data, sentIntent, deliveryIntent);
        } else {
            super.sendData(callingPackage, callingUser, destAddr, scAddr, destPort, data, sentIntent, deliveryIntent, false, 0L, -1);
        }
    }

    public void sendTextWithEncodingType(String destAddr, String scAddr, String text, int encodingType, PendingIntent sentIntent, PendingIntent deliveryIntent, Uri messageUri, String callingPkg, int callingUser, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        if (!isCdmaMo()) {
            sendTextWithEncodingTypeGsm(destAddr, scAddr, text, encodingType, sentIntent, deliveryIntent, messageUri, callingPkg, callingUser, persistMessage, priority, expectMore, validityPeriod);
        } else {
            sendText(destAddr, scAddr, text, sentIntent, deliveryIntent, messageUri, callingPkg, callingUser, persistMessage, priority, expectMore, validityPeriod, false, 0L, -1);
        }
    }

    public void sendMultipartTextWithEncodingType(String destAddr, String scAddr, ArrayList<String> parts, int encodingType, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, Uri messageUri, String callingPkg, int callingUser, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        if (!isCdmaMo()) {
            sendMultipartTextWithEncodingTypeGsm(destAddr, scAddr, parts, encodingType, sentIntents, deliveryIntents, messageUri, callingPkg, callingUser, persistMessage, priority, expectMore, validityPeriod);
        } else {
            sendMultipartText(destAddr, scAddr, parts, sentIntents, deliveryIntents, messageUri, callingPkg, callingUser, persistMessage, priority, expectMore, validityPeriod, 0L, 0L, -1);
        }
    }

    protected SMSDispatcher.SmsTracker getNewSubmitPduTracker(String callingPackage, int callingUser, String destinationAddress, String scAddress, String message, SmsHeader smsHeader, int encoding, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean lastPart, AtomicInteger unsentPartCount, AtomicBoolean anyPartFailed, Uri messageUri, String fullMessageText, int priority, boolean expectMore, int validityPeriod, long messageId, int messageRef, long uniqueMessageId, int uid) {
        if (ENG) {
            Rlog.d(TAG, "getNewSubmitPduTracker w/ validity");
        }
        if (isCdmaMo()) {
            return super.getNewSubmitPduTracker(callingPackage, callingUser, destinationAddress, scAddress, message, smsHeader, encoding, sentIntent, deliveryIntent, lastPart, unsentPartCount, anyPartFailed, messageUri, fullMessageText, priority, expectMore, validityPeriod, messageId, messageRef, uniqueMessageId, uid);
        }
        return getNewSubmitPduTrackerGsm(callingPackage, callingUser, destinationAddress, scAddress, message, smsHeader, encoding, sentIntent, deliveryIntent, lastPart, unsentPartCount, anyPartFailed, messageUri, fullMessageText, priority, expectMore, validityPeriod, messageRef);
    }

    protected void sendDataGsm(String callingPackage, int callingUser, String destAddr, String scAddr, int destPort, int originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        Rlog.d(TAG, "sendData: enter");
        SmsMessageBase.SubmitPduBase pdu = getSubmitPdu(scAddr, destAddr, destPort, originalPort, data, deliveryIntent != null);
        if (pdu != null) {
            HashMap map = getSmsTrackerMap(destAddr, scAddr, destPort, data, pdu);
            int messageRef = nextMessageRef();
            SMSDispatcher.SmsTracker tracker = getSmsTracker(callingPackage, callingUser, map, sentIntent, deliveryIntent, getFormat(), null, false, null, false, true, false, 0L, messageRef);
            if (!sendSmsByCarrierApp(true, tracker)) {
                sendSubmitPdu(tracker);
                return;
            }
            return;
        }
        Rlog.e(TAG, "sendData(): getSubmitPdu() returned null");
    }

    public void sendTextWithEncodingTypeGsm(String destAddr, String scAddr, String text, int encodingType, PendingIntent sentIntent, PendingIntent deliveryIntent, Uri messageUri, String callingPkg, int callingUser, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        Rlog.d(TAG, "sendTextWithEncodingTypeGsm encoding = " + encodingType);
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
        return getSubmitPdu(false, scAddr, destAddr, text, statusReportRequested, null, encodingType, details.languageTable, details.languageShiftTable, validityPeriod, messageRef);
    }

    public void sendMultipartTextWithEncodingTypeGsm(String destAddr, String scAddr, ArrayList<String> parts, int encodingType, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, Uri messageUri, String callingPkg, int callingUser, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
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

    protected SMSDispatcher.SmsTracker getSmsTracker(String callingPackage, int callingUser, HashMap<String, Object> data, PendingIntent sentIntent, PendingIntent deliveryIntent, String format, Uri messageUri, boolean expectMore, String fullMessageText, boolean isText, boolean persistMessage, boolean isForVvm, long messageId, int messageRef) {
        return getSmsTracker(callingPackage, callingUser, data, sentIntent, deliveryIntent, format, null, null, messageUri, null, expectMore, fullMessageText, isText, persistMessage, -1, -1, isForVvm, messageId, messageRef, false, 0L, -1);
    }

    private SMSDispatcher.SmsTracker getNewSubmitPduTrackerGsm(String callingPackage, int callingUser, String destinationAddress, String scAddress, String message, SmsHeader smsHeader, int encoding, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean lastPart, AtomicInteger unsentPartCount, AtomicBoolean anyPartFailed, Uri messageUri, String fullMessageText, int priority, boolean expectMore, int validityPeriod, int messageRef) {
        boolean z = true;
        SmsMessageBase.SubmitPduBase pdu = getSubmitPdu(false, scAddress, destinationAddress, message, deliveryIntent != null, SmsHeader.toByteArray(smsHeader), encoding, smsHeader.languageTable, smsHeader.languageShiftTable, validityPeriod, messageRef);
        if (pdu != null) {
            HashMap map = getSmsTrackerMap(destinationAddress, scAddress, message, pdu);
            String format = getFormat();
            if (lastPart && !expectMore) {
                z = false;
            }
            return getSmsTracker(callingPackage, callingUser, map, sentIntent, deliveryIntent, format, unsentPartCount, anyPartFailed, messageUri, smsHeader, z, fullMessageText, true, true, priority, validityPeriod, false, 0L, messageRef, false, 0L, -1);
        }
        Rlog.e(TAG, "getNewSubmitPduTrackerGsm: getSubmitPdu() returned null");
        return null;
    }

    private boolean isLimitedService() {
        return this.mPhone.getServiceState().isEmergencyOnly();
    }

    protected boolean isEmergencySmsPossible() {
        return super.isEmergencySmsPossible() || isLimitedService();
    }

    public boolean isSmsCapable() {
        Rlog.e(TAG, "isSmsCapable: " + this.mIsSmsCapable);
        return this.mIsSmsCapable;
    }
}
