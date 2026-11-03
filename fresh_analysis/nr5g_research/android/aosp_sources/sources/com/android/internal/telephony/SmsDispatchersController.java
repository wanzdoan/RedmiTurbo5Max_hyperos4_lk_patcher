package com.android.internal.telephony;

import android.R;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserManager;
import android.telephony.DomainSelectionService;
import android.telephony.ServiceState;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.emergency.EmergencyNumber;
import android.text.TextUtils;
import com.android.ims.ImsManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.cdma.CdmaInboundSmsHandler;
import com.android.internal.telephony.cdma.CdmaSMSDispatcher;
import com.android.internal.telephony.configupdate.ConfigProviderAdaptor;
import com.android.internal.telephony.domainselection.DomainSelectionConnection;
import com.android.internal.telephony.domainselection.DomainSelectionResolver;
import com.android.internal.telephony.domainselection.EmergencySmsDomainSelectionConnection;
import com.android.internal.telephony.domainselection.SmsDomainSelectionConnection;
import com.android.internal.telephony.emergency.EmergencyStateTracker;
import com.android.internal.telephony.gsm.GsmInboundSmsHandler;
import com.android.internal.telephony.satellite.DatagramDispatcher;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.LongUnaryOperator;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class SmsDispatchersController extends Handler {
    protected static final int EVENT_GET_SMSC_ADDRESS = 25;
    protected static final int EVENT_SMS_HANDLER_EXITING_WAITING_STATE = 17;
    private final BroadcastReceiver mBroadcastReceiver;
    protected SMSDispatcher mCdmaDispatcher;
    protected CdmaInboundSmsHandler mCdmaInboundSmsHandler;
    protected final CommandsInterface mCi;
    protected final Context mContext;
    private long mCurrentWaitElapsedDuration;
    private long mCurrentWaitStartTime;
    private HashMap mDeliveryPendingMapFor3GPP;
    private HashMap mDeliveryPendingMapFor3GPP2;
    private DomainSelectionResolverProxy mDomainSelectionResolverProxy;
    private DomainSelectionConnectionHolder mDscHolder;
    private DomainSelectionConnectionHolder mEmergencyDscHolder;
    private EmergencyStateTracker mEmergencyStateTracker;
    private final FeatureFlags mFeatureFlags;
    protected SMSDispatcher mGsmDispatcher;
    protected GsmInboundSmsHandler mGsmInboundSmsHandler;
    private boolean mIms;
    protected ImsSmsDispatcher mImsSmsDispatcher;
    private String mImsSmsFormat;
    private long mLastInServiceTime;
    private int mMessageRef;
    private SMSDispatcher mNtnSmsDispatcher;
    protected Phone mPhone;
    private String mSmscAddress;
    private final SmsUsageMonitor mUsageMonitor;

    public interface DomainSelectionResolverProxy {
        DomainSelectionConnection getDomainSelectionConnection(Phone phone, int i, boolean z);

        boolean isDomainSelectionSupported();
    }

    public interface SmsInjectionCallback {
        void onSmsInjectedResult(int i);
    }

    public static class PendingRequest {
        public static final int TYPE_DATA = 1;
        public static final int TYPE_MULTIPART_TEXT = 3;
        public static final int TYPE_RAW_SMS = 5;
        public static final int TYPE_RETRY_SMS = 4;
        public static final int TYPE_TEXT = 2;
        private static final AtomicLong sNextUniqueMessageId = new AtomicLong(0);
        public final String callingPackage;
        public final int callingUser;
        public final byte[] data;
        public final ArrayList<PendingIntent> deliveryIntents;
        public final String destAddr;
        public final int destPort;
        public final boolean expectMore;
        public final boolean isForVvm;
        public final boolean isMtSmsPolling;
        public final int mApplicationUid;
        public final long messageId;
        public final Uri messageUri;
        public final boolean persistMessage;
        public final int priority;
        public final String scAddr;
        public final ArrayList<PendingIntent> sentIntents;
        public final boolean skipShortCodeCheck;
        public final ArrayList<String> texts;
        public final SMSDispatcher.SmsTracker tracker;
        public final int type;
        public final long uniqueMessageId;
        public final int validityPeriod;

        public PendingRequest(int i, SMSDispatcher.SmsTracker smsTracker, String str, int i2, String str2, String str3, ArrayList<PendingIntent> arrayList, ArrayList<PendingIntent> arrayList2, boolean z, byte[] bArr, int i3, ArrayList<String> arrayList3, Uri uri, boolean z2, int i4, boolean z3, int i5, long j, boolean z4, boolean z5, int i6) {
            this.type = i;
            this.tracker = smsTracker;
            this.callingPackage = str;
            this.callingUser = i2;
            this.destAddr = str2;
            this.scAddr = str3;
            this.sentIntents = arrayList;
            this.deliveryIntents = arrayList2;
            this.isForVvm = z;
            this.data = bArr;
            this.destPort = i3;
            this.texts = arrayList3;
            this.messageUri = uri;
            this.persistMessage = z2;
            this.priority = i4;
            this.expectMore = z3;
            this.validityPeriod = i5;
            this.messageId = j;
            this.skipShortCodeCheck = z4;
            if (smsTracker != null) {
                this.uniqueMessageId = smsTracker.mUniqueMessageId;
            } else {
                this.uniqueMessageId = getNextUniqueMessageId();
            }
            this.isMtSmsPolling = z5;
            this.mApplicationUid = i6;
        }

        public static long getNextUniqueMessageId() {
            return sNextUniqueMessageId.getAndUpdate(new LongUnaryOperator() { // from class: com.android.internal.telephony.SmsDispatchersController$PendingRequest$$ExternalSyntheticLambda0
                @Override // java.util.function.LongUnaryOperator
                public final long applyAsLong(long j) {
                    return SmsDispatchersController.PendingRequest.$r8$lambda$jVExf0ZvCtc_nFdVC9TyW4_GBoE(j);
                }
            });
        }

        public static /* synthetic */ long $r8$lambda$jVExf0ZvCtc_nFdVC9TyW4_GBoE(long j) {
            return (j + 1) % Long.MAX_VALUE;
        }
    }

    protected class DomainSelectionConnectionHolder implements DomainSelectionConnection.DomainSelectionConnectionCallback {
        private DomainSelectionConnection mConnection;
        private final boolean mEmergency;
        private final List mPendingRequests = new ArrayList();

        DomainSelectionConnectionHolder(boolean z) {
            this.mEmergency = z;
        }

        public DomainSelectionConnection getConnection() {
            return this.mConnection;
        }

        public List<PendingRequest> getPendingRequests() {
            return this.mPendingRequests;
        }

        public boolean isDomainSelectionRequested() {
            return !this.mPendingRequests.isEmpty();
        }

        public boolean isEmergency() {
            return this.mEmergency;
        }

        public void clearAllRequests() {
            this.mPendingRequests.clear();
        }

        public void addRequest(PendingRequest pendingRequest) {
            this.mPendingRequests.add(pendingRequest);
        }

        public void setConnection(DomainSelectionConnection domainSelectionConnection) {
            this.mConnection = domainSelectionConnection;
        }

        @Override // com.android.internal.telephony.domainselection.DomainSelectionConnection.DomainSelectionConnectionCallback
        public void onSelectionTerminated(int i) {
            SmsDispatchersController.this.logd("onSelectionTerminated: emergency=" + this.mEmergency + ", cause=" + i);
            SmsDispatchersController smsDispatchersController = SmsDispatchersController.this;
            smsDispatchersController.sendMessage(smsDispatchersController.obtainMessage(20, this));
        }
    }

    public void putDeliveryPendingTracker(SMSDispatcher.SmsTracker smsTracker) {
        if (isCdmaFormat(smsTracker.mFormat)) {
            this.mDeliveryPendingMapFor3GPP2.put(Integer.valueOf(smsTracker.mMessageRef), smsTracker);
        } else {
            this.mDeliveryPendingMapFor3GPP.put(Integer.valueOf(smsTracker.mMessageRef), smsTracker);
        }
    }

    public SmsDispatchersController(Phone phone, SmsStorageMonitor smsStorageMonitor, SmsUsageMonitor smsUsageMonitor, FeatureFlags featureFlags) {
        this(phone, smsStorageMonitor, smsUsageMonitor, phone.getLooper(), featureFlags);
    }

    public SmsDispatchersController(Phone phone, SmsStorageMonitor smsStorageMonitor, SmsUsageMonitor smsUsageMonitor, Looper looper, FeatureFlags featureFlags) {
        super(looper);
        this.mLastInServiceTime = -1L;
        this.mCurrentWaitElapsedDuration = 0L;
        this.mCurrentWaitStartTime = -1L;
        this.mCdmaDispatcher = null;
        this.mCdmaInboundSmsHandler = null;
        this.mMessageRef = -1;
        this.mIms = false;
        this.mImsSmsFormat = "unknown";
        this.mDeliveryPendingMapFor3GPP = new HashMap();
        this.mDeliveryPendingMapFor3GPP2 = new HashMap();
        this.mDomainSelectionResolverProxy = new DomainSelectionResolverProxy(this) { // from class: com.android.internal.telephony.SmsDispatchersController.1
            @Override // com.android.internal.telephony.SmsDispatchersController.DomainSelectionResolverProxy
            public DomainSelectionConnection getDomainSelectionConnection(Phone phone2, int i, boolean z) {
                try {
                    return DomainSelectionResolver.getInstance().getDomainSelectionConnection(phone2, i, z);
                } catch (IllegalStateException unused) {
                    return null;
                }
            }

            @Override // com.android.internal.telephony.SmsDispatchersController.DomainSelectionResolverProxy
            public boolean isDomainSelectionSupported() {
                return DomainSelectionResolver.getInstance().isDomainSelectionSupported();
            }
        };
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.SmsDispatchersController.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                Rlog.d("SmsDispatchersController", "Received broadcast " + intent.getAction());
                if ("android.intent.action.USER_UNLOCKED".equals(intent.getAction())) {
                    SmsDispatchersController smsDispatchersController = SmsDispatchersController.this;
                    smsDispatchersController.sendMessage(smsDispatchersController.obtainMessage(16));
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        Rlog.d("SmsDispatchersController", "SmsDispatchersController created");
        Context context = phone.getContext();
        this.mContext = context;
        this.mUsageMonitor = smsUsageMonitor;
        CommandsInterface commandsInterface = phone.mCi;
        this.mCi = commandsInterface;
        this.mFeatureFlags = featureFlags;
        this.mPhone = phone;
        TelephonyComponentFactory telephonyComponentFactoryInject = TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName());
        this.mImsSmsDispatcher = telephonyComponentFactoryInject.makeImsSmsDispatcher(phone, this, featureFlags);
        this.mGsmInboundSmsHandler = TelephonyComponentFactory.getInstance().inject(GsmInboundSmsHandler.class.getName()).makeGsmInboundSmsHandler(phone.getContext(), smsStorageMonitor, phone, looper, featureFlags);
        this.mCdmaDispatcher = telephonyComponentFactoryInject.makeCdmaSMSDispatcher(phone, this, featureFlags);
        this.mCdmaInboundSmsHandler = CdmaInboundSmsHandler.makeInboundSmsHandler(phone.getContext(), smsStorageMonitor, phone, (CdmaSMSDispatcher) this.mCdmaDispatcher, looper, featureFlags);
        this.mGsmDispatcher = telephonyComponentFactoryInject.makeGsmSMSDispatcher(phone, this, this.mGsmInboundSmsHandler, featureFlags);
        telephonyComponentFactoryInject.makeSmsBroadcastUndelivered(phone.getContext(), this.mGsmInboundSmsHandler, this.mCdmaInboundSmsHandler, featureFlags);
        this.mNtnSmsDispatcher = MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).getNtnSmsDispatcher(this.mPhone, this);
        InboundSmsHandler.registerNewMessageNotificationActionHandler(phone.getContext());
        commandsInterface.registerForOn(this, 11, null);
        commandsInterface.registerForImsNetworkStateChanged(this, 12, null);
        if (((UserManager) context.getSystemService("user")).isUserUnlocked()) {
            this.mPhone.registerForServiceStateChanged(this, 14, null);
            resetPartialSegmentWaitTimer();
        } else {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_UNLOCKED");
            context.registerReceiver(broadcastReceiver, intentFilter);
        }
        MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).init(phone, this, new Object[0]);
    }

    public void dispose() {
        this.mCi.unregisterForOn(this);
        this.mCi.unregisterForImsNetworkStateChanged(this);
        this.mPhone.unregisterForServiceStateChanged(this);
        this.mGsmDispatcher.dispose();
        SMSDispatcher sMSDispatcher = this.mCdmaDispatcher;
        if (sMSDispatcher != null) {
            sMSDispatcher.dispose();
        }
        this.mGsmInboundSmsHandler.dispose();
        CdmaInboundSmsHandler cdmaInboundSmsHandler = this.mCdmaInboundSmsHandler;
        if (cdmaInboundSmsHandler != null) {
            cdmaInboundSmsHandler.dispose();
        }
        finishDomainSelection(this.mDscHolder);
        finishDomainSelection(this.mEmergencyDscHolder);
        MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).dispose(this.mPhone);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 11:
            case 12:
                if (MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).carrierDisableSendSmsOverIms(this.mPhone)) {
                    this.mIms = false;
                    this.mImsSmsFormat = "unknown";
                    Rlog.d("SmsDispatchersController", "Carrier disabled send sms over ims");
                    return;
                }
                this.mCi.getImsRegistrationState(obtainMessage(13));
                return;
            case 13:
                AsyncResult asyncResult = (AsyncResult) message.obj;
                if (asyncResult.exception == null) {
                    updateImsInfo(asyncResult);
                    return;
                }
                Rlog.e("SmsDispatchersController", "IMS State query failed with exp " + asyncResult.exception);
                return;
            case 14:
            case 17:
                reevaluateTimerStatus();
                return;
            case 15:
                handlePartialSegmentTimerExpiry(((Long) message.obj).longValue());
                return;
            case 16:
                this.mPhone.registerForServiceStateChanged(this, 14, null);
                resetPartialSegmentWaitTimer();
                return;
            case 18:
                SomeArgs someArgs = (SomeArgs) message.obj;
                try {
                    handleSendSmsUsingDomainSelection((DomainSelectionConnectionHolder) someArgs.arg1, (PendingRequest) someArgs.arg2, (String) someArgs.arg3);
                    return;
                } finally {
                    someArgs.recycle();
                }
            case 19:
                SomeArgs someArgs2 = (SomeArgs) message.obj;
                try {
                    handleSmsSentCompletedUsingDomainSelection((String) someArgs2.arg1, ((Long) someArgs2.arg2).longValue(), ((Boolean) someArgs2.arg3).booleanValue(), ((Boolean) someArgs2.arg4).booleanValue(), ((Boolean) someArgs2.arg5).booleanValue());
                    return;
                } finally {
                    someArgs2.recycle();
                }
            case 20:
                handleDomainSelectionTerminatedAbnormally((DomainSelectionConnectionHolder) message.obj);
                return;
            case 21:
                handleSmsReceivedViaIms((String) message.obj);
                return;
            case 22:
                SomeArgs someArgs3 = (SomeArgs) message.obj;
                try {
                    requestDomainSelection((DomainSelectionConnectionHolder) someArgs3.arg1, (PendingRequest) someArgs3.arg2, (String) someArgs3.arg3);
                    return;
                } finally {
                    someArgs3.recycle();
                }
            case 23:
                PendingRequest pendingRequest = (PendingRequest) message.obj;
                int i = pendingRequest.type;
                if (i == 2) {
                    sendTextInternal(pendingRequest);
                    return;
                }
                if (i == 3) {
                    sendMultipartTextInternal(pendingRequest);
                    return;
                }
                logd("CMD_SEND_TEXT: type=" + pendingRequest.type + " messageId=" + pendingRequest.messageId);
                return;
            case 24:
                PendingRequest pendingRequest2 = (PendingRequest) message.obj;
                logd("EVENT_SEND_TEXT_OVER_NTN_ERROR: type=" + pendingRequest2.type + " messageId=" + pendingRequest2.messageId);
                triggerSentIntentForFailure(pendingRequest2.sentIntents);
                return;
            case 25:
                AsyncResult asyncResult2 = (AsyncResult) message.obj;
                if (asyncResult2.exception == null) {
                    this.mSmscAddress = (String) asyncResult2.result;
                    return;
                }
                Rlog.e("SmsDispatchersController", "get smsc exp " + asyncResult2.exception);
                return;
            default:
                if (isCdmaMo()) {
                    SMSDispatcher sMSDispatcher = this.mCdmaDispatcher;
                    if (sMSDispatcher != null) {
                        sMSDispatcher.handleMessage(message);
                        return;
                    }
                    return;
                }
                this.mGsmDispatcher.handleMessage(message);
                return;
        }
    }

    protected String getSmscAddressFromUSIMWithPhoneIdentity(String str) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            IccSmsInterfaceManager iccSmsInterfaceManager = this.mPhone.getIccSmsInterfaceManager();
            if (iccSmsInterfaceManager != null) {
                return iccSmsInterfaceManager.getSmscAddressFromIccEf(str);
            }
            Rlog.d("SmsDispatchersController", "getSmscAddressFromIccEf iccSmsIntMgr is null");
            return null;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private void reevaluateTimerStatus() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        removeMessages(15);
        long j = this.mLastInServiceTime;
        if (j != -1) {
            this.mCurrentWaitElapsedDuration += jCurrentTimeMillis - j;
        }
        if (this.mCurrentWaitElapsedDuration > 86400000) {
            handlePartialSegmentTimerExpiry(this.mCurrentWaitStartTime);
        } else if (isInService()) {
            handleInService(jCurrentTimeMillis);
        } else {
            handleOutOfService(jCurrentTimeMillis);
        }
    }

    private void handleInService(long j) {
        if (this.mCurrentWaitStartTime == -1) {
            this.mCurrentWaitStartTime = j;
        }
        sendMessageDelayed(obtainMessage(15, Long.valueOf(this.mCurrentWaitStartTime)), 86400000 - this.mCurrentWaitElapsedDuration);
        this.mLastInServiceTime = j;
    }

    private void handleOutOfService(long j) {
        this.mLastInServiceTime = -1L;
    }

    private void handlePartialSegmentTimerExpiry(long j) {
        CdmaInboundSmsHandler cdmaInboundSmsHandler;
        if (MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).ntnInboundSmsHandlerIsWaiting(this.mPhone)) {
            return;
        }
        if (this.mGsmInboundSmsHandler.getCurrentState().getName().equals("WaitingState") || ((cdmaInboundSmsHandler = this.mCdmaInboundSmsHandler) != null && cdmaInboundSmsHandler.getCurrentState().getName().equals("WaitingState"))) {
            logd("handlePartialSegmentTimerExpiry: ignoring timer expiry as InboundSmsHandler is in WaitingState");
        } else {
            SmsBroadcastUndelivered.scanRawTable(this.mContext, j);
            resetPartialSegmentWaitTimer();
        }
    }

    private void resetPartialSegmentWaitTimer() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        removeMessages(15);
        if (isInService()) {
            this.mCurrentWaitStartTime = jCurrentTimeMillis;
            this.mLastInServiceTime = jCurrentTimeMillis;
            sendMessageDelayed(obtainMessage(15, Long.valueOf(jCurrentTimeMillis)), 86400000L);
        } else {
            this.mCurrentWaitStartTime = -1L;
            this.mLastInServiceTime = -1L;
        }
        this.mCurrentWaitElapsedDuration = 0L;
    }

    private boolean isInService() {
        ServiceState serviceState = this.mPhone.getServiceState();
        return serviceState != null && serviceState.getState() == 0;
    }

    private void setImsSmsFormat(int i) {
        if (i == 1) {
            this.mImsSmsFormat = "3gpp";
        } else if (i == 2) {
            this.mImsSmsFormat = "3gpp2";
        } else {
            this.mImsSmsFormat = "unknown";
        }
    }

    private void updateImsInfo(AsyncResult asyncResult) {
        int[] iArr = (int[]) asyncResult.result;
        setImsSmsFormat(iArr[1]);
        this.mIms = iArr[0] == 1 && !"unknown".equals(this.mImsSmsFormat);
        Rlog.d("SmsDispatchersController", "IMS registration state: " + this.mIms + " format: " + this.mImsSmsFormat);
    }

    public void injectSmsPdu(byte[] bArr, String str, boolean z, SmsInjectionCallback smsInjectionCallback) {
        injectSmsPdu(SmsMessage.createFromPdu(bArr, str), str, smsInjectionCallback, false, z, 0);
    }

    public void setImsSmsDispatcher(ImsSmsDispatcher imsSmsDispatcher) {
        this.mImsSmsDispatcher = imsSmsDispatcher;
    }

    public void injectSmsPdu(SmsMessage smsMessage, String str, SmsInjectionCallback smsInjectionCallback, boolean z, boolean z2, int i) {
        Rlog.d("SmsDispatchersController", "SmsDispatchersController:injectSmsPdu");
        try {
            if (smsMessage == null) {
                Rlog.e("SmsDispatchersController", "injectSmsPdu: createFromPdu returned null");
                smsInjectionCallback.onSmsInjectedResult(2);
                return;
            }
            if (!z && smsMessage.getMessageClass() != SmsMessage.MessageClass.CLASS_1) {
                Rlog.e("SmsDispatchersController", "injectSmsPdu: not class 1");
                smsInjectionCallback.onSmsInjectedResult(2);
                return;
            }
            AsyncResult asyncResult = new AsyncResult(smsInjectionCallback, smsMessage, (Throwable) null);
            if (str.equals("3gpp")) {
                Rlog.i("SmsDispatchersController", "SmsDispatchersController:injectSmsText Sending msg=" + smsMessage + ", format=" + str + "to mGsmInboundSmsHandler");
                this.mGsmInboundSmsHandler.sendMessage(7, z2 ? 1 : 0, i, asyncResult);
                return;
            }
            if (str.equals("3gpp2") && this.mCdmaInboundSmsHandler != null) {
                Rlog.i("SmsDispatchersController", "SmsDispatchersController:injectSmsText Sending msg=" + smsMessage + ", format=" + str + "to mCdmaInboundSmsHandler");
                this.mCdmaInboundSmsHandler.sendMessage(7, z2 ? 1 : 0, 0, asyncResult);
                return;
            }
            Rlog.e("SmsDispatchersController", "Invalid pdu format: ".concat(str));
            smsInjectionCallback.onSmsInjectedResult(2);
        } catch (Exception e) {
            Rlog.e("SmsDispatchersController", "injectSmsPdu failed: ", e);
            smsInjectionCallback.onSmsInjectedResult(2);
        }
    }

    public boolean setImsManager(ImsManager imsManager) {
        GsmInboundSmsHandler gsmInboundSmsHandler = this.mGsmInboundSmsHandler;
        if (gsmInboundSmsHandler == null) {
            return false;
        }
        gsmInboundSmsHandler.setImsManager(imsManager);
        return true;
    }

    public void sendRetrySms(SMSDispatcher.SmsTracker smsTracker) {
        SmsDispatchersController smsDispatchersController;
        if (!smsTracker.mUsesImsServiceForIms) {
            if (isSmsDomainSelectionEnabled()) {
                boolean zIsEmergencyNumber = isEmergencyNumber(smsTracker.mDestAddress);
                SomeArgs someArgsObtain = SomeArgs.obtain();
                someArgsObtain.arg1 = getDomainSelectionConnectionHolder(zIsEmergencyNumber);
                someArgsObtain.arg2 = new PendingRequest(4, smsTracker, null, -10000, null, null, null, null, false, null, 0, null, null, false, 0, false, 0, 0L, false, false, smsTracker.getAppUid());
                someArgsObtain.arg3 = "sendRetrySms";
                sendMessage(obtainMessage(22, someArgsObtain));
                return;
            }
            smsDispatchersController = this;
            boolean z = smsDispatchersController.mImsSmsDispatcher.isAvailable();
            smsDispatchersController.sendRetrySms(smsTracker, z);
        }
        smsDispatchersController = this;
        smsDispatchersController.sendRetrySms(smsTracker, z);
    }

    public void sendRetrySms(SMSDispatcher.SmsTracker smsTracker, boolean z) {
        String format;
        boolean z2;
        SmsDispatchersController smsDispatchersController;
        com.android.internal.telephony.cdma.SmsMessage.SubmitPdu submitPdu;
        SMSDispatcher sMSDispatcher;
        String str = smsTracker.mFormat;
        if (z) {
            format = this.mImsSmsDispatcher.getFormat();
        } else if (2 == this.mPhone.getPhoneType()) {
            format = this.mCdmaDispatcher.getFormat();
        } else {
            format = this.mGsmDispatcher.getFormat();
        }
        Rlog.d("SmsDispatchersController", "old format(" + str + ") ==> new format (" + format + ")");
        boolean zContainsSmscOptimization = MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).containsSmscOptimization(this.mPhone, smsTracker);
        if (!str.equals(format) || zContainsSmscOptimization) {
            HashMap<String, Object> data = smsTracker.getData();
            if (!data.containsKey("scAddr") || !data.containsKey("destAddr") || (!data.containsKey("text") && (!data.containsKey(ConfigProviderAdaptor.DOMAIN_DATA) || !data.containsKey("destPort")))) {
                Rlog.e("SmsDispatchersController", "sendRetrySms failed to re-encode per missing fields!");
                smsTracker.onFailed(this.mContext, 30, -1);
                notifySmsSent(smsTracker, !z, true, false);
                MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).sendSmsDotBroadcast(this.mPhone, smsTracker, getSmscAddress(), -1, false);
                return;
            }
            String strReplaceSmscAddr = (String) data.get("scAddr");
            if (zContainsSmscOptimization) {
                z2 = true;
                strReplaceSmscAddr = MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).replaceSmscAddr(this.mPhone, this.mSmscAddress, strReplaceSmscAddr);
            } else {
                z2 = true;
            }
            String str2 = (String) data.get("destAddr");
            if (str2 == null) {
                Rlog.e("SmsDispatchersController", "sendRetrySms failed due to null destAddr");
                smsTracker.onFailed(this.mContext, 30, -1);
                notifySmsSent(smsTracker, !z, z2, false);
                MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).sendSmsDotBroadcast(this.mPhone, smsTracker, getSmscAddress(), -1, false);
                return;
            }
            smsDispatchersController = this;
            com.android.internal.telephony.cdma.SmsMessage.SubmitPdu submitPdu2 = null;
            if (data.containsKey("text")) {
                String str3 = (String) data.get("text");
                StringBuilder sb = new StringBuilder("sms failed was text with length: ");
                sb.append(str3 == null ? null : Integer.valueOf(str3.length()));
                Rlog.d("SmsDispatchersController", sb.toString());
                if (smsDispatchersController.isCdmaFormat(format)) {
                    submitPdu = com.android.internal.telephony.cdma.SmsMessage.getSubmitPdu(strReplaceSmscAddr, str2, str3, smsTracker.mDeliveryIntent != null, (SmsHeader) null);
                } else {
                    PendingIntent pendingIntent = smsTracker.mDeliveryIntent;
                    if (zContainsSmscOptimization) {
                        boolean z3 = pendingIntent != null;
                        SmsHeader smsHeader = smsTracker.mSmsHeader;
                        submitPdu = com.android.internal.telephony.gsm.SmsMessage.getSubmitPdu(strReplaceSmscAddr, str2, str3, z3, smsHeader != null ? SmsHeader.toByteArray(smsHeader) : null);
                    } else {
                        submitPdu = com.android.internal.telephony.gsm.SmsMessage.getSubmitPdu(strReplaceSmscAddr, str2, str3, pendingIntent != null, (byte[]) null, 0, 0, 0, -1, smsTracker.mMessageRef);
                    }
                }
                submitPdu2 = submitPdu;
            } else if (data.containsKey(ConfigProviderAdaptor.DOMAIN_DATA)) {
                byte[] bArr = (byte[]) data.get(ConfigProviderAdaptor.DOMAIN_DATA);
                Integer num = (Integer) data.get("destPort");
                StringBuilder sb2 = new StringBuilder("sms failed was data with length: ");
                sb2.append(bArr != null ? Integer.valueOf(bArr.length) : null);
                Rlog.d("SmsDispatchersController", sb2.toString());
                if (smsDispatchersController.isCdmaFormat(format)) {
                    submitPdu2 = com.android.internal.telephony.cdma.SmsMessage.getSubmitPdu(strReplaceSmscAddr, str2, num.intValue(), bArr, smsTracker.mDeliveryIntent != null);
                } else {
                    submitPdu2 = com.android.internal.telephony.gsm.SmsMessage.getSubmitPdu(strReplaceSmscAddr, str2, num.intValue(), bArr, smsTracker.mDeliveryIntent != null, smsTracker.mMessageRef);
                }
            }
            if (submitPdu2 == null) {
                Rlog.e("SmsDispatchersController", String.format("sendRetrySms failed to encode message.scAddr: %s, destPort: %s", strReplaceSmscAddr, data.get("destPort")));
                smsTracker.onFailed(smsDispatchersController.mContext, 30, -1);
                smsDispatchersController.notifySmsSent(smsTracker, !z, true, false);
                MiuiSmsControllerStub.get(smsDispatchersController.mPhone.getPhoneId()).sendSmsDotBroadcast(smsDispatchersController.mPhone, smsTracker, smsDispatchersController.getSmscAddress(), -1, false);
                Rlog.i("SmsDispatchersController", "pdu encode failed, stop to retry.");
                return;
            }
            data.put("smsc", ((SmsMessageBase.SubmitPduBase) submitPdu2).encodedScAddress);
            data.put("pdu", ((SmsMessageBase.SubmitPduBase) submitPdu2).encodedMessage);
            smsTracker.mFormat = format;
        } else {
            smsDispatchersController = this;
        }
        if (!MiuiSmsControllerStub.get(smsDispatchersController.mPhone.getPhoneId()).isNeedSendCsRetry(smsDispatchersController.mPhone, smsTracker, 101, z)) {
            smsTracker.onFailed(smsDispatchersController.mContext, 30, -1);
            return;
        }
        if (z) {
            sMSDispatcher = smsDispatchersController.mImsSmsDispatcher;
        } else {
            sMSDispatcher = smsDispatchersController.isCdmaFormat(format) ? smsDispatchersController.mCdmaDispatcher : smsDispatchersController.mGsmDispatcher;
        }
        sMSDispatcher.sendSms(smsTracker);
    }

    public void reportSmsMemoryStatus(Message message) {
        Rlog.d("SmsDispatchersController", "reportSmsMemoryStatus: ");
        try {
            this.mImsSmsDispatcher.onMemoryAvailable();
            AsyncResult.forMessage(message, (Object) null, (Throwable) null);
            message.sendToTarget();
        } catch (Exception e) {
            Rlog.e("SmsDispatchersController", "reportSmsMemoryStatus Failed ", e);
            AsyncResult.forMessage(message, (Object) null, e);
            message.sendToTarget();
        }
    }

    public boolean isIms() {
        if (this.mImsSmsDispatcher.isAvailable()) {
            return true;
        }
        return this.mIms;
    }

    public String getImsSmsFormat() {
        return this.mImsSmsDispatcher.isAvailable() ? this.mImsSmsDispatcher.getFormat() : this.mImsSmsFormat;
    }

    public String getSmscAddress() {
        return this.mSmscAddress;
    }

    protected boolean isCdmaMo() {
        if (isIms()) {
            return isCdmaFormat(getImsSmsFormat());
        }
        return 2 == this.mPhone.getPhoneType();
    }

    public boolean isCdmaFormat(String str) {
        return this.mCdmaDispatcher.getFormat().equals(str);
    }

    public void setDomainSelectionResolverProxy(DomainSelectionResolverProxy domainSelectionResolverProxy) {
        this.mDomainSelectionResolverProxy = domainSelectionResolverProxy;
    }

    private boolean isSmsDomainSelectionEnabled() {
        return this.mDomainSelectionResolverProxy.isDomainSelectionSupported();
    }

    private boolean isCdmaMo(int i) {
        if (i != 2) {
            return 2 == this.mPhone.getPhoneType();
        }
        return isCdmaFormat(this.mImsSmsDispatcher.getFormat());
    }

    protected DomainSelectionConnectionHolder getDomainSelectionConnectionHolder(boolean z) {
        if (z) {
            if (this.mEmergencyDscHolder == null) {
                this.mEmergencyDscHolder = new DomainSelectionConnectionHolder(z);
            }
            return this.mEmergencyDscHolder;
        }
        if (this.mDscHolder == null) {
            this.mDscHolder = new DomainSelectionConnectionHolder(z);
        }
        return this.mDscHolder;
    }

    private void requestDomainSelection(final DomainSelectionConnectionHolder domainSelectionConnectionHolder) {
        DomainSelectionService.SelectionAttributes selectionAttributesBuild = new DomainSelectionService.SelectionAttributes.Builder(this.mPhone.getPhoneId(), this.mPhone.getSubId(), 2).setEmergency(domainSelectionConnectionHolder.isEmergency()).build();
        if (domainSelectionConnectionHolder.isEmergency()) {
            ((EmergencySmsDomainSelectionConnection) domainSelectionConnectionHolder.getConnection()).requestDomainSelection(selectionAttributesBuild, domainSelectionConnectionHolder).thenAcceptAsync(new Consumer() { // from class: com.android.internal.telephony.SmsDispatchersController$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$requestDomainSelection$0(domainSelectionConnectionHolder, (Integer) obj);
                }
            }, new Executor() { // from class: com.android.internal.telephony.SmsDispatchersController$$ExternalSyntheticLambda2
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    this.f$0.post(runnable);
                }
            });
        } else {
            ((SmsDomainSelectionConnection) domainSelectionConnectionHolder.getConnection()).requestDomainSelection(selectionAttributesBuild, domainSelectionConnectionHolder).thenAcceptAsync(new Consumer() { // from class: com.android.internal.telephony.SmsDispatchersController$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$requestDomainSelection$1(domainSelectionConnectionHolder, (Integer) obj);
                }
            }, new Executor() { // from class: com.android.internal.telephony.SmsDispatchersController$$ExternalSyntheticLambda2
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    this.f$0.post(runnable);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestDomainSelection$0(DomainSelectionConnectionHolder domainSelectionConnectionHolder, Integer num) {
        sendAllPendingRequests(domainSelectionConnectionHolder, num.intValue());
        finishDomainSelection(domainSelectionConnectionHolder);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestDomainSelection$1(DomainSelectionConnectionHolder domainSelectionConnectionHolder, Integer num) {
        sendAllPendingRequests(domainSelectionConnectionHolder, num.intValue());
        finishDomainSelection(domainSelectionConnectionHolder);
    }

    private void requestDomainSelection(DomainSelectionConnectionHolder domainSelectionConnectionHolder, PendingRequest pendingRequest, String str) {
        boolean zIsDomainSelectionRequested = domainSelectionConnectionHolder.isDomainSelectionRequested();
        domainSelectionConnectionHolder.addRequest(pendingRequest);
        if (domainSelectionConnectionHolder.getConnection() == null) {
            DomainSelectionConnection domainSelectionConnection = this.mDomainSelectionResolverProxy.getDomainSelectionConnection(this.mPhone, 2, domainSelectionConnectionHolder.isEmergency());
            if (domainSelectionConnection == null) {
                logd("requestDomainSelection: fallback for " + str);
                sendAllPendingRequests(domainSelectionConnectionHolder, 0);
                return;
            }
            domainSelectionConnectionHolder.setConnection(domainSelectionConnection);
        }
        if (zIsDomainSelectionRequested) {
            return;
        }
        requestDomainSelection(domainSelectionConnectionHolder);
    }

    private void handleSendSmsUsingDomainSelection(final DomainSelectionConnectionHolder domainSelectionConnectionHolder, final PendingRequest pendingRequest, final String str) {
        if (domainSelectionConnectionHolder.isEmergency()) {
            if (this.mEmergencyStateTracker == null) {
                this.mEmergencyStateTracker = EmergencyStateTracker.getInstance();
            }
            this.mEmergencyStateTracker.startEmergencySms(this.mPhone, String.valueOf(pendingRequest.messageId), isTestEmergencyNumber(pendingRequest.destAddr)).thenAccept(new Consumer() { // from class: com.android.internal.telephony.SmsDispatchersController$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$handleSendSmsUsingDomainSelection$2(str, pendingRequest, domainSelectionConnectionHolder, (Integer) obj);
                }
            });
            return;
        }
        requestDomainSelection(domainSelectionConnectionHolder, pendingRequest, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleSendSmsUsingDomainSelection$2(String str, PendingRequest pendingRequest, DomainSelectionConnectionHolder domainSelectionConnectionHolder, Integer num) {
        logi("startEmergencySms(" + str + "): messageId=" + pendingRequest.messageId + ", result=" + num);
        requestDomainSelection(domainSelectionConnectionHolder, pendingRequest, str);
    }

    private void sendSmsUsingDomainSelection(DomainSelectionConnectionHolder domainSelectionConnectionHolder, PendingRequest pendingRequest, String str) {
        SomeArgs someArgsObtain = SomeArgs.obtain();
        someArgsObtain.arg1 = domainSelectionConnectionHolder;
        someArgsObtain.arg2 = pendingRequest;
        someArgsObtain.arg3 = str;
        sendMessage(obtainMessage(18, someArgsObtain));
    }

    private void handleSmsSentCompletedUsingDomainSelection(String str, long j, boolean z, boolean z2, boolean z3) {
        if (this.mEmergencyStateTracker == null || !isEmergencyNumber(str)) {
            return;
        }
        this.mEmergencyStateTracker.endSms(String.valueOf(j), z, z2 ? 2 : 1, z3);
    }

    protected void notifySmsSent(SMSDispatcher.SmsTracker smsTracker, boolean z, boolean z2, boolean z3) {
        notifySmsSentToEmergencyStateTracker(smsTracker.mDestAddress, smsTracker.mMessageId, z, z2, z3);
        notifySmsSentToDatagramDispatcher(smsTracker.mUniqueMessageId, smsTracker.isSinglePartOrLastPart(), z3 && !smsTracker.isAnyPartFailed());
    }

    private void notifySmsSentToEmergencyStateTracker(String str, long j, boolean z, boolean z2, boolean z3) {
        if (isSmsDomainSelectionEnabled()) {
            SomeArgs someArgsObtain = SomeArgs.obtain();
            someArgsObtain.arg1 = str;
            someArgsObtain.arg2 = Long.valueOf(j);
            someArgsObtain.arg3 = Boolean.valueOf(z3);
            someArgsObtain.arg4 = Boolean.valueOf(z);
            someArgsObtain.arg5 = Boolean.valueOf(z2);
            sendMessage(obtainMessage(19, someArgsObtain));
        }
    }

    private void notifySmsSentToDatagramDispatcher(long j, boolean z, boolean z2) {
        if (SatelliteController.getInstance().shouldSendSmsToDatagramDispatcher(this.mPhone) && z) {
            DatagramDispatcher.getInstance().onSendSmsDone(this.mPhone.getSubId(), j, z2);
        }
    }

    private void handleSmsReceivedViaIms(String str) {
        if (this.mEmergencyStateTracker == null || str == null || !isEmergencyNumber(str)) {
            return;
        }
        this.mEmergencyStateTracker.onEmergencySmsReceived();
    }

    protected void notifySmsReceivedViaImsToEmergencyStateTracker(String str) {
        if (isSmsDomainSelectionEnabled()) {
            sendMessage(obtainMessage(21, str));
        }
    }

    private boolean isTestEmergencyNumber(final String str) {
        TelephonyManager telephonyManager;
        try {
            if (this.mPhone.hasCalling() && (telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)) != null) {
                return telephonyManager.getEmergencyNumberList().values().stream().flatMap(new NetworkScanRequestTracker$$ExternalSyntheticLambda1()).anyMatch(new Predicate() { // from class: com.android.internal.telephony.SmsDispatchersController$$ExternalSyntheticLambda4
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return SmsDispatchersController.$r8$lambda$rgEYpP7iDWqwKeOuykQAvVPrVwg(str, (EmergencyNumber) obj);
                    }
                });
            }
            return false;
        } catch (IllegalStateException | RuntimeException unused) {
            return false;
        }
    }

    public static /* synthetic */ boolean $r8$lambda$rgEYpP7iDWqwKeOuykQAvVPrVwg(String str, EmergencyNumber emergencyNumber) {
        return emergencyNumber.isFromSources(32) && str.equals(emergencyNumber.getNumber());
    }

    private void finishDomainSelection(DomainSelectionConnectionHolder domainSelectionConnectionHolder) {
        DomainSelectionConnection connection = domainSelectionConnectionHolder != null ? domainSelectionConnectionHolder.getConnection() : null;
        if (connection != null) {
            connection.finishSelection();
        }
        if (domainSelectionConnectionHolder != null) {
            List<PendingRequest> pendingRequests = domainSelectionConnectionHolder.getPendingRequests();
            logd("finishDomainSelection: pendingRequests=" + pendingRequests.size());
            Iterator<PendingRequest> it = pendingRequests.iterator();
            while (it.hasNext()) {
                triggerSentIntentForFailure(it.next().sentIntents);
            }
            domainSelectionConnectionHolder.clearAllRequests();
            domainSelectionConnectionHolder.setConnection(null);
        }
    }

    private void handleDomainSelectionTerminatedAbnormally(DomainSelectionConnectionHolder domainSelectionConnectionHolder) {
        logd("handleDomainSelectionTerminatedAbnormally: pendingRequests=" + domainSelectionConnectionHolder.getPendingRequests().size());
        sendAllPendingRequests(domainSelectionConnectionHolder, 0);
        domainSelectionConnectionHolder.setConnection(null);
    }

    private void sendAllPendingRequests(DomainSelectionConnectionHolder domainSelectionConnectionHolder, int i) {
        boolean z;
        List<PendingRequest> pendingRequests = domainSelectionConnectionHolder.getPendingRequests();
        if (i == 0) {
            logd("sendAllPendingRequests: fallback - imsAvailable=" + this.mImsSmsDispatcher.isAvailable());
            if (this.mImsSmsDispatcher.isAvailable()) {
                z = true;
                i = 2;
            } else {
                i = 1;
                z = true;
            }
        } else {
            z = false;
        }
        for (PendingRequest pendingRequest : pendingRequests) {
            int i2 = pendingRequest.type;
            if (i2 == 1) {
                sendData(i, pendingRequest);
            } else if (i2 == 2) {
                if (z && i != 2 && this.mImsSmsDispatcher.isEmergencySmsSupport(pendingRequest.destAddr)) {
                    i = 2;
                }
                sendText(i, pendingRequest);
            } else if (i2 == 3) {
                sendMultipartText(i, pendingRequest);
            } else if (i2 == 4) {
                sendRetrySms(pendingRequest.tracker, i == 2);
            } else if (i2 == 5) {
                sendRawPdu(i, pendingRequest);
            }
        }
        domainSelectionConnectionHolder.clearAllRequests();
    }

    private void sendData(int i, PendingRequest pendingRequest) {
        if (i == 2) {
            this.mImsSmsDispatcher.sendData(pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.destAddr, pendingRequest.scAddr, pendingRequest.destPort, pendingRequest.data, pendingRequest.sentIntents.get(0), pendingRequest.deliveryIntents.get(0), pendingRequest.isForVvm, pendingRequest.uniqueMessageId, pendingRequest.mApplicationUid);
        } else if (isCdmaMo(i)) {
            this.mCdmaDispatcher.sendData(pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.destAddr, pendingRequest.scAddr, pendingRequest.destPort, pendingRequest.data, pendingRequest.sentIntents.get(0), pendingRequest.deliveryIntents.get(0), pendingRequest.isForVvm, pendingRequest.uniqueMessageId, pendingRequest.mApplicationUid);
        } else {
            this.mGsmDispatcher.sendData(pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.destAddr, pendingRequest.scAddr, pendingRequest.destPort, pendingRequest.data, pendingRequest.sentIntents.get(0), pendingRequest.deliveryIntents.get(0), pendingRequest.isForVvm, pendingRequest.uniqueMessageId, pendingRequest.mApplicationUid);
        }
    }

    private void sendText(int i, PendingRequest pendingRequest) {
        if (i == 2) {
            this.mImsSmsDispatcher.sendText(pendingRequest.destAddr, pendingRequest.scAddr, pendingRequest.texts.get(0), pendingRequest.sentIntents.get(0), pendingRequest.deliveryIntents.get(0), pendingRequest.messageUri, pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.persistMessage, pendingRequest.priority, false, pendingRequest.validityPeriod, pendingRequest.isForVvm, pendingRequest.messageId, pendingRequest.skipShortCodeCheck, pendingRequest.uniqueMessageId, pendingRequest.mApplicationUid);
        } else if (isCdmaMo(i)) {
            this.mCdmaDispatcher.sendText(pendingRequest.destAddr, pendingRequest.scAddr, pendingRequest.texts.get(0), pendingRequest.sentIntents.get(0), pendingRequest.deliveryIntents.get(0), pendingRequest.messageUri, pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.persistMessage, pendingRequest.priority, pendingRequest.expectMore, pendingRequest.validityPeriod, pendingRequest.isForVvm, pendingRequest.messageId, pendingRequest.skipShortCodeCheck, pendingRequest.uniqueMessageId, pendingRequest.mApplicationUid);
        } else {
            this.mGsmDispatcher.sendText(pendingRequest.destAddr, pendingRequest.scAddr, pendingRequest.texts.get(0), pendingRequest.sentIntents.get(0), pendingRequest.deliveryIntents.get(0), pendingRequest.messageUri, pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.persistMessage, pendingRequest.priority, pendingRequest.expectMore, pendingRequest.validityPeriod, pendingRequest.isForVvm, pendingRequest.messageId, pendingRequest.skipShortCodeCheck, pendingRequest.uniqueMessageId, pendingRequest.mApplicationUid);
        }
    }

    private void sendMultipartText(int i, PendingRequest pendingRequest) {
        if (i == 2) {
            this.mImsSmsDispatcher.sendMultipartText(pendingRequest.destAddr, pendingRequest.scAddr, pendingRequest.texts, pendingRequest.sentIntents, pendingRequest.deliveryIntents, pendingRequest.messageUri, pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.persistMessage, pendingRequest.priority, false, pendingRequest.validityPeriod, pendingRequest.messageId, pendingRequest.uniqueMessageId, pendingRequest.mApplicationUid);
        } else if (isCdmaMo(i)) {
            this.mCdmaDispatcher.sendMultipartText(pendingRequest.destAddr, pendingRequest.scAddr, pendingRequest.texts, pendingRequest.sentIntents, pendingRequest.deliveryIntents, pendingRequest.messageUri, pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.persistMessage, pendingRequest.priority, pendingRequest.expectMore, pendingRequest.validityPeriod, pendingRequest.messageId, pendingRequest.uniqueMessageId, pendingRequest.mApplicationUid);
        } else {
            this.mGsmDispatcher.sendMultipartText(pendingRequest.destAddr, pendingRequest.scAddr, pendingRequest.texts, pendingRequest.sentIntents, pendingRequest.deliveryIntents, pendingRequest.messageUri, pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.persistMessage, pendingRequest.priority, pendingRequest.expectMore, pendingRequest.validityPeriod, pendingRequest.messageId, pendingRequest.uniqueMessageId, pendingRequest.mApplicationUid);
        }
    }

    private void triggerSentIntentForFailure(PendingIntent pendingIntent) {
        if (pendingIntent == null) {
            logd("sentIntent is null");
            return;
        }
        try {
            pendingIntent.send(1);
        } catch (PendingIntent.CanceledException unused) {
            logd("Intent has been canceled!");
        }
    }

    private void triggerSentIntentForFailure(List list) {
        if (list == null) {
            logd("sentIntents is null");
            return;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            triggerSentIntentForFailure((PendingIntent) it.next());
        }
    }

    private static ArrayList asArrayList(Object obj) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(obj);
        return arrayList;
    }

    protected void sendData(String str, int i, String str2, String str3, int i2, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, int i3) {
        String smscAddressFromUSIMWithPhoneIdentity = TextUtils.isEmpty(str3) ? getSmscAddressFromUSIMWithPhoneIdentity(str) : str3;
        if (isSmsDomainSelectionEnabled()) {
            sendSmsUsingDomainSelection(getDomainSelectionConnectionHolder(false), new PendingRequest(1, null, str, i, str2, smscAddressFromUSIMWithPhoneIdentity, asArrayList(pendingIntent), asArrayList(pendingIntent2), z, bArr, i2, null, null, false, 0, false, 0, 0L, false, false, i3), "sendData");
            return;
        }
        if (TelephonyCommonUtilsStub.get().isSatelliteEnable(this.mContext)) {
            this.mNtnSmsDispatcher.sendData(str, i, str2, this.mSmscAddress, i2, bArr, pendingIntent, pendingIntent2, z, PendingRequest.getNextUniqueMessageId(), i3);
            return;
        }
        if (this.mImsSmsDispatcher.isAvailable()) {
            this.mImsSmsDispatcher.sendData(str, i, str2, smscAddressFromUSIMWithPhoneIdentity, i2, bArr, pendingIntent, pendingIntent2, z, PendingRequest.getNextUniqueMessageId(), i3);
        } else if (isCdmaMo()) {
            this.mCdmaDispatcher.sendData(str, i, str2, smscAddressFromUSIMWithPhoneIdentity, i2, bArr, pendingIntent, pendingIntent2, z, PendingRequest.getNextUniqueMessageId(), i3);
        } else {
            this.mGsmDispatcher.sendData(str, i, str2, smscAddressFromUSIMWithPhoneIdentity, i2, bArr, pendingIntent, pendingIntent2, z, PendingRequest.getNextUniqueMessageId(), i3);
        }
    }

    public void sendRawPdu(String str, int i, String str2, String str3, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2, int i2) {
        String smscAddressFromUSIMWithPhoneIdentity;
        if (str3 == null || TextUtils.isEmpty(str3)) {
            Rlog.d("SmsDispatchersController", "sendRawPdu: scAddr is null or empty");
            smscAddressFromUSIMWithPhoneIdentity = getSmscAddressFromUSIMWithPhoneIdentity(str);
        } else {
            smscAddressFromUSIMWithPhoneIdentity = str3;
        }
        if (isSmsDomainSelectionEnabled()) {
            sendSmsUsingDomainSelection(getDomainSelectionConnectionHolder(false), new PendingRequest(5, null, str, i, str2, smscAddressFromUSIMWithPhoneIdentity, asArrayList(pendingIntent), asArrayList(pendingIntent2), false, bArr, 0, null, null, false, 0, false, 0, 0L, false, false, i2), "sendRawPdu");
        } else if (this.mImsSmsDispatcher.isAvailable()) {
            this.mImsSmsDispatcher.sendRawPdu(str, i, str2, smscAddressFromUSIMWithPhoneIdentity, bArr, pendingIntent, pendingIntent2, i2);
        } else if (isCdmaMo()) {
            this.mCdmaDispatcher.sendRawPdu(str, i, str2, smscAddressFromUSIMWithPhoneIdentity, bArr, pendingIntent, pendingIntent2, i2);
        } else {
            this.mGsmDispatcher.sendRawPdu(str, i, str2, smscAddressFromUSIMWithPhoneIdentity, bArr, pendingIntent, pendingIntent2, i2);
        }
    }

    private void sendRawPdu(int i, PendingRequest pendingRequest) {
        if (i == 2) {
            this.mImsSmsDispatcher.sendRawPdu(pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.destAddr, pendingRequest.scAddr, pendingRequest.data, pendingRequest.sentIntents.get(0), pendingRequest.deliveryIntents.get(0), pendingRequest.mApplicationUid);
        } else if (isCdmaMo(i)) {
            this.mCdmaDispatcher.sendRawPdu(pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.destAddr, pendingRequest.scAddr, pendingRequest.data, pendingRequest.sentIntents.get(0), pendingRequest.deliveryIntents.get(0), pendingRequest.mApplicationUid);
        } else {
            this.mGsmDispatcher.sendRawPdu(pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.destAddr, pendingRequest.scAddr, pendingRequest.data, pendingRequest.sentIntents.get(0), pendingRequest.deliveryIntents.get(0), pendingRequest.mApplicationUid);
        }
    }

    public void sendText(String str, String str2, String str3, PendingIntent pendingIntent, PendingIntent pendingIntent2, Uri uri, String str4, int i, boolean z, int i2, boolean z2, int i3, boolean z3, long j, int i4) {
        sendText(str, str2, str3, pendingIntent, pendingIntent2, uri, str4, i, z, i2, z2, i3, z3, j, false, i4);
    }

    public void sendText(String str, String str2, String str3, PendingIntent pendingIntent, PendingIntent pendingIntent2, Uri uri, String str4, int i, boolean z, int i2, boolean z2, int i3, boolean z3, long j, boolean z4, int i4) {
        this.mPhone.getSmscAddress(obtainMessage(25));
        PendingRequest pendingRequest = new PendingRequest(2, null, str4, i, str, TextUtils.isEmpty(str2) ? getSmscAddressFromUSIMWithPhoneIdentity(str4) : str2, asArrayList(pendingIntent), asArrayList(pendingIntent2), z3, null, 0, asArrayList(str3), uri, z, i2, z2, i3, j, z4, false, i4);
        if (SatelliteController.getInstance().shouldSendSmsToDatagramDispatcher(this.mPhone)) {
            DatagramDispatcher.getInstance().sendSms(pendingRequest);
        } else if (SatelliteController.getInstance().isInCarrierRoamingNbIotNtn()) {
            Rlog.d("SmsDispatchersController", "Block SMS in carrier roaming NB IOT NTN mode.");
            triggerSentIntentForFailure(pendingRequest.sentIntents);
        } else {
            sendTextInternal(pendingRequest);
        }
    }

    private void sendTextInternal(PendingRequest pendingRequest) {
        logd("sendTextInternal: messageId=" + pendingRequest.messageId + ", uniqueMessageId=" + pendingRequest.uniqueMessageId);
        if (isSmsDomainSelectionEnabled()) {
            sendSmsUsingDomainSelection(getDomainSelectionConnectionHolder(isEmergencyNumber(pendingRequest.destAddr)), pendingRequest, "sendText");
            return;
        }
        if (TelephonyCommonUtilsStub.get().isSatelliteEnable(this.mContext)) {
            this.mNtnSmsDispatcher.sendText(pendingRequest.destAddr, this.mSmscAddress, pendingRequest.texts.get(0), pendingRequest.sentIntents.get(0), pendingRequest.deliveryIntents.get(0), pendingRequest.messageUri, pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.persistMessage, pendingRequest.priority, pendingRequest.expectMore, pendingRequest.validityPeriod, pendingRequest.isForVvm, pendingRequest.messageId, pendingRequest.skipShortCodeCheck, pendingRequest.uniqueMessageId, pendingRequest.mApplicationUid);
            return;
        }
        if (this.mImsSmsDispatcher.isAvailable() || this.mImsSmsDispatcher.isEmergencySmsSupport(pendingRequest.destAddr)) {
            this.mImsSmsDispatcher.sendText(pendingRequest.destAddr, pendingRequest.scAddr, pendingRequest.texts.get(0), pendingRequest.sentIntents.get(0), pendingRequest.deliveryIntents.get(0), pendingRequest.messageUri, pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.persistMessage, pendingRequest.priority, false, pendingRequest.validityPeriod, pendingRequest.isForVvm, pendingRequest.messageId, pendingRequest.skipShortCodeCheck, pendingRequest.uniqueMessageId, pendingRequest.mApplicationUid);
        } else if (isCdmaMo()) {
            this.mCdmaDispatcher.sendText(pendingRequest.destAddr, pendingRequest.scAddr, pendingRequest.texts.get(0), pendingRequest.sentIntents.get(0), pendingRequest.deliveryIntents.get(0), pendingRequest.messageUri, pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.persistMessage, pendingRequest.priority, pendingRequest.expectMore, pendingRequest.validityPeriod, pendingRequest.isForVvm, pendingRequest.messageId, pendingRequest.skipShortCodeCheck, pendingRequest.uniqueMessageId, pendingRequest.mApplicationUid);
        } else {
            this.mGsmDispatcher.sendText(pendingRequest.destAddr, pendingRequest.scAddr, pendingRequest.texts.get(0), pendingRequest.sentIntents.get(0), pendingRequest.deliveryIntents.get(0), pendingRequest.messageUri, pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.persistMessage, pendingRequest.priority, pendingRequest.expectMore, pendingRequest.validityPeriod, pendingRequest.isForVvm, pendingRequest.messageId, pendingRequest.skipShortCodeCheck, pendingRequest.uniqueMessageId, pendingRequest.mApplicationUid);
        }
    }

    protected void sendMultipartText(String str, String str2, ArrayList<String> arrayList, ArrayList<PendingIntent> arrayList2, ArrayList<PendingIntent> arrayList3, Uri uri, String str3, int i, boolean z, int i2, boolean z2, int i3, long j, int i4) {
        this.mPhone.getSmscAddress(obtainMessage(25));
        PendingRequest pendingRequest = new PendingRequest(3, null, str3, i, str, TextUtils.isEmpty(str2) ? getSmscAddressFromUSIMWithPhoneIdentity(str3) : str2, arrayList2, arrayList3, false, null, 0, arrayList, uri, z, i2, z2, i3, j, false, false, i4);
        if (SatelliteController.getInstance().shouldSendSmsToDatagramDispatcher(this.mPhone)) {
            DatagramDispatcher.getInstance().sendSms(pendingRequest);
        } else if (SatelliteController.getInstance().isInCarrierRoamingNbIotNtn()) {
            Rlog.d("SmsDispatchersController", "Block SMS in carrier roaming NB IOT NTN mode.");
            triggerSentIntentForFailure(pendingRequest.sentIntents);
        } else {
            sendMultipartTextInternal(pendingRequest);
        }
    }

    private boolean isEmergencyNumber(String str) {
        TelephonyManager telephonyManager;
        if (this.mPhone.hasCalling() && (telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)) != null) {
            return telephonyManager.isEmergencyNumber(str);
        }
        return false;
    }

    private void sendMultipartTextInternal(PendingRequest pendingRequest) {
        logd("sendMultipartTextInternal: messageId=" + pendingRequest.messageId);
        if (isSmsDomainSelectionEnabled()) {
            sendSmsUsingDomainSelection(getDomainSelectionConnectionHolder(isEmergencyNumber(pendingRequest.destAddr)), pendingRequest, "sendMultipartText");
            return;
        }
        if (TelephonyCommonUtilsStub.get().isSatelliteEnable(this.mContext)) {
            this.mNtnSmsDispatcher.sendMultipartText(pendingRequest.destAddr, this.mSmscAddress, pendingRequest.texts, pendingRequest.sentIntents, pendingRequest.deliveryIntents, pendingRequest.messageUri, pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.persistMessage, pendingRequest.priority, pendingRequest.expectMore, pendingRequest.validityPeriod, pendingRequest.messageId, pendingRequest.uniqueMessageId, pendingRequest.mApplicationUid);
            return;
        }
        if (this.mImsSmsDispatcher.isAvailable()) {
            this.mImsSmsDispatcher.sendMultipartText(pendingRequest.destAddr, pendingRequest.scAddr, pendingRequest.texts, pendingRequest.sentIntents, pendingRequest.deliveryIntents, pendingRequest.messageUri, pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.persistMessage, pendingRequest.priority, false, pendingRequest.validityPeriod, pendingRequest.messageId, pendingRequest.uniqueMessageId, pendingRequest.mApplicationUid);
        } else if (isCdmaMo()) {
            this.mCdmaDispatcher.sendMultipartText(pendingRequest.destAddr, pendingRequest.scAddr, pendingRequest.texts, pendingRequest.sentIntents, pendingRequest.deliveryIntents, pendingRequest.messageUri, pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.persistMessage, pendingRequest.priority, pendingRequest.expectMore, pendingRequest.validityPeriod, pendingRequest.messageId, pendingRequest.uniqueMessageId, pendingRequest.mApplicationUid);
        } else {
            this.mGsmDispatcher.sendMultipartText(pendingRequest.destAddr, pendingRequest.scAddr, pendingRequest.texts, pendingRequest.sentIntents, pendingRequest.deliveryIntents, pendingRequest.messageUri, pendingRequest.callingPackage, pendingRequest.callingUser, pendingRequest.persistMessage, pendingRequest.priority, pendingRequest.expectMore, pendingRequest.validityPeriod, pendingRequest.messageId, pendingRequest.uniqueMessageId, pendingRequest.mApplicationUid);
        }
    }

    public int getPremiumSmsPermission(String str) {
        return this.mUsageMonitor.getPremiumSmsPermission(str);
    }

    public void setPremiumSmsPermission(String str, int i) {
        this.mUsageMonitor.setPremiumSmsPermission(str, i);
    }

    public SmsUsageMonitor getUsageMonitor() {
        return this.mUsageMonitor;
    }

    public void handleSmsStatusReport(String str, byte[] bArr) {
        boolean zTriggerDeliveryIntent = false;
        if (isCdmaFormat(str)) {
            com.android.internal.telephony.cdma.SmsMessage smsMessageCreateFromPdu = com.android.internal.telephony.cdma.SmsMessage.createFromPdu(bArr);
            if (smsMessageCreateFromPdu != null) {
                int i = smsMessageCreateFromPdu.mMessageRef;
                SMSDispatcher.SmsTracker smsTracker = (SMSDispatcher.SmsTracker) this.mDeliveryPendingMapFor3GPP2.get(Integer.valueOf(i));
                boolean z = smsTracker == null && (smsTracker = (SMSDispatcher.SmsTracker) this.mDeliveryPendingMapFor3GPP.get(Integer.valueOf(i))) != null;
                if (smsTracker != null) {
                    int status = (smsMessageCreateFromPdu.getStatus() >> 24) & 3;
                    if (status != 2) {
                        smsTracker.updateSentMessageStatus(this.mContext, status == 0 ? 0 : 64);
                        if (z) {
                            this.mDeliveryPendingMapFor3GPP.remove(Integer.valueOf(i));
                        } else {
                            this.mDeliveryPendingMapFor3GPP2.remove(Integer.valueOf(i));
                        }
                    }
                    zTriggerDeliveryIntent = triggerDeliveryIntent(smsTracker, str, bArr);
                }
            }
        } else {
            com.android.internal.telephony.gsm.SmsMessage smsMessageCreateFromPdu2 = com.android.internal.telephony.gsm.SmsMessage.createFromPdu(bArr);
            if (smsMessageCreateFromPdu2 != null) {
                int i2 = smsMessageCreateFromPdu2.mMessageRef;
                SMSDispatcher.SmsTracker smsTracker2 = (SMSDispatcher.SmsTracker) this.mDeliveryPendingMapFor3GPP.get(Integer.valueOf(i2));
                if (smsTracker2 != null) {
                    int status2 = smsMessageCreateFromPdu2.getStatus();
                    if (status2 >= 64 || status2 < 32) {
                        smsTracker2.updateSentMessageStatus(this.mContext, status2);
                        this.mDeliveryPendingMapFor3GPP.remove(Integer.valueOf(i2));
                    }
                    zTriggerDeliveryIntent = triggerDeliveryIntent(smsTracker2, str, bArr);
                }
            }
        }
        if (zTriggerDeliveryIntent) {
            return;
        }
        Rlog.e("SmsDispatchersController", "handleSmsStatusReport: can not handle the status report!");
    }

    private boolean triggerDeliveryIntent(SMSDispatcher.SmsTracker smsTracker, String str, byte[] bArr) {
        PendingIntent pendingIntent = smsTracker.mDeliveryIntent;
        Intent intent = new Intent();
        intent.putExtra("pdu", bArr);
        intent.putExtra("format", str);
        try {
            pendingIntent.send(this.mContext, -1, intent);
            return true;
        } catch (PendingIntent.CanceledException unused) {
            return false;
        }
    }

    public InboundSmsHandler getInboundSmsHandler(boolean z) {
        if (z) {
            return this.mCdmaInboundSmsHandler;
        }
        return this.mGsmInboundSmsHandler;
    }

    public void sendCarrierRoamingNbIotNtnText(PendingRequest pendingRequest) {
        sendMessage(obtainMessage(23, pendingRequest));
    }

    public void onSendCarrierRoamingNbIotNtnTextError(PendingRequest pendingRequest, int i) {
        logd("onSendCarrierRoamingNbIotNtnTextError: messageId=" + pendingRequest.messageId + " errorCode=" + i);
        sendMessage(obtainMessage(24, pendingRequest));
    }

    public void sendMtSmsPollingMessage() {
        if (!SatelliteController.getInstance().shouldSendSmsToDatagramDispatcher(this.mPhone)) {
            logd("sendMtSmsPollingMessage: not in roaming nb iot ntn");
            return;
        }
        String lastKnownPhoneNumber = ((SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service")).getLastKnownPhoneNumber(this.mPhone.getSubId());
        if (TextUtils.isEmpty(lastKnownPhoneNumber)) {
            logd("sendMtSmsPollingMessage: destAddr is null or empty.");
            return;
        }
        String string = this.mContext.getResources().getString(R.string.db_wal_sync_mode);
        if (TextUtils.isEmpty(string)) {
            logd("sendMtSmsPollingMessage: mtSmsPollingText is null or empty.");
            return;
        }
        String packageName = this.mContext.getPackageName();
        PendingRequest pendingRequest = new PendingRequest(2, null, packageName, Binder.getCallingUserHandle().getIdentifier(), lastKnownPhoneNumber, getSmscAddressFromUSIMWithPhoneIdentity(packageName), asArrayList(null), asArrayList(null), false, null, 0, asArrayList(string), null, false, 0, false, 5, 0L, true, true, Binder.getCallingUid());
        if (SatelliteController.getInstance().shouldSendSmsToDatagramDispatcher(this.mPhone)) {
            DatagramDispatcher.getInstance().sendSms(pendingRequest);
        }
    }

    public int getMessageReference() {
        return this.mMessageRef;
    }

    public void setMessageReference(int i) {
        this.mMessageRef = i;
    }

    public int incrementMessageReference() {
        int i = (this.mMessageRef + 1) % CallFailCause.RADIO_UPLINK_FAILURE;
        this.mMessageRef = i;
        return i;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.mGsmInboundSmsHandler.dump(fileDescriptor, printWriter, strArr);
        CdmaInboundSmsHandler cdmaInboundSmsHandler = this.mCdmaInboundSmsHandler;
        if (cdmaInboundSmsHandler != null) {
            cdmaInboundSmsHandler.dump(fileDescriptor, printWriter, strArr);
        }
        this.mGsmDispatcher.dump(fileDescriptor, printWriter, strArr);
        SMSDispatcher sMSDispatcher = this.mCdmaDispatcher;
        if (sMSDispatcher != null) {
            sMSDispatcher.dump(fileDescriptor, printWriter, strArr);
        }
        this.mImsSmsDispatcher.dump(fileDescriptor, printWriter, strArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Rlog.d("SmsDispatchersController", str);
    }

    private void logi(String str) {
        Rlog.i("SmsDispatchersController [" + this.mPhone.getPhoneId() + "]", str);
    }
}
