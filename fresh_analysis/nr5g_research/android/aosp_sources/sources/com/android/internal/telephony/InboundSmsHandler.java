package com.android.internal.telephony;

import android.R;
import android.app.BroadcastOptions;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerWhitelistManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Pair;
import android.view.textclassifier.TextClassificationManager;
import android.view.textclassifier.TextClassifier;
import android.view.textclassifier.TextLinks;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.analytics.TelephonyAnalytics;
import com.android.internal.telephony.metrics.PersistAtomsStorage;
import com.android.internal.telephony.nano.PersistAtomsProto$OtpEvaluationEvent;
import com.android.internal.telephony.nano.PersistAtomsProto$OtpRedactionEvent;
import com.android.internal.telephony.ntnphone.SatelliteNetworkFactory;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.internal.telephony.satellite.metrics.CarrierRoamingSatelliteSessionStats;
import com.android.internal.telephony.util.NotificationChannelController;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/* JADX INFO: loaded from: classes.dex */
public abstract class InboundSmsHandler extends StateMachine {
    private static String ACTION_OPEN_SMS_APP = null;
    public static final int ADDRESS_COLUMN = 6;
    public static final int COUNT_COLUMN = 5;
    public static final int DATE_COLUMN = 3;
    protected static final boolean DBG = true;
    public static final int DELETED_FLAG_COLUMN = 10;
    public static final int DESTINATION_PORT_COLUMN = 2;
    public static final int DISPLAY_ADDRESS_COLUMN = 9;
    public static final int EVENT_BROADCAST_COMPLETE = 3;
    public static final int EVENT_BROADCAST_SMS = 2;
    public static final int EVENT_INJECT_SMS = 7;
    public static final int EVENT_NEW_SMS = 1;
    public static final int EVENT_RECEIVER_TIMEOUT = 9;
    protected static final int EVENT_RETURN_TO_IDLE = 4;
    public static final int EVENT_START_ACCEPTING_SMS = 6;
    public static final int EVENT_UPDATE_TRACKER = 8;
    public static final int ID_COLUMN = 7;
    private static final long MAXIMUM_BROADCAST_DELAY_TIME_MS;
    public static final int MESSAGE_BODY_COLUMN = 8;
    public static final int NOTIFICATION_ID_NEW_MESSAGE = 1;
    public static final String NOTIFICATION_TAG = "InboundSmsHandler";
    public static final int PDU_COLUMN = 0;
    protected static final String[] PDU_DELETED_FLAG_PROJECTION = {"pdu", "deleted"};
    private static final Map PDU_DELETED_FLAG_PROJECTION_INDEX_MAPPING = Map.of(0, 0, 10, 1);
    protected static final String[] PDU_SEQUENCE_PORT_PROJECTION = {"pdu", "sequence", "destination_port", "display_originating_addr", "date"};
    protected static final Map<Integer, Integer> PDU_SEQUENCE_PORT_PROJECTION_INDEX_MAPPING = Map.of(0, 0, 1, 1, 2, 2, 9, 3, 3, 4);
    public static final int REFERENCE_NUMBER_COLUMN = 4;
    public static final String SELECT_BY_ID = "_id=?";
    public static final int SEQUENCE_COLUMN = 1;
    public static final int SOURCE_INJECTED_FROM_IMS = 1;
    public static final int SOURCE_INJECTED_FROM_UNKNOWN = 2;
    public static final int SOURCE_NOT_INJECTED = 0;
    public static final int SUBID_COLUMN = 11;
    private static final TextClassifier.EntityConfig TC_REQUEST_CONFIG;
    protected static final boolean VDBG = false;
    public static final Uri sRawUri;
    public static final Uri sRawUriPermanentDelete;
    public static int sTimeoutDurationMillis;
    protected final int DELETE_PERMANENTLY;
    protected final int MARK_DELETED;
    private final PersistAtomsStorage mAtomsStorage;
    private final Executor mBackgroundExecutor;
    private LocalLog mCarrierServiceLocalLog;
    protected CellBroadcastServiceManager mCellBroadcastServiceManager;
    protected final Context mContext;
    protected DefaultState mDefaultState;
    protected DeliveringState mDeliveringState;
    protected final FeatureFlags mFeatureFlags;
    protected IdleState mIdleState;
    private LocalLog mLocalLog;
    private final Handler mMainThreadHandler;
    protected final PackageManager mPackageManager;
    protected Phone mPhone;
    PowerWhitelistManager mPowerWhitelistManager;
    protected final ContentResolver mResolver;
    private List mSmsFilters;
    private final boolean mSmsReceiveDisabled;
    protected StartupState mStartupState;
    public SmsStorageMonitor mStorageMonitor;
    private TextClassifier mTextClassifier;
    protected UserManager mUserManager;
    protected WaitingState mWaitingState;
    private final PowerManager.WakeLock mWakeLock;
    private int mWakeLockTimeout;
    protected final WapPushOverSms mWapPush;

    public interface SmsFilter {
        boolean filterSms(byte[][] bArr, int i, InboundSmsTracker inboundSmsTracker, SmsBroadcastReceiver smsBroadcastReceiver, boolean z, boolean z2, List<SmsFilter> list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSkipNotifyFlagSet(int i) {
        return (i & 2) > 0;
    }

    protected abstract void acknowledgeLastIncomingSms(boolean z, int i, Message message);

    protected abstract int dispatchMessageRadioSpecific(SmsMessageBase smsMessageBase, int i, int i2);

    protected abstract boolean is3gpp2();

    protected boolean onCheckIfOverrideStates() {
        return false;
    }

    protected boolean onCheckIfStopProcessMessagePart(byte[][] bArr, String str, InboundSmsTracker inboundSmsTracker) {
        return false;
    }

    protected String[] onModifyQueryWhereArgs(String[] strArr) {
        return strArr;
    }

    static {
        Uri uri = Telephony.Sms.CONTENT_URI;
        sRawUri = Uri.withAppendedPath(uri, "raw");
        sRawUriPermanentDelete = Uri.withAppendedPath(uri, "raw/permanentDelete");
        MAXIMUM_BROADCAST_DELAY_TIME_MS = 5000L;
        TC_REQUEST_CONFIG = new TextClassifier.EntityConfig.Builder().setIncludedTypes(getIncludedTextClassifierTypes()).includeTypesFromTextClassifier(false).build();
        ACTION_OPEN_SMS_APP = "com.android.internal.telephony.OPEN_DEFAULT_SMS_APP";
        sTimeoutDurationMillis = 600000;
    }

    public static List<String> getIncludedTextClassifierTypes() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("sms_retriever_otp");
        arrayList.add("sms_web_otp");
        arrayList.add("otp");
        return arrayList;
    }

