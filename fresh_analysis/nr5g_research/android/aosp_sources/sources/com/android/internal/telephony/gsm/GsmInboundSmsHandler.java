package com.android.internal.telephony.gsm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import com.android.ims.ImsManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.InboundSmsHandler;
import com.android.internal.telephony.MiuiSmsControllerStub;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SmsConstants;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsStorageMonitor;
import com.android.internal.telephony.VisualVoicemailSmsFilter;

/* JADX INFO: loaded from: classes.dex */
public class GsmInboundSmsHandler extends InboundSmsHandler {
    private static final boolean TEST_MODE;
    protected final UsimDataDownloadHandler mDataDownloadHandler;
    private BroadcastReceiver mTestBroadcastReceiver;

    private static int resultToCause(int i) {
        if (i == -1 || i == 1) {
            return 0;
        }
        return i != 3 ? 255 : 211;
    }

    @Override // com.android.internal.telephony.InboundSmsHandler
    protected boolean is3gpp2() {
        return false;
    }

    static {
        TEST_MODE = SystemProperties.getInt("ro.debuggable", 0) == 1;
    }

    protected GsmInboundSmsHandler(Context context, SmsStorageMonitor smsStorageMonitor, Phone phone, Looper looper, FeatureFlags featureFlags) {
        super("GsmInboundSmsHandler", context, smsStorageMonitor, phone, looper, featureFlags);
        phone.mCi.setOnNewGsmSms(getHandler(), 1, null);
        this.mDataDownloadHandler = new UsimDataDownloadHandler(phone.mCi, phone.getPhoneId());
        this.mCellBroadcastServiceManager.enable();
        if (TEST_MODE && this.mTestBroadcastReceiver == null) {
            this.mTestBroadcastReceiver = new GsmCbTestBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.android.internal.telephony.gsm.TEST_TRIGGER_CELL_BROADCAST");
            context.registerReceiver(this.mTestBroadcastReceiver, intentFilter, 2);
        }
    }

    private class GsmCbTestBroadcastReceiver extends InboundSmsHandler.CbTestBroadcastReceiver {
        GsmCbTestBroadcastReceiver() {
            super("com.android.internal.telephony.gsm.TEST_TRIGGER_CELL_BROADCAST");
        }

        @Override // com.android.internal.telephony.InboundSmsHandler.CbTestBroadcastReceiver
        protected void handleTestAction(Intent intent) {
            byte[] byteArrayExtra = intent.getByteArrayExtra("pdu");
            if (byteArrayExtra == null) {
                byteArrayExtra = GsmInboundSmsHandler.this.decodeHexString(intent.getStringExtra("pdu_string"));
            }
            if (byteArrayExtra == null) {
                GsmInboundSmsHandler.this.log("No pdu or pdu_string extra, ignoring CB test intent");
                return;
            }
            Message messageObtain = Message.obtain();
            AsyncResult.forMessage(messageObtain, byteArrayExtra, (Throwable) null);
            ((InboundSmsHandler) GsmInboundSmsHandler.this).mCellBroadcastServiceManager.sendGsmMessageToHandler(messageObtain);
        }
    }

    @Override // com.android.internal.telephony.InboundSmsHandler, com.android.internal.telephony.StateMachine
    protected void onQuitting() {
        this.mPhone.mCi.unSetOnNewGsmSms(getHandler());
        log("unregistered for 3GPP SMS");
        super.onQuitting();
    }

    public static GsmInboundSmsHandler makeInboundSmsHandler(Context context, SmsStorageMonitor smsStorageMonitor, Phone phone, Looper looper, FeatureFlags featureFlags) {
        GsmInboundSmsHandler gsmInboundSmsHandler = new GsmInboundSmsHandler(context, smsStorageMonitor, phone, looper, featureFlags);
        gsmInboundSmsHandler.start();
        return gsmInboundSmsHandler;
    }

    @Override // com.android.internal.telephony.InboundSmsHandler
    protected int dispatchMessageRadioSpecific(SmsMessageBase smsMessageBase, int i, int i2) {
        boolean zIsMwiDontStore;
        SmsHeader.PortAddrs portAddrs;
        SmsMessage smsMessage = (SmsMessage) smsMessageBase;
        if (MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).isCTAutoRegMsg(this.mPhone, smsMessage)) {
            return 1;
        }
        if (smsMessage.isTypeZero()) {
            SmsHeader userDataHeader = smsMessage.getUserDataHeader();
            VisualVoicemailSmsFilter.filter(this.mContext, new byte[][]{smsMessage.getPdu()}, "3gpp", (userDataHeader == null || (portAddrs = userDataHeader.portAddrs) == null) ? -1 : portAddrs.destPort, this.mPhone.getSubId());
            log("Received short message type 0, Don't display or store it. Send Ack");
            addSmsTypeZeroToMetrics(i, InboundSmsHandler.getPduLength(smsMessage));
            return 1;
        }
        if (smsMessage.isUsimDataDownload()) {
            return this.mDataDownloadHandler.handleUsimDataDownload(this.mPhone.getUsimServiceTable(), smsMessage, i, i2);
        }
        if (smsMessage.isMWISetMessage()) {
            updateMessageWaitingIndicator(smsMessage.getNumOfVoicemails());
            zIsMwiDontStore = smsMessage.isMwiDontStore();
            StringBuilder sb = new StringBuilder("Received voice mail indicator set SMS shouldStore=");
            sb.append(!zIsMwiDontStore);
            log(sb.toString());
        } else if (smsMessage.isMWIClearMessage()) {
            updateMessageWaitingIndicator(0);
            zIsMwiDontStore = smsMessage.isMwiDontStore();
            StringBuilder sb2 = new StringBuilder("Received voice mail indicator clear SMS shouldStore=");
            sb2.append(!zIsMwiDontStore);
            log(sb2.toString());
        } else {
            zIsMwiDontStore = false;
        }
        if (zIsMwiDontStore) {
            addVoicemailSmsToMetrics(i, InboundSmsHandler.getPduLength(smsMessage));
            return 1;
        }
        if (this.mStorageMonitor.isStorageAvailable() || smsMessage.getMessageClass() == SmsConstants.MessageClass.CLASS_0) {
            return dispatchNormalMessage(smsMessageBase, i);
        }
        return 3;
    }

    private void updateMessageWaitingIndicator(int i) {
        if (i < 0) {
            i = -1;
        } else if (i > 255) {
            i = 255;
        }
        this.mPhone.setVoiceMessageCount(i);
    }

    @Override // com.android.internal.telephony.InboundSmsHandler
    protected void acknowledgeLastIncomingSms(boolean z, int i, Message message) {
        this.mPhone.mCi.acknowledgeLastIncomingGsmSms(z, resultToCause(i), message);
    }

    private void addSmsTypeZeroToMetrics(int i, int i2) {
        this.mPhone.getSmsStats().onIncomingSmsTypeZero(i, i2);
    }

    private void addVoicemailSmsToMetrics(int i, int i2) {
        this.mPhone.getSmsStats().onIncomingSmsVoicemail(false, i, i2);
    }

    public boolean setImsManager(ImsManager imsManager) {
        UsimDataDownloadHandler usimDataDownloadHandler = this.mDataDownloadHandler;
        if (usimDataDownloadHandler == null) {
            return false;
        }
        usimDataDownloadHandler.setImsManager(imsManager);
        return true;
    }
}
