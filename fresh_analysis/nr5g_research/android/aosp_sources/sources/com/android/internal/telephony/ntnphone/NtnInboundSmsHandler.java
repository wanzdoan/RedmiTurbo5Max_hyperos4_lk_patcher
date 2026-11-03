package com.android.internal.telephony.ntnphone;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.InboundSmsHandler;
import com.android.internal.telephony.MiuiSmsControllerStub;
import com.android.internal.telephony.SmsConstants;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsStorageMonitor;
import com.android.internal.telephony.gsm.SmsMessage;

/* JADX INFO: loaded from: classes.dex */
public class NtnInboundSmsHandler extends InboundSmsHandler {
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

    private NtnInboundSmsHandler(Context context, SmsStorageMonitor smsStorageMonitor, NtnPhone ntnPhone, Looper looper, FeatureFlags featureFlags) {
        super("NtnInboundSmsHandler", context, smsStorageMonitor, ntnPhone, looper, featureFlags);
        ntnPhone.mNtnRil.setOnNewNtnSms(getHandler(), 1, null);
    }

    @Override // com.android.internal.telephony.InboundSmsHandler, com.android.internal.telephony.StateMachine
    protected void onQuitting() {
        ((NtnPhone) this.mPhone).mNtnRil.unSetOnNewNtnSms(getHandler());
        log("unregistered for Satellite SMS");
        super.onQuitting();
    }

    @Override // com.android.internal.telephony.InboundSmsHandler
    protected void acknowledgeLastIncomingSms(boolean z, int i, Message message) {
        this.mPhone.mCi.acknowledgeLastIncomingGsmSms(z, resultToCause(i), message);
    }

    public static NtnInboundSmsHandler makeInboundSmsHandler(Context context, SmsStorageMonitor smsStorageMonitor, NtnPhone ntnPhone, Looper looper, FeatureFlags featureFlags) {
        NtnInboundSmsHandler ntnInboundSmsHandler = new NtnInboundSmsHandler(context, smsStorageMonitor, ntnPhone, looper, featureFlags);
        ntnInboundSmsHandler.start();
        return ntnInboundSmsHandler;
    }

    @Override // com.android.internal.telephony.InboundSmsHandler
    protected int dispatchMessageRadioSpecific(SmsMessageBase smsMessageBase, int i, int i2) {
        SmsHeader.PortAddrs portAddrs;
        SmsMessage smsMessage = (SmsMessage) smsMessageBase;
        if (MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).isCTAutoRegMsg(this.mPhone, smsMessage)) {
            return 1;
        }
        if (smsMessage.isTypeZero()) {
            SmsHeader userDataHeader = smsMessage.getUserDataHeader();
            if (userDataHeader != null && (portAddrs = userDataHeader.portAddrs) != null) {
                int i3 = portAddrs.destPort;
            }
            log("Received short message type 0, Don't display or store it. Send Ack");
            addSmsTypeZeroToMetrics(i);
            return 1;
        }
        if (this.mStorageMonitor.isStorageAvailable() || smsMessage.getMessageClass() == SmsConstants.MessageClass.CLASS_0) {
            return dispatchNormalMessage(smsMessageBase, i);
        }
        return 3;
    }

    private void addSmsTypeZeroToMetrics(int i) {
        this.mPhone.getSmsStats().onIncomingSmsTypeZero(i, 0);
    }
}