    protected InboundSmsHandler(String str, Context context, SmsStorageMonitor smsStorageMonitor, Phone phone, Looper looper, FeatureFlags featureFlags) {
        super(str, looper);
        this.mDefaultState = new DefaultState();
        this.mStartupState = new StartupState();
        this.mIdleState = new IdleState();
        this.mDeliveringState = new DeliveringState();
        this.mWaitingState = new WaitingState();
        this.mLocalLog = new LocalLog(64);
        this.mCarrierServiceLocalLog = new LocalLog(8);
        this.DELETE_PERMANENTLY = 1;
        this.MARK_DELETED = 2;
        this.mBackgroundExecutor = Executors.newSingleThreadExecutor();
        this.mMainThreadHandler = new Handler(Looper.getMainLooper());
        this.mFeatureFlags = featureFlags;
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
        this.mStorageMonitor = smsStorageMonitor;
        this.mPhone = phone;
        this.mResolver = context.getContentResolver();
        this.mWapPush = TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName()).makeWapPushOverSms(context, featureFlags);
        this.mAtomsStorage = PhoneFactory.getMetricsCollector().getAtomsStorage();
        TelephonyManager telephonyManagerFrom = TelephonyManager.from(context);
        this.mSmsReceiveDisabled = !telephonyManagerFrom.getSmsReceiveCapableForPhone(this.mPhone.getPhoneId(), telephonyManagerFrom.isDeviceSmsCapable());
        PowerManager.WakeLock wakeLockNewWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, str);
        this.mWakeLock = wakeLockNewWakeLock;
        wakeLockNewWakeLock.acquire();
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mPowerWhitelistManager = (PowerWhitelistManager) context.getSystemService("power_whitelist");
        this.mCellBroadcastServiceManager = new CellBroadcastServiceManager(context, phone);
        this.mSmsFilters = createDefaultSmsFilters();
        if (!onCheckIfOverrideStates()) {
            addState(this.mDefaultState);
            addState(this.mStartupState, this.mDefaultState);
            addState(this.mIdleState, this.mDefaultState);
            addState(this.mDeliveringState, this.mDefaultState);
            addState(this.mWaitingState, this.mDeliveringState);
            setInitialState(this.mStartupState);
        }
        log("created InboundSmsHandler");
    }

    public TextClassifier getTextClassifier() {
        if (this.mTextClassifier == null) {
            this.mTextClassifier = ((TextClassificationManager) this.mContext.getSystemService(TextClassificationManager.class)).getTextClassifier(2);
        }
        return this.mTextClassifier;
    }

    public void dispose() {
        quit();
    }

    @Override // com.android.internal.telephony.StateMachine
    protected void onQuitting() {
        this.mWapPush.dispose();
        this.mCellBroadcastServiceManager.disable();
        while (this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }
    }

    public Phone getPhone() {
        return this.mPhone;
    }

    @Override // com.android.internal.telephony.StateMachine
    protected String getWhatToString(int i) {
        switch (i) {
            case 1:
                return "EVENT_NEW_SMS";
            case 2:
                return "EVENT_BROADCAST_SMS";
            case 3:
                return "EVENT_BROADCAST_COMPLETE";
            case 4:
                return "EVENT_RETURN_TO_IDLE";
            case 5:
                return "EVENT_RELEASE_WAKELOCK";
            case 6:
                return "EVENT_START_ACCEPTING_SMS";
            case 7:
                return "EVENT_INJECT_SMS";
            case 8:
                return "EVENT_UPDATE_TRACKER";
            case 9:
                return "EVENT_RECEIVER_TIMEOUT";
            default:
                return "UNKNOWN EVENT " + i;
        }
    }

    public class DefaultState extends State {
        public DefaultState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            int i = message.what;
            String str = "processMessage: unhandled message type " + InboundSmsHandler.this.getWhatToString(message.what) + " currState=" + InboundSmsHandler.this.getCurrentState().getName();
            boolean z = TelephonyUtils.IS_DEBUGGABLE;
            InboundSmsHandler inboundSmsHandler = InboundSmsHandler.this;
            if (z) {
                inboundSmsHandler.loge("---- Dumping InboundSmsHandler ----");
                InboundSmsHandler.this.loge("Total records=" + InboundSmsHandler.this.getLogRecCount());
                int iMax = Math.max(InboundSmsHandler.this.getLogRecSize() + (-20), 0);
                while (true) {
                    int logRecSize = InboundSmsHandler.this.getLogRecSize();
                    InboundSmsHandler inboundSmsHandler2 = InboundSmsHandler.this;
                    if (iMax < logRecSize) {
                        inboundSmsHandler2.loge("Rec[%d]: %s\n" + iMax + InboundSmsHandler.this.getLogRec(iMax).toString());
                        iMax++;
                    } else {
                        inboundSmsHandler2.loge("---- Dumped InboundSmsHandler ----");
                        Phone$$ExternalSyntheticBUOutline1.m(str);
                        return false;
                    }
                }
            } else {
                inboundSmsHandler.loge(str);
                return true;
            }
        }
    }

    public class StartupState extends State {
        public StartupState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            InboundSmsHandler.this.log("StartupState.enter: entering StartupState");
            InboundSmsHandler.this.setWakeLockTimeout(0);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            InboundSmsHandler.this.log("StartupState.processMessage: processing " + InboundSmsHandler.this.getWhatToString(message.what));
            int i = message.what;
            if (i != 1 && i != 2) {
                if (i == 6) {
                    InboundSmsHandler inboundSmsHandler = InboundSmsHandler.this;
                    inboundSmsHandler.transitionTo(inboundSmsHandler.mIdleState);
                    return true;
                }
                if (i != 7) {
                    return false;
                }
            }
            InboundSmsHandler.this.deferMessage(message);
            return true;
        }
    }

    public class IdleState extends State {
        public IdleState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            InboundSmsHandler.this.log("IdleState.enter: entering IdleState");
            InboundSmsHandler inboundSmsHandler = InboundSmsHandler.this;
            inboundSmsHandler.sendMessageDelayed(5, inboundSmsHandler.getWakeLockTimeout());
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            InboundSmsHandler.this.mWakeLock.acquire();
            InboundSmsHandler.this.log("IdleState.exit: acquired wakelock, leaving IdleState");
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            InboundSmsHandler.this.log("IdleState.processMessage: processing " + InboundSmsHandler.this.getWhatToString(message.what));
            int i = message.what;
            if (i != 1 && i != 2) {
                if (i != 4) {
                    if (i == 5) {
                        if (InboundSmsHandler.this.mWakeLock.isHeld()) {
                            InboundSmsHandler.this.mWakeLock.release();
                        }
                        boolean zIsHeld = InboundSmsHandler.this.mWakeLock.isHeld();
                        InboundSmsHandler inboundSmsHandler = InboundSmsHandler.this;
                        if (zIsHeld) {
                            inboundSmsHandler.log("IdleState.processMessage: EVENT_RELEASE_WAKELOCK: mWakeLock is still held after release");
                        } else {
                            inboundSmsHandler.log("IdleState.processMessage: EVENT_RELEASE_WAKELOCK: mWakeLock released");
                        }
                    } else if (i != 7) {
                        return false;
                    }
                }
                return true;
            }
            InboundSmsHandler.this.deferMessage(message);
            InboundSmsHandler inboundSmsHandler2 = InboundSmsHandler.this;
            inboundSmsHandler2.transitionTo(inboundSmsHandler2.mDeliveringState);
            return true;
        }
    }

    public class DeliveringState extends State {
        public DeliveringState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            InboundSmsHandler.this.log("DeliveringState.enter: entering DeliveringState");
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            InboundSmsHandler.this.log("DeliveringState.exit: leaving DeliveringState");
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) throws Throwable {
            InboundSmsHandler.this.log("DeliveringState.processMessage: processing " + InboundSmsHandler.this.getWhatToString(message.what));
            int i = message.what;
            if (i == 1) {
                InboundSmsHandler.this.handleNewSms((AsyncResult) message.obj);
                InboundSmsHandler.this.sendMessage(4);
                return true;
            }
            if (i == 2) {
                boolean zProcessMessagePart = InboundSmsHandler.this.processMessagePart((InboundSmsTracker) message.obj);
                InboundSmsHandler inboundSmsHandler = InboundSmsHandler.this;
                if (zProcessMessagePart) {
                    inboundSmsHandler.sendMessage(inboundSmsHandler.obtainMessage(8, message.obj));
                    InboundSmsHandler inboundSmsHandler2 = InboundSmsHandler.this;
                    inboundSmsHandler2.transitionTo(inboundSmsHandler2.mWaitingState);
                } else {
                    inboundSmsHandler.log("DeliveringState.processMessage: EVENT_BROADCAST_SMS: No broadcast sent. Return to IdleState");
                    InboundSmsHandler.this.sendMessage(4);
                }
                return true;
            }
            if (i == 4) {
                InboundSmsHandler inboundSmsHandler3 = InboundSmsHandler.this;
                inboundSmsHandler3.transitionTo(inboundSmsHandler3.mIdleState);
                return true;
            }
            if (i == 5) {
                if (InboundSmsHandler.this.mWakeLock.isHeld()) {
                    InboundSmsHandler.this.mWakeLock.release();
                }
                if (!InboundSmsHandler.this.mWakeLock.isHeld()) {
                    InboundSmsHandler.this.loge("mWakeLock released while delivering/broadcasting!");
                }
                return true;
            }
            if (i == 7) {
                InboundSmsHandler.this.handleInjectSms((AsyncResult) message.obj, message.arg1 == 1, message.arg2);
                InboundSmsHandler.this.sendMessage(4);
                return true;
            }
            InboundSmsHandler inboundSmsHandler4 = InboundSmsHandler.this;
            if (i == 8) {
                inboundSmsHandler4.logd("process tracker message in DeliveringState " + message.arg1);
                return true;
            }
            inboundSmsHandler4.logeWithLocalLog("Unhandled msg in delivering state, msg.what = " + InboundSmsHandler.this.getWhatToString(message.what));
            return false;
        }
    }

    public class WaitingState extends State {
        private InboundSmsTracker mLastDeliveredSmsTracker;

        public WaitingState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            InboundSmsHandler.this.log("WaitingState.enter: entering WaitingState");
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            InboundSmsHandler.this.log("WaitingState.exit: leaving WaitingState");
            InboundSmsHandler.this.setWakeLockTimeout(GbaManager.RETRY_TIME_MS);
            InboundSmsHandler.this.mPhone.getIccSmsInterfaceManager().mDispatchersController.sendEmptyMessage(17);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            InboundSmsHandler.this.log("WaitingState.processMessage: processing " + InboundSmsHandler.this.getWhatToString(message.what));
            int i = message.what;
            if (i == 2) {
                if (this.mLastDeliveredSmsTracker != null) {
                    InboundSmsHandler.this.logWithLocalLog("Defer sms broadcast due to undelivered sms,  messageCount = " + this.mLastDeliveredSmsTracker.getMessageCount() + " destPort = " + this.mLastDeliveredSmsTracker.getDestPort() + " timestamp = " + this.mLastDeliveredSmsTracker.getTimestamp() + " currentTimestamp = " + System.currentTimeMillis(), this.mLastDeliveredSmsTracker.getMessageId());
                }
                InboundSmsHandler.this.deferMessage(message);
                return true;
            }
            if (i == 3) {
                this.mLastDeliveredSmsTracker = null;
                InboundSmsHandler.this.sendMessage(4);
                InboundSmsHandler inboundSmsHandler = InboundSmsHandler.this;
                inboundSmsHandler.transitionTo(inboundSmsHandler.mDeliveringState);
                return true;
            }
            if (i != 4) {
                if (i != 8) {
                    if (i != 9) {
                        return false;
                    }
                    InboundSmsHandler.this.logeWithLocalLog("WaitingState.processMessage: received EVENT_RECEIVER_TIMEOUT");
                    InboundSmsTracker inboundSmsTracker = this.mLastDeliveredSmsTracker;
                    if (inboundSmsTracker != null) {
                        inboundSmsTracker.getSmsBroadcastReceiver(InboundSmsHandler.this).fakeNextAction();
                    }
                    return true;
                }
                this.mLastDeliveredSmsTracker = (InboundSmsTracker) message.obj;
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNewSms(AsyncResult asyncResult) {
        int iDispatchMessage;
        SatelliteController satelliteController;
        if (asyncResult.exception != null) {
            loge("Exception processing incoming SMS: " + asyncResult.exception);
            return;
        }
        try {
            iDispatchMessage = dispatchMessage(((SmsMessage) asyncResult.result).mWrappedSmsMessage, 0, 0);
        } catch (RuntimeException e) {
            loge("Exception dispatching message", e);
            iDispatchMessage = 6;
        }
        if (iDispatchMessage == 1 && (satelliteController = SatelliteController.getInstance()) != null && satelliteController.shouldSendSmsToDatagramDispatcher(this.mPhone)) {
            satelliteController.onSmsReceived(this.mPhone.getSubId());
        }
        if (iDispatchMessage != -1) {
            notifyAndAcknowledgeLastIncomingSms(iDispatchMessage == 1, iDispatchMessage, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.internal.telephony.SmsDispatchersController$SmsInjectionCallback] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.internal.telephony.SmsDispatchersController$SmsInjectionCallback] */
    /* JADX WARN: Type inference failed for: r2v0, types: [com.android.internal.telephony.InboundSmsHandler] */
    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.internal.telephony.InboundSmsHandler] */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3, types: [int] */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v7 */
    /* JADX WARN: Type inference failed for: r2v8 */
    public void handleInjectSms(AsyncResult asyncResult, boolean z, int i) {
        ?? r1;
        ?? r0 = 0;
        try {
            r1 = (SmsDispatchersController.SmsInjectionCallback) asyncResult.userObj;
            try {
                SmsMessage smsMessage = (SmsMessage) asyncResult.result;
                if (smsMessage == null) {
                    loge("Null injected sms");
                    this = 7;
                } else {
                    this = dispatchMessage(smsMessage.mWrappedSmsMessage, z ? 1 : 2, i);
                }
            } catch (RuntimeException e) {
                e = e;
                r0 = r1;
                this.loge("Exception dispatching message", e);
                this = 6;
                r1 = r0;
            }
        } catch (RuntimeException e2) {
            e = e2;
        }
        if (r1 != 0) {
            r1.onSmsInjectedResult(this);
        }
    }

    private int dispatchMessage(SmsMessageBase smsMessageBase, int i, int i2) {
        TelephonyAnalytics telephonyAnalytics;
        TelephonyAnalytics.SmsMmsAnalytics smsMmsAnalytics;
        if (smsMessageBase == null) {
            loge("dispatchSmsMessage: message is null");
            return 8;
        }
        if (((IHyperTeleSmsManager) HyperTelephonyFactory.getInstance().initModule(IHyperTeleSmsManager.DEFAULT, new Object[0])).dispatchCarrierTrue(this.mContext, getPhone(), smsMessageBase) == 1) {
            return 1;
        }
        if (this.mSmsReceiveDisabled) {
            log("Received short message on device which doesn't support receiving SMS. Ignored.");
            return 1;
        }
        if (isMtSmsPollingMessage(smsMessageBase)) {
            log("Received MT SMS polling message. Ignored.");
            return 1;
        }
        SatelliteController satelliteController = SatelliteController.getInstance();
        if (satelliteController != null && satelliteController.shouldDropSms(this.mPhone)) {
            log("SMS not supported during satellite session.");
            return 1;
        }
        int iDispatchMessageRadioSpecific = dispatchMessageRadioSpecific(smsMessageBase, i, i2);
        if (iDispatchMessageRadioSpecific != 1 && iDispatchMessageRadioSpecific != -1) {
            this.mPhone.getSmsStats().onIncomingSmsError(is3gpp2(), i, iDispatchMessageRadioSpecific, isEmergencyNumber(smsMessageBase.getOriginatingAddress()), 0);
            Phone phone = this.mPhone;
            if (phone != null && (telephonyAnalytics = phone.getTelephonyAnalytics()) != null && (smsMmsAnalytics = telephonyAnalytics.getSmsMmsAnalytics()) != null) {
                smsMmsAnalytics.onIncomingSmsError(i, iDispatchMessageRadioSpecific);
            }
        }
        return iDispatchMessageRadioSpecific;
    }

    private void notifyAndAcknowledgeLastIncomingSms(boolean z, int i, Message message) {
        if (!z) {
            Intent intent = new Intent("android.provider.Telephony.SMS_REJECTED");
            intent.putExtra("result", i);
            intent.putExtra(CallWaitingController.KEY_SUB_ID, this.mPhone.getSubId());
            this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.RECEIVE_SMS");
        }
        acknowledgeLastIncomingSms(z, i, message);
    }

    protected int dispatchNormalMessage(SmsMessageBase smsMessageBase, int i) {
        int i2;
        InboundSmsTracker inboundSmsTrackerMakeInboundSmsTracker;
        SmsHeader.PortAddrs portAddrs;
        SmsHeader.ConcatRef concatRef;
        SmsHeader userDataHeader = smsMessageBase.getUserDataHeader();
        if (userDataHeader == null || (concatRef = userDataHeader.concatRef) == null) {
            if (userDataHeader == null || (portAddrs = userDataHeader.portAddrs) == null) {
                i2 = -1;
            } else {
                int i3 = portAddrs.destPort;
                log("destination port: " + i3);
                i2 = i3;
            }
            inboundSmsTrackerMakeInboundSmsTracker = TelephonyComponentFactory.getInstance().inject(InboundSmsTracker.class.getName()).makeInboundSmsTracker(this.mContext, smsMessageBase.getPdu(), smsMessageBase.getTimestampMillis(), i2, is3gpp2(), false, smsMessageBase.getOriginatingAddress(), smsMessageBase.getDisplayOriginatingAddress(), smsMessageBase.getMessageBody(), smsMessageBase.getMessageClass() == SmsConstants.MessageClass.CLASS_0, this.mPhone.getSubId(), i);
        } else {
            SmsHeader.PortAddrs portAddrs2 = userDataHeader.portAddrs;
            inboundSmsTrackerMakeInboundSmsTracker = TelephonyComponentFactory.getInstance().inject(InboundSmsTracker.class.getName()).makeInboundSmsTracker(this.mContext, smsMessageBase.getPdu(), smsMessageBase.getTimestampMillis(), portAddrs2 != null ? portAddrs2.destPort : -1, is3gpp2(), smsMessageBase.getOriginatingAddress(), smsMessageBase.getDisplayOriginatingAddress(), concatRef.refNumber, concatRef.seqNumber, concatRef.msgCount, false, smsMessageBase.getMessageBody(), smsMessageBase.getMessageClass() == SmsConstants.MessageClass.CLASS_0, this.mPhone.getSubId(), i);
        }
        return addTrackerToRawTableAndSendMessage(inboundSmsTrackerMakeInboundSmsTracker, inboundSmsTrackerMakeInboundSmsTracker.getDestPort() == -1);
    }

    protected int addTrackerToRawTableAndSendMessage(InboundSmsTracker inboundSmsTracker, boolean z) {
        int iAddTrackerToRawTable = addTrackerToRawTable(inboundSmsTracker, z);
        if (iAddTrackerToRawTable == 1) {
            sendMessage(2, inboundSmsTracker);
            return 1;
        }
        if (iAddTrackerToRawTable != 5) {
            return iAddTrackerToRawTable;
        }
        return 1;
    }

    protected boolean processMessagePart(final InboundSmsTracker inboundSmsTracker) throws Throwable {
        byte[][] bArr;
        int i;
        boolean zIsBlocked;
        int realDestPort;
        TelephonyAnalytics telephonyAnalytics;
        TelephonyAnalytics.SmsMmsAnalytics smsMmsAnalytics;
        TelephonyAnalytics telephonyAnalytics2;
        TelephonyAnalytics.SmsMmsAnalytics smsMmsAnalytics2;
        final int messageCount = inboundSmsTracker.getMessageCount();
        int destPort = inboundSmsTracker.getDestPort();
        final String address = inboundSmsTracker.getAddress();
        if (messageCount <= 0) {
            loge("processMessagePart: returning false due to invalid message count " + messageCount, inboundSmsTracker.getMessageId());
            return false;
        }
        Cursor cursor = null;
        int i2 = 1;
        boolean zIsBlocked2 = false;
        if (messageCount == 1) {
            bArr = new byte[][]{inboundSmsTracker.getPdu()};
            inboundSmsTracker.getTimestamp();
            zIsBlocked = MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).isBlocked(this.mPhone, inboundSmsTracker.getDisplayAddress(), null, 1000L, false);
            i = destPort;
        } else {
            try {
                try {
                    String string = Integer.toString(inboundSmsTracker.getReferenceNumber());
                    try {
                        Cursor cursorQuery = this.mResolver.query(sRawUri, PDU_SEQUENCE_PORT_PROJECTION, inboundSmsTracker.getQueryForSegments(), onModifyQueryWhereArgs(new String[]{address, string, Integer.toString(inboundSmsTracker.getMessageCount())}), null);
                        try {
                            if (cursorQuery == null) {
                                try {
                                    loge("Can't access multipart SMS database");
                                    if (cursorQuery != null) {
                                        cursorQuery.close();
                                    }
                                    return false;
                                } catch (Exception unused) {
                                    cursor = cursorQuery;
                                    loge("Exception -- Can't access multipart SMS database");
                                    if (cursor != null) {
                                        cursor.close();
                                    }
                                    return false;
                                }
                            }
                            try {
                                int count = cursorQuery.getCount();
                                if (count < messageCount) {
                                    log("processMessagePart: returning false. Only " + count + " of " + messageCount + " segments  have arrived. refNumber: " + string, inboundSmsTracker.getMessageId());
                                    cursorQuery.close();
                                    return false;
                                }
                                byte[][] bArr2 = new byte[messageCount][];
                                long[] jArr = new long[messageCount];
                                while (cursorQuery.moveToNext()) {
                                    Map<Integer, Integer> map = PDU_SEQUENCE_PORT_PROJECTION_INDEX_MAPPING;
                                    int i3 = cursorQuery.getInt(map.get(Integer.valueOf(i2)).intValue()) - inboundSmsTracker.getIndexOffset();
                                    if (i3 >= messageCount || i3 < 0) {
                                        loge(String.format("processMessagePart: invalid seqNumber = %d, messageCount = %d", Integer.valueOf(i3 + inboundSmsTracker.getIndexOffset()), Integer.valueOf(messageCount)), inboundSmsTracker.getMessageId());
                                    } else {
                                        bArr2[i3] = HexDump.hexStringToByteArray(cursorQuery.getString(map.get(0).intValue()));
                                        if (i3 == 0 && !cursorQuery.isNull(map.get(2).intValue()) && (realDestPort = InboundSmsTracker.getRealDestPort(cursorQuery.getInt(map.get(2).intValue()))) != -1) {
                                            destPort = realDestPort;
                                        }
                                        jArr[i3] = cursorQuery.getLong(map.get(3).intValue());
                                        if (!zIsBlocked2) {
                                            zIsBlocked2 = MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).isBlocked(this.mPhone, cursorQuery.getString(map.get(9).intValue()), null, 1000L, zIsBlocked2);
                                        }
                                    }
                                    i2 = 1;
                                }
                                log("processMessagePart: all " + messageCount + " segments  received. refNumber: " + string, inboundSmsTracker.getMessageId());
                                cursorQuery.close();
                                bArr = bArr2;
                                i = destPort;
                                zIsBlocked = zIsBlocked2;
                            } catch (SQLException e) {
                                e = e;
                                cursor = cursorQuery;
                                loge("processMessagePart: Can't access multipart SMS database, " + SmsController.formatCrossStackMessageId(inboundSmsTracker.getMessageId()), e);
                                if (cursor != null) {
                                    cursor.close();
                                }
                                return false;
                            }
                        } catch (Throwable th) {
                            th = th;
                            cursor = cursorQuery;
                            if (cursor != null) {
                                cursor.close();
                            }
                            throw th;
                        }
                    } catch (Exception unused2) {
                    }
                } catch (SQLException e2) {
                    e = e2;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
        boolean z = i == 2948;
        String format = inboundSmsTracker.getFormat();
        List listAsList = Arrays.asList(bArr);
        if (listAsList.size() == 0 || listAsList.contains(null)) {
            logeWithLocalLog("processMessagePart: returning false due to ".concat(listAsList.size() == 0 ? "pduList.size() == 0" : "pduList.contains(null)"), inboundSmsTracker.getMessageId());
            this.mPhone.getSmsStats().onIncomingSmsError(is3gpp2(), inboundSmsTracker.getSource(), 7, isEmergencyNumber(inboundSmsTracker.getAddress()), 0);
            Phone phone = this.mPhone;
            if (phone != null && (telephonyAnalytics = phone.getTelephonyAnalytics()) != null && (smsMmsAnalytics = telephonyAnalytics.getSmsMmsAnalytics()) != null) {
                smsMmsAnalytics.onIncomingSmsError(inboundSmsTracker.getSource(), 7);
            }
            deleteFromRawTable(inboundSmsTracker.getDeleteWhere(), inboundSmsTracker.getDeleteWhereArgs(), 2);
            return false;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (z) {
            for (byte[] userData : bArr) {
                if (format == "3gpp") {
                    SmsMessage smsMessageOnCreateSmsMessage = onCreateSmsMessage(userData, "3gpp");
                    if (smsMessageOnCreateSmsMessage == null) {
                        loge("processMessagePart: SmsMessage.createFromPdu returned null", inboundSmsTracker.getMessageId());
                        this.mPhone.getSmsStats().onIncomingSmsWapPush(inboundSmsTracker.getSource(), messageCount, 8, inboundSmsTracker.getMessageId(), isEmergencyNumber(inboundSmsTracker.getAddress()), 0);
                        return false;
                    }
                    userData = smsMessageOnCreateSmsMessage.getUserData();
                }
                byteArrayOutputStream.write(userData, 0, userData.length);
            }
        }
        final SmsBroadcastReceiver smsBroadcastReceiver = inboundSmsTracker.getSmsBroadcastReceiver(this);
        if (onCheckIfStopProcessMessagePart(bArr, inboundSmsTracker.getFormat(), inboundSmsTracker)) {
            return false;
        }
        if (!isMainUserUnlocked()) {
            log("processMessagePart: !isUserUnlocked; calling processMessagePartWithUserLocked. Port: " + i, inboundSmsTracker.getMessageId());
            if (z) {
                bArr = new byte[][]{byteArrayOutputStream.toByteArray()};
            }
            return processMessagePartWithUserLocked(inboundSmsTracker, bArr, i, smsBroadcastReceiver, zIsBlocked);
        }
        if (z) {
            final byte[] byteArray = byteArrayOutputStream.toByteArray();
            final byte[][] bArr3 = bArr;
            this.mBackgroundExecutor.execute(new Runnable() { // from class: com.android.internal.telephony.InboundSmsHandler$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$processMessagePart$0(bArr3, byteArray, smsBroadcastReceiver, address, inboundSmsTracker, messageCount);
                }
            });
            return true;
        }
        byte[][] bArr4 = bArr;
        boolean z2 = zIsBlocked;
        this.mPhone.getSmsStats().onIncomingSmsSuccess(is3gpp2(), inboundSmsTracker.getSource(), messageCount, z2, inboundSmsTracker.getMessageId(), isEmergencyNumber(inboundSmsTracker.getAddress()), getTotalPduLength(bArr4));
        CarrierRoamingSatelliteSessionStats.getInstance(this.mPhone.getSubId()).onIncomingSms(this.mPhone.getSubId());
        Phone phone2 = this.mPhone;
        if (phone2 != null && (telephonyAnalytics2 = phone2.getTelephonyAnalytics()) != null && (smsMmsAnalytics2 = telephonyAnalytics2.getSmsMmsAnalytics()) != null) {
            smsMmsAnalytics2.onIncomingSmsSuccess(inboundSmsTracker.getSource());
        }
        int i4 = i;
        if (filterSms(bArr4, i4, inboundSmsTracker, smsBroadcastReceiver, true, z2)) {
            return true;
        }
        if (!z2) {
            dispatchSmsDeliveryIntent(bArr4, format, i4, smsBroadcastReceiver, inboundSmsTracker.isClass0(), inboundSmsTracker.getSubId(), inboundSmsTracker.getMessageId());
            return true;
        }
        deleteFromRawTable(inboundSmsTracker.getDeleteWhere(), inboundSmsTracker.getDeleteWhereArgs(), 1);
        log("processMessagePart: returning false as the phone number is blocked", inboundSmsTracker.getMessageId());
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processMessagePart$0(byte[][] bArr, byte[] bArr2, SmsBroadcastReceiver smsBroadcastReceiver, String str, InboundSmsTracker inboundSmsTracker, int i) {
        int iOnDispatchWapPdu = onDispatchWapPdu(bArr, bArr2, smsBroadcastReceiver, str, inboundSmsTracker.getSubId(), inboundSmsTracker.getMessageId());
        log("processMessagePart: dispatchWapPdu() returned " + iOnDispatchWapPdu, inboundSmsTracker.getMessageId());
        this.mPhone.getSmsStats().onIncomingSmsWapPush(inboundSmsTracker.getSource(), i, iOnDispatchWapPdu, inboundSmsTracker.getMessageId(), isEmergencyNumber(inboundSmsTracker.getAddress()), (iOnDispatchWapPdu == -1 || iOnDispatchWapPdu == 1) ? bArr2.length : 0);
        if (iOnDispatchWapPdu != -1) {
            deleteFromRawTable(inboundSmsTracker.getDeleteWhere(), inboundSmsTracker.getDeleteWhereArgs(), 2);
            loge("processMessagePart: WAP push broadcast not sent, result=" + iOnDispatchWapPdu, inboundSmsTracker.getMessageId());
            sendMessage(3);
        }
    }

    private boolean isEmergencyNumber(String str) {
        TelephonyManager telephonyManagerFrom;
        if (this.mPhone.hasCalling() && (telephonyManagerFrom = TelephonyManager.from(this.mContext)) != null) {
            return telephonyManagerFrom.isEmergencyNumber(str);
        }
        return false;
    }

    protected boolean processMessagePartWithUserLocked(InboundSmsTracker inboundSmsTracker, byte[][] bArr, int i, SmsBroadcastReceiver smsBroadcastReceiver, boolean z) {
        if (i == 2948 && MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).isWapPushForMms(bArr[0], this, inboundSmsTracker, this.mWapPush)) {
            showNewMessageNotification();
            return false;
        }
        if (i == -1) {
            if (filterSms(bArr, i, inboundSmsTracker, smsBroadcastReceiver, false, z)) {
                return true;
            }
            if (!z) {
                showNewMessageNotification();
            }
        }
        return false;
    }

    private boolean isMainUserUnlocked() {
        UserHandle mainUser = this.mUserManager.getMainUser();
        UserManager userManager = this.mUserManager;
        if (mainUser != null) {
            return userManager.isUserUnlocked(mainUser);
        }
        return userManager.isUserUnlocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showNewMessageNotification() {
        if (StorageManager.isFileEncrypted()) {
            log("Show new message notification.");
            Notification.Builder channelId = new Notification.Builder(this.mContext).setSmallIcon(R.drawable.sym_action_chat).setAutoCancel(true).setVisibility(1).setDefaults(-1).setContentTitle(this.mContext.getString(R.string.permdesc_statusBar)).setContentText(this.mContext.getString(R.string.permdesc_startViewPermissionUsage)).setContentIntent(PendingIntent.getBroadcast(this.mContext, 0, new Intent(ACTION_OPEN_SMS_APP), 1140850688)).setChannelId(NotificationChannelController.CHANNEL_ID_SMS);
            NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService("notification");
            if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
                Notification notificationBuild = channelId.build();
                notificationBuild.extraNotification.setTargetPkg("com.android.mms");
                notificationManager.notify(NOTIFICATION_TAG, 1, notificationBuild);
            } else {
                UserHandle mainUser = this.mUserManager.getMainUser();
                if (mainUser != null) {
                    notificationManager.notifyAsUser(NOTIFICATION_TAG, 1, channelId.build(), mainUser);
                } else {
                    notificationManager.notify(NOTIFICATION_TAG, 1, channelId.build());
                }
            }
        }
    }

    static void cancelNewMessageNotification(Context context) {
        ((NotificationManager) context.getSystemService("notification")).cancel(NOTIFICATION_TAG, 1);
    }

    private List createDefaultSmsFilters() {
        ArrayList arrayList = new ArrayList(3);
        arrayList.add(new SmsFilter() { // from class: com.android.internal.telephony.InboundSmsHandler$$ExternalSyntheticLambda0
            @Override // com.android.internal.telephony.InboundSmsHandler.SmsFilter
            public final boolean filterSms(byte[][] bArr, int i, InboundSmsTracker inboundSmsTracker, InboundSmsHandler.SmsBroadcastReceiver smsBroadcastReceiver, boolean z, boolean z2, List list) {
                return this.f$0.lambda$createDefaultSmsFilters$1(bArr, i, inboundSmsTracker, smsBroadcastReceiver, z, z2, list);
            }
        });
        arrayList.add(new SmsFilter() { // from class: com.android.internal.telephony.InboundSmsHandler$$ExternalSyntheticLambda1
            @Override // com.android.internal.telephony.InboundSmsHandler.SmsFilter
            public final boolean filterSms(byte[][] bArr, int i, InboundSmsTracker inboundSmsTracker, InboundSmsHandler.SmsBroadcastReceiver smsBroadcastReceiver, boolean z, boolean z2, List list) {
                return this.f$0.lambda$createDefaultSmsFilters$2(bArr, i, inboundSmsTracker, smsBroadcastReceiver, z, z2, list);
            }
        });
        arrayList.add(new SmsFilter() { // from class: com.android.internal.telephony.InboundSmsHandler$$ExternalSyntheticLambda2
            @Override // com.android.internal.telephony.InboundSmsHandler.SmsFilter
            public final boolean filterSms(byte[][] bArr, int i, InboundSmsTracker inboundSmsTracker, InboundSmsHandler.SmsBroadcastReceiver smsBroadcastReceiver, boolean z, boolean z2, List list) {
                return this.f$0.lambda$createDefaultSmsFilters$3(bArr, i, inboundSmsTracker, smsBroadcastReceiver, z, z2, list);
            }
        });
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createDefaultSmsFilters$1(byte[][] bArr, int i, InboundSmsTracker inboundSmsTracker, SmsBroadcastReceiver smsBroadcastReceiver, boolean z, boolean z2, List list) {
        CarrierServicesSmsFilterCallback carrierServicesSmsFilterCallback = new CarrierServicesSmsFilterCallback(bArr, i, inboundSmsTracker, inboundSmsTracker.getFormat(), smsBroadcastReceiver, z, inboundSmsTracker.isClass0(), inboundSmsTracker.getSubId(), inboundSmsTracker.getMessageId(), z2, list);
        if (!new CarrierServicesSmsFilter(this.mContext, this.mPhone, bArr, i, inboundSmsTracker.getFormat(), carrierServicesSmsFilterCallback, getName() + "::CarrierServicesSmsFilter", this.mCarrierServiceLocalLog, inboundSmsTracker.getMessageId()).filter()) {
            return false;
        }
        log("SMS is being handled by carrier service", inboundSmsTracker.getMessageId());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createDefaultSmsFilters$2(byte[][] bArr, int i, InboundSmsTracker inboundSmsTracker, SmsBroadcastReceiver smsBroadcastReceiver, boolean z, boolean z2, List list) {
        if (!VisualVoicemailSmsFilter.filter(this.mContext, bArr, inboundSmsTracker.getFormat(), i, inboundSmsTracker.getSubId())) {
            return false;
        }
        logWithLocalLog("Visual voicemail SMS dropped", inboundSmsTracker.getMessageId());
        dropFilteredSms(inboundSmsTracker, smsBroadcastReceiver, z2);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createDefaultSmsFilters$3(byte[][] bArr, int i, InboundSmsTracker inboundSmsTracker, SmsBroadcastReceiver smsBroadcastReceiver, boolean z, boolean z2, List list) {
        if (!new MissedIncomingCallSmsFilter(this.mPhone).filter(bArr, inboundSmsTracker.getFormat())) {
            return false;
        }
        logWithLocalLog("Missed incoming call SMS received", inboundSmsTracker.getMessageId());
        dropFilteredSms(inboundSmsTracker, smsBroadcastReceiver, z2);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dropFilteredSms(InboundSmsTracker inboundSmsTracker, SmsBroadcastReceiver smsBroadcastReceiver, boolean z) {
        if (z) {
            deleteFromRawTable(inboundSmsTracker.getDeleteWhere(), inboundSmsTracker.getDeleteWhereArgs(), 1);
            sendMessage(3);
        } else {
            dropSms(smsBroadcastReceiver);
        }
    }

    protected boolean filterSms(byte[][] bArr, int i, InboundSmsTracker inboundSmsTracker, SmsBroadcastReceiver smsBroadcastReceiver, boolean z, boolean z2) {
        return filterSms(bArr, i, inboundSmsTracker, smsBroadcastReceiver, z, z2, this.mSmsFilters);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean filterSms(byte[][] bArr, int i, InboundSmsTracker inboundSmsTracker, SmsBroadcastReceiver smsBroadcastReceiver, boolean z, boolean z2, List list) {
        ListIterator listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            if (((SmsFilter) listIterator.next()).filterSms(bArr, i, inboundSmsTracker, smsBroadcastReceiver, z, z2, list.subList(listIterator.nextIndex(), list.size()))) {
                return true;
            }
        }
        return false;
    }

    public void dispatchIntent(Intent intent, String str, String str2, Bundle bundle, SmsBroadcastReceiver smsBroadcastReceiver, UserHandle userHandle, int i) {
        intent.addFlags(134217728);
        String action = intent.getAction();
        if ("android.provider.Telephony.SMS_DELIVER".equals(action) || "android.provider.Telephony.SMS_RECEIVED".equals(action) || "android.provider.Telephony.WAP_PUSH_DELIVER".equals(action) || "android.provider.Telephony.WAP_PUSH_RECEIVED".equals(action)) {
            intent.addFlags(268435456);
        }
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent, this.mPhone.getPhoneId());
        if (SubscriptionManager.isValidSubscriptionId(i)) {
            SubscriptionManager.putSubscriptionIdExtra(intent, i);
        }
        Collection otpLinksAndLogMetrics = shouldCheckForOtp(intent) ? getOtpLinksAndLogMetrics(intent) : null;
        if (MiuiSmsControllerStub.get(this.mPhone.getPhoneId()).beforeDispatchIntent(this, intent, str, 0, smsBroadcastReceiver.mDeleteWhere, smsBroadcastReceiver.mDeleteWhereArgs)) {
            return;
        }
        dispatchSmsToUsers(intent, str, str2, bundle, smsBroadcastReceiver, userHandle, otpLinksAndLogMetrics);
    }

    private void dispatchSmsToUsers(Intent intent, String str, String str2, Bundle bundle, SmsBroadcastReceiver smsBroadcastReceiver, UserHandle userHandle, Collection collection) {
        int[] iArr;
        if (userHandle.equals(UserHandle.ALL)) {
            List<UserHandle> userHandles = this.mUserManager.getUserHandles(false);
            UserHandle mainUser = this.mUserManager.getMainUser();
            ArrayList arrayList = new ArrayList();
            for (UserHandle userHandle2 : userHandles) {
                if (this.mUserManager.isUserRunning(userHandle2)) {
                    arrayList.add(userHandle2);
                } else {
                    long messageId = smsBroadcastReceiver != null ? smsBroadcastReceiver.mInboundSmsTracker.getMessageId() : -1L;
                    if (userHandle2.equals(mainUser)) {
                        logeWithLocalLog("dispatchIntent: MAIN user is not running", messageId);
                    } else if (userHandle2.equals(UserHandle.SYSTEM)) {
                        logeWithLocalLog("dispatchIntent: SYSTEM user is not running", messageId);
                    }
                }
            }
            if (arrayList.isEmpty()) {
                iArr = new int[]{userHandle.getIdentifier()};
            } else {
                iArr = new int[arrayList.size()];
                for (int i = 0; i < arrayList.size(); i++) {
                    iArr[i] = ((UserHandle) arrayList.get(i)).getIdentifier();
                }
            }
            int[] iArr2 = iArr;
            for (int length = iArr2.length - 1; length >= 0; length--) {
                UserHandle userHandleOf = UserHandle.of(iArr2[length]);
                if (isMainUser(iArr2[length]) || (!hasUserRestriction("no_sms", userHandleOf) && !this.mUserManager.isManagedProfile(iArr2[length]))) {
                    sendBroadcast(intent, str, str2, bundle, isMainUser(iArr2[length]) ? smsBroadcastReceiver : null, userHandleOf, collection);
                }
            }
            return;
        }
        sendBroadcast(intent, str, str2, bundle, smsBroadcastReceiver, userHandle, collection);
    }

    private boolean shouldCheckForOtp(Intent intent) {
        SmsMessage[] messagesFromIntent;
        if (!"android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction()) || (messagesFromIntent = Telephony.Sms.Intents.getMessagesFromIntent(intent)) == null) {
            return false;
        }
        for (SmsMessage smsMessage : messagesFromIntent) {
            if (smsMessage != null && smsMessage.getDisplayMessageBody() != null && Telephony.Sms.shouldCheckForOtp(this.mContext, smsMessage.getDisplayMessageBody())) {
                return true;
            }
        }
        return false;
    }

    private void sendBroadcast(Intent intent, String str, String str2, Bundle bundle, SmsBroadcastReceiver smsBroadcastReceiver, UserHandle userHandle, Collection collection) {
        if (smsBroadcastReceiver != null) {
            smsBroadcastReceiver.setWaitingForIntent(intent);
        }
        if (collection == null) {
            try {
                Context context = this.mContext;
                context.createPackageContextAsUser(context.getPackageName(), 0, userHandle).sendOrderedBroadcast(intent, -1, str, str2, smsBroadcastReceiver, getHandler(), null, null, bundle);
            } catch (PackageManager.NameNotFoundException unused) {
            }
            PersistAtomsProto$OtpEvaluationEvent persistAtomsProto$OtpEvaluationEvent = new PersistAtomsProto$OtpEvaluationEvent();
            persistAtomsProto$OtpEvaluationEvent.result = 0;
            persistAtomsProto$OtpEvaluationEvent.redactionTimeMs = -1;
            this.mAtomsStorage.addOtpEvaluationEvent(persistAtomsProto$OtpEvaluationEvent);
            return;
        }
        if (containsOtp(collection)) {
            if (isGenericOtp(collection)) {
                sendBroadcastForGenericOtp(intent, str, str2, bundle, smsBroadcastReceiver, userHandle);
                return;
            } else {
                sendBroadcastToTrustedPackages(intent, str, str2, bundle, smsBroadcastReceiver, userHandle, getOtpTrustedPackagesFromTextLinks(collection));
                return;
            }
        }
        sendBroadcastWithStandardPermissions(intent, str, str2, bundle, smsBroadcastReceiver, userHandle);
    }

    private boolean isGenericOtp(Collection collection) {
        Iterator it = collection.iterator();
        boolean z = false;
        while (it.hasNext()) {
            TextLinks.TextLink textLink = (TextLinks.TextLink) it.next();
            for (int i = 0; i < textLink.getEntityCount(); i++) {
                if (textLink.getEntity(i).equals("otp")) {
                    z = true;
                } else if (textLink.getEntity(i).equals("sms_retriever_otp") || textLink.getEntity(i).equals("sms_web_otp")) {
                    return false;
                }
            }
        }
        return z;
    }

    private void sendBroadcastForGenericOtp(Intent intent, String str, String str2, Bundle bundle, SmsBroadcastReceiver smsBroadcastReceiver, UserHandle userHandle) {
        BroadcastOptions broadcastOptions = new BroadcastOptions(bundle);
        broadcastOptions.setRequireCompatChange(437043173L, false);
        sendBroadcastWithStandardPermissions(intent, str, str2, broadcastOptions.toBundle(), smsBroadcastReceiver, userHandle);
        broadcastOptions.setRequireCompatChange(437043173L, true);
        sendBroadcastToTrustedPackages(intent, str, str2, broadcastOptions.toBundle(), smsBroadcastReceiver, userHandle, null);
    }

    private Collection getOtpLinksAndLogMetrics(final Intent intent) {
        Collection collection;
        int iMin;
        try {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            collection = (Collection) CompletableFuture.supplyAsync(new Supplier() { // from class: com.android.internal.telephony.InboundSmsHandler$$ExternalSyntheticLambda4
                @Override // java.util.function.Supplier
                public final Object get() {
                    return this.f$0.lambda$getOtpLinksAndLogMetrics$4(intent);
                }
            }, this.mBackgroundExecutor).get(MAXIMUM_BROADCAST_DELAY_TIME_MS, TimeUnit.MILLISECONDS);
            iMin = (int) Math.min(SystemClock.elapsedRealtime() - jElapsedRealtime, 2147483647L);
        } catch (Exception e) {
            Rlog.e(getName(), "Unable to get OTP links", e);
            collection = Collections.EMPTY_LIST;
            iMin = -1;
        }
        if (iMin != -1) {
            PersistAtomsProto$OtpEvaluationEvent persistAtomsProto$OtpEvaluationEvent = new PersistAtomsProto$OtpEvaluationEvent();
            persistAtomsProto$OtpEvaluationEvent.result = containsOtp(collection) ? 2 : 1;
            persistAtomsProto$OtpEvaluationEvent.redactionTimeMs = iMin;
            this.mAtomsStorage.addOtpEvaluationEvent(persistAtomsProto$OtpEvaluationEvent);
        }
        return collection;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: generateOtpTextLinks, reason: merged with bridge method [inline-methods] */
    public Collection lambda$getOtpLinksAndLogMetrics$4(Intent intent) {
        SmsMessage[] messagesFromIntent = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        if (messagesFromIntent == null) {
            return Collections.EMPTY_LIST;
        }
        StringBuilder sb = new StringBuilder();
        for (SmsMessage smsMessage : messagesFromIntent) {
            if (smsMessage != null && smsMessage.getDisplayMessageBody() != null) {
                sb.append(smsMessage.getDisplayMessageBody());
            }
        }
        if (sb.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return getTextClassifier().generateLinks(new TextLinks.Request.Builder(sb.toString()).setEntityConfig(TC_REQUEST_CONFIG).build()).getLinks();
    }

    private boolean containsOtp(Collection collection) {
        Iterator it = collection.iterator();
        while (true) {
            if (!it.hasNext()) {
                return false;
            }
            TextLinks.TextLink textLink = (TextLinks.TextLink) it.next();
            for (int i = 0; i < textLink.getEntityCount(); i++) {
                if (textLink.getEntity(i).equals("sms_retriever_otp") || textLink.getEntity(i).equals("sms_web_otp") || textLink.getEntity(i).equals("otp")) {
                    return true;
                }
            }
        }
    }

    private static String getSmsRetrieverTargetPackageNameFromTextLinks(Collection collection) {
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            TextLinks.TextLink textLink = (TextLinks.TextLink) it.next();
            for (int i = 0; i < textLink.getEntityCount(); i++) {
                if (textLink.getEntity(i).equals("sms_retriever_otp")) {
                    return textLink.getExtras().getString("sms-retriever-hash-matched-package");
                }
            }
        }
        return null;
    }

    private static ArrayList getWebOtpOwnerPackagesFromTextLinks(Collection collection) {
        ArrayList<String> stringArrayList;
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            TextLinks.TextLink textLink = (TextLinks.TextLink) it.next();
            for (int i = 0; i < textLink.getEntityCount(); i++) {
                if (textLink.getEntity(i).equals("sms_web_otp") && (stringArrayList = textLink.getExtras().getStringArrayList("android.view.textclassifier.extra.OTP_TRUSTED_PACKAGES")) != null && !stringArrayList.isEmpty()) {
                    return stringArrayList;
                }
            }
        }
        return null;
    }

    public static List<String> getOtpTrustedPackagesFromTextLinks(Collection<TextLinks.TextLink> collection) {
        ArrayList arrayList = new ArrayList();
        String smsRetrieverTargetPackageNameFromTextLinks = getSmsRetrieverTargetPackageNameFromTextLinks(collection);
        if (smsRetrieverTargetPackageNameFromTextLinks != null) {
            arrayList.add(smsRetrieverTargetPackageNameFromTextLinks);
        }
        ArrayList webOtpOwnerPackagesFromTextLinks = getWebOtpOwnerPackagesFromTextLinks(collection);
        if (webOtpOwnerPackagesFromTextLinks != null) {
            arrayList.addAll(webOtpOwnerPackagesFromTextLinks);
        }
        return arrayList;
    }

    private void sendBroadcastWithStandardPermissions(Intent intent, String str, String str2, Bundle bundle, SmsBroadcastReceiver smsBroadcastReceiver, UserHandle userHandle) {
        try {
            Context context = this.mContext;
            context.createPackageContextAsUser(context.getPackageName(), 0, userHandle).sendOrderedBroadcast(intent, -1, str, str2, smsBroadcastReceiver, getHandler(), null, null, bundle);
        } catch (PackageManager.NameNotFoundException unused) {
        }
    }

    private void sendBroadcastToTrustedPackages(Intent intent, String str, String str2, Bundle bundle, SmsBroadcastReceiver smsBroadcastReceiver, UserHandle userHandle, List list) {
        Set smsOtpTrustedPackages = SmsManager.getSmsOtpTrustedPackages(this.mContext, userHandle);
        if (list != null) {
            smsOtpTrustedPackages.addAll(list);
        }
        String[] strArr = new String[smsOtpTrustedPackages.size()];
        Iterator it = smsOtpTrustedPackages.iterator();
        int i = 0;
        while (it.hasNext()) {
            strArr[i] = (String) it.next();
            i++;
        }
        try {
            BroadcastOptions broadcastOptions = new BroadcastOptions(bundle);
            broadcastOptions.setIncludedPackages(strArr);
            Context context = this.mContext;
            context.createPackageContextAsUser(context.getPackageName(), 0, userHandle).sendOrderedBroadcast(intent, -1, str, str2, smsBroadcastReceiver, getHandler(), null, null, broadcastOptions.toBundle());
            logRedactedPackages(intent, str, userHandle, smsOtpTrustedPackages);
        } catch (PackageManager.NameNotFoundException unused) {
        }
    }

    private void logRedactedPackages(final Intent intent, final String str, final UserHandle userHandle, final Set set) {
        this.mBackgroundExecutor.execute(new Runnable() { // from class: com.android.internal.telephony.InboundSmsHandler$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$logRedactedPackages$5(userHandle, intent, str, set);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$logRedactedPackages$5(UserHandle userHandle, Intent intent, String str, Set set) {
        int i = 0;
        List<ResolveInfo> listQueryBroadcastReceivers = this.mContext.createContextAsUser(userHandle, 0).getPackageManager().queryBroadcastReceivers(intent, InboundSmsTracker.DEST_PORT_FLAG_3GPP);
        ArrayList arrayList = new ArrayList();
        for (ResolveInfo resolveInfo : listQueryBroadcastReceivers) {
            if (this.mPackageManager.checkPermission(str, resolveInfo.resolvePackageName) == 0 && !set.contains(resolveInfo.resolvePackageName)) {
                arrayList.add(resolveInfo.resolvePackageName);
            }
        }
        int size = arrayList.size();
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            try {
                int packageUid = this.mContext.getPackageManager().getPackageUid((String) obj, InboundSmsTracker.DEST_PORT_FLAG_3GPP);
                PersistAtomsProto$OtpRedactionEvent persistAtomsProto$OtpRedactionEvent = new PersistAtomsProto$OtpRedactionEvent();
                persistAtomsProto$OtpRedactionEvent.uid = packageUid;
                persistAtomsProto$OtpRedactionEvent.count = 1;
                this.mAtomsStorage.addOtpRedactionEvent(persistAtomsProto$OtpRedactionEvent);
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
    }

    private boolean hasUserRestriction(String str, UserHandle userHandle) {
        List userRestrictionSources = this.mUserManager.getUserRestrictionSources(str, userHandle);
        return (userRestrictionSources == null || userRestrictionSources.isEmpty()) ? false : true;
    }

    private boolean isMainUser(int i) {
        return i == this.mUserManager.getMainUser().getIdentifier();
    }

    protected void deleteFromRawTable(String str, String[] strArr, int i) {
        int iDelete = this.mResolver.delete(i == 1 ? sRawUriPermanentDelete : sRawUri, str, strArr);
        if (iDelete == 0) {
            loge("No rows were deleted from raw table!");
            return;
        }
        log("Deleted " + iDelete + " rows from raw table.");
    }

    protected Bundle handleSmsWhitelisting(ComponentName componentName, boolean z) {
        String packageName;
        String str;
        BroadcastOptions broadcastOptionsMakeBasic;
        if (componentName != null) {
            packageName = componentName.getPackageName();
            str = "sms-app";
        } else {
            packageName = this.mContext.getPackageName();
            str = "sms-broadcast";
        }
        if (z) {
            broadcastOptionsMakeBasic = BroadcastOptions.makeBasic();
            broadcastOptionsMakeBasic.setBackgroundActivityStartsAllowed(true);
            broadcastOptionsMakeBasic.toBundle();
        } else {
            broadcastOptionsMakeBasic = null;
        }
        PowerWhitelistManager powerWhitelistManager = this.mPowerWhitelistManager;
        long jWhitelistAppTemporarilyForEvent = powerWhitelistManager != null ? powerWhitelistManager.whitelistAppTemporarilyForEvent(packageName, 1, 314, str) : 0L;
        if (broadcastOptionsMakeBasic == null) {
            broadcastOptionsMakeBasic = BroadcastOptions.makeBasic();
        }
        BroadcastOptions broadcastOptions = broadcastOptionsMakeBasic;
        broadcastOptions.setTemporaryAppAllowlist(jWhitelistAppTemporarilyForEvent, 0, 314, "");
        return broadcastOptions.toBundle();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void dispatchSmsDeliveryIntent(byte[][] bArr, String str, int i, SmsBroadcastReceiver smsBroadcastReceiver, boolean z, int i2, long j) {
        Intent intent = new Intent();
        intent.putExtra("pdus", (Serializable) bArr);
        intent.putExtra("format", str);
        if (j != 0) {
            intent.putExtra("messageId", j);
        }
        UserHandle subscriptionUserHandle = TelephonyUtils.getSubscriptionUserHandle(this.mContext, i2);
        if (subscriptionUserHandle == null) {
            subscriptionUserHandle = this.mUserManager.getMainUser();
        }
        if (i == -1) {
            intent.setAction("android.provider.Telephony.SMS_DELIVER");
            ComponentName defaultSmsApplicationAsUser = SmsApplication.getDefaultSmsApplicationAsUser(this.mContext, true, subscriptionUserHandle);
            if (defaultSmsApplicationAsUser != null) {
                intent.setComponent(defaultSmsApplicationAsUser);
                logWithLocalLog("Delivering SMS to: " + defaultSmsApplicationAsUser.getPackageName() + " " + defaultSmsApplicationAsUser.getClassName(), j);
            } else {
                intent.setComponent(null);
            }
            if (this.mPhone.getAppSmsManager().handleSmsReceivedIntent(intent)) {
                dropSms(smsBroadcastReceiver);
                return;
            }
        } else {
            intent.setAction("android.intent.action.DATA_SMS_RECEIVED");
            intent.setData(Uri.parse("sms://localhost:" + i));
            intent.setComponent(null);
        }
        Bundle bundleHandleSmsWhitelisting = handleSmsWhitelisting(intent.getComponent(), z);
        intent.addFlags(268435456);
        dispatchIntent(intent, "android.permission.RECEIVE_SMS", "android:receive_sms", bundleHandleSmsWhitelisting, smsBroadcastReceiver, subscriptionUserHandle, i2);
    }

    private boolean checkAndHandleDuplicate(InboundSmsTracker inboundSmsTracker) throws Throwable {
        Pair<String, String[]> exactMatchDupDetectQuery = inboundSmsTracker.getExactMatchDupDetectQuery();
        Cursor cursorQuery = null;
        try {
            ContentResolver contentResolver = this.mResolver;
            Uri uri = sRawUri;
            String[] strArr = PDU_DELETED_FLAG_PROJECTION;
            Cursor cursorQuery2 = contentResolver.query(uri, strArr, (String) exactMatchDupDetectQuery.first, (String[]) exactMatchDupDetectQuery.second, null);
            if (cursorQuery2 != null) {
                try {
                    if (cursorQuery2.moveToNext()) {
                        if (cursorQuery2.getCount() != 1) {
                            logeWithLocalLog("checkAndHandleDuplicate: Exact match query returned " + cursorQuery2.getCount() + " rows", inboundSmsTracker.getMessageId());
                        }
                        if (cursorQuery2.getInt(((Integer) PDU_DELETED_FLAG_PROJECTION_INDEX_MAPPING.get(10)).intValue()) == 1) {
                            logWithLocalLog("checkAndHandleDuplicate: Discarding duplicate message/segment: " + inboundSmsTracker);
                            logDupPduMismatch(cursorQuery2, inboundSmsTracker);
                            cursorQuery2.close();
                            return true;
                        }
                        if (inboundSmsTracker.getMessageCount() == 1) {
                            deleteFromRawTable((String) exactMatchDupDetectQuery.first, (String[]) exactMatchDupDetectQuery.second, 1);
                            logWithLocalLog("checkAndHandleDuplicate: Replacing duplicate message: " + inboundSmsTracker);
                            logDupPduMismatch(cursorQuery2, inboundSmsTracker);
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    cursorQuery = cursorQuery2;
                    if (cursorQuery != null) {
                        cursorQuery.close();
                    }
                    throw th;
                }
            }
            if (cursorQuery2 != null) {
                cursorQuery2.close();
            }
            if (inboundSmsTracker.getMessageCount() <= 1) {
                return false;
            }
            Pair<String, String[]> inexactMatchDupDetectQuery = inboundSmsTracker.getInexactMatchDupDetectQuery();
            try {
                cursorQuery = this.mResolver.query(uri, strArr, (String) inexactMatchDupDetectQuery.first, (String[]) inexactMatchDupDetectQuery.second, null);
                if (cursorQuery != null && cursorQuery.moveToNext()) {
                    if (cursorQuery.getCount() != 1) {
                        logeWithLocalLog("checkAndHandleDuplicate: Inexact match query returned " + cursorQuery.getCount() + " rows", inboundSmsTracker.getMessageId());
                    }
                    deleteFromRawTable((String) inexactMatchDupDetectQuery.first, (String[]) inexactMatchDupDetectQuery.second, 1);
                    logWithLocalLog("checkAndHandleDuplicate: Replacing duplicate message segment: " + inboundSmsTracker);
                    logDupPduMismatch(cursorQuery, inboundSmsTracker);
                }
            } finally {
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private void logDupPduMismatch(Cursor cursor, InboundSmsTracker inboundSmsTracker) {
        String string = cursor.getString(((Integer) PDU_DELETED_FLAG_PROJECTION_INDEX_MAPPING.get(0)).intValue());
        byte[] pdu = inboundSmsTracker.getPdu();
        byte[] bArrHexStringToByteArray = HexDump.hexStringToByteArray(string);
        if (Arrays.equals(bArrHexStringToByteArray, inboundSmsTracker.getPdu())) {
            return;
        }
        logeWithLocalLog("Warning: dup message PDU of length " + pdu.length + " is different from existing PDU of length " + bArrHexStringToByteArray.length, inboundSmsTracker.getMessageId());
    }

    protected int addTrackerToRawTable(InboundSmsTracker inboundSmsTracker, boolean z) {
        if (z) {
            try {
                if (checkAndHandleDuplicate(inboundSmsTracker)) {
                    return 5;
                }
            } catch (SQLException e) {
                loge("addTrackerToRawTable: Can't access SMS database, " + SmsController.formatCrossStackMessageId(inboundSmsTracker.getMessageId()), e);
                return 10;
            }
        } else {
            log("addTrackerToRawTable: Skipped message de-duping logic", inboundSmsTracker.getMessageId());
        }
        String address = inboundSmsTracker.getAddress();
        String string = Integer.toString(inboundSmsTracker.getReferenceNumber());
        String string2 = Integer.toString(inboundSmsTracker.getMessageCount());
        Uri uriInsert = this.mResolver.insert(sRawUri, inboundSmsTracker.getContentValues());
        log("addTrackerToRawTable: URI of new row: " + uriInsert, inboundSmsTracker.getMessageId());
        try {
            long id = ContentUris.parseId(uriInsert);
            if (inboundSmsTracker.getMessageCount() == 1) {
                inboundSmsTracker.setDeleteWhere(SELECT_BY_ID, new String[]{Long.toString(id)});
            } else {
                inboundSmsTracker.setDeleteWhere(inboundSmsTracker.getQueryForSegments(), new String[]{address, string, string2});
            }
            return 1;
        } catch (Exception e2) {
            loge("addTrackerToRawTable: error parsing URI for new row: " + uriInsert + " " + SmsController.formatCrossStackMessageId(inboundSmsTracker.getMessageId()), e2);
            return 11;
        }
    }

    public static boolean isCurrentFormat3gpp2() {
        return 2 == TelephonyManager.getDefault().getCurrentPhoneType();
    }

    public final class SmsBroadcastReceiver extends BroadcastReceiver {
        private long mBroadcastTimeMillis;
        private final String mDeleteWhere;
        private final String[] mDeleteWhereArgs;
        private final InboundSmsTracker mInboundSmsTracker;
        public volatile Intent mWaitingForIntent;

        public synchronized void setWaitingForIntent(Intent intent) {
            this.mWaitingForIntent = intent;
            this.mBroadcastTimeMillis = System.currentTimeMillis();
            InboundSmsHandler.this.removeMessages(9);
            InboundSmsHandler.this.sendMessageDelayed(9, InboundSmsHandler.sTimeoutDurationMillis);
        }

        public SmsBroadcastReceiver(InboundSmsTracker inboundSmsTracker) {
            this.mDeleteWhere = inboundSmsTracker.getDeleteWhere();
            this.mDeleteWhereArgs = inboundSmsTracker.getDeleteWhereArgs();
            this.mInboundSmsTracker = inboundSmsTracker;
        }

        public synchronized void fakeNextAction() {
            try {
                Intent intent = this.mWaitingForIntent;
                InboundSmsHandler inboundSmsHandler = InboundSmsHandler.this;
                if (intent != null) {
                    inboundSmsHandler.logeWithLocalLog("fakeNextAction: " + intent.getAction(), this.mInboundSmsTracker.getMessageId());
                    handleAction(intent, false);
                } else {
                    inboundSmsHandler.logeWithLocalLog("fakeNextAction: mWaitingForIntent is null", this.mInboundSmsTracker.getMessageId());
                }
            } catch (Throwable th) {
                throw th;
            }
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                InboundSmsHandler.this.logeWithLocalLog("onReceive: received null intent, faking " + this.mWaitingForIntent, this.mInboundSmsTracker.getMessageId());
                return;
            }
            handleAction(intent, true);
        }

        private synchronized void handleAction(Intent intent, boolean z) {
            try {
                if (intent == null) {
                    InboundSmsHandler.this.loge("handleAction: intent is null");
                    return;
                }
                String action = intent.getAction();
                if (this.mWaitingForIntent != null && this.mWaitingForIntent.getAction().equals(action)) {
                    if (z) {
                        int iCurrentTimeMillis = (int) (System.currentTimeMillis() - this.mBroadcastTimeMillis);
                        InboundSmsHandler inboundSmsHandler = InboundSmsHandler.this;
                        if (iCurrentTimeMillis >= 5000) {
                            inboundSmsHandler.loge("Slow ordered broadcast completion time for " + action + ": " + iCurrentTimeMillis + " ms");
                        } else {
                            inboundSmsHandler.log("Ordered broadcast completed for " + action + " in: " + iCurrentTimeMillis + " ms");
                        }
                    }
                    int intExtra = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
                    if (action.equals("android.provider.Telephony.SMS_DELIVER")) {
                        intent.setAction("android.provider.Telephony.SMS_RECEIVED");
                        intent.setComponent(null);
                        Bundle bundleHandleSmsWhitelisting = InboundSmsHandler.this.handleSmsWhitelisting(null, false);
                        setWaitingForIntent(intent);
                        InboundSmsHandler.this.dispatchIntent(intent, "android.permission.RECEIVE_SMS", "android:receive_sms", bundleHandleSmsWhitelisting, this, UserHandle.ALL, intExtra);
                    } else if (action.equals("android.provider.Telephony.WAP_PUSH_DELIVER")) {
                        intent.setAction("android.provider.Telephony.WAP_PUSH_RECEIVED");
                        intent.setComponent(null);
                        InboundSmsHandler inboundSmsHandler2 = InboundSmsHandler.this;
                        PowerWhitelistManager powerWhitelistManager = inboundSmsHandler2.mPowerWhitelistManager;
                        long jWhitelistAppTemporarilyForEvent = powerWhitelistManager != null ? powerWhitelistManager.whitelistAppTemporarilyForEvent(inboundSmsHandler2.mContext.getPackageName(), 2, 315, "mms-broadcast") : 0L;
                        BroadcastOptions broadcastOptionsMakeBasic = BroadcastOptions.makeBasic();
                        broadcastOptionsMakeBasic.setTemporaryAppAllowlist(jWhitelistAppTemporarilyForEvent, 0, 315, "");
                        Bundle bundle = broadcastOptionsMakeBasic.toBundle();
                        String type = intent.getType();
                        setWaitingForIntent(intent);
                        InboundSmsHandler.this.dispatchIntent(intent, WapPushOverSms.getPermissionForType(type), WapPushOverSms.getAppOpsStringPermissionForIntent(type), bundle, this, InboundSmsHandler.this.mUserManager.getMainUser(), intExtra);
                        ((IHyperTeleSmsManager) HyperTelephonyFactory.getInstance().initModule(IHyperTeleSmsManager.DEFAULT, new Object[0])).sendOmacpBroadcast(InboundSmsHandler.this.mContext, intent);
                    } else {
                        if (!"android.intent.action.DATA_SMS_RECEIVED".equals(action) && !"android.provider.Telephony.SMS_RECEIVED".equals(action) && !"android.provider.Telephony.WAP_PUSH_RECEIVED".equals(action)) {
                            InboundSmsHandler.this.loge("unexpected BroadcastReceiver action: ".concat(action));
                        }
                        if (z) {
                            int resultCode = getResultCode();
                            if (resultCode != -1 && resultCode != 1) {
                                InboundSmsHandler.this.loge("a broadcast receiver set the result code to " + resultCode + ", deleting from raw table anyway!");
                            } else {
                                InboundSmsHandler.this.log("successful broadcast, deleting from raw table.");
                            }
                        }
                        InboundSmsHandler.this.deleteFromRawTable(this.mDeleteWhere, this.mDeleteWhereArgs, 2);
                        this.mWaitingForIntent = null;
                        InboundSmsHandler.this.removeMessages(9);
                        InboundSmsHandler.this.sendMessage(3);
                    }
                    return;
                }
                InboundSmsHandler inboundSmsHandler3 = InboundSmsHandler.this;
                StringBuilder sb = new StringBuilder("handleAction: Received ");
                sb.append(action);
                sb.append(" when expecting ");
                sb.append(this.mWaitingForIntent == null ? "none" : this.mWaitingForIntent.getAction());
                inboundSmsHandler3.logeWithLocalLog(sb.toString(), this.mInboundSmsTracker.getMessageId());
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public final class CarrierServicesSmsFilterCallback implements CarrierServicesSmsFilter.CarrierServicesSmsFilterCallbackInterface {
        private final boolean mBlock;
        private final int mDestPort;
        private final boolean mIsClass0;
        private final long mMessageId;
        private final byte[][] mPdus;
        private final List mRemainingFilters;
        private final SmsBroadcastReceiver mSmsBroadcastReceiver;
        private final String mSmsFormat;
        private final int mSubId;
        private final InboundSmsTracker mTracker;
        private final boolean mUserUnlocked;

        public CarrierServicesSmsFilterCallback(byte[][] bArr, int i, InboundSmsTracker inboundSmsTracker, String str, SmsBroadcastReceiver smsBroadcastReceiver, boolean z, boolean z2, int i2, long j, boolean z3, List<SmsFilter> list) {
            this.mPdus = bArr;
            this.mDestPort = i;
            this.mTracker = inboundSmsTracker;
            this.mSmsFormat = str;
            this.mSmsBroadcastReceiver = smsBroadcastReceiver;
            this.mUserUnlocked = z;
            this.mIsClass0 = z2;
            this.mSubId = i2;
            this.mMessageId = j;
            this.mBlock = z3;
            this.mRemainingFilters = list;
        }

        @Override // com.android.internal.telephony.CarrierServicesSmsFilter.CarrierServicesSmsFilterCallbackInterface
        public void onFilterComplete(int i) {
            InboundSmsHandler.this.log("onFilterComplete: result is " + i, this.mTracker.getMessageId());
            if ((i & 1) != 0) {
                InboundSmsHandler.this.dropFilteredSms(this.mTracker, this.mSmsBroadcastReceiver, this.mBlock);
                return;
            }
            if (InboundSmsHandler.filterSms(this.mPdus, this.mDestPort, this.mTracker, this.mSmsBroadcastReceiver, this.mUserUnlocked, this.mBlock, this.mRemainingFilters)) {
                return;
            }
            boolean z = this.mBlock;
            boolean z2 = this.mUserUnlocked;
            if (z) {
                InboundSmsHandler inboundSmsHandler = InboundSmsHandler.this;
                if (z2) {
                    inboundSmsHandler.log("onFilterComplete: dropping message as the sender is blocked", this.mTracker.getMessageId());
                    InboundSmsHandler.this.dropFilteredSms(this.mTracker, this.mSmsBroadcastReceiver, this.mBlock);
                    return;
                } else {
                    inboundSmsHandler.sendMessage(3);
                    return;
                }
            }
            InboundSmsHandler inboundSmsHandler2 = InboundSmsHandler.this;
            if (z2) {
                inboundSmsHandler2.dispatchSmsDeliveryIntent(this.mPdus, this.mSmsFormat, this.mDestPort, this.mSmsBroadcastReceiver, this.mIsClass0, this.mSubId, this.mMessageId);
                return;
            }
            if (!inboundSmsHandler2.isSkipNotifyFlagSet(i)) {
                InboundSmsHandler.this.showNewMessageNotification();
            }
            InboundSmsHandler.this.sendMessage(3);
        }
    }

    private void dropSms(SmsBroadcastReceiver smsBroadcastReceiver) {
        deleteFromRawTable(smsBroadcastReceiver.mDeleteWhere, smsBroadcastReceiver.mDeleteWhereArgs, 2);
        sendMessage(3);
    }

    private boolean isMtSmsPollingMessage(SmsMessageBase smsMessageBase) {
        if (!this.mContext.getResources().getBoolean(R.bool.config_ignoreVibrationsOnWirelessCharger)) {
            return false;
        }
        String string = this.mContext.getResources().getString(R.string.db_wal_sync_mode);
        return !TextUtils.isEmpty(string) && string.equals(smsMessageBase.getMessageBody());
    }

    public static int getPduLength(SmsMessageBase smsMessageBase) {
        byte[] pdu = smsMessageBase != null ? smsMessageBase.getPdu() : null;
        if (pdu != null) {
            return pdu.length;
        }
        return 0;
    }

    private int getTotalPduLength(byte[][] bArr) {
        if (bArr == null) {
            return 0;
        }
        int length = 0;
        for (byte[] bArr2 : bArr) {
            if (bArr2 != null) {
                length += bArr2.length;
            }
        }
        return length;
    }

    protected void logWithLocalLog(String str) {
        log(str);
        this.mLocalLog.log(str);
    }

    protected void logWithLocalLog(String str, long j) {
        log(str, j);
        this.mLocalLog.log(str + ", " + SmsController.formatCrossStackMessageId(j));
    }

    protected void logeWithLocalLog(String str) {
        loge(str);
        this.mLocalLog.log(str);
    }

    protected void logeWithLocalLog(String str, long j) {
        loge(str, j);
        this.mLocalLog.log(str + ", " + SmsController.formatCrossStackMessageId(j));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.internal.telephony.StateMachine
    public void log(String str) {
        Rlog.d(getName(), str);
    }

    protected void log(String str, long j) {
        log(str + ", " + SmsController.formatCrossStackMessageId(j));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.internal.telephony.StateMachine
    public void loge(String str) {
        Rlog.e(getName(), str);
    }

    protected void loge(String str, long j) {
        loge(str + ", " + SmsController.formatCrossStackMessageId(j));
    }

    @Override // com.android.internal.telephony.StateMachine
    protected void loge(String str, Throwable th) {
        Rlog.e(getName(), str, th);
    }

    @Override // com.android.internal.telephony.StateMachine
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println(getName() + " extends StateMachine:");
        indentingPrintWriter.increaseIndent();
        super.dump(fileDescriptor, indentingPrintWriter, strArr);
        CellBroadcastServiceManager cellBroadcastServiceManager = this.mCellBroadcastServiceManager;
        if (cellBroadcastServiceManager != null) {
            cellBroadcastServiceManager.dump(fileDescriptor, indentingPrintWriter, strArr);
        }
        indentingPrintWriter.println("mLocalLog:");
        indentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("mCarrierServiceLocalLog:");
        indentingPrintWriter.increaseIndent();
        this.mCarrierServiceLocalLog.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.decreaseIndent();
    }

    public PowerManager.WakeLock getWakeLock() {
        return this.mWakeLock;
    }

    public int getWakeLockTimeout() {
        return this.mWakeLockTimeout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setWakeLockTimeout(int i) {
        this.mWakeLockTimeout = i;
    }

    public void setSmsFiltersForTesting(List<SmsFilter> list) {
        if (list == null) {
            this.mSmsFilters = createDefaultSmsFilters();
        } else {
            this.mSmsFilters = list;
        }
    }

    private static class NewMessageNotificationActionReceiver extends BroadcastReceiver {
        private NewMessageNotificationActionReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (InboundSmsHandler.ACTION_OPEN_SMS_APP.equals(intent.getAction())) {
                UserManager userManager = (UserManager) context.getSystemService("user");
                context.getPackageManager();
                UserHandle userHandle = UserHandle.CURRENT;
                PackageManager packageManager = context.createContextAsUser(userHandle, 0).getPackageManager();
                if (userManager.isUserUnlocked()) {
                    String defaultSmsPackage = Telephony.Sms.getDefaultSmsPackage(context);
                    Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(defaultSmsPackage);
                    if (launchIntentForPackage != null) {
                        context.startActivityAsUser(launchIntentForPackage, userHandle);
                        return;
                    }
                    Rlog.e("NewMsgNotifActionReceiver", "smsIntent is null,  default sms package is " + defaultSmsPackage);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] decodeHexString(String str) {
        if (str == null || str.length() % 2 == 1) {
            return null;
        }
        byte[] bArr = new byte[str.length() / 2];
        int i = 0;
        while (i < str.length()) {
            int i2 = i + 2;
            bArr[i / 2] = hexToByte(str.substring(i, i2));
            i = i2;
        }
        return bArr;
    }

    private byte hexToByte(String str) {
        int digit = toDigit(str.charAt(0));
        return (byte) ((digit << 4) + toDigit(str.charAt(1)));
    }

    private int toDigit(char c) {
        int iDigit = Character.digit(c, 16);
        if (iDigit == -1) {
            return 0;
        }
        return iDigit;
    }

    static void registerNewMessageNotificationActionHandler(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_OPEN_SMS_APP);
        context.registerReceiver(new NewMessageNotificationActionReceiver(), intentFilter, 4);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract class CbTestBroadcastReceiver extends BroadcastReceiver {
        protected final String mTestAction;

        protected abstract void handleTestAction(Intent intent);

        public CbTestBroadcastReceiver(String str) {
            this.mTestAction = str;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            InboundSmsHandler.this.logd("Received test intent action=" + intent.getAction());
            if (intent.getAction().equals(this.mTestAction)) {
                int phoneId = InboundSmsHandler.this.mPhone.getPhoneId();
                if (intent.getIntExtra(SatelliteNetworkFactory.KEY_SATELLITE_STATE_CHANGE_PHONE_ID, phoneId) != phoneId) {
                    return;
                }
                handleTestAction(intent);
            }
        }
    }

    public BroadcastReceiver makeNewMessageNotificationActionReceiver() {
        return new NewMessageNotificationActionReceiver();
    }

    protected SmsMessage onCreateSmsMessage(byte[] bArr, String str) {
        return SmsMessage.createFromPdu(bArr, "3gpp");
    }

    protected int onDispatchWapPdu(byte[][] bArr, byte[] bArr2, SmsBroadcastReceiver smsBroadcastReceiver, String str, int i, long j) {
        return this.mWapPush.dispatchWapPdu(bArr2, smsBroadcastReceiver, this, str, i, j);
    }
}
