package com.android.internal.telephony;

import android.R;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.compat.CompatChanges;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.provider.Telephony;
import android.service.carrier.CarrierMessagingServiceWrapper;
import android.telephony.AnomalyReporter;
import android.telephony.CarrierConfigManager;
import android.telephony.MessageUpgradeController;
import android.telephony.PhoneNumberUtils;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.analytics.TelephonyAnalytics;
import com.android.internal.telephony.cdma.SmsMessage;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.telephony.metrics.SmsStats;
import com.android.internal.telephony.nano.TelephonyProto$TelephonyEvent;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.satellite.SatelliteConstants;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.util.ArrayUtils;
import com.android.internal.telephony.util.MiEventUtils;
import com.android.internal.util.FunctionalUtils;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public abstract class SMSDispatcher extends Handler {
    protected static final String CTCC_DM_SERVER_NUMBER = "10659401";
    protected static final int EVENT_CONFIRM_SEND_TO_POSSIBLE_PREMIUM_SHORT_CODE = 8;
    protected static final int EVENT_CONFIRM_SEND_TO_PREMIUM_SHORT_CODE = 9;
    protected static final int EVENT_GET_IMS_SERVICE = 16;
    protected static final int EVENT_ICC_CHANGED = 15;
    protected static final int EVENT_NEW_ICC_SMS = 14;
    protected static final int EVENT_NEW_SMS_STATUS_REPORT = 10;
    protected static final int EVENT_RETRY_SMMA = 11;
    protected static final int EVENT_SEND_LIMIT_REACHED_CONFIRMATION = 4;
    protected static final int EVENT_SEND_RETRY = 3;
    protected static final int EVENT_SEND_SMS_COMPLETE = 2;
    protected static final int EVENT_STOP_SENDING = 6;
    protected static final String MAP_KEY_DATA = "data";
    protected static final String MAP_KEY_DEST_ADDR = "destAddr";
    protected static final String MAP_KEY_DEST_PORT = "destPort";
    protected static final String MAP_KEY_PDU = "pdu";
    protected static final String MAP_KEY_SC_ADDR = "scAddr";
    protected static final String MAP_KEY_SMSC = "smsc";
    protected static final String MAP_KEY_TEXT = "text";
    protected static final int MAX_SEND_RETRIES = 3;
    public static final int MO_MSG_QUEUE_LIMIT = 5;
    protected static final int PREMIUM_RULE_USE_BOTH = 3;
    protected static final int PREMIUM_RULE_USE_NETWORK = 2;
    protected static final int PREMIUM_RULE_USE_SIM = 1;
    public static final int SEND_RETRY_DELAY = 2000;
    private static final UUID sAnomalyNoResponseFromCarrierMessagingService = UUID.fromString("279d9fbc-462d-4fc2-802c-bf21ddd9dd90");
    private static final UUID sAnomalyUnexpectedCallback = UUID.fromString("0103b6d2-ad07-4d86-9102-14341b9074ef");
    private static int sConcatenatedRef = new Random().nextInt(CallFailCause.RADIO_UPLINK_FAILURE);
    private final BroadcastReceiver mBroadcastReceiver;
    public int mCarrierMessagingTimeout;
    protected final CommandsInterface mCi;
    protected final Context mContext;
    private final FeatureFlags mFeatureFlags;
    private IHyperTeleSmsManager mIHyperTeleSmsManager;
    protected final LocalLog mLocalLog;
    private int mPendingTrackerCount;
    protected Phone mPhone;
    protected final AtomicInteger mPremiumSmsRule;
    protected boolean mRPSmmaRetried;
    protected final ContentResolver mResolver;
    private final SettingsObserver mSettingsObserver;
    protected boolean mSmsCapable;
    protected SmsDispatchersController mSmsDispatchersController;
    protected final LocalLog mSmsOutgoingErrorCodes;
    protected boolean mSmsSendDisabled;
    protected final TelephonyManager mTelephonyManager;

    protected static int getNotInServiceError(int i) {
        return i == 3 ? 2 : 4;
    }

    protected abstract GsmAlphabet.TextEncodingDetails calculateLength(CharSequence charSequence, boolean z);

    protected abstract String getFormat();

    protected int getMaxSmsRetryCount() {
        return 3;
    }

    protected int getSmsRetryDelayValue() {
        return 2000;
    }

    protected abstract SmsMessageBase.SubmitPduBase getSubmitPdu(String str, String str2, int i, byte[] bArr, boolean z);

    protected abstract SmsMessageBase.SubmitPduBase getSubmitPdu(String str, String str2, int i, byte[] bArr, boolean z, int i2);

    protected abstract SmsMessageBase.SubmitPduBase getSubmitPdu(String str, String str2, String str3, boolean z, SmsHeader smsHeader, int i, int i2);

    protected abstract SmsMessageBase.SubmitPduBase getSubmitPdu(String str, String str2, String str3, boolean z, SmsHeader smsHeader, int i, int i2, int i3);

    protected abstract void sendSms(SmsTracker smsTracker);

    protected abstract boolean shouldBlockSmsForEcbm();

    protected static int getNextConcatenatedRef() {
        int i = sConcatenatedRef + 1;
        sConcatenatedRef = i;
        return i;
    }

    protected SMSDispatcher(Phone phone, SmsDispatchersController smsDispatchersController, FeatureFlags featureFlags) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        this.mPremiumSmsRule = atomicInteger;
        this.mLocalLog = new LocalLog(16);
        this.mSmsOutgoingErrorCodes = new LocalLog(10);
        this.mRPSmmaRetried = false;
        this.mSmsCapable = true;
        this.mCarrierMessagingTimeout = 600000;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.SMSDispatcher.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                Rlog.d("SMSDispatcher", "Received broadcast " + intent.getAction());
                if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
                    if (!intent.hasExtra("ss")) {
                        Rlog.d("SMSDispatcher", "Extra not found in intent.");
                    } else if (intent.getStringExtra("ss").equals("LOADED")) {
                        Rlog.d("SMSDispatcher", "SIM_STATE_CHANGED : SIM_LOADED");
                        Message messageObtainMessage = SMSDispatcher.this.obtainMessage(18);
                        messageObtainMessage.arg1 = SMSDispatcher.this.getSubId();
                        SMSDispatcher.this.sendMessage(messageObtainMessage);
                    }
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        this.mPhone = phone;
        this.mSmsDispatchersController = smsDispatchersController;
        Context context = phone.getContext();
        this.mContext = context;
        this.mResolver = context.getContentResolver();
        this.mCi = phone.mCi;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        this.mTelephonyManager = telephonyManager;
        SettingsObserver settingsObserver = new SettingsObserver(this, atomicInteger, context);
        this.mSettingsObserver = settingsObserver;
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("sms_short_code_rule"), false, settingsObserver);
        this.mSmsCapable = telephonyManager.isDeviceSmsCapable();
        this.mSmsSendDisabled = !telephonyManager.getSmsSendCapableForPhone(this.mPhone.getPhoneId(), this.mSmsCapable);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        context.registerReceiver(broadcastReceiver, intentFilter);
        this.mFeatureFlags = featureFlags;
        Rlog.d("SMSDispatcher", "SMSDispatcher: ctor mSmsCapable=" + this.mSmsCapable + " format=" + getFormat() + " mSmsSendDisabled=" + this.mSmsSendDisabled);
        this.mIHyperTeleSmsManager = (IHyperTeleSmsManager) HyperTelephonyFactory.getInstance().initModule(IHyperTeleSmsManager.DEFAULT, new Object[0]);
    }

    private static class SettingsObserver extends ContentObserver {
        private final Context mContext;
        private final AtomicInteger mPremiumSmsRule;

        SettingsObserver(Handler handler, AtomicInteger atomicInteger, Context context) {
            super(handler);
            this.mPremiumSmsRule = atomicInteger;
            this.mContext = context;
            onChange(false);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            this.mPremiumSmsRule.set(Settings.Global.getInt(this.mContext.getContentResolver(), "sms_short_code_rule", 1));
        }
    }

    public void dispose() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mSettingsObserver);
    }

    protected void handleStatusReport(Object obj) {
        Rlog.d("SMSDispatcher", "handleStatusReport() called with no subclass.");
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        SubscriptionInfoInternal subscriptionInfoInternal;
        int i = message.what;
        if (i == 17) {
            handleMessageRefStatus(message);
            return;
        }
        if (i != 18) {
            int i2 = 8;
            switch (i) {
                case 2:
                    handleSendComplete((AsyncResult) message.obj);
                    break;
                case 3:
                    Rlog.d("SMSDispatcher", "SMS retry..");
                    sendRetrySms((SmsTracker) message.obj);
                    break;
                case 4:
                    handleReachSentLimit((SmsTracker[]) message.obj);
                    break;
                case 5:
                    for (SmsTracker smsTracker : (SmsTracker[]) message.obj) {
                        sendSms(smsTracker);
                    }
                    this.mPendingTrackerCount--;
                    break;
                case 6:
                    SmsTracker[] smsTrackerArr = (SmsTracker[]) message.obj;
                    int i3 = message.arg1;
                    if (i3 == 0) {
                        if (message.arg2 == 1) {
                            Rlog.d("SMSDispatcher", "SMSDispatcher: EVENT_STOP_SENDING - sending SHORT_CODE_NEVER_ALLOWED error code.");
                        } else {
                            Rlog.d("SMSDispatcher", "SMSDispatcher: EVENT_STOP_SENDING - sending SHORT_CODE_NOT_ALLOWED error code.");
                            i2 = 7;
                        }
                    } else if (i3 == 1) {
                        Rlog.d("SMSDispatcher", "SMSDispatcher: EVENT_STOP_SENDING - sending LIMIT_EXCEEDED error code.");
                        i2 = 5;
                    } else {
                        Rlog.e("SMSDispatcher", "SMSDispatcher: EVENT_STOP_SENDING - unexpected cases.");
                        i2 = 28;
                    }
                    handleSmsTrackersFailure(smsTrackerArr, i2, -1);
                    this.mPendingTrackerCount--;
                    break;
                case 7:
                    SmsTracker[] smsTrackerArr2 = (SmsTracker[]) message.obj;
                    Rlog.d("SMSDispatcher", "SMSDispatcher: EVENT_SENDING_NOT_ALLOWED - sending SHORT_CODE_NEVER_ALLOWED error code.");
                    handleSmsTrackersFailure(smsTrackerArr2, 8, -1);
                    break;
                case 8:
                    handleConfirmShortCode(false, (SmsTracker[]) message.obj);
                    break;
                case 9:
                    handleConfirmShortCode(true, (SmsTracker[]) message.obj);
                    break;
                case 10:
                    handleStatusReport(message.obj);
                    break;
                default:
                    Rlog.e("SMSDispatcher", "handleMessage() ignoring message of unexpected type " + message.what);
                    break;
            }
            return;
        }
        this.mSmsDispatchersController.setMessageReference(getTpmrValueFromSIM());
        if (this.mSmsDispatchersController.getMessageReference() != -1 || (subscriptionInfoInternal = SubscriptionManagerService.getInstance().getSubscriptionInfoInternal(message.arg1)) == null) {
            return;
        }
        this.mSmsDispatchersController.setMessageReference(subscriptionInfoInternal.getLastUsedTPMessageReference());
    }

    private void handleMessageRefStatus(Message message) {
        AsyncResult asyncResult = (AsyncResult) message.obj;
        if (asyncResult.exception != null) {
            Rlog.e("SMSDispatcher", "Failed to update TP - Message reference value to SIM " + asyncResult.exception);
            return;
        }
        Rlog.d("SMSDispatcher", "TP - Message reference updated to SIM Successfully");
    }

    private void updateTPMessageReference() {
        updateSIMLastTPMRValue(this.mSmsDispatchersController.getMessageReference());
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            SubscriptionManagerService.getInstance().setLastUsedTPMessageReference(getSubId(), this.mSmsDispatchersController.getMessageReference());
        } catch (SecurityException e) {
            Rlog.e("SMSDispatcher", "Security Exception caused on messageRef updation to DB " + e.getMessage());
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private void updateSIMLastTPMRValue(int i) {
        Message messageObtainMessage = obtainMessage(17);
        IccRecords iccRecords = getIccRecords();
        if (iccRecords != null) {
            iccRecords.setSmssTpmrValue(i, messageObtainMessage);
        }
    }

    private int getTpmrValueFromSIM() {
        IccRecords iccRecords = getIccRecords();
        if (iccRecords != null) {
            return iccRecords.getSmssTpmrValue();
        }
        return -1;
    }

    private IccRecords getIccRecords() {
        Phone phone = this.mPhone;
        if (phone == null || phone.getIccRecords() == null) {
            return null;
        }
        return this.mPhone.getIccRecords();
    }

    public int nextMessageRef() {
        if (!isMessageRefIncrementViaTelephony()) {
            return 0;
        }
        int iIncrementMessageReference = this.mSmsDispatchersController.incrementMessageReference();
        Rlog.d("SMSDispatcher", "nextMessageRef: " + iIncrementMessageReference);
        updateTPMessageReference();
        return iIncrementMessageReference;
    }

    public boolean isMessageRefIncrementViaTelephony() {
        boolean z;
        try {
            z = this.mContext.getResources().getBoolean(R.bool.config_use_strict_phone_number_comparation_for_russia);
        } catch (Resources.NotFoundException unused) {
            Rlog.e("SMSDispatcher", "isMessageRefIncrementViaTelephony NotFoundException Exception");
            z = false;
        }
        Rlog.i("SMSDispatcher", "bool.config_stk_sms_send_support= " + z);
        return z;
    }

    protected abstract class SmsSender extends Handler {
        protected final CarrierMessagingServiceWrapper mCarrierMessagingServiceWrapper;
        private String mCarrierPackageName;
        protected volatile CarrierMessagingServiceWrapper.CarrierMessagingCallback mSenderCallback;

        public abstract SmsTracker getSmsTracker();

        public abstract SmsTracker[] getSmsTrackers();

        public abstract void onSendComplete(int i);

        /* JADX INFO: renamed from: onServiceReady, reason: merged with bridge method [inline-methods] */
        public abstract void lambda$sendSmsByCarrierApp$1();

        protected SmsSender() {
            super(Looper.getMainLooper());
            this.mCarrierMessagingServiceWrapper = new CarrierMessagingServiceWrapper();
        }

        public synchronized void sendSmsByCarrierApp(String str, CarrierMessagingServiceWrapper.CarrierMessagingCallback carrierMessagingCallback) {
            try {
                this.mCarrierPackageName = str;
                this.mSenderCallback = carrierMessagingCallback;
                if (!this.mCarrierMessagingServiceWrapper.bindToCarrierMessagingService(SMSDispatcher.this.mContext, str, new Executor() { // from class: com.android.internal.telephony.SMSDispatcher$SmsSender$$ExternalSyntheticLambda0
                    @Override // java.util.concurrent.Executor
                    public final void execute(Runnable runnable) {
                        runnable.run();
                    }
                }, new Runnable() { // from class: com.android.internal.telephony.SMSDispatcher$SmsSender$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$sendSmsByCarrierApp$1();
                    }
                })) {
                    Rlog.e("SMSDispatcher", "bindService() for carrier messaging service failed");
                    onSendComplete(1);
                } else {
                    Rlog.d("SMSDispatcher", "bindService() for carrier messaging service succeeded");
                    sendMessageDelayed(obtainMessage(1), SMSDispatcher.this.mCarrierMessagingTimeout);
                }
            } catch (Throwable th) {
                throw th;
            }
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            SMSDispatcher sMSDispatcher = SMSDispatcher.this;
            if (i == 1) {
                sMSDispatcher.logWithLocalLog("handleMessage: No response from " + this.mCarrierPackageName + " for " + SMSDispatcher.this.mCarrierMessagingTimeout + " ms");
                UUID uuid = SMSDispatcher.sAnomalyNoResponseFromCarrierMessagingService;
                StringBuilder sb = new StringBuilder("No response from ");
                sb.append(this.mCarrierPackageName);
                AnomalyReporter.reportAnomaly(uuid, sb.toString(), SMSDispatcher.this.mPhone.getCarrierId());
                onSendComplete(1);
                return;
            }
            sMSDispatcher.logWithLocalLog("handleMessage: received unexpected message " + message.what);
        }

        public void removeTimeout() {
            removeMessages(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logWithLocalLog(String str) {
        this.mLocalLog.log(str);
        Rlog.d("SMSDispatcher", str);
    }

    protected final class TextSmsSender extends SmsSender {
        private final SmsTracker mTracker;

        public TextSmsSender(SmsTracker smsTracker) {
            super();
            this.mTracker = smsTracker;
        }

        @Override // com.android.internal.telephony.SMSDispatcher.SmsSender
        /* JADX INFO: renamed from: onServiceReady */
        public synchronized void lambda$sendSmsByCarrierApp$1() {
            try {
                Rlog.d("SMSDispatcher", "TextSmsSender::onServiceReady");
                String str = (String) this.mTracker.getData().get(SMSDispatcher.MAP_KEY_TEXT);
                if (str != null) {
                    try {
                        CarrierMessagingServiceWrapper carrierMessagingServiceWrapper = this.mCarrierMessagingServiceWrapper;
                        int subId = SMSDispatcher.this.getSubId();
                        SmsTracker smsTracker = this.mTracker;
                        carrierMessagingServiceWrapper.sendTextSms(str, subId, smsTracker.mDestAddress, smsTracker.mDeliveryIntent != null ? 1 : 0, new Executor() { // from class: com.android.internal.telephony.SMSDispatcher$TextSmsSender$$ExternalSyntheticLambda0
                            @Override // java.util.concurrent.Executor
                            public final void execute(Runnable runnable) {
                                runnable.run();
                            }
                        }, this.mSenderCallback);
                    } catch (RuntimeException e) {
                        Rlog.e("SMSDispatcher", "TextSmsSender::onServiceReady: Exception sending the SMS: " + e.getMessage());
                        onSendComplete(1);
                    }
                } else {
                    Rlog.d("SMSDispatcher", "TextSmsSender::onServiceReady: text == null");
                    onSendComplete(1);
                }
            } catch (Throwable th) {
                throw th;
            }
        }

        @Override // com.android.internal.telephony.SMSDispatcher.SmsSender
        public void onSendComplete(int i) {
            this.mSenderCallback.onSendSmsComplete(i, 0);
        }

        @Override // com.android.internal.telephony.SMSDispatcher.SmsSender
        public SmsTracker getSmsTracker() {
            return this.mTracker;
        }

        @Override // com.android.internal.telephony.SMSDispatcher.SmsSender
        public SmsTracker[] getSmsTrackers() {
            Rlog.e("SMSDispatcher", "getSmsTrackers: Unexpected call for TextSmsSender");
            return null;
        }
    }

    protected final class DataSmsSender extends SmsSender {
        private final SmsTracker mTracker;

        public DataSmsSender(SmsTracker smsTracker) {
            super();
            this.mTracker = smsTracker;
        }

        @Override // com.android.internal.telephony.SMSDispatcher.SmsSender
        /* JADX INFO: renamed from: onServiceReady */
        public synchronized void lambda$sendSmsByCarrierApp$1() {
            Rlog.d("SMSDispatcher", "DataSmsSender::onServiceReady");
            HashMap<String, Object> data = this.mTracker.getData();
            byte[] bArr = (byte[]) data.get("data");
            int iIntValue = ((Integer) data.get(SMSDispatcher.MAP_KEY_DEST_PORT)).intValue();
            if (bArr != null) {
                try {
                    CarrierMessagingServiceWrapper carrierMessagingServiceWrapper = this.mCarrierMessagingServiceWrapper;
                    int subId = SMSDispatcher.this.getSubId();
                    SmsTracker smsTracker = this.mTracker;
                    carrierMessagingServiceWrapper.sendDataSms(bArr, subId, smsTracker.mDestAddress, iIntValue, smsTracker.mDeliveryIntent != null ? 1 : 0, new Executor() { // from class: com.android.internal.telephony.SMSDispatcher$DataSmsSender$$ExternalSyntheticLambda0
                        @Override // java.util.concurrent.Executor
                        public final void execute(Runnable runnable) {
                            runnable.run();
                        }
                    }, this.mSenderCallback);
                } catch (RuntimeException e) {
                    Rlog.e("SMSDispatcher", "DataSmsSender::onServiceReady: Exception sending the SMS: " + e + " " + SmsController.formatCrossStackMessageId(this.mTracker.mMessageId));
                    onSendComplete(1);
                }
            } else {
                Rlog.d("SMSDispatcher", "DataSmsSender::onServiceReady: data == null");
                onSendComplete(1);
            }
        }

        @Override // com.android.internal.telephony.SMSDispatcher.SmsSender
        public void onSendComplete(int i) {
            this.mSenderCallback.onSendSmsComplete(i, 0);
        }

        @Override // com.android.internal.telephony.SMSDispatcher.SmsSender
        public SmsTracker getSmsTracker() {
            return this.mTracker;
        }

        @Override // com.android.internal.telephony.SMSDispatcher.SmsSender
        public SmsTracker[] getSmsTrackers() {
            Rlog.e("SMSDispatcher", "getSmsTrackers: Unexpected call for DataSmsSender");
            return null;
        }
    }

    protected final class SmsSenderCallback implements CarrierMessagingServiceWrapper.CarrierMessagingCallback {
        private boolean mCallbackCalled = false;
        private final SmsSender mSmsSender;

        public SmsSenderCallback(SmsSender smsSender) {
            this.mSmsSender = smsSender;
        }

        public void onSendSmsComplete(int i, int i2) {
            Rlog.d("SMSDispatcher", "onSendSmsComplete: result=" + i + " messageRef=" + i2);
            if (cleanupOnSendSmsComplete("onSendSmsComplete")) {
                return;
            }
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                SMSDispatcher.this.processSendSmsResponse(this.mSmsSender.getSmsTracker(), i, i2);
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }

        public void onSendMultipartSmsComplete(int i, int[] iArr) {
            Rlog.d("SMSDispatcher", "onSendMultipartSmsComplete: result=" + i + " messageRefs=" + Arrays.toString(iArr));
            if (cleanupOnSendSmsComplete("onSendMultipartSmsComplete")) {
                return;
            }
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                SMSDispatcher.this.processSendMultipartSmsResponse(this.mSmsSender.getSmsTrackers(), i, iArr);
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }

        private boolean cleanupOnSendSmsComplete(String str) {
            if (this.mCallbackCalled) {
                SMSDispatcher.this.logWithLocalLog(str + ": unexpected call");
                AnomalyReporter.reportAnomaly(SMSDispatcher.sAnomalyUnexpectedCallback, "Unexpected " + str, SMSDispatcher.this.mPhone.getCarrierId());
                return true;
            }
            this.mCallbackCalled = true;
            this.mSmsSender.removeTimeout();
            this.mSmsSender.mCarrierMessagingServiceWrapper.disconnect();
            return false;
        }

        public void onReceiveSmsComplete(int i) {
            Rlog.e("SMSDispatcher", "Unexpected onReceiveSmsComplete call with result: " + i);
        }

        public void onSendMmsComplete(int i, byte[] bArr) {
            Rlog.e("SMSDispatcher", "Unexpected onSendMmsComplete call with result: " + i);
        }

        public void onDownloadMmsComplete(int i) {
            Rlog.e("SMSDispatcher", "Unexpected onDownloadMmsComplete call with result: " + i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSendSmsResponse(SmsTracker smsTracker, int i, int i2) {
        if (smsTracker == null) {
            Rlog.e("SMSDispatcher", "processSendSmsResponse: null tracker");
            return;
        }
        SmsResponse smsResponse = new SmsResponse(i2, null, -1, smsTracker.mMessageId);
        smsTracker.mResultCodeFromCarrierMessagingService = i;
        if (i == 0) {
            Rlog.d("SMSDispatcher", "processSendSmsResponse: Sending SMS by CarrierMessagingService succeeded. " + SmsController.formatCrossStackMessageId(smsTracker.mMessageId));
            sendMessage(obtainMessage(2, new AsyncResult(smsTracker, smsResponse, (Throwable) null)));
            return;
        }
        if (i != 1) {
            if (i != 2) {
                switch (i) {
                    case 200:
                    case 201:
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SIGNAL_STRENGTH_REPORTING_CRITERIA /* 202 */:
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_LINK_CAPACITY_REPORTING_CRITERIA /* 203 */:
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_PREFERRED_DATA_MODEM /* 204 */:
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_EMERGENCY_DIAL /* 205 */:
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_PHONE_CAPABILITY /* 206 */:
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SWITCH_DUAL_SIM_CONFIG /* 207 */:
                    case 208:
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_UICC_APPLICATIONS_ENABLEMENT /* 209 */:
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SYSTEM_SELECTION_CHANNELS /* 210 */:
                    case 211:
                    case 212:
                    case 213:
                    case 214:
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_ALLOCATE_PDU_SESSION_ID /* 215 */:
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_RELEASE_PDU_SESSION_ID /* 216 */:
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_START_HANDOVER /* 217 */:
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_CANCEL_HANDOVER /* 218 */:
                        break;
                    default:
                        Rlog.d("SMSDispatcher", "processSendSmsResponse: Unknown result " + i + " Retry on carrier network. " + SmsController.formatCrossStackMessageId(smsTracker.mMessageId));
                        resetResultCodeFromCarrierMessagingService(smsTracker);
                        sendSubmitPdu(smsTracker);
                        break;
                }
                return;
            }
            Rlog.d("SMSDispatcher", "processSendSmsResponse: Sending SMS by CarrierMessagingService failed. " + SmsController.formatCrossStackMessageId(smsTracker.mMessageId));
            sendMessage(obtainMessage(2, new AsyncResult(smsTracker, smsResponse, new CommandException(CommandException.Error.GENERIC_FAILURE))));
            return;
        }
        Rlog.d("SMSDispatcher", "processSendSmsResponse: Sending SMS by CarrierMessagingService failed. Retry on carrier network. " + SmsController.formatCrossStackMessageId(smsTracker.mMessageId));
        resetResultCodeFromCarrierMessagingService(smsTracker);
        sendSubmitPdu(smsTracker);
    }

    private void resetResultCodeFromCarrierMessagingService(SmsTracker smsTracker) {
        smsTracker.mResultCodeFromCarrierMessagingService = 0;
    }

    private int toSmsManagerResultForSendSms(int i) {
        Rlog.d("SMSDispatcher", "toSmsManagerResultForSendSms: carrierMessagingServiceResult=" + i);
        if (i == 0) {
            return -1;
        }
        if (i == 2) {
            return 124;
        }
        switch (i) {
            case 201:
                return 3;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SIGNAL_STRENGTH_REPORTING_CRITERIA /* 202 */:
                return 4;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_LINK_CAPACITY_REPORTING_CRITERIA /* 203 */:
                return 5;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_PREFERRED_DATA_MODEM /* 204 */:
                return 6;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_EMERGENCY_DIAL /* 205 */:
                return 7;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_PHONE_CAPABILITY /* 206 */:
                return 8;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SWITCH_DUAL_SIM_CONFIG /* 207 */:
                return 10;
            case 208:
                return 11;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_GET_UICC_APPLICATIONS_ENABLEMENT /* 209 */:
                return 12;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_SYSTEM_SELECTION_CHANNELS /* 210 */:
                return 14;
            case 211:
                return 17;
            case 212:
                return 18;
            case 213:
                return 19;
            case 214:
                return 20;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_ALLOCATE_PDU_SESSION_ID /* 215 */:
                return 23;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_RELEASE_PDU_SESSION_ID /* 216 */:
                return 24;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_START_HANDOVER /* 217 */:
                return 29;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_CANCEL_HANDOVER /* 218 */:
                return 30;
            default:
                return 1;
        }
    }

    protected final class MultipartSmsSender extends SmsSender {
        private final List mParts;
        public final SmsTracker[] mTrackers;

        public MultipartSmsSender(ArrayList<String> arrayList, SmsTracker[] smsTrackerArr) {
            super();
            this.mParts = arrayList;
            this.mTrackers = smsTrackerArr;
        }

        public void sendSmsByCarrierApp(String str, SmsSenderCallback smsSenderCallback) {
            super.sendSmsByCarrierApp(str, (CarrierMessagingServiceWrapper.CarrierMessagingCallback) smsSenderCallback);
        }

        @Override // com.android.internal.telephony.SMSDispatcher.SmsSender
        /* JADX INFO: renamed from: onServiceReady */
        public synchronized void lambda$sendSmsByCarrierApp$1() {
            int i;
            Rlog.d("SMSDispatcher", "MultipartSmsSender::onServiceReady");
            SmsTracker[] smsTrackerArr = this.mTrackers;
            int length = smsTrackerArr.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    i = 0;
                    break;
                } else {
                    if (smsTrackerArr[i2].mDeliveryIntent != null) {
                        i = 1;
                        break;
                    }
                    i2++;
                }
            }
            try {
                this.mCarrierMessagingServiceWrapper.sendMultipartTextSms(this.mParts, SMSDispatcher.this.getSubId(), this.mTrackers[0].mDestAddress, i, new Executor() { // from class: com.android.internal.telephony.SMSDispatcher$MultipartSmsSender$$ExternalSyntheticLambda0
                    @Override // java.util.concurrent.Executor
                    public final void execute(Runnable runnable) {
                        runnable.run();
                    }
                }, this.mSenderCallback);
            } catch (RuntimeException e) {
                Rlog.e("SMSDispatcher", "MultipartSmsSender::onServiceReady: Exception sending the SMS: " + e);
                onSendComplete(1);
            }
        }

        @Override // com.android.internal.telephony.SMSDispatcher.SmsSender
        public void onSendComplete(int i) {
            this.mSenderCallback.onSendMultipartSmsComplete(i, (int[]) null);
        }

        @Override // com.android.internal.telephony.SMSDispatcher.SmsSender
        public SmsTracker getSmsTracker() {
            Rlog.e("SMSDispatcher", "getSmsTracker: Unexpected call for MultipartSmsSender");
            return null;
        }

        @Override // com.android.internal.telephony.SMSDispatcher.SmsSender
        public SmsTracker[] getSmsTrackers() {
            return this.mTrackers;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSendMultipartSmsResponse(SmsTracker[] smsTrackerArr, int i, int[] iArr) {
        if (smsTrackerArr == null) {
            Rlog.e("SMSDispatcher", "processSendMultipartSmsResponse: null trackers");
            return;
        }
        if (i == 0) {
            Rlog.d("SMSDispatcher", "processSendMultipartSmsResponse: Sending SMS by CarrierMessagingService succeeded. " + SmsController.formatCrossStackMessageId(smsTrackerArr[0].mMessageId));
            int i2 = 0;
            while (i2 < smsTrackerArr.length) {
                sendMessage(obtainMessage(2, new AsyncResult(smsTrackerArr[i2], new SmsResponse((iArr == null || iArr.length <= i2) ? 0 : iArr[i2], null, -1), (Throwable) null)));
                i2++;
            }
            return;
        }
        if (i == 1) {
            Rlog.d("SMSDispatcher", "processSendMultipartSmsResponse: Sending SMS by CarrierMessagingService failed. Retry on carrier network. " + SmsController.formatCrossStackMessageId(smsTrackerArr[0].mMessageId));
            sendSubmitPdu(smsTrackerArr);
            return;
        }
        if (i == 2) {
            Rlog.d("SMSDispatcher", "processSendMultipartSmsResponse: Sending SMS by CarrierMessagingService failed. " + SmsController.formatCrossStackMessageId(smsTrackerArr[0].mMessageId));
            int i3 = 0;
            while (i3 < smsTrackerArr.length) {
                sendMessage(obtainMessage(2, new AsyncResult(smsTrackerArr[i3], new SmsResponse((iArr == null || iArr.length <= i3) ? 0 : iArr[i3], null, -1), new CommandException(CommandException.Error.GENERIC_FAILURE))));
                i3++;
            }
            return;
        }
        Rlog.d("SMSDispatcher", "processSendMultipartSmsResponse: Unknown result " + i + ". Retry on carrier network. " + SmsController.formatCrossStackMessageId(smsTrackerArr[0].mMessageId));
        sendSubmitPdu(smsTrackerArr);
    }

    protected void sendSubmitPdu(SmsTracker smsTracker) {
        sendSubmitPdu(new SmsTracker[]{smsTracker});
    }

    private void sendSubmitPdu(SmsTracker[] smsTrackerArr) {
        if (shouldBlockSmsForEcbm()) {
            Rlog.d("SMSDispatcher", "Block SMS in Emergency Callback mode");
            handleSmsTrackersFailure(smsTrackerArr, 29, -1);
        } else {
            sendRawPdu(smsTrackerArr);
        }
    }

    protected void notifySmsSentFailedToEmergencyStateTracker(SmsTracker smsTracker, boolean z) {
        this.mSmsDispatchersController.notifySmsSent(smsTracker, z, true, false);
    }

    protected void handleSendComplete(AsyncResult asyncResult) {
        int iRilErrorToSmsManagerResult;
        int i;
        SmsTracker smsTracker = (SmsTracker) asyncResult.userObj;
        PendingIntent pendingIntent = smsTracker.mSentIntent;
        SmsResponse smsResponse = (SmsResponse) asyncResult.result;
        if (smsResponse == null) {
            Rlog.d("SMSDispatcher", "SmsResponse was null");
        } else {
            smsTracker.mMessageRef = smsResponse.mMessageRef;
        }
        Throwable th = asyncResult.exception;
        if (th == null) {
            if (smsTracker.mDeliveryIntent != null) {
                this.mSmsDispatchersController.putDeliveryPendingTracker(smsTracker);
            }
            MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).miuiSmsSendOptimizationStatus(this.mPhone, smsTracker, null, this.mSmsDispatchersController.getSmscAddress(), false, true, 0, this.mSmsDispatchersController);
            smsTracker.onSent(this.mContext);
            this.mPhone.notifySmsSent(smsTracker.mDestAddress);
            this.mSmsDispatchersController.notifySmsSent(smsTracker, false, smsTracker.isSinglePartOrLastPart(), true);
            reportOutgoingSmsMetrics(smsTracker, 0, -1);
            return;
        }
        int i2 = smsTracker.mResultCodeFromCarrierMessagingService;
        if (i2 != 0) {
            iRilErrorToSmsManagerResult = toSmsManagerResultForSendSms(i2);
        } else {
            iRilErrorToSmsManagerResult = rilErrorToSmsManagerResult(((CommandException) th).getCommandError(), smsTracker);
        }
        int i3 = iRilErrorToSmsManagerResult;
        int state = this.mPhone.getServiceState().getState();
        if (smsTracker.mImsRetry > 0 && state != 0 && !this.mIHyperTeleSmsManager.isVoLTERegisteredForPhone(this.mPhone.getPhoneId())) {
            smsTracker.mRetryCount = this.mIHyperTeleSmsManager.getMaxSendRetries(this.mContext, getSubId());
            Rlog.d("SMSDispatcher", "handleSendComplete: Skipping retry:  isIms()=" + isIms() + " mRetryCount=" + smsTracker.mRetryCount + " mImsRetry=" + smsTracker.mImsRetry + " mMessageRef=" + smsTracker.mMessageRef + " SS= " + this.mPhone.getServiceState().getState() + " " + SmsController.formatCrossStackMessageId(smsTracker.mMessageId));
        }
        if (!isIms() && state != 0) {
            if (smsTracker.mSmsOptimizationStatus == 2) {
                MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).miuiSmsSendOptimizationStatus(this.mPhone, smsTracker, null, this.mSmsDispatchersController.getSmscAddress(), false, false, getNotInServiceError(state), this.mSmsDispatchersController);
            }
            smsTracker.onFailed(this.mContext, getNotInServiceError(state), -1);
            notifySmsSentFailedToEmergencyStateTracker(smsTracker, false);
            reportOutgoingSmsMetrics(smsTracker, getNotInServiceError(state), -1);
            return;
        }
        if (i3 == 101 && smsTracker.mRetryCount < this.mIHyperTeleSmsManager.getMaxSendRetries(this.mContext, getSubId()) && smsTracker.mSmsOptimizationStatus != 2) {
            smsTracker.mRetryCount++;
            i = smsResponse != null ? smsResponse.mErrorCode : -1;
            sendMessageDelayed(obtainMessage(3, smsTracker), getSmsRetryDelayValue());
            reportOutgoingSmsMetrics(smsTracker, 101, i);
            return;
        }
        if (MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).miuiSmsSendOptimizationStatus(this.mPhone, smsTracker, null, this.mSmsDispatchersController.getSmscAddress(), false, false, i3, this.mSmsDispatchersController) == 1) {
            this.mPhone.getSmsStats().onOutgoingSms(smsTracker.mImsRetry > 0, "3gpp2".equals(getFormat()), false, 101, smsResponse != null ? smsResponse.mErrorCode : -1, smsTracker.mMessageId, smsTracker.isFromDefaultSmsApplication(this.mContext), smsTracker.getInterval(), this.mTelephonyManager.isEmergencyNumber(smsTracker.mDestAddress), smsTracker.isMtSmsPollingMessage(this.mContext), smsTracker.getPduLength(), smsTracker.getAppPackageName(), smsTracker.getAppUid());
            return;
        }
        i = smsResponse != null ? smsResponse.mErrorCode : -1;
        smsTracker.onFailed(this.mContext, i3, i);
        notifySmsSentFailedToEmergencyStateTracker(smsTracker, false);
        reportOutgoingSmsMetrics(smsTracker, i3, i);
    }

    private int rilErrorToSmsManagerResult(CommandException.Error error, SmsTracker smsTracker) {
        Rlog.d("SMSDispatcher", "rilErrorToSmsManagerResult: rilError=" + error + ", tracker=" + smsTracker);
        this.mSmsOutgoingErrorCodes.log("rilError: " + error + ", MessageId: " + SmsController.formatCrossStackMessageId(smsTracker.mMessageId));
        ApplicationInfo appInfo = smsTracker.getAppInfo();
        if ((appInfo == null || !CompatChanges.isChangeEnabled(250017070L, appInfo.uid)) && (error == CommandException.Error.INVALID_RESPONSE || error == CommandException.Error.SIM_PIN2 || error == CommandException.Error.SIM_PUK2 || error == CommandException.Error.SUBSCRIPTION_NOT_AVAILABLE || error == CommandException.Error.SIM_ERR || error == CommandException.Error.INVALID_SIM_STATE || error == CommandException.Error.NO_SMS_TO_ACK || error == CommandException.Error.SIM_BUSY || error == CommandException.Error.SIM_FULL || error == CommandException.Error.NO_SUBSCRIPTION || error == CommandException.Error.NO_NETWORK_FOUND || error == CommandException.Error.DEVICE_IN_USE || error == CommandException.Error.ABORTED)) {
            return 1;
        }
        switch (AnonymousClass4.$SwitchMap$com$android$internal$telephony$CommandException$Error[error.ordinal()]) {
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
                Rlog.d("SMSDispatcher", "rilErrorToSmsManagerResult: " + error + " " + SmsController.formatCrossStackMessageId(smsTracker.mMessageId));
                return 124;
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.SMSDispatcher$4, reason: invalid class name */
    static /* synthetic */ class AnonymousClass4 {
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

    protected void sendData(String str, int i, String str2, String str3, int i2, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, long j, int i3) {
        int iNextMessageRef = nextMessageRef();
        SmsMessageBase.SubmitPduBase submitPduBaseOnSendData = onSendData(str3, str2, i2, bArr, pendingIntent2 != null, iNextMessageRef);
        SmsMessageBase.SubmitPduBase submitPdu = MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).getSubmitPdu(this.mPhone, str3, str2, bArr, pendingIntent2 != null, submitPduBaseOnSendData, i2, pendingIntent, pendingIntent2);
        SmsMessageBase.SubmitPduBase submitPduBase = submitPdu != null ? submitPdu : submitPduBaseOnSendData;
        if (submitPduBase != null) {
            SmsTracker smsTracker = getSmsTracker(str, i, getSmsTrackerMap(str2, str3, i2, bArr, submitPduBase), pendingIntent, pendingIntent2, getFormat(), null, false, null, false, true, z, 0L, iNextMessageRef, j, i3);
            if (sendSmsByCarrierApp(true, smsTracker)) {
                return;
            }
            sendSubmitPdu(smsTracker);
            return;
        }
        Rlog.e("SMSDispatcher", "SMSDispatcher.sendData(): getSubmitPdu() returned null");
        triggerSentIntentForFailure(pendingIntent);
    }

    public void sendText(String str, String str2, String str3, PendingIntent pendingIntent, PendingIntent pendingIntent2, Uri uri, String str4, int i, boolean z, int i2, boolean z2, int i3, boolean z3, long j, int i4) {
        sendText(str, str2, str3, pendingIntent, pendingIntent2, uri, str4, i, z, i2, z2, i3, z3, j, false, SmsDispatchersController.PendingRequest.getNextUniqueMessageId(), i4);
    }

    public void sendText(String str, String str2, String str3, PendingIntent pendingIntent, PendingIntent pendingIntent2, Uri uri, String str4, int i, boolean z, int i2, boolean z2, int i3, boolean z3, long j, boolean z4, long j2, int i4) {
        Rlog.d("SMSDispatcher", "sendText id: " + SmsController.formatCrossStackMessageId(j));
        int iNextMessageRef = nextMessageRef();
        SmsMessageBase.SubmitPduBase submitPduBaseOnSendText = onSendText(str2, str, str3, pendingIntent2 != null, uri, str4, z, i2, z2, i3, iNextMessageRef);
        if (submitPduBaseOnSendText != null) {
            HashMap<String, Object> smsTrackerMap = getSmsTrackerMap(str, str2, str3, submitPduBaseOnSendText);
            smsTrackerMap.put(MiuiSmsControllerStub.CALLING_PACKAGE_NAME_KEY, str4);
            final SmsTracker smsTracker = getSmsTracker(str4, i, smsTrackerMap, pendingIntent, pendingIntent2, getFormat(), uri, z2, str3, true, z, i2, i3, z3, j, iNextMessageRef, z4, j2, i4);
            if (z) {
                if (MessageUpgradeController.isMessageUpgradeSupportedForPackage(this.mContext, i, str4, true)) {
                    smsTracker.persistPendingMessageIfRequired(this.mContext);
                    if (smsTracker.mMessageUri != null) {
                        Rlog.d("SMSDispatcher", "sendText: requesting message upgrade via DMA.");
                        MessageUpgradeController.upgradeMessage(this.mContext, i, smsTracker.mMessageUri, Collections.singletonList(smsTracker.mSentIntent), Collections.singletonList(smsTracker.mDeliveryIntent), new CarrierPrivilegesTracker$$ExternalSyntheticLambda0(), new Consumer() { // from class: com.android.internal.telephony.SMSDispatcher$$ExternalSyntheticLambda1
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                this.f$0.lambda$sendText$0(smsTracker, (Integer) obj);
                            }
                        });
                        return;
                    }
                } else {
                    Uri uri2 = smsTracker.mMessageUri;
                    if (uri2 != null) {
                        markMessageAsUnrestricted(uri2);
                        Rlog.d("SMSDispatcher", "sendText: message upgrade is not supported or the calling app is DMA.");
                    }
                }
            }
            if (sendSmsByCarrierApp(false, smsTracker)) {
                return;
            }
            sendSubmitPdu(smsTracker);
            return;
        }
        Rlog.e("SMSDispatcher", "SmsDispatcher.sendText(): getSubmitPdu() returned null " + SmsController.formatCrossStackMessageId(j));
        triggerSentIntentForFailure(pendingIntent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendText$0(SmsTracker smsTracker, Integer num) {
        if (num.intValue() != 1) {
            Rlog.d("SMSDispatcher", "sendText: message upgrade request failed.");
            markMessageAsUnrestricted(smsTracker.mMessageUri);
            if (sendSmsByCarrierApp(false, smsTracker)) {
                return;
            }
            sendSubmitPdu(smsTracker);
        }
    }

    private void markMessageAsUnrestricted(final Uri uri) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.internal.telephony.SMSDispatcher$$ExternalSyntheticLambda2
            public final void runOrThrow() {
                this.f$0.lambda$markMessageAsUnrestricted$1(uri);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$markMessageAsUnrestricted$1(Uri uri) {
        Telephony.ReadRestriction.unrestrictMessage(this.mContext.getContentResolver(), uri);
    }

    private void triggerSentIntentForFailure(PendingIntent pendingIntent) {
        if (pendingIntent != null) {
            try {
                pendingIntent.send(1);
            } catch (PendingIntent.CanceledException unused) {
                Rlog.e("SMSDispatcher", "Intent has been canceled!");
            }
        }
    }

    protected void triggerSentIntentForFailure(List<PendingIntent> list) {
        if (list == null) {
            return;
        }
        Iterator<PendingIntent> it = list.iterator();
        while (it.hasNext()) {
            triggerSentIntentForFailure(it.next());
        }
    }

    protected boolean sendSmsByCarrierApp(boolean z, SmsTracker smsTracker) {
        SmsSender textSmsSender;
        String carrierAppPackageName = getCarrierAppPackageName();
        if (carrierAppPackageName == null) {
            return false;
        }
        Rlog.d("SMSDispatcher", "Found carrier package ".concat(carrierAppPackageName));
        if (z) {
            textSmsSender = new DataSmsSender(smsTracker);
        } else {
            textSmsSender = new TextSmsSender(smsTracker);
        }
        textSmsSender.sendSmsByCarrierApp(carrierAppPackageName, new SmsSenderCallback(textSmsSender));
        return true;
    }

    public void sendMultipartText(String str, String str2, final ArrayList<String> arrayList, ArrayList<PendingIntent> arrayList2, ArrayList<PendingIntent> arrayList3, Uri uri, String str3, int i, boolean z, int i2, boolean z2, int i3, long j, long j2, int i4) {
        String str4;
        int i5;
        boolean zIsMessageUpgradeSupportedForPackage;
        SMSDispatcher sMSDispatcher = this;
        String multipartMessageText = sMSDispatcher.getMultipartMessageText(arrayList);
        int nextConcatenatedRef = getNextConcatenatedRef() & 255;
        int size = arrayList.size();
        if (size < 1) {
            sMSDispatcher.triggerSentIntentForFailure(arrayList2);
            return;
        }
        GsmAlphabet.TextEncodingDetails[] textEncodingDetailsArr = new GsmAlphabet.TextEncodingDetails[size];
        String str5 = multipartMessageText;
        int iOnSendMultipartText = sMSDispatcher.onSendMultipartText(str, str2, arrayList, arrayList2, arrayList3, uri, str3, z, i2, z2, i3, textEncodingDetailsArr);
        ArrayList<String> arrayList4 = arrayList;
        ArrayList<PendingIntent> arrayList5 = arrayList2;
        ArrayList<PendingIntent> arrayList6 = arrayList3;
        SmsTracker[] smsTrackerArr = new SmsTracker[size];
        AtomicInteger atomicInteger = new AtomicInteger(size);
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        if (z) {
            str4 = str3;
            i5 = i;
            zIsMessageUpgradeSupportedForPackage = MessageUpgradeController.isMessageUpgradeSupportedForPackage(sMSDispatcher.mContext, i5, str4, true);
        } else {
            str4 = str3;
            i5 = i;
            zIsMessageUpgradeSupportedForPackage = false;
        }
        int i6 = 0;
        while (i6 < size) {
            SmsHeader.ConcatRef concatRef = new SmsHeader.ConcatRef();
            concatRef.refNumber = nextConcatenatedRef;
            int i7 = i6 + 1;
            concatRef.seqNumber = i7;
            concatRef.msgCount = size;
            concatRef.isEightBits = true;
            SmsHeader smsHeaderMakeSmsHeader = TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName()).makeSmsHeader();
            smsHeaderMakeSmsHeader.concatRef = concatRef;
            if (iOnSendMultipartText == 1) {
                GsmAlphabet.TextEncodingDetails textEncodingDetails = textEncodingDetailsArr[i6];
                smsHeaderMakeSmsHeader.languageTable = textEncodingDetails.languageTable;
                smsHeaderMakeSmsHeader.languageShiftTable = textEncodingDetails.languageShiftTable;
            }
            PendingIntent pendingIntent = (arrayList5 == null || arrayList5.size() <= i6) ? null : arrayList5.get(i6);
            PendingIntent pendingIntent2 = (arrayList6 == null || arrayList6.size() <= i6) ? null : arrayList6.get(i6);
            int iNextMessageRef = nextMessageRef();
            String str6 = arrayList4.get(i6);
            int i8 = size - 1;
            boolean z3 = i6 == i8;
            SmsTracker[] smsTrackerArr2 = smsTrackerArr;
            int i9 = i6;
            String str7 = str4;
            int i10 = nextConcatenatedRef;
            int i11 = size;
            int i12 = iOnSendMultipartText;
            String str8 = str5;
            AtomicBoolean atomicBoolean2 = atomicBoolean;
            SmsTracker newSubmitPduTracker = getNewSubmitPduTracker(str7, i5, str, str2, str6, smsHeaderMakeSmsHeader, i12, pendingIntent, pendingIntent2, z3, atomicInteger, atomicBoolean2, uri, str8, i2, z2, i3, j, iNextMessageRef, j2, i4);
            smsTrackerArr2[i9] = newSubmitPduTracker;
            if (newSubmitPduTracker == null) {
                triggerSentIntentForFailure(arrayList2);
                return;
            }
            newSubmitPduTracker.mPersistMessage = z;
            if (zIsMessageUpgradeSupportedForPackage && i9 == i8) {
                newSubmitPduTracker.persistPendingMessageIfRequired(this.mContext);
            }
            arrayList6 = arrayList3;
            i5 = i;
            sMSDispatcher = this;
            arrayList5 = arrayList2;
            atomicBoolean = atomicBoolean2;
            str5 = str8;
            i6 = i7;
            nextConcatenatedRef = i10;
            size = i11;
            smsTrackerArr = smsTrackerArr2;
            arrayList4 = arrayList;
            iOnSendMultipartText = i12;
            str4 = str3;
        }
        final SMSDispatcher sMSDispatcher2 = sMSDispatcher;
        final SmsTracker[] smsTrackerArr3 = smsTrackerArr;
        int i13 = size;
        ArrayList<PendingIntent> arrayList7 = arrayList5;
        if (zIsMessageUpgradeSupportedForPackage) {
            int i14 = i13 - 1;
            if (smsTrackerArr3[i14].mMessageUri != null) {
                Rlog.d("SMSDispatcher", "sendMultipartText: requesting message upgrade via DMA.");
                MessageUpgradeController.upgradeMessage(sMSDispatcher2.mContext, i, smsTrackerArr3[i14].mMessageUri, arrayList7, arrayList3, new CarrierPrivilegesTracker$$ExternalSyntheticLambda0(), new Consumer() { // from class: com.android.internal.telephony.SMSDispatcher$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.lambda$sendMultipartText$2(arrayList, smsTrackerArr3, (Integer) obj);
                    }
                });
                return;
            }
        }
        sMSDispatcher2.sendMultipartTextByCarrierApp(arrayList, smsTrackerArr3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMultipartText$2(ArrayList arrayList, SmsTracker[] smsTrackerArr, Integer num) {
        if (num.intValue() != 1) {
            Rlog.d("SMSDispatcher", "sendText: message upgrade request failed.");
            sendMultipartTextByCarrierApp(arrayList, smsTrackerArr);
        }
    }

    private void sendMultipartTextByCarrierApp(ArrayList arrayList, SmsTracker[] smsTrackerArr) {
        String carrierAppPackageName = getCarrierAppPackageName();
        if (carrierAppPackageName != null) {
            Rlog.d("SMSDispatcher", "Found carrier package " + carrierAppPackageName + " " + SmsController.formatCrossStackMessageId(getMultiTrackermessageId(smsTrackerArr)));
            MultipartSmsSender multipartSmsSender = new MultipartSmsSender(arrayList, smsTrackerArr);
            multipartSmsSender.sendSmsByCarrierApp(carrierAppPackageName, new SmsSenderCallback(multipartSmsSender));
            return;
        }
        Rlog.v("SMSDispatcher", "No carrier package. " + SmsController.formatCrossStackMessageId(getMultiTrackermessageId(smsTrackerArr)));
        sendSubmitPdu(smsTrackerArr);
    }

    protected long getMultiTrackermessageId(SmsTracker[] smsTrackerArr) {
        if (smsTrackerArr.length == 0) {
            return 0L;
        }
        return smsTrackerArr[0].mMessageId;
    }

    protected SmsTracker getNewSubmitPduTracker(String str, int i, String str2, String str3, String str4, SmsHeader smsHeader, int i2, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, AtomicInteger atomicInteger, AtomicBoolean atomicBoolean, Uri uri, String str5, int i3, boolean z2, int i4, long j, int i5, long j2, int i6) {
        if (isCdmaMo()) {
            UserData userData = new UserData();
            userData.payloadStr = str4;
            userData.userDataHeader = smsHeader;
            if (i2 == 1) {
                userData.msgEncoding = isAscii7bitSupportedForLongMessage() ? 2 : 9;
                Rlog.d("SMSDispatcher", "Message encoding for proper 7 bit: " + userData.msgEncoding);
            } else {
                userData.msgEncoding = 4;
            }
            userData.msgEncodingSet = true;
            SmsMessage.SubmitPdu submitPdu = SmsMessage.getSubmitPdu(str2, userData, pendingIntent2 != null && z, i3);
            if (submitPdu != null) {
                return getSmsTracker(str, i, getSmsTrackerMap(str2, str3, str4, submitPdu), pendingIntent, pendingIntent2, getFormat(), atomicInteger, atomicBoolean, uri, smsHeader, !z || z2, str5, true, true, i3, i4, false, j, i5, false, j2, i6);
            }
            Rlog.e("SMSDispatcher", "CdmaSMSDispatcher.getNewSubmitPduTracker(): getSubmitPdu() returned null " + SmsController.formatCrossStackMessageId(j));
            return null;
        }
        com.android.internal.telephony.gsm.SmsMessage.SubmitPdu submitPdu2 = com.android.internal.telephony.gsm.SmsMessage.getSubmitPdu(str3, str2, str4, pendingIntent2 != null, SmsHeader.toByteArray(smsHeader), i2, smsHeader.languageTable, smsHeader.languageShiftTable, i4, i5);
        if (submitPdu2 != null) {
            return getSmsTracker(str, i, getSmsTrackerMap(str2, str3, str4, submitPdu2), pendingIntent, pendingIntent2, getFormat(), atomicInteger, atomicBoolean, uri, smsHeader, !z || z2, str5, true, false, i3, i4, false, j, i5, false, j2, i6);
        }
        Rlog.e("SMSDispatcher", "GsmSMSDispatcher.getNewSubmitPduTracker(): getSubmitPdu() returned null " + SmsController.formatCrossStackMessageId(j));
        return null;
    }

    public void sendRawPdu(String str, int i, String str2, String str3, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2, int i2) {
        SmsMessageBase.SubmitPduBase submitPduBase = new SmsMessageBase.SubmitPduBase(this) { // from class: com.android.internal.telephony.SMSDispatcher.2
        };
        if (str3 == null || TextUtils.isEmpty(str3)) {
            submitPduBase.encodedScAddress = null;
        } else {
            submitPduBase.encodedScAddress = PhoneNumberUtils.networkPortionToCalledPartyBCDWithLength(str3);
        }
        submitPduBase.encodedMessage = bArr;
        sendSubmitPdu(getSmsTracker(str, i, getSmsTrackerMap(str2, str3, -1, null, submitPduBase), pendingIntent, pendingIntent2, getFormat(), null, false, null, false, true, -1, -1, false, 0L, 0, true, SmsDispatchersController.PendingRequest.getNextUniqueMessageId(), i2));
    }

    public void sendRawPdu(SmsTracker[] smsTrackerArr) {
        int i;
        PackageInfo packageInfo = null;
        if (this.mSmsSendDisabled) {
            Rlog.e("SMSDispatcher", "Device does not support sending sms.");
            i = 4;
        } else {
            int length = smsTrackerArr.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    i = 0;
                    break;
                } else {
                    if (smsTrackerArr[i2].getData().get(MAP_KEY_PDU) == null) {
                        Rlog.e("SMSDispatcher", "Empty PDU");
                        i = 3;
                        break;
                    }
                    i2++;
                }
            }
            if (i == 0) {
                try {
                    packageInfo = this.mContext.createContextAsUser(UserHandle.of(smsTrackerArr[0].mUserId), 0).getPackageManager().getPackageInfo(smsTrackerArr[0].getAppPackageName(), 64);
                } catch (PackageManager.NameNotFoundException unused) {
                    Rlog.e("SMSDispatcher", "Can't get calling app package info: refusing to send SMS " + SmsController.formatCrossStackMessageId(getMultiTrackermessageId(smsTrackerArr)));
                    i = 1;
                }
            }
        }
        int iCarrierTureError = this.mIHyperTeleSmsManager.carrierTureError(this.mContext, i, this.mPhone);
        if (iCarrierTureError != 0) {
            handleSmsTrackersFailure(smsTrackerArr, iCarrierTureError, -1);
            return;
        }
        if (checkDestination(smsTrackerArr)) {
            if (MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).shouldCheckSmsUsage(this.mPhone, packageInfo.packageName) && !this.mSmsDispatchersController.getUsageMonitor().check(packageInfo.packageName, smsTrackerArr.length)) {
                sendMessage(obtainMessage(4, smsTrackerArr));
                return;
            }
            for (SmsTracker smsTracker : smsTrackerArr) {
                sendSms(smsTracker);
            }
        }
        if (this.mPhone.hasCalling() && this.mTelephonyManager.isEmergencyNumber(smsTrackerArr[0].mDestAddress)) {
            new AsyncEmergencyContactNotifier(this.mContext).execute(new Void[0]);
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.SMSDispatcher$3, reason: invalid class name */
    class AnonymousClass3 implements SmsUsageMonitor.SmsAuthorizationCallback {
        final /* synthetic */ SMSDispatcher this$0;
        final /* synthetic */ SmsTracker val$tracker;

        @Override // com.android.internal.telephony.SmsUsageMonitor.SmsAuthorizationCallback
        public void onAuthorizationResult(boolean z) {
            if (z) {
                this.this$0.sendSms(this.val$tracker);
            } else {
                this.val$tracker.onFailed(this.this$0.mContext, 1, 191286);
            }
        }
    }

    protected boolean checkDestination(SmsTracker[] smsTrackerArr) {
        int iCheckDestination;
        if (this.mContext.checkCallingOrSelfPermission("android.permission.SEND_SMS_NO_CONFIRMATION") == 0 || smsTrackerArr[0].mIsForVvm || smsTrackerArr[0].mSkipShortCodeDestAddrCheck) {
            Rlog.d("SMSDispatcher", "checkDestination: app pre-approved");
            return true;
        }
        int i = this.mPremiumSmsRule.get();
        if (i == 1 || i == 3) {
            String simCountryIsoForPhone = TelephonyManager.getSimCountryIsoForPhone(this.mPhone.getPhoneId());
            if (simCountryIsoForPhone == null || simCountryIsoForPhone.length() != 2) {
                Rlog.e("SMSDispatcher", "Can't get SIM country Iso: trying network country Iso " + SmsController.formatCrossStackMessageId(getMultiTrackermessageId(smsTrackerArr)));
                simCountryIsoForPhone = this.mTelephonyManager.getNetworkCountryIso(this.mPhone.getPhoneId());
            }
            iCheckDestination = this.mSmsDispatchersController.getUsageMonitor().checkDestination(smsTrackerArr[0].mDestAddress, simCountryIsoForPhone);
            Rlog.d("SMSDispatcher", "checkDestination: simCountryIso=" + simCountryIsoForPhone);
        } else {
            iCheckDestination = 0;
        }
        if (i == 2 || i == 3) {
            String networkCountryIso = this.mTelephonyManager.getNetworkCountryIso(this.mPhone.getPhoneId());
            if (networkCountryIso == null || networkCountryIso.length() != 2) {
                Rlog.e("SMSDispatcher", "Can't get Network country Iso: trying SIM country Iso " + SmsController.formatCrossStackMessageId(getMultiTrackermessageId(smsTrackerArr)));
                networkCountryIso = TelephonyManager.getSimCountryIsoForPhone(this.mPhone.getPhoneId());
            }
            iCheckDestination = SmsUsageMonitor.mergeShortCodeCategories(iCheckDestination, this.mSmsDispatchersController.getUsageMonitor().checkDestination(smsTrackerArr[0].mDestAddress, networkCountryIso));
            Rlog.d("SMSDispatcher", "checkDestination: networkCountryIso=" + networkCountryIso);
        }
        Rlog.d("SMSDispatcher", "checkDestination: smsCategory=" + iCheckDestination);
        if (iCheckDestination != 0) {
            this.mPhone.getSmsStats().onOutgoingShortCodeSms(iCheckDestination, this.mSmsDispatchersController.getUsageMonitor().getShortCodeXmlFileVersion());
        }
        if (iCheckDestination == 0 || iCheckDestination == 1 || iCheckDestination == 2) {
            return true;
        }
        if (Settings.Global.getInt(this.mResolver, "device_provisioned", 0) == 0) {
            Rlog.e("SMSDispatcher", "Can't send premium sms during Setup Wizard " + SmsController.formatCrossStackMessageId(getMultiTrackermessageId(smsTrackerArr)));
            return false;
        }
        if (shouldBlockPremiumSmsInSatelliteMode()) {
            Rlog.d("SMSDispatcher", "Block premium SMS in satellite mode. messageId=" + SmsController.formatCrossStackMessageId(getMultiTrackermessageId(smsTrackerArr)));
            return false;
        }
        int premiumSmsPermission = this.mSmsDispatchersController.getUsageMonitor().getPremiumSmsPermission(smsTrackerArr[0].getAppPackageName());
        if (premiumSmsPermission == 0) {
            premiumSmsPermission = 1;
        }
        if (premiumSmsPermission == 2) {
            Rlog.w("SMSDispatcher", "User denied this app from sending to premium SMS " + SmsController.formatCrossStackMessageId(getMultiTrackermessageId(smsTrackerArr)));
            sendMessage(obtainMessage(7, smsTrackerArr));
            return false;
        }
        if (premiumSmsPermission == 3) {
            Rlog.d("SMSDispatcher", "User approved this app to send to premium SMS " + SmsController.formatCrossStackMessageId(getMultiTrackermessageId(smsTrackerArr)));
            return true;
        }
        sendMessage(obtainMessage(iCheckDestination == 3 ? 8 : 9, smsTrackerArr));
        return false;
    }

    public boolean shouldBlockPremiumSmsInSatelliteMode() {
        SatelliteController satelliteController = SatelliteController.getInstance();
        if (satelliteController.isSatelliteBeingEnabled()) {
            Rlog.d("SMSDispatcher", "shouldBlockPremiumSmsInSatelliteMode: block premium sms when satellite is being enabled");
            return true;
        }
        if (satelliteController.isSatelliteEnabled()) {
            int selectedSatelliteSubId = satelliteController.getSelectedSatelliteSubId();
            int[] supportedServicesOnCarrierRoamingNtn = satelliteController.getSupportedServicesOnCarrierRoamingNtn(selectedSatelliteSubId);
            boolean zContains = ArrayUtils.contains(supportedServicesOnCarrierRoamingNtn, 3);
            Rlog.d("SMSDispatcher", "shouldBlockPremiumSmsInSatelliteMode: satelliteSubId=" + selectedSatelliteSubId + " isSmsSupported=" + zContains + " services=" + Arrays.toString(supportedServicesOnCarrierRoamingNtn));
            return !zContains;
        }
        Rlog.d("SMSDispatcher", "shouldBlockPremiumSmsInSatelliteMode: return false.");
        return false;
    }

    private boolean denyIfQueueLimitReached(SmsTracker[] smsTrackerArr) {
        int i = this.mPendingTrackerCount;
        if (i >= 5) {
            Rlog.e("SMSDispatcher", "Denied because queue limit reached " + SmsController.formatCrossStackMessageId(getMultiTrackermessageId(smsTrackerArr)));
            handleSmsTrackersFailure(smsTrackerArr, 5, -1);
            return true;
        }
        this.mPendingTrackerCount = i + 1;
        return false;
    }

    private CharSequence getAppLabel(String str, int i) {
        PackageManager packageManager = this.mContext.getPackageManager();
        try {
            return packageManager.getApplicationInfoAsUser(str, 0, UserHandle.of(i)).loadSafeLabel(packageManager);
        } catch (PackageManager.NameNotFoundException unused) {
            Rlog.e("SMSDispatcher", "PackageManager Name Not Found for package " + str);
            return str;
        }
    }

    protected void handleReachSentLimit(SmsTracker[] smsTrackerArr) {
        if (denyIfQueueLimitReached(smsTrackerArr)) {
            return;
        }
        CharSequence appLabel = getAppLabel(smsTrackerArr[0].getAppPackageName(), smsTrackerArr[0].mUserId);
        Resources system = Resources.getSystem();
        Spanned spannedFromHtml = Html.fromHtml(system.getString(17042251, appLabel));
        ConfirmDialogListener confirmDialogListener = new ConfirmDialogListener(smsTrackerArr, null, 1);
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this.mContext).setTitle(17042253).setIcon(R.drawable.stat_sys_warning).setMessage(spannedFromHtml).setPositiveButton(system.getString(17042254), confirmDialogListener).setNegativeButton(system.getString(17042252), confirmDialogListener).setOnCancelListener(confirmDialogListener).create();
        alertDialogCreate.getWindow().setType(TelephonyProto$TelephonyEvent.RilSetupDataCallResponse.RilDataCallFailCause.PDP_FAIL_MIP_FA_MOBILE_NODE_AUTHENTICATION_FAILURE);
        alertDialogCreate.show();
    }

    protected void handleConfirmShortCode(boolean z, SmsTracker[] smsTrackerArr) {
        if (denyIfQueueLimitReached(smsTrackerArr)) {
            return;
        }
        int i = z ? 17042255 : 17042261;
        CharSequence appLabel = getAppLabel(smsTrackerArr[0].getAppPackageName(), smsTrackerArr[0].mUserId);
        Resources system = Resources.getSystem();
        Spanned spannedFromHtml = Html.fromHtml(system.getString(17042259, appLabel, smsTrackerArr[0].mDestAddress));
        View viewInflate = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.simple_account_item, (ViewGroup) null);
        ConfirmDialogListener confirmDialogListener = new ConfirmDialogListener(smsTrackerArr, (TextView) viewInflate.findViewById(R.id.textLongMessage), 0);
        ((TextView) viewInflate.findViewById(R.id.textEmailSubject)).setText(spannedFromHtml);
        ((TextView) ((ViewGroup) viewInflate.findViewById(R.id.textEnableTextConversionSuggestions)).findViewById(R.id.textEnd)).setText(i);
        ((CheckBox) viewInflate.findViewById(R.id.textFilter)).setOnCheckedChangeListener(confirmDialogListener);
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this.mContext).setView(viewInflate).setPositiveButton(system.getString(17042256), confirmDialogListener).setNegativeButton(system.getString(17042258), confirmDialogListener).setOnCancelListener(confirmDialogListener).create();
        alertDialogCreate.getWindow().setType(TelephonyProto$TelephonyEvent.RilSetupDataCallResponse.RilDataCallFailCause.PDP_FAIL_MIP_FA_MOBILE_NODE_AUTHENTICATION_FAILURE);
        alertDialogCreate.show();
        confirmDialogListener.setPositiveButton(alertDialogCreate.getButton(-1));
        confirmDialogListener.setNegativeButton(alertDialogCreate.getButton(-2));
    }

    public void sendRetrySms(SmsTracker smsTracker) {
        SmsDispatchersController smsDispatchersController = this.mSmsDispatchersController;
        if (smsDispatchersController != null) {
            smsDispatchersController.sendRetrySms(smsTracker);
            return;
        }
        Rlog.e("SMSDispatcher", this.mSmsDispatchersController + " is null. Retry failed " + SmsController.formatCrossStackMessageId(smsTracker.mMessageId));
    }

    protected void handleSmsTrackersFailure(SmsTracker[] smsTrackerArr, int i, int i2) {
        TelephonyAnalytics telephonyAnalytics;
        TelephonyAnalytics.SmsMmsAnalytics smsMmsAnalytics;
        for (SmsTracker smsTracker : smsTrackerArr) {
            smsTracker.onFailed(this.mContext, i, i2);
            notifySmsSentFailedToEmergencyStateTracker(smsTracker, false);
            MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).sendSmsDotBroadcast(this.mPhone, smsTracker, this.mSmsDispatchersController.getSmscAddress(), i2, false);
        }
        if (smsTrackerArr.length > 0) {
            SmsStats smsStats = this.mPhone.getSmsStats();
            boolean zIsIms = isIms();
            boolean zEquals = "3gpp2".equals(getFormat());
            SmsTracker smsTracker2 = smsTrackerArr[0];
            smsStats.onOutgoingSms(zIsIms, zEquals, false, i, smsTracker2.mMessageId, smsTracker2.isFromDefaultSmsApplication(this.mContext), smsTrackerArr[0].getInterval(), this.mTelephonyManager.isEmergencyNumber(smsTrackerArr[0].mDestAddress), smsTrackerArr[0].isMtSmsPollingMessage(this.mContext), smsTrackerArr[0].getPduLength(), smsTrackerArr[0].getAppPackageName(), smsTrackerArr[0].getAppUid());
            Phone phone = this.mPhone;
            if (phone == null || (telephonyAnalytics = phone.getTelephonyAnalytics()) == null || (smsMmsAnalytics = telephonyAnalytics.getSmsMmsAnalytics()) == null) {
                return;
            }
            smsMmsAnalytics.onOutgoingSms(isIms(), i);
        }
    }

    public static class SmsTracker {
        protected static String MSG_REF_NUM = "msg_ref_num";
        protected static String PDU_SIZE = "pdu_size";
        private final UUID mAnomalyUnexpectedErrorFromRilUUID;
        private AtomicBoolean mAnyPartFailed;
        public final PackageInfo mAppInfo;
        private final int mAppUid;
        private int mCarrierId;
        public final HashMap<String, Object> mData;
        public final PendingIntent mDeliveryIntent;
        public final String mDestAddress;
        public boolean mExpectMore;
        public String mFormat;
        public String mFullMessageText;
        public int mImsRetry;
        public boolean mIsFallBackRetry;
        private final boolean mIsForVvm;
        private Boolean mIsFromDefaultSmsApplication;
        private boolean mIsText;
        public final long mMessageId;
        public int mMessageRef;
        public Uri mMessageUri;
        public boolean mPersistMessage;
        public int mPriority;
        public int mResultCodeFromCarrierMessagingService;
        public int mRetryCount;
        public final PendingIntent mSentIntent;
        private boolean mSkipShortCodeDestAddrCheck;
        public final SmsHeader mSmsHeader;
        public ArrayList<Object> mSmsOptimizationList;
        public int mSmsOptimizationStatus;
        public int mSubId;
        private long mTimestamp;
        public final long mUniqueMessageId;
        private AtomicInteger mUnsentPartCount;
        public final int mUserId;
        public boolean mUsesImsServiceForIms;
        public int mValidityPeriod;

        public SmsTracker(HashMap<String, Object> map, PendingIntent pendingIntent, PendingIntent pendingIntent2, PackageInfo packageInfo, String str, String str2, AtomicInteger atomicInteger, AtomicBoolean atomicBoolean, Uri uri, SmsHeader smsHeader, boolean z, String str3, int i, boolean z2, boolean z3, int i2, int i3, int i4, boolean z4, long j, int i5, int i6, boolean z5, long j2, int i7) {
            this.mTimestamp = SystemClock.elapsedRealtime();
            this.mAnomalyUnexpectedErrorFromRilUUID = UUID.fromString("43043600-ea7a-44d2-9ae6-a58567ac7886");
            this.mData = map;
            this.mSentIntent = pendingIntent;
            this.mDeliveryIntent = pendingIntent2;
            this.mRetryCount = 0;
            this.mAppInfo = packageInfo;
            this.mDestAddress = str;
            this.mFormat = str2;
            this.mExpectMore = z;
            this.mImsRetry = 0;
            this.mUsesImsServiceForIms = false;
            this.mMessageRef = i6;
            this.mUnsentPartCount = atomicInteger;
            this.mAnyPartFailed = atomicBoolean;
            this.mMessageUri = uri;
            this.mSmsHeader = smsHeader;
            this.mFullMessageText = str3;
            this.mSubId = i;
            this.mIsText = z2;
            this.mPersistMessage = z3;
            this.mUserId = i2;
            this.mPriority = i3;
            this.mValidityPeriod = i4;
            this.mIsForVvm = z4;
            this.mMessageId = j;
            this.mCarrierId = i5;
            this.mSkipShortCodeDestAddrCheck = z5;
            this.mSmsOptimizationStatus = 0;
            this.mSmsOptimizationList = new ArrayList<>();
            this.mUniqueMessageId = j2;
            this.mResultCodeFromCarrierMessagingService = 0;
            this.mAppUid = i7;
        }

        public SmsTracker(String str, long j, String str2) {
            this.mTimestamp = SystemClock.elapsedRealtime();
            this.mAnomalyUnexpectedErrorFromRilUUID = UUID.fromString("43043600-ea7a-44d2-9ae6-a58567ac7886");
            this.mData = null;
            this.mSentIntent = null;
            this.mDeliveryIntent = null;
            this.mAppInfo = null;
            this.mDestAddress = str;
            this.mUsesImsServiceForIms = false;
            this.mSmsHeader = null;
            this.mMessageId = j;
            this.mUserId = 0;
            this.mPriority = 0;
            this.mValidityPeriod = 0;
            this.mIsForVvm = false;
            this.mCarrierId = 0;
            this.mSkipShortCodeDestAddrCheck = false;
            this.mUniqueMessageId = 0L;
            this.mResultCodeFromCarrierMessagingService = 0;
            this.mFullMessageText = str2;
            this.mAppUid = -1;
        }

        public SmsTracker(HashMap<String, Object> map, String str, String str2, long j, int i, String str3) {
            this.mTimestamp = SystemClock.elapsedRealtime();
            this.mAnomalyUnexpectedErrorFromRilUUID = UUID.fromString("43043600-ea7a-44d2-9ae6-a58567ac7886");
            this.mData = map;
            this.mDestAddress = str;
            this.mFormat = str2;
            this.mMessageId = j;
            this.mRetryCount = i;
            this.mFullMessageText = str3;
            this.mAppInfo = null;
            this.mSentIntent = null;
            this.mDeliveryIntent = null;
            this.mSmsHeader = null;
            this.mExpectMore = false;
            this.mUsesImsServiceForIms = false;
            this.mMessageRef = 0;
            this.mValidityPeriod = -1;
            this.mPriority = 0;
            this.mIsText = true;
            this.mPersistMessage = false;
            this.mUserId = 0;
            this.mSubId = 0;
            this.mIsForVvm = false;
            this.mCarrierId = 0;
            this.mSkipShortCodeDestAddrCheck = false;
            this.mUniqueMessageId = j;
            this.mResultCodeFromCarrierMessagingService = 0;
            this.mAppUid = -1;
        }

        public HashMap<String, Object> getData() {
            return this.mData;
        }

        public String getAppPackageName() {
            PackageInfo packageInfo = this.mAppInfo;
            if (packageInfo != null) {
                return packageInfo.packageName;
            }
            return null;
        }

        ApplicationInfo getAppInfo() {
            PackageInfo packageInfo = this.mAppInfo;
            if (packageInfo == null) {
                return null;
            }
            return packageInfo.applicationInfo;
        }

        public boolean isFromDefaultSmsApplication(Context context) {
            if (this.mIsFromDefaultSmsApplication == null) {
                long jClearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    UserHandle userHandleOf = UserHandle.of(this.mUserId);
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    this.mIsFromDefaultSmsApplication = Boolean.valueOf(SmsApplication.isDefaultSmsApplicationAsUser(context, getAppPackageName(), userHandleOf));
                } catch (Throwable th) {
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    throw th;
                }
            }
            return this.mIsFromDefaultSmsApplication.booleanValue();
        }

        public boolean isMtSmsPollingMessage(Context context) {
            if (this.mFullMessageText == null) {
                return false;
            }
            return this.mFullMessageText.equals(context.getResources().getString(R.string.db_wal_sync_mode));
        }

        public int getPduLength() {
            byte[] bArr;
            HashMap<String, Object> map = this.mData;
            if (map == null || (bArr = (byte[]) map.get(SMSDispatcher.MAP_KEY_PDU)) == null) {
                return 0;
            }
            return bArr.length;
        }

        public void updateSentMessageStatus(Context context, int i) {
            if (this.mMessageUri != null) {
                ContentValues contentValues = new ContentValues(1);
                contentValues.put("status", Integer.valueOf(i));
                context.getContentResolver().update(this.mMessageUri, contentValues, null, null);
            }
        }

        private void updateMessageState(Context context, int i, int i2) {
            if (this.mMessageUri == null) {
                return;
            }
            ContentValues contentValues = new ContentValues(4);
            contentValues.put("type", Integer.valueOf(i));
            contentValues.put("error_code", Integer.valueOf(i2));
            contentValues.put("seen", (Integer) 1);
            contentValues.put("read", (Integer) 1);
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                if (context.getContentResolver().update(this.mMessageUri, contentValues, null, null) != 1) {
                    Rlog.e("SMSDispatcher", "Failed to move message to " + i);
                }
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }

        protected long getInterval() {
            return SystemClock.elapsedRealtime() - this.mTimestamp;
        }

        protected boolean isSinglePartOrLastPart() {
            AtomicInteger atomicInteger = this.mUnsentPartCount;
            return atomicInteger == null || atomicInteger.get() == 0;
        }

        protected boolean isAnyPartFailed() {
            AtomicBoolean atomicBoolean = this.mAnyPartFailed;
            return atomicBoolean != null && atomicBoolean.get();
        }

        private Uri persistSentMessageIfRequired(Context context, int i, int i2) {
            return persistMessageIfRequired(context, i, Telephony.Sms.Sent.CONTENT_URI, i2, 1, 1);
        }

        private Uri persistMessageIfRequired(Context context, int i, Uri uri, int i2, int i3, int i4) {
            String str;
            if (!this.mIsText || !this.mPersistMessage || isFromDefaultSmsApplication(context) || !SmsApplication.shouldWriteMessageForPackage(this.mAppInfo.packageName, context)) {
                return null;
            }
            if (i == 2) {
                str = "SENT";
            } else if (i == 4) {
                str = "OUTBOX";
            } else if (i == 5) {
                str = "FAILED";
            } else {
                str = SatelliteConstants.DEFAULT_PLMN;
            }
            Rlog.d("SMSDispatcher", "Persist SMS into ".concat(str));
            ContentValues contentValues = new ContentValues();
            contentValues.put("sub_id", Integer.valueOf(this.mSubId));
            contentValues.put("address", this.mDestAddress);
            contentValues.put("body", this.mFullMessageText);
            contentValues.put("date", Long.valueOf(System.currentTimeMillis()));
            contentValues.put("seen", Integer.valueOf(i3));
            contentValues.put("read", Integer.valueOf(i4));
            PackageInfo packageInfo = this.mAppInfo;
            String str2 = packageInfo != null ? packageInfo.packageName : null;
            if (!TextUtils.isEmpty(str2)) {
                contentValues.put("creator", str2);
            }
            if (this.mDeliveryIntent != null) {
                contentValues.put("status", (Integer) 32);
            }
            if (i2 != -1) {
                contentValues.put("error_code", Integer.valueOf(i2));
            }
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            ContentResolver contentResolver = context.getContentResolver();
            try {
                Uri uriInsert = contentResolver.insert(uri, contentValues);
                if (uriInsert != null && i == 5) {
                    ContentValues contentValues2 = new ContentValues(1);
                    contentValues2.put("type", (Integer) 5);
                    contentResolver.update(uriInsert, contentValues2, null, null);
                }
                return uriInsert;
            } catch (Exception e) {
                Rlog.e("SMSDispatcher", "writeOutboxMessage: Failed to persist outbox message", e);
                return null;
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }

        public void persistPendingMessageIfRequired(Context context) {
            if (this.mMessageUri != null) {
                return;
            }
            this.mMessageUri = persistMessageIfRequired(context, 4, Telephony.Sms.Outbox.CONTENT_URI, -1, 0, 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void persistOrUpdateMessage(Context context, int i, int i2) {
            if (this.mMessageUri != null) {
                updateMessageState(context, i, i2);
            } else {
                this.mMessageUri = persistSentMessageIfRequired(context, i, i2);
            }
        }

        public void onFailed(Context context, int i, int i2) {
            AtomicBoolean atomicBoolean = this.mAnyPartFailed;
            if (atomicBoolean != null) {
                atomicBoolean.set(true);
            }
            AtomicInteger atomicInteger = this.mUnsentPartCount;
            boolean z = atomicInteger == null || atomicInteger.decrementAndGet() == 0;
            if (z) {
                new AsyncPersistOrUpdateTask(context, 5, i2, i, true).execute(new Void[0]);
                MiEventUtils.trackerMiEvent(915700001, MiEventUtils.getPhoneId(this.mSubId, context));
                return;
            }
            if (this.mSentIntent != null) {
                try {
                    Intent intent = new Intent();
                    Uri uri = this.mMessageUri;
                    if (uri != null) {
                        intent.putExtra("uri", uri.toString());
                    }
                    if (i2 != -1) {
                        intent.putExtra(TelephonyStatAdapter.ERROR_CODE_KEY, i2);
                    }
                    if (this.mUnsentPartCount != null && z) {
                        intent.putExtra("SendNextMsg", true);
                    }
                    long j = this.mMessageId;
                    if (j != 0) {
                        intent.putExtra("MessageId", j);
                    }
                    intent.putExtra("format", this.mFormat);
                    intent.putExtra("ims", this.mUsesImsServiceForIms);
                    putPduSize(intent);
                    this.mSentIntent.send(context, i, intent);
                } catch (PendingIntent.CanceledException unused) {
                    Rlog.e("SMSDispatcher", "Failed to send result " + SmsController.formatCrossStackMessageId(this.mMessageId));
                }
            }
            reportAnomaly(i, i2);
            MiEventUtils.trackerMiEvent(915700001, MiEventUtils.getPhoneId(this.mSubId, context));
        }

        private void putPduSize(Intent intent) {
            HashMap<String, Object> map = this.mData;
            int length = 0;
            if (map != null) {
                length = (this.mData.get(SMSDispatcher.MAP_KEY_PDU) != null ? ((byte[]) this.mData.get(SMSDispatcher.MAP_KEY_PDU)).length : 0) + (map.get(SMSDispatcher.MAP_KEY_SMSC) != null ? ((byte[]) this.mData.get(SMSDispatcher.MAP_KEY_SMSC)).length : 0);
            }
            intent.putExtra(PDU_SIZE, length);
        }

        private void reportAnomaly(int i, int i2) {
            if (i == 2 || i == 29 || i == 4 || i == 5 || i == 7 || i == 8) {
                return;
            }
            Rlog.d("SMSDispatcher", "SMS failed with error " + i + ", errorCode " + i2);
            SatelliteController satelliteController = SatelliteController.getInstance();
            if (satelliteController != null && satelliteController.isSatelliteBeingDisabled()) {
                Rlog.d("SMSDispatcher", "isSatelliteBeingDisabled() are true, skip reporting anomaly");
            } else {
                AnomalyReporter.reportAnomaly(generateUUID(i, i2), "SMS failed", this.mCarrierId);
            }
        }

        private UUID generateUUID(int i, int i2) {
            return new UUID(this.mAnomalyUnexpectedErrorFromRilUUID.getMostSignificantBits(), this.mAnomalyUnexpectedErrorFromRilUUID.getLeastSignificantBits() + (((long) i2) << 32) + ((long) i));
        }

        public void onSent(Context context) {
            AtomicInteger atomicInteger = this.mUnsentPartCount;
            boolean z = atomicInteger == null || atomicInteger.decrementAndGet() == 0;
            if (z) {
                AtomicBoolean atomicBoolean = this.mAnyPartFailed;
                new AsyncPersistOrUpdateTask(context, (atomicBoolean == null || !atomicBoolean.get()) ? 2 : 5, 0, 0, false).execute(new Void[0]);
                return;
            }
            if (this.mSentIntent != null) {
                try {
                    Intent intent = new Intent();
                    Uri uri = this.mMessageUri;
                    if (uri != null) {
                        intent.putExtra("uri", uri.toString());
                    }
                    if (this.mUnsentPartCount != null && z) {
                        intent.putExtra("SendNextMsg", true);
                    }
                    intent.putExtra("format", this.mFormat);
                    intent.putExtra("ims", this.mUsesImsServiceForIms);
                    putPduSize(intent);
                    intent.putExtra(MSG_REF_NUM, this.mMessageRef);
                    Rlog.d("SMSDispatcher", "message reference number : " + this.mMessageRef);
                    this.mSentIntent.send(context, -1, intent);
                } catch (PendingIntent.CanceledException unused) {
                    Rlog.e("SMSDispatcher", "Failed to send result");
                }
            }
        }

        public int getAppUid() {
            return this.mAppUid;
        }

        class AsyncPersistOrUpdateTask extends AsyncTask<Void, Void, Void> {
            private final Context mContext;
            private int mError;
            private int mErrorCode;
            private boolean mFail;
            private int mMessageType;

            public AsyncPersistOrUpdateTask(Context context, int i, int i2, int i3, boolean z) {
                this.mContext = context;
                this.mMessageType = i;
                this.mErrorCode = i2;
                this.mError = i3;
                this.mFail = z;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Void doInBackground(Void... voidArr) {
                SmsTracker.this.persistOrUpdateMessage(this.mContext, this.mMessageType, this.mErrorCode);
                return null;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Void r9) {
                int i;
                if (SmsTracker.this.mSentIntent != null) {
                    try {
                        Intent intent = new Intent();
                        Uri uri = SmsTracker.this.mMessageUri;
                        if (uri != null) {
                            intent.putExtra("uri", uri.toString());
                        }
                        if (this.mFail && (i = this.mErrorCode) != 0) {
                            intent.putExtra(TelephonyStatAdapter.ERROR_CODE_KEY, i);
                        }
                        if (SmsTracker.this.mUnsentPartCount != null) {
                            intent.putExtra("SendNextMsg", true);
                        }
                        long j = SmsTracker.this.mMessageId;
                        if (j != 0) {
                            intent.putExtra("MessageId", j);
                        }
                        intent.putExtra("format", SmsTracker.this.mFormat);
                        intent.putExtra("ims", SmsTracker.this.mUsesImsServiceForIms);
                        HashMap<String, Object> map = SmsTracker.this.mData;
                        int length = 0;
                        if (map != null) {
                            length = (SmsTracker.this.mData.get(SMSDispatcher.MAP_KEY_PDU) != null ? ((byte[]) SmsTracker.this.mData.get(SMSDispatcher.MAP_KEY_PDU)).length : 0) + (map.get(SMSDispatcher.MAP_KEY_SMSC) != null ? ((byte[]) SmsTracker.this.mData.get(SMSDispatcher.MAP_KEY_SMSC)).length : 0);
                        }
                        intent.putExtra(SmsTracker.PDU_SIZE, length);
                        if (!this.mFail) {
                            intent.putExtra(SmsTracker.MSG_REF_NUM, SmsTracker.this.mMessageRef);
                            Rlog.d("SMSDispatcher", "message reference number : " + SmsTracker.this.mMessageRef);
                            SmsTracker.this.mSentIntent.send(this.mContext, -1, intent);
                            return;
                        }
                        SmsTracker.this.mSentIntent.send(this.mContext, this.mError, intent);
                    } catch (PendingIntent.CanceledException unused) {
                        Rlog.e("SMSDispatcher", "Failed to send result");
                    }
                }
            }
        }
    }

    protected SmsTracker getSmsTracker(String str, int i, HashMap<String, Object> map, PendingIntent pendingIntent, PendingIntent pendingIntent2, String str2, AtomicInteger atomicInteger, AtomicBoolean atomicBoolean, Uri uri, SmsHeader smsHeader, boolean z, String str3, boolean z2, boolean z3, int i2, int i3, boolean z4, long j, int i4, boolean z5, long j2, int i5) {
        PackageInfo packageInfo;
        try {
            packageInfo = this.mContext.createContextAsUser(UserHandle.of(i), 0).getPackageManager().getPackageInfo(str, 64);
        } catch (PackageManager.NameNotFoundException unused) {
            packageInfo = null;
        }
        return new SmsTracker(map, pendingIntent, pendingIntent2, MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).getCallerAppInfo(this, map, packageInfo), PhoneNumberUtils.extractNetworkPortion((String) map.get(MAP_KEY_DEST_ADDR)), str2, atomicInteger, atomicBoolean, uri, smsHeader, z, str3, getSubId(), z2, z3, i, i2, i3, z4, j, this.mPhone.getCarrierId(), i4, z5, j2, i5);
    }

    protected SmsTracker getSmsTracker(String str, int i, HashMap<String, Object> map, PendingIntent pendingIntent, PendingIntent pendingIntent2, String str2, Uri uri, boolean z, String str3, boolean z2, boolean z3, boolean z4, long j, int i2, long j2, int i3) {
        return getSmsTracker(str, i, map, pendingIntent, pendingIntent2, str2, null, null, uri, null, z, str3, z2, z3, -1, -1, z4, j, i2, false, j2, i3);
    }

    protected SmsTracker getSmsTracker(String str, int i, HashMap<String, Object> map, PendingIntent pendingIntent, PendingIntent pendingIntent2, String str2, Uri uri, boolean z, String str3, boolean z2, boolean z3, int i2, int i3, boolean z4, long j, int i4, boolean z5, long j2, int i5) {
        return getSmsTracker(str, i, map, pendingIntent, pendingIntent2, str2, null, null, uri, null, z, str3, z2, z3, i2, i3, z4, j, i4, z5, j2, i5);
    }

    protected HashMap<String, Object> getSmsTrackerMap(String str, String str2, String str3, SmsMessageBase.SubmitPduBase submitPduBase) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(MAP_KEY_DEST_ADDR, str);
        map.put(MAP_KEY_SC_ADDR, str2);
        map.put(MAP_KEY_TEXT, str3);
        map.put(MAP_KEY_SMSC, submitPduBase.encodedScAddress);
        map.put(MAP_KEY_PDU, submitPduBase.encodedMessage);
        return map;
    }

    protected HashMap<String, Object> getSmsTrackerMap(String str, String str2, int i, byte[] bArr, SmsMessageBase.SubmitPduBase submitPduBase) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(MAP_KEY_DEST_ADDR, str);
        map.put(MAP_KEY_SC_ADDR, str2);
        map.put(MAP_KEY_DEST_PORT, Integer.valueOf(i));
        map.put("data", bArr);
        map.put(MAP_KEY_SMSC, submitPduBase.encodedScAddress);
        map.put(MAP_KEY_PDU, submitPduBase.encodedMessage);
        return map;
    }

    private final class ConfirmDialogListener implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener, CompoundButton.OnCheckedChangeListener {
        private int mConfirmationType;
        private Button mNegativeButton;
        private Button mPositiveButton;
        private boolean mRememberChoice;
        private final TextView mRememberUndoInstruction;
        private final SmsTracker[] mTrackers;

        ConfirmDialogListener(SmsTracker[] smsTrackerArr, TextView textView, int i) {
            this.mTrackers = smsTrackerArr;
            this.mRememberUndoInstruction = textView;
            this.mConfirmationType = i;
        }

        void setPositiveButton(Button button) {
            this.mPositiveButton = button;
        }

        void setNegativeButton(Button button) {
            this.mNegativeButton = button;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            int i2 = 1;
            if (i == -1) {
                Rlog.d("SMSDispatcher", "CONFIRM sending SMS");
                ApplicationInfo applicationInfo = this.mTrackers[0].mAppInfo.applicationInfo;
                EventLog.writeEvent(EventLogTags.EXP_DET_SMS_SENT_BY_USER, applicationInfo != null ? applicationInfo.uid : -1);
                SMSDispatcher sMSDispatcher = SMSDispatcher.this;
                sMSDispatcher.sendMessage(sMSDispatcher.obtainMessage(5, this.mTrackers));
                if (this.mRememberChoice) {
                    i2 = 3;
                }
            } else if (i == -2) {
                Rlog.d("SMSDispatcher", "DENY sending SMS");
                ApplicationInfo applicationInfo2 = this.mTrackers[0].mAppInfo.applicationInfo;
                EventLog.writeEvent(EventLogTags.EXP_DET_SMS_DENIED_BY_USER, applicationInfo2 != null ? applicationInfo2.uid : -1);
                Message messageObtainMessage = SMSDispatcher.this.obtainMessage(6, this.mTrackers);
                messageObtainMessage.arg1 = this.mConfirmationType;
                if (this.mRememberChoice) {
                    messageObtainMessage.arg2 = 1;
                    i2 = 2;
                }
                SMSDispatcher.this.sendMessage(messageObtainMessage);
            }
            SMSDispatcher.this.mSmsDispatchersController.setPremiumSmsPermission(this.mTrackers[0].getAppPackageName(), i2);
        }

        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialogInterface) {
            Rlog.d("SMSDispatcher", "dialog dismissed: don't send SMS");
            Message messageObtainMessage = SMSDispatcher.this.obtainMessage(6, this.mTrackers);
            messageObtainMessage.arg1 = this.mConfirmationType;
            SMSDispatcher.this.sendMessage(messageObtainMessage);
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            Rlog.d("SMSDispatcher", "remember this choice: " + z);
            this.mRememberChoice = z;
            Button button = this.mPositiveButton;
            if (z) {
                button.setText(17042257);
                this.mNegativeButton.setText(17042260);
                TextView textView = this.mRememberUndoInstruction;
                if (textView != null) {
                    textView.setText(17042263);
                    this.mRememberUndoInstruction.setPadding(0, 0, 0, 32);
                    return;
                }
                return;
            }
            button.setText(17042256);
            this.mNegativeButton.setText(17042258);
            TextView textView2 = this.mRememberUndoInstruction;
            if (textView2 != null) {
                textView2.setText("");
                this.mRememberUndoInstruction.setPadding(0, 0, 0, 0);
            }
        }
    }

    public boolean isIms() {
        SmsDispatchersController smsDispatchersController = this.mSmsDispatchersController;
        if (smsDispatchersController != null) {
            return smsDispatchersController.isIms();
        }
        Rlog.e("SMSDispatcher", "mSmsDispatchersController is null");
        return false;
    }

    protected String getMultipartMessageText(ArrayList<String> arrayList) {
        StringBuilder sb = new StringBuilder();
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            String str = arrayList.get(i);
            i++;
            String str2 = str;
            if (str2 != null) {
                sb.append(str2);
            }
        }
        return sb.toString();
    }

    protected String getCarrierAppPackageName() {
        CarrierPrivilegesTracker carrierPrivilegesTracker = this.mPhone.getCarrierPrivilegesTracker();
        if (carrierPrivilegesTracker == null) {
            return null;
        }
        List<String> carrierPackageNamesForIntent = carrierPrivilegesTracker.getCarrierPackageNamesForIntent(new Intent("android.service.carrier.CarrierMessagingService"));
        if (carrierPackageNamesForIntent != null && carrierPackageNamesForIntent.size() == 1) {
            return carrierPackageNamesForIntent.get(0);
        }
        return CarrierSmsUtils.getImsRcsPackageForIntent(this.mContext, this.mPhone, new Intent("android.service.carrier.CarrierMessagingService"));
    }

    protected int getSubId() {
        return SubscriptionManager.getSubscriptionId(this.mPhone.getPhoneId());
    }

    protected boolean isCdmaMo() {
        return this.mSmsDispatchersController.isCdmaMo();
    }

    private boolean isAscii7bitSupportedForLongMessage() {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            PersistableBundle configForSubId = ((CarrierConfigManager) this.mContext.getSystemService("carrier_config")).getConfigForSubId(this.mPhone.getSubId());
            if (configForSubId != null) {
                return configForSubId.getBoolean("ascii_7_bit_support_for_long_message_bool");
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private void reportOutgoingSmsMetrics(SmsTracker smsTracker, int i, int i2) {
        TelephonyAnalytics.SmsMmsAnalytics smsMmsAnalytics;
        if (this.mPhone != null) {
            this.mPhone.getSmsStats().onOutgoingSms(smsTracker.mImsRetry > 0, "3gpp2".equals(getFormat()), false, (i != 101 || smsTracker.mRetryCount < getMaxSmsRetryCount()) ? i : 138, i2, smsTracker.mMessageId, smsTracker.isFromDefaultSmsApplication(this.mContext), smsTracker.getInterval(), this.mTelephonyManager.isEmergencyNumber(smsTracker.mDestAddress), smsTracker.isMtSmsPollingMessage(this.mContext), smsTracker.getPduLength(), smsTracker.getAppPackageName(), smsTracker.getAppUid());
            TelephonyAnalytics telephonyAnalytics = this.mPhone.getTelephonyAnalytics();
            if (telephonyAnalytics == null || (smsMmsAnalytics = telephonyAnalytics.getSmsMmsAnalytics()) == null) {
                return;
            }
            smsMmsAnalytics.onOutgoingSms(smsTracker.mImsRetry > 0, i);
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println("SMSDispatcher");
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("mLocalLog:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("mSmsOutgoingErrorCodes:");
        androidUtilIndentingPrintWriter.increaseIndent();
        this.mSmsOutgoingErrorCodes.dump(fileDescriptor, androidUtilIndentingPrintWriter, strArr);
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.decreaseIndent();
    }

    protected SmsMessageBase.SubmitPduBase onSendData(String str, String str2, int i, byte[] bArr, boolean z, int i2) {
        return getSubmitPdu(str, str2, i, bArr, z, i2);
    }

    protected SmsMessageBase.SubmitPduBase onSendText(String str, String str2, String str3, boolean z, Uri uri, String str4, boolean z2, int i, boolean z3, int i2, int i3) {
        return getSubmitPdu(str, str2, str3, z, null, i, i2, i3);
    }

    protected int onSendMultipartText(String str, String str2, ArrayList<String> arrayList, ArrayList<PendingIntent> arrayList2, ArrayList<PendingIntent> arrayList3, Uri uri, String str3, boolean z, int i, boolean z2, int i2, GsmAlphabet.TextEncodingDetails[] textEncodingDetailsArr) {
        int size = arrayList.size();
        int i3 = 0;
        for (int i4 = 0; i4 < size; i4++) {
            GsmAlphabet.TextEncodingDetails textEncodingDetailsCalculateLength = calculateLength(arrayList.get(i4), false);
            int i5 = textEncodingDetailsCalculateLength.codeUnitSize;
            if (i3 != i5 && (i3 == 0 || i3 == 1)) {
                i3 = i5;
            }
            textEncodingDetailsArr[i4] = textEncodingDetailsCalculateLength;
        }
        return i3;
    }
}
